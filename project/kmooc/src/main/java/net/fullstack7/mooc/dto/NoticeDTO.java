package net.fullstack7.mooc.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.time.LocalDateTime;

@Log4j2
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NoticeDTO {
    private int noticeId;
    private String adminId;
    private final String adminName = "관리자";
    @NotBlank(message = "제목을 입력해주세요.")
    @Size(max = 100, message = "100자 이내")
    private String title;
    @NotBlank(message = "내용을 입력해주세요.")
    @Size(max = 2000, message = "2000자 이내")
    private String content;
    private LocalDateTime createdAt;
    @Builder.Default
    private int importance = 1;

    public void setImportance(int importance) {
        if (importance == 0) {
            this.importance = 0;
        } else {
            this.importance = 1;
        }
    }
    public void setImportance(long importance) {
        if (importance == 0) {
            this.importance = 0;
        }
        else {
            this.importance = 1;
        }
    }
    public void setImportance(String importance) {
        if(importance.equals("0")) {
            this.importance = 0;
        }
        else {
            this.importance = 1;
        }
    }
}
