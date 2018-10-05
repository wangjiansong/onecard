package com.interlib.sso.service;

import java.util.List;
import java.util.Map;

import com.interlib.sso.domain.Reader;
import com.interlib.sso.domain.ReaderGroup;

public interface ReaderGroupService {

	public int insertReaderGroup(ReaderGroup group);
	
	public List<Map<String, String>> queryGroupList(ReaderGroup group);
	
	public ReaderGroup getGroupById(int groupId);
	
	public int updateGroup(ReaderGroup group);
	
	public int deleteGroup(String[] idArray);
	
	public List<Map<String, Object>> queryNoGroupReaderList(Reader reader);
	
	public int addMember(Map<String, Object> params);
	
	public List<Map<String, Object>> queryGroupMemberList(Reader reader);
	
	public int deleteMember(String[] rdIdArray);
	
}
