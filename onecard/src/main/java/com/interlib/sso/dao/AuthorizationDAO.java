package com.interlib.sso.dao;

import java.util.List;
import java.util.Map;

import com.interlib.sso.domain.Authorization;
import com.interlib.sso.domain.FinApp;


public interface AuthorizationDAO extends BaseDAO<Authorization, Integer>{
	
	public List<Authorization> queryAuthorizationList(Authorization entity) ;
	
	public Authorization getByAppCode(String appcode);
	
	public List<Authorization> getAllSimple();
	
	public Map<String, String> getAppMap();

	public List<Authorization> getAppsByOperator(String rdId);//2015-1-8

	
	public List<Authorization> getAppsByMultAppcode(String [] appcodes);
}
