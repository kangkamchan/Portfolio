package net.tclass.qna;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import net.tclass.message.MessageDAO;
import net.tclass.message.MessageDTO;
import net.tclass.utils.CommonUtil;
import net.tclass.utils.FileUtil;
import net.tclass.utils.JSFunc;

import java.io.IOException;

/**
 * Servlet implementation class DownloadController
 */
@WebServlet(name = "QnaDownloadController", urlPatterns = { "/qna/download.do" })
public class DownloadController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DownloadController() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int idx = CommonUtil.ifNullInt(request.getParameter("idx"),0);
		System.out.println("idx : "+ idx);
		QnaDAO dao = new QnaDAO();
		QnaDTO dto = dao.getQnaByIdx(idx);
		dao.close();
		if(dto==null) {
			JSFunc.alertBack("파일다운로드 실패", response);
			return;
		}
		if(dto.getFileName()==null||dto.getFileName().isEmpty()) {
			JSFunc.alertBack("파일다운로드 실패", response);
			return;
		}
		FileUtil.downloadFile(request, response, dto.getFilePath(), dto.getFileName()+dto.getFileExt(), dto.getOrgFileName()+dto.getFileExt());
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
