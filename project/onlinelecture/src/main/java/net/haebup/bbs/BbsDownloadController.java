package net.haebup.bbs;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import net.haebup.utils.CommonUtils;
import net.haebup.utils.FileUtil;
import net.haebup.utils.JSFunc;

import java.io.IOException;

/**
 * Servlet implementation class BbsDownloadController
 */
@WebServlet("/bbs/download.do")
public class BbsDownloadController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public BbsDownloadController() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int idx = CommonUtils.ifNullInt(request.getParameter("idx"),0);
		System.out.println("idx : "+ idx);
		BbsDAO dao = new BbsDAO();
		BbsDTO dto = dao.getPostByIdx(idx);
		dao.close();
		if(dto==null) {
			JSFunc.alertBack("파일다운로드 실패", response);
			return;
		}
		FileUtil.downloadFile(request, response, dto.getFilePath(), dto.getFileName(), dto.getOrgFileName());
		JSFunc.back(response);
		return;
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
