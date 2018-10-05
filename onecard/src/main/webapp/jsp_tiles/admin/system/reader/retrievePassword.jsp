<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="java.io.IOException"%>
<%
/* String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
 */

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
//response.getWriter().print(method);
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
	<body>
<script type="text/javascript">
	//取回密码
	function doRetrievePassword(action){
		$rdid = $("input[name='rdId']").val()
		if ($rdid == "") {
			alert("请输入读者证号");
			return false;
		}
		return true;
	}
	
/* 	function sendEmail(){
		document.getElementById("addFrom").submit();
				alert("邮件发送成功");
		
	} */
</script>
		<div id="contentDiv">
			<div id="right_div"><h3>取回密码</h3>
				<form name="retrieveForm" action="/onecard/portal/doRetrievePassword" method="post" 
					onsubmit = "return doRetrievePassword()" >
					读者证号<input type="text" name="rdId"/>
					<input type="submit" value="取回密码"/><br/>
					<input type="hidden" value="/retrievePassword" name="furl" />
					<span style="color:red">
					****注意：此密码取回功能是通过您在注册时的email进行发送的，
					
						<br/>
						****如果需要进行修改，请前往个人资料处修改</span>
					
				</form>
			</div>
		</div>
	</body>
</html>
