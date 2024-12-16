package net.fullstack7.mooc.dto;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;
import jakarta.validation.constraints.Pattern;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LectureContentUpdateDTO {
    @NotBlank(message = "제목은 필수입니다")
    private String title;
    
    private String description;
    
    @Pattern(regexp = "^(video|file)$")
    private String type;
    
    private MultipartFile file;
}