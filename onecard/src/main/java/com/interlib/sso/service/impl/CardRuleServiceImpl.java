package com.interlib.sso.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.interlib.sso.common.TimeUtils;
import com.interlib.sso.dao.CardRuleDao;
import com.interlib.sso.domain.CardRule;
import com.interlib.sso.domain.CardRuleDetail;
import com.interlib.sso.service.CardRuleService;

@Service
public class CardRuleServiceImpl implements CardRuleService {
	
	@Autowired
	private CardRuleDao ruleDao;
	
	public int getRuleId() {
		return ruleDao.getRuleId();
	}
	
	public int insertCardRule(CardRule rule) {
		return ruleDao.insertCardRule(rule);
	}
	
	public int insertCardRuleDetail(List<CardRuleDetail> params) {
		return ruleDao.insertCardRuleDetail(params);
	}
	
	public List<Map<String, String>> queryCardRuleList(CardRule rule) {
		return ruleDao.queryCardRuleList(rule);
	}
	
	public int deleteCardRule(String[] idArray) {
		return ruleDao.deleteCardRule(idArray);
	}
	
	public int deleteCardRuleDetail(String[] idArray) {
		return ruleDao.deleteCardRuleDetail(idArray);
	}
	
	public int deleteCardRuleAssign(String[] idArray) {
		return ruleDao.deleteCardRuleAssign(idArray);
	}
	
	public Map<String, String> getCardRuleById(int ruleId) {
		return ruleDao.getCardRuleById(ruleId);
	}
	
	public List<Map<String, String>> getCardRuleDetails(int ruleId) {
		return ruleDao.getCardRuleDetails(ruleId);
	}
	
	public int updateCardRule(CardRule rule) {
		return ruleDao.updateCardRule(rule);
	}
	
	public List<Map<String, String>> queryAssignGroupList(CardRule rule) {
		return ruleDao.queryAssignGroupList(rule);
	}
	
	public List<Map<String, String>> queryUnAssignedGroupList(CardRule rule) {
		return ruleDao.queryUnAssignedGroupList(rule);
	}
	
	public int insertAssignGroup(List<Map<String, Object>> paramList) {
		return ruleDao.insertAssignGroup(paramList);
	}
	
	public int updateGroupAssigned(String[] groupIds) {
		return ruleDao.updateGroupAssigned(groupIds);
	}
	
	public int deleteAssignGroup(Map<String, Object> params) {
		return ruleDao.deleteAssignGroup(params);
	}
	
	public int updateGroupUnAssigned(String[] groupIds) {
		return ruleDao.updateGroupUnAssigned(groupIds);
	}
	
	public CardRuleDetail slotCard(String rdId, Date slotTime) {
		if (rdId == null || "".equals(rdId) || slotTime == null) {
			return null;
		}
		String todayStr = TimeUtils.dateToString(slotTime, TimeUtils.YYYYMMDD);
		Map<String, Object> params = new HashMap<String, Object>(3);
		params.put("rdId", rdId);
		params.put("slotTime", slotTime);
		params.put("todayStr", todayStr);
		List<CardRuleDetail> details = ruleDao.getSlotDetail(params);
		CardRuleDetail detail = null;
		if (details == null || details.isEmpty() || details.size() > 1) {
			detail = null;
		} else {
			detail = details.get(0);
		}
		return detail;
	}

}
