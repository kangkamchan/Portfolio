package net.tclass.login;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import net.tclass.member.MemberDAO;
import net.tclass.member.MemberDTO;

import java.io.IOException;

/**
 * Servlet implementation class FindPwdController
 */
public class FindPwdController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public FindPwdController() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.sendRedirect("./findPwd.jsp");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String memberId = request.getParameter("idn");
		String name = request.getParameter("iname");
		String phone = request.getParameter("phone");
		
		MemberDAO dao = new MemberDAO();
		MemberDTO dto = dao.findPwd(memberId, name, phone);
		dao.close();
		if(dto==null) {
			request.getRequestDispatcher("findPwdResult.jsp").forward(request, response);
			return;
		}
		request.setAttribute("memberIdFound", dto.getMemberId());
		request.getRequestDispatcher("findPwdResult.jsp").forward(request, response);
	}
}
