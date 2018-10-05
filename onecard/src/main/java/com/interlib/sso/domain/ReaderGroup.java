package com.interlib.sso.domain;

import java.io.Serializable;
import java.sql.Date;

import com.interlib.sso.page.PageEntity;

/**
 * 用户分组(补助发放)
 * @author Lullaby
 *
 */
public class ReaderGroup implements Serializable {

	/**
	 * 用户分组
	 */
	private static final long serialVersionUID = 2995997699050850602L;
	
	private Integer groupId;//分组ID
	
	private String groupName;//分组名称
	
	private Date createTime;//创建时间
	
	private String remark;//备注
	
	private PageEntity page;//分页对象
	
	private Integer grantId;//对应补助发放ID

	public Integer getGroupId() {
		return groupId;
	}

	public void setGroupId(Integer groupId) {
		this.groupId = groupId;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	public PageEntity getPage() {
		return page;
	}

	public void setPage(PageEntity page) {
		this.page = page;
	}
	
	public Integer getGrantId() {
		return grantId;
	}

	public void setGrantId(Integer grantId) {
		this.grantId = grantId;
	}
	
}
