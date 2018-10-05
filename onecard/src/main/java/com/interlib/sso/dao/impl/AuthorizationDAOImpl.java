package com.interlib.sso.dao.impl;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.interlib.sso.dao.AuthorizationDAO;
import com.interlib.sso.domain.Authorization;
import com.interlib.sso.domain.FinApp;

@Repository
public class AuthorizationDAOImpl 
		extends AbstractMybatisBaseDAO<Authorization, Integer>implements AuthorizationDAO {

	@Autowired
	public SqlSession sqlSession;

	@Override
	public SqlSession getSqlSession() {
		return sqlSession;
	}

	
	public void setSqlSession(SqlSession sqlSession) {
		this.sqlSession = sqlSession;
	}


	@Override
	public String getMybatisMapperNamespace() {
		return "Authorization";
	}

	@Override
	public List<Authorization> queryAuthorizationList(Authorization entity) {
		return sqlSession.selectList(getMybatisMapperNamespace() + ".queryPageList", entity);
	}

	@Override
	public Authorization getByAppCode(String appcode) {
		return (Authorization) sqlSession.selectOne(getMybatisMapperNamespace() + ".getByAppCode", appcode);
	}


	@Override
	public List<Authorization> getAllSimple() {
		return sqlSession.selectList(getMybatisMapperNamespace() + ".getAllSimple");
	}

	@Override
	public Map<String, String> getAppMap() {
		List<Authorization> simpleList = getAllSimple();
		Map<String, String> finAppMap = new HashMap<String, String>();
		for(Authorization app : simpleList) {
			finAppMap.put(app.getAppCode(), app.getAppName());
		}
		return finAppMap;
	}


	@Override
	public List<Authorization> getAppsByOperator(String rdId) {
		
		return sqlSession.selectList(getMybatisMapperNamespace() + ".getAppsByOperator",rdId);
	}
	
	
	@Override
	public List<Authorization> getAppsByMultAppcode(String[] appcodes) {

		return sqlSession.selectList(getMybatisMapperNamespace()+ ".getAppsByMultAppcode", appcodes);
	}

}
