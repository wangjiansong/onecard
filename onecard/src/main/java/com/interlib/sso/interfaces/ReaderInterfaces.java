package com.interlib.sso.interfaces;

import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.annotation.XmlRootElement;

import net.sf.json.JSONObject;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.interlib.opac.webservice.ReaderWebservice;
import com.interlib.sso.acs.serivce.AcsService;
import com.interlib.sso.common.Cautions;
import com.interlib.sso.common.Common;
import com.interlib.sso.common.Constants;
import com.interlib.sso.common.EncryptDecryptData;
import com.interlib.sso.common.IdCard;
import com.interlib.sso.common.MD5Util;
import com.interlib.sso.common.TimeUtils;
import com.interlib.sso.domain.LibCode;
import com.interlib.sso.domain.LogCir;
import com.interlib.sso.domain.NetReader;
import com.interlib.sso.domain.RdAccount;
import com.interlib.sso.domain.Reader;
import com.interlib.sso.domain.ReaderCardInfo;
import com.interlib.sso.domain.ReaderType;
import com.interlib.sso.service.LibCodeService;
import com.interlib.sso.service.LogCirService;
import com.interlib.sso.service.NetReaderService;
import com.interlib.sso.service.RdAccountService;
import com.interlib.sso.service.ReaderCardInfoService;
import com.interlib.sso.service.ReaderService;
import com.interlib.sso.service.ReaderTypeService;
import com.interlib.sso.webservice.DomainTransform;


@Controller
@RequestMapping("api/reader")
public class ReaderInterfaces {
	@Autowired
	public ReaderService readerService;
	
	@Autowired
	public ReaderTypeService readerTypeService;
	
	//ACS for onecard 使用
	@Autowired
	public AcsService acsService;

	@Autowired
	public ExecutorService cacheThreadPool;
	//结束
	
	@Autowired
	public NetReaderService netReaderService;
	
	@Autowired
	public RdAccountService rdAccountService;
	
	@Autowired
	public LogCirService logCirService;
	
	@Autowired
	public LibCodeService libCodeService;
	
	@Autowired
	public ReaderCardInfoService readerCardInfoService;
	
	private static String LOGINCODE="netjsonpass123";

	
	@XmlRootElement(name="Result")
	static class R {
		public String message;
		public String success;
		public String rdId;
		public String rdPhone;
		public String rdName;
		public String rdPassword;
		public String rdUnit;
		public String rdType;
		public String rdLibType;
		public String rdSex;
		public String rdState;
		public String rdStateStr;
		public String rdGlobal;
		public String rdLib;
		public String rdLibCode;
		public String rdInTimeStr;
		public String rdStartDateStr;
		public String rdEndDateStr;
		public String rdAccount;
		public String rdAccountStatus;
		public String webserviceUrl;
		public String rdAge;
		public String rdCertify;//身份证号
		public String isAuthor;//上载国图是否标识符。
	}
	@XmlRootElement(name="Results")
	static class R2 {
		public String totalcount;
		public List<R> Result;
		public String message;
		public String success;
	}

	
	//新增读者
	@RequestMapping(value="/addReader", produces="application/xml") 
	public @ResponseBody R addReader(@RequestParam(required=true, value="reader") Reader reader, 
			@RequestParam(required=true, value="userid") String userid, 
			@RequestParam(required=true, value="libcode") String libcode ) {
		R r = new R();
		
		if(reader == null){
			r.message = "读者信息参数不能为空";
			r.success = "0";
			return r;
		}
		if(userid == null){
			r.message = "操作员参数不能为空";
			r.success = "0";
			return r;
		}
		if(libcode == null) {
			r.message = "馆代码不能为空";
			r.success = "1";
			return r;
		}
		if(readerService.getReader(reader.getRdId(), (byte)1) != null){
			r.message = "读者证号已存在";
			r.success = "0";
			return r;
		}
		try {
			reader.setRdStartDate(new Date());//读者证启用日期，默认今天
			reader.setRdInTime(new Date()); //读者证办证日期，默认今天
			ReaderType readerType = readerTypeService.getReaderType(reader.getRdType());
			int valdate = readerType.getValdate();
			reader.setRdEndDate(Common.getDateAfterDaysOtherWay(new Date(), valdate));
			readerService.addReader(reader);
			//TODO 写日志
			r.message = "";
			r.success = "1";
			return r;
		} catch(Exception e) {
			r.message = e.toString();
			r.success = "0";
			return r;
		}
	}
	
	@RequestMapping(value="/updateReader", produces="application/xml")
	public @ResponseBody R updateReader(@RequestParam(required=true, value="reader") Reader reader, 
			@RequestParam(required=true, value="userid") String userid, 
			@RequestParam(required=true, value="libcode") String libcode ) {
		R r = new R();
		if(reader == null){
			r.message = "读者信息参数不能为空";
			r.success = "0";
			return r;
		}
		if(userid == null){
			r.message = "操作员参数不能为空";
			r.success = "0";
			return r;
		}
		if(libcode == null) {
			r.message = "馆代码不能为空";
			r.success = "1";
			return r;
		}
		if(readerService.getReader(reader.getRdId(), (byte)1) == null){
			r.message = "读者证号不存在";
			r.success = "0";
			return r;
		}
		try {
			readerService.updateReader(reader);
			//TODO 写日志
			
			r.message = "";
			r.success = "1";
			return r;
		} catch(Exception e) {
			r.message = e.toString();
			r.success = "0";
			return r;
		}
		
	}
	/**
	 * 读者重置密码 不需要旧密码
	 * example：http://localhost:8089/onecard/api/reader/resetReaderPasswd?rdId=111111
	 * &newpassword=123456&appcode=interlib&enc=a44dacec4b9dc5d90f3704f1e4940733&userid=admin&libcode=999	 
	 * @param rdid
	 * @param newPassword
	 * @return
	 */
	@RequestMapping(value="/resetReaderPasswd", produces="application/xml")
	public @ResponseBody R resetReaderPasswd(@RequestParam(required=true, value="rdid") String rdid,
			@RequestParam(required=true, value="newpassword") String newPassword) {
		R r = new R();
		if(rdid == null){
			r.message = "读者证号不能为空";
			r.success = "0";
			return r;
		}
		if(newPassword == null){
			r.message = "新密码不能为空";
			r.success = "0";
			return r;
		}
		Reader reader = readerService.getReader(rdid, (byte)2);
		if(reader == null){
			r.message = "读者不存在";
			r.success = "0";
			return r;
		}
		try {
			String realPassword = EncryptDecryptData.decryptWithCode(Constants.DES_STATIC_KEY, reader.getRdPasswd());

			
			String newPasswordDes = EncryptDecryptData.encryptWithCode(Constants.DES_STATIC_KEY, newPassword);
			readerService.updatePassword(rdid, newPasswordDes);
			Map<String, Object> synMap = updateOpacReaderPasswd(reader, realPassword, newPassword);//调用同步
			
			if (synMap != null) {
				int rsyn = (Integer)synMap.get("success");
				if (rsyn == 1) {//同步成功，修改本地标识
					readerService.updateOldPasswordAndSynStatus(rdid, newPasswordDes, 1);
				} 
			}
			r.message = "密码修改成功";
			r.success = "1";
			return r;
		} catch(Exception e) {
			r.message = e.toString();
			r.success = "0";
			return r;
		}
		
	}
	
	@RequestMapping(value="/updateReaderPasswd", produces="application/xml")
	public @ResponseBody R updateReaderPasswd(@RequestParam(required=true, value="rdid") String rdid,
			@RequestParam(required=true, value="oldpassword") String oldPassword,
			@RequestParam(required=true, value="newpassword") String newPassword) {
		R r = new R();
		if(rdid == null){
			r.message = "读者证号不能为空";
			r.success = "0";
			return r;
		}
		if(oldPassword == null){
			r.message = "旧密码不能为空";
			r.success = "0";
			return r;
		}
		if(newPassword == null){
			r.message = "新密码不能为空";
			r.success = "0";
			return r;
		}
		Reader reader = readerService.getReader(rdid, (byte)2);
		if(reader == null){
			r.message = "读者不存在";
			r.success = "0";
			return r;
		}
		try {
			String realPassword = EncryptDecryptData.decryptWithCode(Constants.DES_STATIC_KEY, reader.getRdPasswd());
			if(!realPassword.equals(oldPassword)) {
				r.message = "旧密码错误";
				r.success = "0";
				return r;
			}
		
			String newPasswordDes = EncryptDecryptData.encryptWithCode(Constants.DES_STATIC_KEY, newPassword);
			readerService.updatePassword(rdid, newPasswordDes);
			Map<String, Object> synMap = updateOpacReaderPasswd(reader, oldPassword, newPassword);//调用同步
				
			if (synMap != null) {
				int rsyn = (Integer)synMap.get("success");
				if (rsyn == 1) {//同步成功，修改本地标识
					readerService.updateOldPasswordAndSynStatus(rdid, newPasswordDes, 1);
				} 
			}
			
			r.message = "密码修改成功";
			r.success = "1";
			return r;
		} catch(Exception e) {
			r.message = e.toString();
			r.success = "0";
			return r;
		}
		
	}
	/**
	 * 根据读者证号，读者身份证重置密码
	 * %%%%%福建省图专用%%%%%
	 * example：http://localhost:8089/onecard/api/reader/updateReaderPasswdByRdCertify?rdid=111111
	 * &rdCertify=123456&appcode=interlib&enc=a44dacec4b9dc5d90f3704f1e4940733&userid=admin&libcode=999	 
	 * @param rdid
	 * @param rdCertify
	 * @return
	 */
	@RequestMapping(value="/updateReaderPasswdByRdCertify", produces="application/xml")
	public @ResponseBody R updateReaderPasswdByRdCertify(@RequestParam(required=true, value="rdid") String rdid,
			@RequestParam(required=true, value="rdCertify") String rdCertify,
			@RequestParam(required=false, value="libcode") String libcode) {
		R r = new R();
		if(rdid == null){
			r.message = "读者证号不能为空";
			r.success = "0";
			return r;
		}
		if(rdCertify == null){
			r.message = "身份证不能为空";
			r.success = "0";
			return r;
		}
//		Reader reader =readerService.getReader(rdid, (byte) 1);
		Reader reader = readerService.getReaderByRdIdAndRdCertify(rdid, rdCertify);
//		if(reader.getRdCertify() != rdCertify ||reader.getRdId() != rdid){
//			r.message = "读者证号或身份证输入有误";
//			r.success = "0";
//			return r;
//		}
		if(reader == null){
			r.message = "请检查读者证号或者身份证是否输入有误";
			r.success = "0";
			return r;
		}
		if(reader.getRdLibCode() ==null){
			reader.setRdLibCode(libcode);
		}
		try {
			String realPassword = EncryptDecryptData.decryptWithCode(Constants.DES_STATIC_KEY, reader.getRdPasswd());

			String newPassword = rdCertify.substring(8, 14);
			
			String newPasswordDes = EncryptDecryptData.encryptWithCode(Constants.DES_STATIC_KEY, newPassword);
			readerService.updatePassword(rdid, newPasswordDes);
			Map<String, Object> synMap = updateOpacReaderPasswd(reader, realPassword,newPassword);//调用同步
			
			if (synMap != null) {
				int rsyn = (Integer)synMap.get("success");
				if (rsyn == 1) {//同步成功，修改本地标识
					readerService.updateOldPasswordAndSynStatus(rdid, newPasswordDes, 1);
				} 
			}
			r.message = "密码修改成功";
			r.success = "1";
			return r;
		} catch(Exception e) {
			r.message = e.toString();
			r.success = "0";
			return r;
		}
		
	}
	
	/* DES加密默认静态秘钥 */
	private static final String DES_STATIC_KEY = "64074f968502295ca41b7db452c7c639";
	private int checkReaderInfo(String rdid,String rdpasswd){
		int result = 0;//验证信息返回的结果
		String rdpassword = readerService.getRealPassword(rdid);//返回的密码是加密的
		try {
			String despasswd = EncryptDecryptData.encrypt(DES_STATIC_KEY, rdpasswd);
			despasswd  = "^"+despasswd+"^";
			if(rdpassword.equals(despasswd)){//验证密码
				result = 1;
			}
		} catch (Exception e) {
			//统一归到一个异常处理，不细分了
			e.printStackTrace();
		} 
		
		return result;
	}
	
	/**
	 * 20140811
	 * 冻结账户
	 * @param req
	 * @param rdid
	 * @param rdpasswd
	 * @return
	 */
	@RequestMapping(value="/doLock",produces="application/xml")
	public @ResponseBody R doLock(HttpServletRequest request, 
			@RequestParam(required=false, value="rdid") String rdid, 
			@RequestParam(required=false, value="rdpasswd") String rdpasswd,
			@RequestParam(required=false, value="libcode") String libcode, 
			@RequestParam(required=false, value="userid") String userid){
		//首先先验证读者的身份证和密码
		R r = new R();
		if(rdid == null || rdpasswd == null) {
			r.message = "请传递证号和密码";
			r.success = "0";
			return r;
		}
		int result = checkReaderInfo(rdid,rdpasswd);
		if(result!=1){
			r.message = "账号或者密码不对，请检查！";
			r.success = "0";
			return r;
		}
		Reader adminReader = readerService.getReader(userid, (byte)2);
		String ip = Common.getUserIp(request);
		RdAccount rdAccount = rdAccountService.get(rdid);
		rdAccount.setStatus(2);
		rdAccountService.update(rdAccount);
		
		LogCir logCir = new LogCir();
		logCir.setLogType("30701");
		logCir.setLibcode(libcode);//(adminReader.getRdLibCode());
		logCir.setUserId(adminReader.getRdId());
		logCir.setIpAddr(ip);
		logCir.setData2(rdid);
		logCir.setData3("");
		logCir.setData4("");
		logCirService.save(logCir);
		r.message = "冻结成功！";
		r.success = "1";
		return r;
	}
	
	/**
	 * 20140811
	 * 恢复被冻结账户
	 * @param req
	 * @param rdid
	 * @param rdpasswd
	 * @return
	 */
	@RequestMapping(value="/doUnLock",produces="application/xml")
	public @ResponseBody R doUnLock(HttpServletRequest request, 
			@RequestParam(required=false, value="rdid") String rdid, 
			@RequestParam(required=false, value="rdpasswd") String rdpasswd,
			@RequestParam(required=false, value="libcode") String libcode, 
			@RequestParam(required=false, value="userid") String userid){
		//首先先验证读者的身份证和密码
		R r = new R();
		if(rdid == null || rdpasswd == null) {
			r.message = "请传递证号和密码";
			r.success = "0";
			return r;
		}
		int result = checkReaderInfo(rdid,rdpasswd);
		if(result!=1){
			r.message = "账号或者密码不对，请检查！";
			r.success = "0";
			return r;
		}
		Reader adminReader = readerService.getReader(userid, (byte)2);
		String ip = Common.getUserIp(request);
		RdAccount rdAccount = rdAccountService.get(rdid);
		rdAccount.setStatus(1);
		LogCir logCir = new LogCir();
		logCir.setLogType("30702");
		logCir.setLibcode(libcode);//(adminReader.getRdLibCode());
		logCir.setUserId(adminReader.getRdId());
		logCir.setIpAddr(ip);
		logCir.setData2(rdid);
		logCir.setData3("");
		logCir.setData4("");
		logCirService.save(logCir);
		rdAccountService.update(rdAccount);
		
		r.message = "解除冻结成功！";
		r.success = "1";
		return r;
	}
	
	/**
	 * 20140811
	 * 挂失读者账号：读者账号状态挂失，财经方面的结算，因为是手动挂失的使用退还财经方面的操作去掉，只是单纯挂失状态
	 * 以及用户的账户金额被冻结掉
	 * @param req
	 * @param rdid
	 * @param rdpasswd
	 * @return
	 */
	@RequestMapping(value="/loss",produces="application/xml")
	public @ResponseBody R loss(HttpServletRequest request,HttpServletResponse response,
			@RequestParam(required=false,value="rdid")String rdid,
			@RequestParam(required=false,value="rdpasswd")String rdpasswd,
			@RequestParam(required=false, value="libcode") String libcode, 
			@RequestParam(required=false,value="userid")String userid){
		
		R r = new R();
		if(rdid == null || rdpasswd == null) {
			r.message = "请传递证号和密码";
			r.success = "0";
			return r;
		}
		
		//操作员的信息到时候更新为一个信息
		Reader adminReader = readerService.getReader(userid, (byte)2);//操作员信息
//		String libcode = adminReader.getRdLibCode(); //操作员所在
		//首先先验证读者的身份证和密码
		int result = checkReaderInfo(rdid,rdpasswd);
		if(result!=1){
			r.message = "账号或者密码不对，请检查！";
			r.success = "0";
			return r;
		}
		Reader reader = readerService.getReader(rdid, (byte)2);
		LibCode lib = libCodeService.getLibByCode(reader.getRdLibCode());
		reader.setRdLib(reader.getRdLibCode());
		RdAccount rdAccount = rdAccountService.get(reader.getRdId());
		if(rdAccount == null) {//这个不是扣除财经
			//没有这个账户的信息，则重新创建一条信息
			rdAccount = new RdAccount();
			rdAccount.setRdid(reader.getRdId());
			rdAccount.setDeposit(0.0);
			rdAccount.setOnecard(0.0);
			rdAccount.setPrepay(0.0);
			rdAccount.setStatus(1);
			rdAccountService.save(rdAccount);
		}

		LogCir logCir = new LogCir();
		StringBuffer message = new StringBuffer();
		if(reader.getRdCFState() == 3) {//已经是挂失状态了，不用挂失啦
			r.message = "该账号已经挂失,不用再挂失！";
			r.success = "0";
			return r;
		}
		logCir.setLogType("30107");
		logCir.setData3("3");
		logCir.setData4("已挂失");
		rdAccount.setStatus(2); //账户冻结
		reader.setRdCFState((byte)3);//挂失状态
		
		rdAccountService.update(rdAccount);
		reader.setSynStatus(0);//添加同步状态 2014-05-15
		readerService.updateReader(reader);
		logCir.setLibcode(libcode);
		logCir.setData2(reader.getRdId());
		logCir.setUserId(userid);//操作员账号
		logCir.setIpAddr(Common.getUserIp(request));
		logCir.setData4("");
		logCirService.save(logCir);
		
		Map map = updateOpacReader(reader, lib, userid);
		int success = 1;
		String msg = "";
		String exception = "";
		if(map != null) {
			success = (Integer)map.get("success");
			
			msg = (String)map.get("message");
			
			exception = (String)map.get("exception");
		}
		if(success == -2) {
			// 中心保存成功但同步失败
			r.message = "挂失成功，但同步失败！";
			r.success = "1";
		} else if(success == 1) {
			//挂失成功
			r.message = "挂失成功！";
			r.success = "1";
		}else{
			//挂失失败
			r.message = "挂失失败，原因"+ Cautions.EMPTY_URL+"！";
			r.success = "0";
		}
		
		return r;
	}
	/**
	 * 得到读者信息通过RdId
	 * @param request
	 * @param rdid
	 * @param libcode
	 * @return
	 */
	@RequestMapping(value="/getReaderInfoByCardId",produces="application/xml")
	public @ResponseBody R getReaderInfoByRdId(HttpServletRequest request, 
			@RequestParam(required=false, value="cardId") String cardid,
			@RequestParam(required=false, value="libcode") String libcode){
		
		R r = new R();
		if(cardid == null) {
			r.message = "请传递卡号";
			r.success = "0";
			return r;
		}
//	    ReaderCardInfo cardInfo = readerCardInfoService.get(cardid);
//	    if (cardInfo == null)
//	    {
//	      r.message = "该卡号不存在有效记录";
//	      r.success = "0";
//	      return r;
//	    }
//	    if (cardInfo.getIsUsable() == 0)
//	    {
//	      r.message = "该卡暂不能使用";
//	      r.success = "0";
//	      return r;
//	    }
//	    String rdid = cardInfo.getRdId();
	    Reader reader = readerService.getReader(cardid, (byte)2);
	    if (reader == null)
	    {
	      r.message = "该卡号无对应读者信息";
	      r.success = "0";
	      return r;
	    }
		RdAccount rdAccount = rdAccountService.get(cardid);
		r.rdId = reader.getRdId();
		r.rdPhone = reader.getRdLoginId();
		r.rdName = reader.getRdName();
		String realPwd = "";
		try {
			realPwd = EncryptDecryptData.decryptWithCode(Constants.DES_STATIC_KEY, reader.getRdPasswd());
		} catch (Exception e) {
			e.printStackTrace();
		} 
		r.rdPassword = realPwd;
		r.rdUnit = reader.getRdUnit();
		r.rdSex = "" + reader.getRdSex();
		try {
			r.rdAge = getAge(TimeUtils.stringToDate(reader.getRdBornDateStr(), ""))+"";
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		r.rdState = reader.getRdCFState() + "";
		r.rdStateStr = reader.getRdStateStr();
		r.rdGlobal = reader.getRdGlobal() + "";
		r.rdType = reader.getRdType();
		r.rdLibType = reader.getRdLibType();
		r.rdLib = reader.getRdLib();
		r.rdLibCode = reader.getRdLibCode();
		r.rdInTimeStr = reader.getRdInTimeStr();
		r.rdStartDateStr = reader.getRdStartDateStr();
		r.rdEndDateStr = reader.getRdEndDateStr();
		r.rdAccount = rdAccount.getPrepay() + "";
		r.rdAccountStatus = rdAccount.getStatus() + "";
		r.success = "1";
		return r;
	}
	
	public static int getAge(Date birthDate) {
		  int age = 0;
		  Date now = new Date();
		  if (birthDate == null){
			  return 0;
		  }else{
		  SimpleDateFormat format_y = new SimpleDateFormat("yyyy");
		  SimpleDateFormat format_M = new SimpleDateFormat("MM");
		  
		  String birth_year =format_y.format(birthDate);
		  String this_year =format_y.format(now);
		  
		  String birth_month =format_M.format(birthDate);
		  String this_month =format_M.format(now);
		  
		  // 初步，估算
		  age =Integer.parseInt(this_year) - Integer.parseInt(birth_year);
		  
		  // 如果未到出生月份，则age - 1
		  if(this_month.compareTo(birth_month) < 0)
		   age -=1;
		  if (age <0)
		   age =0;
		  return age;
		  }
	}
	
	/**
	 * 得到读者信息通过rdid或者cardID,但必填
	 * @param request
	 * @param cardid
	 * @param rdid
	 * @param libcode
	 * @return
	 */
	@RequestMapping(value="/getReaderInfoByCardIdOrRdId",produces="application/xml")
	public @ResponseBody R getReaderInfoByCardIdOrRdId(HttpServletRequest request, 
			@RequestParam(required=false, value="rdId") String rdid,
			@RequestParam(required=false, value="cardid") String cardid,
			@RequestParam(required=false, value="libcode") String libcode){
		
		R r = new R();
		if(rdid == null || cardid == null) {
			r.message = "请传递卡号";
			r.success = "0";
			return r;
		}
//			Reader cardInfo = readerService.getByRdId(rdid);
//			System.out.println("------"+cardInfo+"------");
//			if(cardInfo == null) {
//				r.message = "该卡号不存在有效记录";
//				r.success = "0";
//				return r;
//			}
//			if(cardInfo.getIsUsable() ==0) {
//				r.message = "该卡暂不能使用";
//				r.success = "0";
//				return r;
//			}
//			String rdId = cardInfo.getRdId();
			Reader reader = readerService.getReader(rdid, (byte)2);
			if(reader == null) {
				r.message = "该卡号无对应读者信息";
				r.success = "0";
				return r;
			}
			RdAccount rdAccount = rdAccountService.get(rdid);
			r.rdId = reader.getRdId();
			r.rdPhone = reader.getRdLoginId();
			r.rdName = reader.getRdName();
			String realPwd = "";
			try {
				realPwd = EncryptDecryptData.decryptWithCode(Constants.DES_STATIC_KEY, reader.getRdPasswd());
			} catch (Exception e) {
				e.printStackTrace();
			} 
			r.rdPassword = realPwd;
			r.rdUnit = reader.getRdUnit();
			r.rdSex = "" + reader.getRdSex();
			r.rdState = reader.getRdCFState() + "";
			r.rdStateStr = reader.getRdStateStr();
			r.rdGlobal = reader.getRdGlobal() + "";
			r.rdType = reader.getRdType();
			r.rdLibType = reader.getRdLibType();
			r.rdLib = reader.getRdLib();
			r.rdLibCode = reader.getRdLibCode();
			r.rdInTimeStr = reader.getRdInTimeStr();
			r.rdStartDateStr = reader.getRdStartDateStr();
			r.rdEndDateStr = reader.getRdEndDateStr();
			r.rdAccount = rdAccount.getPrepay() + "";
			r.rdAccountStatus = rdAccount.getStatus() + "";
			r.success = "1";
			return r;
	}
	
	/**
	 * 得到读者信息通过身份证和姓名--只用于福建省图
	 * @param request
	 * @param cardid
	 * @param rdid
	 * @param libcode
	 * @return
	 */
	@RequestMapping(value="/getReaderInfoByRdCertify",produces="application/xml")
	public @ResponseBody R2 getReaderInfoByRdCertify(HttpServletRequest request, 
			@RequestParam(required=false, value="rdCertify") String rdCertify,
			@RequestParam(required=false, value="rdName") String rdName,
			@RequestParam(required=false, value="libcode") String libcode){
			R2 r2 = new R2();
			if(rdCertify == null) {
				r2.message = "请传递身份证";
				r2.success = "0";
				return r2;
			}
			if(rdName == null) {
				r2.message = "请传递读者姓名";
				r2.success = "0";
				return r2;
			}
			List<Reader> list = readerService.getReaderListByRdCertify(rdCertify,rdName);
			r2.Result = new ArrayList();
			for (int i = 0; i < list.size(); i++) {
			  System.out.println(list.get(i).getRdId());
			  
			  if(list.get(i) == null) {
					r2.message = "该身份证无对应读者信息";
					r2.success = "0";
	
					return r2;
				}
				if(list.get(i).getRdName().startsWith(rdName, 0)==false) {
					r2.message = "该姓名无对应读者信息";
					r2.success = "0";
	
					return r2;
				}
				
//				if(MD5Util.MD5Encode(list.get(i).getRdName(), "UTF-8")!=MD5Util.MD5Encode(rdName)) {
//				r2.message = "该姓名无对应读者信息";
//				r2.success = "0";
//
//				return r2;
//			}
				
			  
				R r = new R();
			  	r.rdId = list.get(i).getRdId();
				r.rdPhone = list.get(i).getRdLoginId();
				r.rdName = list.get(i).getRdName();
				r.rdCertify=list.get(i).getRdCertify();
				String realPwd = "";
				try {
					realPwd = EncryptDecryptData.decryptWithCode(Constants.DES_STATIC_KEY, list.get(i).getRdPasswd());
				} catch (Exception e) {
					e.printStackTrace();
				} 
				r.rdPassword = realPwd;
				r.rdUnit = list.get(i).getRdUnit();
				r.rdSex = "" + list.get(i).getRdSex();
				r.rdState = list.get(i).getRdCFState() + "";
				r.rdStateStr = list.get(i).getRdStateStr();
				r.rdGlobal = list.get(i).getRdGlobal() + "";
				r.rdType = list.get(i).getRdType();
				r.rdLibType = list.get(i).getRdLibType();
				r.rdLib = list.get(i).getRdLib();
				r.rdLibCode = list.get(i).getRdLibCode();
				r.rdInTimeStr = list.get(i).getRdInTimeStr();
				r.rdStartDateStr = list.get(i).getRdStartDateStr();
				r.rdEndDateStr = list.get(i).getRdEndDateStr();      
	
				r.success = "1";
			
			
				r2.Result.add(r);
			}
			return r2;
	}
	/**
	 * 得到读者信息通过身份证(存在多条)
	 * @param request
	 * @param cardid
	 * @param rdid
	 * @param libcode
	 * @return
	 */
//	@RequestMapping(value="/getReaderListByRdCertifyMore",produces="application/xml")
//	public @ResponseBody R2 getReaderListByRdCertifyMore(HttpServletRequest request, 
//			@RequestParam(required=false, value="rdCertify") String rdCertify,
//			@RequestParam(required=false, value="libcode") String libcode){
//		
//			R r = new R();
//			R2 r2 = new R2();
//			Reader reader = null;
//			if(rdCertify == null) {
//				r2.message = "请传递身份证";
//				r2.success = "0";
//				return r2;
//			}
//			List<Reader> list = readerService.getReaderListByRdCertifyMore(rdCertify);
//			r2.Result = new ArrayList();
//
//			 for(int i = 0;i < list.size(); i ++){
//				 reader= list.get(i);
//				
//				if(list.get(i) == null) {
//					r2.message = "该身份证无对应读者信息";
//					r2.success = "0";
//	
//					return r2;
//				}
//				r.rdId = reader.getRdId();
//				r.rdPhone = reader.getRdLoginId();
//				r.rdName = reader.getRdName();
//				r.rdCertify=reader.getRdCertify();
//				String realPwd = "";
//				try {
//					realPwd = EncryptDecryptData.decryptWithCode(Constants.DES_STATIC_KEY, reader.getRdPasswd());
//				} catch (Exception e) {
//					e.printStackTrace();
//				} 
//				r.rdPassword = realPwd;
//				r.rdUnit = reader.getRdUnit();
//				r.rdSex = "" + reader.getRdSex();
//				r.rdState = reader.getRdCFState() + "";
//				r.rdStateStr = reader.getRdStateStr();
//				r.rdGlobal = reader.getRdGlobal() + "";
//				r.rdType = reader.getRdType();
//				r.rdLibType = reader.getRdLibType();
//				r.rdLib = reader.getRdLib();
//				r.rdLibCode = reader.getRdLibCode();
//				r.rdInTimeStr = reader.getRdInTimeStr();
//				r.rdStartDateStr = reader.getRdStartDateStr();
//				r.rdEndDateStr = reader.getRdEndDateStr();      
//	
//				r.success = "1";
//			
//				
//				r2.Result.add(r);
//			}
//			return r2;
//	}
	
	/**
	 * 读者验证接口
	 * example：http://localhost:8089/onecard/api/reader/readerLogin?rdId=111111
	 * &rdPassword=e3ceb5881a0a1fdaad01296d7554868d
	 * &appcode=interlib&enc=a44dacec4b9dc5d90f3704f1e4940733&userid=admin&libcode=999
	 * @param request
	 * @param cardId
	 * @param rdId
	 * @param rdPassword
	 * @return
	 */
	
	@RequestMapping(value="/readerLogin",produces="application/xml")
	public @ResponseBody R readerLogin(HttpServletRequest request, 
			@RequestParam(required=false, value="cardId") String cardId,
			@RequestParam(required=false, value="rdId") String rdId,
			@RequestParam(required=false, value="rdPassword") String rdPassword ){
		
		R r = new R();
		if(cardId ==null && rdId == null) {
			r.message = "卡号和读者证号不能同时为空";
			r.success = "0";
			return r;
		}
		//这里的密码必须是32位 md5加密 小写传过来
		if(rdPassword == null) {
			r.message = "密码不能为空";
			r.success = "0";
			return r;
		}
		
		ReaderCardInfo card = null;
		if(rdId != null && !"".equals(rdId)) {//证明传的是证号，这里要再查询一次
			card = readerCardInfoService.getByRdId(rdId);
		}
		if(cardId != null && !"".equals(cardId)) {
			cardId = cardId.trim();
			card = readerCardInfoService.get(cardId);
			if(card == null) {
				r.message = "该卡号不存在有效记录";
				r.success = "0";
				return r;
			}
			if(card.getIsUsable() == 0) {
				r.message = "该卡暂不能使用";
				r.success = "0";
				return r;
			}
			rdId = card.getRdId();
		}
		

		Reader reader = new Reader();
		
		reader.setRdId(rdId);
		reader.setRdPasswd(rdPassword);
		reader = readerService.readerLogin(reader, true);
		if(reader == null) { //认证不通过
			//查询注册读者证号
			NetReader netReader = new NetReader();
			netReader.setReaderId(rdId);
			netReader.setReaderPassword(rdPassword);
			netReader = netReaderService.netReaderLogin(netReader, true);
			if(netReader == null) {
				r.message = "读者验证失败，请检查证号或密码";
				r.success = "0";
				return r;
			}
			r.rdId = netReader.getReaderId();
			r.rdPhone = netReader.getReaderMobile();
			r.rdName = netReader.getReaderName();
			r.rdSex = "" + netReader.getReaderGender();
//			r.rdState = "" + netReader.getCheckState();
			r.rdState = "" + netReader.getReaderCardState();
			r.rdType = netReader.getReaderType();
			r.rdLib = netReader.getReaderLib();
			r.rdLibCode = netReader.getReaderLib();
			r.success = "1";
			return r;
		}
		
//		if(reader == null) { //认证不通过
//			r.message = "读者验证失败，请检查证号或密码";
//			r.success = "0";
//			return r;
//		}
//		if(reader.getRdCFState() != 1) {
//			r.message = "读者状态无效";
//			r.success = "0";
//			return r;
//		}
//		if(reader.getRdEndDate().before(new Date())) {
//		r.message = "证已过期";
//		r.success = "0";
//		return r;
//		}
		LibCode lib = libCodeService.getLibByCode(reader.getRdLibCode());
		RdAccount rdAccount = rdAccountService.get(rdId);
		r.rdId = reader.getRdId();
		r.rdPhone = reader.getRdLoginId();
		r.rdName = reader.getRdName();
		r.rdCertify = reader.getRdCertify();
		r.rdSex = "" + reader.getRdSex();
		r.rdState = reader.getRdCFState() + "";
		r.rdStateStr = reader.getRdStateStr();
		r.rdGlobal = reader.getRdGlobal() + "";
		r.rdType = reader.getRdType();
		r.rdLibType = reader.getRdLibType();
		r.rdLib = reader.getRdLib();
		r.rdLibCode = reader.getRdLibCode();
		r.rdInTimeStr = reader.getRdInTimeStr();
		r.rdStartDateStr = reader.getRdStartDateStr();
		System.out.println(reader.getRdStartDateStr() + "=====");
		r.rdEndDateStr = reader.getRdEndDateStr();
		r.webserviceUrl = lib.getWebserviceUrl();
		if(rdAccount != null) {
			r.rdAccount = rdAccount.getPrepay() + "";
			r.rdAccountStatus = rdAccount.getStatus() + "";
		}
		r.success = "1";
		return r;
	}

	
	/**
	 * 读者登陆接口，不需要密码
	 * example：http://localhost:8089/onecard/api/reader/readerLoginWithoutPasswd?cardId=abcdedf
	 * &appcode=interlib&enc=a8b148c7fb68928f5d1f6bbcc3dfed7f&userid=admin&libcode=999
	 * @param request
	 * @param cardId
	 * @param rdId
	 * @param rdPassword
	 * @return
	 */
	@RequestMapping(value="/readerLoginWithoutPasswd",produces="application/xml")
	public @ResponseBody R readerLoginWithoutPasswd(HttpServletRequest request, 
			@RequestParam(required=false, value="rdId") String rdId){
		
		R r = new R();
		if(rdId == null) {
			r.message = "读者证号不能为空";
			r.success = "0";
			return r;
		}
		Reader reader = new Reader();
		reader = readerService.getReader(rdId, (byte)2);
		if(reader == null) { //认证不通过
			//查询注册读者证号
			NetReader netReader = new NetReader();
			netReader = netReaderService.getNetReaderByRdId(rdId);
			if(netReader == null) {
				r.message = "读者验证失败，请检查证号或密码";
				r.success = "0";
				return r;
			}
			r.rdId = netReader.getReaderId();
			r.rdPhone = netReader.getReaderMobile();
			r.rdName = netReader.getReaderName();
			r.rdSex = "" + netReader.getReaderGender();
			r.rdState = "" + netReader.getReaderCardState();
			r.rdType = netReader.getReaderType();
			r.rdLib = netReader.getReaderLib();
			r.rdLibCode = netReader.getReaderLib();
			String netRdpasswd = EncryptDecryptData.decryptWithCode(DES_STATIC_KEY, netReader.getReaderPassword());
			r.rdPassword = MD5Util.MD5Encode(netRdpasswd);
			r.success = "1";
			return r;
		}
//		if(reader == null) { //认证不通过
//			r.message = "读者验证失败，请检查证号或密码";
//			r.success = "0";
//			return r;
//		}
		if(reader.getRdCFState() != 1) {
			r.message = "读者状态无效";
			r.success = "0";
			return r;
		}
		LibCode lib = libCodeService.getLibByCode(reader.getRdLibCode());
		RdAccount rdAccount = rdAccountService.get(rdId);
		r.rdId = reader.getRdId();
		r.rdPhone = reader.getRdLoginId();
		r.rdName = reader.getRdName();
		r.rdSex = "" + reader.getRdSex();
		r.rdState = reader.getRdCFState() + "";
		r.rdStateStr = reader.getRdStateStr();
		r.rdGlobal = reader.getRdGlobal() + "";
		r.rdType = reader.getRdType();
		r.rdLibType = reader.getRdLibType();
		r.rdLib = reader.getRdLib();
		r.rdLibCode = reader.getRdLibCode();
		r.rdInTimeStr = reader.getRdInTimeStr();
		r.rdStartDateStr = reader.getRdStartDateStr();
		r.rdEndDateStr = reader.getRdEndDateStr();
		r.webserviceUrl = lib.getWebserviceUrl();
		String rdpasswd = reader.getRdPasswd();
		rdpasswd = EncryptDecryptData.decryptWithCode(DES_STATIC_KEY, rdpasswd);
		r.rdPassword = MD5Util.MD5Encode(rdpasswd);
		if(rdAccount != null) {
			r.rdAccount = rdAccount.getPrepay() + "";
			r.rdAccountStatus = rdAccount.getStatus() + "";
		}
		r.success = "1";
		return r;
	}
	

	/**
	 * 读者验证接口通过手机号。身份证。读者证号登录
	 * example：http://localhost:8090/onecard/api/reader/readerLoginByOther?rdLoginId=13986154677
	 * &rdPassword=e10adc3949ba59abbe56e057f20f883e
	 * &appcode=interlib&enc=a592ab975ee901ee0472be74238ed768&userid=admin&libcode=999
	 * @param request
	 * @param cardId
	 * @param rdId
	 * @param rdCertify
	 * @param rdLoginId
	 * @param rdPassword
	 * @return
	 */
	@RequestMapping(value="/readerLoginByOther",produces="application/xml")
	public @ResponseBody R readerLoginByOther(HttpServletRequest request, 
			@RequestParam(required=false, value="cardId") String cardId,
			@RequestParam(required=false, value="rdId") String rdId,
			@RequestParam(required=false, value="rdPassword") String rdPassword ){
		
		R r = new R();
		if(cardId ==null && rdId == null) {
			r.message = "卡号、读者证号、身份证号、手机号不能同时为空";
			r.success = "0";
			return r;
		}
		//这里的密码必须是32位 md5加密 小写传过来
		if(rdPassword == null) {
			r.message = "密码不能为空";
			r.success = "0";
			return r;
		}
		
		ReaderCardInfo card = null;
		if(rdId != null && !"".equals(rdId)) {//证明传的是证号，这里要再查询一次
//			card = readerCardInfoService.getByRdId(rdId);
			System.out.println("rdId传入的是："+rdId);
		}
		if(cardId != null && !"".equals(cardId)) {
			cardId = cardId.trim();
			card = readerCardInfoService.get(cardId);
			if(card == null) {
				r.message = "该卡号不存在有效记录";
				r.success = "0";
				return r;
			}
			if(card.getIsUsable() == 0) {
				r.message = "该卡暂不能使用";
				r.success = "0";
				return r;
			}
			rdId = card.getRdId();
		}
		
		Reader reader = null;
		Reader newreader = new Reader();

		if(rdId != null && !"".equals(rdId)) {//证明传的是身份证号，这里要再查询一次
			 reader = readerService.getReaderByRdCertify(rdId);
			 if(reader == null){
				 rdId = rdId.trim();
				 reader = readerService.getReaderByRdLoginId(rdId);
					if(reader == null) { //认证不通过
						//查询注册读者证号
						NetReader netReader = new NetReader();
						netReader.setReaderId(rdId);
						netReader.setReaderPassword(rdPassword);
						netReader = netReaderService.netReaderLogin(netReader, true);
						if(netReader == null) {
							r.message = "读者验证失败，请检查证号或密码";
							r.success = "0";
							return r;
						}
						r.rdId = netReader.getReaderId();
						r.rdPhone = netReader.getReaderMobile();
						r.rdName = netReader.getReaderName();
						r.rdSex = "" + netReader.getReaderGender();
						r.rdState = "" + netReader.getCheckState();
						r.rdType = netReader.getReaderType();
						r.rdLib = netReader.getReaderLib();
						r.rdLibCode = netReader.getReaderLib();
						r.success = "1";
						return r;
				 }
				
			 }
			 if(reader.getRdCertify() == null ||reader.getRdLoginId() == null){
				r.message = "身份证，或者手机号为空,请填写完整信息再使用";
				r.success = "0";
				return r;
			 }
			rdId = reader.getRdId();
		}

		newreader.setRdId(rdId);
		newreader.setRdPasswd(rdPassword);
		newreader = readerService.readerLogin(newreader, true);
		
		

		if(newreader.getRdCFState() != 1) {
			r.message = "读者状态无效";
			r.success = "0";
			return r;
		}
		LibCode lib = libCodeService.getLibByCode(newreader.getRdLibCode());
		RdAccount rdAccount = rdAccountService.get(rdId);
		r.rdId = newreader.getRdId();
		r.rdPhone = newreader.getRdLoginId();
		r.rdCertify=newreader.getRdCertify();
		r.rdName = newreader.getRdName();
		r.rdSex = "" + newreader.getRdSex();
		r.rdState = newreader.getRdCFState() + "";
		r.rdStateStr = newreader.getRdStateStr();
		r.rdGlobal = newreader.getRdGlobal() + "";
		r.rdType = newreader.getRdType();
		r.rdLibType = newreader.getRdLibType();
		r.rdLib = newreader.getRdLib();
		r.rdLibCode = newreader.getRdLibCode();
		r.rdInTimeStr = newreader.getRdInTimeStr();
		r.rdStartDateStr = newreader.getRdStartDateStr();
		System.out.println(newreader.getRdStartDateStr() + "=====");
		r.rdEndDateStr = newreader.getRdEndDateStr();
		r.webserviceUrl = lib.getWebserviceUrl();
		if(rdAccount != null) {
			r.rdAccount = rdAccount.getPrepay() + "";
			r.rdAccountStatus = rdAccount.getStatus() + "";
		}
		r.success = "1";
		return r;
	}
	
	/**
	 * 读者验证是否授权接口
	 * example：http://localhost:8089/onecard/api/reader/readerIsAuthor?rdId=111111
	 * &appcode=interlib&enc=a44dacec4b9dc5d90f3704f1e4940733&userid=admin&libcode=999
	 * @param request
	 * @param rdId
	 * @return
	 */
	
	@RequestMapping(value="/readerIsAuthor",produces="application/xml")
	public @ResponseBody R readerIsAuthor(HttpServletRequest request, 
			@RequestParam(required=false, value="rdId") String rdId){
		
		R r = new R();
		if( rdId == null) {
			r.message = "读者证号不能为空";
			r.success = "0";
			return r;
		}		

		Reader reader = new Reader();
		reader = readerService.getReader(rdId, (byte)2);
		if(reader==null){
			r.success = "0";
			r.message = "读者不存在。请检查证号";
			return r;
		}

		r.rdId = reader.getRdId();
		System.out.println("reader.getIsAuthor()==="+reader.getIsAuthor());
		if(reader.getIsAuthor() != null){
			r.isAuthor = reader.getIsAuthor()+"";
		}else{
			r.isAuthor = "0";
		}
		
		r.success = "1";
		return r;
	}
	
	/**
	 * 国图上传接口
	 * example：http://localhost:8089/onecard/api/reader/readerUploadNLC?rdId=111111
	 * &appcode=interlib&enc=a44dacec4b9dc5d90f3704f1e4940733&userid=admin&libcode=999
	 * @param request
	 * @param rdId
	 * @return
	 */
	
	@RequestMapping(value="/readerUploadNLC",produces="application/xml")
	public @ResponseBody R readerUploadNLC(HttpServletRequest request, 
			@RequestParam(required=false, value="rdId") String rdId){
		String userUrl =
//				"http://10.117.3.184:8080/idm/jsoninterface/userManagerForeign/addUser.do";
		"http://192.168.3.184:8080/idm/jsoninterface/userManagerForeign/addUser.do";

		R r = new R();
		if( rdId == null) {
			r.message = "读者证号不能为空";
			r.success = "0";
			return r;
		}
		Reader reader = new Reader();
		
		
		reader = readerService.getReader(rdId, (byte) 1);
		if(reader==null){
			r.success = "0";
			r.message="此读者不存在。请检查账号";
			return r;
		}
		
		String rdPasswd = reader.getRdPasswd();
	
//		String rdName = new String(reader.getRdName().getBytes("iso-8859-1"), "utf-8");
		String rdName = reader.getRdName();
		
		System.out.println("rdName=="+rdName);
		rdPasswd = EncryptDecryptData.decryptWithCode(DES_STATIC_KEY, rdPasswd);//同步的密码是明文的

		String rdCertify = reader.getRdCertify();
		if(rdCertify ==null || "".equals(rdCertify)){
			r.success = "0";
			r.message="身份证号为空，请补全信息后操作！";
			return r;
		}
		String rdSex = IdCard.getGenderByIdCard(rdCertify);
		if(rdSex=="1"){
			rdSex="男";
		}else if(rdSex=="2"){
			rdSex="女";
		}
		DecimalFormat df=new DecimalFormat("00");
		String str1=df.format(IdCard.getMonthByIdCard(rdCertify));
		String str2=df.format(IdCard.getDateByIdCard(rdCertify));
				
		String birthday = IdCard.getYearByIdCard(rdCertify)+"-"+str1+"-"+str2;
				
			System.out.println("birthday===="+birthday);	
//				2018-05-08
			try {
			HttpClient httpclient = new HttpClient();
			PostMethod postMethod = new PostMethod(userUrl);

			
				// 在header中放入接口校验信息
				//注意服务器时间要准确
				postMethod.addRequestHeader("Content-type","application/x-www-form-urlencoded; charset=UTF-8");
				postMethod.setRequestHeader("jsonuser", "user1");
				SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String timestamp=format.format(new Date());
				postMethod.setRequestHeader("timestamp", timestamp);
				postMethod.setRequestHeader("logincode", LOGINCODE);
				//这里的user1及test123需要根据实际情况给出
				postMethod.setRequestHeader("token",getToken(timestamp, "user1", LOGINCODE));
				postMethod.setParameter("userDetailInfo","uid="+rdId+",cn="
					    		+rdName+",userPassword="+rdPasswd+",idtype=01,idno="+rdCertify+
					    		",sex="+rdSex+",birthday="+birthday+",country=china"); 
				int statusCode1 = httpclient.executeMethod(postMethod);
				if (statusCode1 != HttpStatus.SC_OK) {
					System.out.println("Method is wrong "
							+ postMethod.getStatusLine());
				}
				String response = postMethod.getResponseBodyAsString();
				System.out.println("=========="+response);
				readerService.updateIsAuthor(rdId, 1);
				r.success = "1";
				r.message="上传国图成功";
				return r;
			} catch (Exception e) {
				
				e.printStackTrace();		
				r.success = "0";
				r.message="链接超时";
				return r;
			}

	}
	//国图获取token的值
	private static String getToken(String timeStamp,String jsonUser,String loginCode){
		
		String temp=loginCode+jsonUser+timeStamp;
		String token=MD5Util.MD5Encode(temp);
		System.out.println("=======token:"+token+"====temp:"+temp+"====================");
		return token;
	}
	/**
	 * 国图用户删除接口
	 * example：http://localhost:8089/onecard/api/reader/readerUploadNLC?rdId=111111
	 * &appcode=interlib&enc=a44dacec4b9dc5d90f3704f1e4940733&userid=admin&libcode=999
	 * @param request
	 * @param rdId
	 * @return
	 */
	
	@RequestMapping(value="/readerDeleteNLC",produces="application/xml")
	public @ResponseBody R readerDeleteNLC(HttpServletRequest request, 
			@RequestParam(required=false, value="rdId") String rdId){
		String userUrl =
//				"http://10.117.3.184:8080/idm/jsoninterface/userManagerForeign/addUser.do";
		"http://192.168.3.184:8080/idm/jsoninterface/userManagerForeign/delUser.do";
		
		R r = new R();
		if( rdId == null) {
			r.message = "读者证号不能为空";
			r.success = "0";
			return r;
		}
	
//				2018-05-08
		try {
		HttpClient httpclient = new HttpClient();
		PostMethod postMethod = new PostMethod(userUrl);
	
//	    NameValuePair[] data1 = { new NameValuePair("userDetailInfo", "uid="+rdId) };
//		
//			postMethod.setRequestBody(data1);
//			System.out.println("data1===="+data1);
			postMethod.setRequestHeader("jsonuser", "user1");
			SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String timestamp=format.format(new Date());
			postMethod.setRequestHeader("timestamp", timestamp);
			postMethod.setRequestHeader("logincode", "netjsonpass123");
			postMethod.setRequestHeader("token",getToken(timestamp, "user1", "netjsonpass123"));
			postMethod.setParameter("uid",rdId);
			int statusCode1 = httpclient.executeMethod(postMethod);
			if (statusCode1 != HttpStatus.SC_OK) {
				System.out.println("Method is wrong "
						+ postMethod.getStatusLine());
			}
			String response = postMethod.getResponseBodyAsString();
			System.out.println(response);
			
			JSONObject json = JSONObject.fromObject(response);
			String code= json.getString("code");
			System.out.println("=====code:"+code+"========");
		} catch (Exception e) {
			e.printStackTrace();
		}
			readerService.updateIsAuthor(rdId, 0);
		r.success = "1";
		r.message="国图用户删除成功";
		return r;
	}
	/**
	 * 国图用户修改接口
	 * example：http://localhost:8089/onecard/api/reader/readerUploadNLC?rdId=111111
	 * &appcode=interlib&enc=a44dacec4b9dc5d90f3704f1e4940733&userid=admin&libcode=999
	 * @param request
	 * @param rdId
	 * @return
	 */
	
//	@RequestMapping(value="/readerUpdateNLC",produces="application/xml")
	@RequestMapping(value="/readerUpdateNLC",produces = "text/html;charset=UTF-8")
	public @ResponseBody R readerUpdateNLC(HttpServletRequest request, 
			@RequestParam(required=false, value="rdId") String rdId){
		String userUrl =
//				"http://10.117.3.184:8080/idm/jsoninterface/userManagerForeign/modifyUser.do";
		"http://192.168.3.184:8080/idm/jsoninterface/userManagerForeign/modifyUser.do";
		
		R r = new R();
		if( rdId == null) {
			r.message = "读者证号不能为空";
			r.success = "0";
			return r;
		}
		Reader reader = new Reader();
		
		
		reader = readerService.getReader(rdId, (byte) 1);
		if(reader==null){
			r.success = "0";
			r.message="此读者不存在。请检查账号";
			return r;
		}
		
//			String rdName = new String(reader.getRdName().getBytes("iso-8859-1"), "utf-8");
			String rdName = reader.getRdName();
		System.out.println(rdName);
		String rdPasswd = reader.getRdPasswd();
		rdPasswd = EncryptDecryptData.decryptWithCode(DES_STATIC_KEY, rdPasswd);//同步的密码是明文的

		String rdCertify = reader.getRdCertify();
		if(rdCertify ==null  || "".equals(rdCertify)){
			r.success = "0";
			r.message="身份证号为空，请补全信息后操作！";
			return r;
		}
		String rdSex = IdCard.getGenderByIdCard(rdCertify);
		if(rdSex=="1"){
			rdSex="男";
		}else if(rdSex=="2"){
			rdSex="女";
		}
		DecimalFormat df=new DecimalFormat("00");
		String str1=df.format(IdCard.getMonthByIdCard(rdCertify));
		String str2=df.format(IdCard.getDateByIdCard(rdCertify));
				
		String birthday = IdCard.getYearByIdCard(rdCertify)+"-"+str1+"-"+str2;
				
			System.out.println("birthday===="+birthday);	
//				2018-05-08
		try {
		HttpClient httpclient = new HttpClient();
		PostMethod postMethod = new PostMethod(userUrl);
	    NameValuePair[] data1 = { new NameValuePair("userDetailInfo", "uid="+rdId+",cn="
	    		+rdName+",userPassword="+rdPasswd+",idtype=01,idno="+rdCertify+
	    		",sex="+rdSex+",birthday="+birthday+",country=china") };
		
			postMethod.setRequestBody(data1);
			System.out.println("data1===="+data1);
			postMethod.addRequestHeader("Content-type","application/x-www-form-urlencoded; charset=UTF-8");

			postMethod.setRequestHeader("jsonuser", "user1");
			SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String timestamp=format.format(new Date());
			postMethod.setRequestHeader("timestamp", timestamp);
			postMethod.setRequestHeader("logincode", "netjsonpass123");
			postMethod.setRequestHeader("token",getToken(timestamp, "user1", "netjsonpass123"));
			int statusCode1 = httpclient.executeMethod(postMethod);
			if (statusCode1 != HttpStatus.SC_OK) {
				System.out.println("Method is wrong "
						+ postMethod.getStatusLine());
			}
			String response = postMethod.getResponseBodyAsString();
			System.out.println(response);
			
			JSONObject json = JSONObject.fromObject(response);
			String code= json.getString("code");
			System.out.println("=====code:"+code+"========");
		} catch (Exception e) {
			e.printStackTrace();
			
			r.success = "0";
			r.message="链接超时";
			return r;
		}
			readerService.updateIsAuthor(rdId, 1);
		r.success = "1";
		r.message="国图用户修改成功";
		return r;
	}
	
	/**
	 * 更新opac读者
	 * @param reader
	 * @return
	 */
	public Map updateOpacReader(Reader reader, LibCode lib,String userid) {
		//把操作员也同步到opac 20140322
		Reader adminReader = readerService.getReader(userid, (byte)2);
		String regman="";
		if(adminReader!=null){
			regman=adminReader.getRdId();//对应操作的读者账号
			reader.setRegman(regman);
		}
		Map map = null;
		
		String webserviceUrl = lib.getWebserviceUrl();
		
		if(!"".equals(webserviceUrl) && webserviceUrl != null) {
			
			webserviceUrl = webserviceUrl + (webserviceUrl.endsWith("/")?"":"/") +"webservice/readerWebservice";
			
			map = new HashMap();
			JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();
			
			factory.setServiceClass(ReaderWebservice.class);
			
			factory.setAddress(webserviceUrl);
			
			ReaderWebservice client = (ReaderWebservice)factory.create();
			
			reader.setRdLib(reader.getRdLibCode());
			
			String realPasswd =  reader.getOldRdpasswd();//reader.getRdPasswd(); modify by 2014-05-13同步认证使用时旧密码
			
			try {
				realPasswd = EncryptDecryptData.decryptWithCode(DES_STATIC_KEY, realPasswd);//同步的密码是明文的
				reader.setRdPasswd(realPasswd);
				com.interlib.opac.webservice.Reader r = DomainTransform.transToWebserviceReader(reader);
				String rdpasswdMd5 = MD5Util.MD5Encode(realPasswd, "utf-8");
				client.updateReaderInfo(r, rdpasswdMd5); //调用接口更新到interlib
				readerService.updateSynStatus(reader.getRdId(),1);//ADD 2014-05-15 修改同步状态
				map.put("success", 1);
				
			} catch(Exception e) {
				e.printStackTrace();
				map.put("success", -2);
				map.put("message", Cautions.SYNCFAIL);
				String exception = e.toString();
				if (exception.contains("!")) {
					exception = exception.substring(0, exception.indexOf("!"));
				}
				map.put("exception", exception);
			}
		} else {
			map = new HashMap();
			map.put("success", -3);
			map.put("message", Cautions.EMPTY_URL);
		}
		return map;
	}
	
	public Map<String, Object> updateOpacReaderPasswd(Reader reader, String oldRdpasswd,
			String newRdpasswd) {
		Map<String, Object> map = null;

		LibCode lib = libCodeService.getLibByCode(reader.getRdLibCode());

		String webserviceUrl = lib.getWebserviceUrl();

		if (!"".equals(webserviceUrl) && webserviceUrl != null) {

			webserviceUrl = webserviceUrl
					+ (webserviceUrl.endsWith("/") ? "" : "/")
					+ "webservice/readerWebservice";

			map = new HashMap<String, Object>();
			JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();

			factory.setServiceClass(ReaderWebservice.class);

			factory.setAddress(webserviceUrl);

			ReaderWebservice client = (ReaderWebservice) factory.create();

			reader.setRdLib(reader.getRdLibCode());
			try {
				client.updateReaderPasswd(reader.getRdId(), oldRdpasswd, newRdpasswd); // 调用接口更新密码到opac(
				map.put("success", 1);

			} catch (Exception e) {
				e.printStackTrace();
				map.put("success", -2);
				map.put("message", Cautions.SYNCFAIL);
				String exception = e.toString();
				if (exception.contains("!")) {
					exception = exception.substring(0, exception.indexOf("!"));
				}
				map.put("exception", exception);
			}
		} else {
			map = new HashMap<String, Object>();
			map.put("success", -3);
			map.put("message", Cautions.EMPTY_URL);
		}
		return map;
	}
}
