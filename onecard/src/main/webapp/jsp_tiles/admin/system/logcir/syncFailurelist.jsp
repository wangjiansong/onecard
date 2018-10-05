<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/jsp_tiles/include/taglibs.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<script type="text/javascript" src="<c:url value='/media/datepicker/WdatePicker.js' />"></script>
<c:set var="LOGCIR_URL">
	<c:url value="/admin/sys/logcir" />
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
	$("#logcir_table tr").click(function(obj) {
		if($(this).find("input[type='checkbox']").attr("checked")) {
			$(this).find("input[type='checkbox']").attr("checked",false);
			$(this).removeClass("info");
		} else {
			$(this).find("input[type='checkbox']").attr("checked",true);
			$(this).addClass("info");
		}
	});	

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
	        chkGroup.addClass("info");
	    } else {
	        chkGroup.attr("checked", false);
	        chkGroup.removeClass("info");
	    }
	});
});
//批量同步的操作
function dobatchSync(){
	var ids = getcheckBoxVals();
	if(ids == null || ids == '') {
		showResult("未选中记录！");
		return;
	}
	$("#synsButton").attr("disabled", "true");
	$("#synsButton").html("正在批同步...");
	$.ajax({
		type : "POST",
		data : {ids : ids},
		url : "${LOGCIR_URL}/syncLogcir/",
		dataType : "json",
		success : function(jsonData){
			var success = jsonData.success;
			var message = jsonData.message;
			if(success == 1) {
				showResult("同步成功!"+message);
				$("#synsButton").removeAttr("disabled");
				$("#synsButton").html("批量同步");
			} else {
				showResult("同步失败！"+message);
				$("#synsButton").removeAttr("disabled");
				$("#synsButton").html("批量同步");
			}
			setTimeout(function(){
				 window.location = "<c:url value='/admin/sys/logcir/syncFailurelist' />";//刷新页面	
			},1000);
		},
		error : function(message){
			showResult("获取接口数据失败！"+message);
			$("#synsButton").removeAttr("disabled");
			$("#synsButton").html("批量同步");
			setTimeout(function(){
				 window.location = "<c:url value='/admin/sys/logcir/syncFailurelist' />";//刷新页面	
			},1000);
			return;
		},
		cache:false
	});
}
function doSingleSync(id) {
	$("#" + id).attr("disabled", "true");
	$("#" + id).html("正在同步...");
	$.ajax({
		type : "POST",
		data : {ids : id},
		url : "${LOGCIR_URL}/syncLogcir/",
		dataType : "json",
		success : function(jsonData){
			var success = jsonData.success;
			var message = jsonData.message;
			if(success == 1) {
				showResult("同步成功!"+message);
				$("#" + id).removeAttr("disabled");
				$("#" + id).html("手动同步");
			} else {
				showResult("同步失败！"+message);
				$("#" + id).removeAttr("disabled");
				$("#" + id).html("手动同步");
			}
			setTimeout(function(){
				 window.location = "<c:url value='/admin/sys/logcir/syncFailurelist' />";//刷新页面	
			},1000);
		},
		error : function(message){
			showResult("获取接口数据失败！"+message);
			$("#synsButton").removeAttr("disabled");
			$("#synsButton").html("手动同步");
			setTimeout(function(){
				 window.location = "<c:url value='/admin/sys/logcir/syncFailurelist' />";//刷新页面	
			},1000);
			return;
		},
		cache:false
	});
}
</script>
<div id="tip" class="A_MESSAGEBOX_LOADING alert alert-success" style="display: none;">
</div>
<form id="dataform" method="post" class="form-inline" action="">
	<div class="well form-actions_1" align="center">
		<input type="hidden" id="transformer" />
		&nbsp;
		<select id="libcode" name="libcode" style="width: 87px;">
			<option value="">操作馆</option>
		</select>
		&nbsp;
		<select id="data2" name="data2" style="width: 87px;">
			<option value="">操作数据1</option>
		</select>
		&nbsp;
		<select id="data4" name="data4" style="width: 87px;">
			<option value="">操作数据2</option>
		</select>
		&nbsp;
		<select id="ordertype" name="ordertype" style="width: 80px;">
			<option value="">排序方式</option>
			<option value="ASC" selected="selected">升序</option>
			<option value="DESC">降序</option>
		</select>
		&nbsp;
		<select id="condition" name="condition" style="width: 87px;">
			<option value="">请选择</option>
			<option value="regtime" selected="selected">操作时间</option>
		</select>
		<input type="text" id="condvalue" name="condvalue" style="width: 130px;" onkeydown="isEnter();" />
		<button type="button" class="btn btn-success" onclick="doSubmit(1);">查询</button>
		<button type="reset" class="btn" onclick="resetDefaultValue();">重置</button>
	</div>
</form>

<div sytle="padding-left:30px;width:100%;">
	<input type="checkbox" class="selAll"  id="rdids"/> 		
	<button type="button" id="synsButton" class="btn btn-info" onclick="dobatchSync();">批量同步</button>
</div>
<table id="logcir_table" class="table table-bordered table-hover table-condensed">
	<thead>
		<tr>
			<th width="5%">选择</th>
			<th width="10%">操作类型</th>
			<th width="10%">操作馆</th>
			<th width="10%">操作员</th>
			<th width="10%">操作数据1</th>
			<th width="10%">操作数据2</th>
			<th width="5%">状态</th>
			<th width="20%">操作时间</th>
			<th width="10%">操作</th>
		</tr>
 	</thead>
	<tbody>
		<c:forEach items="${pageList}" var="v" varStatus="vs">
			<tr class="f">
				<td style="text-align: center;padding-top: 1px;">
					<input type="checkbox" name="checkbox" id="${v.id}" value="${v.id}" /><!-- 选中可以获取对应的读者的账号 -->
				</td>
				<td>${v.logType}</td>
				<td>${v.libcode}</td>
				<td>${v.userId}</td>
				<td>${v.data2}</td>
				<td>${v.data4}</td>
				<td>${v.data5}</td>
				<td><fmt:formatDate value="${v.regtime}"  pattern="yyyy-MM-dd HH:mm:ss" /></td>
				<td style="text-align: center;">
					<div class="btn-group">
					<button class="btn btn-mini btn-info" type="button" id="${v.id}" onclick="javascript:doSingleSync('${v.id}');">手动同步</button>					
					</div>
				</td>
			</tr>
		</c:forEach> 
	</tbody>
</table>
<%@include file="/jsp_tiles/include/admin/paginator.jsp" %>