package net.fullstack7.mooc.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.fullstack7.mooc.domain.Member;
import net.fullstack7.mooc.dto.CourseResponseDTO;
import net.fullstack7.mooc.dto.CourseSearchDTO;
import net.fullstack7.mooc.dto.MemberDTO;
import net.fullstack7.mooc.dto.MemberModifyDTO;
import net.fullstack7.mooc.mapper.MemberMapper;
import net.fullstack7.mooc.repository.CourseEnrollmentRepository;
import net.fullstack7.mooc.repository.CourseRepository;
import net.fullstack7.mooc.repository.MemberRepository;
import org.apache.ibatis.session.SqlSession;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
@Log4j2
@Transactional
public class MemberServiceImpl implements MemberServiceIf {
    private final MemberMapper memberMapper;
    private final ModelMapper modelMapper;
    private final MemberRepository memberRepository;
    private final CourseRepository courseRepository;
    private final CourseEnrollmentRepository courseEnrollmentRepository;
    @Autowired
    private JavaMailSender mailSender;


    //로그인
    @Override
    public MemberDTO login(String memberId, String password) {
        Member member = memberMapper.login(memberId);
        if(member != null && member.getPassword().equals(password)) {
            if(member.getStatus().equals("ACTIVE")){
                return modelMapper.map(member, MemberDTO.class);
            }
        }
        return null;
    }

    //아이디 찾기
    public String findId(MemberDTO memberDTO) {
        Optional<Member> memberOptional = memberRepository.findByEmail(memberDTO.getEmail());
        if(memberOptional.isPresent()) {
            return memberOptional.get().getMemberId();
        } else {
            return "fail";
        }
    }
    
    //비밀번호 찾기
    public String findPwd(MemberDTO memberDTO) {
        Optional<Member> memberOptional = memberRepository.findByEmail(memberDTO.getEmail());
        if (memberOptional.isPresent()) {
            Member member = memberOptional.get();
            String tempPassword = generateTempPassword();
            member.setPassword(tempPassword);
            memberRepository.save(member);
            sendTempPasswordEmail(member.getEmail(), tempPassword);
            return "success";
        } else {
            return "fail";
        }
    }

    // 임시 비밀번호 생성
    private String generateTempPassword() {
        return UUID.randomUUID().toString().substring(0, 8);
    }

    // 임시 비밀번호를 이메일로 전송
    private void sendTempPasswordEmail(String email, String tempPassword) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("임시 비밀번호 안내");
        message.setText("고객님, 요청하신 임시 비밀번호는 " + tempPassword + " 입니다. 로그인 후 비밀번호를 변경해주세요.");
        mailSender.send(message);
    }


    //회원조회
    @Override
    public MemberDTO viewMember(String memberId) {
        Member member = memberMapper.viewMember(memberId);
        if(member != null && member.getMemberType() == 0){
            return modelMapper.map(member, MemberDTO.class);
        }
        return null;
    }
    
    //회원등록
    @Override
    public int registMember(MemberDTO memberDTO) {
//        log.info("memberDTO: {}", memberDTO);
        Member member = modelMapper.map(memberDTO, Member.class);
        return memberMapper.registMember(member);
    }


    //아이디 중복체크
    @Override
    public boolean memberIdCheck(String memberId) {
        String result = memberMapper.memberIdCheck(memberId);
        return result == null;
//        return memberMapper.memberIdCheck(memberId) == null;
    }

    //이메일 중복체크
    @Override
    public boolean emailCheck(String email) {
        String result = memberMapper.emailCheck(email);
        return result == null;
    }

    //비밀번호 확인
    @Override
    public boolean pwdCheck(String memberId, String currentPassword) {
        String result = memberMapper.pwdCheck(memberId);
        return result != null && result.equals(currentPassword);
    }

    //새 비밀번호
    @Override
    public void updatePassword(String memberId, String newPassword){
//        memberMapper.updatePassword(memberId, newPassword);
        int updatedRows = memberMapper.updatePassword(memberId, newPassword);
//        System.out.println("Rows updated: " + updatedRows);
        if(updatedRows == 0){
            System.out.println("No rows were updated.");
            throw new RuntimeException("업데이트안됨");
        }
    }

    //회원수정
    @Override
    public int modifyMember(MemberDTO memberDTO) {
        Member member = modelMapper.map(memberDTO, Member.class);
        return memberMapper.modifyMember(member);
    }

    //이메일 유효성 검사
    @Override
    public boolean isValidEmail(String email) {
        String emailRegex = "^(?=.{1,254}$)[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    //비밀번호 유효성 검사
    @Override
    public boolean isValidPassword(String password) {
        String passwordRegex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*(),.?\":{}|<>])[A-Za-z\\d!@#$%^&*(),.?\":{}|<>]{10,20}$";
        Pattern pattern = Pattern.compile(passwordRegex);
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }

    //유효성 검사 후 결과 반환
    @Override
    public String validateMember(MemberModifyDTO memberDTO, Model model) {
        if(!isValidEmail(memberDTO.getEmail())) {
            model.addAttribute("error", "유효한 이메일 형식이 아닙니다.");
            return "mypage/memberView";
        }
        if(!isValidPassword(memberDTO.getPassword())) {
            model.addAttribute("error", "비밀번호는 대문자, 소문자, 숫자, 특수문자를 포함하여 10~20자 이내로 입력하세요.");
            return "mypage/memberView";
        }
        return "mypage/myclass";
    }


    //회원 수정
    @Override
    public int modifyMember(MemberModifyDTO memberModifyDTO) {
        Member member = modelMapper.map(memberModifyDTO, Member.class);
        return memberMapper.modifyMember(member);
    }
    
    //비밀번호 없이 회원수정
    @Override
    public int modifyWithoutPassword(MemberModifyDTO memberModifyDTO) {
        Member member = modelMapper.map(memberModifyDTO, Member.class);
        return memberMapper.modifyWithoutPassword(member);
    }
    
    //회원 탈퇴
    @Override
    public void deleteMember(String memberId) {
        try {
//            log.info("회원 탈퇴 처리 시작, memberId: {}", memberId);
            int updatedRows = memberRepository.updateStatusByMemberId(memberId, "WITHDRAWN");
            if (updatedRows > 0) {
//                log.info("회원 탈퇴 처리 성공, memberId: {}", memberId);
            } else {
//                log.warn("회원 탈퇴 처리 실패, memberId: {}", memberId);
            }
        } catch (Exception e) {
//            log.error("회원 탈퇴 중 오류 발생, memberId: {}", memberId, e);
            throw new RuntimeException("회원 탈퇴 처리 중 오류 발생", e);
        }
    }

    @Override
    public Page<CourseResponseDTO> getCourses(CourseSearchDTO searchDTO, String memberId, int isCompleted) {

        Page<CourseResponseDTO> courses = courseRepository.coursePage(searchDTO.getPageable(), searchDTO, memberId, isCompleted);
        searchDTO.setTotalCount((int)courses.getTotalElements());
        courses = courseRepository.coursePage(searchDTO.getPageable(), searchDTO, memberId, isCompleted);
        return courses;
    }

    @Override
    public int getMyCourseCount(String memberId, int isCompleted) {
        return courseEnrollmentRepository.countAllByMember_MemberIdAndIsCompleted(memberId, isCompleted);
    }

    @Override
    public String modifyToCredit(String memberId) {
        int result = memberRepository.updateMemberTypeById(memberId);
        if(result > 0) return null;
        return "다시 시도해주세요.";
    }

    @Override
    public int addCredit(String memberId) {
        return memberRepository.addCredit(memberId, 1);
    }
}
