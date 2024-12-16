package net.fullstack7.mooc.dto;

import jakarta.persistence.*;
import lombok.*;
import net.fullstack7.mooc.domain.Course;
import net.fullstack7.mooc.domain.Member;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CourseEnrollmentDTO {
    private int courseEnrollmentId; // INT PRIMARY KEY AUTO_INCREMENT, -- 강좌 등록 ID
    private Member member;
    private Course course;
    private LocalDateTime enrollmentDate; // DATETIME NOT NULL DEFAULT NOW(), -- 수강신청일
    @Builder.Default
    private int isCompleted=0; // TINYINT(1) DEFAULT 0, -- 수강완료여부(80%이상 수강시 완료)
}
