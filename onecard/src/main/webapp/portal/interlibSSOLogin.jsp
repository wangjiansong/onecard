<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@include file="/jsp_tiles/include/taglibs.jsp"%>
<%@ page import="java.util.Date,java.text.SimpleDateFormat"%>
<%@ page import="com.interlib.sso.domain.ReaderSession" %>
<%@ page import="com.interlib.sso.domain.Reader" %>
<%@ page import="com.interlib.sso.common.MD5Util" %>
<%@ page import="java.net.URLEncoder"%>
<%@ page import="javax.servlet.ServletException"%>
<%@ page import="java.io.IOException"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">


<%
	ReaderSession readerSession = (ReaderSession)session.getAttribute("READER_SESSION");
	Reader reader = readerSession.getReader();
	
	String rdid = reader.getRdId();
	String rdname = reader.getRdName();
	
	SimpleDateFormat dsr = new SimpleDateFormat("yyyy-MM-dd");
  	String dsrDate=dsr.format(new Date());
  	String dsrEnc=MD5Util.MD5Encode(rdid+dsrDate+"*Day#$!");
  	
	rdname = URLEncoder.encode(rdname);
	String interfaceUrl = "http://192.168.0.89:8089/interlibSSO/interface/loginInterface.jsp?loginid="+rdid+"&rdname="+rdname+"&enc="+dsrEnc;
	response.sendRedirect(interfaceUrl);
%>
