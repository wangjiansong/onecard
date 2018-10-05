<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/jsp_tiles/include/taglibs.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<script type="text/javascript">
	$(function(){
		$("#libcode_table tr").click(function(obj) {
			$(this).find("input[type='radio']").attr("checked","checked");
			$("#libcode_table tr").removeClass("info");
			$(this).addClass("info");
		});
	});

	function goAddLibCode() {
		window.location.href = "<c:url value='/system/reader/libcode/goAddLibCode'/>";
	}
	
	function goEditLibCode() {
		var libCode = getSelectedRowId();
		if(!libCode){
			alert("请选择要修改的记录！");
			return;
		}
		window.location.href = "<c:url value='/system/reader/libcode/goEditLibCode/'/>"+libCode;
	}
	
	function getSelectedRowId() {
// 		return $("#libcode_table input[name='id_radio' checked='checked']").val();//error
// 		return $("#libcode_table tbody input:radio[name='id_radio']:checked").val();//error
// 		return $("input:radio[name='id_radio']:checked").val();//IE
// 		return $("#libcode_table input:checked").val();
		return $('#libcode_table input[name="id_radio"]').filter(':checked').val();
	}
	
	function deleteLibCode() {
		var libCode = getSelectedRowId();
		if(!libCode){
			alert("请选择要删除的记录！");
			return;
		}
		if(libCode == '999') {
			alert("不能删除内置馆：999馆");
			return;
		}
		if(confirm("确认删除选中的记录？")){
			$.ajax({
				type : "POST",
				url : "<c:url value='/system/reader/libcode/deleteLibCode' />",
				data : {libCode : libCode},
				dataType : "text",
				success : function(backData){
					if(backData > 0){
						alert("删除成功！");
						window.location.href = "<c:url value='/system/reader/libcode/libCodeIndex' />";
					}else{
						alert("删除失败！");
						return;
					}
				},
				error : function(){
					alert("获取数据失败！");
					return;
				}
			});
		}
	}
	
	function queryLibCodeList(libCode) {
		window.location.href = "<c:url value='/system/reader/libcode/libCodeIndex' />?libCode="+libCode;
	}
</script>
<div class="well form-inline">
	<span>馆代码：</span>
	<div class="btn-group">
	<input type="text" id="libcode_libCode" class="input-medium search-query-extend" 
		style="ime-mode:disabled;" onpaste="return false;" 
		ondragenter="return false;" oncontextmenu="return false;">
	<button class="btn btn-info" onclick="javascript:queryLibCodeList(document.getElementById('libcode_libCode').value);">查询</button>
	<button class="btn btn-primary" onclick="javascript:goAddLibCode();">新增</button>
	<button class="btn btn-success" onclick="javascript:goEditLibCode();">修改</button>
	<button class="btn btn-danger" onclick="javascript:deleteLibCode();">删除</button>
	</div>
</div>
<table id="libcode_table" class="table table-bordered table-hover table-condensed">
	<thead>
		<tr>
			<th width="4%">选择</th>
			<th width="4%">分馆代码</th>
			<th width="11%">分馆简称</th>
			<th width="10%">分馆名称</th>
			<th width="10%">分馆所在地址</th>
			<th width="7%">工作模式</th>
			<th width="10%">馆际还书指定地点</th>
		</tr>
 	</thead>
	<tbody>
		<c:forEach items="${list}" var="v" varStatus="vs">
			<tr>
				<td style="text-align:center;padding-top: 1px;">
					<input type="radio" name="id_radio" value="${v.LIBCODE }"/>
				</td>
				<td>${v.LIBCODE}</td>
				<td>${v.SIMPLENAME}</td>
				<td>${v.NAME}</td>
				<td>${v.ADDRESS}</td>
				<td>${v.WORKMODE}</td>
				<td>${v.RETSITE}</td>
			</tr>
		</c:forEach>
	</tbody>
</table>
<%@include file="/jsp_tiles/include/admin/pager.jsp" %>