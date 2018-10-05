package com.interlib.sso.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.interlib.sso.dao.LogCirDAO;
import com.interlib.sso.domain.CirFinLog;
import com.interlib.sso.domain.LogCir;
import com.interlib.sso.service.LogCirService;

@Service
public class LogCirServiceImpl 
			extends AbstractBaseServiceImpl<LogCir, Integer> implements LogCirService {

	@Autowired
	public LogCirDAO logCirDAO;
	
	@Autowired
	public void setBaseDAO(LogCirDAO logCirDAO) {
		super.setBaseDAO(logCirDAO);
	}

	@Override
	public List<LogCir> queryLogCirList(LogCir logCir) {
		return logCirDAO.queryLogCirList(logCir);
	}

	@Override
	public void batchUpdate(LogCir logCir, List<String> ids) {
		logCirDAO.batchUpdate(logCir, ids);
	}
	@Override
	public int getLogCirCount(LogCir logCir ) {
		
		return logCirDAO.getLogCirCount(logCir);
	}

}
