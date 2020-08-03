#include <ArduinoJson.h> // JSON 통신을 위한 헤더 포함

int trig1 = 2;
int echo1 =  3;

int trig2 = 4;
int echo2 = 5;

int trig3 = 8;
int echo3 =  9;

long duration1, distance1, duration2, distance2, duration3, distance3;

int number_of_drink[3][5] = {{0,1, 2, 3, 4}, {0,5, 6, 7, 8},{0,9, 10, 11, 12}};//0은 손감지 x 상태
int chosen_number[3] = {0}; //사용자가 선택한 음료 번호 저장할 곳
int count[3][5] = {0};//초음파 센서에 감지되는걸 카운트해 기록하는 곳.
int i = 0; // 정확도를 위해 돌려보는 횟수 (총 10회 정도 돌려본 뒤 선택한 버튼 확정).

void setup() {
  //시리얼 통신을 설정(전송속도 9600bps)
  Serial.begin(9600);
  while (!Serial) continue;

  //초음파 송신부-> OUTPUT, 초음파 수신부 -> INPUT
  pinMode(trig1, OUTPUT);pinMode(echo1, INPUT);
  pinMode(trig2, OUTPUT);pinMode(echo2, INPUT);
  pinMode(trig3, OUTPUT);pinMode(echo3, INPUT);

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

  digitalWrite(trig3, LOW);
  delayMicroseconds(2);
  digitalWrite(trig3, HIGH);
  delayMicroseconds(5);
  digitalWrite(trig3, LOW);
  duration3 = pulseIn(echo3, HIGH);

  //초음파는 29마이크로초 당 1센치를 이동
  //초음파의 이동 거리 = duration(왕복에 걸린시간) / 29 / 2
  distance1 = (duration1 / 2) / 29;
  distance2 = (duration2 / 2) / 29;
  distance3 = (duration3 / 2) / 29;


  accuracy(distance1, 0);
  accuracy(distance2, 1);
  accuracy(distance3, 2);


  ultra_Json(distance1, distance2, distance3);
  Serial.println("");

  for (int a = 0; a < 3; a++) {
    for (int b = 0; b < 5; b++) {
      Serial.print(count[a][b]); //위치별로 손이 센서에 감지된 횟수를 보기 위한 for문 
    }
    Serial.println("");
  }
  Serial.print("i=");
  Serial.println(i);
  
  i += 1;
  if ( i == 10) { //0~9까지 총 10회
    i = 0;
    choose_drink(5);//q를 입력하는데 이때 q는 열의 수
    for (int a = 0; a < 3; a++) {
    for (int b = 0; b < 5; b++) {
      count[a][b]=0;//다음 음료 선택을 위해 초기화.
      }
    }
    for (int c = 0; c<3; c++){
      Serial.print(chosen_number[c]); //어떤 음료가 선택 되었는지 확인 
    }
    Serial.println("");

    multi_chosen_check(3); //3 : chosen number의 크기, 음료가 복수선택이 되었는지 보여주는 함수(오류체크)

    chosen_drink_Json(chosen_number[0],chosen_number[1],chosen_number[2]);
    Serial.println("");
  }
  for (int c = 0; c<3; c++){
    chosen_number[c] = 0; //다음 음료 선택을 위해 초기화.
  }

  delay(1000);
}
