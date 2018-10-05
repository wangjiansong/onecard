package com.interlib.sso.common;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Properties;
import java.util.ResourceBundle;

public class PropertyUtils {
	public static Properties getProperties(String fileName) {
		ResourceBundle resourcebundle = ResourceBundle.getBundle(fileName);
		Enumeration<String> enumeration = resourcebundle.getKeys();
		Properties properties = new Properties();
		String propertyName;
		while (enumeration.hasMoreElements()) {
			propertyName = enumeration.nextElement();
			properties.put(propertyName, resourcebundle.getObject(propertyName));
		}
		return properties;
	}

	public static String getProperty(String fileName, String propName) throws Exception {
		// Properties prop=getProperties(fileName);
		// return prop.getProperty(propName, "");
		// 获得类加载器，然后把文件作为一个流获取
		InputStream in = new FileInputStream(getBinPath() + fileName + ".properties");
		// 创建Properties实例
		Properties prop = new Properties();
		// 将Properties和流关联
		prop.load(in);
		// 利用已得到的名称通过Properties对象获得相应的值
		String value = (String) prop.get(propName);
		// 关闭资源
		in.close();
		return value;
	}
	
	public static Properties getProperty(String fileName){
		try {
			// Properties prop=getProperties(fileName);
			// return prop.getProperty(propName, "");
			// 获得类加载器，然后把文件作为一个流获取
			InputStream in = new FileInputStream(getBinPath() + fileName + ".properties");
			// 创建Properties实例
			Properties prop = new Properties();
			// 将Properties和流关联
			prop.load(in);
			in.close();
			return prop;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	public static Object getPropertyFromFolder(String fileName, Object propName, String path) throws Exception {
		// Properties prop=getProperties(fileName);
		// return prop.getProperty(propName, "");
		// 获得类加载器，然后把文件作为一个流获取
		InputStream in = new FileInputStream(getBinPath() + path + "/" + fileName + ".properties");
		// 创建Properties实例
		Properties prop = new Properties();
		// 将Properties和流关联
		prop.load(in);
		if (propName instanceof Integer) {
			propName = propName + "";
		}
		// 利用已得到的名称通过Properties对象获得相应的值
		String value = (String) prop.get(propName);
		if (value != null) {
			value = new String(value.getBytes("ISO-8859-1"), "utf-8");
		}
		// 关闭资源
		in.close();
		return value;
	}

	public static void setProperty(String fileName, String propName, String propValue) throws Exception {
		Properties prop = getProperties(fileName);
		prop.setProperty(propName, propValue);
		FileOutputStream fos = new FileOutputStream(getBinPath() + fileName + ".properties");
		prop.store(fos, "");
		fos.close();
	}

	private static String getBinPath() {
		return PropertyUtils.class.getResource("/").getPath();
	}
}
