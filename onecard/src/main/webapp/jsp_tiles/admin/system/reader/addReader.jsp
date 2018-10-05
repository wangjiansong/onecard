<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/jsp_tiles/include/taglibs.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<c:set var="READER_URL">
	<c:url value="/admin/reader" />
</c:set>
<script type="text/javascript" src="<c:url value='/media/datepicker/WdatePicker.js' />"></script>
<script type="text/javascript">
	var ACTION = "ADD";

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
		    "q+" : Math.floor((this.getMonth()+3)/3),  
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
				{"value" : "5","text" : "注销"},
				{"value" : "6","text" : "信用有效"}
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
		document.getElementById("rdEndDate").value = "${rdEndDate}";
		document.getElementById("rdInTime").value = today;
		document.getElementById("rdStartDate").value = today;
		
		var rdGlobal = document.getElementById("rdGlobal");
		rdGlobal.checked = false;
		isGlobalReader(rdGlobal);
		
		var readertypes = eval('${readertypes}');
		if(readertypes.length){
			var rdType = document.getElementById("rdType");
			for(var i=0; i<readertypes.length; i++){
				rdType.options.add(new Option(readertypes[i].SHOWREADERTYPE,readertypes[i].READERTYPE));
			}
		}
		var globaltypes = eval('${globaltypes}');
		var rdLibType = document.getElementById("rdLibType");
		for(var i=0; i<globaltypes.length; i++){
			rdLibType.options.add(new Option(globaltypes[i].SHOWREADERTYPE,globaltypes[i].READERTYPE));
		}
		var libcodes = eval('${libcodes}');
		var rdLib = document.getElementById("rdLib");
		for(var i=0; i<libcodes.length; i++){
			rdLib.options.add(new Option(libcodes[i].SHOWLIBTYPE,libcodes[i].LIBCODE));
		}
		var defaultLib = "${READER_SESSION.reader.rdLibCode}";
		var defaultLibName = "${READER_SESSION.reader.rdLib}";
		if(defaultLib && defaultLibName){
			rdLib.value = defaultLib;
		}
		//刷卡消费分组
		var group = document.getElementById("group");
		var groups = eval('${groups}');
		if (groups) {
			for (var i = 0, length = groups.length; i < length; i++) {
				group.add(new Option(groups[i].groupName, groups[i].groupId));
			}
		}
// 		var json_url = "<c:url value='/jsp_tiles/admin/system/reader/reader.json' />";
// 		$.getJSON(
// 			json_url,
// 			function(data){
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
				var temp = "${Detail_to_Edit}";
				if(temp && temp == "why_not"){
					var reader = eval('${reader}')[0];
					if(reader==undefined)return;
					document.getElementById("fieldValue").value = reader.rdId;
					initReader(reader, false);
				}
// 			}
// 		);
	});
	
	$(function () {
		$('#otherRdid').change(function(){
			var rdid = $('#otherRdid option:selected').attr('value');
            if(rdid != ''){
            	window.location.href = "<c:url value='/admin/reader/add/"+rdid+"' />";
            }
        }); 
	});
</script>
<form class="form-horizontal" id="dataform" action="javascript:searchReader();" name="dataform" 
	onkeydown="if(event.keyCode==13){return false;}"
	method="post" enctype="multipart/form-data" target="hidden_frame">
	<div class="well form-actions_1" id="buttonDiv" style="padding-left: 144px;">
		<div class="btn-group" style="margin-left: 5px;">
			<button class="btn btn btn-success" id="cardOp">证操作</button>
			<button class="btn btn btn-success dropdown-toggle" data-toggle="dropdown" id="cardDrop">
	    		<span class="caret"></span>
	  		</button>
			<ul class="dropdown-menu">
				<li><a href="#" onclick="renew()">恢复</a></li>
				<li><a href="#" onclick="check()">验证</a></li>
				<li><a href="#" onclick="cardOperation('loss')">挂失</a></li>
				<li><a href="#" onclick="cardOperation('stop')">暂停</a></li>
				<li><a href="#" onclick="quit()">退证</a></li>
				<li class="dropdown-submenu">
					<a tabindex="-1" href="#">补办</a>
					<ul class="dropdown-menu">
				    	<li><a href="#" onclick="cardRepair();">卡补办</a></li>
						<li><a href="#" onclick="repair()">证补办</a></li>
				    </ul>
				</li>
				<li><a href="#" onclick="defer()">延期</a></li>
				<li><a href="#" onclick="change()">换证</a></li>
				<li><a href="#" onclick="rdLogout()">注销</a></li>
			</ul>
		</div>
		<div class="input-append">
			<select id="fieldName" style="width: 123px;">
				<option value="rdId">读者证号</option>
				<option value="cardId">卡号</option>
				<option value="rdName">读者姓名</option>
				<option value="rdCertify">读者身份证号</option>
			</select>
			<input type="text" id="fieldValue" style="border-radius: 0px;width: 130px;border-left: 0px;" onkeydown="isEnter();">
			<button class="btn btn-info" type="button" onclick="javascript:searchReader();">查询</button>
		</div>
		<input type="hidden" id="today" />
		<button type="button" class="btn btn-info" id="syncButton" onclick="javascript:syncReader();" style="margin-left: 5px;">手动同步</button>
		<button type="button" id="saveButton" class="btn btn-success" onclick="javascript:checkSubmit();" style="margin-left: 5px;">保存</button>
		<button type="button" class="btn btn-success" onclick="javascript:writeCard();" style="margin-left: 5px;">写卡</button>
		<button type="reset" onclick="javascript:resetDefaultValue();" class="btn" style="margin-left: 5px;">重置</button>
		<button type="button" onclick="javascript:readerCard();" class="btn" style="margin-left: 5px;">读取信息</button>
		<div class="btn-group" style="margin-left: 30px;">
			<select id="otherRdid" style="width: 123px;">
				<option value="">其他读者证号</option>
				<c:forEach items="${moreReaders}" var="entry">
					<option value="${entry.rdId}" id="${entry.rdId}">${entry.rdId} | ${entry.rdName}</option>
				</c:forEach>
			</select>
		</div>
		
		 <!-- <div class="btn-group">
			<a class="btn btn-info dropdown-toggle" data-toggle="dropdown" href="#" onclick="window.location.href='batch?page.showCount=10';">
			批量办卡
			</a>
		</div> --> 
		<div id="tip" class="A_MESSAGEBOX_LOADING alert alert-success" style="display: none;">
		</div>
	</div>
	<div class="container-fluid" style="padding-right: 30px;">
		<div class="span4">
			<div class="control-group">
				<label class="control-label" for="rdid">
					证号
				</label>
				<div class="controls" style="width:200px;">
					<input type="text" id="rdId" name="rdId" class="input-medium" style="ime-mode:disabled;"
						onblur="javascript:checkRDID(this,this.value);" />
<!-- 						onpaste="return false;"  -->
<!-- 						ondragenter="return false;" oncontextmenu="return false;" -->
<!-- 						onkeyup="this.value=this.value.replace(/[\u4e00-\u9fa5]/g,'');" /> -->
					<font class="label-required" title=""></font>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label" for="cardId">
					卡号
				</label>
				<div class="controls" style="width:200px;">
					<input type="text" id="cardId" name="cardId" class="input-medium" style="ime-mode:disabled;" />
				</div>
			</div>
			<div class="control-group">
				<label class="control-label" for="rdCFState">
					证状态
				</label>
				<div class="controls" style="width:200px;">
					<select id="rdCFState" name="rdCFState" class="select-medium" disabled="disabled">
					</select>
					<font class="label-required" title=""></font>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label" for="rdLib">
					开户馆
				</label>
				<div class="controls" style="width:200px;">
					<select id="rdLib" name="rdLib" class="select-medium" onchange="getLibReaderType(this.value,'','');">
					</select>
					<font class="label-required" title=""></font>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label" for="rdType">
					读者类型
				</label>
				<div class="controls" style="width:200px;">
					<select id="rdType" name="rdType" class="select-medium" onchange="calEndDate(this.value);">
					</select>
					<font class="label-required" title=""></font>
				</div>
			</div>
			<div class="control-group">
				<!-- 是否馆际读者 -->
				<label class="control-label" for="rdGlobal">
					<font color="#e844">是否馆际读者</font>
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
					</select>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label" for="rdStartDate">
					启用日期
				</label>
				<div class="controls" style="width:200px;">
					<input style="width: 150px;" class="Wdate" type="text" id="rdStartDate" name="rdStartDate" 
						onFocus="WdatePicker({isShowClear:false})" />
					<font class="label-required" title=""></font>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label" for="rdEndDate">
					终止日期
				</label>
				<div class="controls" style="width:200px;">
					<input type="text" name="rdEndDate" class="input-medium" id="rdEndDate" readonly="readonly" />
					<font class="label-required" title=""></font>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label" for="rdInTime">
					办证时间
				</label>
				<div class="controls" style="width:200px;">
					<input type="text" name="rdInTime" class="input-medium" id="rdInTime" readonly="readonly" />
					<font class="label-required" title=""></font>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label" for="rdLoginId">
					手机
				</label>
				<div class="controls" style="width:200px;">
					<input type="text" name="rdLoginId" class="input-medium" id="rdLoginId" />
				</div>
			</div>
			<div class="control-group">
				<label class="control-label" for="rdEmail">
					Email
				</label>
				<div class="controls" style="width:200px;">
					<input type="text" name="rdEmail" class="input-medium" id="rdEmail" />
				</div>
			</div>
			<div class="control-group">
				<label class="control-label" for="rdEmail">
					备注
				</label>
				<div class="controls" style="width:200px;">
					<input type="text" name="rdRemark" class="input-medium" id="rdRemark" />
				</div>
			</div>
			
		</div>
		<div class="span4">
			<div class="control-group">
				<label class="control-label" for="rdName">
					姓名
				</label>
				<div class="controls" style="width:200px;">
					<input type="text" name="rdName" class="input-medium" id="rdName" />
					<font class="label-required" title=""></font>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label" for="rdPasswd">
					密码
				</label>
				<div class="controls" style="width:200px;">
					<input type="password" name="rdPasswd" class="input-medium" id="rdPasswd" />
					<input type="hidden" name="oldRdpasswd" class="input-medium" id="oldRdpasswd" />
				</div>
			</div>
			<div class="control-group">
				<label class="control-label" for="rdCertify">
					身份证号
				</label>
				<div class="controls" style="width:200px;">
					<input type="text" id="rdCertify" name="rdCertify" class="input-medium"
						style="ime-mode:disabled;" onblur="javascript:checkIdentity(this,this.value);" 
						onkeydown="if(event.keyCode==13){parseID(this,this.value);}" />
				</div>
			</div>
			<div class="control-group">
				<label class="control-label" for="rdSex">
					性别
				</label>
				<div class="controls" style="width:200px;">
					<select id="rdSex" name="rdSex" class="select-medium">
						<option value="1">男</option>
						<option value="2">女</option>
					</select>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label" for="rdNation">
					民族
				</label>
				<div class="controls" style="width:200px;">
					<input type="text" name="rdNation" class="input-medium" id="rdNation" />
				</div>
			</div>
			<div class="control-group">
				<label class="control-label" for="rdBornDate">
					出生日期
				</label>
				<div class="controls" style="width:200px;">
					<input style="width: 150px;" class="Wdate" type="text" id="rdBornDate" name="rdBornDate"
						onblur="javascript:caculateAge(this.value);"
						onFocus="WdatePicker({onpicked:caculateAge(this.value),maxDate:document.getElementById('today').value})" />
				</div>
			</div>
			<div class="control-group">
				<label class="control-label" for="rdDeposit">
					所属分组
				</label>
				<div class="controls" style="width:200px;">
					<select class="select-medium" id="group" name="group">
						<option value="0">无</option>
					</select><font class="label-required" title=""></font>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label" for="rdAge">
					年龄
				</label>
				<div class="controls" style="width:200px;">
					<input type="text" name="rdAge" class="input-medium" id="rdAge" readonly="readonly" />
				</div>
			</div>
			<div class="control-group">
				<label class="control-label" for="rdAddress">
					地址
				</label>
				<div class="controls" style="width:200px;">
					<input type="text" name="rdAddress" class="input-medium" id="rdAddress" />
				</div>
			</div>
			<div class="control-group">
				<label class="control-label" for="rdNative">
					籍贯
				</label>
				<div class="controls" style="width:200px;">
					<input type="text" name="rdNative" class="input-medium" id="rdNative" />
				</div>
			</div>
			<div class="control-group">
				<label class="control-label" for="rdUnit">
					单位
				</label>
				<div class="controls" style="width:200px;">
					<input type="text" name="rdUnit" class="input-medium" id="rdUnit" />
				</div>
			</div>
			<div class="control-group">
				<label class="control-label" for="rdPhone">
					电话
				</label>
				<div class="controls" style="width:200px;">
					<input type="text" name="rdPhone" class="input-medium" id="rdPhone" />
				</div>
			</div>
		</div>
		<div class="span4">
			<div class="control-group">
				<label class="control-label" for="rdUnit">
					头像
				</label>
				<div class="controls" style="width:200px;position: relative;">
					<div style="padding-top: 7px;width: 164px;" align="center">
						<img id="avatar" style="width: 130px;height: 130px;" class="img-polaroid" src="<c:url value='/admin/reader/showAvatar/defaultPhoto' />" />
						<input type="hidden" id="fileSrc" name="fileSrc" value=""/>
					</div>
					<div id="previewDivForIE">
					</div>
					<div class="caption" style="padding-top: 7px;width: 164px;" align="center">
						<a href="javascript:void(0);" onclick="javascript:void(0);" class="btn btn-success">更换头像</a>
						<input type="file" id="fileObj" name="fileObj" 
							style="position: absolute;width: 82px;heigth: 30px;top: 154px;left: 41px;
							filter:alpha(opacity: 0); -moz-opacity: 0; opacity: 0;"
							onchange="javascript:preview(this.value);" />
							<!-- "filter: alpha(opacity:1);opacity: 1;"  -->
					</div>
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
					<input type="text" name="rdInterest" class="input-medium" id="rdInterest" />
				</div>
			</div>
			<div class="control-group">
				<label class="control-label" for="rdDeposit">
					押金
				</label>
				<div class="controls">
					<input type="text" name="rdDeposit" class="input-medium" id="rdDeposit" disabled="disabled"/>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label" for="rdPostCode">
					邮编
				</label>
				<div class="controls" style="width:200px;">
					<input type="text" name="rdPostCode" class="input-medium" id="rdPostCode" />
				</div>
			</div>
			<input type="hidden" name="paySign" class="input-medium" id="paySign" />
			<input type="hidden" name="newRdid" class="input-medium" id="newRdid" />
			<input type="hidden" name="deferDate" class="input-medium" id="deferDate" />
			<input type="hidden" name="newCardid" class="input-medium" id="newCardid" />
		</div>
	</div>
</form>
<iframe id="hidden_frame" name="hidden_frame" style="display: none;">
</iframe>
<script type="text/javascript">
	var IMAGETYPES = [".JPG",".JPEG",".PNG",".BMP",".GIF"];
	
	var isAvatarChanged = false;
	//图片预览
	function preview(localPath) {
		isAvatarChanged = true;
		var fileObj=document.getElementById("fileObj");
		var avatar=document.getElementById("avatar");
		if(fileObj.files && fileObj.files[0]){
			//火狐,Chrome:直接设img属性
			//avatar.src = fileObj.files[0].getAsDataURL();
			//火狐7以上版本不能用上面的getAsDataURL()方式获取，需要一下方式
			var suffix = localPath.substring(localPath.lastIndexOf("."),localPath.length).toUpperCase();
			var isPermit = false;
			for(var i=0; i<IMAGETYPES.length; i++){
				if(suffix == IMAGETYPES[i]){
					isPermit = true;
					break;
				}
			}
			if(isPermit == false){
				showResult("只支持&nbsp;jpg&nbsp;|&nbsp;jpeg&nbsp;|&nbsp;png&nbsp;|&nbsp;bmp&nbsp;|&nbsp;gif&nbsp;格式的图片！");
				return;
			}
			avatar.src = window.URL.createObjectURL(fileObj.files[0]);
		}else{
			//IE:使用滤镜
			fileObj.select();
			//获取用户选择文本
			var thisPath = document.selection.createRange().text;
			var suffix = localPath.substring(localPath.lastIndexOf("."),localPath.length).toUpperCase();
			var isPermit = false;
			for(var i=0; i<IMAGETYPES.length; i++){
				if(suffix == IMAGETYPES[i]){
					isPermit = true;
					break;
				}
			}
			if(isPermit == false){
				showResult("只支持&nbsp;jpg&nbsp;|&nbsp;jpeg&nbsp;|&nbsp;png&nbsp;|&nbsp;bmp&nbsp;|&nbsp;gif&nbsp;格式的图片！");
				return;
			}
			var previewDivForIE = document.getElementById("previewDivForIE");
			//必须设置初始大小
			previewDivForIE.style.width = "140px";
			previewDivForIE.style.height = "140px";
			//图片异常的捕捉，防止用户修改后缀来伪造图片
			try{
				previewDivForIE.style.filter="progid:DXImageTransform.Microsoft.AlphaImageLoader(sizingMethod=scale)";
				previewDivForIE.filters.item("DXImageTransform.Microsoft.AlphaImageLoader").src = thisPath;
			}catch(e){
				showResult("您上传的图片格式不正确，请重新选择！");
				return;
			}
			avatar.style.display = "none";
			document.selection.empty();
		}
	}
	

	function uploadAvatar(rdId) {
		if(isAvatarChanged) {
			var dataform = document.getElementById("dataform");
			dataform.action = "<c:url value='/admin/reader/uploadAvatar/"+rdId+"' />";
			dataform.submit();
		}
	}
	//计算截止日期
	function calEndDate(rdType) {
		$.ajax({
			type : "POST",
			url : "<c:url value='/admin/reader/calEndDate' />",
			data : {rdType : rdType},
			dataType : "json",
			success : function(jsonData){
				document.getElementById("rdEndDate").value = jsonData;
			},
			error : function(jsonData){
				showResult("获取数据失败！");
				return;
			}
		});
	}
	
	//获取指定馆下的读者类型
	function getLibReaderType(libCode,selectRdtype,selectRdLibtype) {//添加选中的读者类型 2014-05-16 添加馆际读者类型
		$.ajax({
			type : "POST",
			url : "<c:url value='/admin/reader/getLibReaderType' />",
			data : {thisLibCode : libCode},
			dataType : "json",
			success : function(jsonData){
				var rdType = document.getElementById("rdType");
				rdType.length = 0;
				var types = eval(jsonData);
				if(types.length > 1){
					calEndDate(types[0].READERTYPE);
					for(var i=0; i<types.length-1; i++){
						rdType.options.add(new Option(types[i].SHOWREADERTYPE,types[i].READERTYPE));
						if(selectRdtype==types[i].READERTYPE){//2014-05-16
							rdType[i].selected=true;
						}
					}
					//document.getElementById("rdEndDate").value = types[types.length-1].rdEndDate;
				}else{
					//document.getElementById("rdEndDate").value = "";
				}
			},
			error : function(jsonData){
				showResult("获取数据失败！");
				return;
			}
		});
		
		$.ajax({
			type : "POST",
			url : "<c:url value='/admin/reader/getAllGlobalReaderType' />",
			data : {},
			dataType : "json",
			success : function(jsonData){
				var rdLibType = document.getElementById("rdLibType");
				rdLibType.length = 0;
				var types = eval(jsonData);
				if(types.length){
					for(var i=0; i<types.length; i++){
						rdLibType.options.add(new Option(types[i].SHOWREADERTYPE,types[i].READERTYPE));
						if(selectRdLibtype==types[i].READERTYPE){//2014-11-18
							rdLibType[i].selected=true;
						}
					}
				}
			},
			error : function(jsonData){
				showResult("获取数据失败！");
				return;
			}
		});
	}
	
	
	//是否馆际读者
	function isGlobalReader(thisObj) {
		var rdLibType = document.getElementById("rdLibType");
		if(thisObj.checked == true){
			rdLibType.disabled = "";
		}else{
			rdLibType.disabled = "disabled";
		}
	}
	
	//点击重置重新设置默认值
	function resetDefaultValue() {
		setTimeout(function(){
			window.location.href = "<c:url value='/admin/reader/add' />";//重置下访问的路径 2014-11-10
			document.getElementById("rdId").disabled = "";
			document.getElementById("rdPasswd").disabled = "";
			
			var today = document.getElementById("today").value;
			document.getElementById("rdEndDate").value = today;
			document.getElementById("rdInTime").value = today;
			document.getElementById("rdLibType").disabled = "disabled";
			document.getElementById("avatar").src = "<c:url value='/admin/reader/showAvatar/defaultPhoto' />";
			isAvatarChanged = false;
			ACTION = "ADD";
		},1);
	}
	
	
	function checkSubmit() {
		if(ACTION == "ADD"){
			addReader();
		}else{
			editReader();
		}
	}
	/**
	以前操作没有保存头像操作
	2016-1-21
	上传保存头像的信息
	*/
	function uploadAvatarRdCard(rdId) {
		var fileSrc = $("#fileSrc").val();
		if(fileSrc != ''){
			$.ajax({
				type : "POST",
				url : "<c:url value='/admin/reader/uploadImage/"+rdId+"' />",
				data : {fileSrc : fileSrc},
				dataType : "json",
				success : function(jsonData){
					window.location.href = "<c:url value='/admin/reader/add/"+rdId+"' />";
				},
				cache: false
			});
		}else{
			//alert("上传头像失败！");
			return false;
		}
	}
	//检查---保存---新增
	function addReader() {
		$("#saveButton").attr("disabled","true");
		$("#saveButton").html("保存中...");
		var tip = "";
		//添加分组的判断
		var group = document.getElementById("group").value.trim();
		if(!group){
			tip = "请选择分组！";
			showResult(tip);
			$("#saveButton").removeAttr("disabled");
			$("#saveButton").html("保存");
			return;
		}
		var rdId = document.getElementById("rdId").value.trim();
		if(!rdId){
			tip = "请填写证号！";
			showResult(tip);
			$("#saveButton").removeAttr("disabled");
			$("#saveButton").html("保存");
			return;
		}
		var rdCFState = document.getElementById("rdCFState").value;
		var rdType = document.getElementById("rdType").value;
		if(!rdType){
			tip = "请选择读者类型！";
			showResult(tip);
			$("#saveButton").removeAttr("disabled");
			$("#saveButton").html("保存");
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
				$("#saveButton").removeAttr("disabled");
				$("#saveButton").html("保存");
				return;
			}
		}else{
			rdGlobal = 0;
			rdLibType = "";
		}
		var libUser;
		if($("#libUser").checked) {
			libUser = 1;
		} else {
			libUser = 0;
		}
		var rdStartDate = document.getElementById("rdStartDate").value;
		var rdEndDate = document.getElementById("rdEndDate").value;
		var rdInTime = document.getElementById("rdInTime").value;
		var rdLib = document.getElementById("rdLib").value;
		if(!rdLib){
			tip = "请选择开户馆！";
			showResult(tip);
			$("#saveButton").removeAttr("disabled");
			$("#saveButton").html("保存");
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
			$("#saveButton").removeAttr("disabled");
			$("#saveButton").html("保存");
			return;
		}
		var rdPasswd = document.getElementById("rdPasswd").value;
		var oldRdpasswd = document.getElementById("oldRdpasswd").value;
		if(rdPasswd){
			if((rdPasswd.length > 20 || rdPasswd.length < 6) && rdPasswd.length != 32){
				tip = "密码在6至20位之间！";
				showResult(tip);
				$("#saveButton").removeAttr("disabled");
				$("#saveButton").html("保存");
				return;
			}
		}
		var rdCertify = document.getElementById("rdCertify").value.trim();
		var rdSex = document.getElementById("rdSex").value;
		var rdNation = document.getElementById("rdNation").value.trim();
		var rdBornDate = document.getElementById("rdBornDate").value;
		var rdAge = document.getElementById("rdAge").value;
		var rdLoginId = document.getElementById("rdLoginId").value.trim();
		var rdPhone = document.getElementById("rdPhone").value.trim();
		var rdUnit = document.getElementById("rdUnit").value.trim();
		var rdAddress = document.getElementById("rdAddress").value.trim();
		var rdPostCode = document.getElementById("rdPostCode").value.trim();
		var rdEmail = document.getElementById("rdEmail").value.trim();
		var rdNative = document.getElementById("rdNative").value.trim();
		var rdInterest = document.getElementById("rdInterest").value.trim();
		var groupId = document.getElementById("group").value;
		var rdRemark = $("#rdRemark").val();
		var paySign = $("#paySign").val();
		var cardId = $("#cardId").val();
		
		$.ajax({
			type : "POST",
			url : "<c:url value='/admin/reader/checkRdIdExist' />",
			data : {rdId : rdId, rdCertify : rdCertify},
			dataType : "json",
			success : function(backData){
				var result = backData.result;
				if(result == 'rdidExist'){
					tip = "此证号已存在，请重新填写！";
					showResult(tip);
					$("#saveButton").removeAttr("disabled");
					$("#saveButton").html("保存");
					return;
				} else{
					if(result == 'rdcertifyExist') {
						tip = "此身份证号已经办过证，是否继续？";
						if(!confirm(tip)) {
							return;
						}
					}
					var params = {rdId : rdId,rdCFState : rdCFState,rdType : rdType,rdGlobal : rdGlobal,rdLibType : rdLibType,
						rdStartDate : rdStartDate,rdEndDate : rdEndDate,rdInTime : rdInTime,rdLib : rdLib,rdSort1 : rdSort1,
						rdSort2 : rdSort2,rdSort3 : rdSort3,rdSort4 : rdSort4,rdSort5 : rdSort5,rdName : rdName,
						rdPasswd : rdPasswd,rdCertify : rdCertify,rdSex : rdSex,rdNation : rdNation,rdBornDate : rdBornDate,
						rdAge : rdAge,rdLoginId : rdLoginId,rdPhone : rdPhone,rdUnit : rdUnit,rdAddress : rdAddress,
						rdPostCode : rdPostCode,rdEmail : rdEmail,rdNative : rdNative,rdInterest : rdInterest, paySign:paySign, 
						libUser: libUser, rdRemark: rdRemark, groupId: groupId, cardId: cardId,oldRdpasswd:oldRdpasswd};
					$.ajax({
						type : "POST",
						url : "<c:url value='/admin/reader/addReader' />",
						data : params,
						dataType : "json",
						success : function(jsonData){
							var success = jsonData.success;
							uploadAvatar(rdId);
							uploadAvatarRdCard(rdId);//2016-01-21
							if(success == 0) {
								var checkfee = jsonData.checkfee;
								var servicefee = jsonData.servicefee;
								var idfee = jsonData.idfee;
								var deposity = jsonData.deposity;
								var totalfee = jsonData.totalfee;
								var tip = "";
								if(deposity != 0) {
									tip += " 押金：" + deposity + "元";
								}
								if(checkfee != 0) {
									tip += " 验证费： " + checkfee + "元";
								}
								if(servicefee != 0) {
									tip += " 服务费 ：" + servicefee + "元";
								}
								if(idfee != 0) {
									tip += " 工本费 ：" + idfee + "元";
								}
								if(totalfee != 0) {
									tip += " 共应交" + totalfee + "元。";
									if(confirm(tip)) {
										$("#paySign").val(totalfee);
										addReader();
										$("#paySign").val("");
									} else {
										$("#saveButton").removeAttr("disabled");
										$("#saveButton").html("保存");
										return;
									}
								}
							} else if(success == 1) {
								//if(isAvatarChanged){
								//	var dataform = document.getElementById("dataform");
								//	dataform.action = "<c:url value='/admin/reader/uploadAvatar/"+rdId+"' />";
								//	dataform.submit();
								//}
								// 添加这个会影响照片的保存 2014-11-10
								uploadAvatar(rdId);
								uploadAvatarRdCard(rdId);//2016-01-21
								setInterval(function(){
									callBackToCard(rdId);//传给写卡器的请求
									alert("读者添加成功");
									window.location.href = "<c:url value='/admin/reader/add/"+rdId+"' />";
									$("#paySign").val("");
									$("#saveButton").removeAttr("disabled");
									$("#saveButton").html("保存");
									return;
							   },3000);
							} else if(success == -1) {
								//2014-11-12
								//if(isAvatarChanged){
								//	var dataform = document.getElementById("dataform");
								//	dataform.action = "<c:url value='/admin/reader/uploadAvatar/"+rdId+"' />";
								//	dataform.submit();
								//}
								uploadAvatar(rdId);
								uploadAvatarRdCard(rdId);//2016-01-21
								setInterval(function(){
									callBackToCard(rdId);//传给写卡器的请求
									alert('读者'+jsonData.message);
									window.location.href = "<c:url value='/admin/reader/add/"+rdId+"' />";
									$("#saveButton").removeAttr("disabled");
									$("#saveButton").html("保存");
									return ;
								},3000);
							} else if(success == -2) {
								alert(jsonData.message);
								$("#saveButton").removeAttr("disabled");
								$("#saveButton").html("保存");
								return;
							}
						},
						error : function(){
							showResult("获取数据失败！");
							$("#paySign").val("");
							$("#saveButton").removeAttr("disabled");
							$("#saveButton").html("保存");
							return;
						}
					});
				}
			},
			error : function(){
				showResult("获取数据失败！");
				$("#saveButton").removeAttr("disabled");
				$("#saveButton").html("保存");
				return;
			}
		});
	}
	
	
	//检查---保存---修改
	function editReader() {
		$("#saveButton").attr("disabled","true");
		$("#saveButton").html("保存中...");
		var tip = "";
		//添加分组的判断
		var group = document.getElementById("group").value.trim();
		if(!group){
			tip = "请选择分组！";
			showResult(tip);
			$("#saveButton").removeAttr("disabled");
			$("#saveButton").html("保存");
			return;
		}
		var rdId = document.getElementById("rdId").value;
		if(!rdId){
			tip = "请填写证号！";
			showResult(tip);
			$("#saveButton").removeAttr("disabled");
			$("#saveButton").html("保存");
			return;
		}
		var rdPasswd = document.getElementById("rdPasswd").value;
		
		var oldRdpasswd = document.getElementById("oldRdpasswd").value;
		//if(rdPasswd){
		//	if((rdPasswd.length > 20 || rdPasswd.length < 6) && rdPasswd.length != 32){
		//		tip = "密码在6至20位之间！";
		//		showResult(tip);
		//		return;
		//	}
		//}
		var rdCFState = document.getElementById("rdCFState").value;
		var rdType = document.getElementById("rdType").value;
		if(!rdType){
			tip = "请选择读者类型！";
			showResult(tip);
			$("#saveButton").removeAttr("disabled");
			$("#saveButton").html("保存");
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
				$("#saveButton").removeAttr("disabled");
				$("#saveButton").html("保存");
				return;
			}
		}else{
			rdGlobal = 0;
			rdLibType = "";
		}
		var rdStartDate = document.getElementById("rdStartDate").value;
		var rdEndDate = document.getElementById("rdEndDate").value;
		var rdInTime = document.getElementById("rdInTime").value;
		var rdLib = document.getElementById("rdLib").value;
		if(!rdLib){
			tip = "请选择开户馆！";
			showResult(tip);
			$("#saveButton").removeAttr("disabled");
			$("#saveButton").html("保存");
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
			$("#saveButton").removeAttr("disabled");
			$("#saveButton").html("保存");
			return;
		}
		var rdCertify = document.getElementById("rdCertify").value.trim();
		
		var rdSex = document.getElementById("rdSex").value;
		var rdNation = document.getElementById("rdNation").value.trim();
		var rdBornDate = document.getElementById("rdBornDate").value;
		
		var rdLoginId = document.getElementById("rdLoginId").value.trim();
		var rdPhone = document.getElementById("rdPhone").value.trim();
		var rdUnit = document.getElementById("rdUnit").value.trim();
		var rdAddress = document.getElementById("rdAddress").value.trim();
		var rdPostCode = document.getElementById("rdPostCode").value.trim();
		var rdEmail = document.getElementById("rdEmail").value.trim();
		var rdNative = document.getElementById("rdNative").value.trim();
		var rdInterest = document.getElementById("rdInterest").value.trim();
		var rdRemark = $("#rdRemark").val();
		var groupId = document.getElementById("group").value;
		var libUser;
		var paySign = $("#paySign").val();
		if($("#libUser").checked) {
			libUser = 1;
		} else {
			libUser = 0;
		}
		var cardId = $("#cardId").val();
		var params = {rdId : rdId, rdPasswd: rdPasswd,rdCFState : rdCFState,rdType : rdType,rdGlobal : rdGlobal,rdLibType : rdLibType,
			rdStartDate : rdStartDate,rdEndDate : rdEndDate,rdInTime : rdInTime,rdLib : rdLib,rdSort1 : rdSort1,
			rdSort2 : rdSort2,rdSort3 : rdSort3,rdSort4 : rdSort4,rdSort5 : rdSort5,rdName : rdName,
			rdCertify : rdCertify,rdSex : rdSex,rdNation : rdNation,rdBornDate : rdBornDate,
			rdLoginId : rdLoginId,rdPhone : rdPhone,rdUnit : rdUnit,rdAddress : rdAddress,
			rdPostCode : rdPostCode,rdEmail : rdEmail,rdNative : rdNative,rdInterest : rdInterest, 
			libUser: libUser, paySign: paySign, rdRemark: rdRemark, groupId: groupId, cardId: cardId,oldRdpasswd: oldRdpasswd};
		$.ajax({
			type : "POST",
			url : "<c:url value='/admin/reader/editReader' />",
			data : params,
			dataType : "json",
			success : function(jsonData){
				uploadAvatar(rdId);
				uploadAvatarRdCard(rdId);//上传头像 2016-01-21
				var success = jsonData.success;
				if(success == 1){
					//if(isAvatarChanged){
					//	var dataform = document.getElementById("dataform");
					//	dataform.action = "<c:url value='/admin/reader/uploadAvatar/"+rdId+"' />";
					//	dataform.submit();
					//}else{
					//}
					alert("修改成功！");
					callBackToCard(rdId);//传给写卡器的请求
					window.location.href = "<c:url value='/admin/reader/add/"+rdId+"' />";
					$("#saveButton").removeAttr("disabled");
					$("#saveButton").html("保存");
				}else if(success == -1){
					var message = jsonData.message;
					showResult(message);
					$("#saveButton").removeAttr("disabled");
					$("#saveButton").html("保存");
					return;
				} else if(success == 0) {
					var newRdtype = jsonData.newRdtype;
					var oldRdtype = jsonData.oldRdtype;
					var rdDeposity = jsonData.rdDeposity;
					var deposity = jsonData.deposity;
					var tip = "读者类型改变 " + oldRdtype + "->" + newRdtype;
					if(rdDeposity > deposity) {
						var money = rdDeposity - deposity;
						tip += ", 应退还押金 【" + money + "】 元";
					} else {
						var money = deposity - rdDeposity;
						tip += ", 应补交押金 【" + money + "】 元";
					}
					if(confirm(tip)) {
						$("#paySign").val("1");
						editReader();
						$("#paySign").val("");
					} else {
						$("#saveButton").removeAttr("disabled");
						$("#saveButton").html("保存");
					}
					return ;
				} else if(success == -2) {
					alert(jsonData.message);
					$("#saveButton").removeAttr("disabled");
					$("#saveButton").html("保存");
					return;
				}
			},
			error : function(jsonData){
				showResult("获取数据失败！");
				$("#saveButton").removeAttr("disabled");
				$("#saveButton").html("保存");
				return;
			}
		});
	}
	
	function initReader(reader, isNew) {
		var libUser = reader.libUser;
		if(libUser == 1) {
			 //document.getElementById("libUser").checked = true;
			 $("#libUser").checked = true;
		}
		var rdId = document.getElementById("rdId");
		document.getElementById("avatar").src = "<c:url value='/admin/reader/showAvatar/' />"+reader.rdId;
		rdId.value = reader.rdId;
		var rdPasswd = document.getElementById("rdPasswd");
		var oldRdpasswd = document.getElementById("oldRdpasswd");
		rdPasswd.value = reader.rdPasswd;
		oldRdpasswd.value = reader.oldRdpasswd;//add 2014-05-14
		if(reader.rdPasswd == "") {
			rdPasswd.value = "123456";
		}
		if(!isNew) {
			ACTION = "EDIT";
			rdId.disabled = "disabled";
			rdPasswd.disabled = "disabled";
		} else {
			ACTION = "ADD";
			rdId.disabled = "";
			rdPasswd.disabled = "";
		}
		if(reader.rdGlobal == 1){
			document.getElementById("rdGlobal").checked = true;
			document.getElementById("rdLibType").value = reader.rdLibType;
			document.getElementById("rdLibType").disabled = "";
		}else{
			document.getElementById("rdGlobal").checked = false;
		}
		//开户馆和读者类型是关联  读者类型有其他条件触发显示对应值，不能直接只有加载 modify by 2014-05-15
// 		document.getElementById("rdType").value = reader.rdType;
		document.getElementById("rdLib").value = reader.rdLib;
		getLibReaderType(reader.rdLib,reader.rdType,reader.rdLibType);//显示对应的读者类型
		//calEndDate(reader.rdType);
				
		document.getElementById("rdCFState").value = reader.rdCFState;
		if(reader.rdSex == 0) {
			document.getElementById("rdSex").value = 2;
		} else {
			document.getElementById("rdSex").value = reader.rdSex;
		}
		document.getElementById("rdSort1").value = reader.rdSort1;
		document.getElementById("rdSort2").value = reader.rdSort2;
		document.getElementById("rdSort3").value = reader.rdSort3;
		document.getElementById("rdSort4").value = reader.rdSort4;
		document.getElementById("rdSort5").value = reader.rdSort5;
		
		document.getElementById("rdInTime").value = reader.rdInTimeStr;
		document.getElementById("rdStartDate").value = reader.rdStartDateStr;
		document.getElementById("rdEndDate").value = reader.rdEndDateStr;
		if(reader.rdBornDateStr && reader.rdBornDateStr!="null"){
			document.getElementById("rdBornDate").value = reader.rdBornDateStr;
			caculateAge(reader.rdBornDateStr);
		}
		if(reader.rdEmail && reader.rdEmail!="null"){
			document.getElementById("rdEmail").value = reader.rdEmail;
		}
		document.getElementById("rdName").value = reader.rdName;
		if(reader.rdCertify && reader.rdCertify!="null"){
			document.getElementById("rdCertify").value = reader.rdCertify;
		}
		if(reader.rdNation && reader.rdNation!="null"){
			document.getElementById("rdNation").value = reader.rdNation;
		}
		if(reader.rdLoginId && reader.rdLoginId!="null"){
			document.getElementById("rdLoginId").value = reader.rdLoginId;
		}
		if(reader.rdAddress && reader.rdAddress!="null"){
			document.getElementById("rdAddress").value = reader.rdAddress;
		}
		if(reader.rdNative && reader.rdNative!="null"){
			document.getElementById("rdNative").value = reader.rdNative;
		}
		if(reader.rdUnit && reader.rdUnit!="null"){
			document.getElementById("rdUnit").value = reader.rdUnit;
		}
		if(reader.rdPhone && reader.rdPhone!="null"){
			document.getElementById("rdPhone").value = reader.rdPhone;
		}
		if(reader.rdPostCode && reader.rdPostCode!="null"){
			document.getElementById("rdPostCode").value = reader.rdPostCode;
		}
		if(reader.rdInterest && reader.rdInterest!="null"){
			document.getElementById("rdInterest").value = reader.rdInterest;
		}
		if(reader.rdDeposit != "" && reader.rdDeposit!="null"){
			document.getElementById("rdDeposit").value = reader.rdDeposit;
		}
		if(reader.rdRemark && reader.rdRemark!="null") {
			$("#rdRemark").val(reader.rdRemark);
		}
		if(reader.cardId && reader.cardId != "null") {
			$("#cardId").val(reader.cardId);
		}else{
			$("#cardId").val("");//如果是null 则啥也不显示 2014-11-11
		}
		//$("#group").val(reader.groupId);
		document.getElementById("group").value = reader.groupId;
		rdId = $("#rdId").val();
		checkReaderDeposity(rdId);
	}
	
	//对比深图读者状态和本地是否一致
	function checkReaderFromWebServiceAndLocal() {
		var fieldValue = document.getElementById("fieldValue").value.trim();
		if(!fieldValue) {
			showResult("请输入查询条件！");
			return false;
		}
		var fieldName = document.getElementById("fieldName").value;
		if(fieldName == "rdId") {
			var params = {"rdId" : fieldValue};
			$.ajax({
				type : "POST",
				url : "<c:url value='/admin/reader/checkReaderFromWebserviceAndLocal' />",
				data : params,
				dataType : "json",
				success : function(jsonData){
					var success = jsonData.success;
					var message = jsonData.message;
					if(success == -1) {
						if(confirm(message + ",是否要继续显示本地卡状态？")) {
							var localReader = jsonData.localReader;
							initReader(eval(localReader), false);
						} else {
							var szReader = jsonData.szReader;
							initReader(eval(szReader), false);
						}
					} else if(success == -2) {
						alert(message);
						return;
					} else if(success == -3) {
						var szReader = jsonData.szReader;
						showResult(message + ", 请先点击保存开卡。");
						initReader(eval(szReader), true);
					} else if(success == 0) {
						var localReader = jsonData.localReader;
						initReader(eval(localReader), false);
					}
				},
				error : function(){
					showResult("未查询到读者信息！");
					return;
				}
			});
		} else {
			searchReader();
		}
	}
	
	function searchReader() {
		var fieldValue = document.getElementById("fieldValue").value.trim();
		if(!fieldValue) {
			showResult("请输入查询条件！");
			return false;
		}
		var fieldName = document.getElementById("fieldName").value;
		var params = {fieldName : fieldName,fieldValue : fieldValue};
		$.ajax({
			type : "POST",
			url : "<c:url value='/admin/reader/searchReader' />",
			data : params,
			dataType : "json",
			success : function(backData){
				var result = backData.result;
				if(result && result=="none") {
					showResult("未查询到读者信息！");
					return false;
				} else if(result && result=="more") {
					//查询结果有{rdid1， rdid2， rdid3}
					showResult("查询结果大于一条，请细化查询条件！");
					return false;
				} else {
					isAvatarChanged = false;
					var reader = eval(backData);
					//initReader(reader, false);
					window.location.href = "<c:url value='/admin/reader/add/"+reader.rdId+"' />";
				}
			},
			error : function(){
				showResult("未查询到读者信息！");
				return;
			}
		});
// 		var buttonDiv = document.getElementById("buttonDiv");
// 		var pageSpan = document.getElementById("pageSpan");
// 		buttonDiv.style.paddingLeft = "10px";
// 		pageSpan.style.display = "inline";
	}
	
	//检查读者的读者类型和当前押金是否匹配
	function checkReaderDeposity(rdId) {
		var paySign = $("#paySign").val();
		var rdCFState = $("#rdCFState").val(); 
		if(rdCFState != "1") {
			return;
		}
		$.ajax({
			type : "POST",
			url : "<c:url value='/admin/reader/checkReaderDeposity' />",
			data : {rdId: rdId, paySign: paySign},
			dataType : "json",
			success : function(jsonData){
				var result = jsonData.result;
				var deposityOperation = result.deposityOperation;
				var deposityDifferent = result.deposityDifferent;
				var success = result.success;
				if(success == "false") {
					if(deposityOperation == "refund") {
						if(confirm("读者类型与押金不匹配，需要退还押金" + deposityDifferent + "元")) {
							$("#paySign").val("1");
							checkReaderDeposity(rdId);
							$("#paySign").val("");
						}
					} else if(deposityOperation == "add") {
						if(confirm("读者类型与押金不匹配，需要补交押金" + deposityDifferent + "元")) {
							$("#paySign").val("1");
							checkReaderDeposity(rdId);
							$("#paySign").val("");
						}
					}
				} else if(success == "true"){
					if(deposityOperation == "refund") {
						alert("退还押金操作成功！");
						window.location.href = "<c:url value='/admin/reader/add/"+rdId+"' />";
					} else if(deposityOperation == "add") {
						alert("补交押金操作成功！");
						window.location.href = "<c:url value='/admin/reader/add/"+rdId+"' />";
					}
				}
			},
			error : function(){
				return;
			}
		});
	}
	
	//恢复读者证
	var payed = 0.0;
	function renew() {
		$("#cardOp").attr("disabled", "true");
		$("#cardDrop").attr("disabled", "true");
		$("#cardOp").html("操作中...");
		var rdId = $("#rdId").attr("value");
		if(rdId == "") {
			showResult("请先查询读者信息!");
			$("#cardOp").removeAttr("disabled");
			$("#cardDrop").removeAttr("disabled");
			$("#cardOp").html("证操作");
			return;
		}
		$.ajax({
			type : "POST",
			url : "<c:url value='/admin/reader/renew' />",
			data : {rdId: rdId,payed : payed},
			dataType : "json",
			success : function(jsonData){
				var success = jsonData.success;
				if(success == 0) {
					var checkfee = jsonData.checkfee;
					var servicefee = jsonData.servicefee;
					var idfee = jsonData.idfee;
					var deposity = jsonData.deposity;
					var rdDeposity = jsonData.rdDeposity;
					var totalfee = jsonData.totalfee;
					var tip = "";
					tip += " 读者当前押金：" + rdDeposity + "元";
					if(checkfee != 0) {
						tip += " 验证费： " + checkfee + "元";
					}
					if(servicefee != 0) {
						tip += " 服务费 ：" + servicefee + "元";
					}
					if(idfee != 0) {
						tip += " 工本费 ：" + idfee + "元";
					}
					if(deposity != 0) {
						tip += " 押金 ：" + deposity + "元";
					}
					if(totalfee != 0) {
						tip += " 计算后共应交" + totalfee + "元。";
						if(confirm(tip)) {
							payed = totalfee;
							renew();
							payed = 0.0;
						} else {
							$("#cardOp").removeAttr("disabled");
							$("#cardDrop").removeAttr("disabled");
							$("#cardOp").html("证操作");
						}
					}
				} else if(success == -1) {
					var totalfee = jsonData.totalfee;
					var prepay = jsonData.prepay;
					alert("读者账户余额("+ prepay +"元), 不足以完成此次("+ totalfee +"元)扣费!");
					payed = 0.0;
					$("#cardOp").removeAttr("disabled");
					$("#cardDrop").removeAttr("disabled");
					$("#cardOp").html("证操作");
					return;
				} else if(success == 2) {
					showResult("该证不需要恢复!");
					payed = 0.0;
					$("#cardOp").removeAttr("disabled");
					$("#cardDrop").removeAttr("disabled");
					$("#cardOp").html("证操作");
					return;
				} else if(success == -2) {
					alert("证操作成功!同步失败");
					payed = 0.0;
					window.location.href = "<c:url value='/admin/reader/add/"+rdId+"' />";
					return;
				} else {
					alert("证操作成功!");
					payed = 0.0;
					window.location.href = "<c:url value='/admin/reader/add/"+rdId+"' />";
					return;
				}
				
			},
			cache: false
		});
	}
	//读者证注销操作
	function rdLogout() {
		var rdCFState = $("#rdCFState").val();
		if(rdCFState==5){
			if(!isContinue) {
				if(!confirm("该证已注销，请检查是否已经被注销, 是否继续注销？"))return;
				isContinue = true;
			}
		}
		$("#cardOp").attr("disabled", "true");
		$("#cardDrop").attr("disabled", "true");
		$("#cardOp").html("操作中...");
		var rdId = $("#rdId").attr("value");
		if(rdId == "") {
			showResult("请先查询读者信息!");
			$("#cardOp").removeAttr("disabled");
			$("#cardDrop").removeAttr("disabled");
			$("#cardOp").html("证操作");
			return;
		}
		$.ajax({
			timeout:5000,
			type : "POST",
			url : "<c:url value='/admin/reader/logout' />",
			data : {rdId: rdId,payed : payed},
			dataType : "json",
			success : function(jsonData){
				var success = jsonData.success;
				if(success == 0) {
					var checkfee = jsonData.checkfee;
					var servicefee = jsonData.servicefee;
					var idfee = jsonData.idfee;
					var totalfee = jsonData.totalfee;
					var tip = "";
					if(checkfee != 0) {
						tip += " 验证费： " + checkfee + "元";
					}
					if(servicefee != 0) {
						tip += " 服务费 ：" + servicefee + "元";
					}
					if(idfee != 0) {
						tip += " 工本费 ：" + idfee + "元";
					}
					if(totalfee != 0) {
						tip += " 计算后共应交" + totalfee + "元。";
						if(confirm(tip)) {
							payed = totalfee;
							rdLogout();
							payed = 0.0;
						} else {
							$("#cardOp").removeAttr("disabled");
							$("#cardDrop").removeAttr("disabled");
							$("#cardOp").html("证操作");
						}
					}
				} else if(success == -1) {
					var totalfee = jsonData.totalfee;
					var prepay = jsonData.prepay;
					alert("读者账户余额("+ prepay +"元), 不足以完成此次("+ totalfee +"元)扣费!");
					payed = 0.0;
					$("#cardOp").removeAttr("disabled");
					$("#cardDrop").removeAttr("disabled");
					$("#cardOp").html("证操作");
					return;
				} else if(success == 1) {
					alert("证操作成功!");
					payed = 0.0;
					window.location.href = "<c:url value='/admin/reader/add/"+rdId+"' />";
					return;
				} else if(success == -2) {
					alert("证操作成功!同步失败！");
					payed = 0.0;
					window.location.href = "<c:url value='/admin/reader/add/"+rdId+"' />";
					return;
				} else if(success == 2) {
					showResult("该证已经注销！");
					$("#cardOp").removeAttr("disabled");
					$("#cardDrop").removeAttr("disabled");
					$("#cardOp").html("证操作");
				}
				
			},
			cache: false
		});
	}
		//读者证信用有效操作
	function credit() {
		$("#cardOp").attr("disabled", "true");
		$("#cardDrop").attr("disabled", "true");
		$("#cardOp").html("操作中...");
		var rdId = $("#rdId").attr("value");
		if(rdId == "") {
			showResult("请先查询读者信息!");
			$("#cardOp").removeAttr("disabled");
			$("#cardDrop").removeAttr("disabled");
			$("#cardOp").html("证操作");
			return;
		}
//		var rdCFState = $("#rdstate").text();
//		if(rdCFState=="信用有效"){
//			if(!isContinue) {
//				if(!confirm("该证已是信用有效状态，请检查是否已经, 是否继续注销？"))return;
//				isContinue = true;
//			}
//		}
		$.ajax({
			type : "POST",
			url : "credit",
			data : {rdId: rdId,payed : payed},
			dataType : "json",
			success : function(jsonData){
				var success = jsonData.success;
				if(success == 0) {
					var checkfee = jsonData.checkfee;
					var servicefee = jsonData.servicefee;
					var idfee = jsonData.idfee;
					var totalfee = jsonData.totalfee;
					var tip = "";
					if(checkfee != 0) {
						tip += " 验证费： " + checkfee + "元";
					}
					if(servicefee != 0) {
						tip += " 服务费 ：" + servicefee + "元";
					}
					if(idfee != 0) {
						tip += " 工本费 ：" + idfee + "元";
					}
					if(totalfee != 0) {
						tip += " 计算后共应交" + totalfee + "元。";
						if(confirm(tip)) {
							payed = totalfee;
							rdLogout();
							payed = 0.0;
						}
					}
				} else if(success == -1) {
					var totalfee = jsonData.totalfee;
					var prepay = jsonData.prepay;
					alert("读者账户余额("+ prepay +"元), 不足以完成此次("+ totalfee +"元)扣费!");
					payed = 0.0;
					return;
				} else if(success == 1) {
					alert("证信用有效状态操作成功!");
					payed = 0.0;
					window.location.href = "index";//"/sso/admin/reader/add/"+rdId;
					return;
				} else if(success == -2) {
					alert("证信用有效状态操作成功!同步失败！");
					payed = 0.0;
					window.location.href = "index";//"/sso/admin/reader/add/"+rdId;
					return;
				} else if(success == -3) {
					var message = jsonData.message;
					alert(message);
					return;
				}
				
			},
			cache: false
		});
	}
	//验证读者证
	function check() {
		$("#cardOp").attr("disabled", "true");
		$("#cardDrop").attr("disabled", "true");
		$("#cardOp").html("操作中...");
		var rdId = $("#rdId").attr("value");
		if(rdId == "") {
			showResult("请先查询读者信息!");
			$("#cardOp").removeAttr("disabled");
			$("#cardDrop").removeAttr("disabled");
			$("#cardOp").html("证操作");
			return;
		}
		$.ajax({
			type : "POST",
			url : "<c:url value='/admin/reader/check' />",
			data : {rdId: rdId, payed : payed},
			dataType : "json",
			success : function(jsonData){
				var success = jsonData.success;
				if(success == 0) {
					var checkfee = jsonData.checkfee;
					var servicefee = jsonData.servicefee;
					var idfee = jsonData.idfee;
					var totalfee = jsonData.totalfee;
					var tip = "";
					if(checkfee != 0) {
						tip += " 验证费： " + checkfee + "元";
					}
					if(servicefee != 0) {
						tip += " 服务费 ：" + servicefee + "元";
					}
					if(idfee != 0) {
						tip += " 工本费 ：" + idfee + "元";
					}
					if(totalfee != 0) {
						tip += " 计算后共应交" + totalfee + "元。";
						if(confirm(tip)) {
							payed = totalfee;
							check();
							payed = 0.0;
						} else {
							$("#cardOp").removeAttr("disabled");
							$("#cardDrop").removeAttr("disabled");
							$("#cardOp").html("证操作");
						}
					}
				} else if(success == -1) {
					var totalfee = jsonData.totalfee;
					var prepay = jsonData.prepay;
					alert("读者账户余额("+ prepay +"元), 不足以完成此次("+ totalfee +"元)扣费!");
					payed = 0.0;
					$("#cardOp").removeAttr("disabled");
					$("#cardDrop").removeAttr("disabled");
					$("#cardOp").html("证操作");
					return;
				} else if(success == 1) {
					alert("证操作成功!");
					payed = 0.0;
					window.location.href = "<c:url value='/admin/reader/add/"+rdId+"' />";
					return;
				} else if(success == -2) {
					alert("证操作成功!同步失败");
					payed = 0.0;
					window.location.href = "<c:url value='/admin/reader/add/"+rdId+"' />";
					return;
				}
				
			},
			cache: false
		});
	}
	
	function cardOperation(option) {
		$("#cardOp").attr("disabled", "true");
		$("#cardDrop").attr("disabled", "true");
		$("#cardOp").html("操作中...");
		var rdId = $("#rdId").attr("value");
		if(rdId == "") {
			showResult("请先查询读者信息!");
			$("#cardOp").removeAttr("disabled");
			$("#cardDrop").removeAttr("disabled");
			$("#cardOp").html("证操作");
			return;
		}
		
		$.ajax({
			type : "POST",
			url : "<c:url value='/admin/reader/cardOperation' />",
			data : {rdId: rdId, option : option},
			dataType : "json",
			success : function(jsonData){
				var success = jsonData.success;
				if(success == 1) {
					alert("证操作成功!");
					window.location.href = "<c:url value='/admin/reader/add/"+rdId+"' />";
					return;
				} else if(success == 2) {
					showResult("该证无须进行本操作!");
					$("#cardOp").removeAttr("disabled");
					$("#cardDrop").removeAttr("disabled");
					$("#cardOp").html("证操作");
					return;
				} else if(success == -2 ) {
					var message = jsonData.message;
					alert(message);
					window.location.href = "<c:url value='/admin/reader/add/"+rdId+"' />";
				}
				
				
			},
			cache: false
		});
	}
	//退证操作
	function quit() {
		var rdCFState = $("#rdCFState").val();
		if(rdCFState==5){
			if(!isContinue) {
				if(!confirm("该证已注销，请检查是否已经退办读者证, 是否继续退证？"))return;
				isContinue = true;
			}
		}
		$("#cardOp").attr("disabled", "true");
		$("#cardDrop").attr("disabled", "true");
		$("#cardOp").html("操作中...");
		var rdId = $("#rdId").attr("value");
		if(rdId == "") {
			showResult("请先查询读者信息!");
			$("#cardOp").removeAttr("disabled");
			$("#cardDrop").removeAttr("disabled");
			$("#cardOp").html("证操作");
			return;
		}
		$.ajax({
			timeout:10000,
			type : "POST",
			url : "<c:url value='/admin/reader/quit' />",
			data : {rdId: rdId,payed : payed},
			dataType : "json",
			success : function(jsonData){
				var success = jsonData.success;
				if(success == 0) {
					var checkfee = jsonData.checkfee;
					var servicefee = jsonData.servicefee;
					var idfee = jsonData.idfee;
					var totalfee = jsonData.totalfee;
				//	message.append("\"prepay\":" + prepay + ", "); //应退预付款
				//	message.append("\"deposity\":" + rdDeposity ); //押金
					var prepay = jsonData.prepay;
					var deposity = jsonData.deposity;
					var tip = "";
					if(checkfee > 0) {
						tip += " 验证费【" + checkfee + "】元";
					}
					if(servicefee > 0) {
						tip += " 服务费 【" + servicefee + "】元";
					}
					if(idfee > 0) {
						tip += " 工本费 【" + idfee + "】元";
					}
					if(prepay > 0) {
						tip += " 应退还";
						tip += " 一卡通余额【" + prepay + "】元";
					}
					if(deposity > 0) {
						tip += " 应退还";
						tip += " 押金【" + deposity + "】元";
					}
					if(totalfee > 0) {
						tip += " 计算后共应交" + totalfee + "元。";
						if(confirm(tip)) {
							payed = totalfee;
							quit();
							payed = 0.0;
						} else {
							$("#cardOp").removeAttr("disabled");
							$("#cardDrop").removeAttr("disabled");
							$("#cardOp").html("证操作");
						}
					} else {
						tip += " 计算后共应退还" + (0 - totalfee) + "元。";
						if(confirm(tip)) {
							payed = totalfee;
							quit();
							payed = 0.0;
						} else {
							$("#cardOp").removeAttr("disabled");
							$("#cardDrop").removeAttr("disabled");
							$("#cardOp").html("证操作");
						}
					}
					
				} else if(success == -1) {
					var totalfee = jsonData.totalfee;
					var prepay = jsonData.prepay;
					alert("读者账户余额("+ prepay +"元), 不足以完成此次("+ totalfee +"元)扣费!");
					payed = 0.0;
					$("#cardOp").removeAttr("disabled");
					$("#cardDrop").removeAttr("disabled");
					$("#cardOp").html("证操作");
					return;
				} else if(success == 1) {
					alert("证操作成功!");
					payed = 0.0;
					window.location.href = "<c:url value='/admin/reader/add/"+rdId+"' />";
					return;
				} else if(success == -2) {
					alert("证操作成功!同步失败");
					payed = 0.0;
					window.location.href = "<c:url value='/admin/reader/add/"+rdId+"' />";
					return;
				}
				
			},
			cache: false
		});
	}
	function syncReader() {
		var rdId = $("#rdId").attr("value");
		$("#syncButton").attr("disabled", "true");
		$("#syncButton").html("正在同步...");
		
		if(rdId == "") {
			showResult("请先查询读者信息!");
			$("#syncButton").removeAttr("disabled");
			$("#syncButton").html("手动同步");
			return;
		}
		$.ajax({
			type : "POST",
			url : "${READER_URL}/syncReader/" + rdId,
			dataType : "json",
			success : function(jsonData){
				var success = jsonData.success;
				if(success == 1) {
					showResult("同步成功!");
					$("#syncButton").removeAttr("disabled");
					$("#syncButton").html("手动同步");
					return;
				} else {
					var message = jsonData.message;
					var exp = jsonData.exception;
					if(exp == "null") {
						showResult(message);
					} else {
						showResult(message + ", <br />异常：" + exp);
					}
					$("#syncButton").removeAttr("disabled");
					$("#syncButton").html("手动同步");
				}
			},
			error : function(){
				showResult("获取接口数据失败！");
				$("#syncButton").removeAttr("disabled");
				$("#syncButton").html("手动同步");
				return;
			},
			cache:false
		});
	}
	
	var isContinue = false;
	
	//读者补办选择
	function repairSelect(){
		if(confirm("是补办读者证号吗?")){
			repair();
			return;
		}
		if(confirm("是补办读者卡号吗?")){
			cardRepair();
			return;
		}
	}
	
	//补办
	function repair(){
		var rdId = $("#rdId").val();
		if(rdId == "") {
			showResult("请先查询读者信息!");
			$("#cardOp").removeAttr("disabled");
			$("#cardDrop").removeAttr("disabled");
			$("#cardOp").html("证操作");
			return;
		}
		var rdCFState=$("#rdCFState").attr("value");
		if(!(rdCFState==3 || rdCFState==5)){
			showResult("只有挂失、注销状态的读者才能补办读者证！");return;
		}
		if(rdCFState==5){
			if(!isContinue) {
				if(!confirm("该证已注销，请检查是否已经补办读者证, 是否继续补办？"))return;
				isContinue = true;
			}
		}
		if(rdId.charAt(rdId.length-1)=="P"){
			if(!confirm("此读者证已经是补办证！是否继续？"))return;
		}
	
		//基本信息start
		var rdType = $("#rdType").val();
		var rdGlobal;
		var rdLibType;
		if($("#rdGlobal").attr("checked")){
			rdGlobal = 1;
			rdLibType = $("#rdLibType").val();
			if(!rdLibType){
				tip = "请选择馆际流通类型！";
				showResult(tip);
				$("#saveButton").removeAttr("disabled");
				$("#saveButton").html("保存");
				return;
			}
		}else{
			rdGlobal = 0;
			rdLibType = "";
		}
		var libUser;
		if($("#libUser").attr("checked")) {
			libUser = 1;
		} else {
			libUser = 0;
		}
		var rdStartDate = $("#rdStartDate").val();
		
		var rdEndDate = $("#rdEndDate").val();
		var rdInTime = $("#rdInTime").val();
		var rdLib = $("#rdLib").val();
		if(!rdLib){
			tip = "请选择开户馆！";
			showResult(tip);
			$("#saveButton").removeAttr("disabled");
			$("#saveButton").html("保存");
			return;
		}
		var rdSort1 = $("#rdSort1").val();
		var rdSort2 = $("#rdSort2").val();
		var rdSort3 = $("#rdSort3").val();
		var rdSort4 = $("#rdSort4").val();
		var rdSort5 = $("#rdSort5").val();
		
		var rdName = $("#rdName").val().trim();
		if(!rdName){
			tip = "请填写姓名！";
			showResult(tip);
			$("#saveButton").removeAttr("disabled");
			$("#saveButton").html("保存");
			return;
		}
		var rdPasswd = $("#rdPasswd").val();
		var oldRdpasswd = $("#oldRdpasswd").val();//ADD 2014-05-14

		var rdCertify = $("#rdCertify").val().trim();

		var rdSex = $("#rdSex").val();
		var rdNation = $("#rdNation").val().trim();
		var rdBornDate = $("#rdBornDate").val();
		var rdAge = $("#rdAge").val();
		var rdLoginId = $("#rdLoginId").val().trim();
		var rdPhone = $("#rdPhone").val().trim();
		var rdUnit = $("#rdUnit").val().trim();
		var rdAddress = $("#rdAddress").val().trim();
		var rdPostCode = $("#rdPostCode").val().trim();
		var rdEmail = $("#rdEmail").val().trim();
		var rdNative = $("#rdNative").val().trim();
		var rdInterest = $("#rdInterest").val().trim();
		var rdRemark = $("#rdRemark").val();
		var cardId = $("#cardId").val();
		//基本信息end
		
		var newRdid = $("#newRdid").val();
		var paySign = $("#paySign").val();
		
		$("#cardOp").attr("disabled", "true");
		$("#cardDrop").attr("disabled", "true");
		$("#cardOp").html("操作中...");
		
		var params = {rdId : rdId,rdCFState : rdCFState,rdType : rdType,rdGlobal : rdGlobal,rdLibType : rdLibType,
				rdStartDate : rdStartDate,rdEndDate : rdEndDate,rdInTime : rdInTime,rdLib : rdLib,rdSort1 : rdSort1,
				rdSort2 : rdSort2,rdSort3 : rdSort3,rdSort4 : rdSort4,rdSort5 : rdSort5,rdName : rdName,
				rdPasswd : rdPasswd,rdCertify : rdCertify,rdSex : rdSex,rdNation : rdNation,rdBornDate : rdBornDate,
				rdAge : rdAge,rdLoginId : rdLoginId,rdPhone : rdPhone,rdUnit : rdUnit,rdAddress : rdAddress,
				rdPostCode : rdPostCode,rdEmail : rdEmail,rdNative : rdNative,rdInterest : rdInterest, paySign:paySign, 
				libUser:libUser, rdRemark:rdRemark, newRdid: newRdid, cardId : cardId,oldRdpasswd: oldRdpasswd};
		$.ajax({
			type : "POST",
			url : "<c:url value='/admin/reader/repair' />",
			data : params,
			dataType : "json",
			success : function(jsonData){
				var success = jsonData.success;
				if(success == 0) {
					var checkfee = jsonData.checkfee;
					var servicefee = jsonData.servicefee;
					var idfee = jsonData.idfee;
					var totalfee = jsonData.totalfee;
					var tip = "";
					if(checkfee != 0) {
						tip += " 验证费： " + checkfee + "元";
					}
					if(servicefee != 0) {
						tip += " 服务费 ：" + servicefee + "元";
					}
					if(idfee != 0) {
						tip += " 工本费 ：" + idfee + "元";
					}
					if(totalfee != 0) {
						tip += " 共应交" + totalfee + "元。";
						if(confirm(tip)) {
							$("#paySign").val("1");
							var inputRdid = prompt("请输入新读者证号，系统默认如下：", rdId + "P"); //将输入的内容赋给变量 name ，  
					        if (inputRdid == "") {
					        	showResult("新证号不能是空");
					        	$("#cardOp").removeAttr("disabled");
								$("#cardDrop").removeAttr("disabled");
								$("#cardOp").html("证操作");
								$("#paySign").val("0");
					        	return;
					        } else if(inputRdid == null) {
					        	$("#cardOp").removeAttr("disabled");
								$("#cardDrop").removeAttr("disabled");
								$("#cardOp").html("证操作");
								$("#paySign").val("0");
					        	return;
					        }
					        $("#newRdid").val(inputRdid);
							repair();
							$("#paySign").val("0");
							$("#newRdid").val("");
							isContinue = false;
						} else {
							$("#cardOp").removeAttr("disabled");
							$("#cardDrop").removeAttr("disabled");
							$("#cardOp").html("证操作");
							$("#paySign").val("0");
						}
					} else {
						$("#paySign").val("1");
						var inputRdid = prompt("请输入新读者证号，系统默认如下：",  rdId + "P");
				        if (inputRdid == "") {
				        	showResult("新证号不能是空");
				        	$("#cardOp").removeAttr("disabled");
							$("#cardDrop").removeAttr("disabled");
							$("#cardOp").html("证操作");
							$("#paySign").val("0");
				        	return;
				        } else if(inputRdid == null) {
				        	$("#cardOp").removeAttr("disabled");
							$("#cardDrop").removeAttr("disabled");
							$("#cardOp").html("证操作");
							$("#paySign").val("0");
				        	return;
				        }
				        $("#newRdid").val(inputRdid);
						repair();
						$("#paySign").val("0");
						$("#newRdid").val("");
												
						isContinue = false;
					}
				} else if(success == -1) {
					var message = jsonData.message;
					showResult(message);
					$("#paySign").val("0");
					$("#newRdid").val("");
					isContinue = false;

					$("#cardOp").removeAttr("disabled");
					$("#cardDrop").removeAttr("disabled");
					$("#cardOp").html("证操作");
					return;
				} else if(success == 2) {
					showResult("该证不需要补办!");
					$("#paySign").val("0");
					$("#newRdid").val("");
					isContinue = false;
					$("#cardOp").removeAttr("disabled");
					$("#cardDrop").removeAttr("disabled");
					$("#cardOp").html("证操作");
					return;
				} else if(success == -2) {
					callBackToCard(newRdid);
					alert("证操作成功!同步失败");
					isContinue = false;
					window.location.href = "<c:url value='/admin/reader/add/"+newRdid+"' />";
					$("#paySign").val("0");
					$("#newRdid").val("");
					return;
				} else {
					callBackToCard(newRdid);
					alert("证操作成功!");
					isContinue = false;
					window.location.href = "<c:url value='/admin/reader/add/"+newRdid+"' />";
					$("#paySign").val("0");
					$("#newRdid").val("");
					return;
				}
				
			},
			cache: false
		});
	}
	
	/**
	2014-12-11
	卡补办
	*/
	function cardRepair(){
		var rdId = $("#rdId").val();
		if(rdId == "") {
			showResult("请先查询读者信息!");
			$("#cardOp").removeAttr("disabled");
			$("#cardDrop").removeAttr("disabled");
			$("#cardOp").html("证操作");
			return;
		}
		var rdCFState=$("#rdCFState").attr("value");
		if(!(rdCFState==3 || rdCFState==5)){
			showResult("只有挂失、注销状态的读者才能补办读者证！");return;
		}
		if(rdCFState==5){
			if(!isContinue) {
				if(!confirm("该证已注销，请检查是否已经补办读者证, 是否继续补办？"))return;
				isContinue = true;
			}
		}
			
		//基本信息start
		var rdType = $("#rdType").val();
		var rdGlobal;
		var rdLibType;
		if($("#rdGlobal").attr("checked")){
			rdGlobal = 1;
			rdLibType = $("#rdLibType").val();
			if(!rdLibType){
				tip = "请选择馆际流通类型！";
				showResult(tip);
				$("#saveButton").removeAttr("disabled");
				$("#saveButton").html("保存");
				return;
			}
		}else{
			rdGlobal = 0;
			rdLibType = "";
		}
		var libUser;
		if($("#libUser").attr("checked")) {
			libUser = 1;
		} else {
			libUser = 0;
		}
		var rdStartDate = $("#rdStartDate").val();
		
		var rdEndDate = $("#rdEndDate").val();
		var rdInTime = $("#rdInTime").val();
		var rdLib = $("#rdLib").val();
		if(!rdLib){
			tip = "请选择开户馆！";
			showResult(tip);
			$("#saveButton").removeAttr("disabled");
			$("#saveButton").html("保存");
			return;
		}
		var rdSort1 = $("#rdSort1").val();
		var rdSort2 = $("#rdSort2").val();
		var rdSort3 = $("#rdSort3").val();
		var rdSort4 = $("#rdSort4").val();
		var rdSort5 = $("#rdSort5").val();
		
		var rdName = $("#rdName").val().trim();
		if(!rdName){
			tip = "请填写姓名！";
			showResult(tip);
			$("#saveButton").removeAttr("disabled");
			$("#saveButton").html("保存");
			return;
		}
		var rdPasswd = $("#rdPasswd").val();
		var oldRdpasswd = $("#oldRdpasswd").val();//ADD 2014-05-14

		var rdCertify = $("#rdCertify").val().trim();

		var rdSex = $("#rdSex").val();
		var rdNation = $("#rdNation").val().trim();
		var rdBornDate = $("#rdBornDate").val();
		var rdAge = $("#rdAge").val();
		var rdLoginId = $("#rdLoginId").val().trim();
		var rdPhone = $("#rdPhone").val().trim();
		var rdUnit = $("#rdUnit").val().trim();
		var rdAddress = $("#rdAddress").val().trim();
		var rdPostCode = $("#rdPostCode").val().trim();
		var rdEmail = $("#rdEmail").val().trim();
		var rdNative = $("#rdNative").val().trim();
		var rdInterest = $("#rdInterest").val().trim();
		var rdRemark = $("#rdRemark").val();
		var cardId = $("#cardId").val();
		//基本信息end
		
		var newRdid = $("#newRdid").val();
		var paySign = $("#paySign").val();
		var cardType = 2;
		var newCardid = $("#newCardid").val();
		
		$("#cardOp").attr("disabled", "true");
		$("#cardDrop").attr("disabled", "true");
		$("#cardOp").html("操作中...");
		var params = {rdId : rdId,rdCFState : rdCFState,rdType : rdType,rdGlobal : rdGlobal,rdLibType : rdLibType,
				rdStartDate : rdStartDate,rdEndDate : rdEndDate,rdInTime : rdInTime,rdLib : rdLib,rdSort1 : rdSort1,
				rdSort2 : rdSort2,rdSort3 : rdSort3,rdSort4 : rdSort4,rdSort5 : rdSort5,rdName : rdName,
				rdPasswd : rdPasswd,rdCertify : rdCertify,rdSex : rdSex,rdNation : rdNation,rdBornDate : rdBornDate,
				rdAge : rdAge,rdLoginId : rdLoginId,rdPhone : rdPhone,rdUnit : rdUnit,rdAddress : rdAddress,
				rdPostCode : rdPostCode,rdEmail : rdEmail,rdNative : rdNative,rdInterest : rdInterest, paySign:paySign, 
				libUser:libUser, rdRemark:rdRemark, newRdid: newRdid, cardId : cardId,oldRdpasswd: oldRdpasswd,
				newCardid:newCardid,cardType:cardType};
		
		$.ajax({
			type : "POST",
			url : "<c:url value='/admin/reader/cardRepair' />",
			data : params,
			dataType : "json",
			success : function(jsonData){
				var success = jsonData.success;
				if(success == 0) {
					var checkfee = jsonData.checkfee;
					var servicefee = jsonData.servicefee;
					var idfee = jsonData.idfee;
					var totalfee = jsonData.totalfee;
					var tip = "";
					if(checkfee != 0) {
						tip += " 验证费： " + checkfee + "元";
					}
					if(servicefee != 0) {
						tip += " 服务费 ：" + servicefee + "元";
					}
					if(idfee != 0) {
						tip += " 工本费 ：" + idfee + "元";
					}
					if(totalfee != 0) {
						tip += " 共应交" + totalfee + "元。";
						if(confirm(tip)) {
							$("#paySign").val("1");
							newCardid = prompt("请输入该读者新卡号：", ""); //更换的新卡号，证号不变
							if(newCardid == ""){
								showResult("新卡号不能是空");
								$("#cardOp").removeAttr("disabled");
								$("#cardDrop").removeAttr("disabled");
								$("#cardOp").html("证操作");
								$("#paySign").val("0");
					        	return;
							}else if(newCardid == null){
								$("#cardOp").removeAttr("disabled");
								$("#cardDrop").removeAttr("disabled");
								$("#cardOp").html("证操作");
								$("#paySign").val("0");
								return;
							}
							$("#newCardid").val(newCardid);
							cardRepair();
							$("#paySign").val("0");
							$("#newCardid").val("");
							isContinue = false;
						} else {
							$("#cardOp").removeAttr("disabled");
							$("#cardDrop").removeAttr("disabled");
							$("#cardOp").html("证操作");
							$("#paySign").val("0");
						}
					} else {
						$("#paySign").val("1");
						newCardid = prompt("请输入该读者新卡号：", ""); //更换的新卡号，证号不变
						if(newCardid == ""){
							showResult("新卡号不能是空");
							$("#cardOp").removeAttr("disabled");
							$("#cardDrop").removeAttr("disabled");
							$("#cardOp").html("证操作");
							$("#paySign").val("0");
				        	return;
						}else if(newCardid == null){
							$("#cardOp").removeAttr("disabled");
							$("#cardDrop").removeAttr("disabled");
							$("#cardOp").html("证操作");
							$("#paySign").val("0");
							return;
						}
						cardRepair();
						$("#paySign").val("0");
						$("#newCardid").val("");
						
						isContinue = false;
					}
				} else if(success == -1) {
					var message = jsonData.message;
					showResult(message);
					$("#paySign").val("0");
					$("#newCardid").val("");
					isContinue = false;

					$("#cardOp").removeAttr("disabled");
					$("#cardDrop").removeAttr("disabled");
					$("#cardOp").html("证操作");
					return;
				} else if(success == 2) {
					showResult("该证不需要恢复!");
					$("#paySign").val("0");
					$("#newCardid").val("");
					isContinue = false;
					$("#cardOp").removeAttr("disabled");
					$("#cardDrop").removeAttr("disabled");
					$("#cardOp").html("证操作");
					return;
				} else if(success == -2) {
					alert("证操作成功!同步失败");
					isContinue = false;
					window.location.href = "<c:url value='/admin/reader/add/"+rdId+"' />";
					$("#paySign").val("0");
					$("#newCardid").val("");
					return;
				} else {
					alert("证操作成功!");
					isContinue = false;
					window.location.href = "<c:url value='/admin/reader/add/"+rdId+"' />";
					$("#paySign").val("0");
					$("#newCardid").val("");
					return;
				}
				
			},
			cache: false
		});
	}
	
	//换证
	function change(){
		var rdId = $("#rdId").val();
		if(rdId == "") {
			showResult("请先查询读者信息!");
			$("#cardOp").removeAttr("disabled");
			$("#cardDrop").removeAttr("disabled");
			$("#cardOp").html("证操作");
			return;
		}
		
		var rdCFState=$("#rdCFState").attr("value");
		
		//基本信息start
		var rdType = $("#rdType").val();
		var rdGlobal;
		var rdLibType;
		if($("#rdGlobal").attr("checked")){
			rdGlobal = 1;
			rdLibType = $("#rdLibType").val();
			if(!rdLibType){
				tip = "请选择馆际流通类型！";
				showResult(tip);
				$("#saveButton").removeAttr("disabled");
				$("#saveButton").html("保存");
				return;
			}
		}else{
			rdGlobal = 0;
			rdLibType = "";
		}
		var libUser;
		if($("#libUser").attr("checked")) {
			libUser = 1;
		} else {
			libUser = 0;
		}
		var rdStartDate = $("#rdStartDate").val();
		
		var rdEndDate = $("#rdEndDate").val();
		var rdInTime = $("#rdInTime").val();
		var rdLib = $("#rdLib").val();
		if(!rdLib){
			tip = "请选择开户馆！";
			showResult(tip);
			$("#saveButton").removeAttr("disabled");
			$("#saveButton").html("保存");
			return;
		}
		var rdSort1 = $("#rdSort1").val();
		var rdSort2 = $("#rdSort2").val();
		var rdSort3 = $("#rdSort3").val();
		var rdSort4 = $("#rdSort4").val();
		var rdSort5 = $("#rdSort5").val();
		
		var rdName = $("#rdName").val().trim();
		if(!rdName){
			tip = "请填写姓名！";
			showResult(tip);
			$("#saveButton").removeAttr("disabled");
			$("#saveButton").html("保存");
			return;
		}
		var rdPasswd = $("#rdPasswd").val();
		var oldRdpasswd = $("#oldRdpasswd").val();//add 2014-05-14

		var rdCertify = $("#rdCertify").val().trim();

		var rdSex = $("#rdSex").val();
		var rdNation = $("#rdNation").val().trim();
		var rdBornDate = $("#rdBornDate").val();
		var rdAge = $("#rdAge").val();
		var rdLoginId = $("#rdLoginId").val().trim();
		var rdPhone = $("#rdPhone").val().trim();
		var rdUnit = $("#rdUnit").val().trim();
		var rdAddress = $("#rdAddress").val().trim();
		var rdPostCode = $("#rdPostCode").val().trim();
		var rdEmail = $("#rdEmail").val().trim();
		var rdNative = $("#rdNative").val().trim();
		var rdInterest = $("#rdInterest").val().trim();
		var rdRemark = $("#rdRemark").val();
		var cardId = $("#cardId").val();
		//基本信息end
		
		var newRdid = $("#newRdid").val();
		var paySign = $("#paySign").val();
		
		$("#cardOp").attr("disabled", "true");
		$("#cardDrop").attr("disabled", "true");
		$("#cardOp").html("操作中...");
		
		var params = {rdId : rdId,rdCFState : rdCFState,rdType : rdType,rdGlobal : rdGlobal,rdLibType : rdLibType,
				rdStartDate : rdStartDate,rdEndDate : rdEndDate,rdInTime : rdInTime,rdLib : rdLib,rdSort1 : rdSort1,
				rdSort2 : rdSort2,rdSort3 : rdSort3,rdSort4 : rdSort4,rdSort5 : rdSort5,rdName : rdName,
				rdPasswd : rdPasswd,rdCertify : rdCertify,rdSex : rdSex,rdNation : rdNation,rdBornDate : rdBornDate,
				rdAge : rdAge,rdLoginId : rdLoginId,rdPhone : rdPhone,rdUnit : rdUnit,rdAddress : rdAddress,
				rdPostCode : rdPostCode,rdEmail : rdEmail,rdNative : rdNative,rdInterest : rdInterest, paySign:paySign, 
				libUser:libUser, rdRemark:rdRemark, newRdid: newRdid, cardId:cardId,oldRdpasswd: oldRdpasswd};
		$.ajax({
			type : "POST",
			url : "<c:url value='/admin/reader/change' />",
			data : params,
			dataType : "json",
			success : function(jsonData){
				var success = jsonData.success;
				if(success == 0) {
					var checkfee = jsonData.checkfee;
					var servicefee = jsonData.servicefee;
					var idfee = jsonData.idfee;
					var totalfee = jsonData.totalfee;
					var tip = "";
					if(checkfee != 0) {
						tip += " 验证费： " + checkfee + "元";
					}
					if(servicefee != 0) {
						tip += " 服务费 ：" + servicefee + "元";
					}
					if(idfee != 0) {
						tip += " 工本费 ：" + idfee + "元";
					}
					if(totalfee != 0) {
						tip += " 共应交" + totalfee + "元。";
						if(confirm(tip)) {
							$("#paySign").val("1");
							var inputRdid = prompt("请输入新读者证号，系统默认如下：", rdId + "P"); //将输入的内容赋给变量 name ，  
					        if (inputRdid == "") {
					        	showResult("新证号不能是空");
					        	$("#cardOp").removeAttr("disabled");
								$("#cardDrop").removeAttr("disabled");
								$("#cardOp").html("证操作");
								$("#paySign").val("0");
					        	return;
					        } else if(inputRdid == null) {
					        	$("#cardOp").removeAttr("disabled");
								$("#cardDrop").removeAttr("disabled");
								$("#cardOp").html("证操作");
								$("#paySign").val("0");
					        	return;
					        }
					        $("#newRdid").val(inputRdid);
					        change();
							$("#paySign").val("0");
							$("#newRdid").val("");
						} else {
							$("#cardOp").removeAttr("disabled");
							$("#cardDrop").removeAttr("disabled");
							$("#cardOp").html("证操作");
						}
					} else {
						$("#paySign").val("1");
						var inputRdid = prompt("请输入新读者证号，系统默认如下：", rdId + "P");
				        if (inputRdid == "") {
				        	showResult("新证号不能是空");
				        	$("#cardOp").removeAttr("disabled");
							$("#cardDrop").removeAttr("disabled");
							$("#cardOp").html("证操作");
							$("#paySign").val("0");
				        	return;
				        } else if(inputRdid == null) {
				        	$("#cardOp").removeAttr("disabled");
							$("#cardDrop").removeAttr("disabled");
							$("#cardOp").html("证操作");
							$("#paySign").val("0");
				        	return;
				        }
				        $("#newRdid").val(inputRdid);
						change();
						$("#paySign").val("0");
						$("#newRdid").val("");
					}
				} else if(success == -1) {
					var message = jsonData.message;
					showResult(message);
					$("#paySign").val("0");
					$("#newRdid").val("");

					$("#cardOp").removeAttr("disabled");
					$("#cardDrop").removeAttr("disabled");
					$("#cardOp").html("证操作");
					return;
				} else if(success == 2) {
					$("#paySign").val("0");
					$("#newRdid").val("");
					$("#cardOp").removeAttr("disabled");
					$("#cardDrop").removeAttr("disabled");
					$("#cardOp").html("证操作");
					return;
				} else if(success == -2) {
					callBackToCard(newRdid);
					alert("证操作成功!同步失败");
					window.location.href = "<c:url value='/admin/reader/add/"+newRdid+"' />";
					$("#paySign").val("0");
					$("#newRdid").val("");
					return;
				} else {
					callBackToCard(newRdid);
					alert("证操作成功!");
					window.location.href = "<c:url value='/admin/reader/add/"+newRdid+"' />";
					$("#paySign").val("0");
					$("#newRdid").val("");
					return;
				}
				
			},
			cache: false
		});
	}
	
	//延期
	function defer() {
		var rdCFState = $("#rdCFState").val();
		if(rdCFState==5){
			if(!isContinue) {
				showResult("注销的读者证不能延期！");return;
				//if(!confirm("该证已注销，请检查是否已经被注销, 是否继续注销？"))return;
				//isContinue = true;
			}
		}
		var rdId = $("#rdId").val();
		if(rdId == "") {
			showResult("请先查询读者信息!");
			$("#cardOp").removeAttr("disabled");
			$("#cardDrop").removeAttr("disabled");
			$("#cardOp").html("证操作");
			return;
		}
		var rdCFState=$("#rdCFState").attr("value");
		
		//基本信息start
		var rdType = $("#rdType").val();
		var rdGlobal;
		var rdLibType;
		if($("#rdGlobal").attr("checked")){
			rdGlobal = 1;
			rdLibType = $("#rdLibType").val();
			if(!rdLibType){
				tip = "请选择馆际流通类型！";
				showResult(tip);
				$("#saveButton").removeAttr("disabled");
				$("#saveButton").html("保存");
				return;
			}
		}else{
			rdGlobal = 0;
			rdLibType = "";
		}
		var libUser;
		if($("#libUser").attr("checked")) {
			libUser = 1;
		} else {
			libUser = 0;
		}
		var rdStartDate = $("#rdStartDate").val();
		
		var rdEndDate = $("#rdEndDate").val();
		var rdInTime = $("#rdInTime").val();
		var rdLib = $("#rdLib").val();
		if(!rdLib){
			tip = "请选择开户馆！";
			showResult(tip);
			$("#saveButton").removeAttr("disabled");
			$("#saveButton").html("保存");
			return;
		}
		var rdSort1 = $("#rdSort1").val();
		var rdSort2 = $("#rdSort2").val();
		var rdSort3 = $("#rdSort3").val();
		var rdSort4 = $("#rdSort4").val();
		var rdSort5 = $("#rdSort5").val();
		
		var rdName = $("#rdName").val().trim();
		if(!rdName){
			tip = "请填写姓名！";
			showResult(tip);
			$("#saveButton").removeAttr("disabled");
			$("#saveButton").html("保存");
			return;
		}
		var rdPasswd = $("#rdPasswd").val();
		var oldRdpasswd = $("#oldRdpasswd").val();// 2014-05-14

		var rdCertify = $("#rdCertify").val().trim();

		var rdSex = $("#rdSex").val();
		var rdNation = $("#rdNation").val().trim();
		var rdBornDate = $("#rdBornDate").val();
		var rdAge = $("#rdAge").val();
		var rdLoginId = $("#rdLoginId").val().trim();
		var rdPhone = $("#rdPhone").val().trim();
		var rdUnit = $("#rdUnit").val().trim();
		var rdAddress = $("#rdAddress").val().trim();
		var rdPostCode = $("#rdPostCode").val().trim();
		var rdEmail = $("#rdEmail").val().trim();
		var rdNative = $("#rdNative").val().trim();
		var rdInterest = $("#rdInterest").val().trim();
		var rdRemark = $("#rdRemark").val();
		//基本信息end
		var paySign = $("#paySign").val();
		var deferDate = $("#deferDate").val();
		
		$("#cardOp").attr("disabled", "true");
		$("#cardDrop").attr("disabled", "true");
		$("#cardOp").html("操作中...");
		
		var params = {rdId : rdId,rdCFState : rdCFState,rdType : rdType,rdGlobal : rdGlobal,rdLibType : rdLibType,
				rdStartDate : rdStartDate,rdEndDate : rdEndDate,rdInTime : rdInTime,rdLib : rdLib,rdSort1 : rdSort1,
				rdSort2 : rdSort2,rdSort3 : rdSort3,rdSort4 : rdSort4,rdSort5 : rdSort5,rdName : rdName,
				rdPasswd : rdPasswd,rdCertify : rdCertify,rdSex : rdSex,rdNation : rdNation,rdBornDate : rdBornDate,
				rdAge : rdAge,rdLoginId : rdLoginId,rdPhone : rdPhone,rdUnit : rdUnit,rdAddress : rdAddress,
				rdPostCode : rdPostCode,rdEmail : rdEmail,rdNative : rdNative,rdInterest : rdInterest, paySign:paySign, 
				libUser:libUser, rdRemark:rdRemark, deferDate:deferDate,oldRdpasswd:oldRdpasswd};
		$.ajax({
			type : "POST",
			url : "<c:url value='/admin/reader/defer' />",
			data : params,
			dataType : "json",
			success : function(jsonData){
				var success = jsonData.success;
				if(success == 0) {
					var checkfee = jsonData.checkfee;
					var servicefee = jsonData.servicefee;
					var idfee = jsonData.idfee;
					var totalfee = jsonData.totalfee;
					var tip = "";
					if(checkfee != 0) {
						tip += " 验证费： " + checkfee + "元";
					}
					if(servicefee != 0) {
						tip += " 服务费 ：" + servicefee + "元";
					}
					if(idfee != 0) {
						tip += " 工本费 ：" + idfee + "元";
					}
					if(totalfee != 0) {
						tip += " 共应交" + totalfee + "元。";
						if(confirm(tip)) {
							$("#paySign").val("1");
							var inputDefer = prompt("请输入延期终止日期，格式YYYY-MM-DD，系统默认为：当前日期+读者有效期：", rdEndDate);
					        if (inputDefer == "") {
					        	showResult("终止日期不能是空");
					        	$("#cardOp").removeAttr("disabled");
								$("#cardDrop").removeAttr("disabled");
								$("#cardOp").html("证操作");
								$("#paySign").val("0");
					        	return;
					        } else if(inputDefer == null) {
					        	$("#cardOp").removeAttr("disabled");
								$("#cardDrop").removeAttr("disabled");
								$("#cardOp").html("证操作");
								$("#paySign").val("0");
					        	return;
					        }
					        $("#deferDate").val(inputDefer);
					        defer();
							$("#paySign").val("0");
							$("#deferDate").val("");
						} else {
							$("#cardOp").removeAttr("disabled");
							$("#cardDrop").removeAttr("disabled");
							$("#cardOp").html("证操作");
						}
					} else {
						$("#paySign").val("1");
						var inputDefer = prompt("请输入延期终止日期，格式YYYY-MM-DD，系统默认为：当前日期+读者有效期：", rdEndDate);
				        if (inputDefer == "") {
				        	showResult("终止日期不能是空");
				        	return;
				        } else if(inputDefer == null) {
				        	return;
				        }
				        $("#deferDate").val(inputDefer);
						defer();
						$("#paySign").val("0");
						$("#deferDate").val("");
					}
				} else if(success == -1) {
					var message = jsonData.message;
					showResult(message);
					$("#paySign").val("0");
					$("#deferDate").val("");

					$("#cardOp").removeAttr("disabled");
					$("#cardDrop").removeAttr("disabled");
					$("#cardOp").html("证操作");
					return;
				} else if(success == -2) {
					alert("证操作成功!同步失败");
					window.location.href = "<c:url value='/admin/reader/add/"+rdId+"' />";
					$("#paySign").val("0");
					$("#deferDate").val("");
					return;
				} else {
					alert("证操作成功!");
					window.location.href = "<c:url value='/admin/reader/add/"+rdId+"' />";
					$("#paySign").val("0");
					$("#deferDate").val("");
					return;
				}
				
			},
			cache: false
		});
	}
	
	function checkRDID(rdId,val) {
		rdId.value = val.replace(/[\W]/g,'');
	}
	
	function checkIdentity(identity, val) {
		identity.value = val.replace(/[\W]/g,'');
	}
	
    function parseID(idObj, code) {
    	//var idObj = document.getElementById("rdBornDate");
    	//var code = document.getElementById("rdCertify").value.trim();
    	var theEvent = window.event;
		var key = theEvent.keyCode || theEvent.which || theEvent.charCode;
		if (key != 13) {
			return false;
		}
		var city={11:"北京",12:"天津",13:"河北",14:"山西",15:"内蒙古",21:"辽宁",22:"吉林",23:"黑龙江 ",31:"上海",32:"江苏",33:"浙江",34:"安徽",35:"福建",36:"江西",37:"山东",41:"河南",42:"湖北 ",43:"湖南",44:"广东",45:"广西",46:"海南",50:"重庆",51:"四川",52:"贵州",53:"云南",54:"西藏 ",61:"陕西",62:"甘肃",63:"青海",64:"宁夏",65:"新疆",71:"台湾",81:"香港",82:"澳门",91:"国外 "};
		var tip = "";
		var pass= true;
		if(!code || !/^\d{6}(18|19|20)?\d{2}(0[1-9]|1[12])(0[1-9]|[12]\d|3[01])\d{3}(\d|X)$/i.test(code)){
			tip = "身份证号格式错误！";
			pass = false;
		}else if(!city[code.substr(0,2)]){
			tip = "身份证地址编码错误！";
			pass = false;
		}else{
			//18位身份证需要验证最后一位校验位
			if(code.length == 18){
				code = code.split('');
                //∑(ai×Wi)(mod 11)
                //加权因子
                var factor = [ 7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2 ];
                //校验位
                var parity = [ 1, 0, 'X', 9, 8, 7, 6, 5, 4, 3, 2 ];
                var sum = 0;
                var ai = 0;
                var wi = 0;
                for (var i = 0; i < 17; i++){
                    ai = code[i];
                    wi = factor[i];
                    sum += ai * wi;
                }
                //var last = parity[sum % 11];
                if(parity[sum % 11] != code[17]){
                    tip = "身份证校验位错误！";
                    pass =false;
                }
            }
        }
		if(!pass){
			document.getElementById("rdBornDate").value = "";
			document.getElementById("rdAge").value = "";
        	showResult(tip);
		}else{
        	var year = code[6]+code[7]+code[8]+code[9];
        	var month = code[10]+code[11];
        	var day = code[12]+code[13];
        	var birthday = year + "-" + month + "-"+day;
        	document.getElementById("rdBornDate").value = birthday;
        	caculateAge(birthday);
        	return false;
        }
    }
    
	function isEnter() {
		var theEvent = window.event;
		var code = theEvent.keyCode || theEvent.which || theEvent.charCode;
		if (code == 13) {
			searchReader();
			return false;
		}
		return true;
	}
	//上传图片后的回调函数
	function callback2(msg,rdId) {//callback 这个函数名会影响读卡回调函数，同一个名字，所以更改为callback2 
		if(msg == "error" || msg == "ohno"){
			alert("图片上传失败！");
			return false;
		}else{
			alert("保存成功！");
			window.location.href = "<c:url value='/admin/reader/add/"+rdId+"' />";
			$("#saveButton").removeAttr("disabled");
			$("#saveButton").html("保存");
		}
	}
	
	//计算年龄
	function caculateAge(birthday) {
		if(!birthday){
			return;
		}
		birthday=new Date(birthday.replace(/-/g, "\/")); 
		var d=new Date(); 
		var age = d.getFullYear()-birthday.getFullYear()-((d.getMonth()<birthday.getMonth()|| d.getMonth()==birthday.getMonth() && d.getDate()<birthday.getDate())?1:0);
		document.getElementById("rdAge").value = age;
	}
	
	//提示信息
	function showResult(tip) {
		$("#tip").css("display", "block").html(tip);
		setTimeout(function(){
			$("#tip").fadeOut("slow");
		},5000);
		$("#tip").attr("class", "A_MESSAGEBOX_LOADING alert alert-success");
		return;
	}
	
	//读卡信息，并回填到页面
	function readerCard() {
		var readCcard_URL_init = "http://127.0.0.1:7777";
		try {
			$.ajax({
				url : readCcard_URL_init,
				dataType : 'jsonp',  
				jsonp : 'callback',
				jsonpCallback:'callback',
				cache : false,
				async : false,
				success : function(jsonData) {
					readeCard_callback(jsonData);//调用读卡回填到页面
				}
			});
		} catch (e) {
			alert("ER:连接一卡通读卡接口失败。！" + e.message );
		}
	}
	//读卡回填到页面
	function readeCard_callback(jsonData){
		var success = jsonData.success;
		if(success == true) {
			var strName = jsonData.strName;
			var strSex = jsonData.strSex;
			var strNation = jsonData.strNation;
			var strBirth = jsonData.strBirth;
			var strID = jsonData.strID;
			var strAddress = jsonData.strAddress;
			var strStartDate = jsonData.strStartDate;
			var strEndDate = jsonData.strEndDate;
			var strImage = jsonData.strImage;
			if(strImage!=""){
				$("#fileSrc").attr("value", strImage);
				strImage = "data:image/bmp;base64,"+strImage;
			}
			$("#rdName").attr("value", strName);
			$("#rdSex").attr("value", strSex=='男'?'1':'0');
			$("#rdNation").attr("value", strNation);
			$("#rdBornDate").attr("value", strBirth);
			$("#rdCertify").attr("value", strID);
			$("#rdAddress").attr("value", strAddress);
			$("#avatar").attr("src",strImage);
		} else {
			var message = jsonData.message;
			alert(message);
		}
	}
	//写卡请求
	function callBackToCard(rdId) {
		console.log("写卡...");
		var readCcard_URL_init = "http://127.0.0.1:7777/pay";
		try {
			$.ajax({
				url : readCcard_URL_init,
				data : {rdno : rdId},
				dataType : 'jsonp',  
				jsonp : 'writeCardCallback',
				jsonpCallback:'writeCardCallback',
				cache : false,
				async : false,
				success : function(jsonData) {
					//{"success":true,"snrcardnum":"1E652F57","message":"写卡成功."}
					//{"success":false,"snrcardnum":"","message":"写入失败."}
					var success = jsonData.success;
					if(success) {
						var cardId = jsonData.snrcardnum;//物理卡号要写进自己系统
						bindCardAndRdid(rdId, cardId);
					}
				}
			});
		} catch (e) {
			alert("ER:连接一卡通写卡失败。！");
			var erMsg = "写卡连接失败！";
			alert(erMsg);
		}
	}
	function bindCardAndRdid(rdId, cardId) {
		var params = {rdId: rdId, cardId: cardId};
		$.ajax({
			type : "POST",
			url : "<c:url value='/admin/sys/card/bindCardAndRdid' />",
			data : params,
			dataType : "json",
			success : function(jsonData){
				alert("回填账号给读卡端成功！读者账号："+rdId+",返回的信息："+jsonData.message);
			},
			cache: false
		});
	}
	function writeCard() {
		var rdId = $("#rdId").val();
		if(!rdId){
			tip = "请填写证号！";
			showResult(tip);
			return;
		}
		callBackToCard(rdId);
	}
</script>
