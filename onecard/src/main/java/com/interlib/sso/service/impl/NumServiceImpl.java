package com.interlib.sso.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.interlib.sso.dao.NumDAO;
import com.interlib.sso.domain.Num;
import com.interlib.sso.service.NumService;


@Service
public class NumServiceImpl extends AbstractBaseServiceImpl<Num, String> 
		implements NumService {
	@Autowired
	public NumDAO numDao;
	

	public void setNumDao(NumDAO numDao) {
		this.numDao = numDao;
	}


	@Override
	public int addNum(Num num) {
		// TODO Auto-generated method stub
		return numDao.addNum(num);
	}


	@Override
	public int updateNum(String id) {
		// TODO Auto-generated method stub
		return numDao.updateNum(id);
	}


	@Override
	public Num getNum(String id) {
		// TODO Auto-generated method stub
		return numDao.getNum(id);
	}

}
