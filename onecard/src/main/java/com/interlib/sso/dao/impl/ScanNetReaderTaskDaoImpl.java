package com.interlib.sso.dao.impl;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.interlib.sso.dao.ScanNetReaderTaskDao;

@SuppressWarnings("unchecked")
@Repository
public class ScanNetReaderTaskDaoImpl implements ScanNetReaderTaskDao {
	@Autowired
	private SqlSession sqlSession;
	/**
	 * 获取sqlsession
	 * @param sqlSession
	 */
	public void setSqlSession(SqlSession sqlSession) {
		this.sqlSession = sqlSession;
	}
	
	/**
	 * 指定配置文件命名空间
	 */
	private String getMybatisMapperNamespace() {
		return "ScanNetReaderTaskMapper";
	}
	
	public List<Map<String, Object>> queryCurrentScans() {
		return sqlSession.selectList(getMybatisMapperNamespace() + ".queryCurrentScans");
	}
	
	public int doScan() {
		return sqlSession.update(getMybatisMapperNamespace() + ".doScan");
	}
	public int doScanForNetReader(){
		return sqlSession.update(getMybatisMapperNamespace() + ".doScanForNetReader");
	}

	public int batchDeleteNetReaders(){
		return sqlSession.update(getMybatisMapperNamespace() + ".batchDeleteNetReaders");
	}
}
