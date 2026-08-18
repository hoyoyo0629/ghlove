# 로컬 개발 환경 구성 

## 1. 시스템 요구사항 
- nodejs 설치 
- icraft.zip (spring tools4 + openjdk11 + saleson source)

## 2. 개발 환경 구성 
### 1) nodejs 설치
https://nodejs.org/ko/ 에서 16.17.0 TLS 버전을 다운로드 하여 설치한다. 

- Windows Installer (.msi) 64-bit: https://nodejs.org/dist/v16.17.0/node-v16.17.0-x64.msi

### 2) icraft.zip 압축 풀기 
icraft.zip 파일을 c:에 압축을 푼다. 


#### 폴더구조 
- c:/icraft
  - bin
    - openjdk-11
    - sts-4.15.3.RELEASE 
  - workspace
    - saleson


### 3) SpringToolSuite 실행 
c:/icraft/bin/sts-4.15.3.RELEASE/SpringToolSuite4.exe 파일을 실행한다. 

STS 좌측 Project Explorer에서 saleson 선택 후 우클릭 > Gradle > Refresh Gradle Project 클릭 


## 3. 프로젝트 실행
### 1) saleson-web (8080)
- 관리자, 판매관리자 
- STS 좌측 하단 `Boot Dashboard` 툴에서 local > saleson-web 을 선택한 후 실행 아이콘 클릭 

#### 관리자 
- 접속 URL: http://localhost:8080/opmanager
- id: saleson
- pw: pw92%@

#### 판매관리자 
- 접속 URL: http://localhost:8080/seller
- id: seller
- pw: pw92%@

### 2) saleson-api (9080)
- API
- STS 좌측 하단 `Boot Dashboard` 툴에서 local > saleson-api 을 선택한 후 실행 아이콘 클릭
- 접속 URL: http://localhost:8080/api/*

### 3) saleson-frontend (3000)
- frontend 
- STS 좌측 Project Explorer에서 saleson-frontend > index.js 파일을 선택.
- index.js 파일 선택 후 우클릭 > Run As > Node program 클릭 
- 접속 URL: http://localhost:3000

## 4. 빌드 및 배포 
### 1) 프로젝트 빌드 
STS 하단 `Gradle Tasks` 툴에서 saleson > build > build 를 더블 클릭하여 프로젝트를 빌드한다.  


#### 빌드 파일
#### saleson-front
- saleson/build/distributions/saleson-frontend.zip

#### saleson-api
- saleson/saleson-api/build/libs/saleson-api-3.15.0.jar

#### saleson-web
- saleson/saleson-web/build/libs/saleson-web-3.15.0.jar
- saleson/build/distributions/saleson-static-content.zip

### 2) 개발 서버 배포 
#### 빌드 파일 업로드 
- saleson-frontend.zip
- saleson-api-3.15.0.jar
- saleson-web-3.15.0-plain.war
- saleson-static-content.zip

파일을 개발 서버 /home/icraft/_deploy/ 경로에 업로드 

#### 배포 실행 
- /home/icraft/_deploy/deploy.sh 스크립트를 실행하여 배포를 실행함. 


#### 배포 확인 
- 프론트: http://61.72.154.140
- 관리자: http://61.72.154.140/opmanager
- 판매관리자: http://61.72.154.140/seller 


## 5. 개발 서버 설치 정보 
### 1) 세일즈온 설치 경로 
- 설치경로 (home): /home/icraft

#### 폴더 구조 
- /home/icraft
  - _deploy : 배포 폴더 
  - api: api 서비스 
  - backoffice: 관리자 / 판매관리자 
  - frontend: 프론트
  - license: 라이선스 파일 
  - payment: pg 관련 파일 
  - temp: 임시 파일 

### 2) Mysql 
- version: 8.0.30

#### 시작 / 중지 
```
systemctl stop mysqld
systemctl start mysqld
```

#### root 계정 비밀번호 
```shell
Wipia123#
```

#### icraft 개발 DB 정보 
- db: icraft
- id: icraft
- pw: Dnlvldk123#

