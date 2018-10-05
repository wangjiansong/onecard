<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/jsp_tiles/include/taglibs.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<script type="text/javascript">
	String.prototype.trim = function() {
		return this.replace(/^\s+/g, "").replace(/\s+$/g, "");
	};
	
	$(function(){
		var libcodes = eval('${libcodes}');
		var libCode = document.getElementById("libCode");
		for(var i=0; i<libcodes.length; i++){
			libCode.options.add(new Option(libcodes[i].SHOWLIBTYPE,libcodes[i].LIBCODE));
		}
		document.getElementById("libCode").value = "${readertype.libCode}";
		document.getElementById("sign").value = "${readertype.sign}";
		document.getElementById("libSign").value = "${readertype.libSign}";
		$("#valdate").val("${readertype.valdate}");
		$("#deposity").val("${readertype.deposity}");
		$("#checkfee").val("${readertype.checkfee}");
		$("#idfee").val("${readertype.idfee}");
		$("#servicefee").val("${readertype.servicefee}");
		
	});
	
	function checkForm() {
		var tip = "";
		var readerType = document.getElementById("readerType").value.trim();
		if(readerType == ""){
			tip = "请填写读者类型代码！";
			showResult(tip);
			return;
		}
		var descripe = document.getElementById("descripe").value.trim();
		if(descripe == ""){
			tip = "请填写读者类型名称！";
			showResult(tip);
			return;
		}
		var libCode = document.getElementById("libCode").value;
		if(libCode == ""){
			tip = "请选择分馆代码！";
			showResult(tip);
			return;
		}
		var valdate = document.getElementById("valdate").value;
		if(valdate == "") {
			tip = "请填写读者证有效期限";
			showResult(tip);
			return;
		}
		var sign = document.getElementById("sign").value;
		var libSign = document.getElementById("libSign").value;
		
		var deposity = document.getElementById("deposity").value;
		if(deposity == "") {
			deposity = 0;
		}
		var checkfee = document.getElementById("checkfee").value;
		if(checkfee == "") {
			checkfee = 0;
		}
		var idfee = document.getElementById("idfee").value;
		if(idfee == "") {
			idfee = 0;
		}
		var servicefee = document.getElementById("servicefee").value;
		if(servicefee == "") {
			servicefee = 0;
		}
		
		$.ajax({
			type : "POST",
			url : "<c:url value='/system/reader/readertype/checkReaderTypeEdit' />",
			data : {readerType : readerType, readerType_old : "${readertype.readerType}"},
			dataType : "text",
			success : function(backData){
				if(backData > 0){
					alert("该读者类型已存在！");
					return;
				}else{
					var params = {readerType : readerType,libCode : libCode,sign : sign, 
							libSign : libSign, descripe: descripe, readerType_old : "${readertype.readerType}", 
							valdate: valdate, checkfee:checkfee, idfee:idfee, servicefee:servicefee, deposity:deposity};
					$.ajax({
						type : "POST",
						url : "<c:url value='/system/reader/readertype/editReaderType' />",
						data : params,
						dataType : "text",
						success : function(backData){
							if(backData > 0){
								alert("修改成功！");
								getBack();
							}else{
								alert("修改失败！");
								return;
							}
						},
						error : function(){
							alert("获取数据失败！");
							return;
						}
					});
				}
			},
			error : function(){
				alert("获取数据失败！");
				return;
			}
		});
	}
	
	function showResult(tip) {
		var tip_obj = document.getElementById("tip");
		tip_obj.innerHTML = tip;
		tip_obj.style.display = "inline";
		setTimeout(function(){
			$(tip_obj).fadeOut("slow");
		},3000);
		return;
	}

	function getBack() {
		window.location.href = "<c:url value='/system/reader/readertype/readerTypeIndex' />";
	}
</script>
<section id="tabs">
	<div>
		<ul id="myTab" class="nav nav-tabs">
			<li class="active"><a href="#" data-toggle="tab">读者流通类型 >> 修改</a></li>
		</ul>
		<div id="myTabContent" class="tab-content">
		    <div class="tab-pane active">
<form class="form-horizontal" action="javascript:checkForm();" id="form1" method="post">
	<div class="well form-actions_1">
		<div class="btn-group">
			<button type="submit" class="btn btn-primary">保存</button>
			<button type="reset" class="btn btn-info">重置</button>
			<button type="button" class="btn" onclick="javascript:getBack();">取消</button>
		</div>
		<span id="tip" class="alert" style="margin-left:70px;display: none;"></span>
	</div>
	<div class="container-fluid">
		<div class="span6">
			<div class="control-group">
				<label class="control-label" for="input_rdid">
					读者类型代码
				</label>
				<div class="controls" style="width:200px;">
					<input type="text" id="readerType" name="readerType" class="input-medium" value="${readertype.readerType }" />
					<font class="label-required"></font>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label" for="input_rdid">
					读者类型名称
				</label>
				<div class="controls" style="width:200px;">
					<input type="text" id="descripe" name="descripe" class="input-medium" value="${readertype.descripe }" />
					<font class="label-required"></font>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label" for="input_rdcfstate">
					分馆代码
				</label>
				<div class="controls" style="width:200px;">
					<select id="libCode" name="libCode" class="select-medium">
					</select>
					<font class="label-required"></font>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label" for="input_rdcfstate">
					可用标识
				</label>
				<div class="controls" style="width:200px;">
					<select id="sign" name="sign" class="select-medium">
						<option value="1">可用</option>
						<option value="0">不可用</option>
					</select>
					<font class="label-required"></font>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label" for="input_rdcfstate">
					馆际可用标识
				</label>
				<div class="controls" style="width:200px;">
					<select id="libSign" name="libSign" class="select-medium">
						<option value="1">可用</option>
						<option value="0">不可用</option>
					</select>
					<font class="label-required"></font>
				</div>
			</div>
		</div>
		<div class="span6">
			
			<div class="control-group">
			<label class="control-label" for="input_valdate">
				读者证有效期限
			</label>
			<div class="controls" style="width:200px;">
				<input type="text" id="valdate" name="valdate" class="input-medium" />
				<font class="label-required"></font>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label" for="input_deposity">
				读者证押金
			</label>
			<div class="controls" style="width:200px;">
				<input type="text" id="deposity" name="deposity" class="input-medium" />
			</div>
		</div>
		<div class="control-group">
			<label class="control-label" for="input_checkfee">
				验证费
			</label>
			<div class="controls" style="width:200px;">
				<input type="text" id="checkfee" name="checkfee" class="input-medium" />
			</div>
		</div>
		<div class="control-group">
			<label class="control-label" for="input_servicefee">
				服务费
			</label>
			<div class="controls" style="width:200px;">
				<input type="text" id="servicefee" name="servicefee" class="input-medium" />
			</div>
		</div>
		<div class="control-group">
			<label class="control-label" for="input_idfee">
				工本费
			</label>
			<div class="controls" style="width:200px;">
				<input type="text" id="idfee" name="idfee" class="input-medium" />
			</div>
		</div>
		</div>
	</div>
</form>
			</div>
		</div>
	</div>