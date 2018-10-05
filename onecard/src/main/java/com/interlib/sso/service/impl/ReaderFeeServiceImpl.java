package com.interlib.sso.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.interlib.sso.dao.ReaderFeeDAO;
import com.interlib.sso.domain.ReaderFee;
import com.interlib.sso.service.ReaderFeeService;

@Service
public class ReaderFeeServiceImpl 
			extends AbstractBaseServiceImpl<ReaderFee, String> implements ReaderFeeService {

	@Autowired
	public ReaderFeeDAO readerFeeDAO;
	

	@Autowired
	public void setBaseDAO(ReaderFeeDAO readerFeeDAO) {
		super.setBaseDAO(readerFeeDAO);
	}
	
}
