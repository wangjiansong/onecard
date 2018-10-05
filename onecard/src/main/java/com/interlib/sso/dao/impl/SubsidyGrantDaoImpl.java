package com.interlib.sso.dao.impl;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.interlib.sso.dao.SubsidyGrantDao;
import com.interlib.sso.domain.ReaderGroup;
import com.interlib.sso.domain.SubsidyGrant;

@SuppressWarnings("unchecked")
@Repository
public class SubsidyGrantDaoImpl implements SubsidyGrantDao {

	@Autowired
	public SqlSession sqlSession;
	
	/**
	 * 获取sqlSession
	 */
	private SqlSession getSqlSession() {
		return sqlSession;
	}

	/**
	 * 配置命名空间
	 */
	private String getMybatisMapperNamespace() {
		return "SubsidyGrantMapper";
	}
	
	public int getGrantId() {
		return Integer.parseInt(this.getSqlSession().selectOne(getMybatisMapperNamespace() + ".getGrantId").toString());
	}
	
	public int insertGrant(SubsidyGrant grant) {
		return this.getSqlSession().insert(getMybatisMapperNamespace() + ".insertGrant", grant);
	}
	
	public int insertGrantDate(List<Map<String, Object>> paraList) {
		return this.getSqlSession().insert(getMybatisMapperNamespace() + ".insertGrantDate", paraList);
	}
	
	
	public List<Map<String, String>> queryGrantList(SubsidyGrant grant) {
		return this.getSqlSession().selectList(getMybatisMapperNamespace() + ".queryGrantList", grant);
	}
	
	public SubsidyGrant getGrantById(int grantId) {
		return (SubsidyGrant) this.getSqlSession().selectOne(getMybatisMapperNamespace() + ".getGrantById", grantId);
	}
	
	public List<String> getGrantDatesById(int grantId) {
		return this.getSqlSession().selectList(getMybatisMapperNamespace() + ".getGrantDatesById", grantId);
	}
	
	public int deleteGrantDateById(int grantId) {
		return this.getSqlSession().delete(getMybatisMapperNamespace() + ".deleteGrantDateById", grantId);
	}
	
	public int updateGrantById(SubsidyGrant grant) {
		return this.getSqlSession().update(getMybatisMapperNamespace() + ".updateGrantById", grant);
	}
	
	public int deleteGrantByIdArray(String[] idArray) {
		return this.getSqlSession().delete(getMybatisMapperNamespace() + ".deleteGrantByIdArray", idArray);
	}
	
	public int deleteGrantDateByIdArray(String[] idArray) {
		return this.getSqlSession().delete(getMybatisMapperNamespace() + ".deleteGrantDateByIdArray", idArray);
	}
	
	public List<Map<String, String>> queryUnassignedGroupList(ReaderGroup group) {
		return this.getSqlSession().selectList(getMybatisMapperNamespace() + ".queryUnassignedGroupList", group);
	}
	
	public int insertAssignGroup(List<Map<String, Object>> paramList) {
		return this.getSqlSession().insert(getMybatisMapperNamespace() + ".insertAssignGroup", paramList);
	}
	
	public List<Map<String, String>> queryAssignGroupList(ReaderGroup group) {
		return this.getSqlSession().selectList(getMybatisMapperNamespace() + ".queryAssignGroupList", group);
	}
	
	public int deleteAssignGroup(Map<String, Object> param) {
		return this.getSqlSession().delete(getMybatisMapperNamespace() + ".deleteAssignGroup", param);
	}
	
}
