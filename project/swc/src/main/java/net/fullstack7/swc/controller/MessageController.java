package net.fullstack7.swc.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.fullstack7.swc.domain.AlertType;
import net.fullstack7.swc.domain.Member;
import net.fullstack7.swc.domain.Message;
import net.fullstack7.swc.dto.MessageDTO;
import net.fullstack7.swc.dto.PageDTO;
import net.fullstack7.swc.repository.MemberRepository;
import net.fullstack7.swc.repository.MessageRepository;
import net.fullstack7.swc.service.AlertServiceImpl;
import net.fullstack7.swc.service.MemberServiceIf;
import net.fullstack7.swc.service.MessageService;
import net.fullstack7.swc.util.CookieUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.awt.print.Pageable;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/message")
@RequiredArgsConstructor
@Log4j2
public class MessageController {
    private final MessageService messageService;
    private final MemberServiceIf memberService;
    private final CookieUtil cookieUtil;
    private final MessageRepository messageRepository;
    private final AlertServiceImpl alertService;
    private final MemberRepository memberRepository;

    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    public String handleGetRequest() {
        return "redirect:/message/list";
    }

    private String getMemberIdInJwt(HttpServletRequest req) {
        String accessToken = cookieUtil.getCookieValue(req, "accessToken");
        if (accessToken == null || accessToken.isEmpty()) {
            return null;
        }

        try {
            return memberService.getMemberInfo(accessToken).get("memberId");
        } catch (Exception e) {
            return null;
        }
    }
    //받은쪽지목록
    @GetMapping("/list")
    public String messageList(Model model, HttpServletRequest req,
                              @RequestParam(defaultValue = "1") int page,
                              @RequestParam(defaultValue = "10") int size,
                              @Valid PageDTO<MessageDTO> pageDTO, BindingResult bindingResult,
                              RedirectAttributes redirectAttributes) {
        if(bindingResult.hasErrors()) {
            pageDTO = PageDTO.<MessageDTO>builder().build();
        }
        String memberId = getMemberIdInJwt(req);
        if (memberId == null) {
            return "redirect:/sign/signIn";
        }
        if (page < 1) {
            page = 1;
        }
        pageDTO.setPageNo(page);
        pageDTO.setPageSize(size);
        pageDTO.initialize("regDate", "desc");

        int totalCount = messageService.getReceiverMessageCount(memberId);
//        log.info("메시지총개수: {}", totalCount);
        pageDTO.setTotalCount(totalCount);

        List<MessageDTO> messageList = messageService.getReceiverMessageList(memberId, pageDTO.getSortPageable());
//        log.info("리스트사이즈: {}", messageList.size());
        pageDTO.setDtoList(messageList);

        // 총 페이지 수 계산
        int totalPages = (int) Math.ceil((double) totalCount / pageDTO.getPageSize());
        model.addAttribute("totalPages", pageDTO.getTotalPage());
        model.addAttribute("currentPage", pageDTO.getPageNo() - 1);
        model.addAttribute("size", pageDTO.getPageSize());
        model.addAttribute("messages", messageList);
        model.addAttribute("pageDTO", pageDTO);

        return "message/list";
    }

    // 쪽지 작성
    @GetMapping("/regist")
    public String showRegistForm(@RequestParam(required = false) String receiverId, Model model, HttpServletRequest req) {
        String senderId = getMemberIdInJwt(req);
//        log.info("senderId" + senderId);
        if (senderId == null) {
            return "redirect:/sign/signIn";
        }
        if(receiverId == null || receiverId.isEmpty()) {
            receiverId = "";
        }

        model.addAttribute("senderId", senderId);
        model.addAttribute("receiverId", receiverId);
        return "message/regist";
    }

    //쪽지 등록
    @PostMapping("/regist")
    public String registMessage(@RequestParam String receiverId, @RequestParam String content, @RequestParam String title, @RequestParam LocalDateTime regDate, HttpServletRequest req, Model model, RedirectAttributes redirectAttributes) {
        String senderId = getMemberIdInJwt(req);
        if (senderId == null) {
            return "redirect:/sign/signIn";
        }
        try {
            if (senderId.equals(receiverId)) {
                redirectAttributes.addFlashAttribute("error", "자기 자신에게 메시지를 보낼 수 없습니다.");
                return "redirect:/post/main";
            }

            Member receiver = memberRepository.findByMemberId(receiverId);
            if (receiver == null) {
                redirectAttributes.addFlashAttribute("error", "존재하지 않는 수신자입니다.");
                return "redirect:/message/send/list";
            }

            messageService.sendMessage(senderId, receiverId, content, title, regDate);

            String senderName = memberService.getMemberNameById(senderId);
            if (senderName == null) {
                redirectAttributes.addFlashAttribute("error", "발신자 이름을 찾을 수 없습니다.");
                return "redirect:/message/send/list";
            }

            String alertMessage = senderName + "님이 새 쪽지를 보냈습니다: '" + title + "'";

            Member member = memberRepository.findByMemberId(receiverId);
            if (member == null || senderId.equals(member.getMemberId())) {
                redirectAttributes.addFlashAttribute("error", "존재하지 않는 회원입니다.");
                return "redirect:/message/send/list";
            }

            alertService.registAlert(member, AlertType.CHAT_MESSAGE, alertMessage, "/message/list");
            return "redirect:/message/send/list";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorReceiverId", true);
            redirectAttributes.addFlashAttribute("errorMessage", "잘못된 ID입니다.");
            return "redirect:/message/send/list";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", true);
            redirectAttributes.addFlashAttribute("errorMessage", "알 수 없는 오류가 발생했습니다.");
            return "redirect:/message/send/list";
        }
    }


    //삭제
    @PostMapping("/delete")
    public String messageDelete(@RequestParam(value = "messageIds", required = false) List<Long> messageIds, HttpServletRequest req) {
        String memberId = getMemberIdInJwt(req);
        if (memberId == null) {
            return "redirect:/sign/signIn";
        }
        if(messageIds == null || messageIds.isEmpty()){
            return "redirect:/message/list";
        }
        messageService.deleteMessages(messageIds);
        return "redirect:/message/list";
    }

    //안읽음처리
    @PostMapping("/markAsUnRead")
    @Transactional
    public String markAsUnRead(@RequestParam Long messageId){
        Message message = messageService.getMessageById(messageId);
        if(message.isRead()){
            message.setRead(false);
            messageRepository.save(message);
        }
        Message updatedMessage = messageRepository.findById(messageId).orElseThrow(() -> new IllegalArgumentException("Message not found"));
        return "redirect:/message/list";
    }

    //상세(누르면 읽음처리까지)
    @GetMapping("/view")
    public String viewMessage(@RequestParam(required = false) Long messageId, Model model, HttpServletRequest req) {
        String memberId = getMemberIdInJwt(req);
        if (memberId == null) {
            return "redirect:/sign/signIn";
        }
        if(messageId == null) {
            model.addAttribute("errorMessage", "잘못된 접근입니다.");
            return "message/send/list";
        }
        Message message = messageService.getMessageById(messageId);
        if(message == null){
            model.addAttribute("errorMessage", "쪽지를 찾을 수 없습니다.");
            return "message/list";
        }
        if(!message.isRead()){
            message.setRead(true);
            messageRepository.save(message);
        }
        model.addAttribute("message", message);
        return "message/view";
    }

}
