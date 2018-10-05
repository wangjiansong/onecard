package com.interlib.sso.dao;

import java.util.List;

import com.interlib.sso.domain.RestaurantWorkConfig;

public interface RestaurantWorkConfigDAO extends BaseDAO<RestaurantWorkConfig, Integer> {

	public List<RestaurantWorkConfig> queryRestaurantWorkConfigList(RestaurantWorkConfig config);
}
