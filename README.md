# 백엔드 개발자 강감찬 포트폴리오

> **최종 업데이트**: 2024년 11월 20일

## 소개
안녕하세요! 백엔드 개발자 강감찬 입니다.
- 어려운 과제에 도전하고 성장하는 것에 흥미를 느낍니다.

## 기술 스택
- **Backend**: ![Java](https://img.shields.io/badge/Java-007396?style=flat-square&logo=Java&logoColor=white) ![Spring](https://img.shields.io/badge/Spring-6DB33F?style=flat-square&logo=Spring&logoColor=white) ![Spring Boot](https://img.shields.io/badge/Spring_Boot-6DB33D?style=flat-square&logo=Spring_Boot&logoColor=white)

- **DBMS**: ![MySQL](https://img.shields.io/badge/MySQL-4479A1?style=flat-square&logo=MySQL&logoColor=white) ![MariaDB](https://img.shields.io/badge/MariaDB-003545?style=flat-square&logo=MariaDB&logoColor=white)

- **ORM**: ![JPA](https://img.shields.io/badge/JPA-6DB33F?style=flat-square&logo=Spring&logoColor=white) ![Hibernate](https://img.shields.io/badge/Hibernate-59666C?style=flat-square&logo=Hibernate&logoColor=white) ![MyBatis](https://img.shields.io/badge/MyBatis-000000?style=flat-square)

- **DevOps & Tools**: ![Git](https://img.shields.io/badge/Git-F05032?style=flat-square&logo=Git&logoColor=white) ![GitHub](https://img.shields.io/badge/GitHub-181717?style=flat-square&logo=GitHub&logoColor=white)

- **Others**: ![JavaScript](https://img.shields.io/badge/JavaScript-F7DF1E?style=flat-square&logo=JavaScript&logoColor=white) ![HTML](https://img.shields.io/badge/HTML-E34F26?style=flat-square&logo=html5&logoColor=white) ![CSS](https://img.shields.io/badge/CSS-1572B6?style=flat-square&logo=css3&logoColor=white) ![WebSocket](https://img.shields.io/badge/WebSocket-000000?style=flat-square&logo=WebSocket&logoColor=white)

## 주요 프로젝트
### 1.[TSPOON]
> 회원가입, 로그인, 1:1문의 게시판, 쪽지 기능을 포함한 저의 첫 프로젝트입니다.

**역할**: 백엔드 개발
**기술 스택**: Java, Servlet, JSP, MariaDB, Tomcat
**주요 기능**: Servlet과 JSP 기반으로 제한적 MVC 패턴을 적용한 웹사이트
- 회원가입, 로그인, 로그아웃, 자동 로그인
  - 회원 데이터 CRUD
  - Ajax 통신을 통해 아이디 중복체크 비동기 처리
  - AuthenticatedUserFilter로인해 원하는 페이지가 아닌 로그인 페이지로 이동 시
    LoginController에서 referer 헤더로 원하는 페이지의 URL을 확인후 URI부분을 추출 후
    로그인 성공 시 해당 URI로 리다이렉트하여 사용자가 원하는 페이지로 이동하도록 구현
- 1:1 문의 게시판
  - 게시판 데이터 CRUD
  - 파일 업로드, 다운로드 기능
  - 게시판 리스트 페이징 기능 구현
- 쪽지
  - 쪽지 데이터 CRUD
  - 파일 업로드, 다운로드 기능
  - 쪽지 리스트 페이징 기능 구현
- Util
  - Controller에서 JavaScript의 alert, historyback 등의 메서드를 간편하게
    사용할 수 있도록하는 JSFunc 클래스와 메서드를 구현

**성과**:
- MVC 패턴에 대한 이해, 데이터 CRUD 구현 숙련도 증가
- 모듈화를 통한 효율적인 개발방식 학습

**[프레젠테이션](project/tspoon/강감찬_tspoon_ppt.pdf)** | **[주요 소스코드](project/tspoon/강감찬_tspoon_project.md)** | **[github](https://github.com/kangkamchan/Portfolio/tree/main/project/tspoon)**

***
### 2.[onlineLecture]
> 강의 관리, 사용자 인증 등의 기능을 포함한 저의 첫 협업 프로젝트입니다.

**역할**: 백엔드개발 및 공통 모듈 설계 및 DB설계 및 DDL관리
**기술 스택**: Java, Servlet, JSP, MariaDB, Tomcat
**주요 기능**:
- 회원가입, 로그인, 강의 구매, 결제, 게시판 등 기능 구현
- 동영상 업로드 및 재생 기능 구현

**성과**:
-  공통 모듈(유틸, 페이지) 설계
	- jsp 페이지, 공통 백엔드로직 모듈화 하여 개발 효율성 증진 및 시간단축
-  회원가입 백엔드 유효성검사 구현하여 데이터 정합성 유지 및 보안 강화
- 데이터베이스 관리
  - DB 설계 및 DDL 관리
  - SSH를 통한 DB접속 및 관리
- 공통 모듈(유틸) 설계
  - 모듈화된 구조, MVC 패턴적용
  - 클린 코드 작성
- 첫 협업 프로젝트 경험
  - 의사소통 및 협업 능력 향상

**[프레젠테이션](project/onlineLecture/onlinLecture_project.pdf)** | **[주요 소스코드](project/onlineLecture/onlineLecture_project.md)** 

***

### 3.[NANUSAM]

>교사 간 중고 거래 플랫폼으로, 중고 물품 거래 및 채팅 기능을 포함하고 있습니다.

**역할**: 채팅기능, 후기,  알림, 거래예약 부분 백엔드 개발 
**기술 스택**: Java, Servlet, JSP, MariaDB, Tomcat, Spring, WebSocket
**주요 기능**:
- 회원가입, 로그인, 중고 물품 등록,수정,삭제 기능 구현
- 중고물품 거래 프로세스 구현
- 채팅 기능
  - 채팅방 생성, 채팅방 입장, 채팅방 퇴장, 채팅 기능
  - WebSocket과 http 하이브리드 통신 구현
- 관리자 페이지
  - 중고 물품 관리, 회원 관리 기능 구현

**성과**:
- Spring MVC, Spring Web을 웹 애플리케이션 구현
- MyBatis를 활용한 쿼리 작성법 숙달
- 실시간 1:1 채팅 시스템 구현
  - WebSocket을 활용한 실시간 양방향 채팅 시스템 구현
  - 웹소켓 세션에서 Http 세션을 사용할 수 있도록 구현
  - 비동기 멀티스레드 방식으로 메시지 처리
  - 채팅 메시지를 DB에 저장하여 이전 대화 내역을 조회 가능
  - 읽지 않은 메시지 알림 기능 구현

**[프레젠테이션](project/eduSecond/eduSecond_project.pdf)** | **[주요 소스코드](project/nanusam/nanusam_project.md)** 

***

### 4.[KMOOC]

> 학점은행제를 지원하는 온라인 교육플랫폼, K-Mooc 사이트를 참고하여 만든 프로젝트

**역할**: 공통 페이징 처리 클래스 개발, 강의 상세보기 프론트/백엔드 구현, 수강신청/취소 RestController와 Ajax 통신 구현
**기술 스택**: Java, SpringBoot, JPA, Thymeleaf, Javascript, MariaDB
**주요 기능**:
**성과**: 
- SpringBoot와 JPA를 활용한 첫 프로젝트 수행
- 검색, 정렬, 페이징을 처리하는 공통 PageDTO 클래스 구현
- RestController와 Ajax 통신 사용법 숙달

**[프레젠테이션](project/kmooc/kmooc_project.pdf)** | **[주요 소스코드](project/kmooc/kmooc.md)** 
