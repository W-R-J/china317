package com.china317.gmmp.gmmp_report_analysis.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * <p>
 * Title: workflow
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: china317
 * </p>
 * @author maple.shi
 * @version 1.0
 */
public class DateTime {
	/**
	 * yyyyMMddHHmmss
	 */
	public static final String	PATTERN_0	= "yyyyMMddHHmmss";
	public static final SimpleDateFormat FORMATTER_0 = new SimpleDateFormat(PATTERN_0);
	
	/**
	 * yyyy-MM-dd HH:mm:ss
	 */
	public static final String	PATTERN_1	= "yyyy-MM-dd HH:mm:ss";
	/**
	 * yyyy-MM-dd
	 */
	public static final String	PATTERN_2	= "yyyy-MM-dd";
	/**
	 * yyyy��MM��dd�� HHʱmm��ss��
	 */
	public static final String	PATTERN_3	= "yyyy��MM��dd�� HHʱmm��ss��";
	/**
	 * yyyy��MM��dd��
	 */
	public static final String	PATTERN_4	= "yyyy��MM��dd��";
	/**
	 * yyyy-MM-dd HH:mm
	 */
	public static final String	PATTERN_5	= "yyyy-MM-dd HH:mm";
	/**
	 * yyyy��MM��dd�� HHʱmm��
	 */
	public static final String	PATTERN_6	= "yyyy��MM��dd�� HHʱmm��";
	/**
	 * yyyyMMdd
	 */
	public static final String	PATTERN_7	= "yyyyMMdd";
	/**
	 * HHmmss
	 */
	public static final String	PATTERN_8	= "HHmmss";
	/**
	 * HH:mm:ss
	 */
	public static final String	PATTERN_9	= "HH:mm:ss";
	/**
	 * HH:mm
	 */
	public static final String	PATTERN_10	= "HH:mm";

	public static String getSysTime() {
		return new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
	}

	public static byte[] getSimpleSysTime() {
		byte[] ret = new byte[3];
		Calendar now = Calendar.getInstance();
		ret[0] = (byte) now.get(now.HOUR_OF_DAY);
		ret[1] = (byte) now.get(now.MINUTE);
		ret[2] = (byte) now.get(now.SECOND);
		return ret;
	}

	/*
	 * 
	 */
	public static String getTime(String pattern, Calendar cal) {
		return new SimpleDateFormat(pattern).format(cal.getTime());
	}

	public static String getSysTime(String pattern) {
		return new SimpleDateFormat(pattern).format(new Date());
	}

	public static Calendar getDateTime(String dateTime) {
		Calendar date = (Calendar) Calendar.getInstance().clone();
		int year = parseInt(dateTime.substring(0, 4), 1900);
		int Month = parseInt(dateTime.substring(4, 6), 1) - 1;
		int day = parseInt(dateTime.substring(6, 8), 1);
		int hh = parseInt(dateTime.substring(8, 10), 0);
		int mm = parseInt(dateTime.substring(10, 12), 0);
		int ss = parseInt(dateTime.substring(12, 14), 0);
		date.set(year, Month, day, hh, mm, ss);
		return date;
	}

	public static String getDateTimeString(Date t) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
		String time = formatter.format(t);
		return time;
	}

	public static String getDateTimeString(Date t, String pattern) {
		SimpleDateFormat formatter = new SimpleDateFormat(pattern);
		String time = formatter.format(t);
		return time;
	}

	public static String reFormatTime(String str, String oldPattern, String newPattern) {
		try {
			SimpleDateFormat oldFormat = new SimpleDateFormat(oldPattern);
			SimpleDateFormat newFormat = new SimpleDateFormat(newPattern);
			return newFormat.format(oldFormat.parse(str));
		} catch (Exception e) {
			System.out.println("format time Error (" + e.toString() + ")");
			return null;
		}// try//
	}

	public static String formatTime(String str, String pattern) {
		try {
			if (str == null) {
				return null;
			}
			int size = str.length();
			if (size == 0) {
				return "";
			}
			if (size < 14) {
				for (int i = 0; i < 14 - size; i++) {
					str = str + "0";
				}
			}
			int nYear = Integer.parseInt(str.substring(0, 4), 10);
			int nMonth = Integer.parseInt(str.substring(4, 6), 10);
			int nDay = Integer.parseInt(str.substring(6, 8), 10);
			int nHour = Integer.parseInt(str.substring(8, 10), 10);
			int nMinute = Integer.parseInt(str.substring(10, 12), 10);
			int nSecond = Integer.parseInt(str.substring(12, 14), 10);
			Calendar tmpCalendar = Calendar.getInstance(TimeZone.getTimeZone("CTT"));
			tmpCalendar.set(nYear, nMonth - 1, nDay, nHour, nMinute, nSecond);
			SimpleDateFormat formatter = new SimpleDateFormat(pattern);
			Date tmpDate = tmpCalendar.getTime();
			return formatter.format(tmpDate);
		} catch (Exception e) {
			return null;
		}
	}

	public static String getFormatTimebyInt(String str) {
		try {
			if (str == null) {
				return null;
			}
			int size = str.length();
			if (size < 6) {
				for (int i = 0; i < 6 - size; i++) {
					str = "0" + str;
				}
			}
			return str.substring(0, 2) + ":" + str.substring(2, 4) + ":" + str.substring(4, 6);
		} catch (Exception e) {
			return "";
		}
	}

	public static String formatTime(String str) {
		try {
			if (str == null) {
				return null;
			}
			int size = str.length();
			if (size < 6) {
				for (int i = 0; i < 6 - size; i++) {
					str = "0" + str;
				}
			}
			return str.substring(0, 2) + ":" + str.substring(2, 4) + ":" + str.substring(4, 6);
		} catch (Exception e) {
			return "";
		}
	}

	public static int parseInt(String str, int defaultvalue) {
		try {
			return Integer.parseInt(str);
		} catch (Exception e) {
			e.printStackTrace();
			return defaultvalue;
		}
	}

	/**
	 * ���������ʱ���,��λ����
	 * @param time1
	 *        yyyyMMddHHmmss
	 * @param time2
	 *        yyyyMMddHHmmss
	 * @return
	 */
	public static int accountTime(String time1, String time2) {
		Calendar c1 = getDateTime(time1);
		Calendar c2 = getDateTime(time2);
		return (int) ((c2.getTimeInMillis() - c1.getTimeInMillis()) / 1000);
	}
	
	public static long accountTimeLong(String time1, String time2) {
		Calendar c1 = getDateTime(time1);
		Calendar c2 = getDateTime(time2);
		return ((c2.getTimeInMillis() - c1.getTimeInMillis()) / 1000);
	}

	/**
	 * ���������ʱ���,��λ������
	 * @param time_start
	 *        ����ʱ��ֵ����ʽ��hhmmdd��
	 * @param time_end
	 *        ����ʱ��ֵ����ʽ��hhmmdd��
	 * @return
	 *         ���ط��Ӳ�ֵ
	 */
	public static int accountTimeMinute(int time_start, int time_end) {
		return timeTransformMinute(time_end) - timeTransformMinute(time_start);
	}

	/**
	 * ת���ɷ���
	 * @param time
	 * @return
	 */
	public static int timeTransformMinute(int time) {
		int hh = time / 100;
		int mm = time % 100;
		return mm + hh * 60;
	}

	/**
	 * ���������ʱ���,��λ����
	 * @param time_start
	 *        ����ʱ��ֵ����ʽ��hhmmdd��
	 * @param time_end
	 *        ����ʱ��ֵ����ʽ��hhmmdd��
	 * @return
	 */
	public static int accountTime(long time_start, long time_end) {
		return timeTransformSecond((int) time_end) - timeTransformSecond((int) time_start);
	}

	public static int timeTransformSecond(int time) {
		int temp = time;
		int hh = temp / 10000;
		temp = temp % 10000;
		int mm = temp / 100;
		int ss = temp % 100;
		return (ss) + (mm) * 60 + hh * 3600;
	}

	public static String getTimebyInt(long time) {
		String temp = "000000" + time;
		return (temp).substring(temp.length() - 6);
	}

	public static String getFormatTimebyInt(long time) {
		String temp = "000000" + time;
		temp = (temp).substring(temp.length() - 6);
		return ((temp.charAt(0) == '0') ? temp.substring(1, 2) : temp.substring(0, 2)) + ":" + temp.substring(2, 4) + ":" + temp.substring(4, 6);
	}

	public static float getPer(Date date){
		Calendar time = Calendar.getInstance();
		time.setTime(date);
		time.set(Calendar.HOUR_OF_DAY, 0);
		time.set(Calendar.MINUTE, 0);
		time.set(Calendar.SECOND, 0);
		time.set(Calendar.MILLISECOND, 0);
		long overTime = (date.getTime()-time.getTimeInMillis())% (1000*60*60*24);
		return (float)(Math.round((overTime*1.0/(1000*60*60*24))*1000))/1000;
	}
	public static void main(String[] args) {
		String temp = "012300";
		System.out.println(temp.charAt(0) == '0');
	}

	//���㲢��ʽ������ʱ��
	public static String formatLxDate(Date gpsTime) {
		Date now = new Date();
		long between  = (now.getTime()-gpsTime.getTime())/1000;
		//long year = between/(12*30*24*3600);
		//long month = between%(12*30*24*3600)/(30*24*3600);
		//long day = between%(30*24*3600)/(24*3600);
		long day = between/(24*3600);
		long hour = between%(24*3600)/3600;
		long minute = between%3600/60;
		long second = between%60;
		String reStr = "";
		//if(year != 0){
		//	reStr += year+"��";
		//}
		//if(month != 0){
		//	reStr += month+"��";
		//}
		if(day != 0){
			reStr += day+"��";
		}
		if(hour !=0){
			reStr += hour+"Сʱ";
		}
		if(minute != 0){
			reStr += minute+"����";
		}
		if(second != 0){
			reStr += second+"��";
		}
		return reStr;
	}
	
	/**
	 * 
	 * @param str ��ʽ��yyyyMMddHHmmss
	 * @return
	 * @throws Exception
	 */
	public static Date parseDate(String str)throws Exception {
		return parseDate(str, "yyyyMMddHHmmss");
	}
	
	public static Date parseDate(String str, String format)throws Exception {
		if (null != str && str.length() > 0) {
			return new SimpleDateFormat(format).parse(str);
		} else {
			return null;
		}
	}

	public static long accountTime3(Date begin, Date end){
		try{
						
			long between=(end.getTime()-begin.getTime())/1000;//����1000��Ϊ��ת������
				
			return between;
		}catch(Exception e){
			return -1;
		}
	}
	public static String accountTime2_New(String from, String to){
		try{
			SimpleDateFormat dfs = new SimpleDateFormat("yyyyMMddHHmmss");
			java.util.Date begin=dfs.parse(from);
			java.util.Date end = dfs.parse(to);
			long between=(end.getTime()-begin.getTime())/1000;//����1000��Ϊ��ת������
	
			long day1=between/(24*3600);
			long hour1=between%(24*3600)/3600;
			long minute1=between%3600/60;
			long second1=between%60/60;
			
			hour1 += day1*24;
			
			return ""+hour1+"Сʱ"+minute1+"����"+second1+"��";
		}catch(Exception e){
			return null;
		}
	}
	
	public static int getDays(Calendar c1, Calendar c2) {
		  int elapsed = 0;
		  Calendar temp1, temp2;

		  if (c2.after(c1)) {
		   temp2 = (Calendar) c2.clone();
		   temp1 = (Calendar) c1.clone();
		  } else {
		   temp2 = (Calendar) c1.clone();
		   temp1 = (Calendar) c2.clone();
		  }

		  temp1.clear(Calendar.MILLISECOND);
		  temp1.clear(Calendar.SECOND);
		  temp1.clear(Calendar.MINUTE);
		  temp1.clear(Calendar.HOUR_OF_DAY);

		  temp2.clear(Calendar.MILLISECOND);
		  temp2.clear(Calendar.SECOND);
		  temp2.clear(Calendar.MINUTE);
		  temp2.clear(Calendar.HOUR_OF_DAY);

		  while (temp1.before(temp2)) {
		   temp1.add(Calendar.DATE, 1);
		   elapsed++;
		  }
		  return elapsed;
		 }
}
