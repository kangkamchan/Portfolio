package net.haebup.bbs;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import net.haebup.utils.CommonUtils;

import java.io.IOException;

/**
 * Servlet implementation class BbsViewController
 */
@WebServlet("/bbs/view.do")
public class BbsViewController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public BbsViewController() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int idx = CommonUtils.ifNullInt(request.getParameter("idx"), 0);
		String reqUri = CommonUtils.ifNullString(request.getParameter("reqUri"), "");
		BbsDAO dao = new BbsDAO();
		BbsDTO dto = dao.getPostByIdx(idx);
		dao.close();
		request.setAttribute("post", dto);
		request.getRequestDispatcher("../"+reqUri+"?action=bbsViewOk&pageNo="+CommonUtils.ifNullString(request.getParameter("pageNo"),"1")).forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
