<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.ResourceBundle"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%
	pageContext.setAttribute("basePath", request.getContextPath());
    pageContext.setAttribute("rdId",request.getSession().getAttribute("rdId"));
    // pageContext.setAttribute("rdcertify",request.getSession().getAttribute("rdcertify"));
    pageContext.setAttribute("rdName",request.getSession().getAttribute("rdname"));
    pageContext.setAttribute("rdType",request.getSession().getAttribute("rdType"));
    pageContext.setAttribute("rdLibType",request.getSession().getAttribute("rdLibType"));
    pageContext.setAttribute("rdLib",request.getSession().getAttribute("rdLib"));
    pageContext.setAttribute("rdPasswd",request.getSession().getAttribute("rdpasswd"));
    pageContext.setAttribute("rdLoginId",request.getSession().getAttribute("rdLoginId"));
    
    pageContext.setAttribute("rdcertify",request.getSession().getAttribute("rdcertify"));
    pageContext.setAttribute("rdsex",request.getSession().getAttribute("rdsex"));
    pageContext.setAttribute("rdborndate",request.getSession().getAttribute("rdborndate"));
    pageContext.setAttribute("rdemail",request.getSession().getAttribute("rdemail"));
    pageContext.setAttribute("rdSort2",request.getSession().getAttribute("rdSort2"));
    pageContext.setAttribute("rdSort5",request.getSession().getAttribute("rdSort5"));
				
 // properties 配置文件名称
    ResourceBundle res = ResourceBundle.getBundle("sysConfig");
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
		<meta name="apple-mobile-web-app-capable" content="yes">
		<meta name="apple-mobile-web-app-status-bar-style" content="black">
		<meta name="format-detection" content="telephone=no">
		<meta name="format-detection" content="email=no">
		<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=0">
		<link href="<c:url value='/media/css/register/moble/css/addrdinfo.css' />" rel="stylesheet" type="text/css" />
<title>读者注册页面</title>
<style type="text/css">
.dbox{
	height:auto;
}
</style>
<script src="<c:url value='/media/css/register/moble/js/jquery-1.11.1.min.js' />" type="text/javascript"></script>
	<script src="<c:url value='/media/css/register/moble/js/common.js'/>" type="text/javascript"></script>
<script type="text/javascript">
/*判断是否是微信点击的页面
如果是则跳转furl=""
否则跳转*/
function goPAGE() {
     
 }
goPAGE();
	$(function(){
		 var success='${rdId}';
		var tips,btnStr,furl;
		 if(success){
			
			/* furl="http://218.66.36.38:81/interlibSSO/"; */
			/* if ((navigator.userAgent.match(/(phone|pad|pod|iPhone|iPod|ios|iPad|Android|Mobile|BlackBerry|IEMobile|MQQBrowser|JUC|Fennec|wOSBrowser|BrowserNG|WebOS|Symbian|Windows Phone)/i))) {
				window.location.href="你的手机版地址";
			}else {
				window.location.href="你的电脑版地址";	
			} */		
			/* furl=""; */
			//var ua = window.navigator.userAgent.toLowerCase();
		     //if (ua.match(/MicroMessenger/i) == 'micromessenger') {
			      tips="${rdName}您好，欢迎您成为<%=res.getString("LibName")%>读者！<br />您的读者账号是: <span style='color:#0ca3d2;font-size:24px'>${rdId}</span><br /><span>请妥善您的保管账号密码！</span>";
				  btnStr="前往登录";
		          furl="<%=res.getString("ssoUrl")%>";
		    // } else {
		     //	  tips="${rdName}您好，欢迎您成为<%=res.getString("LibName")%>读者！<br />您的读者账号是: <span style='color:#0ca3d2;font-size:24px'>${rdId}</span><br /><span>请妥善您的保管账号密码！</span>";
			//	  btnStr="前往登录";
		    //      furl="http://passport2-api.chaoxing.com/v5/login?fid=28&showUnitname=福建省图书馆&showTip=读者证号;密码";
		     //}
		}else{ 
			tips="您好，很遗憾注册过程出了点问题，您未能成为我馆读者。<br />您可以点击下面按钮重试。"
			btnStr="返回注册";
			furl="./register_moble.jsp";
		 } 	
		$('h1').html(tips);
		$('.goLoginBtn').text(btnStr);
		$('.goLoginBtn').click(function(){
			//if(furl)
				location.href=furl;
			//else
			//	history.go(-2);
		});
	})
</script>
</head>
<body>
	<div class="container">
		<div class="content">
		<div class="dbox">
			<h1></h1>
			<button class="goLoginBtn"></button>
		</div>
		</div>
	</div>
</body>
</html>