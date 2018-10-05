<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/jsp_tiles/include/taglibs.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<script type="text/javascript">
	
</script>
<p>
	<span>馆代码：</span>
	<input type="text" id="libCode" class="input-medium search-query-extend" 
		style="ime-mode:disabled;" onpaste="return false;" 
		ondragenter="return false;" oncontextmenu="return false;">
	<button class="btn btn-info" onclick="javascript:queryLibCodeList(document.getElementById('libCode').value);">查询</button>
	<button class="btn btn-primary" onclick="javascript:goAddLibCode();">新增</button>
	<button class="btn btn-success" onclick="javascript:goEditLibCode();">修改</button>
	<button class="btn btn-danger" onclick="javascript:deleteLibCode();">删除</button>
</p>
<table id="libcode_table" class="table table-bordered table-hover table-condensed">
	<thead>
		<tr>
			<th width="3%">选择</th>
			<th width="4%">分馆代码</th>
			<th width="11%">分馆简称</th>
			<th width="10%">分馆名称</th>
			<th width="10%">分馆所在地址</th>
			<th width="7%">工作模式</th>
			<th width="10%">馆际还书指定地点</th>
		</tr>
 	</thead>
	<tbody>
	</tbody>
</table>
<%@include file="/jsp_tiles/include/admin/pager.jsp" %>