package net.fullstack7.mooc.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name="institution")
public class Institution {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="institutionId", unique = true, nullable = false)
    private int institutionId;// INT PRIMARY KEY AUTO_INCREMENT, -- 소속 기관 ID
    @Column(name="institutionName", nullable = false, length = 100)
    private String institutionName;// VARCHAR(100) NOT NULL
}
