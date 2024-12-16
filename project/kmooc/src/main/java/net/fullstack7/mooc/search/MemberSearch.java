package net.fullstack7.mooc.search;

import net.fullstack7.mooc.domain.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MemberSearch {
    Page<Member> adminMemberPage(Pageable pageable, int memType, String status, String memberId);
}
