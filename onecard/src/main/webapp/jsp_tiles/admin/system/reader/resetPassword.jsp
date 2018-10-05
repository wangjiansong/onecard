<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/jsp_tiles/include/taglibs.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<script type="text/javascript">
	function resetPasswd(rdId) {
// 		var oldPassword = document.getElementById("oldPassword").value;
		var newPassword = document.getElementById("newPassword").value;
		var confirmPassword = document.getElementById("confirmPassword").value;
		var tip = document.getElementById("tip");
		var submitButton = document.getElementById("submitButton");
// 		if(oldPassword.length == 0){
// 			tip.innerHTML = "请输入原始密码！";
// 			tip.style.display = "inline";
// 			return false;
// 		}
// 		if(oldPassword.length > 20 || oldPassword.length < 6){
// 			tip.innerHTML = "密码在6至20之间！";
// 			tip.style.display = "inline";
// 			return false;
// 		}
		if(newPassword.length == 0){
			tip.innerHTML = "请输入新密码！";
			tip.style.display = "inline";
			return false;
		}
		if(newPassword.length > 20 || newPassword.length < 6){
			tip.innerHTML = "密码在6至20之间！";
			tip.style.display = "inline";
			return false;
		}
		if(confirmPassword.length == 0){
			tip.innerHTML = "请重复输入新密码！";
			tip.style.display = "inline";
			return false;
		}
		if(confirmPassword.length > 20 || confirmPassword.length < 6){
			tip.innerHTML = "密码在6至20之间！";
			tip.style.display = "inline";
			return false;
		}
		if(newPassword === confirmPassword){
			submitButton.disabled = "disabled";
// 			var data = {rdId : "${rdId}",oldPassword : oldPassword,newPassword : newPassword};
			var data = {rdId : "${rdId}",newPassword : newPassword};
			$.ajax({
				type : "POST",
				url : "<c:url value='/admin/reader/resetPassword' />",
				data : data,
				dataType : "text",
				success : function(backData){
					if(backData ==1){
						tip.className = "alert alert-success";
						tip.innerHTML = "修改成功！同步成功！";
						tip.style.display = "inline";
						setTimeout(function(){
							window.location.href = "<c:url value='/admin/reader/detailReader/${rdId}' />";
						},1000);
					}else if(backData ==0){
						tip.className = "alert alert-success";
						tip.innerHTML = "修改成功！同步失败 ！";
						tip.style.display = "inline";
						setTimeout(function(){
							window.location.href = "<c:url value='/admin/reader/detailReader/${rdId}' />";
						},1000);
					}else if(backData == -1){
						submitButton.disabled = "";
						tip.className = "alert alert-error";
						tip.innerHTML = "密码错误！";
						tip.style.display = "inline";
						return false;
					}
				},
				error : function(){
					alert("获取数据失败！");
					return;
				}
			});
		}else{
			tip.innerHTML = "两次密码输入不一致！";
			tip.style.display = "inline";
			return false;
		}
	}
	
	function getBack() {
		window.location.href = "<c:url value='/admin/reader/detailReader/${rdId}' />";
	}
</script>
<div class="page-header">
	<span style="font: bold 27px arial,sans-serif;">修改密码</span>
	<span id="tip" class="alert alert-error" style="margin-left:70px;display: none;"></span>
</div>
<div>
	<table>
<!-- 		<tr> -->
<!-- 			<td width="100" height="50">原密码：</td> -->
<!-- 			<td width="100" height="50"> -->
<!-- 				<input type="password" style="margin-top: 10px;" id="oldPassword" /> -->
<!-- 			</td> -->
<!-- 		</tr> -->
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
	<div style="width: 330px;" align="center">
		<input type="button" class="btn btn-success" value="确定" onclick="resetPasswd();" id="submitButton" />&nbsp;&nbsp;&nbsp;
		<input type="button" class="btn btn-danger" value="取消" onclick="getBack();" />
	</div>
</div>