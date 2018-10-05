<%@ page language="java" contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<script type="text/javascript">
	$(document).ready(function(){
		if ("${obj.grantTitle}") {
			$("input[name=grantTitle]").val("${obj.grantTitle}");
		}
	});
	
	function addGrant() {
		window.location.href='grant/add';
	}
	
	function editGrant() {
		var info = document.getElementById("info");
		var item = $("table input[type=checkbox]:checked");
		if (item.length == 1) {
			document.location.href='grant/edit/'+item[0].value;
		} else {
			info.innerHTML = "请选中一条记录！";
			info.className = "alert alert-warning";
			return false;
		}
	}
	
	function editCurrentLine(grantId) {
		window.location.href = "grant/edit/" + grantId;
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
	
	function deleteGrant() {
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
					url : "grant/delete",
					data : {ids:ids},
					dataType : "text",
					success : function(backData){
						if (backData == 1) {
							info.innerHTML = "删除成功！";
							info.className = "alert alert-success";
							setTimeout(function(){
								window.location.href = "grant";
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
	
	function assign(grantId) {
		window.document.location.href = "grant/assign/" + grantId;
	}
</script>
<div class="bs-docs-example">
	<ul class="nav nav-tabs">
		<li class="active"><a href="grant">补助发放规则</a></li>
		<li><a href="group">分组管理</a></li>
	</ul>
</div>
<div id="info"></div>
<div>
	<form action="grant" method="post">
		<span>标题：</span><input type="text" id="grantTitle" name="grantTitle" class="input-medium search-query-extend" />
		<button type="submit" class="btn btn-info">查询</button>
		<button type="button" class="btn btn-primary" onclick="javascript:addGrant();">新增</button>
		<button type="button" class="btn btn-success" onclick="javascript:editGrant();">修改</button>
		<button type="button" class="btn btn-danger" onclick="javascript:deleteGrant();">删除</button>
	</form>
</div>
<div class="bs-docs-example">
	<table class="table table-bordered table-condensed" style="table-layout: fixed;">
		<thead>
			<tr>
				<th width="2%">
					<!-- <label class="checkbox inline"> -->
   					<input type="checkbox" id="allbox" onclick="javascript:selectAll(this.checked);" />
   					<!-- <font style="font: bold 13px Arial;">全选</font> -->
					<!-- </label> -->
    			</th>
				<th width="7%">标题</th>
				<th width="7%">发放金额</th>
				<th width="7%">是否自动发放</th>
				<th width="7%">创建时间</th>
				<th width="7%">备注</th>
				<th width="4%">操作</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${list}" var="v" varStatus="vs">
				<tr id="${v.GRANTID}" ondblclick="editCurrentLine(this.id);">
					<td style="text-align: center;">
						<input type="checkbox" value="${v.GRANTID}" name="grantIdBox" onclick="javascript: selectThis(this.parentNode.parentNode,this.checked);" />
					</td>
					<td>${v.GRANTTITLE}</td>
					<td>${v.GRANTAMOUNT}</td>
					<td>${v.ISAUTOGRANT}</td>
					<td>${v.CREATETIME}</td>
					<td nowrap title="${v.REMARK}" style="overflow: hidden;text-overflow: ellipsis;">${v.REMARK}</td>
					<td style="text-align: center;">
						<button type="button" class="btn btn-mini btn-warning" onclick="javascript:assign(${v.GRANTID});">指派发放分组</button>
					</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
</div>
<%@include file="/jsp_tiles/include/admin/pager.jsp"%>