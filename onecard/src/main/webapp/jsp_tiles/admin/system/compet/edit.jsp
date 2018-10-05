<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/jsp_tiles/include/taglibs.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<c:set var="BASE_URL">
	<c:url value="/admin/sys/compet" />
</c:set>


<form action="${BASE_URL}/update" id="form1" method="POST" >
	<input type="hidden" name="competId" value="${compets.competId}" />
<fieldset>
	<div class="form-actions">
	
		<a class="btn" href="${BASE_URL}/list"><<</a>
		<button type="submit" class="btn btn-primary">保存</button>
		<button type="reset" class="btn">重置</button>
		
		<div class="form-actions"></div>
		<div class="container-fluid">
			<div class="control-group">
				<label class="control-label" for="input_competName">
					权限名称
				</label>
				<div class="controls">
					<input type="text" name="competName" class="input-medium" id="input_competName" value="${compets.competName}" />
				</div>
			</div>
			<div class="control-group">
				<label class="control-label" for="input_describe">
					描述
				</label>
				<div class="controls">
					<input type="text" name="describe" class="input-medium" id="input_describe" value="${compets.describe}" />
				</div>
			</div>
		</div>
	</div>
</fieldset>
</form>