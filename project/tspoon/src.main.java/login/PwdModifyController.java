package net.tclass.login;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import net.tclass.member.MemberDAO;
import net.tclass.member.MemberDTO;
import net.tclass.utils.JSFunc;

import java.io.IOException;

/**
 * Servlet implementation class PwdModifyController
 */
public class PwdModifyController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public PwdModifyController() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String memberId = request.getParameter("memberId");
		String pwd = request.getParameter("pwd");
		MemberDAO dao = new MemberDAO();
		MemberDTO dto = dao.getMemberById(memberId);
		if(dto==null) {
			dao.close();
			response.sendRedirect("../common/error.do");
			return;
		}
		dto.setPwd(pwd);
		int result = dao.modify(dto);
		dao.close();
		if(result==0) {
			response.sendRedirect("../common/error.do");
			return;
		}
		JSFunc.alert("변경된 비밀번호로 로그인 하세요", response);
		response.sendRedirect("./login.do");
	}

}
