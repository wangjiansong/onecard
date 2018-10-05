package com.interlib.sso.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.interlib.sso.dao.RestaurantWorkConfigDAO;
import com.interlib.sso.domain.RestaurantWorkConfig;
import com.interlib.sso.service.RestaurantWorkConfigService;

@Service
public class RestaurantWorkConfigServiceImpl 
		extends AbstractBaseServiceImpl<RestaurantWorkConfig, Integer> implements RestaurantWorkConfigService {

	@Autowired
	public RestaurantWorkConfigDAO restaurantWorkConfigDAO;
	
	@Autowired
	public void setBaseDAO(RestaurantWorkConfigDAO restaurantWorkConfigDAO) {
		super.setBaseDAO(restaurantWorkConfigDAO);
	}

	@Override
	public List<RestaurantWorkConfig> queryRestaurantWorkConfigList(RestaurantWorkConfig config) {
		// TODO Auto-generated method stub
		return restaurantWorkConfigDAO.queryRestaurantWorkConfigList(config);
	}
	
	

}
