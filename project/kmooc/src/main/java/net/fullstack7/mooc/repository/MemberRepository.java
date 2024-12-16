package net.fullstack7.mooc.repository;

import net.fullstack7.mooc.domain.Member;
import net.fullstack7.mooc.search.MemberSearch;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, String>, MemberSearch {
    @Query("SELECT m FROM Member m WHERE m.email = :email")
    Optional<Member> findByEmail(String email);

    @Query("SELECT m FROM Member m WHERE m.memberId = :memberId")
    String pwdCheck(String memberId);

    @Query("UPDATE Member M SET M.password = :newPassword WHERE M.memberId = :memberId")
    void updatePassword(@Param("memberId")String memberId, @Param("newPassword")String newPassword);

    @Modifying
    @Query("update Member M set M.status = :status where M.memberId = :memberId")
    int updateStatusByMemberId(String memberId, String status);

    @Modifying
    @Query("update Member M set M.memberType = 1 where M.memberId = :memberId")
    int updateMemberTypeById(String memberId);

    int countByCreatedAtIsBetween(LocalDateTime from, LocalDateTime to);
    int countByStatusIn(List<String> status);

    Optional<Member> findByMemberId(String memberId);

    @Modifying
    @Query("update Member m set m.credit = m.credit + :credit where m.memberId = :memberId")
    int addCredit(@Param("memberId") String memberId, @Param("credit") int credit);
}
