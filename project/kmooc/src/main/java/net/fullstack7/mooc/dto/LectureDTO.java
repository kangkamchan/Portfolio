package net.fullstack7.mooc.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;  
import net.fullstack7.mooc.dto.LectureContentDTO;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LectureDTO {
    private int lectureId;
    private String title;
    private String description;
    private List<LectureContentDTO> contents;
    private List<QuizDTO> quizzes;
}
