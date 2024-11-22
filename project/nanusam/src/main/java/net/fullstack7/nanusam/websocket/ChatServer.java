package net.fullstack7.nanusam.websocket;

import lombok.extern.log4j.Log4j2;
import net.fullstack7.nanusam.dto.AlertDTO;
import net.fullstack7.nanusam.dto.ChatGroupDTO;
import net.fullstack7.nanusam.dto.ChatMessageDTO;
import net.fullstack7.nanusam.dto.GoodsDTO;
import net.fullstack7.nanusam.service.AlertService;
import net.fullstack7.nanusam.service.ChatService;
import net.fullstack7.nanusam.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import javax.servlet.http.HttpSession;
import javax.websocket.*;
import javax.websocket.server.HandshakeRequest;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import javax.websocket.server.ServerEndpointConfig;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
@ServerEndpoint(value = "/chat/{groupIdx}", configurator = ChatServer.EndpointConfigurator.class)
@Log4j2
public class ChatServer {
    private static ChatService chatService;// 정적 필드로 ChatService를 선언
    private static GoodsService goodsService;
    private static AlertService alertService;
    //세션없음 001, 로그인아이디가없음 002, 채팅방없음 003, 접근권한없음 004, 메시지 형식오류 005, db등록실패 006
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
    @OnMessage
    public synchronized void onMessage(String message, Session session) throws IOException {
        log.info("onMessage: " + message);
        // 비동기 작업으로 메시지 처리
        executorService.submit(() -> {
            processMessage(message, session);
        });
    }
    @OnClose
    public void onClose(Session session) {
        log.info("웹소켓 종료 : " + (String)session.getUserProperties().get("memberId"));
        session.getUserProperties().clear();
    }
    @OnError
    public void onError(Session session, Throwable error) {
        log.info("웹소켓 에러발생");
        log.info("에러메세지 :" + error.getMessage());
    }

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
			//예약 메시지 처리 system||reservation||예약메시지||예약자
            if (messageArr[1].equals("reservation")) {
				//메시지 파싱
                String content = messageArr[2];
                String reservationMemberId = messageArr[3];
                int goodsIdx = groupDTO.getGoodsIdx();
                String customer = groupDTO.getCustomer();
                GoodsDTO goodsDTO = goodsService.view(goodsIdx);
				//상품정보가 없을 경우
                if(goodsDTO == null) {
                    log.info("상품정보조회실패");
                    this.errCode = "006";
                    sendErrorMessage(session, this.errCode);
                    return;
                }
				//예약 처리 로직
                goodsDTO.setIdx(goodsIdx);
                goodsDTO.setStatus("R");
                goodsDTO.setReservationId(reservationMemberId);
                int reservationResult = goodsService.modifyStatus(goodsDTO);
				//예약 처리 실패 경우
                if(reservationResult <= 0) {
                    log.info("예약정보변경실패");
                    this.errCode = "006";
                    sendErrorMessage(session, this.errCode);
                    return;
                }
				//알림 처리 로직
                alertService.regist(AlertDTO.builder()
                        .memberId(customer)
                        .content(goodsDTO.getName()+" 상품의 예약이 확정되었습니다.")
                        .build());
				//db에 메시지 정보 등록
                int messageIdx = chatService.messageRegist(ChatMessageDTO.builder()
                        .groupIdx(groupIdx)
                        .senderId("system")
                        .content(content)
                        .readChk(isUserSessionConnected(reservationMemberId,session)?"Y":"N")
                        .build());
                ChatMessageDTO messageDTO = chatService.getMessage(messageIdx);
				//메시지 등록 실패시
                if(messageDTO==null){
				    this.errCode = "006";
                    sendErrorMessage(session, this.errCode);
                    return;
                }
				//채팅방 날짜정보 최신화
                chatService.updateRecentDate(groupIdx);
				//서버 -> 클라이언트 메시지 전송
                session.getOpenSessions().forEach(s -> {
                    if (customer.equals(s.getUserProperties().get("memberId")) || sender.equals(s.getUserProperties().get("memberId"))) {
                        try {
                            s.getBasicRemote().sendText(
                                    "system"
                                    + "\\|\\|" + messageDTO.getContent()
                                    + "\\|\\|" + messageDTO.getRegDateStr()+" "+messageDTO.getRegTimeStr()
                                    + "\\|\\|" + "예약번호"
                            );
                        } catch (Exception e) {
                            log.info(e.getMessage());
                        }
                    }
                });
                return;
            }else if(messageArr[1].equals("disconnect")) {//상대가 채팅방을 나갔을 때
                log.info("disconnect");
				//메시지 파싱
                String content = messageArr[2];
                String receiverId = messageArr[3];
				//메시지 DB에 등록
                int messageIdx = chatService.messageRegist(ChatMessageDTO.builder()
                        .groupIdx(groupIdx)
                        .senderId("system")
                        .content(content)
                        .readChk(isUserSessionConnected(receiverId,session)?"Y":"N")
                        .build());
                ChatMessageDTO messageDTO = chatService.getMessage(messageIdx);
				//메시지 등록 실패시
                if(messageDTO==null){
                    log.info("시스템메시지 등록 실패");
                    return;
                }
				//알림 처리 로직
                alertService.regist(AlertDTO.builder()
                        .memberId(receiverId)
                        .content(sender+" 님이 채팅방을 나갔습니다. 해당 채팅방은 사라집니다.")
                        .build());
				//채팅방 정보 최신화
                chatService.updateRecentDate(groupIdx);
				//메시지 전송
                session.getOpenSessions().forEach(s -> {
                    if (receiverId.equals(s.getUserProperties().get("memberId")) || sender.equals(s.getUserProperties().get("memberId"))) {
                        try {
                            s.getBasicRemote().sendText(
                                    "system"
                                    + "\\|\\|" + messageDTO.getContent()
                                    + "\\|\\|" + messageDTO.getRegDateStr()+" "+messageDTO.getRegTimeStr()
                                    + "\\|\\|disconnectFinished"
                            );
                        } catch (Exception e) {
                            log.info(e.getMessage());
                        }
                    }
                });
                return;
            }
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

    // 에러 메시지 전송 메서드
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

    public static class EndpointConfigurator extends ServerEndpointConfig.Configurator{
        @Override
        public void modifyHandshake(ServerEndpointConfig config, HandshakeRequest request, HandshakeResponse response) {
            HttpSession httpSession = (HttpSession) request.getHttpSession();
            if(httpSession != null) {
                config.getUserProperties().put(HttpSession.class.getName(), httpSession);
            }
        }
    }

    /**
     * 특정 사용자의 세션이 서버에 접속해 있는지 확인하는 메서드
     *
     * @param memberId 확인하려는 사용자의 ID
     * @return 접속해 있으면 true, 접속하지 않으면 false
     */
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
}
