package net.fullstack7.mooc.repository;

import net.fullstack7.mooc.domain.*;
import net.fullstack7.mooc.search.CourseSearch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.query.Param;

@Repository
public interface CourseRepository extends JpaRepository<Course, Integer>, CourseSearch {
    List<Course> findByTeacherOrderByCreatedAtDesc(Teacher teacher);
    Optional<Course> findByCourseIdAndTeacher(int courseId, Teacher teacher);
//    Optional<Course> findByCourseId(Integer courseId);

    @Query("SELECT DISTINCT c FROM Course c " +
           "LEFT JOIN FETCH c.lectures l " +
           "LEFT JOIN FETCH l.contents lc " +
           "LEFT JOIN FETCH lc.lectureFile lf " +
           "LEFT JOIN FETCH l.quizzes q " +
           "WHERE c.courseId = :courseId " +
           "ORDER BY l.lectureId ASC, lc.lectureContentId ASC")
    Optional<Course> findCourseWithContentsByCourseId(@Param("courseId") int courseId);
    Optional<Course> findByCourseId(int courseId);

    @Modifying
    @Query("update Course C set C.status = :status where C.courseId = :courseId")
    int updateStatus(int courseId, String status);

    int countByCreatedAtIsBetween(LocalDateTime from, LocalDateTime to);
    int countByStatusIn(List<String> status);

    Page<Course> findBySubjectAndStatusOrderByCourseIdDesc(Subject subject, String status, Pageable pageable);
}
