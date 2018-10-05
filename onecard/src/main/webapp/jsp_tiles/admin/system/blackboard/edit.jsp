<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/jsp_tiles/include/taglibs.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<c:set var="BASE_URL">
	<c:url value="/admin/sys/blackboard" />
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
			<li class="active"><a href="#" data-toggle="tab">公告管理 >> 修改公告</a></li>
		</ul>
		<div id="myTabContent" class="tab-content">
		    <div class="tab-pane active">
				<form action="../update" id="form1" method="POST" name="submitform" class="form-inline">
					<input type="hidden" name="id" value="${blackboard.id }" />
					<input type="hidden" name="title" value="${blackboard.title }" />
					<div class="control-group">
						<span>标题：</span>
						<input type="text" name="title" id="title" disabled="disabled" readonly="readonly" class="input-medium search-query-extend" value="${blackboard.title }" />
					</div>
					<div class="control-group">
						<span>内容：</span>
						<textarea name="content" id="content">${blackboard.content }</textarea>
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
