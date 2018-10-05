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
		if ("${obj.rdId}") {
			$("#rdId").val("${obj.rdId}");
		}
		if ("${obj.rdName}") {
			$("#rdName").val("${obj.rdName}");
		}
	});
	
	function addMember(commitButton) {
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
				url : "${base_url}/group/member/addMember",
				data : {groupId: "${groupId}",ids: ids},
				dataType : "text",
				success : function(backData){
					if (backData == 1) {
						info.innerHTML = "添加成功！";
						info.className = "alert alert-success";
						setTimeout(function(){
							parent.document.location.href = "${base_url}/group/member/${groupId}";
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
		<a class="brand" href="javascript:void(0);">添加分组成员</a>
	</div>
</div>
<div>
	<form action="${groupId}" method="post">
		<span style="font: 13px arial;">读者证号：</span>
		<input type="text" id="rdId" name="rdId" class="input-medium search-query-extend" />
		&nbsp;&nbsp;&nbsp;
		<span style="font: 13px arial;">姓名：</span>
		<input type="text" id="rdName" name="rdName" class="input-medium search-query-extend" />
		&nbsp;&nbsp;&nbsp;
		<button type="submit" class="btn btn-info">查询</button>
		<button type="button" class="btn btn-primary" onclick="javascript:addMember(this);">添加</button>
		<button type="button" class="btn btn-inverse" onclick="javascript:getBack();">返回</button>
		&nbsp;&nbsp;&nbsp;
		<span id="info"></span>
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