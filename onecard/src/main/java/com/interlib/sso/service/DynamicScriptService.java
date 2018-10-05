package com.interlib.sso.service;

import java.util.Date;

import com.interlib.sso.domain.DynamicScript;

public interface DynamicScriptService {

	public DynamicScript insert(DynamicScript dynamicScript);
	
	public boolean updateByScriptName(DynamicScript dynamicScript);
	
	public DynamicScript findByScriptName(String scriptName);
	
	public String getScriptSourceByScriptName(String scriptName);
	
	public Date getLastUpdatedByScriptName(String scriptName);
	
	public boolean deleteByScriptName(String scriptName);
	
	public boolean existByScriptName(String scriptName);
	
	public DynamicScript insertOrUpdateByScriptName(
			DynamicScript dynamicScript);
	
}
