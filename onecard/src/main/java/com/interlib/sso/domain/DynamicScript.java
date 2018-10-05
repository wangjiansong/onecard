package com.interlib.sso.domain;

import java.io.Serializable;
import java.util.Date;

public class DynamicScript implements Serializable {
	private static final long serialVersionUID = -4097662336653670646L;
	
	private Long id;
	private String scriptName;
	private String scriptSource;
	private Date lastUpdated;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getScriptName() {
		return scriptName;
	}
	public void setScriptName(String scriptName) {
		this.scriptName = scriptName;
	}
	public String getScriptSource() {
		return scriptSource;
	}
	public void setScriptSource(String scriptSource) {
		this.scriptSource = scriptSource;
	}
	public Date getLastUpdated() {
		return lastUpdated;
	}
	public void setLastUpdated(Date lastUpdated) {
		this.lastUpdated = lastUpdated;
	}
	
}
