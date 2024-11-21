package net.haebup.member;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import net.haebup.utils.CommonUtils;
import net.haebup.utils.CookieManager;
import net.haebup.utils.JSFunc;

import java.io.IOException;

/**
 * Servlet implementation class MemberDeleteController
 */
@WebServlet("/member/delete.do")
public class MemberDeleteController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MemberDeleteController() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String memberId = CommonUtils.ifNullString(request.getParameter("memberId"), "");
		MemberDAO dao= new MemberDAO();
		dao.delete(memberId);
		dao.close();
		if(CookieManager.cookieValue("savedId", request).equals(memberId)) {
			CookieManager.removeCookie("savedId", response);
		}
		JSFunc.alertLocation("회원 탈퇴 완료", "../login/logout.do", response);		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
