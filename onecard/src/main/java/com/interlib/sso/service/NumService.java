package com.interlib.sso.service;

import com.interlib.sso.domain.Num;

public interface NumService extends BaseService<Num, String>{
	
	public int addNum(Num num);
	
	public int updateNum(String id); 
	
	public Num getNum(String id);
}
