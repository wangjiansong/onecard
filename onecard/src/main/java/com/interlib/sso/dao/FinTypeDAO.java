package com.interlib.sso.dao;

import java.util.List;
import java.util.Map;

import com.interlib.sso.domain.FinType;

public interface FinTypeDAO extends BaseDAO<FinType, String>{

	public List<FinType> getAll();
	
	public List<FinType> getAllFeeType();

	public List<FinType> searchFeeType(String[] feetypes);//ADD 2014-05-07

	public List<FinType> getFinTypesByAppcodes(String[] appcodes);//2015-1-8
	
	public List<FinType> getFeeTypeByMultiFeetype(String [] feetypes);
}
