package com.interlib.sso.dao;

import java.util.List;

import com.interlib.sso.domain.Blackboard;

public interface BlackboardDAO extends BaseDAO<Blackboard, Integer> {

	public List<Blackboard> getBlackboardsByLastSomeone();
}
