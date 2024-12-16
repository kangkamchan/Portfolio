package net.fullstack7.mooc.search;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.JPQLQuery;
import net.fullstack7.mooc.domain.Member;
import net.fullstack7.mooc.domain.QMember;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;
import java.util.Objects;

public class MemberSearchImpl extends QuerydslRepositorySupport implements MemberSearch {
    public MemberSearchImpl() {
        super(Member.class);
    }

    @Override
    public Page<Member> adminMemberPage(Pageable pageable, int memType, String status, String memberId) {
        QMember memberq = QMember.member;
        JPQLQuery<Member> query = from(memberq);

        BooleanBuilder bb = new BooleanBuilder();

        if(memType != -1) {
            bb.and(memberq.memberType.eq(memType));
        }
        if(status != null) {
            bb.and(memberq.status.eq(status));
        }
        if(memberId != null) {
            bb.and(memberq.memberId.containsIgnoreCase(memberId));
        }
        if(bb.hasValue()) query.where(bb);

        query.orderBy(memberq.createdAt.desc());

        Objects.requireNonNull(this.getQuerydsl()).applyPagination(pageable, query);
        List<Member> members = query.fetch();

        int total = (int) query.fetchCount();

        return new PageImpl<>(members, pageable, total);
    }
}
