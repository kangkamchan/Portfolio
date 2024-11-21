package net.haebup.utils;
import net.haebup.member.MemberDAO;
import net.haebup.member.MemberDTO;
/**
 * 아이디 중복 체크
 * 아이디
 * 비밀번호
 * 이메일
 * 이름
 * 로그인 검증
 * 회원가입 검증
 */
public class Validation {
	
//	public boolean validMemberRegist(String memberId, String pwd, String email, String name) {
//		return checkMemberId(memberId) && checkId(memberId) && checkPwd(pwd) && checkEmail(email);
//	}
	
	// 로그인 검증 (기본 형식 체크)
    public boolean validateLogin(String memberId, String pwd) {
        return memberId != null && pwd != null && !pwd.isEmpty();
    }
    
	//영문+숫자5~20자, 공백 불가
	public boolean checkId(String memberId) {
		if (memberId == null || memberId.contains(" ")){
			return false;
		}
		String regex = "^[a-zA-Z0-9]{5,20}$";
		return memberId.matches(regex);
	}
	
	//공백 불가, 8자리 이상, 15자리 이하 특수문자 포함, 영어, 숫자 가능
	public boolean checkPwd(String pwd) {
	    // 비밀번호가 null이거나 공백이 포함되면 false 반환
	    if (pwd == null || pwd.contains(" ")) {
	        return false;
	    }
	    // 비밀번호 길이 체크: 10자 이상, 16자 이하
	    if (pwd.length() < 10 || pwd.length() > 16) {
	        return false;
	    }
	    // 비밀번호에 특수문자, 영문자, 숫자가 모두 포함되어 있는지 체크
	    if (!pwd.matches(".*[!@#$%^&*(),.?\":{}|<>].*") || !pwd.matches(".*[a-zA-Z].*") || !pwd.matches(".*[0-9].*")) {
	        return false;
	    }
	    // 모든 조건을 통과하면 true 반환
	    return true;
	}
	
	//공백불가, @포함여부, 영문, 숫자, 한글 가능
	public boolean checkEmail(String email) {
		  if (email == null || email.contains(" ")) {
		        return false;
		    }
		// 이메일 형식에 맞는지 정규식으로 확인
		// 이메일 형식: 영문자 또는 숫자 + @ + 도메인 형식 (예: example@domain.com)
		String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";
		System.out.println("3");
		return email.matches(emailRegex);
	}
	
    // 이름이 null이 아니고, 공백이 없으며, 숫자와 특수문자가 포함되지 않음
    // 자음/모음만 입력되지 않도록 한글 조합 문자를 확인하는 정규식 추가
	public boolean checkName(String name) {
		if (name == null || name.contains(" ")){
			return false;
		}
		String regex = "^[가-힣]{2,5}$";
		return name.matches(regex);				
	}
	
	//아이디 중복 체크 중복이면  true 아니면 false
	public boolean exist(String memberId) {
		//dao 연결
		MemberDAO dao = new MemberDAO();
		MemberDTO dto = dao.getMemberById(memberId);
		dao.close();
		if(dto!=null) {
			return true;
		}else {
			return false;
		}
	}
}