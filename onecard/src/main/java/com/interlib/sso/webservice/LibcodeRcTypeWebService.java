package com.interlib.sso.webservice;

import java.util.List;

import javax.jws.WebParam;
import javax.jws.WebService;

import com.interlib.sso.domain.LibCode;
import com.interlib.sso.domain.ReaderType;

/**
 * 馆代码和读者类型webservice
 * 
 * @author pyx
 * 
 */
@WebService
public interface LibcodeRcTypeWebService {
	/**
	 * 新增馆代码记录
	 * @param  libcode 馆代码信息对象
	 * @return
	 */
	public List<String> addLibcode(@WebParam(name = "libcode") LibCode libcode);

	/**
	 * 修改馆代码记录
	 * @param libcode 馆代码信息对象
	 * @return
	 */
	public List<String> updateLibcode(@WebParam(name = "libcode") LibCode libcode);

	/**
	 * 获取馆代码记录
	 * @param libCode 馆代码
	 * @return
	 */
	public LibCode getLibcode(@WebParam(name = "libCode") String libCode);
	
	/**
	 * 新增读者类型记录
	 * @param  rcType 读者类型对象
	 * @return
	 */
//	public List<String> addPRcType(@WebParam(name = "rcType") PRcType rcType,
//			@WebParam(name = "staticKey") String staticKey);
	
	public List<String> addPRcType(@WebParam(name = "rcType") ReaderType rcType);
	

	/**
	 * 修改读者类型记录
	 * @param rcType读者类型对象
	 * @return
	 */
//	public List<String> updateLibcode(@WebParam(name = "rcType") PRcType rcType);
	public List<String> updatePRcType(@WebParam(name = "rcType") ReaderType rcType);

	/**
	 * 获取读者类型记录
	 * @param readerType 读者类型字段
	 * @return
	 */
	public ReaderType getPRcType(@WebParam(name = "readerType") String readerType);
}
