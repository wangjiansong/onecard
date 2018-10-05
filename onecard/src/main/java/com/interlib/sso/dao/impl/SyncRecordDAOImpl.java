package com.interlib.sso.dao.impl;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.interlib.sso.dao.SyncRecordDAO;
import com.interlib.sso.domain.SyncRecord;

@Repository
public class SyncRecordDAOImpl 
			extends AbstractMybatisBaseDAO<SyncRecord, String>implements SyncRecordDAO {
	
	@Autowired
	public SqlSession sqlSession;

	@Override
	public SqlSession getSqlSession() {
		return sqlSession;
	}

	@Override
	public String getMybatisMapperNamespace() {
		return "SyncRecord";
	}

	@Override
	public List<SyncRecord> querySyncRecordList(SyncRecord syncRecord) {
		return sqlSession.selectList(getMybatisMapperNamespace() + ".queryPageList", syncRecord);
	}
	
}
