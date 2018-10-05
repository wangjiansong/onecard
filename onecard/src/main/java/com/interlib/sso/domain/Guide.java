package com.interlib.sso.domain;

import java.io.Serializable;
import java.util.Date;

import com.interlib.sso.page.PageEntity;

public class Guide implements Serializable{
	private long id;
	private String title;
	private String content;
	private Date createtime;
	
	private PageEntity page;

	
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Date getCreatetime() {
		return createtime;
	}

	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}

	public PageEntity getPage() {
		return page;
	}

	public void setPage(PageEntity page) {
		this.page = page;
	}

	
	

}
