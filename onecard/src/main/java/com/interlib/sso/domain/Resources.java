package com.interlib.sso.domain;

import java.io.Serializable;

import com.interlib.sso.page.PageEntity;

/**
 * 系统资源, 即功能啦
 * @author Home
 *
 */
public class Resources implements Serializable {

	private String resourceId;
	private String subsys;  //所属的父节点
	private String resourceName;
	private String resourceUrl;
	private Integer isMenu;   //是否菜单
	private String describe;
	
	private PageEntity page;//分页对象
	public String getResourceId() {
		return resourceId;
	}
	public void setResourceId(String resourceId) {
		this.resourceId = resourceId;
	}
	public String getSubsys() {
		return subsys;
	}
	public void setSubsys(String subsys) {
		this.subsys = subsys;
	}
	public String getResourceName() {
		return resourceName;
	}
	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
	}
	public String getResourceUrl() {
		return resourceUrl;
	}
	public void setResourceUrl(String resourceUrl) {
		this.resourceUrl = resourceUrl;
	}
	public Integer getIsMenu() {
		return isMenu;
	}
	public void setIsMenu(Integer isMenu) {
		this.isMenu = isMenu;
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
