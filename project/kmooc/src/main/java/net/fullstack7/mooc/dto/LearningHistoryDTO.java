package net.fullstack7.mooc.dto;

import jakarta.persistence.*;
import lombok.*;
import net.fullstack7.mooc.domain.Member;
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class LearningHistoryDTO {
    private int learningHistoryId; // INT PRIMARY KEY AUTO_INCREMENT, -- 학습내역 ID
    private String memberId;
    private int lectureContentId; // INT NOT NULL, -- 강의 영역 콘텐츠 ID
    @Builder.Default
    private int isCompleted=0; // TINYINT(1) DEFAULT 0, -- 완료 여부
//    FOREIGN KEY (memberId) REFERENCES member(memberId), -- 회원 ID 외래 키
//    FOREIGN KEY (lectureContentId) REFERENCES lectureContent(lectureContentId) -- 강의 영역 콘텐츠 ID 외래 키
}
