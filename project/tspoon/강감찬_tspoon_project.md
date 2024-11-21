# tspoon 주요 소스코드
---
## ChkIdDuplication.java

### 백엔드 코드
```java
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		MemberDAO dao = new MemberDAO();
		//요청 파라미터 userId를 받음
		String userId = request.getParameter("userId");
		//member 데이터가 존재할 경우 true, 존재하지 않을 경우 false 반환
		boolean result = true;
		//member 데이터가 존재하지 않을 경우 null 반환
		if(dao.getMemberById(userId)==null) {
			result = false;
		}
		dao.close();
		//json 객체로 결과 반환
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write("{\"result\": " + result + "}");
	}
```

### 프론트엔드 코드
```html
    <div class="formBox">
        <input type="text" class="itxt" id="idn" name="idn" placeholder="아이디" value="${tempDto.memberId }" />
        <label class="title" for="idn">아이디</label>
        <button type="button" id="checkUserId" class="checkId">중복 확인</button>
        <c:if test="${not empty tempDto }">
        <span class="txtSub ${empty tempDto ? 'displayNone' : '' }" id="idTxtSub">이미 등록된 아이디 입니다.</span>
        </c:if>
        <span class="txtSub" id="dupResult"></span>
        <span class="txtSub displayNone" id="idTxtSub2" >아이디는 영문자와 숫자로만 구성된 5~16자 사이여야 합니다.</span>
    </div>
```
```javascript
$(document).ready(function() {
	$('#checkUserId').click(function() {
		let userId = $('#idn').val();
		if (userId === '') {
			alert("아이디를 입력해 주세요.");
			return;
		}
		// AJAX 요청 보내기
		$.ajax({
			type: "POST",
			url: "./chkIdDuplication.do", // 서버의 URL
			data: { userId: userId }, // 보내는 데이터
			success: function(response) {
				// 서버에서 반환된 중복 여부 처리
				if (response.result) {
					$('#dupResult').text("중복된 아이디입니다.");
					flag=false;
				} else {
					$('#dupResult').text("사용 가능한 아이디입니다.");
					flag=true;
				}
			},
			error: function() {
				alert("중복 확인 중 오류가 발생했습니다.");
			}
		});
	});
});
```
#### 주요 기능 
  - ajax를 통한 ID 중복 검사
  - Json 객체로 결과를 반환
---

## LoginController.java
```java
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//로그인 필터를 통해 접근했을 경우 원래 시도했던 페이지 URL을 referer 정보로 저장
		String referer = request.getHeader("referer");
		String refererUri = "";
		if(referer!=null) {
			//URL에서 URI부분을 추출
			refererUri = referer.substring(referer.indexOf("://")+3).substring(referer.substring(referer.indexOf("://")+3).indexOf("/"));
			//쿼리스트링 보존을 위해 URLEncoder로 2번 인코딩
			refererUri = URLEncoder.encode(URLEncoder.encode(refererUri,"UTF-8"),"UTF-8");
		}
		//System.out.println(refererUri);
		//refererUri를 쿼리스트링으로 넘겨줌
		response.sendRedirect("./login.jsp?refererUri="+refererUri);
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		String refererUri = request.getParameter("refererUri");
		String memberId = request.getParameter("idn");
		String pwd = request.getParameter("pwd");
		String saveChk = request.getParameter("saveChk");
		MemberDAO dao = new MemberDAO();
		MemberDTO dto = dao.login(memberId, pwd);
		dao.close();
		//로그인 실패 시
		if(dto==null) {
			JSFunc.alertBack("아이디 또는 비밀번호가 적절하지 않거나 등록된 아이디가 아닙니다.", response);
			return;
		}
		//탈퇴한 회원일 때
		if(dto.getStatus().equals("N")) {
			JSFunc.alertBack("탈퇴한 회원입니다.",response);
			return;
		}
		//자동로그인 체크시
		if(saveChk!=null&&saveChk.equals("Y")) {
			//쿠키에 자동로그인 정보 저장
			CookieManager.makeCookie("savedId", dto.getMemberId(), 3600*24*7, "/", response);
		}else {//자동로그인 체크를 안했을 때
			//쿠키에 자동로그인 정보가 있는지 확인
			String savedId = CookieManager.cookieValue("savedId",request);
			//자동로그인 정보가 있을 때
			if(!savedId.isEmpty()) {
				//로그인 하려는 아이디와 같다면 자동로그인 정보를 지움
				if(savedId.equals(dto.getMemberId())){
					CookieManager.removeCookie("savedId",response);
				}
			}
		}
		session.setAttribute("loginDto", dto);//세션에 로그인 정보 저장
		//System.out.println("Login > doPost > refererUri" + refererUri);
		//회원가입페이지 또는 로그인페이지에서 요청할 경우는 refererUri를 사용하지 않음
		if(refererUri!=null&&!refererUri.isEmpty()&&!refererUri.contains("member%2Fregist")&&!refererUri.contains("login")) {
			//URLDecoder로 2번 디코딩
			refererUri=URLDecoder.decode(URLDecoder.decode(refererUri,"UTF-8"),"UTF-8");
			//refererURI로 리다이렉트
			response.sendRedirect(refererUri);
			return;
		}
		response.sendRedirect("../common/main.do");
	}
```

#### 주요 기능
  - request의 referer header를 활용하여 회원이 원래 요청했던 페이지의 URL 정보를 불러옴
  - String의 내장메서드를 활용하여 URL에서 URI 정보를 추출하여 redirect시 활용함
  - URLEncoder와 URLDecoder를 활용하여 원래 요청 페이지의 querystring 부분을 보존함
---

## JSFunc.java
```java
public class JSFunc {
	public static void alertLocation(String msg, String url, HttpServletResponse response) {
		try {
		PrintWriter writer = response.getWriter();
		response.setContentType("text/html;charset=UTF-8");
		String code = "<script>"
					+ "alert('"+msg+"');"
					+ "location.href='"+url+"';"
					+ "</script>";
		writer.print(code);
		}catch(Exception e) {
			
		}
	}
	public static void	back(HttpServletResponse response) {
		try {
			PrintWriter writer = response.getWriter();
			response.setContentType("text/html;charset=UTF-8");
			String code = "<script>"
						+ "history.back();"
						+ "</script>";
			writer.print(code);
			}catch(Exception e) {
				
			}
	}
	public static void alertBack(String msg, HttpServletResponse response) {
		try {
			PrintWriter writer = response.getWriter();
			response.setContentType("text/html;charset=UTF-8");
			String code = "<script>"
						+ "alert('"+msg+"');"
						+ "history.back();"
						+ "</script>";
			writer.print(code);
			}catch(Exception e) {
				
			}
	}
	public static void alert(String msg, HttpServletResponse response) {
		try {
			PrintWriter writer = response.getWriter();
			response.setContentType("text/html;charset=UTF-8");
			String code = "<script>"
						+ "alert('"+msg+"');"
						+ "</script>";
			writer.print(code);
			}catch(Exception e) {
				
			}
	}
}
```

#### 주요 기능
  - Java 서블릿에서 JavaScript 코드를 동적으로 생성하여 클라이언트(브라우저)로 전달하는 유틸리티 클래스
  - 사용자에게 알림 메시지 전송, 뒤로가기 등 기능을 간편하게 재사용 할 수 있음s