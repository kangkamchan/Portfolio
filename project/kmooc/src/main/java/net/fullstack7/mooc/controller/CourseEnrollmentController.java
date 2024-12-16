package net.fullstack7.mooc.controller;

import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.fullstack7.mooc.domain.Course;
import net.fullstack7.mooc.domain.Member;
import net.fullstack7.mooc.domain.Teacher;
import net.fullstack7.mooc.dto.*;
import net.fullstack7.mooc.service.CourseEnrollmentServiceIf;
import net.fullstack7.mooc.service.CourseService;
import net.fullstack7.mooc.service.LearningHistoryServiceIf;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/courseEnrollment")
@RequiredArgsConstructor
@Log4j2
public class CourseEnrollmentController {
    private final CourseEnrollmentServiceIf courseEnrollmentService;
    private final CourseService courseService;
    private final ModelMapper modelMapper;
    private final LearningHistoryServiceIf learningHistoryService;
    @PostMapping("/regist/{courseId}")
    public ResponseEntity<?> regist(@PathVariable int courseId, HttpSession session) {
        try{
            MemberDTO memberDTO = (MemberDTO) session.getAttribute("memberDTO");
            if (memberDTO == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message","로그인이 필요합니다."));
            }
            courseEnrollmentService.regist(
                    CourseEnrollmentDTO
                            .builder()
                            .course(Course.builder().courseId(courseId).build())
                            .member(modelMapper.map(memberDTO, Member.class))
                            .enrollmentDate(LocalDateTime.now())
                            .build()
            );
            int learningHistoryResult = learningHistoryService.saveAll(courseId, memberDTO.getMemberId());
            log.info("learning history result: {}",learningHistoryResult);
            return ResponseEntity.ok(ApiResponse.success("수강신청이 완료되었습니다."));
        }catch(Exception e){
            log.error(e);
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
    @DeleteMapping("/delete/{courseId}")
    public ResponseEntity<?> delete(@PathVariable int courseId, HttpSession session) {
        try{
            MemberDTO memberDTO = (MemberDTO) session.getAttribute("memberDTO");
            if (memberDTO == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
            }
            CourseEnrollmentDTO courseEnrollmentDTO = courseEnrollmentService.isEnrolled(
              CourseEnrollmentDTO
                      .builder()
                      .course(Course.builder().courseId(courseId).build())
                      .member(Member.builder().memberId(memberDTO.getMemberId()).build())
                      .build()
            );
            if(courseEnrollmentDTO == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.error("수강중인 강의가 아닙니다."));
            }
            String result = courseEnrollmentService.delete(courseEnrollmentDTO);
            if(result != null){
                return ResponseEntity.badRequest().body(ApiResponse.error(result));
            }
            learningHistoryService.deleteAll(courseId, memberDTO.getMemberId());
            return ResponseEntity.ok(ApiResponse.success("수강 취소 완료"));
        }catch(Exception e){
            log.error(e);
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }

    }
}
