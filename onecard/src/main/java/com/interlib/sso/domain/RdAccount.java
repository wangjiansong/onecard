package com.interlib.sso.domain;

import java.io.Serializable;

import com.interlib.sso.page.PageEntity;

public class RdAccount implements Serializable {

	private String rdid;
	private String rdname;
	private Double deposit; //押金
	private Double prepay; //预付款, 现在系统是用这个当做预付款
	private Double arrearage; //欠款
	private Double spDeposit;//专项押金
	private Double onecard; //一卡通余额
	private Integer status;//账户状态  冻结、有效etc
	private Double coupon;//代金券
	private Integer groupId;//分组ID
	
	private PageEntity page;
	
	public String getRdid() {
		return rdid;
	}
	public void setRdid(String rdid) {
		this.rdid = rdid;
	}
	
	public String getRdname() {
		return rdname;
	}
	public void setRdname(String rdname) {
		this.rdname = rdname;
	}
	public Double getDeposit() {
		return deposit;
	}
	public void setDeposit(Double deposit) {
		this.deposit = deposit;
	}
	public Double getPrepay() {
		return prepay;
	}
	public void setPrepay(Double prepay) {
		this.prepay = prepay;
	}
	public Double getArrearage() {
		return arrearage;
	}
	public void setArrearage(Double arrearage) {
		this.arrearage = arrearage;
	}
	public Double getSpDeposit() {
		return spDeposit;
	}
	public void setSpDeposit(Double spDeposit) {
		this.spDeposit = spDeposit;
	}
	public Double getOnecard() {
		return onecard;
	}
	public void setOnecard(Double onecard) {
		this.onecard = onecard;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public PageEntity getPage() {
		return page;
	}
	public void setPage(PageEntity page) {
		this.page = page;
	}
	public Integer getGroupId() {
		return groupId;
	}
	public void setGroupId(Integer groupId) {
		this.groupId = groupId;
	}
//	public Double getCoupon() {
//		return coupon;
//	}
//	public void setCoupon(Double coupon) {
//		this.coupon = coupon;
//	}
}
