package net.tclass.utils;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class CookieManager {
	public static void makeCookie(String name, String value, int age, String path, HttpServletResponse res) {
		Cookie c = new Cookie(name,value);
		c.setMaxAge(age);
		c.setPath((path!=null)?path : "/");
		res.addCookie(c);
	}
	public static String cookieValue(String name, HttpServletRequest req) {
		Cookie[] cookies = req.getCookies();
		String cValue = "";
		if(cookies!=null) {
			for(Cookie c : cookies) {	
				if(c.getName().equals(name)) {
					cValue = c.getValue();
				}
			}
		}
		return cValue;
	}
	public static void removeCookie(String name, HttpServletResponse res) {
		makeCookie(name,"",0,"/",res);
	}
	public static void removeCookie(String name, HttpServletRequest req, HttpServletResponse res) {
		makeCookie(name,cookieValue(name,req),0,"/",res);
	}
}
