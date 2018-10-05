package com.interlib.sso.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.interlib.sso.dao.RdAccountDAO;
import com.interlib.sso.dao.ReaderCardInfoDAO;
import com.interlib.sso.domain.RdAccount;
import com.interlib.sso.domain.ReaderCardInfo;
import com.interlib.sso.service.ReaderCardInfoService;

@Service
public class ReaderCardInfoServiceImpl 
			extends AbstractBaseServiceImpl<ReaderCardInfo, String> implements ReaderCardInfoService {

	@Autowired
	public ReaderCardInfoDAO readerCardInfoDAO;
	
	@Autowired
	public RdAccountDAO rdAccountDAO;
	

	@Autowired
	public void setBaseDAO(ReaderCardInfoDAO readerCardInfoDAO) {
		super.setBaseDAO(readerCardInfoDAO);
	}

	@Override
	public ReaderCardInfo getByRdId(String rdId) {
		
		return readerCardInfoDAO.getByRdId(rdId);
	}

	@Override
	public void deleteByRdId(String rdId) {
		readerCardInfoDAO.deleteByRdId(rdId);
		//下面的操作是为了给桂林旅专学院更新专门处理的，最新的版本在controller处理了，但因为版本更新问题，
		//先在下面处理，方便更新 20161011
		ReaderCardInfo readerCardInfo = readerCardInfoDAO.getByRdId(rdId);
		if(readerCardInfo != null) {
			readerCardInfo.setIsUsable(0);
			readerCardInfoDAO.update(readerCardInfo);
		}
		RdAccount rdAccount = rdAccountDAO.get(rdId);
		rdAccount.setStatus(4);
		rdAccountDAO.update(rdAccount);
	}

	@Override
	public int updateCardInfo(ReaderCardInfo cardInfo) {
		
		return readerCardInfoDAO.updateCardInfo(cardInfo);
	}
	
	
	
}
