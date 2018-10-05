package com.interlib.sso.dao;

import java.util.List;
import java.util.Map;

public interface ScanNetReaderTaskDao {
	
	public List<Map<String, Object>> queryCurrentScans();
		
	public int doScan();
	
	public int doScanForNetReader();
	
	public int batchDeleteNetReaders();

}
