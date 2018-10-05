package com.interlib.sso.domain;

import java.io.Serializable;

import com.interlib.sso.page.PageEntity;

public class ChargeType implements Serializable {

	private String feeType;
	private String describe;
	private String appCode;
	private String appName;
	
	private String typefee;//类型金额  读者扣费给馆员提供选择
	
	private PageEntity page;	

	public String getFeeType() {
		return feeType;
	}
	public void setFeeType(String feeType) {
		this.feeType = feeType;
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
	public String getTypefee() {
		return typefee;
	}
	public void setTypefee(String typefee) {
		this.typefee = typefee;
	}
	public String getDescribe() {
		return describe;
	}
	public void setDescribe(String describe) {
		this.describe = describe;
	}
	
	public PageEntity getPage() {
		return page;
	}
	public void setPage(PageEntity page) {
		this.page = page;
	}

	
	
}
