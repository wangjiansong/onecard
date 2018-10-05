<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/jsp_tiles/include/taglibs.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<script type="text/javascript" src="<c:url value='/media/datepicker/WdatePicker.js' />"></script>
<script type="text/javascript">
	String.prototype.trim = function() {
		return this.replace(/^\s+/g, "").replace(/\s+$/g, "");
	};

	Date.prototype.format = function(format) {
	    var o = {
	    	"M+" : this.getMonth()+1, //month
		    "d+" : this.getDate(),    //day
		    "h+" : this.getHours(),   //hour
		    "m+" : this.getMinutes(), //minute
		    "s+" : this.getSeconds(), //second
		    "q+" : Math.floor((this.getMonth()+3)/3),  //quarter
		    "S" : this.getMilliseconds() //millisecond
	    };
	    if(/(y+)/.test(format)) format=format.replace(RegExp.$1,
	    (this.getFullYear()+"").substr(4 - RegExp.$1.length));
	    for(var k in o)if(new RegExp("("+ k +")").test(format))
	    format = format.replace(RegExp.$1,
	    RegExp.$1.length==1 ? o[k] :
	    ("00"+ o[k]).substr((""+ o[k]).length));
	    return format;
	};
	
	var readerJson = [
		{
			"READERCARDSTATE" : [
				{"value" : "1","text" : "有效"},
				{"value" : "2","text" : "验证"},
				{"value" : "3","text" : "丢失"},
				{"value" : "4","text" : "暂停"},
				{"value" : "5","text" : "注销"}
			]
		},
		{
			"READERSORT1" : [
				{"value" : "其他","text" : "其他"},
				{"value" : "物理","text" : "物理"},
				{"value" : "化学","text" : "化学"},
				{"value" : "中文","text" : "中文"},
				{"value" : "数学","text" : "数学"},
				{"value" : "历史","text" : "历史"},
				{"value" : "经济","text" : "经济"},
				{"value" : "管理","text" : "管理"},
				{"value" : "计算机","text" : "计算机"}
			]
		},
		{
			"READERSORT2" : [
				{"value" : "其他","text" : "其他"},
				{"value" : "公务员","text" : "公务员"},
				{"value" : "科研人员","text" : "科研人员"},
				{"value" : "教师","text" : "教师"},
				{"value" : "文化工作者","text" : "文化工作者"},
				{"value" : "工人","text" : "工人"},
				{"value" : "军人","text" : "军人"},
				{"value" : "义务工作者","text" : "义务工作者"},
				{"value" : "公司职员","text" : "公司职员"},
				{"value" : "学生","text" : "学生"},
				{"value" : "个体劳动者","text" : "个体劳动者"}
			]
		},
		{
			"READERSORT3" : [
				{"value" : "其他","text" : "其他"},
				{"value" : "科级","text" : "科级"},
				{"value" : "处级","text" : "处级"},
				{"value" : "局级","text" : "局级"},
				{"value" : "厅级","text" : "厅级"}
			]
		},
		{
			"READERSORT4" : [
				{"value" : "无","text" : "无"},
				{"value" : "初级","text" : "初级"},
				{"value" : "中级","text" : "中级"},
				{"value" : "副高","text" : "副高"},
				{"value" : "正高","text" : "正高"}
			]
		},
		{
			"READERSORT5" : [
				{"value" : "其他","text" : "其他"},
				{"value" : "高中","text" : "高中"},
				{"value" : "中专","text" : "中专"},
				{"value" : "大专","text" : "大专"},
				{"value" : "本科","text" : "本科"},
				{"value" : "研究生","text" : "研究生"},
				{"value" : "博士生","text" : "博士生"}
			]
		}
	];

	$(function(){
		var today = new Date().format("yyyy-MM-dd");
		document.getElementById("today").value = today;
		document.getElementById("readerHandleDate").value = today;
		document.getElementById("readerEndDate").value = today;
		
		var readertypes = eval('${readertypes}');
		var readerType = document.getElementById("readerType");
		for(var i=0; i<readertypes.length; i++){
			readerType.options.add(new Option(readertypes[i].DESCRIPE,readertypes[i].READERTYPE));
		}
		var libcodes = eval('${libcodes}');
		var readerLib = document.getElementById("readerLib");
		for(var i=0; i<libcodes.length; i++){
			readerLib.options.add(new Option(libcodes[i].SIMPLENAME,libcodes[i].LIBCODE));
		}
		
		var readerCardState = document.getElementById("readerCardState");
		var readerSort1 = document.getElementById("readerSort1");
		var readerSort2 = document.getElementById("readerSort2");
		var readerSort3 = document.getElementById("readerSort3");
		var readerSort4 = document.getElementById("readerSort4");
		var readerSort5 = document.getElementById("readerSort5");
		for(var i=0; i<readerJson.length-5; i++){
			var obj;
			switch(i){
				case 0:{
					obj = readerJson[0].READERCARDSTATE;
					for(var j=0; j<obj.length; j++){
						readerCardState.options.add(new Option(obj[j].text,obj[j].value));
					}
				}
				case 1:{
					obj = readerJson[1].READERSORT1;
					for(var j=0; j<obj.length; j++){
						readerSort1.options.add(new Option(obj[j].text,obj[j].value));
					}
				}
				case 2:{
					obj = readerJson[2].READERSORT2;
					for(var j=0; j<obj.length; j++){
						readerSort2.options.add(new Option(obj[j].text,obj[j].value));
					}
				}
				case 3:{
					obj = readerJson[3].READERSORT3;
					for(var j=0; j<obj.length; j++){
						readerSort3.options.add(new Option(obj[j].text,obj[j].value));
					}
				}
				case 4:{
					obj = readerJson[4].READERSORT4;
					for(var j=0; j<obj.length; j++){
						readerSort4.options.add(new Option(obj[j].text,obj[j].value));
					}
				}
				case 5:{
					obj = readerJson[5].READERSORT5;
					for(var j=0; j<obj.length; j++){
						readerSort5.options.add(new Option(obj[j].text,obj[j].value));
					}
				}
			}
		}
	});
	
	function resetDefaultValue() {
		var today = new Date().format("yyyy-MM-dd");
		setTimeout(function(){
			document.getElementById("readerHandleDate").value = today;
			document.getElementById("readerEndDate").value = today;
		},1);
	}
</script>
<form class="form-horizontal" action="javascript:void(0);" id="dataform"
	method="post" enctype="multipart/form-data">
	<div class="well form-actions_1">
		<input type="hidden" id="today" />
		<button type="button" id="submitButton" onclick="javascript:registReader();" class="btn btn-primary">提交</button>&nbsp;
		<button type="reset" onclick="javascript:resetDefaultValue();" class="btn btn-inverse">重置</button>
		<span id="tip" class="alert alert-error" style="margin-left:70px;display: none;"></span>
	</div>
	<div class="container-fluid" style="padding-right: 40px;">
		<div class="span4">
			<div class="control-group">
				<label class="control-label" for="readerId">
					证号
				</label>
				<div class="controls" style="width:200px;">
					<input type="text" id="readerId" name="readerId" class="input-medium"
						style="ime-mode:disabled;" onpaste="return false;"
						ondragenter="return false;" oncontextmenu="return false;"
						onkeyup="this.value=this.value.replace(/[\u4e00-\u9fa5]/g,'');" />
					<font class="label-required" title=""></font>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label" for="readerCardState">
					证状态
				</label>
				<div class="controls" style="width:200px;">
					<select id="readerCardState" name="readerCardState" class="select-medium">
					</select>
					<font class="label-required" title=""></font>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label" for="readerType">
					读者类型
				</label>
				<div class="controls" style="width:200px;">
					<select id="readerType" name="readerType" class="select-medium">
					</select>
					<font class="label-required" title=""></font>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label" for="readerLib">
					开户馆
				</label>
				<div class="controls" style="width:200px;">
					<select id="readerLib" name="readerLib" class="select-medium">
					</select>
					<font class="label-required" title=""></font>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label" for="rdStartDate">
					启用日期
				</label>
				<div class="controls" style="width:200px;">
					<input style="width: 150px;" class="Wdate" type="text" id="readerStartDate" name="readerStartDate" 
						onFocus="WdatePicker({isShowClear:false})" />
					<font class="label-required" title=""></font>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label" for="readerEndDate">
					终止日期
				</label>
				<div class="controls" style="width:200px;">
					<input type="text" id="readerEndDate" name="readerEndDate" class="input-medium" readonly="readonly" />
					<font class="label-required" title=""></font>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label" for="readerHandleDate">
					办证时间
				</label>
				<div class="controls" style="width:200px;">
					<input type="text" id="readerHandleDate" name="readerHandleDate" class="input-medium" readonly="readonly" />
					<font class="label-required" title=""></font>
				</div>
			</div>
		</div>
		<div class="span4">
			<div class="control-group">
				<label class="control-label" for="readerName">
					姓名
				</label>
				<div class="controls" style="width:200px;">
					<input type="text" id="readerName" name="readerName" class="input-medium" />
					<font class="label-required" title=""></font>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label" for="readerPassword">
					密码
				</label>
				<div class="controls" style="width:200px;">
					<input type="password" id="readerPassword" name="readerPassword" class="input-medium" />
					<font class="label-required" title=""></font>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label" for="readerCertify">
					身份证号
				</label>
				<div class="controls" style="width:200px;">
					<input type="text" id="readerCertify" name="readerCertify" class="input-medium"
						style="ime-mode:disabled;" onpaste="return false;"
						ondragenter="return false;" oncontextmenu="return false;"
						onkeyup="this.value=this.value.replace(/[\u4e00-\u9fa5]/g,'');" />
					<font class="label-required" title=""></font>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label" for="readerGender">
					性别
				</label>
				<div class="controls" style="width:200px;">
					<select id="readerGender" name="readerGender" class="select-medium">
						<option value="1">男</option>
						<option value="2">女</option>
					</select>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label" for="readerMobile">
					手机
				</label>
				<div class="controls" style="width:200px;">
					<input type="text" id="readerMobile" name="readerMobile" class="input-medium" />
				</div>
			</div>
			<div class="control-group">
				<label class="control-label" for="readerNative">
					籍贯
				</label>
				<div class="controls" style="width:200px;">
					<input type="text" id="readerNative" name="readerNative" class="input-medium" />
				</div>
			</div>
			<div class="control-group">
				<label class="control-label" for="readerAddress">
					地址
				</label>
				<div class="controls" style="width:200px;">
					<input type="text" id="readerAddress" name="readerAddress" class="input-medium" />
				</div>
			</div>
		</div>
		<div class="span4">
			<div class="control-group">
				<label class="control-label" for="readerEmail">
					Email
				</label>
				<div class="controls" style="width:200px;">
					<input type="text" id="readerEmail" name="readerEmail" class="input-medium" />
				</div>
			</div>
			<div class="control-group">
				<label class="control-label" for="readerSort1">
					专业
				</label>
				<div class="controls" style="width:200px;">
					<select id="readerSort1" name="readerSort1" class="select-medium">
					</select>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label" for="readerSort2">
					职业
				</label>
				<div class="controls" style="width:200px;">
					<select id="readerSort2" name="readerSort2" class="select-medium">
					</select>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label" for="readerSort3">
					职务
				</label>
				<div class="controls" style="width:200px;">
					<select id="readerSort3" name="readerSort3" class="select-medium">
					</select>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label" for="readerSort4">
					职称
				</label>
				<div class="controls" style="width:200px;">
					<select id="readerSort4" name="readerSort4" class="select-medium">
					</select>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label" for="readerSort5">
					文化
				</label>
				<div class="controls" style="width:200px;">
					<select id="readerSort5" name="readerSort5" class="select-medium">
					</select>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label" for="remark">
					备注
				</label>
				<div class="controls">
					<input type="text" id="remark" name="remark" class="input-medium" />
				</div>
			</div>
		</div>
	</div>
</form>
<script type="text/javascript">
	//检查---提交
	function registReader() {
		var tip = "";
		var readerId = document.getElementById("readerId").value.trim();
		if(!readerId){
			tip = "请填写证号！";
			showResult(tip);
			return;
		}
		var readerCardState = document.getElementById("readerCardState").value;
		var readerType = document.getElementById("readerType").value;
		if(!readerType){
			tip = "请选择读者类型！";
			showResult(tip);
			return;
		}
		var readerLib = document.getElementById("readerLib").value;
		if(!readerLib){
			tip = "请选择开户馆！";
			showResult(tip);
			return;
		}
		var readerStartDate = document.getElementById("readerStartDate").value;
		if(!readerStartDate){
			tip = "请填写启用日期！";
			showResult(tip);
			return;
		}
		var readerEndDate = document.getElementById("readerEndDate").value;
		if(!readerEndDate){
			tip = "请填写终止日期！";
			showResult(tip);
			return;
		}
		var readerHandleDate = document.getElementById("readerHandleDate").value;
		if(!readerHandleDate){
			tip = "请填写办证时间！";
			showResult(tip);
			return;
		}
		var readerName = document.getElementById("readerName").value.trim();
		if(!readerName){
			tip = "请填写姓名！";
			showResult(tip);
			return;
		}
		var readerPassword = document.getElementById("readerPassword").value;
		if(!readerPassword){
			tip = "请填写密码！";
			showResult(tip);
			return;
		}
		if(readerPassword.length < 6){
			tip = "密码不能少于6位！";
			showResult(tip);
			return;
		}
		var readerCertify = document.getElementById("readerCertify").value.trim();
		if(!readerCertify){
			tip = "请填写身份证号！";
			showResult(tip);
			return;
		}
		var readerGender = document.getElementById("readerGender").value;
		var readerMobile = document.getElementById("readerMobile").value;
		var readerNative = document.getElementById("readerNative").value;
		var readerAddress = document.getElementById("readerAddress").value;
		
		var readerEmail = document.getElementById("readerEmail").value;
		var readerSort1 = document.getElementById("readerSort1").value;
		var readerSort2 = document.getElementById("readerSort2").value;
		var readerSort3 = document.getElementById("readerSort3").value;
		var readerSort4 = document.getElementById("readerSort4").value;
		var readerSort5 = document.getElementById("readerSort5").value;
		var remark = document.getElementById("remark").value;
		
		var tip_obj = document.getElementById("tip");
		var submitButton = document.getElementById("submitButton");
		submitButton.disabled = "disabled";
		$.ajax({
			type : "POST",
			url : "checkReaderIdIsExist",
			data : {readerId : readerId},
			dataType : "text",
			success : function(backData){
				if(backData > 0){
					submitButton.disabled = "";
					tip = "此证号已存在或已被注册，请重新填写！";
					showResult(tip);
					return;
				}else{
					var params = {readerId : readerId,
						readerCardState : readerCardState,
						readerType : readerType,
						readerLib : readerLib,
						readerStartDate : readerStartDate,
						readerEndDate : readerEndDate,
						readerHandleDate : readerHandleDate,
						
						readerName : readerName,
						readerPassword : readerPassword,
						readerCertify : readerCertify,
						readerGender : readerGender,
						readerMobile : readerMobile,
						readerNative : readerNative,
						readerAddress : readerAddress,
						
						readerEmail : readerEmail,
						readerSort1 : readerSort1,
						readerSort2 : readerSort2,
						readerSort3 : readerSort3,
						readerSort4 : readerSort4,
						readerSort5 : readerSort5,
						remark : remark
					};
					$.ajax({
						type : "POST",
						url : "registReader",
						data : params,
						dataType : "text",
						success : function(backData){
							if(backData > 0){
								submitButton.disabled = "disabled";
								tip_obj.className = "alert alert-success";
								tip_obj.innerHTML = "注册成功，请待管理员审批！";
								tip_obj.style.display = "inline";
								setTimeout(function(){
									window.location.href = "<c:url value='/admin/netreader/regist' />";
								},2000);
							}else{
								tip_obj.style.display = "inline";
								submitButton.disabled = "";
								return;
							}
						},
						error : function(){
							submitButton.disabled = "";
							alert("获取数据失败！");
							return;
						}
					});
				}
			},
			error : function(){
				submitButton.disabled = "";
				alert("获取数据失败！");
				return;
			}
		});
	}
	
	//提示信息
	function showResult(tip) {
		var tip_obj = document.getElementById("tip");
		tip_obj.innerHTML = tip;
		tip_obj.style.display = "inline";
		setTimeout(function(){
			$(tip_obj).fadeOut("slow");
		},3000);
		return;
	}
</script>
