package com.china317.gmmp.gmmp_report_analysis.util;

public final class StringUtil {
	public static String replaceNull(String str,String replace){
		return (str==null)? replace : str;
	}
	public static String replaceNull(String str){
		return replaceNull(str,"");
	}
	
	public static String replaceNull(Object obj){
		if(obj==null){
			return "";
		}else{
			return obj.toString();
		}
	}
	
	public static String convertTime(long time) {
		String value="";
		int h, m, s;
		h = (int) (time / 3600);
		m = (int) ((time % 3600) / 60);
		s = (int) ((time % 3600) % 60);
		
		if(h>0){
			value = h+"Сʱ"+m+"��"+s+"��";
		}else{
			if(m>0){
				value =+m+"��"+s+"��";
			}else{
				value = s+"��";
			}
		}
		
		return value;
	}
}
