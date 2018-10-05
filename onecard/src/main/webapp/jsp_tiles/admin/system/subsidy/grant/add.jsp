<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<link rel="stylesheet" href="<c:url value='/media/bootstrap/css/bootstrap-datetimepicker.css' />" type="text/css"/>
<script type="text/javascript" src="<c:url value='/media/bootstrap/plugin/bootstrap-datetimepicker.js' />"></script>
<script type="text/javascript" src="<c:url value='/media/bootstrap/i18n/bootstrap-datetimepicker.zh-CN.js' />"></script>
<c:set var="base_url">
	<c:url value="/admin/subsidy" />
</c:set>
<ul class="breadcrumb">
	<li><a href="${base_url}/grant">补助发放</a><span class="divider">/</span></li>
	<li class="active">新增补助发放</li>
</ul>
<div id="info"></div>
<form class="form-horizontal" action="#" method="post">
	<div class="control-group">
		<label class="control-label" for="grantTitle">标题</label>
		<div class="controls">
			<input type="text" id="grantTitle" maxlength="32" />
			<font class="label-required"></font>
		</div>
	</div>
	<div class="control-group">
		<label class="control-label" for="grantAmount">发放金额</label>
		<div class="controls">
			<input type="text" id="grantAmount" onkeyup="javascript: clearNoNum(this);" onblur="javascript: checkNum(this);" maxlength="12" />
			<font class="label-required"></font>
		</div>
	</div>
	<div class="control-group">
		<label class="control-label" for="grantDate">发放日期</label>
		<div class="controls">
			<select id="grantDate" multiple="multiple">
			</select>
			<font class="label-required"></font>
			<span class="add-on"><i class="icon-plus"></i></span>
			<span class="add-on"><i class="icon-minus" onclick="javascript: removeDate();"></i></span>
		</div>
	</div>
	<div class="control-group">
		<label class="control-label" for="grantAmount">是否自动发放</label>
		<div class="controls">
			<select id="isAutoGrant">
				<option value="1">是</option>
				<option value="0">否</option>
			</select>
			<font class="label-required"></font>
		</div>
	</div>
	<div class="control-group">
		<label class="control-label" for="grantAmount">备注</label>
		<div class="controls">
			<textarea rows="4" id="remark"></textarea>
		</div>
	</div>
	<div class="control-group">
		<div class="controls">
			<button type="button" class="btn btn-primary" onclick="javascript:add(this);" id="commitButton">保存</button>
			<button type="button" class="btn btn-inverse" onclick="javascript:document.location.href='${base_url}/grant'";>取消</button>
		</div>
	</div>
</form>
<script type="text/javascript">
	String.prototype.trim = function() {
		return this.replace(/^\s\s*/, '').replace(/\s\s*$/, '');
	};
	
	$('.icon-plus').datetimepicker({
	    format: "yyyy-mm-dd",
	    language:  'zh-CN',
	    startDate: new Date(),
	    weekStart: 1,
	    todayBtn:  1,
		autoclose: true,
		todayHighlight: 1,
		startView: "month",
		minView: 2,
		forceParse: 0
	}).on('changeDate', function(event){
		var date = event.date;
		var grantDate = document.getElementById("grantDate");
		var formatDate = date.getUTCFullYear()+"-"+((date.getUTCMonth()+1) < 10 ? "0"+(date.getUTCMonth()+1) : (date.getUTCMonth()+1))+"-"+(date.getUTCDate() < 10 ? "0"+date.getUTCDate() : date.getUTCDate());
		var option = new Option(formatDate,formatDate);
		var size = grantDate.length;
		var flag = false;
		for (var i=0; i<size; i++) {
			if (grantDate[i].value == formatDate) {
				flag = true;
			}
			if (size) {
				grantDate[i].selected = "";
			}
		}
		if (!flag) {
			grantDate.add(option);
			size = grantDate.length;
		}
		if (size > 0 && size <4) {
			grantDate.size = 4;
		} else {
			grantDate.size = size;
		}
		grantDate[size-1].selected = true;
	});
	
	function add(commitButton) {
		var info = document.getElementById("info");
		var grantTitle = document.getElementById("grantTitle").value.trim();
		if (!grantTitle) {
			info.innerHTML = "请输入标题！";
			info.className = "alert alert-error";
			return false;
		}
		var grantAmount = document.getElementById("grantAmount").value;
		if (!grantAmount) {
			info.innerHTML = "请输入发放金额，只能输入数字且最多保留两位小数！";
			info.className = "alert alert-error";
			return false;
		}
		var grantDate = document.getElementById("grantDate");
		if (!grantDate.length) {
			info.innerHTML = "请选择发放日期！";
			info.className = "alert alert-error";
			return false;
		}
		var isAutoGrant = document.getElementById("isAutoGrant").value;
		var remark = document.getElementById("remark").value;
		var data = {
			grantTitle: grantTitle,
			grantAmount: grantAmount,
			isAutoGrant: isAutoGrant,
			remark: remark
		};
		for (var i=0,size=grantDate.length; i<size; i++) {
			data["grantDateList["+i+"]"] = grantDate[i].value;
		}
		commitButton.disabled = true;
		$.ajax({
			type : "POST",
			url : "${base_url}/grant/add",
			data : data,
			dataType : "text",
			success : function(backData){
				if (backData == 1) {
					info.innerHTML = "新增成功！";
					info.className = "alert alert-success";
					setTimeout(function(){
						window.location.href = "${base_url}/grant";
					},1500);
					return false;
				} else {
					info.innerHTML = "新增失败！";
					info.className = "alert alert-error";
					commitButton.disabled = false;
					return false;
				}
			},
			error : function(){
				info.innerHTML = "获取数据失败！";
				info.className = "alert alert-error";
				commitButton.disabled = false;
				return false;
			}
		});
	}
	
	function removeDate() {
		var dates = document.getElementById("grantDate");
		var size = dates.length;
		var isIE = navigator.userAgent.toUpperCase().indexOf("MSIE") == -1 ? false : true; 
		if (isIE) {
			for (var i=0; i<size; i++) {
				if (dates.options[size-1-i].selected) {
					dates.options[size-1-i].removeNode();
				}
			}
		} else {
			for (var i=0; i<size; i++) {
				if (dates.options[size-1-i].selected) {
					dates.options[size-1-i].remove();
				}
			}
		}
		size = dates.length;
		if (size > 0 && size < 4) {
			dates.options[size-1].selected = true;
			dates.size = 4;
		} else if (size >= 4) {
			dates.options[size-1].selected = true;
			dates.size = size;
		} else {
			dates.size = 4;
		}
	}
	
	function clearNoNum(obj) {
		obj.value = obj.value.replace(/[^\d.]/g,"");
		obj.value = obj.value.replace(/^\./g,"");
		obj.value = obj.value.replace(/\.{2,}/g,".");
		obj.value = obj.value.replace(".","$#$").replace(/\./g,"").replace("$#$",".");
		obj.value = obj.value.replace(/^(\-)*(\d+)\.(\d\d).*$/,'$1$2.$3');
	}
	
	function checkNum(obj) {
		obj.value = parseFloat(obj.value) ? parseFloat(obj.value) : "";
	}
</script>