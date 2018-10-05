<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/jsp_tiles/include/taglibs.jsp" %>
<%
request.getSession().getAttribute("readerId");

 %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<c:set var="READER_URL">
	<c:url value="/admin/reader" />
</c:set>
<script type="text/javascript">
$(function(){
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
		/**
		*
		*/
		var readertypes = eval('${readertypes}');
		if(readertypes.length){
			var rdType = document.getElementById("rdtype");
			for(var i=0; i<readertypes.length; i++){
				rdType.options.add(new Option(readertypes[i].SHOWREADERTYPE,readertypes[i].READERTYPE));
			}
		}
				
		var globaltypes = eval('${globaltypes}');
		var rdLibType = document.getElementById("rdlibType");
		for(var i=0; i<globaltypes.length; i++){
			rdLibType.options.add(new Option(globaltypes[i].SHOWREADERTYPE,globaltypes[i].READERTYPE));
		}
		var libcodes = eval('${libcodes}');
		var rdLib = document.getElementById("rdlib");
		for(var i=0; i<libcodes.length; i++){
			rdLib.options.add(new Option(libcodes[i].SHOWLIBTYPE,libcodes[i].LIBCODE));
		}
		var defaultLib = "${READER_SESSION.reader.rdLibCode}";
		var defaultLibName = "${READER_SESSION.reader.rdLib}";
		if(defaultLib && defaultLibName){
			rdLib.value = defaultLib;
		}
		
		$("#readerId").val('${obj.readerId}');
		$(".form_datetime").datetimepicker({
format: "yyyy-mm-dd",
language: 'zh-CN',
autoclose: true,
startView: 'month',
minView: 2
});
		
	});
	
	function pressEnter(e,method){
	if(e==null){//ie6..
		e=event;
	}	
	if ((e.which == 13 || e.keyCode == 13)&&(!(e.ctrlKey || e.metaKey))
			&& !e.shiftKey && !e.altKey) {
		 method();
	}
}


function exportExcel(){
	var form1 = document.getElementById("form1");
	form1.action = "<c:url value='/admin/reader/exportBatchListExcel' />";
	form1.submit();
	form1.action = "";//重置下action值 不然导出报表之后其他按钮都是导出报表功能 
}

function interval(){
	var form1 = document.getElementById("form2");
	form1.action = "";
	form1.submit();
	form1.action = "";//重置下action值 不然导出报表之后其他按钮都是导出报表功能 
}

function doSubmit(){

	  	/* var dataform = document.getElementById("form");
		dataform.action = "<c:url value='/admin/reader/batchList' />";
		dataform.submit();
		form.action = ""; */

	$.ajax({
      //几个参数需要注意一下
        type: "POST",//方法类型
        url: "<c:url value='/admin/reader/batchList' />" ,//url
        data: $('#form').serialize(),
        success: function (result) {
          alert("批量生成成功");  //就将返回的数据显示出来
          window.location.href="batch";  
        },
        error : function() {
          alert("异常！");
        }
      });
	
}

function deleteNetReader(netreaderId) {
		var boxs = document.getElementsByName("readerId_box");
		var ids = [];
		var states = [];
		for(var i=0; i<boxs.length; i++){
			if(boxs[i].checked){
				ids.push(boxs[i].id);
				states.push(boxs[i].value);
			}
		}
		if(ids.length){
			if(!confirm("确认删除？")){
				return false;
			}
			$.ajax({
				type : "POST",
				url : "<c:url value='/admin/netreader/deleteNetReader' />",
				data : {ids : ids.toString()},
				dataType : "text",
				success : function(backData){
					/* alert(backData);
					alert("1111--"+ids.length);		 */				
					alert("操作成功！！！！");
					window.location = 'batch';
					/* if(backData = ids.length){
						location.href = document.referrer;
					}else if(backData < ids.length){
						showResult("操作失败！", "error");
						alert("操作失败");
					} */
				},
				error : function(){
					alert("获取数据失败！");
					return;
				}
			});
		}else{
			showResult("请选择记录！", "error");
		}
	}
function checkAll(isChecked) {
		var box_title = document.getElementsByName("readerId_box_title");
		var boxs = document.getElementsByName("readerId_box");
		var rows = document.getElementById("netreader_table").rows;
		if(isChecked){
			if(boxs.length>=1){
				for(var j=0; j<box_title.length;j++){
					box_title[j].checked = isChecked;
				}
			}
			for(var i=0; i<boxs.length; i++){
				boxs[i].checked = isChecked;
				rows[i+1].className = "info";
			}
		}else{
			if(boxs.length>=1){
				for(var j=0; j<box_title.length;j++){
					box_title[j].checked = isChecked;
				}
			}
			for(var i=0; i<boxs.length; i++){
				boxs[i].checked = isChecked;
				rows[i+1].className = "";
			}
		}
	}
	
	function getSelected(thisobj,checkobj) {
		if(checkobj.checked){
			checkobj.checked = false;
			thisobj.className = "";
		}else{
			checkobj.checked = true;
			thisobj.className = "info";
		}
	}
function detailReader(readerId) {
	window.location.href = "/onecard/admin/netreader/detailNetReader/"+readerId;
}

function buttonAction(dateFormat) {
	$("#dateFormat").val(dateFormat);
	$('.form_datetime').datetimepicker('remove');
	if(dateFormat == 'DAY') {
		$(".form_datetime").datetimepicker({
format: "yyyy/mm/dd",
language: 'zh-CN',
autoclose: true,
startView: 'month',
minView: 2
});
		
	} else if (dateFormat == 'MONTH' || dateFormat == 'SEASON') {
		$(".form_datetime").datetimepicker({
showMeridian: true,
format: "yyyy/mm",
language: 'zh-CN',
autoclose: true,
startView: 'year',
minView: 3
});
	} else if (dateFormat == 'YEAR') {
		$(".form_datetime").datetimepicker({
showMeridian: true,
format: "yyyy",
language: 'zh-CN',
autoclose: true,
startView: 'decade',
minView: 4
});
	} else {
		$(".form_datetime").datetimepicker({
showMeridian: true,
format: "yyyy/mm/dd HH:ii:ss",
language: 'zh-CN',
autoclose: true,
minView: 0,
minuteStep: 1
});
	}
	
}

function submitForm() {
	var startTimeStr = $("#startTime").val();
	var endTimeStr = $("#endTime").val();
	if(startTimeStr != "" && endTimeStr != "") {
		var startTime = new Date(startTimeStr);
		var endTime = new Date(endTimeStr);
		if(startTime.getTime() > endTime.getTime()) {
			showResult("起始时间应小于结束时间", "error");
			return;
		}
	}
	
	var submitform = document.getElementById("submitform");
	submitform.action = "<c:url value='/admin/reader/batch?page.showCount=10' />";
	submitform.submit();
	form.action = "";//重置下action值 20150710
	
}	
</script>


<section id="tabs">
	<div>
		<ul id="myTab" class="nav nav-tabs">
			<li class="active"><a href="#" data-toggle="tab">批量办卡</a></li>
		</ul>
		<div id="myTabContent" class="tab-content">
		    <div class="tab-pane active">
<fieldset>
	<div class="well form-actions_1">
	<c:if test="${!empty message}">
		<div class="alert alert-success">
			<a class="close" data-dismiss="alert">&times;</a>
			${message}
		</div>
	</c:if>
		<form id="form" class="form-inline" action="batch" method="POST">
		<div class="row-fluid">
			<div class="control-group">
					开户馆
					<select id="rdLib" name="rdLib" class="select-medium" onchange="getLibReaderType(this.value,'','');">
					</select>
					&nbsp;&nbsp;读者类型
					<select id="rdType" name="rdType" class="select-medium"  onchange="calEndDate(this.value);">
					</select>
					<!-- 馆际流通类型 --> 
					<select id="rdLibType" name="rdLibType" class="select-medium" style="display:none">
					</select><br>
					密码生成规则：
						<select name="passwordCreator">
						  <option value ="1">默认</option>
						  <option value ="2">随机</option>
						</select>
					生成个数:
						<input type="text" name="i" id="i" value=""/>
						<input type="hidden"  name="page.showCount" value="10" />
						<button class="btn btn-info" type="button" onclick="doSubmit();">批量生成</button>
						<button type="button" class="btn" onclick="javascript:deleteNetReader();">批删除</button>
				
				</div>
				
		</div>
		</form>
		<table class="table table-bordered table-hover table-condensed table-striped">
		<thead>
			<tr>
				<td>有效读者数统计</td>
				<td>读者激活数总计</td>
		 	</tr>
		 	<tr>
				<td><c:out value="${batchCount}" /></td>
				<td><c:out value="${count}" /></td>
		 	</tr>
		</thead>
	</table>
	</div>
</fieldset>


<a data-toggle="modal" href="#exportModel"  class="btn btn-info"> 导出Excel</a>
<div class="btn-group">
	<a data-toggle="modal" href="#myModal" onclick="buttonAction('DAY')" class="btn btn-info">日统计</a>
	<a data-toggle="modal" href="#myModal" onclick="buttonAction('MONTH')" class="btn btn-info">月统计</a>
	<a data-toggle="modal" href="#myModal" onclick="buttonAction('YEAR')" class="btn btn-info">年统计</a>
</div>
<a data-toggle="modal" href="#intervalModel"  class="btn btn-info"> 批延期</a>			
<table class="table">
	<tbody style="text-align: left">
 		<tr class="f">
 			<td>
			<c:choose> 
				<c:when test="${obj.dateFormat eq 'DAY'}">
				日统计   
				</c:when> 
				<c:when test="${obj.dateFormat eq 'MONTH'}">   
				月统计
				</c:when> 
				<c:when test="${obj.dateFormat eq 'YEAR'}">   
				年统计
				</c:when> 
				<c:otherwise>   
				日统计
				</c:otherwise> 
			</c:choose> 
			：【${obj.startTime}】至【${obj.endTime}】
			</td>
 		</tr>
	</tbody>
</table>

<table id="netreader_table" class="table table-bordered table-hover table-condensed table-striped">
	<thead>
		<tr>			
			<th width="2.3%"><input type="checkbox" name="readerId_box_title" onclick="checkAll(this.checked);" style="margin-bottom: 4px;" /></th>
			<th>读者证号</th>
			<th>读者类型</th>
			<th>开户馆</th>
			<th>办证日期</th>
			<th width="6%">操作</th>
		</tr>
	</thead>
	<tbody>
	<c:forEach items="${pageList}" var="p" varStatus="ps">
			<tr onclick="javascript:getSelected(this,document.getElementById('${p.readerId}'));">
				<td style="text-align: center;padding-top: 1px;">
					<input type="checkbox" id="${p.readerId}" name="readerId_box" value="${ps.index+1}" 
						onclick="javascript:this.checked=!this.checked;" />
						<!-- if(this.checked==true){this.checked=false;}else{this.checked=true;} -->
				</td>
 				<td><c:out value="${p.readerId}" /></td>
 				<td><c:out value="${p.readerType}" /></td>
				<td><c:out value="${p.readerLib}" /></td>
 				<td><fmt:formatDate value="${p.readerHandleDate}"  pattern="yyyy-MM-dd HH:mm:ss" /></td>
 				<td style="text-align: center;">
					<div class="btn-group">
					<button class="btn btn-mini btn-success" type="button" onclick="javascript:detailReader('${p.readerId}');">查看</button>
					</div>
				</td>
 			</tr>
		</c:forEach>
	</tbody>
</table>
<div class="modal hide fade in" id="myModal" style="display: none;" onUnload="opener.location.reload()">
  	<div class="modal-header">
    	<a class="close" data-dismiss="modal">×</a>
    	<h3>条件筛选</h3>
  	</div>
  	<div class="modal-body">
	  	<form name="submitform" class="form-inline" action="batch" method="POST" id="submitform">
			<div class="control-group">
				<input type="text" id="startTime" name="startTime" value="${obj.startTime}" placeholder="起始时间" readonly class="date form_datetime input-medium">
				<input type="text" id="endTime" name="endTime" value="${obj.endTime}"  placeholder="结束时间" readonly class="date form_datetime input-medium">
			</div>
			<input type="hidden" name="dateFormat" id="dateFormat" value="${obj.dateFormat}"/>
	  	</form>
  	</div>
  	<div class="modal-footer">
    	<a href="#" class="btn" data-dismiss="modal">取消</a>
    	<a href="#" onclick="javascript:submitForm();" class="btn btn-success"><i class="icon-ok icon-white"></i>确定</a>
  	</div>
</div>
<div class="modal hide fade in" id="exportModel" style="display: none;" onUnload="opener.location.reload()">
  	<div class="modal-header">
    	<a class="close" data-dismiss="modal">×</a>
    	<h3>导出条件</h3>
  	</div>
  	<div class="modal-body">
	  	<form action="batch" id="form1" method="POST" class="form-inline">
		<div class="row-fluid">
			<div class="control-group">
					开户馆
					<select id="rdlib" name="rdlib" class="select-medium" onchange="getlibReaderType(this.value,'','');">
					</select>
					&nbsp;&nbsp;读者类型
					<select id="rdtype" name="rdtype" class="select-medium" onchange="calendDate(this.value);">
					</select>
					<!-- 馆际流通类型 --> 
					<select id="rdlibType" name="rdlibType" class="select-medium" style="display:none">
					</select><br>
					开始日期<input type="text" id="readerHandleStartDate" name="readerHandleStartDate" value="${obj.readerHandleStartDate}" placeholder="起始时间" readonly class="date form_datetime input-medium">
					终止日期<input type="text" id="readerHandleEndDate" name="readerHandleEndDate" value="${obj.readerHandleEndDate}"  placeholder="结束时间" readonly class="date form_datetime input-medium">
					
			</div>
		</div>
		</form>
  	</div>
  	<div class="modal-footer">
    	<a href="#" class="btn" data-dismiss="modal">取消</a>
    	<a href="#" onclick="javascript:exportExcel();" class="btn btn-success"><i class="icon-ok icon-white"></i>确定</a>
  	</div>
</div>

<div class="modal hide fade in" id="intervalModel" style="display: none;" onUnload="opener.location.reload()">
  	<div class="modal-header">
    	<a class="close" data-dismiss="modal">×</a>
    	<h3>延期条件</h3>
  	</div>
  	<div class="modal-body">
	  	<form id="form2" class="form-inline" action="batch" method="POST">
		<div class="row-fluid">
			<div class="control-group">
					开户馆
					<select id="rdLiB" name="rdLiB" class="select-medium" onchange="getLibReaderTypE(this.value,'','');">
					</select>
					&nbsp;&nbsp;读者类型
					<select id="rdTypE" name="rdTypE" class="select-medium"  onchange="calEndDatE(this.value);">
					</select>
					<!-- 馆际流通类型 --> 
					<select id="rdLibTypE" name="rdLibTypE" class="select-medium" style="display:none">
					</select><br>
					延期时间:
					<input type="text" name="i" id="i" value=""/>
					
				</div>
				
		</div>
		</form>
  	</div>
  	<div class="modal-footer">
    	<a href="#" class="btn" data-dismiss="modal">取消</a>
    	<a href="#" onclick="javascript:interval();" class="btn btn-success"><i class="icon-ok icon-white"></i>确定</a>
  	</div>
</div>


<%@include file="/jsp_tiles/include/admin/paginator.jsp" %>
		    </div>
		</div>
	</div>
	
</section>
<script type="text/javascript">
//计算批量读者证号生成截止日期
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
//计算批量延期截止日期
	function calEndDatE(rdTypE) {
		$.ajax({
			type : "POST",
			url : "<c:url value='/admin/reader/calEndDate' />",
			data : {rdTypE : rdTypE},
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
//计算导出excel截止日期
	function calendDate(rdtype) {
		$.ajax({
			type : "POST",
			url : "<c:url value='/admin/reader/calEndDate' />",
			data : {rdtype : rdtype},
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
//获取指定馆下的读者类型-批量读者证号生成
	function getLibReaderType(libCode,selectRdtype,selectRdLibtype) {//添加选中的读者类型
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
						if(selectRdtype==types[i].READERTYPE){
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
				/* alert(jsonData);  */
				var types = eval(jsonData);
				if(types.length){
					for(var i=0; i<types.length; i++){
						rdLibType.options.add(new Option(types[i].SHOWREADERTYPE,types[i].READERTYPE));
						if(selectRdLibtype==types[i].READERTYPE){
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
	
	//获取指定馆下的读者类型-导出excel
	function getlibReaderType(libCode,selectRdtype,selectRdLibtype) {//添加选中的读者类型
		$.ajax({
			type : "POST",
			url : "<c:url value='/admin/reader/getLibReaderType' />",
			data : {thisLibCode : libCode},
			dataType : "json",
			success : function(jsonData){
				var rdtype = document.getElementById("rdtype");
				rdtype.length = 0;
				var types = eval(jsonData);
				if(types.length > 1){
					 calendDate(types[0].READERTYPE); 
					for(var i=0; i<types.length-1; i++){
						rdtype.options.add(new Option(types[i].SHOWREADERTYPE,types[i].READERTYPE));
						if(selectRdtype==types[i].READERTYPE){
							rdtype[i].selected=true;
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
				var rdlibType = document.getElementById("rdlibType");
				rdlibType.length = 0;
				/* alert(jsonData);  */
				var types = eval(jsonData);
				if(types.length){
					for(var i=0; i<types.length; i++){
						rdlibType.options.add(new Option(types[i].SHOWREADERTYPE,types[i].READERTYPE));
						if(selectRdLibtype==types[i].READERTYPE){
							rdlibType[i].selected=true;
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
	
	//获取指定馆下的读者类型-批量延期
	function getLibReaderTypE(libCode,selectRdtype,selectRdLibtype) {//添加选中的读者类型
		$.ajax({
			type : "POST",
			url : "<c:url value='/admin/reader/getLibReaderType' />",
			data : {thisLibCode : libCode},
			dataType : "json",
			success : function(jsonData){
				var rdTypE = document.getElementById("rdTypE");
				rdTypE.length = 0;
				var types = eval(jsonData);
				if(types.length > 1){
					 calEndDatE(types[0].READERTYPE); 
					for(var i=0; i<types.length-1; i++){
						rdTypE.options.add(new Option(types[i].SHOWREADERTYPE,types[i].READERTYPE));
						if(selectRdtype==types[i].READERTYPE){
							rdTypE[i].selected=true;
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
				var rdLibTypE = document.getElementById("rdLibTypE");
				rdLibTypE.length = 0;
				/* alert(jsonData);  */
				var types = eval(jsonData);
				if(types.length){
					for(var i=0; i<types.length; i++){
						rdLibTypE.options.add(new Option(types[i].SHOWREADERTYPE,types[i].READERTYPE));
						if(selectRdLibtype==types[i].READERTYPE){
							rdLibTypE[i].selected=true;
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
	
</script>