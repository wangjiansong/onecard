<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/jsp_tiles/include/taglibs.jsp"%>
<%@include file="/jsp_tiles/include/head_ztree.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<c:set var="BASE_URL">
	<c:url value="/admin/sys/readerRole" />
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
						alert("1");
						document.getElementById("dynamic").options.add(new Option(node1.name,node1.id));
					});
				}else if(node.level==1){//第三次有具体链接的才加到我的菜单中
					alert("2");
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
	var menu = ${libResourceMenu};
	
	$.fn.zTree.init($("#menutree"), setting, menu);

	var treeObj = $.fn.zTree.getZTreeObj("menutree");
	treeObj.expandNode(treeObj.getNodeByTId("tree_1"), true, false, true, false);
	$("#dynamic option:selected").attr("selected",false);
});
function save() {
	var list = new Array();
	$("#dynamic option").each(function(i){
		if(i==0){
		    list[i]=","+$(this).val();
		}else{
			list[i]=$(this).val();
		}
	});
	$("#resIds").val(list.join(","));//选择多个馆阅览室合并在一起
	$('#assignForm').submit();
}
</script>
<section id="tabs">
	<div>
		<ul id="myTab" class="nav nav-tabs">
			<li class="active"><a href="#" data-toggle="tab">用户角色管理 >> 编辑用户>>馆权限设置</a></li>
		</ul>
		<div id="myTabContent" class="tab-content">
		    <div class="tab-pane active">


<fieldset>
<div class="form-actions">
	<div class="form-actions">
	<form:form id="assignForm" method="POST" action="saveLibAssign">
	<input type="hidden" name="rdId" id="rdId" value="${rdId}" />
	<input type="hidden" name="resIds" id="resIds" />
	</form:form>
	</div>
	<div class="container-fluid">
	<div class="row-fluid">
		<div id="leftDiv" class="span4">
			已选分馆
			<select id="dynamic" multiple="multiple" style="width: 180px;height: 360px">
				<c:forEach items="${selectedLibs}" var="item">
					<option value="${item.libCode}" selected="false">${item.simpleName}</option>
				</c:forEach>
			</select>
		</div>
		<div class="span4" style="overflow:scroll;height: 360px;width: 250px;">
			点击勾选增加，取消勾选删除
			<ul id="menutree" class="ztree"></ul>
		</div>
		<div class="btn-group span4">
			<input type="button" class="btn btn-success" value="保存分配" onclick="javascript:save();"/>
			<a type="button" class="btn btn-danger" href="${BASE_URL}/editOperator/${rdId}">取消</a>
		</div>
	</div>
	</div>
</div>
</fieldset>

		    </div>
		</div>
	</div>
</section>