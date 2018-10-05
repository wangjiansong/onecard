package com.interlib.sso.service;

import java.util.List;

import com.interlib.sso.domain.Compets;
import com.interlib.sso.domain.Resources;
import com.interlib.sso.domain.Roles;
import com.interlib.sso.domain.UCompet_Resource;


public interface CompetsService extends BaseService<Compets, Integer> {

	public void saveCompet_Resource(UCompet_Resource compRes);
	
	public List<String> getResourceByCompetId(Integer competId);
	
	public void deleteComResourceByCompetId(Integer competId);
	
	public List<Integer> getRoleCompByCompetId(Integer competId) ;
		
	public void deleteRoleCompByCompetId(Integer competId) ;
	
	public List<Resources> getResourcesByCompetIds(List<Integer> competIds);
	
	public List<Resources> getResourcesByCompetId(Integer competId);
	
	public List<String> getResourcesIdByCompetIds(List<Integer> competIds);
	
}
