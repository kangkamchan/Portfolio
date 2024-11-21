package net.tclass.message;
	import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

	public class MessageDTO {

	    private int idx;                 // 인덱스
	    private String sendId;           // 보낸이
	    private String receiveId;        // 받는이
	    private String title;            // 제목
	    private String content;          // 내용
	    private String fileName;         // 파일명
	    private String orgFileName;      // 원래 파일명
	    private String fileExt;          // 파일 확장자
	    private String filePath;         // 파일 경로
	    private int fileSize;            // 파일 크기
	    private String sendDel;          // 보낸이 삭제 여부
	    private String receiveDel;       // 받는이 삭제 여부
	    private LocalDateTime regDate;   // 등록일
	    private LocalDateTime receiveDate;// 수신 확인일
	    private String regDateStr;
	    private String receiveDateStr;
	    private String sendName;
	    private String receiveName;
	    public String getSendName() {
			return sendName;
		}

		public void setSendName(String sendName) {
			this.sendName = sendName;
		}

		public String getReceiveName() {
			return receiveName;
		}

		public void setReceiveName(String receiveName) {
			this.receiveName = receiveName;
		}

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yy-MM-dd hh:mm:SS");
	    public String getRegDateStr() {
			return regDateStr;
		}

		public void setRegDateStr(String regDateStr) {
			this.regDateStr = regDateStr;
		}

		public String getReceiveDateStr() {
			return receiveDateStr;
		}

		public void setReceiveDateStr(String receiveDateStr) {
			this.receiveDateStr = receiveDateStr;
		}

		// 기본 생성자
	    public MessageDTO() {}

	    // 모든 필드를 포함한 생성자
	    public MessageDTO(int idx, String sendId, String receiveId, String title, String content, String fileName, 
	                      String orgFileName, String fileExt, String filePath, int fileSize, String sendDel, 
	                      String receiveDel, LocalDateTime regDate, LocalDateTime receiveDate) {
	        this.idx = idx;
	        this.sendId = sendId;
	        this.receiveId = receiveId;
	        this.title = title;
	        this.content = content;
	        this.fileName = fileName;
	        this.orgFileName = orgFileName;
	        this.fileExt = fileExt;
	        this.filePath = filePath;
	        this.fileSize = fileSize;
	        this.sendDel = sendDel;
	        this.receiveDel = receiveDel;
	        this.regDate = regDate;
	        this.receiveDate = receiveDate;
	    }

	    // Getter 및 Setter 메서드

	    public int getIdx() {
	        return idx;
	    }

	    public void setIdx(int idx) {
	        this.idx = idx;
	    }

	    public String getSendId() {
	        return sendId;
	    }

	    public void setSendId(String sendId) {
	        this.sendId = sendId;
	    }

	    public String getReceiveId() {
	        return receiveId;
	    }

	    public void setReceiveId(String receiveId) {
	        this.receiveId = receiveId;
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

	    public String getSendDel() {
	        return sendDel;
	    }

	    public void setSendDel(String sendDel) {
	        this.sendDel = sendDel;
	    }

	    public String getReceiveDel() {
	        return receiveDel;
	    }

	    public void setReceiveDel(String receiveDel) {
	        this.receiveDel = receiveDel;
	    }

	    public LocalDateTime getRegDate() {
	        return regDate;
	    }

	    public void setRegDate(LocalDateTime regDate) {
	        this.regDate = regDate;
	        setRegDateStr(regDate.format(formatter));
	    }

	    public LocalDateTime getReceiveDate() {
	        return receiveDate;
	    }

	    public void setReceiveDate(LocalDateTime receiveDate) {
	        this.receiveDate = receiveDate;
	        setReceiveDateStr(receiveDate.format(formatter));
	    }
	}


