package net.haebup.myclass;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import net.haebup.member.MemberDTO;
import net.haebup.utils.CommonUtils;
import net.haebup.utils.Paging;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Servlet implementation class MyclassCourseListController
 */
@WebServlet("/myclass/courseList.do")
public class MyclassCourseListController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MyclassCourseListController() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("MyclassCourseListController start");
		MemberDTO loginDto = (MemberDTO)request.getSession().getAttribute("loginDto");
		if(loginDto.getAccessLevel().equals("S")) {
			String memberId = loginDto.getMemberId(); 
			String subjectCode = request.getParameter("subjectCode");
			MyclassDAO dao = new MyclassDAO();
			//전체 게시글 수 확인
			int totalCnt = dao.getTotalCount(memberId, subjectCode);
			//페이징
			int pageNo = CommonUtils.ifNullInt(request.getParameter("pageNo"), 1);
			int pageSize = 3;
			int offset = (pageNo-1)*pageSize;
			List<MyclassCourseListDTO> list= dao.getMyclassCourseList(memberId,subjectCode,offset,pageSize);
			dao.close();
			Map<String,String> map = new HashMap<String,String>();
			map.put("pageNo", String.valueOf(pageNo));
			map.put("pageSize", String.valueOf(pageSize));
			map.put("totalCount",String.valueOf(totalCnt));
			map.put("reqUri", "/myclass/view.do?action=courseList");
			map.put("queryString", "&subjectCode="+subjectCode);
			String pagingBlock = Paging.getPageBlock(map, request);
			System.out.println(pagingBlock);
			System.out.println(map);
			System.out.println(list);
			//데이터 전달
			request.setAttribute("list", list);
			request.setAttribute("pagingBlock",pagingBlock);
			request.setAttribute("subjectCode", subjectCode);
			request.getRequestDispatcher("./view.jsp?action=courseList").forward(request, response);
		}else if(loginDto.getAccessLevel().equals("T")) {
			MyclassDAO dao = new MyclassDAO();
			String teacherId= loginDto.getMemberId();
			//전체 게시글 수 확인
			int totalCnt = dao.getTotalCount(teacherId);
			//페이징
			int pageNo = CommonUtils.ifNullInt(request.getParameter("pageNo"), 1);
			int pageSize = 3;
			int offset = (pageNo-1)*pageSize;
			List<MyclassCourseListDTO> list= dao.getMyclassCourseList(teacherId,offset,pageSize);
			dao.close();
			Map<String,String> map = new HashMap<String,String>();
			map.put("pageNo", String.valueOf(pageNo));
			map.put("pageSize", String.valueOf(pageSize));
			map.put("totalCount",String.valueOf(totalCnt));
			map.put("reqUri", "/myclass/view.do?action=courseList");
			String pagingBlock = Paging.getPageBlock(map, request);
			System.out.println(pagingBlock);
			System.out.println(map);
			System.out.println(list);
			//데이터 전달
			request.setAttribute("list", list);
			request.setAttribute("pagingBlock",pagingBlock);
			request.getRequestDispatcher("./view.jsp?action=courseList").forward(request, response);
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
