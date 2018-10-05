<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/jsp_tiles/include/taglibs.jsp"%>
<%@include file="/jsp_tiles/include/head_ztree.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<c:set var="BASE_URL">
	<c:url value="/admin/sys/compet" />
</c:set>
<script type="text/javascript">
window.parent.$("#isUpdate").val("0");
var setting = {

	check: {
		enable: true
	},
	callback: {
		onCheck: zTreeOnCheck,
	}
};
function filter(node) {
    return (node.checked&&node.level==2);
}
function zTreeOnCheck(event, treeId, treeNode) {
	window.parent.$("#isUpdate").val("1");
	if(treeNode.checked){
		if(treeNode.isParent){
			$.each(treeNode.children, function(key, node) {
				if(node.isParent){
					$.each(node.children, function(key, node1) {
						document.getElementById("dynamic").options.add(new Option(node1.name,node1.id));
					});
				}else if(node.level==1){//第三次有具体链接的才加到我的菜单中
					document.getElementById("dynamic").options.add(new Option(node.name,node.id));
				}		
			});
			
		}else{
			if(treeNode.level==1){//第三次有具体链接的才加到我的菜单中
				document.getElementById("dynamic").options.add(new Option(treeNode.name,treeNode.id));
			}
		}			
	}else{
		if(treeNode.isParent){
			var nodes = treeNode.children;
			$.each(nodes, function(key, node) {
				if(node.isParent){
					$.each(node.children, function(key, node1) {
						$("#dynamic option[value='"+node1.id+"']").remove();
					});
				}else{
					$("#dynamic option[value='"+node.id+"']").remove();	
				}
			});
		}else{
			$("#dynamic option[value='"+treeNode.id+"']").remove();
		}
	}
}
$(function(){
	var menu = ${resourceMenu};
	$.fn.zTree.init($("#menutree"), setting, menu);

	var treeObj = $.fn.zTree.getZTreeObj("menutree");
	treeObj.expandNode(treeObj.getNodeByTId("tree_1"), true, false, true, false);
	$("#dynamic option:selected").attr("selected",false);
});
function save() {
	var list = new Array();
	$("#dynamic option").each(function(i){
		list[i]=$(this).val();
	});
	$("#resIds").val(list.join("~m~"));
	$('#assignForm').submit();
}
</script>
<section id="tabs">
	<div>
		<ul id="myTab" class="nav nav-tabs">
			<li class="active"><a href="#" data-toggle="tab">权限管理 >> 分配资源</a></li>
		</ul>
		<div id="myTabContent" class="tab-content">
		    <div class="tab-pane active">

<fieldset>
	<div class="well form-actions_1">
		<div class="btn-group">
			<input type="button" class="btn btn-success" value="保存分配" onclick="javascript:save();"/>
			<a type="button" class="btn" href="${BASE_URL}/list">取消</a>
		</div>
		<form:form id="assignForm" method="POST" action="saveAssign">
		<input type="hidden" name="competId" id="competId" value="${competId}" />
		<input type="hidden" name="resIds" id="resIds" />
		</form:form>
	</div>
	
	<div class="container-fluid">
		<div class="row-fluid">
		<div id="leftDiv" class="span4">
			已选资源
			<select id="dynamic" multiple="multiple" style="width: 180px;height: 360px">
				<c:forEach items="${comRes}" var="item">
					<option value="${item.resourceId}" selected="false">${item.resourceName}</option>
				</c:forEach>
			</select>
		</div>
		<div class="span4" style="overflow:scroll;height: 360px;width: 200px;">
			点击勾选增加，取消勾选删除
			<ul id="menutree" class="ztree"></ul>
		</div>
		<div class="span4"></div>
		</div>
	</div>
</fieldset>

		    </div>
		</div>
	</div>
</section>