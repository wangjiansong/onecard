package com.interlib.sso.dao.impl;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.interlib.sso.dao.impl.AbstractMybatisBaseDAO;
import com.interlib.sso.dao.ReaderTypeDao;
import com.interlib.sso.domain.ReaderType;

@Repository
public class ReaderTypeDaoImpl extends AbstractMybatisBaseDAO<ReaderType, String> implements ReaderTypeDao {

	@Autowired
	public SqlSession sqlSession;
	
	public void setSqlSession(SqlSession sqlSession) {
		this.sqlSession = sqlSession;
	}
	
	/**
	 * 获取sqlSession
	 */
	@Override
	public SqlSession getSqlSession() {
		return sqlSession;
	}

	/**
	 * 配置命名空间
	 */
	@Override
	public String getMybatisMapperNamespace() {
		return "ReaderTypeMapper";
	}
	
	/**
	 * 读者类型列表
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String,String>> queryReaderTypeList(ReaderType readerType) {
		return sqlSession.selectList(getMybatisMapperNamespace()+".queryReaderTypeList", readerType);
	}
	
	/**
	 * 检查读者类型是否存在(新增操作)
	 */
	public String checkReaderType(String readerType) {
		return sqlSession.selectOne(getMybatisMapperNamespace()+".checkReaderType", readerType).toString();
	}
	
	/**
	 * 检查读者类型是否存在(修改操作)
	 */
	public String checkReaderTypeEdit(Map<String,String> params) {
		return sqlSession.selectOne(getMybatisMapperNamespace()+".checkReaderTypeEdit", params).toString();
	}
	
	/**
	 * 新增读者类型
	 */
	public int addReaderType(ReaderType readerType) {
		return sqlSession.insert(getMybatisMapperNamespace()+".addReaderType", readerType);
	}
	
	/**
	 * 获取分馆代码
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String,String>> getLibCode() {
		return sqlSession.selectList(getMybatisMapperNamespace()+".getLibCode");
	}
	
	/**
	 * 获取读者类型信息
	 */
	public ReaderType getReaderType(String readerType) {
		return (ReaderType) sqlSession.selectOne(getMybatisMapperNamespace()+".getReaderType", readerType);
	}
	
	/**
	 * 修改读者类型信息
	 */
	public int editReaderType(Map<String,String> params) {
		return sqlSession.update(getMybatisMapperNamespace()+".editReaderType", params);
	}
	
	/**
	 * 删除读者类型
	 */
	public int deleteReaderType(String readerType) {
		return sqlSession.delete(getMybatisMapperNamespace()+".deleteReaderType", readerType);
	}

}
