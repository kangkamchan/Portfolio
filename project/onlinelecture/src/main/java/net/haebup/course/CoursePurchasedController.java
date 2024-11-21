package net.haebup.course;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import net.haebup.member.MemberDTO;
import net.haebup.payment.PaymentDAO;
import net.haebup.utils.JSFunc;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/course/purchased.do")
public class CoursePurchasedController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       

	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		String courseCode = req.getParameter("courseCode");
		String outlineNo = req.getParameter("outlineNo");
		System.out.println("courseCode purchased: " + courseCode);
		System.out.println("outlineNo outlineNo: " + outlineNo);
		HttpSession session = req.getSession();
		MemberDTO loginDto = (MemberDTO) session.getAttribute("loginDto");
		String memberId = null;
		if (loginDto != null) {
		    memberId = loginDto.getMemberId();
		    System.out.println("로그인된 회원 ID: " + memberId);
		} else {
		    System.out.println("세션에 loginDto가 없습니다");
		}
		
	    
	    //강의 구입 여부
		PaymentDAO paymentDAO = new PaymentDAO();
		System.out.println(memberId);
	    boolean purchased = paymentDAO.getCoursePurchased(memberId, courseCode);
	    paymentDAO.close();
	    System.out.println("구입여부"+ purchased);
	    
	    //자기강의 선생님이면 보게함
	    boolean teacherOfCourse = false;
	    CourseDAO courseDAO = new CourseDAO();
    	List<CourseDTO> role = courseDAO.getCourseList(memberId);
    	courseDAO.close();
    	for (CourseDTO course : role) {
    		if (course.getCourseCode().equals(courseCode)) {
    	        teacherOfCourse = true;
    	        break;
    	    }
    	}
    	System.out.println("teacherOfCourse :" + teacherOfCourse);
    	
	    //사용자가 구매했으면
	    if(purchased || teacherOfCourse) {
	    	req.setAttribute("outlineNo", outlineNo);
	    	req.getRequestDispatcher("/course/learn.do").forward(req, res);
	    	return;
	    	
	    }
	    else {
	    	JSFunc.alertLocation("수강신청 이후 사용할 수 있습니다", "../course/view.do?courseCode="+courseCode, res);
	    	return;
	    }

	    
	}


	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

	}

}
