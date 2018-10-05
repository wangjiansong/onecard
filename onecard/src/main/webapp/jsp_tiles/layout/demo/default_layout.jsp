<%@ page language="java" pageEncoding="UTF-8"%>
<%@include file="/jsp_tiles/include/taglibs.jsp"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="utf-8" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<tiles:insertAttribute name="html_header" ignore="true" />
<title><tiles:insertAttribute name="page_title" ignore="true" /></title>
<!--[if IE]>
<script src="http://html5shiv.googlecode.com/svn/trunk/html5.js"></script>
<![endif]-->
<!-- scripts -->
<script type="text/javascript" src="<c:url value='/media/js/jquery/jquery-1.8.3.min.js' />"></script>
	  
<!-- Reset Stylesheet -->
<link rel="stylesheet" href="<c:url value='/media/resources/css/reset.css'/>" type="text/css" media="screen" />
 
<!-- Main Stylesheet -->
<link rel="stylesheet" href="<c:url value='/media/resources/css/style.css'/>" type="text/css" media="screen" />

<!-- Invalid Stylesheet. This makes stuff look pretty. Remove it if you want the CSS completely valid -->
<link rel="stylesheet" href="<c:url value='/media/resources/css/invalid.css'/>" type="text/css" media="screen" />	

<link rel="stylesheet" href="<c:url value='/media/resources/css/blue.css'/>" type="text/css" media="screen" />

<!-- Internet Explorer Fixes Stylesheet -->

<!--[if lte IE 7]>
	<link rel="stylesheet" href="<c:url value='/media/resources/css/ie.css'/>" type="text/css" media="screen" />
<![endif]-->


<!-- jQuery Configuration -->
<script type="text/javascript" src="<c:url value='/media/resources/scripts/simpla.jquery.configuration.js'/>"></script>

<!-- Facebox jQuery Plugin -->
<script type="text/javascript" src="<c:url value='/media/resources/scripts/facebox.js'/>"></script>

<!-- jQuery WYSIWYG Plugin -->
<script type="text/javascript" src="<c:url value='/media/resources/scripts/jquery.wysiwyg.js'/>"></script>

<!-- jQuery Datepicker Plugin -->
<script type="text/javascript" src="<c:url value='/media/resources/scripts/jquery.datePicker.html'/>"></script>

<script type="text/javascript" src="<c:url value='/media/resources/scripts/jquery.date.js'/>"></script>

<!--[if IE]><script type="text/javascript" src="<c:url value='/media/resources/scripts/jquery.bgiframe.js'/>"></script><![endif]-->

<!-- Internet Explorer .png-fix -->

<!--[if IE 6]>
	<script type="text/javascript" src="<c:url value='/media/resources/scripts/DD_belatedPNG_0.0.7a.js'/>"></script>
	<script type="text/javascript">
		DD_belatedPNG.fix('.png_bg, img, li');
	</script>
<![endif]-->


</head>
<script type="text/javascript">
$(function() {
	createLoading();
});
function createLoading() {
	var message = "${message}";
	if(message != "") {
		$("#tip").css("display", "block").append(message);
		setTimeout(function(){
			$("#tip").fadeOut("slow");
		},5000);
		$("#tip").attr("class", "A_MESSAGEBOX_LOADING alert alert-success");
		return;
	}
}
</script>
<body>
	<div id="tip" class="A_MESSAGEBOX_LOADING alert alert-success" style="display: none;">
	</div>
	<!-- navbar -->
	<div id="body-wrapper">
		<tiles:insertAttribute name="page_menu" />
		<div id="main-content">
			<tiles:insertAttribute name="page_header" />
			<tiles:insertAttribute name="page_shortcut" />
			<div class="clear"></div>
			<tiles:insertAttribute name="page_content" />
			<tiles:insertAttribute name="page_footer"/>
		</div>
	</div>
	
</body>
</html>