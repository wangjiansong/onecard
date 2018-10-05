package com.interlib.sso.service;

import java.util.List;

import com.interlib.sso.domain.RdAccount;

public interface RdAccountService extends BaseService<RdAccount, String> {

	public List<RdAccount> queryRdAccountList(RdAccount rdAccount);
}
