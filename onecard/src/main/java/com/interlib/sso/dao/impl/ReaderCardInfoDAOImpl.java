package com.interlib.sso.dao.impl;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.interlib.sso.dao.ReaderCardInfoDAO;
import com.interlib.sso.domain.ReaderCardInfo;

@Repository
public class ReaderCardInfoDAOImpl 
			extends AbstractMybatisBaseDAO<ReaderCardInfo, String> implements ReaderCardInfoDAO {

	@Autowired
	public SqlSession sqlSession;

	@Override
	public SqlSession getSqlSession() {
		return sqlSession;
	}

	@Override
	public String getMybatisMapperNamespace() {
		return "ReaderCardInfo";
	}

	@Override
	public ReaderCardInfo getByRdId(String rdId) {
		
		return (ReaderCardInfo)sqlSession.selectOne(getMybatisMapperNamespace() + ".getByRdId", rdId);
	}

	@Override
	public void deleteByRdId(String rdId) {
		sqlSession.delete(getMybatisMapperNamespace() + ".deleteByRdId", rdId);
	}

	@Override
	public int updateCardInfo(ReaderCardInfo cardInfo) {
		
		return sqlSession.update(getMybatisMapperNamespace()+".updateCardInfo",cardInfo);
	}
	
	

}
