/* 
 *  hango vending!
 */

// 최개 값 상수 정의
#define MAX_LINE 2        // 자판기 전체 라인 수
#define MAX_POSITION 10   // 자판기 전체 칸 수
#define PIN_COUNT 4       // 핀 개수

// 입출력 핀 정의
const int trigPin[PIN_COUNT] = {2, 7, 8, 12}; //손의 위치 센싱
const int echoPin[PIN_COUNT] = {3, 6, 9, 11}; //음료수 개수

// 지속시간 및 거리 선언
long duration[PIN_COUNT], distance[PIN_COUNT];

String drinks_numbers[MAX_LINE][(MAX_POSITION/2) + 1]; //0은 손감지 x 상태, 음료수 판매감지 x 상태
String sensed_position[2] = {"non", "non"};           //사용자가 선택한 음료 번호 저장할 곳
String sold_position = "non";                         //선택되어 판매되는 음료수
String final_sensed_position = "non";                 //센싱된 손의 영역 확정
String state = "zero";

int sensed_cnt[4][6] = {0, };                         //초음파 센서에 감지되는걸 카운트해 기록하는 곳.
int i = 0;                                            // 정확도를 위해 돌려보는 횟수 (총 10회 정도 돌려본 뒤 선택한 버튼 확정).


// 초기 변수 설정 함수
void setInitVariable() {
  /*  
    drink_number 설정
    MAX_LINE, MAX_POSITION에 따라 자판기 정보 셋업
  */
  int cnt = 1;
  for (int i = 0; i < MAX_LINE; i++) {
    for (int j = 0; j < (MAX_POSITION/2) + 1; j++) {
      if (j == 0) {
        drinks_numbers[i][j] = "non";
      } else {
        drinks_numbers[i][j] = String(cnt) + "st";
        cnt++;
      }
    }
  }
}

// 손의 위치를 카운트 하는 함수
void accuracy(float distance, int i) { 
  if (0 <= distance && distance < 53) {
    if (0 <= distance && distance < 12) sensed_cnt[i][5]++; //제일 오른쪽 음료수
    else if (12 <= distance && distance < 22) sensed_cnt[i][4]++;
    else if (22 <= distance && distance < 34) sensed_cnt[i][3]++;
    else if (34 <= distance && distance < 45) sensed_cnt[i][2]++; //제일 왼쪽 음료수
    else if (45 <= distance && distance < 53) sensed_cnt[i][1]++;
  } else sensed_cnt[i][0]++; //손이 감지되지 않음
}

void sensing_drink(int p){
  for(int i = 1; i < p; i++){
    if(sensed_cnt[2][i] != 0) sold_position = drinks_numbers[0][i];
    else if(sensed_cnt[2][i] == 1){
      if(sensed_cnt[3][i]!=0) sold_position = drinks_numbers[1][i];
    }
  }
}

// 어떤곳이 제일 많이 센싱되었는지 나타내는 함수
void choose_drink(int q) { 
  for (int j = 0; j < q; j++) {
    if (sensed_cnt[0][j] > sensed_cnt[0][0]) sensed_position[0] = drinks_numbers[0][j];
    if (sensed_cnt[1][j] > sensed_cnt[1][0]) sensed_position[1] = drinks_numbers[1][j];
  }
}

// 음료수가 복수 체크되었는지 오류체킹
void multi_chosen_check(int p) { /
  int not_0 = 0;
  for (int i = 0; i < p; i++) {
    if (sensed_position[i] != "non") not_0 += 1;
  }

  if (not_0 == 0) {
    state = "zero";
  } else if (not_0 == 1) {
    state = "one";
    if (sensed_position[0] != "non") final_sensed_position = sensed_position[0];
    if (sensed_position[1] != "non") final_sensed_position = sensed_position[1];
  } else state = "multiple";
}


void setup() {
  // 초기 변수 설정
  setInitVariable();

  //시리얼 통신을 설정(전송속도 9600bps)
  Serial.begin(9600);

  // 입출력 핀 설정
  for (int i = 0; i < PIN_COUNT; i++) {
    pinMode(trigPin[i], OUTPUT);
    pinMode(echoPin[i], INPUT);
  }

}

void loop() {

  for (int i = 0; i < PIN_COUNT; i++) {
    digitalWrite(trigPin[i], LOW);
    delayMicroseconds(2);
    digitalWrite(trigPin[i], HIGH);
    delayMicroseconds(5);
    digitalWrite(trigPin[i], LOW);
    duration[i] = pulseIn(echoPin[i], HIGH);

    //초음파는 29마이크로초 당 1센치를 이동
    //초음파의 이동 거리 = duration(왕복에 걸린시간) / 29 / 2
    distance[i] = (duration[i] / 2) / 29.1;

    //여기서 손 센싱 회수를 더함.
    accuracy(distance[i], 0); 
  }

  i += 1;

  sensing_drink(6); //chosen sale에 값을 넣음. 음료 판매 측정
  if (final_sensed_position != "non"){
      Serial.print("Sold_drink:");
      Serial.println(sold_position);
  }
  else {
    if ( i == 9) {
      choose_drink(6);//q를 입력하는데 이때 q는 열의 수, "sensed_position"에 값이 들어간다.
      for (int a = 0; a < 4; a++) {
        for (int b = 0; b < 6; b++) {
          sensed_cnt[a][b] = 0; //다음 음료 선택을 위해 초기화.
        }
      }

      multi_chosen_check(2); //2 : chosen number의 크기, 음료가 복수선택이 되었는지 보여주는 함수(오류체크)
    
      Serial.print("Final_hand: ");
      Serial.print(final_sensed_position);
      Serial.println(" 1");
      Serial.print("Sold_drink: ");
      Serial.print(sold_position);
      Serial.println(" 2");
      
      Serial.print(state);
      Serial.println(" 3");
      

      i=0;
    }
  }
  for (int c = 0; c < 2; c++) {
    sensed_position[c] = "non"; //다음 음료 선택을 위해 초기화.
  }
  final_sensed_position = "non";
  sold_position = "non";
  state = "zero";
  delay(250);
}
