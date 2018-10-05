package com.interlib.sso.webservice.impl;

import javax.annotation.Resource;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.xml.ws.WebServiceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;

import com.interlib.sso.domain.RdAccount;
import com.interlib.sso.webservice.FinanceWebservice;

@WebService(endpointInterface="com.interlib.sso.webservice.FinanceWebservice")
public class FinanceWebserviceImpl implements FinanceWebservice {
	
	@Autowired
	private MessageSourceAccessor messageSourceAccessor;
	
	@Resource
	WebServiceContext webServiceContext;

	@Override
	public RdAccount queryReaderAccount(@WebParam(name = "rdid") String rdid) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String deduction(@WebParam(name = "rdid") String rdid) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String chargeReaderAccount(@WebParam(name = "rdid") String rdid) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
}
