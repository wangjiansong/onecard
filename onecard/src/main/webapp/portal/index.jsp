<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@include file="/jsp_tiles/include/taglibs.jsp"%>
<%@ page import="com.interlib.sso.domain.ReaderSession" %>
<%@ page import="com.interlib.sso.domain.Reader" %>
<%@ page import="com.interlib.sso.common.PropertyUtils" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<c:set var="BASE_URL">
	<c:url value="/portal" />
</c:set>

<c:set var="BASE2_URL">
	<c:url value="/admin/sys/blackboard" />
</c:set>
<c:set var="BASE3_URL">
	<c:url value="/admin/sys/guide" />
</c:set>

<style>
*{
	}
	body{
		margin:0;
		padding:0;
		font-size:12px;
		background-color:#000000;
		font-family:"Microsoft YaHei", "微软雅黑", "Microsoft JhengHei", "华文细黑";
		background:url(../media/images/bg.jpg) repeat-x bottom #e2f0fc;
	}
	ul{
		list-style-type:none;
		margin:0;
		padding:0;
	}
	a{
		text-decoration:none;
		color:#333333;
	}
	.resource{
		border:1px #84bdea solid!important;
		background:-webkit-linear-gradient(#ffffff,#cce3f5)!important;
		-moz-box-shadow:0px 0px 5px #7fbff2!important;              
    	-webkit-box-shadow:0px 0px 5px #7fbff2!important;
		box-shadow:0px 0px 5px #7fbff2!important;
	}
	.items{
		width:1060px;
		border:1px #b3d4e9 solid;
		padding:10px 10px;
		overflow:hidden;
		margin:20px auto;
		background-color:#fefdfd;
		-moz-border-radius:6px; 
		-webkit-border-radius:6px;
		border-radius:6px;
		-moz-box-shadow:0px 0px 5px #aed3f0;              
    	-webkit-box-shadow:0px 0px 5px #aed3f0;
		box-shadow:0px 0px 5px #aed3f0;
		min-height:100px;
		padding-bottom:20px;
		padding-top:20px;
		background:url(../media/images/bodybg.jpg) repeat-x;
		border-bottom:none; 
	}
	.items .showlist .title{
		line-height:41px;
		height:41px;
		margin-bottom:5px;
		text-indent:10px;
		font-size:18px;
	}
	.items .showlist .list ul li{
		float:left;
		line-height:31px;
		height:31px;
		margin:10px 14px;
		-moz-border-radius: 4px; 
		-webkit-border-radius: 4px;
		border-radius:4px;
		padding:0 20px;
		border:1px #dbd3d3 solid;
		background:url(../media/images/btnbg.jpg) repeat-x;
		-moz-box-shadow:0px 0px 5px #eee inset;              
    	-webkit-box-shadow:0px 0px 5px #eee inset;
		box-shadow:0px 0px 5px #eee inset; 
		font-family:"Microsoft YaHei", "微软雅黑", "Microsoft JhengHei", "华文细黑";
		-moz-box-shadow:0px 0px 5px #ddd;              
    	-webkit-box-shadow:0px 0px 5px #ddd;
		box-shadow:0px 0px 5px #ddd;
		cursor:pointer;
		font-size:14px;
		color:#333333;
	}
	.items .showlist .list ul li:hover{
		-moz-box-shadow:0px 0px 5px #c6c4c4;              
    	-webkit-box-shadow:0px 0px 5px #c6c4c4;
		box-shadow:0px 0px 5px #c6c4c4;
		background:-webkit-linear-gradient(#ffffff,#fcfafa);  
		color:red;
	}
	.header{
		width:100%;
		height:61px;
		line-height:60px;
		background:url(../media/images/headerbg.jpg) repeat-x;
	}
	.header .content{
		width:980px;
		margin:auto auto;
	}
	.header .content .name{
		font-size:24px;
		color:#FFFFFF;
		position:relative;
		top:-4px;
	}
	.forum{
		width:280px;
		height:275px;
		float:left;
		margin:0 20px 0 10px;
		border:1px #b1d2ec solid;
		border-bottom:none;
		-moz-border-radius: 4px 4px 0 0; 
		-webkit-border-radius: 4px 4px 0 0;
		border-radius: 4px 4px 0 0;
		background:url(../media/images/forumbg.jpg) repeat-x bottom;
		-moz-box-shadow:0px 0px 5px #b7d7ef;              
    	-webkit-box-shadow:0px 0px 5px #b7d7ef;
		box-shadow:0px 0px 5px #b7d7ef;
	}
	.forum .title{
		height:35px;
		line-height:35px;
		font-size:16px;
		text-indent:20px;
		font-family:"Microsoft YaHei", "微软雅黑", "Microsoft JhengHei", "华文细黑";
		margin-bottom:5px;
		color:#666699;
		padding:10px 0;
	}
	.forum .infolist .list ul li {
		line-height:31px;
		height:31px;
		text-indent:10px;
		font-family:"Microsoft YaHei", "微软雅黑", "Microsoft JhengHei", "华文细黑";
	}
	.forum .infolist .list ul li em{
		font-style:normal;
		color:#999999;
		margin-right:5px;
	}
	.forum .infolist .list ul li a{
	}
	.body{
	}
	.manage{
		width:400px;
	}
	.manageinfo{
		height:210px;
		-moz-border-radius: 4px; 
		-webkit-border-radius: 4px;
		border-radius:4px;
		padding:0px 20px;
		font-size:14px;
	}
	.manageinfo .info{
		line-height:31px;
		height:31px;
		margin-bottom:0px;
		
	}
	.manageinfo .controller .list{
		width:100%;
		display:block;
		overflow:hidden;
	}
	.line{
		padding-bottom:8px;
		margin:8px 0;
	}
	.manageinfo .controller .list ul li{
		float:left;
		width:130px;
		line-height:31px;
	}
</style>
<%
Properties sysConfig=PropertyUtils.getProperties("sysConfig");
	String casUrl = sysConfig.getProperty("casUrl");
	String onecardUrl = sysConfig.getProperty("onecardUrl");
%>
<script type="text/javascript" src="<c:url value='/media/bootstrap/js/bootstrap.min.js' />"></script>
<script type="text/javascript" src="<c:url value='/media/js/jquery/jquery-1.8.3.min.js' />"></script>
<script type="text/javascript">
$(function(){
	myResources();
	getPubResource();
	
	
});

function Look(id){
	document.location.href="${BASE2_URL}/list/one/"+id;
}
function See(id){
	document.location.href="${BASE3_URL}/list/one/"+id;
}

function myResources() {
	$.ajax({
		type: "GET",
		url: "getMyResources",
		dataType: "json",
		success: function(jsonData){
			$.each(jsonData, function(idx, item) {
				if (idx < 0) {
					return true;
				}
				$("#resourceDiv").append("<li><a href=\""+ item.resourceUrl +"\" target=\"_blank\">"+ item.resourceName +"</a></li>");
				
			});
		},
		cache : false
	});
}
function getPubResource() {
	var rdid = $("#rdid").val();
	var rdname = encodeURIComponent($("#rdname").val());
	var opacUrl = $("#webserviceurl").val();
	var today = new Date().Format("yyyy-MM-dd");
	var enc = hex_md5(rdid + today + "*Day#$!");
	$.ajax({
		type: "GET",
		url: "getPubResource",
		dataType: "json",
		success: function(jsonData){
			$.each(jsonData, function(idx, item) {
				if (idx < 0) {
					return true;
				}
				var paramInfo = "loginid=" + rdid + "&rdname=" + rdname + "&opacurl=" + opacUrl + "&enc=" + enc + "&furl=../main/main.jsp";
				$("#pubResourceDiv").append("<li><a href=\""+ item.serviceURL + "?" + paramInfo + "\" target=\"_blank\">"+ item.appName +"</a></li>");
				
			});
		},
		cache : false
	});
}
function logout() {
	if(confirm("确定注销？")) {
		$.ajax({
			type: "GET",
			url: "${BASE_URL}/logout",
			dataType: "html",
			success: function(htmlData){
				if(htmlData == "ok") {
					document.location.href = "${BASE_URL}/login";
					//cas下使用
					//document.location.href="<%=casUrl%>logout?service=<%=onecardUrl%>portal/login";
				}
			},
			cache: false
		});
	}
}

function autoLoginOpac(){
	var rdid = $("#rdid").val();
	var rdpasswd = $("#rdpasswd").val();
	var webserviceurl = $("#webserviceurl").val();
	//跳转到个人用户的资料
	if(webserviceurl == ""){
		alert("请先配置用户所在馆webserviceurl地址！");
		return;
	}
	if(rdpasswd == ""){
		alert("请先完善用户密码！");
		return;
	}
	document.location.href=webserviceurl+"/reader/doLogin?rdid="+rdid+"&rdPasswd="+rdpasswd;
}

/* function loginChange() {
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
}  */

</script>
<body>
<div class="header">
	<input type="hidden" id="rdid" value="${READER_SESSION.reader.rdId }"/>
	<input type="hidden" id="rdname" value="${READER_SESSION.reader.rdName}"/>
	<input type="hidden" id="rdpasswd" value="${RdpasswdMD5Session}"/>
	<input type="hidden" id="webserviceurl" value="${WebServiceURLSession}"/>
	<div class="content">
		<div class="name">图书馆公共服务平台</div>
		
	</div>
</div>
<div class="body">
   	<div class="items">
       	<div class="forum manage" >
           	<div class="title" style="float:left;">控制面板</div>
           	<%-- <div style="float:right;margin-left:50px;margin-right:50px;margin-top: 10px;" >
           	切换读者：<select id="ddlregtype" class="selector" onchange="loginChange()" style="width: 100px;" name="ddlregtype">
							<c:forEach var="list" items="${list}">
								<option value="${list.rdId}" selected>
								${list.rdId}
								</option>
							</c:forEach>
						</select>
			   </div> --%>
               <div class="manageinfo">
               	<div class="info" style="clear:both"><strong>您好，
               	
               		<c:if test="${READER_SESSION.reader.libUser eq 1}">
                    	馆员
                    </c:if>
                    <c:if test="${READER_SESSION.reader.libUser eq 0}">
						读者
                    </c:if>
                   ${READER_SESSION.reader.rdName}</strong>[${READER_SESSION.reader.rdLib}(${READER_SESSION.reader.rdLibCode})]
					</div>
                   <div class="controller">
                   	
                   	<div class="list" >
                       	<ul>
                           	<li><a data-toggle="modal" href="#myModal" onclick="init()">密码修改</a></li>
                           	<li><a href="#" onclick="logout()">注销登录</a></li>
                           	<li><a href="javascript:void(0)" onclick="autoLoginOpac();" >个人空间</a></li>
                           	<c:if test="${READER_SESSION.reader.libUser eq 1}">
                           	<li><a href="<c:url value="/admin/" />">统一办证管理中心</a></li>
                   			</c:if>
                        </ul>
                    </div>
                    
                   </div>
               </div>
           </div>
       	<div class="forum">
           	<div class="title">公告</div>
               <div class="infolist">
               
               	<div class="list">
                   	<ul>
		             <c:forEach items="${BlackboardAndGuideSession.blackboardList }" var="blackboard">
		             	<li>
		             	<em>[<fmt:formatDate value="${blackboard.createTime}" pattern="yyyy/MM/dd"/>]</em>
		             	<a href="#myModal" onclick="Look('${blackboard.id}')">${blackboard.title}</a>
		             	</li>
		             </c:forEach>
                     <li><a href="/onecard/admin/sys/blackboard/list">>>更多</a></li>
                    </ul>
                   </div>
               </div>
           </div>
           <div class="forum">
           	<div class="title">办事指南</div>
               <div class="infolist">
               	<div class="list">
                   	<ul>
                   		<c:forEach items="${BlackboardAndGuideSession.guideList }" var="guide">
		             		<li><em>[<fmt:formatDate value="${guide.createtime}" pattern="yyyy/MM/dd"/>]</em><a href="#myModal" onclick="See('${guide.id}')">${guide.title}</a></li>
		            	</c:forEach>
<!-- 		            	<li><em>[2013/12/09]</em><a href="#">公告测试1</a></li> -->
<!-- 		            	<li><em>[2013/12/09]</em><a href="#">公告测试1</a></li> -->
<!-- 		            	<li><em>[2013/12/09]</em><a href="#">公告测试1</a></li> -->
                       	<li><a href="/onecard/admin/sys/guide/list">>>更多</a></li>
                       </ul>
                   </div>
               </div>
           </div> 
       </div>
		<div class="items resource">
       	<div class="showlist">
           	<div class="title">公共资源</div>
               <div class="list">
                   <ul id="pubResourceDiv">
                   	
                   </ul>
               </div>
           </div>
        </div>
       <c:if test="${READER_SESSION.reader.libUser eq 1}">
       <div class="items resource">
       	<div class="showlist">
           	<div class="title">已开通服务</div>
               <div class="list">
                   <ul id="resourceDiv">
                   </ul>
               </div>
           </div>
       </div>
       </c:if>
   </div>
   <div class="modal hide fade in" id="myModal" style="display: none;">
  	<div class="modal-header">
    	<a class="close" data-dismiss="modal">×</a>
    	<h4>密码修改</h4>
  	</div>
  	<div class="modal-body">
	  	<div>
			<table>
				<tr>
					<td width="100" height="50">原密码：</td>
					<td width="100" height="50">
						<input type="password" style="margin-top: 10px;" id="oldPassword" />
					</td>
				</tr>
				<tr>
					<td width="100" height="50">新密码：</td>
					<td width="100" height="50">
						<input type="password" style="margin-top: 10px;" id="newPassword" />
					</td>
				</tr>
				<tr>
					<td width="100" height="50">确认新密码：</td>
					<td width="100" height="50">
						<input type="password" style="margin-top: 10px;" id="confirmPassword" />
					</td>
				</tr>
			</table>
		</div>
  	</div>
  	<div class="modal-footer">
    	<a href="#" class="btn" data-dismiss="modal">取消</a>
    	<a href="#" onclick="resetPasswd('${READER_SESSION.reader.rdId}');" class="btn btn-success"><i class="icon-ok icon-white"></i>确定</a>
  	</div>
</div>
<script type="text/javascript">

	function init() {
		$("#oldPassword").val("");
		$("#newPassword").val("");
		$("#confirmPassword").val("");
	}
	function resetPasswd(rdId) {
		var oldPassword = document.getElementById("oldPassword").value;
		var newPassword = document.getElementById("newPassword").value;
		var confirmPassword = document.getElementById("confirmPassword").value;
		var submitButton = $("#submitButton");
		if(oldPassword.length == 0){
			alert("请输入原始密码！");
			return false;
		}
		if(oldPassword.length > 20 || oldPassword.length < 6){
			alert("密码在6至20之间！");
			return false;
		}
		if(newPassword.length == 0){
			alert("请输入新密码！");
			return false;
		}
		if(newPassword.length > 20 || newPassword.length < 6){
			alert("密码在6至20之间！");
			return false;
		}
		if(confirmPassword.length == 0){
			alert("请重复输入新密码！");
			return false;
		}
		if(confirmPassword.length > 20 || confirmPassword.length < 6){
			alert("密码在6至20之间！");
			return false;
		}
		if(newPassword === confirmPassword){
			submitButton.attr("disabled", true);
			var data = {rdId : rdId,oldPassword : oldPassword,newPassword : newPassword};
			$.ajax({
				type : "POST",
				url : "${BASE_URL}/resetPassword",
				data : data,
				dataType : "text",
				success : function(backData){
					submitButton.attr("disabled", false);
					if(backData ==1){
						alert("修改成功！同步成功！");
						$('#myModal').modal('hide');
					}else if(backData ==0){
						alert("修改成功！同步失败 ！");
						$('#myModal').modal('hide');
					}else if(backData == -1){
						submitButton.disabled = "";
						alert("密码错误！");
						return false;
					}
				},
				error : function(){
					alert("获取数据失败！");
					return;
				}
			});
		}else{
			alert("两次密码输入不一致！");
			return false;
		}
	}
	
</script>
</body>