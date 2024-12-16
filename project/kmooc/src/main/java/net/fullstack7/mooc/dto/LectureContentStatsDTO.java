package net.fullstack7.mooc.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LectureContentStatsDTO{
  private int totalCount;
  private int completedCount;
  private int uncompletedCount;

  public double getProgressRate(){
    return (double)completedCount / (double)totalCount;
  }
}
