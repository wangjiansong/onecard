<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="/jsp_tiles/include/taglibs.jsp"%>

<h1>快速导航</h1>

<hr />

<div class="row-fluid">
	<div class="span6">
		<h3>读者管理</h3>
		<p>
			统一管理读者
		</p>
		<ul class="unstyled">
			<li>
				<shiro:hasPermission name="reader:add">
					<a href="<c:url value='/admin/reader/add' />">读者办证</a>
				</shiro:hasPermission>
			</li>
			<li>
				<shiro:hasPermission name="reader:index">
					<a href="<c:url value='/admin/reader/index' />">读者查询</a>
				</shiro:hasPermission>
			</li>
			<li>
				<shiro:hasPermission name="reader:readerUnSynList">
					<a href="<c:url value='/admin/reader/readerUnSynList' />">未同步列表</a>
				</shiro:hasPermission>
			</li>
			<li>
				<shiro:hasPermission name="reader:regist">
					<a href="<c:url value='/admin/netreader/regist' />">网络注册</a>
				</shiro:hasPermission>
			</li>
			<li>
				<shiro:hasPermission name="reader:pay">
					<a href="<c:url value='/admin/sys/readerPay/detail' />">读者欠款查询</a>
				</shiro:hasPermission>
			</li>
			<li>
				<shiro:hasPermission name="reader:approve">
						<a href="<c:url value='/admin/netreader/approve' />">注册审批</a>
					</shiro:hasPermission>
			</li>
			<li>
				<shiro:hasPermission name="reader:deduction">
					<a href="<c:url value='/admin/reader/deduction' />">读者扣费</a>
				</shiro:hasPermission>
			</li>
		</ul>
	</div>
	<div class="span6">
		<h3>结算中心</h3>
		<p>
			收费、结算、财经管理
		</p>
		<ul class="unstyled">
			<li>
			<shiro:hasPermission name="cirfin:charge_list">
						<a href="<c:url value='/admin/sys/cirfinlog/chargeList' />">充值查询</a>
					</shiro:hasPermission>
			</li>
			<li>
			<shiro:hasPermission name="cirfin:consumeList">
						<a href="<c:url value='' />">消费明细查询</a>
					</shiro:hasPermission>
			</li>
			<li>
			<shiro:hasPermission name="cirfin:statistics">
						<a href="<c:url value='/admin/sys/cirfinlog/statistics' />">财经统计</a>
					</shiro:hasPermission>
			</li>
			<li>
			<shiro:hasPermission name="cirfin:detail_list">
						<a href="<c:url value='/admin/sys/cirfinlog/list' />">财经明细统计</a>
					</shiro:hasPermission>
			</li>
			<li>
			<shiro:hasPermission name="cirfin:cirfinlogList">
						<a href="<c:url value='/admin/sys/cirfinlog/cirfinlogList' />">财经明细列表</a>
					</shiro:hasPermission>
			</li>
			<li>
			<shiro:hasPermission name="cirfin:monetaryList">
						<a  href="<c:url value='/admin/sys/cirfinlog/monetaryList' />">财经金额统计</a>
					</shiro:hasPermission>
			</li>
			<li>
			<shiro:hasPermission name="cirfin:finSettlement">
						<a href="<c:url value='/admin/sys/cirfinlog/finSettlement' />">刷卡财经结算</a>
					</shiro:hasPermission>
			</li>
			<li>
			<shiro:hasPermission name="cirfin:autoStatistics">
						<a href="<c:url value='/admin/sys/cirfinlog/autoStatistics' />">多馆自动结算</a>
					</shiro:hasPermission>
			</li>
			<li>
			<shiro:hasPermission name="cirfin:moreLibFinSettle">
						<a href="<c:url value='/admin/sys/cirfinlog/moreLibFinSettle' />">多馆财经结算</a>
					</shiro:hasPermission>
			</li>
			<li>
			<shiro:hasPermission name="cirfin:moreLibFinSettleDetail">
						<a href="<c:url value='/admin/sys/cirfinlog/moreLibFinSettleDetail' />">多馆财经明细</a>
					</shiro:hasPermission>
			</li>
			<li>
			<shiro:hasPermission name="fintype:list">
						<a href="<c:url value='/admin/sys/fintype/list' />">费用类型管理</a>
					</shiro:hasPermission>
			</li>
			<li>
			<shiro:hasPermission name="readerfee:list">
						<a href="<c:url value='/admin/sys/readerfee/list' />">费用管理参数</a>
					</shiro:hasPermission>
			</li>
			<li>
			<shiro:hasPermission name="rdaccount:list">
						<a href="<c:url value='/admin/sys/rdaccount/list' />">账户列表</a>
					</shiro:hasPermission>
			</li>
			<li>
			<shiro:hasPermission name="cirfin:charge">
						<a href="<c:url value='/admin/sys/rdaccount/charge' />">充值处理</a>
					</shiro:hasPermission>
			</li>
			<li>
			<shiro:hasPermission name="cirfin:refund">
						<a href="<c:url value='/admin/sys/rdaccount/refund' />">退款处理</a>
					</shiro:hasPermission>
			</li>
			<li>
			<shiro:hasPermission name="cirfin:rule">
						<a href="<c:url value='/admin/card/consumption/rule' />">刷卡消费管理</a>
					</shiro:hasPermission>
			</li>
		</ul>
	</div>
</div>

<hr />

<div class="row-fluid">
	<div class="span6">
		<h3>授权管理</h3>
		<p>
			第三方应用授权、管理
		</p>
		<ul class="unstyled">
			<li>
			<shiro:hasPermission name="finapp:list">
						<a href="<c:url value='/admin/sys/finapp/list' />">授权系统管理</a>
					</shiro:hasPermission>
			</li>
			<li>
			<shiro:hasPermission name="finapp:accessControl">
						<a href="<c:url value='/admin/sys/auth/list' />">访问控制</a>
					</shiro:hasPermission>
			</li>
			<li>
			<shiro:hasPermission name="finapp:api">
						<a href="<c:url value='/admin/sys/auth/apiInfo' />">API</a>
					</shiro:hasPermission>
			</li>
		</ul>
	</div>
	<div class="span6">
		<h3>系统组织管理</h3>
		<p>
			图书馆组织、角色管理
		</p>
		<ul class="unstyled">
			<li>
			<shiro:hasPermission name="readerRole:list">
						<a href="<c:url value='/admin/sys/readerRole/list' />">用户管理</a>
					</shiro:hasPermission>
			</li>
			<li>
			<shiro:hasPermission name="role:list">
						<a href="<c:url value='/admin/sys/role/list' />">角色管理</a>
					</shiro:hasPermission>
			</li>
			<li>
			<shiro:hasPermission name="compet:list">
						<a href="<c:url value='/admin/sys/compet/list' />">权限管理</a>
					</shiro:hasPermission>
			</li>
			<li>
			<shiro:hasPermission name="resource:list">
						<a href="<c:url value='/admin/sys/res/list' />">资源管理</a>
					</shiro:hasPermission>
			</li>
			<li>
			<shiro:hasPermission name="restaurant:list">
					<a href="<c:url value='/admin/sys/restaurant/list' />">班次时间设置</a>
					</shiro:hasPermission>
			</li>
			<li>
			<shiro:hasPermission name="blackboard:list">
						<a href="<c:url value='/admin/sys/blackboard/list' />">公告指南</a>
					</shiro:hasPermission>
			</li>
			<li>
			<shiro:hasPermission name="guide:list">
						<a href="<c:url value='/admin/sys/guide/list' />">办事指南</a>
					</shiro:hasPermission>
			</li>
			<li>
			<shiro:hasPermission name="reader:readertype">
						<a href="<c:url value='/system/reader/readertype/readerTypeIndex' />">系统管理</a>
					</shiro:hasPermission>
			</li>
		</ul>
	</div>
</div>

<hr />
