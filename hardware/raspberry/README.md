# hango-raspberry 
> 행고 프로젝트에서 센싱 데이터 수집 및 처리, 스피커 출력을 맡은 라즈베리파이입니다.  

## 시작하기에 앞서 

   라즈베리파이 환경 설정 

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
$ wget https://www.arduino.cc/en/main/software   # Linux ARM 32 bits 
$ cd ~                    
mkdir Programs
cd ~/Downloads
cp ./arduino-1.8.13-linuxarm.tar.xz ~/Programs              # 다운받는 버전에 따라 숫자 변경
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
   3. espeak 설치 
```
$ sudo apt-get update
$ sudo apt-get upgrade
$ sudo apt-get install espeak
```
   설치가 잘 되었는지 출력 테스트
```
$ espeak -v ko "안녕"
$ espeak "Hello"
```

   4. 서버와 연결하기 위한 준비

   * Node 설치 
```
$ uname -m    # arm 버전 확인
   --> armv71
```
   라즈베리파이의 arm 버전에 맞춰 LTS버전 설치
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
   서버를 이용하기 위해
```
$ git clone https://github.com/golagola2020/hango-server.git
$ cd hango-server/server
$ npm install
$ vi .env   #'.env' 파일에 DB 환경 변수 등록
   # ENV
   DB_DOMAIN="Your DB Host Domain"
   DB_USER="Your DB User Name"
   DB_PASSWORD="Your DB User Password"
   DB_NAME="Your DB Name"
$ sudo node server.js  #실행
```
## 시작하기
   터미널에서 
```
$ sudo node server.js
```
  서버가 정상동작하고 있다는 알림 확인 뒤 다른 터미널을 열어 파이썬 파일을 열어 실행시킵니다. 
```
$ pwd
/hango-client/hardware
$ python3 raspberry.py
``` 

