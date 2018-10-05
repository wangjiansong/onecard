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
				document.location.href="https://demo.sso.com:8443/cas/logout";
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
			<a class="brand" href="#">统一办证系统</a>
			
			<div class="nav-collapse">
				<ul class="nav">
					<li class="active">
						<a href="<c:url value='/admin' />">首页</a>
					</li>
					<li><a href="#about">关于</a></li>
					<li><a href="#contact">联系</a></li>
				</ul>
			</div>
			
			<div class="btn-group pull-right">
				<a class="btn btn-primary" href="<c:url value='/portal/index' />"><i class="icon-user icon-white"></i>&nbsp;&nbsp;&nbsp;${READER_SESSION.reader.rdName}</a>
			  	<a class="btn btn-primary dropdown-toggle" data-toggle="dropdown" href="#"><span class="caret"></span></a>
			  	<ul class="dropdown-menu">
				    <li class="divider"></li>
				    <li><a href="#" onclick="logout()"><i class="icon-off"></i>&nbsp&nbsp退出</a></li>
			  	</ul>
			</div>
			<!--/.nav-collapse -->
		</div>
	</div>
</div><!-- end of navbar -->
	