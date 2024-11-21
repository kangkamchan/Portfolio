package net.haebup.member;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import net.haebup.utils.CommonUtils;
import net.haebup.utils.Validation;

import java.io.IOException;

/**
 * Servlet implementation class MemberRegistController
 */
@WebServlet("/member/regist.do")
public class MemberRegistController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MemberRegistController() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.sendRedirect("./regist.jsp");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String name = request.getParameter("name");
		String memberId = request.getParameter("memberId");
		String pwd = request.getParameter("pwd");
		String birthdate = request.getParameter("birthYear") + "-" + request.getParameter("birthMonth") + "-" + request.getParameter("birthDate");
		String tel = request.getParameter("tel");
		String email = request.getParameter("email");
		int grade = CommonUtils.ifNullInt(request.getParameter("grade"),1);
		String gender = request.getParameter("gender");
		MemberDTO dto = new MemberDTO();
		MemberDAO dao = new MemberDAO();
		dto.setName(name);
		dto.setMemberId(memberId);
		dto.setPwd(pwd);
		dto.setBirthdate(birthdate);
		dto.setTel(tel);
		dto.setEmail(email);
		dto.setGrade(grade);
		dto.setGender(gender);
		dto.setAccessLevel("S");
		Validation val = new Validation();
		/*valMsg
		 * idDup : 아이디중복
		 * idFail : 아이디 검증 실패
		 * nameFail : 이름 검증 실패
		 * pwdFail : 비밀번호 검증 실패
		 */
		if(dao.getMemberById(memberId)!=null) {
			dao.close();
			request.setAttribute("valMsg", "idDup"); 
			request.setAttribute("tempDto",dto);
			request.getRequestDispatcher("./regist.jsp").forward(request, response);
			return;
		}
		
		if(!val.checkId(memberId)) {
			dao.close();
			request.setAttribute("valMsg", "idFail");
			request.setAttribute("tempDto",dto);
			request.getRequestDispatcher("./regist.jsp").forward(request, response);
			return;
		}
		if(!val.checkName(name)) {
			dao.close();
			request.setAttribute("valMsg", "nameFail");
			request.setAttribute("tempDto",dto);
			request.getRequestDispatcher("./regist.jsp").forward(request, response);
			return;
		}
		if(!val.checkPwd(pwd)) {
			dao.close();
			request.setAttribute("valMsg", "pwdFail");
			request.setAttribute("tempDto",dto);
			request.getRequestDispatcher("./regist.jsp").forward(request, response);
			return;
		}
		int result = dao.regist(dto);
		dao.close();
		if(result <= 0) {
			request.setAttribute("errMsg", "dbFail");
			request.setAttribute("tempDto",dto);
			request.getRequestDispatcher("./regist.jsp").forward(request, response);
			return;
		}
			response.sendRedirect("../login/login.do");
	}
}
