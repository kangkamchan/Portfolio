package net.fullstack7.mooc.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.fullstack7.mooc.domain.Lecture;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LectureResponseDTO {
    private int lectureId;
    private String title;
    private String description;
    private int courseId;

    public static LectureResponseDTO from(Lecture lecture) {
        return LectureResponseDTO.builder()
                .lectureId(lecture.getLectureId())
                .title(lecture.getTitle())
                .description(lecture.getDescription())
                .courseId(lecture.getCourse().getCourseId())
                .build();
    }
}