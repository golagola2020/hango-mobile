/* 
 *  hango vending!
 */

// 최대값 [상수] 정의
#define MAX_LINE 2        // 자판기 전체 라인 수
#define MAX_POSITION 8   // 자판기 전체 칸 수
#define PIN_COUNT 4       // 핀 개수
#define ROOP_CNT 5        // i의 최대값을 결정. (정확도를 위해 손 측정시 몇번의 루프를 돌것인지 결정)


// 입출력 핀 정의, 초음파센서가 추가된다면 직접 수정해야 함.
const int trigPin[PIN_COUNT] = {2, 7, 8, 12}; //손의 위치 센싱
const int echoPin[PIN_COUNT] = {3, 6, 9, 11}; //음료수 개수

// 지속시간 및 거리 선언
long duration[PIN_COUNT], distance[PIN_COUNT];

int drinks_numbers[MAX_LINE][(MAX_POSITION/MAX_LINE) + 1];    //0은 손감지 x 상태, 음료수 판매감지 x 상태
int sensed_position[MAX_LINE];   //사용자가 선택한 음료 번호 저장할 곳
int sold_position = -1;                             //선택되어 판매되는 음료수
int final_sensed_position = -1;                     //센싱된 손의 영역 확정
int state = 0;

int sensed_cnt[PIN_COUNT][(MAX_POSITION/MAX_LINE) + 1] = {0, };   //초음파 센서에 감지되는걸 카운트해 기록하는 곳.
int i = 0;  // 정확도를 위해 돌려보는 횟수

// 초기 변수 설정 함수
void setInitVariable() {
  /*  
    drink_number 설정
    MAX_LINE, MAX_POSITION에 따라 자판기 정보 셋업
  */
  int cnt = 1;
  for (int i = 0; i < MAX_LINE; i++) {
    for (int j = 0; j < (MAX_POSITION/MAX_LINE) + 1; j++) {
      if (j == 0) {
        drinks_numbers[i][j] = -1;     
      } else {
        drinks_numbers[i][j] = cnt;
        cnt++;
      }
    }  
  }
}




void hand_count(float distance, int i, int cell) {
  /* 
 *  손의 위치를 카운트 하는 함수 
 *  distance : 초음파센서와 인식된 손 사이의 거리
 *  i : 몇번째 라인인지 결정.
 *  cell : 한 라인에 음료수가 몇개 들어가는지. (칸)
 */
 if (10 <= distance && distance < (cell*10)+10)
    for (int k = 0; k < cell ; k++ ){
       if ( ((cell*10)+10 - 10*(k+1)) <= distance && distance < ((cell*10)+10 - 10*k ) ) sensed_cnt[i][k]++; 
    }
  if (distance >= (cell*10)+10)
      sensed_cnt[i][0]++;
}   


void sensing_drink(int drink){//6   maxline 2 ~ pin num 4     3~4
  for(int j = PIN_COUNT-1; j>= MAX_LINE; j--){
    for(int i = 1; i < drink; i++){
      if(sensed_cnt[j][i] != 0) sold_position = drinks_numbers[j-MAX_LINE][i];
    }
  }
}

// 어떤곳에서 손이 제일 많이 센싱되었는지 나타내는 함수
void most_hand_detect(int drink) { 
  for (int j = 0; j < drink; j++) {
    for(int i = 0; i < MAX_LINE; i++)
      if (sensed_cnt[i][j] > sensed_cnt[i][0]) sensed_position[i] = drinks_numbers[i][j];
  }
}

// 음료수가 복수 체크되었는지 오류체킹
void multi_chosen_check(int line) { //MAXLINE
  int not_0 = 0;
  int min = drinks_numbers[MAX_LINE-1][(MAX_POSITION/MAX_LINE)] ;
  //Serial.print(min);
  for (int i = 0; i < line; i++) {
    if (sensed_position[i] != -1) not_0 += 1;
  }

  if (not_0 == 0) {
    state = 0;
  } else if (not_0 == 1) {
    state = 1;
    for (int i = 0; i < line; i++) 
      if (sensed_position[i] != -1) final_sensed_position = sensed_position[i];
  }
     else for (int i = 0; i < line; i++){
      state = 1;
      if (sensed_position[i] != -1){
        if (min > sensed_position[i]) 
          min = sensed_position[i];       
      }
      final_sensed_position = min;
     }
      
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
    delayMicroseconds(2);
    digitalWrite(trigPin[i], LOW);
    duration[i] = pulseIn(echoPin[i], HIGH);

    //초음파는 29마이크로초 당 1센치를 이동
    //초음파의 이동 거리 = duration(왕복에 걸린시간) / 29 / 2
    distance[i] = (duration[i] / 2) / 29.1;

    //여기서 손 센싱 회수를 더함.
    hand_count(distance[i], i, (MAX_POSITION/MAX_LINE) + 1); 

    //Serial.println(distance[i]);
  }
  //Serial.println(" ");
  
/*
    for(int a=0;a<PIN_COUNT;a++){
    for(int b=0;b<(MAX_POSITION/MAX_LINE) + 1;b++){
      Serial.print(sensed_cnt[a][b]);
      Serial.print(" ");
    }
    Serial.println(" ");
    }
*/

  i += 1;

  sensing_drink((MAX_POSITION/MAX_LINE) + 1); //chosen sale에 값을 넣음. 음료 판매 측정
  
  if (final_sensed_position != -1){
      Serial.print("Sold_drink:");
      Serial.println(sold_position); //선택되어 판매되는 음료수
  }
  else {
    if ( i == ROOP_CNT) {
      most_hand_detect((MAX_POSITION/MAX_LINE) + 1);//q를 입력하는데 이때 q는 열의 수, "sensed_position"에 값이 들어간다.
      for (int a = 0; a < PIN_COUNT; a++) {
        for (int b = 0; b < (MAX_POSITION/MAX_LINE) + 1; b++) {
          sensed_cnt[a][b] = 0; //다음 음료 선택을 위해 초기화.
        }
      }

      multi_chosen_check(MAX_LINE); //2 : chosen number의 크기, 음료가 복수선택이 되었는지 보여주는 함수(오류체크)

      if (final_sensed_position != -1 || sold_position != -1|| state != 0){   
        Serial.print("success ");
        Serial.println(true);

        Serial.print("sensed_position ");
        Serial.println(final_sensed_position);
        
        Serial.print("sold_position ");
        Serial.println(sold_position);
  
        Serial.print("state ");
        Serial.println(state);
      } else {
        Serial.print("success ");
        Serial.println(false);
      }
      
      i=0;
    }
  }
  for (int c = 0; c < MAX_LINE; c++) {
    sensed_position[c] = -1; //다음 음료 선택을 위해 초기화.
  }
  final_sensed_position = -1;
  sold_position = -1;
  state = 0;
  delay(250);
}
