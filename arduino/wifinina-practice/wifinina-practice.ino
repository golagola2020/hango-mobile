// WiFi 통신을 위한 헤더 포함
#include <SPI.h>
#include <WiFiNINA.h>
#include "arduino-secrets.h" // 정보관리와 보안유지를 위해 따로 관리함.

#include <ArduinoJson.h> // JSON 통신을 위한 헤더 포함

// 네트워크 통신에 사용될 기본 변수 선언
char ssid[] = SECRET_SSID; // 네트워크에 연결할 IP 도메인 (Service Set ID)
char pass[] = SECRET_PASS; // 네트워크에 연결할 IP 패스워드

// JSON 객체 선언 (동적 메모리 할당, 정적 메모리 할당하고자 할 경우 Dynamic => Static 변경)
DynamicJsonDocument json(200);
String lastConnectionTime;
boolean isConnected;

/*
  @ [WiFi 상태 상수]
  @ WL_CONNECTED : WiFi 네트워크에 연결될 때 할당됩니다 .
  @ WL_NO_SHIELD : WiFi 쉴드가 없을 때 할당됩니다 .
  @ WL_IDLE_STATUS : WiFi .begin () 이 호출 될 때 할당 된 임시 상태 이며 시도 횟수가 만료되거나 (WL_CONNECT_FAILED에 발생) 연결이 설정 될 때까지 (WL_CONNECTED에 발생) 활성 상태를 유지합니다.
  @ WL_NO_SSID_AVAIL : 사용 가능한 SSID가 없을 때 할당됩니다.
  @ WL_SCAN_COMPLETED : 스캔 네트워크가 완료 될 때 할당됩니다.
  @ WL_CONNECT_FAILED : 모든 시도에서 연결이 실패 할 때 할당됩니다.
  @ WL_CONNECTION_LOST : 연결이 끊어졌을 때 할당됩니다.
  @ WL_DISCONNECTED : 네트워크 연결이 끊어졌을 때 할당됩니다. 
*/

// WiFi 상태
int status = WL_IDLE_STATUS;

// 클라이언트 인스턴스 생성
WiFiClient client;

/* ************************************************************************************ */

void setup() {
  
  // 시리얼 초기화
  Serial.begin(9600);
  
  while(!Serial) {
    // 시리얼 포트와 연결될 때까지 기다린다.
    // 기본 USB 포트에만 필요.
  }

  // WiFi 설정
  setWifi();

  //  WiFi 상태 출력
  Serial.println("WiFi에 연결되었습니다.");
  printWifiStatus();

  // 서버에게 JSON 데이터를 요청
  requestJsonToServer();
}

/* ************************************************************************************ */

void loop() {

  // 서버 데이터 처리
  String serverData = receiveServerData(); // 서버 데이터가 저장될 변수 선언
  processData(serverData);                 // 서버 데이터 처리

  // 서버 연결이 끊어지면 클라이언트 중지
  if (!client.connected()) {
    Serial.println();
    Serial.println("서버와의 연결이 끊어졌습니다...");
    client.stop();

    // 더 이상 아무것도 하지 않는다.
    while (true);
  }
}

/* ************************************************************************************ */

/*
 @ requestJsonFromServer : 서버에게 JSON 데이터를 요청하는 함수
 @ 파라미터 없음
*/
void requestJsonToServer() {
  
  Serial.println("\n서버와 연결 중...");  

  String jsonData = "";
  json["managerID"] = "parkwoorim";
  json["serialNumber"] = "00000001";
  serializeJson(json, jsonData);
  Serial.println(jsonData);
  Serial.println(jsonData.length());

  // 연결이 되면 serial을 통해 결과 출력
  if (client.connect(SERVER_IP, PORT)) {
    Serial.println("서버에 연결되었습니다.");
    
    // HTTP 요청
    client.println("POST /arduino/" + jsonData + "/ HTTP/1.1");         // 요청 메소드와 경로, 통신 프로토콜 정의한다.
    client.println("Cache-Control: no-cache");
    client.print("Host: ");
    client.println(SERVER_HOST);                      // 호스트로 서버의 호스트를 정의한다.
    client.println("User-Agent: ArduinoWiFi/1.1");    // 요청한 에이전트
    client.print("Content-Type: application/json");   // 데이터 전송 유형 (JSON 형식)
    client.print("Content-Length: ");
    client.println(jsonData.length());
    client.println("Connection: keep-alive");          
    client.println();
    client.println(jsonData);

    // 마지막에 println을 함으로써 서버에게 데이터를 요청한다.
    client.println();
  }
}

/*
 @ receiveData : 서버가 전달한 데이터를 반환해주는 함수
 @ 파라미터 없음

 @ 반환 타입 : String
*/
String receiveServerData() {

  // 서버 데이터를 저장할 변수 선언
  String serverData = "";
  
  // 서버에서 수신 가능한 바이트가 있으면 실행
  while (client.available()) {
    // JSON 데이터 수신
    char data = client.read();  // 데이터 수신 (1바이트 단위로 수신)
    Serial.print(data);
    serverData += data;        // 1바이트 단위의 수신 데이터를 사용가능한 형식으로 바꾸기 위해 문자열 연산  

    // 수신 데이터 중 헤더 부분을 제거하기 위함 => 수신하고자 하는 데이터는 JSON 객체이기 때문
    if (data == '\n') {
      serverData = "";         // 수신된 헤더 데이터를 초기화 해준다.
    }
  }

  return serverData;
}

/*
 @ receiveData : 서버가 전달한 데이터를 반환해주는 함수
 @ 파라미터
 @ serverData : 문자열 : 서버가 전달한 데이터
*/
void processData(String serverData) {

  // 서버에서 수신한 데이터를 전부 받았으면 실행
  if (serverData != NULL) {
    // 서버 데이터(JSON)를 아두이노에서 원하는 형태로 파싱하여 받아온다.
    json = getParsedJSON(serverData);

    // 수신 데이터를 파싱하여 각 변수에 저장
    const char* sensor = json["sensor"];
    double distance = json["distance"];
    double latitude = json["data"][0];

    // 출력
    Serial.println();
    Serial.println(sensor);
    Serial.println(distance, 1);
    Serial.println(latitude, 5);
  }
}

/*
 @ getParsedJSON : JSON 객체 반환 함수
 @ 파라미터
 @ serverData : 문자열 : JSON 객체로 변환할 데이터

 @ 반환 타입 : JSON
*/
DynamicJsonDocument getParsedJSON(String serverData) {
  
  // 수신 데이터(JSON)를 아두이노에서 사용할 수 있는 형태로 변환
  DeserializationError error = deserializeJson(json, serverData);
  
  // JSON 변환 에러시 실행(EX : receiveData가 JSON 객체가 아닐 경우)
  if (error) {
    Serial.print(("deserializeJson() 실패: "));
    Serial.println(error.c_str());
  }

  // JSON 객체 반환
  return json;
}

/*
 @ setWiFi : WiFi 초기 설정 함수
 @ 파라미터 없음
*/
void setWifi() {
  
  // WiFi 모듈 확인
  if (WiFi.status() == WL_NO_MODULE) {
    Serial.println("WiFi 모듈과의 통신 실패!");
    
    // 계속하지 않는다.
    while(true);
  }

  // 모듈 펌웨어 버젼 확인
  String fv = WiFi.firmwareVersion();
  if (fv < WIFI_FIRMWARE_LATEST_VERSION) {
    Serial.println("펌웨어를 업그레이드하십시오.");
  }

  // WiFi 네트워크 연결 시도
  while(status != WL_CONNECTED) {
    Serial.print("SSID 연결 시도 중 : ");
    Serial.println(ssid);

    // WPA/WPA2 네트워크에 연결. 개방형 또는 WEP 네트워크를 사용하는 경우 아래 라인 변경.
    status = WiFi.begin(ssid, pass);

    // 연결을 5초 동안 대기
    delay(5000);
  }
}

/*
 @ printWifiStatus : WiFi 상태 출력 함수
 @ 파라미터 없음
*/
void printWifiStatus() {
  
  // 연결된 네트워크의 SSID 출력
  Serial.print("SSID : ");
  Serial.println(WiFi.SSID());

  // 보드의 IP 주소 출력
  IPAddress ip = WiFi.localIP();
  Serial.print("아두이노 IP : ");
  Serial.println(ip);

  // 수신 신호 강도 출력
  long rssi = WiFi.RSSI();
  Serial.print("신호 강도 (RSSI) : ");
  Serial.print(rssi);
  Serial.println(" dBm");
}
