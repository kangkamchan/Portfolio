package net.haebup.mypage;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import net.haebup.bbs.BbsDTO;
import net.haebup.course.CourseDAO;
import net.haebup.course.CourseDTO;
import net.haebup.member.MemberDAO;
import net.haebup.member.MemberDTO;
import net.haebup.payment.PaymentDAO;
import net.haebup.payment.PaymentDTO;
import net.haebup.utils.CommonUtils;
import net.haebup.utils.JSFunc;
import net.haebup.utils.Paging;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Servlet implementation class MypageViewController
 */
@WebServlet("/mypage/view.do")
public class MypageViewController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MypageViewController() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	/*
	 * postList
	 * map 에 들어가야할 값
	 * 필수
	 * key : category, 값 : 게시글 종류 코드 F자유, R후기, M자료실, N공지
	 * key : pageNo, 값 : 출력할 페이지 번호
	 * key : pageSize, 값 : 페이지에 출력할 게시글 수
	 * 선택
	 * key : searchWord, 값 : 검색어
	 * key : searchCategory, 값 : 검색대상
	 * key : whereWord, 값 : where 조건절에 조건으로 들어갈 컬럼명
	 * key : whereValue, 값 : 강의후기, where 조건절에 조건으로 들어갈 값
	 * 
	 * 예)
	 * 마이페이지의 내가쓴 글 목록 : category - F, whereWord - memberId, whereValue - 아이디값
	 * 강의페이지의 후기목록 : category - R, whereWord - courseCode, whereValue - 강의코드값
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String action = request.getParameter("action");
		MemberDTO loginDto = (MemberDTO)request.getSession().getAttribute("loginDto");
		if(action.equals("modify")) {
			String memberId = request.getParameter("memberId");
			MemberDAO dao = new MemberDAO();
			MemberDTO dto = dao.getMemberById(memberId);
			dao.close();
			if(dto==null) {
				JSFunc.alertBack("비정상적인 접근입니다.", response);
				return;
			}
			request.setAttribute("tempDto", dto);
			request.getRequestDispatcher("./view.jsp?action="+action).forward(request, response);
		}else if(action.equals("postList")) {
				System.out.println("postList 들어옴");
				String reqUri = request.getRequestURI().replace(request.getContextPath(), "")+"?action=postList";
				Map<String,String> map = new HashMap<String,String>();
				map.put("reqUri", reqUri);
				map.put("category", request.getParameter("category"));
				map.put("pageNo", CommonUtils.ifNullString(request.getParameter("pageNo"), "1"));
				map.put("pageSize", CommonUtils.ifNullString(request.getParameter("pageSize"), "10"));
				map.put("searchWord", CommonUtils.ifNullString(request.getParameter("searchWord"), ""));
				map.put("searchCategory", CommonUtils.ifNullString(request.getParameter("searchCategory"), ""));
				map.put("whereWord", CommonUtils.ifNullString(request.getParameter("whereWord"), ""));
				map.put("whereValue", CommonUtils.ifNullString(request.getParameter("whereValue"), ""));
				map.put("levelIdx", CommonUtils.ifNullString(request.getParameter("levelIdx"), "0"));
				request.setAttribute("map", map);
				request.getRequestDispatcher("../bbs/list.do").forward(request, response);
		}else if(action.equals("bbsListOk")) {
			System.out.println("bbsListOk 들어옴");
			request.setAttribute("bbsList",request.getAttribute("bbsList"));
			request.setAttribute("pagingBlock",request.getAttribute("pagingBlock"));
			request.getRequestDispatcher("./view.jsp?action=postList").forward(request,response);
		}else  if(action.equals("paymentList")) {
			int pageNo = CommonUtils.ifNullInt(request.getParameter("pageNo"), 1);
			int pageSize = CommonUtils.ifNullInt(request.getParameter("pageSize"), 5);
			int offset = (pageNo-1) * pageSize;
			PaymentDAO paymentDao = new PaymentDAO();
			List<PaymentDTO> paymentList = paymentDao.getList(loginDto.getMemberId(), offset, pageSize);
			int totalCnt = paymentDao.getTotalCountByMemberId(loginDto.getMemberId());
			paymentDao.close();
			Map<String, String> map = new HashMap<String,String>();
			map.put("pageNo", String.valueOf(pageNo));
			map.put("pageSize", String.valueOf(pageSize));
			map.put("totalCount", String.valueOf(totalCnt));
			map.put("reqUri", "/mypage/view.do?");
			map.put("queryString", "action=paymentList");
			String pagingBlock = Paging.getPageBlock(map, request);
			request.setAttribute("pagingBlock", pagingBlock);
			request.setAttribute("paymentList", paymentList);
			request.setAttribute("map", map);
			request.getRequestDispatcher("./view.jsp?action="+action).forward(request, response);
		}else if(action.equals("bbsView")){
			System.out.println("bbsView 들어옴");
			String idx=CommonUtils.ifNullString(request.getParameter("idx"), "");
			response.sendRedirect("../free/view.do?action="+action+"&idx="+idx);
  	    	return;
		}else if(action.equals("ask")) {
			request.setAttribute("ask", "ask");
			request.getRequestDispatcher("./view.jsp?action="+action).forward(request, response);
		}else if(action.equals("courseRegist")) {
			request.getRequestDispatcher("./view.jsp?action="+action).forward(request, response);
		}else if(action.equals("courseModify")) {
			String form = request.getParameter("form");
			if(form.equals("false")) {
				CourseDAO dao = new CourseDAO();
				List<CourseDTO> courseList = dao.getCourseList(loginDto.getMemberId());
				dao.close();
				request.setAttribute("courseList",courseList);
				request.setAttribute("form", "false");
				request.getRequestDispatcher("./view.jsp?action="+action).forward(request, response);
			}else if(form.equals("true")){
				String courseCode = request.getParameter("courseCode");
				CourseDAO dao = new CourseDAO();
				CourseDTO courseDto = dao.getCourseInfo(courseCode);
				courseDto.setIntroduce(courseDto.getIntroduce().replace("<br>", "\r\n"));
				List<CourseDTO> courseList = dao.getCourseList(loginDto.getMemberId());
				dao.close();
				request.setAttribute("courseDto",courseDto);
				request.setAttribute("courseList", courseList);
				request.setAttribute("form","true");
				if(CommonUtils.ifNullString(request.getParameter("modifyOk"),"").equals("true")) {
					request.setAttribute("modifyOk", "true");
				}
				request.getRequestDispatcher("./view.jsp?action=courseModify").forward(request, response);
			}else {
				JSFunc.alertBack("비정상적인 접근입니다.", response);
				return;
			}
		}else if(action.equals("deleteMember")){
			response.sendRedirect("../member/delete.do?memberId="+loginDto.getMemberId());
		}else {
			JSFunc.alertBack("비정상적인 접근입니다.", response);
			return;
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		MemberDTO loginDto = (MemberDTO)request.getSession().getAttribute("loginDto");
		String action = request.getParameter("action");
		if(action.equals("courseRegist")) {
			String subjectCode = request.getParameter("subjectCode");
			String courseName = request.getParameter("courseName");
			String teacherId = loginDto.getMemberId();
			String introduce = request.getParameter("introduce");
			System.out.println("introduce : " + introduce);
			int price = CommonUtils.ifNullInt(request.getParameter("price"), 0);
			int courseDuration = CommonUtils.ifNullInt(request.getParameter("courseDuration"), 0);
			CourseDTO dto = new CourseDTO();
			dto.setSubjectCode(subjectCode);
			dto.setCourseName(courseName);
			dto.setTeacherId(teacherId);
			dto.setIntroduce(introduce);
			dto.setPrice(price);
			dto.setCourseDuration(courseDuration);
			CourseDAO dao = new CourseDAO();
			String courseCode = dao.getNextCourseCode(subjectCode);
			dto.setCourseCode(courseCode);
			int result = dao.regist(dto);
			dao.close();
			if(result<=0) {
				JSFunc.alertBack("강의 등록 실패", response);
				return;
			}
			JSFunc.alertLocation("강의 등록 성공", "./view.jsp?action="+action, response);
			return;
		}else if(action.equals("courseModify")) {
			String courseCode = request.getParameter("courseCode");
			CourseDAO dao = new CourseDAO();
			CourseDTO dto = dao.getCourseInfo(courseCode);
			if(dto==null) {
				JSFunc.alertBack("잘못된접근입니다.", response);
				dao.close();
				return;
			}
			String subjectCode = request.getParameter("subjectCode");
			String courseName = request.getParameter("courseName");
			String introduce = request.getParameter("introduce");
			String teacherId = loginDto.getMemberId();
			int price = CommonUtils.ifNullInt(request.getParameter("price"), 0);
			int courseDuration = CommonUtils.ifNullInt(request.getParameter("courseDuration"), 0);
			dto.setSubjectCode(subjectCode);
			dto.setCourseName(courseName);
			dto.setTeacherId(teacherId);
			dto.setPrice(price);
			dto.setCourseDuration(courseDuration);
			dto.setIntroduce(introduce);
			int result = dao.modify(dto);
			dao.close();
			if(result<=0) {
				
				JSFunc.alertBack("강의 수정 실패", response);
				return;
			}
			response.sendRedirect("./view.do?action=courseModify&form=true&modifyOk=true&courseCode="+courseCode);
			return;
		}else {
			JSFunc.alertBack("잘못된 접근입니다", response);
			return;
		}
	}

}

