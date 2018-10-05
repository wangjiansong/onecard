package com.interlib.sso.service;

import java.util.List;

import com.interlib.sso.domain.Guide;

public interface GuideService extends BaseService<Guide, Long> {

	public List<Guide> getGuidesByLastSomeone();

}
