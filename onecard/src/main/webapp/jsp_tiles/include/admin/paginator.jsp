<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="currentPage" value="${obj.page.currentPage}" scope="request"></c:set>
<c:set var="totalPage" value="${obj.page.totalPage}" scope="request"></c:set>
<input type="hidden" id="sortType" name="page.sortType" value="${obj.page.sortType}"></input>
<input type="hidden" id="sortCol" name="page.sortCol" value="${obj.page.sortCol}"></input>
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
				int showTag = 10;
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
		</ul>
		<script type="text/javascript">
			function nextPage(page){
				if(true && document.forms[0]){
					var url = document.forms[0].getAttribute("action");
					if(url.indexOf('?')>-1){
						url += "&page.currentPageS=";
					}else{
						url += "?page.currentPageS=";
					}
					document.forms[0].action = url+page;
					document.forms[0].submit();
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
					document.location = url + page;
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