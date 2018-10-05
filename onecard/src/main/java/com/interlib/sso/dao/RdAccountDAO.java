package com.interlib.sso.dao;

import java.util.List;

import com.interlib.sso.domain.RdAccount;

public interface RdAccountDAO extends BaseDAO<RdAccount, String> {

	public List<RdAccount> queryRdAccountList(RdAccount rdAccount);
}
