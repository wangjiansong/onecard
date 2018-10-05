package com.interlib.sso.dao;

import java.util.List;

import com.interlib.sso.domain.Guide;

public interface GuideDAO extends BaseDAO<Guide, Long>{

	public List<Guide> getGuidesByLastSomeone();

}
