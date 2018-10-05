package com.interlib.sso.domain;

import java.io.Serializable;
import java.util.List;

import com.interlib.sso.page.PageEntity;

/**
 * 权限
 * @author Home
 *
 */
public class Compets implements Serializable{

	/**
	 * 权限
	 */
	private static final long serialVersionUID = 1972257483287384592L;

	private Integer competId;//主键ID
	
	private String competName;//权限名称
	
	private String describe;//描述
	
	private List<Resources> resList;//资源列表
	
	private PageEntity page;//分页对象

	public Integer getCompetId() {
		return competId;
	}

	public void setCompetId(Integer competId) {
		this.competId = competId;
	}

	public String getCompetName() {
		return competName;
	}

	public void setCompetName(String competName) {
		this.competName = competName;
	}

	public String getDescribe() {
		return describe;
	}

	public void setDescribe(String describe) {
		this.describe = describe;
	}

	public List<Resources> getResList() {
		return resList;
	}

	public void setResList(List<Resources> resList) {
		this.resList = resList;
	}

	public PageEntity getPage() {
		return page;
	}

	public void setPage(PageEntity page) {
		this.page = page;
	}
	
}
