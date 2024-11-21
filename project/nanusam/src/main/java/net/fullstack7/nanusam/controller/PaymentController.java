package net.fullstack7.nanusam.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.fullstack7.nanusam.dto.AlertDTO;
import net.fullstack7.nanusam.dto.GoodsDTO;
import net.fullstack7.nanusam.dto.PageRequestDTO;
import net.fullstack7.nanusam.dto.PaymentDTO;
import net.fullstack7.nanusam.service.AlertService;
import net.fullstack7.nanusam.service.GoodsService;
import net.fullstack7.nanusam.service.PaymentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
@Log4j2
@RequestMapping("/payment")
public class PaymentController {
    private final PaymentService paymentService;
    private final GoodsService goodsService;
    private final AlertService alertService;
    @GetMapping("list.do")
    public String list(HttpSession session, Model model, @Valid PageRequestDTO pageRequestDTO) {

        pageRequestDTO.setReservationId(session.getAttribute("memberId").toString());
        model.addAttribute("pageinfo", paymentService.listWithGoodsByBuyer(pageRequestDTO));

        return "payment/list";
    }

    @GetMapping("/regist.do")
    public String registGet(@RequestParam(defaultValue = "0") int goodsIdx, HttpSession session, RedirectAttributes redirectAttributes
            , HttpServletRequest request, Model model) {

        if (goodsIdx == 0) {
            redirectAttributes.addFlashAttribute("errors", "결제할 상품을 선택해주세요.");
            return "redirect:/goods/list.do";
        }

        GoodsDTO item = goodsService.view(goodsIdx);

        if (item == null) {
            redirectAttributes.addFlashAttribute("errors", "등록되지 않은 상품입니다.");
            return "redirect:/goods/list.do";
        }

        if(item.getMemberId().equals(session.getAttribute("memberId").toString())) {
            redirectAttributes.addFlashAttribute("errors", "내가 등록한 상품입니다.");
            return "redirect:/goods/list.do";
        }

        if(item.getStatus().equals("R") && !item.getReservationId().equals(session.getAttribute("memberId").toString())) {
            redirectAttributes.addFlashAttribute("errors", "예약된 상품입니다.");
            return "redirect:/goods/list.do";
        }

        model.addAttribute("goodsInfo", item);

        return "payment/regist";
    }

    @PostMapping("/regist.do")
    public String registPost(HttpSession session, @RequestParam int goodsIdx
            , @Valid PaymentDTO paymentDTO, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {

        log.info("goodsIdx = " + goodsIdx);
        paymentDTO.setGoodsIdx(goodsIdx);

        if (goodsIdx == 0) {
            redirectAttributes.addFlashAttribute("errors", "결제할 상품을 선택해주세요.");
            return "redirect:/goods/list.do";
        }

        if (bindingResult.hasErrors()) {
            log.info("bindingResult has errors");
            redirectAttributes.addFlashAttribute("item", paymentDTO);
            redirectAttributes.addFlashAttribute("formerrors", bindingResult.getAllErrors());
            return "redirect:/payment/regist.do?goodsIdx=" + goodsIdx;
        }

        paymentDTO.setBuyer(session.getAttribute("memberId").toString());
        String message = paymentService.regist(paymentDTO);

        if (message != null) {
            redirectAttributes.addFlashAttribute("message", message);
            redirectAttributes.addFlashAttribute("item", paymentDTO);
            return "redirect:/payment/regist.do?goodsIdx=" + goodsIdx;
        }

        alertService.regist(AlertDTO.builder()
                        .memberId(paymentDTO.getSeller())
                        .content(paymentDTO.getGoodsInfo().getName()+"상품이 결제되었습니다.")
                .build());
        return "redirect:/payment/list.do";
    }

    @GetMapping("/view.do")
    public String viewGet(@RequestParam(defaultValue = "0") int idx, RedirectAttributes redirectAttributes, HttpSession session, Model model, HttpServletRequest request) {

        if (idx == 0) {
            redirectAttributes.addFlashAttribute("errors", "주문번호를 선택해주세요.");
            return "redirect:/payment/list.do";
        }

        PaymentDTO paymentDTO = paymentService.view(idx);
        if (paymentDTO == null) {
            redirectAttributes.addFlashAttribute("errors", "결제 내역이 없습니다.");
            return "redirect:/payment/list.do";
        }
        if (!paymentDTO.getSeller().equals(session.getAttribute("memberId").toString())
                && !paymentDTO.getBuyer().equals(session.getAttribute("memberId").toString())) {
            redirectAttributes.addFlashAttribute("errors", "접근 권한이 없습니다.");
            return "redirect:/payment/list.do";
        }

        model.addAttribute("item", paymentDTO);

        return "payment/view";
    }

    @GetMapping("/deliveryStart.do")
    public String deliveryStartGet(@RequestParam(defaultValue = "0") int idx, RedirectAttributes redirectAttributes, HttpSession session, @RequestParam(defaultValue = "1") int page_no) {

        if(idx == 0) {
            redirectAttributes.addFlashAttribute("errors", "결제 정보가 없습니다.");
            return "redirect:/goods/mygoods.do?page_no=" + page_no;
        }

        String errors = paymentService.deliveryStart(idx, session.getAttribute("memberId").toString());
        String[] result = errors.split("::");
        redirectAttributes.addFlashAttribute("errors", result[0]);
        if(result.length > 1) {
            alertService.regist(AlertDTO.builder()
                    .memberId(result[1])
                    .content("구매하신 "+result[2]+" 상품의 배송이 시작되었습니다.")
                    .build());
        }
        return "redirect:/goods/mygoods.do?page_no=" + page_no;
    }

    @GetMapping("/deliveryEnd.do")
    public String deliveryEndGet(@RequestParam(defaultValue = "0") int idx, RedirectAttributes redirectAttributes, HttpSession session) {

        if(idx == 0) {
            redirectAttributes.addFlashAttribute("errors", "결제 정보가 없습니다.");
            return "redirect:/payment/list.do?";
        }

        String errors = paymentService.deliveryEnd(idx, session.getAttribute("memberId").toString());
        String[] result = errors.split("::");
        redirectAttributes.addFlashAttribute("errors", result[0]);
        if(result.length > 1) {
            alertService.regist(AlertDTO.builder()
                    .memberId(result[1])
                    .content("판매하신 " + result[2] + " 상품의 배송이 완료되었습니다.")
                    .build());
        }
        return "redirect:/payment/view.do?idx=" + idx;
    }
}
