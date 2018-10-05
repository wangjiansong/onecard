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
	
	$(function(){
		var defaultLib = "${READER_SESSION.reader.rdLibCode}";
		var defaultLibName = "${READER_SESSION.reader.rdLib}";
		if(defaultLib && defaultLibName){
			//rdLib.value = defaultLib;//不是默认是管理员的分馆
		}
	});
	

</script>
<section id="tabs">
	<div>
		<ul id="myTab" class="nav nav-tabs">
			<li class="active"><a href="#" data-toggle="tab">用户角色管理 >> 编辑用户</a></li>
		</ul>
		<div id="myTabContent" class="tab-content">
		    <div class="tab-pane active">


<form class="form-horizontal" id="dataform" action="javascript:searchReader();" name="dataform" 
	method="post" enctype="multipart/form-data" target="hidden_frame">
	
	<div class="well form-actions_1" id="buttonDiv" style="padding-left: 144px;">
		<div class="btn-group">
			<button type="button" id="resetPasswd" class="btn btn-info" onclick="javascript:modifyPasswd('${reader.rdId}');">密码修改</button>
			<button type="button" id="saveButton" class="btn btn-success" onclick="javascript:checkSubmit('${reader.rdId}');">保存</button>
			<button type="button" onclick="javascript:resetDefaultValue();" class="btn" >重置</button>
			<a href="<c:url value='/admin/sys/readerRole/list' />" type="button" class="btn">返回</a>
		</div>
		<div id="tip" class="A_MESSAGEBOX_LOADING alert alert-success" style="display: none;">
		</div>
	</div>
	<div class="container-fluid" style="padding-right: 40px;">
		<div class="span12">
			<div class="control-group">
				<label class="control-label" for="rdid">
					登录证号
				</label>
				<div class="controls" style="width:200px;">
					<input type="text" id="newRdId" name="newRdId" class="input-medium" style="ime-mode:disabled;"
						onblur="javascript:checkRDID(this,this.value);" value="${reader.rdId}" />
					
					<input type="hidden" id="rdId" name="rdId" class="input-medium" value="${reader.rdId}" />
					<font class="label-required" title=""></font>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label" for="rdLib">
					分馆代码
				</label>
				<div class="controls" style="width:200px;">
					<select id="rdLib" name="rdLib" class="select-medium">
						<option value="${reader.rdLibCode}" checked>${reader.rdLibCode} | ${reader.rdLib}</option>
						<c:forEach items="${libcodes}" var="entry">
							<c:if test="${reader.rdLibCode ne entry.libCode}">
								<option value="${entry.libCode}">${entry.libCode} | ${entry.simpleName}</option>
							</c:if>
						</c:forEach>
					</select>
					<font class="label-required" title=""></font>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label" for="rdName">
					姓名
				</label>
				<div class="controls" style="width:200px;">
					<input type="text" name="rdName" class="input-medium" id="rdName" value="${reader.rdName}"/>
					<font class="label-required" title=""></font>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label" for="rdEmail">
					Email地址
				</label>
				<div class="controls" style="width:200px;">
					<input type="text" name="rdEmail" class="input-medium" id="rdEmail" value="${reader.rdEmail}"/>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label" for="rdLoginId">
					电话
				</label>
				<div class="controls" style="width:200px;">
					<input type="text" name="rdLoginId" class="input-medium" id="rdLoginId" value="${reader.rdLoginId}"/>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label" for="rdLoginId">
					分馆权限
				</label>
				<div class="controls" style="width:200px;">
					<a type="button" class="btn btn-danger" href="<c:url value='/admin/reader/libassign?rdId=${reader.rdId}' />">添加馆权限</a>
					<br/>
					<br/>
					显示已选择的馆代码：${reader.libAsSign}
				</div>
			</div>
		</div>
		
	</div>
</form>
<script type="text/javascript">
	
	function checkSubmit(rdId) {
		$("#saveButton").attr("disabled","true");
		$("#saveButton").html("保存中...");
		var newRdId = $("#rdId").val().trim();
		if(!newRdId) {
			showResult("请输入登录证号");
			$("#saveButton").removeAttr("disabled");
			$("#saveButton").html("保存");
			return;
		}
		var rdLib = $("#rdLib").val();
		if(!rdLib){
			showResult("请选择开户馆！");
			$("#saveButton").removeAttr("disabled");
			$("#saveButton").html("保存");
			return;
		}
		var rdName = $("#rdName").val();
		if(!rdName) {
			showResult("请输入姓名！");
			$("#saveButton").removeAttr("disabled");
			$("#saveButton").html("保存");
			return;
		}
		var rdEmail = $("#rdEmail").val();
		var rdLoginId = $("#rdLoginId").val();
		var params = {rdId:rdId, newRdId: newRdId, rdLib:rdLib, 
				rdName:rdName, rdEmail:rdEmail, rdLoginId:rdLoginId};
		$.ajax({
			type : "POST",
			url : "<c:url value='/admin/sys/readerRole/updOperator' />",
			data : params,
			dataType : "json",
			success : function(jsonData){
				var success = jsonData.success;
				if(success == 1) {
					alert("保存成功!");
					window.location.href = "<c:url value='/admin/sys/readerRole/editOperator/"+rdId+"' />";
					$("#saveButton").removeAttr("disabled");
					$("#saveButton").html("保存");
					return;
				} else if(success == -1) {
					var message = jsonData.message;
					showResult(message);
					$("#saveButton").removeAttr("disabled");
					$("#saveButton").html("保存");
					return ;
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
	
	function modifyPasswd(rdId) {
		//alert(rdId);
		window.location.href = "<c:url value='/admin/sys/readerRole/resetPasswd/"+rdId+"' />";
	}
	//获取指定馆下的读者类型
	function getLibReaderType(libCode) {
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
					for(var i=0; i<types.length-1; i++){
						rdType.options.add(new Option(types[i].SHOWREADERTYPE,types[i].READERTYPE));
					}
					document.getElementById("rdEndDate").value = types[types.length-1].rdEndDate;
				}else{
					document.getElementById("rdEndDate").value = "";
				}
			},
			error : function(jsonData){
				showResult("获取数据失败！");
				return;
			}
		});
		
		$.ajax({
			type : "POST",
			url : "<c:url value='/admin/reader/getThisGlobalReaderType' />",
			data : {thisLibCode : libCode},
			dataType : "json",
			success : function(jsonData){
				var rdLibType = document.getElementById("rdLibType");
				rdLibType.length = 0;
				var types = eval(jsonData);
				if(types.length){
					for(var i=0; i<types.length; i++){
						rdLibType.options.add(new Option(types[i].SHOWREADERTYPE,types[i].READERTYPE));
					}
				}
			},
			error : function(jsonData){
				showResult("获取数据失败！");
				return;
			}
		});
	}

	//点击重置重新设置默认值
	function resetDefaultValue() {
		setTimeout(function(){
			document.getElementById("rdId").disabled = "";
			document.getElementById("rdPasswd").disabled = "";
			
			var today = document.getElementById("today").value;
			document.getElementById("rdEndDate").value = today;
			document.getElementById("rdInTime").value = today;
			document.getElementById("rdLibType").disabled = "disabled";
			isAvatarChanged = false;
			ACTION = "ADD";
		},1);
	}
	

	function checkRDID(rdId,val) {
		rdId.value = val.replace(/[\W]/g,'');
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
</script>
 </div>
		</div>
	</div>
</section>