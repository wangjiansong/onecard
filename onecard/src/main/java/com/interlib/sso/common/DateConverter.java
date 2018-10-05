package com.interlib.sso.common;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.core.convert.converter.Converter;
  
  
  
/** 
 *  
 * spring3 mvc 的日期传递[前台-后台]bug: 
 * org.springframework.validation.BindException 
 * 的解决方式.包括xml的配置 
 * @author zhanglei 
 * 
 */  
public class DateConverter implements Converter<String, Date> {    
	@Override    
	public Date convert(String source) {    
	    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");    
	    dateFormat.setLenient(false);    
	    try {    
	        return dateFormat.parse(source);    
	    } catch (ParseException e) {    
	        e.printStackTrace();    
	    }           
	    return null;    
	}  
}
