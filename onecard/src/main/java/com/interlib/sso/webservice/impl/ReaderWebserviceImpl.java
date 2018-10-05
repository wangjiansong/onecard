package com.interlib.sso.webservice.impl;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;

import javax.jws.WebService;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.interlib.sso.acs.serivce.AcsService;
import com.interlib.sso.common.Common;
import com.interlib.sso.common.Constants;
import com.interlib.sso.common.EncryptDecryptData;
import com.interlib.sso.domain.LibCode;
import com.interlib.sso.domain.NetReader;
import com.interlib.sso.domain.RdAccount;
import com.interlib.sso.domain.Reader;
import com.interlib.sso.domain.ReaderCardInfo;
import com.interlib.sso.domain.ReaderFee;
import com.interlib.sso.domain.ReaderType;
import com.interlib.sso.service.LibCodeService;
import com.interlib.sso.service.NetReaderService;
import com.interlib.sso.service.RdAccountService;
import com.interlib.sso.service.ReaderCardInfoService;
import com.interlib.sso.service.ReaderService;
import com.interlib.sso.service.ReaderTypeService;
import com.interlib.sso.sync.SyncQueue;
import com.interlib.sso.webservice.ReaderWebservice;

@WebService(endpointInterface = "com.interlib.sso.webservice.ReaderWebservice")
public class ReaderWebserviceImpl implements ReaderWebservice {

	@Autowired
	private ReaderService readerService;

	@Autowired
	private RdAccountService rdAccountService;
	
	@Autowired
	private NetReaderService netReaderService;
	
	@Autowired
	private ReaderTypeService readerTypeService;
	@Autowired
	public AcsService acsService;

	@Autowired
	public ExecutorService cacheThreadPool;

	@Autowired
	public ReaderCardInfoService readerCardInfoService;
	
	@Autowired
	public LibCodeService libCodeService;
	
	private static Logger logger = Logger.getLogger(ReaderWebserviceImpl.class);
//	private static SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHH");
//	private static final String HAND_KEY = "TCSOFT_INTERLIB";
//	private static final String DES_STATIC_KEY = "64074f968502295ca41b7db452c7c639";

	@Override
	public List<String> addReader(Reader reader, String staticKey) {
		List<String> result = new ArrayList<String>();
		if (reader != null) {
			reader.setSynStatus(1);//接口调用默认为1，同步成功
			String rdId = reader.getRdId();
//			String rdtype = reader.getRdType();
//			ReaderType readerType = readerTypeService.getReaderType(rdtype);
//			if(readerType == null) {
//				result.add("中心找不到该读者类型：" + reader.getRdType());
//				return result;
//			}
			if (rdId != null && !"".equals(rdId)) {
				String rdPassword = reader.getRdPasswd();
				if (rdPassword != null && !"".equals(rdPassword)) {
					try {
						String encryptPassword = EncryptDecryptData.encrypt(
								Constants.DES_STATIC_KEY, rdPassword);
						reader.setRdPasswd("^" + encryptPassword + "^");
						reader.setOldRdpasswd("^" + encryptPassword + "^");
					} catch (Exception e1) {
						logger.error("【新增读者】密码加密失败！", e1);
					}
					if (staticKey != null && !"".equals(staticKey)) {
//						String targetKey = getMD5(rdId + formatter.format(new Date()) + HAND_KEY);
						String targetKey = getMD5(rdId + Constants.HAND_KEY);
						if (staticKey.equals(targetKey)) {
							try {
								readerService.addReader(reader);
								RdAccount rdAccount = new RdAccount();
								rdAccount.setRdid(rdId);
								rdAccount.setDeposit(reader.getRdDeposit()==null?0:reader.getRdDeposit());
								rdAccount.setPrepay(reader.getRdPrepay()==null?0:reader.getRdPrepay());
								rdAccount.setStatus(1);
								rdAccountService.save(rdAccount);
								result.add("新增读者成功！！");

							} catch (Exception e) {
								result.add("执行数据库失败！");
								logger.error("执行数据库异常！", e);
							}
						} else {
							result.add("静态秘钥匹配失败！");
						}
					} else {
						result.add("静态秘钥为空！");
					}
				} else {
					result.add("读者密码为空！");
				}
			} else {
				result.add("读者证号为空！");
			}
		} else {
			result.add("读者对象为空！");
		}
		return result;
	}
/**
 * 增加读者时候同步到interlib
 */
	@Override
	public List<String> addReaderSync(Reader reader, String staticKey) {
		List<String> result = new ArrayList<String>();
		if (reader != null) {
			reader.setSynStatus(1);//接口调用默认为1，同步成功
			String rdId = reader.getRdId();
			String rdtype = reader.getRdType();
			ReaderType readerType = readerTypeService.getReaderType(rdtype);
			if(readerType == null) {
				result.add("中心找不到该读者类型：" + reader.getRdType());
				return result;
			}
			int valdate = readerType.getValdate(); // 有效期
			reader.setRdStartDate(new Date());//读者证启用日期，默认今天
			reader.setRdEndDate(Common.getDateAfterDaysOtherWay(new Date(), valdate));
			reader.setRdInTime(new Date());//读者证办证日期，默认今天
			
			if (rdId != null && !"".equals(rdId)) {
				String rdPassword = reader.getRdPasswd();
				if (rdPassword != null && !"".equals(rdPassword)) {
					try {
						String encryptPassword = EncryptDecryptData.encrypt(
								Constants.DES_STATIC_KEY, rdPassword);
						reader.setRdPasswd("^" + encryptPassword + "^");
						reader.setOldRdpasswd("^" + encryptPassword + "^");
					} catch (Exception e1) {
						logger.error("【新增读者】密码加密失败！", e1);
					}
					if (staticKey != null && !"".equals(staticKey)) {
//						String targetKey = getMD5(rdId + formatter.format(new Date()) + HAND_KEY);
						String targetKey = getMD5(rdId + Constants.HAND_KEY);
						if (staticKey.equals(targetKey)) {
							try {
								
								
								double deposity = readerType.getDeposity(); // 押金
								double checkfee = readerType.getCheckfee(); // 验证费
								double servicefee = readerType.getServicefee(); // 服务费
								double idfee = readerType.getIdfee(); // 工本费

								
								String payed = checkfee + "]]" + servicefee + "]]" + idfee;
								LibCode lib = libCodeService.getLibByCode(reader.getRdLib());
								System.out.println("传入的时间为："+reader.getRdEndDate());
								
								readerService.addReader(reader);
								RdAccount rdAccount = new RdAccount();
								rdAccount.setRdid(rdId);
								rdAccount.setDeposit(reader.getRdDeposit()==null?0:reader.getRdDeposit());
								rdAccount.setPrepay(reader.getRdPrepay()==null?0:reader.getRdPrepay());
								rdAccount.setStatus(1);
								rdAccountService.save(rdAccount);
								result.add("新增读者成功！！");
								
								payed = deposity + "]]" + payed;
								cacheThreadPool.execute(new SyncQueue(reader, payed, lib, acsService, 
										SyncQueue.SEND_ADD_READER));
								
							} catch (Exception e) {
								result.add("执行数据库失败！");
								logger.error("执行数据库异常！", e);
							}
						} else {
							result.add("静态秘钥匹配失败！");
						}
					} else {
						result.add("静态秘钥为空！");
					}
				} else {
					result.add("读者密码为空！");
				}
			} else {
				result.add("读者证号为空！");
			}
		} else {
			result.add("读者对象为空！");
		}
		return result;
	}
	
	@Override
	public List<String> addNetReader(NetReader netReader, String staticKey) {
		List<String> result = new ArrayList<String>();
		if (netReader != null) {
			String rdId = netReader.getReaderId();
			if (rdId != null && !"".equals(rdId)) {
				String rdPassword = netReader.getReaderPassword();
				if (rdPassword != null && !"".equals(rdPassword)) {
					try {
						String encryptPassword = EncryptDecryptData.encryptWithCode(
								Constants.DES_STATIC_KEY, rdPassword);
						netReader.setReaderPassword(encryptPassword);
					} catch (Exception e1) {
						logger.error("【新增注册读者】密码加密失败！", e1);
					}
					if (staticKey != null && !"".equals(staticKey)) {
						String targetKey = getMD5(rdId + Constants.HAND_KEY);
						if (staticKey.equals(targetKey)) {
							try {
								netReaderService.insertNetReader(netReader);
							} catch (Exception e) {
								result.add("执行数据库失败！");
								logger.error("执行数据库异常！", e);
							}
						} else {
							result.add("静态秘钥匹配失败！");
						}
					} else {
						result.add("静态秘钥为空！");
					}
				} else {
					result.add("注册读者密码为空！");
				}
			} else {
				result.add("注册读者证号为空！");
			}
		} else {
			result.add("注册读者对象为空！");
		}
		return result;
	}
	
	@Override
	public List<String> updateReader(Reader reader) {
		List<String> result = new ArrayList<String>();
		if (reader != null) {
//			if (password != null && !"".equals(password)) {
				String rdId = reader.getRdId();
				if (rdId != null || !"".equals(rdId)) {
					//String realPassword = readerService.getRealPassword(rdId);
//					if (realPassword != null) {
//						String decryptPassword = "";
//						try {
//							decryptPassword = EncryptDecryptData.decrypt(
//									DES_STATIC_KEY,
//									realPassword.substring(1, realPassword.length() - 1));
//						} catch (Exception e1) {
//							logger.error("SSO数据库密码解密失败！");
//						}
//						String targetKey = getMD5(decryptPassword);
//						if (password.equals(targetKey)) {
							String readerPassword = reader.getRdPasswd();
							if (readerPassword == null) {
								reader.setRdPasswd("");
							} else {
								try {
									String encryptPassword = EncryptDecryptData.encrypt(Constants.DES_STATIC_KEY, readerPassword);
									reader.setRdPasswd("^"+encryptPassword+"^");
								} catch (Exception e) {
									result.add("【修改读者】密码加密出错！");
								}
							}
							try {
								//这里还要判断更新的读者是否为挂失，注销，暂停
								RdAccount rdAccount = rdAccountService.get(reader.getRdId());
								if (rdAccount != null) {
									if(reader.getRdCFState() == 3) {//挂失
										rdAccount.setStatus(2);//冻结
									} else if(reader.getRdCFState() == 4) {
										rdAccount.setStatus(2);//冻结
									} else if(reader.getRdCFState() == 5) {
										rdAccount.setStatus(4);//注销
									} else if(reader.getRdCFState() == 1) {//有可能账号是做了恢复操作
										rdAccount.setStatus(1);
									}
									rdAccountService.update(rdAccount);
								}
								readerService.updateReader(reader);
//								readerService.updateDeposit(rdId, reader.getRdDeposit());
							} catch (Exception e) {
								result.add("执行数据库失败！");
								logger.error("执行数据库异常！", e);
							}
//						} else {
//							result.add("密码匹配失败！");
//						}
//					} else {
//						result.add("SSO数据库密码为空！");
//					}
				} else {
					result.add("读者证号为空！");
				}
//			} else {
//				result.add("Interlib数据库密码为空！");
//			}
		} else {
			result.add("读者对象为空！");
		}
		return result;
	}

	@Override
	public Reader getReader(String rdId, String key) {
		Reader reader = null;
		if (rdId != null && !"".equals(rdId)) {
			if (key != null && !"".equals(key)) {
				String targetKey = getMD5(Constants.HAND_KEY);
				if (key.equals(targetKey)) {
					reader = readerService.getReader(rdId, (byte) 0);
				}
			}
		}
		return reader;
	}

	/**
	 * 32位MD5加密
	 * 
	 * @param plainText
	 * @return
	 */
	private static String getMD5(String plainText) {
		StringBuffer buf = new StringBuffer();
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(plainText.getBytes());
			byte b[] = md.digest();
			int i;
			for (int offset = 0; offset < b.length; offset++) {
				i = b[offset];
				if (i < 0)
					i += 256;
				if (i < 16)
					buf.append("0");
				buf.append(Integer.toHexString(i));
			}
		} catch (NoSuchAlgorithmException e) {
			logger.error("算法不存在！", e);
		}
		return buf.toString();
	}

}
