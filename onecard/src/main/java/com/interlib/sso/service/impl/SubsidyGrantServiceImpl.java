package com.interlib.sso.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.interlib.sso.dao.SubsidyGrantDao;
import com.interlib.sso.domain.ReaderGroup;
import com.interlib.sso.domain.SubsidyGrant;
import com.interlib.sso.service.SubsidyGrantService;

@Service
public class SubsidyGrantServiceImpl implements SubsidyGrantService {
	
	@Autowired
	private SubsidyGrantDao grantDao;
	
	public int getGrantId() {
		return grantDao.getGrantId();
	}
	
	public int insertGrant(SubsidyGrant grant) {
		return grantDao.insertGrant(grant);
	}
	
	public int insertGrantDate(List<Map<String, Object>> paraList) {
		return grantDao.insertGrantDate(paraList);
	}
	
	public List<Map<String, String>> queryGrantList(SubsidyGrant grant) {
		return grantDao.queryGrantList(grant);
	}
	
	public SubsidyGrant getGrantById(int grantId) {
		return grantDao.getGrantById(grantId);
	}
	
	public List<String> getGrantDatesById(int grantId) {
		return grantDao.getGrantDatesById(grantId);
	}
	
	public int deleteGrantDateById(int grantId) {
		return grantDao.deleteGrantDateById(grantId);
	}
	
	public int updateGrantById(SubsidyGrant grant) {
		return grantDao.updateGrantById(grant);
	}
	
	public int deleteGrantByIdArray(String[] idArray) {
		return grantDao.deleteGrantByIdArray(idArray);
	}
	
	public int deleteGrantDateByIdArray(String[] idArray) {
		return grantDao.deleteGrantDateByIdArray(idArray);
	}
	
	public List<Map<String, String>> queryUnassignedGroupList(ReaderGroup group) {
		return grantDao.queryUnassignedGroupList(group);
	}
	
	public int insertAssignGroup(List<Map<String, Object>> paramList) {
		return grantDao.insertAssignGroup(paramList);
	}
	
	public List<Map<String, String>> queryAssignGroupList(ReaderGroup group) {
		return grantDao.queryAssignGroupList(group);
	}
	
	public int deleteAssignGroup(Map<String, Object> param) {
		return grantDao.deleteAssignGroup(param);
	}

}
