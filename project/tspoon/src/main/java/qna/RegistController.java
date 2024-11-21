package net.tclass.qna;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import net.tclass.member.MemberDTO;
import net.tclass.utils.FileUtil;
import net.tclass.utils.JSFunc;

import java.io.IOException;
import java.util.Map;

/**
 * Servlet implementation class RegistController
 */
@MultipartConfig(
	    fileSizeThreshold = 1024 * 1024, // 1 MB
	    maxFileSize = 1024 * 1024 * 5,   // 5 MB
	    maxRequestSize = 1024 * 1024 * 5 * 5 // 25 MB
	)
@WebServlet(name = "QnaRegistController", urlPatterns = { "/qna/regist.do" })
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
		response.sendRedirect("./regist.jsp");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		MemberDTO loginDto = (MemberDTO)request.getSession().getAttribute("loginDto");
		String category1 = request.getParameter("category1");
		String category2 = request.getParameter("category2");
		String title = request.getParameter("title");
		String content = request.getParameter("content");
		String saveDir = getServletContext().getRealPath("/uploads");
		QnaDTO dto = new QnaDTO();
		dto.setMemberId(loginDto.getMemberId());
		dto.setCategory1(category1);
		dto.setCategory2(category2);
		dto.setTitle(title);
		dto.setContent(content);		
		System.out.println("saveDir : " + saveDir);
		String orgFileName = FileUtil.uploadFile(request, saveDir, "file");
		Map<String, String> fMap = FileUtil.fileRename(saveDir, orgFileName);
		if(fMap !=null) {
			dto.setFileName(fMap.get("newFileName"));
			dto.setOrgFileName(fMap.get("orgFileName"));
			dto.setFileExt(fMap.get("fileExt"));
			dto.setFilePath(fMap.get("filePath"));
			dto.setFileSize(Integer.parseInt(fMap.get("fileSize")));
		}
		
		QnaDAO dao = new QnaDAO();
		int result = dao.regist(dto);
		dao.close();
		
		if(result<=0) {
			JSFunc.alert("1:1 문의글 등록에 실패했습니다.", response);
			return;
		}
		
		JSFunc.alertLocation("정상적으로 등록되었습니다.", "./list.do", response);
		return;
		//C:\Users\User\Desktop\project\tclass\.metadata\.plugins\org.eclipse.wst.server.core\tmp0\wtpwebapps\tspoon\\uploads
	}

}
