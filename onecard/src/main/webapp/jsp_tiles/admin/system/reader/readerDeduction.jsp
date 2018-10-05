<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/jsp_tiles/include/taglibs.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<c:set var="READER_URL">
	<c:url value="/admin/reader" />
</c:set>
<script type="text/javascript" src="<c:url value='/media/datepicker/WdatePicker.js' />"></script>
<script type="text/javascript">
	var ACTION = "saveReaderDeduction";

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
		}
	];
</script>
<script type="text/javascript">
	var IMAGETYPES = [".JPG",".JPEG",".PNG",".BMP",".GIF"];
	
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

	//点击重置重新设置默认值
	function resetDefaultValue() {
		setTimeout(function(){
			window.location.href = "<c:url value='/admin/reader/deduction' />";//重置下访问的路径 2014-11-10
			document.getElementById("rdId").disabled = "";
			document.getElementById("avatar").src = "<c:url value='/admin/reader/showAvatar/defaultPhoto' />";
			isAvatarChanged = false;
		},1);
	}
	


	function saveReaderDeduction() {
		var rdid = $("#rdId").val();
		var cardid = $("#cardId").val();
		var rdName = $("rdName").val();
		var prepay = $("#prepay").val();//余额
		var appcode = $("#appcode").val();
		var fintype = $("#fintype").val();
		var fee = $("#fee").val();//扣费金额
				
		//读者证号必须存在
		if(rdid==""){
			showResult("请先查询读者信息!");
			return;
		}
		//余额和扣费比较判断
		if(prepay == ""){
			$("#showResult").show();
			$("#detail").html("该账号余额不足，请先充值!");
			return;
		}
		if(fee == ""){
			showResult("请填写扣费金额!");
			return;
		}
		if(Number(fee) > Number(prepay)){
			$("#showResult").show();
			$("#detail").html("该账号余额不足，请先充值!");
			return;
		}
		
		//添加显示提示框信息处理
		var showDiv = $('#showMessageDiv');
		showDiv.show();
		$("#showPrepay").html(prepay+"元");
		$("#showFee").html(fee+"元");
		
		$('#cancelButton').click(function(){//取消按钮
			$("#showFee").html("");
			fee="";
			showDiv.hide();
		});
		//弹出框关闭按钮
		$("#closeA").click(function(){
			$("#showFee").html("");
			fee="";
			showDiv.hide();
		});
		
	}
	
	//2015-03-19
	function confirmSaveDeduction(){
		var rdid = $("#rdId").val();
		var cardid = $("#cardId").val();
		var prepay = $("#prepay").val();//余额
		var appcode = $("#appcode").val();
		var fintype = $("#fintype").val();
		var fee = $("#fee").val();//扣费金额
		var showDiv = $('#showMessageDiv');
			showDiv.hide();
			//提交数据信息
			//扣费操作 start
			$.ajax({
				type : "POST",
				url : "<c:url value='/admin/reader/saveReaderDeduction' />",
				data : {rdid : rdid,cardid:cardid,prepay:prepay,appcode:appcode,feetype:fintype,fee:fee},
				dataType : "json",
				success : function(jsonData){
					
					$("#showResult").show();
					$("#detail").html(jsonData.message);
					
					fee = 0;
					if(jsonData.success == "1"){
						document.getElementById("fieldValue").value=rdid;
						document.getElementById("fieldName").value="rdId";
						findReaderDeduction();
					}
				},
				error : function(jsonData){
					showResult("获取数据失败！");
					return;
				}
			});//扣费操作end
	}
	function hiddenConfirm() {
		$("#showResult").hide();
	}
	var isAvatarChanged = false;
	
	function initReader(reader, isNew) {
		document.getElementById("rdId").value  = reader.rdId;
		document.getElementById("avatar").src = "<c:url value='/admin/reader/showAvatar/' />"+reader.rdId;
		document.getElementById("rdName").value = reader.rdName;
		document.getElementById("cardId").value = reader.cardId;
		document.getElementById("prepay").value = reader.prepay;
		if(reader.rdCFState == 1) {
			document.getElementById("rdcfstate").value = "有效";
		} else if(reader.rdCFState == 3) {
			document.getElementById("rdcfstate").value = "挂失";
		} else if(reader.rdCFState == 4) {
			document.getElementById("rdcfstate").value = "暂停";
		} else if(reader.rdCFState == 5) {
			document.getElementById("rdcfstate").value = "注销";
		}
		if(reader.cardId && reader.cardId != "null") {
			$("#cardId").val(reader.cardId);
		}else{
			$("#cardId").val("");//如果是null 则啥也不显示 2014-11-11
		}
		rdId = $("#rdId").val();
	}
	
	
	function findReaderDeduction() {
		var fieldValue = document.getElementById("fieldValue").value.trim();
		if(!fieldValue) {
			showResult("请输入查询条件！");
			return false;
		}
		var fieldName = document.getElementById("fieldName").value;
		//2015-03-18 抽取出来
		findReader(fieldName,fieldValue);
	}
	
	function findReader(fieldName,fieldValue){
		var params = {fieldName : fieldName,fieldValue : fieldValue};
		$.ajax({
			type : "POST",
			url : "<c:url value='/admin/reader/findReaderDeduction' />",
			data : params,
			dataType : "json",
			success : function(backData){
				var result = backData.result;
				if(result && result=="none") {
					showResult("未查询到读者信息！");
					return false;
				} else if(result && result=="more") {
					showResult("查询结果大于一条，请细化查询条件！");
					return false;
				} else {
					isAvatarChanged = false;
					var reader = eval(backData);
					initReader(reader, false);
					
				}
			},
			error : function(){
				showResult("未查询到读者信息！");
				return;
			}
		});
	}
	
		
	var isContinue = false;
	
	
	function checkRDID(rdId,val) {
		rdId.value = val.replace(/[\W]/g,'');
	}
	
	function checkIdentity(identity, val) {
		identity.value = val.replace(/[\W]/g,'');
	}
	
	    
	function isEnter() {
		var theEvent = window.event;
		var code = theEvent.keyCode || theEvent.which || theEvent.charCode;
		if (code == 13) {
			findReaderDeduction();
			return false;
		}
		return true;
	}
	//上传图片后的回调函数
	function callback(msg,rdId) {
		if(msg == "error" || msg == "ohno"){
			alert("图片上传失败！");
			return false;
		}else{
			alert("保存成功！");
			$("#saveButton").removeAttr("disabled");
			$("#saveButton").html("保存");
		}
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
	
	//初始化消费项目和消费类型
	$(function(){
		var authorizations = eval('${authorizations}');
		var appcode = document.getElementById("appcode");
		for(var i=0; i<authorizations.length; i++){
			appcode.options.add(new Option(authorizations[i].appName,authorizations[i].appCode));
			if(i==0){
				getFinTypes(authorizations[0].appCode);
			}
		}
	});
	
	var fintypes;
	
	function getFinTypes(appcode) {//根据选择的appcode获取对应的消费类型
		$("#fintype").empty();//清除原有所有值
		$("#typefee").empty();//清除原有所有值
		$("#fee").val("");//清除原有所有值
		var selectFintype ='';
		$.ajax({
			type : "POST",
			url : "<c:url value='/admin/sys/fintype/getFinTypesByAppcode' />",
			data : {appcode : appcode},
			dataType : "json",
			success : function(jsonData){
				var fintype = document.getElementById("fintype");
				fintypes = eval(jsonData);
				if(fintypes.length > 0){
					for(var i=0; i<fintypes.length; i++){
						fintype.options.add(new Option(fintypes[i].describe,fintypes[i].feeType));
						if(selectFintype==fintypes[i].feeType){
							getTypeFees(fintypes[i].feeType);
							fintype[i].selected=true;
						}else if(i==0){
							getTypeFees(fintypes[i].feeType);
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
	
	//2015-03-18
	function getTypeFees(fintype){//根据选择消费类型，选择对应的金额
		var typefee = document.getElementById("typefee");
		for(var i=0; i<fintypes.length; i++){
			if(fintype == fintypes[i].feeType){
				if(fintypes[i].typefee != null){
					$("#typefee").empty();//清除原有所有值
					$("#fee").val("");//清除原有所有值
					var typefees = fintypes[i].typefee.split(',');
					for(var j=0; j<typefees.length;j++){
						typefee.options.add(new Option(typefees[j],typefees[j]));
						if(j==0){
							$("#fee").val(typefees[j]);//第一个值初始化
						}
					}
				}else{
					$("#typefee").empty();//清除原有所有值
					$("#fee").val("");//清除原有所有值
				}
			}
		}
	}
	//2015-03-18
	function getFees(typefee){
		var fee = document.getElementById("fee");
		fee.value = typefee;
	}
</script>
<form class="form-horizontal" id="dataform" action="javascript:findReaderDeduction();" name="dataform" 
	onkeydown="if(event.keyCode==13){return false;}"
	method="post" enctype="multipart/form-data" target="hidden_frame">
	<div class="well form-actions_1" id="buttonDiv" style="padding-left: 14px;">
		<span id="pageSpan" style="display: none;">
			<button class="btn btn-info btn-small">上一条</button>
			<button class="btn btn-info btn-small" style="margin-left: 5px;margin-right: 5px;">下一条</button>
		</span>
		<div class="input-append">
			<select id="fieldName" style="width: 123px;">
				<option value="cardId">卡号</option>
				<option value="rdId">读者证号</option>
				<option value="rdName">读者姓名</option>
			</select>
			<input type="text" id="fieldValue" style="border-radius: 0px;width: 130px;border-left: 0px;" onkeydown="isEnter();">
			<button class="btn btn-info" type="button" onclick="javascript:findReaderDeduction();">查询</button>
		</div>
		<input type="hidden" id="today" />
		<div id="tip" class="A_MESSAGEBOX_LOADING alert alert-success" style="display: none;">
		</div>
	</div>
	<div class="container-fluid" >
		<div class="span6">
			<div class="control-group">
				<div class="controls" style="width:200px;position: relative;">
					<div style="padding-top: 7px;width: 164px;" align="center">
						<img id="avatar" style="width: 130px;height: 130px;" class="img-polaroid" src="<c:url value='/admin/reader/showAvatar/defaultPhoto' />" />
					</div>
					<div id="previewDivForIE">
					</div>
				</div>
			</div>
			<div class="control-group" >
				<label class="control-label" for="rdName" style="font-size:30px;line-height:30px;">
					姓名
				</label>
				<div class="controls" style="width:200px;">
					<input type="text" name="rdName" class="input-medium" id="rdName" disabled="disabled" style="width:180px;font-size:30px;height: 30px;"/>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label" for="cardId" style="font-size:30px;line-height:30px;">
					卡号
				</label>
				<div class="controls" style="width:200px;" style="font-size:30px;">
					<input type="text" id="cardId" name="cardId" class="input-medium" disabled="disabled" style="width:180px;font-size:30px;height: 30px;" />
				</div>
			</div>
			<div class="control-group">
				<label class="control-label" for="rdId" style="font-size:30px;line-height:30px;">
					证号
				</label>
				<div class="controls" style="width:200px;">
					<input type="text" id="rdId" name="rdId" class="input-medium" disabled="disabled"
						onblur="javascript:checkRDID(this,this.value);" style="width:180px;font-size:30px;height: 30px;" />
				</div>
			</div>
			<div class="control-group">
				<label class="control-label" for="rdcfstate" style="font-size:30px;line-height:30px;">
					证状态
				</label>
				<div class="controls" style="width:200px;">
					<input type="text" id="rdcfstate" name="rdcfstate" class="input-medium" disabled="disabled"
						 style="width:180px;font-size:30px;height: 30px;" />
				</div>
			</div>
		</div>
		<div class="span6">
			<div class="control-group">
				<label class="control-label" for="prepay" style="font-size:30px;line-height:30px;">
					账户余额
				</label>
				<div class="controls" style="width:200px;">
					<input type="text" id="prepay" name="prepay" class="input-medium" disabled="disabled" style="width:180px;font-size:30px;height: 30px;"/>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label" for="appcode" style="font-size:30px;line-height:30px;">
					消费项目
				</label>
				<div class="controls" style="width:200px;">
					<select class="select-medium" onchange="getFinTypes(this.value)" id="appcode" name="appcode" style="width:150px;font-size:20px;height: 45px;line-height: 30px;">
					</select>
					<font class="label-required" title=""></font>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label" for="fintype" style="font-size:30px;line-height:30px;">
					消费类型
				</label>
				<div class="controls" style="width:200px;">
					<select class="select-medium" id="fintype" onchange="getTypeFees(this.value)" name="fintype" style="width:150px;font-size:20px;height: 45px;line-height: 30px;">
					</select>
					<font class="label-required" title=""></font>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label" for="fee" style="font-size:30px;line-height:30px;">
					消费金额
				</label>
				<div class="controls" style="width:300px;">
					<div class="input-append">
					<input type="text" name="fee" id="fee" class="input-medium" id="fee" style="font-size:30px;height: 30px;width:90px;"/>
					<select class="select-medium" id="typefee" onchange="getFees(this.value)" name="typefee" style="width:80px;font-size:20px;height: 40px;line-height: 30px;">
					</select>
					<font class="label-required" title=""></font>
					</div>
				</div>
			</div>
			<div class="control-group" style="text-align: center;">
				<button type="button" id="saveButton" class="btn btn-success btn-large" onclick="javascript:saveReaderDeduction();">确认扣费</button>
				<button type="reset" onclick="javascript:resetDefaultValue();" class="btn" style="margin-left: 5px;height: 45px;">重置</button>
			</div>
		</div>
<!-- 		<div class="span10"> -->
<!-- 			<div class="control-group" style="text-align: center;"> -->
<!-- 				<button type="button" id="saveButton" class="btn btn-success" onclick="javascript:saveReaderDeduction();" style="margin-left: 5px;height: 50px;">确认扣费</button> -->
<!-- 				<button type="reset" onclick="javascript:resetDefaultValue();" class="btn" style="margin-left: 5px;height: 50px;">重置</button> -->
<!-- 			</div> -->
<!-- 		</div> -->
	</div>
</form>
<div class="modal hide fade in" id="showMessageDiv" style="display: none;top:30%">
  	<div class="modal-header">
    	<a class="close" id="closeA" data-dismiss="modal">×</a>
    	<h4>扣费提示</h4>
  	</div>
  	<div class="modal-body">
	  	<div class="control-group" style="font-size:20px;font-weight: 900;">
	  		<span style="font-size:30px;font-weight: 900;">余&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;额：</span>
	  		<span style="font-size:30px;font-weight: 900;" id="showPrepay"></span>
		</div>
		</br>
		<div class="control-group" >
			<span style="font-size:30px;font-weight: 900;">扣费金额：</span>
			<span style="font-size:30px;font-weight: 900;" id="showFee"></span>
		</div>
  	</div>
  	<div class="modal-footer">
    	<a id="confirmButton" href="#" onclick="confirmSaveDeduction();" class="btn btn-success"> 确定</a>
    	<a id="cancelButton" href="#" class="btn" data-dismiss="modal">取消</a>
  	</div>
</div>
<div class="modal hide fade in" id="showResult" style="display: none;top:30%">
  	<div class="modal-header">
    	<a class="close" data-dismiss="modal" onclick="hiddenConfirm();">×</a>
    	<h4>扣费提示</h4>
  	</div>
  	<div class="modal-body">
	  	<div class="control-group" style="font-size:20px;font-weight: 900;">
	  		<span style="font-size:30px;font-weight: 900;" id="detail"></span>
		</div>
  	</div>
  	<div class="modal-footer">
    	<a id="confirm" href="#" onclick="hiddenConfirm();" class="btn btn-success" data-dismiss="modal">确定</a>
  	</div>
</div>
<iframe id="hidden_frame" name="hidden_frame" style="display: none;">
</iframe>