package net.tclass.qna;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class QnaDTO {
    private int idx;                 // 인덱스
    private String memberId;         // 아이디
    private String title;            // 제목
    private String content;          // 내용
    private LocalDateTime regDate;   // 등록일
    private String answerTitle;      // 답변 제목
    private String answerContent;    // 답변 내용
    private LocalDateTime answerDate;// 답변 등록일
    private String fileName;         // 파일명
    private String orgFileName;      // 원래 파일명
    private String fileExt;          // 파일 확장자
    private String filePath;         // 파일 경로
    private int fileSize;            // 파일 크기
    private String category1;
    private String category2;
    private String regDateStr;
    private String answerDateStr;
	private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yy-MM-dd hh:mm:SS");
    // 기본 생성자
    public QnaDTO() {}

    // 모든 필드를 포함한 생성자
    public QnaDTO(int idx, String memberId, String title, String content, LocalDateTime regDate, 
                  String answerTitle, String answerContent, LocalDateTime answerDate) {
        this.idx = idx;
        this.memberId = memberId;
        this.title = title;
        this.content = content;
        this.regDate = regDate;
        this.answerTitle = answerTitle;
        this.answerContent = answerContent;
        this.answerDate = answerDate;
    }

    // Getter 및 Setter 메서드

    public int getIdx() {
        return idx;
    }

    public void setIdx(int idx) {
        this.idx = idx;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getRegDate() {
        return regDate;
    }

    public void setRegDate(LocalDateTime regDate) {
        this.regDate = regDate;
        setRegDateStr(regDate.format(formatter));
    }

    public String getAnswerTitle() {
        return answerTitle;
    }

    public void setAnswerTitle(String answerTitle) {
        this.answerTitle = answerTitle;
    }

    public String getAnswerContent() {
        return answerContent;
    }

    public void setAnswerContent(String answerContent) {
        this.answerContent = answerContent;
    }

    public LocalDateTime getAnswerDate() {
        return answerDate;
    }

    public void setAnswerDate(LocalDateTime answerDate) {
        this.answerDate = answerDate;
        setAnswerDateStr(answerDate.format(formatter));
    }

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getOrgFileName() {
		return orgFileName;
	}

	public void setOrgFileName(String orgFileName) {
		this.orgFileName = orgFileName;
	}

	public String getFileExt() {
		return fileExt;
	}

	public void setFileExt(String fileExt) {
		this.fileExt = fileExt;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public int getFileSize() {
		return fileSize;
	}

	public void setFileSize(int fileSize) {
		this.fileSize = fileSize;
	}

	public String getRegDateStr() {
		return regDateStr;
	}

	public void setRegDateStr(String regDateStr) {
		this.regDateStr = regDateStr;
	}

	public String getAnswerDateStr() {
		return answerDateStr;
	}

	public void setAnswerDateStr(String answerDateStr) {
		this.answerDateStr = answerDateStr;
	}

	public String getCategory1() {
		return category1;
	}

	public void setCategory1(String category1) {
		this.category1 = category1;
	}

	public String getCategory2() {
		return category2;
	}

	public void setCategory2(String category2) {
		this.category2 = category2;
	}
}
