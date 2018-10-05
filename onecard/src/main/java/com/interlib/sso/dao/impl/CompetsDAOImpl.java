package com.interlib.sso.dao.impl;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.interlib.sso.dao.CompetsDAO;
import com.interlib.sso.domain.Compets;
import com.interlib.sso.domain.Resources;
import com.interlib.sso.domain.UCompet_Resource;

@Repository
public class CompetsDAOImpl 
		extends AbstractMybatisBaseDAO<Compets, Integer> implements CompetsDAO {

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
		return "Compets";
	}

	@Override
	public void saveCompet_Resource(UCompet_Resource compRes) {
		sqlSession.insert(getMybatisMapperNamespace() + ".insertCompRes", compRes);
	}

	@Override
	public List<String> getResourceByCompetId(Integer competId) {
		
		return sqlSession.selectList(getMybatisMapperNamespace() + ".getCompRes", competId);
	}

	@Override
	public void deleteComResourceByCompetId(Integer competId) {
		
		sqlSession.delete(getMybatisMapperNamespace() + ".deleteCompRes", competId);
	}

	@Override
	public List<Integer> getRoleCompByCompetId(Integer competId) {
		
		return sqlSession.selectList(getMybatisMapperNamespace() + ".getRoleComp", competId);
	}

	@Override
	public void deleteRoleCompByCompetId(Integer competId) {
		sqlSession.delete(getMybatisMapperNamespace() + ".deleteRoleComp", competId);
	}

	@Override
	public List<Resources> getResourcesByCompetId(Integer competId) {
		return sqlSession.selectList(getMybatisMapperNamespace() + ".getResourceByCompetId", competId);
	}

	@Override
	public Collection<String> getResourceIdByCompetId(Integer competId) {
		return sqlSession.selectList(getMybatisMapperNamespace() + ".getCompRes", competId);
	}

}
