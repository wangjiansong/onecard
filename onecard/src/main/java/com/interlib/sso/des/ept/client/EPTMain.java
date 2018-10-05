package com.interlib.sso.des.ept.client;

import com.interlib.sso.des.ept.service.EPTService;
import com.interlib.sso.des.ept.util.DBUtil;
import com.interlib.sso.des.ept.util.LogUtil;
import com.interlib.sso.des.ept.util.PropertyUtil;

public class EPTMain {
	
	public static void main(String[] args) {
		PropertyUtil.loadProperty();
		DBUtil.init();
		LogUtil.openWriter();
		EPTService service  = new EPTService();
		service.doEncrypt();
	}

}
