package com.interlib.sso.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.interlib.sso.dao.LogCirTypeDAO;
import com.interlib.sso.domain.LogCirType;
import com.interlib.sso.service.LogCirTypeService;
@Service
public class LogCirTypeServiceImpl 
		extends AbstractBaseServiceImpl<LogCirType, String> implements LogCirTypeService {

	@Autowired
	public LogCirTypeDAO logCirTypeDAO;
	
	@Autowired
	public void setBaseDAO(LogCirTypeDAO logCirTypeDAO) {
		super.setBaseDAO(logCirTypeDAO);
	}

	@Override
	public List<LogCirType> getAll() {
		return logCirTypeDAO.getAll();
	}

	@Override
	public List<LogCirType> getAllLogCirType() {
		return logCirTypeDAO.getAllLogCirType();
	}

}
