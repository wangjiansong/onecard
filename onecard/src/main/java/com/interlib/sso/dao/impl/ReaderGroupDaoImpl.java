package com.interlib.sso.dao.impl;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.interlib.sso.dao.ReaderGroupDao;
import com.interlib.sso.domain.Reader;
import com.interlib.sso.domain.ReaderGroup;

@SuppressWarnings("unchecked")
@Repository
public class ReaderGroupDaoImpl implements ReaderGroupDao {

	@Autowired
	public SqlSession sqlSession;
	
	/**
	 * 获取sqlSession
	 */
	public SqlSession getSqlSession() {
		return sqlSession;
	}

	/**
	 * 配置命名空间
	 */
	public String getMybatisMapperNamespace() {
		return "ReaderGroupMapper";
	}

	@Override
	public int insertReaderGroup(ReaderGroup group) {
		return this.getSqlSession().insert(getMybatisMapperNamespace() + ".insertReaderGroup", group);
	}
	
	public List<Map<String, String>> queryGroupList(ReaderGroup group) {
		return this.getSqlSession().selectList(getMybatisMapperNamespace() + ".queryGroupList", group);
	}
	
	public ReaderGroup getGroupById(int groupId) {
		return (ReaderGroup) this.getSqlSession().selectOne(getMybatisMapperNamespace() + ".getGroupById", groupId);
	}
	
	public int updateGroup(ReaderGroup group) {
		return this.getSqlSession().update(getMybatisMapperNamespace() + ".updateGroup", group);
	}
	
	public int deleteGroup(String[] idArray) {
		return this.getSqlSession().delete(getMybatisMapperNamespace() + ".deleteGroup", idArray);
	}
	
	public List<Map<String, Object>> queryNoGroupReaderList(Reader reader) {
		return this.getSqlSession().selectList(getMybatisMapperNamespace() + ".queryNoGroupReaderList", reader);
	}
	
	public int addMember(Map<String, Object> params) {
		return this.getSqlSession().update(getMybatisMapperNamespace() + ".addMember", params);
	}
	
	public List<Map<String, Object>> queryGroupMemberList(Reader reader) {
		return this.getSqlSession().selectList(getMybatisMapperNamespace() + ".queryGroupMemberList", reader);
	}
	
	public int deleteMember(String[] rdIdArray) {
		return this.getSqlSession().delete(getMybatisMapperNamespace() + ".deleteMember", rdIdArray);
	}
	
	public List<Map<String, String>> getReaderType() {
		return this.getSqlSession().selectList(getMybatisMapperNamespace() + ".getReaderType");
	}
	
	public List<Map<String, String>> getLibType() {
		return this.getSqlSession().selectList(getMybatisMapperNamespace() + ".getLibType");
	}

}
