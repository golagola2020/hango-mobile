# hango-server (행고 웹서버)
> 주의 : [GitHub Pages](https://pages.github.com/)에 대해서 충분히 숙지할 것.  
주의 : [Collaborating with issues and pull requests](https://docs.github.com/en/github/collaborating-with-issues-and-pull-requests)을 정독할 것

## 시작하기에 앞서

[hango-server](https://github.com/golagola2020/hango-server) 프로젝트를 실행시키기 위한 도구 및 프로그램 설치  
   1. [Node](https://nodejs.org/ko/download/) 설치
   2. [MySQL](https://dev.mysql.com/downloads/installer/) 설치

## 설치

https://github.com/golagola2020/hango-server 에 push 권한이 있다면 :  
   1. git fetch or pull or clone
```
$ git clone https://github.com/golagola2020/hango-server.git
$ cd hango-server
```

https://github.com/golagola2020/hango-server 에 push 권한이 없다면 :  
   1. https://github.com/golagola2020/hango-server 에서 ```Fork```버튼 클릭하고,
   2. 포크 저장소 계정(maybe 개인 계정) 선택
   3. git fetch or pull or clone
   4. 포크 설정 [Configuring a remote for a fork](https://docs.github.com/en/github/collaborating-with-issues-and-pull-requests/configuring-a-remote-for-a-fork)
   5. 포크 동기화 [Syncing a fork](https://docs.github.com/en/github/collaborating-with-issues-and-pull-requests/syncing-a-fork)
```
$ git clone https://github.com:YOUR_GITHUB_ACCOUNT/hango-server.git
$ cd hango-server
$ git remote add upstream https://github.com/golagola2020/hango-server.git
$ git fetch upstream
$ git checkout master
$ git merge upstream/master
```

## 실행(로컬)

   1. 패키지 설치
   2. '.env' 파일 생성 후 DB 환경 변수 등록
   3. 실행
```
# 1. 패키지 설치
$ npm install

# 2. '.env' 파일 생성 후 DB 환경 변수 등록
$ touch .env
$ vi .env
```
이어서 '.env' 파일에 DB 환경 변수 등록.
```
# ENV
DB_DOMAIN="Your DB Host Domain"
DB_USER="Your DB User Name"
DB_PASSWORD="Your DB User Password"
DB_NAME="Your DB Name"
```
```
# 3. 실행
$ sudo node server.js
```

## 배포(발행)

https://github.com/golagola2020/hango-server 에 push 권한이 있다면 :  
```
$ git ch -b 'features to develop'
$ git commit -m '[features to develop] message...'
$ git push origin 'features to develop'
```

https://github.com/golagola2020/hango-server 에 push 권한이 없다면 :  
   1. 포크 동기화 [Syncing a fork](https://docs.github.com/en/github/collaborating-with-issues-and-pull-requests/syncing-a-fork)
   2. Pull Request 보내기 [Creating a pull request](https://docs.github.com/en/github/collaborating-with-issues-and-pull-requests/creating-a-pull-request)

## 사용된 도구

* [Node.js](https://nodejs.org/ko/about/) - 서버 프레임워크
* [Express.js](https://expressjs.com/ko/) - 웹 프레임워크
* [MySQL](https://www.mysql.com/about/) - 관계형 데이터베이스 관리시스템

## 의존성

```json
"dependencies": {
    "body-parser": "^1.19.0",
    "dotenv": "^8.2.0",
    "ejs": "^3.1.3",
    "express": "^4.17.1",
    "mysql": "^2.18.1"
}
```
* [body-parser](https://github.com/expressjs/body-parser#readme)
* [dotenv](https://github.com/motdotla/dotenv#readme)
* [ejs](https://github.com/mde/ejs)
* [express](https://github.com/expressjs/express)
* [mysql](https://github.com/mysql)


## 기여하기

[CONTRIBUTING.md](https://gist.github.com/PurpleBooth/b24679402957c63ec426) 를 읽으신 후 기여를 해주십시오. 자세한 Pull Request 절차와 행동 규칙을 확인하실 수 있습니다.

## 개발자

  - **박우림** [woorim960](https://github.com/woorim960) : 라즈베리파이와 모바일간 통신을 위한 서버


[기여자 목록](https://github.com/golagola2020/hango-server/graphs/contributors)을 확인하여 이 프로젝트에 참가하신 분들을 보실 수 있습니다.

## 라이센스

이 프로젝트는 MIT 허가서를 사용합니다 - [LICENSE.md](LICENSE.md) 파일에서 자세히 알아보세요.


