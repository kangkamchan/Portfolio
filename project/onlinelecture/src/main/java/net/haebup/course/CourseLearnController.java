package net.haebup.course;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import net.haebup.courseoutline.CourseOutlineDTO;
import net.haebup.member.MemberDTO;
import net.haebup.payment.PaymentDAO;
import net.haebup.utils.JSFunc;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/course/learn.do")
public class CourseLearnController extends HttpServlet {
	private static final long serialVersionUID = 1L;


	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		String courseCode = req.getParameter("courseCode");
		int outlineNo = Integer.parseInt( req.getParameter("outlineNo"));
		MemberDTO loginDto = (MemberDTO)req.getSession().getAttribute("loginDto");
		String memberId = loginDto.getMemberId();

		System.out.println("courseCode :" + courseCode);
		System.out.println("outlineNo :" + outlineNo);
		
		//목차번호 조회
		CourseDAO dao = new CourseDAO();
		CourseOutlineDTO info = dao.getCourseLearnInfo(courseCode, outlineNo);
		if(info != null) {
			PaymentDAO paymentDao = new PaymentDAO();
			boolean result = paymentDao.modifyViewStatus(courseCode, memberId);
			paymentDao.close();
		}
		else{
			JSFunc.alertBack("등록된 강의가 존재하지 않습니다", res);
			dao.close();
			return;
		}

		req.setAttribute("info", info);
		System.out.println(info.getOutlineNo());
		System.out.println(info.getCourseCode());
		
		
		//커리큘럼 조회
		List<CourseOutlineDTO> list = dao.getCourseChapters(courseCode);
		dao.close();
		req.setAttribute("bbsList", list);
		req.setAttribute("category", "C");
		
		int previousOutlineNo = 0;
		int nextOutlineNo = 0;
		int currentOutlineNo = info.getOutlineNo();

		for (int i = 0; i < list.size(); i++) {
		    CourseOutlineDTO outline = list.get(i);
		    if (outline.getOutlineNo() == currentOutlineNo) {
		        if (i > 0) {
		            previousOutlineNo = list.get(i - 1).getOutlineNo();
		        }
		        if (i < list.size() - 1) {
		            nextOutlineNo = list.get(i + 1).getOutlineNo();
		        }
		        break;
		    }
		}
		req.setAttribute("videoDir",req.getServletContext().getRealPath("/resource/video"));
		req.setAttribute("previousOutlineNo", previousOutlineNo > 0 ? previousOutlineNo : 0);
		req.setAttribute("nextOutlineNo", nextOutlineNo > 0 ? nextOutlineNo : 0);
		req.getRequestDispatcher("/course/learn.jsp").forward(req, res);
		
		//강의 있는지 확인
		
	}

	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

	}

}