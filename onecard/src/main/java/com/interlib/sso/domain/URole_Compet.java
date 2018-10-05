package com.interlib.sso.domain;

public class URole_Compet {

	private Integer roleId;
	private Integer competId;
	
	public URole_Compet(Integer roleId, Integer competId) {
		this.roleId = roleId;
		this.competId = competId;
	}
	
	public Integer getRoleId() {
		return roleId;
	}
	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}

	public Integer getCompetId() {
		return competId;
	}

	public void setCompetId(Integer competId) {
		this.competId = competId;
	}
	
}
