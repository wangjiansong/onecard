package com.interlib.sso.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.interlib.sso.dao.AuthorizationDAO;
import com.interlib.sso.domain.Authorization;
import com.interlib.sso.service.AuthorizationService;

@Service
public class AuthorizationServiceImpl 
		extends AbstractBaseServiceImpl<Authorization, Integer> implements AuthorizationService {

	@Autowired
	public AuthorizationDAO authorizationDAO;
	
	
	public void setAuthorizationDAO(AuthorizationDAO authorizationDAO) {
		this.authorizationDAO = authorizationDAO;
	}

	@Autowired
	public void setBaseDAO(AuthorizationDAO authorizationDAO) {
		super.setBaseDAO(authorizationDAO);
	}

	@Override
	public List<Authorization> queryAuthorizationList(Authorization entity) {
		return authorizationDAO.queryAuthorizationList(entity);
	}

	@Override
	public Authorization getByAppCode(String appcode) {
		return authorizationDAO.getByAppCode(appcode);
	}

	@Override
	public List<Authorization> getAllSimple() {
		return authorizationDAO.getAllSimple();
	}

	@Override
	public Map<String, String> getAppMap() {
		return authorizationDAO.getAppMap();
	}

	@Override
	public List<Authorization> getAppsByOperator(String rdId) {
		
		return authorizationDAO.getAppsByOperator(rdId);
	}
	
	
	@Override
	public List<Authorization> getAppsByMultAppcode(String[] appcode) {
		return authorizationDAO.getAppsByMultAppcode(appcode);
	}
	
	
}
