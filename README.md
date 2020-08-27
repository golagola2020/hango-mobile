# hango-client (행고 클라이언트)
> hango-client : 행고 프로젝트의 클라이언트(아두이노, 라즈베리파이, 안드로이드 스튜디오) 구현
주의 : [GitHub Pages](https://pages.github.com/)에 대해서 충분히 숙지할 것.  
주의 : [Collaborating with issues and pull requests](https://docs.github.com/en/github/collaborating-with-issues-and-pull-requests)을 정독할 것


## 시작하기에 앞서 
[hango-client](https://github.com/golagola2020/hango-client.git) 프로젝트를 실해시키기 위한 도구 및 프로그램 설치
### 라즈베리 파이
   1. [Arduino IDE](https://www.arduino.cc/en/main/software) 최신버전 설치
```
$ sudo apt-get update
$ sudo apt-get upgrade
$ sudo apt-get --purge remove arduino #기존에 설치된 구버전 제거
$ sudo apt-get autoremove
$ sudo apt-get clean
```
   사용하는 라즈베리파이의 사양에 맞춰 Linux ARM 버전 설치 뒤 압축 해제 & 설치
```
$ wget https://www.arduino.cc/en/main/software   # Linux ARM 32 bits (명령어 실행되는지 확인)
$ cd ~                    
mkdir Programs
cd ~/Downloads
cp ./arduino-1.8.13-linuxarm.tar.xz ~/Programs              # 다운받는 버전에 따라 숫자 변경.
cd ~/Programs
tar xvf arduino-1.8.13-linuxarm.tar.xz
cd arduino-1.8.13
./install.sh
```
설치된 압축파일 제거
``` 
$ rm arduino-1.8.3-linuxarm.tar.xz
```      

  2. Python3 설치
```  
$ sudo apt-get update
$ sudo apt-get install python3
```
  3. Node 설치 
```
$ uname -m    # arm 버전 확인
   --> armv71
```
라즈베리파이의 arm 버전에 맞춰 LTS버전 설치.
```
$ wget https://nodejs.org/dist/v12.18.3/node-v12.18.3-linux-armv7l.tar.xz  
$ tar -xvf node-v12.18.3-linux-armv7l.tar.xz
$ sudo mv node-v12.18.3-linux-armv7l /opt/nodejs 
$ $ sudo ln -s /opt/nodejs/bin/node /usr/bin/node
$ sudo ln -s /opt/nodejs/bin/npm /usr/bin/npm
$ sudo ln -s /opt/nodejs/bin/npx /usr/bin/npx
$ node -v
  -->v12.18.3
```
   라즈베리 파이에서 서버를 이용하기 위해서
```
$ git clone https://github.com/golagola2020/hango-server.git
$ cd hango-server/server
$ npm install
$ vi .env   #'.env' 파일에 DB 환경 변수 등록.
   # ENV
   DB_DOMAIN="Your DB Host Domain"
   DB_USER="Your DB User Name"
   DB_PASSWORD="Your DB User Password"
   DB_NAME="Your DB Name"
$ sudo node server.js  #실행
```

## 배포(발행)
https://github.com/golagola2020/hango-client 에 push 권한이 있다면 :
```
$ git checkout -b 'features to develop'
$ git commit -m '[features to develop] message...'
$ git push origin 'features to develop'
```
https://github.com/golagola2020/hango-client 에 push 권한이 없다면 :
   1. 포크 동기화 [Syncing a fork](https://docs.github.com/en/github/collaborating-with-issues-and-pull-requests/syncing-a-fork)
   2. Pull Request 보내기 [Creating a pull request](https://docs.github.com/en/github/collaborating-with-issues-and-pull-requests/creating-a-pull-request)


## 사용된 도구
* 아두이노 UNO - 손과 음료 센싱
* 라즈베리파이 3B+ - 센싱 데이터 관리 및 TTS 출력 
 

## 기여하기

[CONTRIBUTING.md](https://gist.github.com/PurpleBooth/b24679402957c63ec426) 를 읽으신 후 기여를 해주십시오. 자세한 Pull Request 절차와 행동 규칙을 확인하실 수 있습니다.

## 개발자

**안혜수** [shehdn](https://github.com/suehdn) : 아두이노와 라즈베리파이를 이용한 사물(손) 인식 및 스피커 출력 로직 구현
**송기수** [thdrltn684](https://github.com/thdrlcks784) : 모바일을 통해 '행고' 관리자 어플리케이션 구현
**박우림** [woorim960](https://github.com/woorim960) :

[기여자 목록](https://github.com/golagola2020/hango-opensource/graphs/contributors)을 확인하여 이 프로젝트에 참가하신 분들을 보실 수 있습니다.

## 라이센스

이 프로젝트는 MIT 허가서를 사용합니다 - [LICENSE.md](LICENSE.md) 파일에서 자세히 알아보세요.

