package com.interlib.sso.domain;

import java.io.Serializable;
import java.util.Date;

import com.interlib.sso.page.PageEntity;


/**
 * 授权管理
 * @author Home
 *
 */
public class Authorization implements Serializable {
	
	private Integer id;
	private String appCode;//授权app
	private String appName;
	private String ipAddress;//授权的ip段，为空则不限制ip
	private String staticCode; //密钥
	private String encodeRule; //加密规则   {appCode}{today:YYYYMMDD}{code}
	private String authorizeApi; //允许调用的接口
	private String endDate;	//截止日期
	private String bindingUserId;  //绑定的操作员帐号
	private String bindingUserName;
	private PageEntity page;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getAppCode() {
		return appCode;
	}
	public void setAppCode(String appCode) {
		this.appCode = appCode;
	}
	public String getAppName() {
		return appName;
	}
	public void setAppName(String appName) {
		this.appName = appName;
	}
	public String getIpAddress() {
		return ipAddress;
	}
	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}
	public String getStaticCode() {
		return staticCode;
	}
	public void setStaticCode(String staticCode) {
		this.staticCode = staticCode;
	}
	
	public String getEncodeRule() {
		return encodeRule;
	}
	public void setEncodeRule(String encodeRule) {
		this.encodeRule = encodeRule;
	}
	public String getAuthorizeApi() {
		return authorizeApi;
	}
	public void setAuthorizeApi(String authorizeApi) {
		this.authorizeApi = authorizeApi;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public String getBindingUserId() {
		return bindingUserId;
	}
	public void setBindingUserId(String bindingUserId) {
		this.bindingUserId = bindingUserId;
	}
	
	public String getBindingUserName() {
		return bindingUserName;
	}
	public void setBindingUserName(String bindingUserName) {
		this.bindingUserName = bindingUserName;
	}
	public PageEntity getPage() {
		return page;
	}
	public void setPage(PageEntity page) {
		this.page = page;
	}
	
}
