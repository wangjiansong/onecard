<%@ page language="java" pageEncoding="UTF-8"%>
<%@include file="/jsp_tiles/include/taglibs.jsp"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="utf-8" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<link rel="stylesheet" href="<c:url value='/media/bootstrap/css/bootstrap.min.css' />" type="text/css" />
<link rel="stylesheet" href="<c:url value='/media/bootstrap/css/bootstrap-responsive.min.css' />" type="text/css" />
<link rel="stylesheet" href="<c:url value='/media/css/default_layout.css' />" type="text/css" />
<tiles:insertAttribute name="html_header" ignore="true" />
<title><tiles:insertAttribute name="page_title" ignore="true" /></title>
<!--[if IE]>
<script src="http://html5shiv.googlecode.com/svn/trunk/html5.js"></script>
<![endif]-->
<!-- scripts -->
<script type="text/javascript" src="<c:url value='/media/js/jquery/jquery-1.8.3.min.js' />"></script>
<script type="text/javascript" src="<c:url value='/media/bootstrap/js/bootstrap.min.js' />"></script>
<script type="text/javascript" src="<c:url value='/media/bootstrap/plugin/bootstrap-button.js' />"></script>
<script type="text/javascript" src="<c:url value='/media/bootstrap/plugin/bootstrap-collapse.js' />"></script>
<link rel="stylesheet" href="<c:url value='/media/bootstrap/css/bootstrap-datetimepicker.min.css' />" type="text/css"/>
<script type="text/javascript" src="<c:url value='/media/bootstrap/plugin/bootstrap-datetimepicker.js' />"></script>
<script type="text/javascript" src="<c:url value='/media/bootstrap/i18n/bootstrap-datetimepicker.zh-CN.js' />"></script>
</head>
<script type="text/javascript">
	$(function() {
		createLoading();
	});
	
	function createLoading() {
		var message = "${message}";
		if (message != "") {
			$("#tip").css("display", "block").append(message);
			setTimeout(function() {
				$("#tip").fadeOut("slow");
			}, 5000);
			$("#tip").attr("class", "A_MESSAGEBOX_LOADING alert alert-success");
			return;
		}
	}
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
	}
</script>
<body>
	<div id="tip" class="A_MESSAGEBOX_LOADING alert alert-success" style="display: none;">
	</div>
	<!-- navbar -->
	<tiles:insertAttribute name="navbar" ignore="true" />
	<!-- container-fluid -->
	<div class="container-fluid">
		<!-- row-fluid -->
		<div class="row-fluid">
			<!-- span2, left menu -->
			<div class="span2">
				<tiles:insertAttribute name="page_menu" ignore="true" />
			</div>
			<!-- end of span2, left menu -->
			<!-- span10, right content -->
			<div class="span10">
				<tiles:insertAttribute name="page_content" />
			</div>
			<!-- end of span10, right content -->
		</div>
		<!-- end of row-fluid -->
		<hr />
		<tiles:insertAttribute name="page_footer" ignore="true" />
	</div>
	<!-- end of container-fluid -->
	<%-- <tiles:insertAttribute name="analytics" ignore="true" /> --%>
</body>
</html>