package com.interlib.sso.dao.impl;


import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.lob.LobHandler;
import org.springframework.stereotype.Repository;

import com.interlib.sso.dao.ResourcesDAO;
import com.interlib.sso.domain.Resources;

@Repository
public class ResourcesDAOImpl 
		extends AbstractMybatisBaseDAO<Resources, String> implements ResourcesDAO {

	private final Logger log = LoggerFactory.getLogger(getClass());

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

	@Override
	public String getMybatisMapperNamespace() {
		return "Resources";
	}

	@Override
	public List<String> listAllSubsys() {
		return sqlSession.selectList(getMybatisMapperNamespace() + ".listSubsys");
	}

	@Override
	public List<Resources> getResourcesBySubsys(String subsys) {
		
		return sqlSession.selectList(getMybatisMapperNamespace() + ".getBySubsys", subsys);
	}

	@Override
	public List<Integer> getCompResourceByResourceId(String resourceId) {
		
		return sqlSession.selectList(getMybatisMapperNamespace() + ".getCompRes", resourceId);
	}

	@Override
	public void deleteCompResourceByResourceId(String resourceId) {
		sqlSession.delete(getMybatisMapperNamespace() + ".deleteCompRes", resourceId);
	}
	
	
}
