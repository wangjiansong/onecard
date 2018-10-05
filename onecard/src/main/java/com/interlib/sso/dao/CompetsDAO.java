package com.interlib.sso.dao;

import java.util.Collection;
import java.util.List;

import com.interlib.sso.domain.Compets;
import com.interlib.sso.domain.Resources;
import com.interlib.sso.domain.UCompet_Resource;

public interface CompetsDAO extends BaseDAO<Compets, Integer> {

	public void saveCompet_Resource(UCompet_Resource compRes);
	
	public List<String> getResourceByCompetId(Integer competId);
	
	public void deleteComResourceByCompetId(Integer competId);
	
	public List<Integer> getRoleCompByCompetId(Integer competId);
	
	public void deleteRoleCompByCompetId(Integer competId);
	
	public List<Resources> getResourcesByCompetId(Integer competId);
	
	public Collection<String> getResourceIdByCompetId(Integer competId);
	
}
