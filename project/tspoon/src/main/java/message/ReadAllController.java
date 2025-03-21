package net.tclass.message;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import net.tclass.utils.JSFunc;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Servlet implementation class ReadAllController
 */
@WebServlet("/message/readAll.do")
public class ReadAllController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ReadAllController() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String[] idxArr = request.getParameterValues("checkedMsg");
		List<Integer> idxList = new ArrayList<Integer>();
		for(String idx : idxArr) {
			idxList.add(Integer.parseInt(idx));
		}
		
		if(idxList.isEmpty()) {		
			JSFunc.alertBack("쪽지 읽기처리 실패", response);
			return;
		}
		MessageDAO dao = new MessageDAO();
		int result = dao.readMessageList(idxList);
		dao.close();
		if(result<=0) {
			JSFunc.alertBack("쪽지 읽기처리 실패", response);
			return;
		}
		
		request.getRequestDispatcher("./receiveList.do").forward(request, response);
	}

}
