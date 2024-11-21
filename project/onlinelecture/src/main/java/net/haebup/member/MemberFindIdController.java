package net.haebup.member;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Servlet implementation class MemberFindIdController
 */
@WebServlet("/member/findId.do")
public class MemberFindIdController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MemberFindIdController() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.sendRedirect("./findId.jsp");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("===================================");
		System.out.println("FindIdController > doPost > start");
		String name = request.getParameter("name");
		String tel = request.getParameter("tel");
		MemberDAO dao = new MemberDAO();
		List<MemberDTO> foundMemberList = dao.findId(name, tel);
		dao.close();
		if(foundMemberList.isEmpty()) {
			request.setAttribute("result", "fail");
			request.getRequestDispatcher("./findId.jsp").forward(request, response);
			return;
		}
		request.setAttribute("result", "success");
		request.setAttribute("foundList", foundMemberList);
		System.out.println("FindIdController > doPost > end");
		System.out.println("===================================");
		request.getRequestDispatcher("./findId.jsp").forward(request, response);
		
	}

}
