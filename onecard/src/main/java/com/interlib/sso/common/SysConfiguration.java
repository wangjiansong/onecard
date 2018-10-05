package com.interlib.sso.common;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Properties;
import java.util.ResourceBundle;

public class SysConfiguration {

	private Properties props;
	
	public SysConfiguration() {
		try {
			props = loadParams("config");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public Properties getProps() {
		return this.props;
	}
	
	private static Properties loadParams(String s) throws IOException {
		Properties properties = new Properties();
		ResourceBundle resourcebundle = ResourceBundle.getBundle(s);
		Enumeration enumeration = resourcebundle.getKeys();
		// Object obj = null;
		String s1;
		for (; enumeration.hasMoreElements(); properties.put(s1,
				resourcebundle.getObject(s1)))
			s1 = (String) enumeration.nextElement();

		return properties;
	}
}
