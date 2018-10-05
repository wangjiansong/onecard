package com.interlib.sso.service;

import java.util.List;
import java.util.Map;

import com.interlib.sso.domain.FinApp;

public interface FinAppService extends BaseService<FinApp, String>{

	public Map<String, String> getFinAppMap();
	
	public List<FinApp> queryFinAppList(FinApp finApp);
	
	public List<FinApp> getAllSimple();
}
