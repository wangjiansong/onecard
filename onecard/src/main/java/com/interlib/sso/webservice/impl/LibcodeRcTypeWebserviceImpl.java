package com.interlib.sso.webservice.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


import java.util.Map;

import javax.jws.WebService;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.interlib.sso.common.Constants;
import com.interlib.sso.common.MD5Util;
import com.interlib.sso.domain.LibCode;
import com.interlib.sso.domain.ReaderType;
import com.interlib.sso.service.LibCodeService;
import com.interlib.sso.service.ReaderTypeService;
import com.interlib.sso.webservice.LibcodeRcTypeWebService;
@WebService(endpointInterface = "com.interlib.sso.webservice.LibcodeRcTypeWebService")
public class LibcodeRcTypeWebserviceImpl implements LibcodeRcTypeWebService {


	private static Logger logger = Logger.getLogger(LibcodeRcTypeWebserviceImpl.class);

	
	@Autowired
	private LibCodeService libcodeService;
	
	@Autowired
	private ReaderTypeService readerTypeService;
	
	@Override
	public List<String> addLibcode(LibCode libcode) {
		List<String> result = new ArrayList<String>();
		if (libcode != null) {
			String libcodeID = libcode.getLibCode();//获取对象的馆代码
			if (libcodeID != null && !"".equals(libcodeID)) {
				try {
					//先到数据库查有没有改编码的记录，有则修改，无则新增
					LibCode findLibcode = libcodeService.getLibByCode(libcodeID);
					if(findLibcode!=null){
						libcodeService.editLibCode(libcode);//修改
					}else{
						libcodeService.insertLibCode(libcode);//新增
					}
				} catch (Exception e) {
					result.add("执行数据库失败！");
					logger.error("执行数据库异常！", e);
				}
			} else {
				result.add("馆代码为空！");
			}
		} else {
			result.add("读者对象为空！");
		}
		return result;
	}

	@Override
	public List<String> updateLibcode(LibCode libcode) {
		List<String> result = new ArrayList<String>();
		if (libcode != null) {
			String libcodeID = libcode.getLibCode();//获取对象的馆代码
				if (libcodeID != null || !"".equals(libcodeID)) {
							try {
								//先到数据库查有没有改编码的记录，有则修改，无则新增
								LibCode findLibcode = libcodeService.getLibByCode(libcodeID);
								if(findLibcode!=null){
									libcodeService.editLibCode(libcode);//更新
								}else{
									libcodeService.insertLibCode(libcode);//新增
								}
							} catch (Exception e) {
								result.add("执行数据库失败！");
								logger.error("执行数据库异常！", e);
							}
				} else {
					result.add("馆代码为空！");
				}
		} else {
			result.add("馆代码对象为空！");
		}
		return result;
	}

	@Override
	public LibCode getLibcode(String libCode) {
		LibCode libcodeBean = null;
		if (libCode != null && !"".equals(libCode)) {
			libcodeBean = libcodeService.getLibByCode(libCode);
		}
		return libcodeBean;
	}

	@Override
	public List<String> addPRcType(ReaderType rcType) {
		List<String> result = new ArrayList<String>();
		if (rcType != null) {
			String readerType = rcType.getReaderType();//获取对象的馆代码
			if (readerType != null && !"".equals(readerType)) {
				try {
					//先到数据库查有没有改编码的记录，有则修改，无则新增
					ReaderType findType = readerTypeService.getReaderType(readerType);
					if(findType!=null){
						Map<String,String> params = new HashMap<String,String>();
						
						params.put("readerType_new", rcType.getReaderType());
						params.put("readerType_old",findType.getReaderType());
						params.put("libCode", rcType.getLibCode());
						params.put("sign", rcType.getSign()+"");
						params.put("libSign", rcType.getLibSign()+"");
						params.put("descripe", rcType.getDescripe()+"");
						params.put("deposity", rcType.getDeposity()+"");
						params.put("servicefee", rcType.getServicefee()+"");
						params.put("checkfee", rcType.getCheckfee()+"");
						params.put("idfee", rcType.getIdfee()+"");
						params.put("valdate", rcType.getValdate()+"");
						readerTypeService.editReaderType(params);//修改
					}else{
						readerTypeService.addReaderType(rcType);//新增
					}
				} catch (Exception e) {
					result.add("执行数据库失败！");
					logger.error("执行数据库异常！", e);
				}
					
			} else {
				result.add("读者类型为空！");
			}
		} else {
			result.add("读者类型对象为空！");
		}
		return result;
	}

	@Override
	public List<String> updatePRcType(ReaderType rcType) {
		List<String> result = new ArrayList<String>();
		if (rcType != null) {
			String readerType = rcType.getReaderType();//获取读者类型
				if (readerType != null || !"".equals(readerType)) {
							try {
								//先到数据库查有没有改编码的记录，有则修改，无则新增
								ReaderType findType = readerTypeService.getReaderType(readerType);
								if(findType!=null){
									Map<String,String> params = new HashMap<String,String>();
									params.put("readerType", rcType.getReaderType());
									params.put("libCode", rcType.getLibCode());
									params.put("sign", rcType.getSign()+"");
									params.put("libSign", rcType.getLibSign()+"");
									params.put("descripe", rcType.getDescripe()+"");
									params.put("deposity", rcType.getDeposity()+"");
									params.put("servicefee", rcType.getServicefee()+"");
									params.put("checkfee", rcType.getCheckfee()+"");
									params.put("idfee", rcType.getIdfee()+"");
									params.put("valdate", rcType.getValdate()+"");
									readerTypeService.editReaderType(params);//修改
								}else{
									readerTypeService.addReaderType(rcType);//新增
								}
							} catch (Exception e) {
								result.add("执行数据库失败！");
								logger.error("执行数据库异常！", e);
							}
				} else {
					result.add("读者类型为空！");
				}
		} else {
			result.add("读者类型对象为空！");
		}
		return result;
	}

	@Override
	public ReaderType getPRcType(String readerType) {
		ReaderType typeBean = null;
		if (readerType != null && !"".equals(readerType)) {
			typeBean = readerTypeService.getReaderType(readerType);
		}
		return typeBean;
	}

}
