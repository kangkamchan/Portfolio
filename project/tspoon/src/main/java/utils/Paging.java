package net.tclass.utils;

import java.util.Map;

public class Paging {
	public static String getPageBlock(int totalPage, int pageNo, int pageSize, String queryString) {
		int blockStart = ((pageNo-1)/10)*10 + 1;
		int blockEnd = blockStart + 10;
		if(blockEnd>totalPage+1) {
			blockEnd = totalPage+1;
		}
		StringBuilder sb = new StringBuilder();
		if(blockStart>1) {
		sb.append("<a href='?pageNo="+(blockStart-1)+"&pageSize="+pageSize+"' class='pageBlock'>이전 10개</a>    ");
		}else {
			sb.append("<a  href='?pageNo="+1+"&pageSize="+pageSize+"' class='pageBlock'>이전 10개</a>    ");
		}
		for(int i = blockStart; i < blockEnd; i++) {
			if(i!=pageNo) {
			sb.append("<a href='?pageNo="+i+"&pageSize="+pageSize+"' class='pageBlock'>   "+i+"   </a>");
			}
			else {
				sb.append("<span class='tempPage'>   ["+i+"]   </span>");
			}
		}
		if(blockEnd<=totalPage) {
			return sb.append("<a  href='?pageNo="+(blockEnd)+"&pageSize="+pageSize+"' class='pageBlock'>다음 10개</a>").toString();
		}else {
			return sb.append("<a  href='?pageNo="+(blockEnd-1)+"&pageSize="+pageSize+"' class='pageBlock'>다음 10개</a>").toString();
		}
	}
	
	public static String getPageBlock(Map<String,String> map) {
		int pageNo = 1;
		if(map.get("pageNo")!=null&&!map.get("pageNo").equals("")) {
			pageNo = Integer.parseInt(map.get("pageNo"));
		}
		int pageSize = 10;
		if(map.get("pageSize")!=null&&!map.get("pageSize").equals("")) {
			pageSize = Integer.parseInt(map.get("pageSize"));
		}
		int totalCount = Integer.parseInt(map.get("totalCount"));
		int totalPage = ((totalCount-1)/pageSize) + 1;
		int blockStart = ((pageNo-1)/10)*10 + 1;
		int blockEnd = blockStart + 10;
		if(blockEnd>totalPage+1) {
			blockEnd = totalPage+1;
		}
		String queryString = (map.get("queryString") != null ? map.get("queryString") : "" );
		String reqUrl =(map.get("reqUrl") != null ? map.get("reqUrl") : "" );
		StringBuilder sb = new StringBuilder();
		if(blockStart>1) {
		sb.append("<a  href='"+reqUrl+"?pageNo="+1+"&pageSize="+pageSize+queryString+"' class='pageBlock'>[첫 페이지]</a>    ");
		sb.append("<a href='"+reqUrl+"?pageNo="+(blockStart-1)+"&pageSize="+pageSize+queryString+"' class='pageBlock'>[이전 10개]</a>    ");
		}
		for(int i = blockStart; i < blockEnd; i++) {
			if(i!=pageNo) {
			sb.append("<a href='"+reqUrl+"?pageNo="+i+"&pageSize="+pageSize+queryString+"' class='pageBlock'>   "+i+"   </a>");
			}
			else {
				sb.append("<span class='tempPage'>   ["+i+"]   </span>");
			}
		}
		if(blockEnd<=totalPage) {
			sb.append("<a  href='"+reqUrl+"?pageNo="+(blockEnd)+"&pageSize="+pageSize+queryString+"' class='pageBlock'>[다음 10개]</a>").toString();
			return sb.append("<a  href='"+reqUrl+"?pageNo="+(totalPage)+"&pageSize="+pageSize+queryString+"' class='pageBlock'>[마지막 페이지]</a>").toString();
		}else {
			return sb.toString();
		}
	}
}
