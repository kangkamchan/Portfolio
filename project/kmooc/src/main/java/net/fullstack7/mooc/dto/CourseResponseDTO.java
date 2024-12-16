package net.fullstack7.mooc.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.fullstack7.mooc.domain.Course;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CourseResponseDTO {
    private int courseId;
    private String title;
    private String description;
    private int weeks;
    private int learningTime;
    private String language;
    private int isCreditBank;
    private String thumbnail;
    private String status;
    private String teacherName;
    private String institutionName;
    private LocalDateTime createdAt;
    
    public static CourseResponseDTO from(Course course) {
        return CourseResponseDTO.builder()
                .courseId(course.getCourseId())
                .title(course.getTitle())
                .description(course.getDescription())
                .weeks(course.getWeeks())
                .learningTime(course.getLearningTime())
                .language(course.getLanguage())
                .isCreditBank(course.getIsCreditBank())
                .thumbnail(course.getThumbnail())
                .build();
    }
}