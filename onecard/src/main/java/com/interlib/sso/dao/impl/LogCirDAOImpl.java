package com.interlib.sso.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.interlib.sso.dao.LogCirDAO;
import com.interlib.sso.domain.CirFinLog;
import com.interlib.sso.domain.LogCir;

@Repository
public class LogCirDAOImpl 
			extends AbstractMybatisBaseDAO<LogCir, Integer> implements LogCirDAO {

	@Autowired
	public SqlSession sqlSession;

	@Override
	public SqlSession getSqlSession() {
		return sqlSession;
	}

	@Override
	public String getMybatisMapperNamespace() {
		return "LogCir";
	}

	@Override
	public List<LogCir> queryLogCirList(LogCir logCir) {
		return sqlSession.selectList(getMybatisMapperNamespace() + ".queryPageList", logCir);
	}

	@Override
	public void batchUpdate(LogCir logCir, List<String> list) {
		Map<String,Object> paramMap = new HashMap<String,Object>();  
		paramMap.put("logCir", logCir);  
		paramMap.put("list",  list);  
		sqlSession.update(getMybatisMapperNamespace() + ".batchUpdate", paramMap);
	}

	@Override
	public int getLogCirCount(LogCir logCir) {
		
		return Integer.parseInt(sqlSession.selectOne(getMybatisMapperNamespace() + ".getLogCirCount", logCir).toString());
	}
	
}
