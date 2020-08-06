#include <ArduinoJson.h> // JSON 통신을 위한 헤더 포함

int trig1 = 2;
int echo1 =  3;

int trig2 = 7;
int echo2 = 6;

long duration1, distance1, duration2, distance2;

String number_of_drink[2][6] = {{"non","1st", "2st", "3st", "4st", "5st"},{"non", "6st", "7st", "8st", "9st", "10st"}};//0은 손감지 x 상태
String chosen_number[2] = {"",""}; //사용자가 선택한 음료 번호 저장할 곳
int count[2][6] = {0};//초음파 센서에 감지되는걸 카운트해 기록하는 곳.
int i = 0; // 정확도를 위해 돌려보는 횟수 (총 10회 정도 돌려본 뒤 선택한 버튼 확정).
String final_check = "";


void setup() {
  //시리얼 통신을 설정(전송속도 9600bps)
  Serial.begin(9600);
  while (!Serial) continue;

  //초음파 송신부-> OUTPUT, 초음파 수신부 -> INPUT
  pinMode(trig1, OUTPUT);pinMode(echo1, INPUT);
  pinMode(trig2, OUTPUT);pinMode(echo2, INPUT);

}

void loop() {
  
  digitalWrite(trig1, LOW);
  delayMicroseconds(2);
  digitalWrite(trig1, HIGH);
  delayMicroseconds(5);
  digitalWrite(trig1, LOW);
  duration1 = pulseIn(echo1, HIGH);


  digitalWrite(trig2, LOW);
  delayMicroseconds(2);
  digitalWrite(trig2, HIGH);
  delayMicroseconds(5);
  digitalWrite(trig2, LOW);
  duration2 = pulseIn(echo2, HIGH);

  //초음파는 29마이크로초 당 1센치를 이동
  //초음파의 이동 거리 = duration(왕복에 걸린시간) / 29 / 2
  distance1 = (duration1 / 2) / 29;
  distance2 = (duration2 / 2) / 29;

  accuracy(distance1, 0); //여기서 손 센싱 회수를 더함.
  accuracy(distance2, 1);

  ultra_Json(distance1, distance2);
  Serial.println("");

   for (int a = 0; a < 2; a++) {
    for (int b = 0; b < 6; b++) {
      Serial.print(count[a][b]); //위치별로 손이 센서에 감지된 횟수를 보기 위한 for문 
    }
    Serial.println("");
  }
  Serial.print("i=");
  Serial.println(i);
  
  i += 1;

  if ( i == 9) {
    choose_drink(6);//q를 입력하는데 이때 q는 열의 수
    for (int a = 0; a < 2; a++) {
      for (int b = 0; b < 6; b++) {
        count[a][b]=0;//다음 음료 선택을 위해 초기화.
        }
    }
    for (int c = 0; c<2; c++){
      Serial.print("chosen_number:");
      Serial.print(chosen_number[c]); //어떤 음료가 선택 되었는지 확인 
      Serial.println("");
    }

    multi_chosen_check(2); //2 : chosen number의 크기, 음료가 복수선택이 되었는지 보여주는 함수(오류체크)

    chosen_drink_Json(chosen_number[0],chosen_number[1], final_check);
    Serial.println("");

    i = 0;
  }
  for (int c = 0; c<2; c++){
    chosen_number[c] = "non"; //다음 음료 선택을 위해 초기화.
  }
  final_check = "non";  
  delay(1000);
}
