package net.haebup.teacher;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import net.haebup.bbs.BbsDAO;
import net.haebup.bbs.BbsDTO;
import net.haebup.course.CourseDAO;
import net.haebup.course.CourseDTO;
import net.haebup.utils.CommonUtils;
import net.haebup.utils.Paging;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/teacher/view.do")
public class TeacherViewController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       

	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		String teacherId = req.getParameter("teacherId");
		String teacherName = req.getParameter("teacherName");
		String category = req.getParameter("category");
		int pageNo = CommonUtils.ifNullInt(req.getParameter("pageNo"), 1);
		System.out.println("pageNo : " + pageNo);
		int pageSize = 6;
		//System.out.println("category: " + category);
		//System.out.println("teacherId: " + teacherId);
		//System.out.println("teacherName: " + teacherName);
		System.out.println("teacherId : " + teacherId);
		CourseDAO dao = new CourseDAO();
		CourseDTO teacherInfo = dao.getCourse(teacherId);
		req.setAttribute("teacherInfo", teacherInfo);
		req.setAttribute("teacherName", teacherName);
		
		if (category == null) {
            category = "C"; // 기본값 예시
        }
		
		if(category.equals("C")) { //수강 강의
			List<CourseDTO> courseInfo = dao.getCourseList(teacherId);
			req.setAttribute("courseInfo", courseInfo);
		}else if(category.equals("R")) {
			System.out.println("R 진입");
	    	Map<String, String> rMap = new HashMap<>();
	    	rMap.put("pageNo", String.valueOf(pageNo));
	    	rMap.put("pageSize", "6");
	    	TeacherDAO tDao = new TeacherDAO();
	    	List<BbsDTO> reviewInfo = tDao.getAllReviews(teacherId,(pageNo-1)*pageSize,pageSize);   	
	    	
	    	System.out.println("size : " + reviewInfo.size());
	        req.setAttribute("bbsList", reviewInfo);
	        rMap.put("reqUri","/teacher/view.do?category=R");
	        rMap.put("queryString", "&teacherId="+teacherId);
	        rMap.put("totalCount", String.valueOf(tDao.getTotalCount(teacherId)));
	        tDao.close();
	        String pagingBlock = Paging.getPageBlock(rMap, req);
	        System.out.println(pagingBlock);
	        req.setAttribute("pagingBlock",pagingBlock);
		}
		dao.close();
		req.setAttribute("category", category);
        req.getRequestDispatcher("/teacher/view.jsp").forward(req, res);

	}


	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

	}
}
