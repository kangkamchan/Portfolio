package net.haebup.payment;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import net.haebup.course.CourseDAO;
import net.haebup.course.CourseDTO;
import net.haebup.member.MemberDTO;
import net.haebup.utils.JSFunc;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/payment/pay.do")
public class PayController extends HttpServlet {
	private static final long serialVersionUID = 1L;


	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		String courseCode = req.getParameter("courseCode");
		PaymentDAO paymentDAO = new PaymentDAO();
		MemberDTO loginDto = (MemberDTO)req.getSession().getAttribute("loginDto");
		String memberId = loginDto.getMemberId();
	    boolean purchased = paymentDAO.getCoursePurchased(memberId, courseCode);
	    paymentDAO.close();
	    System.out.println("구입여부"+ purchased);
	    if(purchased) {
	    	JSFunc.alertBack("이미 구매한 강의입니다.", res);
	    	return;
	    }
		CourseDAO dao = new CourseDAO();
		CourseDTO info = dao.getCourseInfo(courseCode);
		dao.close();
		req.setAttribute("info", info);
		
		req.getRequestDispatcher("/payment/pay.jsp").forward(req, res);
		
	}


	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		//받아서 넘길 것
		MemberDTO loginDto = (MemberDTO)req.getSession().getAttribute("loginDto");
		String memberId = loginDto.getMemberId();
		String courseCode = req.getParameter("courseCode");
		int price = Integer.parseInt(req.getParameter("price"));
		PaymentDTO dto = new PaymentDTO();
		dto.setMemberId(memberId);
		dto.setCourseCode(courseCode);
		dto.setPrice(price);
		
		PaymentDAO dao = new PaymentDAO();
		boolean result = dao.getPay(dto);
		dao.close();
		if(result) {
			JSFunc.alertLocation("결제 완료", req.getContextPath() + "/myclass/view.do?action=courseList", res); //나의 학습방으로 이동
			//res.sendRedirect(req.getContextPath() + "/course/list.do");
			return;
		}else {
			JSFunc.alertBack("결제에 실패했습니다", res);
			return;
		}
		
		
		
	}

}
