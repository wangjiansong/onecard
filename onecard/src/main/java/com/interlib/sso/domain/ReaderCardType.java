package com.interlib.sso.domain;

import java.io.Serializable;

public class ReaderCardType implements Serializable {
	
	private String cardType;
	private String cardName;
	public String getCardType() {
		return cardType;
	}
	public void setCardType(String cardType) {
		this.cardType = cardType;
	}
	public String getCardName() {
		return cardName;
	}
	public void setCardName(String cardName) {
		this.cardName = cardName;
	}
	
	

}
