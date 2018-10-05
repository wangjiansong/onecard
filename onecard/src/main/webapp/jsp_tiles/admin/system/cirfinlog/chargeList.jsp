<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/jsp_tiles/include/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<c:set var="BASE_URL">
	<c:url value="/admin/sys/cirfinlog" />
</c:set>

<script type="text/javascript">
$(function(){
	if("${obj.startTime}"){
		document.getElementById("startTime").value = "${obj.startTime}";
	}
	if("${obj.endTime}"){
		document.getElementById("endTime").value = "${obj.endTime}";
	}
	if("${obj.searchKey}"){
		document.getElementById("searchKey").value = "${obj.searchKey}";
	}
	if("${obj.searchValue}"){
		document.getElementById("searchValue").value = "${obj.searchValue}";
	}
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
function checkForm() {
	var searchKey = document.getElementById("searchKey").value;
	var searchValue = document.getElementById("searchValue").value;
	
	if(searchKey == 'rdid') {
		document.getElementById("rdid").value = searchValue;
	} else if(searchKey == 'rdname') {
		document.getElementById("rdname").value = searchValue;
	} else if(searchKey == 'payId') {
		document.getElementById("payId").value = searchValue;
	}
	document.searchform.submit();
}

</script>
<section id="tabs">
	<div>
		<ul id="myTab" class="nav nav-tabs">
			<li class="active"><a href="#" data-toggle="tab">充值查询</a></li>
		</ul>
		<div id="myTabContent" class="tab-content">
		    <div class="tab-pane active">


<fieldset>
	<c:if test="${!empty message}">
		<div class="alert alert-success">
			<a class="close" data-dismiss="alert">&times;</a>
			${message}
		</div>
	</c:if>
	<form action="chargeList" id="form1" method="POST" class="form-inline" name="searchform">
	<div class="well form-actions_1" align="left">
		<div class="input-append">
			<select id="searchKey" name="searchKey" class="input-medium search-query-extend">
				<option value="rdid">读者证号</option>
				<option value="rdname">读者姓名</option>
				<!--充值接口产生的交易单号 实际统一用户没有 <option value="payId">交易单号</option> -->
			</select>
			<input type="text" id="searchValue" name="searchValue" class="input-medium search-query-extend" />
		</div>
		<span>充值日期</span>
		<div class="input-append">
		<input type="text" name="startTime" id="startTime"  class="date form_datetime input-small search-query-extend" />
		<span class="add-on"><i class="icon-calendar"></i></span>
		</div>
		到
		<div class="input-append">
			<input type="text" name="endTime" id="endTime"  class="date form_datetime input-small search-query-extend" />
			<span class="add-on"><i class="icon-calendar"></i></span>
			<button class="btn btn-info" onclick="javascript:checkForm();">查询</button>
			<button class="btn" type="reset">重置</button>
		</div>
		
		<input type="hidden" name="rdid" id="rdid" class="input-medium search-query-extend" />
		<input type="hidden" name="rdname" id="rdname" class="input-medium search-query-extend" />
		<input type="hidden" name="payId" id="payId" class="input-medium search-query-extend" />
	</div>
	</form>
	
</fieldset>
<table class="table table-bordered table-hover table-condensed table-striped">
	<thead>
		<tr>
			<th>姓名</th>
			<th>读者证号</th>
			<th>充值金额(单位：元)</th>
			<th>充值时间</th>
			<th>充值类型</th>
			<!-- <th>交易单号</th> -->
			<th>经手人</th>
			<th>经手馆</th>
		</tr>
	</thead>
	<tbody>
		<c:forEach items="${pageList}" var="p">
 			<tr class="f">
 				<td><c:out value="${p.rdname}" /></td>
 				<td><c:out value="${p.rdid}" /></td>
 				<td><c:out value="${p.fee}" /></td>
 				<td><fmt:formatDate value="${p.regtime}"  pattern="yyyy-MM-dd HH:mm:ss" /></td>
 				<td><c:out value="${p.feetype}" /></td>
 				<%-- <td>
	 				<c:forEach items="${list}" var="l">
	 					<c:out value="${l.charType}" />
	 				</c:forEach>
 				</td>--%>
 				
 				<%-- <td><c:out value="${p.payId}" /></td> --%>
 				<td><c:out value="${p.regman}" /></td>
 				<td><c:out value="${p.regLib}" /></td>
 			</tr>
    	</c:forEach>
	</tbody>
</table>

<c:set var="pager_base_url">${BASE_URL}/list?</c:set>
<%@include file="/jsp_tiles/include/admin/pager.jsp" %>


		    </div>
		</div>
	</div>
</section>