package com.interlib.sso.service;


import java.util.List;

import com.interlib.sso.domain.SyncRecord;


public interface SyncRecordService extends BaseService<SyncRecord, String> {

	public List<SyncRecord> querySyncRecordList(SyncRecord syncRecord);
}
