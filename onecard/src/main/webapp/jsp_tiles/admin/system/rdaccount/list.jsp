<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/jsp_tiles/include/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<script type="text/javascript" src="<c:url value='/media/datepicker/WdatePicker.js' />"></script>
<c:set var="BASE_URL">
	<c:url value="/admin/sys/rdaccount" />
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
function statusInfo(status) {
	if(status == 1) return "正常使用";
	if(status == 2) return "冻结";
	if(status == 3) return "未激活";
	if(status == 4) return "注销";
}
function lock(rdid) {
	if(!confirm("冻结后该账户将不能正常使用，确定执行冻结？")) {
		return;
	}
	document.location.href = "${BASE_URL}/doLock/" +rdid;
}
function unlock(rdid) {
	if(!confirm("恢复该账户将继续正常使用，确定执行恢复？")) {
		return;
	}
	document.location.href = "${BASE_URL}/doUnLock/" +rdid;
}
//提示信息
function showResult(tip) {
	$("#tip").css("display", "block").html(tip);
	setTimeout(function() {
		$("#tip").fadeOut("slow");
	}, 5000);
	$("#tip").attr("class", "A_MESSAGEBOX_LOADING alert alert-success");
	return;
}
</script>
<div id="tip" class="A_MESSAGEBOX_LOADING alert alert-success" style="display: none;">
</div>
<section id="tabs">
	<div>
		<ul id="myTab" class="nav nav-tabs">
			<li class="active"><a href="#" data-toggle="tab">账户列表</a></li>
		</ul>
		<div id="myTabContent" class="tab-content">
		    <div class="tab-pane active">

<fieldset>
	<div class="well form-actions_1">
	<p>
		<form action="list" id="form1" method="POST" class="form-inline">
			<span>读者证号：</span>
			<div class="input-append">
			<input type="text" name="rdid" value="${obj.rdid }" class="input-medium search-query-extend" />
			<button class="btn btn-info" type="submit">查询</button>
			</div>
		</form>
	</p>
	
	</div>
</fieldset>
<table class="table table-bordered table-hover table-condensed table-striped">
	<thead>
		<tr>
			<th>读者证号</th>
			<th>押金(单位：元)</th>
			<th>一卡通余额(单位：元)</th>
			<th>帐户状态</th>
			<th>操作</th>
		</tr>
	</thead>
	<tbody>
		<c:forEach items="${pageList}" var="p">
 			<tr class="f">
 				<td><c:out value="${p.rdid}" /></td>
 				<td><c:out value="${p.deposit}" /></td>
 				<td><c:out value="${p.prepay}" /></td>
 				
 					<c:if test="${p.status==1}">
 					<td>
 						正常使用
 					</td>
 					<td>
	 				<div class="btn-group">
					<input id="lock" type="button" class="btn btn-danger btn-small" value="冻结" onclick="lock('${p.rdid}')"/>
					</div>
	 				</td>
 					</c:if>
 					<c:if test="${p.status==2}">
 					<td>
 						冻结
 					</td>
 					<td>
	 				<div class="btn-group">
					<input id="unlock" type="button" class="btn btn-success btn-small" value="恢复" onclick="unlock('${p.rdid}')" />
					</div>
	 				</td>
 					</c:if>
 					<c:if test="${p.status==3}">
 					<td>
 						未激活
 					</td>
 					<td>
	 				</td>
 					</c:if>
 					<c:if test="${p.status==4}">
 					<td>
 						注销
 					</td>
 					<td>
	 				</td>
 					</c:if>
 				</td>
 			</tr>
    	</c:forEach>
	</tbody>
</table>

<c:set var="pager_base_url">${BASE_URL}/list?</c:set>
<%@include file="/jsp_tiles/include/admin/paginator.jsp" %>


		    </div>
		</div>
	</div>
</section>