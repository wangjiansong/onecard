package com.interlib.sso.domain;

import java.io.Serializable;

public class Operator implements Serializable {
	private static final long serialVersionUID = 4248819000941746386L;
	
	private Integer recno;
	private String libcode;
	private String loginId;
	private String name;
	private String workSize;
	private String style;
	
	public Integer getRecno() {
		return recno;
	}
	public void setRecno(Integer recno) {
		this.recno = recno;
	}
	public String getLibcode() {
		return libcode;
	}
	public void setLibcode(String libcode) {
		this.libcode = libcode;
	}
	public String getLoginId() {
		return loginId;
	}
	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getWorkSize() {
		return workSize;
	}
	public void setWorkSize(String workSize) {
		this.workSize = workSize;
	}
	public String getStyle() {
		return style;
	}
	public void setStyle(String style) {
		this.style = style;
	}
	
}
