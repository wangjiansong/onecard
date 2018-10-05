package com.interlib.sso.dao;

import java.util.List;

import com.interlib.sso.domain.FinType;
import com.interlib.sso.domain.LogCirType;

public interface LogCirTypeDAO extends BaseDAO<LogCirType, String>{

	public List<LogCirType> getAll();
	
	public List<LogCirType> getAllLogCirType();

}
