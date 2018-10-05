package com.interlib.sso.log4j;

import java.io.File;

import org.apache.log4j.RollingFileAppender;

public class MyDailyRollingFileAppender extends RollingFileAppender {

	@Override
	public void setFile(String file) {
		String filePath = file;
		File fileCheck = new File(filePath);
		if (!fileCheck.exists())
			fileCheck.getParentFile().mkdirs();
		super.setFile(filePath);
	}

}
