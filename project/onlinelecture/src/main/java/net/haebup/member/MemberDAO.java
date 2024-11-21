package net.haebup.member;

import java.util.ArrayList;
import java.util.List;

import net.haebup.common.DBConnPool;

public class MemberDAO extends DBConnPool{
	public MemberDAO() {
		super();
	}
	
	// 회원가입
	public int regist(MemberDTO dto) {
		System.out.println("-----------------------------------------");
		System.out.println("MemberDAO > setMemberRegist > start");
		String sql = "INSERT INTO tbl_member ("
				+ "memberId, pwd, name, birthDate, ssn, "
				+ "gender, tel, email, grade, accessLevel) "
				//+ "agreeLocation, agreePromotion, agreeThirdparty, agreeChunjae) "
				+ "VALUES ("
				+ "?, ?, ?, ?, ?, "
				+ "?, ?, ?, ?, ?"
				+ ")"
				;
		try {
			pstm = con.prepareStatement(sql);
			pstm.setString(1, dto.getMemberId());
			pstm.setString(2, dto.getPwd());
			pstm.setString(3, dto.getName());
			pstm.setString(4, dto.getBirthdate());
			pstm.setString(5, dto.getSsn());
			pstm.setString(6, dto.getGender());
			pstm.setString(7, dto.getTel());
			//pstm.setString(8, dto.getAddr1());
			//pstm.setString(9, dto.getAddr2());
			pstm.setString(8, dto.getEmail());
			//pstm.setString(11, dto.getMemberType());
			pstm.setInt(9, dto.getGrade());
			pstm.setString(10, dto.getAccessLevel());
//			pstm.setString(14, dto.getAgreeLocation());
//			pstm.setString(15, dto.getAgreePromotion());
//			pstm.setString(16, dto.getAgreeThirdparty());
//			pstm.setString(17, dto.getAgreeChunjae());
			return pstm.executeUpdate();
		}catch(Exception e) {
			System.out.println(e.getMessage());
		}
		System.out.println("MemberDAO > setMemberRegist > end");
		System.out.println("-----------------------------------------");
		return 0;
	}

	//
	public int registTeacher(MemberDTO dto) {
		System.out.println("-----------------------------------------");
		System.out.println("MemberDAO > setMemberRegist > start");
		String sql = "INSERT INTO tbl_member ("
				+ "memberId, pwd, name, birthDate, ssn, "
				+ "gender, tel, addr1, addr2, "
				+ "email, memberType, grade, accessLevel, "
				+ "agreeLocation, agreePromotion, agreeThirdparty, agreeChunjae) "
				+ "VALUES ("
				+ "?, ?, ?, ?, ?, "
				+ "?, ?, ?, ?, "
				+ "?, ?, ?, ?, "
				+ "?, ?, ?, ?"
				+ ")"
				;
		try {
			pstm = con.prepareStatement(sql);
			pstm.setString(1, dto.getMemberId());
			pstm.setString(2, dto.getPwd());
			pstm.setString(3, dto.getName());
			pstm.setString(4, dto.getBirthdate());
			pstm.setString(5, dto.getSsn());
			pstm.setString(6, dto.getGender());
			pstm.setString(7, dto.getTel());
			pstm.setString(8, dto.getAddr1());
			pstm.setString(9, dto.getAddr2());
			pstm.setString(10, dto.getEmail());
			pstm.setString(11, dto.getMemberType());
			pstm.setInt(12, dto.getGrade());
			pstm.setString(13, "T");
			pstm.setString(14, dto.getAgreeLocation());
			pstm.setString(15, dto.getAgreePromotion());
			pstm.setString(16, dto.getAgreeThirdparty());
			pstm.setString(17, dto.getAgreeChunjae());																																																															
			return pstm.executeUpdate();
		}catch(Exception e) {
			System.out.println(e.getMessage());
		}
		System.out.println("MemberDAO > setMemberRegist > end");
		System.out.println("-----------------------------------------");
		return 0;
	}
	
	//아이디로 회원조회 회원 존재하지 않으면 null 반환
	public MemberDTO getMemberById(String memberId) {
		System.out.println("-----------------------------------------");
		System.out.println("MemberDAO > getMemberById > start");
		String sql = "SELECT "
				+ "memberId, pwd, name, birthDate, ssn, "
				+ "gender, tel, addr1, addr2, point, "
				+ "email, memberType, grade, agreeLocation, agreePromotion, "
				+ "agreeThirdparty, agreeChunjae, status, accessLevel, regDate, "
				+ "modifyDate, leaveDate "
				+ "FROM tbl_member WHERE memberId=?";
		try {
			pstm = con.prepareStatement(sql);
			pstm.setString(1, memberId);
			rs = pstm.executeQuery();
			if(rs.next()) {
				MemberDTO dto = new MemberDTO();
				dto.setMemberId(rs.getString("memberId"));
				dto.setPwd(rs.getString("pwd"));
				dto.setName(rs.getString("name"));
				dto.setBirthdate(rs.getString("birthDate"));
				dto.setSsn(rs.getString("ssn"));
				dto.setGender(rs.getString("gender"));
				dto.setTel(rs.getString("tel"));
				dto.setAddr1(rs.getString("addr1"));
				dto.setAddr2(rs.getString("addr2"));
				dto.setPoint(rs.getInt("point"));
				dto.setEmail(rs.getString("email"));
				dto.setMemberType(rs.getString("memberType"));
				dto.setGrade(rs.getInt("grade"));
				dto.setAgreeLocation(rs.getString("agreeLocation"));
				dto.setAgreePromotion(rs.getString("agreePromotion"));
				dto.setAgreeThirdparty(rs.getString("agreeThirdparty"));
				dto.setAgreeChunjae(rs.getString("agreeChunjae"));
				dto.setStatus(rs.getString("status"));
				dto.setAccessLevel(rs.getString("accessLevel"));
				dto.setRegDate(rs.getTimestamp("regDate").toLocalDateTime());
				if(rs.getTimestamp("modifyDate")!=null)dto.setModifyDate(rs.getTimestamp("modifyDate").toLocalDateTime());
				if(rs.getTimestamp("leaveDate")!=null)dto.setLeaveDate(rs.getTimestamp("leaveDate").toLocalDateTime());
				return dto;
			}
		}catch(Exception e) {
			System.out.println("MemberDAO > getMemberById > error");
			System.out.println(e.getMessage());
		}
		System.out.println("MemberDAO > getMemberById > end");
		System.out.println("-----------------------------------------");
		return null;
	}
	
	//아이디 찾기
	//이름 + 휴대폰번호로 회원 조회(아이디찾기)
	public List<MemberDTO> findId(String name, String tel) {
		System.out.println("=================================");
		System.out.println("MemberDAO > findId > start");
		String sql = "SELECT "
				+ "memberId, pwd, name, birthDate, ssn, "
				+ "gender, tel, addr1, addr2, point, "
				+ "email, memberType, grade, agreeLocation, agreePromotion, "
				+ "agreeThirdparty, agreeChunjae, status, accessLevel, regDate, "
				+ "modifyDate, leaveDate "
				+ "FROM tbl_member WHERE name=? AND tel=?";
		try {
			pstm = con.prepareStatement(sql);
			pstm.setString(1, name);
			pstm.setString(2, tel);
			rs = pstm.executeQuery();
			List<MemberDTO> list = new ArrayList<MemberDTO>();
			while(rs.next()) {				
				MemberDTO dto = new MemberDTO();
				dto.setMemberId(rs.getString("memberId"));
				dto.setPwd(rs.getString("pwd"));
				dto.setName(rs.getString("name"));
				dto.setBirthdate(rs.getString("birthDate"));
				dto.setSsn(rs.getString("ssn"));
				dto.setGender(rs.getString("gender"));
				dto.setTel(rs.getString("tel"));
				dto.setAddr1(rs.getString("addr1"));
				dto.setAddr2(rs.getString("addr2"));
				dto.setPoint(rs.getInt("point"));
				dto.setEmail(rs.getString("email"));
				dto.setMemberType(rs.getString("memberType"));
				dto.setGrade(rs.getInt("grade"));
				dto.setAgreeLocation(rs.getString("agreeLocation"));
				dto.setAgreePromotion(rs.getString("agreePromotion"));
				dto.setAgreeThirdparty(rs.getString("agreeThirdparty"));
				dto.setAgreeChunjae(rs.getString("agreeChunjae"));
				dto.setStatus(rs.getString("status"));
				dto.setAccessLevel(rs.getString("accessLevel"));
				dto.setRegDate(rs.getTimestamp("regDate").toLocalDateTime());
				if(rs.getTimestamp("modifyDate")!=null)dto.setModifyDate(rs.getTimestamp("modifyDate").toLocalDateTime());
				if(rs.getTimestamp("leaveDate")!=null)dto.setLeaveDate(rs.getTimestamp("leaveDate").toLocalDateTime());
				list.add(dto);		
			}
			return list;
		}catch(Exception e) {
			System.out.println("MemberDAO > findId > error");
			System.out.println(e.getMessage());
		}
		
		System.out.println("MemberDAO > findId > end");
		System.out.println("=================================");
		return null;
	}
	//아이디 + 이름 + 휴대폰번호로 회원 조회(비밀번호 찾기)
	public MemberDTO findPwd(String memberId, String name, String tel) {
		System.out.println("=================================");
		System.out.println("MemberDAO > findPwd > start");
		try {
			MemberDTO dto = getMemberById(memberId);
			if(dto==null) {
				return null;
			}
			if(dto.getName().equals(name)&&dto.getTel().equals(tel)){
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
	//회원리스트 조회->관리자
	public List<MemberDTO> getMemberList(){
		return null;
	}
	//회원 수정
	public int modify (MemberDTO dto) {
		System.out.println("-----------------------------------------");
		System.out.println("MemberDAO > modify > start");
		String sql = "UPDATE tbl_member SET "
				+ "pwd=?, tel=?, addr1=?, addr2=?, email=?, "
				+ "memberType=?, grade=?, agreeLocation=?, agreePromotion=?, agreeThirdparty=?, "
				+ "agreeChunjae=?, status=?, modifyDate=NOW() "
				+ "WHERE memberId = ?";
		try {
			pstm=con.prepareStatement(sql);
			pstm.setString(1, dto.getPwd());
			pstm.setString(2, dto.getTel());
			pstm.setString(3, dto.getAddr1());
			pstm.setString(4, dto.getAddr2());
			pstm.setString(5, dto.getEmail());
			pstm.setString(6, dto.getMemberType());
			pstm.setInt(7, dto.getGrade());
			pstm.setString(8,dto.getAgreeLocation());
			pstm.setString(9, dto.getAgreePromotion());
			pstm.setString(10, dto.getAgreeThirdparty());
			pstm.setString(11, dto.getAgreeChunjae());
			pstm.setString(12, dto.getStatus());
			pstm.setString(13, dto.getMemberId());
			return pstm.executeUpdate();
		}catch(Exception e) {
			System.out.println("MemberDAO > modify > error");
			System.out.println(e.getMessage());
		}
		System.out.println("MemberDAO > modify > end");
		System.out.println("-----------------------------------------");
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
	// 로그아웃
}
