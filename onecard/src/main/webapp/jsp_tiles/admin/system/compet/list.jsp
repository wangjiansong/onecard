<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/jsp_tiles/include/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<c:set var="BASE_URL">
	<c:url value="/admin/sys/compet" />
</c:set>

<script type="text/javascript">
$(function(){
	var chkSingle = $(".selAll");
	var chkGroup = $(".f input");
	//获取所有被勾选的input
	var funTrGet = function() {
		return $(".f input:checked");
	};
	//正选反选
	chkSingle.bind("click", function() {
	    if ($(this).attr("checked")) {
	        chkGroup.attr("checked", true);	
	    } else {
	        chkGroup.attr("checked", false);
	    }
	});
});
function Add(){
	document.location.href="<c:url value='' />";
}
function Edit(id){
	document.location.href="${BASE_URL}/edit/"+id;
}
function validata(id) {
	$.ajax({
		type: "GET",
		url: "validate/"+id,
		dataType: "html",
		success: function(htmlData){
			if (!confirm(htmlData + " 你确定要删除此条记录？")) return;
			Delete(id);
		},
		cache: false
	});
}

function Edit(id){
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
}
function submitForm() {
	document.submitform.submit();
}

function Delete(id){
	if (!confirm("你确定要删除此条记录？")) return;
	document.location.href = "${BASE_URL}/delete/" +id;
}

function showRes(id) {
	document.location.href= "${BASE_URL}/assign?competId=" + id;
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
			<li class="active"><a href="#" data-toggle="tab">权限管理</a></li>
		</ul>
		<div id="myTabContent" class="tab-content">
		    <div class="tab-pane active">
		   
<fieldset>
	<form action="list" id="form1" method="POST" class="form-inline">
	<div class="well form-actions_1">
			<span>权限名称：</span>
			<div class="input-append">
			<input type="text" name="competName" value="${competName}" class="input-medium search-query-extend" />
			<button class="btn btn-info" type="submit"><i class="icon-search icon-white"></i>查询</button>
			<a class="btn btn-success" href="${BASE_URL}/add"><i class="icon-plus icon-white"></i>新增权限</a>
			</div>
	</div>
	</form>
</fieldset>
<table class="table table-bordered table-hover table-condensed table-striped">
	<thead>
		<tr>
			<th width="13%"><input type="checkbox" class="selAll"/></th>
			<th>权限名称</th>
			<th>权限描述</th>
			<th width="15%">操作</th>
		</tr>
	</thead>
	<tbody>
		<c:forEach items="${pageList}" var="p">
 			<tr class="f">
 				<td><input type="checkbox" id="${p.COMPETID}"/></td>
 				<td><c:out value="${p.COMPETNAME}" /></td>
 				<td><c:out value="${p.DESCRIBE}" /></td>
 				<td>	
 				<div class="btn-group">
 				<a data-toggle="modal" href="#myModal" id="editButton" class="btn btn-success btn-small" onclick="Edit('${p.COMPETID}')">查看</a>
				<input id="resButton" type="button" class="btn btn-info btn-small" value="分配资源" onclick="showRes('${p.COMPETID}')"/>
				<input id="deleteButton" type="button" class="btn btn-small" value="删除" onclick="Delete('${p.COMPETID}')"/>
				</div>
			</td>
 			</tr>
    	</c:forEach>
	</tbody>
</table>
<div class="modal hide fade in" id="myModal" style="display: none;">
  	<div class="modal-header">
    	<a class="close" data-dismiss="modal">×</a>
    	<h3>编辑</h3>
  	</div>
  	<div class="modal-body">
	  	<form action="${BASE_URL}/update" name="submitform" method="POST" >
	  		<input type="hidden" name="competId" id="input_competId" value="" />
			<div class="control-group">
				<label class="control-label" for="input_competName">
					权限名称
				</label>
				<div class="controls">
					<input type="text" name="competName" class="input-medium" id="input_competName" value="" />
				</div>
			</div>
			<div class="control-group">
				<label class="control-label" for="input_describe">
					描述
				</label>
				<div class="controls">
					<textarea rows="5" id="input_describe" name="describe" style="width: 300px;" value=""></textarea>
				</div>
			</div>
	  	</form>
  	</div>
  	<div class="modal-footer">
    	<a href="#" class="btn" data-dismiss="modal">取消</a>
    	<a href="#" onclick="submitForm()" class="btn btn-success"><i class="icon-ok icon-white"></i>保存修改</a>
  	</div>
</div>

<c:set var="pager_base_url">${BASE_URL}/list?</c:set>
<%@include file="/jsp_tiles/include/admin/pager.jsp" %>
 </div>
		</div>
	</div>
</section>
