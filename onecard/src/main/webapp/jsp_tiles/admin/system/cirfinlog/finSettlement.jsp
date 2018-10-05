<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%-- <%@ page autoFlush="true" buffer="1094kb"%> --%>  
<%@include file="/jsp_tiles/include/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<c:set var="BASE_URL">
	<c:url value="/admin/sys/cirfinlog" />
</c:set>

<script type="text/javascript">

String.prototype.trim = function() {
	return this.replace(/^\s\s*/, '').replace(/\s\s*$/, '');
};


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
function buttonAction(dateFormat) {
	$("#dateFormat").val(dateFormat);
	$('.form_datetime').datetimepicker('remove');
	if(dateFormat == 'DAY') {
		$(".form_datetime").datetimepicker({
	        format: "yyyy/mm/dd",
	        language: 'zh-CN',
	        autoclose: true,
	        startView: 'month',
	        minView: 2
	    });
		
	} else if (dateFormat == 'MONTH' || dateFormat == 'SEASON') {
		$(".form_datetime").datetimepicker({
	        showMeridian: true,
	        format: "yyyy/mm",
	        language: 'zh-CN',
	        autoclose: true,
	        startView: 'year',
	        minView: 3
	    });
	} else if (dateFormat == 'YEAR') {
		$(".form_datetime").datetimepicker({
	        showMeridian: true,
	        format: "yyyy",
	        language: 'zh-CN',
	        autoclose: true,
	        startView: 'decade',
	        minView: 4
	    });
	} else {
		$(".form_datetime").datetimepicker({
	        showMeridian: true,
	        format: "yyyy/mm/dd HH:ii:ss",
	        language: 'zh-CN',
	        autoclose: true,
	        minView: 0,
	        minuteStep: 1
	    });
	}
	
}

function submitForm() {
	var startTimeStr = $("#startTime").val();
	var endTimeStr = $("#endTime").val();
	if(startTimeStr != "" && endTimeStr != "") {
		var startTime = new Date(startTimeStr);
		var endTime = new Date(endTimeStr);
		if(startTime.getTime() > endTime.getTime()) {
			showResult("起始时间应小于结束时间", "error");
			return;
		}
	}
	
	var submitform = document.getElementById("submitform");
	submitform.action = "<c:url value='/admin/sys/cirfinlog/finSettlement' />";
	submitform.submit();
	form1.action = "";//重置下action值 20150710
	
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

function exportExcel(){
	var submitform = document.getElementById("submitform");
	submitform.action = "<c:url value='/admin/sys/cirfinlog/exportFinSettlementExcel' />";
	submitform.submit();
	form1.action = "";//重置下action值 不然导出报表之后其他按钮都是导出报表功能 20150710
}
</script>

<div id="tip" class="A_MESSAGEBOX_LOADING alert alert-success" style="display: none;">
</div>
<section id="tabs">
	<div>
		<ul id="myTab" class="nav nav-tabs">
			<li class="active"><a href="#" data-toggle="tab">刷卡财经结算</a></li>
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
		<div class="btn-group">
			<a data-toggle="modal" href="#myModal" onclick="buttonAction('DAY')" class="btn btn-info">日统计</a>
			<a data-toggle="modal" href="#myModal" onclick="buttonAction('MONTH')" class="btn btn-info">月统计</a>
			<a data-toggle="modal" href="#myModal" onclick="buttonAction('MONTH')" class="btn btn-info">季统计</a>
			<a data-toggle="modal" href="#myModal" onclick="buttonAction('YEAR')" class="btn btn-info">年统计</a>
		</div>
	</p>
	
	</div>
</fieldset>
</p>


<table class="table">
	<tbody style="text-align: left">
 		<tr class="f">
 			<td>
			<c:choose> 
				<c:when test="${obj.dateFormat eq 'DAY'}">
				日统计   
				</c:when> 
				<c:when test="${obj.dateFormat eq 'MONTH'}">   
				月统计
				</c:when> 
				<c:when test="${obj.dateFormat eq 'YEAR'}">   
				年统计
				</c:when> 
				<c:otherwise>   
				日统计
				</c:otherwise> 
			</c:choose> 
			：【${obj.startTime}】至【${obj.endTime}】
			
			<c:if test="${obj.regman ne null}">
				收费人员：
				【${obj.regman}】
			</c:if>
			<c:if test="${groupbean.groupName ne null }">
				所属分组：【${groupbean.groupName }】
			</c:if>
			</td>
 			<td><button class="btn btn-info" type="button" onclick="javascript:exportExcel();">导出报表</button></td>
 		</tr>
	</tbody>
</table>
</p>


<table class="table table-bordered table-hover table-condensed table-striped">
	<thead>
		<tr>
			<th>消费时间</th>
			<th colspan="2">早班(折扣/原价)</th>
			<th colspan="2">午班(折扣/原价)</th>
			<th colspan="2">晚班(折扣/原价)</th>
			<th colspan="2">班次汇总(折扣/原价)</th>
			<th>总金额</th>
		</tr>
	</thead>
	<tbody>
		<c:forEach items="${pageList}" var="p">
 			<tr class="f">
 				<td><c:out value="${p.day}" /></td>
 				<td><c:out value="${p.morningSale}" /></td>
 				<td><c:out value="${p.morningCost}" /></td>
 				<td><c:out value="${p.noonSale}" /></td>
 				<td><c:out value="${p.noonCost}" /></td>
 				<td><c:out value="${p.nightSale}" /></td>
 				<td><c:out value="${p.nightCost}" /></td>
 				<td><c:out value="${p.daySale}" /></td>
 				<td><c:out value="${p.dayCost}" /></td>
 				<td><c:out value="${p.sumFee}" /></td>
 			</tr>
    	</c:forEach>
	</tbody>
</table>
<div class="modal hide fade in" id="myModal" style="display: none;">
  	<div class="modal-header">
    	<a class="close" data-dismiss="modal">×</a>
    	<h3>条件筛选</h3>
  	</div>
  	<div class="modal-body">
	  	<form name="submitform" class="form-inline" action="finSettlement" method="POST" id="submitform">
	  		<div class="control-group">
				<select name="regLib" class="input-medium search-query-extend" >
					<option value="">操作馆</option>
					<c:forEach items="${simpleLibcode}" var="entry">
						<option value="${entry.libCode}">${entry.libCode} | ${entry.simpleName}</option>
					</c:forEach>
				</select>
				<input type="text" name="regman" placeholder="收款人员" value="" class="input-medium search-query-extend" />
			</div>
			<div class="control-group">
			<span>所属分组：</span>
			<select name="groupID" id="groupID" class="input-medium search-query-extend">
				<option value="">无</option>
				<c:forEach items="${groups}" var="gs">
					<c:if test="${groupID==(gs.GROUPID) }">
						<option value="${gs.GROUPID}" selected="selected">${gs.GROUPNAME}</option>
					</c:if>
					<c:if test="${(groupID!=(gs.GROUPID)) }">
						<option value="${gs.GROUPID}">${gs.GROUPNAME}</option>
					</c:if>
				</c:forEach>
			</select>
			</div>
			<div class="control-group">
				<input type="text" id="startTime" name="startTime" value="${obj.startTime}" placeholder="起始时间" readonly class="date form_datetime input-medium">
				<input type="text" id="endTime" name="endTime" value="${obj.endTime}"  placeholder="结束时间" readonly class="date form_datetime input-medium">
			</div>
			<input type="hidden" name="dateFormat" id="dateFormat" value="${obj.dateFormat}"/>
	  	</form>
  	</div>
  	<div class="modal-footer">
    	<a href="#" class="btn" data-dismiss="modal">取消</a>
    	<a href="#" onclick="javascript:submitForm();" class="btn btn-success"><i class="icon-ok icon-white"></i>确定</a>
  	</div>
</div>

<c:set var="pager_base_url">${BASE_URL}/finSettlement?</c:set>
<%@include file="/jsp_tiles/include/admin/pager.jsp" %>
		    </div>
		</div>
	</div>
<table class="table table-bordered table-hover table-condensed table-striped">
	<thead>
		<tr>
			<td>消费总计</td>
			<td><c:out value="${result.morningSale}" /></td>
			<td><c:out value="${result.morningCost}" /></td>
			<td><c:out value="${result.noonSale}" /></td>
			<td><c:out value="${result.noonCost}" /></td>
			<td><c:out value="${result.nightSale}" /></td>
			<td><c:out value="${result.nightCost}" /></td>
			<td><c:out value="${result.daySale}" /></td>
 			<td><c:out value="${result.dayCost}" /></td>
			<td><c:out value="${result.sumFee}" /></td>
	 	</tr>
	</thead>
	<tbody>
		 	<tr>
				<td style="height: 20px;" colspan="10"></td>
		 	</tr>
		 	<tr style="font-size:1em; font-weight: 600;" align="center" valign="middle">
				<td>早餐折扣价数</td>
				<td>早餐原价数</td>
				<td>午餐折扣价数</td>
				<td>午餐原价数</td>
				<td>晚餐折扣价数</td>
				<td>晚餐原价数</td>
				<td>消费总金额</td>
				<td>原价总金额</td>
				<td colspan="2">应补差价</td>
		 	</tr>
		 	<tr>
				<td><c:out value="${repairCost.morningSale}" /></td>
				<td><c:out value="${repairCost.morningCost}" /></td>
				<td><c:out value="${repairCost.noonSale}" /></td>
				<td><c:out value="${repairCost.noonCost}" /></td>
				<td><c:out value="${repairCost.nightSale}" /></td>
				<td><c:out value="${repairCost.nightCost}" /></td>
				<td><c:out value="${repairCost.sumFee}" /></td>
				<td><c:out value="${repairCost.sumCost}" /></td>
				<td colspan="2"><c:out value="${repairCost.sumRepairCost}" /></td>
		 	</tr>
	</tbody>
</table>
</section>
