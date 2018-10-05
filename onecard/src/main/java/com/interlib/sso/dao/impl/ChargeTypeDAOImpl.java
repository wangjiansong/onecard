package com.interlib.sso.dao.impl;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.interlib.sso.dao.ChargeTypeDAO;
import com.interlib.sso.domain.ChargeType;
import com.interlib.sso.domain.FinType;

@Repository
public class ChargeTypeDAOImpl 
		extends AbstractMybatisBaseDAO<ChargeType, String> implements ChargeTypeDAO {

	@Autowired
	public SqlSession sqlSession;

	@Override
	public SqlSession getSqlSession() {
		return sqlSession;
	}

	@Override
	public String getMybatisMapperNamespace() {
		return "ChargeType";
	}

	@Override
	public List<ChargeType> getAll() {
		return sqlSession.selectList(getMybatisMapperNamespace() + ".getAll");
	}
	
	@Override
	public List<ChargeType> getChargeTypesByAppcodes(String[] appcodes) {
		
		return sqlSession.selectList(getMybatisMapperNamespace() + ".getChargeTypesByAppcodes", appcodes);
	}


	@Override
	public 	List<ChargeType> getAllChargeType() {
		return sqlSession.selectList( getMybatisMapperNamespace() + ".getAllChargeType");
	}

	@Override
	public List<ChargeType> searchChargeType(String[] chargetypes) {
		
		return sqlSession.selectList(getMybatisMapperNamespace() + ".searchChargeType", chargetypes);
	}


	@Override
	public List<ChargeType> getChargeTypeByMultiChargetype(String[] chargetypes) {
		return sqlSession.selectList(getMybatisMapperNamespace() + ".getChargeTypeByMultiChargetype", chargetypes);
	}
	
}
