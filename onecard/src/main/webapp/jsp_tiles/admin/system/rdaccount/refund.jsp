<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/jsp_tiles/include/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<script type="text/javascript"
	src="<c:url value='/media/datepicker/WdatePicker.js' />"></script>
<c:set var="BASE_URL">
	<c:url value="/admin/sys/charge" />
</c:set>

<script type="text/javascript">
	function pressEnter(e, method) {
		if (e == null) {//ie6..
			e = event;
		}
		if ((e.which == 13 || e.keyCode == 13) && (!(e.ctrlKey || e.metaKey))
				&& !e.shiftKey && !e.altKey) {
			method();
		}
	}
	function checkForm() {
		var searchKey = document.getElementById("searchKey").value;
		var searchValue = document.getElementById("searchValue").value;
		if (searchKey == 'rdid') {
			document.getElementById("rdid").value = searchValue;
		} else if (searchKey == 'rdname') {
			document.getElementById("rdname").value = searchValue;
		}
		document.searchform.submit();
	}
	function statusInfo(status) {
		if(status == 1) return "正常使用";
		if(status == 2) return "冻结";
		if(status == 3) return "未激活";
		if(status == 4) return "注销";
	}
	
	function searchRdAccount() {
		var fieldName = $("#fieldName").val();
		var fieldValue = $("#fieldValue").val();
		if(fieldValue != "") {
			$.ajax({
				type: "GET",
				data : {fieldName : fieldName, fieldValue: fieldValue},
				url: "getRdAccount",
				dataType: "json",
				success: function(jsonData){
					if(jsonData == null) {
						showResult("找不到该帐号信息！");
						return;
					}
					$("#fieldValue").val(jsonData.rdid);
					$("#rdname").val(jsonData.rdname);
					$("#prepay").val(jsonData.prepay);
					$("#status").val(jsonData.status);
					$("#statusName").val(statusInfo(jsonData.status));
				},
				cache: false
			});
		}
	}
	
	function checkform() {
		$("#submit_btn").attr("disabled","true");
		var rdid = $("#fieldValue").val();
		var rdname = $("#rdname").val();
		var refund = $("#refund").val();
		var status = $("#status").val();
		var prepay = $("#prepay").val();
		if(rdid == "") {
			showResult("请先查询读者帐号信息！");
			$("#submit_btn").removeAttr("disabled");
			return ;
		}
		if(refund == "") {
			showResult("请填写退还金额！");
			$("#submit_btn").removeAttr("disabled");
			return ;			
		}
		var p = /^([1-9][\d]{0,7}|0)(\.[\d]{1,2})?$/;
		if(!p.test($('#refund').val())) {
			showResult('请输入正确的金额值') ;
			return;
		} 
		if(Number(refund) > Number(prepay)) {
			$("#refund").val(prepay);
			refund = prepay;
		}
		if(status != 1) {
			showResult("当前账户状态非正常状态！");
			$("#submit_btn").removeAttr("disabled");
			return;
		}
		if (!confirm("确定退款？ " + refund + "元")) {
			$("#submit_btn").removeAttr("disabled");
			return;
		}
		var feeMemo = "";
		if($("#option1").attr("checked")) {
			feeMemo = "退证";
		} 
		if($("#option2").attr("checked")) {
			feeMemo = $("#option2_value").val();
		} 
		
		$.ajax({
			type: "GET",
			url: "doRefund",
			data: {rdid: rdid, rdname:rdname, refund:refund, feeMemo: feeMemo},
			dataType: "json",
			success: function(jsonData){
				var success = jsonData.success;
				var rdid = jsonData.rdid;
				var prepay = jsonData.prepay;
				var status = jsonData.status;
				if(success == 0) {
					var message = jsonData.message;
					showResult(message);
					$("#submit_btn").removeAttr("disabled");
					return;
				} else if(success == 1) {
					$("#prepay").val(jsonData.prepay);
					$("#status").val(jsonData.status);
					$("#statusName").val(statusInfo(jsonData.status));
					$("#refund").val("");
					showResult("退款成功！");
					$("#submit_btn").removeAttr("disabled");
					return;
				}
			},
			cache: false
		});
	}
	
	//提示信息
	function showResult(tip) {
		$("#tip").css("display", "block").html(tip);
		setTimeout(function() {
			$("#tip").fadeOut("slow");
		}, 5000);
		$("#tip").attr("class", "A_MESSAGEBOX_LOADING alert alert-success");
		return;
	}
</script>
<div id="tip" class="A_MESSAGEBOX_LOADING alert alert-success" style="display: none;">
</div>
<section id="tabs">
	<div>
		<ul id="myTab" class="nav nav-tabs">
			<li class="active"><a href="#" data-toggle="tab">退款处理</a></li>
		</ul>
		<div id="myTabContent" class="tab-content">
		    <div class="tab-pane active">


<form id="form1" method="POST" name="submitform" class="form-inline">
	<div class="well form-actions_1">
		<div class="container-fluid">
			<div class="row-fluid">
				<div class="span4"></div>
				<div class="span4">
					<div class="control-group">
						<div class="input-append">
							<select id="fieldName" style="width: 100px;">
								<option value="rdId">读者证号</option>
								<option value="cardNo">卡号</option>
							</select>
							<input type="text" id="fieldValue" name="fieldValue"
								class="input-medium search-query-extend" onkeyup="this.value=this.value.replace(/[\u4e00-\u9fa5]/g,'');"/>
							<button class="btn btn-info" type="button" onclick="searchRdAccount();">查询</button>
						</div>
					</div>
					<div class="control-group">
						<span>读者姓名：</span> 
						<input type="text" id="rdname" name="rdname" class="input-medium search-query-extend" />
					</div>
					<div class="control-group">
						<span>退还金额：</span> 
						<div class="input-append">
						<input type="text" name="refund" id="refund" class="input-medium search-query-extend" />元
						<span class="add-on">元</span>
						</div>
					</div>
					<div class="control-group">
						<span>退款原因：</span> 
						<div class="input-append">
							<label class="radio">
								<input type="radio" name="optionsRadios" id="option1" value="option1" checked >
								1. 退证
							</label>
						</div>
					</div>
					<div class="control-group">
						<div class="input-append">
							<label class="radio">
								<input type="radio" name="optionsRadios" id="option2" value="option2" >
								2. 其他：
							</label>
							<input type="text" id="option2_value" class="input-medium" name="defaultvalue"/>
						</div>
					</div>
					<div class="control-group">
						<span>账户余额：</span> 
						<div class="input-append">
						<input type="text" name="prepay" id="prepay" class="input-medium search-query-extend" disabled="disabled" />元
						<span class="add-on">元</span>
						</div>
					</div>
					<div class="control-group">
						<span>账户状态：</span> 
						<input type="hidden" name="status" id="status" />
						<input type="text" id="statusName" class="input-medium search-query-extend" disabled="disabled" />
					</div>
				</div>
				<div class="span4"></div>
			</div>
			<div class="row-fluid">
				<div class="span4"></div>
				<div class="span4">
					<input type="button" id="submit_btn" class="btn btn-success" onclick="checkform()" value="退款"/>
					<button type="reset" class="btn">重置</button>
				</div>
				<div class="span4"></div>
			</div>
		</div>
		</p>
	</div>
</form>

<c:set var="pager_base_url">${BASE_URL}/list?</c:set>


		    </div>
		</div>
	</div>
</section>