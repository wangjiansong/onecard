<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/jsp_tiles/include/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<script type="text/javascript" src="<c:url value='/media/datepicker/WdatePicker.js' />"></script>
<c:set var="BASE_URL">
	<c:url value="/admin/sys/restaurant" />
</c:set>
<link rel="stylesheet" href="<c:url value='/media/bootstrap/css/bootstrap-datetimepicker.min.css' />" type="text/css"/>
<script type="text/javascript" src="<c:url value='/media/bootstrap/plugin/bootstrap-datetimepicker.js' />"></script>
<script type="text/javascript" src="<c:url value='/media/bootstrap/i18n/bootstrap-datetimepicker.zh-CN.js' />"></script>
<script type="text/javascript">
$(function (){
	$(".form_datetime").datetimepicker({
        showMeridian: true,
        format: "HH:ii:ss",
        language: 'zh-CN',
        autoclose: true,
        startView: 'day',
        minView: 0,
        minuteStep: 5
    });
	
});
function pressEnter(e,method){
	if(e==null){//ie6..
		e=event;
	}	
	if ((e.which == 13 || e.keyCode == 13)&&(!(e.ctrlKey || e.metaKey))
			&& !e.shiftKey && !e.altKey) {
		 method();
	}
}
function edit(id) {
	window.location.href = "${BASE_URL}/edit/" + id;
}
function update() {
	var breakfastStartTime = $("#breakfastStartTime").val();
	var breakfastEndTime = $("#breakfastEndTime").val();
	var lunchStartTime = $("#lunchStartTime").val();
	var lunchEndTime = $("#lunchEndTime").val();
	var dinnerStartTime = $("#dinnerStartTime").val();
	var dinnerEndTime = $("#dinnerEndTime").val();
	var supperStartTime = $("#supperStartTime").val();
	var supperEndTime = $("#supperEndTime").val();
	
	if(breakfastStartTime == null) showResult("请选择早班开始时间", "error");return false;
	if(breakfastEndTime == null) showResult("请选择早班结束时间", "error");return false;
	if(lunchStartTime == null) showResult("请选择午班开始时间", "error");return false;
	if(lunchEndTime == null) showResult("请选择午班结束时间", "error");return false;
	if(dinnerStartTime == null) showResult("请选择晚班开始时间", "error");return false;
	if(dinnerEndTime == null) showResult("请选择晚班结束时间", "error");return false;
	if(supperStartTime == null) showResult("请选择夜班开始时间", "error");return false;
	if(supperEndTime == null) showResult("请选择夜班结束时间", "error");return false;
	
	document.submitform.submit();
}

//提示信息
function showResult(tip, caution) {
	$("#tip").css("display", "block").html(tip);
	setTimeout(function() {
		$("#tip").fadeOut("slow");
	}, 5000);
	$("#tip").attr("class", "A_MESSAGEBOX_LOADING alert alert-" + caution);
	return;
}
</script>
<div id="tip" class="A_MESSAGEBOX_LOADING alert alert-success" style="display: none;">
</div>
<section id="tabs">
	<div>
		<ul id="myTab" class="nav nav-tabs">
			<li class="active"><a href="#" data-toggle="tab">班饮设置</a></li>
		</ul>
		<div id="myTabContent" class="tab-content">
		    <div class="tab-pane active">

<fieldset>
	<div class="well form-actions_1">
	<p>
		<form action="${BASE_URL}/update" id="form1" method="POST" class="form-inline" name="submitform">
		
		    <input type="hidden" id="id" name="id" value="${config.id}">
		<div>
			<span>早班开始</span>
		    <input size="10" type="text" id="breakfastStartTime" name="breakfastStartTime" value="${config.breakfastStartTime}" readonly class="date form_datetime input-medium">
		    <span>结束</span>
		    <input size="10" type="text" id="breakfastEndTime" name="breakfastEndTime" value="${config.breakfastEndTime}" readonly class="date form_datetime input-medium">
			<span>午班开始</span>
		    <input size="10" type="text" id="lunchStartTime" name="lunchStartTime" value="${config.lunchStartTime}" readonly class="date form_datetime input-medium">
		    <span>结束</span>
		    <input size="10" type="text" id="lunchEndTime" name="lunchEndTime" value="${config.lunchEndTime}" readonly class="date form_datetime input-medium">
		</div>
		<div>    
			<span>晚班开始</span>
		    <input size="10" type="text" id="dinnerStartTime" name="dinnerStartTime" value="${config.dinnerStartTime}" readonly class="date form_datetime input-medium">
		    <span>结束</span>
		    <input size="10" type="text" id="dinnerEndTime" name="dinnerEndTime" value="${config.dinnerEndTime}" readonly class="date form_datetime input-medium">
			<span>夜班开始</span>
		    <input size="10" type="text" id="supperStartTime" name="supperStartTime" value="${config.supperStartTime}" readonly class="date form_datetime input-medium">
		    <span>结束</span>
		    <input size="10" type="text" id="supperEndTime" name="supperEndTime" value="${config.supperEndTime}" readonly class="date form_datetime input-medium">
		</div>
			<button class="btn btn-success" onclick="update()">保存</button>
		</form>
	</p>
	
	</div>
</fieldset>
<table class="table table-bordered table-hover table-condensed table-striped">
	<thead>
		<tr>
			<th>早班开始时间/结束时间</th>
			<th>午班开始时间/结束时间</th>
			<th>晚班开始时间/结束时间</th>
			<th>夜班开始时间/结束时间</th>
			<th>操作</th>
		</tr>
	</thead>
	<tbody>
		<c:forEach items="${pageList}" var="p">
 			<tr class="f">
 				<td>
 					<c:out value="${p.breakfastStartTime}" />/
 					<c:out value="${p.breakfastEndTime}" />
 				</td>
 				<td>
 					<c:out value="${p.lunchStartTime}" />/
 					<c:out value="${p.lunchEndTime}" />
 				</td>
 				<td>
 					<c:out value="${p.dinnerStartTime}" />/
 					<c:out value="${p.dinnerEndTime}" />
 				</td>
 				<td>
 					<c:out value="${p.supperStartTime}" />/
 					<c:out value="${p.supperEndTime}" />
 				</td>
 				<td>
					<button class="btn btn-success" type="button" onclick="edit('${p.id}')">修改</button>
				</td>
 			</tr>
    	</c:forEach>
	</tbody>
</table>

		    </div>
		</div>
	</div>
</section>

