<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/jsp_tiles/include/taglibs.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<c:set var="BASE_URL">
	<c:url value="/admin/sys/finapp" />
</c:set>


<form action="${BASE_URL}/update" id="form1" method="POST" >
	<fieldset>
		<div class="well form-actions_1">
			<button type="submit" class="btn btn-success">保存</button>
			<button type="reset" class="btn btn-info">重置</button>
			<a class="btn" href="${BASE_URL}/list">取消</a>	
		</div>
	</fieldset>		
	<div class="container-fluid">
		<div class="control-group">
			<label class="control-label" for="input_appCode">
				应用代码
			</label>
			<div class="controls">
				<input type="text" name="appCode" class="input-medium" id="input_appCode" value="${finApp.appCode}" />
			</div>
		</div>
		<div class="control-group">
			<label class="control-label" for="input_appName">
				应用名称
			</label>
			<div class="controls">
				<input type="text" name="appName" class="input-medium" id="input_appName" value="${finApp.appName }" />
			</div>
		</div>
		<div class="control-group">
			<label class="control-label" for="input_serviceUrl">
				接口地址
			</label>
			<div class="controls">
				<input type="text" name="serviceURL" class="input-medium" id="input_serviceUrl" value=${finApp.serviceURL } />
			</div>
		</div>
		<div class="control-group">
			<label class="control-label" for="input_serviceUrl">
				参数
			</label>
			<div class="controls">
			${finAPP.paramInfo }
				<input type="text" name="paramInfo" class="input-medium" id="input_paramInfo" value="${finApp.paramInfo }" />
				<font class="" title="" style="color:red;">格式：参数名={xxx},参数名={xxx};例：rdid={rdid}</font>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label" for="input_describe">
				描述
			</label>
			<div class="controls">
				<textarea rows="5" id="describe" name="describe" style="width: 300px;" value="${finApp.describe }"></textarea>
			</div>
		</div>
		
	</div>
</form>