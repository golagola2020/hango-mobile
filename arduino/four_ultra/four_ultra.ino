#include <ArduinoJson.h> // JSON 통신을 위한 헤더 포함

const int trigPin1 = 2; //손의 위치 센싱 
const int echoPin1 =  3;

const int trigPin2= 7;
const int echoPin2 = 6;

const int trigPin3 = 8; //음료수 개수
const int echoPin3 =  9;

const int trigPin4 = 12;
const int echoPin4 =  11;

long duration1, distance1, duration2, distance2, duration3, distance3, duration4, distance4;

String number_of_drink[2][6] = {{"non","1st", "2st", "3st", "4st", "5st"},{"non", "6st", "7st", "8st", "9st", "10st"}};//0은 손감지 x 상태, 음료수 판매감지 x 상태
String chosen_number[2] = {"",""}; //사용자가 선택한 음료 번호 저장할 곳
String chosen_sale = {"non"}; //선택되어 판매되는 음료수
int count[4][6] = {0};//초음파 센서에 감지되는걸 카운트해 기록하는 곳.
int i = 0; // 정확도를 위해 돌려보는 횟수 (총 10회 정도 돌려본 뒤 선택한 버튼 확정).
String final_check = "";

void setup (){
  //시리얼 통신을 설정(전송속도 9600bps)
  Serial.begin(9600);
  
  pinMode(trigPin1,OUTPUT);
  pinMode(echoPin1,INPUT);
  pinMode(trigPin2,OUTPUT);
  pinMode(echoPin2,INPUT);
  pinMode(trigPin3,OUTPUT);
  pinMode(echoPin3,INPUT);
  pinMode(trigPin4,OUTPUT);
  pinMode(echoPin4,INPUT);
}



void loop(){

digitalWrite(trigPin1,LOW);
delayMicroseconds(2);
digitalWrite(trigPin1,HIGH);
delayMicroseconds(5);
digitalWrite(trigPin1,LOW);
duration1= pulseIn(echoPin1, HIGH);

digitalWrite(trigPin2,LOW);
delayMicroseconds(2);
digitalWrite(trigPin2,HIGH);
delayMicroseconds(5);
digitalWrite(trigPin2,LOW);
duration2= pulseIn(echoPin2, HIGH);

digitalWrite(trigPin3,LOW);
delayMicroseconds(2);
digitalWrite(trigPin3,HIGH);
delayMicroseconds(5);
digitalWrite(trigPin3,LOW);
duration3= pulseIn(echoPin3, HIGH);

digitalWrite(trigPin4,LOW);
delayMicroseconds(2);
digitalWrite(trigPin4,HIGH);
delayMicroseconds(5);
digitalWrite(trigPin4,LOW);
duration4= pulseIn(echoPin4, HIGH);

//초음파는 29마이크로초 당 1센치를 이동
  //초음파의 이동 거리 = duration(왕복에 걸린시간) / 29 / 2
distance1 = (duration1/2)/29.1;
distance2 = (duration2/2)/29.1;
distance3 = (duration3/2)/29.1;
distance4 = (duration4/2)/29.1;

accuracy(distance1, 0); //여기서 손 센싱 회수를 더함.
  accuracy(distance2, 1);
  accuracy(distance3, 2); //음료수 판매 센싱
  accuracy(distance4, 3);

ultra_Json(distance1, distance2, distance3, distance4);
  Serial.println("");

   for (int a = 0; a < 4; a++) {
    for (int b = 0; b < 6; b++) {
      Serial.print(count[a][b]); //위치별로 손과 음료수가 센서에 감지된 횟수를 보기 위한 for문 
    }
    Serial.println("");
  }
  Serial.print("i=");
  Serial.println(i);
  
  i += 1;

  sensing_drink(6);
  

  if ( i == 9) {
    choose_drink(6);//q를 입력하는데 이때 q는 열의 수, chosen_number에 값이 들어간다.
    for (int a = 0; a < 4; a++) {
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
    
    chosen_drink_Json(chosen_number[0],chosen_number[1], final_check, chosen_sale);
    Serial.println("");

    i = 0;
  }
  for (int c = 0; c<2; c++){
    chosen_number[c] = "non"; //다음 음료 선택을 위해 초기화.
  }
  final_check = "non";  
  delay(1000);


}
