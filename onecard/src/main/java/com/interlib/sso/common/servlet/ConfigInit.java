package com.interlib.sso.common.servlet;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import javax.servlet.ServletConfig;
import javax.servlet.http.HttpServlet;

import org.apache.log4j.Logger;

public class ConfigInit extends HttpServlet {

	public static Logger logger = Logger.getLogger(ConfigInit.class);
	public static Properties prop = new Properties();
	
	/**
	 * 系统参数配置
	 */
	private static final long serialVersionUID = -4309631856586355132L;
	
	public void init(ServletConfig config) {
		String prefix = config.getServletContext().getRealPath("/");
		String file = config.getInitParameter("config");
		String filepath = prefix + file;
		
		try {
			FileInputStream instream = new FileInputStream(filepath);
			prop.load(instream);
			instream.close();
			
			logger.info("系统参数配置初始化完成！");
		} catch (FileNotFoundException e) {
			logger.error("系统参数配置文件未找到！", e);
		} catch (IOException e) {
			logger.error("读取系统参数配置文件出错！", e);
		}
	}

}
