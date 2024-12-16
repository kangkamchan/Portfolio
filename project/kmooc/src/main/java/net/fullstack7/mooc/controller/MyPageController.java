package net.fullstack7.mooc.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.fullstack7.mooc.domain.Course;
import net.fullstack7.mooc.domain.Member;
import net.fullstack7.mooc.dto.CourseSearchDTO;
import net.fullstack7.mooc.dto.MemberDTO;
import net.fullstack7.mooc.dto.MemberModifyDTO;
import net.fullstack7.mooc.dto.ModifyCreditDTO;
import net.fullstack7.mooc.service.CourseService;
import net.fullstack7.mooc.service.MemberServiceIf;
import net.fullstack7.mooc.service.MemberServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.Map;

@Controller
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/mypage")
public class MyPageController {
    private final MemberServiceIf memberServiceImpl;
    private final CourseService courseService;

    //마이클래스 이동
    @GetMapping("/myclass")
    public String mypage(Model model, HttpSession session, @Valid CourseSearchDTO searchDTO, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if(bindingResult.hasErrors()) {
            searchDTO = CourseSearchDTO.builder().build();
        }

        MemberDTO memberDTO = (MemberDTO) session.getAttribute("memberDTO");

        if (memberDTO == null) {
            redirectAttributes.addFlashAttribute("errors", "로그인 후 이용");
            return "redirect:/login/login";
        }
        model.addAttribute("member", memberDTO);

        searchDTO.initialize();
        model.addAttribute("courselist", memberServiceImpl.getCourses(searchDTO, memberDTO.getMemberId(), 0));
        model.addAttribute("pageDTO", searchDTO);
        model.addAttribute("notcompleted", memberServiceImpl.getMyCourseCount(memberDTO.getMemberId(), 0));
        model.addAttribute("completed", memberServiceImpl.getMyCourseCount(memberDTO.getMemberId(), 1));

        return "mypage/myclass";
    }

    @GetMapping("/creditclass")
    public String creditclass(Model model, HttpSession session, @Valid CourseSearchDTO searchDTO, BindingResult bindingResult, RedirectAttributes redirectAttributes) {

        if(session.getAttribute("memberDTO") == null) {
            redirectAttributes.addFlashAttribute("errors", "로그인 후 이용");
            return "redirect:/login/login";
        }

        if(((MemberDTO) session.getAttribute("memberDTO")).getMemberType() == 0) {
            return "redirect:/mypage/creditTransform";
        }

        if(bindingResult.hasErrors()) {
            searchDTO = CourseSearchDTO.builder().build();
        }
        searchDTO.setIsCreditBank(1);
        String memberId = ((MemberDTO) session.getAttribute("memberDTO")).getMemberId();
        searchDTO.initialize();

        model.addAttribute("courselist", memberServiceImpl.getCourses(searchDTO, memberId, 0));
//        model.addAttribute("pageInfo", searchDTO);
        return "mypage/creditclass";
    }


    @GetMapping("/creditTransform")
    public String creditTransform(HttpSession session, RedirectAttributes redirectAttributes) {
        if(session.getAttribute("memberDTO") == null) {
            redirectAttributes.addFlashAttribute("errors", "로그인 후 이용");
            return "redirect:/login/login";
        }

        if(((MemberDTO)session.getAttribute("memberDTO")).getMemberType() == 1) {
            return "redirect:/mypage/creditclass";
        }
        return "mypage/creditTransform" ;
    }
    @GetMapping("/creditTransform2")
    public String creditTransform2(HttpSession session, RedirectAttributes redirectAttributes, Model model) {
        if(session.getAttribute("memberDTO") == null) {
            redirectAttributes.addFlashAttribute("errors", "로그인 후 이용");
            return "redirect:/login/login";
        }
        if(((MemberDTO)session.getAttribute("memberDTO")).getMemberType() == 1) {
            return "redirect:/mypage/creditclass";
        }

        model.addAttribute("memberInfo", session.getAttribute("memberDTO"));

        return "mypage/creditTransform2" ;
    }

    @PostMapping("/creditTransform2")
    public String creditTransform2Post(HttpSession session
           , @Valid ModifyCreditDTO modifyDTO, BindingResult bindingResult, RedirectAttributes redirectAttributes) {

        MemberDTO member = (MemberDTO)session.getAttribute("memberDTO");

        if(session.getAttribute("memberDTO") == null) {
            redirectAttributes.addFlashAttribute("errors", "로그인 후 이용");
            return "redirect:/login/login";
        }

        if(member.getMemberType() == 1) {
            return "redirect:/mypage/creditclass";
        }

        if(bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("item", modifyDTO);
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors().get(0).getDefaultMessage());
            return "redirect:/mypage/creditTransform2";
        }

        String msg = memberServiceImpl.modifyToCredit(member.getMemberId());

        if(msg == null) {
            member.setMemberType(1);
            session.setAttribute("memberDTO", member);
            msg = "학점은행제 회원으로 전환 완료";
        }

        redirectAttributes.addFlashAttribute("errors", msg);

        return "redirect:/mypage/creditclass";
    }

//    @GetMapping("/creditTransform3")
//    public String creditTransform3(HttpSession session, RedirectAttributes redirectAttributes) {
//        if(session.getAttribute("memberDTO") == null) {
//            redirectAttributes.addFlashAttribute("errors", "로그인 후 이용");
//            return "redirect:/login/login";
//        }
//        if(((MemberDTO)session.getAttribute("memberDTO")).getMemberType() == 1) {
//            return "redirect:/mypage/creditclass";
//        }
//        return "mypage/creditTransform3" ;
//    }

    //회원 조회
    @GetMapping("/memberView")
    public String memberView(Model model, HttpSession session) {
        MemberDTO memberDTO = (MemberDTO) session.getAttribute("memberDTO");
        if(memberDTO != null) {
            model.addAttribute("member", memberDTO);
        } else {
            return "redirect:/login/login";
        }
        return "mypage/memberView" ;
    }

    @PostMapping("/memberModify")
    public String modifyMember(@Valid MemberDTO memberDTO, BindingResult bindingResult,
                               RedirectAttributes redirectAttributes, HttpSession session, Model model) {
//        System.out.println("POST 요청 받음");
        MemberDTO loginedMemberInfo = (MemberDTO)session.getAttribute("memberDTO");
        if (loginedMemberInfo==null){
            redirectAttributes.addFlashAttribute("errors", "로그인 후 이용");
            return "redirect:/login/login";
        }
        if (bindingResult.hasErrors()) {
            model.addAttribute("fielderror", bindingResult.getAllErrors());
            model.addAttribute("member", memberDTO);
            return "mypage/memberView";
        }

        try {
            if (memberDTO != null) {
                memberServiceImpl.modifyMember(memberDTO);
            }
            memberDTO.setMemberId(loginedMemberInfo.getMemberId());

            MemberDTO originDto = (MemberDTO) session.getAttribute("memberDTO");

            if(memberDTO.getPassword() != null)
                originDto.setPassword(memberDTO.getPassword());
            if(memberDTO.getEmail() != null)
                originDto.setEmail(memberDTO.getEmail());

            session.setAttribute("memberDTO", originDto);
            
            redirectAttributes.addFlashAttribute("errors", "회원 정보가 수정되었습니다.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errors", "회원 정보 수정에 실패했습니다.");
            return "redirect:/mypage/memberView";
        }
        return "redirect:/mypage/myclass";
    }

    //비밀번호 변경
    @GetMapping("/pwdCheck")
    public String pwdCheck(HttpSession session) {
        if (session.getAttribute("memberDTO") == null) {
            return "redirect:/login/login";
        }
        return "mypage/pwdCheck" ;
    }

    @PostMapping("/pwdCheck")
    public String pwdCheck(@RequestParam("current-password")String currentPassword, @RequestParam("new-password")String newPassword, @RequestParam("confirm-password")String confirmPassword, HttpSession session, RedirectAttributes redirectAttributes) {
        MemberDTO memberDTO = (MemberDTO) session.getAttribute("memberDTO");
        if(memberDTO == null) {
            return "redirect:/login/login";
        }
//        System.out.println("Session memberDTO: " + memberDTO);
//        System.out.println("Current Password: " + currentPassword);
//        System.out.println("New Password: " + newPassword);
//        System.out.println("Confirm Password: " + confirmPassword);

        boolean isCurrentPasswordValid = memberServiceImpl.pwdCheck(memberDTO.getMemberId(), currentPassword);
//        System.out.println("Is Current Password Valid: " + isCurrentPasswordValid);
        if(!isCurrentPasswordValid) {
            redirectAttributes.addFlashAttribute("errorMessage", "현재 비밀번호가 일치하지 않습니다.");
            return "redirect:/mypage/pwdCheck";
        }
        if(!newPassword.equals(confirmPassword)) {
            redirectAttributes.addFlashAttribute("errorMessage", "새 비밀번호 확인이 일치하지 않습니다.");
            return "redirect:/mypage/pwdCheck";
        }

        if(!newPassword.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*(),.?\":{}|<>])[A-Za-z\\d!@#$%^&*(),.?\":{}|<>]{10,20}$")){
            redirectAttributes.addFlashAttribute("errorMessage", "비밀번호는 대문자, 소문자, 숫자, 특수문자를 포함하여 10~20자로 입력해주세요.");
            return "redirect:/mypage/pwdCheck";
        }
        try{
            memberServiceImpl.updatePassword(memberDTO.getMemberId(), newPassword);
//            System.out.println("Password updated successfully.");
            redirectAttributes.addFlashAttribute("successMessage", "비밀번호가 변경되었습니다.");
            return "redirect:/mypage/myclass";
        } catch (Exception e) {
//            System.out.println("Error occurred while updating password: " + e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage", "비밀번호 변경에 실패했습니다.");
            return "redirect:/mypage/pwdCheck";
        }
    }



    //회원 탈퇴
    @PostMapping("/memberDelete")
    @ResponseBody
    public Map<String, Object> deleteMember(@RequestParam String memberId, HttpSession session) {
//        log.info("회원 탈퇴 요청, memberId: {}", memberId);
        Map<String, Object> response = new HashMap<>();
        try {
            memberServiceImpl.deleteMember(memberId);
            session.invalidate();
            response.put("success", true);
            response.put("message", "탈퇴가 완료되었습니다.");
//            return ResponseEntity.ok(response);  // 성공 응답
        } catch (Exception e) {
//            log.error("회원 탈퇴 처리 중 오류 발생, memberId: {}", memberId, e);
            response.put("success", false);
            response.put("message", "회원 탈퇴 처리 중 오류가 발생했습니다.");
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);  // 실패 응답
        }
        return response;
    }

}
