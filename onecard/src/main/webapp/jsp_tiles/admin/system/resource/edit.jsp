<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/jsp_tiles/include/taglibs.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<c:set var="BASE_URL">
	<c:url value="/admin/sys/res" />
</c:set>


<form action="${BASE_URL}/update" id="form1" method="POST" >
<fieldset>
	<div class="well form-actions_1">
		<a class="btn" href="${BASE_URL}/list"><<</a>
		<button type="submit" class="btn btn-primary">保存修改</button>
		<button type="reset" class="btn">重置</button>
		
		<div class="form-actions"></div>
		<div class="container-fluid">
			<div class="control-group">
				<label class="control-label" for="input_resourceId">
					资源代码
					<input type="hidden" name="resourceId" value="${resources.resourceId}"/>
				</label>
				<div class="controls">
					<input type="text" name="resourceId" class="input-medium disabled" id="input_resourceId" value="${resources.resourceId}" disabled/>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label" for="input_resourceName">
					资源名称
				</label>
				<div class="controls">
					<input type="text" name="resourceName" class="input-medium" id="input_resourceName" value="${resources.resourceName}" />
				</div>
			</div>
			<div class="control-group">
				<label class="control-label" for="input_subsys">
					所属父菜单
				</label>
				<div class="controls">
					<input type="text" name="subsys" class="input-medium" id="input_subsys" value="${resources.subsys}" />
				</div>
			</div>
			<div class="control-group">
				<label class="control-label" for="input_isMenu">
					是否菜单
				</label>
				<div class="controls">
					<input type="text" name="isMenu" class="input-medium" id="input_isMenu" value="${resources.isMenu}" />
				</div>
			</div>
			<div class="control-group">
				<label class="control-label" for="input_resourceUrl">
					URL
				</label>
				<div class="controls">
					<input type="text" name="resourceUrl" class="input-medium" id="input_resourceUrl" value="${resources.resourceUrl}" />
				</div>
			</div>
			<div class="control-group">
				<label class="control-label" for="input_describe">
					描述
				</label>
				<div class="controls">
					<input type="text" name="describe" class="input-medium" id="input_describe" value="${resources.describe}" />
				</div>
			</div>
		</div>
	</div>
</fieldset>
</form>