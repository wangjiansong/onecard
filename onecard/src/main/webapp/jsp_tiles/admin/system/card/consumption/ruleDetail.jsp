<%@page language="java" contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="/jsp_tiles/include/taglibs.jsp"%>
<link type="text/css" rel="stylesheet" href="<c:url value='/media/bootstrap/css/bootstrap.min.css' />" />
<script type="text/javascript" src="<c:url value='/media/js/jquery/jquery-1.8.3.min.js' />"></script>
<div class="navbar">
	<div class="navbar-inner">
		<a class="brand" href="javascript:void(0);">刷卡消费规则详情</a>
	</div>
</div>
<div>
	<div class="span6">
		<p class="text-info">标题：${rule.RULETITLE}</p>
	</div>
	<div class="span6">
		<p class="text-info">补助餐数：${rule.SUBSIDIZETIMES}</p>
	</div>
	<c:forEach items="${details}" var="v" varStatus="vs">
		<div class="span6">
			<p class="text-info">
				消费时段${vs.index+1}：${v.STARTTIME}~${v.ENDTIME}
				&nbsp;&nbsp;&nbsp;
				折扣价：${v.SALEPRICE}元
				&nbsp;&nbsp;&nbsp;
				成本价：${v.COSTPRICE}元
			</p>
		</div>
	</c:forEach>
	<div class="span6">
		<p class="text-info">创建时间：${rule.CREATETIME}</p>
	</div>
	<div class="span6">
		<p class="text-info">备注：${rule.REMARK}</p>
	</div>
	<div class="span6" style="text-align: center;">
		<button type="button" class="btn btn-primary" onclick="javascript:parent.$.fn.colorbox.close();return false;">返回</button>
	</div>
</div>
	