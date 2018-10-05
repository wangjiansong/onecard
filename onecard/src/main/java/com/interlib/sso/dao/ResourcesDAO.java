package com.interlib.sso.dao;

import java.util.List;

import com.interlib.sso.domain.Resources;

public interface ResourcesDAO extends BaseDAO<Resources, String> {

	public List<String> listAllSubsys() ;
	
	public List<Resources> getResourcesBySubsys(String subsys);
	
	public List<Integer> getCompResourceByResourceId(String resourceId);
	
	public void deleteCompResourceByResourceId(String resourceId);
	
}
