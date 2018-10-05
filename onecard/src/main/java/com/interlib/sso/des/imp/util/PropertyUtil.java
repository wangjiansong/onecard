package com.interlib.sso.des.imp.util;

import java.io.IOException;
import java.io.InputStream;

import com.interlib.sso.des.imp.service.IMPService;

public class PropertyUtil {

	public static void loadProperty(String filePath) {
		InputStream inputStream = null;
		try {
			// Thread.currentThread().getContextClassLoader().getResourceAsStream("config.properties")
			inputStream = ClassLoader.getSystemResourceAsStream(filePath);
			IMPService.getProp().load(inputStream);
		} catch (IOException e) {
			System.out.println("读取配置文件出错！");
			e.printStackTrace();
			System.exit(0);
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					System.out.println("关闭配置文件输入流出错！");
					e.printStackTrace();
					System.exit(0);
				}
			}
		}
	}
	
}
