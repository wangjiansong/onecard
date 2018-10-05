<%@page import="com.interlib.sso.common.SqlUtils"%>
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
		$("#reader_table tr").click(function(obj) {
			$(this).find("input[type='radio']").attr("checked","checked");
			$("#reader_table tr").removeClass("info");
			$(this).addClass("info");
		});
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
		
		$('#condition').change(function(){
			var condition = $('#condition option:selected').attr('value');
            if(condition=="rdStartDate" || condition=="rdEndDate" 
				|| condition=="rdInTime" || condition=="rdBornDate") {
            	$("#condvalue").addClass("date form_datetime");
            	$(".form_datetime").datetimepicker({
                    format: "yyyy-mm-dd",
                    language: 'zh-CN',
                    autoclose: true,
                    startView: 'month',
                    minView: 2
                });
            } else {
            	$(".form_datetime").datetimepicker("remove");
            	$("#condvalue").removeAttr("class");
            	$("#add-on").remove();
            }
        }); 
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
			dataform.action = "<c:url value='/admin/reader/index' />";
			dataform.submit();
		} else if(type == 3) {
			document.getElementById("exportType").value = "3";
			dataform.action = "<c:url value='/admin/reader/exportTxt' />";
			dataform.submit();
		} else if(type == 4) {
			document.getElementById("exportType").value = "4";
			dataform.action = "<c:url value='/admin/reader/exportTxt' />";
			dataform.submit();
		} else{
			dataform.action = "<c:url value='/admin/reader/exportReaderExcel' />";
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
	
	function check(a){
		return 1;
			fibdn = new Array ("'","\\","/","%27","%3B");
				i=fibdn.length;
				j=a.length;
			for (ii=0; ii<i; ii++){ 
				for (jj=0; jj<j; jj++){ 
					temp1=a.charAt(jj);
					temp2=fibdn[ii];
					if (temp1==temp2){ 
						return 0; 
					}
				}
			}
		return 1;
	}
	
	$("#ordertype").change(function(){
		var str ="${obj.ordertype}";
		//动作
		SqlUtils.likeEscape(str);
	});
	$("#condition").change(function(){
		
		var s ="${obj.condition}";
		//动作		
		SqlUtils.likeEscape(s);
	});
</script>
<div id="tip" class="A_MESSAGEBOX_LOADING alert alert-success" style="display: none;">
</div>
<form id="dataform" method="post" class="form-inline" action="">
	<div class="well form-actions_1" align="center">
	<div class="input-append">
		<select id="libUser" name="libUser" style="width: 100px;" class="input-medium search-query-extend">
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
			<option value="10">信用有效</option>
			
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
		<select id="ordertype" name="ordertype" style="width: 90px;">
			<option value="">排序方式</option>
			<option value="ASC" selected="selected">升序</option>
			<option value="DESC">降序</option>
		</select>
		&nbsp;
		<select id="condition" name="condition" style="width: 95px;">
			<option value="">请选择</option>
			<option value="rdId" selected="selected">证号</option>
			<option value="cardId">卡号</option>
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
		<input type="hidden" id="exportType" name="exportType" />
		<input type="text" id="condvalue" name="condvalue" style="width: 130px;" onkeydown="isEnter();" />
		<button type="button" class="btn btn-success" onclick="doSubmit(1);" onblur="check(a)">查询</button>
		<button type="reset" class="btn" onclick="resetDefaultValue();">重置</button>
		<div class="btn-group">
			<a class="btn btn-info dropdown-toggle" data-toggle="dropdown" href="#">
			导出
		 	<span class="caret"></span>
			</a>
			<ul class="dropdown-menu">
		 		<li><a href="#" onclick="doSubmit(2);" onblur="check(a)">导出excel</a></li>
				<li><a href="#" onclick="doSubmit('3')" onblur="check(a)">导出金图格式1txt</a></li>
				<li><a href="#" onclick="doSubmit('4')" onblur="check(a)">导出金图格式2txt</a></li>
		  	</ul>
		</div>
	</div>
	</div>
</form>
<div class="input-append">
	<button class="btn" onclick="renew()">恢复</button>
	<button class="btn" onclick="check()">验证</button>
	<button class="btn" onclick="cardOperation('loss')">挂失</button>
	<button class="btn" onclick="cardOperation('stop')">暂停</button>
	<button class="btn" onclick="quit()">退证</button>
	<div class="btn-group">
	  	<a class="btn dropdown-toggle" data-toggle="dropdown" href="#">
	   	补办
	    <span class="caret"></span>
	  	</a>
	  	<ul class="dropdown-menu">
	    	<li><a href="#" onclick="cardRepair();">卡补办</a></li>
			<li><a href="#" onclick="repair()">证补办</a></li>
	  	</ul>
	</div>
	<button class="btn" onclick="defer()">延期</button>
	<button class="btn" onclick="change()">换证</button>
	<button class="btn" onclick="rdLogout()">注销</button>
	<button class="btn" onclick="credit()">信用有效</button>
</div>
<table id="reader_table" class="table table-bordered table-hover table-condensed">
	<thead>
		<tr>
			<th width="2.3%">选择</th>
			<th width="10%">读者证号</th>
			<th width="7%">姓名</th>
			<th width="13%">开户馆</th>
			<th width="5%">读者证状态</th>
			<th width="6%">操作</th>
		</tr>
 	</thead>
	<tbody>
		<c:forEach items="${list}" var="v" varStatus="vs">
			<tr ondblclick="javascript:detailReader('${v.rdId}');">
				<td style="text-align: center;padding-top: 1px;">
					<input type="radio" name="id_radio" value="${v.rdId}" /><!-- 选中可以获取对应的读者的账号 -->
				</td>
				<c:choose>  
				    <c:when test="${fn:length(v.rdId) == 18}">  
				        <td><c:out value="${fn:substring(v.rdId, 0, 4)}" />**********<c:out value="${fn:substring(v.rdId, 16, 18)}" /></td>
				    </c:when>  
				    <c:when test="${fn:length(v.rdId) == 15}">  
				        <td><c:out value="${fn:substring(v.rdId, 0, 4)}" />**********<c:out value="${fn:substring(v.rdId, 13, 15)}" /></td>
				    </c:when>  
				   <c:otherwise>  
				      <td><c:out value="${v.rdId}" /></td>
				    </c:otherwise>  
				</c:choose>
				<td>${v.rdName}</td>
				<td>${v.rdLib}</td>
				<td id="rdstate">${v.rdStateStr}</td>
				<td style="text-align: center;">
					<div class="btn-group">
					<button class="btn btn-mini btn-success" type="button" onclick="javascript:detailReader('${v.rdId}');">查看</button>
					<button class="btn btn-mini btn-info" type="button" id="${v.rdId}" onclick="javascript:syncReader('${v.rdId}');">手动同步</button>
					<button class="btn btn-mini" type="button" onclick="javascript:setReaderLibUser('${v.rdId}');">设为管理员</button>
					<button class="btn btn-mini btn-info" type="button" onclick="javascript:deleteReader('${v.rdId}');">删除读者</button>
					</div>
				</td>
			</tr>
		</c:forEach>
	</tbody>
</table>
<%@include file="/jsp_tiles/include/admin/paginator.jsp" %>