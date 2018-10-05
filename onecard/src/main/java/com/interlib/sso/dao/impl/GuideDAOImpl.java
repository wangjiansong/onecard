package com.interlib.sso.dao.impl;


import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.interlib.sso.dao.GuideDAO;
import com.interlib.sso.domain.Guide;
@Repository
public class GuideDAOImpl 
			extends AbstractMybatisBaseDAO<Guide, Long> implements GuideDAO {

	@Autowired
	public SqlSession sqlSession;

	@Override
	public SqlSession getSqlSession() {
		return sqlSession;
	}

	@Override
	public String getMybatisMapperNamespace() {
		return "Guide";
	}

	@Override
	public List<Guide> getGuidesByLastSomeone() {
		
		return sqlSession.selectList(getMybatisMapperNamespace() + ".getGuidesByLastSomeone");
	}

}
