package com.interlib.sso.domain;

import java.io.Serializable;

/**
 * 刷卡消费规则明细
 * @author Lullaby
 *
 */
public class CardRuleDetail implements Serializable {

	private static final long serialVersionUID = 5427562961226142964L;
	
	private Integer ruleId;//规则ID
	
	private String startTime;//开始时间
	
	private String endTime;//结束时间
	
	private Float salePrice;//折扣价
	
	private Float costPrice;//成本价
	
	private Byte timeFlag;//时段标识

	public Integer getRuleId() {
		return ruleId;
	}

	public void setRuleId(Integer ruleId) {
		this.ruleId = ruleId;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public Float getSalePrice() {
		return salePrice;
	}

	public void setSalePrice(Float salePrice) {
		this.salePrice = salePrice;
	}

	public Float getCostPrice() {
		return costPrice;
	}

	public void setCostPrice(Float costPrice) {
		this.costPrice = costPrice;
	}
	
	public Byte getTimeFlag() {
		return timeFlag;
	}

	public void setTimeFlag(Byte timeFlag) {
		this.timeFlag = timeFlag;
	}
	
}
