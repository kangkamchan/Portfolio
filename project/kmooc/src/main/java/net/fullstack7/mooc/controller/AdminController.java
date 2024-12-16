package net.fullstack7.mooc.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.fullstack7.mooc.domain.*;
import net.fullstack7.mooc.dto.*;
import net.fullstack7.mooc.service.AdminServiceIf;
import net.fullstack7.mooc.service.CourseService;
import net.fullstack7.mooc.service.NoticeServiceIf;
import net.fullstack7.mooc.service.SubjectServiceImpl;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.stream.Collectors;

@Controller
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {
    private final AdminServiceIf adminService;
    private final NoticeServiceIf noticeService;
    private final SubjectServiceImpl subjectService;
    private final CourseService courseService;
    private final HttpMessageConverters messageConverters;

    @GetMapping("/main")
    public String main(Model model) {
        model.addAttribute("mainPageInfo", adminService.mainPageInfo());
        return "admin/main";
    }

    @GetMapping("/login")
    public String loginGet(HttpSession session, RedirectAttributes redirectAttributes) {
        if (session.getAttribute("loginAdminId") != null) {
            redirectAttributes.addFlashAttribute("errors", "이미 로그인했습니다.");
            return "redirect:/admin/main";
        }
        return "admin/login";
    }

    @PostMapping("/login")
    public String loginPost(@Valid AdminLoginDTO adminLoginDTO, BindingResult bindingResult, RedirectAttributes redirectAttributes
            , HttpSession session) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("loginerror", bindingResult.getAllErrors().get(0).getDefaultMessage());
            return "redirect:/admin/login";
        }
        String result = adminService.login(adminLoginDTO);
        if (result == null) {
            redirectAttributes.addFlashAttribute("loginerror", "비밀번호가 일치하지 않거나 존재하지 않는 회원입니다.");
            return "redirect:/admin/login";
        }
        session.setAttribute("loginAdminId", result);
        return "redirect:/admin/main";
    }

    @GetMapping("/logout")
    public String logoutGet(HttpSession session) {
        session.removeAttribute("loginAdminId");
        return "redirect:/admin/login";
    }


    @GetMapping("/memberList")
    public String memberListGet(Model model, @Valid AdminSearchDTO adminSearchDTO, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            adminSearchDTO = AdminSearchDTO.<Teacher>builder()
                    .status(adminSearchDTO.getStatus())
                    .isApproved(adminSearchDTO.getIsApproved())
                    .memberType(adminSearchDTO.getMemberType())
                    .searchId(adminSearchDTO.getSearchId().substring(0, Math.min(50, adminSearchDTO.getSearchId().length())))
                    .build();
        }

        adminSearchDTO.initialize();

        if (adminSearchDTO.getTypeSelect().equals("t"))
            model.addAttribute("pageinfo", adminService.getTeachers(adminSearchDTO));
        else
            model.addAttribute("pageinfo", adminService.getMembers(adminSearchDTO));

        model.addAttribute("pageDTO", adminSearchDTO);

        return "admin/member/memberList";
    }

    @GetMapping("/memberView/{type}")
    public String memberViewGet(@RequestParam(defaultValue = "0") String memberId, @PathVariable String type
            , Model model, RedirectAttributes redirectAttributes) {

        if (memberId.equals("0")) {
            redirectAttributes.addFlashAttribute("errors", "조회할 계정을 선택하세요.");
            return "redirect:/admin/memberList";
        }

        if (type.equals("t")) {
            Teacher item = adminService.getTeacher(memberId);
            if (item == null) {
                redirectAttributes.addFlashAttribute("존재하지 않는 회원입니다.");
                return "redirect:/admin/memberList";
            }
            model.addAttribute("item", item);
            model.addAttribute("type", "t");
        } else if (type.equals("s")) {
            Member item = adminService.getMember(memberId);
            if (item == null) {
                redirectAttributes.addFlashAttribute("존재하지 않는 회원입니다.");
                return "redirect:/admin/memberList";
            }
            model.addAttribute("item", item);
            model.addAttribute("type", "s");
        } else {
            redirectAttributes.addFlashAttribute("errors", "조회 실패 - 존재하지 않는 회원 유형");
            return "redirect:/admin/memberList";
        }

        return "admin/member/memberView";
    }

    @GetMapping("/memberModify/{type}")
    public String memberModifyGet(@PathVariable String type, @RequestParam(defaultValue = "0") String id
            , Model model, RedirectAttributes redirectAttributes) {

        if (id.equals("0")) {
            redirectAttributes.addFlashAttribute("errors", "상태 변경할 계정 선택하세요.");
            return "redirect:/admin/memberList";
        }

        redirectAttributes.addFlashAttribute("errors", adminService.modifyMemberStatus(type, id));

        return "redirect:/admin/memberView/"+type.substring(type.length()-1)+"?"+"memberId="+id;

    }

    @GetMapping("/memberDelete/{type}")
    public String memberDeleteGet(@RequestParam(defaultValue = "0") String id, @PathVariable String type, Model model, RedirectAttributes redirectAttributes) {
        if (id.equals("0")) {
            redirectAttributes.addFlashAttribute("errors", "삭제할 회원을 선택해주세요.");
            return "redirect:/admin/memberList";
        }
        if ((!type.equals("t") && !type.equals("s"))) {
            redirectAttributes.addFlashAttribute("errors", "회원 분류 설정 오류");
            return "redirect:/admin/memberList";
        }


        redirectAttributes.addFlashAttribute("errors", adminService.deleteMember(id, type));
        return "redirect:/admin/memberView/"+type+"?memberId="+id;
    }

    @GetMapping("/approve/{teacherId}")
    public String approveGet(@PathVariable String teacherId, Model model, RedirectAttributes redirectAttributes) {
        if (teacherId == null) {
            redirectAttributes.addFlashAttribute("errors", "존재하지 않는 계정입니다.");
            return "redirect:/admin/memberList";
        }
        redirectAttributes.addFlashAttribute("errors", adminService.approveTeacherRegist(teacherId));
        return "redirect:/admin/memberView/t?memberId="+teacherId;
    }

    @GetMapping("/courseList")
    public String courseListGet(@Valid CourseSearchDTO searchDTO, BindingResult bindingResult
            , Model model, RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            searchDTO = CourseSearchDTO.builder()
                    .institutionId(searchDTO.getInstitutionId())
                    .subjectId(searchDTO.getSubjectId())
                    .searchValue(searchDTO.getSearchValue())
                    .isCreditBank(searchDTO.getIsCreditBank())
                    .status(searchDTO.getStatus())
                    .build();
        }

        searchDTO.initialize();
        searchDTO.setSortField("createdAt");

        model.addAttribute("pageinfo", adminService.getCourses(searchDTO));
        model.addAttribute("pageDTO", searchDTO);
        model.addAttribute("subjects", subjectService.getAllSubjects());

        return "admin/course/courseList";
    }

    @GetMapping("/courseView/{courseId}")
    public String courseView(Model model, RedirectAttributes redirectAttributes, @PathVariable String courseId) {
        if (courseId == null || !courseId.matches("^\\d+$")) {
            redirectAttributes.addFlashAttribute("errors", "잘못된 강의 번호");
            return "redirect:/admin/courseList";
        }

        int id = Integer.parseInt(courseId);

        Course item = adminService.getCourse(id);

        //수정
        if(item == null) {
            redirectAttributes.addFlashAttribute("errors", "없는 강의");
            return "redirect:/admin/courseList";
        }

        model.addAttribute("item", item);

        return "admin/course/courseView";
    }

    @GetMapping("/courseApprove/{courseId}")
    public String courseModifyGet(@PathVariable String courseId, Model model, RedirectAttributes redirectAttributes) {
        if (courseId == null || !courseId.matches("^\\d+$")) {
            redirectAttributes.addFlashAttribute("errors", "잘못된 강의 번호");
            return "redirect:/admin/courseList";
        }

        int id = Integer.parseInt(courseId);

        redirectAttributes.addFlashAttribute("errors", adminService.modifyCourseStatus("PUBLISHED", id));

        return "redirect:/admin/courseView/"+courseId;
    }

    @GetMapping("/courseDelete")
    public String courseDeleteGet(@RequestParam(defaultValue = "0") String courseId, Model model, RedirectAttributes redirectAttributes) {
        if (courseId.equals("0") || !courseId.matches("^\\d+$")) {
            redirectAttributes.addFlashAttribute("errors", "잘못된 강의 번호");
            return "redirect:/admin/courseList";
        }

        int id = Integer.parseInt(courseId);

        Course course = adminService.getCourse(id);

        if(course == null) {
            redirectAttributes.addFlashAttribute("errors", "존재하지 않는 강의");
            return "redirect:/admin/courseList";
        }

        if(course.getStatus().equals("DRAFT")){
            try {
                courseService.deleteCourse(id);
                redirectAttributes.addFlashAttribute("errors", "삭제 완료");
            } catch (Exception e) {
                redirectAttributes.addFlashAttribute("errors", e.getMessage());
            }
        }
        else if(course.getStatus().equals("PUBLISHED")) {
            redirectAttributes.addFlashAttribute("errors", adminService.modifyCourseStatus("DELETED", id));
        }
        else if(course.getStatus().equals("DELETED")) {
            redirectAttributes.addFlashAttribute("errors", "이미 삭제된 강의");
        }


        return "redirect:/admin/courseList";
    }

    @GetMapping("/noticeList")
    public String noticeListGet(@Valid PageDTO<NoticeDTO> pageDTO, BindingResult bindingResult
            , Model model, RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            pageDTO = PageDTO.<NoticeDTO>builder().build();
        }

        pageDTO.initialize();

        Page<NoticeDTO> notices = noticeService.getNotices(pageDTO);

        model.addAttribute("pageinfo", notices);
        model.addAttribute("pageDTO", pageDTO);

        return "admin/notice/noticeList";
    }

    @GetMapping("/noticeView/{noticeId}")
    public String noticeViewGet(Model model, @PathVariable String noticeId, RedirectAttributes redirectAttributes) {
        if (noticeId == null || noticeId.equals("0") || !noticeId.matches("^\\d+$")) {
            redirectAttributes.addFlashAttribute("errors", "조회할 수 없는 게시글입니다.");
            return "redirect:/admin/noticeList";
        }

        int id = Integer.parseInt(noticeId);

        Notice view = noticeService.view(id);

        if (view == null) {
            redirectAttributes.addFlashAttribute("errors", "조회할 수 없는 게시글입니다.");
            return "redirect:/admin/noticeList";
        }

        model.addAttribute("item", view);

        return "admin/notice/noticeView";
    }

    @GetMapping("/noticeRegist")
    public String noticeRegistGet(Model model, RedirectAttributes redirectAttributes) {
        return "admin/notice/noticeRegist";
    }

    @PostMapping("/noticeRegist")
    public String noticeRegistPost(@Valid NoticeDTO noticeDTO, BindingResult bindingResult, RedirectAttributes redirectAttributes
            , HttpSession session) {
        if (bindingResult.hasErrors()) {
            String message = bindingResult.getAllErrors().get(0).getDefaultMessage();
            if(message != null && message.startsWith("Failed")) {
                message = "필드 값 오류";
            }
            redirectAttributes.addFlashAttribute("errors", message);
            redirectAttributes.addFlashAttribute("item", noticeDTO);
            return "redirect:/admin/noticeRegist";
        }


        noticeDTO.setAdminId(session.getAttribute("loginAdminId").toString());

        adminService.insertNotice(noticeDTO);

        return "redirect:/admin/noticeList";
    }

    @GetMapping("/noticeModify/{noticeId}")
    public String noticeModifyGet(Model model, @PathVariable String noticeId, RedirectAttributes redirectAttributes) {
        if (noticeId == null || noticeId.equals("0") || !noticeId.matches("^\\d+$")) {
            redirectAttributes.addFlashAttribute("errors", "조회할 수 없는 게시글입니다.");
            return "redirect:/admin/noticeList";
        }

        int id = Integer.parseInt(noticeId);

        Notice view = noticeService.view(id);

        if (view == null) {
            redirectAttributes.addFlashAttribute("errors", "조회할 수 없는 게시글입니다.");
            return "redirect:/admin/noticeList";
        }

        model.addAttribute("item", view);

        return "admin/notice/noticeModify";
    }

    @PostMapping("/noticeModify/{noticeId}")
    public String noticeModifyPost(@Valid NoticeDTO noticeDTO, BindingResult bindingResult, RedirectAttributes redirectAttributes
            , @PathVariable String noticeId
            , HttpSession session) {
        if (noticeId == null || noticeId.equals("0") || !noticeId.matches("^\\d+$")) {
            redirectAttributes.addFlashAttribute("errors", "조회할 수 없는 게시글입니다.");
            return "redirect:/admin/noticeList";
        }
        if (bindingResult.hasErrors()) {
            String message = bindingResult.getAllErrors().get(0).getDefaultMessage();
            if(message != null && message.startsWith("Failed")) {
                message = "필드 값 오류";
            }
            redirectAttributes.addFlashAttribute("errors", message);

            return "redirect:/admin/noticeModify/"+noticeId;
        }
        noticeDTO.setAdminId((String) session.getAttribute("loginAdminId"));

        adminService.modifyNotice(noticeDTO);

        return "redirect:/admin/noticeList";
    }

    @GetMapping("/noticeDelete/{noticeId}")
    public String noticeDeleteGet(Model model, @PathVariable String noticeId, RedirectAttributes redirectAttributes
            , HttpSession session) {
        if (noticeId == null || noticeId.equals("0") || !noticeId.matches("^\\d+$")) {
            redirectAttributes.addFlashAttribute("errors", "조회할 수 없는 게시글입니다.");
            return "redirect:/admin/noticeList";
        }
        int id = Integer.parseInt(noticeId);
        String result = adminService.deleteNotice(id, (String) session.getAttribute("loginAdminId"));
        log.info("=========================" + result);
        redirectAttributes.addFlashAttribute("errors", result);
        return "redirect:/admin/noticeList";
    }

}
