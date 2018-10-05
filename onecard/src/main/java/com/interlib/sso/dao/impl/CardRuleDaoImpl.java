package com.interlib.sso.dao.impl;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.interlib.sso.dao.CardRuleDao;
import com.interlib.sso.domain.CardRule;
import com.interlib.sso.domain.CardRuleDetail;

@SuppressWarnings("unchecked")
@Repository
public class CardRuleDaoImpl implements CardRuleDao {

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
		return "CardRuleMapper";
	}
	
	public int getRuleId() {
		return (Integer) this.getSqlSession().selectOne(getMybatisMapperNamespace() + ".getRuleId");
	}
	
	public int insertCardRule(CardRule rule) {
		return this.getSqlSession().insert(getMybatisMapperNamespace() + ".insertCardRule", rule);
	}
	
	public int insertCardRuleDetail(List<CardRuleDetail> params) {
		return this.getSqlSession().insert(getMybatisMapperNamespace() + ".insertCardRuleDetail", params);
	}
	
	public List<Map<String, String>> queryCardRuleList(CardRule rule) {
		return this.getSqlSession().selectList(getMybatisMapperNamespace() + ".queryCardRuleList", rule);
	}
	
	public int deleteCardRule(String[] idArray) {
		return this.getSqlSession().delete(getMybatisMapperNamespace() + ".deleteCardRule", idArray);
	}
	
	public int deleteCardRuleDetail(String[] idArray) {
		return this.getSqlSession().delete(getMybatisMapperNamespace() + ".deleteCardRuleDetail", idArray);
	}
	
	public int deleteCardRuleAssign(String[] idArray) {
		return this.getSqlSession().delete(getMybatisMapperNamespace() + ".deleteCardRuleAssign", idArray);
	}
	
	public Map<String, String> getCardRuleById(int ruleId) {
		return (Map<String, String>) this.getSqlSession().selectOne(getMybatisMapperNamespace() + ".getCardRuleById", ruleId);
	}
	
	public List<Map<String, String>> getCardRuleDetails(int ruleId) {
		return this.getSqlSession().selectList(getMybatisMapperNamespace() + ".getCardRuleDetails", ruleId);
	}
	
	public int updateCardRule(CardRule rule) {
		return this.getSqlSession().update(getMybatisMapperNamespace() + ".updateCardRule", rule);
	}
	
	public List<Map<String, String>> queryAssignGroupList(CardRule rule) {
		return this.getSqlSession().selectList(getMybatisMapperNamespace() + ".queryAssignGroupList", rule);
	}
	
	public List<Map<String, String>> queryUnAssignedGroupList(CardRule rule) {
		return this.getSqlSession().selectList(getMybatisMapperNamespace() + ".queryUnAssignedGroupList", rule);
	}
	
	public int insertAssignGroup(List<Map<String, Object>> paramList) {
		return this.getSqlSession().insert(getMybatisMapperNamespace() + ".insertAssignGroup", paramList);
	}
	
	public int updateGroupAssigned(String[] groupIds) {
		return this.getSqlSession().update(getMybatisMapperNamespace() + ".updateGroupAssigned", groupIds);
	}
	
	public int deleteAssignGroup(Map<String, Object> params) {
		return this.getSqlSession().delete(getMybatisMapperNamespace() + ".deleteAssignGroup", params);
	}
	
	public int updateGroupUnAssigned(String[] groupIds) {
		return this.getSqlSession().update(getMybatisMapperNamespace() + ".updateGroupUnAssigned", groupIds);
	}
	
	public List<CardRuleDetail> getSlotDetail(Map<String, Object> params) {
		return this.getSqlSession().selectList(getMybatisMapperNamespace() + ".getSlotDetail", params);
	}
	
}
