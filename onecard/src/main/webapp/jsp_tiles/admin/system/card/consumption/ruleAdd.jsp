<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@include file="/jsp_tiles/include/taglibs.jsp"%>
<link rel="stylesheet" href="<c:url value='/media/bootstrap/css/bootstrap-datetimepicker.css' />" type="text/css"/>
<script type="text/javascript" src="<c:url value='/media/bootstrap/plugin/bootstrap-datetimepicker.js' />"></script>
<script type="text/javascript" src="<c:url value='/media/bootstrap/i18n/bootstrap-datetimepicker.zh-CN.js' />"></script>
<ul class="breadcrumb">
	<li><a href="rule">刷卡消费管理</a><span class="divider">/</span></li>
	<li class="active">新增刷卡消费规则</li>
</ul>
<div id="info"></div>
<form class="form-horizontal" action="#" method="post">
	<div class="control-group">
		<label class="control-label" for="ruleTitle">标题</label>
		<div class="controls">
			<input type="text" id="ruleTitle" maxlength="32" />
			<font class="label-required"></font>
		</div>
	</div>
	<div class="control-group">
		<label class="control-label" for="subsidizeTimes">补助餐数</label>
		<div class="controls">
			<input type="text" id="subsidizeTimes" maxlength="32" 
				onkeyup="javascript:clearNoNum(this);"
				onblur="javascript:checkIntNum(this);"/>
			<font class="label-required"></font>
		</div>
	</div>
	<div class="control-group" name="slotdiv1">
		<label class="control-label">消费时段1</label>
		<div class="controls">
			<input type="text" class="input-mini" name="timeslot" readonly="readonly" />&nbsp;~
			<input type="text" class="input-mini" name="timeslot" readonly="readonly" />
			&nbsp;&nbsp;&nbsp;
			<span>折扣价</span>
			<div class="input-append">
				<input class="input-mini" name="price" type="text" 
					onkeyup="javascript:clearNoNum(this);" 
					onblur="javascript:checkNum(this);" maxlength="6" />
				<span class="add-on">元</span>
			</div>
			&nbsp;&nbsp;&nbsp;
			<span>成本价</span>
			<div class="input-append">
				<input class="input-mini" name="price" type="text" 
					onkeyup="javascript:clearNoNum(this);" 
					onblur="javascript:checkNum(this);" maxlength="6">
				<span class="add-on">元</span>
			</div>
			<font class="label-required"></font>
		</div>
	</div>
	<div class="control-group" name="slotdiv2">
		<label class="control-label">消费时段2</label>
		<div class="controls">
			<input type="text" class="input-mini" name="timeslot" readonly="readonly" />&nbsp;~
			<input type="text" class="input-mini" name="timeslot" readonly="readonly" />
			&nbsp;&nbsp;&nbsp;
			<span>折扣价</span>
			<div class="input-append">
				<input class="input-mini" name="price" type="text" 
					onkeyup="javascript:clearNoNum(this);" 
					onblur="javascript:checkNum(this);" maxlength="6" />
				<span class="add-on">元</span>
			</div>
			&nbsp;&nbsp;&nbsp;
			<span>成本价</span>
			<div class="input-append">
				<input class="input-mini" name="price" type="text" 
					onkeyup="javascript:clearNoNum(this);" 
					onblur="javascript:checkNum(this);" maxlength="6">
				<span class="add-on">元</span>
			</div>
			<font class="label-required"></font>
		</div>
	</div>
	<div class="control-group" name="slotdiv3">
		<label class="control-label">消费时段3</label>
		<div class="controls">
			<input type="text" class="input-mini" name="timeslot" readonly="readonly" />&nbsp;~
			<input type="text" class="input-mini" name="timeslot" readonly="readonly" />
			&nbsp;&nbsp;&nbsp;
			<span>折扣价</span>
			<div class="input-append">
				<input class="input-mini" name="price" type="text" 
					onkeyup="javascript:clearNoNum(this);" 
					onblur="javascript:checkNum(this);" maxlength="6" />
				<span class="add-on">元</span>
			</div>
			&nbsp;&nbsp;&nbsp;
			<span>成本价</span>
			<div class="input-append">
				<input class="input-mini" name="price" type="text" 
					onkeyup="javascript:clearNoNum(this);" 
					onblur="javascript:checkNum(this);" maxlength="6">
				<span class="add-on">元</span>
			</div>
			<font class="label-required"></font>
		</div>
	</div>
	<div class="control-group">
		<label class="control-label" for="remark">备注</label>
		<div class="controls">
			<textarea rows="4" id="remark" maxlength="1024"></textarea>
		</div>
	</div>
	<div class="control-group">
		<div class="controls">
			<button type="button" class="btn btn-primary" onclick="javascript:add(this);" id="commitButton">保存</button>
			<button type="button" class="btn btn-inverse" onclick="javascript:document.location.href='rule'";>取消</button>
		</div>
	</div>
</form>
<script type="text/javascript">
	String.prototype.trim = function() {
		return this.replace(/^\s\s*/, '').replace(/\s\s*$/, '');
	};
	
	$("input[name=timeslot]").datetimepicker({
	    format: "hh:ii",
	    language:  'zh-CN',
	    weekStart: 1,
		autoclose: 1,
		todayHighlight: 1,
		startView: 1,
		minView: 0,
		maxView: 0,
		forceParse: 0
	});
	
	function add(commitButton) {
		var info = document.getElementById("info");
		var ruleTitle = document.getElementById("ruleTitle").value.trim();
		if (!ruleTitle) {
			info.innerHTML = "请输入标题！";
			info.className = "alert alert-error";
			return false;
		}
		var subsidizeTimes = document.getElementById("subsidizeTimes").value.trim();
		if(!subsidizeTimes) {
			info.innerHTML = "请输入补助餐数！";
			info.className = "alert alert-error";
			return false;
		}
		if (!checkDetail()) {
			return false;
		}
		var remark = document.getElementById("remark").value;
		var data = {
			ruleTitle: ruleTitle,
			subsidizeTimes: subsidizeTimes,
			remark: remark
		};
		var divs = $("div[name^=slotdiv]");
		for (var i=0,len=divs.length; i<len; i++) {
			var str = "";
			var details = $("div[name=slotdiv"+(i+1)+"] input");
			for (var j = 0,size = details.length; j < size; j++) {
				str += details[j].value;
				if (j < size - 1) {
					str += ",";
				}
			}
			data["details["+i+"]"] = str + "," + (i + 1);
		}
		commitButton.disabled = true;
		$.ajax({
			type : "POST",
			url : "add",
			data : data,
			dataType : "text",
			success : function(backData){
				if (backData == 1) {
					info.innerHTML = "新增成功！";
					info.className = "alert alert-success";
					setTimeout(function(){
						window.location.href = "rule";
					},1000);
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
	
	function checkDetail() {
		var info = document.getElementById("info");
		var objs = $("input[name=timeslot],input[name=price]");
		for (var i=0; i<objs.length; i++) {
			if (objs[i].value == "") {
				info.innerHTML = "请补充完整规则信息！";
				info.className = "alert alert-error";
				return false;
			}
		}
		var slots = $("input[name='timeslot']");
		for (var i=0; i<slots.length/2; i++) {
			if (parseInt(slots[i*2].value.replace(":","")) > parseInt(slots[i*2+1].value.replace(":",""))) {
				info.innerHTML = "开始时间不能大于结束时间！";
				info.className = "alert alert-error";
				return false;
			}
		}
		for (var i=0; i<slots.length-1; i++) {
			if (parseInt(slots[i].value.replace(":","")) > parseInt(slots[i+1].value.replace(":",""))) {
				info.innerHTML = "时间段不能重复，且按顺序设置！";
				info.className = "alert alert-error";
				return false;
			}
		}
		var prices = $("input[name='price']");
		for (var i=0; i<prices.length/2; i++) {
			if (parseFloat(prices[i*2].value) > parseFloat(prices[i*2+1].value)) {
				console.info(prices[i*2].value);
				console.info(prices[i*2+1].value);
				info.innerHTML = "折扣价不能高于成本价！";
				info.className = "alert alert-error";
				return false;
			}
		}
		return true;
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
	function checkIntNum(obj) {
		obj.value = parseInt(obj.value) ? parseInt(obj.value) : "";
	}
</script>