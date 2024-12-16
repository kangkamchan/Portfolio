package net.fullstack7.mooc.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import net.fullstack7.mooc.domain.Course;
import net.fullstack7.mooc.domain.Institution;
import net.fullstack7.mooc.repository.InstitutionRepository;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import jakarta.validation.Valid;
import net.fullstack7.mooc.dto.CourseDetailDTO;
import net.fullstack7.mooc.dto.TeacherJoinDTO;
import net.fullstack7.mooc.service.TeacherService;
import net.fullstack7.mooc.domain.Teacher;
import net.fullstack7.mooc.dto.TeacherLoginDTO;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.List;
import net.fullstack7.mooc.domain.Subject;
import net.fullstack7.mooc.repository.SubjectRepository;
import net.fullstack7.mooc.service.CourseService;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Controller
@RequiredArgsConstructor
@RequestMapping("/teacher")
public class TeacherController {
  private final TeacherService teacherService;
  private final InstitutionRepository institutionRepository;
  private final SubjectRepository subjectRepository;
  private final CourseService courseService;

  @GetMapping("/login")
  public String loginForm() {
    return "teacher/login";
  }

  @GetMapping("/join")
  public String joinForm(Model model) {
    List<Institution> institutions = institutionRepository.findAllByOrderByInstitutionIdAsc();
    model.addAttribute("institutions", institutions);
    return "teacher/join";
  }

  @PostMapping("/login")
  public String login(@ModelAttribute TeacherLoginDTO loginDTO,
      HttpSession session,
      RedirectAttributes redirectAttributes) {
    try {
      Teacher teacher = teacherService.login(loginDTO.getTeacherId(), loginDTO.getPassword());
      if (teacher.getStatus().equals("ACTIVE") && teacher.getIsApproved() == 1) {
        session.setAttribute("teacher", teacher);
        return "redirect:/teacher/myLectures";
      }
      redirectAttributes.addFlashAttribute("error", "승인 대기 중이거나 비활성화된 계정입니다.");
      return "redirect:/teacher/login";
    } catch (IllegalArgumentException e) {
      redirectAttributes.addFlashAttribute("error", e.getMessage());
      return "redirect:/teacher/login";
    }
  }

  @PostMapping("/join")
  public String join(@Valid @ModelAttribute TeacherJoinDTO dto,
      BindingResult bindingResult,
      RedirectAttributes redirectAttributes,
      Model model) {
    if (bindingResult.hasErrors()) {
      model.addAttribute("institutions", institutionRepository.findAllByOrderByInstitutionIdAsc());
      return "teacher/join";
    }

    try {
      teacherService.join(dto);
      redirectAttributes.addFlashAttribute("message", "회원가입이 완료되었습니다. 관리자 승인 후 이용 가능합니다.");
      return "redirect:/teacher/login";
    } catch (Exception e) {
      redirectAttributes.addFlashAttribute("error", e.getMessage());
      return "redirect:/teacher/join";
    }
  }

  @GetMapping("/myLectures")
  public String myLectures(Model model, HttpSession session, RedirectAttributes redirectAttributes) {
    Teacher teacher = (Teacher) session.getAttribute("teacher");
    if (teacher == null) {
      redirectAttributes.addFlashAttribute("error", "로그인이 필요합니다.");
      return "redirect:/teacher/login";
    }

    List<Course> lectures = teacherService.getMyLectures(teacher.getTeacherId());
    model.addAttribute("lectures", lectures);
    return "teacher/myLectures";
  }

  @GetMapping("/registLecture")
  public String registLecture(Model model, HttpSession session, RedirectAttributes redirectAttributes) {
    Teacher teacher = (Teacher) session.getAttribute("teacher");
    if (teacher == null) {
      redirectAttributes.addFlashAttribute("error", "로그인이 필요합니다.");
      return "redirect:/teacher/login";
    }

    List<Subject> subjects = subjectRepository.findAllByOrderBySubjectIdAsc();
    model.addAttribute("subjects", subjects);
    return "teacher/registLecture";
  }

  @GetMapping("/lectures/edit/{courseId}")
  public String editLecture(@PathVariable int courseId, Model model, HttpSession session, RedirectAttributes redirectAttributes) {
    Teacher teacher = (Teacher) session.getAttribute("teacher");
    if (teacher == null) {
      redirectAttributes.addFlashAttribute("error", "로그인이 필요합니다.");
      return "redirect:/teacher/login";
    }

    CourseDetailDTO courseDetail = courseService.getCourseWithContents(courseId);

    if (!courseDetail.getTeacherId().equals(teacher.getTeacherId())) {
      redirectAttributes.addFlashAttribute("error", "권한이 없습니다.");
      return "redirect:/teacher/myLectures";
    }

    model.addAttribute("course", courseDetail);
    model.addAttribute("subjects", subjectRepository.findAllByOrderBySubjectIdAsc());
    return "teacher/updateLecture";
  }

  @GetMapping("/courses/{courseId}/detail")
  public String getCourseDetail(@PathVariable int courseId, Model model, HttpSession session, RedirectAttributes redirectAttributes) {
    Teacher teacher = (Teacher) session.getAttribute("teacher");
    if (teacher == null) {
      redirectAttributes.addFlashAttribute("error", "로그인이 필요합니다.");
      return "redirect:/teacher/login";
    }
    CourseDetailDTO courseDetail = courseService.getCourseWithContents(courseId);

    if (!courseDetail.getTeacherId().equals(teacher.getTeacherId())) {
      redirectAttributes.addFlashAttribute("error", "권한이 없습니다.");
      return "redirect:/teacher/myLectures";
    }
    log.info("courseDetail: {}", courseDetail);
    log.info("quizzes: {}", courseDetail.getLectures().get(0).getQuizzes());
    model.addAttribute("course", courseDetail);
    model.addAttribute("teacherName", teacher.getTeacherName());
    return "teacher/courseDetail";
  }

}
