<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/jsp_tiles/include/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<script type="text/javascript" src="<c:url value='/media/datepicker/WdatePicker.js' />"></script>
<c:set var="BASE_URL">
	<c:url value="/admin/sys/cirfinlog" />
</c:set>

<script type="text/javascript">
$(function(){
	$("#regLib").val('${obj.regLib}');
	$("#regman").val('${obj.regman}');
	$("#rdid").val('${obj.rdid}');
	$("#feetype").val('${obj.feetype}');
	$("#startTime").val('${obj.startTime}');
	$("#endTime").val('${obj.endTime}');
});

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


function exportMoreLibFinSettleExcel(){
	var form1 = document.getElementById("form1");
	form1.action = "<c:url value='/admin/sys/cirfinlog/exportMoreLibFinSettleExcel' />";
	form1.submit();
	form1.action = "";//重置下action值 不然导出报表之后其他按钮都是导出报表功能 20150710
}

function search(){
	var form1 = document.getElementById("form1");
	form1.action = "<c:url value='/admin/sys/cirfinlog/moreLibFinSettle' />";
	form1.submit();
	form1.action = "";//重置下action值  20150710
}

</script>

<section id="tabs">
	<div>
		<ul id="myTab" class="nav nav-tabs">
			<li class="active"><a href="#" data-toggle="tab">多馆财经结算功能</a></li>
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
		<form action="moreLibFinSettle" id="form1" method="POST" class="form-inline">
			<div class="control-group">
			<span>操&nbsp;&nbsp;作&nbsp;&nbsp;馆：</span>
			<select name="regLib" id="regLib" class="input-medium search-query-extend">
				<option value="">请选择</option>
				<c:forEach items="${simpleLibcode}" var="entry">
					<c:if test="${obj.regLib eq (entry.libCode) }">
						<option value="${entry.libCode}" selected="selected">${entry.libCode} | ${entry.simpleName}</option>
					</c:if>
					<c:if test="${!(obj.regLib eq (entry.libCode)) }">
						<option value="${entry.libCode}">${entry.libCode} | ${entry.simpleName}</option>
					</c:if>
				</c:forEach>
			</select>
			</div>
			<div class="control-group">
			<span>所属分组：</span>
			<select name="groupID" id="groupID" class="input-medium search-query-extend">
				<option value="">无</option>
				<c:forEach items="${groups}" var="gs">
					<c:if test="${obj.groupID==(gs.GROUPID) }">
						<option value="${gs.GROUPID}" selected="selected">${gs.GROUPNAME}</option>
					</c:if>
					<c:if test="${(obj.groupID!=(gs.GROUPID)) }">
						<option value="${gs.GROUPID}">${gs.GROUPNAME}</option>
					</c:if>
				</c:forEach>
			</select>
			</div>
			<div class="control-group">
				<span>日&nbsp;&nbsp;期&nbsp;&nbsp;段：</span>
				<input type="text" name="startTime" id="startTime" value="${obj.startTime }" onFocus="WdatePicker({isShowClear:false})" class="input-medium search-query-extend" /> 
				到
				<input type="text" name="endTime" id="endTime" value="${obj.endTime }" onFocus="WdatePicker({isShowClear:false})" class="input-medium search-query-extend" />
				<button class="btn btn-info" type="submit" onclick="search();">查询</button>
			</div>
		</form>
	</p>
	
	</div>
</fieldset>
</p>
<button class="btn btn-info" type="button" onclick="exportMoreLibFinSettleExcel();">导出Excel</button>
</p>
<table class="table table-bordered table-hover table-condensed table-striped">
	<thead>
		<tr>
			<th>所属馆名称</th>
			<c:forEach items="${libs }" var="li">
				<th>${li.simpleName }</th>
			</c:forEach>
			<th>馆小计</th>
		</tr>
	</thead>
	<tbody>
		<c:forEach items="${pageList }" var="lib" >
			<tr>
				<td><c:out value="${lib.SIMPLENAME }"></c:out></td>
				<c:forEach items="${libs }" var="li" varStatus="status">
					<c:set var="n1" value="LIBNAME${status.index }" />
				 	<td>
				 	<c:if test="${lib[n1]>0 }">
				 	 收取${lib[n1] }
				 	</c:if>
				 	<c:if test="${lib[n1]<0 }">
				 	 退还${-lib[n1] }
				 	</c:if>
				 	<c:if test="${lib[n1]==0 }">
				 	 ${lib[n1] }
				 	</c:if>
				 	</td>
				</c:forEach>
			    <td><c:out value="${lib.TOTAL_FEE }"></c:out></td>
			</tr>
		</c:forEach>
	</tbody>
</table>

<%-- <c:set var="pager_base_url">${BASE_URL}/moreLibFinSettle?</c:set> --%>
<%-- <%@include file="/jsp_tiles/include/admin/pager.jsp" %> --%>


		    </div>
		</div>
	</div>
</section>