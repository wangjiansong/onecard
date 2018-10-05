package com.interlib.sso.dao.impl;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.interlib.sso.dao.LogCirTypeDAO;
import com.interlib.sso.domain.LogCirType;

@Repository
public class LogCirTypeDAOImpl 
		extends AbstractMybatisBaseDAO<LogCirType, String> implements LogCirTypeDAO {

	@Autowired
	public SqlSession sqlSession;

	@Override
	public SqlSession getSqlSession() {
		return sqlSession;
	}

	@Override
	public String getMybatisMapperNamespace() {
		return "LogCirType";
	}

	@Override
	public List<LogCirType> getAll() {
		return sqlSession.selectList(getMybatisMapperNamespace() + ".getAll");
	}

	@Override
	public 	List<LogCirType> getAllLogCirType() {
		return sqlSession.selectList( getMybatisMapperNamespace() + ".getAllLogCirType");
	}

}
