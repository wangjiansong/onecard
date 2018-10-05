package com.interlib.sso.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.interlib.sso.dao.ReaderTypeDao;
import com.interlib.sso.domain.Reader;
import com.interlib.sso.domain.ReaderType;
import com.interlib.sso.service.ReaderTypeService;

@Service
public class ReaderTypeServiceImpl extends AbstractBaseServiceImpl<ReaderType, String> 
		implements ReaderTypeService {
	
	@Autowired
	private ReaderTypeDao readerTypeDao;
	
	public void setReaderTypeDao(ReaderTypeDao readerTypeDao) {
		this.readerTypeDao = readerTypeDao;
	}

	public List<Map<String,String>> queryReaderTypeList(ReaderType readerType) {
		return readerTypeDao.queryReaderTypeList(readerType);
	}
	
	public String checkReaderType(String readerType) {
		return readerTypeDao.checkReaderType(readerType);
	}
	
	public String checkReaderTypeEdit(Map<String,String> params) {
		return readerTypeDao.checkReaderTypeEdit(params);
	}

	public int addReaderType(ReaderType readerType) {
		return readerTypeDao.addReaderType(readerType);
	}
	
	public List<Map<String,String>> getLibCode() {
		return readerTypeDao.getLibCode();
	}
	
	public ReaderType getReaderType(String readerType) {
		return readerTypeDao.getReaderType(readerType);
	}
	
	public int editReaderType(Map<String,String> params) {
		return readerTypeDao.editReaderType(params);
	}
	
	public int deleteReaderType(String readerType) {
		return readerTypeDao.deleteReaderType(readerType);
	}
	
}
