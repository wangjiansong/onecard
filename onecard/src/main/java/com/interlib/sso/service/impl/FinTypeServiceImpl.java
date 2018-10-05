package com.interlib.sso.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.interlib.sso.dao.FinTypeDAO;
import com.interlib.sso.domain.FinType;
import com.interlib.sso.service.FinTypeService;
@Service
public class FinTypeServiceImpl 
		extends AbstractBaseServiceImpl<FinType, String> implements FinTypeService {

	@Autowired
	public FinTypeDAO finTypeDAO;
	
	@Autowired
	public void setBaseDAO(FinTypeDAO finTypeDAO) {
		super.setBaseDAO(finTypeDAO);
	}

	@Override
	public List<FinType> getAll() {
		return finTypeDAO.getAll();
	}

	@Override
	public List<FinType> getAllFeeType() {
		return finTypeDAO.getAllFeeType();
	}

	@Override
	public List<FinType> searchFeeType(String[] feetypes) {
		
		return finTypeDAO.searchFeeType(feetypes);
	}

	@Override
	public List<FinType> getFinTypesByAppcodes(String[] appcodes) {
		
		return finTypeDAO.getFinTypesByAppcodes(appcodes);
	}

	@Override
	public List<FinType> getFeeTypeByMultiFeetype(String[] feetypes) {
		return finTypeDAO.getFeeTypeByMultiFeetype(feetypes);
	}
	
}
