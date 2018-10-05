package com.interlib.sso.common.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ServletUtil {
	
	/**
	 * 异步请求返回String
	 * 类型：text/xml
	 * @param encoding 编码格式
	 * @param data 返回数据
	 * @param response 对象 
	 */
	public static void responseOut(String encoding, String data, HttpServletResponse response) {
		response.setContentType("text/xml;charset="+encoding);
		try {
			PrintWriter writer = response.getWriter();
			writer.print(data);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 自定返回类型的异步返回
	 * @param type contentType类型
	 * @param encoding 编码格式
	 * @param data 返回数据
	 * @param response 对象
	 */
	public static void responseCustom(String type, String encoding, String data, HttpServletResponse response) {
		response.setContentType(type+";charset="+encoding);
		try {
			PrintWriter writer = response.getWriter();
			writer.print(data);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 获取request对象中所有参数并设置到map中，不包含为null或""的参数
	 */
	public static Map<String,String> getMapByRequest(HttpServletRequest request) {
		Map<String,String> map = new HashMap<String,String>();
		Enumeration<?> enums = request.getParameterNames();
		while(enums.hasMoreElements()){
			String paramName = (String) enums.nextElement();
			String paramValue = request.getParameter(paramName);
			if(paramValue!=null && !"".equals(paramValue)){
				map.put(paramName, paramValue);
			}
		}
		return map;
	}
	
	/**
	 * 获取request对象中所有参数并设置到map中，包含为null或""的参数
	 */
	public static Map<String,String> getExistParamByRequest(HttpServletRequest request) {
		Map<String,String> map = new HashMap<String,String>();
		Enumeration<?> enums = request.getParameterNames();
		while(enums.hasMoreElements()){
			String paramName = (String) enums.nextElement();
			String paramValue = request.getParameter(paramName);
			map.put(paramName, paramValue);
		}
		return map;
	}
	
}
