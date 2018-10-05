<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/jsp_tiles/include/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<c:set var="BASE_URL">
	<c:url value="/admin/sys/cirfinlog" />
</c:set>

<script type="text/javascript">
$(function() {
	$("#regLib").val('${obj.regLib}');
	$(".form_datetime").datetimepicker({
        format: "yyyy-mm-dd",
        language: 'zh-CN',
        autoclose: true,
        startView: 'month',
        minView: 2
    });
});

function Add(){
	document.location.href="<c:url value='' />";
}
function Edit(id){
	document.location.href="${BASE_URL}/edit/"+id;
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

function pressEnter(e,method){
	if(e==null){//ie6..
		e=event;
	}	
	if ((e.which == 13 || e.keyCode == 13)&&(!(e.ctrlKey || e.metaKey))
			&& !e.shiftKey && !e.altKey) {
		 method();
	}
}

function exportExcel(){
	var form1 = document.getElementById("form1");
	form1.action = "<c:url value='/admin/sys/cirfinlog/exportMonetaryListExcel' />";
	form1.submit();
	form1.action = "";//重置下action值 不然导出报表之后其他按钮都是导出报表功能 20150710
}

function doSubmit(){
	var dataform = document.getElementById("form1");
	dataform.action = "<c:url value='/admin/sys/cirfinlog/monetaryList' />";
	dataform.submit();
	form1.action = "";//重置下action值  20150710
}

</script>

<section id="tabs">
	<div>
		<ul id="myTab" class="nav nav-tabs">
			<li class="active"><a href="#" data-toggle="tab">财经金额统计</a></li>
		</ul>
		<div id="myTabContent" class="tab-content">
		    <div class="tab-pane active">

<fieldset>
	<div class="well form-actions_1">
	<c:if test="${!empty message}">
		<div class="alert alert-success">
			<a class="close" data-dismiss="alert">&times;</a>
			${message}
		</div>
	</c:if>
		<form action="monetaryList" id="form1" method="POST" class="form-inline">
			<div class="control-group">
			<span>操作馆：</span>
			<select name="regLib" id="regLib" class="input-small search-query-extend">
				<option value="">请选择</option>
				<c:forEach items="${simpleLibcode}" var="entry">
					<option value="${entry.libCode}">${entry.libCode} | ${entry.simpleName}</option>
				</c:forEach>
			</select>
			<span>读者姓名：</span>
			<input type="text" name="rdname" id="rdname" value="${obj.rdname}" class="input-small search-query-extend" />
			<span>读者证号：</span>
			<input type="text" name="rdid" id="rdid" value="${obj.rdid}" class="input-small search-query-extend" />
			</div>
			<div class="control-group">
			<span>每页记录数：</span>
			<input type="text"  name="page.showCount" value="${obj.page.showCount }" class="input-small search-query-extend"  />
						<span>所属分组：</span>
						<select id="groupID" name="groupID" class="input-small search-query-extend">
							<option value="">无</option>
							<c:forEach items="${groups}" var="gs">
								<c:if test="${obj.groupID==(gs.GROUPID) }">
									<option value="${gs.GROUPID}" selected="selected">${gs.GROUPNAME}</option>
								</c:if>
								<c:if test="${(obj.groupID!=(gs.GROUPID)) }">
									<option value="${gs.GROUPID}">${gs.GROUPNAME}</option>
								</c:if>
							</c:forEach>
						</select>
			</div>
			<div class="control-group">
			<span>日期段：</span>
			<div class="input-append">
			<input type="text" name="startTime" value="${obj.startTime }" class="date form_datetime input-small search-query-extend" />
			<span class="add-on"><i class="icon-calendar"></i></span>
			<span>到</span>
			<input type="text" name="endTime" value="${obj.endTime }" class="date form_datetime input-small search-query-extend" />
			<span class="add-on"><i class="icon-calendar"></i></span>
			<button class="btn btn-info" type="button" onclick="doSubmit();">查询</button>
			</div>
			</div>
		</form>
	
	</div>
</fieldset>
<table class="table table-bordered table-hover table-condensed table-striped">
	<thead>
		<tr>
			<th>人数</th>
			<th>人次</th>
			<th>财经总额(元)</th>
		</tr>
	</thead>
	<tbody>
 		<tr class="f" width="50%;">
 			<td><c:out value="${obj.rdidCount }" /></td>
 			<td><c:out value="${obj.rdidSum }" /></td>
 			<td><c:out value="${obj.feeSum }" /></td>
 		</tr>
	</tbody>
</table>
</p>
<button class="btn btn-info" type="button" onclick="exportExcel();">导出Excel</button>
</p>
<table class="table table-bordered table-hover table-condensed table-striped">
	<thead>
		<tr>
			<th>读者姓名</th>
			<th>读者账号</th>
			<th>参与系统</th>
			<th>财经类型</th>
			<th>财经金额(元)</th>
			<th>经手人</th>
			<th>发生时间</th>
		</tr>
	</thead>
	<tbody>
		<c:forEach items="${pageList}" var="p">
 			<tr class="f">
 				<td><c:out value="${p.rdname}" /></td>
 				<td><c:out value="${p.rdid}" /></td>
 				<td><c:out value="${p.feeAppCode}" /></td>
 				<td><c:out value="${p.feetype}" /></td>
 				<td><c:out value="${p.fee}" /></td>
 				<td>
 					<c:out value="${p.regman}" />
 				</td>
 				<td><fmt:formatDate value="${p.regtime}"  pattern="yyyy-MM-dd HH:mm:ss" /></td>
 			</tr>
    	</c:forEach>
	</tbody>
</table>

<c:set var="pager_base_url">${BASE_URL}/monetaryList?</c:set>
<%@include file="/jsp_tiles/include/admin/pager.jsp" %>


		    </div>
		</div>
	</div>
</section>