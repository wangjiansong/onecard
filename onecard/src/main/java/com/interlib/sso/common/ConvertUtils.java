package com.interlib.sso.common;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

public class ConvertUtils {

	public static XMLGregorianCalendar getXmlDatetime(String dateTimeStr, String format) {
		
		GregorianCalendar nowGregorianCalendar = new GregorianCalendar();
		
		XMLGregorianCalendar xmlDatetime = null;
		
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		try {
			nowGregorianCalendar.setTime(sdf.parse(dateTimeStr));
			
			xmlDatetime = DatatypeFactory.newInstance().newXMLGregorianCalendar(nowGregorianCalendar);
			
		} catch (DatatypeConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return xmlDatetime;
	}
	
	public static XMLGregorianCalendar getXmlDatetime(Date date, String format) {
		
		GregorianCalendar nowGregorianCalendar = new GregorianCalendar();
		
		XMLGregorianCalendar xmlDatetime = null;
		
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		try {
			String dateTimeStr = sdf.format(date);
			
			nowGregorianCalendar.setTime(sdf.parse(dateTimeStr));
			
			xmlDatetime = DatatypeFactory.newInstance().newXMLGregorianCalendar(nowGregorianCalendar);
			
		} catch (DatatypeConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return xmlDatetime;
	}
}
