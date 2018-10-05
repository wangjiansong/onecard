package com.interlib.sso.domain;

import java.io.Serializable;

import com.interlib.sso.page.PageEntity;

public class LogCirType implements Serializable {

	private String logType;
	private String typeName;
	private PageEntity page;
	
	public String getLogType() {
		return logType;
	}
	public void setLogType(String logType) {
		this.logType = logType;
	}
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	public PageEntity getPage() {
		return page;
	}
	public void setPage(PageEntity page) {
		this.page = page;
	}
	
	
	
}
