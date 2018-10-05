package com.interlib.sso.common;

public class CardOption {

	public static final String cardfee = "CARDFEE";
	public static final String renewfee = "RENEWFEE";
	public static final String checkfee = "CHECKFEE";
	public static final String lossfee = "LOSSFEE";
	public static final String stopfee = "STOPFEE";
	public static final String logoutfee = "LOGOUTFEE";
	public static final String quitfee = "QUITFEE";
	public static final String repairfee = "REPAIRFEE";
	public static final String deferfee = "DEFERFEE";
	public static final String changefee = "CHANGEFEE";
	
	public static String cardOption(String num) {
		if(num.equals("card")) return cardfee;
		if(num.equals("renew")) return renewfee;
		if(num.equals("check")) return checkfee;
		if(num.equals("loss")) return lossfee;
		if(num.equals("stop")) return stopfee;
		if(num.equals("logout")) return logoutfee;
		if(num.equals("quit")) return quitfee;
		if(num.equals("repair")) return repairfee;
		if(num.equals("defer")) return deferfee;
		if(num.equals("change")) return changefee;
		else return "";
	}
}
