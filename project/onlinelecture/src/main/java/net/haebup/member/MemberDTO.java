package net.haebup.member;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MemberDTO {
	private String memberId;
	private String name;
	private String pwd;
	private String gender;
	private String birthdate;
	private String ssn;
	private String tel;
	private String addr1;
	private String addr2;
	private int point;
	private String email;
	private String memberType;
	private int grade;
	private String agreeLocation;
	private String agreePromotion;
	private String agreeThirdparty;
	private String agreeChunjae;
	private String status;
	private String accessLevel;
	private LocalDateTime regDate;
	private LocalDateTime modifyDate;
	private LocalDateTime leaveDate;
	private String regDateStr;
	private String modifyDateStr;
	private String leaveDateStr;
	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yy-MM-dd hh:mm:SS");
	public String getMemberId() {
		return memberId;
	}
	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPwd() {
		return pwd;
	}
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getBirthdate() {
		return birthdate;
	}
	public void setBirthdate(String birthdate) {
		this.birthdate = birthdate;
	}
	public String getSsn() {
		return ssn;
	}
	public void setSsn(String ssn) {
		this.ssn = ssn;
	}
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	public String getAddr1() {
		return addr1;
	}
	public void setAddr1(String addr1) {
		this.addr1 = addr1;
	}
	public String getAddr2() {
		return addr2;
	}
	public void setAddr2(String addr2) {
		this.addr2 = addr2;
	}
	public int getPoint() {
		return point;
	}
	public void setPoint(int point) {
		this.point = point;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getMemberType() {
		return memberType;
	}
	public void setMemberType(String memberType) {
		this.memberType = memberType;
	}
	public int getGrade() {
		return grade;
	}
	public void setGrade(int grade) {
		this.grade = grade;
	}
	public String getAgreeLocation() {
		return agreeLocation;
	}
	public void setAgreeLocation(String agreeLocation) {
		this.agreeLocation = agreeLocation;
	}
	public String getAgreePromotion() {
		return agreePromotion;
	}
	public void setAgreePromotion(String agreePromotion) {
		this.agreePromotion = agreePromotion;
	}
	public String getAgreeThirdparty() {
		return agreeThirdparty;
	}
	public void setAgreeThirdparty(String agreeThirdparty) {
		this.agreeThirdparty = agreeThirdparty;
	}
	public String getAgreeChunjae() {
		return agreeChunjae;
	}
	public void setAgreeChunjae(String agreeChunjae) {
		this.agreeChunjae = agreeChunjae;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getAccessLevel() {
		return accessLevel;
	}
	public void setAccessLevel(String accessLevel) {
		this.accessLevel = accessLevel;
	}
	public LocalDateTime getRegDate() {
		return regDate;
	}
	public void setRegDate(LocalDateTime regDate) {
		this.regDate = regDate;
        setRegDateStr(regDate.format(formatter));
	}
	public LocalDateTime getModifyDate() {
		return modifyDate;
	}
	public void setModifyDate(LocalDateTime modifyDate) {
		this.modifyDate = modifyDate;
        setModifyDateStr(modifyDate.format(formatter));
	}
	public LocalDateTime getLeaveDate() {
		return leaveDate;
	}
	public void setLeaveDate(LocalDateTime leaveDate) {
		this.leaveDate = leaveDate;
        setLeaveDateStr(leaveDate.format(formatter));
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
	public String getLeaveDateStr() {
		return leaveDateStr;
	}
	public void setLeaveDateStr(String leaveDateStr) {
		this.leaveDateStr = leaveDateStr;
	}
	
	
	
	
	
}
