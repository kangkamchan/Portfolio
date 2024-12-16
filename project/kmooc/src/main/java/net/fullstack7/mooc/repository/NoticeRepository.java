package net.fullstack7.mooc.repository;

import net.fullstack7.mooc.domain.Notice;
import net.fullstack7.mooc.search.NoticeSearch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface NoticeRepository extends JpaRepository<Notice, Integer>, NoticeSearch {
    @Modifying
    @Query("update Notice N set N.title = :title, N.content = :content, N.importance = :importance where N.noticeId = :noticeId")
    int updateNotice(int noticeId, String title, String content, int importance);

    List<Notice> findTop5ByOrderByCreatedAtDesc();
}
