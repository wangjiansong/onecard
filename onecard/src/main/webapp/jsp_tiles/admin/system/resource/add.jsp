<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/jsp_tiles/include/taglibs.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<c:set var="BASE_URL">
	<c:url value="/admin/sys/res" />
</c:set>
<script type="text/javascript">
function checkForm() {
	if($("#input_resourceId").val == "") {
		alert("资源ID是必须的！");
		return false;
	}
	if($("#input_resourceName").val == "") {
		alert("资源名称是必须的！");
		return false;
	}
	if($("#input_subsys").val == "") {
		alert("所属父菜单是必须的！");
		return false;
	}
	if($("#input_isMenu").val == "") {
		alert("是否前台是必须的！");
		return false;
	}
	if($("#input_resourceUrl").val == "") {
		alert("URL是必须的！");
		return false;
	}
	document.submitform.submit();
}
</script>
<section id="tabs">
	<div>
		<ul id="myTab" class="nav nav-tabs">
			<li class="active"><a href="#" data-toggle="tab">资源管理 >> 新增</a></li>
		</ul>
		<div id="myTabContent" class="tab-content">
		    <div class="tab-pane active">


<form action="${BASE_URL}/save" id="form1" method="POST" name="submitform" class="form-horizontal">
<fieldset>
	<div class="well form-actions_1">
		<div class="btn-group">
		<button type="submit" class="btn btn-success" onclick="javascript:checkForm();">保存</button>
		<button type="reset" class="btn btn-info">重置</button>
		<a class="btn" href="${BASE_URL}/list">取消</a>
		<span id="tip" class="alert" style="margin-left:70px;display: none;"></span>
		</div>
	</div>
	<div class="container-fluid">
		<div class="control-group">
			<label class="control-label" for="input_resourceId">
				资源代码
			</label>
			<div class="controls">
				<input type="text" name="resourceId" class="input-medium" id="input_resourceId"  />
				<font class="label-required" title=""></font>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label" for="input_resourceName">
				资源名称
			</label>
			<div class="controls">
				<input type="text" name="resourceName" class="input-medium" id="input_resourceName"  />
				<font class="label-required" title=""></font>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label" for="input_subsys">
				所属父菜单
			</label>
			<div class="controls">
				<select id="input_subsys" name="subsys" class="input-medium search-query-extend">
					<option value="reader">用户管理</option>
					<option value="cirfin">结算中心</option>
					<option value="rights">授权管理</option>
					<option value="library">图书馆组织管理</option>
				</select>
				<font class="label-required" title=""></font>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label" for="input_isMenu">
				是否前台显示
			</label>
			<div class="controls">
				<select id="input_isMenu" name="isMenu" class="input-medium search-query-extend">
					<option value="0">否</option>
					<option value="1">是</option>
				</select>
				<font class="label-required" title=""></font>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label" for="input_resourceUrl">
				URL
			</label>
			<div class="controls">
				<input type="text" name="resourceUrl" class="input-medium" id="input_resourceUrl"  />
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
</fieldset>
</form>
		    </div>
		</div>
	</div>
</section>