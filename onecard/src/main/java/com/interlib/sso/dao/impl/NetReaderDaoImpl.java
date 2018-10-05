package com.interlib.sso.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.interlib.sso.common.Constants;
import com.interlib.sso.common.EncryptDecryptData;
import com.interlib.sso.common.MD5Util;
import com.interlib.sso.dao.NetReaderDao;
import com.interlib.sso.domain.LibCode;
import com.interlib.sso.domain.NetReader;
import com.interlib.sso.domain.Reader;
import com.interlib.sso.domain.ReaderType;

@Repository
public class NetReaderDaoImpl 
	extends AbstractMybatisBaseDAO<LibCode, String> implements NetReaderDao {
	
	@Autowired
	public SqlSession sqlSession;
	
	/**
	 * 获取sqlSession
	 */
	@Override
	public SqlSession getSqlSession() {
		return sqlSession;
	}

	@Override
	public String getMybatisMapperNamespace() {
		return "NetReaderMapper";
	}
	
	public int getReaderNum(String rdId) {
		return Integer.valueOf(sqlSession.selectOne(getMybatisMapperNamespace()+".getReaderNum", rdId).toString());
	}
	
	public int getNetReaderNum(String readerId) {
		return Integer.valueOf(sqlSession.selectOne(getMybatisMapperNamespace()+".getNetReaderNum", readerId).toString());
	}
	
	public int insertNetReader(NetReader netreader) {
		return sqlSession.insert(getMybatisMapperNamespace()+".insertNetReader", netreader);
	}
	
	@SuppressWarnings("unchecked")
	public List<NetReader> queryNetReaderList(NetReader netreader) {
		return sqlSession.selectList(getMybatisMapperNamespace()+".queryNetReaderList", netreader);
	}
	@SuppressWarnings("unchecked")
	public List<NetReader> queryBatchNetReaderList(NetReader netreader) {
		
		if(netreader.getDateFormat().equals("YEAR")) {
			return sqlSession.selectList(getMybatisMapperNamespace() + ".queryBatchNetReaderByYearList", netreader);
		} else if(netreader.getDateFormat().equals("MONTH")) {
			return sqlSession.selectList(getMybatisMapperNamespace() + ".queryBatchNetReaderByMonthList", netreader);
		} else if(netreader.getDateFormat().equals("DAY")) {
			return sqlSession.selectList(getMybatisMapperNamespace() + ".queryBatchNetReaderByDayList", netreader);
		} else {
			return sqlSession.selectList(getMybatisMapperNamespace() + ".queryBatchNetReaderByDayList", netreader);
		}
	}
	
	public List<NetReader> queryBatchNetReaderListByReaderType(String rdtype,
			String readerHandleStartDate,String readerHandleEndDate) {
		
		Map<String, String> map = new HashMap<String, String>();
		
		map.put("rdtype", rdtype);
		map.put("start", readerHandleStartDate);
        map.put("end", readerHandleEndDate);
        System.out.println("map----"+map);
		return sqlSession.selectList(getMybatisMapperNamespace() + ".queryBatchNetReaderListByReaderType",map);
	}
	
	public int approveReject(List<String> idList) {
		return sqlSession.update(getMybatisMapperNamespace()+".approveReject", idList);
	}
	
	@SuppressWarnings("unchecked")
	public List<NetReader> getNetReaders(List<String> idList) {
		return sqlSession.selectList(getMybatisMapperNamespace()+".getNetReaders", idList);
	}
	
	public int approvePass(NetReader netreader) {
		return sqlSession.insert(getMybatisMapperNamespace()+".approvePass", netreader);
	}
	
	public int updateCheckPassed(List<String> netreaderIds) {
		return sqlSession.update(getMybatisMapperNamespace()+".updateCheckPassed", netreaderIds);
	}
	
	public NetReader getNetReader(String netreaderId) {
		return (NetReader) sqlSession.selectOne(getMybatisMapperNamespace()+".getNetReader", netreaderId);
	}
	
	public NetReader getNetReaderByRdId(String netreaderId) {
		return (NetReader) sqlSession.selectOne(getMybatisMapperNamespace()+".getNetReaderByRdId", netreaderId);
	}
	public int getBatchNetReader(){
		return  (Integer) sqlSession.selectOne(getMybatisMapperNamespace()+".getBatchNetReader");
	}
	@Override
	public int createAccount(NetReader netreader) {
		return sqlSession.insert(getMybatisMapperNamespace()+".createAccount", netreader);
	}

	@Override
	public int deleteNetReader(List<String> netreaderIds) {
		return sqlSession.delete(getMybatisMapperNamespace() + ".deleteNetReader", netreaderIds) ;
	}
	
}
