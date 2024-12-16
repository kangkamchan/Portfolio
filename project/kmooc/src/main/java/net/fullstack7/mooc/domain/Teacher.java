package net.fullstack7.mooc.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.FetchType;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name="teacher")
public class Teacher {
    @Id
    @Column(name = "teacherId", nullable = false, length = 50)
    private String teacherId; // VARCHAR(50) PRIMARY KEY, -- 교사 ID

    @Column(name = "password", nullable = false, length = 200)
    private String password; // VARCHAR(200) NOT NULL, -- 비밀번호

    @Column(name = "teacherName", nullable = false, length = 20)
    private String teacherName; // VARCHAR(50) NOT NULL, -- 교사 이름

    @Column(name = "email", nullable = false, length = 100)
    private String email; // VARCHAR(100) UNIQUE NOT NULL, -- 이메일 (고유)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "institutionId", nullable = false)
    private Institution institution;

    @Column(name = "isApproved", columnDefinition = "tinyint(1) not null default 0")
    private int isApproved; // TINYINT(1) DEFAULT 0, -- 관리자 승인 여부 0: 대기, 1: 승인

    @Column(name = "status", nullable = false, length = 20)
    private String status; // VARCHAR(20) NOT NULL, -- 교사 상태 (ACTIVE, INACTIVE, WITHDRAWN)
    
    @CreatedDate
    @Column(name = "createdAt", columnDefinition = "datetime not null default now()")
    private LocalDateTime createdAt; //
}
