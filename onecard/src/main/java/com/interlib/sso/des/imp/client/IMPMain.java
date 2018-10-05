package com.interlib.sso.des.imp.client;

import com.interlib.sso.des.imp.service.IMPService;
import com.interlib.sso.des.imp.util.PropertyUtil;

public class IMPMain {
	
	public static void main(String[] args) {
		PropertyUtil.loadProperty("config.properties");
		IMPService service = new IMPService();
		service.importDMP();
	}

}
