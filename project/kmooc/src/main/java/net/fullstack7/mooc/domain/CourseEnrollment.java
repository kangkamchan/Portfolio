package net.fullstack7.mooc.domain;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name="courseEnrollment")
public class CourseEnrollment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="courseEnrollmentId", unique = true, nullable = false)
    private int courseEnrollmentId; // INT PRIMARY KEY AUTO_INCREMENT, -- 강좌 등록 ID
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memberId", nullable = false)
    @ToString.Exclude
    private Member member;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "courseId", nullable = false)
    @ToString.Exclude
    private Course course;
    @CreatedDate
    @Column(name="enrollmentDate", columnDefinition = "datetime not null default now()")
    private LocalDateTime enrollmentDate; // DATETIME NOT NULL DEFAULT NOW(), -- 수강신청일
    @Column(name="isCompleted", columnDefinition = "tinyint(1) default 0")
    private int isCompleted; // TINYINT(1) DEFAULT 0, -- 수강완료여부(80%이상 수강시 완료)
}
