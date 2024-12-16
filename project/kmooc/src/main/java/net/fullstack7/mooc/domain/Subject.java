package net.fullstack7.mooc.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name="subject")
public class Subject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="subjectId")
    private int subjectId; // INT PRIMARY KEY AUTO_INCREMENT, -- 과목 ID
    @Column(name="subjectName", length = 50, nullable = false)
    private String subjectName; // VARCHAR(50) NOT NULL -- 과목 이름
}
