<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="/jsp_tiles/include/taglibs.jsp" %>
<ul class="shortcut-buttons-set">
				
	<li><a class="shortcut-button" href="<c:url value='/admin/reader/add' />"><span>
		<img src="<c:url value='/media/resources/images/icons/pencil_48.png' />" alt="icon"><br>
		读者办证
	</span></a></li>
	
	<li><a class="shortcut-button" href="<c:url value='/admin/sys/rdaccount/charge' />"><span>
		<img src="<c:url value='/media/resources/images/icons/paper_content_pencil_48.png' />" alt="icon"><br>
		一卡通充值
	</span></a></li>
	
	<li><a class="shortcut-button" href="<c:url value='/admin/sys/readerRole/list' />"><span>
		<img src="<c:url value='/media/resources/images/icons/image_add_48.png' />" alt="icon"><br>
		用户角色管理
	</span></a></li>
	
	<li><a class="shortcut-button" href="<c:url value='/admin/sys/cirfinlog/list' />"><span>
		<img src="<c:url value='/media/resources/images/icons/clock_48.png'/>" alt="icon"><br>
		收费明细统计
	</span></a></li>
	
</ul><!-- End .shortcut-buttons-set -->