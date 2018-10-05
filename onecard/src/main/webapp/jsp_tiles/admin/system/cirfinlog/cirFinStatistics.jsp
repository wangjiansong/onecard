<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/jsp_tiles/include/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<c:set var="BASE_URL">
	<c:url value="/admin/sys/cirfinlog" />
</c:set>
<style type="text/css">
/**loading style*/
.A_MESSAGEBOX_LOADING {
	left: 50%;
	padding: 3px 6px;
	position: fixed;
	top: 0;
	z-index: 10000;
}
</style>
<script type="text/javascript">
$(function(){
	var chkSingle = $(".selAll");
	var chkGroup = $(".f input");
	//获取所有被勾选的input
	var funTrGet = function() {
		return $(".f input:checked");
	};
	//正选反选
	chkSingle.bind("click", function() {
	    if ($(this).attr("checked")) {
	        chkGroup.attr("checked", true);	
	    } else {
	        chkGroup.attr("checked", false);
	    }
	});
	$(".form_datetime").datetimepicker({
        format: "yyyy-mm-dd",
        language: 'zh-CN',
        autoclose: true,
        startView: 'month',
        minView: 2,
        pickerPosition: "bottom-left",
        todayBtn: true
    });
	$("#startTime").val(new Date().Format("yyyy-MM-dd"));
	
	$("#endTime").val(new Date().Format("yyyy-MM-dd"));
});
String.prototype.trim = function() {
	return this.replace(/^\s+/g, "").replace(/\s+$/g, "");
};

function pressEnter(e,method){
	if(e==null){//ie6..
		e=event;
	}	
	if ((e.which == 13 || e.keyCode == 13)&&(!(e.ctrlKey || e.metaKey))
			&& !e.shiftKey && !e.altKey) {
		 method();
	}
}

function doStatistics() {
	$.post("doCirFinStatistics",$("#searchForm").serialize(),function(data){
		document.searchform.submit();
	},'text');
}
//提示信息
function createLoading(tip) {
	$("#tip").css("display", "inline").html(tip);
	$("#tip").fadeOut(3000).remove();
	return;
}
</script>
	<form name="searchform" method="POST" action="doCirFinStatistics" class="form-inline" target="_blank">
		<div class="form-actions_1">
			<p>
			<div id="tip" class="A_MESSAGEBOX_LOADING alert" style="display: none;">
				<a class="close" data-dismiss="alert">&times;</a>
			</div>
			<div class="container-fluid">
			<div class="row-fluid">
				<div class="span3">
					<div class="control-group">
						<span>操&nbsp作&nbsp馆&nbsp： </span>
						<select id="regLib" name="regLib" class="input-medium search-query-extend" multiple="multiple" style="width: 200px;height:160px">
							<option value="">--按住CTRL键多选--</option>
							<c:forEach items="${simpleLibcode}" var="entry">
								<option value="${entry.libCode}">${entry.libCode} | ${entry.simpleName}</option>
							</c:forEach>
						</select>
					</div>
				</div>
				<div class="span3">
					
					<div class="control-group">
						<span>参与系统：</span>
						<select id="feeAppCode"  name="feeAppCode" multiple="multiple" style="width: 230px;height:160px">
							<option value="">--按住CTRL键多选--</option>
							<c:forEach items="${authAppList}" var="entry">
								<option value="${entry.appCode}" selected>${entry.appCode} | ${entry.appName}</option>
							</c:forEach>
						</select>
					</div>
				</div>
				<div class="span3">
					<div class="control-group">
						<span>收款人员：</span>
						<select id="regman"  name="regman" multiple="multiple" style="width: 200px;height:160px">
							<option value="">--按住CTRL键多选--</option>
							<c:forEach items="${libUserList}" var="r">
								<option value="${r.rdId}" selected>${r.rdId} | ${r.rdName}</option>
							</c:forEach>
						</select>
					</div>
				</div>
				<div class="span3">
					<div class="control-group">
						<span>收费类型：</span>
						<select id="feetype"  name="feetype" multiple="multiple" style="width: 200px;height:160px">
							<option value="">--按住CTRL键多选--</option>
							<c:forEach items="${finTypeList}" var="entry">
								<option value="${entry.feeType}" selected>${entry.describe} | ${entry.appName}</option>
							</c:forEach>
						</select>
					</div>
				</div>
				<div class="span4">
					<div class="control-group">
						<span>读者姓名：</span>
						<div class="input-append">
						<input type="text" id="rdname" name="rdname" class="input-medium search-query-extend" />
						</div>
					</div>
					<div class="control-group">
						<span>起始证号：</span>
						<div class="input-append">
						<input type="text" id="startRdid" name="startRdid" class="input-medium search-query-extend" />
						</div>
					</div>
					<div class="control-group">
						<span>终止证号：</span>
						<div class="input-append">
						<input type="text" id="endRdid" name="endRdid" class="input-medium search-query-extend" />
						</div>
					</div>
					<div class="control-group">
						<span>起始日期：</span>
						<div class="input-append date form_datetime ">
						<input type="text" id="startTime" name="startTime" class="input-medium search-query-extend" />
						<span class="add-on"><i class="icon-calendar"></i></span>
						</div>
					</div>
					<div class="control-group">
						<span>终止日期：</span>
						<div class="input-append date form_datetime ">
						<input type="text" id="endTime" name="endTime" class="input-medium search-query-extend" />
						<span class="add-on"><i class="icon-calendar"></i></span>
						</div>
					</div>
					<div class="control-group">
					<span>（*起始证号和终止证号位数必须限定一致，否则统计结果可能不准确，如起始证号：2015001，终止证号2015100，同为7位）</span>
					</div>
				</div>
				<div class="span4"></div>
			</div>
			<div class="row-fluid">
				<div class="span4" data-toggle="buttons-radio">
					<a id="regmanbutton" class="btn btn-info" onclick="javascript:doStatistics();" href="javascript:void(0);" >执行统计</a>
				</div>
				<div class="span4"></div>
				<div class="span4"></div>
			</div>
			</div>
			</p>
		</div>
	
	</form>
<c:set var="pager_base_url">${BASE_URL}/list?</c:set>
