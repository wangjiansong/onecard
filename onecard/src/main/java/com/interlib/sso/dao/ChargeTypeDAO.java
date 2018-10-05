package com.interlib.sso.dao;

import java.util.List;

import com.interlib.sso.domain.ChargeType;
import com.interlib.sso.domain.FinType;

public interface ChargeTypeDAO extends BaseDAO<ChargeType, String>{

	public List<ChargeType> getAll();
	
	public List<ChargeType> getAllChargeType();

	public List<ChargeType> searchChargeType(String[] chargetypes);//ADD 2017-09-29
	
	public List<ChargeType> getChargeTypesByAppcodes(String[] appcodes);//ADD 2017-09-29
	
	public List<ChargeType> getChargeTypeByMultiChargetype(String [] chargetypes);
}
