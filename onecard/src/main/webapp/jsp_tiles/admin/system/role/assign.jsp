<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/jsp_tiles/include/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<c:set var="BASE_URL">
	<c:url value="/admin/sys/role" />
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
	$("#competIds").val(list.join("~m~"));
	$('#assignForm').submit();
	
}
</script>
<section id="tabs">
	<div>
		<ul id="myTab" class="nav nav-tabs">
			<li class="active"><a href="#" data-toggle="tab">角色管理 >> 分配权限</a></li>
		</ul>
		<div id="myTabContent" class="tab-content">
		    <div class="tab-pane active">


<fieldset>
	<div class="well form-actions_1">
		<c:if test="${!empty message}">
			<div class="alert alert-success">
				<a class="close" data-dismiss="alert">&times;</a>
				${message}
			</div>
		</c:if>
		<div class="btn-group">
			<a class="btn btn-success"  onclick="javascript:save();">保存分配</a>
			<a class="btn" href="${BASE_URL}/list">取消</a>
		</div>
		<form:form id="assignForm" method="POST" action="saveAssign">
		<input type="hidden" name="competIds" id="competIds" />
		<input type="hidden" name="roleId" id="roleId" value="${roleId}" />
		</form:form>
	</div>
	<div class="container-fluid">
		<table class="table table-bordered">
			<thead>
				<tr>
					<th width="13%">全选/反选<input type="checkbox" class="selAll"/></th>
					<th>权限名称</th>
					<th>权限描述</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${roleComList}" var="p">
		  			<tr class="f">
		  				<td><input type="checkbox" id="${p.competId}" checked=true></td>
		  				<td><c:out value="${p.competName}" /></td>
		  				<td><c:out value="${p.describe}" /></td>
		  			</tr>
		    	</c:forEach>
				<c:forEach items="${otherComList}" var="c">
		  			<tr class="f">
		  				<td><input type="checkbox" id="${c.competId}"></td>
		  				<td><c:out value="${c.competName}" /></td>
		  				<td><c:out value="${c.describe}" /></td>
		  			</tr>
		    	</c:forEach>
			</tbody>
		</table>
	
	</div>
</fieldset>

		    </div>
		</div>
	</div>
</section>