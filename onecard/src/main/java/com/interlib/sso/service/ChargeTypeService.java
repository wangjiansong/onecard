package com.interlib.sso.service;

import java.util.List;

import com.interlib.sso.domain.ChargeType;
import com.interlib.sso.domain.FinType;

public interface ChargeTypeService extends BaseService<ChargeType, String>{

	public List<ChargeType> getAll();
	
	public List<ChargeType> getAllChargeType();
	
	public List<ChargeType> searchChargeType(String[] chargetypes);//2017-09-29 查找出选择了对应的财经类型
	
	public List<ChargeType> getChargeTypesByAppcodes(String[] appcodes);//2017-09-29

	
	public List<ChargeType> getChargeTypeByMultiChargetype(String[] chargetypes);//2017-09-29 查找出选择了对应的财经类型
	
}
