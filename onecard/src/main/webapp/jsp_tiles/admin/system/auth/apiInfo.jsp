<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/jsp_tiles/include/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<script type="text/javascript" src="<c:url value='/media/datepicker/WdatePicker.js' />"></script>

<c:set var="BASE_URL">
	<c:url value="/admin/sys/auth" />
</c:set>
<h2>接口说明</h2>

<hr />

<div class="row-fluid">
	<div class="span6">
		<h3>查询余额</h3>
		
		<ul class="unstyled">
			<li>http://ip:port/onecard/api/finance/query</li>
			<li>参数：</li>
			<li>rdid 读者证号</li>
			<li>appcode 调用接口的应用代码</li>
			<li>enc md5加密的32位小写约定密钥，规则请查看授权控制配置</li>
			<li>userid 操作员帐号</li>
			<li>libcode 馆代码</li>
		</ul>
	</div>
	<div class="span6">
	<h3>新增读者</h3>
		
		<ul class="unstyled">
			<li>http://ip:port/onecard/api/reader/addreader</li>
			<li>参数：</li>
			<li>rdid 读者证号</li>
			<li>appcode 调用接口的应用代码</li>
			<li>enc md5加密的32位小写约定密钥，规则请查看授权控制配置</li>
			<li>userid 操作员帐号</li>
			<li>libcode 馆代码</li>
		</ul>
	</div>
</div>

<hr />

<div class="row-fluid">
	<div class="span6">
		<h3>扣费</h3>
		
		<ul class="unstyled">
			<li>http://ip:port/onecard/api/finance/deduction</li>
			<li>参数：</li>
			<li>rdid 读者证号</li>
			<li>appcode 调用接口的应用代码</li>
			<li>fee 操作金额</li>
			<li>feetype 操作类型</li>
			<li>enc md5加密的32位小写约定密钥，规则请查看授权控制配置</li>
			<li>userid 操作员帐号</li>
			<li>libcode 馆代码</li>
		</ul>
	</div>
	<div class="span6">
		<h3>充值</h3>
		
		<ul class="unstyled">
			<li>http://ip:port/onecard/api/finance/charge</li>
			<li>参数：</li>
			<li>payId 交易票据单号</li>
			<li>rdid 读者证号</li>
			<li>appcode 调用接口的应用代码</li>
			<li>fee 操作金额</li>
			<li>enc md5加密的32位小写约定密钥，规则请查看授权控制配置</li>
			<li>userid 操作员帐号</li>
			<li>libcode 馆代码</li>
		</ul>
	</div>
</div>

<hr />

<div class="row-fluid">
	<div class="span6">
		<h3>冻结</h3>
		
		<ul class="unstyled">
			<li>http://ip:port/onecard/api/reader/doLock</li>
			<li>参数：</li>
			<li>rdid 读者证号</li>
			<li>rdpasswd 读者密码</li>
			<li>appcode 调用接口的应用代码</li>
			<li>enc md5加密的32位小写约定密钥，规则请查看授权控制配置</li>
			<li>userid 操作员帐号</li>
			<li>libcode 馆代码</li>
		</ul>
	</div>
	<div class="span6">
		<h3>恢复</h3>
		
		<ul class="unstyled">
			<li>http://ip:port/onecard/api/reader/doUnLock</li>
			<li>参数：</li>
			<li>rdid 读者证号</li>
			<li>rdpasswd 读者密码</li>
			<li>appcode 调用接口的应用代码</li>
			<li>enc md5加密的32位小写约定密钥，规则请查看授权控制配置</li>
			<li>userid 操作员帐号</li>
			<li>libcode 馆代码</li>
		</ul>
	</div>
</div>

<hr />
<div class="row-fluid">
	<div class="span6">
		<h3>挂失</h3>
		
		<ul class="unstyled">
			<li>http://ip:port/onecard/api/reader/loss</li>
			<li>参数：</li>
			<li>rdid 读者证号</li>
			<li>rdpasswd 读者密码</li>
			<li>appcode 调用接口的应用代码</li>
			<li>enc md5加密的32位小写约定密钥，规则请查看授权控制配置</li>
			<li>userid 操作员帐号</li>
			<li>libcode 馆代码</li>
		</ul>
	</div>
	<div class="span6">	
	<h3>查询充值</h3>
		
		<ul class="unstyled">
			<li>http://ip:port/onecard/api/finance/querycharge</li>
			<li>参数：</li>
			<li>payId 交易票据单号</li>
			<li>cardId卡号或rdid 读者证号</li>
			<li>appcode 调用接口的应用代码</li>
			<li>fee 操作金额</li>
			<li>enc md5加密的32位小写约定密钥，规则请查看授权控制配置</li>
			<li>userid 操作员帐号</li>
			<li>libcode 馆代码</li>
		</ul>
	</div>
</div>

<hr />
<div class="row-fluid">
	<div class="span6">
		<h3>财经日志</h3>
		
		<ul class="unstyled">
			<li>http://ip:port/onecard/api/cirfinlog/listCirFinLogForPager</li>
			<li>参数：</li>
			<li>rdid 读者证号</li>
			<li>rdpasswd 读者密码(MD5)</li>
			<li>appcode 调用接口的应用代码</li>
			<li>enc md5加密的32位小写约定密钥，规则请查看授权控制配置</li>
			<li>libcode 馆代码</li>
			<li>pageno 页码</li>
			<li>pagesize 分页大小</li>
			<li>feetype 财经类型</li>
			<li>startdate 开始日期(YYYY-MM-DD)</li>
			<li>enddate 结束日期(YYYY-MM-DD)</li>
			<li>返回:JSON格式数据</li>
		</ul>
	</div>
	<div class="span6">
		<h3>返回结果</h3>
		
		<ul class="unstyled">
			<li>xml格式
			<li>&lt;Result&gt;</li>
			<li>&lt;message&gt;&lt;&#47;message&gt;</li>
			<li>&lt;success&gt;1&lt;&#47;success&gt;</li>
			<li>&lt;account&gt;0.0&lt;&#47;account&gt;</li>
			<li>&lt;&#47;Result&gt;</li>
			</li>
			<li>参数：</li>
			<li>success 是否成功，1成功，0失败</li>
			<li>message 提示信息，当success返回0时才显示信息</li>
			<li>account 执行成功后账户余额</li>
		</ul>
	</div>
</div>

<div class="row-fluid">
	
</div>


