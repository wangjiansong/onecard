<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/jsp_tiles/include/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<c:set var="BASE_URL">
	<c:url value="/admin/sys/readerfee" />
</c:set>

<script type="text/javascript">
$(function(){
	var libcodes = eval('${libcodes}');
	var libcode = $("#libcode");
	for(var i=0; i<libcodes.length; i++){
		//libcode.options.add(new Option(libcodes[i].SIMPLENAME +" | " + libcodes[i].LIBCODE,));
		//<li><a href="#" onclick="renew()">恢复</a></li>
		libcode.append("<li><a href=\"#\" onclick=\"showFee('"+ libcodes[i].LIBCODE +"')\">"+ libcodes[i].SIMPLENAME +" | " + libcodes[i].LIBCODE +"</a></li>");
	}
});
function showFee(libcode) {
	document.location.href = "${BASE_URL}/list/" +libcode;
}
function submitForm() {
	var cardfee = checkChecked($(".cardfee .check").attr("checked")) + checkChecked($(".cardfee .service").attr("checked")) + checkChecked($(".cardfee .id").attr("checked"));
	var renewfee = checkChecked($(".renewfee .check").attr("checked")) + checkChecked($(".renewfee .service").attr("checked")) + checkChecked($(".renewfee .id").attr("checked"));
	var checkfee = checkChecked($(".checkfee .check").attr("checked")) + checkChecked($(".checkfee .service").attr("checked")) + checkChecked($(".checkfee .id").attr("checked"));
	var lossfee = checkChecked($(".lossfee .check").attr("checked")) + checkChecked($(".lossfee .service").attr("checked")) + checkChecked($(".lossfee .id").attr("checked"));
	var stopfee = checkChecked($(".stopfee .check").attr("checked")) + checkChecked($(".stopfee .service").attr("checked")) + checkChecked($(".stopfee .id").attr("checked"));
	var logoutfee = checkChecked($(".logoutfee .check").attr("checked")) + checkChecked($(".logoutfee .service").attr("checked")) + checkChecked($(".logoutfee .id").attr("checked"));
	var quitfee = checkChecked($(".quitfee .check").attr("checked")) + checkChecked($(".quitfee .service").attr("checked")) + checkChecked($(".quitfee .id").attr("checked"));
	var repairfee = checkChecked($(".repairfee .check").attr("checked")) + checkChecked($(".repairfee .service").attr("checked")) + checkChecked($(".repairfee .id").attr("checked"));
	var deferfee = checkChecked($(".deferfee .check").attr("checked")) + checkChecked($(".deferfee .service").attr("checked")) + checkChecked($(".deferfee .id").attr("checked"));
	var changefee = checkChecked($(".changefee .check").attr("checked")) + checkChecked($(".changefee .service").attr("checked")) + checkChecked($(".changefee .id").attr("checked"));
	$("#cardfee").attr("value", cardfee);
	$("#renewfee").attr("value", renewfee);
	$("#checkfee").attr("value", checkfee);
	$("#lossfee").attr("value", lossfee);
	$("#stopfee").attr("value", stopfee);
	$("#logoutfee").attr("value", logoutfee);
	$("#quitfee").attr("value", quitfee);
	$("#repairfee").attr("value", repairfee);
	$("#deferfee").attr("value", deferfee);
	$("#changefee").attr("value", changefee);
	document.submitform.submit();
}
function checkChecked(val) {
	if(val == 'checked') {
		return '1';
	} else {
		return '0';
	}
	
} 
</script>

<section id="tabs">
	<div>
		<ul id="myTab" class="nav nav-tabs">
			<li class="active"><a href="#" data-toggle="tab">费用参数管理</a></li>
		</ul>
		<div id="myTabContent" class="tab-content">
		    <div class="tab-pane active">

<fieldset>
	
	<div class="well form-actions_1">
	<div class="btn-group" style="margin-left: 5px;">
			<button class="btn btn btn-success">选择分馆</button>
			<button class="btn btn btn-success dropdown-toggle" data-toggle="dropdown">
	    		<span class="caret"></span>
	  		</button>
			<ul class="dropdown-menu" id="libcode">
			</ul>
		</div>
	<a class="btn btn-success" onclick="submitForm()">保存修改</a>
	<a class="btn" href="${BASE_URL}/list">重置</a>
	
	</div>
</fieldset>
<form action="${BASE_URL}/update" id="form1" method="POST" name="submitform">
<table class="table table-striped table-hover">
	<thead>
		<tr>
			<th>
			${libcode}
			</th>
			<th>验证费</th>
			<th>服务费</th>
			<th>工本费</th>
		</tr>
	</thead>
	<tbody>
		<input type="hidden" name="libcode" value="${readerFee.libcode}"/>
		<tr class="f cardfee">
			<td>办证费用</td>
			<input type="hidden" name="cardfee" id="cardfee"/>
			<td><input type="checkbox" class="check" <c:if test="${fn:substring(readerFee.cardfee, 0, 1) eq '1'}">checked</c:if>></td>
			<td><input type="checkbox" class="service" <c:if test="${fn:substring(readerFee.cardfee, 1, 2) eq '1'}">checked</c:if>></td>
			<td><input type="checkbox" class="id" <c:if test="${fn:substring(readerFee.cardfee, 2, 3) eq '1'}">checked</c:if>></td>
		</tr>
		<tr class="f renewfee">
			<td>恢复费用</td>
			<input type="hidden" name="renewfee" id="renewfee"/>
			<td><input type="checkbox" class="check" <c:if test="${fn:substring(readerFee.renewfee, 0, 1) eq '1'}">checked</c:if>/></td>
			<td><input type="checkbox" class="service" <c:if test="${fn:substring(readerFee.renewfee, 1, 2) eq '1'}">checked</c:if>/></td>
			<td><input type="checkbox" class="id" <c:if test="${fn:substring(readerFee.renewfee, 2, 3) eq '1'}">checked</c:if>/></td>
		</tr>
		<tr class="f checkfee">
			<td>验证费用</td>
			<input type="hidden" name="checkfee" id="checkfee"/>
			<td><input type="checkbox" class="check" <c:if test="${fn:substring(readerFee.checkfee, 0, 1) eq '1'}">checked</c:if>/></td>
			<td><input type="checkbox" class="service" <c:if test="${fn:substring(readerFee.checkfee, 1, 2) eq '1'}">checked</c:if>/></td>
			<td><input type="checkbox" class="id" <c:if test="${fn:substring(readerFee.checkfee, 2, 3) eq '1'}">checked</c:if>/></td>
		</tr>
		<tr class="f lossfee">
			<td>挂失费用</td>
			<input type="hidden" name="lossfee" id="lossfee"/>
			<td><input type="checkbox" class="check" <c:if test="${fn:substring(readerFee.lossfee, 0, 1) eq '1'}">checked</c:if>/></td>
			<td><input type="checkbox" class="service" <c:if test="${fn:substring(readerFee.lossfee, 1, 2) eq '1'}">checked</c:if>/></td>
			<td><input type="checkbox" class="id" <c:if test="${fn:substring(readerFee.lossfee, 2, 3) eq '1'}">checked</c:if>/></td>
		</tr>
		<tr class="f stopfee">
			<td>暂停费用</td>
			<input type="hidden" name="stopfee" id="stopfee"/>
			<td><input type="checkbox" class="check" <c:if test="${fn:substring(readerFee.stopfee, 0, 1) eq '1'}">checked</c:if> /></td>
			<td><input type="checkbox" class="service" <c:if test="${fn:substring(readerFee.stopfee, 1, 2) eq '1'}">checked</c:if> /></td>
			<td><input type="checkbox" class="id" <c:if test="${fn:substring(readerFee.stopfee, 2, 3) eq '1'}">checked</c:if> /></td>
		</tr>
		<tr class="f logoutfee">
			<td>注销费用</td>
			<input type="hidden" name="logoutfee" id="logoutfee"/>
			<td><input type="checkbox" class="check" <c:if test="${fn:substring(readerFee.logoutfee, 0, 1) eq '1'}">checked</c:if> /></td>
			<td><input type="checkbox" class="service" <c:if test="${fn:substring(readerFee.logoutfee, 1, 2) eq '1'}">checked</c:if> /></td>
			<td><input type="checkbox" class="id" <c:if test="${fn:substring(readerFee.logoutfee, 2, 3) eq '1'}">checked</c:if> /></td>
		</tr>
		<tr class="f quitfee">
			<td>退证费用</td>
			<input type="hidden" name="quitfee" id="quitfee"/>
			<td><input type="checkbox" class="check" <c:if test="${fn:substring(readerFee.quitfee, 0, 1) eq '1'}">checked</c:if> /></td>
			<td><input type="checkbox" class="service" <c:if test="${fn:substring(readerFee.quitfee, 1, 2) eq '1'}">checked</c:if> /></td>
			<td><input type="checkbox" class="id" <c:if test="${fn:substring(readerFee.quitfee, 2, 3) eq '1'}">checked</c:if> /></td>
		</tr>
		<tr class="f repairfee">
			<td>补办费用</td>
			<input type="hidden" name="repairfee" id="repairfee"/>
			<td><input type="checkbox" class="check" <c:if test="${fn:substring(readerFee.repairfee, 0, 1) eq '1'}">checked</c:if> /></td>
			<td><input type="checkbox" class="service" <c:if test="${fn:substring(readerFee.repairfee, 1, 2) eq '1'}">checked</c:if> /></td>
			<td><input type="checkbox" class="id" <c:if test="${fn:substring(readerFee.repairfee, 2, 3) eq '1'}">checked</c:if> /></td>
		</tr>
		<tr class="f deferfee">
			<td>延期费用</td>
			<input type="hidden" name="deferfee" id="deferfee"/>
			<td><input type="checkbox" class="check" <c:if test="${fn:substring(readerFee.deferfee, 0, 1) eq '1'}">checked</c:if> /></td>
			<td><input type="checkbox" class="service" <c:if test="${fn:substring(readerFee.deferfee, 1, 2) eq '1'}">checked</c:if>  /></td>
			<td><input type="checkbox" class="id" <c:if test="${fn:substring(readerFee.deferfee, 2, 3) eq '1'}">checked</c:if> /></td>
		</tr>
		<tr class="f changefee">
			<td>换证费用</td>
			<input type="hidden" name="changefee" id="changefee"/>
			<td><input type="checkbox" class="check" <c:if test="${fn:substring(readerFee.changefee, 0, 1) eq '1'}">checked</c:if> /></td>
			<td><input type="checkbox" class="service" <c:if test="${fn:substring(readerFee.changefee, 1, 2) eq '1'}">checked</c:if>  /></td>
			<td><input type="checkbox" class="id" <c:if test="${fn:substring(readerFee.changefee, 2, 3) eq '1'}">checked</c:if> /></td>
		</tr>
	</tbody>
</table>
</form>

<c:set var="pager_base_url">${BASE_URL}/list?</c:set>


		    </div>
		</div>
	</div>
</section>