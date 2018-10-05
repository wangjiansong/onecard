<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="/jsp_tiles/include/taglibs.jsp" %>
<c:set var="BASE_URL">
	<c:url value="/" />
</c:set>
<script type="text/javascript">
</script>
<div class="well sidebar-nav">
	<div class="accordion" id="accordion2">
		<div class="accordion-heading">
			<a class="accordion-toggle" data-toggle="collapse" data-parent="#accordion2" href="#collapseOne">
				<i class="icon-home"></i>&nbsp控制面板
			</a>
		</div>
		<div id="collapseOne" class="accordion-body collapse <c:if test="${fn:startsWith(ACTIVE_MENU, 'admin')}">in</c:if>">
			<ul class="nav nav-list">
				<li class="submenuItem <c:if test="${ACTIVE_MENU eq 'admin_index'}">active</c:if>">
					<a href="<c:url value='/admin' />">首页</a>
				</li>
			</ul>
		</div>
		<div class="accordion-heading">
			<a class="accordion-toggle" data-toggle="collapse" data-parent="#accordion2" href="#collapseTwo">
				<i class="icon-user"></i>&nbsp读者管理
			</a>
		</div>
		<div id="collapseTwo" class="accordion-body collapse 
			<c:if test="${ACTIVE_MENU eq 'reader_add'}">in</c:if>
			<c:if test="${ACTIVE_MENU eq 'reader_index'}" >in</c:if>
			<c:if test="${ACTIVE_MENU eq 'reader_readerUnSynList'}" >in</c:if>
			<c:if test="${ACTIVE_MENU eq 'regist_index'}" >in</c:if>
			<c:if test="${ACTIVE_MENU eq 'reader_pay'}" >in</c:if>
			<c:if test="${ACTIVE_MENU eq 'reader_approve'}" >in</c:if>
			<c:if test="${ACTIVE_MENU eq 'reader_deduction'}" >in</c:if>
			<c:if test="${ACTIVE_MENU eq 'reader_deduction'}" >in</c:if>
			">
			<ul class="nav nav-list">
				<li class="submenuItem <c:if test="${ACTIVE_MENU eq 'reader_add'}">active</c:if>">
					<shiro:hasPermission name="reader:add">
						<a href="<c:url value='/admin/reader/add' />">读者办证</a>
					</shiro:hasPermission>
				</li>
				<li class="submenuItem <c:if test="${ACTIVE_MENU eq 'reader_index'}" >active</c:if>">
					<shiro:hasPermission name="reader:index">
						<a href="<c:url value='/admin/reader/index' />">读者查询</a>
					</shiro:hasPermission>
				</li>
				<li class="submenuItem <c:if test="${ACTIVE_MENU eq 'reader_readerUnSynList'}" >active</c:if>">
					<shiro:hasPermission name="reader:readerUnSynList">
						<a href="<c:url value='/admin/reader/readerUnSynList' />">未同步列表</a>
					</shiro:hasPermission>
				</li>
				<li class="submenuItem <c:if test="${ACTIVE_MENU eq 'regist_index'}" >active</c:if>">
					<shiro:hasPermission name="reader:regist">
						<a href="<c:url value='/admin/netreader/regist' />">网络注册</a>
					</shiro:hasPermission>
				</li>
				<li class="submenuItem <c:if test="${ACTIVE_MENU eq 'reader_pay'}" >active</c:if>">
					<shiro:hasPermission name="reader:pay">
						<a href="<c:url value='/admin/sys/readerPay/detail' />">读者欠款查询</a>
					</shiro:hasPermission>
				</li>
				<li class="submenuItem <c:if test="${ACTIVE_MENU eq 'reader_approve'}" >active</c:if>">
					<shiro:hasPermission name="reader:approve">
						<a href="<c:url value='/admin/netreader/approve' />">注册审批</a>
					</shiro:hasPermission>
				</li>
				<li class="submenuItem <c:if test="${ACTIVE_MENU eq 'reader_deduction'}" >active</c:if>">
					<shiro:hasPermission name="reader:deduction">
						<a href="<c:url value='/admin/reader/deduction' />">读者扣费</a>
					</shiro:hasPermission>
				</li>
				<li class="submenuItem">
					<shiro:hasPermission name="reader:list">
						<a href="<c:url value='' />">读者清单</a>
					</shiro:hasPermission>
				</li>
				<li class="submenuItem">
					<shiro:hasPermission name="reader:batchImport">
						<a href="<c:url value='' />">读者批导入</a>
					</shiro:hasPermission>
				</li>
				<li class="submenuItem">
					<shiro:hasPermission name="reader:statistics">
						<a href="<c:url value='' />">读者统计</a>
					</shiro:hasPermission>
				</li>
			</ul>
		</div>
		<div class="accordion-heading">
			<a class="accordion-toggle" data-toggle="collapse" data-parent="#accordion2" href="#collapseThree">
				<i class="icon-calendar"></i>&nbsp结算中心
			</a>
		</div>
		<div id="collapseThree" class="accordion-body collapse <c:if test="${fn:startsWith(ACTIVE_MENU, 'cirfinlog')}">in</c:if> 
			<c:if test="${ACTIVE_MENU eq 'finType_list'}">in</c:if>
			<c:if test="${ACTIVE_MENU eq 'chargeType_list'}">in</c:if>
			<c:if test="${ACTIVE_MENU eq 'readerfee_list'}">in</c:if>
			<c:if test="${fn:startsWith(ACTIVE_MENU, 'rdaccount')}">in</c:if>
			<c:if test="${ACTIVE_MENU eq 'card_consumption'}">in</c:if>">
			<ul class="nav nav-list">
				<li class="submenuItem <c:if test="${ACTIVE_MENU eq 'cirfinlog_chargelist'}">active</c:if>">
					<shiro:hasPermission name="cirfin:charge_list">
						<a href="<c:url value='/admin/sys/cirfinlog/chargeList' />">充值查询</a>
					</shiro:hasPermission>
				</li>
				<li class="submenuItem">
					<shiro:hasPermission name="cirfin:consumeList">
						<a href="<c:url value='' />">消费明细查询</a>
					</shiro:hasPermission>
				</li>
				<li class="submenuItem <c:if test="${ACTIVE_MENU eq 'cirfinlog_statistics'}">active</c:if>">
					<shiro:hasPermission name="cirfin:statistics">
						<a href="<c:url value='/admin/sys/cirfinlog/statistics' />">财经统计</a>
					</shiro:hasPermission>
				</li>
				<li class="submenuItem <c:if test="${ACTIVE_MENU eq 'cirfinlog_cirFinStatistics'}">active</c:if>">
					<shiro:hasPermission name="cirfin:cirFinStatistics">
						<a href="<c:url value='/admin/sys/cirfinlog/cirFinStatistics' />">流通财经统计</a>
					</shiro:hasPermission>
				</li>
				<li class="submenuItem <c:if test="${ACTIVE_MENU eq 'cirfinlog_personalList'}">active</c:if>">
					<shiro:hasPermission name="cirfin:personalList">
						<a href="<c:url value='/admin/sys/cirfinlog/personalList' />">个人财经明细</a>
					</shiro:hasPermission>
				</li>
				<li class="submenuItem <c:if test="${ACTIVE_MENU eq 'cirfinlog_list'}">active</c:if>">
					<shiro:hasPermission name="cirfin:detail_list">
						<a href="<c:url value='/admin/sys/cirfinlog/list' />">财经明细统计</a>
					</shiro:hasPermission>
				</li>
				<li class="submenuItem <c:if test="${ACTIVE_MENU eq 'cirfinlogList'}">active</c:if>">
					<shiro:hasPermission name="cirfin:cirfinlogList">
						<a href="<c:url value='/admin/sys/cirfinlog/cirfinlogList' />">财经明细列表</a>
					</shiro:hasPermission>
				</li>
				<li class="submenuItem <c:if test="${ACTIVE_MENU eq 'cirfinlog_monetaryList'}">active</c:if>">
					<shiro:hasPermission name="cirfin:monetaryList">
						<a  href="<c:url value='/admin/sys/cirfinlog/monetaryList' />">财经金额统计</a>
					</shiro:hasPermission>
				</li>
				<li class="submenuItem <c:if test="${ACTIVE_MENU eq 'cirfinlog_finSettlement'}">active</c:if>">
					<shiro:hasPermission name="cirfin:finSettlement">
						<a href="<c:url value='/admin/sys/cirfinlog/finSettlement' />">刷卡财经结算</a>
					</shiro:hasPermission>
				</li>
				<li class="submenuItem <c:if test="${ACTIVE_MENU eq 'cirfinlog_autoStatistics'}">active</c:if>">
					<shiro:hasPermission name="cirfin:autoStatistics">
						<a href="<c:url value='/admin/sys/cirfinlog/autoStatistics' />">多馆自动结算</a>
					</shiro:hasPermission>
				</li>
				<li class="submenuItem <c:if test="${ACTIVE_MENU eq 'cirfinlog_moreLibFinSettle'}">active</c:if>">
					<shiro:hasPermission name="cirfin:moreLibFinSettle">
						<a href="<c:url value='/admin/sys/cirfinlog/moreLibFinSettle' />">多馆财经结算</a>
					</shiro:hasPermission>
				</li>
				<li class="submenuItem <c:if test="${ACTIVE_MENU eq 'cirfinlog_moreLibFinSettleDetail'}">active</c:if>">
					<shiro:hasPermission name="cirfin:moreLibFinSettleDetail">
						<a href="<c:url value='/admin/sys/cirfinlog/moreLibFinSettleDetail' />">多馆财经明细</a>
					</shiro:hasPermission>
				</li>
				<li class="submenuItem <c:if test="${ACTIVE_MENU eq 'finType_list'}">active</c:if>">
					<shiro:hasPermission name="fintype:list">
						<a href="<c:url value='/admin/sys/fintype/list' />">费用类型管理</a>
					</shiro:hasPermission>
				</li>
				<li class="submenuItem <c:if test="${ACTIVE_MENU eq 'readerfee_list'}">active</c:if>">
					<shiro:hasPermission name="readerfee:list">
						<a href="<c:url value='/admin/sys/readerfee/list' />">费用管理参数</a>
					</shiro:hasPermission>
				</li>
				<li class="submenuItem <c:if test="${ACTIVE_MENU eq 'rdaccount_list'}">active</c:if>">
					<shiro:hasPermission name="rdaccount:list">
						<a href="<c:url value='/admin/sys/rdaccount/list' />">账户列表</a>
					</shiro:hasPermission>
				</li>
				<li class="submenuItem <c:if test="${ACTIVE_MENU eq 'rdaccount_charge'}">active</c:if>">
					<shiro:hasPermission name="cirfin:charge">
						<a href="<c:url value='/admin/sys/rdaccount/charge' />">充值处理</a>
					</shiro:hasPermission>
				</li>
				<li class="submenuItem <c:if test="${ACTIVE_MENU eq 'chargeType_list'}">active</c:if>">
					<shiro:hasPermission name="chargeType:list">
						<a href="<c:url value='/admin/sys/chargetype/list' />">充值类型管理</a>
					</shiro:hasPermission>
				</li>
				<li class="submenuItem <c:if test="${ACTIVE_MENU eq 'rdaccount_refund'}">active</c:if>">
					<shiro:hasPermission name="cirfin:refund">
						<a href="<c:url value='/admin/sys/rdaccount/refund' />">退款处理</a>
					</shiro:hasPermission>
				</li>
				<%-- <li class="submenuItem <c:if test="${ACTIVE_MENU eq 'subsidy_grant'}">active</c:if>"> --%>
				<%-- 	<a href="<c:url value='/admin/subsidy/grant' />">补助发放管理</a> --%>
				<%-- </li> --%>
				<li class="submenuItem <c:if test="${ACTIVE_MENU eq 'card_consumption'}">active</c:if>">
					<shiro:hasPermission name="cirfin:rule">
						<a href="<c:url value='/admin/card/consumption/rule' />">刷卡消费管理</a>
					</shiro:hasPermission>
				</li>
			</ul>
		</div>
		<div class="accordion-heading">
			<a class="accordion-toggle" data-toggle="collapse" data-parent="#accordion2" href="#collapseFour">
				<i class="icon-eye-open"></i>&nbsp授权管理
			</a>
		</div>
		<div id="collapseFour" class="accordion-body collapse <c:if test="${fn:startsWith(ACTIVE_MENU, 'authorization')}">in</c:if>
			<c:if test="${ACTIVE_MENU eq 'finApp_list'}">in</c:if>">
			<ul class="nav nav-list">
				<li class="submenuItem <c:if test="${ACTIVE_MENU eq 'finApp_list'}">active</c:if>">
					<shiro:hasPermission name="finapp:list">
						<a href="<c:url value='/admin/sys/finapp/list' />">授权系统管理</a>
					</shiro:hasPermission>
				</li>
				<li class="submenuItem <c:if test="${ACTIVE_MENU eq 'authorization_list'}">active</c:if>">
					<shiro:hasPermission name="finapp:accessControl">
						<a href="<c:url value='/admin/sys/auth/list' />">访问控制</a>
					</shiro:hasPermission>
				</li>
				<li class="submenuItem <c:if test="${ACTIVE_MENU eq 'authorization_api'}">active</c:if>">
					<shiro:hasPermission name="finapp:api">
						<a href="<c:url value='/admin/sys/auth/apiInfo' />">API</a>
					</shiro:hasPermission>
				</li>
			</ul>
		</div>
		<div class="accordion-heading">
			<a class="accordion-toggle" data-toggle="collapse" data-parent="#accordion2" href="#collapseFive">
				<i class="icon-globe"></i>&nbsp系统组织管理
			</a>
		</div>
		<div id="collapseFive" class="accordion-body collapse 
			<c:if test="${ACTIVE_MENU eq 'reader_role_list'}">in</c:if>
			<c:if test="${ACTIVE_MENU eq 'role_list'}">in</c:if>
			<c:if test="${ACTIVE_MENU eq 'compet_list'}">in</c:if>
			<c:if test="${ACTIVE_MENU eq 'resource_list'}">in</c:if>
			<c:if test="${ACTIVE_MENU eq 'restaurant_list'}">in</c:if>
			<c:if test="${ACTIVE_MENU eq 'blackboard_index'}">in</c:if>
			<c:if test="${ACTIVE_MENU eq 'guide_index'}">in</c:if>
			<c:if test="${ACTIVE_MENU eq 'system_index'}" >in</c:if>
			">
			<ul class="nav nav-list">
				<li class="submenuItem <c:if test="${ACTIVE_MENU eq 'reader_role_list'}">active</c:if>">
					<shiro:hasPermission name="readerRole:list">
						<a href="<c:url value='/admin/sys/readerRole/list' />">用户管理</a>
					</shiro:hasPermission>
				</li>
				<li class="submenuItem <c:if test="${ACTIVE_MENU eq 'role_list'}">active</c:if>">
					<shiro:hasPermission name="role:list">
						<a href="<c:url value='/admin/sys/role/list' />">角色管理</a>
					</shiro:hasPermission>
				</li>
				<li class="submenuItem <c:if test="${ACTIVE_MENU eq 'compet_list'}">active</c:if>">
					<shiro:hasPermission name="compet:list">
						<a href="<c:url value='/admin/sys/compet/list' />">权限管理</a>
					</shiro:hasPermission>
				</li>
				<li class="submenuItem <c:if test="${ACTIVE_MENU eq 'resource_list'}">active</c:if>">
					<shiro:hasPermission name="resource:list">
						<a href="<c:url value='/admin/sys/res/list' />">资源管理</a>
					</shiro:hasPermission>
				</li>
				<li class="submenuItem <c:if test="${ACTIVE_MENU eq 'restaurant_list'}">active</c:if>">
					<shiro:hasPermission name="restaurant:list">
					<a href="<c:url value='/admin/sys/restaurant/list' />">班次时间设置</a>
					</shiro:hasPermission>
				</li>
				<li class="submenuItem <c:if test="${ACTIVE_MENU eq 'blackboard_index'}">active</c:if>">
					<shiro:hasPermission name="blackboard:list">
						<a href="<c:url value='/admin/sys/blackboard/list' />">公告指南</a>
					</shiro:hasPermission>
				</li>
				<li class="submenuItem <c:if test="${ACTIVE_MENU eq 'guide_index'}">active</c:if>">
					<shiro:hasPermission name="guide:list">
						<a href="<c:url value='/admin/sys/guide/list' />">办事指南</a>
					</shiro:hasPermission>
				</li>
				<li class="submenuItem <c:if test="${ACTIVE_MENU eq 'system_index'}" >active</c:if>">
					<shiro:hasPermission name="reader:readertype">
						<a href="<c:url value='/system/reader/readertype/readerTypeIndex' />">系统管理</a>
					</shiro:hasPermission>
				</li>
			</ul>
		</div>
		<div class="accordion-heading">
			<a class="accordion-toggle" data-toggle="collapse" data-parent="#accordion2" href="#collapseSix">
				<i class="icon-tasks"></i>&nbsp系统日志
			</a>
		</div>
		<div id="collapseSix" class="accordion-body collapse <c:if test="${fn:startsWith(ACTIVE_MENU, 'logcir')}">in</c:if>
			<c:if test="${fn:startsWith(ACTIVE_MENU, 'syncRecord')}">in</c:if>">
			<ul class="nav nav-list">
				<li class="submenuItem <c:if test="${ACTIVE_MENU eq 'logcir_list'}">active</c:if>">
					<shiro:hasPermission name="logcir:list">
						<a href="<c:url value='/admin/sys/logcir/list' />">操作日志</a>
					</shiro:hasPermission>
				</li>
				<li class="submenuItem <c:if test="${ACTIVE_MENU eq 'syncRecord_list'}">active</c:if>">
					<shiro:hasPermission name="logcir:syncRecord_list">
						<a href="<c:url value='/admin/sys/syncrecord/list' />">同步日志</a>
					</shiro:hasPermission>
				</li>
			</ul>
		</div>
				<%-- <div class="accordion-heading">
			<a class="accordion-toggle" data-toggle="collapse" data-parent="#accordion2" href="#collapseSeven">
				<i class="icon-user"></i>&nbsp短信服务中心
			</a>
		</div>
		<div id="collapseSeven" class="accordion-body collapse 
			<c:if test="${ACTIVE_MENU eq 'SMSservice_add'}">in</c:if>
			">
			<ul class="nav nav-list">
				<li class="submenuItem <c:if test="${ACTIVE_MENU eq 'SMSservice_add'}">active</c:if>">
					<shiro:hasPermission name="SMSservice:add">
						<a href="<c:url value='/admin/reader/add' />">个人借阅信息查询</a>
					</shiro:hasPermission>
				</li>
			</ul>
		</div> --%>
		
		
	</div>
	
</div>