package net.haebup.courseoutline;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import net.haebup.utils.CommonUtils;
import net.haebup.utils.FileUtil;
import net.haebup.utils.JSFunc;

import java.io.IOException;
import java.util.Map;

/**
 * Servlet implementation class CourseOutlineRegistController
 */
@MultipartConfig(
	    fileSizeThreshold = 1024 * 1024, // 1 MB
	    maxFileSize = 1024 * 1024 * 100,   // 5 MB
	    maxRequestSize = 1024 * 1024 * 100 // 25 MB
	)
@WebServlet("/courseoutline/regist.do")

public class CourseOutlineRegistController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CourseOutlineRegistController() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String courseCode = request.getParameter("courseCode");
		CourseOutlineDAO dao = new CourseOutlineDAO();
		int nextNo = dao.getNextOutlineNo(courseCode);
		dao.close();
		request.setAttribute("courseCode", courseCode);
		request.setAttribute("outlineNo", nextNo);
		request.getRequestDispatcher("../course/view.do?action=outlineRegist").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String courseCode = request.getParameter("courseCode");
		int outlineNo = Integer.parseInt(request.getParameter("outlineNo"));
		String content = request.getParameter("content");
		String hour = CommonUtils.ifNullString(request.getParameter("hour"),"00");
		String minute = request.getParameter("minute");
		String second = request.getParameter("second");
		
		//String saveDir = request.getServletContext().getRealPath("/resource/video");
		String saveDir = "D:\\project\\haebup\\haebup\\src\\main\\webapp\\resource\\video";
		//String saveDir = "C:\\Users\\User\\Desktop\\project\\haebup\\haebup\\src\\main\\webapp\\resource\\video";
		String orgFileName = FileUtil.uploadFile(request, saveDir, "file");
		if(orgFileName==null) {
			JSFunc.alertBack("동영상 업로드 실패", response);
			return;
		}
		Map<String,String> map = FileUtil.fileRename(saveDir, orgFileName, "video_"+courseCode+"_"+outlineNo+orgFileName.substring(orgFileName.lastIndexOf(".")));
		if(map==null) {
			JSFunc.alertBack("동영상 업로드 실패", response);
			return;
		}
		
		System.out.println(map);
		if(hour.length()==1) {
			hour = "0" + hour;
		}
		if(minute.length()==1) {
			minute = "0" + minute;
		}
		if(second.length()==1) {
			second = "0" + second;
		}
		String time = hour + ":" + minute + ":" + second;
		System.out.println("courseCode : " + courseCode);
		System.out.println("outlineNo : " + outlineNo);
		System.out.println("content : " + content);
		System.out.println("time : " + time);
		CourseOutlineDTO dto = new CourseOutlineDTO();
		dto.setCourseCode(courseCode);
		dto.setOutlineNo(outlineNo);
		dto.setContent(content);
		dto.setTime(time);
		CourseOutlineDAO dao = new CourseOutlineDAO();
		int result = dao.regist(dto);
		dao.close();
		if(result<=0) {
			JSFunc.alertBack("강의 목차 등록 실패", response);
			return;
		}
		JSFunc.alertLocation("강의목차 등록 성공", "../course/view.do?action=chapters&courseCode="+courseCode, response);
		return;
	}

}
