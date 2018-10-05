<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/jsp_tiles/include/taglibs.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<script type="text/javascript" src="<c:url value='/media/datepicker/WdatePicker.js' />"></script>
<c:set var="LOGCIR_URL">
	<c:url value="/admin/sys/syncrecord" />
</c:set>
<script type="text/javascript">

var table = document.getElementById("reader_table");
function getThisLine(line) {
	line.className += "info";
}

function showResult(tip) {
	$("#tip").css("display", "block").html(tip);
	setTimeout(function(){
		$("#tip").fadeOut("slow");
	},5000);
	$("#tip").attr("class", "A_MESSAGEBOX_LOADING alert alert-success");
	return;
}

function isEnter() {
	var theEvent = window.event;
	var code = theEvent.keyCode || theEvent.which || theEvent.charCode;
	if (code == 13) {
		doSubmit(1);
		return false;
	}
	return true;
}

function getcheckBoxVals(){
	var vals ;
    $("input[name='checkbox']:checked").each(function () {
         vals=vals+this.value+",";
    });
    vals = vals.replace("undefined","");
    vals = vals.substring(0,vals.lastIndexOf(","));
    return vals;
}

//复选框 全选和撤销 2014-05-26
$(function(){
	$("#syncLib").val('${obj.syncLib}');
	$("#syncType").val('${obj.syncType}');
	$(".form_datetime").datetimepicker({
        format: "yyyy-mm-dd hh:ii:ss",
        language: 'zh-CN',
        autoclose: true,
        startView: 'month',
        minView: 0,
        pickerPosition: "bottom-left",
        todayBtn: true
    });
	$("#startDate").val(new Date().Format("yyyy-MM-dd hh:mm:ss"));
	
	$("#endDate").val(new Date().Format("yyyy-MM-dd hh:mm:ss"));
	$("#startDate").val('${obj.startDate}');
	$("#endDate").val('${obj.endDate}');
});

function doSingleSync(id) {
	$("#" + id).attr("disabled", "true");
	$("#" + id).html("正在同步...");
	$.ajax({
		type : "POST",
		url : "${LOGCIR_URL}/dosync/" + id,
		dataType : "json",
		success : function(jsonData){
			var success = jsonData.success;
			var message = jsonData.message;
			if(success == 1) {
				alert("同步成功!");
				$("#" + id).removeAttr("disabled");
				$("#" + id).html("手动同步");
			} else {
				alert("同步失败！"+message);
				$("#" + id).removeAttr("disabled");
				$("#" + id).html("手动同步");
			}
			window.location = "<c:url value='/admin/sys/syncrecord/list' />";//刷新页面	
		},
		error : function(message){
			alert("获取接口数据失败！"+message);
			$("#synsButton").removeAttr("disabled");
			$("#synsButton").html("手动同步");
			return;
		},
		cache:false
	});
}
</script>
<section id="tabs">
	<div>
		<ul id="myTab" class="nav nav-tabs">
			<li class="active"><a href="#" data-toggle="tab">同步日志</a></li>
		</ul>
		<div id="myTabContent" class="tab-content">
		    <div class="tab-pane active">


<div id="tip" class="A_MESSAGEBOX_LOADING alert alert-success" style="display: none;">
</div>
	<div class="well form-actions_1">
		<form id="dataform" method="post" class="form-inline" action="list">
		<div class="control-group">
		<span>操作类型：</span>
		<select id="syncType" name="syncType" class="input-medium search-query-extend">
			<option value="">选择</option>
			<c:forEach items="${logCirTypeList}" var="entry">
				<c:if test="${obj.syncType eq (entry.logType)}">
					<option value="${entry.logType}" selected="selected">${entry.logType} | ${entry.typeName}</option>
				</c:if>
				<c:if test="${!(obj.syncType eq (entry.logType)) }">
					<option value="${entry.logType}">${entry.logType} | ${entry.typeName}</option>
				</c:if>
			</c:forEach>
		</select>
		<span>操作馆：</span>
		<select name="syncLib" id="syncLib" class="input-medium search-query-extend">
			<option value="">操作馆</option>
			<c:forEach items="${simpleLibcode}" var="entry">
				<c:if test="${obj.syncLib eq (entry.libCode) }">
					<option value="${entry.libCode}" selected="selected">${entry.libCode} | ${entry.simpleName}</option>
				</c:if>
				<c:if test="${!(obj.syncLib eq (entry.libCode)) }">
					<option value="${entry.libCode}">${entry.libCode} | ${entry.simpleName}</option>
				</c:if>
			</c:forEach>
		</select>
		</div>
		<div class="control-group">
			<span>起始日期：</span>
			<div class="input-append date form_datetime ">
			<input type="text" id="startDate" name="startDate" class="input-medium search-query-extend" />
			<span class="add-on"><i class="icon-calendar"></i></span>
			</div>
			<span>终止日期：</span>
			<div class="input-append date form_datetime ">
			<input type="text" id="endDate" name="endDate" class="input-medium search-query-extend" />
			<span class="add-on"><i class="icon-calendar"></i></span>
			</div>
			<button class="btn btn-info" type="submit">查询</button>
			<button type="reset" class="btn">重置</button>
		</div>
		</form>
	</div>

<div sytle="padding-left:30px;width:100%;">
</div>
<table id="logcir_table" class="table table-bordered table-hover table-condensed">
	<thead>
		<tr>
			<th width="10%">操作类型</th>
			<th width="10%">操作馆</th>
			<th width="10%">操作员</th>
			<th width="10%">操作读者</th>
			<th width="5%">状态</th>
			<th width="20%">操作时间</th>
			<th width="10%">操作</th>
		</tr>
 	</thead>
	<tbody>
		<c:forEach items="${pageList}" var="v" varStatus="vs">
			<tr class="f">
				<td>${v.syncType}</td>
				<td>${v.syncLib}</td>
				<td>${v.syncOperator}</td>
				<td>${v.syncRdid}</td>
				<c:if test="${v.syncStatus=='1'}">
					<td>已同步</td>
				</c:if>
				<c:if test="${v.syncStatus=='0'}">
					<td>未同步</td>
				</c:if>
				<c:if test="${v.syncStatus=='-1'}">
					<td>同步异常</td>
				</c:if>
				<td><fmt:formatDate value="${v.syncDate}"  pattern="yyyy-MM-dd HH:mm:ss" /></td>
				<td style="text-align: center;">
					<div class="btn-group">
					<button class="btn btn-mini btn-info" type="button" id="${v.syncId}" onclick="javascript:doSingleSync('${v.syncId}');">立即同步</button>					
					</div>
				</td>
			</tr>
		</c:forEach> 
	</tbody>
</table>
<%@include file="/jsp_tiles/include/admin/paginator.jsp" %>

  </div>
		</div>
	</div>
</section>