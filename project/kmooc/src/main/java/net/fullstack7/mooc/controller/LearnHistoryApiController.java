package net.fullstack7.mooc.controller;

import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.fullstack7.mooc.dto.MemberDTO;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import net.fullstack7.mooc.service.LearningHistoryServiceIf;
import net.fullstack7.mooc.service.CourseEnrollmentServiceIf;
import net.fullstack7.mooc.service.CourseService;
import net.fullstack7.mooc.service.MemberServiceIf;
import net.fullstack7.mooc.dto.LectureContentStatsDTO;
import lombok.extern.log4j.Log4j2;
import lombok.Builder;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/learn-history")
public class LearnHistoryApiController {
  private final LearningHistoryServiceIf learningHistoryService;
  private final CourseEnrollmentServiceIf courseEnrollmentService;
  private final CourseService courseService;
  private final MemberServiceIf memberService;

  @PutMapping("/{lectureContentId}")
  public ResponseEntity<?> updateLearnHistory(@PathVariable int lectureContentId, HttpSession session) {
    MemberDTO member = (MemberDTO) session.getAttribute("memberDTO");

    learningHistoryService.updateLearnHistory(lectureContentId, member.getMemberId());
    int courseId = courseService.getCourseByLectureContentId(lectureContentId).getCourseId();

    if (learningHistoryService.getLectureContentStats(courseId, member.getMemberId()).getProgressRate() >= 0.8) {
      // 이미 완료된 상태였던 경우 확인
      boolean alreadyCompleted = courseEnrollmentService.isAlreadyCompleted(courseId, member.getMemberId());
      int enrollmentUpdated = courseEnrollmentService.updateCourseEnrollment(courseId, member.getMemberId());

      if (!alreadyCompleted) {
        if (enrollmentUpdated > 0) {
          int creditAdded = 0;
          // 학점은행제 회원여부 확인
          if (member.getMemberType() == 1) {
            creditAdded = memberService.addCredit(member.getMemberId());
          }
          // 아닐경우 크레딧 추가 안됨
          log.info("수강 업데이트: {}, 크레딧 추가: {}", enrollmentUpdated, creditAdded);

          return ResponseEntity.ok(LearnHistoryResponse.builder()
              .isCompleted(true)
              .creditAdded(creditAdded > 0)
              .build());
        }
      }

      // 이미 완료된 상태였던 경우
      return ResponseEntity.ok(LearnHistoryResponse.builder()
          .isCompleted(true)
          .creditAdded(false)
          .build());
    }

    return ResponseEntity.ok(LearnHistoryResponse.builder()
        .isCompleted(false)
        .creditAdded(false)
        .build());
  }

  @GetMapping("/{courseId}")
  public ResponseEntity<?> getProgressRate(@PathVariable int courseId, HttpSession session) {
    MemberDTO member = (MemberDTO) session.getAttribute("memberDTO");
    log.info("courseId {}", courseId);
    log.info("memberId {}", member.getMemberId());
    return ResponseEntity.ok(LearnHistoryResponse.builder()
        .progressRate(learningHistoryService.getLectureContentStats(courseId, member.getMemberId()).getProgressRate())
        .isCompleted(false)
        .creditAdded(false)
        .build());
  }

  @Getter
  @AllArgsConstructor
  @Builder
  static class LearnHistoryResponse {
    private double progressRate;
    private boolean isCompleted;
    private boolean creditAdded;
  }
}
