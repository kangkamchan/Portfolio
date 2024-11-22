# NANUSAM 주요 소스코드
---
## 환경 설정
- **JDK 버전** : 11
- **Tomcat 버전** : 9.0.96

## 주요 의존성 및 버전
- **Spring**: 5.3.39
- **JUnit**: 5.10.2
- **Servlet API**: 4.0.1
- **JSTL**: 1.2
- **Log4j**: 2.24.1
- **Lombok**: 1.18.34
- **HikariCP**: 5.1.0
- **MyBatis**: 3.5.15
- **Websocket**: 1.1
---

## 채팅 기능

### 주요 기능 요약
- 비동기 멀티스레드 방식으로 실시간 1:1 채팅기능 구현
- HttpSession을 활용하여 인증 및 비즈니스 로직 처리
- 중고거래 페이지 특성에 따라 채팅을 통한 예약기능 수행
- 예외 발생 상황을 상세하게 설정 및 처리 

### 개선사항
- 채팅 서버의 안정성 확보를 위한 웹소켓 동시 접속자 수 테스트 절차 필요
- 메시지를 ||으로 구분하는 형식으로 구현하였는데, 더 유연하고 안정적인 처리를 위해 JSON 등의 표준 형식으로 변경
- 데이터베이스 접근, 비즈니스로직을 분리하여 모듈화
- RESTful 방식으로 채팅리스트, 채팅방을 실시간으로 업데이트하는 로직 구현

### Websocket.java

#### 웹소켓 설정 부분
- 웹소켓 내부에서 비즈니스 로직 처리를 위한 Service 객체를 선언 및 의존성을 주입
- 로직 처리 중 유효하지 않은 값이 입력되었을 경우 오류 메세지를 전송하기 위한 사용자 정의 에러코드인 errCode를 선언
- 채팅 기능을 비동기 멀티스레드 방식으로 처리하기 위해 Executor 프레임워크를 사용, ExecutorService로 스레드풀을 관리
- 웹소켓 내부에서 HttpSession에 저장되는 로그인 정보(memberId)를 활용하여 세션 인증 및 메시지 전송 기능을 구현하기 위해
  @ServerEndpoint의 configurator 속성을 활용하여 웹소켓 연결 설정 단계에서의 handshake 과정에서 HttpSession을 
  설정 정보에 저장하도록 구현
```java
@Component
@ServerEndpoint(value = "/chat/{groupIdx}", configurator = ChatServer.EndpointConfigurator.class)
@Log4j2
public class ChatServer {
	//클래스 내에서 사용할 Service 객체 선언 및 의존성 주입
	private static ChatService chatService;
	private static GoodsService goodsService;
	private static AlertService alertService;
	/*사용자 정의 에러 코드
	001 : 세션없음
	002 : 로그인아이디가없음
	003 : 채팅방없음
	004 : 접근권한없음
	005 : 메시지 형식오류
	006 : db등록실패
	*/
	private String errCode;
	// ExecutorService 선언 및 초기화
	private static final ExecutorService executorService = Executors.newCachedThreadPool();
	@Autowired
	public void setChatService(ChatService chatService) {
		ChatServer.chatService = chatService;
	}
	@Autowired
	public void setGoodsService(GoodsService goodsService) {
		ChatServer.goodsService = goodsService;
	}
	@Autowired
	public void setAlertService(AlertService alertService) {
		ChatServer.alertService = alertService;
	}

			(...)
			
    public static class EndpointConfigurator extends ServerEndpointConfig.Configurator{
        @Override
        public void modifyHandshake(ServerEndpointConfig config, HandshakeRequest request, HandshakeResponse response) {
            HttpSession httpSession = (HttpSession) request.getHttpSession();
            if(httpSession != null) {
                config.getUserProperties().put(HttpSession.class.getName(), httpSession);
            }
        }
    }
	
			(...)
			
}
```
  
#### onOpen() 메서드
- 웹소켓 연결 시 설정정보에 저장된 HttpSession을 활용하여 로그인 세션 정보와 채팅방 정보에 대한 인증 로직을 수행
- 유효할 시 필요한 정보를 웹소켓 세션에 저장하고 유효하지 않으면 사용자 정의 오류 코드를 활용하여 에러 메세지를 반환
 ```java
     @OnOpen
    public void onOpen(Session session, EndpointConfig config, @PathParam("groupIdx") int groupIdx) throws IOException {
        log.info("onOpen");
        HttpSession httpSession = (HttpSession) config.getUserProperties().get(HttpSession.class.getName());
        if(httpSession != null) {
            String loginId = (String) httpSession.getAttribute("memberId");
            if(loginId != null) {
                session.getUserProperties().put("memberId", loginId);
                session.getUserProperties().put("groupIdx", groupIdx);
                log.info("접속아이디 : " + loginId);
            }else{
                log.info("접속한 세션이 유효하지 않습니다");
                this.errCode = "002";
                sendErrorMessage(session, this.errCode);
            }
        }else{
            log.info("세션이 없습니다.");
            this.errCode= "001";
            sendErrorMessage(session, this.errCode);
        }
    }
 ```
   
#### onMessage() 메서드
- 클라이언트로부터 메시지를 받아 executorService를 통해 메시지를 처리
```java
	@OnMessage
	public synchronized void onMessage(String message, Session session) throws IOException {
		log.info("onMessage: " + message);
		// 비동기 작업으로 메시지 처리
		executorService.submit(() -> {
			processMessage(message, session);
		});
	}
```


#### processMessage() 메서드
- 클라이언트로부터 전달받은 메시지를 처리하는 핵심 메서드
- 메시지 형식과 권한을 검증하고 메시지를 데이터베이스에 저장 및 다른 사용자에게 전달하는 역할 수행
- 메시지 형식 : ||으로 값을 구분
  - 클라이언트 -> 서버 : 받는회원아이디||메시지내용
  - 서버 -> 클라이언트 : 보내는회원아이디||메시지내용||보낸날짜시간||읽음여부
  - 시스템 메시지(예약, 채팅방 나가기)의 경우 회원 아이디에 system 입력
  - 에러 메시지의 경우 회원 아이디에 error 입력
   
```java
    private void processMessage(String message, Session session) {
		//권한검증, DB 접근시 사용할 정보 초기화
        Integer groupIdx = (Integer)session.getUserProperties().get("groupIdx");
        String sender = (String)session.getUserProperties().get("memberId");
        ChatGroupDTO groupDTO = chatService.getGroup(groupIdx);
		//groupIdx 에 해당하는 채팅방이 없을 경우
        if(groupDTO == null) {
            log.info("채팅방 없음");
            this.errCode = "003";
            sendErrorMessage(session, this.errCode);
            return;
        }
		//해당 채팅방 권한 없는 회원이 접근한 경우
        if(!groupDTO.getSeller().equals(sender)&&!groupDTO.getCustomer().equals(sender)) {
            log.info("채팅방접근권한없는사람");
            this.errCode = "004";
            sendErrorMessage(session, this.errCode);
            return;
        }
        //메시지 형식 receiver||message 에 위배된 경우
        if (message == null || !message.contains("\\|\\|")) {
            log.info("잘못된 메시지 형식: " + message);
            this.errCode = "005";
            sendErrorMessage(session, this.errCode);
            return;
        }
        String[] messageArr = message.split("\\|\\|");
		//메시지 형식에 위배된 경우
        if (messageArr.length < 2) {
            log.info("메시지 형식 오류: " + message);
            this.errCode = "005";
            sendErrorMessage(session, this.errCode);
            return;
        }
        String receiver = messageArr[0];
		//시스템 메시지 처리
        if(receiver.equals("system")) {
		
			(... 중략 ...)
		
		}
		//일반 메시지 처리
		//해당 채팅방 권한 없는 회원이 접근한 경우
        if(!groupDTO.getSeller().equals(receiver)&&!groupDTO.getCustomer().equals(receiver)) {
            log.info("채팅방접근권한없는사람");
            this.errCode = "004";
            sendErrorMessage(session, this.errCode);
            return;
        }
		//메시지 파싱
        String content = messageArr[1];
		//db에 메시지 등록
        int messageIdx = chatService.messageRegist(ChatMessageDTO.builder()
                .groupIdx(groupIdx)
                .senderId(sender)
                .content(content)
                .readChk(isUserSessionConnected(receiver,session)?"Y":"N")
                .build());
        ChatMessageDTO messageDTO = chatService.getMessage(messageIdx);
		//메시지 등록 성공시
        if(messageDTO != null){
            log.info("db등록성공");
			//알림 처리 로직
            alertService.regist(AlertDTO.builder()
                    .memberId(receiver)
                    .content(sender+" 님과의 채팅방에 새 메시지가 도착했습니다.")
                    .build());
			//채팅방 정보 최신화
            chatService.updateRecentDate(groupIdx);
			//메시지 전송
            session.getOpenSessions().forEach(s -> {
                if (receiver.equals(s.getUserProperties().get("memberId"))||sender.equals(s.getUserProperties().get("memberId"))) {
                    try {
                        s.getBasicRemote().sendText(messageDTO.getSenderId()
                                + "\\|\\|" + messageDTO.getContent()
                                + "\\|\\|" + messageDTO.getRegDateStr()+" "+messageDTO.getRegTimeStr()
                                + "\\|\\|" + messageDTO.getReadChk()
                        );
                    } catch (Exception e) {
                        log.info(e.getMessage());
                    }
                }
            });
        }else{//메시지 등록 실패시
            log.info("db등록실패");
            this.errCode = "006";
            sendErrorMessage(session, this.errCode);
        }
    }
```

#### isUserSessionConnected()메서드
- 특정 사용자의 세션이 서버에 접속해 있는지 확인하는 메서드
```java
    private boolean isUserSessionConnected(String memberId, Session session) {
        for (Session s : session.getOpenSessions()) {
            String sessionMemberId = (String) s.getUserProperties().get("memberId");
            log.info("openSessionMemberId : " + sessionMemberId);
            if (memberId.equals(sessionMemberId)) {
                return true; // 세션이 서버에 접속해 있음
            }
        }
        return false; // 세션이 서버에 접속해 있지 않음
    }
```

#### sendErrorMessage() 메서드
- 사용자 정의 코드에 따라 에러메시지를 전송하는 메서드
```java
    private void sendErrorMessage(Session session, String errCode) {
        String errorMessage;
        // errCode에 따른 메시지 설정
        switch (errCode) {
            case "001":
                errorMessage = "세션이 유효하지 않습니다.";
                break;
            case "002":
                errorMessage = "접속한 세션이 유효하지 않습니다";
                break;
            case "003":
                errorMessage = "해당 채팅방이 없습니다";
                break;
            case "004":
                errorMessage = "접근권한이 없습니다";
                break;
            case "005":
                errorMessage = "메세지 형식이 올바르지 않습니다(\\|\\| 포함하지 마세요)";
                break;
            case "006":
                errorMessage = "db 접근시 오류 발생";
                break;
            default:
                errorMessage = "일시적 오류가 발생했습니다.";
        }
        try {
            session.getBasicRemote().sendText("error||" + errorMessage);
        } catch (IOException e) {
            log.error("에러 메시지 전송 실패: " + e.getMessage());
        }
    }
```

#### onClose() 메서드
- 웹소켓 연결 종료 시 세션의 사용자 속성을 초기화하고 executorService를 닫음
```java
    @OnClose
    public void onClose(Session session) {
        log.info("웹소켓 종료 : " + (String)session.getUserProperties().get("memberId"));
        session.getUserProperties().clear();
		executorService.shutdown();
    }
```
#### onError() 메서드
- 웹소켓에서 에러 발생시 에러를 처리하는 메서드
```java
    @OnError
    public void onError(Session session, Throwable error) {
        log.info("웹소켓 에러발생");
        log.info("에러메세지 :" + error.getMessage());
    }
```