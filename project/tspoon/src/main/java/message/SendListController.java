package net.tclass.message;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import net.tclass.member.MemberDTO;
import net.tclass.utils.CommonUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Servlet implementation class SendListController
 */
public class SendListController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SendListController() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		MemberDTO loginDto = (MemberDTO)request.getSession().getAttribute("loginDto");
		String memberId = loginDto.getMemberId();
		String tempCountStr = request.getParameter("tempCnt");
		MessageDAO dao = new MessageDAO();
		int tempCount = CommonUtil.ifNullInt(tempCountStr, 0);
		int pageSize = 5;
		int pageNo = tempCount/pageSize + 1;
		int printCnt = pageNo*pageSize;
		Map<String,String> map = new HashMap<String,String>();
		map.put("sendId", memberId);
		map.put("pageSize", String.valueOf(pageSize));
		map.put("printCnt", String.valueOf(printCnt));
		map.put("category", "send");
		List<MessageDTO> messageList = dao.getSendList(map);
		dao.close();
		System.out.println(map);
		System.out.println(messageList);
		request.setAttribute("map", map);
		request.setAttribute("messageList", messageList);
		request.getRequestDispatcher("./list.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
