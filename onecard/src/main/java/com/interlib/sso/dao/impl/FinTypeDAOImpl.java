package com.interlib.sso.dao.impl;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.interlib.sso.dao.FinTypeDAO;
import com.interlib.sso.domain.CirFinLog;
import com.interlib.sso.domain.FinType;

@Repository
public class FinTypeDAOImpl 
		extends AbstractMybatisBaseDAO<FinType, String> implements FinTypeDAO {

	@Autowired
	public SqlSession sqlSession;

	@Override
	public SqlSession getSqlSession() {
		return sqlSession;
	}

	@Override
	public String getMybatisMapperNamespace() {
		return "FinType";
	}

	@Override
	public List<FinType> getAll() {
		return sqlSession.selectList(getMybatisMapperNamespace() + ".getAll");
	}

	@Override
	public 	List<FinType> getAllFeeType() {
		return sqlSession.selectList( getMybatisMapperNamespace() + ".getAllFeeType");
	}

	@Override
	public List<FinType> searchFeeType(String[] feetypes) {
		
		return sqlSession.selectList(getMybatisMapperNamespace() + ".searchFeeType", feetypes);
	}

	@Override
	public List<FinType> getFinTypesByAppcodes(String[] appcodes) {
		
		return sqlSession.selectList(getMybatisMapperNamespace() + ".getFinTypesByAppcodes", appcodes);
	}

	@Override
	public List<FinType> getFeeTypeByMultiFeetype(String[] feetypes) {
		return sqlSession.selectList(getMybatisMapperNamespace() + ".getFeeTypeByMultiFeetype", feetypes);
	}
	
}
