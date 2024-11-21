package net.haebup.bbs;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class BbsDTO {
	private int idx;
	private int refIdx;
	private int levelIdx;
	private int sortOrder;
	private String category;
	private String memberId;
	private String title;
	private String content;
	private LocalDateTime regDate;
	private String regDateStr;
	private LocalDateTime modifyDate;
	private String modifyDateStr;
	private int readCnt;
	private String filePath;
	private String fileName;
	private String orgFileName;
	private String fileExt;
	private int fileSize;
	private int rating;
	private String courseCode;
	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yy-MM-dd");
	public String getCourseCode() {
		return courseCode;
	}
	public void setCourseCode(String courseCode) {
		this.courseCode = courseCode;
	}
	public int getIdx() {
		return idx;
	}
	public void setIdx(int idx) {
		this.idx = idx;
	}
	public int getRefIdx() {
		return refIdx;
	}
	public void setRefIdx(int refIdx) {
		this.refIdx = refIdx;
	}
	public int getLevelIdx() {
		return levelIdx;
	}
	public void setLevelIdx(int levelIdx) {
		this.levelIdx = levelIdx;
	}
	public int getSortOrder() {
		return sortOrder;
	}
	public void setSortOrder(int sortOrder) {
		this.sortOrder = sortOrder;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
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
	public String getRegDateStr() {
		return regDateStr;
	}
	public void setRegDateStr(String regDateStr) {
		this.regDateStr = regDateStr;
	}
	public LocalDateTime getModifyDate() {
		return modifyDate;
	}
	public void setModifyDate(LocalDateTime modifyDate) {
		this.modifyDate = modifyDate;
        setModifyDateStr(modifyDate.format(formatter));
	}
	public String getModifyDateStr() {
		return modifyDateStr;
	}
	public void setModifyDateStr(String modifyDateStr) {
		this.modifyDateStr = modifyDateStr;
	}
	public int getReadCnt() {
		return readCnt;
	}
	public void setReadCnt(int readCnt) {
		this.readCnt = readCnt;
	}
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
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
	public int getFileSize() {
		return fileSize;
	}
	public void setFileSize(int fileSize) {
		this.fileSize = fileSize;
	}
	public int getRating() {
		return rating;
	}
	public void setRating(int rating) {
		this.rating = rating;
	}
	
	
}
