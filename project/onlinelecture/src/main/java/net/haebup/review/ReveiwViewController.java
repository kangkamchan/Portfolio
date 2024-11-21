package net.haebup.review;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import net.haebup.bbs.BbsDAO;
import net.haebup.bbs.BbsDTO;
import net.haebup.utils.JSFunc;

import java.io.IOException;

@WebServlet("/reivew/view.do")
public class ReveiwViewController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       

	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		int idx = Integer.parseInt(req.getParameter("idx"));
		System.out.println("idx" + idx);
		BbsDAO dao = new BbsDAO();
		BbsDTO info = dao.getPostByIdx(idx);
		dao.close();
		System.out.println("info" + info);
		if(info != null) {
			req.setAttribute("post", info);
			req.setAttribute("action", "bbsViewOk");
			req.getRequestDispatcher("/review/view.jsp").forward(req, res);
		}
		else {
			JSFunc.alertBack("해당 내용을 찾을 수 없습니다.", res);
		}
	}


	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

	}

}
