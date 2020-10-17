# hango-mobile
> 주의 : [GitHub Pages](https://pages.github.com/)에 대해서 충분히 숙지할 것.  
주의 : [Collaborating with issues and pull requests](https://docs.github.com/en/github/collaborating-with-issues-and-pull-requests)을 정독할 것


## 소개
> 'Hango'의 자판기 관리자를 위한 Android Application 
   
   1. 현재 보유 중인 자판기들을 보여 주고 본인의 자판기를 관리 할 수 있습니다.
   2. 각 자판기의 음료 현황(이름, 가격, 남은 양 등)을 관리 할 수 있습니다. 
   
   * 데모 영상 : https://youtu.be/K7cLH89WKPQ
   
## 주요 기능

   * SignUp & SignIn
   * 자판기 정보 조회, 수정, 삭제
   * 자판기의 음료 정보 등록, 조회, 수정 기능
   
## 설치
   
   https://github.com/golagola2020/hango-client 에 push 권한이 있다면 :  
   1. git fetch or pull or clone
```
$ git clone https://github.com/golagola2020/hango-client.git
$ cd hango-client/mobile/hango
```

https://github.com/golagola2020/hango-client 에 push 권한이 없다면 :  
   1. https://github.com/golagola2020/hango-client 에서 ```Fork```버튼 클릭하고,
   2. 포크 저장소 계정(maybe 개인 계정) 선택
   3. git fetch or pull or clone
   4. 포크 설정 [Configuring a remote for a fork](https://docs.github.com/en/github/collaborating-with-issues-and-pull-requests/configuring-a-remote-for-a-fork)
   5. 포크 동기화 [Syncing a fork](https://docs.github.com/en/github/collaborating-with-issues-and-pull-requests/syncing-a-fork)
```
$ git clone https://github.com:YOUR_GITHUB_ACCOUNT/hango-client.git
$ cd hango-client/mobile/hango
$ git remote add upstream https://github.com/golagola2020/hango-client.git
$ git fetch upstream
$ git checkout master
$ git merge upstream/master
```
   
## 환경변수 설정

   * 환경변수 설정을 위한 environment package를 ```/mobile/hango/app/src/main/java/com/hango``` 경로에 만듭니다.
   * environment package안에 Network Class 를 작성하고, URL 정보를 Setting 하고 Getter 접근합니다.
   
   ```java
   public class Network {
      private String URL = "http://127.0.0.1";

      public String getURL(){
         return URL;
    }
}
   ```

## 개발 환경

   * Android Studio @4.0.1
   
## Application Version

   * minSdkVersion : 23
   * targetSdkVersion : 29(API 29: Android 10.0(Q))
   
## Screenshots

<img src="https://user-images.githubusercontent.com/64050689/96349945-ddd5c500-10ed-11eb-8939-d69fb5b7a1c5.jpg" width="180px" height="320px"></img>
<img src="https://user-images.githubusercontent.com/64050689/96349958-efb76800-10ed-11eb-90e0-874ac5909cc5.jpg" width="180px" height="320px"></img>
<img src="https://user-images.githubusercontent.com/64050689/96349967-fb0a9380-10ed-11eb-988e-42412e0b211a.jpg" width="180px" height="320px"></img>
<img src="https://user-images.githubusercontent.com/64050689/96349983-0c53a000-10ee-11eb-9edc-28dc0e478303.jpg" width="180px" height="320px"></img>
<img src="https://user-images.githubusercontent.com/64050689/96350000-23928d80-10ee-11eb-9de8-8e57a92f1109.jpg" width="180px" height="320px"></img>
<img src="https://user-images.githubusercontent.com/64050689/96350004-2d1bf580-10ee-11eb-83ff-c378fc3e0941.jpg" width="180px" height="320px"></img>
<img src="https://user-images.githubusercontent.com/64050689/96350012-373df400-10ee-11eb-9dd2-4395158c41fb.jpg" width="180px" height="320px"></img>
<img src="https://user-images.githubusercontent.com/64050689/96350017-402ec580-10ee-11eb-9659-a83a154a440a.jpg" width="180px" height="320px"></img>
<img src="https://user-images.githubusercontent.com/64050689/96350022-491f9700-10ee-11eb-8766-36c52b62bb27.jpg" width="180px" height="320px"></img>
<img src="https://user-images.githubusercontent.com/64050689/96350030-53da2c00-10ee-11eb-979b-56e190453327.jpg" width="180px" height="320px"></img>
<img src="https://user-images.githubusercontent.com/64050689/96350039-5c326700-10ee-11eb-9cc5-0f0958a3fe0a.jpg" width="180px" height="320px"></img>
<img src="https://user-images.githubusercontent.com/64050689/96350044-66546580-10ee-11eb-9252-f6bb3f6925a4.jpg" width="180px" height="320px"></img>
<img src="https://user-images.githubusercontent.com/64050689/96350051-6f453700-10ee-11eb-9420-83f7f8734e0f.jpg" width="180px" height="320px"></img>
<img src="https://user-images.githubusercontent.com/64050689/96350053-79673580-10ee-11eb-8e3f-87c4ef3bde47.jpg" width="180px" height="320px"></img>
<img src="https://user-images.githubusercontent.com/64050689/96350060-83893400-10ee-11eb-8312-23d0052cf29f.jpg" width="180px" height="320px"></img>
<img src="https://user-images.githubusercontent.com/64050689/96350066-8be16f00-10ee-11eb-870f-1611abb4e5c3.jpg" width="180px" height="320px"></img>
<img src="https://user-images.githubusercontent.com/64050689/96350069-93087d00-10ee-11eb-8363-cbd9e15411ba.jpg" width="180px" height="320px"></img>
<img src="https://user-images.githubusercontent.com/64050689/96350073-9b60b800-10ee-11eb-9b65-ce6ff0f40af3.jpg" width="180px" height="320px"></img>
<img src="https://user-images.githubusercontent.com/64050689/96350075-a3b8f300-10ee-11eb-99b1-627eda92d26d.jpg" width="180px" height="320px"></img>

## 의존성
   * Volley  
      네트워크 연결에 ```Volley @1.1.1```를 사용
      Volley 사용을 위해 Gradle에 아래의 의존성 문장 추가
      ```
      implementation 'com.android.volley:volley:1.1.1'
      ```

      예시  
      ```
      dependencies {
               implementation fileTree(dir: "libs", include: ["*.jar"])
               implementation 'com.android.support:appcompat-v7:28.0.0'
               implementation 'com.android.support.constraint:constraint-layout:1.1.3'
               implementation 'com.android.volley:volley:1.1.1'   //Volley 사용을 위한 의존성 추가
               testImplementation 'junit:junit:4.12'
               androidTestImplementation 'com.android.support.test:runner:1.0.2'
               androidTestImplementation 'com.android.support.test.espresso:espresso-core:3.0.2'
           }
      ```
      
## 기여하기

[CONTRIBUTING.md](https://gist.github.com/PurpleBooth/b24679402957c63ec426) 를 읽으신 후 기여를 해주십시오. 자세한 Pull Request 절차와 행동 규칙을 확인하실 수 있습니다.

## 개발자

  - **송기수** [thdrlcks784](https://github.com/thdrlcks784) : 자판기 관리자 Application 개발


[기여자 목록](https://github.com/golagola2020/hango-server/graphs/contributors)을 확인하여 이 프로젝트에 참가하신 분들을 보실 수 있습니다.
