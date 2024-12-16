package net.fullstack7.mooc.domain;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name="course")
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="courseId", unique = true, nullable = false)
    private int courseId; // INT PRIMARY KEY AUTO_INCREMENT, -- 강좌 ID
    @Column(name="viewCount", columnDefinition = "int default 0")
    private int viewCount; // INT DEFAULT 0, -- 조회수
    @Column(name="isCreditBank", columnDefinition = "tinyint(1) default 0")
    private int isCreditBank; // TINYINT(1) DEFAULT 0, -- 학점 은행 여부
    @Column(name="title", nullable = false, length = 200, columnDefinition = "varchar(200) not null collate 'utf8mb4_general_ci'")
    private String title; // VARCHAR(200) NOT NULL, -- 강좌 제목
    @Column(name="thumbnail", nullable = false, length = 200)
    private String thumbnail; // VARCHAR(200), -- 썸네일 이미지

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="subjectId", nullable = false)
    @ToString.Exclude
    private Subject subject;

    @Column(name="weeks", nullable = false)
    private int weeks; // INT NOT NULL, -- 주 수
    @Column(name="learningTime", nullable = false)
    private int learningTime; // INT NOT NULL, -- 학습 시간
    @Column(name="language", nullable = false, length = 50)
    private String language; // VARCHAR(50) NOT NULL, -- 언어
    @Column(name="description", columnDefinition = "text not null")
    private String description; // TEXT NOT NULL, -- 강좌 설명

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="teacherId", nullable = false)
    @ToString.Exclude
    private Teacher teacher;

    @Column(name = "status", nullable = false, length = 20)
    private String status; // VARCHAR(20) NOT NULL, -- 강좌 상태 (DRAFT, PUBLISHED, DELETED)
    @CreatedDate
    @Column(name="createdAt", columnDefinition = "datetime not null default now()")
    private LocalDateTime createdAt; // TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- 생성일

    @OneToMany(mappedBy = "course", fetch = FetchType.LAZY)
    @OrderBy("lectureId ASC")
    @Builder.Default
    @ToString.Exclude
    private List<Lecture> lectures = new ArrayList<>();
}
