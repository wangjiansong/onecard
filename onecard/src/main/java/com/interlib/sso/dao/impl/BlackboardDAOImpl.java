package com.interlib.sso.dao.impl;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.interlib.sso.dao.BlackboardDAO;
import com.interlib.sso.domain.Blackboard;

@Repository
public class BlackboardDAOImpl 
			extends AbstractMybatisBaseDAO<Blackboard, Integer>implements BlackboardDAO {
	
	@Autowired
	public SqlSession sqlSession;

	@Override
	public SqlSession getSqlSession() {
		return sqlSession;
	}

	@Override
	public String getMybatisMapperNamespace() {
		return "Blackboard";
	}
	
	@Override
	public List<Blackboard> getBlackboardsByLastSomeone() {
		
		return sqlSession.selectList(getMybatisMapperNamespace() + ".getBlackboardsByLastSomeone");
	}

}
