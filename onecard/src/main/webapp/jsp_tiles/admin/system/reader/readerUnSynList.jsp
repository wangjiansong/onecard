<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/jsp_tiles/include/taglibs.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<c:set var="READER_URL">
	<c:url value="/admin/reader" />
</c:set>
<script type="text/javascript" src="<c:url value='/media/js/reader/readerUtils.js' />"></script>
<script type="text/javascript">
	String.prototype.trim = function() {
		return this.replace(/^\s+/g, "").replace(/\s+$/g, "");
	};
	
	$(function(){
		var readertypes = eval('${readertypes}');
		var rdType = document.getElementById("rdType");
		for(var i=0; i<readertypes.length; i++){
			rdType.options.add(new Option(readertypes[i].DESCRIPE,readertypes[i].READERTYPE));
		}
		var libcodes = eval('${libcodes}');
		var rdLib = document.getElementById("rdLib");
		for(var i=0; i<libcodes.length; i++){
			rdLib.options.add(new Option(libcodes[i].SIMPLENAME,libcodes[i].LIBCODE));
		}
// 		$("#reader_table tr").click(function(obj) {
// 			$(this).find("input[type='checkbox']").attr("checked","checked");
// 			$("#reader_table tr").removeClass("info");
// 			$(this).addClass("info");
// 		});
		if("${obj.rdCFState}"){
			document.getElementById("rdCFState").value = "${obj.rdCFState}";
		}
		if("${obj.rdType}"){
			document.getElementById("rdType").value = "${obj.rdType}";
		}
		if("${obj.rdLib}"){
			document.getElementById("rdLib").value = "${obj.rdLib}";
		}
		if("${obj.ordertype}"){
			document.getElementById("ordertype").value = "${obj.ordertype}";
		}
		if("${obj.condition}"){
			document.getElementById("condition").value = "${obj.condition}";
			document.getElementById("transformer").name = "${obj.condition}";
		}
		if("${obj.condvalue}"){
			document.getElementById("condvalue").value = "${obj.condvalue}";
			document.getElementById("transformer").value = "${obj.condvalue}";
		}
	});

	var table = document.getElementById("reader_table");
	function getThisLine(line) {
		line.className += "info";
	}
	
	function deleteReader(rdId) {
		if(rdId == 'admin') {
			showResult("系统内置管理员，无法删除！");
		}
		setTimeout(function(){
			if(confirm("警告：确认删除选中的读者记录？")){
				$.ajax({
					type : "POST",
					url : "<c:url value='/admin/reader/deleteReader' />",
					data : {rdId : rdId},
					dataType : "json",
					success : function(jsonData){
						var success = jsonData.success;
						if(success == 1){
							alert("删除成功！");
							document.location = document.location;
							//location.reload();//重载当前页面，IE上会提示表单重复提交
							//window.location.reload(true);
						}else if(success == 0){
							var message = jsonData.message;
							showResult(message);
							return;
						}
					},
					error : function(){
						showResult("获取数据失败！");
						return;
					}
				});
			}
		},1);
	}
	
	function detailReader(rdId) {
		window.location.href = "${READER_URL}/detailReader/"+rdId;
	}
	
	function setReaderLibUser(rdId) {
		$.ajax({
			type : "POST",
			url : "<c:url value='/admin/reader/setReaderLibUser' />",
			data : {rdId : rdId, libUser: 1},
			dataType : "json",
			success : function(jsonData){
				var success = jsonData.success;
				if(success == 1) {
					alert("证操作成功!");
					document.location = document.location;
				}
			},
			cache:false
		});
	}
	function syncReader(rdId) {
		$("#" + rdId).attr("disabled", "true");
		$("#" + rdId).html("正在同步...");
		$.ajax({
			type : "POST",
			url : "${READER_URL}/syncReader/" + rdId,
			dataType : "json",
			success : function(jsonData){
				var success = jsonData.success;
				if(success == 1) {
					showResult("同步成功!");
					$("#" + rdId).removeAttr("disabled");
					$("#" + rdId).html("手动同步");
				} else {
					var message = jsonData.message;
					var exp = jsonData.exception;
					if(exp == "null") {
						showResult(message);
					} else {
						showResult(message + ", <br />异常：" + exp);
					}
					$("#" + rdId).removeAttr("disabled");
					$("#" + rdId).html("手动同步");
				}
					
			},
			error : function(){
				showResult("获取接口数据失败！");
				$("#" + rdId).removeAttr("disabled");
				$("#" + rdId).html("手动同步");
				return;
			},
			cache:false
		});
	}
	
	function checkDate(date) {
		var result = date.match(/^(\d{1,4})(-|\/)(\d{1,2})\2(\d{1,2})$/);
		if (result == null) {
			return false;
		}
		var d = new Date(result[1], result[3] - 1, result[4]);
		return (d.getFullYear() == result[1] && (d.getMonth() + 1) == result[3] && d.getDate() == result[4]);
    }
	
	function doSubmit(type) {
		var condition = document.getElementById("condition").value;
		var condvalue = document.getElementById("condvalue").value.trim();
		if(condition && condvalue){
			if(condition=="rdStartDate" || condition=="rdEndDate" 
					|| condition=="rdInTime" || condition=="rdBornDate"){
				if(!checkDate(condvalue)){
					alert("日期格式不正确！(e.g:1970-01-01)");
					return;
				}
			}
			if(condition == "rdSex"){
				if(!(condvalue==1 || condvalue==2)){
					alert("查询条件输入有误！");
					return;
				}
			}
			if(condition == "rdGlobal"){
				if(!(condvalue==1 || condvalue==0)){
					alert("查询条件输入有误！");
					return;
				}
			}
			var transformer = document.getElementById("transformer");
			transformer.name = condition;
			transformer.value = condvalue;
		}
		var dataform = document.getElementById("dataform");
		if(type == 1){
			dataform.action = "<c:url value='/admin/reader/readerUnSynList' />";
			dataform.submit();
		}else{
			dataform.action = "<c:url value='/admin/reader/exportReaderUnSynListExcel' />";
			dataform.submit();
		}
	}
	
	function showResult(tip) {
		$("#tip").css("display", "block").html(tip);
		setTimeout(function(){
			$("#tip").fadeOut("slow");
		},5000);
		$("#tip").attr("class", "A_MESSAGEBOX_LOADING alert alert-success");
		return;
	}
	
	function isEnter() {
		var theEvent = window.event;
		var code = theEvent.keyCode || theEvent.which || theEvent.charCode;
		if (code == 13) {
			doSubmit(1);
			return false;
		}
		return true;
	}
	
	function getcheckBoxVals(){
		var vals ;
	    $("input[name='checkbox']:checked").each(function () {
	         vals=vals+this.value+",";
	    });
	    vals = vals.replace("undefined","");
	    vals = vals.substring(0,vals.lastIndexOf(","));
	    
	    return vals;
	}
	
	//复选框 全选和撤销 2014-05-26
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
		    } else {
		        chkGroup.attr("checked", false);
		    }
		});
		
// 		this.toggle(function() {
// 		    if ($(this).attr("checked")) {
// 		    	$(this).attr("checked", true);	
// 		    } else {
// 		    	$(this).attr("checked", false);
// 		    }
// 		});

	});
	
	//批量同步的操作
	function doSynSubmit(){
		var rdIds = getcheckBoxVals();
		$("#synsButton").attr("disabled", "true");
		$("#synsButton").html("正在批同步...");
		$.ajax({
			type : "POST",
			data : {rdIds : rdIds},
			url : "${READER_URL}/doUnSynStatus/",
			dataType : "json",
			success : function(jsonData){
				var success = jsonData.success;
				var message = jsonData.message;
				if(success == 1) {
					showResult("同步成功!"+message);
					$("#synsButton").removeAttr("disabled");
					$("#synsButton").html("批量同步");
				} else {
					showResult("同步失败！"+message);
					$("#synsButton").removeAttr("disabled");
					$("#synsButton").html("批量同步");
				}
				setTimeout(function(){
					 window.location = "<c:url value='/admin/reader/readerUnSynList' />";//刷新页面	
				},1000);
			},
			error : function(message){
				showResult("获取接口数据失败！"+message);
				$("#synsButton").removeAttr("disabled");
				$("#synsButton").html("批量同步");
				setTimeout(function(){
					 window.location = "<c:url value='/admin/reader/readerUnSynList' />";//刷新页面	
				},1000);
				return;
			},
			cache:false
		});
		
	}
</script>
<div id="tip" class="A_MESSAGEBOX_LOADING alert alert-success" style="display: none;">
</div>
<form id="dataform" method="post" class="form-inline" action="">
	<div class="well form-actions_1" align="center">
		<div class="input-append">
		<select id="libUser" style="width: 100px;" name="libUser" class="input-medium search-query-extend">
			<option value="0">普通读者</option>
		</select>
		<input type="hidden" id="transformer" />
		<!-- <span style="font: 13px/20px arial,sans-serif;">证状态：</span> -->
		<select id="rdCFState" name="rdCFState" style="width: 87px;">
			<option value="0">证状态</option>
			<option value="1">有效</option>
			<option value="2">验证</option>
			<option value="3">丢失</option>
			<option value="4">暂停</option>
			<option value="5">注销</option>
		</select>
		&nbsp;
		<select id="rdType" name="rdType" style="width: 100px;">
			<option value="">读者类型</option>
		</select>
		&nbsp;
		<select id="rdLib" name="rdLib" style="width: 87px;">
			<option value="">开户馆</option>
		</select>
		&nbsp;
		<select id="ordertype" name="ordertype" style="width: 80px;">
			<option value="">排序方式</option>
			<option value="ASC" selected="selected">升序</option>
			<option value="DESC">降序</option>
		</select>
		&nbsp;
		<select id="condition" name="condition" style="width: 87px;">
			<option value="">请选择</option>
			<option value="rdId" selected="selected">证号</option>
			<option value="rdName">姓名</option>
			<option value="rdCertify">身份证号</option>
			<option value="rdStartDate">启用日期</option>
			<option value="rdEndDate">终止日期</option>
			<option value="rdInTime">办证时间</option>
			<option value="rdBornDate">出生日期</option>
			<option value="rdLoginId">手机</option>
			<option value="rdNative">籍贯</option>
			<option value="rdAddress">地址</option>
			<option value="rdNation">民族</option>
			<option value="rdUnit">单位</option>
			<option value="rdPhone">电话</option>
			<option value="rdPostCode">邮编</option>
			<option value="rdSort1">专业</option>
			<option value="rdSort2">职业</option>
			<option value="rdSort3">职务</option>
			<option value="rdSort4">职称</option>
			<option value="rdSort5">文化</option>
			<option value="rdSex">性别1:男|2:女</option>
			<option value="rdGlobal">馆际读者1:是|0:否</option>
		</select>
		<input type="text" id="condvalue" name="condvalue" style="width: 130px;" onkeydown="isEnter();" />
		<button type="button" class="btn btn-success" onclick="doSubmit(1);">查询</button>
<!-- 		<button type="button" class="btn btn-info" onclick="doSubmit(2);">导出Excel</button> -->
		<button type="reset" class="btn" onclick="resetDefaultValue();">重置</button>
		</div>
	</div>
</form>

<div class="input-append">
	<span class="add-on"><input type="checkbox" class="selAll"  id="rdids"/></span> 		
	<button type="button" id="synsButton" class="btn btn-info" onclick="doSynSubmit();">批量同步</button>
</div>
<table id="reader_table" class="table table-bordered table-hover table-condensed">
	<thead>
		<tr>
			<th width="2.3%">选择</th>
			<th width="10%">读者证号</th>
			<th width="7%">姓名</th>
			<th width="13%">开户馆</th>
			<th width="5%">读者证状态</th>
<!-- 			<th width="5%">同步状态</th> -->
			<th width="6%">操作</th>
		</tr>
 	</thead>
	<tbody>
		<c:forEach items="${list}" var="v" varStatus="vs">
			<tr class="f">
				<td style="text-align: center;padding-top: 1px;">
					<input type="checkbox" name="checkbox" id="${v.rdId}" value="${v.rdId}" /><!-- 选中可以获取对应的读者的账号 -->
				</td>
				<td>${v.rdId}</td>
				<td>${v.rdName}</td>
				<td>${v.rdLib}</td>
				<td>${v.rdStateStr}</td>
<!-- 				<td> -->
<%-- 					<c:if test="${v.synStatus==1 }">成功</c:if> --%>
<%-- 					<c:if test="${v.synStatus==0 }">失败</c:if> --%>
<!-- 				</td> -->
				<td style="text-align: center;">
					<div class="btn-group">
					<button class="btn btn-mini btn-success" type="button" onclick="javascript:detailReader('${v.rdId}');">查看</button>
					<button class="btn btn-mini btn-info" type="button" id="${v.rdId}" onclick="javascript:syncReader('${v.rdId}');">手动同步</button>					
					</div>
				</td>
			</tr>
		</c:forEach> 
			 
	</tbody>
</table>
<%@include file="/jsp_tiles/include/admin/paginator.jsp" %>