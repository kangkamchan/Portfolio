# tspoon 주요 소스코드
---
 
## BbsListController.java
공통 게시판 리스트를 처리하는 Controller 클래스

### 백엔드 코드
```java
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Map<String, String> map = (Map<String, String>)request.getAttribute("map");
		BbsDAO dao = new BbsDAO();
		List<BbsDTO> bbsList = dao.getPostList(map);
		int totalCnt = dao.getTotalCount(map);
		dao.close();
		String queryString = Paging.getQueryString(map);
		map.put("queryString", queryString);
		map.put("totalCount", String.valueOf(totalCnt));
		String pagingBlock = Paging.getPageBlock(map, request);
		String pageNo =CommonUtils.ifNullString(map.get("pageNo"), "1");
		request.setAttribute("result", "success");
		request.setAttribute("bbsList", bbsList);
		request.setAttribute("pagingBlock", pagingBlock);
		request.getRequestDispatcher(reqUri.split("\\?")[0]+"?action=bbsListOk&pageNo="+pageNo).forward(request, response);
	}
```
#### 기능설명
  - 게시판 형태의 데이터의(공지사항, 후기, 자료실 등) 리스트조회를 공통으로 처리하는 ServeletController
  - 게시판 형태별 상이한 데이터를 처리하기 위해 Map객체를 활용해 파라미터를 전송함
    
## Paging.java
페이징 기능을 처리하는 클래스

### 백엔드 코드
```java
	public static String getQueryString(Map<String,String> map) {
		Map<String,String> tempMap = map;
		Set<String> kSet = tempMap.keySet();
		Iterator<String> it = kSet.iterator();
		//값이 없는 key 제거
		while(it.hasNext()) {
			String key = it.next();
			if(tempMap.get(key)==null || tempMap.get(key).equals("")) {
				it.remove();
			}
		}
		it = null;
		it = tempMap.keySet().iterator();
		StringBuilder sb = new StringBuilder();
		//값이 있는 key에 대해서 querystring 형태로 재구성
		while(it.hasNext()) {
			String key = it.next();
			if(key.equals("pageNo")||key.equals("pageSize")) continue;
			sb.append(key+"="+tempMap.get(key));
			//다음 값이 있을 때만 &를 붙임
			if(it.hasNext()) {
				sb.append("&");
			}
		}
		return sb.toString();
	}
```
#### 기능설명
  - 공통 게시판 영역 페이징을 위한 Map 객체를 파라미터로 링크 이동시 사용할 querystring을 반환하는 메서드
  - Map 객체의 keySet 속성과 Iterator 객체의 remove()메서드를 활용함
	
## CommonUtilis.java
개발 전반에서 사용하는 공통 유틸리티 메서드를 구현한 클래스

### 백엔드 코드
```java
	public static String ifNullString(String str, String defaultStr) {
		if(str!=null&&!str.equals("")) {
			return str;
		}
		return defaultStr;
	}
		
	public static int ifNullInt(String str, int defaultInt) {
		if(str!=null&&!str.equals("")) {
			return Integer.parseInt(str);
		}
		return defaultInt;
	}
	
	public static String getRefererUri(String referer) {
		if(referer!=null) {
			return referer.substring(referer.indexOf("://")+3).substring(referer.substring(referer.indexOf("://")+3).indexOf("/"));
		}
		return "";
	}
```
#### 기능설명
  - request parameter를 처리할 때 NullpointerException을 회피하고 기본값을 주입하는 공통메서드 구현
  - URL에서 URI부분만 추출하는 공통메서드 구현