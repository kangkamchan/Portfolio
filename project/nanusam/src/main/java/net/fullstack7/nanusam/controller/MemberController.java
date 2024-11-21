package net.fullstack7.nanusam.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.fullstack7.nanusam.dto.MemberDTO;
import net.fullstack7.nanusam.dto.MemberModifyDTO;
import net.fullstack7.nanusam.service.MemberService;
import net.fullstack7.nanusam.util.JSFunc;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@Log4j2
@RequestMapping("/member")
public class MemberController {
    private final MemberService memberService;

    @GetMapping("/pwdCheck.do")
    public String pwdCheck(HttpSession session) {
        session.removeAttribute("isPwdChecked");
        return "myPage/pwdCheck";
    }

    @PostMapping("/pwdCheck.do")
    public String pwdCheck(@RequestParam String pwd, HttpSession session,  RedirectAttributes redirectAttributes) {
        String memberId = (String) session.getAttribute("memberId");
        boolean pwdCheck = memberService.pwdCheck(memberId, pwd);
        if (pwdCheck) {
            session.setAttribute("isPwdChecked", true);
            return "redirect:/member/view.do";
        } else {
            redirectAttributes.addFlashAttribute("errors", "비밀번호가 일치하지 않습니다.");
            return "redirect:/member/pwdCheck.do";
        }
    }

    @GetMapping("/view.do")
    public String viewGet(HttpSession session, Model model) {
        Boolean isPwdChecked = (Boolean) session.getAttribute("isPwdChecked");
        if (isPwdChecked == null || !isPwdChecked) {
            model.addAttribute("errors", "비밀번호 확인이 필요합니다.");
            return "forward:/member/pwdCheck.do";
        }
        session.removeAttribute("isPwdChecked");

        String memberId = (String) session.getAttribute("memberId");
        MemberDTO memberDTO = memberService.viewMember(memberId);
//        log.info("회원정보확인"+ memberDTO);
        if (memberDTO != null) {
            model.addAttribute("memberDTO", memberDTO);
        } else {
            model.addAttribute("errors", "회원 정보를 불러올 수 없습니다.");
            return "myPage/pwdCheck";
        }
        return "myPage/view";
    }

    @PostMapping("/modify.do")
    public String modifyPost(@Valid MemberModifyDTO memberModifyDTO
            , BindingResult bindingResult
            , RedirectAttributes redirectAttributes
            , HttpSession session
            , Model model
    ) {
//        log.info("회원수정 컨트롤러 시작");
        if (bindingResult.hasErrors()) {
//            log.info("hasErrors");
            session.setAttribute("isPwdChecked", true);
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
            redirectAttributes.addFlashAttribute("memberDTO", memberModifyDTO);
//            log.info("memberDTO: " + memberModifyDTO);
//            log.info("errors: " +bindingResult.getAllErrors());
            return "redirect:/member/view.do";
        }

//        // 생일로부터 나이를 계산
//        if (!memberModifyDTO.isEligibleAge()) {
//            redirectAttributes.addFlashAttribute("errors", "만 20세 이상만 가입 가능합니다.");
//            redirectAttributes.addFlashAttribute("memberDTO", memberModifyDTO);
//            return "redirect:/member/view.do";
//        }
        String sessionMemberId = (String) session.getAttribute("memberId");
        if (!memberModifyDTO.getMemberId().equals(sessionMemberId)) {
            session.setAttribute("isPwdChecked", true);
            redirectAttributes.addFlashAttribute("errors", "잘못된 접근입니다.");  // 알림 설정
            return "redirect:/member/view.do";
        }

        int result = memberService.modifyMember(memberModifyDTO);
        if (result > 0) {
//            log.info("회원수정성공"+memberModifyDTO);
            session.setAttribute("isPwdChecked", true);
            model.addAttribute("memberDTO", memberModifyDTO);
            return "redirect:/member/view.do";
        }else {
//            log.info("회원수정실패"+memberModifyDTO);
            redirectAttributes.addFlashAttribute("errors", "회원정보수정실패");
        }
        return "redirect:/member/view.do";
    }
    
    // D전환 여부
    @GetMapping("/checkGoodsStatusY.do")
    @ResponseBody
    public String checkGoodsStatusY(HttpSession session) {
        log.info("판매중상품갯수확인");
        String memberId = (String) session.getAttribute("memberId");
        boolean hasGoods = !memberService.goodsStatusY(memberId);
        JSONObject jsonResponse = new JSONObject();
        jsonResponse.put("hasGoods", hasGoods);
        return jsonResponse.toString();
    }

    //탈퇴
    @PostMapping("/delete.do")
    public String delete(@RequestParam String memberId, RedirectAttributes redirectAttributes, HttpSession session) {
        
        log.info("탈퇴컨트롤러시작");
        if (memberService.dontDelete(memberId)) {
            log.info("dontDelete실패");
            redirectAttributes.addFlashAttribute("errors", "탈퇴가 불가합니다. 현재 예약 중이거나 배송 중인 상품이 있습니다.");
            return "redirect:/member/view.do";

        }
//        log.info("dontDelete성공");
        memberService.goDelete(memberId);
        redirectAttributes.addFlashAttribute("errors", "탈퇴가 완료되었습니다.");
        session.invalidate();

        return "redirect:/";
    }

}
