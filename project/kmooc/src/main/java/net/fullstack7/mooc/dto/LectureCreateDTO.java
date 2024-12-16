package net.fullstack7.mooc.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
  
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LectureCreateDTO {
    @NotBlank(message = "섹션 제목을 입력해주세요")
    private String title;
    
    @NotBlank(message = "섹션 설명을 입력해주세요")
    private String description;
    
    @NotNull(message = "강좌 ID가 필요합니다")
    private Integer courseId;
}