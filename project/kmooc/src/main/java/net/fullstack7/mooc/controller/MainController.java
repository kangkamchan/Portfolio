package net.fullstack7.mooc.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.fullstack7.mooc.domain.Notice;
import net.fullstack7.mooc.dto.NoticeDTO;
import net.fullstack7.mooc.dto.PageDTO;
import net.fullstack7.mooc.service.AdminServiceIf;
import net.fullstack7.mooc.service.CourseService;
import net.fullstack7.mooc.service.NoticeServiceIf;
import net.fullstack7.mooc.service.SubjectServiceImpl;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/main")
public class MainController {
    private final NoticeServiceIf noticeService;
    private final CourseService courseService;

    @GetMapping("/notice/list")
    public String noticeListGet(@Valid PageDTO<NoticeDTO> pageDTO, BindingResult bindingResult
            , Model model, RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            pageDTO = PageDTO.<NoticeDTO>builder().build();
        }

        pageDTO.initialize();

        Page<NoticeDTO> notices = noticeService.getNotices(pageDTO);

        model.addAttribute("pageinfo", notices);
        model.addAttribute("pageDTO", pageDTO);

        return "notice/list";
    }

    @GetMapping("/notice/view/{noticeId}")
    public String noticeViewGet(Model model, @PathVariable String noticeId, RedirectAttributes redirectAttributes) {
        if (noticeId == null || noticeId.equals("0") || noticeId.length() > 9 || !noticeId.matches("^\\d+$")) {
            redirectAttributes.addFlashAttribute("errors", "조회할 수 없는 게시글입니다.");
            return "redirect:/main/notice/list";
        }

        int id = Integer.parseInt(noticeId);

        Notice view = noticeService.view(id);

        if (view == null) {
            redirectAttributes.addFlashAttribute("errors", "조회할 수 없는 게시글입니다.");
            return "redirect:/main/notice/list";
        }

        model.addAttribute("item", view);

        return "notice/view";
    }

    @GetMapping("/main")
    public String main(Model model) {
        log.info(courseService.mainCourseList(4));
        model.addAttribute("mainCourseList", courseService.mainCourseList(4));
        model.addAttribute("notices", noticeService.getNewestNotices());
        return "main/main";
    }

    @GetMapping("/footer/personalrule")
    public String personalrule() {
        return "main/footer/personalrule";
    }

    @GetMapping("/footer/userterms")
    public String userterms() {
        return "main/footer/userterms";
    }

    @GetMapping("/footer/copyright")
    public String copyright() {
        return "main/footer/copyright";
    }

    @GetMapping("/footer/intro")
    public String intro() {
        return "main/footer/intro";
    }

    @GetMapping("/footer/business")
    public String business() {
        return "main/footer/business";
    }
    @GetMapping("/bankintro")
    public String bankintro() {
        return "main/bankintro";
    }


}
