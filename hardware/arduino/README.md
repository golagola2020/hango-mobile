# hango-arduino
> 행고 자판기에서 센싱파트를 맡은 아두이노입니다.

## 시작하기에 앞서
   * 이용하고있는 환경에서 [Arduiono IDE](https://www.arduino.cc/en/main/software)가 설치되어있는지 확인해주세요.
   * 아두이노에 초음파 센서 연결을 합니다.   
      (fritzing 소프트웨어 이용) 아래의 회로도는 arduino.ino 파일을 실행시킬 아두이노의 회로도입니다. 
![아두이노회로도(번호추가)](https://user-images.githubusercontent.com/67812466/91624116-7054c480-e9d9-11ea-92f0-77129b903d9e.jpg)

   
   
   초음파센서 4개 중 1번과 2번은 음료수를 선택하는 버튼과 같은 행에 존재하고, 3번과 4번은 음료수가 나오는 곳과 같은 행에 존재합니다. (이해를 돕기위해 사진 첨부) 
   
   각 초음파 센서들의 trig핀은 순서대로 2,7,8,12에 연결하고 echo핀은 3,6,9,11에 연결합니다.
```
const int trigPin[PIN_COUNT] = {2, 7, 8, 12};
const int echoPin[PIN_COUNT] = {3, 6, 9, 11};
```
    
   현재 설정되어있는 자판기는 총 8 종류의 음료수를 판매하며 한 줄에 4종류를 판매하는 총 2개의 라인을 갖습니다. 
   ROOP_COUNT는 현재 자판기에서 손을 인식할 때, 초음파 센서에 한번 인식 되는것이 아닌 여러번 인식해 그 횟수를 더합니다. 이때 가장 많이 센싱된 곳을 실제 측정된다고 보기 때문에 정확도가 높아집니다.  
```
#define MAX_LINE 2          // 자판기 전체 라인 수
#define MAX_POSITION 8      // 자판기 전체 칸 수
#define PIN_COUNT 4         // 핀 개수
#define ROOP_CNT 5          // 정확도를 위해 손 측정시 몇번의 루프를 돌것인지 결정(i의 최대값을 결정.)
#define BOTH_SIDE_SPACE 10  // 자판기에서 버튼이 존재하지 않는 양옆 구간의 거리(cm)
#define BUTTON_RANGE 10     // 버튼과 버튼 사이의 거리
```
   
     
## 시작하기
   git clone을 통해 얻은 hango-client 파일에서 라즈베리파이에서 받아놓은 최신 버전의 Arduino IDE로 arduino.ino 파일을 실행시킵니다.
   
   
** 초음파 센서의 위치에 대한 이해를 돕기 위해 아래의 사진을 첨부합니다. 위의 회로도의 전선 색에 맞춰 변경하시기 바랍니다. 

![자판기예시](https://user-images.githubusercontent.com/67812466/91648593-5207cb00-eaa4-11ea-8af1-d01680747228.PNG)

   
