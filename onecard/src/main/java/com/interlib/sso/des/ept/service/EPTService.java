package com.interlib.sso.des.ept.service;

import java.sql.Timestamp;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.interlib.sso.des.ept.dao.EPTDao;
import com.interlib.sso.des.ept.util.DBUtil;
import com.interlib.sso.des.ept.util.DESEncryptUtil;
import com.interlib.sso.des.ept.util.LogUtil;

public class EPTService {
	
	private EPTDao dao = new EPTDao();
	
	private static final String DES_STATIC_KEY = "64074f968502295ca41b7db452c7c639";
	private int successCount = 0;
	private int failureCount = 0;
	
	public void doEncrypt() {
		System.out.println("#################################################");
		LogUtil.printlnContent("#################################################");
		Timestamp start = new Timestamp(System.currentTimeMillis());
		try {
			List<Map<String,String>> list = dao.getDataForEncrypt();
			for (Iterator<Map<String, String>> ite = list.iterator(); ite.hasNext();) {
				Map<String,String> map = ite.next();
				try {
					String encryptPassword = DESEncryptUtil.encrypt(DES_STATIC_KEY, map.get("RDPASSWD"));
					boolean flag = dao.encryptDESPassword(map.get("RDID"), "^"+encryptPassword+"^");
					if (flag) {
						successCount++;
					} else {
						failureCount++;
						System.out.println("证号：["+map.get("RDID")+"]加密失败！");
						LogUtil.printlnContent("证号：["+map.get("RDID")+"]加密失败！");
					}
				} catch (Exception e) {
					failureCount++;
					System.out.println("证号：["+map.get("RDID")+"]加密异常！");
					e.printStackTrace();
					LogUtil.printlnContent("证号：["+map.get("RDID")+"]加密异常！");
					LogUtil.printlnException(e);
				}
			}
		} catch (Exception e) {
			System.out.println("加密过程异常！");
			LogUtil.printlnContent("加密过程异常！");
			e.printStackTrace();
			LogUtil.printlnException(e);
		} finally {
			System.out.println("程序开始执行时间："+start);
			System.out.println("加密成功："+successCount+"条。");
			System.out.println("加密失败："+failureCount+"条。");
			System.out.println("程序结束执行时间："+new Timestamp(System.currentTimeMillis()));
			System.out.println("#################################################");
			
			LogUtil.printlnContent("程序开始执行时间："+start);
			LogUtil.printlnContent("加密成功："+successCount+"条。");
			LogUtil.printlnContent("加密失败："+failureCount+"条。");
			LogUtil.printlnContent("程序结束执行时间："+new Timestamp(System.currentTimeMillis()));
			LogUtil.printlnContent("#################################################");
			DBUtil.closeDB();
			LogUtil.closeWriter();
		}
	}
	
}
