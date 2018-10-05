package com.interlib.sso.dao;


import java.util.List;

import com.interlib.sso.domain.SyncRecord;


public interface SyncRecordDAO extends BaseDAO<SyncRecord, String>{
	
	public List<SyncRecord> querySyncRecordList(SyncRecord syncRecord);
	
}
