<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/jsp_tiles/include/taglibs.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<script type="text/javascript">
	function changeAvatar(rdId) {
		window.location.href = "<c:url value='/admin/reader/avatar/"+rdId+"' />";
	}

	function resetPasswd(rdId) {
		window.location.href = "<c:url value='/admin/reader/resetPasswd/"+rdId+"' />";
	}
	
	function editReader(rdId) {
		window.location.href = "<c:url value='/admin/reader/add/"+rdId+"' />";
	}
	
	function getBack() {
		window.location.href = "<c:url value='/admin/reader/index' />";
	}
</script>
<div class="page-header">
	<h3>用户详细资料</h3>
</div>
<div class="hero-unit" style="margin: 0;padding: 0;">
	<div style="float: right;width: 160px;height: 699px;">
		<div class="thumbnail">
				<img style="width: 130px;height: 130px;" class="img-polaroid" src="<c:url value='/admin/reader/showAvatar/${rdId}' />" />
			<div class="caption" align="center">
				<a href="javascript:void(0);" onclick="javascript:changeAvatar('${reader.rdId}');" class="btn btn-success">更换头像</a>
			</div>
		</div>
		<div align="center" style="padding-top: 10px;">
			<input type="button" class="btn btn-success" value="修改信息" onclick="javascript:editReader('${reader.rdId}');" />
		</div>
		<div align="center" style="padding-top: 10px;">
			<input type="button" class="btn btn-success" value="修改密码" onclick="javascript:resetPasswd('${reader.rdId}');" />
		</div>
		<div align="center" style="padding-top: 10px;">
			<input type="button" class="btn btn-inverse" value="&nbsp;返&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;回&nbsp;" onclick="javascript:getBack();" />
		</div>
	</div>
	<div>
		<div class="span5">
			证号：
			<c:choose>  
			    <c:when test="${fn:length(reader.rdId) == 18}">  
			    	<c:out value="${fn:substring(reader.rdId, 0, 4)}" />**********<c:out value="${fn:substring(reader.rdId, 16, 18)}" />
			    </c:when>
			    <c:when test="${fn:length(reader.rdId) == 15}">  
			    	<c:out value="${fn:substring(reader.rdId, 0, 4)}" />**********<c:out value="${fn:substring(reader.rdId, 13, 15)}" />
			    </c:when>
			   <c:otherwise>  
			      	<c:out value="${reader.rdId}" />
			    </c:otherwise>  
			</c:choose>
		</div>
		<div class="span5">
			证状态：${reader.rdStateStr}
		</div>
	</div>
	<div>
		<div class="span5">
			姓名：${reader.rdName}
		</div>
		<div class="span5">
			性别：${reader.rdSex eq 1 ? "男" : "女"}
		</div>
	</div>
	<div>
		<div class="span5">
			读者类型：${reader.rdType}
		</div>
		<div class="span5">
			开户馆：${reader.rdLib}
		</div>
	</div>
	<div>
		<div class="span5">
			是否馆际读者：${reader.rdGlobal eq 1 ? "是" : "否"}
		</div>
		<div class="span5">
			馆际流通类型：${reader.rdLibType}
		</div>
	</div>
	<div>
		<div class="span5">
			办证时间：${reader.rdInTimeStr}
		</div>
		<div class="span5">
			身份证号：
			<c:choose>  
			    <c:when test="${fn:length(reader.rdCertify) == 18}">  
			    	<c:out value="${fn:substring(reader.rdCertify, 0, 4)}" />**********<c:out value="${fn:substring(reader.rdCertify, 16, 18)}" />
			    </c:when>
			   <c:otherwise>  
			      	<c:out value="${reader.rdCertify}" />
			    </c:otherwise>  
			</c:choose>
		</div>
	</div>
	<div>
		<div class="span5">
			启用日期：${reader.rdStartDateStr}
		</div>
		<div class="span5">
			终止日期：${reader.rdEndDateStr}
		</div>
	</div>
	<div>
		<div class="span5">
			出生日期：${reader.rdBornDateStr}
		</div>
		<div class="span5">
			民族：${reader.rdNation}
		</div>
	</div>
	<div>
		<div class="span5">
			手机：${reader.rdLoginId}
		</div>
		<div class="span5">
			Email：${reader.rdEmail}
		</div>
	</div>
	<div>
		<div class="span5">
			籍贯：${reader.rdNative}
		</div>
		<div class="span5">
			地址：${reader.rdAddress}
		</div>
	</div>
	<div>
		<div class="span5">
			单位：${reader.rdUnit}
		</div>
		<div class="span5">
			邮编：${reader.rdPostCode}
		</div>
	</div>
	<div>
		<div class="span5">
			职务：${reader.rdSort3}
		</div>
		<div class="span5">
			电话：${reader.rdPhone}
		</div>
	</div>
	<div>
		<div class="span5">
			职业：${reader.rdSort2}
		</div>
		<div class="span5">
			专业：${reader.rdSort1}
		</div>
	</div>
	<div>
		<div class="span5">
			职称：${reader.rdSort4}
		</div>
		<div class="span5">
			文化：${reader.rdSort5}
		</div>
	</div>
	<div>
		<div class="span5">
			兴趣：${reader.rdInterest}
		</div>
	</div>
	<div>
		<div class="span5">
			所属分组：${groupBelong}
		</div>
	</div>
	<div>
		<div class="span5">
			同步状态：<c:if test="${reader.synStatus==1}">成功</c:if>
			<c:if test="${reader.synStatus==0}">失败</c:if>
		</div>
	</div>
	<div>
		<div class="span10">
			备注：${reader.rdRemark}
		</div>
	</div>
</div>
