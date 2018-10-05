package com.interlib.sso.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.interlib.sso.domain.NetReader;

public interface NetReaderDao{
	
	public int getReaderNum(String rdId);
	
	public int getNetReaderNum(String readerId);
	
	public int insertNetReader(NetReader netreader);
	
	public List<NetReader> queryNetReaderList(NetReader netreader);
	
	public List<NetReader> queryBatchNetReaderList(NetReader netreader);
	
	public List<NetReader> queryBatchNetReaderListByReaderType(@Param("rdtype")String rdtype,
			@Param("start")String readerHandleStartDate,
			@Param("end")String readerHandleEndDate);
	
	public int approveReject(List<String> idList);
	
	public List<NetReader> getNetReaders(List<String> idList);
	
	public int approvePass(NetReader netreader);
	
	public int updateCheckPassed(List<String> netreaderIds);
	//这个处理了读者类型和分馆字段为中文说明，关联了读者类型表和分馆表查询
	public NetReader getNetReader(String netreaderId);
	//这个直接查原始代码，不返回中文说明
	public NetReader getNetReaderByRdId(String netreaderId);
	
	//这个直接查原始代码，不返回中文说明,查询netreader for batch ！！！
	public int getBatchNetReader();
	
	public int createAccount(NetReader netreader);
	
	public int deleteNetReader(List<String> netreaderIds);

	
}
