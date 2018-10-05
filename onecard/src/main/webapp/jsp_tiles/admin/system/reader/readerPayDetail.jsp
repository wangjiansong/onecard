<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/jsp_tiles/include/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<script type="text/javascript" src="<c:url value='/media/datepicker/WdatePicker.js' />"></script>
<c:set var="BASE_URL">
	<c:url value="/admin/sys/readerPay" />
</c:set>
<link rel="stylesheet" href="<c:url value='/media/bootstrap/css/bootstrap-datetimepicker.min.css' />" type="text/css"/>
<script type="text/javascript" src="<c:url value='/media/bootstrap/plugin/bootstrap-datetimepicker.js' />"></script>
<script type="text/javascript" src="<c:url value='/media/bootstrap/i18n/bootstrap-datetimepicker.zh-CN.js' />"></script>
<script type="text/javascript">

String.prototype.trim = function() {
	return this.replace(/^\s\s*/, '').replace(/\s\s*$/, '');
};
$(function(){
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
	        $("#costSum").attr("value", '${allCost}');
	    } else {
	        chkGroup.attr("checked", false);
	        $("#costSum").attr("value", '0.0');
	    }
	});
	
	var rdId = '${reader.rdId}';
	if(rdId == null || rdId == "") {
		$("#payNow").attr("disabled","true");
		$("#payFromOnecard").attr("disabled","true");
		$("#cancelPay").attr("disabled","true");
	}
	var fee = '${allCost}';
	if(fee == null ||fee == "") {
		$("#payNow").attr("disabled","true");
		$("#payFromOnecard").attr("disabled","true");
		$("#cancelPay").attr("disabled","true");
	}
});

function payNow(payAll, rdId) {
	if(!confirm("您确定已经收到读者欠款款项？")) {
		return;
	}
	var tranids = "";
	var totalfee = 0;
	if(payAll == true) {
		$(".f input").each(function () {
			var tranid = $(this).val();
			tranids = tranids + tranid + ";";
			totalfee = '${allCost}';
		});
	} else {
		$(".f input:checked").each(function () {
			var tranid = $(this).val();
			tranids = tranids + tranid + ";";
			totalfee = $("#costSum").val();
		});
	}
	$.ajax({
		type: "POST",
		data : {rdId : rdId, tranids: tranids, paytype: "1", totalfee :totalfee},
		url: "${BASE_URL}/payFee",
		dataType: "json",
		success: function(jsonData){
			if(jsonData == null) {
				showResult("找不到该帐号信息！");
				return;
			}
			var success = jsonData.success;
			if(success == "1") {
				alert("交付成功！");
				window.location.href = "<c:url value='/admin/sys/readerPay/detail/rdId/"+rdId+"' />";
			} else {
				showResult("交付失败！同步失败");
			}
		},
		cache: false
	});
}

function payFromOnecard(payAll, rdId) {
	var prepay = '${rdAccount.prepay}';
	if(prepay == 0.0) {
		showResult("一卡通余额不足！");
		return;
	}
	if(!confirm("您确定从读者一卡通扣除相应欠款？")) {
		return;
	}
	var tranids = "";
	var totalfee;
	if(payAll == true) {
		$(".f input").each(function () {
			var tranid = $(this).val();
			tranids = tranids + tranid + ";";
			totalfee = '${allCost}';
		});
	} else {
		$(".f input:checked").each(function () {
			var tranid = $(this).val();
			tranids = tranids + tranid + ";";
			totalfee = $("#costSum").val();
		});
	}
	if(Number(totalfee) > Number(prepay)) {
		showResult("一卡通余额不足！");
		return;
	}
	
	$.ajax({
		type: "POST",
		data : {rdId : rdId, tranids: tranids, paytype: "2", totalfee :totalfee},
		url: "${BASE_URL}/payFee",
		dataType: "json",
		success: function(jsonData){
			if(jsonData == null) {
				showResult("找不到该帐号信息！");
				return;
			}
			var success = jsonData.success;
			if(success == "1") {
				alert("交付成功！");
				window.location.href = "<c:url value='/admin/sys/readerPay/detail/rdId/"+rdId+"' />";
			} else {
				showResult("交付失败！同步失败");
			}
		},
		cache: false
	});
}

function cancelPay(rdId) {
	if(confirm("您确定取消本次一次罚款？")) {
		
	}
}

function showDetail(id) {
	$("#" + id).show();
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

//提示信息
function showResult(tip, caution) {
	$("#tip").css("display", "block").html(tip);
	setTimeout(function() {
		$("#tip").fadeOut("slow");
	}, 3000);
	$("#tip").attr("class", "A_MESSAGEBOX_LOADING alert alert-" + caution);
	return;
}

function searchReaderPay() {
	var fieldName = $("#fieldName").val();
	var fieldValue = $("#fieldValue").val();
	if(fieldValue == "") {
		showResult("请先输入查询信息！");
		return;
	}
	window.location.href = "<c:url value='/admin/sys/readerPay/detail/"+fieldName+"/"+fieldValue+"' />";
}

function calSum(tranid, cost) {
	var costSum = $("#costSum").val();
	costSum = costSum - 0.0;
	cost = cost - 0.0;
	if ($("#" + tranid).attr("checked")) {
		costSum = costSum + cost;
	} else {
		costSum = costSum - cost;		
	}
	$("#costSum").attr("value", costSum.toFixed(1));
}

</script>

<div id="tip" class="A_MESSAGEBOX_LOADING alert alert-success" style="display: none;">
</div>
<section id="tabs">
	<div>
		<ul id="myTab" class="nav nav-tabs">
			<li class="active"><a href="#" data-toggle="tab">读者欠款信息</a></li>
		</ul>
		<div id="myTabContent" class="tab-content">
		    <div class="tab-pane active">

<fieldset>
	<c:if test="${!empty message}">
		<div class="alert alert-success">
			<a class="close" data-dismiss="alert">&times;</a>
			${message}
		</div>
	</c:if>
	
</fieldset>

<div class="control-group">
	<div class="input-append">
		<select id="fieldName" style="width: 100px;">
			<option value="rdId">读者证号</option>
			<option value="cardNo">卡号</option>
		</select>
		<input type="text" id="fieldValue" name="fieldValue"
			class="input-medium search-query-extend" onkeyup="this.value=this.value.replace(/[\u4e00-\u9fa5]/g,'');"/>
		<button class="btn btn-info" type="button" onclick="searchReaderPay();">查询</button>
	</div>
</div>

<table class="table table-bordered table-hover table-condensed table-striped">
	<tbody>
		<tr class="f">
			<td><p class="text-left">读者证号</p></td> <td><p class="text-left"><c:out value="${reader.rdId}" /></p></td>
		</tr>
		<tr>
			<td><p class="text-left">读者姓名</p></td> <td><p class="text-left"><c:out value="${reader.rdName}" /></p></td>
		</tr>
		<tr>
			<td><p class="text-left">开户馆</p></td> <td><p class="text-left"><c:out value="${reader.rdLib}" /></p></td>
		</tr>
		<tr>
			<td><p class="text-left">一卡通余额</p></td> <td><p class="text-left"><c:out value="${rdAccount.prepay}" />元</p></td>
		</tr>
		<tr>
			<td><p class="text-left" style="font-size:30px">欠款</p></td> <td><p class="text-left" style="color:red; font-size:30px">-<c:out value="${allCost}" />元</p></td>
		</tr>
		<tr>
			<td colspan="2">
				<button onclick="showDetail('payDetail')" class="btn btn-small btn-info">查看欠款详细信息</button>
				
				<div class="btn-group">
					<button id="payNow" onclick="payNow(true, '${reader.rdId}')" class="btn btn-small btn-info">现在交付</button>
					<button id="payFromOnecard" onclick="payFromOnecard(true, '${reader.rdId}')" class="btn btn-small btn-success">从一卡通扣除</button>
				</div>
			</td>
		</tr>
	</tbody>
</table>

<div style="display:none;" id="payDetail">
<p class="text-center" style="color:red;font-size:20px">未交付财经清单</p>
<table class="table table-bordered table-hover table-condensed table-striped">
	<thead>
		<tr>
			<td><input type="checkbox" class="selAll"/></td>
			<td>罚款类型</td>
			<td>金额</td>
 			<td>发生馆</td>
			<td>发生时间</td>
			<td>流水号</td>
	 	</tr>
	</thead>
	<tbody>
		
		<c:forEach items="${financeList}" var="p">
		<tr class="f">
			<td><input type="checkbox" id="${p.tranid}" value="${p.tranid}" onclick="calSum('${p.tranid}', ${p.cost})" /></td>
			<td><c:out value="${p.feetype}" /></td>
			<td><c:out value="${p.cost}" /></td>
			<td><c:out value="${p.reglib}" /></td>
			<td><c:out value="${p.regTimeInStr}" /></td>
			<td><c:out value="${p.tranid}" /></td>
		</tr>
		</c:forEach>
		<tr>
			<div class="input-append">
				<input type="text" id="costSum" value="0.0" style="border-radius: 0px;width: 130px;height:15px;border-left: 0px;" disabled="disabled">
				<button id="payNow" onclick="payNow(false,'${reader.rdId}')" class="btn btn-small btn-info">现在交付</button>
				<a onclick="payFromOnecard(false, '${reader.rdId}')" class="btn btn-small btn-success">从一卡通扣除</a>
			</div>
		</tr>
	</tbody>
</table>
</div>

		    </div>
		</div>
	</div>

</section>