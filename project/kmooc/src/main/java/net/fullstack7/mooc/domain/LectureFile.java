package net.fullstack7.mooc.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name="lectureFile")
public class LectureFile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="lectureFileId", unique = true, nullable = false)
    private int lectureFileId; // INT PRIMARY KEY AUTO_INCREMENT, -- 강의 파일 ID
    @Column(name="fileName", nullable = false, length = 200)
    private String fileName; // VARCHAR(200) NOT NULL, -- 파일 이름
    @Column(name="filePath", nullable = false, length = 200)
    private String filePath; // VARCHAR(200) NOT NULL, -- 파일 경로
    @Column(name="fileExtension", nullable = false, length = 50)
    private String fileExtension; // VARCHAR(50) NOT NULL, -- 파일 확장자

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="lectureContentId", nullable = false)
    private LectureContent lectureContent;
}
