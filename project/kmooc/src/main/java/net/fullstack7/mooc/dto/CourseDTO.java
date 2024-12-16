package net.fullstack7.mooc.dto;

import jakarta.persistence.*;
import lombok.*;
import net.fullstack7.mooc.domain.Lecture;
import net.fullstack7.mooc.domain.Subject;
import net.fullstack7.mooc.domain.Teacher;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CourseDTO {
    private int courseId; // INT PRIMARY KEY AUTO_INCREMENT, -- 강좌 ID
    private int viewCount; // INT DEFAULT 0, -- 조회수
    private int isCreditBank; // TINYINT(1) DEFAULT 0, -- 학점 은행 여부
    private String title; // VARCHAR(200) NOT NULL, -- 강좌 제목
    private String thumbnail; // VARCHAR(200), -- 썸네일 이미지
    private Subject subject;
    private int weeks; // INT NOT NULL, -- 주 수
    private int learningTime; // INT NOT NULL, -- 학습 시간
    private String language; // VARCHAR(50) NOT NULL, -- 언어
    private String description; // TEXT NOT NULL, -- 강좌 설명
    private Teacher teacher;
    private String status; // VARCHAR(20) NOT NULL, -- 강좌 상태 (DRAFT, PUBLISHED, DELETED)
    private LocalDateTime createdAt; // TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- 생성일
}
