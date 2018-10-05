package com.interlib.sso.dao.impl;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.interlib.sso.dao.ReaderFeeDAO;
import com.interlib.sso.domain.ReaderFee;

@Repository
public class ReaderFeeDAOImpl 
			extends AbstractMybatisBaseDAO<ReaderFee, String> implements ReaderFeeDAO {

	@Autowired
	public SqlSession sqlSession;

	@Override
	public SqlSession getSqlSession() {
		return sqlSession;
	}

	@Override
	public String getMybatisMapperNamespace() {
		return "ReaderFee";
	}

}
