package com.interlib.sso.service;


import java.util.List;

import com.interlib.sso.domain.Blackboard;


public interface BlackboardService extends BaseService<Blackboard, Integer> {

	public List<Blackboard> getBlackboardsByLastSomeone();
	

}
