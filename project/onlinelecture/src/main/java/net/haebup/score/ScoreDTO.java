package net.haebup.score;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ScoreDTO {
	private int idx;
	private String memberId;
	private String courseCode;
	private int courseNo;
	private int score;
	private LocalDateTime regDate;
	private LocalDateTime modifyDate;
	private LocalDateTime deleteDate;
	private String courseName;
	private String regDateStr;
	private String modifyDateStr;
	private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	public String getCourseName() {
		return courseName;
	}
	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}
	public String getRegDateStr() {
		return regDateStr;
	}
	public void setRegDateStr(String regDateStr) {
		this.regDateStr = regDateStr;
	}
	public String getModifyDateStr() {
		return modifyDateStr;
	}
	public void setModifyDateStr(String modifyDateStr) {
		this.modifyDateStr = modifyDateStr;
	}
	public String getDeleteDateStr() {
		return deleteDateStr;
	}
	public void setDeleteDateStr(String deleteDateStr) {
		this.deleteDateStr = deleteDateStr;
	}
	private String deleteDateStr;
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
	public String getCourseCode() {
		return courseCode;
	}
	public void setCourseCode(String courseCode) {
		this.courseCode = courseCode;
	}
	public int getCourseNo() {
		return courseNo;
	}
	public void setCourseNo(int courseNo) {
		this.courseNo = courseNo;
	}
	public int getScore() {
		return score;
	}
	public void setScore(int score) {
		this.score = score;
	}
	public LocalDateTime getRegDate() {
		return regDate;
	}
	public void setRegDate(LocalDateTime regDate) {
		this.regDate = regDate;
		setRegDateStr(formatter.format(regDate));
	}
	public LocalDateTime getModifyDate() {
		return modifyDate;
	}
	public void setModifyDate(LocalDateTime modifyDate) {
		this.modifyDate = modifyDate;
		setModifyDateStr(formatter.format(modifyDate));
	}
	public LocalDateTime getDeleteDate() {
		return deleteDate;
	}
	public void setDeleteDate(LocalDateTime deleteDate) {
		this.deleteDate = deleteDate;
		setDeleteDateStr(formatter.format(deleteDate));
	}
	
	
	
}
