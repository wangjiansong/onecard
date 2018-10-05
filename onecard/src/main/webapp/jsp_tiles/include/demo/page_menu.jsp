<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="/jsp_tiles/include/taglibs.jsp" %>

<c:set var="BASE_URL">
	<c:url value="/" />
</c:set>
<script type="text/javascript">
$(function(){
	$(".nav-top-item").click(function (e) {
		$(".nav-top-item").removeClass("current");
		$(this).addClass("current");
	});
	
});
</script>

<div id="sidebar">
	<div id="sidebar-wrapper">
	
	<h1 id="sidebar-title"><a href="#">Simpla Admin</a></h1>
	<!-- Logo (221px wide) -->
	<a href="#"><div align="center"></div></a>
		  
	<!-- Sidebar Profile links -->
	<div id="profile-links">
		<p>欢迎, <a href="#" title="Edit your profile">${READER_SESSION.reader.rdName}</a>, 你好! 
		</p>
		<!--<a href="#messages" rel="modal" title="3 Messages">3 Messages</a><br />-->
		<br />
		<a href="#" title="Sign Out">注销登录</a>
	</div>
	
	
	<ul id="main-nav">  <!-- Accordion Menu -->
		<li> 
			<a href="#" class="nav-top-item current">用户管理</a>
			<ul>
				<li><shiro:hasPermission name="reader:add">
				<a href="<c:url value='/admin/reader/add' />"  class="<c:if test="${ACTIVE_MENU eq 'reader_add'}" >current</c:if>">读者办证</a>
				</shiro:hasPermission></li>
				<li><shiro:hasPermission name="reader:index">
					<a href="<c:url value='/admin/reader/index' />" class="<c:if test="${ACTIVE_MENU eq 'reader_index'}" >current</c:if>">读者查询</a>
				</shiro:hasPermission></li>
				<li><shiro:hasPermission name="reader:approve">
				<a href="<c:url value='/admin/netreader/approve' />" class="<c:if test="${ACTIVE_MENU eq 'approve_index'}" >current</c:if>">注册审批</a>
				</shiro:hasPermission></li>
			</ul>
		</li>
		
		<li>
			<a href="#" class="nav-top-item">结算中心</a>
			<ul>
				<li><shiro:hasPermission name="cirfin:charge_list">
				<a href="<c:url value='/admin/sys/cirfinlog/chargeList' />" class="<c:if test="${ACTIVE_MENU eq 'cirfinlog_chargelist'}" >current</c:if>">充值查询</a>
				</shiro:hasPermission></li>
				<li><shiro:hasPermission name="cirfin:statistics">
				<a href="<c:url value='/admin/sys/cirfinlog/statistics' />" class="<c:if test="${ACTIVE_MENU eq 'cirfinlog_statistics'}" >current</c:if>">收费统计</a>
				</shiro:hasPermission></li>
				<li><shiro:hasPermission name="cirfin:detail_list">
				<a href="<c:url value='/admin/sys/cirfinlog/list' />" class="<c:if test="${ACTIVE_MENU eq 'cirfinlog_list'}" >current</c:if>">收费明细统计</a>
				</shiro:hasPermission></li>
				<li><shiro:hasPermission name="fintype:list">
				<a href="<c:url value='/admin/sys/fintype/list' />" class="<c:if test="${ACTIVE_MENU eq 'finType_list'}" >current</c:if>">费用类型管理</a>
				</shiro:hasPermission></li>
				<li><shiro:hasPermission name="readerfee:list">
				<a href="<c:url value='/admin/sys/readerfee/list' />" class="<c:if test="${ACTIVE_MENU eq 'readerfee_list'}" >current</c:if>">费用管理参数</a>
				</shiro:hasPermission></li>
				<li><shiro:hasPermission name="rdaccount:list">
				<a href="<c:url value='/admin/sys/rdaccount/list' />" class="<c:if test="${ACTIVE_MENU eq 'rdaccount_list'}" >current</c:if>">账户列表</a>
				</shiro:hasPermission></li>
				<li><shiro:hasPermission name="cirfin:charge">
				<a href="<c:url value='/admin/sys/rdaccount/charge' />" class="<c:if test="${ACTIVE_MENU eq 'rdaccount_charge'}" >current</c:if>">充值处理</a>
				</shiro:hasPermission></li>
				<li><shiro:hasPermission name="cirfin:refund">
				<a href="<c:url value='/admin/sys/rdaccount/refund' />" class="<c:if test="${ACTIVE_MENU eq 'rdaccount_refund'}" >current</c:if>">退款处理</a>
				</shiro:hasPermission></li>
			</ul>
		</li>
		
		<li>
			<a href="#" class="nav-top-item">授权管理</a>
			<ul>
				<li>
					<shiro:hasPermission name="finapp:list">
					<a href="<c:url value='/admin/sys/finapp/list' />" class="<c:if test="${ACTIVE_MENU eq 'finApp_list'}">current</c:if>">公共资源管理</a>
					</shiro:hasPermission>
				</li>
				<li>
					<shiro:hasPermission name="finapp:accessControl">
					<a href="<c:url value='/admin/sys/auth/list' />" class="<c:if test="${ACTIVE_MENU eq 'authorization_list'}">current</c:if>">访问控制</a>
					</shiro:hasPermission>
				</li>
				<li>
					<shiro:hasPermission name="finapp:api">
					<a href="<c:url value='/admin/sys/auth/apiInfo' />" class="<c:if test="${ACTIVE_MENU eq 'authorization_api'}">current</c:if>">API</a>
					</shiro:hasPermission>
				</li>
			</ul>
		</li>
		<li>
			<a href="#" class="nav-top-item">系统组织管理</a>
			<ul>
				<li>
					<shiro:hasPermission name="readerRole:list">
					<a href="<c:url value='/admin/sys/readerRole/list' />" class="<c:if test="${ACTIVE_MENU eq 'reader_role_list'}">current</c:if>">用户角色管理</a>
					</shiro:hasPermission>
				</li>
				<li>
					<shiro:hasPermission name="role:list">
					<a href="<c:url value='/admin/sys/role/list' />"  class="<c:if test="${ACTIVE_MENU eq 'role_list'}">current</c:if>">角色管理</a>
					</shiro:hasPermission>
				</li>
				<li>
					<shiro:hasPermission name="compet:list">
					<a href="<c:url value='/admin/sys/compet/list' />"  class="<c:if test="${ACTIVE_MENU eq 'compet_list'}">current</c:if>">权限管理</a>
					</shiro:hasPermission>
				</li>
				<li>
					<shiro:hasPermission name="resource:list">
					<a href="<c:url value='/admin/sys/res/list' />"  class="<c:if test="${ACTIVE_MENU eq 'resource_list'}">current</c:if>">资源管理</a>
					</shiro:hasPermission>
				</li>
				<li>
					<shiro:hasPermission name="blackboard:list">
					<a href="<c:url value='/admin/sys/blackboard/list' />"  class="<c:if test="${ACTIVE_MENU eq 'blackboard_index'}">current</c:if>">公告指南</a>
					</shiro:hasPermission>
				</li>
				<li>
					<shiro:hasPermission name="guide:list">
					<a href="<c:url value='/admin/sys/guide/list' />"  class="<c:if test="${ACTIVE_MENU eq 'guide_index'}">current</c:if>">办事指南</a>
					</shiro:hasPermission>
				</li>
				<li>
					<shiro:hasPermission name="reader:readertype">
					<a href="<c:url value='/system/reader/readertype/readerTypeIndex' />"  class="<c:if test="${ACTIVE_MENU eq 'system_index'}" >current</c:if>">系统管理</a>
					</shiro:hasPermission>
				</li>
			</ul>
		</li>
	
	</div>
</div>