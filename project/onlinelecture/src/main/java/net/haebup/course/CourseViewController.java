package net.haebup.course;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import net.haebup.bbs.BbsDAO;
import net.haebup.bbs.BbsDTO;
import net.haebup.courseoutline.CourseOutlineDTO;
import net.haebup.member.MemberDAO;
import net.haebup.member.MemberDTO;
import net.haebup.payment.PaymentDAO;
import net.haebup.payment.PaymentDTO;
import net.haebup.utils.CommonUtils;
import net.haebup.utils.Paging;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
 

@WebServlet("/course/view.do")
public class CourseViewController extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		HttpSession session = req.getSession();
		MemberDTO loginDto = (MemberDTO) session.getAttribute("loginDto");
		String memberId = null;
		if (loginDto != null) {
		    memberId = loginDto.getMemberId();
		    System.out.println("로그인된 회원 ID: " + memberId);
		} else {
		    System.out.println("세션에 loginDto가 없습니다.");
		}

	    String action = req.getParameter("action");
	    String courseCode = req.getParameter("courseCode");
	    
	    
	    //기본 정보
	    CourseDAO courseDAO = new CourseDAO();
	    CourseDTO info = courseDAO.getCourseInfo(courseCode);
	    req.setAttribute("memberId", memberId);
	    req.setAttribute("info", info);
	    
	    //강의 구입 여부
	    PaymentDAO paymentDAO = new PaymentDAO();
	    boolean purchased = paymentDAO.getCoursePurchased(memberId, courseCode);
	    System.out.println(purchased);
	    req.setAttribute("purchased", purchased);
	    
	    //구입 정보
	    PaymentDTO payInfo = paymentDAO.getPurchasedInfo(memberId, courseCode);
	    paymentDAO.close();
	    req.setAttribute("payInfo", payInfo);
	   
	    
	    //후기
    	BbsDAO bbsDAO = new BbsDAO();
    	Map<String, String> rMap = new HashMap<>();
    	rMap.put("category", "R");
    	rMap.put("pageNo", req.getParameter("pageNo") == null ? "1" : req.getParameter("pageNo"));
    	rMap.put("pageSize", "6"); 
    	rMap.put("levelIdx", "0"); 
    	rMap.put("whereWord", "courseCode"); 
    	rMap.put("whereValue", courseCode); 
    	
    	//자료실
    	Map<String, String> mMap = new HashMap<>();
    	mMap.put("category", "M");
    	mMap.put("pageNo", req.getParameter("pageNo") == null ? "1" : req.getParameter("pageNo"));
    	mMap.put("pageSize", "6"); 
    	mMap.put("levelIdx", "0"); 
    	mMap.put("whereWord", "courseCode"); 
    	mMap.put("whereValue", courseCode);
 
    	//자신의 강의가 맞는지(선생님)
    	boolean teacherOfCourse = false;
    	List<CourseDTO> role = courseDAO.getCourseList(memberId);
    	for (CourseDTO course : role) {
    		if (course.getCourseCode().equals(courseCode)) {
    	        teacherOfCourse = true;
    	        break;
    	    }
    	}
    	System.out.println("teacherOfCourse :" + teacherOfCourse);
    	req.setAttribute("teacherOfCourse", teacherOfCourse);
    	
    	
	    if (action == null || action.isEmpty()) {
	        action = "intro";
	    }
	    if (action.equals("chapters")) {
	        List<CourseOutlineDTO> chapterInfo = courseDAO.getCourseChapters(courseCode);
	        req.setAttribute("courseCode", info.getCourseCode());  //1028수정
	        req.setAttribute("teacherId", info.getTeacherId());  //1028수정
	        req.setAttribute("category", "C");
	        req.setAttribute("bbsList", chapterInfo);
	    } else if (action.equals("reviews")) {
	    	req.setAttribute("category", "R");
	    	req.setAttribute("outlineNo", rMap);
	        List<BbsDTO> reviewInfo = bbsDAO.getPostList(rMap);
	        req.setAttribute("bbsList", reviewInfo);
	        rMap.put("reqUri","/course/view.do?action=reviews");
	        rMap.put("queryString", "&courseCode="+courseCode);
	        rMap.put("totalCount", String.valueOf(bbsDAO.getTotalCount(rMap)));
	        String pagingBlock = Paging.getPageBlock(rMap, req);
	        req.setAttribute("pagingBlock",pagingBlock);
	    } else if(action.equals("material")) {
	    	req.setAttribute("category", "M");
	    	req.setAttribute("outlineNo", mMap);
	    	List<BbsDTO> materialInfo = bbsDAO.getPostList(mMap);
	    	mMap.put("reqUri","/course/view.do?action=material");
	        mMap.put("queryString", "&courseCode="+courseCode);
	        mMap.put("totalCount", String.valueOf(bbsDAO.getTotalCount(mMap)));
	        String pagingBlock = Paging.getPageBlock(mMap, req);
	        req.setAttribute("pagingBlock",pagingBlock);
	    	System.out.println(materialInfo.size());
        	req.setAttribute("bbsList", materialInfo);
		}
	    
	    courseDAO.close();
	    bbsDAO.close();
	    req.setAttribute("action", action);
	    req.getRequestDispatcher("/course/view.jsp").forward(req, res);
	}

}	

