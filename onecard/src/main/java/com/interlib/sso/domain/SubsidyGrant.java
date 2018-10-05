package com.interlib.sso.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.interlib.sso.page.PageEntity;

/**
 * 补助发放
 * @author Lullaby
 *
 */
public class SubsidyGrant implements Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 8648743455755135736L;
	
	private Integer grantId;//主键
	
	private String grantTitle;//标题

	private Float grantAmount;//发放金额
	
	private Byte isAutoGrant;//是否自动发放
	
	private Date createTime;//创建时间
	
	private Date lastModifyTime;//最近修改时间
	
	private String remark;//备注
	
	private List<Date> grantDateList;//发放时间集合
	
	private PageEntity page;//分页对象

	public Integer getGrantId() {
		return grantId;
	}

	public void setGrantId(Integer grantId) {
		this.grantId = grantId;
	}

	public String getGrantTitle() {
		return grantTitle;
	}

	public void setGrantTitle(String grantTitle) {
		this.grantTitle = grantTitle;
	}

	public Float getGrantAmount() {
		return grantAmount;
	}

	public void setGrantAmount(Float grantAmount) {
		this.grantAmount = grantAmount;
	}

	public Byte getIsAutoGrant() {
		return isAutoGrant;
	}

	public void setIsAutoGrant(Byte isAutoGrant) {
		this.isAutoGrant = isAutoGrant;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getLastModifyTime() {
		return lastModifyTime;
	}

	public void setLastModifyTime(Date lastModifyTime) {
		this.lastModifyTime = lastModifyTime;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public List<Date> getGrantDateList() {
		return grantDateList;
	}

	public void setGrantDateList(List<Date> grantDateList) {
		this.grantDateList = grantDateList;
	}

	public PageEntity getPage() {
		return page;
	}

	public void setPage(PageEntity page) {
		this.page = page;
	}

}