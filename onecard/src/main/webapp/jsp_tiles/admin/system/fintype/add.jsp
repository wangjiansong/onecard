<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/jsp_tiles/include/taglibs.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<c:set var="BASE_URL">
	<c:url value="/admin/sys/fintype" />
</c:set>

<script type="text/javascript">
function checkForm() {
	
	document.submitform.submit();
}
</script>
<section id="tabs">
	<div>
		<ul id="myTab" class="nav nav-tabs">
			<li class="active"><a href="#" data-toggle="tab">费用类型管理 >> 新增</a></li>
		</ul>
		<div id="myTabContent" class="tab-content">
		    <div class="tab-pane active">
		    
<form action="save" id="form1" method="POST" name="submitform">
	<fieldset>
		<div class="well form-actions_1">
			<a href="#" class="btn btn-success" onclick="checkForm()">保存</a>
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
				<input type="text" name="feeType" class="input-medium" id="input_feeType"  />
				<font class="label-required" title=""></font>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label" for="input_describe">
				类型名称
			</label>
			<div class="controls">
				<input type="text" name="describe" class="input-medium" id="input_describe"  />
				<font class="label-required" title=""></font>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label" for="input_appCode">
				所属系统
			</label>
			<div class="controls">
				<select id="input_appCode" name="appCode" class="select-medium">
					<c:forEach items="${finAppMap}" var="entry">
						<option value="${entry.key}">${entry.key}|${entry.value}</option>
					</c:forEach>
				</select>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label" for="input_typefee">
				类型金额
			</label>
			<div class="controls">
				<input type="text" name="typefee" class="input-medium" id="input_typefee"  />
				<font class="" style="color: red;" title=""> 多个数值用英文逗号隔开(比如：1,2.0,3)</font>
			</div>
		</div>
	</div>
</form>
		    </div>
		</div>
	</div>
</section>