package com.interlib.sso.des.exp.client;

import com.interlib.sso.des.exp.service.EXPService;
import com.interlib.sso.des.exp.util.PropertyUtil;

public class EXPMain {
	
	public static void main(String[] args) {
		PropertyUtil.loadProperty("config.properties");
		EXPService service = new EXPService();
		service.exportDMP();
	}

}
