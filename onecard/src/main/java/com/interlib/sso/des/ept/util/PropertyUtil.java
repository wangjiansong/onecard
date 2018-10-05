package com.interlib.sso.des.ept.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertyUtil {
	
	private static final Properties prop = new Properties();
	
	public static void loadProperty() {
		InputStream inputStream = null;
		inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("config.properties");
		try {
			prop.load(inputStream);
		} catch (IOException e) {
			System.out.println("读取配置文件出错！");
			e.printStackTrace();
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					System.out.println("关闭配置文件输入流出错！");
					e.printStackTrace();
				}
			}
		}
	}
	
	public static Properties getProp() {
		return prop;
	}

}
