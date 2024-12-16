package net.fullstack7.mooc.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.core.config.plugins.validation.constraints.NotBlank;

@Log4j2
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MemberModifyDTO {
//    private String memberId;
////    @Pattern(regexp = "^(|(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*(),.?\":{}|<>])[A-Za-z\\d!@#$%^&*(),.?\":{}|<>]{10,20})$\n", message = "비밀번호는 대문자, 소문자, 숫자, 특수문자를 포함하여 10~20자로 입력해주세요.")
//    private String password;
//    @Pattern(regexp = "^(?=.{1,254}$)[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = "유효한 이메일 형식으로 입력해주세요.")
//    private String email;

    private String memberId;

    // 비밀번호는 선택 사항이며, 있을 때만 검증
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*(),.?\":{}|<>])[A-Za-z\\d!@#$%^&*(),.?\":{}|<>]{10,20}$",
            message = "비밀번호는 대문자, 소문자, 숫자, 특수문자를 포함하여 10~20자로 입력해주세요.",
            groups = {UpdatePasswordGroup.class})  // 특정 그룹에서만 검증
    private String password;

    // 이메일은 선택 사항이며, 있을 때만 검증
//    @Email(message = "유효한 이메일 형식으로 입력해주세요.", groups = {UpdateEmailGroup.class})  // 특정 그룹에서만 검증
    @Pattern(regexp = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$", message = "유효한 이메일 형식으로 입력해주세요.", groups = {UpdateEmailGroup.class})
    private String email;

    // Getters and Setters...

    // 그룹을 정의
    public interface UpdatePasswordGroup {}
    public interface UpdateEmailGroup {}
}
