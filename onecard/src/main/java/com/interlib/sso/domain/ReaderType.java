package com.interlib.sso.domain;

import java.io.Serializable;

import com.interlib.sso.page.PageEntity;

public class ReaderType implements Serializable {

	/**
	 * 读者流通类型
	 */
	private static final long serialVersionUID = 1564837009642200202L;
	
	private String readerType;//读者类型代码
	
	private String libCode;//分馆代码
	
	private String descripe;//读者类型名称
	
	private byte sign;//可用标识(1：可用；0：不可用；)
	
	private byte libSign;//馆际可用标识
	
	private double deposity;
	private double checkfee;
	private double servicefee;
	private double idfee;
	private int valdate;
	
	private PageEntity page;//分页对象

	public String getReaderType() {
		return readerType;
	}

	public void setReaderType(String readerType) {
		this.readerType = readerType;
	}

	public String getLibCode() {
		return libCode;
	}

	public void setLibCode(String libCode) {
		this.libCode = libCode;
	}

	public String getDescripe() {
		return descripe;
	}

	public void setDescripe(String descripe) {
		this.descripe = descripe;
	}

	public byte getSign() {
		return sign;
	}

	public void setSign(byte sign) {
		this.sign = sign;
	}

	public byte getLibSign() {
		return libSign;
	}

	public void setLibSign(byte libSign) {
		this.libSign = libSign;
	}
	
	public double getDeposity() {
		return deposity;
	}

	public void setDeposity(double deposity) {
		this.deposity = deposity;
	}

	public double getCheckfee() {
		return checkfee;
	}

	public void setCheckfee(double checkfee) {
		this.checkfee = checkfee;
	}

	public double getServicefee() {
		return servicefee;
	}

	public void setServicefee(double servicefee) {
		this.servicefee = servicefee;
	}

	public double getIdfee() {
		return idfee;
	}

	public void setIdfee(double idfee) {
		this.idfee = idfee;
	}

	public int getValdate() {
		return valdate;
	}

	public void setValdate(int valdate) {
		this.valdate = valdate;
	}

	public PageEntity getPage() {
		return page;
	}

	public void setPage(PageEntity page) {
		this.page = page;
	}

}
