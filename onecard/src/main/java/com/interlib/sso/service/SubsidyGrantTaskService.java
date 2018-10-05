package com.interlib.sso.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface SubsidyGrantTaskService {
	
	public List<Map<String, Object>> queryCurrentGrants(Date currentDate);
	
	public List<Integer> queryGrantGroups(int grantId);
	
	public int doGrant(Map<String, Object> param);

}
