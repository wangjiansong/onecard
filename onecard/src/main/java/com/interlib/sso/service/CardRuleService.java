package com.interlib.sso.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.interlib.sso.domain.CardRule;
import com.interlib.sso.domain.CardRuleDetail;

public interface CardRuleService {
	
	public int getRuleId();
	
	public int insertCardRule(CardRule rule);
	
	public int insertCardRuleDetail(List<CardRuleDetail> params);
	
	public List<Map<String, String>> queryCardRuleList(CardRule rule);
	
	public int deleteCardRule(String[] idArray);
	
	public int deleteCardRuleDetail(String[] idArray);
	
	public int deleteCardRuleAssign(String[] idArray);
	
	public Map<String, String> getCardRuleById(int ruleId);
	
	public List<Map<String, String>> getCardRuleDetails(int ruleId);
	
	public int updateCardRule(CardRule rule);
	
	public List<Map<String, String>> queryAssignGroupList(CardRule rule);
	
	public List<Map<String, String>> queryUnAssignedGroupList(CardRule rule);
	
	public int insertAssignGroup(List<Map<String, Object>> paramList);
	
	public int updateGroupAssigned(String[] groupIds);
	
	public int deleteAssignGroup(Map<String, Object> params);
	
	public int updateGroupUnAssigned(String[] groupIds);
	
	public CardRuleDetail slotCard(String rdId, Date slotTime);
	
}
