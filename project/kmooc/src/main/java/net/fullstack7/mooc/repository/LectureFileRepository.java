package net.fullstack7.mooc.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import net.fullstack7.mooc.domain.LectureFile;
import net.fullstack7.mooc.domain.LectureContent;

@Repository
public interface LectureFileRepository extends JpaRepository<LectureFile, Integer> {
    Optional<LectureFile> findByLectureFileId(int lectureFileId);
    Optional<LectureFile> findByLectureContent(LectureContent lectureContent);
}