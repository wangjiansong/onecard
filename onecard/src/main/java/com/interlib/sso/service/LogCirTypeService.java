package com.interlib.sso.service;

import java.util.List;

import com.interlib.sso.domain.LogCirType;

public interface LogCirTypeService extends BaseService<LogCirType, String>{

	public List<LogCirType> getAll();
	
	public List<LogCirType> getAllLogCirType();
	
}
