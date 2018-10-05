<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%-- <% 					
 String oldPassword = (String) request.getSession().getAttribute("oldPassword");
 
%> --%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
	<body>

		<div >
			<div><h3>取回密码</h3>
					密码修改成功！！！！请牢记您的新密码！<br />
					您的新密码为:${newPassword} 
					<br />
					<input type="submit" value="返回登录" onclick="window.location.href='login';"/><br/>
			</div>
		</div>
	</body>
</html>
