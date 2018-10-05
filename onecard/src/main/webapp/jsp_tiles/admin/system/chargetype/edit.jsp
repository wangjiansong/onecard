<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/jsp_tiles/include/taglibs.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<c:set var="BASE_URL">
	<c:url value="/admin/sys/chargetype" />
</c:set>
<section id="tabs">
	<div>
		<ul id="myTab" class="nav nav-tabs">
			<li class="active"><a href="#" data-toggle="tab">充值类型管理 >> 修改</a></li>
		</ul>
		<div id="myTabContent" class="tab-content">
		    <div class="tab-pane active">

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
			<label class="control-label" for="input_feeType">
				类型代码
			</label>
			<div class="controls">
				<input type="text" name="feeType" class="input-medium" id="input_feeType" value="${finType.feeType}" />
			</div>
		</div>
		<div class="control-group">
			<label class="control-label" for="input_describe">
				费用类型
			</label>
			<div class="controls">
				<input type="text" name="describe" class="input-medium" id="input_describe" value="${finType.describe}" />
			</div>
		</div>
		<div class="control-group">
			<label class="control-label" for="input_appCode">
				所属系统
			</label>
			<div class="controls">
				<select id="input_appCode" name="appCode" class="select-medium">
					<option value="${chargeType.appCode}">${chargeType.appName}</option>
					<c:forEach items="${chargeAppMap}" var="entry">
						<c:if test="${chargeType.appCode ne entry.key}">
							<option value="${entry.key}">${entry.value}</option>
						</c:if>
					</c:forEach>
				</select>
			</div>
		</div>
	</div>
</form>
		    </div>
		</div>
	</div>
</section>