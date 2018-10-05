package com.interlib.sso.domain;

import java.io.Serializable;
import java.util.Date;

public class ReaderCardInfo implements Serializable {
	
	private String rdId;
	private String cardId;
	private String cardType;
	private Date transactTime;
	private Date lastUseTime;
	private int isUsable;
	private int totalOfUsed;
	public String getRdId() {
		return rdId;
	}
	public void setRdId(String rdId) {
		this.rdId = rdId;
	}
	public String getCardId() {
		return cardId;
	}
	public void setCardId(String cardId) {
		this.cardId = cardId;
	}
	public String getCardType() {
		return cardType;
	}
	public void setCardType(String cardType) {
		this.cardType = cardType;
	}
	public Date getTransactTime() {
		return transactTime;
	}
	public void setTransactTime(Date transactTime) {
		this.transactTime = transactTime;
	}
	public Date getLastUseTime() {
		return lastUseTime;
	}
	public void setLastUseTime(Date lastUseTime) {
		this.lastUseTime = lastUseTime;
	}
	public int getIsUsable() {
		return isUsable;
	}
	public void setIsUsable(int isUsable) {
		this.isUsable = isUsable;
	}
	public int getTotalOfUsed() {
		return totalOfUsed;
	}
	public void setTotalOfUsed(int totalOfUsed) {
		this.totalOfUsed = totalOfUsed;
	}
	

}
