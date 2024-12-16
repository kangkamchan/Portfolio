package net.fullstack7.mooc.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.Min;
import net.fullstack7.mooc.domain.Institution;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TeacherJoinDTO {
    
    @Pattern(regexp = "^[a-z0-9]{5,15}$", message = "아이디는 5~15자의 영문 소문자, 숫자만 사용 가능합니다")
    private String teacherId;
    
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*(),.?\":{}|<>])[A-Za-z\\d!@#$%^&*(),.?\":{}|<>]{10,20}$",
            message = "비밀번호는 10~20자의 영문 대/소문자, 숫자, 특수문자 조합이어야 합니다")
    private String password;
    
    @Pattern(regexp = "^[가-힣]{2,5}$", message = "이름은 2~5자의 한글만 사용 가능합니다")
    private String teacherName;
    
    @Pattern(regexp = "^(?=.{1,254}$)[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$",
            message = "올바른 이메일 형식이 아닙니다")
    private String email;
    
    @NotNull(message = "소속기관을 선택해주세요")
    @Min(value = 1, message = "잘못된 소속기관입니다")
    private int institutionId;
}
