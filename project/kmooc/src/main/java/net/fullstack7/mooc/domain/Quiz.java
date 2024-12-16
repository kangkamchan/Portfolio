package net.fullstack7.mooc.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "lecture")
@Table(name="quiz")
public class Quiz {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int quizId;
    
    @Column(columnDefinition = "text not null")
    private String question;
    
    @Column(columnDefinition = "text not null")
    private String answer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="lectureId", nullable=false)
    private Lecture lecture;
}
