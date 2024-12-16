package net.fullstack7.mooc.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AdminLoginDTO {
    @NotBlank(message = "아이디를 입력해주세요.")
    @Size(min = 5, max=15, message = "아이디 또는 비밀번호가 일치하지 않습니다.1")
    @Pattern(regexp = "^[a-z0-9]{5,15}$", message = "아이디 또는 비밀번호가 일치하지 않습니다.2")
    private String adminId;
    @NotBlank(message = "비밀번호를 입력해주세요.")
    @Size(max=20, message = "아이디 또는 비밀번호가 일치하지 않습니다.3")
    private String password;
}
