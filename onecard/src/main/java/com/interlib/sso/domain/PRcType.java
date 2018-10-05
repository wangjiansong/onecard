package com.interlib.sso.domain;

import java.io.Serializable;
import java.util.Date;

/**
 * 读者类型相应的权限，参考表p_rctype
 * @author shun
 */
public class PRcType implements Serializable{

	private static final long serialVersionUID = 1L;

	private Integer id;
	private String readerType;
	private String libcode;
	private String descripe;
	private int sign;
	private int libSign;
	private int valDate;
	private Date startDate;
	private int score;
	private double deposity;
	private double checkfee;
	private double servicefee;
	private double idfee;
	private int borrowNum;
	private int maxResNum;
	private int resDateNum;
	private int maxLibRes;
	private int precCancyNum;
	private int overDateBorrow;
	private int resSign;
	private int prelendSign;
	private int prelendDate;
	private int bookCanloanOpac;
	private int seriesCanloanOpac;
	private int cdCanloanOpac;
	
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public int getPrecCancyNum() {
		return precCancyNum;
	}
	public void setPrecCancyNum(int precCancyNum) {
		this.precCancyNum = precCancyNum;
	}
	public String getReaderType() {
		return readerType;
	}
	public void setReaderType(String readerType) {
		this.readerType = readerType;
	}
	public String getLibcode() {
		return libcode;
	}
	public void setLibcode(String libcode) {
		this.libcode = libcode;
	}
	public String getDescripe() {
		return descripe;
	}
	public void setDescripe(String descripe) {
		this.descripe = descripe;
	}
	public int getSign() {
		return sign;
	}
	public void setSign(int sign) {
		this.sign = sign;
	}
	public int getLibSign() {
		return libSign;
	}
	public void setLibSign(int libSign) {
		this.libSign = libSign;
	}
	public int getValDate() {
		return valDate;
	}
	public void setValDate(int valDate) {
		this.valDate = valDate;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public int getScore() {
		return score;
	}
	public void setScore(int score) {
		this.score = score;
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
	public int getBorrowNum() {
		return borrowNum;
	}
	public void setBorrowNum(int borrowNum) {
		this.borrowNum = borrowNum;
	}
	public int getMaxResNum() {
		return maxResNum;
	}
	public void setMaxResNum(int maxResNum) {
		this.maxResNum = maxResNum;
	}
	public int getResDateNum() {
		return resDateNum;
	}
	public void setResDateNum(int resDateNum) {
		this.resDateNum = resDateNum;
	}
	public int getMaxLibRes() {
		return maxLibRes;
	}
	public void setMaxLibRes(int maxLibRes) {
		this.maxLibRes = maxLibRes;
	}
	public int getPecCancyNum() {
		return precCancyNum;
	}
	public void setPecCancyNum(int precCancyNum) {
		this.precCancyNum = precCancyNum;
	}
	public int getOverDateBorrow() {
		return overDateBorrow;
	}
	public void setOverDateBorrow(int overDateBorrow) {
		this.overDateBorrow = overDateBorrow;
	}
	public int getResSign() {
		return resSign;
	}
	public void setResSign(int resSign) {
		this.resSign = resSign;
	}
	public int getPrelendSign() {
		return prelendSign;
	}
	public void setPrelendSign(int prelendSign) {
		this.prelendSign = prelendSign;
	}
	public int getPrelendDate() {
		return prelendDate;
	}
	public void setPrelendDate(int prelendDate) {
		this.prelendDate = prelendDate;
	}
	public int getBookCanloanOpac() {
		return bookCanloanOpac;
	}
	public void setBookCanloanOpac(int bookCanloanOpac) {
		this.bookCanloanOpac = bookCanloanOpac;
	}
	public int getSeriesCanloanOpac() {
		return seriesCanloanOpac;
	}
	public void setSeriesCanloanOpac(int seriesCanloanOpac) {
		this.seriesCanloanOpac = seriesCanloanOpac;
	}
	public int getCdCanloanOpac() {
		return cdCanloanOpac;
	}
	public void setCdCanloanOpac(int cdCanloanOpac) {
		this.cdCanloanOpac = cdCanloanOpac;
	}

}
