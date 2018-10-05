package com.interlib.sso.common;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtils {
	
	// 获取当月第一天  
    public static String getFirstDayOfMonth() {  
        String strFirstDay = "";  
        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy/MM/dd");  
          
        Calendar calendar = Calendar.getInstance();  
        calendar.set(Calendar.DATE, 1);     // 设置当前月的1号  
          
        strFirstDay = sDateFormat.format(calendar.getTime());  
        return strFirstDay;  
    }  
  
    // 获取当月最后一天  
    public static String getLastDayOfMonth() {  
        String strLastDay = "";  
        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy/MM/dd");  
          
        Calendar calendar = Calendar.getInstance();  
        calendar.set(Calendar.DATE, 1);     // 设置当前月的1号  
        calendar.add(Calendar.MONDAY, 1);   // 加一个月，变为下月的1号  
        calendar.add(Calendar.DATE, -1);    // 减去一天，变为当前月的最后一天  
          
        strLastDay = sDateFormat.format(calendar.getTime());  
        return strLastDay;  
    }  

    /**
     * 取得两个时间之间的月数
     * @param date1
     * @param date2
     * @return
     */
    public static int getMonthSpace(Date date1, Date date2) {
        int result = 0;

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        String d1 = sdf.format(date1);
        String d2 = sdf.format(date2);
        
        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();

        try {
			c1.setTime(sdf.parse(d1));
			c2.setTime(sdf.parse(d2));
		} catch (ParseException e) {
			e.printStackTrace();
		}
        
        if(c2.get(Calendar.YEAR) == c1.get(Calendar.YEAR)) {
            result = c2.get(Calendar.MONTH) - c1.get(Calendar.MONTH);
        } else {
            result = 12*(c2.get(Calendar.YEAR) - c1.get(Calendar.YEAR)) + c2.get(Calendar.MONTH) - c1.get(Calendar.MONTH);
        }
        
        return result;

    }
    
	/**
	 * @param args
	 */
	public static void main(String[] args) {
	}

}
