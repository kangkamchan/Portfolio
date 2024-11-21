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
 * Servlet implementation class DeleteController
 */
@WebServlet(name = "QnaDeleteController", urlPatterns = { "/qna/delete.do" })
public class DeleteController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DeleteController() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int idx = CommonUtil.ifNullInt(request.getParameter("idx"),0);
		QnaDAO dao = new QnaDAO();
		int result = dao.delete(idx);
		dao.close();
		if(result<=0) {
			JSFunc.alertBack("게시글 삭제 실패",response);
			return;
		}
		JSFunc.alertLocation("게시글 삭제 성공", "./list.do", response);
		return;
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
