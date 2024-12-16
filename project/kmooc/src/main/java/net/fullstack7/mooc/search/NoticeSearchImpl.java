package net.fullstack7.mooc.search;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.JPQLQuery;
import net.fullstack7.mooc.domain.Notice;
import net.fullstack7.mooc.domain.QNotice;
import net.fullstack7.mooc.dto.NoticeDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class NoticeSearchImpl extends QuerydslRepositorySupport implements NoticeSearch {
    public NoticeSearchImpl() {
        super(Notice.class);
    }

    @Override
    public Page<NoticeDTO> noticePage(Pageable pageable, String searchType, String searchWord) {
        QNotice noticeq = QNotice.notice;

        JPQLQuery<Notice> query = from(noticeq);

        BooleanBuilder bb = new BooleanBuilder();

        if (searchType != null) {
            switch (searchType) {
                case "title":
                    bb.and(noticeq.title.containsIgnoreCase(searchWord));
                    break;
                case "content":
                    bb.and(noticeq.content.containsIgnoreCase(searchWord));
                    break;
                default:
                    bb.and(noticeq.title.containsIgnoreCase(searchWord));
                    bb.and(noticeq.content.containsIgnoreCase(searchWord));
                    break;
            }
        }

        if (bb.hasValue())
            query.where(bb);

        query.orderBy(noticeq.importance.asc(), noticeq.createdAt.desc());

        Objects.requireNonNull(this.getQuerydsl()).applyPagination(pageable, query);
        List<Notice> notices = query.fetch();
        List<NoticeDTO> dtos = notices.stream().map(item ->
                NoticeDTO.builder()
                        .adminId(item.getAdmin().getAdminId())
                        .noticeId(item.getNoticeId())
                        .title(item.getTitle())
                        .content(item.getContent())
                        .importance(item.getImportance())
                        .createdAt(item.getCreatedAt())
                        .build()
        ).collect(Collectors.toList());

        int total = (int) query.fetchCount();

        return new PageImpl<>(dtos, pageable, total);
    }
}
