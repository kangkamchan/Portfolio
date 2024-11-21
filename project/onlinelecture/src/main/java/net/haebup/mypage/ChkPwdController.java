package net.haebup.mypage;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import net.haebup.member.MemberDAO;
import net.haebup.member.MemberDTO;
import net.haebup.utils.JSFunc;

import java.io.IOException;

/**
 * Servlet implementation class ChkPwdController
 */
@WebServlet("/mypage/chkPwd.do")
public class ChkPwdController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ChkPwdController() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.sendRedirect("./chkPwd.jsp");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String memberId = request.getParameter("memberId");
		String pwd = request.getParameter("pwd");
		MemberDAO dao = new MemberDAO();
		MemberDTO dto = dao.getMemberById(memberId);
		dao.close();
		if(!pwd.equals(dto.getPwd())) {
			JSFunc.alertBack("비밀번호가 일치하지 않습니다.", response);
			return;
		}
		response.sendRedirect("./view.do?action=modify&memberId="+memberId);
	}

}
