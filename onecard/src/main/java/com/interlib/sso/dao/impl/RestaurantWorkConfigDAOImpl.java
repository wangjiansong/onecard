package com.interlib.sso.dao.impl;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.interlib.sso.dao.RestaurantWorkConfigDAO;
import com.interlib.sso.domain.RestaurantWorkConfig;

@Repository
public class RestaurantWorkConfigDAOImpl 
		extends AbstractMybatisBaseDAO<RestaurantWorkConfig, Integer>implements RestaurantWorkConfigDAO {

	@Autowired
	public SqlSession sqlSession;

	@Override
	public SqlSession getSqlSession() {
		return sqlSession;
	}

	@Override
	public String getMybatisMapperNamespace() {
		return "RestaurantWorkConfig";
	}

	@Override
	public List<RestaurantWorkConfig> queryRestaurantWorkConfigList(RestaurantWorkConfig config) {
		return sqlSession.selectList(getMybatisMapperNamespace() + ".queryPageList", config);
	}


}
