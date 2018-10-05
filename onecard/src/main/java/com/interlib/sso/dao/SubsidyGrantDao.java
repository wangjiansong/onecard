package com.interlib.sso.dao;

import java.util.List;
import java.util.Map;

import com.interlib.sso.domain.ReaderGroup;
import com.interlib.sso.domain.SubsidyGrant;

public interface SubsidyGrantDao {
	
	public int getGrantId();
	
	public int insertGrant(SubsidyGrant grant);
	
	public int insertGrantDate(List<Map<String, Object>> paraList);
	
	public List<Map<String, String>> queryGrantList(SubsidyGrant grant);
	
	public SubsidyGrant getGrantById(int grantId);
	
	public List<String> getGrantDatesById(int grantId);
	
	public int deleteGrantDateById(int grantId);
	
	public int updateGrantById(SubsidyGrant grant);
	
	public int deleteGrantByIdArray(String[] idArray);
	
	public int deleteGrantDateByIdArray(String[] idArray);
	
	public List<Map<String, String>> queryUnassignedGroupList(ReaderGroup group);
	
	public int insertAssignGroup(List<Map<String, Object>> paramList);
	
	public List<Map<String, String>> queryAssignGroupList(ReaderGroup group);
	
	public int deleteAssignGroup(Map<String, Object> param);

}
