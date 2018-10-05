package com.interlib.sso.service;

import java.util.List;
import java.util.Map;

public interface ScanNetReaderTaskService {
	
	public List<Map<String, Object>> queryCurrentScans();
		
	public int doScan();

	public int doScanForNetReader();
	
	public int batchDeleteNetReaders();
}
