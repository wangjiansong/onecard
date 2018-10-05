<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="/jsp_tiles/include/taglibs.jsp"%>

<%--
现在是用的@include方式，需要保证页面上提供pager和pager_base_url给pager.jsp使用
--%>
<div class="pagination">
	<ul>
	<li class="disabled"><a>总页数: ${paginator.getTotalPages()}, 总记录数: ${paginator.totalItems}</a></li>
	<li <c:if test="${paginator.isFirstPage() == true}">class='disabled'</c:if>><a href="${pager_base_url}pageSize=20&pageNo=1">首页</a></li>
	<li <c:if test="${paginator.isFirstPage() == true}">class='disabled'</c:if>><a href="${pager_base_url}pageSize=20&pageNo=${paginator.getPrePage()}">上一页</a></li>
	<c:forEach var="p" items="${paginator.getSlider()}">
		<c:choose>
			<c:when test="${p eq paginator.page}">
				<li class="disabled"><a>${p}</a></li>
			</c:when>
			<c:otherwise>
				<li<c:if test="${p eq paginator.page}">class='disabled'</c:if>><a href="${pager_base_url}pageSize=20&pageNo=${p}">${p}</a></li>
			</c:otherwise>
		</c:choose>
	</c:forEach>
	<li <c:if test="${paginator.getTotalPages() eq paginator.page}">class='disabled'</c:if>><a href="${pager_base_url}pageSize=20&pageNo=${paginator.getNextPage()}">下一页</a></li>
	<li <c:if test="${paginator.getTotalPages() eq paginator.page}">class='disabled'</c:if>><a href="${pager_base_url}pageSize=20&pageNo=${paginator.getTotalPages()}">尾页</a></li></ul>
</div>
