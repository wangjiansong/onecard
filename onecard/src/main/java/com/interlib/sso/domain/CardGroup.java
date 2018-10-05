package com.interlib.sso.domain;

import java.io.Serializable;
import java.util.Date;

import com.interlib.sso.page.PageEntity;

/**
 * 刷卡消费分组
 * @author Lullaby
 *
 */
public class CardGroup implements Serializable {

	private static final long serialVersionUID = -217214737101315881L;
	
	private Integer groupId;//分组ID
	
	private String groupName;//分组名称
	
	private Byte isAssigned;//是否已指定
	
	private Date createTime;//创建时间
	
	private String remark;//备注
	
	private PageEntity page;//分页对象

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
	
	public Byte getIsAssigned() {
		return isAssigned;
	}

	public void setIsAssigned(Byte isAssigned) {
		this.isAssigned = isAssigned;
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

}
