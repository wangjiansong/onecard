/*
 * @(#)Common.java	1.0	2003-9-12
 *
 * Copyright (c) 2001-2010 Tuchuang, Inc. All rights reserved.
 */

package com.interlib.sso.common;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 * @author Home
 *
 */
public class Common {
	
	private static final Log log = LogFactory.getLog(Common.class);

	public static String getCirfinTranID() {
		return "'"
				+ new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date())
				+ org.apache.commons.lang.RandomStringUtils.randomNumeric(4)
				+ "'";
		// or return CIR_FIN_TRANID_SQ.nextval()
	}
	/**
	 * 自定义生成payId
	 */
	public static String getCirfinPayID() {
		HttpServletRequest request =null;
		
		String payid = request.getParameter("payId");
		
		return payid  ;
		// or return CIR_FIN_TRANID_SQ.nextval()
	}
	/**
	 * 根据传入的日期和天数,返回一个格式为yyyy-MM-dd的日期字符串,为日期+天数 如果日期小于当天则改为当天+天数,否则就是日期+天数
	 * 
	 * @param currentDate
	 * @param afterDays
	 * @return
	 */
	public static Date getDateAfterDaysOtherWay(Date currentDate, int afterDays) {
		if (currentDate.getTime() < new Date().getTime()) {
			currentDate = new Date();
		}
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(currentDate);
		calendar.add(Calendar.DATE, afterDays);
		Date enddate = new Date(calendar.getTimeInMillis());

		return enddate;
	}

	public static String getUserIp(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		if (ip.equals("0:0:0:0:0:0:0:1") || ip.indexOf(".") < 0) {// 获取的ip可能不正确
			ip = "127.0.0.1";
		}
		return ip;
	}

	public static boolean isOKOneIP(String reqIp, String iparea) {
		if (reqIp.equals("0:0:0:0:0:0:0:1") || reqIp.indexOf(".") < 0) {// 获取的ip可能不正确
			reqIp = "127.0.0.1";
		}
		String[] iparr = iparea.split(";");
		for (int i = 0; i < iparr.length; i++) {
			if (isInRange(reqIp, iparr[i])) {// 
				return true;
			}
		}
		return false;
	}

	public static boolean isInRange(String userIp, String ipRangeStr) {
		boolean isIn = true;
		int n = ipRangeStr.indexOf("-");

		if (ipRangeStr == null || ipRangeStr.equals("")) {
			isIn = false;
		} else if (n == -1) {
			int[] uerIpArr = disassemble(userIp, ".");
			int[] ipRangeStrArr = disassemble(ipRangeStr, ".");
			for (int i = 0; i < uerIpArr.length; i++) {
				if (uerIpArr[i] != ipRangeStrArr[i])
					isIn = false;
			}
		} else {
			String str1 = ipRangeStr.substring(0, n);
			String str2 = ipRangeStr.substring(n + 1);
			int[] startIP = new int[4];
			int[] endIP = new int[4];
			int[] testIP = new int[4];
			startIP = disassemble(str1, ".");
			endIP = disassemble(str2, ".");
			testIP = disassemble(userIp, ".");
			for (int i = 0; i < 4; i++) {
				if (startIP[i] > testIP[i] || endIP[i] < testIP[i]) {
					isIn = false;
					break;
				}
			}
		}
		return isIn;
	}

	public static int[] disassemble(String str, String strch) {
		int a[] = new int[4];
		int nNext;
		int nPre = nNext = 0;
		nNext = str.indexOf(strch, nPre);
		for (int i = 0; i < 3; i++) {
			a[i] = Integer.parseInt(str.substring(nPre, nNext));
			nPre = nNext + 1;
			nNext = str.indexOf(strch, nPre);
		}
		a[3] = Integer.parseInt(str.substring(nPre, str.length()));
		return a;
	}

	/**
	 * 把加密规则paras替换好相应的值再返回
	 * @param appCode
	 * @param staticCode
	 * @param queryUrl
	 * @param paras
	 * @return
	 */
	public static String translatePara(String appCode, String staticCode, String paras){
		log.debug("替换前的值："+paras);
		if(paras==null)return "";
		paras=paras.replaceAll("\\{appcode\\}", appCode);
		paras=paras.replaceAll("\\{static\\}", staticCode);
		paras=truanslateDatePara(paras);
		
		log.debug("替换后的值："+paras);
		return paras;
	}
	/**
	 * 根据{today:yyyy-M-d} 这种格式获取相应的日期
	 * @param paraStr
	 * @return
	 */
	public static String truanslateDatePara(String paraStr) {
		String newStr = "";
		try {
			if (paraStr != null && paraStr.indexOf("{today:") > -1) {// 看看是否有今天的时间标志
				newStr = paraStr.substring(0, paraStr.indexOf("{today:"));// 前面部分
				paraStr = paraStr.substring(paraStr.indexOf("{today:")
						+ "{today:".length());
				String dataFormat = "";
				if (paraStr.indexOf("}") > -1) {
					dataFormat = paraStr.substring(0, paraStr.indexOf("}"));
					if (dataFormat != null && dataFormat.length() > -1) {
						newStr = newStr + TimeUtils.dateToString(new Date(), dataFormat);
					}
				}
				newStr = newStr + paraStr.substring(paraStr.indexOf("}") + 1);
				return newStr;
			}
		} catch (java.lang.IllegalArgumentException e) {
			log.error("The method truanslateDatePara for class FormLogin throws IllegalArgumentException:"
					+ e);
		}
		return paraStr;// 有异常等返回原值。
	}
	
	public static void main(String[] args) {
		String a = translatePara("interlib", "dglib", "{appcode}{today:yyyyMMdd}{static}");
		a = MD5Util.MD5Encode(a, "UTF-8");
		System.out.println(a);
		
	}
	
}