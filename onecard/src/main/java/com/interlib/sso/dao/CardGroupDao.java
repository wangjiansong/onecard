package com.interlib.sso.dao;

import java.util.List;
import java.util.Map;

import com.interlib.sso.domain.Reader;
import com.interlib.sso.domain.CardGroup;

public interface CardGroupDao {

	public int insertGroup(CardGroup group);
	
	public List<Map<String, String>> queryGroupList(CardGroup group);
	
	public CardGroup getGroupById(int groupId);
	
	public int updateGroup(CardGroup group);
	
	public int deleteGroup(String[] idArray);
	
	public int deleteGroupMember(String[] idArray);
	
	public int deleteGroupAssign(String[] idArray);
	
	public List<Map<String, Object>> queryNoGroupReaderList(Reader reader);
	
	public int addMember(List<Map<String, Object>> params);
	
	public List<Map<String, Object>> queryGroupMemberList(Reader reader);
	
	public int deleteMember(Map<String, Object> params);
	
	public List<Map<String, String>> getReaderType();
	
	public List<Map<String, String>> getLibType();
	
}
