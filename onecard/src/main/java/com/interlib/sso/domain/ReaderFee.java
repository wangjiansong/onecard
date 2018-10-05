package com.interlib.sso.domain;

import java.io.Serializable;

public class ReaderFee implements Serializable {
	
	private String libcode;
	private String cardfee;
	private String renewfee;
	private String checkfee;
	private String lossfee;
	private String stopfee;
	private String logoutfee;
	private String quitfee;
	private String repairfee;
	private String deferfee;
	private String changefee;
	
	public ReaderFee() {
		
	}
	
	public ReaderFee(String libcode, String cardfee, String renewfee,
			String checkfee, String lossfee, String stopfee, String logoutfee,
			String quitfee, String repairfee, String deferfee, String changefee) {
		super();
		this.libcode = libcode;
		this.cardfee = cardfee;
		this.renewfee = renewfee;
		this.checkfee = checkfee;
		this.lossfee = lossfee;
		this.stopfee = stopfee;
		this.logoutfee = logoutfee;
		this.quitfee = quitfee;
		this.repairfee = repairfee;
		this.deferfee = deferfee;
		this.changefee = changefee;
	}
	public String getLibcode() {
		return libcode;
	}
	public void setLibcode(String libcode) {
		this.libcode = libcode;
	}
	public String getCardfee() {
		return cardfee;
	}
	public void setCardfee(String cardfee) {
		this.cardfee = cardfee;
	}
	public String getRenewfee() {
		return renewfee;
	}
	public void setRenewfee(String renewfee) {
		this.renewfee = renewfee;
	}
	public String getCheckfee() {
		return checkfee;
	}
	public void setCheckfee(String checkfee) {
		this.checkfee = checkfee;
	}
	public String getLossfee() {
		return lossfee;
	}
	public void setLossfee(String lossfee) {
		this.lossfee = lossfee;
	}
	public String getStopfee() {
		return stopfee;
	}
	public void setStopfee(String stopfee) {
		this.stopfee = stopfee;
	}
	public String getLogoutfee() {
		return logoutfee;
	}
	public void setLogoutfee(String logoutfee) {
		this.logoutfee = logoutfee;
	}
	public String getQuitfee() {
		return quitfee;
	}
	public void setQuitfee(String quitfee) {
		this.quitfee = quitfee;
	}
	public String getRepairfee() {
		return repairfee;
	}
	public void setRepairfee(String repairfee) {
		this.repairfee = repairfee;
	}
	public String getDeferfee() {
		return deferfee;
	}
	public void setDeferfee(String deferfee) {
		this.deferfee = deferfee;
	}
	public String getChangefee() {
		return changefee;
	}
	public void setChangefee(String changefee) {
		this.changefee = changefee;
	}

}
