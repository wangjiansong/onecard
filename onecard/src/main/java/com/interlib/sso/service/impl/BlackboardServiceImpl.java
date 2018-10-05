package com.interlib.sso.service.impl;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.interlib.sso.dao.BlackboardDAO;
import com.interlib.sso.domain.Blackboard;
import com.interlib.sso.service.BlackboardService;

@Service
public class BlackboardServiceImpl 
		extends AbstractBaseServiceImpl<Blackboard, Integer> implements BlackboardService {

	@Autowired
	public BlackboardDAO blackboardDAO;
	
	@Autowired
	public void setBlackboardDAO(BlackboardDAO blackboardDAO) {
		this.blackboardDAO = blackboardDAO;
	}

	@Autowired
	public void setBaseDAO(BlackboardDAO blackboardDAO) {
		super.setBaseDAO(blackboardDAO);
	}

	@Override
	public List<Blackboard> getBlackboardsByLastSomeone() {
		
		return blackboardDAO.getBlackboardsByLastSomeone();
	}
	

	
}
