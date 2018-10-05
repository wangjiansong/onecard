package com.interlib.sso.dao;

import java.util.List;
import java.util.Map;

import com.interlib.sso.domain.ReaderType;

public interface ReaderTypeDao {
	
	public List<Map<String,String>> queryReaderTypeList(ReaderType readerType);
	
	public String checkReaderType(String readerType);
	
	public String checkReaderTypeEdit(Map<String,String> params);
	
	public int addReaderType(ReaderType readerType);
	
	public List<Map<String,String>> getLibCode();
	
	public ReaderType getReaderType(String readerType);
	
	public int editReaderType(Map<String,String> params);
	
	public int deleteReaderType(String readerType);

}
