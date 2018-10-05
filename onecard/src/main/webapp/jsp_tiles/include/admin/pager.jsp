<%-- <%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%> --%>
<%-- <%@include file="/jsp_tiles/include/taglibs.jsp"%> --%>
<%--
	现在是用的@include方式，需要保证页面上提供pager和pager_base_url给pager.jsp使用
--%>
<!-- <script type="text/javascript"> -->
<%-- // // 	alert('${pager.nextPage}'); --%>
<!-- </script> -->
<!-- <div class="pagination pagination-right"> -->
<!-- 	<ul> -->
<!-- 	<li class="disabled"> -->
<%-- 		<a>总页数：${pager.pageCount}&nbsp;&nbsp;&nbsp;总记录数：${pager.totalCount}</a> --%>
<!-- 	</li> -->
<%-- 	<li <c:if test="${1 eq pager.pageNumber}">class="disabled"</c:if>><a href="${pager_base_url}&pageNo=1">首页</a></li> --%>
<%-- 	<li <c:if test="${1 eq pager.pageNumber}">class="disabled"</c:if>><a href="${pager_base_url}&pageNo=${pager.prevPage}">上一页</a></li> --%>
<%-- 	<c:forEach var="p" items="${pager.navPages}"> --%>
<%-- 		<c:choose> --%>
<%-- 			<c:when test="${p eq pager.pageNumber}"> --%>
<%-- 				<li class="disabled"><a>${p}</a></li> --%>
<%-- 			</c:when> --%>
<%-- 			<c:otherwise> --%>
<%-- 				<li<c:if test="${p eq pager.pageNumber}">class="disabled"</c:if>><a href="${pager_base_url}&pageNo=${p}">${p}</a></li> --%>
<%-- 			</c:otherwise> --%>
<%-- 		</c:choose> --%>
<%-- 	</c:forEach> --%>
<%-- 	<li <c:if test="${pager.pageCount eq pager.pageNumber}">class="disabled"</c:if>><a href="${pager_base_url}&pageNo=${pager.nextPage}">下一页</a></li> --%>
<%-- 	<li <c:if test="${pager.pageCount eq pager.pageNumber}">class="disabled"</c:if>><a href="${pager_base_url}&pageNo=${pager.pageCount}">尾页</a></li></ul> --%>
<!-- </div> -->



<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="currentPage" value="${obj.page.currentPage}" scope="request"></c:set>
<c:set var="totalPage" value="${obj.page.totalPage}" scope="request"></c:set>

<input type="hidden" id="sortType" name="page.sortType" value="${obj.page.sortType}"></input>
<input type="hidden" id="sortCol" name="page.sortCol" value="${obj.page.sortCol}"></input>
<input type="hidden" id="showCount" name="page.showCount" value="${obj.page.showCount}"/>
<c:choose>
	<c:when test="${obj.page.totalResult>0}">
	<div class="pagination pagination-right">
		<ul>
			<li class="disabled"><a>共${obj.page.totalResult}条记录</a></li>
			<c:choose>
				<c:when test="${currentPage==1}">
					<li class="disabled"><a href="javascript:void(0);">首页</a></li>
					<li class="disabled"><a href="javascript:void(0);">上页</a></li>
				</c:when>
				<c:otherwise>
					<li class="pageinfo"><a href="javascript:nextPage(1)">首页</a></li>
					<li class="pageinfo"><a href="javascript:nextPage(${currentPage-1})">上页</a></li>
				</c:otherwise>
		 	</c:choose>
			<%
				int showTag = 10;//分页标签显示数量
				int startTag = 1;
				Integer currentPage = (Integer)request.getAttribute("currentPage");
				Integer totalPage = (Integer)request.getAttribute("totalPage");
				if(currentPage>showTag){
					startTag = currentPage-1;
				}
				int endTag = startTag+showTag-1;
				for(int i=startTag; i<=totalPage && i<=endTag; i++){
					if(currentPage==i)
						out.println("<li class=\"disabled\"><a href=\"javascript:void(0);\">"+i+"</a></li>");
					else
						out.println("<li class=\"pageinfo\"><a href=\"javascript:nextPage("+
							i+")\">"+i+"</a></li>");
				}
			%>
			<c:choose>
				<c:when test="${currentPage==totalPage}">
					<li class="disabled"><a href="javascript:void(0);">下页</a></li>
					<li class="disabled"><a href="javascript:void(0);">尾页</a></li>
					<li class="disabled"><a>${currentPage}/${totalPage}页</a></li>
				</c:when>
				<c:otherwise>
					<li class="pageinfo"><a href="javascript:nextPage(${currentPage+1});">下页</a></li>
					<li class="pageinfo"><a href="javascript:nextPage(${totalPage});">尾页</a></li>
					<li class="disabled"><a>${currentPage}/${totalPage}页</a></li>
				</c:otherwise>
		 	</c:choose>
<%-- 			<li><input type="text" style="width:25px;" value="${currentPage}"/><input type="button"  --%>
<!-- 					style="width:30px;" value="GO"  -->
<!-- 					onclick="nextPage(this.previousSibling.value)"/></li> -->
		</ul>
		<script type="text/javascript">
			function nextPage(page){
				//ADD BY PYX 20140228 手动控制页面的数量
				var showCount=document.getElementById("showCount").value;
				if(showCount==0)showCount=10;
				if(true && document.forms[0]){
					var url = document.forms[0].getAttribute("action");
					if(url.indexOf('?')>-1){
						url += "&page.currentPageS=";
					}else{
						url += "?page.currentPageS=";
					}
					//modify by 20140228 add +"&page.showCount="+showCount;
					document.forms[0].action = url+page+"&page.showCount="+showCount;
					document.forms[0].submit();
					//modify by 20170929 清空
					showCount.action="";
				}else{
					var url = document.location+'';
					if(url.indexOf('?')>-1){
						if(url.indexOf('currentPageS')>-1){
							var reg = /(page.)?currentPageS=\d*/g;
							url = url.replace(reg,'$1currentPageS=');
						}else{
							url += "&page.currentPageS=";
						}
					}else{
						url += "?page.currentPageS=";
					}
					//modify by 20140228 add +"&page.showCount="+showCount;
					document.location = url + page+"&page.showCount="+showCount;
				}
			}
		</script>
		</div>
	</c:when>
	<c:otherwise>
		<div class="pagination pagination-right">
			<ul>
				<li class="disabled"><a href="javascript:void(0);">首页</a></li>
				<li class="disabled"><a href="javascript:void(0);">上页</a></li>
				<li class="disabled"><a href="javascript:void(0);">下页</a></li>
				<li class="disabled"><a href="javascript:void(0);">尾页</a></li>
				<li class="disabled"><a>0/0页</a></li>
			</ul>
		</div>
	</c:otherwise>
</c:choose>