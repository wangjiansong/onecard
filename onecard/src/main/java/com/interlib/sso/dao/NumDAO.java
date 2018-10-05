package com.interlib.sso.dao;

import com.interlib.sso.domain.Num;

/**
 * 证号相关的数据处理接口
 */
public interface NumDAO extends BaseDAO<Num, String>{
	
	
	public int addNum(Num num);
	public int updateNum(String id);
	public Num getNum(String id);

}
