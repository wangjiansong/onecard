package com.interlib.sso.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.interlib.sso.domain.Resources;

public interface ResourceService extends BaseService<Resources, String> {

	public List<String> listAllSubsys();
	
	public List<Resources> getResourcesBySubsys(String subsys);
	
	public List<Integer> getCompResourceByResourceId(String resourceId) ;
	
	public void deleteCompResourceByResourceId(String resourceId);
}
