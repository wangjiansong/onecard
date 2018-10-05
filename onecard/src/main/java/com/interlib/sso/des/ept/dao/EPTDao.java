package com.interlib.sso.des.ept.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.interlib.sso.des.ept.util.DBUtil;

public class EPTDao {
	
	public List<Map<String,String>> getDataForEncrypt() {
		StringBuffer sql = new StringBuffer();
		sql.
		append("SELECT RDID,RDPASSWD FROM READER ").
		append("WHERE RDID NOT IN(").
		append("SELECT RDID FROM READER ").
		append("WHERE RDPASSWD IS NOT NULL AND RDPASSWD LIKE '^%' AND RDPASSWD LIKE '%^') ").
		append("AND RDPASSWD IS NOT NULL");
		return DBUtil.getBaseDataList(sql.toString());
	}
	
	public boolean encryptDESPassword(String rdId, String encryptPassword) {
		List<Object> params = new ArrayList<Object>();
		params.add(encryptPassword);
		params.add(rdId);
		String sql = "UPDATE READER SET RDPASSWD=? WHERE RDID=?";
		return DBUtil.execUpdate(sql, params);
	}

}
