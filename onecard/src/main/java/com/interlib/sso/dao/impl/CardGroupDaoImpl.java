package com.interlib.sso.dao.impl;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.interlib.sso.dao.CardGroupDao;
import com.interlib.sso.domain.Reader;
import com.interlib.sso.domain.CardGroup;

@SuppressWarnings("unchecked")
@Repository
public class CardGroupDaoImpl implements CardGroupDao {
	
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
		return "CardGroupMapper";
	}

	@Override
	public int insertGroup(CardGroup group) {
		return this.getSqlSession().insert(getMybatisMapperNamespace() + ".insertGroup", group);
	}
	
	public List<Map<String, String>> queryGroupList(CardGroup group) {
		return this.getSqlSession().selectList(getMybatisMapperNamespace() + ".queryGroupList", group);
	}
	
	public CardGroup getGroupById(int groupId) {
		return (CardGroup) this.getSqlSession().selectOne(getMybatisMapperNamespace() + ".getGroupById", groupId);
	}
	
	public int updateGroup(CardGroup group) {
		return this.getSqlSession().update(getMybatisMapperNamespace() + ".updateGroup", group);
	}
	
	public int deleteGroup(String[] idArray) {
		return this.getSqlSession().delete(getMybatisMapperNamespace() + ".deleteGroup", idArray);
	}
	
	public int deleteGroupMember(String[] idArray) {
		return this.getSqlSession().delete(getMybatisMapperNamespace() + ".deleteGroupMember", idArray);
	}
	
	public int deleteGroupAssign(String[] idArray) {
		return this.getSqlSession().delete(getMybatisMapperNamespace() + ".deleteGroupAssign", idArray);
	}
	
	public List<Map<String, Object>> queryNoGroupReaderList(Reader reader) {
		return this.getSqlSession().selectList(getMybatisMapperNamespace() + ".queryNoGroupReaderList", reader);
	}
	
	public int addMember(List<Map<String, Object>> params) {
		return this.getSqlSession().update(getMybatisMapperNamespace() + ".addMember", params);
	}
	
	public List<Map<String, Object>> queryGroupMemberList(Reader reader) {
		return this.getSqlSession().selectList(getMybatisMapperNamespace() + ".queryGroupMemberList", reader);
	}
	
	public int deleteMember(Map<String, Object> params) {
		return this.getSqlSession().delete(getMybatisMapperNamespace() + ".deleteMember", params);
	}
	
	public List<Map<String, String>> getReaderType() {
		return this.getSqlSession().selectList(getMybatisMapperNamespace() + ".getReaderType");
	}
	
	public List<Map<String, String>> getLibType() {
		return this.getSqlSession().selectList(getMybatisMapperNamespace() + ".getLibType");
	}

}
