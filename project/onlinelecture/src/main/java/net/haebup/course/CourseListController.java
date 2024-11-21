package net.haebup.course;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import net.haebup.utils.Pagination;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/course/list.do")
public class CourseListController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       

	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		//String action = req.getParameter("action");
		String subjectCode = req.getParameter("subjectCode");
		String searchKey = req.getParameter("searchKey");
		String searchValue= req.getParameter("searchValue");
		
		Map<String, String> map = new HashMap<String, String>();
		map.put("searchKey", searchKey);
		map.put("searchValue", searchValue);
		map.put("subjectCode", subjectCode);
		
		CourseDAO dao = new CourseDAO();
		int totalCnt = dao.getTotalCourseCnt(map);
		
		//페이징
		int pageNo = req.getParameter("page") == null ? 1 : Integer.parseInt(req.getParameter("page"));
		int pageSize = 6;
		int blockSize = 5;
		Pagination paging = new Pagination(pageNo, totalCnt, pageSize, blockSize);
		
		int startIdx = paging.getStartIndex();
		List<CourseDTO> list= dao.getCourseList(map, startIdx, pageSize);
		dao.close();
		
		//데이터 전달
		req.setAttribute("list", list);
		req.setAttribute("paging", paging);
		req.setAttribute("pageNo", pageNo);
		req.setAttribute("startBlock", paging.getStartBlockPage());
		req.setAttribute("endBlock", paging.getEndBlockPage());
		req.setAttribute("blockSize", blockSize);
		
		req.getRequestDispatcher("/course/list.jsp").forward(req, res);
	}


}
