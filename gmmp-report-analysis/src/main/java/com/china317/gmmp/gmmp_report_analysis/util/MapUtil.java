/*
 * Created on 2006-3-17
 *
 * 
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.china317.gmmp.gmmp_report_analysis.util;

/**
 * @author JosephZhou
 *
 * 
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class MapUtil {

	/**
	 * 
	 */
	public MapUtil() {
		super();
		// 
	}
	
	public static long distance(double lon1,double lat1,double lon2,double lat2) 
	{ 
		double R=6378136.49;//mi
		double PI=Math.PI;
		double dlon=Math.abs(lon1-lon2);
		if(dlon>180)dlon=360-dlon;
		double lsqure=2-2*Math.sin(lat1*PI/180)*Math.sin(lat2*PI/180)-2*Math.cos(lat1*PI/180)*Math.cos(lat2*PI/180)*Math.cos(dlon*PI/180);
		double d=Math.asin(Math.sqrt(lsqure)*0.5)*2*R;
		return Math.round(d*100); 
	}

	public static void main(String[] args) {
		System.out.println(MapUtil.distance(0.0041667,31.236111,0.0041667,31.237222));
	}
}
