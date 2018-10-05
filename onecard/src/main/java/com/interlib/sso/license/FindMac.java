/*
 * FindMac 1.0.6 2005-12-1
 * Copyright 2002 Tuchang, Inc. All rights reserved.
 * 创建日期 2005-12-1
 *
 * TODO 要更改此生成的文件的模板，请转至
 * 窗口 － 首选项 － Java － 代码样式 － 代码模板
 */
package com.interlib.sso.license;

/**
 * @author toofu
 * @reviewer toofu
 * @version 1.0, 2003-9-12
 * @env JDK1.4.1
 * @modified toofu, 2005-12-1 TODO 要更改此生成的类型注释的模板，请转至 窗口 － 首选项 － Java － 代码样式 －
 *           代码模板
 */

/**
 * try to determine MAC address of local network card; this is done using a
 * shell to run ifconfig (linux) or ipconfig (windows). The output of the
 * processes will be parsed.
 * 
 * <p>
 * 
 * To run the whole thing, just type java FindMac
 * 
 * <p>
 * 
 * Current restrictions:
 * 
 * <ul>
 * <li>Will probably not run in applets
 * 
 * <li>Tested Windows / Linux / Mac OSX only
 * 
 * <li>Tested J2SDK 1.4 only
 * 
 * <li>If a computer has more than one network adapters, only one MAC address
 * will be returned
 * 
 * <li>will not run if user does not have permissions to run ifconfig/ipconfig
 * (e.g. under linux this is typically only permitted for root)
 * </ul>
 */

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.io.InputStream;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.text.ParseException;
import java.util.Enumeration;
import java.util.StringTokenizer;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public final class FindMac {
	public final static String getMacAddress() {
		String mac = "";
		String os = System.getProperty("os.name");

		try {
			if (os.startsWith("Windows")) {
				mac = windowsParseMacAddress(windowsRunIpConfigCommand());
			} else if (os.startsWith("Linux")) {
				// mac = linuxParseMacAddress(linuxRunIfConfigCommand());
				mac = getLinuxMac();
			} else if (os.startsWith("Mac OS X")) {
				mac = osxParseMacAddress(osxRunIfConfigCommand());
			}
		} catch (Exception ex) {
			ex.printStackTrace();

		}
		return mac;
	}

	/*
	 * Linux stuff
	 */

	private final static String linuxParseMacAddress(String ipConfigResponse)
			throws ParseException {
		String localHost = null;
		try {
			localHost = InetAddress.getLocalHost().getHostAddress();
		} catch (java.net.UnknownHostException ex) {
			ex.printStackTrace();
			throw new ParseException(ex.getMessage(), 0);
		}

		StringTokenizer tokenizer = new StringTokenizer(ipConfigResponse, "\n");
		String lastMacAddress = null;

		while (tokenizer.hasMoreTokens()) {
			String line = tokenizer.nextToken().trim();
			boolean containsLocalHost = line.indexOf(localHost) >= 0;

			// see if line contains IP address
			if (containsLocalHost && lastMacAddress != null) {
				return lastMacAddress;
			}

			// see if line contains MAC address
			int macAddressPosition = line.indexOf("HWaddr");
			if (macAddressPosition <= 0)
				continue;

			String macAddressCandidate = line.substring(macAddressPosition + 6)
					.trim();
			if (linuxIsMacAddress(macAddressCandidate)) {
				lastMacAddress = macAddressCandidate;
				continue;
			}
		}

		ParseException ex = new ParseException("cannot read MAC address for "
				+ localHost + " from [" + ipConfigResponse + "]", 0);
		ex.printStackTrace();
		throw ex;
	}

	private final static boolean linuxIsMacAddress(String macAddressCandidate) {
		Pattern macPattern = Pattern
				.compile("[0-9a-fA-F]{2}[-:][0-9a-fA-F]{2}[-:][0-9a-fA-F]{2}[-:][0-9a-fA-F]{2}[-:][0 -9a-fA-F]{2}[-:][0-9a-fA-F]{2}");
		Matcher m = macPattern.matcher(macAddressCandidate);
		return m.matches();
	}

	private final static String linuxRunIfConfigCommand() throws IOException {
		Process p = Runtime.getRuntime().exec("ifconfig");
		InputStream stdoutStream = new BufferedInputStream(p.getInputStream());

		StringBuffer buffer = new StringBuffer();
		for (;;) {
			int c = stdoutStream.read();
			if (c == -1)
				break;
			buffer.append((char) c);
		}
		String outputText = buffer.toString();

		stdoutStream.close();

		return outputText;
	}

	/*
	 * Windows stuff
	 */
	private final static String windowsParseMacAddress(String ipConfigResponse)
			throws ParseException {
		String localHost = null;
		try {
			localHost = InetAddress.getLocalHost().getHostAddress();
		} catch (java.net.UnknownHostException ex) {
			ex.printStackTrace();
			throw new ParseException(ex.getMessage(), 0);
		}
		StringTokenizer tokenizer = new StringTokenizer(ipConfigResponse, "\n");
		String lastMacAddress = null;
		while (tokenizer.hasMoreTokens()) {
			String line = tokenizer.nextToken().trim();
			// see if line contains IP address
			if (line.endsWith(localHost) && lastMacAddress != null) {
				return lastMacAddress;
			}
			// see if line contains MAC address
			int macAddressPosition = line.indexOf(":");
			if (macAddressPosition <= 0)
				continue;

			String macAddressCandidate = line.substring(macAddressPosition + 1)
					.trim();
			if (windowsIsMacAddress(macAddressCandidate)) {
				lastMacAddress = macAddressCandidate;
				continue;
			}
		}
		if (lastMacAddress != null && !lastMacAddress.equals("")) {// 说明取到了一个网卡地址，而且是最后一个，这里可以使用则返回。
			return lastMacAddress;
		}

		ParseException ex = new ParseException("cannot read MAC address from ["
				+ ipConfigResponse + "]", 0);
		ex.printStackTrace();
		throw ex;
	}

	private final static boolean windowsIsMacAddress(String macAddressCandidate) {
		if (macAddressCandidate.equals(""))
			return false;
		Pattern macPattern = Pattern
				.compile("[0-9a-fA-F]{2}[-:][0-9a-fA-F]{2}[-:][0-9a-fA-F]{2}[-:][0-9a-fA-F]{2}[-:][0 -9a-fA-F]{2}[-:][0-9a-fA-F]{2}");
		Matcher m = macPattern.matcher(macAddressCandidate);
		return m.matches();
	}

	private final static String windowsRunIpConfigCommand() throws IOException {
		Process p = Runtime.getRuntime().exec("ipconfig /all");
		InputStream stdoutStream = new BufferedInputStream(p.getInputStream());

		StringBuffer buffer = new StringBuffer();
		for (;;) {
			int c = stdoutStream.read();
			if (c == -1)
				break;
			buffer.append((char) c);
		}
		String outputText = buffer.toString();

		stdoutStream.close();

		return outputText;
	}

	/*
	 * Mac OS X Stuff
	 */
	private final static String osxParseMacAddress(String ipConfigResponse)
			throws ParseException {
		String localHost = null;

		try {
			localHost = InetAddress.getLocalHost().getHostAddress();
		} catch (java.net.UnknownHostException ex) {
			ex.printStackTrace();
			throw new ParseException(ex.getMessage(), 0);
		}

		StringTokenizer tokenizer = new StringTokenizer(ipConfigResponse, "\n");

		while (tokenizer.hasMoreTokens()) {
			String line = tokenizer.nextToken().trim();
			boolean containsLocalHost = line.indexOf(localHost) >= 0;
			// see if line contains MAC address
			int macAddressPosition = line.indexOf("ether");
			if (macAddressPosition != 0)
				continue;
			String macAddressCandidate = line.substring(macAddressPosition + 6)
					.trim();
			if (osxIsMacAddress(macAddressCandidate)) {
				return macAddressCandidate;
			}
		}

		ParseException ex = new ParseException("cannot read MAC address for "
				+ localHost + " from [" + ipConfigResponse + "]", 0);
		ex.printStackTrace();
		throw ex;
	}

	private final static boolean osxIsMacAddress(String macAddressCandidate) {
		Pattern macPattern = Pattern
				.compile("[0-9a-fA-F]{2}[-:][0-9a-fA-F]{2}[-:][0-9a-fA-F]{2}[-:][0-9a-fA-F]{2}[-:][0 -9a-fA-F]{2}[-:][0-9a-fA-F]{2}");
		Matcher m = macPattern.matcher(macAddressCandidate);
		return m.matches();
	}

	private final static String osxRunIfConfigCommand() throws IOException {
		Process p = Runtime.getRuntime().exec("ifconfig");
		InputStream stdoutStream = new BufferedInputStream(p.getInputStream());
		StringBuffer buffer = new StringBuffer();
		for (;;) {
			int c = stdoutStream.read();
			if (c == -1)
				break;
			buffer.append((char) c);
		}
		String outputText = buffer.toString();
		stdoutStream.close();
		return outputText;
	}

	public final static String hexByte(byte b) {
		String s = "000000" + Integer.toHexString(b);
		return s.substring(s.length() - 2);
	}

	// 获取linux MAC地址的方法
	public final static String getLinuxMac() {
		try {
			Enumeration<NetworkInterface> el = NetworkInterface
					.getNetworkInterfaces();
			StringBuilder builder = new StringBuilder();
			while (el.hasMoreElements()) {
				byte[] mac = el.nextElement().getHardwareAddress();
				for (byte b : mac) {
					builder.append(hexByte(b));
					builder.append("-");
				}
				if (builder.length() > 0) {

					builder.deleteCharAt(builder.length() - 1);
				}
			}
			return builder.toString().toUpperCase();
		} catch (Exception exception) {
			exception.printStackTrace();
			return "";
		}
	}

	/*
	 * Main
	 */
	public final static void main(String[] args) {
		try {
			System.out.println("Network info:");

			System.out.println("  Operating System: "
					+ System.getProperty("os.name"));
			System.out.println("  HostName: " + InetAddress.getLocalHost());
			System.out.println("  IP/Localhost: "
					+ InetAddress.getLocalHost().getHostAddress());
			System.out.println("  MAC Address: " + getMacAddress());
			// System.out.println(System.getProperties());//user.dir;os.name

			// String hid =
			// MacAddress.getMacAddress().getFirstMacAddress().toLowerCase();
			// System.out.println(hid);

		} catch (Throwable t) {
			t.printStackTrace();
		}
	}
}
