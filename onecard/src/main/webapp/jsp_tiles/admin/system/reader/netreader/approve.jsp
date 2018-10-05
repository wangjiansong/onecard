<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/jsp_tiles/include/taglibs.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<c:set var="NETREADER_URL">
	<c:url value="/admin/netreader" />
</c:set>
<%
Object a = request .getAttribute("mp");
%>
<script src="<c:url value='/media/bootstrap/js/bootstrap.file-input.js' />"></script>
<script src="<c:url value='/media/js/ajaxfileupload.js' />"></script>
<script type="text/javascript">
	String.prototype.trim = function() {
		return this.replace(/^\s+/g, "").replace(/\s+$/g, "");
	};
	
	String.prototype.trim = function() {
		return this.replace(/(^\s*)|(\s*$)/g, "");
	};
	
	
	$(function() {
		$('input[type=file]').bootstrapFileInput();
		checkAll(true);
	});
	
	function ajaxFileUpload() {
		var fileName = $('#fileupload').val();
		if(fileName.indexOf(".") == -1) {
			showResult("请选择xls格式的文件", "error");
			return false;
		}
		var tmp = fileName.split(".");
		var type = tmp[1];
		if(type != 'xls') {
			showResult("请选择xls格式的文件", "error");
			return false;
		} else {
			$.ajaxFileUpload ({
		        url: 'uploadFile',
		        secureuri: false,
		        fileElementId: 'fileupload',
		        async:false,
		        dataType: 'json',
		        success: function(data,textStatus){
			        if(data.success==true){	
			        	showResult(data.info, "success");
			        	alert(data.info);
			        	window.location.href = "<c:url value='/admin/netreader/approve' />";
			        	checkAll(true);
		        		return true;
			        }else{
			        	showResult(data.info, "fail");
			        	alert(data.info);
		        		window.location.href = "<c:url value='/admin/netreader/showImportReaderList' />";
		        		return false;
			        }
		        },
		        error: function(xhr,status,errMsg){
		        	showResult("无数据返回", "error");
		        	return false;
		        }
		    });
		}
	}
	
	function exportDemoExcel(){
		window.location.href = "<c:url value='/admin/netreader/exportDemoExcel' />";
		
	}
	
	function approvePass() {
		var boxs = document.getElementsByName("readerId_box");
		var ids = [];
		var states = [];
		for(var i=0; i<boxs.length; i++){
			if(boxs[i].checked){
				ids.push(boxs[i].id);
				states.push(boxs[i].value);
			}
		}
		if(ids.length){
			var table = document.getElementById("netreader_table");
			for(var i=0; i<states.length; i++){
				if(table.rows[states[i]].cells[1].innerText != 1){
					showResult("请选择待审批的记录！", "error");
					return false;
				}
			}
			if(!confirm("确认操作？")){
				return false;
			}
			$.ajax({
				type : "POST",
				url : "<c:url value='/admin/netreader/approvePass' />",
				data : {ids : ids.toString()},
				dataType : "text",
				success : function(backData){
					if(backData == ids.length){
						showResult("操作成功！", "success");
						window.location = window.location;
						checkAll(true);
					}else{
						showResult("操作失败！", "error");
					}
				},
				error : function(){
					alert("获取数据失败！");
					return;
				}
			});
		}else{
			showResult("请选择记录！", "error");
		}
	}
	
	function approveReject() {
		var boxs = document.getElementsByName("readerId_box");
		var ids = [];
		var states = [];
		for(var i=0; i<boxs.length; i++){
			if(boxs[i].checked){
				ids.push(boxs[i].id);
				states.push(boxs[i].value);
			}
		}
		if(ids.length){
			var table = document.getElementById("netreader_table");
			for(var i=0; i<states.length; i++){
				if(table.rows[states[i]].cells[1].innerText != 1){
					showResult("请选择待审批的记录！", "error");
					return false;
				}
			}
			if(!confirm("确认操作？")){
				return false;
			}
			$.ajax({
				type : "POST",
				url : "<c:url value='/admin/netreader/approveReject' />",
				data : {ids : ids.toString()},
				dataType : "text",
				success : function(backData){
					if(backData > 0){
						showResult("操作成功！", "success");
						window.location = window.location;
					}else{
						showResult("操作失败！", "error");
					}
				},
				error : function(){
					showResult("操作失败！", "error");
					return;
				}
			});
		}else{
			showResult("请选择记录！", "error");
		}
	}
	
	function showResult(tip, caution) {
		$("#tip").css("display", "block").html(tip);
		setTimeout(function(){
			$("#tip").fadeOut("slow");
		},5000);
		$("#tip").attr("class", "A_MESSAGEBOX_LOADING alert alert-" + caution);
		return;
	}
	
	function detailNetReader(netreaderId) {
		window.location.href = "${NETREADER_URL}/detailNetReader/"+netreaderId;
	}
	function deleteNetReader(netreaderId) {
		var boxs = document.getElementsByName("readerId_box");
		var ids = [];
		var states = [];
		for(var i=0; i<boxs.length; i++){
			if(boxs[i].checked){
				ids.push(boxs[i].id);
				states.push(boxs[i].value);
			}
		}
		if(ids.length){
			if(!confirm("确认删除？")){
				return false;
			}
			$.ajax({
				type : "POST",
				url : "<c:url value='/admin/netreader/deleteNetReader' />",
				data : {ids : ids.toString()},
				dataType : "text",
				success : function(backData){
					if(backData > 0){
						showResult("操作成功！", "success");
						window.location = window.location;
					}else{
						showResult("操作失败！", "error");
					}
				},
				error : function(){
					alert("获取数据失败！");
					return;
				}
			});
		}else{
			showResult("请选择记录！", "error");
		}
	}
	function checkAll(isChecked) {
		var box_title = document.getElementsByName("readerId_box_title");
		var boxs = document.getElementsByName("readerId_box");
		var rows = document.getElementById("netreader_table").rows;
		if(isChecked){
			if(boxs.length>=1){
				for(var j=0; j<box_title.length;j++){
					box_title[j].checked = isChecked;
				}
			}
			for(var i=0; i<boxs.length; i++){
				boxs[i].checked = isChecked;
				rows[i+1].className = "info";
			}
		}else{
			if(boxs.length>=1){
				for(var j=0; j<box_title.length;j++){
					box_title[j].checked = isChecked;
				}
			}
			for(var i=0; i<boxs.length; i++){
				boxs[i].checked = isChecked;
				rows[i+1].className = "";
			}
		}
	}
	
	function getSelected(thisobj,checkobj) {
		if(checkobj.checked){
			checkobj.checked = false;
			thisobj.className = "";
		}else{
			checkobj.checked = true;
			thisobj.className = "info";
		}
	}
</script>

<section id="tabs">
	<div>
		<ul id="myTab" class="nav nav-tabs">
			<li class="active"><a href="#" data-toggle="tab">注册审批</a></li>
		</ul>
		<div id="myTabContent" class="tab-content">
		    <div class="tab-pane active">

<div id="tip" class="A_MESSAGEBOX_LOADING alert alert-success" style="display: none;">
</div>

<form id="dataform" method="post" class="form-inline" action="<c:url value='/admin/netreader/approve' />">
	<div class="well form-actions_1">
		<div class="control-group">
			<span>审批状态：</span>
			<select id="checkState" name="checkState" style="width: 170px;">
				<option value="0">请选择</option>
				<option value="1">待审批</option>
				<option value="2">审批通过</option>
				<option value="3">审批未通过</option>
			</select>
			<span>读者证号：</span>
			<input type="text" id="readerId" name="readerId" class="input-medium search-query-extend" />
			<span>读者姓名：</span>
			<input type="text" id="readerName" name="readerName" class="input-medium search-query-extend" />
			<button type="submit" class="btn btn-info">查询</button>
		</div>
		&nbsp;
		<div class="btn-group">
		<button type="button" class="btn btn-success" onclick="javascript:approvePass();">审核通过</button>
		<button type="button" class="btn btn-danger" onclick="javascript:approveReject();">审核不通过</button>
		<button type="button" class="btn" onclick="javascript:deleteNetReader();">批删除</button>
		</div>
		<input id="fileupload" title="选择文件" type="file" class="btn-success" name="files[]" multiple/>
		<div class="btn-group">
			<button type="button" class="btn btn-info" onclick="javascript:ajaxFileUpload()">上传</button>
			<button type="button" class="btn" onclick="javascript:exportDemoExcel()">下载模板</button>
		</div>
		
	</div>
</form>
<table id="netreader_table" class="table table-bordered table-hover table-condensed">
	<thead>
		<tr>
			<th width="2.3%"><input type="checkbox" name="readerId_box_title" onclick="checkAll(this.checked);" style="margin-bottom: 4px;" /></th>
			<th width="6%">审批状态</th>
			<th width="6%">读者证号</th>
			<th width="5%">姓名</th>
			<th width="9%">读者类型</th>
			<th width="5%">读者证状态</th>
			<th width="11%">开户馆</th>
			<th width="11%">单位</th>
			<th width="3%">操作</th>
		</tr>
 	</thead>
	<tbody>
		<c:forEach items="${list}" var="v" varStatus="vs">
			<tr onclick="javascript:getSelected(this,document.getElementById('${v.readerId}'));">
				<td style="text-align: center;padding-top: 1px;">
					<input type="checkbox" id="${v.readerId}" name="readerId_box" value="${vs.index+1}" 
						onclick="javascript:this.checked=!this.checked;" />
						<!-- if(this.checked==true){this.checked=false;}else{this.checked=true;} -->
				</td>
				<td style="display: none;">${v.checkState}</td>
				<td>${v.checkStateStr}</td>
				<td>${v.readerId}</td>
				<td>${v.readerName}</td>
				<td>${v.readerType}</td>
				<td>${v.readerCardStateStr}</td>
				<td>${v.readerLib}</td>
				<td>${v.readerUnit}</td>
				<td style="text-align: center;">
					<button class="btn btn-mini btn-success" type="button" onclick="javascript:detailNetReader('${v.readerId}');">查看</button>
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