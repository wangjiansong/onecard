package com.interlib.sso.license;

import java.net.NetworkInterface;
import java.util.Enumeration;

/**
 * 新的获取mac地址的方法
 * @author Danniel
 *
 */
public class FindMacAddr {

	static String hexByte(byte b) {
		String s = "000000" + Integer.toHexString(b);
		return s.substring(s.length() - 2);
	}

	// 获取MAC地址的方法
	public static String getMacAddress() {
		try {
			Enumeration<NetworkInterface> el = NetworkInterface.getNetworkInterfaces();
			StringBuilder builder = new StringBuilder();
			while (el.hasMoreElements()) {
				byte[] mac = el.nextElement().getHardwareAddress();
				if(mac == null) {
					continue;
				}
				for (byte b : mac) {
					builder.append(hexByte(b));
					builder.append("-");
				}
				if(builder.length() > 0) {
					builder.deleteCharAt(builder.length() - 1);
				}
				if(builder.toString().length()>0){
					break;
				}
			}
			return builder.toString().toUpperCase();
		} catch (Exception exception) {
			exception.printStackTrace();
			return "";
		}
	}
	
	public static void main(String[] arguments) throws Exception {
		//60-EB-69-D0-82-42
		System.out.println(getMacAddress());
	}
}