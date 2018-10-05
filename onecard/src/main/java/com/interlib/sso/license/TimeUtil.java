/*
 * TimeUtil.java 1.0.6 2005-12-1
 * Copyright 2002 Tuchang, Inc. All rights reserved.
 * �������� 2005-12-1
 */
package com.interlib.sso.license;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

/**
 * @author toofu
 * @reviewer 	toofu
 * @version     1.0, 2003-9-12
 * @env		    JDK1.4.1_02	
 * @modified	toofu,	2005-12-1
 */
public class TimeUtil {
    /**
     * �����Ǵ������ڵĸ�ʽ.
     */
    public static String DATE_FORMAT="yyyyMMdd";
	/**
	 * ��ʽ�����ַ�ת�������ڶ���
	 * @param strDate
	 * @return
	 */
	public static Date str2Date(String strDate){
		Date result;
		try{	
			    SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
				result=sdf.parse(strDate);
				return result;
		}catch(Exception e){
			return null;
			//System.out.println("ת������ʱ����!��Ч�����ڣ��뱣�����ڸ����ȷ�Ը�ʽ���磺2000-01-02 12:04:02"+e.getMessage());
		}			 
	}
	/**
	 * �����ڶ���ת�����ַ�
	 * @param dateStr  Դ���ڶ���
	 * @return
	 */
	public static String date2Str(Date dateStr){
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
		String strNow=sdf.format(dateStr); 
		return strNow;
	}
	
	 /**
     * ��ȡ��ǰʱ��
     * @return yyyyMMdd
     */
    public static String getCurrentDateStr() {
        return TimeUtil.date2Str(new Date());
    }

    public static Date appendDay(Date old_date,int days) {
        Date new_date=old_date;
        new_date.setDate(old_date.getDate()+days);
        return new_date;
        
    }
    public static String addDay(String dateStr,int days) {
        Date ss=getDate(dateStr,"");
        GregorianCalendar gc=new GregorianCalendar();
        gc.setTime(ss);
        gc.add(GregorianCalendar.DAY_OF_YEAR,days);
        return getStringTime(gc.getTime(),"");
    }
    public static Date trimDate(Date in){
    	return getDate(getStringTime(in,"yyyyMMdd"),"yyyyMMdd");
    }
    public static Date getDate(String dateStr,String format) {
	    Date date = new Date();
	    if(format==null||format.equals(""))format="yyyyMMdd";
	    
	    SimpleDateFormat sdf = new SimpleDateFormat(format);//eg. yyyy-M-d  yyyyMMdd hh:mm:ss
	    try{
            date = sdf.parse(dateStr);
        } catch (ParseException e) {
        	e.printStackTrace();
        }
	    return date;
    }
    public static String getStringTime(Date date,String format) {
	    if(format==null||format.equals(""))format="yyyyMMdd";
	    SimpleDateFormat sdf = new SimpleDateFormat(format);//eg. yyyy-M-d  yyyyMMdd hh:mm:ss
	    String currenttime = sdf.format(date);
	    return currenttime;
    }
    public static String appendDay(String old_date,int days) {
        Date new_date=str2Date(old_date);
        new_date.setDate(new_date.getDate()+days);
        return date2Str(new_date);
    }
    
    public static XMLGregorianCalendar getXMLGregorianCalendar() {
		
		GregorianCalendar cal = new GregorianCalendar();
		
		cal.setTime(new Date());
		
		XMLGregorianCalendar xmlDatetime = null;
		
		try {
			xmlDatetime = DatatypeFactory.newInstance().newXMLGregorianCalendar(cal);
			
		} catch (DatatypeConfigurationException e) {
			e.printStackTrace();
		} 
		return xmlDatetime;
	}
    
    public static void main(String[] args) {
    	
    }
}
