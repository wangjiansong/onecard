package com.interlib.sso.common;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 时间工具类
 * @author Lullaby
 *
 */
public class TimeUtils {

	//常用转换格式
	public static final String YYMMDD = "yy-MM-dd";
	public static final String YYYYMMDD = "yyyy-MM-dd";
	public static final String HHMMSS = "HH:mm:ss";
	public static final String YYYYMMDDHHMMSS = "yyyy-MM-dd HH:mm:ss";
	public static final String YYYYMMDDHHMM = "yyyy-MM-dd HH:mm";
	public static final String YYMMDDHHMMSS = "yy-MM-dd HH:mm:ss";
	
	//转换oracle时间使用的时间格式
	public static final String Oracle_YYMMDD = "YY-MM-DD";
	public static final String Oracle_YYYYMMDD = "YYYY-MM-DD";
	public static final String Oracle_HHMMSS = "HH24:MI:SS";
	public static final String Oracle_YYYYMMDDHHMMSS = "YYYY-MM-DD HH24:MI:SS";
	public static final String Oracle_YYYYMMDDHHMM = "YYYY-MM-DD HH24:MI";
	public static final String Oracle_YYMMDDHHMMSS = "YY-MM-DD HH24:MI:SS";
	
	private static DateFormat formatter;
	
	/**
	 * 时间对象转字符串
	 * @param date 时间对象
	 * @param pattern 格式
	 * @return
	 */
	public static String dateToString(Date date, String pattern) {
		if (date == null) {
			return null;
		}
		if (pattern == null || "".equals(pattern)) {
			pattern = YYYYMMDD;
		}
		formatter = new SimpleDateFormat(pattern);
		return formatter.format(date);
	}
	
	/**
	 * 字符串转时间对象
	 * @param datetime 时间对象
	 * @param pattern 格式
	 * @return
	 * @throws ParseException
	 */
	public static Date stringToDate(String dateString, String pattern) throws ParseException {
		if (dateString == null) {
			return null;
		}
		if (pattern == null || "".equals(pattern)) {
			pattern = YYYYMMDD;
		}
		formatter = new SimpleDateFormat(pattern);
		return formatter.parse(dateString);
	}
	
	/**
	 * 格式化时间对象
	 * @param date
	 * @param pattern
	 * @return
	 * @throws ParseException
	 */
	public static Date formatDate(Date date, String pattern) throws ParseException {
		if (date == null) {
			return null;
		}
		if (pattern == null || "".equals(pattern)) {
			pattern = YYYYMMDD;
		}
		formatter = new SimpleDateFormat(pattern);
		return formatter.parse(formatter.format(date));
	}
	public static void main(String[] args) {

		//获取时间加十年
	    Date date = new Date();
	    Calendar cal = Calendar.getInstance();
	    cal.setTime(date);//设置起时间
	    //System.out.println("111111111::::"+cal.getTime());
	    cal.add(Calendar.YEAR, 10);//增加十年 
	    System.out.println("输出::"+cal.getTime());
	    SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String sDate=sdf.format(cal.getTime());
		
	    System.out.println("输出::"+sDate);

	}
}
