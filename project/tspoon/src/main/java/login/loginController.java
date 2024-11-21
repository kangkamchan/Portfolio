package net.tclass.login;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import net.tclass.member.MemberDAO;
import net.tclass.member.MemberDTO;
import net.tclass.utils.CookieManager;
import net.tclass.utils.JSFunc;

import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * Servlet implementation class loginController
 */
public class loginController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public loginController() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String referer = request.getHeader("referer");
		String refererUri = "";
		if(referer!=null) {
			refererUri = referer.substring(referer.indexOf("://")+3).substring(referer.substring(referer.indexOf("://")+3).indexOf("/"));
			refererUri = URLEncoder.encode(URLEncoder.encode(refererUri,"UTF-8"),"UTF-8");
		}
		System.out.println(refererUri);
		
		response.sendRedirect("./login.jsp?refererUri="+refererUri);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		String refererUri = request.getParameter("refererUri");
		String memberId = request.getParameter("idn");
		String pwd = request.getParameter("pwd");
		String saveChk = request.getParameter("saveChk");
		MemberDAO dao = new MemberDAO();
		MemberDTO dto = dao.login(memberId, pwd);
		dao.close();
		if(dto==null) {
			JSFunc.alertBack("아이디 또는 비밀번호가 적절하지 않거나 등록된 아이디가 아닙니다.", response);
			return;
		}
		if(dto.getStatus().equals("N")) {
			JSFunc.alertBack("탈퇴한 회원입니다.",response);
			return;
		}
		if(saveChk!=null&&saveChk.equals("Y")) {
			CookieManager.makeCookie("savedId", dto.getMemberId(), 3600*24*7, "/", response);
		}else {
			String savedId = CookieManager.cookieValue("savedId",request);
			if(!savedId.isEmpty()) {
				if(savedId.equals(dto.getMemberId())){
					CookieManager.removeCookie("savedId",response);
				}
			}
		}
		session.setAttribute("loginDto", dto);
		System.out.println("Login > doPost > refererUri" + refererUri);
		if(refererUri!=null&&!refererUri.isEmpty()&&!refererUri.contains("member%2Fregist")&&!refererUri.contains("login")) {
			refererUri=URLDecoder.decode(URLDecoder.decode(refererUri,"UTF-8"),"UTF-8");
			response.sendRedirect(refererUri);
			return;
		}
		response.sendRedirect("../common/main.do");
	}

}
