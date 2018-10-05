package com.interlib.sso.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.interlib.sso.dao.ScanNetReaderTaskDao;
import com.interlib.sso.service.ScanNetReaderTaskService;
@Service
public class ScanNetReaderTaskServiceImpl implements ScanNetReaderTaskService {
	@Autowired
	private ScanNetReaderTaskDao scanNetReaderTaskDao;

	public void setScanNetReaderTaskDao(ScanNetReaderTaskDao scanNetReaderTaskDao) {
		this.scanNetReaderTaskDao = scanNetReaderTaskDao;
	}

	public List<Map<String, Object>> queryCurrentScans() {
		return scanNetReaderTaskDao.queryCurrentScans();
	}
	
	public int doScan() {
		return scanNetReaderTaskDao.doScan();
	}
	public int doScanForNetReader(){
		return scanNetReaderTaskDao.doScanForNetReader();

	}
	public int batchDeleteNetReaders(){
		return scanNetReaderTaskDao.batchDeleteNetReaders();
	}
}
