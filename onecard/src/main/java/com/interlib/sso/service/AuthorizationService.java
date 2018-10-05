package com.interlib.sso.service;

import java.util.List;
import java.util.Map;

import com.interlib.sso.domain.Authorization;

public interface AuthorizationService extends BaseService<Authorization, Integer> {
	
	public List<Authorization> queryAuthorizationList(Authorization entity) ;
	
	public Authorization getByAppCode(String appcode);
	
	public List<Authorization> getAllSimple();
	
	public Map<String, String> getAppMap();

	public List<Authorization> getAppsByOperator(String rdId);//根据操作人查询对应的数据 2015-1-8
	
	public List<Authorization> getAppsByMultAppcode(String []appcode);//根据操作人查询对应的数据 2015-1-8
	
}
