package com.interlib.sso.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.interlib.sso.dao.impl.AbstractMybatisBaseDAO;
import com.interlib.sso.dao.LibCodeDao;
import com.interlib.sso.domain.LibCode;

@Repository
public class LibCodeDaoImpl extends AbstractMybatisBaseDAO<LibCode, String> implements LibCodeDao {

	@Autowired
	public SqlSession sqlSession;
	
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
		return "LibCodeMapper";
	}
	
	/**
	 * 检查分馆代码
	 */
	public String checkLibCode(String libCode) {
		return sqlSession.selectOne(getMybatisMapperNamespace()+".checkLibCode", libCode).toString();
	}
	
	/**
	 * 新增分馆
	 */
	public int insertLibCode(LibCode libCode) {
		return sqlSession.insert(getMybatisMapperNamespace()+".insertLibCode", libCode);
	}
	
	/**
	 * 分馆列表
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String,String>> queryLibCodeList(LibCode libCode) {
		return sqlSession.selectList(getMybatisMapperNamespace()+".queryLibCodeList", libCode);
	}
	
	/**
	 * 获取分馆信息
	 */
	public LibCode getLibByCode(String libCode) {
		return (LibCode) sqlSession.selectOne(getMybatisMapperNamespace()+".getLibByCode", libCode);
	}
	
	/**
	 * 修改时间检查分馆代码是否存在
	 */
	public String checkLibCodeEdit(String libCode) {
		return sqlSession.selectOne(getMybatisMapperNamespace()+".checkLibCodeEdit", libCode).toString();
	}
	
	/**
	 * 修改分馆信息
	 */
	public int editLibCode(LibCode libCode) {
		return sqlSession.update(getMybatisMapperNamespace()+".editLibCode", libCode);
	}
	
	/**
	 * 删除分馆记录
	 */
	public int deleteLibCode(String libCode) {
		return sqlSession.delete(getMybatisMapperNamespace()+".deleteLibCode", libCode);
	}

	@Override
	public List<LibCode> getSimpleInfo() {
		return sqlSession.selectList(getMybatisMapperNamespace() + ".getSimpleInfo");
	}

	@Override
	public List<LibCode> getStaticsLibCodeSet() {
		
		return sqlSession.selectList(getMybatisMapperNamespace() + ".getStaticsLibCodeSet");
	}

	
	@Override
	public List<LibCode> getSelectLibs(Map<String, String> map) {
		
		return sqlSession.selectList(getMybatisMapperNamespace() + ".getSelectLibs",map);

	}

	@Override
	public List<LibCode> getLibCodeSet() {
		
		return sqlSession.selectList(getMybatisMapperNamespace() + ".getLibCodeSet");
	}

	
}
