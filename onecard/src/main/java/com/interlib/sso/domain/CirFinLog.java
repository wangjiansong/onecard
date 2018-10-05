package com.interlib.sso.domain;

import java.io.Serializable;
import java.util.Date;

import com.interlib.sso.page.PageEntity;

public class CirFinLog implements Serializable {

	private Integer id;
	private String rdid;
	private String feetype;
	private Double fee;
	private Date regtime;
	private String regman;
	private String regLib;
	private String orgLib;
	private Integer paySign;
	private String feeMemo;
	private String tranId;
	private String feeAppCode;
	private Integer paytype;
	private String payId;

	//做时间段查询用的
	private String searchKey;
	private String searchValue;
	private String rdname;
//	private Date startTime;
//	private Date endTime;
	private String startTime;
	private String endTime;
	private String startRdid;
	private String endRdid;
	//用于按日、月、年统计 DAY MONTH YEAR
	private String dateFormat = "DAY";
	
	private String reglibSet;//自动统计
	
	private String totalfeeSet;//自动统计
	
	private double sumFee;//自动统计
	
	private Integer isReturn;//是否消费标识 1是已经消费或者是退款 20140707
	
	public Integer getIsReturn() {
		return isReturn;
	}
	public void setIsReturn(Integer isReturn) {
		this.isReturn = isReturn;
	}
	public double getSumFee() {
		return sumFee;
	}
	public void setSumFee(double sumFee) {
		this.sumFee = sumFee;
	}
	public String getReglibSet() {
		return reglibSet;
	}
	public void setReglibSet(String reglibSet) {
		this.reglibSet = reglibSet;
	}
	public String getTotalfeeSet() {
		return totalfeeSet;
	}
	public void setTotalfeeSet(String totalfeeSet) {
		this.totalfeeSet = totalfeeSet;
	}
	
	public String getDateFormat() {
		return dateFormat;
	}
	public void setDateFormat(String dateFormat) {
		this.dateFormat = dateFormat;
	}
	private PageEntity page;
	
	//ADD by pyx 2014-02-28  总的统计的结果
	private Long rdidSum;
	private Long rdidCount;
	private Double feeSum;
	
	//ADD BY PYX 20140428 
	private Integer groupID;
	
	private String[] feetypes;// add by pyx 2014-05-06 存放查询查询条件in里面的数据
	
	private Integer firstResult;
	
	private Integer maxResult;
	
	public Integer getFirstResult() {
		return firstResult;
	}
	public void setFirstResult(Integer firstResult) {
		this.firstResult = firstResult;
	}
	public Integer getMaxResult() {
		return maxResult;
	}
	public void setMaxResult(Integer maxResult) {
		this.maxResult = maxResult;
	}
	public String[] getFeetypes() {
		return feetypes;
	}
	public void setFeetypes(String[] feetypes) {
		this.feetypes = feetypes;
	}
	public Integer getGroupID() {
		return groupID;
	}
		
	public void setGroupID(Integer groupID) {
		this.groupID = groupID;
	}
	private Double cost;//ADD by pyx 20140321 消费原价
	
	public Double getCost() {
		return cost;
	}
	public void setCost(Double cost) {
		this.cost = cost;
	}
	public Long getRdidSum() {
		return rdidSum;
	}
	public void setRdidSum(Long rdidSum) {
		this.rdidSum = rdidSum;
	}
	public Long getRdidCount() {
		return rdidCount;
	}
	public void setRdidCount(Long rdidCount) {
		this.rdidCount = rdidCount;
	}
	
	public Double getFeeSum() {
		return feeSum;
	}
	public void setFeeSum(Double feeSum) {
		this.feeSum = feeSum;
	}
	public PageEntity getPage() {
		return page;
	}
	public void setPage(PageEntity page) {
		this.page = page;
	}
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getRdid() {
		return rdid;
	}
	public void setRdid(String rdid) {
		this.rdid = rdid;
	}
	public String getFeetype() {
		return feetype;
	}
	public void setFeetype(String feetype) {
		this.feetype = feetype;
	}
	public Double getFee() {
		return fee;
	}
	public void setFee(Double fee) {
		this.fee = fee;
	}
	
	public Date getRegtime() {
		return regtime;
	}
	public void setRegtime(Date regtime) {
		this.regtime = regtime;
	}
	public String getRegman() {
		return regman;
	}
	public void setRegman(String regman) {
		this.regman = regman;
	}
	public String getRegLib() {
		return regLib;
	}
	public void setRegLib(String regLib) {
		this.regLib = regLib;
	}
	public String getOrgLib() {
		return orgLib;
	}
	public void setOrgLib(String orgLib) {
		this.orgLib = orgLib;
	}
	public Integer getPaySign() {
		return paySign;
	}
	public void setPaySign(Integer paySign) {
		this.paySign = paySign;
	}
	public String getFeeMemo() {
		return feeMemo;
	}
	public void setFeeMemo(String feeMemo) {
		this.feeMemo = feeMemo;
	}
	public String getTranId() {
		return tranId;
	}
	public void setTranId(String tranId) {
		this.tranId = tranId;
	}
	public String getFeeAppCode() {
		return feeAppCode;
	}
	public void setFeeAppCode(String feeAppCode) {
		this.feeAppCode = feeAppCode;
	}
//	public Date getStartTime() {
//		return startTime;
//	}
//	public void setStartTime(Date startTime) {
//		this.startTime = startTime;
//	}
//	public Date getEndTime() {
//		return endTime;
//	}
//	public void setEndTime(Date endTime) {
//		this.endTime = endTime;
//	}
	
	public String getRdname() {
		return rdname;
	}
	public String getSearchKey() {
		return searchKey;
	}
	public void setSearchKey(String searchKey) {
		this.searchKey = searchKey;
	}
	public String getSearchValue() {
		return searchValue;
	}
	public void setSearchValue(String searchValue) {
		this.searchValue = searchValue;
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
	public void setRdname(String rdname) {
		this.rdname = rdname;
	}
	public Integer getPaytype() {
		return paytype;
	}
	public void setPaytype(Integer paytype) {
		this.paytype = paytype;
	}
	public String getStartRdid() {
		return startRdid;
	}
	public void setStartRdid(String startRdid) {
		this.startRdid = startRdid;
	}
	public String getEndRdid() {
		return endRdid;
	}
	public void setEndRdid(String endRdid) {
		this.endRdid = endRdid;
	}
	public String getPayId() {
		return payId;
	}
	public void setPayId(String payId) {
		this.payId = payId;
	}
	
	
}
