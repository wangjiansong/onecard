package com.interlib.sso.dao;

import java.util.List;
import java.util.Map;

import com.interlib.sso.domain.LibCode;

public interface LibCodeDao {

	public String checkLibCode(String libCode);
	
	public int insertLibCode(LibCode libCode);
	
	public List<Map<String,String>> queryLibCodeList(LibCode libCode);
	
	public LibCode getLibByCode(String libCode);
	
	public String checkLibCodeEdit(String libCode);
	
	public int editLibCode(LibCode libCode);
	
	public int deleteLibCode(String libCode);
	
	public List<LibCode> getSimpleInfo();

	public List<LibCode> getStaticsLibCodeSet();


	public List<LibCode> getSelectLibs(Map<String, String> map);//2014-06-17

	public List<LibCode> getLibCodeSet();
	
	
	
}
