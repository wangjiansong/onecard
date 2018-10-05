package com.interlib.sso.domain;

import java.io.Serializable;
import java.util.List;

import com.interlib.sso.page.PageEntity;

public class Roles implements Serializable {
	
	private Integer roleId;
	private String roleName;
	private String libcode;
	private String describe;
	private List<Compets> comList;
	
	private PageEntity page;//分页对象
	
	public Integer getRoleId() {
		return roleId;
	}
	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}
	public String getRoleName() {
		return roleName;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	
	public String getLibcode() {
		return libcode;
	}
	public void setLibcode(String libcode) {
		this.libcode = libcode;
	}
	public String getDescribe() {
		return describe;
	}
	public void setDescribe(String describe) {
		this.describe = describe;
	}
	public List<Compets> getComList() {
		return comList;
	}
	public void setComList(List<Compets> comList) {
		this.comList = comList;
	}
	public PageEntity getPage() {
		return page;
	}
	public void setPage(PageEntity page) {
		this.page = page;
	}
	
	
}
