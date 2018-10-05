package com.interlib.sso.service;

import java.util.List;

import com.interlib.sso.domain.RestaurantWorkConfig;

public interface RestaurantWorkConfigService extends BaseService<RestaurantWorkConfig, Integer> {
	
	public List<RestaurantWorkConfig> queryRestaurantWorkConfigList(RestaurantWorkConfig config);
}
