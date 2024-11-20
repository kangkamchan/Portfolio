package net.tclass.login;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import net.tclass.member.MemberDAO;
import net.tclass.member.MemberDTO;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Servlet implementation class FindIdController
 */
public class FindIdController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public FindIdController() {
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
		String name = request.getParameter("iname");
		String phone = request.getParameter("phone");
		MemberDAO dao = new MemberDAO();
		MemberDTO memberFound = dao.findId(name, phone);
		if(memberFound==null) {
			request.getRequestDispatcher("./findIdResult.jsp").forward(request, response);
			return;
		}
		String memberIdFound = memberFound.getMemberId();
		LocalDateTime regDateFound = memberFound.getRegDate();
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yy.MM.dd");
		String regDateStr = regDateFound.format(dtf);
		 
		
		System.out.println("memberIdFound : " + memberIdFound);
		System.out.println("regDateStr : " + regDateStr);
		
		request.setAttribute("memberIdFound", memberIdFound);
		request.setAttribute("regDateFound", regDateStr);
		
		System.out.println("FindIdController > doPost > end");
		System.out.println("===================================");
		
		request.getRequestDispatcher("./findIdResult.jsp").forward(request, response);
		
	}

}

