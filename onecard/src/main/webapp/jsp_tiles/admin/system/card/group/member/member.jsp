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
		if ("${obj.rdId}") {
			$("#rdId").val("${obj.rdId}");
		}
		if ("${obj.rdName}") {
			$("#rdName").val("${obj.rdName}");
		}
	});
	
	function addMember() {
		$.colorbox({
			width: "80%",
			height: "86%",
			iframe: true,
			close: "",
			href: "add/${groupId}",
			overlayClose: true
		});
	}
	
	function deleteMember() {
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
					url : "${base_url}/group/member/deleteMember",
					data : {groupId:"${groupId}",ids:ids},
					dataType : "text",
					success : function(backData){
						if (backData == 1) {
							info.innerHTML = "删除成功！";
							info.className = "alert alert-success";
							setTimeout(function(){
								window.location.href = "${base_url}/group/member/${groupId}";
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
		window.document.location.href = "${base_url}/group/index";
	}
</script>
<div id="info"></div>
<ul class="breadcrumb">
	<li><a href="${base_url}/group/index">分组管理</a><span class="divider">/</span></li>
	<li class="active">分组成员</li>
</ul>
<div>
	<form action="${base_url}/group/member/${groupId}" method="post">
		<span style="font: 13px arial;">读者证号：</span>
		<input type="text" id="rdId" name="rdId" class="input-medium search-query-extend" />
		&nbsp;&nbsp;&nbsp;
		<span style="font: 13px arial;">姓名：</span>
		<input type="text" id="rdName" name="rdName" class="input-medium search-query-extend" />
		&nbsp;&nbsp;&nbsp;
		<button type="submit" class="btn btn-info">查询</button>
		<button type="button" class="btn btn-primary" onclick="javascript:addMember();">添加</button>
		<button type="button" class="btn btn-danger" onclick="javascript:deleteMember();">删除</button>
		<button type="button" class="btn btn-inverse" onclick="javascript:getBack();">返回</button>
		&nbsp;&nbsp;&nbsp;
	</form>
</div>
<div class="bs-docs-example">
	<table class="table table-bordered table-condensed">
		<thead>
			<tr>
				<th width="1%">
   					<input type="checkbox" id="allbox" onclick="javascript:selectAll(this.checked);" />
    			</th>
				<th width="3%">读者证号</th>
				<th width="3%">姓名</th>
				<th width="3%">身份证号</th>
				<th width="4%">读者类型</th>
				<th width="5%">开户管</th>
				<th width="2%">读者证状态</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${readers}" var="v" varStatus="vs">
				<tr>
					<td style="text-align: center;">
						<input type="checkbox" value="${v.RDID}" name="rdIdBox" onclick="javascript: selectThis(this.parentNode.parentNode,this.checked);" />
					</td>
					<td>${v.RDID}</td>
					<td>${v.RDNAME}</td>
					<td>${v.RDCERTIFY}</td>
					<td>${v.RDTYPE}</td>
					<td>${v.RDLIB}</td>
					<td>${v.RDCFSTATE}</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
</div>
<%@include file="/jsp_tiles/include/admin/pager.jsp" %>