package com.interlib.sso.sync;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.apache.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.interlib.opac.webservice.CustomHashMap;
import com.interlib.opac.webservice.CustomHashMapArray;
import com.interlib.opac.webservice.ReaderWebservice;
import com.interlib.sso.acs.Clients;
import com.interlib.sso.acs.serivce.AcsService;
import com.interlib.sso.common.Cautions;
import com.interlib.sso.common.Constants;
import com.interlib.sso.common.DateUtils;
import com.interlib.sso.common.EncryptDecryptData;
import com.interlib.sso.common.MD5Util;
import com.interlib.sso.domain.LibCode;
import com.interlib.sso.domain.RdAccount;
import com.interlib.sso.domain.Reader;
import com.interlib.sso.domain.ReaderCardInfo;
import com.interlib.sso.domain.ReaderSession;
import com.interlib.sso.domain.ReaderType;
import com.interlib.sso.service.LibCodeService;
import com.interlib.sso.service.RdAccountService;
import com.interlib.sso.service.ReaderCardInfoService;
import com.interlib.sso.service.ReaderService;
import com.interlib.sso.service.ReaderTypeService;
import com.interlib.sso.webservice.DomainTransform;

public class SyncQueue implements Runnable {
	
	private static final Logger logger = Logger.getLogger(SyncQueue.class);
	
	public static final String SEND_ADD_READER = "sendAddReader";
	
	public static final String SEND_UPDATE_READER = "sendUpdateReader";
	
	public static final String SEND_UPDATE_PASSWORD = "sendUpdatePassword";
	
	public static final String SEND_SYNC_READER = "sendSyncReader";
	
	public static final String SEND_CHANGE_READER = "sendChangeReader";
	
	public static final String SEND_REPAIR_READER = "sendRepairReader";
	
	public static final String SEND_SUSPEND_READER = "sendSuspendReader";
	
	public static final String SEND_REPORT_LOSS = "sendReportLoss";
	
	public static final String SEND_CANCEL_READER = "sendCancelReader";
	
	public static final String SEND_DEFER_READER = "sendDeferReader";
	
	public static final String SEND_VALIDATE_READER = "sendValidateReader";
	
	public static final String SEND_RESUME_READER = "sendResumeReader";
	
	public static final String SEND_QUIT_READER = "sendQuitReader";
	
	public static final String SEND_UPLOAD_IMAGE = "sendUploadImage";
	
	public static final String SEND_DELETE_READER = "sendDeleteReader";
	
	public ReaderService readerService;
	
	public LibCodeService libCodeService;
	
	public ReaderTypeService readerTypeService;
	
	public RdAccountService rdAccountService;
	
	public ReaderCardInfoService readerCardInfoService;
	
	public AcsService acsService;
	
	/* DES加密默认静态秘钥 */
	private static final String DES_STATIC_KEY = "64074f968502295ca41b7db452c7c639";

	private String sendType;
	
	private Reader reader;
	
	private Reader newReader;
	
	private ReaderCardInfo card;
	
	private String rdid;
	
	private String oldPassword;
	
	private String newPassword;
	
	private String cardSyncFlag;
	
	private LibCode lib;
	
	private String payed;
	
	public SyncQueue() {
		
	}
	
	public SyncQueue(Reader reader, ReaderService readerService, String sendType) {
		this.sendType = sendType;
		this.reader = reader;
		this.readerService = readerService;
	}
	
	public SyncQueue(Reader reader, ReaderCardInfo card, LibCodeService libCodeService, 
			ReaderTypeService readerTypeService, ReaderService readerService, String sendType) {
		this.sendType = sendType;
		this.reader = reader;
		this.card = card;
		this.libCodeService = libCodeService;
		this.readerTypeService = readerTypeService;
		this.readerService = readerService;
	}
	
	public SyncQueue(String rdid, String oldPassword, String newPassword, 
			LibCodeService libCodeService, ReaderService readerService, String sendType) {
		this.sendType = sendType;
		this.rdid = rdid;
		this.oldPassword = oldPassword;
		this.newPassword = newPassword;
		this.libCodeService = libCodeService;
		this.readerService = readerService;
	}
	
	public SyncQueue(Reader reader, ReaderCardInfo card, String cardSyncFlag, 
			LibCodeService libCodeService, ReaderService readerService, String sendType) {
		this.sendType = sendType;
		this.reader = reader;
		this.card = card;
		this.cardSyncFlag = cardSyncFlag;
		this.libCodeService = libCodeService;
		this.readerService = readerService;
	}
	
	public SyncQueue(Reader reader, LibCode lib, ReaderService readerService, String sendType) {
		this.sendType = sendType;
		this.reader = reader;
		this.lib = lib;
		this.readerService = readerService;
	}
	
	public SyncQueue(Reader reader, Reader newReader, String payed, LibCode lib,
			AcsService acsService, String sendType) {
		this.sendType = sendType;
		this.reader = reader;
		this.newReader = newReader;
		this.payed = payed;
		this.lib = lib;
		this.acsService = acsService;
	}
	
	public SyncQueue(Reader reader, String payed, LibCode lib, AcsService acsService, String sendType) {
		this.sendType = sendType;
		this.reader = reader;
		this.payed = payed;
		this.lib = lib;
		this.acsService = acsService;
	}
	
	public void addToSyncRecord(Reader reader, ReaderCardInfo card) {
		com.interlib.opac.webservice.Reader webserviceReader = 
				DomainTransform.transToWebserviceReader(reader);// 转成webservice的reader对象
		LibCode lib = libCodeService.getLibByCode(reader.getRdLib());
		String webserviceUrl = lib.getWebserviceUrl();//接口地址
		String webserviceKey = MD5Util.MD5Encode(lib.getOpacKey());//接口密钥
		ReaderType readerType = readerTypeService.getReaderType(reader.getRdType());
		int valdate = readerType.getValdate(); //证有效期
		Integer indate = valdate / 30;
		try {
			JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();
			factory.setServiceClass(ReaderWebservice.class);
			
			webserviceUrl = webserviceUrl
					+ (webserviceUrl.endsWith("/") ? "" : "/")
					+ "webservice/readerWebservice";
			
			factory.setAddress(webserviceUrl);
			ReaderWebservice client = (ReaderWebservice) factory.create();
			List<String> retList = client.addReader(webserviceReader, indate);
			String ret = "";
			for(String r : retList) {
				ret = r;
			}
			logger.info("新增读者：" + ret);
			if(!ret.equals("OK")) {
				return;
			}
			if(card != null) {
				//增加卡号同步
				client.updateReaderCard(reader.getRdId(), Integer.parseInt(card.getCardType()), card.getCardId(), webserviceKey);
			}
			readerService.updateSynStatus(reader.getRdId(), 1);
		} catch(Exception e) {
			logger.error(e.toString());
		}
	}

	/**
	 * 同步读者信息到其他系统，这里是opac
	 * 
	 * @param request
	 * @param response
	 * @param rdId
	 */
	@SuppressWarnings("rawtypes")
	public String syncReaderInfo(String rdId) {
		StringBuffer message = new StringBuffer();

		Reader reader = readerService.getReader(rdId, (byte) 2);
		RdAccount rdAccount = rdAccountService.get(rdId);
		reader.setRdDeposit(rdAccount.getDeposit());
		ReaderCardInfo card = readerCardInfoService.getByRdId(rdId);
		String syncFlag = "none";
		if(card != null) { 
			syncFlag = "update";
			reader.setCardId(card.getCardId());
		} else {
			syncFlag = "delete";
		}
		Map map = syncOpacReader(reader, card, syncFlag);
		
		int success = 1;
		String msg = "";
		String exception = "";
		if (map != null) {
			success = (Integer) map.get("success");
			msg = (String) map.get("message");
			exception = (String) map.get("exception");
		}
		message.append("{");
		message.append("\"success\": " + success + ",");
		message.append("\"message\": \"" + msg + "\",");
		message.append("\"exception\": \"" + exception + "\"");
		message.append("}");
		
		return message.toString();
	}
	
	/**
	 * 同步方法,判断是否存在，存在则更新，不存在则新增
	 * @param reader
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Map syncOpacReader(Reader reader, ReaderCardInfo cardInfo, String cardSyncFlag) {
		// 把操作员也同步到opac 20140322
		ReaderSession readerSession = (ReaderSession) SecurityUtils
				.getSubject().getSession().getAttribute("READER_SESSION");
		String regman = "";
		if (readerSession.getReader() != null) {
			regman = readerSession.getReader().getRdId();// 对应操作的读者账号
			reader.setRegman(regman);
		}
		Map map = null;
		LibCode lib = libCodeService.getLibByCode(reader.getRdLibCode());
		String webserviceUrl = lib.getWebserviceUrl();
		String webserviceKey = MD5Util.MD5Encode(lib.getOpacKey());
		if (!"".equals(webserviceUrl) && webserviceUrl != null) {
			webserviceUrl = webserviceUrl
					+ (webserviceUrl.endsWith("/") ? "" : "/")
					+ "webservice/readerWebservice";
			map = new HashMap();
			JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();
			factory.setServiceClass(ReaderWebservice.class);
			factory.setAddress(webserviceUrl);
			ReaderWebservice client = (ReaderWebservice) factory.create();
			reader.setRdLib(reader.getRdLibCode());
			String realPasswd = reader.getOldRdpasswd();// reader.getRdPasswd();//这里的密码存的是md5加密的
														// modify by
														// 2017-09-14同步认证使用时旧密码
			try {
				realPasswd = EncryptDecryptData.decryptWithCode(DES_STATIC_KEY,
						realPasswd);// 同步的密码是明文的
				
				reader.setRdPasswd(realPasswd);
				//看看opac这个读者是否存在
				com.interlib.opac.webservice.Reader r = client.getReader(reader.getRdId(), Constants.HAND_KEY);
				r = DomainTransform.transToWebserviceReader(reader);
				int addMonth = DateUtils.getMonthSpace(reader.getRdStartDate(), reader.getRdEndDate());
				CustomHashMapArray result = client.addOrUpdateReader(r, MD5Util.MD5Encode(r.getRdid() + lib.getOpacKey()), addMonth);//更新opac上读者信息
				if(r.getRdphoto() != null && r.getRdphoto().length > 0) {
					//String rdpasswdMd5 = MD5Util.MD5Encode(realPasswd, "utf-8");
					client.updateReaderInfo(r, realPasswd);//上传照片
				}
				List<CustomHashMap> resultList = result.getItem();
				for(CustomHashMap resultMap : resultList) {
					String code = resultMap.getCode();
					String name = resultMap.getName();
					if(!code.equals("1001")&&!code.equals("1000")) {//同步失败
						map.put("success", -1);
						map.put("message", Cautions.SYNCFAIL);
						String exception = name;
						map.put("exception", exception);
						return map;
					}
				}
				if(cardSyncFlag.equals("add") || cardSyncFlag.equals("update")) {
					client.updateReaderCard(reader.getRdId(), 0, reader.getCardId(), webserviceKey);//更新或者添加
				} else if(cardSyncFlag.equals("delete")){
					if(cardInfo != null) {
						client.deleteReaderCard(cardInfo.getCardId(), webserviceKey);
					}
				}
				readerService.updateSynStatus(reader.getRdId(), 1);// 修改同步状态
				map.put("success", 1);
			} catch (Exception e) {
				e.printStackTrace();
				map.put("success", -1);
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
	
	/**
	 * 更新opac读者
	 * 
	 * @param reader
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Map updateOpacReader(Reader reader, LibCode lib) {
		// 把操作员也同步到opac 20140322
		ReaderSession readerSession = (ReaderSession) SecurityUtils
				.getSubject().getSession().getAttribute("READER_SESSION");
		String regman = "";
		if (readerSession.getReader() != null) {
			regman = readerSession.getReader().getRdId();// 对应操作的读者账号
			reader.setRegman(regman);
		}
		Map map = null;

		String webserviceUrl = lib.getWebserviceUrl();

		if (!"".equals(webserviceUrl) && webserviceUrl != null) {
			webserviceUrl = webserviceUrl
					+ (webserviceUrl.endsWith("/") ? "" : "/")
					+ "webservice/readerWebservice";

			map = new HashMap();
			JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();

			factory.setServiceClass(ReaderWebservice.class);

			factory.setAddress(webserviceUrl);

			ReaderWebservice client = (ReaderWebservice) factory.create();

			reader.setRdLib(reader.getRdLibCode());

			String realPasswd = reader.getOldRdpasswd();// reader.getRdPasswd();
														// modify by
														// 2017-09-14同步认证使用时旧密码

			try {
				realPasswd = EncryptDecryptData.decryptWithCode(DES_STATIC_KEY,
						realPasswd);// 同步的密码是明文的
				reader.setRdPasswd(realPasswd);
				com.interlib.opac.webservice.Reader r = DomainTransform.transToWebserviceReader(reader);
				//String rdpasswdMd5 = MD5Util.MD5Encode(realPasswd, "utf-8");
				client.updateReaderInfo(r, realPasswd); // 调用接口更新到interlib
				readerService.updateSynStatus(reader.getRdId(), 1);// ADD
																	// 2014-05-15
																	// 修改同步状态
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
			map = new HashMap();
			map.put("success", -3);
			map.put("message", Cautions.EMPTY_URL);
		}
		return map;
	}
	
	/**
	 * ADD 2014-05-13 修改密码之后同步密码到opac
	 * 
	 * @param rdid
	 * @param oldRdpasswd
	 * @param newRdpasswd
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Map UpdateOpacReaderPasswd(String rdId, String oldRdpasswd,
			String newRdpasswd) {
		Map map = null;
		Reader reader = readerService.getReader(rdId, (byte) 2);

		LibCode lib = libCodeService.getLibByCode(reader.getRdLibCode());

		String webserviceUrl = lib.getWebserviceUrl();

		if (!"".equals(webserviceUrl) && webserviceUrl != null) {

			webserviceUrl = webserviceUrl
					+ (webserviceUrl.endsWith("/") ? "" : "/")
					+ "webservice/readerWebservice";

			map = new HashMap();
			JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();

			factory.setServiceClass(ReaderWebservice.class);

			factory.setAddress(webserviceUrl);

			ReaderWebservice client = (ReaderWebservice) factory.create();

			reader.setRdLib(reader.getRdLibCode());
			try {
				client.updateReaderPasswd(rdId, oldRdpasswd, newRdpasswd); // 调用接口更新密码到opac(
																			// 新旧密码都不需要)
				String encryptPassword = EncryptDecryptData.encrypt(
						DES_STATIC_KEY, newRdpasswd);
				// ADD 添加同步到opac端 同步成功同时修改reader旧密码和标识
				readerService.updateOldPasswordAndSynStatus(rdId, "^" + encryptPassword + "^", 1);
				
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
			map = new HashMap();
			map.put("success", -3);
			map.put("message", Cautions.EMPTY_URL);
		}
		return map;
	}
	
	/**
	 * 新增读者
	 * @param reader
	 * @param payed
	 * @param libcode
	 */
	public void addReader(Reader reader, String payed, LibCode libcode) {
		ReaderSession readerSession = (ReaderSession) SecurityUtils
				.getSubject().getSession().getAttribute("READER_SESSION");
		acsService.newReader(libcode, reader, payed, readerSession.getReader().getRdId());
	
	}
	
	/**
	 * 新增读者web接口专用
	 * @param reader
	 * @param payed
	 * @param libcode
	 */
	public void addReaderSync(Reader reader, String payed, LibCode libcode) {
		acsService.newReader(libcode, reader, payed, reader.getRdId());
	}
	
	/**
	 * 修改读者证
	 * @param reader
	 * @param payed
	 * @param libcode
	 */
	public void updateReader(Reader reader, String payed, LibCode libcode) {
		ReaderSession readerSession = (ReaderSession) SecurityUtils
				.getSubject().getSession().getAttribute("READER_SESSION");
	}
	
	/**
	 * 修改读者照片
	 * @param reader
	 * @param libcode
	 */
	public void updateReaderImage(Reader reader, LibCode libcode) {
		ReaderSession readerSession = (ReaderSession) SecurityUtils
				.getSubject().getSession().getAttribute("READER_SESSION");
		acsService.uploadRdImage(libcode, reader, readerSession.getReader().getRdId());
	}
	
	/**
	 * 暂停读者证
	 * @param reader
	 * @param payed
	 * @param libcode
	 */
	public void suspendReader(Reader reader, String payed, LibCode libcode) {
		ReaderSession readerSession = (ReaderSession) SecurityUtils
				.getSubject().getSession().getAttribute("READER_SESSION");
		acsService.suspendReader(libcode, reader, readerSession.getReader().getRdId());
	}
	
	/**
	 * 读者验证
	 * @param reader
	 * @param payed
	 * @param libcode
	 */
	public void validateReader(Reader reader, String payed, LibCode libcode) {
		ReaderSession readerSession = (ReaderSession) SecurityUtils
				.getSubject().getSession().getAttribute("READER_SESSION");
		acsService.validateReader(libcode, reader, payed, readerSession.getReader().getRdId());
	}
	
	/**
	 * 延期
	 * @param reader
	 * @param payed
	 * @param libcode
	 */
	public void deferReader(Reader reader, String payed, LibCode libcode) {
		ReaderSession readerSession = (ReaderSession) SecurityUtils
				.getSubject().getSession().getAttribute("READER_SESSION");
		acsService.deferReader(libcode, reader, readerSession.getReader().getRdId());
	}
	
	/**
	 * 注销
	 * @param reader
	 * @param payed
	 * @param libcode
	 */
	public void cancelReader(Reader reader, String payed, LibCode libcode) {
		ReaderSession readerSession = (ReaderSession) SecurityUtils
				.getSubject().getSession().getAttribute("READER_SESSION");
		acsService.cancelReader(libcode, reader,  readerSession.getReader().getRdId());
	}
	
	/**
	 * 挂失
	 * @param reader
	 * @param payed
	 * @param libcode
	 */
	public void reportLoss(Reader reader, String payed, LibCode libcode) {
		ReaderSession readerSession = (ReaderSession) SecurityUtils
				.getSubject().getSession().getAttribute("READER_SESSION");
		acsService.reportLoss(libcode, reader, readerSession.getReader().getRdId());
	}
	
	/**
	 * 恢复读者证
	 */
	public void resumeReader(Reader reader, String payed, LibCode libcode) {
		ReaderSession readerSession = (ReaderSession) SecurityUtils
				.getSubject().getSession().getAttribute("READER_SESSION");
		acsService.resumeReader(libcode, reader, payed, readerSession.getReader().getRdId());
	}
	
	/**
	 * 换证
	 * @param reader
	 * @param newReader
	 * @param payed
	 * @param libcode
	 */
	public void changeReader(Reader reader, Reader newReader, String payed, LibCode libcode) {
		ReaderSession readerSession = (ReaderSession) SecurityUtils
				.getSubject().getSession().getAttribute("READER_SESSION");
		acsService.changeReader(libcode, reader, newReader, payed, readerSession.getReader().getRdId());
	}
	
	/**
	 * 补办
	 * @param reader
	 * @param newReader
	 * @param payed
	 * @param libcode
	 */
	public void repairReader(Reader reader, Reader newReader, String payed, LibCode libcode) {
		ReaderSession readerSession = (ReaderSession) SecurityUtils
				.getSubject().getSession().getAttribute("READER_SESSION");
		 acsService.repairReader(libcode, reader, newReader, payed, readerSession.getReader().getRdId());
	}
	
	/**
	 * 退证
	 * @param reader
	 * @param newReader
	 * @param payed
	 * @param libcode
	 */
	public void quitReader(Reader reader, String payed, LibCode libcode) {
		ReaderSession readerSession = (ReaderSession) SecurityUtils
				.getSubject().getSession().getAttribute("READER_SESSION");
		acsService.quitReader(libcode, reader, payed, readerSession.getReader().getRdId());
	}
	
	/**
	 * 删除读者
	 * @param reader
	 * @param libcode
	 */
	public void deleteReader(Reader reader, LibCode libcode) {
		ReaderSession readerSession = (ReaderSession) SecurityUtils
				.getSubject().getSession().getAttribute("READER_SESSION");
		acsService.deleteReader(libcode, reader, readerSession.getReader().getRdId());
	}
	
	@Override
	public void run() {
		if(SEND_ADD_READER.equals(sendType)) {
			addReaderSync(this.reader, this.payed, this.lib);
		}else if(SEND_UPDATE_PASSWORD.equals(sendType)) {
			UpdateOpacReaderPasswd(this.rdid, this.oldPassword, this.newPassword);
		} else if(SEND_SYNC_READER.equals(sendType)) {
			syncOpacReader(this.reader, this.card, this.cardSyncFlag);
		} else if(SEND_UPDATE_READER.equals(sendType)) {
			updateOpacReader(this.reader, this.lib);
		} else if(SEND_CHANGE_READER.equals(sendType)) {
			changeReader(this.reader, this.newReader, this.payed, this.lib);
		} else if(SEND_REPAIR_READER.equals(sendType)) {
			repairReader(this.reader, this.newReader, this.payed, this.lib);
		} else if(SEND_SUSPEND_READER.equals(sendType)) {
			suspendReader(this.reader, this.payed, this.lib);
		} else if(SEND_REPORT_LOSS.equals(sendType)) {
			reportLoss(this.reader, this.payed, this.lib);
		} else if(SEND_CANCEL_READER.equals(sendType)) {
			cancelReader(this.reader, this.payed, this.lib);
		} else if(SEND_DEFER_READER.equals(sendType)) {
			deferReader(this.reader, this.payed, this.lib);
		} else if(SEND_VALIDATE_READER.equals(sendType)) {
			validateReader(this.reader, this.payed, this.lib);
		} else if(SEND_RESUME_READER.equals(sendType)) {
			resumeReader(this.reader, this.payed, this.lib);
		} else if(SEND_QUIT_READER.equals(sendType)) {
			quitReader(this.reader, this.payed, this.lib);
		} else if(SEND_UPLOAD_IMAGE.equals(sendType)) {
			updateReaderImage(this.reader, this.lib);
		} else if(SEND_DELETE_READER.equals(sendType)) {
			deleteReader(this.reader, this.lib);
		}
	}
}
