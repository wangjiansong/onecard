<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/jsp_tiles/include/taglibs.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<script type="text/javascript">
	function getBack() {
		/*window.location.href = "<c:url value='/admin/netreader/approve' />";*/
		window.history.back(-1); 
	}
</script>
<div class="page-header">
	<h3>
		网络读者详情
		<input type="button" class="btn btn-inverse" style="margin-left: 70px;" value="&nbsp;返&nbsp;&nbsp;&nbsp;回&nbsp;" onclick="javascript:getBack();" />
	</h3>
</div>
<div class="hero-unit" style="margin: 0;padding: 0;">
	<div>
		<div class="span5">
			证号：${netreader.readerId}
		</div>
		<div class="span6">
			姓名：${netreader.readerName}
		</div>
	</div>
	<div>
		<div class="span5">
			审批状态：${netreader.checkStateStr}
		</div>
		<div class="span6">
			性别：${netreader.readerGender eq 1 ? "男" : "女"}
		</div>
	</div>
	<div>
		<div class="span5">
			证状态：${netreader.readerCardStateStr}
		</div>
		<div class="span6">
			身份证号：${netreader.readerCertify}
		</div>
	</div>
	<div>
		<div class="span5">
			读者类型：${netreader.readerType}
		</div>
		<div class="span6">
			籍贯：${netreader.readerNative}
		</div>
	</div>
	<div>
		<div class="span5">
			开户馆：${netreader.readerLib}
		</div>
		<div class="span6">
			地址：${netreader.readerAddress}
		</div>
	</div>
	<div>
		<div class="span5">
			启用日期：${netreader.readerStartDateStr}
		</div>
		<div class="span6">
			专业：${netreader.readerSort1}
		</div>
	</div>
	<div>
		<div class="span5">
			终止日期：${netreader.readerEndDateStr}
		</div>
		<div class="span6">
			职业：${netreader.readerSort2}
		</div>
	</div>
	<div>
		<div class="span5">
			办证时间：${netreader.readerHandleDateStr}
		</div>
		<div class="span6">
			职务：${netreader.readerSort3}
		</div>
	</div>
	<div>
		<div class="span5">
			手机：${netreader.readerMobile}
		</div>
		<div class="span6">
			职称：${netreader.readerSort4}
		</div>
	</div>
	<div>
		<div class="span5">
			Email：${netreader.readerEmail}
		</div>
		<div class="span6">
			文化：${netreader.readerSort5}
		</div>
	</div>
	<div>
		<div class="span5">
			单位：${netreader.readerUnit}
		</div>
		<div class="span6">
			备注：${netreader.remark}
		</div>
	</div>
</div>
<!-- <div> -->
<!-- 	<div class="span5" style="padding-top: 30px;padding-left: 300px;"> -->
<!-- 		<input type="button" class="btn btn-inverse" value="&nbsp;返&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;回&nbsp;" onclick="javascript:getBack();" /> -->
<!-- 	</div> -->
<!-- </div> -->
