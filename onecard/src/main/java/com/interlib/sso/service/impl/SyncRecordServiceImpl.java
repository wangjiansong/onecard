package com.interlib.sso.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.interlib.sso.dao.SyncRecordDAO;
import com.interlib.sso.domain.SyncRecord;
import com.interlib.sso.service.SyncRecordService;

@Service
public class SyncRecordServiceImpl 
		extends AbstractBaseServiceImpl<SyncRecord, String> implements SyncRecordService {

	@Autowired
	public SyncRecordDAO syncRecordDAO;
	
	
	@Autowired
	public void setBaseDAO(SyncRecordDAO syncRecordDAO) {
		super.setBaseDAO(syncRecordDAO);
	}


	@Override
	public List<SyncRecord> querySyncRecordList(SyncRecord syncRecord) {
		return syncRecordDAO.querySyncRecordList(syncRecord);
	}

	
}
