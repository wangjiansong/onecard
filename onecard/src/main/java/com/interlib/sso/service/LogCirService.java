package com.interlib.sso.service;

import java.util.List;

import com.interlib.sso.domain.CirFinLog;
import com.interlib.sso.domain.LogCir;

public interface LogCirService extends BaseService<LogCir, Integer>{

	public List<LogCir> queryLogCirList(LogCir logCir);
	
	public void batchUpdate(LogCir logCir, List<String> ids);
	//ADD 2017-09-28根据条件查询出需要的结果的数量
	public int getLogCirCount(LogCir logCir);
	
}
