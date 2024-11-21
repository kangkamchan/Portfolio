package net.haebup.member;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.json.JSONObject;

/**
 * Servlet implementation class ChkIdDuplication
 */
@WebServlet("/member/chkIdDuplication.do")

public class ChkIdDuplication extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ChkIdDuplication() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		MemberDAO dao = new MemberDAO();
		String userId = request.getParameter("userId");
		boolean result = true;
		if(dao.getMemberById(userId)==null) {
			result = false;
		}
		dao.close();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write("{\"result\": " + result + "}");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String userId = request.getParameter("userId");

        // 데이터베이스를 통해 중복 여부를 확인하는 로직 (예시)
        boolean isAvailable = checkUserIdAvailability(userId); // 중복 확인 메서드 호출

        // JSON 형식으로 결과 반환
        JSONObject result = new JSONObject();
        result.put("isAvailable", isAvailable);

        response.setContentType("application/json");
        response.getWriter().write(result.toString());
	}
    private boolean checkUserIdAvailability(String userId) {
    	MemberDAO dao = new MemberDAO();
    	MemberDTO dto = dao.getMemberById(userId);
    	dao.close();
    	if(dto==null) {
    		return true;
    	}
    	return false;
    	
    }

}
