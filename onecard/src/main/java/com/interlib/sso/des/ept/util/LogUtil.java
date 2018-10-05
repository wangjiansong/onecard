package com.interlib.sso.des.ept.util;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;

public class LogUtil {
	
	private static String logUrl;
	private static FileWriter fileWriter;
	private static BufferedWriter bufferedWriter;
	private static PrintWriter printWriter;
	
	static {
		logUrl = PropertyUtil.getProp().getProperty("logUrl");
	}
	
	public static void openWriter() {
		try {
			fileWriter = new FileWriter(logUrl, true);
			bufferedWriter = new BufferedWriter(fileWriter);
			printWriter = new PrintWriter(bufferedWriter);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void printlnContent(String content) {
		printWriter.println(content);
	}
	
	public static void printlnException(Exception e) {
		printCurrentTime();
		e.printStackTrace(printWriter);
	}
	
	private static void printCurrentTime() {
		printlnContent(new Timestamp(System.currentTimeMillis()).toString());
	}
	
	public static void closeWriter() {
		try {
			printWriter.close();
			bufferedWriter.close();
			fileWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
