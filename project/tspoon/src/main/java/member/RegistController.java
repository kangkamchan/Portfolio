package net.tclass.member;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import net.tclass.utils.JSFunc;

import java.io.IOException;

/**
 * Servlet implementation class RegistController
 */
public class RegistController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public RegistController() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.sendRedirect("./regist.jsp?pageHeadLine=SignIn&topLeftBtnType=btnSide");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String name = request.getParameter("iname");
		String memberId = request.getParameter("idn");
		String pwd = request.getParameter("pwd");
		String birthDate = request.getParameter("birthYear") + "-" + request.getParameter("birthMonth") + "-" + request.getParameter("birthDate");
		String phone = request.getParameter("phone");
		String email = request.getParameter("emailId") + "@" + request.getParameter("emailUrl");
		String interest = request.getParameter("interest");
		String grade = request.getParameter("grade");
		String gender = request.getParameter("rr");
		String[] agree = request.getParameterValues("agree");
		MemberDTO dto = new MemberDTO();
		MemberDAO dao = new MemberDAO();
		dto.setName(name);
		dto.setMemberId(memberId);
		dto.setPwd(pwd);
		dto.setBirthDate(birthDate);
		dto.setPhone(phone);
		dto.setEmail(email);
		dto.setInterest(interest);
		dto.setGender(grade);
		dto.setGender(gender);
		if(agree!=null&&agree.length>0) {
			for(String s : agree) {
				switch(s) {
				case "1" : dto.setAgreeLocation("Y"); break;
				case "2" : dto.setAgreePromotion("Y"); break;
				case "3" : dto.setAgreeThirdparty("Y"); break;
				case "4" : dto.setAgreeChunjae("Y"); break;
				default : break;
				}
			}
		}
		if(dao.getMemberById(memberId)!=null) {
			dao.close();
			request.setAttribute("tempDto",dto);
			request.getRequestDispatcher("./regist.jsp").forward(request, response);
			return;
		}
		int result = dao.regist(dto);
		dao.close();
		if(result>0) {
			response.sendRedirect("../login/login.do");
		}else {
			request.setAttribute("tempDto",dto);
			request.getRequestDispatcher("./regist.jsp?pageHeadLine=SignIn&topLeftBtnType=btnSide").forward(request, response);
		}
	}
}
