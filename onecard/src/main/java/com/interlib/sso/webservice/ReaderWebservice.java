package com.interlib.sso.webservice;

import java.util.List;

import javax.jws.WebParam;
import javax.jws.WebService;

import com.interlib.sso.domain.NetReader;
import com.interlib.sso.domain.Reader;

/**
 * 读者webservice
 * 
 * @author Lullaby
 * 
 */
@WebService
public interface ReaderWebservice {

	/**
	 * 新增读者
	 * @param reader 读者对象
	 * @param staticKey 静态秘钥
	 * @return
	 */
	public List<String> addReader(@WebParam(name = "reader") Reader reader,
			@WebParam(name = "staticKey") String staticKey);
	
	/**
	 * 新增注册读者
	 * @param netReader
	 * @param staticKey
	 * @return
	 */
	public List<String> addNetReader(@WebParam(name = "netReader") NetReader netReader,
			@WebParam(name = "staticKey") String staticKey);

	/**
	 * 修改读者
	 * @param reader 读者对象
	 * @param password 读者证密码
	 * @return
	 */
	public List<String> updateReader(@WebParam(name = "reader") Reader reader);

	/**
	 * 获取读者
	 * @param rdId 读者证号
	 * @param key 握手密码
	 * @return
	 */
	public Reader getReader(@WebParam(name = "rdId") String rdId,
			@WebParam(name = "key") String key);

	/**
	 * 新增读者并同步到interlib
	 * @param reader 读者对象
	 * @param staticKey 静态秘钥
	 * @return
	 */
	public List<String> addReaderSync(@WebParam(name = "reader") Reader reader, 
			@WebParam(name = "staticKey") String staticKey);
	
}
