package net.fullstack7.mooc.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Table(name="member")
public class Member {
    @Id
    @Column(name = "memberId", nullable = false, length = 50)
    private String memberId; //VARCHAR(50) PRIMARY KEY, -- 회원 ID
    @Column(name = "password", nullable = false, length = 200)
    private String password; //VARCHAR(200) NOT NULL, -- 비밀번호
    @Column(name = "userName", nullable = false, length = 20)
    private String userName; //VARCHAR(20) NOT NULL, -- 회원 이름
    @Column(name = "email", nullable = false, length = 100, unique = true)
    private String email; //VARCHAR(100) UNIQUE NOT NULL, -- 이메일 (고유)
    @Column(name = "gender", columnDefinition = "tinyint(1) not null default 0")
    private int gender; //TINYINT(1) NOT NULL, -- 성별(0 : male, 1 : female)
    @Column(name = "memberType", columnDefinition = "tinyint(1) not null default 0")
    private int memberType; //TINYINT(1) NOT NULL, -- 회원 유형 (0 : STUDENT, 1 : CREDIT_BANK)
    @Column(name = "status", nullable = false, length = 20, columnDefinition = "varchar(20) not null default 'ACTIVE'")
    private String status; // VARCHAR(20) NOT NULL, -- 회원 상태 (ACTIVE, INACTIVE, WITHDRAWN)
    @Column(name = "credit", columnDefinition = "int not null default 0")
    private int credit; //INT DEFAULT 0, -- 학점
    @CreatedDate
    @Column(name = "createdAt", columnDefinition = "datetime not null default now()")
    private LocalDateTime createdAt; //TIMESTAMP DEFAULT CURRENT_TIMESTAMP -- 생성일


    public void setPassword(String password) {
        this.password = password;
    }
}
