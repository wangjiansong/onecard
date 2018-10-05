<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="/jsp_tiles/include/taglibs.jsp"%>
<%@ page import="com.interlib.sso.common.PropertyUtils" %>
<c:set var="BASE_URL">
	<c:url value="/portal" />
</c:set>
<script type="text/javascript">
<%
Properties sysConfig=PropertyUtils.getProperties("sysConfig");
	String casUrl = sysConfig.getProperty("casUrl");
	String ssoUrl = sysConfig.getProperty("ssoUrl");
%>
function logout() {
	$.ajax({
		type: "GET",
		url: "${BASE_URL}/logout/",
		dataType: "html",
		success: function(htmlData){
			if(htmlData == "ok") {
				//document.location.href="<%=casUrl%>logout?service=<%=ssoUrl%>portal/login";
				document.location.href = "${BASE_URL}/login";
			}
		},
		cache: false
	});
}

function loginChange() {
     var str = $("#ddlregtype").val();   //获得选中的值
     if(confirm("是否切换读者登录？？")) {
     $.ajax({
         type:"post",
         async: false,
         dataType:"json",
         url:"${BASE_URL}/indexChange",
         data:{'select':str},
         success:function(msg){
                 alert(msg);
         }
     }); 
       window.location.reload(true); 
	}
} 

</script>
<div class="navbar navbar-inverse navbar-fixed-top">
	<div class="navbar-inner">
		<div class="container-fluid">
			<a class="brand" href="#">统一用户管理系统</a>
			
			<div class="nav-collapse">
				<ul class="nav">
					<li class="active">
						<a href="<c:url value='/admin' />">首页</a>
					</li>
					<li><a href="<c:url value='/admin/reader/index' />">读者查询</a></li>
					<li><a href="<c:url value='/admin/sys/cirfinlog/chargeList' />">充值查询</a></li>
					<li><a href="<c:url value='/admin/sys/auth/list' />">访问控制</a></li>
					<li><a href="<c:url value='/admin/sys/readerRole/list' />">用户管理</a></li>
					<li><a href="<c:url value='/system/reader/readertype/readerTypeIndex' />">系统管理</a></li>
				</ul>
			</div>
			
			<div class="nav-collapse pull-right">
				<ul class="nav">
					<li class="dropdown active">
						<a class="dropdown-toggle" data-toggle="dropdown" href="<c:url value='/portal/index' />">
							<i class="icon-user icon-white"></i>&nbsp;&nbsp;&nbsp;${READER_SESSION.reader.rdName}
							<b class="caret"></b>
						</a>
						<ul class="dropdown-menu">
						     <li><a href="<c:url value='/portal/index' />"><i class="icon-home"></i>&nbsp&nbsp返回门户</a></li>
						     <li class="divider"></li>
						    <li><a href="#" onclick="logout()"><i class="icon-off"></i>&nbsp&nbsp退出</a></li>
						</ul>
					</li>
				</ul>
			</div>
			<!--/.nav-collapse -->
		</div>
	</div>
</div><!-- end of navbar -->
	