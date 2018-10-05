package com.interlib.sso.domain;

import java.io.Serializable;

public class Num implements Serializable{
	
	/**
	 * 读者
	 */
	private static final long serialVersionUID = -8764976668238767442L;
	private String id;
	private String num;//记录证号条数
	

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNum() {
		return num;
	}

	public void setNum(String num) {
		this.num = num;
	}
	
	
	
	
}