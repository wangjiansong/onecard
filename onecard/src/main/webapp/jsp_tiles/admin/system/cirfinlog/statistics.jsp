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
        minView: 2
    });
});
String.prototype.trim = function() {
	return this.replace(/^\s+/g, "").replace(/\s+$/g, "");
};

Date.prototype.format = function(format) {
    var o = {
    	"M+" : this.getMonth()+1, //month
	    "d+" : this.getDate(),    //day
	    "h+" : this.getHours(),   //hour
	    "m+" : this.getMinutes(), //minute
	    "s+" : this.getSeconds(), //second
	    "q+" : Math.floor((this.getMonth()+3)/3),  //quarter
	    "S" : this.getMilliseconds() //millisecond
    };
    if(/(y+)/.test(format)) format=format.replace(RegExp.$1,
    (this.getFullYear()+"").substr(4 - RegExp.$1.length));
    for(var k in o)if(new RegExp("("+ k +")").test(format))
    format = format.replace(RegExp.$1,
    RegExp.$1.length==1 ? o[k] :
    ("00"+ o[k]).substr((""+ o[k]).length));
    return format;
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
function checkForm(groupby) {
	$("#regmanbutton").attr("disabled","true");
	$("#orglibbutton").attr("disabled","true");
	$("#reglibbutton").attr("disabled","true");
	$("#rdtypebutton").attr("disabled","true");
	document.getElementById("groupby").value=groupby;
    
	var vals ;
    $("input[name='checkbox']:checked").each(function () {
         vals=vals+this.value+",";
    });
    vals = vals.replace("undefined","");
    vals = vals.substring(0,vals.lastIndexOf(","));
	document.getElementById("feetype").value =vals;
	document.searchform.submit();
}
//提示信息
function createLoading(tip) {
	$("#tip").css("display", "inline").html(tip);
	$("#tip").fadeOut(3000).remove();
	return;
}
</script>
	<form name="searchform" method="POST" action="dostatistics" class="form-inline">
		<input type="hidden" id="feetype" name="feetype" class="input-medium search-query-extend"/>
		<div class="well form-actions_1">
			<p>
			<div id="tip" class="A_MESSAGEBOX_LOADING alert" style="display: none;">
				<a class="close" data-dismiss="alert">&times;</a>
			</div>
			<div class="container-fluid">
			<div class="row-fluid">
				<div class="span4"></div>
				<div class="span4">
					<div class="control-group">
						<span>操&nbsp;作&nbsp;馆：&nbsp;</span>
						<select id="regLib" name="regLib" class="input-medium search-query-extend">
							<option value="">请选择</option>
							<c:forEach items="${simpleLibcode}" var="entry">
								<option value="${entry.libCode}">${entry.libCode} | ${entry.simpleName}</option>
							</c:forEach>
						</select>
					</div>
					<div class="control-group">
						<span>收款人员：</span>
<!-- 						<input type="text" id="regman" name="regman" class="input-medium search-query-extend" /> -->
						<select id="regman"  name="regman" class="input-medium search-query-extend">
							<option value="">请选择</option>
							<c:forEach items="${readerList}" var="r">
								<option value="${r.rdId}">${r.rdName}</option>
							</c:forEach>
						</select>
					</div>
					<div class="control-group">
						<span>所属分组：</span>
						<select id="groupID" name="groupID" class="input-medium search-query-extend">
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
						<span>起始日期：</span>
						<div class="input-append">
						<input type="text" id="startTime" name="startTime" class="date form_datetime input-small search-query-extend" />
						<span class="add-on"><i class="icon-calendar"></i></span>
						</div>
					</div>
					<div class="control-group">
						<span>终止日期：</span>
						<div class="input-append">
						<input type="text" id="endTime" name="endTime" class="date form_datetime input-small search-query-extend" />
						<span class="add-on"><i class="icon-calendar"></i></span>
						</div>
					</div>
				</div>
				<div class="span4"></div>
			</div>
			<div class="row-fluid">
				<div class="span3"></div>
				<div class="btn-group span5" data-toggle="buttons-radio">
					<button id="regmanbutton" class="btn btn-info" onclick="javascript:checkForm('regman');">按操作员分类统计</button>
					<button id="reglibbutton" class="btn btn-info" onclick="javascript:checkForm('regLib');">按操作馆分类统计</button>
					<button id="rdtypebutton" class="btn btn-info" onclick="javascript:checkForm('rdtype');">按读者类型分类统计</button>
				</div>
				<div class="span4"></div>
			</div>
			<input type="hidden" id="groupby" name="groupby" />
			</div>
			</p>
		</div>
		<div class="container-fluid">
		<div class="row-fluid">
			<div class="span4"></div>
			<div class="span4">
				<table class="table table-bordered table-hover table-condensed table-striped">
					<thead>
						<tr>
							<th><input type="checkbox" class="selAll" checked id="feeType"/></th>
							<th>选择财经类型</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${finTypeList}" var="entry">
				 			<tr class="f">
				 				<td><input name="checkbox" type="checkbox" id="${entry.feeType}" value="${entry.feeType}" checked /></td>
				 				<td><c:out value="${entry.describe}|${entry.appName}" /></td>
				 			</tr>
				    	</c:forEach>
					</tbody>
				</table>
			</div>
			<div class="span4"></div>
		</div>
		</div>
	
	</form>
<c:set var="pager_base_url">${BASE_URL}/list?</c:set>
