package net.tclass.member;

import java.time.LocalDateTime;

public class MemberDTO {
    
    private String memberId;         // 아이디
    private String pwd;              // 비밀번호
    private String name;             // 이름
    private String birthDate;        // 생년월일(YYYY-MM-dd)
    private String gender;           // 성별 (M: 남, F: 여)
    private String phone;            // 전화번호(0000-0000-0000)
    private String email;            // 이메일
    private String interest;         // 관심분야 (P: 유/초등, H: 중/고등)
    private String grade;            // 학년 (0: 7세 이하, 1~6: 각 학년)
    private String agreeLocation;    // 선택약관 (위치정보)
    private String agreePromotion;   // 선택약관 (프로모션안내수신)
    private String agreeThirdparty;  // 선택약관 (제3자 제공 동의)
    private String agreeChunjae;     // 선택약관 (천재교육 이용약관 동의)
    private String status;           // 회원 상태 (Y: 정상, P: 정지/휴면, N: 탈퇴)
    private LocalDateTime regDate;   // 등록일
    private LocalDateTime modifyDate;// 수정일
    private LocalDateTime leaveDate; // 탈퇴일
    
    // 기본 생성자
    public MemberDTO() {}

    // 모든 필드를 포함한 생성자
    public MemberDTO(String memberId, String pwd, String name, String birthDate, String gender, String phone, String email, String interest, 
                     String grade, String agreeLocation, String agreePromotion, String agreeThirdparty, String agreeChunjae, 
                     String status, LocalDateTime regDate, LocalDateTime modifyDate, LocalDateTime leaveDate) {
        this.memberId = memberId;
        this.pwd = pwd;
        this.name = name;
        this.birthDate = birthDate;
        this.gender = gender;
        this.phone = phone;
        this.email = email;
        this.interest = interest;
        this.grade = grade;
        this.agreeLocation = agreeLocation;
        this.agreePromotion = agreePromotion;
        this.agreeThirdparty = agreeThirdparty;
        this.agreeChunjae = agreeChunjae;
        this.status = status;
        this.regDate = regDate;
        this.modifyDate = modifyDate;
        this.leaveDate = leaveDate;
    }

    // Getter 및 Setter 메서드

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getInterest() {
        return interest;
    }

    public void setInterest(String interest) {
        this.interest = interest;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
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

    public LocalDateTime getRegDate() {
        return regDate;
    }

    public void setRegDate(LocalDateTime regDate) {
        this.regDate = regDate;
    }

    public LocalDateTime getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(LocalDateTime modifyDate) {
        this.modifyDate = modifyDate;
    }

    public LocalDateTime getLeaveDate() {
        return leaveDate;
    }

    public void setLeaveDate(LocalDateTime leaveDate) {
        this.leaveDate = leaveDate;
    }
}
