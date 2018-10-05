package com.interlib.sso.dao.impl;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.lob.LobHandler;
import org.springframework.stereotype.Repository;

import com.interlib.sso.dao.RolesDAO;
import com.interlib.sso.domain.Compets;
import com.interlib.sso.domain.Roles;
import com.interlib.sso.domain.URole_Compet;

@Repository
public class RolesDAOImpl 
		extends AbstractMybatisBaseDAO<Roles, Integer> implements RolesDAO {

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
		// TODO Auto-generated method stub
		return "Roles";
	}


	@Override
	public List<Compets> getRoleCompetByRoleId(Integer roleId) {
		
		return sqlSession.selectList(getMybatisMapperNamespace() + ".getRoleCompet", roleId);
	}


	@Override
	public void deleteRoleCompByRoleId(Integer roleId) {
		sqlSession.delete(getMybatisMapperNamespace() + ".deleteRoleComp", roleId);
	}


	@Override
	public void saveRoleCompet(URole_Compet roleCom) {
		sqlSession.insert(getMybatisMapperNamespace() + ".insertRoleComp", roleCom);
	}


	@Override
	public List<Compets> getOtherCompetByRoleId(Integer roleId) {
		return sqlSession.selectList(getMybatisMapperNamespace() + ".getOtherCompet", roleId);
	}


	@Override
	public List<Integer> getRoleCompetIdByRolesId(Integer roleId) {
		return sqlSession.selectList(getMybatisMapperNamespace() + ".getRoleCompetId", roleId);
	}

}
