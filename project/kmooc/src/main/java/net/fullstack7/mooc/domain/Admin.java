package net.fullstack7.mooc.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name="admin")
public class Admin {
    @Id
    @Column(name="adminId", length = 50)
    private String adminId;// VARCHAR(50) PRIMARY KEY, -- 관리자 ID
    @Column(name="password", length = 200)
    private String password;// VARCHAR(200) NOT NULL, -- 비밀번호
//    @Column(name="adminName", length = 200)
//    private String adminName;// VARCHAR(50) NOT NULL, -- 관리자 이름
    @Column(name="createdAt", columnDefinition = "datetime not null default now()")
    private LocalDateTime createdAt;// TIMESTAMP DEFAULT CURRENT_TIMESTAMP -- 생성일
}
