package com.interlib.sso.service;

import com.interlib.sso.domain.ReaderCardInfo;


public interface ReaderCardInfoService extends BaseService<ReaderCardInfo, String>{

	public ReaderCardInfo getByRdId(String rdId) ;
	
	public void deleteByRdId(String rdId);

	public int updateCardInfo(ReaderCardInfo cardInfo);//2014-12-04 根据读者更新读者卡号
}
