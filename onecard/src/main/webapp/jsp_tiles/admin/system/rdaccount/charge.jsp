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
		var charge = $("#charge").val();
		var status = $("#status").val();
		var payType = $("#payType").val();
		var charType = $("#charType").val();
		if(rdid == "") {
			showResult("请先查询读者帐号信息！");
			$("#submit_btn").removeAttr("disabled");
			return ;
		}
		if(charge == "") {
			showResult("请填写充值金额！");
			$("#submit_btn").removeAttr("disabled");
			return ;			
		}
		var p = /^([1-9][\d]{0,7}|0)(\.[\d]{1,2})?$/;
		if(!p.test($('#charge').val())) {
			showResult('请输入正确的金额值') ;
			return;
		} 
		if(status != 1) {
			showResult("当前账户状态非正常状态！");
			$("#submit_btn").removeAttr("disabled");
			return;
		}
		if(charType == "") {
			showResult("请选择充值类型！");
			$("#submit_btn").removeAttr("disabled");
			return;
		}
		if (!confirm("确定充值？ " + charge + "元")) {
			
			$("#submit_btn").removeAttr("disabled");
			return;
		}
		$.ajax({
			type: "GET",
			url: "doCharge",
			data: {rdid: rdid, rdname:rdname, charge:charge, payType:payType},
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
				}
				else if(success == 1) {
					$("#prepay").val(jsonData.prepay);
					$("#status").val(jsonData.status);
					$("#statusName").val(statusInfo(jsonData.status));
					$("#charge").val("");
					showResult("充值成功！");
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
	
	$(".data").bind("change",function(){
            var dataname = $(this).val();
            var url = "basic/update/updateData?id="+$(this).attr("name");
            $.post(url,{"dataname":dataname},function(rd){
                if(rd.flag){
                    layer.msg("修改成功");
                    window.location.reload();
                }else{
                    layer.alert(rd.msg);
                }
            })
       });
       
       function ch3(){
		var s1 = document.getElementsByName("s1")[0];
			alert('you choice:' + s1.value);
		}
	
</script>
<div id="tip" class="A_MESSAGEBOX_LOADING alert alert-success" style="display: none;">
</div>
<section id="tabs">
	<div>
		<ul id="myTab" class="nav nav-tabs">
			<li class="active"><a href="#" data-toggle="tab">充值处理</a></li>
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
						<span>充值金额：</span> 
						<div class="input-append">
						<input type="text" name="charge" id="charge" class="input-medium search-query-extend" />
						<span class="add-on">元</span>
						</div>
					</div>
					<!-- 针对山西太原图书馆做的充值类型处理 -->
					<%-- <div class="control-group">
						<span>充值类型：</span> 
						<div class="input-append">
                           <select id="chargeType"  name="chargeType" class="input-medium search-query-extend">
							<option value="">请选择</option>
							<c:forEach items="${chargeTypeList}" var="r">
								<option value="${r.feeType}">${r.describe}</option>
							</c:forEach>
						</select>
						</div>
					</div> --%>
					
					<div class="control-group">
						<span>账户余额：</span> 
						<div class="input-append">
						<input type="text" name="prepay" id="prepay" class="input-medium search-query-extend" disabled="disabled" />
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
					<input type="button" id="submit_btn" class="btn btn-success" onclick="checkform()" value="充值"/>
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