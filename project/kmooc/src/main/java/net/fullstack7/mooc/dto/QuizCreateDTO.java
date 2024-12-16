package net.fullstack7.mooc.dto;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class QuizCreateDTO {
  private int lectureId;
  private List<QuizDTO> quizzes; 
}
