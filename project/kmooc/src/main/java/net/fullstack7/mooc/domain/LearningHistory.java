package net.fullstack7.mooc.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name="learningHistory")
public class LearningHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="learningHistoryId", unique = true, nullable = false)
    private int learningHistoryId; // INT PRIMARY KEY AUTO_INCREMENT, -- 학습내역 ID

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="memberId", nullable = false)
    @ToString.Exclude
    private Member member;

    @Column(name="lectureContentId", nullable = false)
    private int lectureContentId; // INT NOT NULL, -- 강의 영역 콘텐츠 ID
    @Column(name="isCompleted", columnDefinition = "tinyint(1) default 0")
    private int isCompleted; // TINYINT(1) DEFAULT 0, -- 완료 여부
//    FOREIGN KEY (memberId) REFERENCES member(memberId), -- 회원 ID 외래 키
//    FOREIGN KEY (lectureContentId) REFERENCES lectureContent(lectureContentId) -- 강의 영역 콘텐츠 ID 외래 키
}
