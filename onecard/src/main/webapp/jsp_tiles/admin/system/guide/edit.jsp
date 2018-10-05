<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/jsp_tiles/include/taglibs.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<c:set var="BASE_URL">
	<c:url value="/admin/sys/guide" />
</c:set>

<script type="text/javascript" src="<c:url value='/media/tinymce/tinymce.min.js' />"></script>

<script type="text/javascript">
tinymce.init({
    selector: "textarea",
    toolbar: "insertfile undo redo | styleselect | bold italic | alignleft aligncenter alignright alignjustify | bullist numlist outdent indent | link image"
});

function checkForm() {
	if($("#title").val == "") {
		alert("标题是必须的！");
		return false;
	}
	if($("#content").val == "") {
		alert("内容是必须的！");
		return false;
	}
	document.submitform.submit();
}
</script>
<section id="tabs">
	<div>
		<ul id="myTab" class="nav nav-tabs">
			<li class="active"><a href="#" data-toggle="tab">办事指南管理 >> 修改办事指南</a></li>
		</ul>
		<div id="myTabContent" class="tab-content">
		    <div class="tab-pane active">
				<form action="../update" id="form1" method="POST" name="submitform" class="form-inline">
					<input type="hidden" name="id" value="${guide.id }" />
					<input type="hidden" name="title" value="${guide.title }" />
					<div class="control-group">
						<span>标题：</span>
						<input type="text" name="title" id="title" disabled="disabled" readonly="readonly" class="input-medium search-query-extend" value="${guide.title }" />
					</div>
					<div class="control-group">
						<span>内容：</span>
						<textarea name="content" id="content">${guide.content }</textarea>
					</div>
				
					<fieldset>
					<div class="well form-actions_1">
						<button class="btn btn-success" onclick="javascript:checkForm();">修改</button>
						<a class="btn" href="${BASE_URL}/list">取消</a>
					</div>
					</fieldset>
				</form>

 			</div>
		</div>
	</div>
</section>

<!-- 
<form action="${BASE_URL}/update" id="form1" method="POST" >
	<input type="hidden" name="competId" value="${compets.competId}" />
<fieldset>
	<div class="form-actions">
	
		<a class="btn" href="${BASE_URL}/list"><<</a>
		<button type="submit" class="btn btn-primary">保存</button>
		<button type="reset" class="btn">重置</button>
		<a class="btn btn-danger" href="${BASE_URL}/list">取消</a>
		<div class="form-actions"></div>
		<div class="container-fluid">
			<div class="control-group">
				<label class="control-label" for="input_competName">
					权限名称
				</label>
				<div class="controls">
					<input type="text" name="competName" class="input-medium" id="input_competName" value="${compets.competName}" />
				</div>
			</div>
			<div class="control-group">
				<label class="control-label" for="input_describe">
					描述
				</label>
				<div class="controls">
					<input type="text" name="describe" class="input-medium" id="input_describe" value="${compets.describe}" />
				</div>
			</div>
		</div>
	</div>
</fieldset>
</form>
 -->