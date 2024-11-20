package net.tclass.member;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import net.tclass.utils.JSFunc;

import java.io.IOException;

/**
 * Servlet implementation class ModifyController
 */
public class ModifyController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ModifyController() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		MemberDTO dto = (MemberDTO)request.getSession().getAttribute("loginDto");
		String[] birthArr = dto.getBirthDate().split("-");
		request.setAttribute("birthYear",birthArr[0]);
		request.setAttribute("birthMonth",birthArr[1]);
		request.setAttribute("birthDate",birthArr[2]);
		request.getRequestDispatcher("./modify.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("===========================");
		System.out.println("ModifyController > doPost > start");
		String memberId = request.getParameter("memberId");
		String pwd = request.getParameter("pwd");
		String phone = request.getParameter("phone");
		MemberDAO dao = new MemberDAO();
		MemberDTO dto = dao.getMemberById(memberId);
		boolean pwdFlag = false;
		boolean phoneFlag = false;
		System.out.println("memberId : " + memberId);
		System.out.println("pwd : " + pwd);
		System.out.println("phone : " + phone);
		if(dto==null) {
			dao.close();
			response.sendRedirect("../common/error.do");
			System.out.println("ModifyController > doPost > end");
			System.out.println("===========================");
			return;
		}
		if(pwd!=null){
			if(!pwd.equals(dto.getPwd())) {
				dto.setPwd(pwd);
				pwdFlag=true;
			}
		}
		if(phone!=null) {
			if(!phone.equals(dto.getPhone())) {
				dto.setPhone(phone);
				phoneFlag = true;
			}
		}
		if((!pwdFlag)&&(!phoneFlag)) {
			JSFunc.alertBack("변경된 내용이 없습니다.",response);
			dao.close();
			return;
		}
		int result = dao.modify(dto);
		dao.close();
		if(result==0) {
			response.sendRedirect("../common/error.do");
			System.out.println("ModifyController > doPost > end");
			System.out.println("===========================");
			return;
		}
		if(pwdFlag) {
			JSFunc.alertLocation("정상적으로 변경되었습니다. 변경된 비밀번호로 로그인 하세요","../login/logout.do" , response);
			System.out.println("ModifyController > doPost > end");
			System.out.println("===========================");
			return;
		}
		JSFunc.alertLocation("정상적으로 변경되었습니다","../common/main.do",response);
		System.out.println("ModifyController > doPost > end");
		System.out.println("===========================");
	}

}
