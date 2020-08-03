#include <WiFiNINA.h>
#include <ArduinoHttpClient.h>      // HTTP 통신을 위한 헤더 포
#include <ArduinoJson.h>            // JSON 통신을 위한 헤더 포함
#include "arduino-secrets.h"        // 정보관리와 보안유지를 위해 따로 관리함

// WiFi 연결을 위한 IP, PW 정의
char ssid[] = SECRET_SSID;
char pass[] = SECRET_PASS;

// http 통신을 위한 인스턴스 생성
WiFiClient wifi;
HttpClient client = HttpClient(wifi, SERVER_IP, PORT);

// JSON 객체 선언 (동적 메모리 할당, 정적 메모리 할당하고자 할 경우 Dynamic => Static 변경)
DynamicJsonDocument json(200);
String jsonData = "";

/*
  @ WiFi 상태 값
  @ WL_CONNECTED : WiFi 네트워크에 연결될 때 할당됩니다 .
  @ WL_NO_SHIELD : WiFi 쉴드가 없을 때 할당됩니다 .
  @ WL_IDLE_STATUS : WiFi .begin () 이 호출 될 때 할당 된 임시 상태 이며 시도 횟수가 만료되거나 (WL_CONNECT_FAILED에 발생) 연결이 설정 될 때까지 (WL_CONNECTED에 발생) 활성 상태를 유지합니다.
  @ WL_NO_SSID_AVAIL : 사용 가능한 SSID가 없을 때 할당됩니다.
  @ WL_SCAN_COMPLETED : 스캔 네트워크가 완료 될 때 할당됩니다.
  @ WL_CONNECT_FAILED : 모든 시도에서 연결이 실패 할 때 할당됩니다.
  @ WL_CONNECTION_LOST : 연결이 끊어졌을 때 할당됩니다.
  @ WL_DISCONNECTED : 네트워크 연결이 끊어졌을 때 할당됩니다. 
*/
int status = WL_IDLE_STATUS;

/* ************************************************************************************ */

void setup() {

  // 시리얼 통신 보드레이트 설정
  Serial.begin(9600);
  while(!Serial);

  // WiFi 연결 후 상태 출력
  connectWifi();
  printWifiStatus();
}

/* ************************************************************************************ */

void loop() {
  requestHttp();
}

/* ************************************************************************************ */

/*
 @ requestHTTP : 서버에게 HTTP 요청(전송) 함수
 @ 파라미터 없음
*/
void requestHttp() {

  // http 통신 경로 설정
  String path = "/arduino";                     // 요청 url
  String contentType = "application/json";      // 요청 유형

  // 서버의 BODY로 전송할 데이터 정의
  json["userId"] = "woorim960";
  json["serialNumber"] = "00000001";
  serializeJson(json, jsonData);

  // 데이터 전송
  Serial.println("POST 메소드로 전송합니다.");
  client.post(path, contentType, jsonData);

  // 서버의 응답 데이터
  int statusCode = client.responseStatusCode();     // HTTP 상태 코드
  String serverResponse = client.responseBody();          // 응답 데이터

  // 요청 및 응답 출력
  Serial.print("요청 데이터 : ");
  Serial.println(jsonData);
  Serial.print("HTTP 상태 코드 : ");
  Serial.println(statusCode);
  Serial.print("응답 데이터 : ");
  Serial.println(serverResponse);

  Serial.println("5초 뒤 재전송");
  Serial.println("====================");
  delay(5000);
}

/*
 @ connectWifi : WiFi 연결 함수
 @ 파라미터 없음
*/
void connectWifi() {

  // 와이파이에 연결되지 않았다면 실행
  while ( status != WL_CONNECTED) {
    Serial.print("네트워크에 연결 중 SSID : ");
    Serial.println(ssid);

    // 네트워크 연결 시
    status = WiFi.begin(ssid, pass);

    // 연결 5초 대기
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
