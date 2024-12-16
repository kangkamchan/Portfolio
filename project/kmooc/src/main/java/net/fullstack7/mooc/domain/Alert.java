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
@Table(name="alert")
public class Alert {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="alertId", unique = true, nullable = false)
    private int alertId; // INT PRIMARY KEY AUTO_INCREMENT, -- 알림 ID
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memberId")
    private Member member;
    @Column(name="content", columnDefinition = "text not null")
    private String content; // TEXT NOT NULL, -- 알림 내용ㄲ
    @Column(name="isRead", columnDefinition = "tinyint(1) default 0")
    private int isRead; // TINYINT(1) DEFAULT 0, -- 읽음 여부
    @CreatedDate
    @Column(name="createdAt", columnDefinition = "datetime not null default now()")
    private LocalDateTime createdAt; // TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- 생성일
//    FOREIGN KEY (memberId) REFERENCES member(memberId) -- 회원 ID 외래 키
}
