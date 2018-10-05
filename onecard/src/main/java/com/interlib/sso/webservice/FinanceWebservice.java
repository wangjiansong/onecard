package com.interlib.sso.webservice;

import javax.jws.WebParam;
import javax.jws.WebService;

import com.interlib.sso.domain.RdAccount;

/**
 * 标准webservice接口
 * @author Home
 *
 */
@WebService
public interface FinanceWebservice {

	/**
	 * 查询读者账户余额
	 * @param rdid
	 * @param password
	 * @return
	 */
	public RdAccount queryReaderAccount(@WebParam(name="rdid") String rdid);
	
	/**
	 * 扣费
	 * @param rdid
	 * @param password
	 * @return
	 */
	public String deduction(@WebParam(name="rdid") String rdid) ;
	
	
	/**
	 * 充值接口
	 * @param rdid
	 * @param password
	 * @return
	 */
	public String chargeReaderAccount(@WebParam(name="rdid") String rdid) ;
	
}
