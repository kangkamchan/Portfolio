package net.haebup.myclass;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import net.haebup.member.MemberDTO;
import net.haebup.score.ScoreDAO;
import net.haebup.score.ScoreDTO;
import net.haebup.utils.CommonUtils;
import net.haebup.utils.JSFunc;
import net.haebup.utils.Paging;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Servlet implementation class MyclassViewController
 */
@WebServlet("/myclass/view.do")
public class MyclassViewController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MyclassViewController() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("MyclassViewController doGet start");
		MemberDTO loginDto = (MemberDTO)request.getSession().getAttribute("loginDto");
		String action = CommonUtils.ifNullString(request.getParameter("action"),"courseList");
		if(action.equals("courseList")) {
			if(loginDto.getAccessLevel().equals("S")) {
				System.out.println("MyclassViewController courseList");
				String subjectCode = CommonUtils.ifNullString(request.getParameter("subjectCode"),"KO");
				int pageNo = CommonUtils.ifNullInt(request.getParameter("pageNo"), 1);
				request.getRequestDispatcher("./courseList.do?subjectCode="+subjectCode+"&pageNo="+pageNo).forward(request, response);
			}else if(loginDto.getAccessLevel().equals("T")) {
				System.out.println("MyclassViewController courseList");
				int pageNo = CommonUtils.ifNullInt(request.getParameter("pageNo"), 1);
				request.getRequestDispatcher("./courseList.do?pageNo="+pageNo).forward(request, response);
			}
		}else if(action.equals("scoreList")) {
			System.out.println("MyclassViewController scoreList");
			String memberId = loginDto.getMemberId();
			int pageNo = CommonUtils.ifNullInt(request.getParameter("pageNo"), 1);
			int pageSize = 6;
			Map<String,String> map = new HashMap<String,String>();
			map.put("pageNo", String.valueOf(pageNo));
			map.put("pageSize", String.valueOf(pageSize));
			map.put("memberId", memberId);
			ScoreDAO dao = new ScoreDAO();
			List<ScoreDTO> list = dao.getScoreList(map);
			int totalCnt = dao.getTotalCount(memberId);
			dao.close();
			map.put("totalCount", String.valueOf(totalCnt));
			map.put("reqUri","/myclass/view.do?action=scoreList");
			String pagingBlock = Paging.getPageBlock(map, request);
			request.setAttribute("list", list);
			request.setAttribute("pagingBlock", pagingBlock);
			request.getRequestDispatcher("./view.jsp?action=scoreList").forward(request, response);
		}else{
			JSFunc.alertBack("비정상적인 접근입니다", response);
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
