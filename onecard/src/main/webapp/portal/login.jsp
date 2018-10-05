<%@ page language="java" pageEncoding="UTF-8"%>
<%@include file="/jsp_tiles/include/taglibs.jsp"%>
<%@page import="java.io.IOException"%>
<%

String method = request.getMethod();  
		if(!(method.equalsIgnoreCase("get")||method.equalsIgnoreCase("post")
			||method.equalsIgnoreCase("head")||method.equalsIgnoreCase("trace")
				||method.equalsIgnoreCase("connect")||method.equalsIgnoreCase("options"))){
            try {
            	response.setContentType("text/html;charset=GBK");  
                response.setCharacterEncoding("GBK");  
                response.setStatus(403);
				response.getWriter().print("<font size=6 color=red>对不起，您的请求非法，系统拒绝响应!</font>");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			  
        }

%>
<!DOCTYPE html>

<link rel="stylesheet" href="<c:url value='/media/css/bootstrap.min.css' />" type="text/css"/>
<link href="<c:url value='/media/css/font-awesome.css' />" media="all" rel="stylesheet" type="text/css" />
<link href="<c:url value='/media/css/se7en-font.css' />" media="all" rel="stylesheet" type="text/css" />
<link href="<c:url value='/media/css/style.css' />" media="all" rel="stylesheet" type="text/css" />
<script src="<c:url value='/media/js/jquery/jquery-1.8.3.min.js' />" type="text/javascript"></script>
<script src="<c:url value='/media/js/jquery/jquery-ui.js' />" type="text/javascript"></script>
<script src="<c:url value='/media/bootstrap/js/bootstrap.min.js' />" type="text/javascript"></script>
<script src="<c:url value='/media/js/modernizr.custom.js' />" type="text/javascript"></script>
<script src="<c:url value='/media/js/main.js' />" type="text/javascript"></script>
<body class="login2">
    <!-- Login Screen -->
    <div class="login-wrapper">
      <legend><h2><font style="color:#007aff">统一用户平台-登录</font></h2></legend>
      <form action="<c:url value='/portal/doLogin' />" method="POST" name="loginform">
	      <div class="alert-danger" id="tip">
				${message}
		  </div>
        <div class="form-group">
          <div class="input-group">
            <span class="input-group-addon"><i class="icon-user"></i></span>
            <input class="form-control" placeholder="用户名" id="rdid" type="text" name="rdId" autocomplete="off">
          </div>
        </div>
        <div class="form-group">
          <div class="input-group">
            <span class="input-group-addon"><i class="icon-lock"></i></span><input class="form-control" placeholder="密码" 
            id="rdpasswd" type="password" name="rdPasswd" autocomplete="off" onKeyDown="pressEnter(event)">
          </div>
        </div>        
        <button type="button"  class="btn btn-lg btn-primary btn-block" id="loginButton" onclick="checkform()">登录</button>
          <div>
                <a href="../jsp_tiles/admin/system/reader/retrievePassword.jsp" class="input-group">忘记密码？</a>
          </div>
		 <div class="social-login clearfix">
        </div>
      </form>
    </div>
    <!-- End Login Screen -->
  </body>
  
<script type="text/javascript">
String.prototype.trim = function() {
	return this.replace(/^\s+/g, "").replace(/\s+$/g, "");
};
$(function() {
	$("#rdid").focus();
	createTips();
});

function createTips() {
	var message = "${message}";
	if(message != "") {
		$("#tip").css("display", "block").html(message);
		return;
	}
}

function pressEnter(e){
	if(e==null){//ie6..
		e=event;
	}	
	if ((e.which == 13 || e.keyCode == 13)&&(!(e.ctrlKey || e.metaKey))
			&& !e.shiftKey && !e.altKey) {
		$("#loginButton").click();
	}
}

function checkform(){
	var tips = "";
	var rdid = document.getElementById("rdid").value.trim();
	var rdpasswd = document.getElementById("rdpasswd").value.trim();
	if(!rdid){
		tips = "请填写用户名！";
		$("#for_rdid").addClass("error");
		
		$("#tip").css("display", "block").html(tips);
		
		$("#rdid").focus();
		return false;
	} else {
		$("#for_rdid").removeClass("error");
	}
	if(!rdpasswd){
		tips = "请填写密码！";
		$("#for_rdpasswd").addClass("error");
		$("#tip").css("display", "block").html(tips);

		$("#rdpasswd").focus();
		return false;
	} else {
		$("#for_rdpasswd").removeClass("error");
	}
	$("#loginButton").attr("disabled","true");
	$("#loginButton").html("登录中...");
	
	document.loginform.submit(); 
}


</script>
