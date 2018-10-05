package com.interlib.sso.dao;

import com.interlib.sso.domain.ReaderCardInfo;

public interface ReaderCardInfoDAO extends BaseDAO<ReaderCardInfo, String> {
	
	public ReaderCardInfo getByRdId(String rdId) ;
	
	public void deleteByRdId(String rdId);

	public int updateCardInfo(ReaderCardInfo cardInfo);
}
