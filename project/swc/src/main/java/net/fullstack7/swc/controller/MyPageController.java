package net.fullstack7.swc.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.fullstack7.swc.domain.Message;
import net.fullstack7.swc.dto.*;
import net.fullstack7.swc.service.MemberServiceIf;
import net.fullstack7.swc.service.MessageService;
import net.fullstack7.swc.util.ErrorUtil;
import net.fullstack7.swc.util.LogUtil;
import org.springframework.beans.factory.annotation.Autowired;
import net.fullstack7.swc.domain.Member;
import net.fullstack7.swc.service.AlertServiceIf;
import net.fullstack7.swc.service.FriendServiceIf;
import net.fullstack7.swc.service.MemberServiceIf;
import net.fullstack7.swc.util.CookieUtil;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import net.fullstack7.swc.config.JwtTokenProvider;
import net.fullstack7.swc.service.MemberServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.List;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.Map;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

@Log4j2
@Controller
@RequestMapping("/myPage")
@RequiredArgsConstructor
public class MyPageController {
  private final FriendServiceIf friendService;
  private final CookieUtil cookieUtil;
  private final MemberServiceImpl memberService;
  private final AlertServiceIf alertService;
  private final MessageService messageService;
  private final ErrorUtil errorUtil;

  private String getMemberIdInJwt(HttpServletRequest req) {
    String accessToken = cookieUtil.getCookieValue(req,"accessToken");
    return memberService.getMemberInfo(accessToken).get("memberId");
  }

  @GetMapping("/info")
  public String myPage(Model model, HttpServletRequest req) {
    try {
      String memberId = getMemberIdInJwt(req);

      Member member = memberService.getMemberById(memberId);

      String name = member.getName() != null ? member.getName() : "비공개";
      String email = member.getEmail() != null ? member.getEmail() : "비공개";
      String phone = member.getPhone() != null ? member.getPhone() : "비공개";
      String myInfo = member.getMyInfo() != null ? member.getMyInfo() : "비공개";

      model.addAttribute("name", name);
      model.addAttribute("email", email);
      model.addAttribute("phone", phone);
      model.addAttribute("myInfo", myInfo);

      DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd a h시 mm분");
      String lastLoginFormatted = member.getLastLoginAt() != null ? member.getLastLoginAt().format(formatter) : "없음";
      String updatedAtFormatted = member.getUpdatedAt() != null ? member.getUpdatedAt().format(formatter) : "없음";

      model.addAttribute("lastLogin", lastLoginFormatted);
      model.addAttribute("updatedAt", updatedAtFormatted);

      return "myPage/myPageInfo";
    } catch (Exception e) {
      model.addAttribute("error", e.getMessage());
      return "error";
    }
  }

  @GetMapping("/followList")
  public String myPageFollowList(
          @Valid PageDTO<FriendListDTO> pageDTO,
          BindingResult bindingResult,
          RedirectAttributes redirectAttributes,
          Model model,
          HttpServletRequest request
  ) {

    LogUtil.logLine("MypageController friendRequestList");
    if(bindingResult.hasErrors()) {
      return errorUtil.redirectWithError("/post/main",redirectAttributes,bindingResult);
    }
    try {
      String memberId = getMemberIdInJwt(request);
      LogUtil.log("memberId",memberId);
      pageDTO.setPageSize(8);
      pageDTO.initialize("regDate","desc");
      pageDTO.setTotalCount(friendService.getRequestTotalCount(pageDTO,memberId));
      pageDTO = friendService.getFriendRequestList(pageDTO,memberId);
      model.addAttribute("pageDTO", pageDTO);
      model.addAttribute("memberDTO", MemberDTO.builder().memberId(memberId).build());
      return "myPage/myPageFollowList";
    }catch(Exception e) {
      return errorUtil.redirectWithError(e.getMessage(), "/post/main", redirectAttributes);
    }
  }

  @GetMapping("/friend")
  public String myPageFriend(
          @Valid PageDTO<FriendListDTO> pageDTO,
          BindingResult bindingResult,
          RedirectAttributes redirectAttributes,
          Model model,
          HttpServletRequest request
          ) {
    LogUtil.logLine("MypageController friendList");
    if(bindingResult.hasErrors()) {
      return errorUtil.redirectWithError("/post/main",redirectAttributes,bindingResult);
    }
    try {
      String memberId = getMemberIdInJwt(request);
      LogUtil.log("memberId",memberId);
      pageDTO.setPageSize(8);
      pageDTO.initialize("regDate","desc");
      pageDTO.setTotalCount(friendService.getTotalCount(pageDTO,memberId));
      pageDTO = friendService.getFriendList(pageDTO,memberId);
      model.addAttribute("pageDTO", pageDTO);
      model.addAttribute("memberDTO", MemberDTO.builder().memberId(memberId).build());
      return "myPage/myPageFriend";
    }catch(Exception e) {
      return errorUtil.redirectWithError(e.getMessage(), "/post/main", redirectAttributes);
    }
  }

  @GetMapping("/myPageMsg")
  public String myPageMsg() {
    return "myPage/myPageMsg";
  }

  @GetMapping("/myPageSendMsg")
  public String myPageSendMsg(Model model, HttpServletRequest req) {
    String memberId = getMemberIdInJwt(req);

    if (memberId == null) {
      return "redirect:/sign/signIn";
    }
    List<Message> messageList = messageService.getSenderMessageList(memberId);
    model.addAttribute("messages", messageList);

    return "myPage/myPageSendMsg";
  }

  @GetMapping("/message")
  public String myPageMessage() {
    return "myPage/myPageMsg";
  }

  @PostMapping("/update-name")
  public ResponseEntity<Map<String, Object>> updateName(@RequestBody Map<String, String> request,
                                                        HttpServletRequest req) {
    String memberId = getMemberIdInJwt(req);
    memberService.updateName(memberId, request.get("name"));
    return ResponseEntity.ok(Map.of("success", true));
  }

  @PostMapping("/update-email")
  public ResponseEntity<Map<String, Object>> updateEmail(@RequestBody Map<String, String> request, 
  HttpServletRequest req) {
    String memberId = getMemberIdInJwt(req);
    memberService.updateEmail(memberId, request.get("email"));
    return ResponseEntity.ok(Map.of("success", true));
  }

  @PostMapping("/update-phone")
  public ResponseEntity<Map<String, Object>> updatePhone(@RequestBody Map<String, String> request, 
  HttpServletRequest req) {
    String memberId = getMemberIdInJwt(req);
    memberService.updatePhone(memberId, request.get("phone"));
    return ResponseEntity.ok(Map.of("success", true));
  }

  @PostMapping("/update-myInfo")
  public ResponseEntity<Map<String, Object>> updateMyInfo(@RequestBody Map<String, String> request,
  HttpServletRequest req) {
    String memberId = getMemberIdInJwt(req);
    memberService.updateMyInfo(memberId, request.get("myInfo"));
    return ResponseEntity.ok(Map.of("success", true));
  }

  @PostMapping("/delete")
  public ResponseEntity<Map<String, Object>> deleteMember(HttpServletRequest req) {
    String memberId = getMemberIdInJwt(req);
    memberService.deleteMember(memberId);
    return ResponseEntity.ok(Map.of("success", true));
  }

  @PostMapping("/update-profile-image")
  public ResponseEntity<Map<String, Object>> updateProfileImage(
          @RequestParam("file") MultipartFile file,
          HttpServletRequest req) {
      try {
          String memberId = getMemberIdInJwt(req);
          memberService.updateProfileImage(memberId, file);
          return ResponseEntity.ok(Map.of("success", true));
      } catch (Exception e) {
          log.error("프로필 사진 업데이트 실패: {}", e.getMessage());
          return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
      }
  }
}
