<%@ page language="java" contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="/jsp_tiles/include/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<c:set var="base_url">
	<c:url value="/admin/card" />
</c:set>
<link type="text/css" rel="stylesheet" href="<c:url value='/media/colorbox/colorbox_4.css' />" />
<link type="text/css" rel="stylesheet" href="<c:url value='/media/bootstrap/css/bootstrap.min.css' />" />
<script type="text/javascript" src="<c:url value='/media/js/jquery/jquery-1.8.3.min.js' />"></script>
<script type="text/javascript" src="<c:url value='/media/bootstrap/js/bootstrap.min.js' />"></script>
<script type="text/javascript" src="<c:url value='/media/colorbox/jquery.colorbox-min.js' />"></script>
<script type="text/javascript">
	$(document).ready(function(){
		if ("${obj.groupName}") {
			$("input[name=groupName]").val("${obj.groupName}");
		}
	});
	
	function addAssignGroup(commitButton) {
		var info = document.getElementById("info");
		var item = $("table input[type=checkbox][id!='allbox']:checked");
		var size =  item.length;
		if (size > 0) {
			commitButton.disabled = true;
			var ids = "";
			for (var i = 0; i < size; i++) {
				ids += item[i].value;
				if (i < size - 1) {
					ids += ",";
				}
			}
			$.ajax({
				type : "POST",
				url : "${base_url}/consumption/assign/group/${ruleId}",
				data : {ids: ids},
				dataType : "text",
				success : function(backData){
					if (backData == 1) {
						info.innerHTML = "添加成功！";
						info.className = "alert alert-success";
						setTimeout(function(){
							parent.document.location.href = "${base_url}/consumption/assign/${ruleId}";
						},1000);
						return false;
					} else {
						info.innerHTML = "添加失败！";
						info.className = "alert alert-error";
						commitButton.disabled = false;
						return false;
					}
				},
				error : function(){
					info.innerHTML = "获取数据失败！";
					info.className = "alert alert-error";
					commitButton.disabled = false;
					return false;
				}
			});
		} else {
			info.innerHTML = "请选择要添加的记录！";
			info.className = "alert alert-warning";
			return false;
		}
	}
	
	function selectThis(thisTr, isChecked) {
		if (isChecked) {
			thisTr.className = "warning";
		} else {
			thisTr.className = "";
		}
	}
	
	function selectAll(isChecked) {
		if (isChecked) {
			$("table tr").attr("class","warning");
			$("table tr td input[type=checkbox]").attr("checked",true);
		} else {
			$("table tr").attr("class","");
			$("table tr td input[type=checkbox]").attr("checked",false);
		}
	}
	
	function getBack() {
		parent.$.fn.colorbox.close();
	}
</script>
<div class="navbar">
	<div class="navbar-inner">
		<a class="brand" href="javascript:void(0);">添加指派分组</a>
	</div>
</div>
<div>
	<form action="${ruleId}" method="post">
		<span style="font: 13px arial;">分组名称：</span>
		<input type="text" id="groupName" name="groupName" class="input-medium search-query-extend" />
		&nbsp;&nbsp;&nbsp;
		<button type="submit" class="btn btn-info">查询</button>
		<button type="button" class="btn btn-primary" onclick="javascript:addAssignGroup(this);">添加</button>
		<button type="button" class="btn btn-inverse" onclick="javascript:getBack();">返回</button>
		&nbsp;&nbsp;&nbsp;
		<span id="info"></span>
	</form>
</div>
<div class="bs-docs-example">
	<table class="table table-bordered table-condensed" style="table-layout: fixed;">
		<thead>
			<tr>
				<th width="1%">
   					<input type="checkbox" id="allbox" onclick="javascript:selectAll(this.checked);" />
    			</th>
				<th width="3%">分组名称</th>
				<th width="3%">创建时间</th>
				<th width="3%">备注</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${groups}" var="v" varStatus="vs">
				<tr>
					<td style="text-align: center;">
						<input type="checkbox" value="${v.GROUPID}" name="groupIdBox" onclick="javascript: selectThis(this.parentNode.parentNode,this.checked);" />
					</td>
					<td>${v.GROUPNAME}</td>
					<td>${v.CREATETIME}</td>
					<td nowrap title="${v.REMARK}" style="overflow: hidden;text-overflow: ellipsis;">${v.REMARK}</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
</div>
<%@include file="/jsp_tiles/include/admin/pager.jsp" %>