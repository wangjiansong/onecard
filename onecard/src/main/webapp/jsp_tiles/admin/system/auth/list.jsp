<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/jsp_tiles/include/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<c:set var="BASE_URL">
	<c:url value="/admin/sys/auth" />
</c:set>

<script type="text/javascript">
$(function(){
	$(".form_datetime").datetimepicker({
        format: "yyyy-mm-dd",
        language: 'zh-CN',
        autoclose: true,
        startView: 'month',
        minView: 2
    });
});
function Add(){
	document.location.href="${BASE_URL}/add";
}
function Edit(id){
	$.ajax({
		type: "GET",
		url: "${BASE_URL}/edit/"+id,
		dataType: "json",
		success: function(jsonData){
			$("#id").val(jsonData.id);
			$("#appCode").val(jsonData.appCode);
			$("#input_appCode").val(jsonData.appCode);
			$("#appName").val(jsonData.appName);
			$("#ipAddress").val(jsonData.ipAddress);
			$("#staticCode").val(jsonData.staticCode);
			$("#authorizeApi").val(jsonData.authorizeApi);
			$("#encodeRule").val(jsonData.encodeRule);
			$("#input_bindingUserId").val(jsonData.bindingUserId);
			$("#bindingUserId").val(jsonData.bindingUserId);
			/* $("#bindingUserId").val(jsonData.bindingUserName +" | " +jsonData.bindingUserId); */
			$("#endDate").val(jsonData.endDate);
		},
		cache: true
	});
}
function Delete(id){
	if (!confirm(" 你确定要删除此条记录？")) return;
	document.location.href = "${BASE_URL}/delete/" +id;
}
function checkForm() {
	var appCode = $("#appCode").val();
	if(appCode == "") {
		showResult("请填写应用代码！");
		return ;
	}
	var appName = $("#appName").val();
	if(appName == "") {
		showResult("请填写应用名称！");
		return ;
	}
	var ipAddress = $("#ipAddress").val();
	if(ipAddress == "") {
		showResult("请填写授权IP地址！");
		return ;
	}
	var staticCode = $("#staticCode").val();
	if(staticCode == "") {
		showResult("请填写静态密钥！");
		return ;
	}
	var authorizeApi = $("#authorizeApi").val();
	if(authorizeApi == "") {
		showResult("请填写授权API接口！");
		return ;
	}
	var endDate = $("#endDate").val();
	if(endDate == "") {
		showResult("请填写截止日期！");
		return ;
	}
	document.submitform.submit();
}
//提示信息
function showResult(tip) {
	$("#tip").css("display", "block").html(tip);
	setTimeout(function(){
		$("#tip").fadeOut("slow");
	},5000);
	$("#tip").attr("class", "A_MESSAGEBOX_LOADING alert alert-success");
	return;
}

</script>
<div id="tip" class="A_MESSAGEBOX_LOADING alert alert-success" style="display: none;">
</div>
<section id="tabs">
	<div>
		<ul id="myTab" class="nav nav-tabs">
			<li class="active"><a href="#" data-toggle="tab">访问控制</a></li>
		</ul>
		<div id="myTabContent" class="tab-content">
		    <div class="tab-pane active">
		    

<fieldset>
	<form action="list" id="form1" method="POST" class="form-inline">	
	<div class="well form-actions_1">
			<span>应用代码：</span>
			<input type="text" name="appCode" value="${appCode}" class="input-medium search-query-extend" />
			<span>应用名称：</span>
			<input type="text" name="appName" value="${appName}" class="input-medium search-query-extend" />
			<span>绑定的操作员ID：</span>
			<div class="input-append">
			<input type="text" name="bindingUserId" value="${bindingUserId}" class="input-medium search-query-extend" />
			<button class="btn btn-info" type="submit">查询</button>
			<button type="reset" class="btn">重置</button>
			</div>
	</div>
	</form>
</fieldset>
<button class="btn btn-success" onclick="javascript:Add();">新增</button>
</p>
<table class="table table-bordered table-hover table-condensed table-striped">
	<thead>
		<tr>
			<th>应用代码</th>
			<th>应用名称</th>
			<th>授权ip地址</th>
			<th>静态密钥</th>
			<th>授权API接口</th>
			<th>加密规则</th>
			<th>到期时间</th>
			<th>绑定操作员</th>
			<th>操作</th>
		</tr>
	</thead>
	<tbody>
		<c:forEach items="${pageList}" var="p">
 			<tr class="f">
 				<td><c:out value="${p.appCode}" /></td>
 				<td><c:out value="${p.appName}" /></td>
 				<td><c:out value="${p.ipAddress}" /></td>
 				<td><c:out value="${p.staticCode}" /></td>
 				<td><c:out value="${p.authorizeApi}" /></td>
 				<td><c:out value="${p.encodeRule}" /></td>
 				<td><c:out value="${p.endDate}" /></td>
 				<td><c:out value="${p.bindingUserId}" /></td>
 				<td>	
 					<div class="btn-group">
  				<a data-toggle="modal" href="#myModal" id="editButton" class="btn btn-success btn-small" onclick="Edit('${p.id}')">查看</a>
				<input id="deleteButton" type="button" class="btn btn-small" value="删除" onclick="Delete('${p.id}')"/>
				</div>
			</td>
 			</tr>
    	</c:forEach>
	</tbody>
</table>
<div class="modal hide fade in" id="myModal" style="display: none;">
  	<div class="modal-header">
    	<a class="close" data-dismiss="modal">×</a>
    	<h3>编辑</h3>
  	</div>
  	<div class="modal-body">
	  	<form action="${BASE_URL}/update" name="submitform" method="POST" class="form-horizontal">
	  	<input type="hidden" name="id" value="" id="id"/>
		<div class="control-group">
			<label class="control-label" for="appCode">
				应用代码
			</label>
			<div class="controls">
				<input type="hidden" name="appCode" class="input-medium" id="appCode" />
				<input type="text" class="input-medium" id="input_appCode" disabled="true" />
				<font class="label-required" title="必填"></font>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label" for="appName">
				应用名称
			</label>
			<div class="controls">
				<input type="text" name="appName" class="input-medium" id="appName"  />
				<font class="label-required" title="必填"></font>
			</div>
		</div>
		<div class="control-group" for="input_ipAddress">
			<label class="control-label" for="ipAddress">
				授权IP
			</label>
			<div class="controls">
				<input type="text" name="ipAddress" class="input-medium" id="ipAddress"  
					placeholder="格式:127.0.0.1-127.0.0.1;" />
				<font class="label-required" title="必填"></font>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label" for="staticCode">
				静态密钥
			</label>
			<div class="controls">
				<input type="text" name="staticCode" class="input-medium" id="staticCode"  />
				<font class="label-required" title="必填"></font>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label" for="bindingUserId">
				绑定操作员帐号
			</label>
			<div class="controls">
				<input type="hidden" name="input_bindingUserId" class="input-medium" id="input_bindingUserId" />
				<input type="text" class="input-medium" id="bindingUserId" name="bindingUserId" />
				<font class="label-required" title="必填"></font>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label" for="authorizeApi">
				授权API接口
			</label>
			<div class="controls">
				<input type="text" name="authorizeApi" class="input-medium" id="authorizeApi"  />
				<font class="label-required" title="必填"></font>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label" for="encodeRule">
				加密规则
			</label>
			<div class="controls">
				<input type="text" name="encodeRule" class="input-medium" id="encodeRule"  />
			</div>
		</div>
		<div class="control-group">
			<label class="control-label" for="endDate">
				截止日期
			</label>
			<div class="controls">
				<input style="width: 150px;" class="date form_datetime input-small search-query-extend" type="text" id="endDate" name="endDate" />
				<font class="label-required" title="必填"></font>
			</div>
		</div>
	  	</form>
  	</div>
  	<div class="modal-footer">
    	<a href="#" class="btn" data-dismiss="modal">取消</a>
    	<a href="#" onclick="checkForm()" class="btn btn-primary"><i class="icon-ok icon-white"></i>保存修改</a>
  	</div>
</div>

<c:set var="pager_base_url">${BASE_URL}/list?</c:set>
<%@include file="/jsp_tiles/include/admin/paginator.jsp" %>

</div>
		</div>
	</div>
</section>

