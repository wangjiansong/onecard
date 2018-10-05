package com.interlib.sso.dao;

import java.util.Date;

import com.interlib.sso.domain.DynamicScript;

public interface DynamicScriptDAO {
	
	public DynamicScript insert(DynamicScript dynamicScript);
	
	public int updateByScriptName(DynamicScript dynamicScript);
	
	public DynamicScript findByScriptName(String scriptName);
	
	public String getScriptSourceByScriptName(String scriptName);
	
	public Date getLastUpdatedByScriptName(String scriptName);
	
	public int deleteByScriptName(String scriptName);
	
	public boolean existByScriptName(String scriptName);
	
}
