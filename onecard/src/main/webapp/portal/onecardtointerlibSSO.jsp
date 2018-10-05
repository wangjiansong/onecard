<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%
	pageContext.setAttribute("basePath", request.getContextPath());
    pageContext.setAttribute("username",request.getSession().getAttribute("username"));
    //pageContext.setAttribute("mobilePhone",request.getSession().getAttribute("mobilePhone").equals("null")?"":request.getSession().getAttribute("mobilePhone"));
%>
<!DOCTYPE html>
<html>
<head>
	<meta name="renderer" content="webkit|ie-comp|ie-stand">
<script type="text/javascript">
	//window.location.href="http://www.fjlib.net/"; 
			//alert(document.referrer);
			//javascript:history.go(-2);
			//window.history.go(-2);
			// window.history.back(-2);
			//return false;
	function myfun() {
		window.history.go(-2);
	}
// 用js实现在加载完成一个页面后自动执行一个方法
/*用window.onload调用myfun()*/
window.onload=myfun;//不要括号				
			
</script>
</head>
	<body onload="myfun()">  
    
    </body> 
</html>