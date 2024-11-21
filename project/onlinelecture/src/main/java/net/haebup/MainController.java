package net.haebup;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import net.haebup.utils.CommonUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Servlet implementation class MainController
 */
@WebServlet("/main.do")
public class MainController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MainController() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    System.out.println("MainController > doGet > start");
	    String action = CommonUtils.ifNullString(request.getParameter("action"), "");
		if(action.isEmpty()) {
			System.out.println("MainController > doGet > action_empty");
			Map<String,String> map = new HashMap<String,String>();
			map.put("reqUri", "/main.do?");
			map.put("category", "N");
			map.put("pageNo", CommonUtils.ifNullString(request.getParameter("pageNo"), "1"));
			map.put("pageSize", CommonUtils.ifNullString(request.getParameter("pageSize"), "5"));
			map.put("searchWord", CommonUtils.ifNullString(request.getParameter("searchWord"), ""));
			map.put("searchCategory", CommonUtils.ifNullString(request.getParameter("searchCategory"), ""));
			map.put("whereWord", CommonUtils.ifNullString(request.getParameter("whereWord"), ""));
			map.put("whereValue", CommonUtils.ifNullString(request.getParameter("whereValue"), ""));
			map.put("levelIdx", CommonUtils.ifNullString(request.getParameter("levelIdx"), "0"));
			request.setAttribute("map", map);
			request.getRequestDispatcher("./bbs/list.do").forward(request, response);
		}else if(action.equals("bbsListOk")){
			System.out.println("MainController > doGet > bbsListOk");
			request.setAttribute("bbsList",request.getAttribute("bbsList"));
			request.getRequestDispatcher("./index.jsp").forward(request,response);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
