package net.tclass.message;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import net.tclass.member.MemberDAO;
import net.tclass.member.MemberDTO;
import net.tclass.utils.FileUtil;
import net.tclass.utils.JSFunc;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Servlet implementation class RegistController
 */
@MultipartConfig(
	    fileSizeThreshold = 1024 * 1024, // 1 MB
	    maxFileSize = 1024 * 1024 * 5,   // 5 MB
	    maxRequestSize = 1024 * 1024 * 5 * 5 // 25 MB
	)
@WebServlet(name = "MessgaeRegistController", urlPatterns = { "/message/regist.do" })
public class RegistController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public RegistController() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		MemberDTO loginDto = (MemberDTO)request.getSession().getAttribute("loginDto");
		String interest = loginDto.getInterest();
		MemberDAO dao = new MemberDAO();
		List<MemberDTO> teacherList = dao.getTeacherList(interest);
		dao.close();
		request.setAttribute("teacherList", teacherList);
		request.getRequestDispatcher("./regist.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		MemberDTO loginDto = (MemberDTO)request.getSession().getAttribute("loginDto");
		MessageDTO messageDto = new MessageDTO();
		MemberDAO memberDao = new MemberDAO();
		String filePath = request.getServletContext().getRealPath("/uploads");
		String orgFileName = FileUtil.uploadFile(request, filePath, "file");
		Map<String,String> fMap = FileUtil.fileRename(filePath, orgFileName);
		
		if(fMap!=null) {
			messageDto.setOrgFileName(fMap.get("orgFileName"));
			messageDto.setFileName(fMap.get("newFileName"));
			messageDto.setFileExt(fMap.get("fileExt"));
			messageDto.setFilePath(fMap.get("filePath"));
			messageDto.setFileSize(Integer.parseInt(fMap.get("fileSize")));
		}
		
		String receiveId = request.getParameter("teacher");
		String title = request.getParameter("title");
		String content = request.getParameter("content");
		messageDto.setSendId(loginDto.getMemberId());
		messageDto.setSendName(loginDto.getName());
		messageDto.setReceiveId(receiveId);
		messageDto.setTitle(title);
		messageDto.setContent(content);
		messageDto.setReceiveName(memberDao.getMemberById(receiveId).getName());
		memberDao.close();
		
		MessageDAO dao = new MessageDAO();
		int result = dao.regist(messageDto);
		dao.close();
		if(result<=0) {
			JSFunc.alertBack("메시지 전송에 실패했습니다", response);
			return;
		}
		
		JSFunc.alertLocation("정상적으로 쪽지가 발송되었습니다.","./sendList.do",response);
		return;
	}

}
