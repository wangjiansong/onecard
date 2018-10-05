package com.interlib.sso.common;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
/**
 * 对响应进行处理的封装类
 * @author toofu
 *
 */
public class ResponseUtil {
	/**
	 * 获取log4j用于write log
	 */
	private static final Log log = LogFactory.getLog(ResponseUtil.class);
	/**
	 * 当没有登录时刻的返回，这里先暂时这样，可以修改。
	 * @param response
	 */
	public static void sendNoLoginJsonRs(HttpServletResponse response){
		boolean isOK=false;
		String msg="您没有登录，请进行登录";
		sendJson(response,JsonUtil.makeOneRsJson(isOK,msg));
	}
	/**
	 * 返回增加修改删除登录等的处理结果 
	 * @param response HttpServletResponse
	 * @param isOK true or false
	 * @param msg eg.增加失败
	 */
	public static void sendOneJsonRs(HttpServletResponse response,boolean isOK,Object msg){
		sendJson(response,JsonUtil.makeOneRsJson(isOK,msg));
	}
	/**
	 * 返回增加修改删除登录等的处理结果 或者返回处理对象均可
	 * @param response
	 * @param isOK
	 * @param msg
	 * @param delClassNames
	 * @param delPropertiesNames
	 */
	public static void sendOneJsonRs(HttpServletResponse response,boolean isOK,Object msg,Object[] delClassNames,String[] delPropertiesNames){
		sendJson(response,JsonUtil.makeOneRsJson(isOK,msg,delClassNames,delPropertiesNames));
	}
	/**
	 * 返回grid结果的jsonString，这个不需要使用jsonconfig
	 * @param response
	 * @param isOK
	 * @param msg
	 */
	public static void sendGridJsonRs(HttpServletResponse response,int total,List list){
		Map map=new HashMap();
		if(total<list.size())
			map.put("total",list.size());
		else
			map.put("total",total);
		map.put("records", list);
		sendJson(response,JsonUtil.beanTOjson(map));
	}
	/**
	 * 返回grid结果的jsonString，这个需要使用jsonconfig
	 * @param response
	 * @param total
	 * @param list
	 * @param delClassNames
	 * @param delPropertiesNames
	 */
	public static void sendGridJsonRs(HttpServletResponse response,int total,List list,Object[] delClassNames,String[] delPropertiesNames){
		Map map=new HashMap();
		if(total<list.size())
			map.put("total",list.size());
		else
			map.put("total",total);
		map.put("records", list);
		sendJson(response,JsonUtil.beanTOjson(map,delClassNames,delPropertiesNames));
	}
	public static void sendGridJsonRsInDate(HttpServletResponse response,long total,List list){
		Map map=new HashMap();
		if(total<list.size())
			map.put("total",list.size());
		else
			map.put("total",total);
		    map.put("records", list);
		try {
			sendJson(response,JsonUtil.getJSONString(map));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void sendGridJsonRsInDate(HttpServletResponse response,List list){
		try {
			sendJson(response,JsonUtil.getJSONString(list));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * 根据grid返回数据，主要用于前台采用同样的form格式，便于解析
	 * @param response
	 * @param total
	 * @param object
	 */
	public static void sendGridJsonRs(HttpServletResponse response,int total,Object object){
		List list=new ArrayList();
		list.add(object);
		Map map=new HashMap();
		if(total<list.size())
			map.put("total",list.size());
		else
			map.put("total",total);
		map.put("records", list);
		sendJson(response,JsonUtil.beanTOjson(map));
	}
	/**
	 * 根据grid返回数据，主要用于前台采用同样的form格式，便于解析
	 * @param response
	 * @param total
	 * @param object
	 * @param delClassNames
	 * @param delPropertiesNames
	 */
	public static void sendGridJsonRs(HttpServletResponse response,int total,Object object,Object[] delClassNames,String[] delPropertiesNames){
		List list=new ArrayList();
		list.add(object);
		Map map=new HashMap();
		if(total<list.size())
			map.put("total",list.size());
		else
			map.put("total",total);
		map.put("records", list);
		sendJson(response,JsonUtil.beanTOjson(map,delClassNames,delPropertiesNames));
	}
/**
 * 传递json字符串到前台
 * @param response 响应
 * @param jsongStr  json内容
 */
	public static void sendJson(HttpServletResponse response,String jsonStr){
		log.debug("传递到前台的json为："+jsonStr);
		response.setHeader("Pragma", "no-cache"); 
    	response.setHeader("Cache-Control", "no-cache");
    	//response.setHeader("X-JSON", "no-cache");
    	response.setDateHeader("Expires", 0); 
    	response.setContentType("text/html"); //这里针对json的传输可能要进行修改
//    	response.setContentType("text/javascript"); 
    	response.setCharacterEncoding("UTF-8"); 
    	PrintWriter out;
		try {
			out = response.getWriter();
			out.print(jsonStr); 
			out.flush();
	    	out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * 响应的util
	 * @param args
	 */
	
	public static void main(String[] args) {
		// TODO 自动生成方法存根

	}

}
