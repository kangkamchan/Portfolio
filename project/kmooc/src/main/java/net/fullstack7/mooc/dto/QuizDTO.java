package net.fullstack7.mooc.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.fullstack7.mooc.domain.Quiz; 
import jakarta.validation.constraints.NotBlank;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuizDTO {
    private int quizId;
    @NotBlank(message = "질문은 필수입니다")
    private String question;
    @NotBlank(message = "답은 필수입니다")
    private String answer;
}