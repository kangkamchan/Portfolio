package net.fullstack7.mooc.repository;

import net.fullstack7.mooc.domain.LearningHistory;
import net.fullstack7.mooc.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LearningHistoryRepository extends JpaRepository<LearningHistory, Integer> {
    LearningHistory findByLectureContentIdAndMember(int lectureContentId, Member member);

    @Modifying
    @Query("UPDATE LearningHistory SET isCompleted = 1 WHERE lectureContentId = :lectureContentId AND member = :member")
    void updateLearnHistory(@Param("lectureContentId") int lectureContentId, @Param("member") Member member);

    // select count(learningHistory.lectureContentId) as completedLectureContentCount, course.courseId
    // from learningHistory
    // inner join lectureContent on learningHistory.lectureContentId = lectureContent.lectureContentId
    // inner join lecture on lectureContent.lectureId = lecture.lectureId
    // inner join course on lecture.courseId = course.courseId
    // where learningHistory.isCompleted = 0 AND learningHistory.memberId="아이디" AND course.courseId = 강의번호
    // group by course.courseId;
}