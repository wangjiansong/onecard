package com.interlib.sso.service.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.interlib.sso.dao.CompetsDAO;
import com.interlib.sso.domain.Compets;
import com.interlib.sso.domain.Resources;
import com.interlib.sso.domain.UCompet_Resource;
import com.interlib.sso.service.CompetsService;

@Service
public class CompetsServiceImpl 
			extends AbstractBaseServiceImpl<Compets, Integer> implements CompetsService {

	@Autowired
	public CompetsDAO competsDAO;
	
	public void setCompetsDAO(CompetsDAO competsDAO) {
		this.competsDAO = competsDAO;
	}

	@Autowired
	public void setBaseDAO(CompetsDAO competsDAO) {
		super.setBaseDAO(competsDAO);
	}
	public void saveCompet_Resource(UCompet_Resource compRes) {
		competsDAO.saveCompet_Resource(compRes);
	}
	@Override
	public List<String> getResourceByCompetId(Integer competId) {
		return competsDAO.getResourceByCompetId(competId);
	}
	@Override
	public void deleteComResourceByCompetId(Integer competId) {
		competsDAO.deleteComResourceByCompetId(competId);
	}
	@Override
	public List<Integer> getRoleCompByCompetId(Integer competId) {
		
		return competsDAO.getRoleCompByCompetId(competId);
	}
	@Override
	public void deleteRoleCompByCompetId(Integer competId) {
		competsDAO.deleteRoleCompByCompetId(competId);
	}
	@Override
	public List<Resources> getResourcesByCompetId(Integer competId) {
		return competsDAO.getResourcesByCompetId(competId);
	}
	
	@Override
	public List<Resources> getResourcesByCompetIds(List<Integer> competIds) {
		List<Resources> retList = new ArrayList<Resources>();
		for(Integer competId : competIds) {
			List<Resources> resourceIdList = getResourcesByCompetId(competId);
			retList.addAll(resourceIdList);
		}
		HashSet h = new HashSet(retList);
		retList.clear();
		retList.addAll(h);
		return retList;
	}
	
	@Override
	public List<String> getResourcesIdByCompetIds(List<Integer> competIds) {
		List<String> retList = new ArrayList<String>();
		for(Integer competId : competIds) {
			List<String> resourceIdList = getResourceByCompetId(competId);
			retList.addAll(resourceIdList);
		}
		HashSet h = new HashSet(retList);
		retList.clear();
		retList.addAll(h);
		return retList;
	}
}
