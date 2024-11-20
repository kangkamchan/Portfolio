package net.tclass.member;

import java.util.ArrayList;
import java.util.List;

import net.tclass.common.DBConnPool;

public class MemberDAO extends DBConnPool{
	//회원 등록
	public int regist(MemberDTO dto) {
		System.out.println("=================================");
		System.out.println("MemberDAO > regist > start");
		String sql = "INSERT INTO tbl_member("
				+ "memberId, pwd, name, birthDate, gender, "
				+ "phone, email, interest, grade, agreeLocation, "
				+ "agreePromotion, agreeThirdparty, agreeChunjae "
				+ ") VALUES("
				+ "?, ?, ?, ?, ?, "
				+ "?, ?, ?, ?, ?, "
				+ "?, ?, ?"
				+ ")";
		System.out.println("MemberDAO > regist > sql : " + sql);
		try {
			pstm = con.prepareStatement(sql);
			pstm.setString(1,dto.getMemberId());
			pstm.setString(2, dto.getPwd());
			pstm.setString(3, dto.getName());
			pstm.setString(4, dto.getBirthDate());
			pstm.setString(5, dto.getGender());
			pstm.setString(6, dto.getPhone());
			pstm.setString(7, dto.getEmail());
			pstm.setString(8, dto.getInterest());
			pstm.setString(9, dto.getGrade());
			pstm.setString(10, dto.getAgreeLocation());
			pstm.setString(11, dto.getAgreePromotion());
			pstm.setString(12, dto.getAgreeThirdparty());
			pstm.setString(13, dto.getAgreeChunjae());
			System.out.println("MemberDAO > regist > pstm : " + pstm.toString());
			return pstm.executeUpdate();
		}catch(Exception e) {
			System.out.println("MemberDAO > regist > error");
			System.out.println(e.getMessage());
		}
		System.out.println("MemberDAO > regist > end");
		System.out.println("=================================");
		return 0;
	}
	//회원 조회
	public MemberDTO getMemberById(String memberId) {
		System.out.println("=================================");
		System.out.println("MemberDAO > getMemberById > start");
		String sql = "SELECT memberId, pwd, name, birthDate, gender, "
				+ "phone, email, interest, grade, agreeLocation, "
				+ "agreePromotion, agreeThirdparty, agreeChunjae, "
				+ "status, regDate, modifyDate, leaveDate "
				+ "FROM tbl_member "
				+ "WHERE memberId = ?";
		try {
			pstm = con.prepareStatement(sql);
			pstm.setString(1,memberId);
			rs = pstm.executeQuery();
			if(rs.next()) {
				MemberDTO dto = new MemberDTO();
				
				dto.setMemberId(rs.getString("memberId"));
				dto.setPwd(rs.getString("pwd"));
				dto.setName(rs.getString("name"));
				dto.setBirthDate(rs.getString("birthDate"));
				dto.setGender(rs.getString("gender"));
				dto.setPhone(rs.getString("phone"));
				dto.setEmail(rs.getString("email"));
				dto.setInterest(rs.getString("interest"));
				dto.setGrade(rs.getString("grade"));
				dto.setAgreeLocation(rs.getString("agreeLocation"));
				dto.setAgreePromotion(rs.getString("agreePromotion"));
				dto.setAgreeThirdparty(rs.getString("agreeThirdparty"));
				dto.setAgreeChunjae(rs.getString("agreeChunjae"));
				dto.setStatus(rs.getString("status"));
				dto.setRegDate(rs.getTimestamp("regDate").toLocalDateTime());
				if(rs.getTimestamp("modifyDate")!=null) {
					dto.setModifyDate(rs.getTimestamp("modifyDate").toLocalDateTime());
				}
				if(rs.getTimestamp("leaveDate")!=null) {
					dto.setLeaveDate(rs.getTimestamp("leaveDate").toLocalDateTime());
				}
				return dto;
			}
		}catch(Exception e) {
			System.out.println("MemberDAO > getMemberById > error");
			System.out.println(e.getMessage());
		}
		System.out.println("MemberDAO > getMemberById > end");
		System.out.println("=================================");
		return null;
	}
	//회원 로그인(아이디 + 비밀번호로 조회)
	public MemberDTO login(String memberId, String pwd) {
		System.out.println("=================================");
		System.out.println("MemberDAO > login > start");
		try {
		MemberDTO dto = getMemberById(memberId);
		if(dto==null) {
			return dto;
		}
		if(dto.getPwd().equals(pwd)) {
			return dto;
		}else {
			return null;
		}
		}catch(Exception e) {
			System.out.println("MemberDAO > login > error");
			System.out.println(e.getMessage());
		}
		System.out.println("MemberDAO > login > end");
		System.out.println("=================================");
		return null;
	}
	//이름 + 휴대폰번호로 회원 조회(아이디찾기)
	public MemberDTO findId(String name, String phone) {
		System.out.println("=================================");
		System.out.println("MemberDAO > findId > start");
		String sql = "SELECT memberId FROM tbl_member WHERE name=? AND phone=?";
		try {
			pstm = con.prepareStatement(sql);
			pstm.setString(1, name);
			pstm.setString(2, phone);
			rs = pstm.executeQuery();
			if(rs.next()) {				
				return getMemberById(rs.getString("memberId"));
			}
		}catch(Exception e) {
			System.out.println("MemberDAO > findId > error");
			System.out.println(e.getMessage());
		}
		
		System.out.println("MemberDAO > findId > end");
		System.out.println("=================================");
		return null;
	}
	//아이디 + 이름 + 휴대폰번호로 회원 조회(비밀번호 찾기)
	public MemberDTO findPwd(String memberId, String name, String phone) {
		System.out.println("=================================");
		System.out.println("MemberDAO > findPwd > start");
		try {
			MemberDTO dto = getMemberById(memberId);
			if(dto==null) {
				return null;
			}
			if(dto.getName().equals(name)&&dto.getPhone().equals(phone)){
				return dto;
			}
		}catch(Exception e) {
			System.out.println("MemberDAO > findPwd > error");
			System.out.println(e.getMessage());
		}
		
		System.out.println("MemberDAO > findPwd > end");
		System.out.println("=================================");
		return null;
	}
	//회원 수정
	public int modify(MemberDTO dto) {
		System.out.println("=================================");
		System.out.println("MemberDAO > modify > start");
		String sql = "UPDATE tbl_member SET "
				+ "pwd=?, birthDate=?, gender=?, "
				+ "phone=?, email=? "
				+ "WHERE memberId=?";
		try {
			pstm = con.prepareStatement(sql);
			pstm.setString(1, dto.getPwd());
			pstm.setString(2, dto.getBirthDate());
			pstm.setString(3, dto.getGender());
			pstm.setString(4, dto.getPhone());
			pstm.setString(5, dto.getEmail());
			pstm.setString(6, dto.getMemberId());
			return pstm.executeUpdate();
		}catch(Exception e) {
			System.out.println("MemberDAO > modify > error");
			System.out.println(e.getMessage());
		}
		System.out.println("MemberDAO > modify > end");
		System.out.println("=================================");
		return 0;
	}
	//회원 삭제
	public int delete(String memberId) {
		System.out.println("=================================");
		System.out.println("MemberDAO > delete > start");
		String sql = "UPDATE tbl_member SET status='N' WHERE memberId=?";
		try {
			pstm = con.prepareStatement(sql);
			pstm.setString(1, memberId);
			return pstm.executeUpdate();
		}catch(Exception e) {
			System.out.println("MemberDAO > delete > error");
			System.out.println(e.getMessage());
		}
		System.out.println("MemberDAO > delete > end");
		System.out.println("=================================");
		return 0;
	}
	//선생님 리스트 조회
	public List<MemberDTO> getTeacherList(String interest){
		System.out.println("=================================");
		System.out.println("MemberDAO > getTeacherList > start");
		String sql = "SELECT memberId, pwd, name, birthDate, gender, "
				+ "phone, email, interest, grade, agreeLocation, "
				+ "agreePromotion, agreeThirdparty, agreeChunjae, "
				+ "status, regDate, modifyDate, leaveDate "
				+ "FROM tbl_member "
				+ "WHERE status = ? AND interest = ?";
		try {
			pstm = con.prepareStatement(sql);
			pstm.setString(1, "T");
			pstm.setString(2, interest);
			rs = pstm.executeQuery();
			List<MemberDTO> list = new ArrayList<MemberDTO>();
			while(rs.next()) {
				MemberDTO dto = new MemberDTO();
				dto.setMemberId(rs.getString("memberId"));
				dto.setPwd(rs.getString("pwd"));
				dto.setName(rs.getString("name"));
				dto.setBirthDate(rs.getString("birthDate"));
				dto.setGender(rs.getString("gender"));
				dto.setPhone(rs.getString("phone"));
				dto.setEmail(rs.getString("email"));
				dto.setInterest(rs.getString("interest"));
				dto.setGrade(rs.getString("grade"));
				dto.setAgreeLocation(rs.getString("agreeLocation"));
				dto.setAgreePromotion(rs.getString("agreePromotion"));
				dto.setAgreeThirdparty(rs.getString("agreeThirdparty"));
				dto.setAgreeChunjae(rs.getString("agreeChunjae"));
				dto.setStatus(rs.getString("status"));
				dto.setRegDate(rs.getTimestamp("regDate").toLocalDateTime());
				if(rs.getTimestamp("modifyDate")!=null) {
					dto.setModifyDate(rs.getTimestamp("modifyDate").toLocalDateTime());
				}
				if(rs.getTimestamp("leaveDate")!=null) {
					dto.setLeaveDate(rs.getTimestamp("leaveDate").toLocalDateTime());
				}
				list.add(dto);
			}
			return list;
		}catch(Exception e) {
			System.out.println("MemberDAO > getTeacherList > error");
			System.out.println(e.getMessage());
		}
		System.out.println("MemberDAO > getTeacherList > end");
		System.out.println("=================================");
		return null;
	}
}
