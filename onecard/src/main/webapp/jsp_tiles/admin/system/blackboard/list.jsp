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
			<li class="active"><a href="#" data-toggle="tab">公告管理</a></li>
		</ul>
		<div id="myTabContent" class="tab-content">
		    <div class="tab-pane active">
		   
<fieldset>
	<form action="list" id="form1" method="POST" class="form-inline">
	<div class="well form-actions_1">
			<span>公告标题：</span>
			<div class="input-append">
			<input type="text" name="title" value="" class="input-medium search-query-extend" />
			<button class="btn btn-info" type="submit"><i class="icon-search icon-white"></i>查询</button>
			<a class="btn btn-success" href="${BASE_URL}/add"><i class="icon-plus icon-white"></i>新增公告</a>
			</div>
	</div>
	</form>
</fieldset>
<table class="table table-bordered table-hover table-condensed table-striped">
	<thead>
		<tr>
			<th width="15%">行号</th>
			<th width="15%">公告标题</th>
			<th>内容</th>
			<th width="15%">操作</th>
		</tr>
	</thead>
	<tbody>
		<c:forEach items="${pageList}" var="p">
 			<tr class="f">
 				<td><c:out value="${p.id}" /></td>
 				<td><c:out value="${p.title}" /></td>
 				<td>${p.content}</td>
 				<td>	
 				<div class="btn-group">
 				<a data-toggle="modal" href="#myModal" id="editButton" class="btn btn-primary btn-small" onclick="Edit('${p.id}')">查看</a>
				<input id="deleteButton" type="button" class="btn btn-danger btn-small" value="删除" onclick="Delete('${p.id}')"/>
				</div>
			</td>
 			</tr>
    	</c:forEach>
	</tbody>
</table>


<c:set var="pager_base_url">${BASE_URL}/list?</c:set>
<%@include file="/jsp_tiles/include/admin/pager.jsp" %>
 </div>
		</div>
	</div>
</section>
