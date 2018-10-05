package com.interlib.sso.dao;

import java.util.List;

import com.interlib.sso.domain.LogCir;

public interface LogCirDAO extends BaseDAO<LogCir, Integer> {

	public List<LogCir> queryLogCirList(LogCir logCir);
	
	public void batchUpdate(LogCir logCir, List<String> ids);
	
	public int getLogCirCount(LogCir logCir);

}
