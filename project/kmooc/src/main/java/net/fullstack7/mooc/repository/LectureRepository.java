package net.fullstack7.mooc.repository;

import net.fullstack7.mooc.domain.Course;
import net.fullstack7.mooc.domain.Lecture;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

@Repository
public interface LectureRepository extends JpaRepository<Lecture, Integer> {
    List<Lecture> findByCourseOrderByLectureIdAsc(Course course);
    List<Lecture> findAllByCourse(Course course);
    Optional<Lecture> findByLectureIdAndCourse(int lectureId, Course course);
    Optional<Lecture> findByLectureId(int lectureId);
}