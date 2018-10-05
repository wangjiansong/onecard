package com.interlib.sso.acs.serivce;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Date;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.interlib.sso.acs.Clients;
import com.interlib.sso.acs.Extractor;
import com.interlib.sso.common.Constants;
import com.interlib.sso.common.EncryptDecryptData;
import com.interlib.sso.common.ParsePic;
import com.interlib.sso.common.TimeUtils;
import com.interlib.sso.common.UUIDGenerator;
import com.interlib.sso.domain.LibCode;
import com.interlib.sso.domain.Reader;
import com.interlib.sso.domain.SyncRecord;
import com.interlib.sso.service.LibCodeService;
import com.interlib.sso.service.SyncRecordService;

/** 
 * @author home
 * 有关读者证操作的接口
 */
@Service
public class AcsService {
	
	private static final Logger logger = Logger.getLogger(AcsService.class);

	
	@Autowired
	private SyncRecordService syncRecordService;
	
	@Autowired
	private LibCodeService libCodeService;
	
	public String execute(String libCode, String sendMsg) {
		Clients client = new Clients();
		LibCode lib = libCodeService.getLibByCode(libCode);
		String retMsg = "";
		try {
			String acsIp = lib.getAcsIp();
			String acsPort = lib.getAcsPort();
			if(!acsIp.equals("") && !acsPort.equals("")) {
				client.startup(acsIp, Integer.parseInt(acsPort));
				retMsg = client.send(sendMsg, "UTF-8");
			}
			return retMsg;
			
		} catch (Exception e) {
			logger.error(e.toString());
			return e.toString();
		} finally {
			try {
				client.close();
			} catch (Exception e) {
			}
		}
	}
	
	/**
	 * 办证新增读者
	 * @param libcode
	 * @param reader
	 * @return
	 */
	public void newReader(LibCode libcode, Reader reader, String payed, String regman) {
		logger.info("办证同步：" + reader.getRdId());
		Clients client = new Clients();
//		String sendMsg = "81YN20080828    15294220080828    152942AO|AA{rdid}|AD{rdpasswd}|AE{rdname}|"
//				+ "AM{rdlib}|BP{rdphone}|BE{rdemail}|BD{rdaddress}|BT|BV{rdfee}|XO{rdcertify}|XT{rdtype}|"
//				+ "XB|XH{rdborndate}|XN{rdnation}|XP{rdnative}|XF{rdremark}|XD|IC{cardId}|XE|XM{rdsex}|"
//				+ "PY{payed}|CN{regman}|XK01|";
		String sendMsg = "81YN20080828    15294220080828    152942AO|AA{rdid}|AD{rdpasswd}|AE{rdname}|"
				+ "AM{rdlib}|BP{rdphone}|BE{rdemail}|BD{rdaddress}|BT|BV{rdfee}|XO{rdcertify}|XT{rdtype}|"
				+ "XB|XH{rdborndate}|XN{rdnation}|XP{rdnative}|XF{rdremark}|XD{rdenddate}|" 
				+"IC{cardId}|XE|XM{rdsex}|PY{payed}|CN{regman}|XK01|";
		
		String realPasswd = reader.getOldRdpasswd();// reader.getRdPasswd();//这里的密码存的是des加密的
		realPasswd = EncryptDecryptData.decryptWithCode(Constants.DES_STATIC_KEY, realPasswd);// 同步的密码是明文的
		
		sendMsg = sendMsg.replaceAll("\\{rdid\\}", reader.getRdId());
		sendMsg = sendMsg.replaceAll("\\{rdpasswd\\}", realPasswd);
		sendMsg = sendMsg.replaceAll("\\{rdname\\}", reader.getRdName());
		sendMsg = sendMsg.replaceAll("\\{rdlib\\}", reader.getRdLib());
		sendMsg = sendMsg.replaceAll("\\{rdphone\\}", reader.getRdLoginId());
		sendMsg = sendMsg.replaceAll("\\{rdemail\\}", reader.getRdEmail() != null ?reader.getRdEmail():"");
		sendMsg = sendMsg.replaceAll("\\{rdaddress\\}", reader.getRdAddress() != null ?reader.getRdAddress():"");
		sendMsg = sendMsg.replaceAll("\\{rdfee\\}", reader.getRdDeposit() != null ?reader.getRdDeposit() + "" :"0");
		if(reader.getRdCertify() !=null){
			sendMsg = sendMsg.replaceAll("\\{rdcertify\\}", reader.getRdCertify());
		}else{
			sendMsg = sendMsg.replaceAll("\\{rdcertify\\}", reader.getRdId());
		}
//		sendMsg = sendMsg.replaceAll("\\{rdcertify\\}", reader.getRdCertify()!= null ?reader.getRdCertify():regman);
		sendMsg = sendMsg.replaceAll("\\{rdtype\\}", reader.getRdType());
		if(reader.getRdBornDate() != null) {
			sendMsg = sendMsg.replaceAll("\\{rdborndate\\}", TimeUtils.dateToString(reader.getRdBornDate(), "yyyy-MM-dd"));
		} else {
			sendMsg = sendMsg.replaceAll("\\{rdborndate\\}", "");
		}
		sendMsg = sendMsg.replaceAll("\\{rdnation\\}", reader.getRdNation() != null ?reader.getRdNation():"");
		sendMsg = sendMsg.replaceAll("\\{rdnative\\}", reader.getRdNative() != null ?reader.getRdNative():"");
		sendMsg = sendMsg.replaceAll("\\{rdremark\\}", reader.getRdRemark() != null ?reader.getRdRemark():"");
		if(reader.getRdSex() ==(byte) 1 ||reader.getRdSex() ==(byte) 2) {
		sendMsg = sendMsg.replaceAll("\\{rdsex\\}", reader.getRdSex() + "");
		}else{
			sendMsg = sendMsg.replaceAll("\\{rdsex\\}", "");
	
		}
		sendMsg = sendMsg.replaceAll("\\{cardId\\}", reader.getCardId() != null ?reader.getCardId():"");
		sendMsg = sendMsg.replaceAll("\\{payed\\}", payed != null ?payed:"");
		sendMsg = sendMsg.replaceAll("\\{regman\\}", regman != null ?regman:"");
		if(reader.getRdEndDate() != null) {
			sendMsg = sendMsg.replaceAll("\\{rdenddate\\}", TimeUtils.dateToString(reader.getRdEndDate(), "yyyy-MM-dd"));
		} else {
			sendMsg = sendMsg.replaceAll("\\{rdenddate\\}", TimeUtils.dateToString(new Date(), "yyyy-MM-dd"));
		}
		
		SyncRecord syncRecord = new SyncRecord();
		syncRecord.setSyncType("30101");
		syncRecord.setSyncCode(sendMsg);
		syncRecord.setSyncLib(libcode.getLibCode());
		syncRecord.setSyncRdid(reader.getRdId());
		syncRecord.setSyncDate(new Date());
		syncRecord.setSyncId(UUIDGenerator.getUUID());
		syncRecord.setSyncOperator(regman);
		try {
			String acsIp = libcode.getAcsIp();
			String acsPort = libcode.getAcsPort();
			if(!acsIp.equals("") && !acsPort.equals("")) {
				client.startup(acsIp, Integer.parseInt(acsPort));
				String retMsg = client.send(sendMsg, "UTF-8");
				Extractor ex = new Extractor(retMsg);
				retMsg = ex.extract("OK");
				if(retMsg.equals("1")) {
					syncRecord.setSyncStatus("1");
				} else {
					//TODO 同步失败的记录日志
					logger.info(sendMsg);
					syncRecord.setSyncStatus("0");
				}
			}
			syncRecordService.save(syncRecord);
		} catch (Exception e) {
			logger.error(e.toString());
			syncRecord.setSyncStatus("-1");
			syncRecordService.save(syncRecord);
		} finally {
			try {
				client.close();
			} catch (Exception e) {
			}
		}
	}
	
	public void uploadRdImage(LibCode libcode, Reader reader, String regman) {
		logger.info("照片同步：" + reader.getRdId());
		Clients client = new Clients();
		String sendMsg = "77|AA{rdid}|LE{length}|DA{DA}";
		sendMsg = sendMsg.replaceAll("\\{rdid\\}", reader.getRdId());
		byte[] data = reader.getRdPhoto();
		String rdphotoData = ParsePic.byte2hex(data);
		sendMsg = sendMsg.replaceAll("\\{rdid\\}", reader.getRdId());
		sendMsg = sendMsg.replaceAll("\\{length\\}", rdphotoData.length() + "");
		sendMsg = sendMsg.replaceAll("\\{DA\\}", rdphotoData);
		SyncRecord syncRecord = new SyncRecord();
		syncRecord.setSyncType("30104");
//		syncRecord.setSyncCode(sendMsg);这个数据太长了，考虑特殊处理
		syncRecord.setSyncLib(libcode.getLibCode());
		syncRecord.setSyncRdid(reader.getRdId());
		syncRecord.setSyncDate(new Date());
		syncRecord.setSyncId(UUIDGenerator.getUUID());
		syncRecord.setSyncOperator(regman);
		try {
			String acsIp = libcode.getAcsIp();
			String acsPort = libcode.getAcsPort();
			if(!acsIp.equals("") && !acsPort.equals("")) {
				client.startup(acsIp, Integer.parseInt(acsPort));
				String retMsg = client.send(sendMsg, "UTF-8");
				Extractor ex = new Extractor(retMsg);
				retMsg = ex.extract("AF");
				if(retMsg.contains("成功")) {
					syncRecord.setSyncStatus("1");
				} else {
					syncRecord.setSyncStatus("0");
				}
			}
			syncRecordService.save(syncRecord);
		} catch (Exception e) {
			logger.error(e.toString());
			syncRecord.setSyncStatus("-1");
			syncRecordService.save(syncRecord);
		} finally {
			try {
				client.close();
			} catch (Exception e) {
			}
		}
	}
	
	/**
	 * 查询读者是否有书或者欠费，有书话返回1，有欠费返回2， 都没有返回0
	 * @param libcode
	 * @param reader
	 * @return
	 */
	public String queryReader(LibCode libcode, Reader reader) {
		Clients client = new Clients();
		String sendMsg = "6300120131111    1659201234567890AO|AA{rdid}|AD|AY1AZF4A6|";
		sendMsg = sendMsg.replaceAll("\\{rdid\\}", reader.getRdId());
		try {
			String acsIp = libcode.getAcsIp();
			String acsPort = libcode.getAcsPort();
			if(!acsIp.equals("") && !acsPort.equals("")) {
				client.startup(acsIp, Integer.parseInt(acsPort));
				String retMsg = client.send(sendMsg, "UTF-8");
				Extractor ex = new Extractor(retMsg);
				String as = ex.extract("AS");//有在借书
				String jf = ex.extract("JF");
				if(as != null && !"".equals(as)) {
					return "1";
				} 
				if(jf != null && !"".equals(jf)) {
					double fee = Double.parseDouble(jf);
					if(fee > 0) {
						return "2";
					}
				} 
				return "0";
			}
			return "0";
		} catch (Exception e) {
			logger.error(e.toString());
			return "";//查询出错咋整？
		} finally {
			try {
				client.close();
			} catch (Exception e) {
			}
		}
	}
	
	/**
	 * 挂失读者证
	 * @param ex
	 * @return
	 * @throws IOException 
	 * @throws UnsupportedEncodingException 
	 */
	public void reportLoss(LibCode libcode, Reader reader, String regman) {
		logger.info("挂失同步：" + reader.getRdId());
		Clients client = new Clients();
		String sendMsg = "83YN20080828    15294220080828    152942AO|AA{rdid}|AD{rdpasswd}|CN{regman}|XK08";
		String realPasswd = reader.getOldRdpasswd();// reader.getRdPasswd();//这里的密码存的是des加密的
		realPasswd = EncryptDecryptData.decryptWithCode(Constants.DES_STATIC_KEY, realPasswd);// 同步的密码是明文的
		
		reader.setRdPasswd(realPasswd);
		sendMsg = sendMsg.replaceAll("\\{rdid\\}", reader.getRdId());
		sendMsg = sendMsg.replaceAll("\\{rdpasswd\\}", reader.getRdPasswd());
		sendMsg = sendMsg.replaceAll("\\{regman\\}", regman);
		SyncRecord syncRecord = new SyncRecord();
		syncRecord.setSyncType("30107");
		syncRecord.setSyncCode(sendMsg);
		syncRecord.setSyncLib(libcode.getLibCode());
		syncRecord.setSyncRdid(reader.getRdId());
		syncRecord.setSyncDate(new Date());
		syncRecord.setSyncId(UUIDGenerator.getUUID());
		syncRecord.setSyncOperator(regman);
		try {
			String acsIp = libcode.getAcsIp();
			String acsPort = libcode.getAcsPort();
			if(!acsIp.equals("") && !acsPort.equals("")) {
				client.startup(acsIp, Integer.parseInt(acsPort));
				String retMsg = client.send(sendMsg, "UTF-8");
				Extractor ex = new Extractor(retMsg);
				retMsg = ex.extract("OK");
				if(retMsg.equals("1")) {
					syncRecord.setSyncStatus("1");
				} else {
					//TODO 同步失败的记录日志
					logger.info(sendMsg);
					syncRecord.setSyncStatus("0");
				}
			}
			syncRecordService.save(syncRecord);
		} catch (Exception e) {
			logger.error(e.toString());
			syncRecord.setSyncStatus("-1");
			syncRecordService.save(syncRecord);
		} finally {
			try {
				client.close();
			} catch (Exception e) {
			}
		}
	}
	/**
	 * 注销读者证操作
	 * @param ex
	 * @return
	 * @throws IOException 
	 * @throws UnsupportedEncodingException 
	 */
	public void cancelReader(LibCode libcode, Reader reader, String regman) {
		logger.info("注销同步：" + reader.getRdId());
		Clients client = new Clients();
		String sendMsg = "83YN20080828    15294220080828    152942AO|AA{rdid}|AD{rdpasswd}|CN{regman}|XK06";
		String realPasswd = reader.getOldRdpasswd();// reader.getRdPasswd();//这里的密码存的是des加密的
		realPasswd = EncryptDecryptData.decryptWithCode(Constants.DES_STATIC_KEY, realPasswd);// 同步的密码是明文的
		sendMsg = sendMsg.replaceAll("\\{rdid\\}", reader.getRdId());
		sendMsg = sendMsg.replaceAll("\\{rdpasswd\\}", realPasswd);
		sendMsg = sendMsg.replaceAll("\\{regman\\}", regman);
		SyncRecord syncRecord = new SyncRecord();
		syncRecord.setSyncType("30111");
		syncRecord.setSyncCode(sendMsg);
		syncRecord.setSyncLib(libcode.getLibCode());
		syncRecord.setSyncRdid(reader.getRdId());
		syncRecord.setSyncDate(new Date());
		syncRecord.setSyncId(UUIDGenerator.getUUID());
		syncRecord.setSyncOperator(regman);
		try {
			String acsIp = libcode.getAcsIp();
			String acsPort = libcode.getAcsPort();
			if(!acsIp.equals("") && !acsPort.equals("")) {
				client.startup(acsIp, Integer.parseInt(acsPort));
				String retMsg = client.send(sendMsg, "UTF-8");
				Extractor ex = new Extractor(retMsg);
				retMsg = ex.extract("OK");
				if(retMsg.equals("1")) {
					syncRecord.setSyncStatus("1");
				} else {
					syncRecord.setSyncStatus("0");
				}
			}
			syncRecordService.save(syncRecord);
		} catch (Exception e) {
			logger.error(e.toString());
			syncRecord.setSyncStatus("-1");
			syncRecordService.save(syncRecord);
		} finally {
			try {
				client.close();
			} catch (Exception e) {
			}
		}
		
	}
	/**
	 * 延期读者证操作
	 * @param ex
	 * @return
	 * @throws IOException 
	 * @throws UnsupportedEncodingException 
	 */
	public void deferReader(LibCode libcode, Reader reader, String regman) {
		logger.info("延期同步：" + reader.getRdId());
		Clients client = new Clients();
		String sendMsg = "81YN20080828    15294220080828    152942AO|AA{rdid}|AD{rdpasswd}|CN{regman}|XK03";
//		82{trx}AO|AA{cardno}|OK{OK}|XK03|AF{screen}|AG{print}
		String realPasswd = reader.getOldRdpasswd();// reader.getRdPasswd();//这里的密码存的是des加密的
		realPasswd = EncryptDecryptData.decryptWithCode(Constants.DES_STATIC_KEY, realPasswd);// 同步的密码是明文的
		sendMsg = sendMsg.replaceAll("\\{rdid\\}", reader.getRdId());
		sendMsg = sendMsg.replaceAll("\\{rdpasswd\\}", realPasswd);
		sendMsg = sendMsg.replaceAll("\\{regman\\}", regman);
		SyncRecord syncRecord = new SyncRecord();
		syncRecord.setSyncType("30106");
		syncRecord.setSyncCode(sendMsg);
		syncRecord.setSyncLib(libcode.getLibCode());
		syncRecord.setSyncRdid(reader.getRdId());
		syncRecord.setSyncDate(new Date());
		syncRecord.setSyncId(UUIDGenerator.getUUID());
		syncRecord.setSyncOperator(regman);
		try {
			String acsIp = libcode.getAcsIp();
			String acsPort = libcode.getAcsPort();
			if(!acsIp.equals("") && !acsPort.equals("")) {
				client.startup(acsIp, Integer.parseInt(acsPort));
				String retMsg = client.send(sendMsg, "UTF-8");
				Extractor ex = new Extractor(retMsg);
				retMsg = ex.extract("OK");
				if(retMsg.equals("1")) {
					syncRecord.setSyncStatus("1");
				} else {
					syncRecord.setSyncStatus("0");
				}
			}
			syncRecordService.save(syncRecord);
		} catch (Exception e) {
			syncRecord.setSyncStatus("-1");
			logger.error(e.toString());
			syncRecordService.save(syncRecord);
		} finally {
			try {
				client.close();
			} catch (Exception e) {
			}
		}
	}
	/**
	 * 验证读者证操作
	 * @param ex
	 * @return
	 * @throws IOException 
	 * @throws UnsupportedEncodingException 
	 */
	public void validateReader(LibCode libcode, Reader reader, String payed, String regman) {
		logger.info("验证同步：" + reader.getRdId());
		Clients client = new Clients();
		String sendMsg = "83YN20080828    15294220080828    152942AO|AA{rdid}|AD{rdpasswd}|CN{regman}|PY{payed}|XK09";
//		840{trx}AO|AA{cardno}|OK{OK}|XK12|AF{screen}|AG{print}
		String realPasswd = reader.getOldRdpasswd();// reader.getRdPasswd();//这里的密码存的是des加密的
		realPasswd = EncryptDecryptData.decryptWithCode(Constants.DES_STATIC_KEY, realPasswd);// 同步的密码是明文的
		sendMsg = sendMsg.replaceAll("\\{rdid\\}", reader.getRdId());
		sendMsg = sendMsg.replaceAll("\\{rdpasswd\\}", realPasswd);
		sendMsg = sendMsg.replaceAll("\\{regman\\}", regman);
		sendMsg = sendMsg.replaceAll("\\{payed\\}", payed);
		SyncRecord syncRecord = new SyncRecord();
		syncRecord.setSyncType("30112");
		syncRecord.setSyncCode(sendMsg);
		syncRecord.setSyncLib(libcode.getLibCode());
		syncRecord.setSyncRdid(reader.getRdId());
		syncRecord.setSyncDate(new Date());
		syncRecord.setSyncId(UUIDGenerator.getUUID());
		syncRecord.setSyncOperator(regman);
		try {
			String acsIp = libcode.getAcsIp();
			String acsPort = libcode.getAcsPort();
			if(!acsIp.equals("") && !acsPort.equals("")) {
				client.startup(acsIp, Integer.parseInt(acsPort));
				String retMsg = client.send(sendMsg, "UTF-8");
				Extractor ex = new Extractor(retMsg);
				retMsg = ex.extract("OK");
				if(retMsg.equals("1")) {
					syncRecord.setSyncStatus("1");
				} else {
					syncRecord.setSyncStatus("0");
				}
			}
			syncRecordService.save(syncRecord);
		} catch (Exception e) {
			logger.error(e.toString());
			syncRecord.setSyncStatus("-1");
			syncRecordService.save(syncRecord);
		} finally {
			try {
				client.close();
			} catch (Exception e) {
			}
		}
	}
	/**
	 * 读者证暂停操作
	 * @param ex
	 * @return
	 * @throws IOException 
	 * @throws UnsupportedEncodingException 
	 */
	public void suspendReader(LibCode libcode, Reader reader, String regman) {
		logger.info("暂停同步：" + reader.getRdId());
		Clients client = new Clients();
		String sendMsg = "83YN20080828    15294220080828    152942AO|AA{rdid}|AD{rdpasswd}|CN{regman}|XK10";
//		84{ok}{trx}AO|AA{cardno}|XK10|OK{OK}|AF{screen}|AG{print}
		String realPasswd = reader.getOldRdpasswd();// reader.getRdPasswd();//这里的密码存的是des加密的
		realPasswd = EncryptDecryptData.decryptWithCode(Constants.DES_STATIC_KEY, realPasswd);// 同步的密码是明文的
		sendMsg = sendMsg.replaceAll("\\{rdid\\}", reader.getRdId());
		sendMsg = sendMsg.replaceAll("\\{rdpasswd\\}", realPasswd);
		sendMsg = sendMsg.replaceAll("\\{regman\\}", regman);
		SyncRecord syncRecord = new SyncRecord();
		syncRecord.setSyncType("30110");
		syncRecord.setSyncCode(sendMsg);
		syncRecord.setSyncLib(libcode.getLibCode());
		syncRecord.setSyncRdid(reader.getRdId());
		syncRecord.setSyncDate(new Date());
		syncRecord.setSyncId(UUIDGenerator.getUUID());
		syncRecord.setSyncOperator(regman);
		try {
			String acsIp = libcode.getAcsIp();
			String acsPort = libcode.getAcsPort();
			if(!acsIp.equals("") && !acsPort.equals("")) {
				client.startup(acsIp, Integer.parseInt(acsPort));
				String retMsg = client.send(sendMsg, "UTF-8");
				Extractor ex = new Extractor(retMsg);
				retMsg = ex.extract("OK");
				if(retMsg.equals("1")) {
					syncRecord.setSyncStatus("1");
				} else {
					syncRecord.setSyncStatus("0");
				}
			}
			syncRecordService.save(syncRecord);
		} catch (Exception e) {
			//TODO 同步失败的记录日志
			logger.error(e.toString());
			syncRecord.setSyncStatus("-1");
			syncRecordService.save(syncRecord);
		} finally {
			try {
				client.close();
			} catch (Exception e) {
			}
		}
	}
	/**
	 * 读者证恢复操作
	 * @param ex
	 * @return
	 * @throws IOException 
	 * @throws UnsupportedEncodingException 
	 */
	public void resumeReader(LibCode libcode, Reader reader, String payed, String regman) {
		logger.info("证恢复同步：" + reader.getRdId());
		Clients client = new Clients();
		String sendMsg = "83YN20080828    15294220080828    152942AO|AA{rdid}|AD{rdpasswd}|PY{payed}|CN{regman}|XK07";
//		84{trx}AO|AA{cardno}|OK{OK}|XK07|AF{screen}|AG{print}
		String realPasswd = reader.getOldRdpasswd();// reader.getRdPasswd();//这里的密码存的是des加密的
		realPasswd = EncryptDecryptData.decryptWithCode(Constants.DES_STATIC_KEY, realPasswd);// 同步的密码是明文的
		sendMsg = sendMsg.replaceAll("\\{rdid\\}", reader.getRdId());
		sendMsg = sendMsg.replaceAll("\\{rdpasswd\\}", realPasswd);
		sendMsg = sendMsg.replaceAll("\\{regman\\}", regman);
		sendMsg = sendMsg.replaceAll("\\{payed\\}", payed);
		SyncRecord syncRecord = new SyncRecord();
		syncRecord.setSyncType("30102");
		syncRecord.setSyncCode(sendMsg);
		syncRecord.setSyncLib(libcode.getLibCode());
		syncRecord.setSyncRdid(reader.getRdId());
		syncRecord.setSyncDate(new Date());
		syncRecord.setSyncId(UUIDGenerator.getUUID());
		syncRecord.setSyncOperator(regman);
		try {
			String acsIp = libcode.getAcsIp();
			String acsPort = libcode.getAcsPort();
			if(!acsIp.equals("") && !acsPort.equals("")) {
				client.startup(acsIp, Integer.parseInt(acsPort));
				String retMsg = client.send(sendMsg, "UTF-8");
				Extractor ex = new Extractor(retMsg);
				retMsg = ex.extract("OK");
				if(retMsg.equals("1")) {
					syncRecord.setSyncStatus("1");
				} else {
					syncRecord.setSyncStatus("0");
				}
			}
			syncRecordService.save(syncRecord);
		} catch (Exception e) {
			logger.error(e.toString());
			syncRecord.setSyncStatus("-1");
			syncRecordService.save(syncRecord);
		} finally {
			try {
				client.close();
			} catch (Exception e) {
			}
		}
	}
	/**
	 * 读者证补办操作
	 * @param ex
	 * @return
	 * @throws IOException 
	 * @throws UnsupportedEncodingException 
	 */
	public void repairReader(LibCode libcode, Reader reader, Reader newReader, String payed, String regman) {
		logger.info("补办同步：" + reader.getRdId());
		Clients client = new Clients();
		String sendMsg = "81YN20080828    15294220080828    152942AO|AA{rdid}|AD{rdpasswd}|AE{rdname}|"
				+ "AM{rdlib}|BP|BE|BD|BT|BV|"
				+ "XO{rdcertify}|XT{rdtype}|XB|XH{rdborndate}|XN|XP|XF{rdremark}|"
				+ "XD|XE{rdstartdate}|XM{rdsex}|XA{rdnewid}|CN{regman}|PY{payed}|XK04";
		String realPasswd = reader.getOldRdpasswd();// reader.getRdPasswd();//这里的密码存的是des加密的
		realPasswd = EncryptDecryptData.decryptWithCode(Constants.DES_STATIC_KEY, realPasswd);// 同步的密码是明文的
		reader.setRdPasswd(realPasswd);
		sendMsg = sendMsg.replaceAll("\\{rdid\\}", reader.getRdId());
		sendMsg = sendMsg.replaceAll("\\{rdpasswd\\}", reader.getRdPasswd());
		sendMsg = sendMsg.replaceAll("\\{rdname\\}", reader.getRdName());
		sendMsg = sendMsg.replaceAll("\\{rdlib\\}", reader.getRdLib());
		sendMsg = sendMsg.replaceAll("\\{rdcertify\\}", reader.getRdCertify());
		sendMsg = sendMsg.replaceAll("\\{rdtype\\}", reader.getRdType());
		sendMsg = sendMsg.replaceAll("\\{rdborndate\\}", TimeUtils.dateToString(reader.getRdBornDate(), "yyyy-MM-dd"));
		sendMsg = sendMsg.replaceAll("\\{rdremark\\}", reader.getRdRemark());
		sendMsg = sendMsg.replaceAll("\\{rdstartdate\\}", TimeUtils.dateToString(reader.getRdStartDate(), "yyyy-MM-dd"));
		sendMsg = sendMsg.replaceAll("\\{rdsex\\}", reader.getRdSex() + "");
		sendMsg = sendMsg.replaceAll("\\{rdnewid\\}", newReader.getRdId());
		sendMsg = sendMsg.replaceAll("\\{regman\\}", regman);
		sendMsg = sendMsg.replaceAll("\\{payed\\}", payed);
		SyncRecord syncRecord = new SyncRecord();
		syncRecord.setSyncType("30310");
		syncRecord.setSyncCode(sendMsg);
		syncRecord.setSyncLib(libcode.getLibCode());
		syncRecord.setSyncRdid(reader.getRdId());
		syncRecord.setSyncDate(new Date());
		syncRecord.setSyncId(UUIDGenerator.getUUID());
		syncRecord.setSyncOperator(regman);
		try {
			String acsIp = libcode.getAcsIp();
			String acsPort = libcode.getAcsPort();
			if(!acsIp.equals("") && !acsPort.equals("")) {
				client.startup(acsIp, Integer.parseInt(acsPort));
				String retMsg = client.send(sendMsg, "UTF-8");
				Extractor ex = new Extractor(retMsg);
				retMsg = ex.extract("AF");
				if(retMsg.contains("成功")) {
					syncRecord.setSyncStatus("1");
				} else {
					syncRecord.setSyncStatus("0");
				}
			}
			syncRecordService.save(syncRecord);
		} catch (Exception e) {
			logger.error(e.toString());
			syncRecord.setSyncStatus("-1");
			syncRecordService.save(syncRecord);
		} finally {
			try {
				client.close();
			} catch (Exception e) {
			}
		}
	}
	/**
	 * 换证操作
	 * @param client
	 * @param reader
	 * @param newRdid
	 * @param userid
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws IOException
	 */
	public void changeReader(LibCode libcode, Reader reader, Reader newReader, String payed, String regman) {
		logger.info("换证同步：" + reader.getRdId());
		Clients client = new Clients();
		String realPasswd = reader.getOldRdpasswd();// reader.getRdPasswd();//这里的密码存的是des加密的
		realPasswd = EncryptDecryptData.decryptWithCode(Constants.DES_STATIC_KEY, realPasswd);// 同步的密码是明文的
		
		reader.setRdPasswd(realPasswd);
		String sendMsg = "81YN20080828    15294220080828    152942AO|AA{rdid}|AD{rdpasswd}|AE{rdname}|AM{rdlib}"
				+ "|BP|BE|BD|BT|BV{rddeposity}|XO{rdcertify}|XT{rdtype}|XB|PY{payed}|XH|XN|XP|XF|XD|XE{rdstartdate}|XM|"
				+ "XA{newrdid}|CN{regman}|XK05";
		sendMsg = sendMsg.replaceAll("\\{rdid\\}", reader.getRdId());
		sendMsg = sendMsg.replaceAll("\\{rdpasswd\\}", reader.getRdPasswd());
		sendMsg = sendMsg.replaceAll("\\{rdname\\}", reader.getRdName());
		sendMsg = sendMsg.replaceAll("\\{rdlib\\}", reader.getRdLib());
		sendMsg = sendMsg.replaceAll("\\{rddeposity\\}", newReader.getRdDeposit() + "");
		sendMsg = sendMsg.replaceAll("\\{rdcertify\\}", newReader.getRdCertify());
		sendMsg = sendMsg.replaceAll("\\{rdtype\\}", reader.getRdType());
		sendMsg = sendMsg.replaceAll("\\{rdstartdate\\}", TimeUtils.dateToString(reader.getRdStartDate(), "yyyy-MM-dd"));
		sendMsg = sendMsg.replaceAll("\\{newrdid\\}", newReader.getRdId());
		sendMsg = sendMsg.replaceAll("\\{regman\\}", regman);
		sendMsg = sendMsg.replaceAll("\\{payed\\}", payed);//增加传递收费信息
		SyncRecord syncRecord = new SyncRecord();
		syncRecord.setSyncType("30309");
		syncRecord.setSyncCode(sendMsg);
		syncRecord.setSyncLib(libcode.getLibCode());
		syncRecord.setSyncRdid(reader.getRdId());
		syncRecord.setSyncDate(new Date());
		syncRecord.setSyncId(UUIDGenerator.getUUID());
		syncRecord.setSyncOperator(regman);
		try {
			String acsIp = libcode.getAcsIp();
			String acsPort = libcode.getAcsPort();
			if(!acsIp.equals("") && !acsPort.equals("")) {
				client.startup(acsIp, Integer.parseInt(acsPort));
				String retMsg = client.send(sendMsg, "UTF-8");
				Extractor ex = new Extractor(retMsg);
				retMsg = ex.extract("AF");
				if(retMsg.contains("成功")) {
					syncRecord.setSyncStatus("1");
				} else {
					syncRecord.setSyncStatus("0");
				}
			}
			syncRecordService.save(syncRecord);
		} catch (Exception e) {
			logger.error(e.toString());
			syncRecord.setSyncStatus("-1");
			syncRecordService.save(syncRecord);
		} finally {
			try {
				client.close();
			} catch (Exception e) {
			}
		}
	}
	/**
	 * 读者证退证操作
	 * @param ex
	 * @return
	 * @throws IOException 
	 * @throws UnsupportedEncodingException 
	 */
	public void quitReader(LibCode libcode, Reader reader, String payed, String regman) {
		logger.info("退证同步：" + reader.getRdId());
		Clients client = new Clients();
		String sendMsg = "83YN20080828    15294220080828    152942AO|AA{rdid}|AD{rdpasswd}|PY{payed}|CN{regman}|XK11";
//		84{trx}AO|AA{cardno}|OK{OK}|XK11|AF{screen}|AG{print}
		String realPasswd = reader.getOldRdpasswd();// reader.getRdPasswd();//这里的密码存的是des加密的
		realPasswd = EncryptDecryptData.decryptWithCode(Constants.DES_STATIC_KEY, realPasswd);// 同步的密码是明文的
		sendMsg = sendMsg.replaceAll("\\{rdid\\}", reader.getRdId());
		sendMsg = sendMsg.replaceAll("\\{rdpasswd\\}", realPasswd);
		sendMsg = sendMsg.replaceAll("\\{regman\\}", regman);
		sendMsg = sendMsg.replaceAll("\\{payed\\}", payed);
		SyncRecord syncRecord = new SyncRecord();
		syncRecord.setSyncType("30109");
		syncRecord.setSyncCode(sendMsg);
		syncRecord.setSyncLib(libcode.getLibCode());
		syncRecord.setSyncRdid(reader.getRdId());
		syncRecord.setSyncDate(new Date());
		syncRecord.setSyncId(UUIDGenerator.getUUID());
		syncRecord.setSyncOperator(regman);
		try {
			String acsIp = libcode.getAcsIp();
			String acsPort = libcode.getAcsPort();
			if(!acsIp.equals("") && !acsPort.equals("")) {
				client.startup(acsIp, Integer.parseInt(acsPort));
				String retMsg = client.send(sendMsg, "UTF-8");
				Extractor ex = new Extractor(retMsg);
				retMsg = ex.extract("AF");
				if(retMsg.contains("成功")) {
					syncRecord.setSyncStatus("1");
				} else {
					syncRecord.setSyncStatus("0");
				}
			}
			syncRecordService.save(syncRecord);
		} catch (Exception e) {
			logger.error(e.toString());
			syncRecord.setSyncStatus("-1");
			syncRecordService.save(syncRecord);
		} finally {
			try {
				client.close();
			} catch (Exception e) {
			}
		}
	}
	
	/**
	 * 删除读者
	 * @param libcode
	 * @param reader
	 * @param regman
	 */
	public void deleteReader(LibCode libcode, Reader reader, String regman) {
		logger.info("删除同步：" + reader.getRdId());
		Clients client = new Clients();
		String sendMsg = "7900120131111    1659201234567890AO|AA{rdid}|AD|CN{regman}|AY1AZF4A7|";
		sendMsg = sendMsg.replaceAll("\\{rdid\\}", reader.getRdId());
		sendMsg = sendMsg.replaceAll("\\{regman\\}", regman);
		SyncRecord syncRecord = new SyncRecord();
		syncRecord.setSyncType("30103");
		syncRecord.setSyncCode(sendMsg);
		syncRecord.setSyncLib(libcode.getLibCode());
		syncRecord.setSyncRdid(reader.getRdId());
		syncRecord.setSyncDate(new Date());
		syncRecord.setSyncId(UUIDGenerator.getUUID());
		syncRecord.setSyncOperator(regman);
		try {
			String acsIp = libcode.getAcsIp();
			String acsPort = libcode.getAcsPort();
			if(!acsIp.equals("") && !acsPort.equals("")) {
				client.startup(acsIp, Integer.parseInt(acsPort));
				String retMsg = client.send(sendMsg, "UTF-8");
				Extractor ex = new Extractor(retMsg);
				retMsg = ex.extract("OK");
				if(retMsg.equals("1")) {
					syncRecord.setSyncStatus("1");
				} else {
					syncRecord.setSyncStatus("0");
				}
			}
			syncRecordService.save(syncRecord);
		} catch (Exception e) {
			logger.error(e.toString());
			syncRecord.setSyncStatus("-1");
			syncRecordService.save(syncRecord);
		} finally {
			try {
				client.close();
			} catch (Exception e) {
			}
		}
	}
	
	public static void main(String[] args) {
		String sendMsg = "81YN20080828    15294220080828    152942AO|AA{rdid}|AD{rdpasswd}|AE{rdname}|AM{rdlib}|BP|BE|BD|BT|BV{rddeposity}|XO{rdcertify}|XT|XB{rdstartdate}|XH|XN|XP|XF|XD|XE|XM|XA{newrdid}|XK05";
		sendMsg = sendMsg.replaceAll("\\{rdid\\}", "123456");
		System.out.println(sendMsg);
	}
}
