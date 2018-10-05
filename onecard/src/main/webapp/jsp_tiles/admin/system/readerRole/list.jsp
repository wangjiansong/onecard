<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/jsp_tiles/include/taglibs.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<c:set var="BASE_URL">
	<c:url value="/admin/sys/readerRole" />
</c:set>
<c:set var="READER_URL">
	<c:url value="/admin/reader" />
</c:set>


<script type="text/javascript">
	String.prototype.trim = function() {
		return this.replace(/^\s+/g, "").replace(/\s+$/g, "");
	};

	$(function(){
		$("#reader_table tr").click(function(obj) {
			$(this).find("input[type='radio']").attr("checked","checked");
			$("#reader_table tr").removeClass("info");
			$(this).addClass("info");
		});
		if("${obj.rdId}"){
			document.getElementById("rdId").value = "${obj.rdId}";
		}
		if("${obj.rdName}"){
			document.getElementById("rdName").value = "${obj.rdName}";
		}
	});

	var table = document.getElementById("reader_table");
	function getThisLine(line) {
		line.className += "info";
	}
	
	function editOperator(rdId) {
		window.location.href = "${BASE_URL}/editOperator/"+rdId;
	}
	
	function exportReaderExcel() {
		var rdType = document.getElementById("rdType").value;
		var rdId = document.getElementById("rdId").value.trim();
		var rdName = document.getElementById("rdName").value.trim();
		var params = {rdType : rdType,rdId : rdId,rdName : rdName};
		$.ajax({
			type : "POST",
			url : "<c:url value='/admin/reader/exportReaderExcel' />",
			data : params,
			dataType : "text",
			success : function(backData){
// 				if(backData > 0){
// 					alert("删除成功！");
// 					document.location = document.location;
// 				}else{
// 					alert("删除失败！");
// 					return;
// 				}
			},
			error : function(){
				alert("获取数据失败！");
				return;
			}
		});
	}
	
	function setOperator(id) {
		document.location.href="${BASE_URL}/assign?rdId=" + id;
	}
	
	function doSubmit(type) {
		var dataform = document.getElementById("dataform");
		if(type == 1){
			dataform.action = "list";
			dataform.submit();
		}else{
			dataform.action = "<c:url value='/admin/reader/exportReaderExcel' />";
			dataform.submit();
		}
	}
	
	function setReaderLibUser(rdId) {
		$.ajax({
			type : "POST",
			url : "<c:url value='/admin/reader/setReaderLibUser' />",
			data : {rdId : rdId, libUser: 0},
			dataType : "json",
			success : function(jsonData){
				var success = jsonData.success;
				if(success == 1) {
					alert("证操作成功!");
					document.location = document.location;
				}
			},
			cache:false
		});
	}
	function doAddOperator() {
		window.location.href = "${BASE_URL}/addOperator";
	}
	
	function deleteOperator(rdId) {
		if (!confirm("你确定要删除这个用户？")) return;
		document.location.href = "${BASE_URL}/deleteOperator/" + rdId ;
	}
	
</script>
<section id="tabs">
	<div>
		<ul id="myTab" class="nav nav-tabs">
			<li class="active"><a href="#" data-toggle="tab">用户角色管理</a></li>
		</ul>
		<div id="myTabContent" class="tab-content">
		    <div class="tab-pane active">


<form id="dataform" method="post" class="form-inline" action="">
	<div class="well form-actions_1" align="center">
		<span style="font: 13px/20px arial,sans-serif;">用户类型：</span>
		<select id="isLibUser" name="libUser" class="input-medium search-query-extend">
			<option value="1">馆内用户</option>
		</select>
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		<span style="font: 13px/20px arial,sans-serif;">用户账号：</span>
		<input type="text" id="rdId" name="rdId" class="input-medium" />
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		<span style="font: 13px/20px arial,sans-serif;">姓名：</span>
		<div class="input-append">
		<input type="text" id="rdName" name="rdName" class="input-medium" />
		<button type="button" class="btn btn-info" onclick="doSubmit(1);"><i class="icon-search icon-white"></i>查询</button>
		<button class="btn btn-success" type="button" onclick="doAddOperator()">新增用户</button>
		</div>
	</div>
</form>
<table id="reader_table" class="table table-bordered table-hover table-condensed table-striped">
	<thead>
		<tr>
			<th width="6%">用户账号</th>
			<th width="5%">姓名</th>
			<th width="13%">工作单位</th>
			<th width="13%">电子邮箱</th>
			<th width="5%">手机</th>
			<th width="10%">操作</th>
		</tr>
 	</thead>
	<tbody>
		<c:forEach items="${pageList}" var="v" varStatus="vs">
			<tr>
				<td>${v.rdId}</td>
				<td>${v.rdName}</td>
				<td>${v.rdUnit}</td>
				<td>${v.rdEmail}</td>
				<td>${v.rdLoginId}</td>
				<td style="text-align: center;">
					<div class="btn-group">
					<button class="btn btn-small btn-success" type="button" onclick="javascript:editOperator('${v.rdId}');">查看</button>&nbsp;|
					<button class="btn btn-small btn-info" type="button" onclick="javascript:setOperator('${v.rdId}');">分配角色</button>
					<button class="btn btn-small" type="button" onclick="javascript:deleteOperator('${v.rdId}');">删除</button>
					</div>
				</td>
			</tr>
		</c:forEach>
	</tbody>
</table>
<%@include file="/jsp_tiles/include/admin/paginator.jsp" %>


 </div>
		</div>
	</div>
</section>