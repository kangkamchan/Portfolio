package net.haebup.member;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import net.haebup.utils.JSFunc;

import java.io.IOException;

/**
 * Servlet implementation class MemberModifyController
 */
@WebServlet("/member/modify.do")
public class MemberModifyController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MemberModifyController() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("===========================");
		System.out.println("ModifyController > doPost > start");
		String memberId = request.getParameter("memberId");
		String pwd = request.getParameter("pwd");
		String tel = request.getParameter("tel");
		String addr1 = request.getParameter("addr1");
		String addr2 = request.getParameter("addr2");
		String email = request.getParameter("email");
		String memberType = request.getParameter("memberType");
		String grade = request.getParameter("grade");
		MemberDAO dao = new MemberDAO();
		MemberDTO dto = dao.getMemberById(memberId);
		boolean flag = false;
		if(dto==null) {
			dao.close();
			JSFunc.alertBack("정상적인접근이 아닙니다.",response);
			System.out.println("ModifyController > doPost > end");
			System.out.println("===========================");
			return;
		}
		if(pwd!=null&&!pwd.equals("")&&!pwd.equals(dto.getPwd())) {
			
			dto.setPwd(pwd);
			flag=true;
		}
		if(tel!=null&&!tel.equals("")&&!tel.equals(dto.getTel())) {
			dto.setTel(tel);
			flag=true;
		}
		if(addr1!=null&&!addr1.equals("")&&!addr1.equals(dto.getAddr1())) {
			dto.setAddr1(addr1);
			flag=true;
		}
		if(addr2!=null&&!addr2.equals("")&&!addr2.equals(dto.getAddr2())) {
			flag=true;
		}
		if(email!=null&&!email.equals("")&&!email.equals(dto.getEmail())) {
			dto.setEmail(email);
			flag=true;
		}
		if(memberType!=null&&!memberType.equals("")&&!memberType.equals(dto.getMemberType())) {
			dto.setMemberType(memberType);
			flag=true;
		}
		if(grade!=null&&!grade.equals("")&&!grade.equals(String.valueOf(dto.getGrade()))) {
			dto.setGrade(Integer.parseInt(grade));
			flag=true;
		}
		if(!flag) {
			JSFunc.alertLocation("변경사항이 없습니다","../mypage/view.do?action=modify",response);
			dao.close();
			return;
		}
		int result = dao.modify(dto);
		dao.close();
		if(result<=0) {
			JSFunc.alertLocation("변경사항이 없습니다","../mypage/view.do?action=modify",response);
			return;
		}
//		request.setAttribute("tempDto", dto);
		response.sendRedirect("../mypage/view.do?action=modify&modifyOk=true&memberId="+memberId);
		System.out.println("ModifyController > doPost > end");
		System.out.println("===========================");
	}

}
