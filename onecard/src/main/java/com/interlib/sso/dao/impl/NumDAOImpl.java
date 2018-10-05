package com.interlib.sso.dao.impl;

import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.interlib.sso.dao.NumDAO;
import com.interlib.sso.domain.Num;

@SuppressWarnings("unchecked")
@Repository
public class NumDAOImpl 
		extends AbstractMybatisBaseDAO<Num, String> implements NumDAO {
	
	private final Logger log = LoggerFactory.getLogger(getClass());
	
	@Autowired
	public SqlSession sqlSession;
	

	public void setSqlSession(SqlSession sqlSession) {
		this.sqlSession = sqlSession;
	}

	@Override
	public SqlSession getSqlSession() {
		return sqlSession;
	}

	@Override
	public String getMybatisMapperNamespace() {
		return "NumMapper";
	}
	
	
	public int addNum(Num num) {
		return sqlSession.insert(getMybatisMapperNamespace()+".addNum", num);
	}
	
	public int updateNum(String id) {
		return sqlSession.update(getMybatisMapperNamespace()+".updateNum",id);
	}
	
	public Num getNum(String id) {
		return (Num) sqlSession.selectOne(getMybatisMapperNamespace()+".getNum",id);
	}
	

	
	
}
