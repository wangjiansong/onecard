package com.interlib.sso.common.beanutils.converter;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.support.WebBindingInitializer;
import org.springframework.web.context.request.WebRequest;

public class DateConverter implements WebBindingInitializer {
	
	/**
	 * spring3 mvc 的日期传递[前台String-后台Date] bug:
	 * org.springframework.validation.BindException 
	 * 解决方式.包括xml的配置 
	 */
	public void initBinder(WebDataBinder binder, WebRequest request) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		binder.registerCustomEditor(Date.class, new CustomDateEditor(sdf, true));
	}
	
}
