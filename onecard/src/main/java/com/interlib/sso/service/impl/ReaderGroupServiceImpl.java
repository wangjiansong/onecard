package com.interlib.sso.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.interlib.sso.dao.ReaderGroupDao;
import com.interlib.sso.domain.Reader;
import com.interlib.sso.domain.ReaderGroup;
import com.interlib.sso.service.ReaderGroupService;

@Service
public class ReaderGroupServiceImpl implements ReaderGroupService {

	@Autowired
	private ReaderGroupDao groupDao;
	
	private static final String[] RDCFSTATEARRAY = {"","有效","验证","丢失","暂停","注销"};
	private static final Map<String, String> RDTYPEMAP = new HashMap<String, String>();
	private static final Map<String, String> RDLIBMAP = new HashMap<String, String>();
	
	public int insertReaderGroup(ReaderGroup group) {
		return groupDao.insertReaderGroup(group);
	}
	
	public List<Map<String,String>> queryGroupList(ReaderGroup group) {
		return groupDao.queryGroupList(group);
	}
	
	public ReaderGroup getGroupById(int groupId) {
		return groupDao.getGroupById(groupId);
	}
	
	public int updateGroup(ReaderGroup group) {
		return groupDao.updateGroup(group);
	}
	
	public int deleteGroup(String[] idArray) {
		return groupDao.deleteGroup(idArray);
	}
	
	public List<Map<String, Object>> queryNoGroupReaderList(Reader reader) {
		if (RDTYPEMAP.isEmpty()) {
			List<Map<String, String>> readerTypeList = groupDao.getReaderType();
			for (Map<String, String> map : readerTypeList) {
				RDTYPEMAP.put(map.get("READERTYPE"), map.get("DESCRIPE"));
			}
		}
		if (RDLIBMAP.isEmpty()) {
			List<Map<String, String>> libTypeList = groupDao.getLibType();
			for (Map<String, String> map : libTypeList) {
				RDLIBMAP.put(map.get("LIBCODE"), map.get("SIMPLENAME"));
			}
		}
		List<Map<String, Object>> list = groupDao.queryNoGroupReaderList(reader);
		for (Map<String, Object> map : list) {
			map.put("RDCFSTATE", RDCFSTATEARRAY[Integer.valueOf(map.get("RDCFSTATE").toString())]);
			map.put("RDTYPE", RDTYPEMAP.get(map.get("RDTYPE")));
			map.put("RDLIB", RDLIBMAP.get(map.get("RDLIB")));
		}
		return list;
	}
	
	public int addMember(Map<String, Object> params) {
		return groupDao.addMember(params);
	}
	
	public List<Map<String, Object>> queryGroupMemberList(Reader reader) {
		if (RDTYPEMAP.isEmpty()) {
			List<Map<String, String>> readerTypeList = groupDao.getReaderType();
			for (Map<String, String> map : readerTypeList) {
				RDTYPEMAP.put(map.get("READERTYPE"), map.get("DESCRIPE"));
			}
		}
		if (RDLIBMAP.isEmpty()) {
			List<Map<String, String>> libTypeList = groupDao.getLibType();
			for (Map<String, String> map : libTypeList) {
				RDLIBMAP.put(map.get("LIBCODE"), map.get("SIMPLENAME"));
			}
		}
		List<Map<String, Object>> list = groupDao.queryGroupMemberList(reader);
		for (Map<String, Object> map : list) {
			map.put("RDCFSTATE", RDCFSTATEARRAY[Integer.valueOf(map.get("RDCFSTATE").toString())]);
			map.put("RDTYPE", RDTYPEMAP.get(map.get("RDTYPE")));
			map.put("RDLIB", RDLIBMAP.get(map.get("RDLIB")));
		}
		return list;
	}
	
	public int deleteMember(String[] rdIdArray) {
		return groupDao.deleteMember(rdIdArray);
	}

}
