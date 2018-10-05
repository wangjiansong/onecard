package com.interlib.sso.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.interlib.sso.dao.FinAppDAO;
import com.interlib.sso.domain.FinApp;

@Repository
public class FinAppDAOImpl 
		extends AbstractMybatisBaseDAO<FinApp, String> implements FinAppDAO {

	@Autowired
	public SqlSession sqlSession;

	@Override
	public SqlSession getSqlSession() {
		return sqlSession;
	}

	@Override
	public String getMybatisMapperNamespace() {
		return "FinApp";
	}

	@Override
	public List<FinApp> getAllSimple() {
		return sqlSession.selectList(getMybatisMapperNamespace() + ".getAllSimple");
	}

	@Override
	public Map<String, String> getFinAppMap() {
		List<FinApp> simpleList = getAllSimple();
		Map<String, String> finAppMap = new HashMap<String, String>();
		for(FinApp finApp : simpleList) {
			finAppMap.put(finApp.getAppCode(), finApp.getAppName());
		}
		return finAppMap;
	}

	@Override
	public List<FinApp> queryFinAppList(FinApp finApp) {
		
		return sqlSession.selectList(getMybatisMapperNamespace() + ".queryPageList", finApp);
	}

}
