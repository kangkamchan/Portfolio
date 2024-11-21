package net.tclass.qna;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import net.tclass.utils.CommonUtil;
import net.tclass.utils.JSFunc;

import java.io.IOException;

/**
 * Servlet implementation class ViewController
 */
@WebServlet(name = "QnaViewController", urlPatterns = { "/qna/view.do" })
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
		String referer = request.getHeader("referer");
		String refererUri = "";
		if(referer!=null) {
			refererUri = referer.substring(referer.indexOf("://")+3).substring(referer.substring(referer.indexOf("://")+3).indexOf("/"));
		}
		int idx = CommonUtil.ifNullInt(request.getParameter("idx"), 0);
		QnaDAO dao = new QnaDAO();
		QnaDTO dto = dao.getQnaByIdx(idx);
		dao.close();
		if(dto==null) {
			JSFunc.alertBack("게시글 조회 실패", response);
		}
		request.setAttribute("refererUri",refererUri);
		request.setAttribute("qna", dto);
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
