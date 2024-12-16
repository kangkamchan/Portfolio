package net.fullstack7.mooc.dto;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CourseCreateDTO {
    @NotBlank(message = "강좌 제목을 입력해주세요")
    private String title;
    
    @NotNull(message = "과목을 선택해주세요")
    private Integer subjectId;
    
    @Min(value = 1, message = "주차 수는 1 이상이어야 합니다")
    private int weeks;
    
    @Min(value = 1, message = "학습 시간은 1 이상이어야 합니다")
    private int learningTime;
    
    @NotBlank(message = "언어를 선택해주세요")
    @Pattern(regexp = "^(KOREAN|ENGLISH)$", message = "언어는 한국어 또는 영어만 가능합니다")
    private String language;
    
    @NotBlank(message = "강좌 설명을 입력해주세요")
    private String description;
    
    @Min(value = 0 , message = "유효한 값을 입력해주세요")
    @Max(value = 1, message = "유효한 값을 입력해주세요")
    private int isCreditBank;
    
    @NotNull(message = "썸네일 이미지를 업로드해주세요")
    private MultipartFile thumbnail;
}