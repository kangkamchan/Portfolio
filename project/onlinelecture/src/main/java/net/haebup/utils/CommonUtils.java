package net.haebup.utils;

import java.io.PrintWriter;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse; 

public class CommonUtils {
	public void setCookiesInfo(HttpServletRequest req, HttpServletResponse res, String path, int maxAge, String domain, String cName, String cValue){
		Cookie cookie = new Cookie(cName, cValue);
		cookie.setPath(path != null ? path : req.getContextPath());	// /
		cookie.setMaxAge(maxAge);
		cookie.setDomain(domain != null ? domain : "/");
		res.addCookie(cookie);
	}

	public String getCookiesInfo(HttpServletRequest req, String cName){
		Cookie[] cookies = req.getCookies();	// 요청 헤더에 있는 모든 쿠키 조회
		if ( cookies != null ){
			for(Cookie c : cookies){
				String cookieName = c.getName();
				String cookieValue= c.getValue();

				if ( cookieName.equals(cName) ) return cookieValue;
			}
		}
		
		return "";
	}

	public String getCookiesInfo(HttpServletRequest req, PrintWriter o, String cName){
		Cookie[] cookies = req.getCookies();	// 요청 헤더에 있는 모든 쿠키 조회
		if ( cookies != null ){
			for(Cookie c : cookies){
				String cookieName = c.getName();
				String cookieValue= c.getValue();
				o.print(String.format("%s : %s<br>", cookieName, cookieValue));

				if ( cookieName.equals(cName) ) return cookieValue;
			}
		}
		
		return "";
	}
	
	 public void deleteCookie(HttpServletRequest req, HttpServletResponse res, String key) {
	        Cookie cookie = new Cookie(key, null); 
	        cookie.setMaxAge(0); 
	        cookie.setPath("/"); 
	        res.addCookie(cookie); 
	    }
	
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
	public static String getRefererUriWithQueryString(String referer) {
		if(referer!=null) {
			return referer.substring(referer.indexOf("://")+3).substring(referer.substring(referer.indexOf("://")+3).indexOf("/"));
		}
		return "";
	}
}
