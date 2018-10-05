<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/jsp_tiles/include/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<c:set var="BASE_URL">
	<c:url value="/admin/sys/res" />
</c:set>

<script type="text/javascript">
$(function(){
	var chkSingle = $(".selAll");
	var chkGroup = $(".f input");
	//获取所有被勾选的input
	var funTrGet = function() {
		return $(".f input:checked");
	};
	//正选反选
	chkSingle.bind("click", function() {
	    if ($(this).attr("checked")) {
	        chkGroup.attr("checked", true);	
	    } else {
	        chkGroup.attr("checked", false);
	    }
	});
});
function Add(){
	document.location.href="${BASE_URL}/add";
}
function Edit(id){
	//document.location.href="${BASE_URL}/edit/"+id;
	$.ajax({
		type: "GET",
		url: "${BASE_URL}/edit/"+id,
		dataType: "json",
		success: function(jsonData){
			//{"resourceId":"SysCompet_add","subsys":"compet","resourceName":"增加权限","resourceUrl":"/admin/sys/compet/add","isMenu":0,"describe":null,"page":null}
			$("#input_resourceId").val(jsonData.resourceId);
			$("#hidden_resourceId").val(jsonData.resourceId);
			$("#input_resourceName").val(jsonData.resourceName);
			$("#input_resourceUrl").val(jsonData.resourceUrl);
			$("#input_subsys").val(jsonData.subsys);
			$("#input_isMenu").val(jsonData.isMenu);
			$("#input_describe").val(jsonData.describe);
		},
		cache: false
	});
}
function submitForm() {
	document.submitform.submit();
}
function validata(id) {
	$.ajax({
		type: "GET",
		url: "validate/"+id,
		dataType: "html",
		success: function(htmlData){
			if (!confirm(htmlData + " 你确定要删除此条记录？")) return;
			Delete(id);
		},
		cache: false
	});
}

function Delete(id){
	if (!confirm(" 你确定要删除此条记录？")) return;
	document.location.href = "${BASE_URL}/delete/" +id;
}
</script>
<section id="tabs">
	<div>
		<ul id="myTab" class="nav nav-tabs">
			<li class="active"><a href="#" data-toggle="tab">资源管理</a></li>
		</ul>
		<div id="myTabContent" class="tab-content">
		    <div class="tab-pane active">
		    	<fieldset>
	<form action="list" id="form1" method="POST" class="form-inline">
	<div class="well form-actions_1">
		<span>资源名称：</span>
		<div class="input-append">
		<input type="text" name="resourceName" value="${recourceName}" class="input-medium search-query-extend" />
		<button class="btn btn-info" type="submit"><i class="icon-search icon-white"></i>查询</button>
		<a class="btn btn-success" onclick="Add()" ><i class="icon-plus icon-white"></i>新增资源</a>
		</div>
	</div>
	</form>
</fieldset>
<table class="table table-bordered table-hover table-condensed table-striped">
	<thead>
		<tr>
			<th width="13%"><input type="checkbox" class="selAll"/></th>
			<th>资源名称</th>
			<th>前台显示</th>
			<th>URL</th>
			<th width="15%">操作</th>
		</tr>
	</thead>
	<tbody>
		<c:forEach items="${pageList}" var="p">
 			<tr class="f">
 				<td><input type="checkbox" id="${p.RESOURCEID}"/></td>
 				<td><c:out value="${p.RESOURCENAME}" /></td>
 				<td>
 					<c:if test="${p.ISMENU eq '1'}">是</c:if>
 					<c:if test="${p.ISMENU eq '0'}">否</c:if>
 				</td>
 				<td><c:out value="${p.RESOURCEURL}" /></td>
 				<td>	
 				<div class="btn-group">
  				<a data-toggle="modal" href="#myModal" id="editButton" class="btn btn-success btn-small" onclick="Edit('${p.RESOURCEID}')">查看</a>
				<input id="deleteButton" type="button" class="btn btn-small" value="删除" onclick="Delete('${p.RESOURCEID}')"/>
 				</div>
			</td>
 			</tr>
    	</c:forEach>
	</tbody>
</table>
<div class="modal hide fade in" id="myModal" style="display: none;">
  	<div class="modal-header">
    	<a class="close" data-dismiss="modal">×</a>
    	<h3>编辑</h3>
  	</div>
  	<div class="modal-body">
	  	<form action="${BASE_URL}/update" name="submitform" method="POST" >
			<div class="control-group">
				<label class="control-label" for="input_resourceI">
					资源ID
				</label>
				<div class="controls">
					<input type="text" name="resourceId" id="input_resourceId" value="" disabled/>
					<input type="hidden" name="resourceId" id="hidden_resourceId" />
				</div>
			</div>
			<div class="control-group">
				<label class="control-label" for="input_resourceName">
					资源名称
				</label>
				<div class="controls">
					<input type="text" name="resourceName" class="input-medium" id="input_resourceName" value="" />
				</div>
			</div>
			<div class="control-group">
				<label class="control-label" for="input_subsys">
					所属父菜单
				</label>
				<div class="controls">
					<select id="input_subsys" name="subsys" class="input-medium search-query-extend">
						<option value="reader">用户管理 | reader</option>
						<option value="cirfin">结算中心 | cirfin</option>
						<option value="rights">授权管理 | rights</option>
						<option value="library">图书馆组织管理 | library</option>
					</select>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label" for="input_isMenu">
					是否前台显示
				</label>
				<div class="controls">
					<select id="input_isMenu" name="isMenu" class="input-medium search-query-extend">
						<option value="1">是</option>
						<option value="0">否</option>
					</select>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label" for="input_resourceUrl">
					URL
				</label>
				<div class="controls">
					<input type="text" name="resourceUrl" class="input-medium" id="input_resourceUrl" value="" />
				</div>
			</div>
			<div class="control-group">
				<label class="control-label" for="input_describe">
					描述
				</label>
				<div class="controls">
					<textarea rows="5" id="input_describe" name="describe" style="width: 300px;" value=""></textarea>
				</div>
			</div>
	  	</form>
  	</div>
  	<div class="modal-footer">
    	<a href="#" class="btn" data-dismiss="modal">取消</a>
    	<a href="#" onclick="submitForm()" class="btn btn-success"><i class="icon-ok icon-white"></i>保存修改</a>
  	</div>
</div>

<c:set var="pager_base_url">${BASE_URL}/list?</c:set>
<%@include file="/jsp_tiles/include/admin/paginator.jsp" %>
		    </div>
		</div>
	</div>
</section>




