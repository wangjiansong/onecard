package com.interlib.sso.dao;

import java.util.List;
import java.util.Map;

import com.interlib.sso.domain.FinApp;

public interface FinAppDAO extends BaseDAO<FinApp, String>{

	public List<FinApp> getAllSimple();
	
	public Map<String, String> getFinAppMap();
	
	public List<FinApp> queryFinAppList(FinApp finApp);
}
