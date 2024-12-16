package net.fullstack7.mooc.service;

import net.fullstack7.mooc.dto.LectureContentStatsDTO;

import java.util.List;

public interface LearningHistoryServiceIf {
    public int saveAll(int courseId, String memberId);
    public void deleteAll(int courseId, String memberId);
    public void updateLearnHistory(int lectureContentId, String memberId);
    public LectureContentStatsDTO getLectureContentStats(int courseId, String memberId);
}
