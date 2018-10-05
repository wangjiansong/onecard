<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/jsp_tiles/include/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<script type="text/javascript" src="<c:url value='/media/datepicker/WdatePicker.js' />"></script>

<c:set var="BASE_URL">
	<c:url value="/admin/sys/cirfinlog" />
</c:set>

<script type="text/javascript">
$(function() {
	reshMessage();
});
//日期格式转换
function formatDate(str, format) {
    if (!str) return '';

    var i = parseInt(str.match(/[-]*\d+/g)[0]);
    if (i < 0) return '';
    var d = new Date(i);
    if (d.toString() == 'Invalid Date') return '';

    //处理客户端时区不同导致的问题
    //480 是UTC+8
    var utc8Offset = 480;
    d.setMinutes(d.getMinutes() + (d.getTimezoneOffset() + 480));

    format = format || 'MM/dd hh:mm:ss tt';

    var hour = d.getHours();
    var month = FormatNum(d.getMonth() + 1)

    var re = format.replace('YYYY', d.getFullYear())
    .replace('YY', FormatNum(d.getFullYear() % 100))
    .replace('MM', FormatNum(month))
    .replace('dd', FormatNum(d.getDate()))
    .replace('hh', hour == 0 ? '12' : FormatNum(hour <= 12 ? hour : hour - 12))
    .replace('HH', FormatNum(hour))
    .replace('mm', FormatNum(d.getMinutes()))
    .replace('ss', FormatNum(d.getSeconds()))
    .replace('tt', (hour < 12 ? 'AM' : 'PM'));

    return re;

    function FormatNum(num) {
        num = Number(num);
        return num < 10 ? ('0' + num) : num.toString();
    }
}

function reshMessage(){
	var feeType=document.getElementById("feeType").value;
	var showCount=document.getElementById("showCount").value;
	$.ajax({
		type: "GET",
		url: "<c:url value='/cirfinloglist/cirfinlogListJson?feetype="+feeType+"&page.showCount="+showCount+"' />",
		dataType: "json",
		success: function(jsonData){
			var table=document.getElementById("cirfinlogList");
			var cirfinlogs = eval(jsonData);
			var totalnum=table.rows.length;
			if(totalnum>1){
				for(var n=totalnum-1;n>=1;n--){
					table.deleteRow(n);
				}
			}
			if(cirfinlogs.length >= 1){
				for(var i=0; i<cirfinlogs.length; i++){
					var paystr="";
					var row = table.insertRow(i+1);
					if(i%2==0){
						row.setAttribute("style","background-color: #d9edf7;");
					}
					row.setAttribute("class","f");
					row.setAttribute("align","center");
					row.style.align = "center";
					if(cirfinlogs[i].paySign==0){
						paystr="未交付";
					}else if(cirfinlogs[i].paySign==1){
						paystr="已交付";
					}else if(cirfinlogs[i].paySign==2){
						paystr="已取消";
					}else if(cirfinlogs[i].paySign==3){
						paystr="已退还";
					}
					row.insertCell(0).innerHTML = cirfinlogs[i].regman;
					row.insertCell(1).innerHTML = cirfinlogs[i].rdname;
					row.insertCell(2).innerHTML = cirfinlogs[i].rdid;
					row.insertCell(3).innerHTML = cirfinlogs[i].feetype;
					row.insertCell(4).innerHTML = cirfinlogs[i].fee;
					row.insertCell(5).innerHTML = paystr;
					var regtime=formatDate('/Date('+cirfinlogs[i].regtime+')/', 'YYYY-MM-dd HH:mm:ss')
					row.insertCell(6).innerHTML =regtime ;
				}
			}else{
				//没有数据返回
			}
		},
		cache: false
	});
}
window.onload=setInterval("reshMessage()","5000");//每隔5秒自动刷新
function Add(){
	document.location.href="<c:url value='' />";
}
function Edit(id){
	document.location.href="${BASE_URL}/edit/"+id;
}
function validata(id) {
	$.ajax({
		type: "GET",
		url: "validate/"+id,
		dataType: "html",
		success: function(htmlData){
			if (!confirm(htmlData + " 你确定要删除此条记录？")) return;
			Delete(id);
		},
		cache: false
	});
}

function pressEnter(e,method){
	if(e==null){//ie6..
		e=event;
	}	
	if ((e.which == 13 || e.keyCode == 13)&&(!(e.ctrlKey || e.metaKey))
			&& !e.shiftKey && !e.altKey) {
		 method();
	}
}
/* function exportExcel(){
	var form1 = document.getElementById("form1");
	form1.action = "<c:url value='/admin/sys/cirfinlog/exportCirfinlogListExcel' />";
	form1.submit();
	form1.action = "";//重置下action值 不然导出报表之后其他按钮都是导出报表功能 20150710
} */
function doSubmit(){
	var dataform = document.getElementById("form1");
	dataform.action = "<c:url value='/admin/sys/cirfinlog/cirfinlogList' />";
	dataform.submit();
	form1.action = "";
}
</script>

<section id="tabs">
	<div>
		<ul id="myTab" class="nav nav-tabs">
			<li class="active"><a href="#" data-toggle="tab">财经明细列表</a></li>
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
	<%--  <p>
		<form action="list" id="form1" method="POST" class="form-inline">
			<input type="hidden" id="feeType" name="feetype" value="206" />
			<div class="control-group" width=" 50px;">
			<span>每页记录数：</span>
			<input type="text" id="showCount"  name="page.showCount" value="${obj.page.showCount }" class="input-medium search-query-extend"  />
			<button class="btn btn-info" type="submit" onclick="doSubmit();">查询</button>
			</div>
		</form>
	</p>  --%>
	<p>
		<form action="list" id="form1" method="POST" class="form-inline">
			<span>读者证号：</span>
			<div class="input-append">
			<input type="text" name="rdid" value="${obj.rdid }" class="input-medium search-query-extend" />
			<button class="btn btn-info" type="submit" onclick="doSubmit();">查询</button>
			</div>
		</form>
	</p> 
	</div>
</fieldset>
<!-- <table align = "center" class="table table-bordered table-hover table-condensed table-striped">
 -->	
 <table class="table table-bordered table-hover table-condensed table-striped">
 <thead>
		<tr>
			<th>操作人</th>
			<th>操作馆 </th>			
			<th>读者证号</th>
			<th>读者姓名</th>
			<!-- <th>收费类型</th> -->
			<th>费用(单位：元)</th>
			<th>缴付标识</th>
			<th>日期</th>
		</tr>
	</thead>
<tbody>
		<c:forEach items="${pageList}" var="p">
 			<tr class="f">
 				<td><c:out value="${p.regman}" /></td>
 				<td><c:out value="${p.regLib}" /></td>
 				<td><c:out value="${p.rdid}" /></td>
 				<td><c:out value="${p.rdname}" /></td>
 				<td><c:out value="${p.fee}" /></td>
 				<td>
 					<c:if test="${p.paySign eq 0}">
 						未交付
 					</c:if>
 					<c:if test="${p.paySign eq 1}">
 						已交付
 					</c:if>
 					<c:if test="${p.paySign eq 2}">
 						已取消
 					</c:if>
 					<c:if test="${p.paySign eq 3}">
 						已退还
 					</c:if>
 				</td>

 				<td><fmt:formatDate value="${p.regtime}"  pattern="yyyy-MM-dd HH:mm:ss" /></td>
 			</tr>
    	</c:forEach>
    		</tbody>
    	</table>

<%--  <c:set var="pager_base_url">${BASE_URL}/cirfinlogList?</c:set>
 <%@include file="/jsp_tiles/include/admin/pager.jsp" %>   --%>


		    </div>
		</div>
	</div>
</section>