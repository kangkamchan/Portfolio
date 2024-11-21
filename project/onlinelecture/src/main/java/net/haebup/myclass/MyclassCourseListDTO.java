package net.haebup.myclass;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MyclassCourseListDTO {
	private String CourseCode;
	private String memberId;
	private LocalDateTime startDate;
	private String startDateStr;
	private LocalDateTime endDate;
	private String endDateStr;
	private String courseName;
	private String teacherName;
	private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yy-MM-dd");
	public String getCourseCode() {
		return CourseCode;
	}
	public void setCourseCode(String courseCode) {
		CourseCode = courseCode;
	}
	public String getMemberId() {
		return memberId;
	}
	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}
	public LocalDateTime getStartDate() {
		return startDate;
	}
	public void setStartDate(LocalDateTime startDate) {
		this.startDate = startDate;
		setStartDateStr(formatter.format(startDate));
	}
	public String getStartDateStr() {
		return startDateStr;
	}
	public void setStartDateStr(String startDateStr) {
		this.startDateStr = startDateStr;
	}
	public LocalDateTime getEndDate() {
		return endDate;
	}
	public void setEndDate(LocalDateTime endDate) {
		this.endDate = endDate;
		setEndDateStr(formatter.format(endDate));
	}
	public String getEndDateStr() {
		return endDateStr;
	}
	public void setEndDateStr(String endDateStr) {
		this.endDateStr = endDateStr;
	}
	public String getCourseName() {
		return courseName;
	}
	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}
	public String getTeacherName() {
		return teacherName;
	}
	public void setTeacherName(String teacherName) {
		this.teacherName = teacherName;
	}
}
