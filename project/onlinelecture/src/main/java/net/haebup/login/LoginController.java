package net.haebup.login;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import net.haebup.member.MemberDAO;
import net.haebup.member.MemberDTO;
import net.haebup.utils.CommonUtils;
import net.haebup.utils.CookieManager;
import net.haebup.utils.JSFunc;

import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * Servlet implementation class LoginController
 */
@WebServlet("/login/login.do")
public class LoginController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LoginController() {
        super();
        // TODO Auto-generated constructor stub
    }
    //URLEncoder.encode(, "UTF-8")
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Referer 헤더에서 URI 추출 후 두 번 인코딩
        String referer = request.getHeader("referer");
        String refererUri = referer != null ? CommonUtils.getRefererUri(referer) : "";

        if (refererUri != null && !refererUri.isEmpty()) {
        	refererUri = URLEncoder.encode(URLEncoder.encode(refererUri, "UTF-8"), "UTF-8"); // 두 번 인코딩
        }
        
        System.out.println("Double Encoded refererUri : " + refererUri);
        
        response.sendRedirect("./login.jsp?refererUri=" + refererUri);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();

        // 두 번 디코딩하여 refererUri 획득
        String refererUri = request.getParameter("refererUri");
        System.out.println(refererUri);
        if (refererUri != null && !refererUri.isEmpty()) {
            refererUri = URLDecoder.decode(URLDecoder.decode(refererUri, "UTF-8"), "UTF-8"); // 두 번 디코딩
        }
        System.out.println("refererUri : "+refererUri);
        String memberId = request.getParameter("memberId");
        String pwd = request.getParameter("pwd");
        String saveChk = request.getParameter("saveChk");

        MemberDAO dao = new MemberDAO();
        MemberDTO dto = dao.getMemberById(memberId);
        dao.close();

        if (dto == null) {
            JSFunc.alertBack("존재하지 않는 회원아이디입니다.", response);
            return;
        }
        if (!pwd.equals(dto.getPwd())) {
            JSFunc.alertBack("비밀번호가 일치하지 않습니다.", response);
            return;
        }
        if (dto.getStatus().equals("N")) {
            JSFunc.alertBack("탈퇴한 회원입니다.", response);
            return;
        }

        // 쿠키 저장 로직
        if (saveChk != null && saveChk.equals("Y")) {
            CookieManager.makeCookie("savedId", dto.getMemberId(), 3600 * 24 * 7, "/", response);
        } else {
            String savedId = CookieManager.cookieValue("savedId", request);
            if (!savedId.isEmpty() && savedId.equals(dto.getMemberId())) {
                CookieManager.removeCookie("savedId", response);
            }
        }

        session.setAttribute("loginDto", dto);
        System.out.println("Login > doPost > Double Decoded refererUri : " + refererUri);

        // 로그인 후 리다이렉트 처리
        if (refererUri != null && !refererUri.isEmpty() && !refererUri.contains("/member/regist") && !refererUri.contains("/member/find")) {
            response.sendRedirect(refererUri);
        } else {
            response.sendRedirect("../main.do");
        }
    }

}
	
