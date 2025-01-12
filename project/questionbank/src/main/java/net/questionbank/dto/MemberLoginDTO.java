package net.questionbank.dto;

import jakarta.persistence.Column;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Valid
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class MemberLoginDTO {
    @NotBlank(message = "회원 ID는 필수입니다.")
    @Pattern(regexp = "^[a-z0-9]{5,20}$", message = "회원 ID는 영어 소문자와 숫자만 포함할 수 있습니다.")
    private String memberId;
    @NotBlank(message = "비밀번호는 필수입니다.")
    @Size(max = 20)
    private String pwd;
    private String name;
}
