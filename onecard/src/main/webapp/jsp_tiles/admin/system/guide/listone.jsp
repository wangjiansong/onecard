<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/jsp_tiles/include/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<c:set var="BASE_URL">
	<c:url value="/admin/sys/blackboard" />
</c:set>

<script type="text/javascript">
function Add(){
	document.location.href="<c:url value='' />";
}
function Edit(id){
	document.location.href="${BASE_URL}/edit/"+id;
}

function Edit(id){
	document.location.href = "${BASE_URL}/edit/" +id;
	/*
	$.ajax({
		type: "GET",
		url: "${BASE_URL}/edit/"+id,
		dataType: "json",
		success: function(jsonData){
			//{"competId":2,"competName":"读者管理","describe":null,"resList":null,"page":null}
			$("#input_competId").val(jsonData.competId);
			$("#input_competName").val(jsonData.competName);
			$("#input_describe").val(jsonData.describe);
		},
		cache: false
	});
	*/
}
function submitForm() {
	document.submitform.submit();
}

function Delete(id){
	if (!confirm("你确定要删除此条记录？")) return;
	document.location.href = "${BASE_URL}/delete/" +id;
}

function pressEnter(e,method){
	if(e==null){//ie6..
		e=event;
	}	
	if ((e.which == 13 || e.keyCode == 13)&&(!(e.ctrlKey || e.metaKey))
			&& !e.shiftKey && !e.altKey) {
		 method();
	}
}
</script>
<section id="tabs">
	<div>
		<ul id="myTab" class="nav nav-tabs">
			<li class="active"><a href="#" data-toggle="tab">办事指南详情</a></li>
		</ul>
		<div id="myTabContent" class="tab-content">
		    <div class="tab-pane active">
		   
<fieldset>
	<form action="list" id="form1" method="POST" class="form-inline" >
			<div class="well form-actions_1">
					<input type="hidden" name="id" value="${guide.id }" />
					<input type="hidden" name="title" value="${guide.title }" />
					<div class="control-group" >
						<span style="font-weight:bold;">标题：</span>${guide.title }
					</div>
					<div class="control-group" >	
						<span style="font-weight:bold;">内容：</span>${guide.content }
					</div> 			
			</div>
	</form>
</fieldset>
 </div>
		</div>
	</div>
</section>
