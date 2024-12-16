package net.fullstack7.mooc.domain;

import jakarta.persistence.*;
import lombok.*;
import java.util.ArrayList;
import java.util.List;

import static com.querydsl.core.types.OrderSpecifier.NullHandling.Default;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name="lecture")
public class Lecture {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="lectureId", unique = true, nullable = false)
    private int lectureId; // INT PRIMARY KEY AUTO_INCREMENT, -- 강의 영역 ID
    @Column(name="title", nullable = false, length = 200)
    private String title; // VARCHAR(200) NOT NULL, -- 강의 영역 제목
    @Column(name="description", columnDefinition = "text not null")
    private String description; // TEXT NOT NULL, -- 강의 영역 설명

    @ManyToOne
    @JoinColumn(name="courseId", nullable = false)
    @ToString.Exclude
    private Course course;

    @OneToMany(mappedBy = "lecture", fetch = FetchType.LAZY)
    @OrderBy("lectureContentId ASC")
    @Builder.Default
    @ToString.Exclude
    private List<LectureContent> contents = new ArrayList<>();

    @OneToMany(mappedBy = "lecture", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    @ToString.Exclude
    private List<Quiz> quizzes = new ArrayList<>();

    public void addQuiz(Quiz quiz) {
        quizzes.add(quiz);
        quiz.setLecture(this);
    }

    //courseId; // INT NOT NULL, -- 강좌 ID
//    FOREIGN KEY (courseId) REFERENCES course(courseId) -- 강좌 ID 외래 키
}
