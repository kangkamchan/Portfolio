package net.questionbank.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.questionbank.annotation.Logging;
import net.questionbank.domain.Member;
import net.questionbank.dto.MemberLoginDTO;
import net.questionbank.dto.MemberRegisterDTO;
import net.questionbank.exception.CustomRuntimeException;
import net.questionbank.repository.MemberRepository;
import net.questionbank.util.PasswordUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@Logging
@Log4j2
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberServiceIf{
    private final MemberRepository memberRepository;
    private final PasswordUtil passwordUtil;
    @Override
    public Member register(MemberRegisterDTO registerDTO) {
        try{
            if(memberRepository.existsById(registerDTO.getMemberId())) {
                throw new CustomRuntimeException("이미 존재하는 회원 ID 입니다.");
            }
            Member member = Member.builder()
                    .memberId(registerDTO.getMemberId())
                    .pwd(passwordUtil.hashPassword(registerDTO.getPwd()))
                    .email(registerDTO.getEmail())
                    .name(registerDTO.getName())
                    .build();
            return memberRepository.save(member);
        }catch(CustomRuntimeException e){
            log.error("customRuntimeException : {}",e.getMessage());
            throw e;
        } catch(Exception e){
            log.error(e.getMessage());
            throw new CustomRuntimeException("오류가 발생했습니다. 다시시도하세요");
        }
    }

    @Override
    public MemberLoginDTO login(MemberLoginDTO loginDTO) {
        try{
            Member member = memberRepository.findByMemberId(loginDTO.getMemberId()).orElseThrow(()->new CustomRuntimeException("아이디를 다시 확인하세요"));
            if(!passwordUtil.checkPassword(loginDTO.getPwd(),member.getPwd())){
                throw new CustomRuntimeException("비밀번호가 틀렸습니다.");
            }
            return MemberLoginDTO.builder()
                    .memberId(member.getMemberId())
                    .name(member.getName())
                    .build();
        }catch(CustomRuntimeException e){
            log.error("customRuntimeException : {}",e.getMessage());
            throw e;
        }
        catch(Exception e){
            log.error(e.getMessage());
            throw new CustomRuntimeException("오류가 발생했습니다. 다시시도하세요");
        }
    }

    @Override
    public boolean exists(String memberId) {
        try {
            return memberRepository.existsById(memberId);
        }catch(Exception e){
            log.error(e.getMessage());
            throw new CustomRuntimeException("오류가 발생했습니다. 다시시도하세요");
        }
    }
}
