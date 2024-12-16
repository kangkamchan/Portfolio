package net.fullstack7.mooc.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CourseUpdateDTO {
    @NotBlank(message = "제목은 필수입니다")
    private String title;
    
    private String description;
    
    @NotNull(message = "과목은 필수입니다")
    private Integer subjectId;
    
    @Min(value = 1, message = "주차는 1 이상이어야 합니다")
    private Integer weeks;
    
    @Min(value = 1, message = "학습시간은 1 이상이어야 합니다")
    private Integer learningTime;
    
    @NotBlank(message = "언어는 필수입니다")
    @Pattern(regexp = "^(KOREAN|ENGLISH)$", message = "언어는 한국어 또는 영어만 가능합니다")
    private String language;

    private Integer isCreditBank;
    
    private MultipartFile thumbnail;
}