<%@ page language="java" contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="/jsp_tiles/include/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<link type="text/css" rel="stylesheet" href="<c:url value='/media/colorbox/colorbox_4.css' />" />
<script type="text/javascript" src="<c:url value='/media/colorbox/jquery.colorbox-min.js' />"></script>
<c:set var="base_url">
	<c:url value="/admin/card" />
</c:set>
<script type="text/javascript">
	$(document).ready(function(){
		if ("${obj.groupName}") {
			$("input[name=groupName]").val("${obj.groupName}");
		}
	});
	
	function addAssignGroup() {
		$.colorbox({
			width: "60%",
			height: "60%",
			iframe: true,
			close: "",
			href: "add/${ruleId}",
			overlayClose: true
		});
	}
	
	function deleteAssignGroup() {
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
					url : "slotCard",
					data : {ruleId:"${ruleId}",ids:ids},
					dataType : "text",
					success : function(backData){
						if (backData == 1) {
							info.innerHTML = "删除成功！";
							info.className = "alert alert-success";
							setTimeout(function(){
								window.location.href = "${ruleId}";
							},1000);
							return false;
						} else {
							info.innerHTML = "删除失败！";
							info.className = "alert alert-error";
							return false;
						}
					},
					error : function(){
						info.innerHTML = "获取数据失败！";
						info.className = "alert alert-error";
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
		window.document.location.href = "${base_url}/consumption/rule";
	}
</script>
<div id="info"></div>
<ul class="breadcrumb">
	<li><a href="${base_url}/consumption/rule">刷卡消费规则</a><span class="divider">/</span></li>
	<li class="active">指定消费分组</li>
</ul>
<div>
	<form action="${base_url}/consumption/assign/${ruleId}" method="post">
		<span style="font: 13px arial;">分组名称：</span>
		<input type="text" id="groupName" name="groupName" class="input-medium search-query-extend" />
		&nbsp;&nbsp;&nbsp;
		<button type="submit" class="btn btn-info">查询</button>
		<button type="button" class="btn btn-primary" onclick="javascript:addAssignGroup();">添加</button>
		<button type="button" class="btn btn-danger" onclick="javascript:deleteAssignGroup();">删除</button>
		<button type="button" class="btn btn-inverse" onclick="javascript:getBack();">返回</button>
	</form>
</div>
<div class="bs-docs-example">
	<table class="table table-bordered table-condensed" style="table-layout: fixed;">
		<thead>
			<tr>
				<th width="1%">
   					<input type="checkbox" id="allbox" onclick="javascript:selectAll(this.checked);" />
    			</th>
				<th width="7%">分组名称</th>
				<th width="7%">创建时间</th>
				<th width="13%">备注</th>
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