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


function exportAutoStatistcsExcel(){
	var form1 = document.getElementById("form1");
	form1.action = "<c:url value='/admin/sys/cirfinlog/exportAutoStatistcsExcel' />";
	form1.submit();
	form1.action = "";//重置下action值 不然导出报表之后其他按钮都是导出报表功能 20150710
}

function search(){
	var form1 = document.getElementById("form1");
	form1.action = "<c:url value='/admin/sys/cirfinlog/autoStatistics' />";
	form1.submit();
	form1.action = "";//重置下action值20150710
}

</script>

<section id="tabs">
	<div>
		<ul id="myTab" class="nav nav-tabs">
			<li class="active"><a href="#" data-toggle="tab">自动统计功能</a></li>
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
		<form action="autoStatistics" id="form1" method="POST" class="form-inline">
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
			<span>读者证号：</span>
			<input type="text" name="rdid" id="rdid" value="${obj.rdid }" class="input-medium search-query-extend" />
			</div>
			<!--  -->
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
			<span>每页数量：</span>
			<input type="text"  name="page.showCount" value="${obj.page.showCount }" class="input-medium search-query-extend"  />
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
<button class="btn btn-info" type="button" onclick="exportAutoStatistcsExcel();">导出Excel</button>
</p>
<table class="table table-bordered table-hover table-condensed table-striped">
	<thead>
		<tr>
			<th>读者账号</th>
<!-- 			<th>可用余额(单位：元)</th> -->
			<!-- 下面是操作馆名字 从数据库查询显示 -->
			<c:forEach items="${libs }" var="lib">
				<th>${lib.simpleName }费用(单位：元)</th>
			</c:forEach>
			
		</tr>
	</thead>
	<tbody>
		<c:forEach items="${pageList}" var="p">
 			<tr class="f">
 				<td><c:out value="${p.rdid}" /></td>
<%--  				<td><c:out value="${p.sumFee}" /></td> --%>
 				<c:forEach items="${libs }" var="lib">
 					<c:forEach items="${fn:split(p.reglibSet, ',')}" var="reglib" varStatus="s">
 					<c:if test="${reglib eq lib.libCode}">
 						<td>${-(fn:split(p.totalfeeSet, ',')[s.index])}</td>
 					</c:if>
 					</c:forEach>
				</c:forEach>
 			</tr>
    	</c:forEach>
    	<tr style="font-weight: 800;font-size:2em;">
    		<c:if test="${totalFeeResult.size()>0 }">
    	 	<td>本页小计</td>
    		</c:if>
    		<!-- 
    	 	<c:forEach items="${totalFeeResult }" var="total">
    	 		<c:if test="${total.key eq 'totalrdfee'}"><td>${total.value }</td></c:if>
    	 	</c:forEach>
    	 	 -->
	    	<c:forEach items="${libs }" var="lib" varStatus="s">
	    	 	<c:forEach items="${totalFeeResult }" var="total">
		    	 	<c:if test="${total.key eq lib.libCode}">
			    	 	<c:if test="${total.value<0 }">
			    	 	<td>应退还${-total.value }</td>
			    	 	</c:if>
			    	 	<c:if test="${total.value==0 }">
			    	 	 <td>${total.value }</td>
			    	 	</c:if>
			    	 	<c:if test="${total.value>0 }">
			    	 	<td>应收取${total.value }</td>
			    	 	</c:if>
		    	 	</c:if>
	    	 	</c:forEach>
	    	</c:forEach>
    	</tr>
    	<tr><td colspan="${libs.size()+1 }" style="height: 20px;"></td></tr>
	<!-- 总计集合    	alltotalFeeResult -->
	<tr style="font-weight: 800;font-size:2em;color:red;">
    		<c:if test="${alltotalFeeResult.size()>0 }">
    	 	<td>总计</td>
    		</c:if>
	    	<c:forEach items="${libs }" var="lib" varStatus="s">
	    	 	<c:forEach items="${alltotalFeeResult }" var="total">
		    	 	<c:if test="${total.key eq lib.libCode}">
			    	 	<c:if test="${total.value<0 }">
			    	 	<td>应退还${-total.value }</td>
			    	 	</c:if>
			    	 	<c:if test="${total.value==0 }">
			    	 	 <td>${total.value }</td>
			    	 	</c:if>
			    	 	<c:if test="${total.value>0 }">
			    	 	<td>应收取${total.value }</td>
			    	 	</c:if>
		    	 	</c:if>
	    	 	</c:forEach>
	    	</c:forEach>
    	</tr>
	</tbody>
</table>

<c:set var="pager_base_url">${BASE_URL}/autoStatistics?</c:set>
<%@include file="/jsp_tiles/include/admin/pager.jsp" %>


		    </div>
		</div>
	</div>
</section>