<%@ page language="java" pageEncoding="UTF-8"%>
<%@include file="/jsp_tiles/include/taglibs.jsp"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="utf-8" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<link rel="stylesheet" href="<c:url value='/media/bootstrap/css/bootstrap.min.css' />" type="text/css"/>
<link rel="stylesheet" href="<c:url value='/media/bootstrap/css/bootstrap-responsive.min.css' />" type="text/css"/>

<tiles:insertAttribute name="html_header" ignore="true" />
<title><tiles:insertAttribute name="page_title" ignore="true" /></title>
<!--[if IE]>
<script src="http://html5shiv.googlecode.com/svn/trunk/html5.js"></script>
<![endif]-->
<script type="text/javascript" src="<c:url value='/media/js/jquery/jquery-1.8.3.min.js' />"></script>
<script type="text/javascript" src="<c:url value='/media/js/md5.js' />"></script>
<script type="text/javascript">
Date.prototype.Format = function (fmt) { //author: meizz 
    var o = {
        "M+": this.getMonth() + 1, //月份 
        "d+": this.getDate(), //日 
        "h+": this.getHours(), //小时 
        "m+": this.getMinutes(), //分 
        "s+": this.getSeconds(), //秒 
        "q+": Math.floor((this.getMonth() + 3) / 3), //季度 
        "S": this.getMilliseconds() //毫秒 
    };
    if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    for (var k in o)
    if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
    return fmt;
};
$(function() {
	$("#rdid").focus();
	createTips();
});
</script>
</head>
	<!-- container-fluid -->
	<tiles:insertAttribute name="page_content" />
	<tiles:insertAttribute name="page_footer" ignore="true" />
</html>