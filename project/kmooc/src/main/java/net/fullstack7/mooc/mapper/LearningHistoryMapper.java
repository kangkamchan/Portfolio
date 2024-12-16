package net.fullstack7.mooc.mapper;

import net.fullstack7.mooc.dto.LectureContentStatsDTO; 
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface LearningHistoryMapper {
  LectureContentStatsDTO getLectureContentStats(int courseId, String memberId);
}
