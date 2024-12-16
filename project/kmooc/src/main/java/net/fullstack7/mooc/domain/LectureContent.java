package net.fullstack7.mooc.domain;

import jakarta.persistence.*;
import lombok.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name="lectureContent")
public class LectureContent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="lectureContentId", unique = true, nullable = false)
    private int lectureContentId; // INT PRIMARY KEY AUTO_INCREMENT, -- 강의 영역 콘텐츠 ID
    @Column(name="title", nullable = false, length = 200)
    private String title; // VARCHAR(200) NOT NULL, -- 강의 영역 콘텐츠 제목
    @Column(name="description", columnDefinition = "text")
    private String description; // TEXT NOT NULL, -- 강의 영역 콘텐츠 설명
//    private int lectureId; // INT NOT NULL, -- 강의 영역 ID

    @ManyToOne
    @JoinColumn(name="lectureId", nullable = false)
    @ToString.Exclude
    private Lecture lecture;

    @Column(name="isVideo", columnDefinition = "tinyint(1) default 0")
    private int isVideo; // TINYINT(1) DEFAULT 0, -- 동영상 여부
//    FOREIGN KEY (lectureId) REFERENCES lecture(lectureId)

    @OneToOne(mappedBy = "lectureContent", fetch = FetchType.LAZY)
    @ToString.Exclude
    private LectureFile lectureFile;
}
