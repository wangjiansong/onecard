<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/jsp_tiles/include/taglibs.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<c:set var="BASE_URL">
	<c:url value="/system/reader/readertype" />
</c:set>
<script type="text/javascript">
	$(function(){
		$("#readertype_table tr").click(function(obj) {
			$(this).find("input[type='radio']").attr("checked","checked");
			$("#readertype_table tr").removeClass("info");
			$(this).addClass("info");
		});
	});

	function goAddReaderType() {
		window.location.href = "<c:url value='/system/reader/readertype/goAddReaderType'/>";
	}
	
	function goEditReaderType() {
		var readerType = $('#readertype_table input[name="id_radio"]').filter(':checked').val();
		if(!readerType){
			alert("请选择要修改的记录！");
			return;
		}
		window.location.href = "<c:url value='/system/reader/readertype/goEditReaderType/'/>"+readerType;
	}
	
// 	function getSelectedRowId() {
// 		var id = $('#readertype_table input[name="id_radio"]').filter(':checked').val();
// 		return id;
// 	}
	
	function deleteReaderType() {
		var readerType = $('#readertype_table input[name="id_radio"]').filter(':checked').val();
		if(!readerType){
			alert("请选择要删除的记录！");
			return;
		}
		if(confirm("确认删除选中的记录？")){
			$.ajax({
				type : "POST",
				url : "<c:url value='/system/reader/readertype/deleteReaderType' />",
				data : {readerType : readerType},
				dataType : "text",
				success : function(backData){
					if(backData > 0){
						alert("删除成功！");
						window.location.href = "<c:url value='/system/reader/readertype/readerTypeIndex' />";
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
	
	function queryReaderTypeList(readerType) {
		window.location.href = "<c:url value='/system/reader/readertype/readerTypeIndex'/>?readerType="+readerType;
	}
</script>


 <div class="well form-inline">
	<span>读者类型代码：</span>
	<div class="btn-group">
	<input type="text" id="readertype_readertype" class="input-medium search-query-extend" />
	<button class="btn btn-info" onclick="javascript:queryReaderTypeList(document.getElementById('readertype_readertype').value);">查询</button>
	<button class="btn btn-primary" onclick="javascript:goAddReaderType();">新增</button>
	<button class="btn btn-success" onclick="javascript:goEditReaderType();">修改</button>
	<button class="btn btn-danger" onclick="javascript:deleteReaderType();">删除</button>
	</div>
</div>
<table id="readertype_table" class="table table-bordered table-hover table-condensed">
	<thead>
		<tr>
			<th width="3%">选择</th>
			<th width="10%">读者类型代码</th>
			<th width="15%">读者类型名称</th>
			<th width="10%">分馆代码</th>
			<th width="5%">是否可用</th>
			<th width="5%">馆际是否可用</th>
		</tr>
 	</thead>
	<tbody>
		<c:forEach items="${list}" var="v" varStatus="vs">
			<tr>
				<td style="text-align:center;padding-top: 1px;">
					<input type="radio" name="id_radio" value="${v.READERTYPE }"/>
				</td>
				<td>${v.READERTYPE}</td>
				<td>${v.DESCRIPE}</td>
				<td>${v.LIBCODE}</td>
				<td>${v.SIGN}</td>
				<td>${v.LIBSIGN}</td>
			</tr>
		</c:forEach>
	</tbody>
</table>
<c:set var="pager_base_url">${BASE_URL}/readerTypeIndex?</c:set>
<%@include file="/jsp_tiles/include/admin/pager.jsp" %>
