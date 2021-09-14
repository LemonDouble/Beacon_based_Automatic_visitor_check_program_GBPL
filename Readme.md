# Beacon_based_Automatic_visitor_check_program_GBPL

## 비콘 기반 자동 방문자 기록 프로그램

이 프로젝트는

- 경북대학교 Kyungpook National University (Korea)
- Shibaura Institute of Technology (Japan)
- University of Technology Malaysia (Malaysia)
- UCSI University (Malaysia)
- Ming Chi University of Technology (Taiwan)
- Dong A University (Vietnam)

총 5개국, 6개 국가가 참력하는 국제 협력 프로젝트의 결과물을 저장하는 레포지토리입니다.

(2021/02/22~ 2021/03/09 진행)

## 프로젝트의 배경 및 목적

- 2021년 3월 현재, 전 세계적으로 발병중인 Covid-19로 인해, 거의 모든 나라에서 역학 조사 및 추적을 위해 수기로 방문 기록지를 작성하거나, QR Code, ARS 등을 이용하여 방문 지역을 기록하고 있습니다.
- 하지만 상기한 세가지 방법 다, 방문 체크를 위해 별도의 Action이 필요하다는 단점이 있습니다.
  - 수기 명부는 시간을 들여 직접 작성 해야 합니다.
  - QR Code, ARS는 수기 명부에 비하면 간편하나, 핸드폰을 꺼내 특정한 액션을 취해야 한다는 수고로움이 있습니다.
- 따라서, 핸드폰에 있는 Bluetooth 장치와 비콘을 사용하여, 사용자가 핸드폰 어플리케이션을 실행시키고 돌아다니기만 하면, 자동적으로 체크인이 되는 시스템을 개발하는 것을 목적으로 하였습니다.

## 프로젝트 전체 작동 방식

- 이 프로젝트는 크게 세 가지 파트로 구성됩니다.
  1. ESP32 보드 기반, 블루투스 비콘 (리포지토리의 Beacon_code_ESP32 폴더) : Check-in을 할 업장에서 벽 등에 부착하여 사용할 비콘입니다. 이 비콘은 현재 위치를 주변에 Broadcasting 하는 역할을 맡습니다.
  2. Android 기반, 스마트폰 어플리케이션 ( 리포지토리의 mobile-application 폴더) : 주변의 비콘 신호를 Scanning하고, 만약 감지된 신호가 있다면 통계 서버로 Http Request를 보내는 역할을 맡습니다.
  3. 통계 서버 ( 리포지토리의 backend-server 폴더) : HTTP Request를 받으면 해당 Request를 MongoDB에 저장합니다. 지정된 사이트에 들어가면 DB에서 데이터를 가져와, 지금까지의 방문자 내역을 출력해주는 역할도 합니다. ( 개인정보가 들어가는 특성상, 인증과 인가를 구현해야 하나 시간 상 구현하지 못 하였습니다.)

![Untitled](%E1%84%8C%E1%85%A6%E1%84%86%E1%85%A9%E1%86%A8%20%E1%84%8B%E1%85%A5%E1%86%B9%E1%84%8B%E1%85%B3%E1%86%B7%2094962c0f62094567b1ba14482cc456d5/Untitled.png)

## 1. Beacon 작동 매커니즘 및 사용법

- Data Format은 Apple사의 iBeacon을 사용하였습니다.
- 테스트용 보드로는 Wemos D1 R32 보드를 사용하였습니다.
  - Wemos D1 R32 보드의 경우, 보드 내에 Wifi, Bluetooth 모듈이 내장되어 있어 별도 모듈 설치 없이 개발하기 편리하므로, 개발 보드로 사용하였습니다.
- ESP32 보드를 사용하기 위해서는 관련 라이브러리 설치가 필요합니다. ( [https://github.com/espressif/arduino-esp32](https://github.com/espressif/arduino-esp32) ) 를 통해 다운받을 수 있습니다.

### Beacon의 역할은 UUID, Major, Minor 정보를 Broadcasting 하는 것입니다.

- 이때, UUID는 각 사업장의 Unique Identifier, Major는 지역 구분, Major는 세부 지역 구분에 사용한다고 가정하였습니다.
  - Major, Minor는 단순한 숫자이나, 이후 통계 서버에서 구체적인 값으로 매핑됩니다.
  - 즉, 비콘에서 발송하는 원 데이터는 ea199aea-2fff-430a-ad7d-30f12334696c / 1 / 1 과 같은 식입니다.
  - 이후, 통계 서버에서 미리 등록된 ea199aea-2fff-430a-ad7d-30f12334696c / 1 / 1 값을 찾아, 구체적인 물리 주소와 매핑시킵니다.
  - UUID, Major, Minor의 쓰임새는 다음과 같습니다.
    - UUID (16byte) : 패밀리마트, 세븐일레븐, LAWSON 등, 사업체를 구분합니다.
    - Major (2byte) : 큰 지역을 구분합니다.
      - 1, 2, 3, 4..
      - 이후, 통계 서버에선 1은 Tokyo, 2는 Osaka 등으로 매핑됩니다.
    - Minor (2byte) : Major 지역의 범위 내, 작은 지역을 구분합니다.
      - 1, 2, 3, 4...
      - 이후, 통계 서버에서 미리 정해진 값으로 매핑됩니다.
      - 예를 들어,
        Major : 1, Minor : 1이라면 Tokyo, Shibya,
        Major : 1, Minor : 2라면 Tokyo, Akihabara
        Major : 2, Minor : 1라면 Osaka, Dotonbori
        식으로 매칭됩니다.
  - BLE_iBeacon.ino 파일의 45line에서 UUID를, 56, 57line에서 Major, Minor을 설정할 수 있습니다.

### Mobile Phone (Android) 의 역할은 각 유저의 정보를 저장하고,

비콘 정보를 수신하면 유저 정보와 비콘(위치) 정보를 취합하여
통계 서버로 HTTP Request를 보내는 것입니다.

- mobile-application 내부 폴더는 Android Studio의 프로젝트를 옮겨 놓은 것이므로, Android Studio로 열면 프로젝트를 볼 수 있습니다.
- Beacon 스캔을 위해 Android Beacon Library를 사용하였습니다. ( [https://altbeacon.github.io/android-beacon-library/download.html](https://altbeacon.github.io/android-beacon-library/download.html) )
- HTTP Request를 위하여 Retrofit2 Library를 사용하였습니다. ( [https://square.github.io/retrofit/](https://square.github.io/retrofit/) )

![Untitled](%E1%84%8C%E1%85%A6%E1%84%86%E1%85%A9%E1%86%A8%20%E1%84%8B%E1%85%A5%E1%86%B9%E1%84%8B%E1%85%B3%E1%86%B7%2094962c0f62094567b1ba14482cc456d5/Untitled%201.png)

- 유저 정보를 받기 위한 Activity들은 위와 같습니다.

  - 첫 화면은 앱 실행 화면입니다.
  - 두 번째 화면은 개인 정보를 취급하므로, 관련 동의를 받는 화면입니다.
  - 세 번째 화면은 개인 정보를 받는 화면입니다. 유저로부터 이름, 전화번호, 이메일 주소를 받습니다.
  - 네 번째 화면은 앱이 준비되었고, 비콘 스캔을 하는 화면입니다. 앱을 닫아도 백그라운드에서 작동하도록 구현하였습니다.

  ![Untitled](%E1%84%8C%E1%85%A6%E1%84%86%E1%85%A9%E1%86%A8%20%E1%84%8B%E1%85%A5%E1%86%B9%E1%84%8B%E1%85%B3%E1%86%B7%2094962c0f62094567b1ba14482cc456d5/Untitled%202.png)

- 위와 같이, 비콘이 작동하고 있는 경우 해당 Beacon Data를 수신할 수 있습니다.

### 통계 서버(백엔드 서버) 의 역할은, HTTP Request를 받아 저장하는 것입니다.

- backend-server 폴더가 해당 폴더입니다.
- Node.js, Express.js를 사용하였습니다.
- DB로는 MongoDB를 사용하였고, mongoose를 사용하여 연결하였습니다.
- 사용자가 미리 등록한 데이터를 기반으로, Android에서 수신받은 UUID, Major, Minor를 해당 Place 데이터로 바꾸고 저장합니다.

![Untitled](%E1%84%8C%E1%85%A6%E1%84%86%E1%85%A9%E1%86%A8%20%E1%84%8B%E1%85%A5%E1%86%B9%E1%84%8B%E1%85%B3%E1%86%B7%2094962c0f62094567b1ba14482cc456d5/Untitled%203.png)
