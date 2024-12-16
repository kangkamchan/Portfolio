package net.fullstack7.mooc.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ModifyCreditDTO {
    @Pattern(regexp = "^[가-힣]{2,5}$", message = "이름은 한글 2~5자 이내로 입력 가능합니다.")
    private String name;
    @Pattern(regexp = "^01[0-9]{9}$", message = "11자리 숫자로 입력해주세요.")
    private String phone;
}
