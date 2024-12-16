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
@Table(name="notice")
public class Notice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="noticeId", unique = true, nullable = false)
    private int noticeId; // INT PRIMARY KEY AUTO_INCREMENT, -- 공지사항 ID

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "adminId")
    private Admin admin;

    @Column(name="title", nullable = false, length = 200)
    private String title; // VARCHAR(200) NOT NULL, -- 공지사항 제목
    @Column(name="content", columnDefinition = "text not null")
    private String content; // TEXT NOT NULL, -- 공지사항 내용
    @CreatedDate
    @Column(name="createdAt", columnDefinition = "datetime not null default now()")
    private LocalDateTime createdAt; // TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- 생성일
    @Column(name="importance", columnDefinition = "tinyint(1) not null default 0")
    private int importance; // TINYINT(1) NOT NULL, -- 중요도
//    FOREIGN KEY (adminId) REFERENCES admin(adminId) -- 관리자 ID 외래 키
}
