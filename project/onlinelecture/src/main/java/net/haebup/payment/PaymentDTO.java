package net.haebup.payment;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class PaymentDTO {
	private int paymentCode;
	private String courseCode;
	private String memberId;
	private int price;
	private LocalDateTime paymentDate;
	private String paymentDateStr;
	private String refund;
	private LocalDateTime refundDate;
	private String refundDateStr;
	private String viewStatus;
	private LocalDateTime startDate;
	private String startDateStr;
	private LocalDateTime endDate;
	private String endDateStr;
	private String courseName;
	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yy-MM-dd");
	
	public String getCourseName() {
		return courseName;
	}
	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}

	
	public int getPaymentCode() {
		return paymentCode;
	}
	public void setPaymentCode(int paymentCode) {
		this.paymentCode = paymentCode;
	}
	public String getCourseCode() {
		return courseCode;
	}
	public void setCourseCode(String courseCode) {
		this.courseCode = courseCode;
	}
	public String getMemberId() {
		return memberId;
	}
	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}
	public LocalDateTime getPaymentDate() {
		return paymentDate;
	}
	public void setPaymentDate(LocalDateTime paymentDate) {
		this.paymentDate = paymentDate;
		this.paymentDateStr = paymentDate.format(formatter);
	}
	public String getRefund() {
		return refund;
	}
	public void setRefund(String refund) {
		this.refund = refund;

	}
	public LocalDateTime getRefundDate() {
		return refundDate;
	}
	public void setRefundDate(LocalDateTime refundDate) {
		this.refundDate = refundDate;
		this.refundDateStr = refundDate.format(formatter);
	}
	public String getViewStatus() {
		return viewStatus;
	}
	public void setViewStatus(String viewStatus) {
		this.viewStatus = viewStatus;
	}
	public LocalDateTime getStartDate() {
		return startDate;
	}
	public void setStartDate(LocalDateTime startDate) {
		this.startDate = startDate;
		this.startDateStr = startDate.format(formatter);
	}
	public String getStartDateStr() {
	    return startDateStr;
	}
	
	public LocalDateTime getEndDate() {
		return endDate;
	}
	
	public void setEndDate(LocalDateTime endDate) {
		this.endDate = endDate;
		this.endDateStr = endDate.format(formatter);
	}
	public String getEndDateStr() {
	    return endDateStr;
	}
	public String getPaymentDateStr() {
		return paymentDateStr;
	}
	public void setPaymentDateStr(String paymentDateStr) {
		this.paymentDateStr = paymentDateStr;
	}
	public String getRefundDateStr() {
		return refundDateStr;
	}
	public void setRefundDateStr(String refundDateStr) {
		this.refundDateStr = refundDateStr;
	}
	public void setStartDateStr(String startDateStr) {
		this.startDateStr = startDateStr;
	}
	public void setEndDateStr(String endDateStr) {
		this.endDateStr = endDateStr;
	}

	
	
	
	
}
