package com.interlib.sso.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.interlib.sso.page.PageEntity;

/**
 * 刷卡消费规则
 * @author Lullaby
 *
 */
public class CardRule implements Serializable {

	private static final long serialVersionUID = 2856291937186604073L;
	
	private Integer ruleId;//主键ID
	
	private String ruleTitle;//标题
	
	private String subsidizeTimes; //增加个字段，表示每个月补助的餐数，之前是默认22次
	
	private Date createTime;//创建时间
	
	private Date lastModifyTime;//最后修改时间
	
	private String remark;//备注
	
	private PageEntity page;//分页对象
	
	private List<String> details;//时间段/价格明细
	
	private String groupName;//分组名称(做参数用)

	public Integer getRuleId() {
		return ruleId;
	}

	public void setRuleId(Integer ruleId) {
		this.ruleId = ruleId;
	}

	public String getRuleTitle() {
		return ruleTitle;
	}

	public void setRuleTitle(String ruleTitle) {
		this.ruleTitle = ruleTitle;
	}

	public String getSubsidizeTimes() {
		return subsidizeTimes;
	}

	public void setSubsidizeTimes(String subsidizeTimes) {
		this.subsidizeTimes = subsidizeTimes;
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

	public PageEntity getPage() {
		return page;
	}

	public void setPage(PageEntity page) {
		this.page = page;
	}

	public List<String> getDetails() {
		return details;
	}

	public void setDetails(List<String> details) {
		this.details = details;
	}
	
	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

}
