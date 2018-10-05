package com.interlib.sso.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.interlib.sso.dao.SubsidyGrantTaskDao;
import com.interlib.sso.service.SubsidyGrantTaskService;

public class SubsidyGrantTaskServiceImpl implements SubsidyGrantTaskService {
	
	private SubsidyGrantTaskDao subsidyGrantTaskDao;

	public void setSubsidyGrantTaskDao(SubsidyGrantTaskDao subsidyGrantTaskDao) {
		this.subsidyGrantTaskDao = subsidyGrantTaskDao;
	}

	public List<Map<String, Object>> queryCurrentGrants(Date currentDate) {
		return subsidyGrantTaskDao.queryCurrentGrants(currentDate);
	}
	
	public List<Integer> queryGrantGroups(int grantId) {
		return subsidyGrantTaskDao.queryGrantGroups(grantId);
	}
	
	public int doGrant(Map<String, Object> param) {
		return subsidyGrantTaskDao.doGrant(param);
	}
	
}
