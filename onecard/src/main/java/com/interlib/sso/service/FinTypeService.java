package com.interlib.sso.service;

import java.util.List;

import com.interlib.sso.domain.FinType;

public interface FinTypeService extends BaseService<FinType, String>{

	public List<FinType> getAll();
	
	public List<FinType> getAllFeeType();
	
	public List<FinType> searchFeeType(String[] feetypes);//2014-05-07 查找出选择了对应的财经类型

	public List<FinType> getFinTypesByAppcodes(String[] appcodes);//2015-1-8
	
	public List<FinType> getFeeTypeByMultiFeetype(String[] feetypes);//2014-05-07 查找出选择了对应的财经类型
	
}
