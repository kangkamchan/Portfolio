package net.haebup.bbs;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import net.haebup.utils.CommonUtils;
import net.haebup.utils.Paging;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Servlet implementation class BbsListController
 */
@WebServlet("/bbs/list.do")
public class BbsListController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public BbsListController() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Map<String, String> map = (Map<String, String>)request.getAttribute("map");
		BbsDAO dao = new BbsDAO();
		List<BbsDTO> bbsList = dao.getPostList(map);
		int totalCnt = dao.getTotalCount(map);
		dao.close();
		String queryString = Paging.getQueryString(map);
		map.put("queryString", queryString);
		map.put("totalCount", String.valueOf(totalCnt));
		String pagingBlock = Paging.getPageBlock(map, request);
		String pageNo =CommonUtils.ifNullString(map.get("pageNo"), "1");
		request.setAttribute("result", "success");
		request.setAttribute("bbsList", bbsList);
		request.setAttribute("pagingBlock", pagingBlock);
		request.getRequestDispatcher(reqUri.split("\\?")[0]+"?action=bbsListOk&pageNo="+pageNo).forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
