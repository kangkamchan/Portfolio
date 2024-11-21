package net.tclass.qna;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import net.tclass.member.MemberDTO;
import net.tclass.utils.CommonUtil;
import net.tclass.utils.FileUtil;
import net.tclass.utils.JSFunc;

import java.io.IOException;
import java.util.Map;

/**
 * Servlet implementation class ModifyController
 */
@MultipartConfig(
	    fileSizeThreshold = 1024 * 1024, // 1 MB
	    maxFileSize = 1024 * 1024 * 5,   // 5 MB
	    maxRequestSize = 1024 * 1024 * 5 * 5 // 25 MB
	)
@WebServlet(name = "QnaModifyController", urlPatterns = { "/qna/modify.do" })
public class ModifyController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ModifyController() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String referer = request.getHeader("referer");
		String refererUri = "";
		if(referer!=null) {
			refererUri = referer.substring(referer.indexOf("://")+3).substring(referer.substring(referer.indexOf("://")+3).indexOf("/"));
		}
		int idx = CommonUtil.ifNullInt(request.getParameter("idx"), 0);
		QnaDAO dao = new QnaDAO();
		QnaDTO dto = dao.getQnaByIdx(idx);
		dao.close();
				
		request.setAttribute("qna", dto);
		request.setAttribute("refererUri",refererUri);
		request.getRequestDispatcher("modify.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int idx = CommonUtil.ifNullInt(request.getParameter("idx"), 0);
		String title = request.getParameter("title");
		String content = request.getParameter("content");
		String saveDir = getServletContext().getRealPath("/uploads");
		String orgFileName = FileUtil.uploadFile(request, saveDir, "file");
		QnaDAO dao = new QnaDAO();
		QnaDTO dto = dao.getQnaByIdx(idx);
		dto.setTitle(title);
		dto.setContent(content);
		if(!dto.getOrgFileName().equals(orgFileName)) {
			Map<String,String> fMap = FileUtil.fileRename(saveDir, orgFileName);
			if(fMap!=null) {
				dto.setFileName(fMap.get("newFileName"));
				dto.setFileExt(fMap.get("fileExt"));
				dto.setFilePath(fMap.get("filePath"));
				dto.setFileSize(Integer.parseInt(fMap.get("fileSize")));
				dto.setOrgFileName(fMap.get("orgFileName"));
				FileUtil.fileDelete(request, saveDir, fMap.get("newFileName"));
			}
		}
		int result = dao.modify(dto);
		dao.close();
		if(result<=0) {
			JSFunc.alertBack("파일 수정 실패", response);
			return;
		}
		response.sendRedirect("./view.do?idx="+idx);
	}

}
