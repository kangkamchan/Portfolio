package net.fullstack7.mooc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import net.fullstack7.mooc.domain.Lecture;
import net.fullstack7.mooc.domain.LectureContent;

import java.util.List;
import java.util.Optional;

@Repository
public interface LectureContentRepository extends JpaRepository<LectureContent, Integer> {
    List<LectureContent> findByLectureOrderByLectureContentIdAsc(Lecture lecture);
    Optional<LectureContent> findByLectureContentId(int lectureContentId);
    List<LectureContent> findByLecture(Lecture lecture);
    List<LectureContent> findByLectureAndIsVideo(Lecture lecture, int isVideo);
}
