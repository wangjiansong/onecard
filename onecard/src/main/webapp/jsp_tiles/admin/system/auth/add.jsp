<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/jsp_tiles/include/taglibs.jsp" %>
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
	var bindingUserId = $("#bindingUserId").val();
	if(!bindingUserId) {
		showResult("请选择绑定操作员！");
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

$(function(){  
            //全选/取消全选  
         $("input[id='selectAll']").live("click",function(){   
             if($(this).is(':checked')){   
                $(this).parent().parent().find(".select input").each(function(i){  
                        $(this).attr("checked",true);  
                 });   
             }else{  
                 $(this).parent().parent().find(".select input").each(function(i){  
                        $(this).attr("checked",false);  
                 });   
             }  
        });  
      
});  

</script>
<section id="tabs">
	<div>
		<ul id="myTab" class="nav nav-tabs">
			<li class="active"><a href="#" data-toggle="tab">访问控制 >> 新增</a></li>
		</ul>
		<div id="myTabContent" class="tab-content">
		    <div class="tab-pane active">

<div id="tip" class="A_MESSAGEBOX_LOADING alert alert-success" style="display: none;">
</div>
<form action="save" method="POST" id="form1" name="submitform" class="form-horizontal">
	<fieldset>
		<div class="well form-actions_1">
			<div class="btn-group">
			<a href="#" class="btn btn-success" onclick="checkForm()">保存</a>
			<button type="reset" class="btn btn-info">重置</button>
			<a class="btn" href="${BASE_URL}/list">取消</a>
			</div>
		</div>
	</fieldset>		
	<div class="container-fluid">
		<div class="control-group">
			<label class="control-label" for="appCode">
				应用代码
			</label>
			<div class="controls">
				<input type="text" name="appCode" class="input-medium" id="appCode"  />
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
		<div class="control-group" id="for_ipAddress">
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
				<input type="text" name="staticCode" class="input-medium" id="istaticCode"  />
				<font class="label-required" title="必填"></font>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label" for="authorizeApi">
				授权API接口
			</label>
			<div class="controls">
				<select id="authorizeApi" name="authorizeApi" class="select-medium">
					<option value="charge">充值接口</option>
					<option value="deduction">扣费接口</option>
					<option value="querycharge">查询充值接口</option>
					<option value="query">查询余额接口</option>
					<option value="addreader">新增读者接口</option>
				</select>
				<font class="label-required" title="必填"></font>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label" for="encodeRule">
				加密规则
			</label>
			<div class="controls">
				<input type="text" name="encodeRule" class="input-medium" id="encodeRule" value="{appcode}{today:yyyyMMdd}{static}" />
			</div>
		</div>
		<div class="control-group">
			<label class="control-label" for="bindingUserId">
				绑定操作员帐号
			</label>
			<div class="controls">
				 <select id="bindingUserId" name="bindingUserId" class="select-medium" multiple="multiple">
					<c:forEach items="${libReader}" var="entry">
						<option value="${entry.rdId}">${entry.rdName} | ${entry.rdId}</option>
					</c:forEach>
				</select> 		
				<font class="label-required" title="必填"></font>
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
		
	</div>
</form>
		    </div>
		</div>
	</div>
</section>