package com.interlib.sso.updatedb;

import java.io.FileInputStream;
import java.util.Properties;
import java.util.Date;

public class OracleDataBaseInfo {
	private static String HostName = "";
	private static String SID = "";
	private static String Port = "";
	private static String UserName = "";
	private static String Password = "";
	private static String Url = "";// 新添加的支持RAC 用来进行Url配置连接

	public static String trimToEmpty(String s) {
		return s == null ? "" : s.trim();
	}

	public OracleDataBaseInfo(String connFileName) {
		Properties props = new Properties();
		try {
			props.load(new FileInputStream(connFileName));
			HostName = trimToEmpty(props.getProperty("HostName"));
			SID = trimToEmpty(props.getProperty("SID"));
			Port = trimToEmpty(props.getProperty("Port"));
			UserName = trimToEmpty(props.getProperty("jdbc.username"));
			Password = trimToEmpty(props.getProperty("jdbc.password"));
			Url = trimToEmpty(props.getProperty("jdbc.url"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void setHostName(String HostName) {
		this.HostName = HostName;
	}

	public String getHostName() {
		return HostName;
	}

	public void setSID(String SID) {
		this.SID = SID;
	}

	public String getSID() {
		return SID;
	}

	public void setPort(String Port) {
		this.Port = Port;
	}

	public String getPort() {
		return Port;
	}

	public void setUserName(String UserName) {
		this.Port = UserName;
	}

	public String getUserName() {
		return UserName;
	}

	public void setPassword(String Password) {
		this.Password = Password;
	}

	public String getPassword() {
		return Password;
	}

	public void setUrl(String url) {
		this.Url = url;
	}

	public String getUrl() {
		return this.Url;
	}

	public String toString() {
		return "HostName=" + getHostName() + ";SID=" + getSID() + ";Port="
				+ getPort() + ";UserName=" + getUserName() + ";Password="
				+ getPassword() + ";Url=" + getUrl() + ";";
	}

	public static void main(String[] args) {
		OracleDataBaseInfo dbInfo = new OracleDataBaseInfo("jdbc.properties");
		System.out.println(dbInfo.toString());

	}
}