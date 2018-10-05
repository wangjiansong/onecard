package com.interlib.sso.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.interlib.sso.dao.ResourcesDAO;
import com.interlib.sso.domain.Resources;
import com.interlib.sso.service.ResourceService;

@Service
public class ResourcesServiceImpl 
		extends AbstractBaseServiceImpl<Resources, String> implements ResourceService {

	@Autowired
	public ResourcesDAO resourcesDAO;
	

	@Autowired
	public void setBaseDAO(ResourcesDAO resourcesDAO) {
		super.setBaseDAO(resourcesDAO);
	}

	@Override
	public List<String> listAllSubsys() {
		return resourcesDAO.listAllSubsys();
	}

	@Override
	public List<Resources> getResourcesBySubsys(String subsys) {
		
		return resourcesDAO.getResourcesBySubsys(subsys);
	}

	@Override
	public List<Integer> getCompResourceByResourceId(String resourceId) {
		
		return resourcesDAO.getCompResourceByResourceId(resourceId);
	}

	@Override
	public void deleteCompResourceByResourceId(String resourceId) {
		resourcesDAO.deleteCompResourceByResourceId(resourceId);
	}
}
