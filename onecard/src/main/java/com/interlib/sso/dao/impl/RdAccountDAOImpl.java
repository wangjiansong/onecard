package com.interlib.sso.dao.impl;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.lob.LobHandler;
import org.springframework.stereotype.Repository;

import com.interlib.sso.dao.RdAccountDAO;
import com.interlib.sso.domain.RdAccount;

@Repository
public class RdAccountDAOImpl extends AbstractMybatisBaseDAO<RdAccount, String> implements RdAccountDAO {

	@Autowired
	public LobHandler lobHandler;
	
	@Autowired
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	
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
		return "RdAccount";
	}

	@Override
	public List<RdAccount> queryRdAccountList(RdAccount rdAccount) {
		return sqlSession.selectList(getMybatisMapperNamespace() + ".queryPageList", rdAccount);
	}

}
