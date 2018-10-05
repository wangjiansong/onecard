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

function exportExcel(){
	var form1 = document.getElementById("form1");
	form1.action = "<c:url value='/admin/sys/cirfinlog/exportStaticsExcel' />";
	form1.submit();
	form1.action = "";//重置下action值 不然导出报表之后其他按钮都是导出报表功能 20150710
}
</script>

		<div class="well form-actions_1">
			<%-- 		 发生分馆：【${orgLib}】--%>
			 操作分馆：【${cirFinLog.regLib}】  所属分组：【${groupbean.groupName }】时间：【${cirFinLog.startTime}】至【${cirFinLog.endTime}】 
		</div>
		<form action="list" id="form1" method="POST" class="form-inline">
		<input type="hidden" name="regLib" value="${cirFinLog.regLib }" />
		<input type="hidden" name="regman" value="${cirFinLog.regman }"/>
		<input type="hidden" name="groupID" value="${cirFinLog.groupID}"/>
		<input type="hidden" name="feetype" value="${cirFinLog.feetype }"/>
		<input type="hidden" name="startTime" value="${cirFinLog.startTime}" />
		<input type="hidden" name="endTime" value="${cirFinLog.endTime}" />
		<input type="hidden" name="groupby" value="${groupby}"/>
		
		</p>
			<button class="btn btn-info" type="button" onclick="exportExcel();">导出Excel</button>
		</p>
		</form>
		<table class="table table-bordered table-hover table-condensed table-striped">
			<thead>
				<tr>
				<c:forEach items="${showColNameList}" var="showColName">
					<th>${showColName }</th>
				</c:forEach>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${statisList}" var="map">
					<c:if test="${not empty map['FEETYPE']}">
		 			<tr class="f">
		 				<td>
		 					<c:out value="${map['GROUPNAME']}" />
<%-- 		 					<c:if test="${map['FEETYPESIGN'] eq '1'}"><c:out value="${map['GROUPNAME']}" /></c:if> --%>
<%-- 		 					<c:if test="${map['GROUPNAMESIGN'] eq '1'}">总计</c:if> --%>
		 				</td>
		 				<td><c:out value="${map['COLNAME1']}" /></td>
		 				<td><c:out value="${map['COLNAME2']}" /></td>
		 				<td>
		 					<c:forEach items="${finTypeList}" var="fintype">
			 					<c:if test="${map['FEETYPE'] eq (fintype.feeType)}">
			 					<c:out value="${fintype.describe }"/>
			 					</c:if>
		 					</c:forEach>
<%-- 		 				<c:out value="${map['FEETYPE']}" /> --%>
		 				</td>
		 				<td><c:out value="${map['COUNT']}" /></td>
		 				<td><c:out value="${map['NUM']}" /></td>
		 			</tr>
		 			</c:if>
		 			<c:if test="${(map['GROUPNAMESIGN'] eq '1') }">
		 			<tr class="f">
		 				<td>
		 					<c:if test="${map['GROUPNAMESIGN'] eq '1'}">总计</c:if>
		 				</td>
		 				<td> </td>
		 				<td> </td>
		 				<td>
		 					<c:forEach items="${finTypeList}" var="fintype">
			 					<c:if test="${map['FEETYPE'] eq (fintype.feeType)}">
			 					<c:out value="${fintype.describe }"/>
			 					</c:if>
		 					</c:forEach>
		 				</td>
		 				<td><c:out value="${map['COUNT']}" /></td>
		 				<td><c:out value="${map['NUM']}" /></td>
		 			</tr>
		 			</c:if>
		    	</c:forEach>
			</tbody>
		</table>

<c:set var="pager_base_url">${BASE_URL}/list?</c:set>

