<%@page language="java" contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="edit_url">
	<c:url value="/admin/subsidy/group/edit" />
</c:set>
<link type="text/css" rel="stylesheet" href="<c:url value='/media/bootstrap/css/bootstrap.min.css' />" />
<script type="text/javascript" src="<c:url value='/media/js/jquery/jquery-1.8.3.min.js' />"></script>
<script type="text/javascript">
	String.prototype.trim = function() {
		return this.replace(/^\s\s*/, '').replace(/\s\s*$/, '');
	};
	
	function editGroup(submitButton) {
		var groupName = $("#groupName").val().trim();
		if (!groupName) {
			$("#info").
				attr("style","display:inline;color:red;font:13px Arial;").
				html("分组名称不能为空！");
			return false;
		}
		submitButton.disabled = true;
		var remark = $("#remark").val();
		$.ajax({
			type : "POST",
			url : "${edit_url}",
			data : {groupId: "${group.groupId}",groupName: groupName,remark: remark},
			dataType : "text",
			success : function(backData){
				if (backData == 1) {
					$("#info").
					attr("style","display:inline;color:green;font:13px Arial;").
					html("修改成功！");
					setTimeout(function(){
						parent.$.fn.colorbox.close();
						parent.location.reload(true);
					},1000);
					return false;
				} else {
					$("#info").
					attr("style","display:inline;color:red;font:13px Arial;").
					html("修改失败！");
					submitButton.disabled = false;
					return false;
				}
			},
			error : function(){
				$("#info").
				attr("style","display:inline;color:red;font:13px Arial;").
				html("获取数据失败！");
				submitButton.disabled = false;
				return false;
			}
		});
	}
</script>
<div class="navbar">
	<div class="navbar-inner">
		<a class="brand" href="javascript:void(0);">修改分组</a>
	</div>
</div>
<form class="form-horizontal" action="">
	<div class="control-group">
		<label class="control-label" for="groupName">分组名称：</label>
		<div class="controls">
			<input type="text" id="groupName" maxlength="32" value="${group.groupName}" />
		</div>
	</div>
	<div class="control-group">
		<label class="control-label" for="remark">备注：</label>
		<div class="controls">
			<textarea rows="3" id="remark" maxlength="128">${group.remark}</textarea>
		</div>
	</div>
	<div class="control-group">
		<div class="controls">
			<button type="button" class="btn btn-primary" onclick="javascript:editGroup(this);">保存</button>
			<button type="button" class="btn btn-inverse" onclick="javascript:parent.$.fn.colorbox.close();return false;">返回</button>
			<font id="info"></font>
		</div>
	</div>
</form>