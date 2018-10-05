<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/jsp_tiles/include/taglibs.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<script type="text/javascript">
	String.prototype.trim = function() {
		return this.replace(/^\s+/g, "").replace(/\s+$/g, "");
	};

	$(function(){
		$("#workMode").val("${lib.workMode}");
		var defaultRdPasswd = "${lib.defaultRdPasswd}";
		if(defaultRdPasswd != "") {
			if(defaultRdPasswd.indexOf(";") > 0) {
				$("#option3").attr("checked", true);
				var rule = defaultRdPasswd.split(";");
				var column = rule[0];
				var subIndex = rule[1];
				var reversed = rule[2];
				if(reversed == "true") {
					$("#checkbox_value").attr("checked", true);
				}
				$("#" + column).attr("selected", true);
				$("#option3_value").val(subIndex);
			} else {
				$("#option2").attr("checked", true);
				$("#option2_value").val(defaultRdPasswd);
			}
		}
	});
	
	function checkForm() {
		var tip = "";
		var libCode = document.getElementById("libCode").value.trim();
		if(libCode == ""){
			tip = "请填写分馆代码！";
			showResult(tip);
			return;
		}
		var simpleName = document.getElementById("simpleName").value.trim();
		if(simpleName == ""){
			tip = "请填写分馆简称！";
			showResult(tip);
			return;
		}
		var name = document.getElementById("name").value.trim();
		if(name == ""){
			tip = "请填写分馆名称！";
			showResult(tip);
			return;
		}
		var libraryId = document.getElementById("libraryId").value.trim();
		if(libraryId == "") {
			tip = "请填写全局馆编码！";
			showResult(tip);
			return;
		}
		var address = document.getElementById("address").value.trim();

		var retSite = document.getElementById("retSite").value.trim();

		var workMode = document.getElementById("workMode").value;
		
		var defaultRdPasswd = "";
		if($("#option1").attr("checked")) {
			defaultRdPasswd = "";
		} 
		if($("#option2").attr("checked")) {
			defaultRdPasswd = $("#option2_value").val();
		} 
		//如果第三个被选中，则要获取选中的字段，是否反向截取，截取的密码段三个值
		var reversed = "false";
		var column = "";
		if($("#option3").attr("checked")) {
			column = $("#column").val();
			if($("#checkbox_value").attr("checked")) {
				reversed = "true";
			}
			
			defaultRdPasswd = column + ";" + $("#option3_value").val() + ";" + reversed;
			
		}
		var webserviceUrl = document.getElementById("webserviceUrl").value.trim();
		var opacKey = document.getElementById("opacKey").value.trim();
		var acsIp = document.getElementById("acsIp").value.trim();
		var acsPort = document.getElementById("acsPort").value.trim();
		var params = {"libCode" : libCode,"simpleName" : simpleName,"name" : name,
				"address" : address,"retSite" : retSite,"workMode" : workMode, 
				"webserviceUrl" : webserviceUrl, "defaultRdPasswd" : defaultRdPasswd,
				"libraryId" : libraryId, "opacKey": opacKey,
				"acsIp" : acsIp,"acsPort" : acsPort};
		$.ajax({
			type : "POST",
			url : "<c:url value='/system/reader/libcode/editLibCode' />",
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
		window.location.href = "<c:url value='/system/reader/libcode/libCodeIndex' />";
	}
</script>
<section id="tabs">
	<div>
		<ul id="myTab" class="nav nav-tabs">
			<li class="active"><a href="#" data-toggle="tab">分馆管理 >> 修改</a></li>
		</ul>
		<div id="myTabContent" class="tab-content">
		    <div class="tab-pane active">
<form id="libCodeAddForm" class="form-horizontal" action="javascript:checkForm();" method="post">
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
				<label class="control-label" for="input_libCode">
					分馆代码
				</label>
				<div class="controls" style="width:200px;">
					<input type="text" id="libCode" name="libCode" class="input-medium" 
						style="ime-mode:disabled;" onpaste="return false;" readonly="readonly"
						onkeyup="this.value=this.value.replace(/[\u4e00-\u9fa5]/g,'');"
						ondragenter="return false;" oncontextmenu="return false;" value="${lib.libCode }" />
					<font class="label-required"></font>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label" for="input_simpleName">
					分馆简称
				</label>
				<div class="controls" style="width:200px;">
					<input type="text" id="simpleName" name="simpleName" class="input-medium" value="${lib.simpleName }" />
					<font class="label-required"></font>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label" for="input_name">
					分馆名称
				</label>
				<div class="controls" style="width:200px;">
					<input type="text" id="name" name="name" class="input-medium" value="${lib.name }" />
					<font class="label-required"></font>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label" for="input_libraryId">
					全局馆编号
				</label>
				<div class="controls" style="width:200px;">
					<input type="text" id="libraryId" name="libraryId" class="input-medium" value="${lib.libraryId}" />
					<font class="label-required"></font>
				</div>
			</div>
			
			<div class="control-group">
				<label class="control-label">
					读者默认密码
				</label>
				<div class="controls">
					<label class="radio">
						<input type="radio" name="optionsRadios" id="option1" value="option1" checked >1. 不设置默认密码
					</label>
					<label class="radio">
						<input type="radio" name="optionsRadios" id="option2" value="option2" >
						2. 设置默认值</br>
						<input type="text" id="option2_value" class="input-medium" name="defaultvalue" onkeyup="this.value=this.value.replace(/[\u4e00-\u9fa5]/g,'');"/>
					</label>
					<label class="radio">
						<input type="radio" name="optionsRadios" id="option3" value="option3" >
						3. 截取其他字段值：
						<select id="column" name="column" style="width: 165px;">
							<option id="rdid" value="rdid">rdid | 读者证号</option>
							<option id="rdcertify" value="rdcertify">rdcertify | 身份证号</option>
							<option id="rdloginid" value="rdloginid">rdloginid | 手机号</option>
						</select></br>
						<label class="checkbox">
					      	<input type="checkbox" value="reversed" id="checkbox_value"> 是否反向
					    </label>
						截取的密码段：
						<input type="text" id="option3_value" class="input-medium" name="defaultvalue" value="7-14" onkeyup="this.value=this.value.replace(/[\u4e00-\u9fa5]/g,'');"/>
					</label>
				</div>
			</div>
		</div>
		<div class="span6">
			<div class="control-group">
				<label class="control-label" for="input_rdPasswd">
					工作模式
				</label>
				<div class="controls" style="width:200px;">
					<select id="workMode" name="rdtype" class="select-medium">
						<option value="1">验收直接流通</option>
						<option value="2">验收－编目－馆藏</option>
					</select>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label" for="input_address">
					分馆所在地址
				</label>
				<div class="controls" style="width:200px;">
					<input type="text" id="address" name="address" class="input-medium" value="${lib.address }" />
				</div>
			</div>
			<div class="control-group">
				<label class="control-label" for="input_retSite">
					馆际还书指定地点
				</label>
				<div class="controls" style="width:200px;">
					<input type="text" id="retSite" name="retSite" class="input-medium" value="${lib.retSite }" />
				</div>
			</div>
			<div class="control-group">
				<label class="control-label" for="input_acsPort">
					ACS端口
				</label>
				<div class="controls" style="width:200px;">
					<input type="text" id="acsPort" name="acsPort" class="input-medium" value="${lib.acsPort }" placeholder=""/>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label" for="input_acsIp">
					ACS IP
				</label>
				<div class="controls" style="width:200px;">
					<input type="text" id="acsIp" name="acsIp" class="input-medium" value="${lib.acsIp }" placeholder="192.X.X.X"/>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label" for="input_webserviceUrl">
					webservice接口
				</label>
				<div class="controls" style="width:200px;">
					<input type="text" id="webserviceUrl" name="webserviceUrl" class="input-medium" value="${lib.webserviceUrl }" placeholder="http://ip:port/opac"/>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label" for="input_opacKey">
					webservice接口密钥
				</label>
				<div class="controls" style="width:200px;">
					<input type="text" id="opacKey" name="opacKey" class="input-medium" value="${lib.opacKey}" />
				</div>
			</div>
			
		</div>
	</div>
</form>
		    </div>
		</div>
	</div>