package com.interlib.sso.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.interlib.sso.dao.ChargeTypeDAO;
import com.interlib.sso.dao.FinTypeDAO;
import com.interlib.sso.domain.ChargeType;
import com.interlib.sso.domain.FinType;
import com.interlib.sso.service.ChargeTypeService;
@Service
public class ChargeTypeServiceImpl 
		extends AbstractBaseServiceImpl<ChargeType, String> implements ChargeTypeService {

	@Autowired
	public ChargeTypeDAO chargeTypeDAO;
	
	@Autowired
	public void setBaseDAO(ChargeTypeDAO chargeTypeDAO) {
		super.setBaseDAO(chargeTypeDAO);
	}

	@Override
	public List<ChargeType> getAll() {
		return chargeTypeDAO.getAll();
	}

	@Override
	public List<ChargeType> getAllChargeType() {
		return chargeTypeDAO.getAllChargeType();
	}

	@Override
	public List<ChargeType> searchChargeType(String[] chargetypes) {
		
		return chargeTypeDAO.searchChargeType(chargetypes);
	}

	@Override
	public List<ChargeType> getChargeTypesByAppcodes(String[] appcodes) {
		
		return chargeTypeDAO.getChargeTypesByAppcodes(appcodes);
	}

	@Override
	public List<ChargeType> getChargeTypeByMultiChargetype(String[] chargetypes) {
		return chargeTypeDAO.getChargeTypeByMultiChargetype(chargetypes);
	}
	
}
