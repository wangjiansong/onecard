package com.interlib.sso.domain;

import java.util.ArrayList;
import java.util.List;

public class ReaderSession {

	private Reader reader;
	
	private String libName;
	
	private String competNo;
	
	private String ip;
	
	private List<Roles> rolesList = new ArrayList<Roles>();
	
	private List<Compets> competsList = new ArrayList<Compets>();
	
	private List<Resources> resourceList = new ArrayList<Resources>();

	public Reader getReader() {
		return reader;
	}

	public void setReader(Reader reader) {
		this.reader = reader;
	}

	public List<Roles> getRolesList() {
		return rolesList;
	}

	public void setRolesList(List<Roles> rolesList) {
		this.rolesList = rolesList;
	}

	public List<Compets> getCompetsList() {
		return competsList;
	}

	public void setCompetsList(List<Compets> competsList) {
		this.competsList = competsList;
	}

	public List<Resources> getResourceList() {
		return resourceList;
	}

	public void setResourceList(List<Resources> resourceList) {
		this.resourceList = resourceList;
	}
	
	//后面还可以增加需要记录读者登录之后操作的日志记录
	
}
