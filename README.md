# 백엔드 개발자 강감찬 포트폴리오

## 소개
안녕하세요! 백엔드 개발자 강감찬 입니다.
- 반복된 코드를 최대한 피하려 노력합니다. 모듈화, AOP 에 관심이 있습니다.
- 변수, 함수명칭을 의미있고 직관적으로 짓기 위해 고민합니다.
- 어려운 과제에 도전하고 성장하는 것에 흥미를 느낍니다.

## 기술 스택
- **Strong**: Java, Spring Framework, Javascript, MariaDB/MySQL
- **Usable**: JPA, MyBatis, WebSocket, Linux(ubuntu), AWS
- **ETC**: Git

## 프로젝트 경험

### 1.[시험지 제작 웹 페이지 T-Sherpa] [PL]
> 문제은행 API 에서 받아온 문제를 바탕으로 사용자가 원하는 시험지를 제작하는 교사 대상 웹페이지입니다.

**기간**:2024.12.16 ~ 2025.01.09

**역할**:
 - 회원가입, 로그인 프론트/백엔드 개발 (JBcrypt 라이브러리 활용한 비밀번호 암호화)
 - 출제방식 선택 부분 프론트/백엔드 개발 (Ajax 통신, FetchAPI, Server to Server API통신 구현)
 - 문항삭제 부분 프론트엔드 개발 (Javascript로 문항 UI 컨트롤)
 - 오류신고 부분 프론트/백엔드 개발(RESTful API, Ajax 통신)

**기술스택**:Java, Spring Boot, JPA, Thymeleaf, Javascript, MariaDB, Ajax, WebClient, RESTful API, Spring AOP

**성과**
1. 회원가입, 로그인 로직 구현 시 Bcrypt 라이브러를 활용한 암호화 로직을 구현하여 보안성 향상
2. Spring WebFlux라이브러리의 WebClient 를 활용한 Server to Server API 통신 구현
3. Logging, RedirectWithError 애너테이션과 Aspect 클래스를 구현 반복 코드를 줄이고 클린코드 작성에 기여함

![step0](https://github.com/user-attachments/assets/edc326f2-7c77-4398-b632-e840cde35df9)

### 2.[학습공유 플랫폼 swc]
> 학습공유와 학습내용 정리를 통한 동기부여와 학업성취도 향상을 목적으로 하는 플랫폼입니다.

**기간**:2024.12.5 ~ 12.13

**역할**: 
 - 학습게시물 등록, 조회, 수정, 삭제, 좋아요, 공유하기 기능 프론트, 백엔드 구현
 - 오늘의학습 메인페이지 프론트, 백엔드 구현(Ajax 통신 활용 비동기 UI 업데이트)
 - JWT Token 인증 확인 @CheckJwt 애너테이션과 Aspect 클래스 구현
 - 에러메시지와 함께 지정 URI 로 리다이렉트 하는 ErrorUtil 구현
 
**기술 스택**: Java, SpringBoot, JPA, MyBatis, Thymeleaf, Javascript, MariaDB
 
**성과**:
 1. **RESTful API 설계 및 구현**  
   - RESTful한 URI 네이밍 규칙을 적용하여 `RestController`를 개발.  
   - API 정의서를 작성하여 팀원들과 원활한 협업 환경 구축.

 2. **AJAX 통신 및 JSON 데이터 처리**  
   - AJAX를 활용하여 객체 리스트를 요청하고, 받은 JSON 데이터를 파싱하여 UI에 적용.  
   - 비동기 통신을 통해 사용자 경험(UX) 개선.

 3. **공통 기능 모듈화**  
   - 반복적인 작업을 줄이기 위해 애너테이션과 공통 Util 클래스를 구현.  
   - 개발 효율성을 크게 향상시키고 유지보수성을 강화.
     
 **[주요 소스코드](project/swc/swc.md)** 

 ![main](https://github.com/user-attachments/assets/20c6ce35-41cd-4744-96a0-95ecb8a7e20d)
 ![share](https://github.com/user-attachments/assets/2acc18c0-0b24-4779-915b-a160be4cb832)
 ![thumb-up](https://github.com/user-attachments/assets/737c2773-c97e-4070-98aa-b9831170f48d)

### 3.[KMOOC]
> 학점은행제를 지원하는 온라인 교육플랫폼, K-Mooc 사이트를 참고하여 만든 프로젝트

**기간**: 2024.11.22 ~ 12.1

**역할**
 - 공통 페이징 처리 클래스 PageDTO 개발
 - 강의 상세보기 프론트/백엔드 구현(Spring, Thymeleaf)
 - 수강신청/취소 RestController와 Ajax 통신 구현

**기술 스택**: Java, SpringBoot, JPA, Thymeleaf, Javascript, MariaDB

**성과**: 
- SpringBoot와 JPA를 활용한 첫 프로젝트 수행
- 검색, 정렬, 페이징을 처리하는 공통 PageDTO 클래스 구현
- RestController와 Ajax 통신 사용법 숙달

**[프레젠테이션](project/kmooc/kmooc_project.pdf)** | **[주요 소스코드](project/kmooc/kmooc.md)** 


### 4.[NANUSAM]

>교사 간 중고 거래 플랫폼으로, 중고 물품 거래 및 채팅 기능을 포함하고 있습니다.

**역할**
 - 실시간 채팅기능 구현(javax.websocket 활용)
 - 알림기능 프론트, 백엔드 구현(Ajax 통신)
 - 후기 작성, 수정, 삭제 프론트, 백엔드 구현
   
**기술 스택**: Java, Servlet, JSP, MariaDB, Tomcat, Spring, WebSocket

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

### 5.[onlineLecture]
> 강의 관리, 사용자 인증 등의 기능을 포함한 저의 첫 협업 프로젝트입니다.

**역할**: 프론트/백엔드개발 및 공통 모듈 설계, DB설계 및 DDL관리

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

### 6.[TSPOON]
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
