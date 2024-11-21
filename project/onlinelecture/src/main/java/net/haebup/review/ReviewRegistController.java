package net.haebup.review;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import net.haebup.bbs.BbsDAO;
import net.haebup.bbs.BbsDTO;
import net.haebup.course.CourseDAO;
import net.haebup.course.CourseDTO;
import net.haebup.utils.JSFunc;

import java.io.IOException;

@WebServlet("/review/regist.do")
public class ReviewRegistController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       

	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		String courseCode = req.getParameter("courseCode");
		CourseDAO dao = new CourseDAO();
		CourseDTO info = dao.getCourseInfo(courseCode);
		dao.close();
		req.setAttribute("info", info);
		req.getRequestDispatcher("/review/regist.jsp").forward(req, res);
	}

	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		String courseCode = req.getParameter("courseCode");
		String memberId = req.getParameter("memberId");
		int rating = Integer.parseInt(req.getParameter("rating"));
		String content = req.getParameter("content");
		String category = req.getParameter("category");
		String title = req.getParameter("title");
		
		System.out.println("memberId: " + memberId);
        System.out.println("courseCode: " + courseCode);
        System.out.println("content: " + content);
        System.out.println("rating: " + rating);
        System.out.println("category: " + category);
        System.out.println("title: " + title);
        
        BbsDTO review = new BbsDTO();
        review.setMemberId(memberId);
        review.setCourseCode(courseCode);
        review.setContent(content);
        review.setRating(rating);
        review.setCategory(category);
        review.setTitle(title);
        
        BbsDAO dao = new BbsDAO();
        boolean result = dao.getRegistReview(review);
        dao.close();
        if(result) {
        	JSFunc.alertLocation("등록되었습니다","../course/view.do?action=reviews&courseCode="+courseCode, res);
        }
        else {
        	JSFunc.alertBack("다시 등록해주세요", res);
        }
        

	}

}
