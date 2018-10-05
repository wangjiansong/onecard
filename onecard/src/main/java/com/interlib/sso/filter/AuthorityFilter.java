package com.interlib.sso.filter;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.interlib.sso.common.Cautions;
import com.interlib.sso.common.Common;
import com.interlib.sso.common.MD5Util;
import com.interlib.sso.common.ServletUtils;
import com.interlib.sso.common.TimeUtils;
import com.interlib.sso.domain.Authorization;
import com.interlib.sso.service.AuthorizationService;
import com.interlib.sso.service.RdAccountService;
import com.interlib.sso.service.ReaderService;


public class AuthorityFilter implements Filter{

	private static final Log log = LogFactory.getLog(AuthorityFilter.class);
	
	@Autowired
	public ReaderService readerService;
	
	@Autowired
	public RdAccountService rdAccountService;
	
	@Autowired
	public AuthorizationService authorizationService;
	public void setReaderService(ReaderService readerService) {
		this.readerService = readerService;
	}

	public void setRdAccountService(RdAccountService rdAccountService) {
		this.rdAccountService = rdAccountService;
	}

	public void setAuthorizationService(AuthorizationService authorizationService) {
		this.authorizationService = authorizationService;
	}
	
	public void destroy() {
		
	}

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain filterChain) throws IOException, ServletException {
		
		HttpServletRequest req = (HttpServletRequest)request;
		HttpServletResponse res = (HttpServletResponse)response;
		
		String reqIp = Common.getUserIp(req);
		String appCode = req.getParameter("appcode");  //调用方代号
		String enc = req.getParameter("enc");  //传过来的加密串
		String userid = req.getParameter("userid");
		
		if(appCode == null) {
			res.sendError(HttpServletResponse.SC_FORBIDDEN, Cautions.INVALID_VISIT);
			return;
		}
		
		Authorization autho = authorizationService.getByAppCode(appCode);
		if(autho == null) {
			res.sendError(HttpServletResponse.SC_FORBIDDEN, Cautions.INVALID_LIBCODE);
			return;
		}
		
		String ipArea = autho.getIpAddress();
		String staticCode = autho.getStaticCode();
		Date endDate = new Date(); //截止时间
		Date today = new Date();
		try {
			 endDate = TimeUtils.stringToDate(autho.getEndDate(), "yyyy-MM-dd");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		boolean isIpRight = Common.isOKOneIP(reqIp, ipArea);
		//看看ip是否在允许范围
		StringBuffer xml = new StringBuffer();
		if(!isIpRight) {
			xml.append("<Result>");
			xml.append("<message>");
			xml.append("请求ip：" + reqIp + "," + Cautions.ERROR_IP);
			xml.append("</message>");
			xml.append("<success>0</success>");
			xml.append("</Result>");
			ServletUtils.printXML(res, xml.toString());
			return ;
		} 
		if(endDate.getTime() < today.getTime()) {
			xml.append("<Result>");
			xml.append("<message>");
			xml.append(Cautions.AUTH_OUTDATE);
			xml.append("</message>");
			xml.append("<success>0</success>");
			xml.append("</Result>");
			ServletUtils.printXML(res, xml.toString());
			return;
		}
		//再根据加密规则
		String paras = autho.getEncodeRule();
		if(paras.equals("")) {
			filterChain.doFilter(req, res);
		}
		//把规则替换上相应的参数做md5加密
		paras = Common.translatePara(appCode, staticCode, paras);
		paras = MD5Util.MD5Encode(paras, "UTF-8");
		log.info(paras);
		if(!paras.equalsIgnoreCase(enc)) { //密钥加密串验证不通过
			xml.append("<Result>");
			xml.append("<message>");
			xml.append(Cautions.ERROR_ENC);
			xml.append("</message>");
			xml.append("<success>0</success>");
			xml.append("</Result>");
			ServletUtils.printXML(res, xml.toString());
			return;
		}
		filterChain.doFilter(req, res);
		
	}
	
	public void init(FilterConfig config) throws ServletException {
	}

}
