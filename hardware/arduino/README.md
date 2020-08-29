# hango-arduino
> 행고 자판기에서 센싱파트를 맡은 아두이노입니다.

## 시작하기에 앞서
   * 이용하고있는 환경에서 [Arduiono IDE](https://www.arduino.cc/en/main/software)가 설치되어있는지 확인해주세요.
   * 아두이노에 초음파 센서 연결을 합니다.   
![아두이노회로도(번호추가)](https://user-images.githubusercontent.com/67812466/91624116-7054c480-e9d9-11ea-92f0-77129b903d9e.jpg)

    (fritzing 소프트웨어 이용) 위 회로도는 arduino.ino 파일의 회로도입니다. 
   초음파센서 4개 중 1번과 2번은 음료수를 선택하는 버튼과 같은 행에 존재하고, 3번과 4번은 음료수가 나오는 곳과 같은 행에 존재합니다. (이해를 돕기위해 사진 첨부) 
   
   각 초음파 센서들의 trig핀은 순서대로 2,7,8,12에 연결하고 echo핀은 3,6,9,11에 연결합니다. 
     
## 시작하기
   git clone을 통해 얻은 hango-client 파일에서 라즈베리파이에서 받아놓은 최신 버전의 Arduino IDE로 arduino.ino 파일을 실행시킵니다.
   
   
** 초음파 센서의 위치에 대한 이해를 돕기 위해 아래의 사진을 첨부합니다. 
![자판기예시](https://user-images.githubusercontent.com/67812466/91625163-0c81ca00-e9e0-11ea-9eb3-ca127ad3facd.PNG)

   
