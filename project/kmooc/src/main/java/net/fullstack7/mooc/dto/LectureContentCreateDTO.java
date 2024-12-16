package net.fullstack7.mooc.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.AssertTrue;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LectureContentCreateDTO {
    @NotNull(message = "강의 ID는 필수입니다")
    private Integer lectureId;

    @NotBlank(message = "제목은 필수입니다")
    @Size(min = 2, max = 200, message = "제목은 2-200자 사이여야 합니다")
    private String title;

    @NotBlank(message = "설명은 필수입니다")
    private String description;

    @NotNull(message = "콘텐츠 타입은 필수입니다")
    @Pattern(regexp = "^(video|file)$", message = "콘텐츠 타입은 video, file 중 하나여야 합니다")
    private String type;

    private MultipartFile file;

    // private List<QuizDTO> quizzes;

    // @AssertTrue(message = "동영상/문서 타입일 경우 제목, 설명 및 파일은 필수입니다")
    // public boolean isValid() {
    //     if ("quiz".equals(type)) {
    //         return true;
    //     }
    //     return (title != null && !title.trim().isEmpty() 
    //             && description != null && !description.trim().isEmpty() 
    //             && file != null && !file.isEmpty());
    // }
}
