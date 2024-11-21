package net.haebup.member;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import net.haebup.utils.JSFunc;

import java.io.IOException;

/**
 * Servlet implementation class MemberFindPwdController
 */
@WebServlet("/member/findPwd.do")
public class MemberFindPwdController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MemberFindPwdController() {
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
		String action = request.getParameter("action");
		if(action.equals("find")) {
			String memberId = request.getParameter("memberId");
			String name = request.getParameter("name");
			String tel = request.getParameter("tel");
			
			MemberDAO dao = new MemberDAO();
			MemberDTO dto = dao.findPwd(memberId, name, tel);
			dao.close();
			if(dto==null) {
				request.setAttribute("result", "fail");
				request.getRequestDispatcher("findPwd.jsp").forward(request, response);
				return;
			}
			request.setAttribute("result", "success");
			request.setAttribute("memberIdFound", dto.getMemberId());
			request.getRequestDispatcher("findPwd.jsp").forward(request, response);
		}else if(action.equals("modify")) {
			String memberId = request.getParameter("memberId");
			MemberDAO dao = new MemberDAO();
			MemberDTO dto = dao.getMemberById(memberId);
			if(dto==null) {
				dao.close();
				JSFunc.alertBack("정상적인 접근이 아닙니다.", response);
				return;
			}
			dto.setPwd(request.getParameter("pwd"));
			int result = dao.modify(dto);
			dao.close();
			if(result<=0) {
				JSFunc.alertBack("비밀번호변경실패", response);
				return;
			}
			response.sendRedirect("../login/login.do");
			JSFunc.alert("변경된 비밀번호로 로그인하세요", response);
			return;
		}else {
			JSFunc.alertBack("정상적인 접근이 아닙니다.", response);
		}
	}

}
