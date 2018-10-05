<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/jsp_tiles/include/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<c:set var="BASE_URL">
	<c:url value="/admin/sys/readerRole" />
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
function save() {
	var list = new Array();
	$(".f input:checked").each(function(i){
		list[i]=$(this).attr('id')
	});
	$("#roleIds").val(list.join("~m~"));
	$('#assignForm').submit();
	
}
</script>
<section id="tabs">
	<div>
		<ul id="myTab" class="nav nav-tabs">
			<li class="active"><a href="#" data-toggle="tab">用户角色管理 >> 分配角色</a></li>
		</ul>
		<div id="myTabContent" class="tab-content">
		    <div class="tab-pane active">
		    </div>
		</div>
	</div>
</section>

<fieldset>
	<div class="well form-actions_1">
		<div class="btn-group">
		<input type="button" class="btn btn-success" value="保存分配" onclick="javascript:save();"/>
		<a type="button" class="btn" href="${BASE_URL}/list">取消</a>
		</div>
	</div>
	<form:form id="assignForm" method="POST" action="saveAssign">
		<input type="hidden" name="roleIds" id="roleIds" />
		<input type="hidden" name="rdId" id="rdId" value="${rdId}" />
	</form:form>
	用户：<font color="red">${rdId}</font> 分配角色
	</p>
	<div class="container-fluid">
		<table class="table table-bordered table-hover table-condensed table-striped">
			<thead>
				<tr>
					<th width="13%">全选/反选<input type="checkbox" class="selAll"/></th>
					<th>角色名称</th>
					<th>角色描述</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${readerRoleList}" var="p">
		  			<tr class="f">
		  				<td><input type="checkbox" id="${p.roleId}" checked=true></td>
		  				<td><c:out value="${p.roleName}" /></td>
		  				<td><c:out value="${p.describe}" /></td>
		  			</tr>
		    	</c:forEach>
				<c:forEach items="${otherRoleList}" var="c">
		  			<tr class="f">
		  				<td><input type="checkbox" id="${c.roleId}"></td>
		  				<td><c:out value="${c.roleName}" /></td>
		  				<td><c:out value="${c.describe}" /></td>
		  			</tr>
		    	</c:forEach>
			</tbody>
		</table>
		
	</div>
</fieldset>
