package net.fullstack7.mooc.dto;

import net.fullstack7.mooc.domain.LectureFile;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LectureFileDTO {
    private int lectureFileId;
    private String fileName;
    private String filePath;
    private String fileExtension;
    
    public static LectureFileDTO from(LectureFile file) {
        return LectureFileDTO.builder()
            .lectureFileId(file.getLectureFileId())
            .fileName(file.getFileName())
            .filePath(file.getFilePath())
            .fileExtension(file.getFileExtension())
            .build();
    }
}