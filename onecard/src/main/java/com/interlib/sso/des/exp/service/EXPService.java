package com.interlib.sso.des.exp.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

public class EXPService {
	
	private static final Properties prop = new Properties();
	
	public void exportDMP() {
		String username = prop.getProperty("jdbc.username");
		if (username == null || "".equals(username)) {
			System.out.println("数据库用户名为空！");
			return;
		}
		String password = prop.getProperty("jdbc.password");
		if (password == null || "".equals(password)) {
			System.out.println("数据库密码为空！");
			return;
		}
		String instance = prop.getProperty("jdbc.instance");
		if (instance == null || "".equals(instance)) {
			System.out.println("数据库实例名为空！");
			return;
		}
		String tablename = prop.getProperty("jdbc.tablename");
		if (tablename == null || "".equals(tablename)) {
			System.out.println("导出表名为空！");
			return;
		}
		String exportUrl = prop.getProperty("exportUrl");
		if (exportUrl == null || "".equals(exportUrl)) {
			System.out.println("文件导出路径为空！");
			return;
		}
		String logUrl = prop.getProperty("logUrl");
		if (logUrl == null || "".equals(logUrl)) {
			System.out.println("日志导出路径为空！");
			return;
		}
		
		StringBuffer exportCommand = new StringBuffer();
		exportCommand.
			append("exp ").
			append(username).
			append("/").
			append(password).
			append("@").
			append(instance).
			append(" file=").
			append(exportUrl).
			append(" log=").
			append(logUrl).
			append(" tables=(").
			append(tablename).
			append(")");
		
//		String exportCommand = "exp onecard/inerlib@orcl file=E:\\reader_test1.dmp log=E:\\export_test.log tables=(NETREADER)";
		String[] cmds = new String[3];
		cmds[0] = "cmd";
		cmds[1] = "/c";
		cmds[2] = exportCommand.toString();
		
		doExcuteCommand(exportCommand.toString());
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
