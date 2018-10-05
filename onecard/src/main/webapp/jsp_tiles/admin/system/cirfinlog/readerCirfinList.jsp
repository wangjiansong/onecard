<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/jsp_tiles/include/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<script type="text/javascript" src="<c:url value='/media/datepicker/WdatePicker.js' />"></script>
<c:set var="BASE_URL">
	<c:url value="/admin/sys/cirfinlog" />
</c:set>

<script type="text/javascript">

function pressEnter(e,method){
	if(e==null){//ie6..
		e=event;
	}	
	if ((e.which == 13 || e.keyCode == 13)&&(!(e.ctrlKey || e.metaKey))
			&& !e.shiftKey && !e.altKey) {
		 method();
	}
}

</script>

<section id="tabs">
	<div>
		<ul id="myTab" class="nav nav-tabs">
			<li class="active"><a href="#" data-toggle="tab">消费金额查询</a></li>
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
	<p>
		<form action="list" id="form1" method="POST" class="form-inline">
			<div class="control-group">
			<select name="regLib" class="input-medium search-query-extend">
				<option value="">请选择分馆</option>
				<c:forEach items="${simpleLibcode}" var="entry">
					<option value="${entry.libCode}">${entry.libCode} | ${entry.simpleName}</option>
				</c:forEach>
			</select>
			</div>
			<div class="control-group">
			<input type="text" name="regman" class="input-medium search-query-extend" placeholder="操作人ID"/>
			</div>
			<div class="input-append">
				<select id="fieldName" style="width: 123px;">
					<option value="rdId">读者证号</option>
					<option value="rdName">读者姓名</option>
					<option value="rdCertify">读者身份证号</option>
				</select>
				<input type="text" id="fieldValue" style="border-radius: 0px;width: 130px;border-left: 0px;">
			</div>
			<div class="control-group">
			<span>日期段：</span>
			<input type="text" name="startTime" onFocus="WdatePicker({isShowClear:false})" class="input-medium search-query-extend" />到
			<input type="text" name="endTime" onFocus="WdatePicker({isShowClear:false})" class="input-medium search-query-extend" />
			<button class="btn btn-info" type="submit">查询</button>
			</div>
		</form>
	</p>
	
	</div>
</fieldset>

<table class="table table-bordered table-hover table-condensed table-striped">
	<thead>
		<tr>
			<th>消费人数</th>
			<th>消费人次</th>
			<th>消费总额</th>
		</tr>
	</thead>
	<tbody>
		<tr class="f">
			<td><c:out value="${p.regman}" /></td>
			<td><c:out value="${p.regLib}" /></td>
			<td><c:out value="${p.rdid}" /></td>
		</tr>
	</tbody>
</table>

<button class="btn btn-success" type="submit">导出报表</button>

<table class="table table-bordered table-hover table-condensed table-striped">
	<thead>
		<tr>
			<th>读者姓名</th>
			<th>读者证号</th>
			<th>消费类型</th>
			<th>消费金额（单位：元）</th>
			<th>消费系统</th>
			<th>收款员</th>
			<th>消费时间</th>
		</tr>
	</thead>
	<tbody>
		<c:forEach items="${pageList}" var="p">
 			<tr class="f">
 				<td><c:out value="${p.rdname}" /></td>
 				<td><c:out value="${p.rdid}" /></td>
 				<td><c:out value="${p.feetype}" /></td>
 				<td><c:out value="${p.fee}" /></td>
 				<td><c:out value="${p.feeAppCode}" /></td>
 				<td><c:out value="${p.regman}" /></td>
 				<td><fmt:formatDate value="${p.regtime}"  pattern="yyyy-MM-dd HH:mm:ss" /></td>
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