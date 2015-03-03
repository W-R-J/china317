package com.china317.gmmp.gmmp_report_analysis.util;

import java.util.HashMap;
import java.util.Map;

public class LineDenoterCodeUtil {
	public static String[] prov_Name = { "京", "津", "冀", "晋", "蒙", "辽", 
		"吉","黑", "沪", "苏", "浙", "皖", "闽", "赣", "鲁", "豫", "鄂", 
		"湘", "粤", "桂","琼", "渝", "川", "贵", "云", "藏", "陕", "甘",
		"青", "宁", "新", "测" };
	
	public static String[] prov_Code = { "11", "12", "13", "14", "15", "21",
		"22", "23", "31", "32", "33", "34", "35", "36", "37", "41", "42",
		"43", "44", "45", "46", "50", "51", "52", "53", "54", "61", "62",
		"63", "64", "65", "99" };
	
	public static String[] color_Code = { "1", "2", "3", "4", "9" };
	public static String[] color_Name = { "蓝", "黄", "黑", "白", "其他" };
	public static Map<String, String> codeMap = new HashMap<String, String>();
	public static Map<String, String> colorMap = new HashMap<String, String>();

	static {
		for (int i = 0; i < prov_Code.length; i++) {
			codeMap.put(prov_Name[i], prov_Code[i]);
		}

		for (int i = 0; i < color_Name.length; i++) {
			colorMap.put(color_Name[i], color_Code[i]);
		}
	}
	
	/**
	 * 获取key
	 * @param license
	 * @param color
	 * @return
	 */
	public static String getVehicleCode(String license, String color) {
		if(license==null) return null;
		String code = null;
		String name = license.trim().replaceAll("-", "");
		name = name.replaceAll(" ", "");
		String pri = "";
		if(license!=null){
			if(license.length() > 1){
				pri = license.substring(0, 1);
			}
		}
		if (codeMap.containsKey(pri)) {
			code = codeMap.get(pri);
			code += name.substring(1);
			if (colorMap.containsKey(color)) {
				code += colorMap.get(color);
			} else {
				code += colorMap.get("其他");
			}
		} else {
			code = name;
		}
		return code;
	}

}
