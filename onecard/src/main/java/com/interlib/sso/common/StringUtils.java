package com.interlib.sso.common;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class StringUtils {
	
	
	public static void removeEndChar(StringBuffer sb, String s) {
		if(sb.toString().endsWith(",")) {
			int lastChar = sb.lastIndexOf(s);
			if(lastChar >= 0) {
				sb.deleteCharAt(lastChar);
			}
		}
	}
	/**
	* 判断User-Agent 是不是来自于手机
	* @param ua
	*/
	public static void checkAgentIsMobile(HttpServletRequest request, HttpServletResponse response) {
		String userAgent = request.getHeader("user-agent");
		if (userAgent.indexOf("Android") != -1) {
			// 安卓
			String defaultFailureUrl = "/login_moblie.jsp";
			System.out.println("Android访问！！！" + "没有登录,返回的页面===" +defaultFailureUrl);
			
		} else if (userAgent.indexOf("iPhone") != -1 || userAgent.indexOf("iPad") != -1) {
			// 苹果
			String defaultFailureUrl = "/login_moblie.jsp";
			System.out.println("iPhone/iPad访问！！！"+ "没有登录,返回的页面==="+ defaultFailureUrl);
		} else {   // 电脑
			String defaultFailureUrl = "/login.jsp";
			System.out.println("电脑访问！！！"+ "没有登录,返回的页面===" + defaultFailureUrl);
			//逻辑处理
		}
	}
}
