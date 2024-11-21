package net.haebup.teacher;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import net.haebup.course.CourseDTO;
import net.haebup.member.MemberDAO;
import net.haebup.member.MemberDTO;
import net.haebup.utils.CommonUtils;

import java.io.IOException;
import java.util.List;

@WebServlet("/teacher/list.do")
public class TeacherListController extends HttpServlet {
	private static final long serialVersionUID = 1L;


	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
	     String subjectCode = CommonUtils.ifNullString(req.getParameter("subjectCode"), "");
	     TeacherDAO dao = new TeacherDAO();
	     List<CourseDTO> teacherInfo = dao.getTeacherList(subjectCode);
	     dao.close();

	     MemberDAO memberDao = new MemberDAO();
	     // 각 강사 ID에 대해 이름을 가져와 객체 설정
	     for(CourseDTO dto : teacherInfo) {
	         String teacherId = dto.getTeacherId();
	         MemberDTO member = memberDao.getMemberById(teacherId);
	         if(member != null) {
	        	 dto.setTeacherName(member.getName());
	         }
	     }
	     memberDao.close();
	     //System.out.println(teacherInfo.size());
		 req.setAttribute("teacherInfo", teacherInfo);
	     req.getRequestDispatcher("/teacher/list.jsp").forward(req, res);
	}


	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

	}

}
