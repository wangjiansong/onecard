package com.interlib.sso.dao.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;

import com.interlib.sso.dao.SubsidyGrantTaskDao;

@SuppressWarnings("unchecked")
public class SubsidyGrantTaskDaoImpl implements SubsidyGrantTaskDao {
	
	private SqlSession sqlSession;
	
	public void setSqlSession(SqlSession sqlSession) {
		this.sqlSession = sqlSession;
	}
	
	/**
	 * 指定配置文件命名空间
	 */
	private String getMybatisMapperNamespace() {
		return "SubsidyGrantTaskMapper";
	}
	
	public List<Map<String, Object>> queryCurrentGrants(Date currentDate) {
		return sqlSession.selectList(getMybatisMapperNamespace() + ".queryCurrentGrants", currentDate);
	}
	
	public List<Integer> queryGrantGroups(int grantId) {
		return sqlSession.selectList(getMybatisMapperNamespace() + ".queryGrantGroups", grantId);
	}
	
	public int doGrant(Map<String, Object> param) {
		return sqlSession.update(getMybatisMapperNamespace() + ".doGrant", param);
	}
	
}
