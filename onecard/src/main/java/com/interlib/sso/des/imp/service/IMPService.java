package com.interlib.sso.des.imp.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

public class IMPService {
	
	private static final Properties prop = new Properties();
	
	public void importDMP() {
		if (checkProp()) {
			StringBuffer importCommand = new StringBuffer();
			importCommand.
				append("imp ").
				append(prop.get("jdbc.username")).
				append("/").
				append(prop.get("jdbc.password")).
				append("@").
				append(prop.get("jdbc.instance")).
				append(" ignore=y").
				append(" file=").
				append(prop.get("importUrl")).
				append(" tables=(").
				append(prop.get("jdbc.tablename")).
				append(")").
				append(" log=").
				append(prop.get("logUrl"));
			
//			String importCommand = "imp onecard/interlib@orcl ignore=y file=e:\reader_test1.dmp tables=(READER) log=e:\import_test1.log";
			String[] cmds = new String[3];
			cmds[0] = "cmd";
			cmds[1] = "/c";
			cmds[2] = importCommand.toString();
			
			System.out.println(importCommand.toString());
			doExcuteCommand(importCommand.toString());
		}
	}
	
	private boolean checkProp() {
		String username = prop.getProperty("jdbc.username");
		if (username == null || "".equals(username)) {
			System.out.println("数据库用户名为空！");
			return false;
		}
		String password = prop.getProperty("jdbc.password");
		if (password == null || "".equals(password)) {
			System.out.println("数据库密码为空！");
			return false;
		}
		String instance = prop.getProperty("jdbc.instance");
		if (instance == null || "".equals(instance)) {
			System.out.println("数据库实例名为空！");
			return false;
		}
		String tablename = prop.getProperty("jdbc.tablename");
		if (tablename == null || "".equals(tablename)) {
			System.out.println("导入表名为空！");
			return false;
		}
		String importUrl = prop.getProperty("importUrl");
		if (importUrl == null || "".equals(importUrl)) {
			System.out.println("文件导入路径为空！");
			return false;
		}
		String logUrl = prop.getProperty("logUrl");
		if (logUrl == null || "".equals(logUrl)) {
			System.out.println("日志导出路径为空！");
			return false;
		}
		
		return true;
	}
	
	private void doExcuteCommand(String command) {
		Process process = null;
		try {
			process = Runtime.getRuntime().exec(command);
		} catch (IOException e) {
			e.printStackTrace();
		}
		boolean shouldClose = false;
		try {
			InputStreamReader isr = new InputStreamReader(
					process.getErrorStream(),"GBK");
			BufferedReader br = new BufferedReader(isr);
			String line = null;
			while ((line = br.readLine()) != null) {
				System.out.println(line);
				if (line.indexOf("????") != -1) {
					shouldClose = true;
					break;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (shouldClose) {
			process.destroy();
		}
		try {
			process.waitFor();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public static Properties getProp() {
		return prop;
	}

}
