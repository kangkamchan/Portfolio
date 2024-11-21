package net.haebup.free;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import net.haebup.bbs.BbsDAO;
import net.haebup.bbs.BbsDTO;
import net.haebup.member.MemberDTO;
import net.haebup.utils.CommonUtils;
import net.haebup.utils.JSFunc;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Servlet implementation class FreeViewController
 */
@WebServlet("/free/view.do")
public class FreeViewController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public FreeViewController() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("FreeViewController > doGet > start");
		String action = CommonUtils.ifNullString(request.getParameter("action"),"");
		System.out.println("action : " + action);
		MemberDTO loginDto = (MemberDTO)request.getSession().getAttribute("loginDto");
	    if(action.equals("bbsView")) {//게시글 인덱스, 요청url 받아서 bbsViewController로 넘김
  	    	System.out.println("FreeViewController > doGet > bbsView");
  	    	String idx = request.getParameter("idx");
  	    	String reqUri = request.getRequestURI().replace(request.getContextPath(), "");
  	    	System.out.println("doGet > bbsView > reqUri : "+reqUri);
  	    	if(idx==null||idx.isEmpty()) {
  	    		JSFunc.alertBack("잘못된접근입니다1", response);
  	    		return;
  	    	}
  	    	response.sendRedirect("../bbs/view.do?idx="+idx+"&reqUri="+reqUri+"&pageNo="+CommonUtils.ifNullString(request.getParameter("pageNo"),"1"));
  	    	return;
  	    }else if(action.equals("bbsViewOk")){//bbsViewController에서 게시글 객체 받아서 게시글 조회페이지로 이동
  	    	System.out.println("FreeViewController > doGet > bbsViewOk");
  	    	BbsDTO dto = (BbsDTO) request.getAttribute("post");
  	    	request.setAttribute("post", dto);
  	    	request.setAttribute("action", "bbsViewOk");
  	    	request.setAttribute("category", "M");
  	    	request.getRequestDispatcher("./view.jsp?pageNo="+CommonUtils.ifNullString(request.getParameter("pageNo"),"1")).forward(request,response);
  	    	return;
  	    }else if(action.equals("bbsList")) {
  	    	System.out.println("FreeViewController > doGet > bbsList");
			System.out.println("bbsList 들어옴");
			String reqUri = request.getRequestURI().replace(request.getContextPath(), "");
			Map<String,String> map = new HashMap<String,String>();
			map.put("reqUri", "/free/view.do?action=bbsList&");
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
  	    	System.out.println("FreeViewController > doGet > bbsListOk");
			request.setAttribute("bbsList",request.getAttribute("bbsList"));
			request.setAttribute("pagingBlock",request.getAttribute("pagingBlock"));
			request.setAttribute("category", "F");
			request.getRequestDispatcher("./view.jsp?action=bbsListOk").forward(request,response);
  	    }else if(action.equals("regist")) {
  	    	System.out.println("FreeViewController > doGet > regist");
  	    	request.setAttribute("category", "F");
  	    	request.getRequestDispatcher("./view.jsp?action=regist&pageNo="+CommonUtils.ifNullString(request.getParameter("pageNo"), "1")).forward(request, response);
  	    }else if(action.equals("modify")) {
  	    	System.out.println("FreeViewController > doGet > modify");
  	    	int idx = CommonUtils.ifNullInt(request.getParameter("idx"),0);
  	    	if(idx==0) {
  	    		JSFunc.alertBack("잘못된 접근입니다.", response);
  	    		return;
  	    	}
  	    	BbsDAO dao = new BbsDAO();
  	    	BbsDTO dto = dao.getPostByIdx(idx);
  	    	dao.close();
  	    	if(dto==null) {
  	    		JSFunc.alertBack("해당 게시물은 존재하지 않습니다.", response);
  	    		return;
  	    	}
  	    	if(!dto.getMemberId().equals(loginDto.getMemberId())) {
  	    		JSFunc.alertBack("자신의 글만 수정할 수 있습니다.", response);
  	    		return;
  	    	}
  	    	dto.setContent(dto.getContent().replace("<br>","\r\n"));
  	    	request.setAttribute("post", dto);
  	    	request.setAttribute("category", "F");
  	    	request.getRequestDispatcher("./view.jsp?action=modify&pageNo="+CommonUtils.ifNullString(request.getParameter("pageNo"), "1")).forward(request, response);
  	    }else if(action.equals("delete")) {
  	    	System.out.println("NoticeViewController > doGet > delete");
  	    	int idx = CommonUtils.ifNullInt(request.getParameter("idx"),0);
  	    	if(idx==0) {
  	    		JSFunc.alertBack("잘못된 접근입니다.", response);
  	    		return;
  	    	}
  	    	BbsDAO dao = new BbsDAO();
  	    	BbsDTO dto = dao.getPostByIdx(idx);
  	    	if(dto==null) {
  	    		JSFunc.alertBack("해당 게시물은 존재하지 않습니다.", response);
  	    		dao.close();
  	    		return;
  	    	}
  	    	if(!dto.getMemberId().equals(loginDto.getMemberId())) {
  	    		JSFunc.alertBack("자신의 글만 삭제할 수 있습니다.", response);
  	    		dao.close();
  	    		return;
  	    	}
  	    	int result = dao.deletePost(dto);
  	    	dao.close();
  	    	if(result<=0) {
  	    		JSFunc.alertBack("게시글 삭제 실패", response);
  	    		return;
  	    	}
  		    JSFunc.alertLocation("게시글 삭제 성공", "../free/view.do?action=bbsList&category=F&pageNo=1&pageSize=5&levelIdx=0", response);
  	  	    return;
  	    }
  	    else {
  	    	JSFunc.alertBack("잘못된접근입니다.", response);
  	    	return;
  	    }
  	    
  	    

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("FreeViewController > doPost > start");
		String action = CommonUtils.ifNullString(request.getParameter("action"),"");
		System.out.println("action : " + action);
		MemberDTO loginDto = (MemberDTO)request.getSession().getAttribute("loginDto");
		if(action.equals("regist")) {
			String memberId= loginDto.getMemberId();
			String title = request.getParameter("title");
			String content = request.getParameter("content");
			BbsDTO dto = new BbsDTO();
			dto.setMemberId(memberId);
			dto.setTitle(title);
			dto.setContent(content);
			dto.setCategory("F");
			BbsDAO dao = new BbsDAO();
			boolean flag = dao.regist(dto);
			dao.close();
			if(!flag) {
				JSFunc.alertBack("게시글 등록실패", response);
				return;
			}
			JSFunc.alertLocation("게시글 등록 성공", "./view.do?action=bbsList&category=F&pageNo=1&pageSize=5&levelIdx=0", response);
			return;
		}else if(action.equals("modify")) {
  	    	System.out.println("FreeViewController > doGet > modify");
  	    	int idx = CommonUtils.ifNullInt(request.getParameter("idx"),0);
  	    	if(idx==0) {
  	    		JSFunc.alertBack("잘못된 접근입니다.", response);
  	    		return;
  	    	}
  	    	BbsDAO dao = new BbsDAO();
  	    	BbsDTO dto = dao.getPostByIdx(idx);
  	    	dao.close();
  	    	if(dto==null) {
  	    		JSFunc.alertBack("해당 게시물은 존재하지 않습니다.", response);
  	    		return;
  	    	}
  	    	String title = request.getParameter("title");
  	    	String content = request.getParameter("content");
  	    	dto.setTitle(title);
  	    	dto.setContent(content);
  	    	BbsDAO bbsDao = new BbsDAO();
  	    	int result = bbsDao.modifyPost(dto);
  	    	bbsDao.close();
  	    	if(result<=0) {
  	    		JSFunc.alertBack("게시글 수정 실패", response);
  	    		return;
  	    	}
  	    	JSFunc.alertLocation("게시글 수정 성공", "../free/view.do?action=bbsView&idx="+idx+"&pageNo="+CommonUtils.ifNullString(request.getParameter("pageNo"), "1"), response);
  	    	
		}else {
			JSFunc.alertBack("잘못된접근입니다.", response);
			return;
		}
		
	}

}
