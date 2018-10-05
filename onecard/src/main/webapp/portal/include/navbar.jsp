<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="/jsp_tiles/include/taglibs.jsp"%>

<c:set var="BASE_URL">
	<c:url value="/portal" />
</c:set>
<script type="text/javascript">
function logout() {
	$.ajax({
		type: "GET",
		url: "${BASE_URL}/logout/",
		dataType: "html",
		success: function(htmlData){
			if(htmlData == "ok") {
				document.location.href="http://192.168.0.89:8080/cas/logout?service=http://192.168.0.89:8089/sso/porta/login";
			//	document.location.href="${BASE_URL}/login";
			}
		},
		cache: false
	});
}
</script>
<div class="navbar navbar-inverse navbar-fixed-top">
	<div class="navbar-inner">
		<div class="container-fluid">
			<a class="btn btn-navbar" data-toggle="collapse"
				data-target=".nav-collapse"> <span class="icon-bar"></span> <span
				class="icon-bar"></span> <span class="icon-bar"></span>
			</a>
			<a class="brand" href="#">公共服务平台</a>
			
			<div class="btn-group pull-right">
				<a class="btn btn-primary" href="<c:url value="/admin/" />"><i class="icon-user icon-white"></i>后台管理</a>
				<a class="btn btn-primary dropdown-toggle" data-toggle="dropdown" href="#"><span class="caret"></span></a>
				<ul class="dropdown-menu">
				     <li><a href="#" onclick="logout()"><i class="icon-off"></i>&nbsp&nbsp退出</a></li>
			  	</ul>
			</div>
			<!--/.nav-collapse -->
		</div>
	</div>
</div><!-- end of navbar -->
	