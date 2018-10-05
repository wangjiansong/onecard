package com.interlib.sso.service;

import java.util.List;
import java.util.Map;

import com.interlib.sso.domain.NetReader;
import com.interlib.sso.domain.Reader;
import com.interlib.sso.domain.ReaderType;

public interface NetReaderService{
	
	public int checkReaderIdIsExist(String readerId);
	
	public int insertNetReader(NetReader netreader);
	
	public List<NetReader> queryNetReaderList(NetReader netreader);
	//批量生成查询读者办证时间
	public List<NetReader> queryBatchNetReaderList(NetReader netreader);
	
	//查询根据读者类型
	public List<NetReader> queryBatchNetReaderListByReaderType(String rdtype,
			String readerHandleStartDate,String readerHandleEndDate);
	
	public int approveReject(List<String> idList);
	
	public int approvePass(List<String> idList);
	
	public NetReader getNetReader(String netreaderId);
	
	public int getBatchNetReader();
	
	public NetReader getNetReaderByRdId(String netreaderId);
	
	public int deleteNetReader(List<String> idList);
	
	public NetReader netReaderLogin(NetReader netReader, boolean isPasswordMd5);


}
