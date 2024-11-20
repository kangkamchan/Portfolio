package net.tclass.utils;

public class CommonUtil {
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
	
}
