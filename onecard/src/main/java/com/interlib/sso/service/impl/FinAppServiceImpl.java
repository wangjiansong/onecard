package com.interlib.sso.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.interlib.sso.dao.FinAppDAO;
import com.interlib.sso.domain.FinApp;
import com.interlib.sso.service.FinAppService;

@Service
public class FinAppServiceImpl 
		extends AbstractBaseServiceImpl<FinApp, String> implements FinAppService {


	@Autowired
	public FinAppDAO finAppDAO;

	@Autowired
	public void setBaseDAO(FinAppDAO finAppDAO) {
		super.setBaseDAO(finAppDAO);
	}

	@Override
	public Map<String, String> getFinAppMap() {
		return finAppDAO.getFinAppMap();
	}

	@Override
	public List<FinApp> queryFinAppList(FinApp finApp) {
		return finAppDAO.queryFinAppList(finApp);
	}
	
	@Override
	public List<FinApp> getAllSimple() {
		return finAppDAO.getAllSimple();
	}

}
