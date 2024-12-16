package net.fullstack7.mooc.dto;

import java.util.List;

import lombok.*;
import net.fullstack7.mooc.domain.Subject;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class CourseDetailDTO {
    private int courseId;
    private String title;
    private String description;
    private String thumbnail;
    private int weeks;
    private int learningTime;
    private String language;
    private int isCreditBank;
    private String teacherName;
    private String teacherId;
    private String status;
    private int subjectId;
//    private Subject subject;
    private List<LectureDTO> lectures;
}