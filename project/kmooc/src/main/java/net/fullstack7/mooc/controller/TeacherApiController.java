package net.fullstack7.mooc.controller;

import net.fullstack7.mooc.domain.Course;
import net.fullstack7.mooc.domain.Lecture;
import net.fullstack7.mooc.domain.Teacher;
import net.fullstack7.mooc.dto.*;
import net.fullstack7.mooc.service.CourseService;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.fullstack7.mooc.util.FileUploadUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/teacher")
@Log4j2
public class TeacherApiController {

    private final CourseService courseService;
    private final FileUploadUtil fileUploadUtil;

    @PostMapping("/courses")
    public ResponseEntity<?> createCourse(
            @Valid CourseCreateDTO courseDTO,
            HttpSession session) {
        try {
            Teacher teacher = (Teacher) session.getAttribute("teacher");
            if (teacher == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
            }

            Course course = courseService.createCourse(courseDTO, teacher);

            return ResponseEntity.ok(CourseResponseDTO.from(course));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/courses/{courseId}")
    public ResponseEntity<ApiResponse<Void>> updateCourse(
            @PathVariable int courseId,
            @ModelAttribute CourseUpdateDTO dto,
            HttpSession session) {
        try {
            Teacher teacher = (Teacher) session.getAttribute("teacher");
            if (!courseService.checkAuthority(courseId, teacher, "course")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ApiResponse.error("권한이 없습니다."));
            }
            if (courseService.checkPublished(courseId, "course")) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ApiResponse.error("강좌가 배포되어 수정할 수 없습니다."));
            }
            courseService.updateCourse(courseId, dto, teacher);
            return ResponseEntity.ok(ApiResponse.success("강좌가 수정되었습니다."));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @PostMapping("/lectures/{lectureId}/contents")
    public ResponseEntity<ApiResponse<Void>> createContent(
            @PathVariable int lectureId,
            @ModelAttribute LectureContentCreateDTO dto) {
        try {
            dto.setLectureId(lectureId);
            courseService.createContent(dto);
            return ResponseEntity.ok(ApiResponse.success("콘텐츠가 성공적으로 생성되었습니다."));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @DeleteMapping("/contents/{contentId}")
    public ResponseEntity<ApiResponse<Void>> deleteContent(
            @PathVariable int contentId,
            HttpSession session) {
        try {
            Teacher teacher = (Teacher) session.getAttribute("teacher");
            if (!courseService.checkAuthority(contentId, teacher, "content")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ApiResponse.error("권한이 없습니다."));
            }
            if (courseService.checkPublished(contentId, "content")) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ApiResponse.error("강좌가 배포되어 삭제할 수 없습니다."));
            }
            courseService.deleteContent(contentId);
            return ResponseEntity.ok(ApiResponse.success("콘텐츠가 성공적으로 삭제되었습니다."));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @PostMapping("/lectures/{lectureId}/quizzes")
    public ResponseEntity<ApiResponse<Void>> createQuizzes(
            @PathVariable int lectureId,
            @RequestBody QuizCreateDTO dto,
            HttpSession session) {
        try {
            Teacher teacher = (Teacher) session.getAttribute("teacher");
            if (!courseService.checkAuthority(lectureId, teacher, "lecture")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ApiResponse.error("권한이 없습니다."));
            }
            dto.setLectureId(lectureId);
            courseService.createQuizzes(dto);
            return ResponseEntity.ok(ApiResponse.success("퀴즈가 성공적으로 생성되었습니다."));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @PutMapping("/quizzes/{quizId}")
    public ResponseEntity<ApiResponse<Void>> updateQuiz(
            @PathVariable int lectureId,
            @PathVariable int quizId,
            @RequestBody QuizDTO dto,
            HttpSession session) {
        try {
            Teacher teacher = (Teacher) session.getAttribute("teacher");
            if (!courseService.checkAuthority(quizId, teacher, "quiz")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ApiResponse.error("권한이 없습니다."));
            }
            if (courseService.checkPublished(quizId, "quiz")) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ApiResponse.error("강좌가 배포되어 수정할 수 없습니다."));
            }
            courseService.updateQuiz(quizId, dto);
            return ResponseEntity.ok(ApiResponse.success("퀴즈가 수정되었습니다."));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @DeleteMapping("/quizzes/{quizId}")
    public ResponseEntity<ApiResponse<Void>> deleteQuiz(
            @PathVariable int lectureId,
            @PathVariable int quizId,
            HttpSession session) {
        try {
            Teacher teacher = (Teacher) session.getAttribute("teacher");
            if (!courseService.checkAuthority(quizId, teacher, "quiz")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ApiResponse.error("권한이 없습니다."));
            }
            if (courseService.checkPublished(quizId, "quiz")) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ApiResponse.error("강좌가 배포되어 삭제할 수 없습니다."));
            }
            courseService.deleteQuiz(quizId);
            return ResponseEntity.ok(ApiResponse.success("퀴즈가 삭제되었습니다."));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/download")
    public ResponseEntity<Resource> downloadFile(@RequestParam String filePath) {
        log.debug("다운로드 요청 파일 경로: {}", filePath);
        return fileUploadUtil.downloadFile(filePath);
    }

    @PostMapping("/courses/{courseId}/lectures")
    public ResponseEntity<ApiResponse<Void>> createLecture(
            @PathVariable int courseId,
            @RequestBody LectureCreateDTO dto) {
        try {
            dto.setCourseId(courseId);
            courseService.createLecture(dto);
            return ResponseEntity.ok(ApiResponse.success("섹션이 추가되었습니다."));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @PostMapping("/courses/{courseId}/lectures/batch")
    public ResponseEntity<?> createLectures(
            @PathVariable int courseId,
            @RequestBody List<LectureCreateDTO> dtos) {
        try {
            List<LectureResponseDTO> responses = new ArrayList<>();
            for (LectureCreateDTO dto : dtos) {
                dto.setCourseId(courseId);
                Lecture lecture = courseService.createLecture(dto);
                responses.add(LectureResponseDTO.from(lecture));
            }
            return ResponseEntity.ok(responses);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
        

    @DeleteMapping("/lectures/{lectureId}")
    public ResponseEntity<ApiResponse<Void>> deleteLecture(
            @PathVariable int lectureId,
            HttpSession session) {
        try {
            Teacher teacher = (Teacher) session.getAttribute("teacher");
            if (!courseService.checkAuthority(lectureId, teacher, "lecture")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ApiResponse.error("권한이 없습니다."));
            }
            if (courseService.checkPublished(lectureId, "lecture")) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ApiResponse.error("강좌가 배포되어 삭제할 수 없습니다."));
            }
            courseService.deleteLecture(lectureId);
            return ResponseEntity.ok(ApiResponse.success("섹션이 삭제되었습니다."));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @PutMapping("/lectures/{lectureId}")
    public ResponseEntity<ApiResponse<Void>> updateLecture(
            @PathVariable int lectureId,
            @RequestBody LectureUpdateDTO dto,
            HttpSession session) {
        try {
            Teacher teacher = (Teacher) session.getAttribute("teacher");
            if (!courseService.checkAuthority(lectureId, teacher, "lecture")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ApiResponse.error("권한이 없습니다."));
            }
            if (courseService.checkPublished(lectureId, "lecture")) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ApiResponse.error("강좌가 배포되어 수정할 수 없습니다."));
            }
            courseService.updateLecture(lectureId, dto);
            return ResponseEntity.ok(ApiResponse.success("섹션이 수정되었습니다."));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @PutMapping("/contents/{contentId}")
    public ResponseEntity<ApiResponse<Void>> updateContent(
            @PathVariable int contentId,
            @ModelAttribute LectureContentUpdateDTO dto,
            HttpSession session) {
        try {
            Teacher teacher = (Teacher) session.getAttribute("teacher");
            if (!courseService.checkAuthority(contentId, teacher, "content")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ApiResponse.error("권한이 없습니다."));
            }
            if (courseService.checkPublished(contentId, "content")) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ApiResponse.error("강좌가 배포되어 수정할 수 없습니다."));
            }
            courseService.updateContent(contentId, dto);
            return ResponseEntity.ok(ApiResponse.success("콘텐츠가 수정되었습니다."));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
    @DeleteMapping("/courses/{courseId}")
    public ResponseEntity<ApiResponse<Void>> deleteCourse(
            @PathVariable int courseId,
            HttpSession session) {
        try {
            Teacher teacher = (Teacher) session.getAttribute("teacher");
            if (!courseService.checkAuthority(courseId, teacher, "course")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ApiResponse.error("권한이 없습니다."));
            }
            if (courseService.checkPublished(courseId, "course")) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ApiResponse.error("강좌가 배포되어 삭제할 수 없습니다."));
            }
            courseService.deleteCourse(courseId);
            return ResponseEntity.ok(ApiResponse.success("강좌가 삭제되었습니다."));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
}
