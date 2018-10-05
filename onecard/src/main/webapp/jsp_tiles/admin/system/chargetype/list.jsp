<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/jsp_tiles/include/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<c:set var="BASE_URL">
	<c:url value="/admin/sys/chargetype" />
</c:set>

<script type="text/javascript">

function Add(){
	document.location.href="${BASE_URL}/add";
}
function Edit(id){
	document.location.href="${BASE_URL}/edit/"+id;
}
function Delete(id){
	if (!confirm(" 你确定要删除此条记录？")) return;
	document.location.href="${BASE_URL}/delete/" +id;
}
</script>
<section id="tabs">
	<div>
		<ul id="myTab" class="nav nav-tabs">
			<li class="active"><a href="#" data-toggle="tab">充值类型管理</a></li>
		</ul>
		<div id="myTabContent" class="tab-content">
		    <div class="tab-pane active">


<fieldset>
	<div class="well form-actions_1">
	<form action="list" id="form1" method="POST" class="form-horizontal">	
		<span>费用类型：</span>
		<input type="text" name="describe" value="${describe}" class="input-small search-query-extend" />
		<span>所属系统：</span>
		<div class="input-append">
		<select name="appCode" class="input-medium search-query-extend">
			<option value="">请选择</option>
			<c:forEach items="${chargeAppMap}" var="entry">
				<option value="${entry.key}">${entry.value}</option>
			</c:forEach>
		</select>
		<button class="btn btn-info" type="submit">查询</button>
		<button type="reset" class="btn">重置</button>
		</div>
	</form>
	</div>
	
</fieldset>
	<a class="btn btn-success" href="${BASE_URL}/add">新增</a>
	</p>
	<table class="table table-bordered table-hover table-condensed table-striped">
		<thead>
			<tr>
				<th width="15%">类型代码</th>
				<th>费用类型</th>
				<th>所属系统</th>
				<th width="15%">操作</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${pageList}" var="p">
  			<tr class="f">
  				<td><c:out value="${p.FEETYPE}" /></td>
  				<td><c:out value="${p.DESCRIBE}" /></td>
  				<td><c:out value="${p.APPNAME}" /></td>
  				<td>	
  					<div class="btn-group">
	  				<input id="editButton" type="button" class="btn btn-success btn-small" value="修改" onclick="Edit('${p.FEETYPE}')"/>
					<input id="deleteButton" type="button" class="btn btn-small" value="删除" onclick="Delete('${p.FEETYPE}')"/>
					</div>
				</td>
  			</tr>
	    	</c:forEach>
		</tbody>
	</table>
	<c:set var="pager_base_url">${BASE_URL}/list?</c:set>
	<%@include file="/jsp_tiles/include/admin/pager.jsp" %>
	


		    </div>
		</div>
	</div>
</section>