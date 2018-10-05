<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/jsp_tiles/include/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<c:set var="BASE_URL">
	<c:url value="/admin/sys/finapp" />
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
	document.location.href = "${BASE_URL}/delete/" +id;
}

</script>

<section id="tabs">
	<div>
		<ul id="myTab" class="nav nav-tabs">
			<li class="active"><a href="#" data-toggle="tab">授权系统管理</a></li>
		</ul>
		<div id="myTabContent" class="tab-content">
		    <div class="tab-pane active">
<fieldset>
	<form action="list" id="form1" method="POST" class="form-inline">	
		<div class="well form-actions_1">
			<span>应用代码：</span>
			<input type="text" name="appCode" value="${appCode}" class="input-medium search-query-extend" />
			<span>应用名称：</span>
			<div class="input-append">
			<input type="text" name="appName" value="${appName}" class="input-medium search-query-extend" />
			<button class="btn btn-info" type="submit">查询</button>
			<button type="reset" class="btn">重置</button>
			</div>
		</div>
	</form>
</fieldset>
	<button class="btn btn-success" onclick="javascript:Add();">新增</button>
	</p>
	<table class="table table-bordered table-hover table-condensed table-striped">
		<thead>
			<tr>
				<th>应用代码</th>
				<th>应用名称</th>
				<th>接口地址</th>
				<th>参数</th>
				<th>加入时间</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${pageList}" var="p">
  			<tr class="f">
  				<td><c:out value="${p.appCode}" /></td>
  				<td><c:out value="${p.appName}" /></td>
  				<td><c:out value="${p.serviceURL}" /></td>
  				<td><c:out value="${p.paramInfo}" /></td>
  				<td>
  				<fmt:formatDate value="${p.regtime}"  pattern="yyyy-MM-dd HH:mm:ss" />
  				</td>
  				<td>	
  					<div class="btn-group">
	  				<input id="editButton" type="button" class="btn btn-success btn-small" value="修改" onclick="Edit('${p.appCode}')"/>
					<input id="deleteButton" type="button" class="btn btn-small" value="删除" onclick="Delete('${p.appCode}')"/>
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
