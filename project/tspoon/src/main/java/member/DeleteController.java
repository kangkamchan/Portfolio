package net.tclass.member;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import net.tclass.utils.CookieManager;

import java.io.IOException;

/**
 * Servlet implementation class DeleteController
 */
@WebServlet(name = "MemberDeleteController", urlPatterns = { "/member/delete.do" })
public class DeleteController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DeleteController() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String memberId = request.getParameter("memberId");
		MemberDAO dao= new MemberDAO();
		dao.delete(memberId);
		dao.close();
		if(CookieManager.cookieValue("savedId", request).equals(memberId)) {
			CookieManager.removeCookie("savedId", response);
		}
		response.sendRedirect("../login/logout.do");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
