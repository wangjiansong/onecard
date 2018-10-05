package com.interlib.sso.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.interlib.sso.dao.RdAccountDAO;
import com.interlib.sso.domain.RdAccount;
import com.interlib.sso.service.RdAccountService;

@Service
public class RdAccountServiceImpl 
			extends AbstractBaseServiceImpl<RdAccount, String> implements RdAccountService {

	@Autowired
	private RdAccountDAO rdAccountDAO;
	
	
	public void setRdAccountDAO(RdAccountDAO rdAccountDAO) {
		this.rdAccountDAO = rdAccountDAO;
	}

	@Autowired
	public void setBaseDAO(RdAccountDAO rdAccountDAO) {
		super.setBaseDAO(rdAccountDAO);
	}
	
	@Override
	public List<RdAccount> queryRdAccountList(RdAccount rdAccount) {
		return rdAccountDAO.queryRdAccountList(rdAccount);
	}

}
