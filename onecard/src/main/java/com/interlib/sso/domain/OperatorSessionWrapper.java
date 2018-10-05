package com.interlib.sso.domain;

import java.io.Serializable;

public class OperatorSessionWrapper implements Serializable {
	private static final long serialVersionUID = 7787146682864907231L;
	
	private Operator operator;
	private String libName;
	private String competNo;
	private String ip;
	
	public Operator getOperator() {
		return operator;
	}
	public void setOperator(Operator operator) {
		this.operator = operator;
	}
	public String getLibName() {
		return libName;
	}
	public void setLibName(String libName) {
		this.libName = libName;
	}
	public String getCompetNo() {
		return competNo;
	}
	public void setCompetNo(String competNo) {
		this.competNo = competNo;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	
}
