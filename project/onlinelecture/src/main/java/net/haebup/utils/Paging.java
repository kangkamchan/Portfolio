package net.haebup.utils;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import jakarta.servlet.http.HttpServletRequest;

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
	
	public static String getPageBlock(Map<String,String> map, HttpServletRequest request) {
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
		String reqUri =(map.get("reqUri") != null ? map.get("reqUri") : "" );
		StringBuilder sb = new StringBuilder();
		sb.append("<nav aria-label='Page navigation example'><ul class='pagination'>");
		if(blockStart>1) {
		sb.append("<li class='page-item'>");
		sb.append("<a class='page-link' href='"+request.getContextPath()+reqUri+queryString+"&pageNo="+(blockStart-1)+"&pageSize="+pageSize+"' aria-label='Previous'>"
				+ "<span aria-hidden='true'>&laquo;</span>"
				+ "</a>");
		sb.append("</li>");
		}
		for(int i = blockStart; i < blockEnd; i++) {
			if(i!=pageNo) {
			sb.append("<li class='page-item'>"
					+ "<a class='page-link' href='"+request.getContextPath()+reqUri+queryString+"&pageNo="+i+"&pageSize="+pageSize+"'>"
					+ i
					+ "</a>"
					+ "</li>");
			}
			else {
				sb.append("<li class='page-item active'>"
						+ "<a class='page-link' href='"+request.getContextPath()+reqUri+queryString+"&pageNo="+i+"&pageSize="+pageSize+"'>"
						+ i
						+ "</a>"
						+ "</li>");
			}
		}
		if(blockEnd<=totalPage) {
			sb.append("<li class='page-item'>");
			sb.append("<a class='page-link' href='"+request.getContextPath()+reqUri+queryString+"&pageNo="+(blockEnd)+"&pageSize="+pageSize+"' aria-label='Next'>"
					+ "<span aria-hidden='true'>&laquo;</span>"
					+ "</a>");
			sb.append("</li>");
		}
		return sb.append("</ul></nav>").toString();

	}
	
	
	public static String getQueryString(Map<String,String> map) {
		Map<String,String> tempMap = map;
		Set<String> kSet = tempMap.keySet();
		Iterator<String> it = kSet.iterator();
		while(it.hasNext()) {
			String key = it.next();
			if(tempMap.get(key)==null || tempMap.get(key).equals("")) {
				it.remove();
			}
		}

		it = null;
		it = tempMap.keySet().iterator();
		StringBuilder sb = new StringBuilder();
		while(it.hasNext()) {
			String key = it.next();
			if(key.equals("pageNo")||key.equals("pageSize")) continue;
			sb.append(key+"="+tempMap.get(key));
			if(it.hasNext()) {
				sb.append("&");
			}
		}
		return sb.toString();
	}
}
