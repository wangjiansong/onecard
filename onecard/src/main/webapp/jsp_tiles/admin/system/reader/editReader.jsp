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
			"RDCFSTATE" : [
				{"value" : "1","text" : "有效"},
				{"value" : "2","text" : "验证"},
				{"value" : "3","text" : "丢失"},
				{"value" : "4","text" : "暂停"},
				{"value" : "5","text" : "注销"}
			]
		},
		{
			"RDSORT1" : [
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
			"RDSORT2" : [
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
			"RDSORT3" : [
				{"value" : "其他","text" : "其他"},
				{"value" : "科级","text" : "科级"},
				{"value" : "处级","text" : "处级"},
				{"value" : "局级","text" : "局级"},
				{"value" : "厅级","text" : "厅级"}
			]
		},
		{
			"RDSORT4" : [
				{"value" : "无","text" : "无"},
				{"value" : "初级","text" : "初级"},
				{"value" : "中级","text" : "中级"},
				{"value" : "副高","text" : "副高"},
				{"value" : "正高","text" : "正高"}
			]
		},
		{
			"RDSORT5" : [
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
		
		var rdGlobal = document.getElementById("rdGlobal");
		rdGlobal.checked = false;
		isGlobalReader(rdGlobal);
		
		var readertypes = eval('${readertypes}');
		var rdType = document.getElementById("rdType");
		for(var i=0; i<readertypes.length; i++){
			rdType.options.add(new Option(readertypes[i].DESCRIPE,readertypes[i].READERTYPE));
		}
		var globaltypes = eval('${globaltypes}');
		var rdLibType = document.getElementById("rdLibType");
		for(var i=0; i<globaltypes.length; i++){
			rdLibType.options.add(new Option(globaltypes[i].DESCRIPE,globaltypes[i].READERTYPE));
		}
		var libcodes = eval('${libcodes}');
		var rdLib = document.getElementById("rdLib");
		for(var i=0; i<libcodes.length; i++){
			rdLib.options.add(new Option(libcodes[i].SIMPLENAME,libcodes[i].LIBCODE));
		}
		
		var rdCFState = document.getElementById("rdCFState");
		var rdSort1 = document.getElementById("rdSort1");
		var rdSort2 = document.getElementById("rdSort2");
		var rdSort3 = document.getElementById("rdSort3");
		var rdSort4 = document.getElementById("rdSort4");
		var rdSort5 = document.getElementById("rdSort5");
		for(var i=0; i<readerJson.length-5; i++){
			var obj;
			switch(i){
				case 0:{
					obj = readerJson[0].RDCFSTATE;
					for(var j=0; j<obj.length; j++){
						rdCFState.options.add(new Option(obj[j].text,obj[j].value));
					}
				}
				case 1:{
					obj = readerJson[1].RDSORT1;
					for(var j=0; j<obj.length; j++){
						rdSort1.options.add(new Option(obj[j].text,obj[j].value));
					}
				}
				case 2:{
					obj = readerJson[2].RDSORT2;
					for(var j=0; j<obj.length; j++){
						rdSort2.options.add(new Option(obj[j].text,obj[j].value));
					}
				}
				case 3:{
					obj = readerJson[3].RDSORT3;
					for(var j=0; j<obj.length; j++){
						rdSort3.options.add(new Option(obj[j].text,obj[j].value));
					}
				}
				case 4:{
					obj = readerJson[4].RDSORT4;
					for(var j=0; j<obj.length; j++){
						rdSort4.options.add(new Option(obj[j].text,obj[j].value));
					}
				}
				case 5:{
					obj = readerJson[5].RDSORT5;
					for(var j=0; j<obj.length; j++){
						rdSort5.options.add(new Option(obj[j].text,obj[j].value));
					}
				}
			}
		}
		initReader();
	});
	
	function initReader() {
		if("${reader.rdGlobal}" == 1){
			document.getElementById("rdGlobal").checked = true;
			document.getElementById("rdLibType").value = "${reader.rdLibType}";
			document.getElementById("rdLibType").disabled = "";
		}else{
			document.getElementById("rdGlobal").checked = false;
		}
		document.getElementById("rdType").value = "${reader.rdType}";
		document.getElementById("rdCFState").value = "${reader.rdCFState}";
		document.getElementById("rdLib").value = "${reader.rdLibCode}";
		document.getElementById("rdSex").value = "${reader.rdSex}";
		document.getElementById("rdSort1").value = "${reader.rdSort1}";
		document.getElementById("rdSort2").value = "${reader.rdSort2}";
		document.getElementById("rdSort3").value = "${reader.rdSort3}";
		document.getElementById("rdSort4").value = "${reader.rdSort4}";
		document.getElementById("rdSort5").value = "${reader.rdSort5}";
	}
	
	function getBack() {
		window.location.href = "<c:url value='/admin/reader/detailReader/${reader.rdId}' />";
	}
</script>
<form class="form-horizontal" action="javascript:void(0);" id="dataform"
	method="post" enctype="multipart/form-data">
	<div class="well form-actions_1">
		<input type="hidden" id="today" />
		<button type="button" class="btn btn-primary" onclick="javascript:editReader();">保存</button>
		&nbsp;
		<button type="button" onclick="getBack();" class="btn btn-inverse">返回</button>
		<span id="tip" class="alert" style="margin-left:70px;display: none;"></span>
	</div>
	<div class="container-fluid" style="padding-right: 40px;">
		<div class="span4">
			<div class="control-group">
				<label class="control-label" for="rdid">
					证号
				</label>
				<div class="controls" style="width:200px;">
					<input type="text" id="rdId" name="rdId" value="${reader.rdId}" class="input-medium" readonly="readonly"
						style="ime-mode:disabled;" onpaste="return false;"
						ondragenter="return false;" oncontextmenu="return false;"
						onkeyup="this.value=this.value.replace(/[\u4e00-\u9fa5]/g,'');" />
					<font class="label-required" title=""></font>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label" for="rdCFState">
					证状态
				</label>
				<div class="controls" style="width:200px;">
					<select id="rdCFState" name="rdCFState" class="select-medium">
					</select>
					<font class="label-required" title=""></font>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label" for="rdType">
					读者类型
				</label>
				<div class="controls" style="width:200px;">
					<select id="rdType" name="rdType" class="select-medium">
					</select>
					<font class="label-required" title=""></font>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label" for="rdGlobal">
					是否馆际读者
				</label>
				<div class="controls" style="width:200px;">
					<input type="checkbox" id="rdGlobal" name="rdGlobal" id="rdGlobal" 
						onclick="isGlobalReader(this);" class="input-medium" />
				</div>
			</div>
			<div class="control-group">
				<label class="control-label" for="rdLibType">
					馆际流通类型
				</label>
				<div class="controls" style="width:200px;">
					<select id="rdLibType" name="rdLibType" class="select-medium">
<!-- 						<option value="999_001" selected="selected">999_001馆际普通读者</option> -->
<!-- 						<option value="999_002">999_002馆际重点读者</option> -->
<!-- 						<option value="999_003">999_003馆际少儿读者</option> -->
<!-- 						<option value="999_004">999_004馆际工作人员</option> -->
<!-- 						<option value="999_005">999_005馆际免费读者</option> -->
<!-- 						<option value="999_006">999_006馆际团体读者</option> -->
<!-- 						<option value="999_007">999_007馆际贵宾A</option> -->
<!-- 						<option value="999_008">999_008馆际贵宾B</option> -->
<!-- 						<option value="999_009">999_009馆际贵宾C</option> -->
<!-- 						<option value="999_010">999_010馆际贵宾D</option> -->
<!-- 						<option value="999_011">999_011乡镇50</option> -->
<!-- 						<option value="999_013">999_013乡镇20</option> -->
<!-- 						<option value="999_014">999_014馆际市民卡读者</option> -->
<!-- 						<option value="999_015">999_015新证(6册)</option> -->
<!-- 						<option value="999_016">999_016新证(6册,100押金)</option> -->
<!-- 						<option value="999_017">999_017免押金10册读者</option> -->
<!-- 						<option value="999_018">999_018交押金10册读者</option> -->
<!-- 						<option value="999_019">999_019交押金10册少儿读者</option> -->
<!-- 						<option value="999_020">999_020免押金10册少儿读者</option> -->
					</select>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label" for="rdStartDate">
					启用日期
				</label>
				<div class="controls" style="width:200px;">
					<input style="width: 150px;" class="Wdate" type="text" id="rdStartDate" name="rdStartDate" value="${reader.rdStartDateStr}"
						onFocus="WdatePicker({isShowClear:false})" />
					<font class="label-required" title=""></font>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label" for="rdEndDate">
					终止日期
				</label>
				<div class="controls" style="width:200px;">
					<input type="text" name="rdEndDate" class="input-medium" id="rdEndDate" readonly="readonly" value="${reader.rdEndDateStr}" />
					<font class="label-required" title=""></font>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label" for="rdInTime">
					办证时间
				</label>
				<div class="controls" style="width:200px;">
					<input type="text" name="rdInTime" class="input-medium" id="rdInTime" readonly="readonly" value="${reader.rdInTimeStr}" />
					<font class="label-required" title=""></font>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label" for="rdLib">
					开户馆
				</label>
				<div class="controls" style="width:200px;">
					<select id="rdLib" name="rdLib" class="select-medium">
					</select>
					<font class="label-required" title=""></font>
				</div>
			</div>
		</div>
		<div class="span4">
			<div class="control-group">
				<label class="control-label" for="rdName">
					姓名
				</label>
				<div class="controls" style="width:200px;">
					<input type="text" name="rdName" class="input-medium" id="rdName" value="${reader.rdName}" />
					<font class="label-required" title=""></font>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label" for="rdCertify">
					身份证号
				</label>
				<div class="controls" style="width:200px;">
					<input type="text" id="rdCertify" name="rdCertify" class="input-medium"
						 value="${reader.rdCertify}"
						style="ime-mode:disabled;" onpaste="return false;"
						ondragenter="return false;" oncontextmenu="return false;"
						onkeyup="this.value=this.value.replace(/[\u4e00-\u9fa5]/g,'');" />
					<font class="label-required" title=""></font>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label" for="rdSex">
					性别
				</label>
				<div class="controls" style="width:200px;">
					<select id="rdSex" name="rdSex" class="select-medium">
						<option value="1">男</option>
						<option value="0">女</option>
					</select>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label" for="rdNation">
					民族
				</label>
				<div class="controls" style="width:200px;">
					<input type="text" name="rdNation" class="input-medium" id="rdNation" value="${reader.rdNation}" />
				</div>
			</div>
			<div class="control-group">
				<label class="control-label" for="rdBornDate">
					出生日期
				</label>
				<div class="controls" style="width:200px;">
					<input style="width: 150px;" class="Wdate" type="text" id="rdBornDate" name="rdBornDate"
						value="${reader.rdBornDateStr}"
						onFocus="WdatePicker({maxDate:document.getElementById('today').value})" />
					<font class="label-required" title=""></font>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label" for="rdLoginId">
					手机
				</label>
				<div class="controls" style="width:200px;">
					<input type="text" name="rdLoginId" class="input-medium" id="rdLoginId" value="${reader.rdLoginId}" />
				</div>
			</div>
			<div class="control-group">
				<label class="control-label" for="rdAddress">
					地址
				</label>
				<div class="controls" style="width:200px;">
					<input type="text" name="rdAddress" class="input-medium" id="rdAddress" value="${reader.rdAddress}" />
				</div>
			</div>
			<div class="control-group">
				<label class="control-label" for="rdNative">
					籍贯
				</label>
				<div class="controls" style="width:200px;">
					<input type="text" name="rdNative" class="input-medium" id="rdNative" value="${reader.rdNative}" />
				</div>
			</div>
			<div class="control-group">
				<label class="control-label" for="rdEmail">
					Email
				</label>
				<div class="controls" style="width:200px;">
					<input type="text" name="rdEmail" class="input-medium" id="rdEmail" value="${reader.rdEmail}" />
				</div>
			</div>
		</div>
		<div class="span4">
			<div class="control-group">
				<label class="control-label" for="rdUnit">
					单位
				</label>
				<div class="controls" style="width:200px;">
					<input type="text" name="rdUnit" class="input-medium" id="rdUnit" value="${reader.rdUnit}" />
				</div>
			</div>
			<div class="control-group">
				<label class="control-label" for="rdPhone">
					电话
				</label>
				<div class="controls" style="width:200px;">
					<input type="text" name="rdPhone" class="input-medium" id="rdPhone" value="${reader.rdPhone}" />
				</div>
			</div>
			<div class="control-group">
				<label class="control-label" for="rdPostCode">
					邮编
				</label>
				<div class="controls" style="width:200px;">
					<input type="text" name="rdPostCode" class="input-medium" id="rdPostCode" value="${reader.rdPostCode}" />
				</div>
			</div>
			<div class="control-group">
				<label class="control-label" for="rdSort1">
					专业
				</label>
				<div class="controls" style="width:200px;">
					<select id="rdSort1" name="rdSort1" class="select-medium">
					</select>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label" for="rdSort2">
					职业
				</label>
				<div class="controls" style="width:200px;">
					<select id="rdSort2" name="rdSort2" class="select-medium">
					</select>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label" for="rdSort3">
					职务
				</label>
				<div class="controls" style="width:200px;">
					<select id="rdSort3" name="rdSort3" class="select-medium">
					</select>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label" for="rdSort4">
					职称
				</label>
				<div class="controls" style="width:200px;">
					<select id="rdSort4" name="rdSort4" class="select-medium">
					</select>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label" for="rdSort5">
					文化
				</label>
				<div class="controls" style="width:200px;">
					<select id="rdSort5" name="rdSort5" class="select-medium">
					</select>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label" for="rdInterest">
					兴趣
				</label>
				<div class="controls">
					<input type="text" name="rdInterest" class="input-medium" id="rdInterest" value="${reader.rdInterest}" />
				</div>
			</div>
		</div>
	</div>
</form>
<script type="text/javascript">
	 //是否馆际读者
	function isGlobalReader(thisObj) {
		var rdLibType = document.getElementById("rdLibType");
		if(thisObj.checked == true){
			rdLibType.disabled = "";
		}else{
			rdLibType.disabled = "disabled";
		}
	}
	
	//检查---保存
	function editReader() {
		var tip = "";
		var rdId = "${reader.rdId}";
		if(!rdId){
			tip = "请填写证号！";
			showResult(tip);
			return;
		}
		var rdCFState = document.getElementById("rdCFState").value;
		var rdType = document.getElementById("rdType").value;
		if(!rdType){
			tip = "请选择读者类型！";
			showResult(tip);
			return;
		}
		var rdGlobalObj = document.getElementById("rdGlobal");
		var rdGlobal;
		var rdLibType;
		if(rdGlobalObj.checked){
			rdGlobal = 1;
			rdLibType = document.getElementById("rdLibType").value;
			if(!rdLibType){
				tip = "请选择馆际流通类型！";
				showResult(tip);
				return;
			}
		}else{
			rdGlobal = 0;
			rdLibType = "";
		}
		var rdStartDate = document.getElementById("rdStartDate").value;
		if(!rdStartDate){
			tip = "请填写启用日期！";
			showResult(tip);
			return;
		}
		var rdEndDate = document.getElementById("rdEndDate").value;
		if(!rdEndDate){
			tip = "请填写终止日期！";
			showResult(tip);
			return;
		}
		var rdInTime = document.getElementById("rdInTime").value;
		if(!rdInTime){
			tip = "请填写办证时间！";
			showResult(tip);
			return;
		}
		var rdLib = document.getElementById("rdLib").value;
		if(!rdLib){
			tip = "请选择开户馆！";
			showResult(tip);
			return;
		}
		var rdSort1 = document.getElementById("rdSort1").value;
		var rdSort2 = document.getElementById("rdSort2").value;
		var rdSort3 = document.getElementById("rdSort3").value;
		var rdSort4 = document.getElementById("rdSort4").value;
		var rdSort5 = document.getElementById("rdSort5").value;
		
		var rdName = document.getElementById("rdName").value.trim();
		if(!rdName){
			tip = "请填写姓名！";
			showResult(tip);
			return;
		}
		var rdCertify = document.getElementById("rdCertify").value.trim();
		if(!rdCertify){
			tip = "请填写身份证号！";
			showResult(tip);
			return;
		}
		var rdSex = document.getElementById("rdSex").value;
		var rdNation = document.getElementById("rdNation").value.trim();
		var rdBornDate = document.getElementById("rdBornDate").value;
		if(!rdBornDate){
			tip = "请填写出生日期！";
			showResult(tip);
			return;
		}
		var rdLoginId = document.getElementById("rdLoginId").value.trim();
		var rdPhone = document.getElementById("rdPhone").value.trim();
		var rdUnit = document.getElementById("rdUnit").value.trim();
		var rdAddress = document.getElementById("rdAddress").value.trim();
		var rdPostCode = document.getElementById("rdPostCode").value.trim();
		var rdEmail = document.getElementById("rdEmail").value.trim();
		var rdNative = document.getElementById("rdNative").value.trim();
		var rdInterest = document.getElementById("rdInterest").value.trim();
		
		var tipobj = document.getElementById("tip");
		var params = {rdId : rdId,rdCFState : rdCFState,rdType : rdType,rdGlobal : rdGlobal,rdLibType : rdLibType,
			rdStartDate : rdStartDate,rdEndDate : rdEndDate,rdInTime : rdInTime,rdLib : rdLib,rdSort1 : rdSort1,
			rdSort2 : rdSort2,rdSort3 : rdSort3,rdSort4 : rdSort4,rdSort5 : rdSort5,rdName : rdName,
			rdCertify : rdCertify,rdSex : rdSex,rdNation : rdNation,rdBornDate : rdBornDate,
			rdLoginId : rdLoginId,rdPhone : rdPhone,rdUnit : rdUnit,rdAddress : rdAddress,
			rdPostCode : rdPostCode,rdEmail : rdEmail,rdNative : rdNative,rdInterest : rdInterest};
		$.ajax({
			type : "POST",
			url : "<c:url value='/admin/reader/editReader' />",
			data : params,
			dataType : "text",
			success : function(backData){
				if(backData > 0){
					tipobj.className = "alert alert-success";
					tipobj.innerHTML = "修改成功！";
					tipobj.style.display = "inline";
					setTimeout(function(){
						window.location.href = "<c:url value='/admin/reader/detailReader/${reader.rdId}' />";
					},1000);
				}else{
					tipobj.className = "alert alert-error";
					tipobj.innerHTML = "修改失败！";
					tipobj.style.display = "inline";
					return;
				}
			},
			error : function(){
				tipobj.className = "alert alert-error";
				tipobj.innerHTML = "获取数据失败！";
				tipobj.style.display = "inline";
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
