<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/jsp_tiles/include/taglibs.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<c:set var="BASE_URL">
	<c:url value="/admin/sys/role" />
</c:set>
<script type="text/javascript">
function checkForm() {
	if($("#input_roleName").val == "") {
		alert("角色名称是必须的！");
		return false;
	}
	if($("#input_libcode").val == "") {
		alert("所属馆是必须的！");
		return false;
	}
	document.submitform.submit();
}
</script>
<section id="tabs">
	<div>
		<ul id="myTab" class="nav nav-tabs">
			<li class="active"><a href="#" data-toggle="tab">角色管理 >> 新增</a></li>
		</ul>
		<div id="myTabContent" class="tab-content">
		    <div class="tab-pane active">


<form action="save" id="form1" method="POST" name="submitform" class="form-horizontal">
<fieldset>
	<div class="well form-actions_1">
		<div class="btn-group">
			<a href="#" class="btn btn-success" onclick="checkForm()">保存</a>
			<button type="reset" class="btn btn-info">重置</button>
			<a class="btn" href="${BASE_URL}/list">取消</a>
		</div>
	</div>
		<div class="container-fluid">
		<div class="span12">
			<div class="control-group">
				<label class="control-label" for="input_roleName">
					角色名称
				</label>
				<div class="controls">
					<input type="text" name="roleName" class="input-medium" id="input_roleName"  />
					<font class="label-required" title=""></font>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label" for="input_libcode">
					所属馆
				</label>
				<div class="controls">
					<input type="text" name="libcode" class="input-medium" id="input_libcode"  />
					<font class="label-required" title=""></font>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label" for="input_describe">
					描述
				</label>
				<div class="controls">
					<input type="text" name="describe" class="input-medium" id="input_describe"  />
				</div>
			</div>
		</div>
		</div>
</fieldset>
</form>
		    </div>
		</div>
	</div>
</section>