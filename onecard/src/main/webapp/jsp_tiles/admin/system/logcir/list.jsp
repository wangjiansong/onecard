<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/jsp_tiles/include/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<c:set var="BASE_URL">
	<c:url value="/admin/sys/logcir" />
</c:set>

<script type="text/javascript">
$(function(){
	$("#libcode").val('${obj.libcode}');
	$("#userId").val('${obj.userId}');
	$("#data2").val('${obj.data2}');
	$("#startTime").val('${obj.startTime}');
	$("#endTime").val('${obj.endTime}');
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
	form1.action = "<c:url value='/admin/sys/logcir/exportLogcirListExcel' />";
	form1.submit();
}

</script>

<section id="tabs">
	<div>
		<ul id="myTab" class="nav nav-tabs">
			<li class="active"><a href="#" data-toggle="tab">操作日志</a></li>
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
	<p>
		<form action="list" id="form1" method="POST" class="form-inline">
			<div class="control-group">
			<span>操作馆：</span>
			<select name="libcode" id="libcode" class="input-medium search-query-extend">
				<option value="">请选择</option>
				<c:forEach items="${simpleLibcode}" var="entry">
					<c:if test="${obj.libcode eq (entry.libCode) }">
						<option value="${entry.libCode}" selected="selected">${entry.libCode} | ${entry.simpleName}</option>
					</c:if>
					<c:if test="${!(obj.libcode eq (entry.libCode)) }">
						<option value="${entry.libCode}">${entry.libCode} | ${entry.simpleName}</option>
					</c:if>
				</c:forEach>
			</select>
			</div>
			<div class="control-group">
			<span>操作人：</span>
			<input type="text" name="userId" id="userId" value="${obj.userId }" class="input-medium search-query-extend" />
			</div>
			<div class="control-group">
			<span>读者证号：</span>
			<input type="text" name="data2" id="data2" value="${obj.data2 }" class="input-medium search-query-extend" />
			</div>

			<div class="control-group">
				<span>日期段：</span>
				<div class="input-append">
				<input type="text" name="startTime" id="startTime" value="${obj.startTime }"  
					class="date form_datetime input-small search-query-extend" /><span class="add-on"><i class="icon-calendar"></i></span>
				<input type="text" name="endTime" id="endTime" value="${obj.endTime }" 
					class="date form_datetime input-small search-query-extend" /><span class="add-on"><i class="icon-calendar"></i></span>
				<button class="btn btn-info" type="submit">查询</button>
				</div>
			</div>
		</form>
	</p>
	
	</div>
</fieldset>
</p>
<button class="btn btn-info" type="button" onclick="exportExcel();">导出Excel</button>
</p>
<table class="table table-bordered table-hover table-condensed table-striped">
	<thead>
		<tr>
			<th width="10%">操作类型</th>
			<th width="10%">操作馆</th>
			<th width="10%">操作员</th>
			<th width="10%">操作读者</th>
			<th width="10%">操作内容</th>
			<th width="20%">操作时间</th>
		</tr>
	</thead>
	<tbody>
		<c:forEach items="${pageList}" var="v" varStatus="vs">
			<tr class="f">
				<td>${v.logType}</td>
				<td>${v.libcode}</td>
				<td>${v.userId}</td>
				<td>${v.data2}</td>
				<td>${v.data4}</td>
				<td><fmt:formatDate value="${v.regtime}"  pattern="yyyy-MM-dd HH:mm:ss" /></td>
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