package net.fullstack7.mooc.mapper;

import net.fullstack7.mooc.domain.Member;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MemberMapper {
    Member login(String memberId);
    Member viewMember(String memberId);
    int registMember(Member member);
    String pwdCheck(String memberId);
    int updatePassword(String memberId, String newPassword);
    int modifyMember(Member member);
    int modifyWithoutPassword(Member member);
    String memberIdCheck(String memberId);
    String emailCheck(String email);

    void deleteMember(String memberId);

}
