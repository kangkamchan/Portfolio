package net.haebup.material;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import net.haebup.bbs.BbsDAO;
import net.haebup.bbs.BbsDTO;
import net.haebup.utils.CommonFileUtil;
import net.haebup.utils.JSFunc;

import java.io.IOException;
import java.util.Map;
 
@WebServlet("/material/regist.do")
@MultipartConfig 
public class MaterialRegistController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		String courseCode = req.getParameter("courseCode");
		req.setAttribute("courseCode", courseCode);
		req.setAttribute("category", "M");
		req.getRequestDispatcher("/material/regist.jsp").forward(req, res);
	}


	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		BbsDTO dto = new BbsDTO();
		System.out.println(req.getParameter("memberId"));
		System.out.println(req.getParameter("title"));
		System.out.println(req.getParameter("content"));
		System.out.println(req.getParameter("courseCode"));
		
		
		
		dto.setMemberId(req.getParameter("memberId"));
		dto.setTitle(req.getParameter("title"));
		dto.setContent(req.getParameter("content"));
		dto.setCourseCode(req.getParameter("courseCode"));
		dto.setCategory("M");

		//String saveDir = "C:\\Users\\User\\Desktop\\project\\haebup\\haebup\\src\\main\\webapp\\upload\\material";
		
		String saveDir = "D:\\project\\haebup\\haebup\\src\\main\\webapp\\upload\\material";
		System.out.println("saveDir : " + saveDir);
		
		String orgfileName = CommonFileUtil.FileUpload(req, saveDir, "file");
		String newfileName = "";
		String filePath = "";
		String fileExt = "";
		int fileSize = 0;
		BbsDAO dao = new BbsDAO();
		
		if(!orgfileName.isEmpty()) {
			Map<String, String> fmap = CommonFileUtil.fileRename(saveDir, orgfileName);
			if(fmap != null ) {
				filePath = fmap.get("filePath");
				orgfileName = fmap.get("orgfileName");
				fileExt = fmap.get("fileExt");
				fileSize = Integer.parseInt(fmap.get("fileSize"));
				newfileName = fmap.get("newfileName");
				dto.setOrgFileName(orgfileName);
				dto.setFilePath(filePath);
				dto.setFileName(newfileName);
				dto.setFileExt(fileExt);
				dto.setFileSize(fileSize);
			}
			
			System.out.println("orgfileName : " + orgfileName);
			System.out.println("newfileName : " + newfileName);
			
			dao = new BbsDAO();
			boolean result = dao.regist(dto);
			dao.close();
			
			if(result) {
				JSFunc.alertLocation("등록되었습니다.", "../course/view.do?action=material&courseCode="+dto.getCourseCode(), res);
			}
			else {
				CommonFileUtil.fileDelete(req, saveDir, orgfileName);
				CommonFileUtil.fileDelete(req, saveDir, fmap.get("newfileName"));
				JSFunc.alertBack("자료 등록 실패. 다시 등록하세요", res);
			}

		}
		else {
			boolean result = dao.regist(dto);
			dao.close();
			
			//등록 성공(첨부 파일 없을 때)
			if(result) {
				JSFunc.alertLocation("등록되었습니다.", "../course/view.do?action=material&courseCode="+dto.getCourseCode(), res);
			}
			else {
				JSFunc.alertBack("자료 등록 실패. 다시 등록하세요", res);
			}
		}
		
	}

}
