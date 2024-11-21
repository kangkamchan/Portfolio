package net.haebup.material;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import net.haebup.bbs.BbsDAO;
import net.haebup.bbs.BbsDTO;

import java.io.IOException;

@WebServlet("/material/view.do")
public class MaterialViewController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       

	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		int idx = Integer.parseInt(req.getParameter("idx"));
		BbsDAO dao = new BbsDAO();
		BbsDTO dto = dao.getPostByIdx(idx);
		dao.close();
		req.setAttribute("post", dto);
		req.getRequestDispatcher("/material/view.jsp").forward(req, res);
	}


	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

	}

}
