package com.interlib.sso.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.interlib.sso.dao.LibCodeDao;
import com.interlib.sso.domain.LibCode;
import com.interlib.sso.service.LibCodeService;

@Service
public class LibCodeServiceImpl implements LibCodeService {
	
	@Autowired
	private LibCodeDao dao;
	
	public String checkLibCode(String libCode) {
		return dao.checkLibCode(libCode);
	}
	
	public int insertLibCode(LibCode libCode) {
		return dao.insertLibCode(libCode);
	}
	
	public List<Map<String,String>> queryLibCodeList(LibCode libCode) {
		return dao.queryLibCodeList(libCode);
	}
	
	public LibCode getLibByCode(String libCode) {
		return dao.getLibByCode(libCode);
	}
	
	public String checkLibCodeEdit(String libCode) {
		return dao.checkLibCodeEdit(libCode);
	}

	public int editLibCode(LibCode libCode) {
		return dao.editLibCode(libCode);
	}
	
	public int deleteLibCode(String libCode) {
		return dao.deleteLibCode(libCode);
	}

	@Override
	public List<LibCode> getSimpleInfo() {
		return dao.getSimpleInfo();
	}

	@Override
	public List<LibCode> getStaticsLibCodeSet() {
		
		return dao.getStaticsLibCodeSet();
	}

	
	@Override
	public List<LibCode> getSelectLibs(Map<String, String> map) {
		
		return dao.getSelectLibs(map);
	}

	@Override
	public List<LibCode> getLibCodeSet() {
		
		return dao. getLibCodeSet();
	}
	

}
