<%@ page language="java" contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="/jsp_tiles/include/taglibs.jsp"%>
<link type="text/css" rel="stylesheet" href="<c:url value='/media/colorbox/colorbox_4.css' />" />
<script type="text/javascript" src="<c:url value='/media/colorbox/jquery.colorbox-min.js' />"></script>
<c:set var="base_url">
	<c:url value="/admin/card" />
</c:set>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<script type="text/javascript">
	$(document).ready(function(){
		if ("${obj.ruleTitle}") {
			$("input[name=ruleTitle]").val("${obj.ruleTitle}");
		}
	});
	
	function addCardRule() {
		window.location.href = "add";
	}
	
	function detail(ruleId) {
		$.colorbox({
			width: "45%",
			height: "55%",
			iframe: true,
			close: "",
			href: "rule/detail/"+ruleId,
			overlayClose: true
		});
	}
	
	function editCardRule() {
		var info = document.getElementById("info");
		var item = $("table input[type=checkbox]:checked");
		if (item.length == 1) {
			document.location.href='edit/'+item[0].value;
		} else {
			info.innerHTML = "请选中一条记录！";
			info.className = "alert alert-warning";
			return false;
		}
	}
	
	function selectThis(thisTr, isChecked) {
		if (isChecked) {
			thisTr.className = "warning";
		} else {
			thisTr.className = "";
		}
	}
	
	function selectAll(isChecked) {
		if (isChecked) {
			$("table tr").attr("class","warning");
			$("table tr td input[type=checkbox]").attr("checked",true);
		} else {
			$("table tr").attr("class","");
			$("table tr td input[type=checkbox]").attr("checked",false);
		}
	}
	
	function deleteCardRule() {
		var info = document.getElementById("info");
		var item = $("table input[type=checkbox][id!='allbox']:checked");
		var size =  item.length;
		if (size > 0) {
			if (confirm("确定删除？")) {
				var ids = "";
				for (var i = 0; i < size; i++) {
					ids += item[i].value;
					if (i < size - 1) {
						ids += ",";
					}
				}
				$.ajax({
					type : "POST",
					url : "delete",
					data : {ids:ids},
					dataType : "text",
					success : function(backData){
						if (backData == 1) {
							info.innerHTML = "删除成功！";
							info.className = "alert alert-success";
							setTimeout(function(){
								window.location.href = "rule";
							},1000);
							return false;
						} else {
							info.innerHTML = "删除失败！";
							info.className = "alert alert-error";
							commitButton.disabled = false;
							return false;
						}
					},
					error : function(){
						info.innerHTML = "获取数据失败！";
						info.className = "alert alert-error";
						commitButton.disabled = false;
						return false;
					}
				});
			}
		} else {
			info.innerHTML = "请选择要删除的记录！";
			info.className = "alert alert-warning";
			return false;
		}
	}
	
	function assign(ruleId) {
		window.document.location.href = "assign/" + ruleId;
	}
	
	function slotCard() {
		var params = {
			slotTime: "2013-03-08 12:23:47",
			rdId: "101063"
		};
		$.ajax({
			type : "POST",
			url : "slotCard",
			data : params,
			dataType : "text",
			success : function(backData){
				if (backData == 1) {
					info.innerHTML = "成功！";
					info.className = "alert alert-success";
					setTimeout(function(){
						window.location.href = "rule";
					},1000);
					return false;
				} else {
					info.innerHTML = "失败！";
					info.className = "alert alert-error";
					return false;
				}
			},
			error : function(){
				info.innerHTML = "获取数据失败！";
				info.className = "alert alert-error";
				return false;
			}
		});
	}
</script>
<div class="bs-docs-example">
	<ul class="nav nav-tabs">
		<li class="active"><a href="rule">刷卡消费规则</a></li>
		<li><a href="${base_url}/group/index">分组管理</a></li>
	</ul>
</div>
<div id="info"></div>
<div>
	<form action="rule" method="post">
		<span>标题：</span><input type="text" id="ruleTitle" name="ruleTitle" class="input-medium search-query-extend" />
		<button type="submit" class="btn btn-info">查询</button>
		<button type="button" class="btn btn-primary" onclick="javascript:addCardRule();">新增</button>
		<button type="button" class="btn btn-success" onclick="javascript:editCardRule();">修改</button>
		<button type="button" class="btn btn-danger" onclick="javascript:deleteCardRule();">删除</button>
		<!-- <button type="button" class="btn btn-warning" onclick="javascript:slotCard();">刷卡</button> -->
	</form>
</div>
<div class="bs-docs-example">
	<table class="table table-bordered table-condensed" style="table-layout: fixed;">
		<thead>
			<tr>
				<th width="2%">
   					<input type="checkbox" id="allbox" onclick="javascript:selectAll(this.checked);" />
    			</th>
				<th width="7%">标题</th>
				<th width="7%">补助餐数</th>
				<th width="7%">创建时间</th>
				<th width="7%">备注</th>
				<th width="4%">操作</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${list}" var="v" varStatus="vs">
				<tr ondblclick="javascript:detail(${v.RULEID});">
					<td style="text-align: center;">
						<input type="checkbox" value="${v.RULEID}" name="ruleIdBox" onclick="javascript: selectThis(this.parentNode.parentNode,this.checked);" />
					</td>
					<td>${v.RULETITLE}</td>
					<td>${v.SUBSIDIZETIMES}</td>
					<td>${v.CREATETIME}</td>
					<td nowrap title="${v.REMARK}" style="overflow: hidden;text-overflow: ellipsis;">${v.REMARK}</td>
					<td style="text-align: center;">
						<div class="btn-group">
							<button type="button" class="btn btn-mini btn-info" onclick="javascript:detail(${v.RULEID});">查看</button>
							<button type="button" class="btn btn-mini btn-warning" onclick="javascript:assign(${v.RULEID});">指定消费分组</button>
						</div>
					</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
</div>
<%@include file="/jsp_tiles/include/admin/pager.jsp"%>