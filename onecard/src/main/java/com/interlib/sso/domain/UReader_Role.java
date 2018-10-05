package com.interlib.sso.domain;

import java.io.Serializable;

public class UReader_Role implements Serializable{

	private String rdId;
	
	private Integer roleId;
	
	public UReader_Role(String rdId, Integer roleId) {
		this.rdId = rdId;
		this.roleId = roleId;
	}

	public String getRdId() {
		return rdId;
	}

	public void setRdId(String rdId) {
		this.rdId = rdId;
	}

	public Integer getRoleId() {
		return roleId;
	}

	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}
	
	
}
