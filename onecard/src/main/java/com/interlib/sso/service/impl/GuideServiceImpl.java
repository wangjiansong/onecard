package com.interlib.sso.service.impl;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.interlib.sso.dao.GuideDAO;
import com.interlib.sso.domain.Guide;
import com.interlib.sso.service.GuideService;

@Service
public class GuideServiceImpl 
			extends AbstractBaseServiceImpl<Guide, Long>implements GuideService {
	@Autowired
	public GuideDAO guideDAO;
	
	@Autowired
	public void setGuidedao(GuideDAO guideDAO) {
		this.guideDAO = guideDAO;
	}
	
	@Autowired
	public void setBaseDAO(GuideDAO guideDAO){
		super.setBaseDAO(guideDAO);
	}

	@Override
	public List<Guide> getGuidesByLastSomeone() {
		return guideDAO.getGuidesByLastSomeone();
	}
	
}
