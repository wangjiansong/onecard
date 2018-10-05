<%@ page language="java" contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<link type="text/css" rel="stylesheet" href="<c:url value='/media/colorbox/colorbox_4.css' />" />
<script type="text/javascript" src="<c:url value='/media/colorbox/jquery.colorbox-min.js' />"></script>
<c:set var="base_url">
	<c:url value="/admin/subsidy" />
</c:set>
<script type="text/javascript">
	$(document).ready(function(){
		if ("${obj.groupName}") {
			$("input[name=groupName]").val("${obj.groupName}");
		}
	});
	
	function addGroup() {
		$.colorbox({
			width: "40%",
			height: "48%",
			iframe: true,
			close: "",
			href: "group/add",
			overlayClose: true
		});
	}
	
	function editGroup() {
		var info = document.getElementById("info");
		var item = $("table input[type=checkbox]:checked");
		if (item.length == 1) {
			$.colorbox({
				width: "40%",
				height: "48%",
				iframe: true,
				close: "",
				href: "group/edit/"+item[0].value,
				overlayClose: true
			});
		} else {
			info.innerHTML = "请选中一条记录！";
			info.className = "alert alert-warning";
			return false;
		}
	}
	
	function editCurrentLine(groupId) {
		$.colorbox({
			width: "40%",
			height: "48%",
			iframe: true,
			close: "",
			href: "group/edit/"+groupId,
			overlayClose: true
		});
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
	
	function deleteGroup() {
		var info = document.getElementById("info");
		var item = $("table input[type=checkbox][id!='allbox']:checked");
		var size =  item.length;
		if (size > 0) {
			if (confirm("确定删除？")) {
				var ids = "";
				for (var i = 0; i < size; i++) {
					ids += item[i].value;
					if (i < size - 1) {
						ids += ",";
					}
				}
				$.ajax({
					type : "POST",
					url : "${base_url}/group/delete",
					data : {ids:ids},
					dataType : "text",
					success : function(backData){
						if (backData == 1) {
							info.innerHTML = "删除成功！";
							info.className = "alert alert-success";
							setTimeout(function(){
								window.location.href = "${base_url}/group";
							},1000);
							return false;
						} else {
							info.innerHTML = "删除失败！";
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
			}
		} else {
			info.innerHTML = "请选择要删除的记录！";
			info.className = "alert alert-warning";
			return false;
		}
	}
	
	function member(groupId) {
		document.location.href = "group/member/"+groupId;
	}
</script>
<div class="bs-docs-example">
	<ul class="nav nav-tabs">
		<li><a href="${base_url}/grant">补助发放规则</a></li>
		<li class="active"><a href="${base_url}/group">分组管理</a></li>
	</ul>
</div>
<div id="info"></div>
<div>
	<form action="group" method="post">
		<span>分组名称：</span><input type="text" name="groupName" class="input-medium search-query-extend" />
		<button type="submit" class="btn btn-info">查询</button>
		<button type="button" class="btn btn-primary" onclick="javascript:addGroup();">新增</button>
		<button type="button" class="btn btn-success" onclick="javascript:editGroup();">修改</button>
		<button type="button" class="btn btn-danger" onclick="javascript:deleteGroup();">删除</button>
	</form>
</div>
<div class="bs-docs-example">
	<table class="table table-bordered table-condensed" style="table-layout: fixed;">
		<thead>
			<tr>
				<th width="1%">
   					<input type="checkbox" id="allbox" onclick="javascript:selectAll(this.checked);" />
    			</th>
				<th width="6%">分组名称</th>
				<th width="6%">创建时间</th>
				<th width="10%">备注</th>
				<th width="3%">操作</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${groups}" var="v" varStatus="vs">
				<tr id="${v.GROUPID}" ondblclick="editCurrentLine(this.id);">
					<td style="text-align: center;">
						<input type="checkbox" value="${v.GROUPID}" name="groupIdBox" onclick="javascript: selectThis(this.parentNode.parentNode,this.checked);" />
					</td>
					<td>${v.GROUPNAME}</td>
					<td>${v.CREATETIME}</td>
					<td nowrap title="${v.REMARK}" style="overflow: hidden;text-overflow: ellipsis;">${v.REMARK}</td>
					<td style="text-align: center;">
						<button type="button" class="btn btn-mini btn-warning" onclick="javascript:member(${v.GROUPID});">添加 / 删除成员</button>
					</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
</div>
<%@include file="/jsp_tiles/include/admin/pager.jsp"%>