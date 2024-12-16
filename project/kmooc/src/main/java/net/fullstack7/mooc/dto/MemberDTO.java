package net.fullstack7.mooc.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.fullstack7.mooc.domain.Member;
import org.apache.logging.log4j.core.config.plugins.validation.constraints.NotBlank;

import java.sql.Timestamp;

@Log4j2
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MemberDTO {
    @NotBlank
    @Size(min = 5, max = 15)
    @Pattern(regexp = "^[a-z0-9]{5,15}$", message = "아이디는 영문자로 시작하고 5~15자의 영문자와 숫자만 사용 가능합니다.")
    private String memberId;
    @NotBlank
    @Size(min = 10, max = 20)
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*(),.?\":{}|<>])[A-Za-z\\d!@#$%^&*(),.?\":{}|<>]{10,20}$", message = "비밀번호는 대문자, 소문자, 숫자, 특수문자를 포함하여 10~20자로 입력해주세요.")
    private String password;
    @NotBlank
    @Size(min = 2, max = 5)
    @Pattern(regexp = "^[가-힣]{2,5}$", message = "이름은 한글 2~5자 이내로 입력 가능합니다.")
    private String userName;
    @NotBlank
    @Pattern(regexp = "^(?=.{1,254}$)[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,7}$", message = "유효한 이메일 형식으로 입력해주세요.")
    private String email;
    @NotBlank
    private int gender;
    @NotBlank
    private String education;
    @NotBlank
    private int memberType;

    private String status;
    private int credit;
    private Timestamp createdAt;

    public Member toEntityForFindId(){
        return Member.builder()
//                .memberId(memberId)
                .email(email)
                .build();
    }
}
