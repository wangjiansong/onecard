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
<meta name="viewport" content="width=device-width, initial-scale=1" />  
<!--[if lt IE 9]>  
　　　　<script src="http://css3-mediaqueries-js.googlecode.com/svn/trunk/css3-mediaqueries.js"></script>  
<![endif]--> 
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link href="<c:url value='/media/css/register/css/addrdinfo.css' />" media="all" rel="stylesheet" type="text/css" />
<title>读者注册页面</title>
<style type="text/css">
.dbox{
	height:320px;
}
</style>
<script src="<c:url value='/media/css/register/js/jquery-1.11.1.min.js' />" type="text/javascript"></script>
<script type="text/javascript">
	$(function(){
		 var success='${rdId}';
		var tips,btnStr,furl;
		 if(success){
			tips="${rdName}您好，欢迎您成为<%=res.getString("LibName")%>读者！<br />您的读者账号是: ${rdId}";
			btnStr="前往登录";
			/* furl="http://218.66.36.38:83/interlibSSO/"; */
			//furl="./login.jsp";
			furl="<%=res.getString("ssoUrl")%>";
		}else{ 
			tips="您好，很遗憾注册过程出了点问题，您未能成为我馆读者。<br />您可以点击下面按钮重试。"
			btnStr="返回注册";
			furl="./register.jsp";
		 } 	
		$('h1').html(tips);
		$('.goLoginBtn').text(btnStr);
		$('.goLoginBtn').click(function(){
			location.href=furl;
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