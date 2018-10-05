package com.interlib.sso.domain;

import java.io.Serializable;

public class FinSettlementResult implements Serializable {
	
	private String day;
	
	private String sumFee;
	
	private String morningSale;
	
	private String morningCost;
	
	private String noonSale;
	
	private String noonCost;
	
	private String nightSale;
	
	private String nightCost;
	
	private String otherFee;
	
	private String daySale;
	
	private String dayCost;
	
	private Integer groupSign;
	
	private String sumCost;//ADD 20140321
	
	private String sumRepairCost;//ADD 20140321
	
	
	public String getSumCost() {
		return sumCost;
	}

	public void setSumCost(String sumCost) {
		this.sumCost = sumCost;
	}

	public String getSumRepairCost() {
		return sumRepairCost;
	}

	public void setSumRepairCost(String sumRepairCost) {
		this.sumRepairCost = sumRepairCost;
	}

	public String getDay() {
		return day;
	}

	public void setDay(String day) {
		this.day = day;
	}

	public String getSumFee() {
		return sumFee;
	}

	public void setSumFee(String sumFee) {
		this.sumFee = sumFee;
	}

	
	public String getMorningSale() {
		return morningSale;
	}

	public void setMorningSale(String morningSale) {
		this.morningSale = morningSale;
	}

	public String getMorningCost() {
		return morningCost;
	}

	public void setMorningCost(String morningCost) {
		this.morningCost = morningCost;
	}

	public String getNoonSale() {
		return noonSale;
	}

	public void setNoonSale(String noonSale) {
		this.noonSale = noonSale;
	}

	public String getNoonCost() {
		return noonCost;
	}

	public void setNoonCost(String noonCost) {
		this.noonCost = noonCost;
	}

	public String getNightSale() {
		return nightSale;
	}

	public void setNightSale(String nightSale) {
		this.nightSale = nightSale;
	}

	public String getNightCost() {
		return nightCost;
	}

	public void setNightCost(String nightCost) {
		this.nightCost = nightCost;
	}

	public String getOtherFee() {
		return otherFee;
	}

	public void setOtherFee(String otherFee) {
		this.otherFee = otherFee;
	}

	public Integer getGroupSign() {
		return groupSign;
	}

	public void setGroupSign(Integer groupSign) {
		this.groupSign = groupSign;
	}

	public String getDaySale() {
		return daySale;
	}

	public void setDaySale(String daySale) {
		this.daySale = daySale;
	}

	public String getDayCost() {
		return dayCost;
	}

	public void setDayCost(String dayCost) {
		this.dayCost = dayCost;
	}
	
	
}
