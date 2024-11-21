package net.tclass.message;

import jakarta.servlet.ServletException;
//import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import net.tclass.member.MemberDAO;
import net.tclass.member.MemberDTO;
import net.tclass.utils.CommonUtil;
import net.tclass.utils.JSFunc;

import java.io.IOException;

/**
 * Servlet implementation class ViewController
 */
public class ViewController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ViewController() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		MemberDTO loginDto = (MemberDTO)request.getSession().getAttribute("loginDto");
		int idx = CommonUtil.ifNullInt(request.getParameter("idx"), 0);
		MessageDAO dao = new MessageDAO();
		MessageDTO dto = dao.getMessage(idx);
		
		if(dto==null) {
			dao.close();
			JSFunc.alertBack("메시지 조회에 실패했습니다", response);
			return;
		}
		MemberDAO mDao = new MemberDAO();
		MemberDTO receiveDto = mDao.getMemberById(dto.getReceiveId());
		String receiveName = receiveDto.getName();
		if(loginDto.getMemberId().equals(dto.getReceiveId())) {
			if(dto.getReceiveDate()==null || dto.getReceiveDateStr().isEmpty()) {
				dao.updateRecieveDate(idx);
				dto = dao.getMessage(idx);
			}
		}
		
		dao.close();
		request.setAttribute("category", (loginDto.getMemberId().equals(dto.getSendId()) ? "send" : "receive"));
		request.setAttribute("message", dto);
		request.setAttribute("receiveName", receiveName);
		request.getRequestDispatcher("view.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
