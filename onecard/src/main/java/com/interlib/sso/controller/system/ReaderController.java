package com.interlib.sso.controller.system;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.interlib.opac.webservice.CustomHashMap;
import com.interlib.opac.webservice.CustomHashMapArray;
import com.interlib.opac.webservice.Finance;
import com.interlib.opac.webservice.FinanceWebservice;
import com.interlib.opac.webservice.ReaderWebservice;
import com.interlib.sso.acs.serivce.AcsService;
import com.interlib.sso.common.Cautions;
import com.interlib.sso.common.Common;
import com.interlib.sso.common.Constants;
import com.interlib.sso.common.DateUtils;
import com.interlib.sso.common.EncryptDecryptData;
import com.interlib.sso.common.MD5Util;
import com.interlib.sso.common.PasswordCreator;
import com.interlib.sso.common.PasswordCreator.GeneratedPassword;
import com.interlib.sso.common.ServletUtils;
import com.interlib.sso.common.StringUtils;
import com.interlib.sso.common.TimeUtils;
import com.interlib.sso.common.file.FileConfig;
import com.interlib.sso.common.json.Jackson;
import com.interlib.sso.common.servlet.ServletUtil;
import com.interlib.sso.domain.Authorization;
import com.interlib.sso.domain.CardGroup;
import com.interlib.sso.domain.CirFinLog;
import com.interlib.sso.domain.LibCode;
import com.interlib.sso.domain.LogCir;
import com.interlib.sso.domain.NetReader;
import com.interlib.sso.domain.Num;
import com.interlib.sso.domain.RdAccount;
import com.interlib.sso.domain.Reader;
import com.interlib.sso.domain.ReaderCardInfo;
import com.interlib.sso.domain.ReaderFee;
import com.interlib.sso.domain.ReaderSession;
import com.interlib.sso.domain.ReaderType;
import com.interlib.sso.page.PageEntity;
import com.interlib.sso.service.AuthorizationService;
import com.interlib.sso.service.CirFinLogService;
import com.interlib.sso.service.FinTypeService;
import com.interlib.sso.service.LibCodeService;
import com.interlib.sso.service.LogCirService;
import com.interlib.sso.service.NetReaderService;
import com.interlib.sso.service.NumService;
import com.interlib.sso.service.RdAccountService;
import com.interlib.sso.service.ReaderCardInfoService;
import com.interlib.sso.service.ReaderFeeService;
import com.interlib.sso.service.ReaderService;
import com.interlib.sso.service.ReaderTypeService;
import com.interlib.sso.sync.SyncQueue;
import com.interlib.sso.webservice.DomainTransform;


/** 
 * 读者管理
 * 
 * @author Lullaby
 * 
 */
@Controller
@RequestMapping("admin/reader")
public class ReaderController {

	private String CHECKSQL = "^(.+)\\sand\\s(.+)|(.+)\\sor(.+)\\s$";
	
	private static final String LIST_VIEW = "admin/reader/batch";
	
	@Autowired
	public LibCodeService libcodeService;
	
	@Autowired
	public NumService numService;
	
	@Autowired
	public ReaderService readerService;

	@Autowired
	public NetReaderService netReaderService;
	
	@Autowired
	public ReaderFeeService readerFeeService;

	@Autowired
	public ReaderTypeService readerTypeService;

	@Autowired
	public CirFinLogService cirFinLogService;

	@Autowired
	public RdAccountService rdAccountService;

	@Autowired
	public LogCirService logCirService;

	@Autowired
	public LibCodeService libCodeService;

	@Autowired
	public ReaderCardInfoService readerCardInfoService;
	
	@Autowired
	public FinTypeService finTypeService;
	
	@Autowired
	public AuthorizationService authorizationService;
	
	@Autowired
	public AcsService acsService;

	@Autowired
	public ExecutorService cacheThreadPool;
	
	// @Autowired
	// @Qualifier("client")
	public ReaderWebservice client;

	
	@InitBinder
	public void initBinder(ServletRequestDataBinder binder) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		binder.registerCustomEditor(Date.class, new CustomDateEditor(
				dateFormat, true));
	}

	/* 默认头像在应用中的相对路径 */
	private static final String AVATAR_DEFAULT = "/media/images/defaultReaderPic.jpg";
	/* 头像图片路径相对于应用的路径 */
	private static final String AVATAR_PROJECT_PATH = "READER/AVATAR";
	/* 头像图片大小限制1M(1*1024*1024) */
	// private static final int AVATAR_MAX_SIZE = 1048576;
	/* 头像图片在服务器上的绝对路径 */
	private static String AVATAR_SERVER_PATH = "";

	/* DES加密默认静态秘钥 */
	private static final String DES_STATIC_KEY = "64074f968502295ca41b7db452c7c639";

	private static final Logger logger = Logger
			.getLogger(ReaderController.class);
	

	/****************************************** 读者管理 ******************************************/
	/**
	 * 读者新增页
	 * 
	 * @param model
	 * @return
	 * @throws ParseException
	 */
	@RequiresRoles("admin")
	@RequestMapping("/add")
	public ModelAndView addReaderView(Model model, HttpServletRequest request)
			throws ParseException {
		// 取得当前登录的管理员session信息
		ReaderSession currReader = (ReaderSession) request.getSession()
				.getAttribute("READER_SESSION");
		String rdLibCode = currReader.getReader().getRdLibCode();
		String thisLibCode = "";
		// 取得管理员所属馆
		List<Map<String, String>> libCodes = readerService.getLibCode();
		if (rdLibCode != null) {
			thisLibCode = rdLibCode;
		} else {
			thisLibCode = libCodes.get(0).get("LIBCODE");
		}
		// 取得所属馆下的读者类型用来设置页面默认显示的读者类型
		List<Map<String, String>> readertypes = readerService
				.getLibReaderType(thisLibCode);
		model.addAttribute("readertypes", Jackson.getBaseJsonData(readertypes));
		model.addAttribute("libcodes", Jackson.getBaseJsonData(libCodes));
		model.addAttribute("globaltypes", Jackson.getBaseJsonData(readerService
				.getGlobalReaderType()));
		model.addAttribute("defaultLibCode", thisLibCode);
		// 有效期
		if (!readertypes.isEmpty()) {
			String rdType = readertypes.get(0).get("READERTYPE");
			int effectDays = readerService.getValDate(rdType);
			model.addAttribute("rdEndDate", calculateDate(effectDays));
		} else {
			model.addAttribute("rdEndDate", "");
		}
		// 刷卡消费分组
		List<CardGroup> groups = readerService.getCardGroups();
		if (!groups.isEmpty()) {
			model.addAttribute("groups", Jackson.getBaseJsonData(groups));
		}
		model.addAttribute(Constants.ACTIVE_MENU_KEY, "reader_add");
		return new ModelAndView("admin/reader/add");
	}
	

	/**
	 * 获取对应馆下的读者类型
	 * 
	 * @param thisLibCode
	 * @return
	 * @throws ParseException
	 */
	@RequiresRoles("admin")
	@RequestMapping("/getLibReaderType")
	public void getLibReaderType(HttpServletResponse response,
			String thisLibCode) throws ParseException {
		List<Map<String, String>> readertypes = readerService
				.getLibReaderType(thisLibCode);
		String rdEndDate = "";
		if (!readertypes.isEmpty()) {
			String rdType = readertypes.get(0).get("READERTYPE");
			int effectDays = readerService.getValDate(rdType);
			rdEndDate = calculateDate(effectDays);
			Map<String, String> map = new HashMap<String, String>();
			map.put("rdEndDate", rdEndDate);
			readertypes.add(map);
			ServletUtil.responseOut("utf-8",
					Jackson.getBaseJsonData(readertypes), response);
		}
		ServletUtil.responseOut("utf-8", Jackson.getBaseJsonData(readertypes),
				response);
	}

	/**
	 * 获取对应馆下的馆际读者类型
	 * 
	 * @param response
	 * @param libCode
	 */
	@RequiresRoles("admin")
	@RequestMapping(value = "/getThisGlobalReaderType")
	public void getThisGlobalReaderType(HttpServletResponse response,
			String thisLibCode) {
		ServletUtil.responseOut("utf-8", Jackson.getBaseJsonData(readerService
				.getThisGlobalReaderType(thisLibCode)), response);
	}
	
	/**
	 * 获取所有馆际读者类型
	 * 
	 * @param response
	 * @param libCode
	 */
	@RequiresRoles("admin")
	@RequestMapping(value = "/getAllGlobalReaderType")
	public void getAllGlobalReaderType(HttpServletResponse response) {
		ServletUtil.responseOut("utf-8", Jackson.getBaseJsonData(readerService
				.getGlobalReaderType()), response);
	}

	
	/**
	 * 计算读者类型有效期
	 * 
	 * @param response
	 * @param rdType
	 * @throws ParseException
	 */
	@RequiresRoles("admin")
	@RequestMapping(value = "/calEndDate")
	public void calEndDate(HttpServletResponse response, String rdType)
			throws ParseException {
		int effectDays = readerService.getValDate(rdType);
		ServletUtil.responseOut("utf-8",
				Jackson.getBaseJsonData(calculateDate(effectDays)), response);
	}

	/**
	 * 计算当前日期+有效期限
	 * 
	 * @param effectDays
	 * @return
	 * @throws ParseException
	 */
	private String calculateDate(int effectDays) throws ParseException {
		Date today = TimeUtils.stringToDate(
				TimeUtils.dateToString(new Date(), TimeUtils.YYYYMMDD),
				TimeUtils.YYYYMMDD);
		long allMillis = effectDays * 86400000l + today.getTime();
		Date endDate = new Date(allMillis);
		return TimeUtils.dateToString(endDate, TimeUtils.YYYYMMDD);
	}

	/**
	 * 检查证号是否已经存在
	 * 
	 * @param response
	 * @param rdId
	 */
	@RequiresRoles("admin")
	@RequestMapping(value = "/checkRdIdExist")
	public void checkRdIdExist(HttpServletResponse response, String rdId, String rdCertify) {
		String countRdId = readerService.checkRdIdExist(rdId);
		if(!countRdId.equals("0")) {
			ServletUtil.responseOut("GBK", "{\"result\": \"rdidExist\"}",
					response);
			return;
		} else {
			if(rdCertify != null) {
				int countRdCertify = readerService.checkRdCertifyExist(rdCertify);
				if(countRdCertify > 0) {
					ServletUtil.responseOut("GBK", "{\"result\": \"rdcertifyExist\"}",
							response);
					return;
				} else {
					ServletUtil.responseOut("GBK", "{\"result\": \"0\"}",
							response);
					return;
				}
			}
		}
	}

	/**
	 * 新增读者
	 * 
	 * @param request
	 * @param response
	 * @param reader
	 */
	@RequiresRoles("admin")
	@RequestMapping("/addReader")
	public void addReader(HttpServletRequest request,
			HttpServletResponse response, Reader reader) {
		StringBuffer message = new StringBuffer();
		try {
			ReaderSession readerSession = (ReaderSession) SecurityUtils
					.getSubject().getSession().getAttribute("READER_SESSION");
			// String libcode = readerSession.getReader().getRdLibCode();
			// //操作员所在馆
			LibCode lib = libCodeService.getLibByCode(reader.getRdLib());
			String defaultRdPasswd = lib.getDefaultRdPasswd();
			String rdPasswd = reader.getRdPasswd();
			// 获取默认密码的
			if (rdPasswd == null || "".equals(rdPasswd)) {
				if (defaultRdPasswd != null && !"".equals(defaultRdPasswd)) {
					// rdcertify18:+7-14
					rdPasswd = defaultRdPasswd;
					if (defaultRdPasswd.indexOf(";") > 0) {
						String[] rule = defaultRdPasswd.split(";");
						try {
							String column = rule[0];//字段名
							String subIndex = rule[1];//截取范围
							String reversed = rule[2];//是否反向
							String columnVal = "";
							if(column.equals("rdid")) {
								columnVal = reader.getRdId();
							} else if(column.equals("rdcertify")) {
								columnVal = reader.getRdCertify();
							}
							int start = Integer.parseInt(subIndex.split("-")[0]);
							int end = Integer.parseInt(subIndex.split("-")[1]);
							if (!"".equals(columnVal)) {
								if(reversed.equals("true")) {//如果是要反转截取的
									StringBuffer sb = new StringBuffer(columnVal);
									columnVal = sb.reverse().toString(); 
									columnVal = columnVal.substring(start-1, end);
									sb = new StringBuffer(columnVal);
									rdPasswd = sb.reverse().toString();
								} else {
									rdPasswd = columnVal.substring(start-1, end);
								}
							}
						} catch(Exception e) {
							logger.error("密码截取异常：" + e.toString());
							ServletUtils.printHTML(response,
									"{\"success\":-2,\"message\":\"密码截取异常！\"}");
							return;
						}
					}
				} else {
					ServletUtils.printHTML(response,
							"{\"success\":-2,\"message\":\"密码为空或分馆默认密码为空！\"}");
					return;
				}
			}
			// 对密码des加密
			String desPassword = EncryptDecryptData.encrypt(DES_STATIC_KEY,
					rdPasswd);
			reader.setRdPasswd("^" + desPassword + "^");
			reader.setOldRdpasswd("^" + desPassword + "^");// ADD 第一次添加数据时候
															// 旧密码和密码一致2014-05-13
			// 用于前台确认操作的回调标识
			Object paySignObj = request.getParameter("paySign");
			double paySign = Double.parseDouble(paySignObj == null
					|| paySignObj.equals("") ? "0" : request
					.getParameter("paySign"));

			ReaderType readerType = readerTypeService.getReaderType(reader
					.getRdType());
			double deposity = readerType.getDeposity(); // 押金
			double checkfee = readerType.getCheckfee(); // 验证费
			double servicefee = readerType.getServicefee(); // 服务费
			double idfee = readerType.getIdfee(); // 工本费
			int valdate = readerType.getValdate(); // 有效期

			// 获取当前操作需要收取什么费用
			ReaderFee readerFee = readerFeeService.get(lib.getLibCode());// 根据读者开户馆选择费用
			String optionFee = "";
			optionFee = readerFee.getCardfee();
			if (optionFee.length() > 0) {
				// 表示不收取验证费,重置验证费为0
				if (optionFee.charAt(0) == '0') {
					checkfee = 0;
				}
			}
			if (optionFee.length() > 1) {
				// 表示不收取服务费,重置服务费为0
				if (optionFee.charAt(1) == '0') {
					servicefee = 0;
				}
			}
			if (optionFee.length() > 2) {
				// 表示不收取工本费,重置工本费为0
				if (optionFee.charAt(2) == '0') {
					idfee = 0;
				}
			}
			double totalFee = deposity + checkfee + servicefee + idfee;
			// 因为办证界面同时也可以办理操作员信息，操作员就不要交押金了
			if (reader.getLibUser() == 1) {
				totalFee = 0;// 操作员不需要交押金
			}
			String rdRemark = "";
			byte rdcfstate = 1;// 有效读者证状态
			if (totalFee > paySign) {
				message.append("{");
				message.append("\"success\": 0, ");
				message.append("\"checkfee\":" + checkfee + ", ");
				message.append("\"servicefee\":" + servicefee + ", ");
				message.append("\"idfee\":" + idfee + ", ");
				message.append("\"deposity\":" + deposity + ", ");
				message.append("\"totalfee\":" + totalFee + "");
				message.append("}");
				ServletUtils.printHTML(response, message.toString());
				return;
			}

			reader.setRdCFState(rdcfstate);
//			reader.setRdRemark(rdRemark);//前面是""改变了原来的备注信息
			reader.setRdEndDate(Common.getDateAfterDaysOtherWay(new Date(),
					valdate));
			reader.setSynStatus(0);// 添加同步状态 2014-05-15s
			if(reader.getRdStartDate()==null){//2014-11-10
				reader.setRdStartDate(new Date());
			}
			readerService.addReader(reader);// 新增读者，写入数据库
			// 刷卡消费分组
			int groupId = reader.getGroupId();
			if (groupId != 0) {
				readerService.setReaderGroup(reader.getRdId(), groupId);
			}
			// 账户信息
			RdAccount rdAccount = new RdAccount();
			rdAccount.setRdid(reader.getRdId());
			rdAccount.setDeposit(deposity);
			rdAccount.setArrearage(0.0);
			rdAccount.setOnecard(0.0);
			rdAccount.setPrepay(0.0);
			rdAccount.setSpDeposit(0.0);
			rdAccount.setStatus(1);

			rdAccountService.save(rdAccount);// 新增账户
			// 如果物理卡号不为空，增加绑定物理卡号
			ReaderCardInfo card = null;
			if (reader.getCardId() != null && !"".equals(reader.getCardId())) {
				card = readerCardInfoService.get(reader.getCardId());
				if (card != null) {
					message.append("{");
					message.append("\"success\": -1 ,");
					message.append("\"message\": \""
							+ Cautions.READERCARDID_EXSITS + "\"");
					message.append("}");
					ServletUtils.printHTML(response, message.toString());
					return;
				} else {
					card = new ReaderCardInfo();
					card.setCardId(reader.getCardId());
					card.setRdId(reader.getRdId());
					card.setCardType("0");
					card.setIsUsable(1);
					readerCardInfoService.save(card);
				}
			}

			String ip = Common.getUserIp(request);
			// 记录新增读者和收押金日志
			int paytype = 1;//纸币
			int paySigned = 1;//已交付
			writeDeposityLog(reader, readerSession, "101", "30207", ip,
					deposity, paySigned, paytype);
			String payed = checkfee + "]]" + servicefee + "]]" + idfee;
			logs("30101", reader, payed, readerSession, ip, reader.getRdType(), "");
			// 其他费用日志
			writeLog(reader, readerSession, ip, checkfee, servicefee, idfee, paytype);
			
			// 把操作员也同步到opac 20140322
			String regman = "";
			if (readerSession.getReader() != null) {
				regman = readerSession.getReader().getRdId();// 对应操作的读者账号
				reader.setRegman(regman);
			}

			if (reader.getLibUser() == 0) {
				
				if(card != null) {
					reader.setCardId(card.getCardId());
				}
				payed = deposity + "]]" + payed;
				cacheThreadPool.execute(new SyncQueue(reader, payed, lib, acsService, 
						SyncQueue.SEND_ADD_READER));
				
//				String syncFlag = "none";
//				if(card != null) { 
//					syncFlag = "update";
//					reader.setCardId(card.getCardId());
//				} else {
//					syncFlag = "delete";
//				}
//				syncOpacReader(reader, card, syncFlag);
//				logger.info("同步读者成功。。。。。");
				
				
				/*Integer indate = valdate / 30;
				reader.setRdDeposit(deposity);// 读者押金
				String encryptPassword = reader.getRdPasswd();
				// 由于opac密码是明文，这里再给它解密成明文再同步
				if (encryptPassword != null && !"".equals(encryptPassword)) {
					String decryptPassword = EncryptDecryptData.decryptWithCode(DES_STATIC_KEY, encryptPassword);
					reader.setRdPasswd(decryptPassword);
				}
				String webserviceUrl = lib.getWebserviceUrl();
				String webserviceKey = MD5Util.MD5Encode(lib.getOpacKey());
				if (webserviceUrl != null && !"".equals(webserviceUrl)) {
					webserviceUrl = webserviceUrl
							+ (webserviceUrl.endsWith("/") ? "" : "/")
							+ "webservice/readerWebservice";
					
					com.interlib.opac.webservice.Reader webserviceReader = DomainTransform.transToWebserviceReader(reader);// 转成webservice的reader对象
					try {
						JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();
						factory.setServiceClass(ReaderWebservice.class);
						factory.setAddress(webserviceUrl);
						ReaderWebservice client = (ReaderWebservice) factory.create();
						List<String> retList = client.addReader(webserviceReader, indate);
						String ret = "";
						for(String r : retList) {
							ret = r;
						}
						if(!ret.equals("OK")) {
							message.append("{");
							message.append("\"success\": -1 ,");
							message.append("\"message\": \""
									+ Cautions.SAVESUCCESS_BUTSYNCFAIL + "\", ");
							String exception = ret;
							message.append("\"exception\": \"" + exception + "\"");
							message.append("}");
							ServletUtils.printHTML(response, message.toString());
							return;
						}
						if(card != null) {
							//增加卡号同步
							logger.debug("同步卡号。。。");
							client.updateReaderCard(reader.getRdId(), Integer.parseInt(card.getCardType()), card.getCardId(), webserviceKey);
						}
						readerService.updateSynStatus(reader.getRdId(), 1);
					} catch (Exception e) {
						e.printStackTrace();
						message.append("{");
						message.append("\"success\": -1 ,");
						message.append("\"message\": \""
								+ Cautions.SAVESUCCESS_BUTSYNCFAIL + "\", ");
						String exception = e.toString();
						if (exception.contains("!")) {
							exception = exception.substring(0,
									exception.indexOf("!"));
						}
						message.append("\"exception\": \"" + exception + "\"");
						message.append("}");
						ServletUtils.printHTML(response, message.toString());
						return;
					}
				}*/
			}
			message.append("{");
			message.append("\"success\": 1 ");
			message.append("}");
		
			ServletUtils.printHTML(response, message.toString());
			return;
			
		} catch (Exception e) {
			logger.error("新增读者异常！", e);
			message.append("{");
			message.append("\"success\": -1 ");
			message.append("}");
			
			ServletUtils.printHTML(response, message.toString());
			return;
			
		}
	}



	/****************************************** 读者查询 ******************************************/

	/**
	 * 读者查询界面
	 * 
	 * @return
	 */
	@RequiresRoles("admin")
	@RequestMapping(value = "/index")
	public ModelAndView queryReaderList(Model model, Reader reader) {
		/*CharSequence targerStr = null;
		Pattern.matches(CHECKSQL,targerStr);*/
		
		reader.setLibUser(0);// 只查普通读者，操作员libuser为1
		model.addAttribute("list", readerService.queryReaderList(reader));
		model.addAttribute("readertypes",
				Jackson.getBaseJsonData(readerService.getReaderType()));
		model.addAttribute("libcodes",
				Jackson.getBaseJsonData(readerService.getLibCode()));
		model.addAttribute("obj", reader);
		model.addAttribute(Constants.ACTIVE_MENU_KEY, "reader_index");
		return new ModelAndView("admin/reader/index");
	}
	/**
	 * 读者批量生成   add BY 2017、10、25
 	 * @param model
	 * @param reader
	 * @return
	 * @throws ParseException 
	 */
	@RequiresRoles("admin")
	@RequestMapping(value = "/batch")
	public ModelAndView batchReaderList(Model model, NetReader reader,
			HttpServletRequest request,HttpServletResponse response) throws ParseException {
		// 取得当前登录的管理员session信息
		ReaderSession currReader = (ReaderSession) request.getSession()
				.getAttribute("READER_SESSION");
		String rdLibCode = currReader.getReader().getRdLibCode();
		String thisLibCode = "";
		// 取得管理员所属馆
		List<Map<String, String>> libCodes = readerService.getLibCode();
		if (rdLibCode != null) {
			thisLibCode = rdLibCode;
		} else {
			thisLibCode = libCodes.get(0).get("LIBCODE");
		}
		// 取得所属馆下的读者类型用来设置页面默认显示的读者类型
		List<Map<String, String>> readertypes = readerService
				.getLibReaderType(thisLibCode);
		model.addAttribute("readertypes", Jackson.getBaseJsonData(readertypes));
		model.addAttribute("libcodes", Jackson.getBaseJsonData(libCodes));
		model.addAttribute("globaltypes", Jackson.getBaseJsonData(readerService
				.getGlobalReaderType()));
		model.addAttribute("defaultLibCode", thisLibCode);
		// 有效期
		if (!readertypes.isEmpty()) {
			String rdType = readertypes.get(0).get("READERTYPE");
			int effectDays = readerService.getValDate(rdType);
			model.addAttribute("rdEndDate", calculateDate(effectDays));
		} else {
			model.addAttribute("rdEndDate", "");
		}
		// 刷卡消费分组
		List<CardGroup> groups = readerService.getCardGroups();
		if (!groups.isEmpty()) {
			model.addAttribute("groups", Jackson.getBaseJsonData(groups));
		}		
		model.addAttribute(Constants.ACTIVE_MENU_KEY, "reader_add");
		List<NetReader> pageList = netReaderService.queryBatchNetReaderList(reader);		
		model.addAttribute("pageList", pageList);
		if(pageList.size() > 0) {
			
			if(reader.getEndTime() == null || "".equals(reader.getEndTime())) {
//				String endTime = pageList.get(pageList.size()-1).getDay();
//				System.out.println("--endTime:--"+endTime);

				SimpleDateFormat sdf=new SimpleDateFormat("yyyy/MM/dd");  
				Date date=new Date();  
				String str=sdf.format(date);
				System.out.println("--date:--"+str);

				reader.setEndTime(str);
			}
			if(reader.getStartTime() == null || "".equals(reader.getStartTime())) {
				String startTime = pageList.get(pageList.size()-1).getDay();
				reader.setStartTime(startTime);
				System.out.println("--startTime:--"+startTime);

			}
		}
		model.addAttribute("obj", reader);
		int batchCount = netReaderService.getBatchNetReader();
		model.addAttribute("batchCount", batchCount);

		int count = readerService.checkReaderActivate();
		model.addAttribute("count", count);
		return new ModelAndView(LIST_VIEW);
	}
	  
	/**
	 * 批量添加读者列表显示
	 * @throws IOException 
	 * @throws ParseException 
	 */
	@RequiresRoles("admin")
	@RequestMapping("/batchList")
	public String list(Model model, NetReader reader,int i,
			HttpServletRequest request,HttpServletResponse response) throws IOException, ParseException {
		
//		int j1 =Integer.parseInt(request.getParameter("j"));
		//new 一个Num 
		Num num = new Num();
		
		// 取得当前登录的管理员session信息
		ReaderSession currReader = (ReaderSession) request.getSession()
				.getAttribute("READER_SESSION");
		String rdLibCode = currReader.getReader().getRdLibCode();
		String thisLibCode = "";
		// 取得管理员所属馆
		List<Map<String, String>> libCodes = readerService.getLibCode();
		if (rdLibCode != null) {
			thisLibCode = rdLibCode;
		} else {
			thisLibCode = libCodes.get(0).get("LIBCODE");
		}
		// 取得所属馆下的读者类型用来设置页面默认显示的读者类型
		List<Map<String, String>> readertypes = readerService
				.getLibReaderType(thisLibCode);
		model.addAttribute("readertypes", Jackson.getBaseJsonData(readertypes));
		model.addAttribute("libcodes", Jackson.getBaseJsonData(libCodes));
		model.addAttribute("globaltypes", Jackson.getBaseJsonData(readerService
				.getGlobalReaderType()));
		model.addAttribute("defaultLibCode", thisLibCode);
		// 有效期
		if (!readertypes.isEmpty()) {
			String rdType = readertypes.get(0).get("READERTYPE");
			int effectDays = readerService.getValDate(rdType);
			model.addAttribute("rdEndDate", calculateDate(effectDays));
		} else {
			model.addAttribute("rdEndDate", "");
		}
		// 刷卡消费分组
		List<CardGroup> groups = readerService.getCardGroups();
		if (!groups.isEmpty()) {
			model.addAttribute("groups", Jackson.getBaseJsonData(groups));
		}
		
		
		//批量生成的读者导入到net_reader 表 只有证号、 类型、馆代码、生成日期
		//判断读者类型的值是否相等。。 如果相等要先添加Num表中num的值 如果不相等则跳出操作
		// ID num 表示该证号最后一位所生成的 。。 
		//  Modify By 2017/10/31  ADD wjs 
		int i1 =Integer.parseInt(request.getParameter("i"));
		String rdLib =  request.getParameter("rdLib");
		String rdType =  request.getParameter("rdType");		
		String passwordCreator =  request.getParameter("passwordCreator");		
		System.out.println("passwordCreator：：：：："+passwordCreator);
	if(!rdLib.equals("999") && !rdType.equals("999_GJ")){
		for(int j = 0; j < i1; j++){
			//小写转大写 toUpperCase  大写转小写toLowerCase 
			System.out.println("rdType.substring(0,1):"+rdType.substring(0,1).toUpperCase());
			System.out.println("rdType.substring(8):"+rdType.substring(8));
			String nuM =  numService.getNum(rdType.substring(8)).getNum();//存入的数量
			
			int nUm = numService.updateNum(rdType.substring(8));
			System.out.println("--nuM为："+nuM+"--");
			
			System.out.println("--nUm为："+nUm+"--");
			 DecimalFormat df=new DecimalFormat("0000000");
		     String str2=df.format(Integer.parseInt(nuM));
		     System.out.println("1243546767iu--------"+str2);
				String r = rdType.substring(0,1).toUpperCase()+rdType.substring(8)+str2;
			if(netReaderService.checkReaderIdIsExist(r) !=0){
				System.out.println("我进到这里来了。。。。。。。");
	    		r = rdType.substring(0,1).toUpperCase()+rdType.substring(8)+str2;
	        	System.out.println("here's r :"+r);
	        	reader.setReaderId(r);
	        	reader.setReaderName(r);
				reader.setReaderCardState((byte) 2);
				reader.setReaderType(rdType);
				reader.setReaderLib(rdLib);
				if(passwordCreator.equals("1")){
					try {
						String desPassword = EncryptDecryptData.encrypt(DES_STATIC_KEY,
									"123456");
						reader.setReaderPassword("^" + desPassword + "^");
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}else{
					try {
						
						GeneratedPassword pass = PasswordCreator.generate(6);
						String desPassword = EncryptDecryptData.encrypt(DES_STATIC_KEY,
								pass.getClearText());
						reader.setReaderPassword("^" + desPassword + "^");
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}				
				}
				
				//获取时间加十年
			    Date date = new Date();
			    Calendar cal = Calendar.getInstance();
			    cal.setTime(date);//设置起时间
			    //System.out.println("111111111::::"+cal.getTime());
			    cal.add(Calendar.YEAR, 10);//增加十年 
			    System.out.println("输出::"+cal.getTime());
			    SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String sDate=sdf.format(cal.getTime());
				
			    System.out.println("输出::"+sDate);
				
				reader.setReaderEndDate(cal.getTime());
				reader.setReaderStartDate(new Date());
				reader.setReaderHandleDate(new Date());
				reader.setReaderBornDate(new Date());
				netReaderService.insertNetReader(reader);
				System.out.println("读者证号为"+reader.getReaderId());
			}else{
				reader.setReaderId(r);
	        	reader.setReaderName(r);
				reader.setReaderCardState((byte) 2);
				reader.setReaderType(rdType);
				reader.setReaderLib(rdLib);
				 
				if(passwordCreator.equals("1")){
					try {
						String desPassword = EncryptDecryptData.encrypt(DES_STATIC_KEY,
									"123456");
						reader.setReaderPassword("^" + desPassword + "^");
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}else{
					try {
						
						GeneratedPassword pass = PasswordCreator.generate(6);
						String desPassword = EncryptDecryptData.encrypt(DES_STATIC_KEY,
								pass.getClearText());
						reader.setReaderPassword("^" + desPassword + "^");
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}				
				}

				//获取时间加十年
			    Date date = new Date();
			    Calendar cal = Calendar.getInstance();
			    cal.setTime(date);//设置起时间
			    //System.out.println("111111111::::"+cal.getTime());
			    cal.add(Calendar.YEAR, 10);//增加十年 
			    System.out.println("输出::"+cal.getTime());
			    SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String sDate=sdf.format(cal.getTime());
				
			    System.out.println("输出::"+sDate);
				
				reader.setReaderEndDate(cal.getTime());
				reader.setReaderStartDate(new Date());
				reader.setReaderHandleDate(new Date());
				reader.setReaderBornDate(new Date());

				netReaderService.insertNetReader(reader);
			}
		  }
	}else{
		ServletUtils.printHTML(response,
				"{\"success\":-2,\"message\":\"开户馆选择错误或读者类型选择错误！\"}");
	}
	//批量生成OVER  下面是model。。
		model.addAttribute(Constants.ACTIVE_MENU_KEY, "reader_add");
		List<NetReader> pageList = netReaderService.queryBatchNetReaderList(reader);
		model.addAttribute("pageList", pageList);
		model.addAttribute("obj", reader);

		int count = readerService.checkReaderActivate();
		model.addAttribute("count", count);
		//有效读者数
		int batchCount = netReaderService.getBatchNetReader();
		model.addAttribute("batchCount", batchCount);
		return LIST_VIEW;
	}
	/**
	 * 导出批量读者
	 * @param request
	 * @param response
	 * @param reader
	 */
	@RequiresRoles("admin")
	@RequestMapping(value = "/exportBatchListExcel")
	public void exportBatchListExcel(Model model, HttpServletRequest request,
			HttpServletResponse response, NetReader reader,
			String rdtype,String readerHandleStartDate,String readerHandleEndDate){
		

		System.out.println("-------"+readerHandleStartDate+"-------"+readerHandleEndDate);
		
		//统计的总数  
		int count=netReaderService.getBatchNetReader();
		if(count==0)count=1;

		if(reader!=null){
			PageEntity page= new PageEntity();//前台没有传这个，肯定null啦
			reader.setPage(page);
			if(count<65535){//工作表最大的行数
				reader.getPage().setShowCount(count);
			}else{
				reader.getPage().setShowCount(65535);
			}
		}
		
		List<NetReader> readerlist = netReaderService.queryBatchNetReaderListByReaderType(rdtype,readerHandleStartDate, readerHandleEndDate);
//		if(readerlist.size()> 0) {
//			
//			if(reader.getReaderHandleEndDate() == null || "".equals(reader.getReaderHandleEndDate())) {
//				SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");  
//				Date ReaderHandleEndDate =new Date();  
//				String str=sdf.format(ReaderHandleEndDate);
//				System.out.println("--ReaderHandleEndDate:--"+str);
//
//				reader.setReaderHandleEndDate(str);
//			}
//			if(reader.getReaderHandleStartDate() == null || "".equals(reader.getReaderHandleStartDate())) {
//				String startTime = readerlist.get(0).getDay();
//				reader.setReaderHandleStartDate(startTime);
//			}
//		}
		


		String inFilePath = request.getSession().getServletContext().getRealPath("/jsp_tiles/excel/inFile/batchList.xls");
		String filename = "读者批量生成_"+System.currentTimeMillis()+".xls";
		
		OutputStream outputStream = null;
		InputStream inputStream = null;
		try {
			response.reset();  
			response.setContentType("application/vnd.ms-excel; charset=GBK");
			response.addHeader("Content-Disposition", "attachment;filename=" + new String(filename.getBytes("GBK"), "ISO8859-1"));
			outputStream = response.getOutputStream();
			inputStream = new FileInputStream(inFilePath);
			
			HSSFWorkbook workbook = new HSSFWorkbook(inputStream);
			HSSFSheet worksheet = workbook.getSheetAt(0);
			
				int size = readerlist.size();
				
				for(int i=0; i<size; i++){
					NetReader reader1 = readerlist.get(i);
					HSSFRow row = worksheet.createRow(i+1);
					row.createCell(0).setCellValue(reader1.getReaderId());
					try {
						String readerpassword = reader1.getReaderPassword();
						String depassword =	EncryptDecryptData.decryptWithCode(
												DES_STATIC_KEY,readerpassword);
						row.createCell(1).setCellValue(depassword);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					row.createCell(2).setCellValue(reader1.getReaderType());
					row.createCell(3).setCellValue(reader1.getReaderLib());
					//设置时间格式
					SimpleDateFormat format1=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
					row.createCell(4).setCellValue(format1.format(reader1.getReaderHandleDate()));
				}
			
			workbook.write(outputStream);
		} catch (FileNotFoundException e) {
			logger.error("【批量读者办证】导出读者批量列表时未读取到模板文件！", e);
		} catch (IOException e) {
			logger.error("【批量读者办证】导出读者批量列表时出现IO流异常！", e);
		} catch (IllegalStateException e) {
			logger.error("【批量读者办证】导出读者批量列表时出现异常！", e);
		}finally {
			try {
				outputStream.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
//				e.printStackTrace();
				logger.error("【批量读者办证】导出读者批量列表关闭输出流时异常！", e);

			}
		}
	}
	
	/**
	 * 删除读者
	 * 
	 * @param response
	 * @param rdId
	 */
	@RequiresRoles("admin")
	@RequestMapping(value = "/deleteReader")
	public void deleteReader(HttpServletRequest request, HttpServletResponse response, String rdId) {
		ReaderSession readerSession = (ReaderSession) SecurityUtils
				.getSubject().getSession().getAttribute("READER_SESSION");
		Reader reader = readerService
				.getReader(rdId, (byte) 2);
		LibCode lib = libCodeService.getLibByCode(reader.getRdLibCode());
		StringBuffer message = new StringBuffer();
		if (rdId.equals("admin")) {
			message.append("{");
			message.append("\"success\": 0,");
			message.append("\"message\": \"" + Cautions.CANNOT_DELETE_ADMIN
					+ "\"");
			message.append("}");

			
			ServletUtils.printHTML(response, message.toString());
			return;
			
		}
		//针对福建虚拟注册读者没有读者卡和证号的增加判断为空
		ReaderCardInfo rc = readerCardInfoService.get(rdId);
		if(rc!=null || "".equals(rc)){
			readerCardInfoService.deleteByRdId(rdId);
		}
		RdAccount ra =rdAccountService.get(rdId);
		if(ra != null || "".equals(ra)){
			rdAccountService.delete(rdId);
		}
		readerService.deleteReader(rdId);
//		Reader reader = new Reader();
//		reader.setRdId(rdId);
		String ip = Common.getUserIp(request);
		//删除读者日志
		logs("30103", reader, "", readerSession, ip, "", "");
		message.append("{");
		message.append("\"success\": 1");
		message.append("}");
		
		cacheThreadPool.execute(new SyncQueue(reader, "", lib, acsService,
				SyncQueue.SEND_DELETE_READER));
		ServletUtils.printHTML(response, message.toString());
		return;
		
	}

	/**
	 * 读者明细页
	 * 
	 * @param rdId
	 * @param model
	 * @return
	 */
	@RequiresRoles("admin")
	@RequestMapping(value = "/detailReader/{rdId}")
	public ModelAndView detailReader(Model model, @PathVariable String rdId) {
		Reader reader = readerService.getReader(rdId, (byte) 1);
		CardGroup group = readerService.getReaderGroupBelong(rdId);
		if (group == null) {
			model.addAttribute("groupBelong", "无");
		} else {
			model.addAttribute("groupBelong", group.getGroupName());
		}
		model.addAttribute("reader", reader);
		model.addAttribute(Constants.ACTIVE_MENU_KEY, "reader_index");
		return new ModelAndView("admin/reader/detailReader");
	}

	/**
	 * 重置密码页
	 * 
	 * @param rdId
	 * @param model
	 * @return
	 */
	@RequiresRoles("admin")
	@RequestMapping(value = "/resetPasswd/{rdId}")
	public ModelAndView resetPasswd(@PathVariable String rdId, Model model) {
		model.addAttribute("rdId", rdId);
		model.addAttribute(Constants.ACTIVE_MENU_KEY, "reader_index");
		return new ModelAndView("admin/reader/resetPasswd");
	}
	
	/**
	 * 同步读者信息到其他系统，这里是opac
	 * 
	 * @param request
	 * @param response
	 * @param rdId
	 */
	@SuppressWarnings("rawtypes")
	@RequiresRoles("admin")
	@RequestMapping("/syncReader/{rdId}")
	public void syncReaderInfo(HttpServletRequest request,
			HttpServletResponse response, @PathVariable String rdId) {
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
		
		ServletUtils.printHTML(response, message.toString());
		return;
		
	}

	/**
	 * 管理员自己修改密码
	 * 
	 * @param rdId
	 * @param model
	 * @return
	 */
	@RequiresRoles("admin")
	@RequestMapping(value = "/adminResetPasswd/{rdId}")
	public ModelAndView adminResetPasswd(@PathVariable String rdId, Model model) {
		model.addAttribute("rdId", rdId);
		model.addAttribute(Constants.ACTIVE_MENU_KEY, "reader_index");
		return new ModelAndView("admin/reader/adminResetPasswd");
	}

	/**
	 * 重置密码
	 * 
	 * @param request
	 * @param response
	 * @throws InvalidKeySpecException
	 * @throws NoSuchPaddingException
	 * @throws NoSuchAlgorithmException
	 * @throws BadPaddingException
	 * @throws IllegalBlockSizeException
	 * @throws InvalidKeyException
	 */
	@RequiresRoles("admin")
	@RequestMapping("/resetPassword")
	public void resetPassword(HttpServletRequest request,
			HttpServletResponse response) throws InvalidKeyException,
			IllegalBlockSizeException, BadPaddingException,
			NoSuchAlgorithmException, NoSuchPaddingException,
			InvalidKeySpecException {
		String rdId = request.getParameter("rdId");
		String oldPassword = "";
		oldPassword = request.getParameter("oldPassword");// 管理员修改自己本事的密码需要输入原密码，如果是修改读者的密码不需要原密码
															// 2014-06-03
		if (oldPassword == null || "".equals(oldPassword)) {// 页面没有传递原密码
			// 去掉页面输入原密码，从数据库获取
			String oldPasswordDB = readerService.getReader(rdId, (byte) 2)
					.getRdPasswd();// request.getParameter("oldPassword");
			oldPassword = EncryptDecryptData.decrypt(DES_STATIC_KEY,
					oldPasswordDB.substring(1, oldPasswordDB.length() - 1));// 去掉
																			// ^^
																	// ^^
		}
		String newPassword = request.getParameter("newPassword");
		String realPassword = readerService.getRealPassword(rdId);
		int result;
		Map synmap = null;
		if (realPassword == null || "".equals(realPassword)) {
			try {
				String encryptPassword = EncryptDecryptData.encrypt(
						DES_STATIC_KEY, newPassword);
				result = readerService.updatePassword(rdId, "^"
						+ encryptPassword + "^");

				if (result == 1) {// 只有修改成功才进行同步操作//ADD 添加同步到opac端
									// 同步成功同时修改reader旧密码和标识
//					synmap = UpdateOpacReaderPasswd(rdId, oldPassword,
//							newPassword);
					// 调用同步接口
					cacheThreadPool.execute(new SyncQueue(rdId, oldPassword,
							newPassword, libCodeService, readerService, 
							SyncQueue.SEND_UPDATE_PASSWORD));
				}

			} catch (Exception e) {
				result = -1;
				logger.error("【修改密码】新密码加密出错！");
			}
		} else {
			try {
				String decryptPassword = EncryptDecryptData.decrypt(
						DES_STATIC_KEY,
						realPassword.substring(1, realPassword.length() - 1));
				if (oldPassword.equals(decryptPassword)) {
					String encryptPassword = EncryptDecryptData.encrypt(
							DES_STATIC_KEY, newPassword);
					result = readerService.updatePassword(rdId, "^"
							+ encryptPassword + "^");
					if (result == 1) {// 只有修改成功才进行同步操作 ADD 添加同步到opac端
										// 同步成功同时修改reader旧密码和标识
						cacheThreadPool.execute(new SyncQueue(rdId, oldPassword,
								newPassword, libCodeService, readerService, 
								SyncQueue.SEND_UPDATE_PASSWORD));
					}
				} else {
					result = -1;
				}
			} catch (Exception e) {
				if (oldPassword.equals(realPassword)) {
					try {
						String encryptPassword = EncryptDecryptData.encrypt(
								DES_STATIC_KEY, newPassword);
						result = readerService.updatePassword(rdId, "^"
								+ encryptPassword + "^");
						if (result == 1) {// 只有修改成功才进行同步操作//ADD 添加同步到opac端
											// 同步成功同时修改reader旧密码和标识
							cacheThreadPool.execute(new SyncQueue(rdId, oldPassword,
									newPassword, libCodeService, readerService, 
									SyncQueue.SEND_UPDATE_PASSWORD));
						}
					} catch (Exception e1) {
						result = -1;
						logger.error("【修改密码】新密码加密出错！！！");
					}
				} else {
					result = -1;
				}
				logger.error("【修改密码】原密码解密出错！");
			}
		}
		// ADD 2014-05-15 添加同步之后的判断处理
//		if (synmap != null) {
//			int rsyn = (Integer) synmap.get("success");
//			if (result == 1 && rsyn == 1) {
//				result = 1;// 修改成功，同步成功
//			} else if (result == 1 && rsyn != 1) {
//				result = 0;// 修改成功，同步失败
//			}
//		}
		ServletUtil.responseOut("GBK", "1", response);
	}

	/**
	 * 读者修改页
	 * 
	 * @param rdId
	 * @return
	 */
	@RequiresRoles("admin")
	@RequestMapping("/edit/{rdId}")
	public ModelAndView editPage(@PathVariable String rdId, Model model) {
		model.addAttribute("readertypes",
				Jackson.getBaseJsonData(readerService.getReaderType()));
		model.addAttribute("globaltypes",
				Jackson.getBaseJsonData(readerService.getGlobalReaderType()));
		model.addAttribute("libcodes",
				Jackson.getBaseJsonData(readerService.getLibCode()));
		Reader reader = readerService.getReader(rdId, (byte) 2);
		RdAccount rdaccount = rdAccountService.get(rdId);
		reader.setRdDeposit(rdaccount.getDeposit());
		model.addAttribute(reader);
		model.addAttribute(Constants.ACTIVE_MENU_KEY, "reader_index");
		return new ModelAndView("admin/reader/edit");
	}

	@RequiresRoles("admin")
	@RequestMapping("/setReaderLibUser")
	public void setReaderLibUser(HttpServletResponse response,
			HttpServletRequest request, Reader reader) {
		int ret = readerService.updateLibUser(reader.getRdId(),
				reader.getLibUser());
		StringBuffer sb = new StringBuffer();
		sb.append("{");
		sb.append("\"success\":" + (ret > 0 ? "1" : "0"));
		sb.append("}");
		ServletUtils.printHTML(response, sb.toString());
	}

	/**
	 * 修改读者信息
	 * 
	 * @param response
	 * @param model
	 */
	@SuppressWarnings("rawtypes")
	@RequiresRoles("admin")
	@RequestMapping("/editReader")
	public void editReader(HttpServletResponse response,
			HttpServletRequest request, Reader reader) {
		ReaderSession readerSession = (ReaderSession) request.getSession()
				.getAttribute("READER_SESSION");

		String ip = Common.getUserIp(request);

		Reader oldReader = readerService.getReader(reader.getRdId(), (byte) 2);
		String oldPassword = oldReader.getRdPasswd();
		// 密码为空或未加密
		if (oldPassword == null || "".equals(oldPassword)
				|| !oldPassword.startsWith("^")) {
			try {
				String encryptPassword = EncryptDecryptData.encrypt(
						DES_STATIC_KEY, oldPassword);
				reader.setRdPasswd("^" + encryptPassword + "^");
				
			} catch (Exception e) {
				reader.setRdPasswd(oldPassword);
				logger.error("【修改读者】未经过加密的密码解密时出错！");
			}
		} else {
			reader.setRdPasswd(oldPassword);
		}
		String oldRdpasswd = oldReader.getOldRdpasswd();// 保存在数据的上一次的旧密码
														// 2014-05-14
		if (oldRdpasswd != null) {
			reader.setOldRdpasswd(oldRdpasswd);
		}

		String paySign = request.getParameter("paySign");// 用这个来判断前台是否做了确认操作

		StringBuffer message = new StringBuffer();
		// 判断读者类型是否有变更
		String data4 = "";
		// 读者类型有变化的话需要判断押金
		if (!oldReader.getRdType().equals(reader.getRdType())) {
			data4 = oldReader.getRdType() + " to " + reader.getRdType();
			RdAccount rdAccount = rdAccountService.get(oldReader.getRdId());// 获取账户信息
			double oldRdDeposite = rdAccount.getDeposit();
			double newDeposite = readerTypeService.getReaderType(
					reader.getRdType()).getDeposity();
			if (newDeposite != oldRdDeposite) {// 要处理押金
				if (!"1".equals(paySign)) {
					message.append("{");
					message.append("\"success\": 0,");
					message.append("\"newRdtype\":\"" + reader.getRdType()
							+ "\",");
					message.append("\"oldRdtype\":\"" + oldReader.getRdType()
							+ "\",");
					message.append("\"rdDeposity\":" + oldRdDeposite + ", ");
					message.append("\"deposity\":" + newDeposite);
					message.append("}");
					
					ServletUtils.printHTML(response, message.toString());
					return;
					
				} else { // 已经处理好押金
					reader.setRdDeposit(newDeposite);
					rdAccount.setDeposit(newDeposite);

					reader.setSynStatus(0);// 添加同步状态 2014-05-15
					readerService.updateReader(reader);// 本地读者数据更新
					rdAccountService.update(rdAccount);// 账户更新

					// 写日志，修改读者30102，减少押金30210或补交押金30209==
					// 财经日志 109补交，110减少
					String feeType = "110";
					String logType = "30210";
					int paytype = 1;
					if (newDeposite > oldRdDeposite) {
						int paySigned = 1;//已交付
						feeType = "109";
						logType = "30209";
						writeDeposityLog(reader, readerSession, feeType,
								logType, ip, newDeposite - oldRdDeposite, paySigned, paytype);
					} else {
						// writeDeposityLog(reader, readerSession, feeType,
						// logType, ip, oldRdDeposite-newDeposite);
						// 退还给读者的押金的费用日志记成负数
						int paySigned = 3;//已退还
						writeDeposityLog(reader, readerSession, feeType,
								logType, ip, newDeposite - oldRdDeposite, paySigned, paytype);
					}
				}
			} else {
				reader.setSynStatus(0);// 添加同步状态 2014-05-15
				readerService.updateReader(reader);// 本地读者数据更新
			}
		} else {
			reader.setSynStatus(0);// 添加同步状态 2014-05-15
			readerService.updateReader(reader);// 本地读者数据更新
		}
		int groupId = reader.getGroupId();
		if (groupId == 0) {
			readerService.deleteReaderBelongGroup(reader.getRdId());
		} else {
			readerService.deleteReaderBelongGroup(reader.getRdId());
			readerService.setReaderGroup(reader.getRdId(), groupId);
		}

		String cardSyncFlag = "none";//默认卡不更新
		// 如果物理卡号不为空，增加修改绑定物理卡号,当前只能一个读者证对应一个ic卡号，后面要做成一个对应多个
		ReaderCardInfo cardInfo = readerCardInfoService.getByRdId(reader.getRdId());
		// 如果读者卡信息存在，且有传递卡号
		if (cardInfo != null && !"".equals(reader.getCardId())) {
			//则更新卡号
			cardSyncFlag = "update";
			cardInfo.setCardId(reader.getCardId());
			readerCardInfoService.update(cardInfo);
		} else if(cardInfo == null && !"".equals(reader.getCardId())) {//如果卡信息不存在且传递了卡号
			//如果按读者证号查不到卡信息，再用卡号去查对应的，看看是否这个卡号已经有其他读者再使用
			cardInfo = readerCardInfoService.get(reader.getCardId());
			if(cardInfo != null) {
				//这个卡号已经属于其他读者，不能继续了
				message.append("{");
				message.append("\"success\": -1 ,");
				message.append("\"message\": \"" + Cautions.READERCARDID_EXSITS
						+ "\"");
				message.append("}");
				
				ServletUtils.printHTML(response, message.toString());
				return;
				
			}
			// 增加新卡
			cardInfo = new ReaderCardInfo();
			cardInfo.setCardId(reader.getCardId());
			cardInfo.setRdId(reader.getRdId());
			cardInfo.setCardType("0");
			cardInfo.setIsUsable(1);
			readerCardInfoService.save(cardInfo);
			cardSyncFlag = "add";
		} else if(cardInfo != null && (reader.getCardId() == null || "".equals(reader.getCardId()))) {
			readerCardInfoService.delete(cardInfo.getCardId());// 删除旧卡
			cardSyncFlag = "delete";
		}

		logs("30102", reader, "", readerSession, ip, "", data4);// 操作修改读者日志
		reader.setRdLibCode(reader.getRdLib());
//		Map map = syncOpacReader(reader, cardInfo, cardSyncFlag); // 同步
		
		cacheThreadPool.execute(new SyncQueue(reader, cardInfo, cardSyncFlag, 
				libCodeService, readerService, SyncQueue.SEND_SYNC_READER));
		
		int success = 1;
		String msg = "";
		String exception = "";
//		if (map != null) {
//			success = (Integer) map.get("success");
//			msg = (String) map.get("message");
//			exception = (String) map.get("exception");
//		}

		if (success == -1) {
			message.append("{");
			message.append("\"success\": -1 ,");
			message.append("\"message\": \"" + Cautions.SAVESUCCESS_BUTSYNCFAIL
					+ "\",");
			message.append("\"exception\": \"" + exception + "\"");
			message.append("}");
		} else {
			message.append("{");
			message.append("\"success\": 1 "); // 保存成功且同步成功
			message.append("}");
		}
		
		ServletUtils.printHTML(response, message.toString());
		return;
		
	}

	/**
	 * 读者恢复, 费用检查，当前状态检查
	 * 
	 * @param response
	 * @param reader
	 */
	@SuppressWarnings("rawtypes")
	@RequiresRoles("admin")
	@RequestMapping("/renew")
	public void renew(HttpServletResponse response, HttpServletRequest request,
			Reader reader) {
		ReaderSession readerSession = (ReaderSession) request.getSession()
				.getAttribute("READER_SESSION");
		// String libcode = readerSession.getReader().getRdLib(); //操作员所在馆
		reader = readerService.getReader(reader.getRdId(), (byte) 2);
		reader.setRdLib(reader.getRdLibCode()); // 上面查询读者把libcode设置成中文了，一点都不通用啊给后面调用的人，这里再设为代码

		LibCode lib = libCodeService.getLibByCode(reader.getRdLibCode());

		StringBuffer message = new StringBuffer();
		String ip = Common.getUserIp(request);
		if (reader.getRdCFState() == 1) {// 有效不需要恢复
			message.append("{");
			message.append("\"success\": 2");
			message.append("}");
			
			ServletUtils.printHTML(response, message.toString());
			return;
			
		}
		// 前台确认付费的标志
		double payed = Double
				.parseDouble(request.getParameter("payed") == null ? "0"
						: request.getParameter("payed"));

		ReaderType readerType = readerTypeService.getReaderType(reader
				.getRdType());
		RdAccount rdAccount = rdAccountService.get(reader.getRdId());
		if (rdAccount == null) {
			rdAccount = new RdAccount();
			rdAccount.setRdid(reader.getRdId());
			rdAccount.setDeposit(0.0);
			rdAccount.setOnecard(0.0);
			rdAccount.setPrepay(0.0);
			rdAccount.setStatus(1);
			rdAccountService.save(rdAccount);
		}
		double rdDeposity = rdAccount.getDeposit(); // 读者财经表里的押金

		double deposity = readerType.getDeposity();
		double checkfee = readerType.getCheckfee();
		double servicefee = readerType.getServicefee();
		double idfee = readerType.getIdfee();

		// 当前证操作需要收取什么费用
		ReaderFee readerFee = readerFeeService.get(reader.getRdLibCode());
		String optionFee = "";
		optionFee = readerFee.getRenewfee();
		if (optionFee.length() > 0) {
			// 表示不收取验证费,重置验证费为0
			if (optionFee.charAt(0) == '0') {
				checkfee = 0;
			}
		}
		if (optionFee.length() > 1) {
			// 表示不收取服务费,重置服务费为0
			if (optionFee.charAt(1) == '0') {
				servicefee = 0;
			}
		}
		if (optionFee.length() > 2) {
			// 表示不收取工本费,重置工本费为0
			if (optionFee.charAt(2) == '0') {
				idfee = 0;
			}
		}

		double totalFee = deposity + checkfee + servicefee + idfee - rdDeposity;

		if (totalFee != payed) {
			message.append("{");
			message.append("\"success\": 0, ");
			message.append("\"checkfee\":" + checkfee + ", ");
			message.append("\"servicefee\":" + servicefee + ", ");
			message.append("\"idfee\":" + idfee + ", ");
			message.append("\"deposity\":" + deposity + ", ");
			message.append("\"rdDeposity\":" + rdDeposity + ", ");
			message.append("\"totalfee\":" + totalFee + "");
			message.append("}");
			ServletUtils.printHTML(response, message.toString());
			return;
			
		} else {

			rdAccount.setDeposit(deposity);
			reader.setRdDeposit(deposity);
			rdAccount.setStatus(1);
			rdAccountService.update(rdAccount);// 更新读者账户

			//卡处理
			ReaderCardInfo cardInfo = readerCardInfoService.getByRdId(reader.getRdId());
			if(cardInfo != null) {
				cardInfo.setIsUsable(1);//卡恢复
				readerCardInfoService.update(cardInfo);
			}
			
			reader.setRdCFState((byte) 1);
			reader.setRdRemark("该证已恢复！");
			reader.setSynStatus(0);// 添加同步状态 2014-05-15
			readerService.updateReader(reader);// 更新读者状态
			// 记录日志
			int paytype = 1;//纸币
			writeLog(reader, readerSession, ip, checkfee, servicefee, idfee, paytype);

			// log("30102","1",rdtype+"to"+rdtype) + " ]] ";
			String payedStr = checkfee + "]]" + servicefee + "]]" + idfee + "]]" + deposity;
			logs("30102", reader, payedStr, readerSession, ip, "1", "读者证恢复");

			if (rdDeposity <= 0 && deposity > 0) {// 读者财经表里没有押金，也就是现在有交了，更新财经押金，写日志
				rdAccount.setDeposit(deposity);
				rdAccountService.update(rdAccount);
				// 押金操作日志
				int paySigned = 1;
				writeDeposityLog(reader, readerSession, "109", "30209", ip,
						deposity, paySigned, paytype);
			}
			// 同步
//			Map map = updateOpacReader(reader, lib);
			cacheThreadPool.execute(new SyncQueue(reader, payedStr, lib, acsService,
					SyncQueue.SEND_RESUME_READER));
			
			int success = 1;
			String msg = "";
			String exception = "";
//			if (map != null) {
//				success = (Integer) map.get("success");
//				msg = (String) map.get("message");
//				exception = (String) map.get("exception");
//			}
			if (success == -2) {
				message.append("{");
				message.append("\"success\": -2 ,");
				message.append("\"message\": \""
						+ Cautions.SAVESUCCESS_BUTSYNCFAIL + "\",");
				message.append("\"exception\": \"" + exception + "\"");
				message.append("}");
			} else {
				message.append("{");
				message.append("\"success\": 1, ");
				message.append("\"prepay\": " + rdAccount.getPrepay() + ",");
				message.append("\"deposity\": " + rdAccount.getDeposit());
				message.append("}");
			}

			ServletUtils.printHTML(response, message.toString());
			return;
			
		}

	}

	/**
	 * 注销读者证
	 */
	@SuppressWarnings("rawtypes")
	@RequiresRoles("admin")
	@RequestMapping("/logout")
	public void logout(HttpServletResponse response,
			HttpServletRequest request, Reader reader) {
		ReaderSession readerSession = (ReaderSession) request.getSession()
				.getAttribute("READER_SESSION");
		// String libcode = readerSession.getReader().getRdLib();
		reader = readerService.getReader(reader.getRdId(), (byte) 2);
		reader.setRdLib(reader.getRdLibCode());

		LibCode lib = libCodeService.getLibByCode(reader.getRdLibCode());

		StringBuffer message = new StringBuffer();
		if (reader.getRdCFState() == 5) {// 已经是注销状态
			message.append("{");
			message.append("\"success\": 2"); // 不需要注销
			message.append("}");
			ServletUtils.printHTML(response, message.toString());
			return;
		}
		String hasBookOrFee = acsService.queryReader(lib, reader);//有没有欠费，未还书
		
		if (hasBookOrFee.equals("1")) {// 表示有在借书未还
			message.append("{");
			message.append("\"success\": -3 ,");
			message.append("\"message\": \"" + Cautions.LOANEDBOOKS + "\"");
			message.append("}");
			ServletUtils.printHTML(response, message.toString());
			return;
		}
		if (hasBookOrFee.equals("2")) {
			message.append("{");
			message.append("\"success\": -3 ,");
			message.append("\"message\": \"" + Cautions.FEE_PAY + "\"");
			message.append("}");
			ServletUtils.printHTML(response, message.toString());
			return;
		}

		RdAccount rdAccount = rdAccountService.get(reader.getRdId());
		if (rdAccount == null) {
			rdAccount = new RdAccount();
			rdAccount.setRdid(reader.getRdId());
			rdAccount.setDeposit(0.0);
			rdAccount.setOnecard(0.0);
			rdAccount.setPrepay(0.0);
			rdAccount.setStatus(1);
			rdAccountService.save(rdAccount);
		}
		// 前台确认收取了相关费用的标识
		double payed = Double.parseDouble(request.getParameter("payed"));
		ReaderType readerType = readerTypeService.getReaderType(reader
				.getRdType());
		double checkfee = readerType.getCheckfee();
		double servicefee = readerType.getServicefee();
		double idfee = readerType.getIdfee();

		// 当前操作需要收取得费用
		ReaderFee readerFee = readerFeeService.get(reader.getRdLibCode());
		String optionFee = "";
		optionFee = readerFee.getLogoutfee();
		if (optionFee.length() > 0) {
			// 表示不收取验证费,重置验证费为0
			if (optionFee.charAt(0) == '0') {
				checkfee = 0;
			}
		}
		if (optionFee.length() > 1) {
			// 表示不收取服务费,重置服务费为0
			if (optionFee.charAt(1) == '0') {
				servicefee = 0;
			}
		}
		if (optionFee.length() > 2) {
			// 表示不收取工本费,重置工本费为0
			if (optionFee.charAt(2) == '0') {
				idfee = 0;
			}
		}
		double totalFee = checkfee + servicefee + idfee;
		if (totalFee != payed) {
			message.append("{");
			message.append("\"success\": 0, ");
			message.append("\"checkfee\":" + checkfee + ", ");
			message.append("\"servicefee\":" + servicefee + ", ");
			message.append("\"idfee\":" + idfee + ", ");
			message.append("\"totalfee\":" + totalFee + "");
			message.append("}");
			ServletUtils.printHTML(response, message.toString());
			return;
		} else {
			// 1. 查询用户余额
			// 2. 扣费
			// TODO 这里应该增加个选择，是从卡里扣费，还是收取现金，而且如果卡状态是冻结的，那就只能收取现金了
//			double prepay = rdAccount.getPrepay();
//			if (totalFee > prepay) {
//				message.append("{");
//				message.append("\"success\": -1" + ", "); // 余额不足以扣费
//				message.append("\"prepay\": " + prepay + ", ");
//				message.append("\"totalfee\":" + totalFee + "");
//				message.append("}");
//				ServletUtils.printHTML(response, message.toString());
//				return;
//			}
//			rdAccount.setPrepay(prepay - totalFee);
			rdAccount.setStatus(4);
			rdAccountService.update(rdAccount);// 更新读者账户

			reader.setRdCFState((byte) 5);
			reader.setRdRemark("该证已注销！");
			reader.setSynStatus(0);// 添加同步状态 2014-05-15
			readerService.updateReader(reader);// 更新读者状态
			//卡处理
			ReaderCardInfo cardInfo = readerCardInfoService.getByRdId(reader.getRdId());
			if(cardInfo != null) {
				cardInfo.setIsUsable(0);//卡失效
				readerCardInfoService.update(cardInfo);
			}
			int paytype = 1;
			writeLog(reader, readerSession, Common.getUserIp(request),
					checkfee, servicefee, idfee, paytype);
			// log("30102","5",rdtype+"to"+rdtype);
			String payedStr = checkfee + "]]" + servicefee + "]]" + idfee;
			logs("30111", reader, payedStr, readerSession, Common.getUserIp(request),
					"5", reader.getRdType() + " to " + reader.getRdType());

			//同步
			cacheThreadPool.execute(new SyncQueue(reader, payedStr, lib, acsService,
					SyncQueue.SEND_CANCEL_READER));
			int success = 1;
			String msg = "";
			String exception = "";
//			if (map != null) {
//				success = (Integer) map.get("success");
//				msg = (String) map.get("message");
//				exception = (String) map.get("exception");
//			}
			if (success == -2) {
				message.append("{");
				message.append("\"success\": -2 ,");
				message.append("\"message\": \""
						+ Cautions.SAVESUCCESS_BUTSYNCFAIL + "\",");
				message.append("\"exception\": \"" + exception + "\"");
				message.append("}");
			} else {
				message.append("{");
				message.append("\"success\": 1,");
				message.append("\"prepay\": " + rdAccount.getPrepay() + ",");
				message.append("\"deposity\": " + rdAccount.getDeposit());
				message.append("}");
			}

			ServletUtils.printHTML(response, message.toString());
			return;
		}

	}

	/**
	 * 验证读者证
	 * 
	 * @param response
	 * @param request
	 * @param reader
	 */
	@SuppressWarnings("rawtypes")
	@RequiresRoles("admin")
	@RequestMapping("/check")
	public void check(HttpServletResponse response, HttpServletRequest request,
			Reader reader) {
		ReaderSession readerSession = (ReaderSession) request.getSession()
				.getAttribute("READER_SESSION");
		// String libcode = readerSession.getReader().getRdLib(); //操作员所在馆
		reader = readerService.getReader(reader.getRdId(), (byte) 2);
		reader.setRdLib(reader.getRdLibCode());

		LibCode lib = libCodeService.getLibByCode(reader.getRdLibCode());

		double payed = Double.parseDouble(request.getParameter("payed"));
		ReaderType readerType = readerTypeService.getReaderType(reader
				.getRdType());
		RdAccount rdAccount = rdAccountService.get(reader.getRdId());
		if (rdAccount == null) {
			rdAccount = new RdAccount();
			rdAccount.setRdid(reader.getRdId());
			rdAccount.setDeposit(0.0);
			rdAccount.setOnecard(0.0);
			rdAccount.setPrepay(0.0);
			rdAccount.setStatus(1);
			rdAccountService.save(rdAccount);
		}
		double checkfee = readerType.getCheckfee();
		double servicefee = readerType.getServicefee();
		double idfee = readerType.getIdfee();
		int valdate = readerType.getValdate();

		ReaderFee readerFee = readerFeeService.get(reader.getRdLibCode());
		String optionFee = "";
		optionFee = readerFee.getCheckfee();
		if (optionFee.length() > 0) {
			// 表示不收取验证费,重置验证费为0
			if (optionFee.charAt(0) == '0') {
				checkfee = 0;
			}
		}
		if (optionFee.length() > 1) {
			// 表示不收取服务费,重置服务费为0
			if (optionFee.charAt(1) == '0') {
				servicefee = 0;
			}
		}
		if (optionFee.length() > 2) {
			// 表示不收取工本费,重置工本费为0
			if (optionFee.charAt(2) == '0') {
				idfee = 0;
			}
		}

		double totalFee = checkfee + servicefee + idfee;
		StringBuffer message = new StringBuffer();
		if (totalFee != payed) {
			message.append("{");
			message.append("\"success\": 0, ");
			message.append("\"checkfee\":" + checkfee + ", ");
			message.append("\"servicefee\":" + servicefee + ", ");
			message.append("\"idfee\":" + idfee + ", ");
			message.append("\"totalfee\":" + totalFee + "");
			message.append("}");
			ServletUtils.printHTML(response, message.toString());
			return;
		} else {
			// 1. 查询用户余额
			// 2. 扣费
			// TODO 这里应该增加个选择，是从卡里扣费，还是收取现金，而且如果卡状态是冻结的，那就只能收取现金了
			/*double prepay = rdAccount.getPrepay();
			if (totalFee > prepay) {
				message.append("{");
				message.append("\"success\": -1,"); // 余额不足以扣费
				message.append("\"prepay\":" + prepay + ", ");
				message.append("\"totalfee\":" + totalFee + "");
				message.append("}");
				ServletUtils.printHTML(response, message.toString());
				return;
			}
			rdAccount.setPrepay(prepay - totalFee);
			rdAccountService.update(rdAccount);// 更新读者账户
*/			byte oriCfState = reader.getRdCFState();
			reader.setRdCFState((byte) 2);
			reader.setRdEndDate(Common.getDateAfterDaysOtherWay(
					reader.getRdEndDate(), valdate));
			reader.setSynStatus(0);// 添加同步状态 2014-05-15
			readerService.updateReader(reader);
			// 财经日志
			int paytype = 1;
			writeLog(reader, readerSession, Common.getUserIp(request),
					checkfee, servicefee, idfee, paytype);
			// 操作日志
			String payedStr = checkfee + "]]" + servicefee + "]]" + idfee;
			logs("30112", reader, payedStr, readerSession, Common.getUserIp(request),
					oriCfState + "", "已验证");

//			Map map = updateOpacReader(reader, lib);
			//同步
			cacheThreadPool.execute(new SyncQueue(reader, payedStr, lib, acsService,
					SyncQueue.SEND_VALIDATE_READER));
			int success = 1;
			String msg = "";
			String exception = "";
//			if (map != null) {
//				success = (Integer) map.get("success");
//				msg = (String) map.get("message");
//				exception = (String) map.get("exception");
//			}
			if (success == -2) {
				message.append("{");
				message.append("\"success\": -2 ,");
				message.append("\"message\": \""
						+ Cautions.SAVESUCCESS_BUTSYNCFAIL + "\",");
				message.append("\"exception\": \"" + exception + "\"");
				message.append("}");
			} else {
				message.append("{");
				message.append("\"success\": 1,");
				message.append("\"rdenddate\": \"" + reader.getRdEndDate()
						+ "\",");
				message.append("\"prepay\": " + rdAccount.getPrepay());
				message.append("}");
			}
			ServletUtils.printHTML(response, message.toString());
			return;
		}
	}

	/**
	 * 信用有效读者证
	 * 
	 * @param response
	 * @param request
	 * @param reader
	 */
	@SuppressWarnings("rawtypes")
	@RequiresRoles("admin")
	@RequestMapping("/credit")
	public void credit(HttpServletResponse response, HttpServletRequest request,
			Reader reader) {
		ReaderSession readerSession = (ReaderSession) request.getSession()
				.getAttribute("READER_SESSION");
		// String libcode = readerSession.getReader().getRdLib(); //操作员所在馆
		reader = readerService.getReader(reader.getRdId(), (byte) 2);
		reader.setRdLib(reader.getRdLibCode());

		LibCode lib = libCodeService.getLibByCode(reader.getRdLibCode());

		double payed = Double.parseDouble(request.getParameter("payed"));
		ReaderType readerType = readerTypeService.getReaderType(reader
				.getRdType());
		RdAccount rdAccount = rdAccountService.get(reader.getRdId());
		if (rdAccount == null) {
			rdAccount = new RdAccount();
			rdAccount.setRdid(reader.getRdId());
			rdAccount.setDeposit(0.0);
			rdAccount.setOnecard(0.0);
			rdAccount.setPrepay(0.0);
			rdAccount.setStatus(1);
			rdAccountService.save(rdAccount);
		}
		double checkfee = readerType.getCheckfee();
		double servicefee = readerType.getServicefee();
		double idfee = readerType.getIdfee();
		int valdate = readerType.getValdate();

		ReaderFee readerFee = readerFeeService.get(reader.getRdLibCode());
		String optionFee = "";
		optionFee = readerFee.getCheckfee();
		if (optionFee.length() > 0) {
			// 表示不收取验证费,重置验证费为0
			if (optionFee.charAt(0) == '0') {
				checkfee = 0;
			}
		}
		if (optionFee.length() > 1) {
			// 表示不收取服务费,重置服务费为0
			if (optionFee.charAt(1) == '0') {
				servicefee = 0;
			}
		}
		if (optionFee.length() > 2) {
			// 表示不收取工本费,重置工本费为0
			if (optionFee.charAt(2) == '0') {
				idfee = 0;
			}
		}

		double totalFee = checkfee + servicefee + idfee;
		StringBuffer message = new StringBuffer();
		if (totalFee != payed) {
			message.append("{");
			message.append("\"success\": 0, ");
			message.append("\"checkfee\":" + checkfee + ", ");
			message.append("\"servicefee\":" + servicefee + ", ");
			message.append("\"idfee\":" + idfee + ", ");
			message.append("\"totalfee\":" + totalFee + "");
			message.append("}");
			ServletUtils.printHTML(response, message.toString());
			return;
		} else {
			// 1. 查询用户余额
			// 2. 扣费
			// TODO 这里应该增加个选择，是从卡里扣费，还是收取现金，而且如果卡状态是冻结的，那就只能收取现金了
			/*double prepay = rdAccount.getPrepay();
			if (totalFee > prepay) {
				message.append("{");
				message.append("\"success\": -1,"); // 余额不足以扣费
				message.append("\"prepay\":" + prepay + ", ");
				message.append("\"totalfee\":" + totalFee + "");
				message.append("}");
				ServletUtils.printHTML(response, message.toString());
				return;
			}
			rdAccount.setPrepay(prepay - totalFee);
			rdAccountService.update(rdAccount);// 更新读者账户
*/			byte oriCfState = reader.getRdCFState();
			reader.setRdCFState((byte) 10);
			reader.setRdEndDate(Common.getDateAfterDaysOtherWay(
					reader.getRdEndDate(), valdate));
			reader.setSynStatus(0);// 添加同步状态 2014-05-15
			readerService.updateReader(reader);
			// 财经日志
			int paytype = 1;
			writeLog(reader, readerSession, Common.getUserIp(request),
					checkfee, servicefee, idfee, paytype);
			// 操作日志
			String payedStr = checkfee + "]]" + servicefee + "]]" + idfee;
			logs("30112", reader, payedStr, readerSession, Common.getUserIp(request),
					oriCfState + "", "已改为信用有效");

//			Map map = updateOpacReader(reader, lib);
			//同步
			cacheThreadPool.execute(new SyncQueue(reader, payedStr, lib, acsService,
					SyncQueue.SEND_VALIDATE_READER));
			int success = 1;
			String msg = "";
			String exception = "";
//			if (map != null) {
//				success = (Integer) map.get("success");
//				msg = (String) map.get("message");
//				exception = (String) map.get("exception");
//			}
			if (success == -2) {
				message.append("{");
				message.append("\"success\": -2 ,");
				message.append("\"message\": \""
						+ Cautions.SAVESUCCESS_BUTSYNCFAIL + "\",");
				message.append("\"exception\": \"" + exception + "\"");
				message.append("}");
			} else {
				message.append("{");
				message.append("\"success\": 1,");
				message.append("\"rdenddate\": \"" + reader.getRdEndDate()
						+ "\",");
				message.append("\"prepay\": " + rdAccount.getPrepay());
				message.append("}");
			}
			ServletUtils.printHTML(response, message.toString());
			return;
		}
	}
	
	/**
	 * 退证操作 如果读者办证交有押金，预付款，系统提示退还读者押金，预付款，处理后读者证状态为“注销”。还需要调用opac接口判断是否有借书没还或欠款
	 * 
	 * @param request
	 * @param response
	 * @param reader
	 */
	@SuppressWarnings("rawtypes")
	@RequiresRoles("admin")
	@RequestMapping("/quit")
	public void quit(HttpServletRequest request, HttpServletResponse response,
			Reader reader) {
		ReaderSession readerSession = (ReaderSession) request.getSession()
				.getAttribute("READER_SESSION");
		// String libcode = readerSession.getReader().getRdLib(); //操作员所在馆
		StringBuffer message = new StringBuffer();

		reader = readerService.getReader(reader.getRdId(), (byte) 2);
		LibCode lib = libCodeService.getLibByCode(reader.getRdLibCode());

//		String hasBookOrFee = acsService.queryReader(lib, reader);//有没有欠费，未还书
		String hasBookOrFee = "0";//上面acs查询慢
		if (hasBookOrFee.equals("1")) {// 表示有在借书未还
			message.append("{");
			message.append("\"success\": -3 ,");
			message.append("\"message\": \"" + Cautions.LOANEDBOOKS + "\"");
			message.append("}");
			ServletUtils.printHTML(response, message.toString());
			return;
		}
		if (hasBookOrFee.equals("2")) {
			message.append("{");
			message.append("\"success\": -4 ,");
			message.append("\"message\": \"" + Cautions.FEE_PAY + "\"");
			message.append("}");
			ServletUtils.printHTML(response, message.toString());
			return;
		}

		reader.setRdLib(reader.getRdLibCode());
		double payed = Double.parseDouble(request.getParameter("payed"));
		ReaderType readerType = readerTypeService.getReaderType(reader
				.getRdType());
		RdAccount rdAccount = rdAccountService.get(reader.getRdId());
		if (rdAccount == null) {
			rdAccount = new RdAccount();
			rdAccount.setRdid(reader.getRdId());
			rdAccount.setDeposit(0.0);
			rdAccount.setOnecard(0.0);
			rdAccount.setPrepay(0.0);
			rdAccount.setStatus(1);
			rdAccountService.save(rdAccount);
		}
		double prepay = rdAccount.getPrepay();
		double rdDeposity = rdAccount.getDeposit();

		double checkfee = readerType.getCheckfee();
		double servicefee = readerType.getServicefee();
		double idfee = readerType.getIdfee();

		ReaderFee readerFee = readerFeeService.get(reader.getRdLibCode());
		String optionFee = "";
		optionFee = readerFee.getQuitfee();
		if (optionFee.length() > 0) {
			// 表示不收取验证费,重置验证费为0
			if (optionFee.charAt(0) == '0') {
				checkfee = 0;
			}
		}
		if (optionFee.length() > 1) {
			// 表示不收取服务费,重置服务费为0
			if (optionFee.charAt(1) == '0') {
				servicefee = 0;
			}
		}
		if (optionFee.length() > 2) {
			// 表示不收取工本费,重置工本费为0
			if (optionFee.charAt(2) == '0') {
				idfee = 0;
			}
		}
		double totalFee = checkfee + servicefee + idfee - prepay - rdDeposity;

		if (totalFee != payed) {
			message.append("{");
			message.append("\"success\": 0, ");
			message.append("\"checkfee\":" + checkfee + ", ");
			message.append("\"servicefee\":" + servicefee + ", ");
			message.append("\"idfee\":" + idfee + ", ");
			message.append("\"totalfee\":" + totalFee + ", ");
			message.append("\"prepay\":" + prepay + ", "); // 应退预付款
			message.append("\"deposity\":" + rdDeposity); // 押金
			message.append("}");
			ServletUtils.printHTML(response, message.toString());
			return;
		} else {
			// 1. 读者账户清0， 押金清0，状态注销
			String ip = Common.getUserIp(request);
			rdAccount.setDeposit(0.0);
			rdAccount.setPrepay(0.0);
			rdAccount.setStatus(4); // 注销账户
			rdAccountService.update(rdAccount);
			// 2. 读者证状态注销
			reader.setRdCFState((byte) 5);
			reader.setRdRemark("该证已经退还!");
			reader.setSynStatus(0);// 添加同步状态 2014-05-15
			readerService.updateReader(reader);
			// 删除卡号信息
			readerCardInfoService.deleteByRdId(reader.getRdId());

			// 3. 写财经日志
			// 4. 写扣费日志
			int paytype = 1;
			int paySigned = 3;//已退还
			//少记了一个退证的操作动作
			String payedStr = checkfee + "]]" + servicefee + "]]" + idfee;
			logs("30109", reader, payedStr, readerSession, ip, "5", "1 to 5");
			writeDeposityLog(reader, readerSession, "108", "30208", ip, -rdDeposity, paySigned, paytype);
			writeDeposityLog(reader, readerSession, "205", "30217", ip, -prepay, paySigned, paytype);

			writeLog(reader, readerSession, ip, checkfee, servicefee, idfee, paytype);// 其他费用日志
			reader.setRdDeposit(0.0);
//			Map map = updateOpacReader(reader, lib);

			//同步
			cacheThreadPool.execute(new SyncQueue(reader, payedStr, lib, acsService,
					SyncQueue.SEND_QUIT_READER));
			
			int success = 1;
			String msg = "";
			String exception = "";
//			if (map != null) {
//				success = (Integer) map.get("success");
//				msg = (String) map.get("message");
//				exception = (String) map.get("exception");
//			}
			if (success == -2) {
				message.append("{");
				message.append("\"success\": -2 ,");
				message.append("\"message\": \""
						+ Cautions.SAVESUCCESS_BUTSYNCFAIL + "\",");
				message.append("\"exception\": \"" + exception + "\"");
				message.append("}");
			} else {
				message.append("{");
				message.append("\"success\": 1");
				message.append("}");
			}
			ServletUtils.printHTML(response, message.toString());
			return;

		}
	}

	/**
	 * 补办
	 * @param request
	 * @param response
	 * @param reader
	 */
	@SuppressWarnings({ "unused", "rawtypes" })
	@RequiresRoles("admin")
	@RequestMapping("/repair")
	public void repair(HttpServletRequest request,
			HttpServletResponse response, Reader inputReader) {

		ReaderSession readerSession = (ReaderSession) request.getSession()
				.getAttribute("READER_SESSION");
		
		
		StringBuffer message = new StringBuffer();
		Reader reader = readerService
				.getReader(inputReader.getRdId(), (byte) 2);
		reader.setCardId(inputReader.getCardId());//查询出的数据是没有卡号的

		// 这里不能修改读者类型
		if (!reader.getRdType().equals(inputReader.getRdType())) {//
			message.append("{");
			message.append("\"success\": -1,");
			message.append("\"message\": \"" + Cautions.CANNOT_CHANGE_RDTYPE
					+ "\"");
			message.append("}");
			ServletUtils.printHTML(response, message.toString());
			return;
		}

		// 只有注销，挂失的才能补办
		if (!(reader.getRdCFState() == 3 || reader.getRdCFState() == 5)) {
			message.append("{");
			message.append("\"success\": -1,");
			message.append("\"message\": \"" + Cautions.CANNOT_REPAIR + "\"");
			message.append("}");
			ServletUtils.printHTML(response, message.toString());
			return;
		}

		// 获取应缴费用返回给界面提示
		reader.setRdLib(reader.getRdLibCode());
		String paySign = request.getParameter("paySign");
		ReaderType readerType = readerTypeService.getReaderType(reader
				.getRdType());
		RdAccount rdAccount = rdAccountService.get(reader.getRdId());

		double rdDeposity = rdAccount.getDeposit();

		double checkfee = readerType.getCheckfee();
		double servicefee = readerType.getServicefee();
		double idfee = readerType.getIdfee();

		ReaderFee readerFee = readerFeeService.get(reader.getRdLibCode());
		String optionFee = "";
		optionFee = readerFee.getRepairfee();
		if (optionFee.length() > 0) {
			// 表示不收取验证费,重置验证费为0
			if (optionFee.charAt(0) == '0') {
				checkfee = 0;
			}
		}
		if (optionFee.length() > 1) {
			// 表示不收取服务费,重置服务费为0
			if (optionFee.charAt(1) == '0') {
				servicefee = 0;
			}
		}
		if (optionFee.length() > 2) {
			// 表示不收取工本费,重置工本费为0
			if (optionFee.charAt(2) == '0') {
				idfee = 0;
			}
		}
		double totalFee = checkfee + servicefee + idfee;
		// add 20131224
		String ip = Common.getUserIp(request);
		if (paySign != null && !paySign.equals("1")) {
			message.append("{");
			message.append("\"success\": 0, ");
			message.append("\"checkfee\":" + checkfee + ", ");
			message.append("\"servicefee\":" + servicefee + ", ");
			message.append("\"idfee\":" + idfee + ", ");
			message.append("\"totalfee\":" + totalFee + "");
			message.append("}");
			ServletUtils.printHTML(response, message.toString());
			return;
			
		} else if(paySign != null && paySign.equals("1")) {
			String newRdid = request.getParameter("newRdid"); // 新的证号
			// if(newRdid == null || "".equals(newRdid)) {
			// newRdid = reader.getRdId() + "P";
			// }
			String isRdidExit = readerService.checkRdIdExist(newRdid);
			if (isRdidExit.equals("1")) {
				message.append("{");
				message.append("\"success\": -1 ,");
				message.append("\"message\": \"" + Cautions.READER_EXIT + "\"");
				message.append("}");
				ServletUtils.printHTML(response, message.toString());
				return;
			}
			// 1. 新建个读者newRdid，账户转移
			inputReader.setRdLibCode(inputReader.getRdLib());
			Reader newReader = inputReader;
			// newReader.setRdLib(newReader.getRdLibCode());
			Reader r = readerService.getAvatar(reader.getRdId());
			if (r != null && r.getRdPhoto() != null) {
				newReader.setRdPhoto(r.getRdPhoto());// 转移照片
			}
			newReader.setRdInTime(new Date());
			newReader.setRdDeposit(rdDeposity); // 押金
			newReader.setRdId(newRdid);
			newReader.setRdCFState((byte) 1);
			newReader.setRdRemark("此读者证由" + reader.getRdId() + "补办而来");//更换读者证才是这个注释
			RdAccount newRdAccount = rdAccountService.get(reader.getRdId());
			newRdAccount.setRdid(newRdid);
			newRdAccount.setStatus(1);// 正常状态
			newReader.setSynStatus(0);// 添加同步状态 2014-05-15
			
			readerService.addReader(newReader); // 新增读者
			rdAccountService.save(newRdAccount); // 新增账户
			
			// 3. 处理旧证
			reader.setRdDeposit(0.0);
			reader.setRdCFState((byte) 5);
			reader.setRdRemark("此读者证已经补办为" + newRdid);
			rdAccount.setDeposit(0.0); // 押金清0
			rdAccount.setPrepay(0.0); // 余额清0
			rdAccount.setStatus(4); // 注销
			reader.setSynStatus(0);// 添加同步状态 2014-05-15
			readerService.updateReader(reader); // 旧账户读者更新
			rdAccountService.update(rdAccount);
			// 4. 转移财经日志
			Map<String, String> rdidMap = new HashMap<String, String>();
			rdidMap.put("newRdid", newRdid);
			rdidMap.put("rdid", reader.getRdId());
			cirFinLogService.updateRdid(rdidMap);
			// 5. 记录日志
			int paytype = 1;
			writeLog(newReader, readerSession, ip, checkfee, servicefee, idfee, paytype); // 记录收取费用日志和财经
			String payedStr = checkfee + "]]" + servicefee + "]]" + idfee;
			logs("30101", newReader, "", readerSession, ip, newReader.getRdType(), ""); // 记录读者增加日志
			logs("30310", reader, payedStr, readerSession, ip, "", newRdid); // 记录补办日志
			logs("30102", reader, "", readerSession, ip, "5", reader.getRdId() + "to" + newRdid);// 记录修改读者证日志
			
//			Map map = syncOpacReader(reader, null, "none");//同步旧读者的新信息
//			Map map2 = syncOpacReader(newReader, null, "none");//同步新读者信息

			//同步
//			ExecutorService cacheThreadPool = Executors.newCachedThreadPool();
//			cacheThreadPool.execute(new SyncQueue(reader, null, "none", 
//					libCodeService, readerService, SyncQueue.SEND_SYNC_READER));
//			
//			cacheThreadPool.execute(new SyncQueue(newReader, null, "none", 
//					libCodeService, readerService, SyncQueue.SEND_SYNC_READER));
			LibCode libcode = libCodeService.getLibByCode(inputReader.getRdLibCode());
			cacheThreadPool.execute(new SyncQueue(reader, newReader, payedStr, libcode, acsService,
					SyncQueue.SEND_REPAIR_READER));
			
			int success = 1;
			String msg = "";
			String exception = "";
//			if (map != null) {
//				success = (Integer) map.get("success");
//				msg = (String) map.get("message");
//				exception = (String) map.get("exception");
//			}

			if (success == -1) {
				message.append("{");
				message.append("\"success\": -2 ,");
				message.append("\"message\": \""
						+ Cautions.SAVESUCCESS_BUTSYNCFAIL + "\",");
				message.append("\"exception\": \"" + exception + "\"");
				message.append("}");
			} else {
				message.append("{");
				message.append("\"success\": 1");
				message.append("}");
			}

			ServletUtils.printHTML(response, message.toString());
			return;
		}

	}
	
	/**
	 * 卡补办
	 * @param request
	 * @param response
	 * @param reader
	 */
	@SuppressWarnings({ "unused", "rawtypes" })
	@RequiresRoles("admin")
	@RequestMapping("/cardRepair")
	public void cardRepair(HttpServletRequest request,
			HttpServletResponse response, Reader inputReader) {

		ReaderSession readerSession = (ReaderSession) request.getSession()
				.getAttribute("READER_SESSION");
//		String cardType = request.getParameter("cardType");//增加这个标识：1 代表更新rdid 2代表更新cardid
		String newCardid = request.getParameter("newCardid");//新卡号的值;
		
		StringBuffer message = new StringBuffer();
		Reader reader = readerService.getReader(inputReader.getRdId(), (byte) 2);
		
		// 这里不能修改读者类型
		if (!reader.getRdType().equals(inputReader.getRdType())) {//
			message.append("{");
			message.append("\"success\": -1,");
			message.append("\"message\": \"" + Cautions.CANNOT_CHANGE_RDTYPE
					+ "\"");
			message.append("}");
			ServletUtils.printHTML(response, message.toString());
			return;
		}

		// 只有注销，挂失的才能补办
		if (!(reader.getRdCFState() == 3 || reader.getRdCFState() == 5)) {
			message.append("{");
			message.append("\"success\": -1,");
			message.append("\"message\": \"" + Cautions.CANNOT_REPAIR + "\"");
			message.append("}");
			ServletUtils.printHTML(response, message.toString());
			return;
		}

		// 获取应缴费用返回给界面提示
		reader.setRdLib(reader.getRdLibCode());
		String paySign = request.getParameter("paySign");
		ReaderType readerType = readerTypeService.getReaderType(reader
				.getRdType());
		RdAccount rdAccount = rdAccountService.get(reader.getRdId());

		double rdDeposity = rdAccount.getDeposit();

		double checkfee = readerType.getCheckfee();
		double servicefee = readerType.getServicefee();
		double idfee = readerType.getIdfee();

		ReaderFee readerFee = readerFeeService.get(reader.getRdLibCode());
		String optionFee = "";
		optionFee = readerFee.getRepairfee();
		if (optionFee.length() > 0) {
			// 表示不收取验证费,重置验证费为0
			if (optionFee.charAt(0) == '0') {
				checkfee = 0;
			}
		}
		if (optionFee.length() > 1) {
			// 表示不收取服务费,重置服务费为0
			if (optionFee.charAt(1) == '0') {
				servicefee = 0;
			}
		}
		if (optionFee.length() > 2) {
			// 表示不收取工本费,重置工本费为0
			if (optionFee.charAt(2) == '0') {
				idfee = 0;
			}
		}
		double totalFee = checkfee + servicefee + idfee;
		// add 20131224
		String ip = Common.getUserIp(request);
		if (paySign != null && !paySign.equals("1")) {
			message.append("{");
			message.append("\"success\": 0, ");
			message.append("\"checkfee\":" + checkfee + ", ");
			message.append("\"servicefee\":" + servicefee + ", ");
			message.append("\"idfee\":" + idfee + ", ");
			message.append("\"totalfee\":" + totalFee + "");
			message.append("}");
			ServletUtils.printHTML(response, message.toString());
			return;
		} else if (paySign != null && paySign.equals("1")) {
			ReaderCardInfo oldCardInfo = readerCardInfoService.get(newCardid);
			if(oldCardInfo != null) {
				//卡号已经绑定过了
				message.append("{");
				message.append("\"success\": -1 ,");
				message.append("\"message\": \"" + Cautions.READERCARDID_EXSITS + "\"");
				message.append("}");
				ServletUtils.printHTML(response, message.toString());
				return;
			}
			oldCardInfo = readerCardInfoService.getByRdId(inputReader.getRdId());//查询旧卡号信息
			ReaderCardInfo newCardInfo = new ReaderCardInfo() ;//新卡
			Map map = new HashMap();
			
			String oldCardid = "";
			if(oldCardInfo != null) {
				oldCardid = oldCardInfo.getCardId();
			}
			reader.setRdRemark("此读者证原卡号为：" +oldCardid+ " 新补办卡号为："+newCardid);//更换卡号是这个注释
			reader.setCardId(newCardid);//更新新卡号
			reader.setRdDeposit(rdDeposity); // 押金
			reader.setRdCFState((byte) 1);
			reader.setSynStatus(0);// 添加同步状态 2014-05-15
			rdAccount.setRdid(reader.getRdId());
			rdAccount.setStatus(1);// 正常状态
			if(oldCardInfo == null){
				newCardInfo = new ReaderCardInfo();
				newCardInfo.setRdId(reader.getRdId());
				newCardInfo.setCardId(newCardid);//新写入的卡ID
				newCardInfo.setLastUseTime(new Date());
				newCardInfo.setCardType("0");
				newCardInfo.setIsUsable(1);
				newCardInfo.setTotalOfUsed(0);
				readerCardInfoService.save(newCardInfo);//增加新读者卡号
			}else{
				oldCardInfo.setCardId(newCardid);//新写入的卡ID
				readerCardInfoService.updateCardInfo(oldCardInfo);//更新读者卡号
			}
			readerService.updateReader(reader);//更新押金和读者的状态
			rdAccountService.update(rdAccount);//更新读者账户信息
//			map = syncOpacReader(reader, null, "update");//同步旧读者的新信息和卡号信息
			
			//同步
			cacheThreadPool.execute(new SyncQueue(reader, null, "update", 
					libCodeService, readerService, SyncQueue.SEND_SYNC_READER));
			
			//记录日志
			int paytype = 1;
			writeLog(reader, readerSession, ip, checkfee, servicefee, idfee, paytype); // 记录收取费用日志和财经
			String payedStr = checkfee + "]]" + servicefee + "]]" + idfee;
			logs("30102", reader, payedStr, readerSession, ip, "5", inputReader.getCardId() + "to" + newCardid);// 记录修改读者证日志
				
			
			int success = 1;
			String msg = "";
			String exception = "";
//			if (map != null) {
//				success = (Integer) map.get("success");
//				msg = (String) map.get("message");
//				exception = (String) map.get("exception");
//			}

			if (success == -1) {
				message.append("{");
				message.append("\"success\": -2 ,");
				message.append("\"message\": \""
						+ Cautions.SAVESUCCESS_BUTSYNCFAIL + "\",");
				message.append("\"exception\": \"" + exception + "\"");
				message.append("}");
			} else {
				message.append("{");
				message.append("\"success\": 1");
				message.append("}");
			}

			ServletUtils.printHTML(response, message.toString());
			return;
		}

	}


	/**
	 * 换证
	 * 
	 * @param request
	 * @param response
	 * @param reader
	 */
	@RequiresRoles("admin")
	@SuppressWarnings({ "unused", "rawtypes" })
	@RequestMapping("/change")
	public void change(HttpServletRequest request,
			HttpServletResponse response, Reader inputReader) {
		
		ReaderSession readerSession = (ReaderSession) request.getSession()
				.getAttribute("READER_SESSION");

		StringBuffer message = new StringBuffer();
		Reader reader = readerService
				.getReader(inputReader.getRdId(), (byte) 2);

		// 这里不能修改读者类型
		if (!reader.getRdType().equals(inputReader.getRdType())) {//
			message.append("{");
			message.append("\"success\": -1,");
			message.append("\"message\": \"" + Cautions.CANNOT_CHANGE_RDTYPE
					+ "\"");
			message.append("}");
			ServletUtils.printHTML(response, message.toString());
			return;
		}
		// 获取应缴费用返回给界面提示
		reader.setRdLib(reader.getRdLibCode());
		String paySign = request.getParameter("paySign");
		ReaderType readerType = readerTypeService.getReaderType(reader
				.getRdType());
		RdAccount rdAccount = rdAccountService.get(reader.getRdId());

		double rdDeposity = rdAccount.getDeposit();

		double checkfee = readerType.getCheckfee();
		double servicefee = readerType.getServicefee();
		double idfee = readerType.getIdfee();

		ReaderFee readerFee = readerFeeService.get(reader.getRdLibCode());
		String optionFee = "";
		optionFee = readerFee.getChangefee(); // 换证费
		if (optionFee.length() > 0) {
			// 表示不收取验证费,重置验证费为0
			if (optionFee.charAt(0) == '0') {
				checkfee = 0;
			}
		}
		if (optionFee.length() > 1) {
			// 表示不收取服务费,重置服务费为0
			if (optionFee.charAt(1) == '0') {
				servicefee = 0;
			}
		}
		if (optionFee.length() > 2) {
			// 表示不收取工本费,重置工本费为0
			if (optionFee.charAt(2) == '0') {
				idfee = 0;
			}
		}
		double totalFee = checkfee + servicefee + idfee;

		// add 20131224
		String ip = Common.getUserIp(request);
		if (paySign != null && !paySign.equals("1")) {
			message.append("{");
			message.append("\"success\": 0, ");
			message.append("\"checkfee\":" + checkfee + ", ");
			message.append("\"servicefee\":" + servicefee + ", ");
			message.append("\"idfee\":" + idfee + ", ");
			message.append("\"totalfee\":" + totalFee + "");
			message.append("}");
			ServletUtils.printHTML(response, message.toString());
			return;
		} else if (paySign != null && paySign.equals("1")) {// 已经收取了费用了
			String newRdid = request.getParameter("newRdid"); // 新的证号
			// if(newRdid == null || "".equals(newRdid)) {
			// newRdid = reader.getRdId() + "P";
			// }
			String isRdidExit = readerService.checkRdIdExist(newRdid);
			if (isRdidExit.equals("1")) {
				message.append("{");
				message.append("\"success\": -1 ,");
				message.append("\"message\": \"" + Cautions.READER_EXIT + "\"");
				message.append("}");
				ServletUtils.printHTML(response, message.toString());
				return;
			}
			// 1. 新建个读者newRdid，账户转移
			inputReader.setRdLibCode(inputReader.getRdLib());
			Reader newReader = inputReader;
			// newReader.setRdLib(newReader.getRdLibCode());
			Reader r = readerService.getAvatar(reader.getRdId());
			if (r != null && r.getRdPhoto() != null) {
				newReader.setRdPhoto(r.getRdPhoto());// 转移照片
			}
			newReader.setRdDeposit(rdDeposity); // 押金
			newReader.setRdId(newRdid);
			newReader.setRdCFState((byte) 1);
			newReader.setRdRemark("此读者证由" + reader.getRdId() + "换证而来");
			RdAccount newRdAccount = rdAccountService.get(reader.getRdId());
			newRdAccount.setRdid(newRdid);
			newRdAccount.setStatus(1);// 正常状态
			newReader.setSynStatus(0);// 添加同步状态 2014-05-15
			readerService.addReader(newReader); // 新增读者
			rdAccountService.save(newRdAccount); // 新增账户

			// 3. 处理旧证
			reader.setRdDeposit(0.0);
			reader.setRdCFState((byte) 5);
			reader.setRdRemark("此读者证已经换证为" + newRdid);
			rdAccount.setDeposit(0.0); // 押金清0
			rdAccount.setPrepay(0.0); // 余额清0
			rdAccount.setStatus(4); // 注销
			reader.setSynStatus(0);// 添加同步状态 2014-05-15
			readerService.updateReader(reader); // 旧账户读者更新
			rdAccountService.update(rdAccount);

			// 4. 转移财经日志
			Map<String, String> rdidMap = new HashMap<String, String>();
			rdidMap.put("newRdid", newRdid);
			rdidMap.put("rdid", reader.getRdId());
			cirFinLogService.updateRdid(rdidMap);

			// 5. 记录日志
			int paytype = 1;
			writeLog(newReader, readerSession, ip, checkfee, servicefee, idfee, paytype); // 记录收取费用日志和财经
			String payedStr = checkfee + "]]" + servicefee + "]]" + idfee;
			logs("30101", newReader, "", readerSession, ip, newReader.getRdType(), ""); // 记录读者增加日志
			
			logs("30102", reader, payedStr, readerSession, ip, "5", reader.getRdId()
					+ "to" + newRdid);// 记录修改读者证日志
			
			int success = 1;
			String msg = "";
			String exception = "";
			LibCode libcode = libCodeService.getLibByCode(inputReader.getRdLibCode());
			
			cacheThreadPool.execute(new SyncQueue(reader, newReader, payedStr, libcode, acsService,
					SyncQueue.SEND_CHANGE_READER));
			
//			Map map = syncOpacReader(reader, null, "none");
//			Map map2 = syncOpacReader(newReader, null, "none");

//			if (map != null) {
//				success = (Integer) map.get("success");
//				msg = (String) map.get("message");
//				exception = (String) map.get("exception");
//			}
			String syncStatus = "1";
			if (success == -1) {
				//如果同步失败呢？
				//TODO 同步失败，最好启动一个线程，用来记录什么操作同步失败，记录原始数据，已备下次同步
				syncStatus = "0";//同步失败
				logs("30309", reader, readerSession, ip, "", newRdid, syncStatus); // 记录换证日志
				message.append("{");
				message.append("\"success\": -2 ,");
				message.append("\"message\": \""
						+ Cautions.SAVESUCCESS_BUTSYNCFAIL + "\",");
				message.append("\"exception\": \"" + exception + "\"");
				message.append("}");
			} else {
				logs("30309", reader, readerSession, ip, "", newRdid, syncStatus); // 记录换证日志
				message.append("{");
				message.append("\"success\": 1");
				message.append("}");
			}

			ServletUtils.printHTML(response, message.toString());
			return;
		}
	}

	/**
	 * 延期读者证
	 * 
	 * @param request
	 * @param response
	 * @param reader
	 */
	@RequiresRoles("admin")
	@SuppressWarnings("rawtypes")
	@RequestMapping("/defer")
	public void defer(HttpServletRequest request, HttpServletResponse response,
			Reader inputReader) {
		ReaderSession readerSession = (ReaderSession) request.getSession()
				.getAttribute("READER_SESSION");

		StringBuffer message = new StringBuffer();
		Reader reader = readerService
				.getReader(inputReader.getRdId(), (byte) 2);
		LibCode lib = libCodeService.getLibByCode(reader.getRdLibCode());
		// 这里不能修改读者类型
		if (!reader.getRdType().equals(inputReader.getRdType())) {//
			message.append("{");
			message.append("\"success\": -1,");
			message.append("\"message\": \"" + Cautions.CANNOT_CHANGE_RDTYPE
					+ "\"");
			message.append("}");
			ServletUtils.printHTML(response, message.toString());
			return;
		}
		// 注销的证不能延期
		if (reader.getRdCFState() == 5) {
			message.append("{");
			message.append("\"success\": -1,");
			message.append("\"message\": \"" + Cautions.CANNOT_DEFER + "\"");
			message.append("}");
			ServletUtils.printHTML(response, message.toString());
			return;
		}

		// 获取应缴费用返回给界面提示
		reader.setRdLib(reader.getRdLibCode());
		String paySign = request.getParameter("paySign");

		ReaderType readerType = readerTypeService.getReaderType(reader
				.getRdType());
		double checkfee = readerType.getCheckfee();
		double servicefee = readerType.getServicefee();
		double idfee = readerType.getIdfee();

		ReaderFee readerFee = readerFeeService.get(reader.getRdLibCode());
		String optionFee = "";
		optionFee = readerFee.getDeferfee(); // 延期费
		if (optionFee.length() > 0) {
			// 表示不收取验证费,重置验证费为0
			if (optionFee.charAt(0) == '0') {
				checkfee = 0;
			}
		}
		if (optionFee.length() > 1) {
			// 表示不收取服务费,重置服务费为0
			if (optionFee.charAt(1) == '0') {
				servicefee = 0;
			}
		}
		if (optionFee.length() > 2) {
			// 表示不收取工本费,重置工本费为0
			if (optionFee.charAt(2) == '0') {
				idfee = 0;
			}
		}
		double totalFee = checkfee + servicefee + idfee;

		// add 20131224
		String ip = Common.getUserIp(request);
		if (paySign != null && !paySign.equals("1")) {
			message.append("{");
			message.append("\"success\": 0, ");
			message.append("\"checkfee\":" + checkfee + ", ");
			message.append("\"servicefee\":" + servicefee + ", ");
			message.append("\"idfee\":" + idfee + ", ");
			message.append("\"totalfee\":" + totalFee + "");
			message.append("}");
			ServletUtils.printHTML(response, message.toString());
			return;
		} else if (paySign != null && paySign.equals("1")) {
			String deferDate = request.getParameter("deferDate");
			Date rdEndDate;
			try {
				rdEndDate = TimeUtils.stringToDate(deferDate, "yyyy-MM-dd");
				reader.setRdEndDate(rdEndDate);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			reader.setRdCFState((byte) 1);
			reader.setRdLib(reader.getRdLibCode());
			reader.setSynStatus(0);// 添加同步状态 2014-05-15
			readerService.updateReader(reader);

			// log("30106","1",rdenddate);
			// 财经日志
			int paytype = 1;
			writeLog(reader, readerSession, ip, checkfee, servicefee, idfee, paytype);
			String payedStr = checkfee + "]]" + servicefee + "]]" + idfee;
			logs("30106", reader, payedStr, readerSession, ip, "1", deferDate);// 延期日志

//			Map map = syncOpacReader(reader, null, "none");
			cacheThreadPool.execute(new SyncQueue(reader, payedStr, lib, acsService,
					SyncQueue.SEND_DEFER_READER));
			int success = 1;
			String msg = "";
			String exception = "";
//			if (map != null) {
//				success = (Integer) map.get("success");
//				msg = (String) map.get("message");
//				exception = (String) map.get("exception");
//			}

			if (success == -1) {
				message.append("{");
				message.append("\"success\": -2 ,");
				message.append("\"message\": \""
						+ Cautions.SAVESUCCESS_BUTSYNCFAIL + "\",");
				message.append("\"exception\": \"" + exception + "\"");
				message.append("}");
			} else {
				message.append("{");
				message.append("\"success\": 1");
				message.append("}");
			}

			ServletUtils.printHTML(response, message.toString());
			return;

		}
	}

	/**
	 * 挂失，暂停，帐号冻结,这三个操作有点类似，写在一起了就
	 * 
	 * @param request
	 * @param response
	 * @param reader
	 */
	@RequiresRoles("admin")
	@SuppressWarnings("rawtypes")
	@RequestMapping("/cardOperation")
	public void loss(HttpServletRequest request, HttpServletResponse response,
			Reader reader) {
		ReaderSession readerSession = (ReaderSession) request.getSession()
				.getAttribute("READER_SESSION");
		String libcode = readerSession.getReader().getRdLibCode(); // 操作员所在馆
		String option = request.getParameter("option");
		reader = readerService.getReader(reader.getRdId(), (byte) 2);
		LibCode lib = libCodeService.getLibByCode(reader.getRdLibCode());

		StringBuffer message = new StringBuffer();
		// 已经注销，不用注销了
		if ((byte) reader.getRdCFState() == 5) {
			message.append("{");
			message.append("\"success\": 2");
			message.append("}");
			ServletUtils.printHTML(response, message.toString());
			return;
		}

		reader.setRdLib(reader.getRdLibCode());
		RdAccount rdAccount = rdAccountService.get(reader.getRdId());
		if (rdAccount == null) {
			rdAccount = new RdAccount();
			rdAccount.setRdid(reader.getRdId());
			rdAccount.setDeposit(0.0);
			rdAccount.setOnecard(0.0);
			rdAccount.setPrepay(0.0);
			rdAccount.setStatus(1);
			rdAccountService.save(rdAccount);
		}
		LogCir logCir = new LogCir();
		if (option.equals("loss")) { // 挂失
			if (reader.getRdCFState() == 3) {// 已经是挂失状态了，不用挂失啦
				message.append("{");
				message.append("\"success\": 2");
				message.append("}");
				ServletUtils.printHTML(response, message.toString());
				return;
			}
			logCir.setLogType("30107");
			logCir.setData3("3");
			logCir.setData4("已挂失");
			rdAccount.setStatus(2); // 账户冻结
			reader.setRdCFState((byte) 3);
		} else if (option.equals("stop")) {// 暂停
			if (reader.getRdCFState() == 4) {// 已经是暂停状态了，不用暂停了
				message.append("{");
				message.append("\"success\": 2");
				message.append("}");
				ServletUtils.printHTML(response, message.toString());
				return;
			}
			logCir.setLogType("30110");
			logCir.setData3("4");
			logCir.setData4("已暂停");
			rdAccount.setStatus(2); // 账户冻结
			reader.setRdCFState((byte) 4);
		}

		rdAccountService.update(rdAccount);
		reader.setSynStatus(0);// 添加同步状态 2014-05-15
		readerService.updateReader(reader);
		logCir.setLibcode(libcode);
		logCir.setData2(reader.getRdId());
		logCir.setUserId(readerSession.getReader().getRdId());
		logCir.setIpAddr(Common.getUserIp(request));
		logCir.setData4("");
		logCirService.save(logCir);

//		Map map = updateOpacReader(reader, lib);
		
		String payed = "0.0]]0.0]]0.0";
		if(option.equals("loss")) {
			cacheThreadPool.execute(new SyncQueue(reader, payed, lib, acsService,
					SyncQueue.SEND_REPORT_LOSS));
		} else if(option.equals("stop")) {
			cacheThreadPool.execute(new SyncQueue(reader, payed, lib, acsService,
					SyncQueue.SEND_SUSPEND_READER));
		}
		
		int success = 1;
		String msg = "";
		String exception = "";
//		if (map != null) {
//			success = (Integer) map.get("success");
//
//			msg = (String) map.get("message");
//
//			exception = (String) map.get("exception");
//		}
		if (success == -2) {
			message.append("{");
			message.append("\"success\": -2 ,");
			message.append("\"message\": \"" + Cautions.SAVESUCCESS_BUTSYNCFAIL
					+ "\",");
			message.append("\"exception\": \"" + exception + "\"");
			message.append("}");
		} else {
			message.append("{");
			message.append("\"success\": 1");
			message.append("}");
		}
		ServletUtils.printHTML(response, message.toString());
	}

	/**
	 * 记录财经日志和收取财经这个动作的操作日志，interlib里是这样记录的，有这个必要吗记
	 * 
	 * @param reader
	 * @param readerSession
	 * @param ip
	 * @param checkfee
	 * @param servicefee
	 * @param idfee
	 */
	public void writeLog(Reader reader, ReaderSession readerSession, String ip,
			double checkfee, double servicefee, double idfee, int paytype) {
		CirFinLog cirFinLog = new CirFinLog();
		LogCir logCir = new LogCir();
		if (checkfee > 0) {
			cirFinLog.setRdid(reader.getRdId());
			cirFinLog.setFeetype("105");
			cirFinLog.setFee(checkfee);
			cirFinLog.setRegtime(new Date());
			cirFinLog.setRegman(readerSession.getReader().getRdId());
			cirFinLog.setRegLib(readerSession.getReader().getRdLibCode());
			cirFinLog.setOrgLib(reader.getRdLib());
			cirFinLog.setPaySign(1);
			cirFinLog.setPaytype(paytype);// 2：IC卡或1：纸币
			cirFinLog.setTranId(Common.getCirfinTranID());
			cirFinLog.setFeeAppCode("onecard");
			cirFinLogService.save(cirFinLog);

			logCir.setLogType("30206");
			logCir.setLibcode(readerSession.getReader().getRdLibCode());
			logCir.setUserId(readerSession.getReader().getRdId());
			logCir.setIpAddr(ip);
			logCir.setData2(reader.getRdId());
			logCir.setData3("");
			logCir.setData4(checkfee + "");
			logCirService.save(logCir);
		}
		if (servicefee > 0) {
			cirFinLog.setRdid(reader.getRdId());
			cirFinLog.setFeetype("103");
			cirFinLog.setFee(servicefee);
			cirFinLog.setRegtime(new Date());
			cirFinLog.setRegman(readerSession.getReader().getRdId());
			cirFinLog.setRegLib(readerSession.getReader().getRdLibCode());
			cirFinLog.setOrgLib(reader.getRdLib());
			cirFinLog.setPaySign(1);
			cirFinLog.setPaytype(paytype);
			cirFinLog.setFeeAppCode("onecard");
			cirFinLog.setTranId(Common.getCirfinTranID());
			cirFinLogService.save(cirFinLog);
			logCir.setLogType("30204");
			logCir.setLibcode(readerSession.getReader().getRdLibCode());
			logCir.setUserId(readerSession.getReader().getRdId());
			logCir.setIpAddr(ip);
			logCir.setData2(reader.getRdId());
			logCir.setData3("");
			logCir.setData4(servicefee + "");
			logCirService.save(logCir);
		}
		if (idfee > 0) {
			cirFinLog.setRdid(reader.getRdId());
			cirFinLog.setFeetype("102");
			cirFinLog.setFee(idfee);
			cirFinLog.setRegtime(new Date());
			cirFinLog.setRegman(readerSession.getReader().getRdId());
			cirFinLog.setRegLib(readerSession.getReader().getRdLibCode());
			cirFinLog.setOrgLib(reader.getRdLib());
			cirFinLog.setPaySign(1);
			cirFinLog.setPaytype(paytype);
			cirFinLog.setFeeAppCode("onecard");
			cirFinLog.setTranId(Common.getCirfinTranID());
			cirFinLogService.save(cirFinLog);
			logCir.setLogType("30203");
			logCir.setLibcode(readerSession.getReader().getRdLibCode());
			logCir.setUserId(readerSession.getReader().getRdId());
			logCir.setIpAddr(ip);
			logCir.setData2(reader.getRdId());
			logCir.setData3("");
			logCir.setData4(idfee + "");
			logCirService.save(logCir);
		}
	}

	/**
	 * 操作日志
	 * 
	 * @param logType
	 * @param reader
	 * @param readerSession
	 * @param ip
	 * @param data3
	 * @param data4
	 * @param data5
	 */
	public void logs(String logType, Reader reader,
			ReaderSession readerSession, String ip, String data3, String data4, String data5) {
		LogCir logCir = new LogCir();
		logCir.setLogType(logType);
		logCir.setLibcode(readerSession.getReader().getRdLibCode());
		logCir.setUserId(readerSession.getReader().getRdId());
		logCir.setIpAddr(ip);
		logCir.setData2(reader.getRdId());
		logCir.setData3(data3);
		logCir.setData4(data4);
		logCir.setData5(data5);//保存个同步的状态
		logCirService.save(logCir);
	}
	/**
	 * 操作日志
	 * 
	 * @param logType
	 * @param reader
	 * @param readerSession
	 * @param ip
	 * @param data3
	 * @param data4
	 */
	public void logs(String logType, Reader reader, String payed,
			ReaderSession readerSession, String ip, String data3, String data4) {
		LogCir logCir = new LogCir();
		logCir.setLogType(logType);
		logCir.setLibcode(readerSession.getReader().getRdLibCode());
		logCir.setUserId(readerSession.getReader().getRdId());
		logCir.setIpAddr(ip);
		logCir.setData1(payed);
		logCir.setData2(reader.getRdId());
		logCir.setData3(data3);
		logCir.setData4(data4);
		logCirService.save(logCir);
	}

	/**
	 * 记录财经操作日志
	 * 
	 * @param reader
	 * @param readerSession
	 * @param ip
	 * @param deposity
	 */
	public void writeDeposityLog(Reader reader, ReaderSession readerSession,
			String feeType, String logType, String ip, double fee, int paySign, int paytype) {
		if (fee != 0) {
			CirFinLog cirFinLog = new CirFinLog();
			LogCir logCir = new LogCir();
			cirFinLog.setRdid(reader.getRdId());
			cirFinLog.setFeetype(feeType);
			cirFinLog.setFee(fee);
			cirFinLog.setRegtime(new Date());
			cirFinLog.setRegman(readerSession.getReader().getRdId());
			cirFinLog.setRegLib(readerSession.getReader().getRdLibCode());
			// cirFinLog.setOrgLib(reader.getRdLibCode());//这个值是空值，要rdlib才对
			cirFinLog.setOrgLib(reader.getRdLib());// modify by 20140714
			cirFinLog.setPaySign(paySign);
			cirFinLog.setPaytype(paytype);//
			cirFinLog.setFeeAppCode("onecard");
			cirFinLog.setTranId(Common.getCirfinTranID());
			cirFinLogService.save(cirFinLog);
			logCir.setLogType(logType);
			logCir.setLibcode(readerSession.getReader().getRdLibCode());
			logCir.setUserId(readerSession.getReader().getRdId());
			logCir.setIpAddr(ip);
			logCir.setData2(reader.getRdId());
			logCir.setData3("");
			logCir.setData4(fee + "");
			logCirService.save(logCir);
		}
	}

	/**
	 * 头像修改页
	 * 
	 * @param rdId
	 * @return
	 */
	@RequestMapping("/avatar/{rdId}")
	public ModelAndView avatarPage(@PathVariable String rdId, Model model) {
		model.addAttribute(Constants.ACTIVE_MENU_KEY, "reader_index");
		return new ModelAndView("admin/reader/avatar");
	}

	/**
	 * 上传头像图片 一个request参数只能用来构造一次MultipartRequest
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/uploadAvatar/{rdId}")
	public void uploadAvatar(HttpServletRequest request,
			HttpServletResponse response, @PathVariable String rdId) {
		try {
			System.out.println("there");
			CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
			MultipartHttpServletRequest multipartRequest = multipartResolver
					.resolveMultipart(request);
			MultipartFile multipartFile = multipartRequest.getFile("fileObj");
			// 设置上传限制大小、默认编码等
			// multipartResolver.setMaxUploadSize(111111111111l);
			multipartResolver.setDefaultEncoding("UTF-8");
			byte[] b = multipartFile.getBytes();
			if(b == null || b.length ==0) {
				return ;
			}
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("rdId", rdId);
			map.put("rdPhoto", b);
			int result = readerService.updateReaderAvatar(map);
			if (result > 0) {
				byte type = 2;
				Reader reader = readerService.getReader(rdId, type);
				reader.setPhotobytes(b);
				LibCode lib = libCodeService
						.getLibByCode(reader.getRdLibCode());
				//Reader reader, String payed, LibCode lib, AcsService acsService, String sendType
				cacheThreadPool.execute(new SyncQueue(reader, "", lib, acsService,
						SyncQueue.SEND_UPLOAD_IMAGE));
				
				ServletUtil.responseCustom("text/html", "utf-8",
						"<script>window.parent.callback('ohyeah','" + rdId
								+ "');</script>", response);
			} else {
				ServletUtil.responseCustom("text/html", "utf-8",
						"<script>window.parent.callback('ohno');</script>",
						response);
			}
		} catch (Exception e) {
			logger.error("【读者管理】读者头像上传失败！", e);
			ServletUtil.responseCustom("text/html", "utf-8",
					"<script>window.parent.callback('error');</script>",
					response);
			return;
		}
	}
	
	@RequestMapping(value = "/uploadImage/{rdId}")
	public void uploadImage(HttpServletRequest request,
			HttpServletResponse response, @PathVariable String rdId) {
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			int result = 0;
			String base64Img = request.getParameter("fileSrc");

			if(base64Img == null || "".equals(base64Img)) {
				return;
			}
			byte[] b = org.apache.commons.codec.binary.Base64.decodeBase64(base64Img.getBytes());
			map.put("rdId", rdId);
			map.put("rdPhoto", b);
			result = readerService.updateReaderAvatar(map);
			
			if (result > 0) {
				/********* 调用opac接口 *********/
				byte type = 2;
				Reader reader = readerService.getReader(rdId, type);
				reader.setPhotobytes(b);
				// 同步到把照片同步到 opac
				LibCode lib = libCodeService
						.getLibByCode(reader.getRdLibCode());
//				updateOpacReader(reader, lib);// 调用opac端接口
				
				cacheThreadPool.execute(new SyncQueue(reader, lib, readerService,
						SyncQueue.SEND_UPDATE_READER));
				
				ServletUtil.responseCustom("text/html", "utf-8",
						"<script>window.parent.callback('ohyeah','" + rdId
								+ "');</script>", response);
			} else {
				ServletUtil.responseCustom("text/html", "utf-8",
						"<script>window.parent.callback('ohno');</script>",
						response);
			}
		} catch (Exception e) {
			logger.error("【读者管理】读者头像上传失败！", e);
			ServletUtil.responseCustom("text/html", "utf-8",
					"<script>window.parent.callback('error');</script>",
					response);
			return;
		}
	}
	

	/**
	 * 上传头像(BLOB字段)
	 * 
	 * @param request
	 * @param response
	 * @param rdId
	 * @RequestMapping(value = "/uploadAvatar/{rdId}") public void
	 *                       uploadAvatar(HttpServletRequest request,
	 *                       HttpServletResponse response, @PathVariable String
	 *                       rdId) { try { String avatarPath =
	 *                       request.getContextPath() +"/" +
	 *                       AVATAR_PROJECT_PATH; FileConfig fileConfig = new
	 *                       FileConfig(avatarPath); String serverPath =
	 *                       fileConfig.getUploadPath();
	 *                       if("".equals(AVATAR_SERVER_PATH)){
	 *                       AVATAR_SERVER_PATH = serverPath; } CosFileRename
	 *                       fileRename = new CosFileRename(rdId);
	 * @SuppressWarnings("unused") MultipartRequest multi = new
	 *                             MultipartRequest(request, serverPath,
	 *                             AVATAR_MAX_SIZE, "UTF-8", fileRename);
	 * 
	 *                             Reader reader = new Reader();
	 *                             reader.setRdId(rdId);
	 *                             reader.setRdPhoto(fileRename.getNewName());
	 *                             readerService.updatePhotoName(reader); byte
	 *                             type=1; reader=readerService.getReader(rdId,
	 *                             type);
	 * 
	 *                             String photoPath=serverPath+File.separator+
	 *                             fileRename.getNewName(); FileInputStream
	 *                             inputStream = new FileInputStream(new
	 *                             File(photoPath)); byte[]
	 *                             photobytes=org.springframework
	 *                             .util.FileCopyUtils
	 *                             .copyToByteArray(inputStream);
	 *                             reader.setPhotobytes(photobytes); //同步到把照片同步到
	 *                             opac updateOpacReader(reader);//调用opac端接口 }
	 *                             catch (IOException e) {
	 *                             logger.error("【读者管理】读者头像上传失败！", e);
	 *                             ServletUtil.responseCustom("text/html",
	 *                             "utf-8",
	 *                             "<script>window.parent.callback('error');</script>"
	 *                             , response); return; }
	 *                             ServletUtil.responseCustom("text/html",
	 *                             "utf-8",
	 *                             "<script>window.parent.callback('ohyeah');</script>"
	 *                             , response); }
	 */

	/**
	 * 修改读者头像(BLOB字段)
	 * 
	 * @param rdId
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/showAvatar/{rdId}")
	public void showAvatar(@PathVariable String rdId,
			HttpServletRequest request, HttpServletResponse response) {
		Reader reader = readerService.getAvatar(rdId);
		FileInputStream imageFile = null;
		OutputStream toClient = null;
		try {
			if (reader == null || reader.getRdPhoto() == null) {
				String defaultAvatarPath = request.getSession()
						.getServletContext().getRealPath(AVATAR_DEFAULT);
				imageFile = new FileInputStream(defaultAvatarPath);
				int i = imageFile.available();
				byte data[] = new byte[i];
				imageFile.read(data);
				toClient = response.getOutputStream();
				toClient.write(data);
			} else {
				byte[] avatar = reader.getRdPhoto();
				toClient = response.getOutputStream();
				toClient.write(avatar);
			}
		} catch (FileNotFoundException e) {
			logger.error("【读者管理】未读取到读者头像图片！", e);
		} catch (IOException e) {
			logger.error("【读者管理】读取读者头像图片出现异常！", e);
		} finally {
			try {
				if (toClient != null) {
					toClient.flush();
					toClient.close();
				}
				if (imageFile != null) {
					imageFile.close();
				}
			} catch (IOException e) {
				logger.error("【读者管理】读取读者头像关闭流时出现异常！", e);
			}
		}
	}

	/**
	 * 读取读者头像
	 * 
	 * @param rdId
	 * @param response
	 */
	@RequestMapping(value = "/showReaderAvatar/{rdId}")
	public void showReaderAvatar(@PathVariable String rdId,
			HttpServletRequest request, HttpServletResponse response) {
		String avatarPath = "";
		String projectPath = request.getSession().getServletContext()
				.getRealPath("");
		String photoName = readerService.getPhotoName(rdId);
		if ("".equals(photoName)) {
			avatarPath = projectPath + AVATAR_DEFAULT;
		} else {
			if ("".equals(AVATAR_SERVER_PATH)) {
				FileConfig fileConfig = new FileConfig(request.getContextPath()
						+ "/" + AVATAR_PROJECT_PATH);
				String serverPath = fileConfig.getUploadPath();
				AVATAR_SERVER_PATH = serverPath;
			}
			avatarPath = AVATAR_SERVER_PATH + "/" + photoName;
		}
		FileInputStream imageFile = null;
		OutputStream toClient = null;
		try {
			imageFile = new FileInputStream(avatarPath);
			int i = imageFile.available();
			byte data[] = new byte[i];
			imageFile.read(data);
			// response.setContentType("image");
			toClient = response.getOutputStream();
			toClient.write(data);
		} catch (FileNotFoundException e) {
			logger.error("【读者管理】未读取到读者头像图片！", e);
		} catch (IOException e) {
			logger.error("【读者管理】读取读者头像图片出现异常！", e);
		} finally {
			try {
				if (toClient != null) {
					toClient.flush();
					toClient.close();
				}
				if (imageFile != null) {
					imageFile.close();
				}
			} catch (IOException e) {
				logger.error("【读者管理】读取读者头像关闭流时出现异常！", e);
			}
		}
	}

	/**
	 * 导出读者列表
	 * 
	 * @param reader
	 * @param response
	 */
	@RequestMapping(value = "/exportReaderExcel")
	public synchronized void exportReaderExcel(HttpServletRequest request,
			HttpServletResponse response, Reader reader) {
		List<Reader> readerlist = readerService.exportReaderData(reader);

		String inFilePath = request.getSession().getServletContext()
				.getRealPath("/jsp_tiles/excel/inFile/READER.xls");
		String filename = "读者明细列表_" + System.currentTimeMillis() + ".xls";

		OutputStream outputStream = null;
		InputStream inputStream = null;
		try {
			response.setContentType("application/vnd.ms-excel; charset=GBK");
			response.addHeader("Content-Disposition", "attachment;filename="+ new String(filename.getBytes("GBK"), "ISO8859-1"));
			outputStream = response.getOutputStream();
			inputStream = new FileInputStream(inFilePath);

			HSSFWorkbook workbook = new HSSFWorkbook(inputStream);
			HSSFSheet worksheet = workbook.getSheetAt(0);
			int size = readerlist.size();
			for (int i = 0; i < size; i++) {
				Reader thisreader = readerlist.get(i);
				HSSFRow row = worksheet.createRow(i + 1);
				row.createCell(0).setCellValue(thisreader.getRdId());
				row.createCell(1).setCellValue(thisreader.getRdName());
				row.createCell(2).setCellValue(thisreader.getRdType());
				row.createCell(3).setCellValue(thisreader.getRdStateStr());
				row.createCell(4).setCellValue(thisreader.getRdLib());
				row.createCell(5).setCellValue(
						thisreader.getRdGlobal() == 1 ? "是" : "否");
				row.createCell(6).setCellValue(thisreader.getRdLibType());
				row.createCell(7).setCellValue(thisreader.getRdInTimeStr());
				row.createCell(8).setCellValue(thisreader.getRdStartDateStr());
				row.createCell(9).setCellValue(thisreader.getRdEndDateStr());
				row.createCell(10).setCellValue(thisreader.getRdCertify());
				row.createCell(11).setCellValue(
						thisreader.getRdSex() == 1 ? "男" : "女");
				row.createCell(12).setCellValue(thisreader.getRdNation());
				row.createCell(13).setCellValue(thisreader.getRdBornDateStr());
				row.createCell(14).setCellValue(thisreader.getRdLoginId());
				row.createCell(15).setCellValue(thisreader.getRdAddress());
				row.createCell(16).setCellValue(thisreader.getRdNative());
				row.createCell(17).setCellValue(thisreader.getRdEmail());
				row.createCell(18).setCellValue(thisreader.getRdUnit());
				row.createCell(19).setCellValue(thisreader.getRdPhone());
				row.createCell(20).setCellValue(thisreader.getRdPostCode());
				row.createCell(21).setCellValue(thisreader.getRdSort1());
				row.createCell(22).setCellValue(thisreader.getRdSort2());
				row.createCell(23).setCellValue(thisreader.getRdSort3());
				row.createCell(24).setCellValue(thisreader.getRdSort4());
				row.createCell(25).setCellValue(thisreader.getRdSort5());
				row.createCell(26).setCellValue(thisreader.getRdInterest());
			}

			workbook.write(outputStream);
		} catch (FileNotFoundException e) {
			logger.error("【读者管理】导出读者列表时未读取到模板文件！", e);
		} catch (IOException e) {
			logger.error("【读者管理】导出读者列表时出现IO流异常！", e);
		} finally {
			if (inputStream != null || outputStream != null) {
				try {
					inputStream.close();
					outputStream.close();
				} catch (IOException e) {
					logger.error("【读者管理】导出读者列表关闭输出流时异常！", e);
				}
			}
		}
	}

	@RequestMapping(value = "/exportTxt")
	public synchronized void exportTxt(HttpServletRequest request,
			HttpServletResponse response, Reader reader) {
		List<Reader> readerlist = readerService.exportReaderData(reader);

		String fileName = "读者_金图格式1_" + System.currentTimeMillis() + ".txt";
		String type = request.getParameter("exportType");
		if(type.equals("4")) {
			fileName =  "读者_金图格式2_" + System.currentTimeMillis() + ".txt";
		}
		OutputStream outputStream = null;
		BufferedOutputStream buff = null;
		StringBuffer write = new StringBuffer(); 
		try {
			fileName = URLEncoder.encode(fileName, "UTF-8");
			response.setContentType("text/plain");
			response.addHeader("Content-Disposition", "attachment;filename="
					+ fileName);
			outputStream = response.getOutputStream();
			buff = new BufferedOutputStream(outputStream);
			String enter = "\r\n";
			int size = readerlist.size();
			if(type.equals("2")) {
				write.append("用户名,密码,用户组ID,真实姓名,到期日期,密码问题,问题答案,联系Email,所在部门,联系电话,联系地址,邮码,证件号码,备注,用户MAC地址,余额");
				write.append(enter);
				for (int i = 0; i < size; i++) {
					Reader thisreader = readerlist.get(i);
					write.append(thisreader.getRdId() + ",");
					write.append(EncryptDecryptData.decryptWithCode(DES_STATIC_KEY, thisreader.getRdPasswd()) + ",");
					write.append(",");
					write.append(thisreader.getRdName() + ",");
					write.append(",");
					write.append(",");
					write.append(",");
					write.append(",");
					write.append(",");
					write.append(",");
					write.append(",");
					write.append(",");
					write.append(thisreader.getRdCertify() + ",");
					write.append(",");
					write.append(",");
					write.append(enter);
				}
			} else {
				write.append("用户名,密码,真实姓名,联系地址,联系电话,证件号码,密码问题,密码答案,备注");
				write.append(enter);
				for (int i = 0; i < size; i++) {
					Reader thisreader = readerlist.get(i);
					write.append(thisreader.getRdId() + ",");					
					write.append(EncryptDecryptData.decryptWithCode(DES_STATIC_KEY, thisreader.getRdPasswd()) + ",");
					write.append(thisreader.getRdName() + ",");
					write.append(",");
					write.append(",");
					write.append(thisreader.getRdCertify() + ",");
					write.append(",");
					write.append(",");
					write.append(enter);
				}
			}
			buff.write(write.toString().getBytes("UTF-8"));
			buff.flush();
			buff.close();
		} catch (Exception e) {
			logger.error("【读者管理】导出读者列表异常！", e);
		} finally {
			try {
				buff.close();
				outputStream.close();
			} catch (IOException e) {
				logger.error("【读者管理】导出读者列表关闭输出流时异常！", e);
			}
		}
	}
	
	/**
	 * 输出流转输入流
	 * 
	 * @param outputStream
	 * @return
	 */
	@SuppressWarnings("unused")
	private InputStream getInputFileStream(OutputStream outputStream) {
		ObjectInputStream ois = null;
		try {
			// 创建二进制输出流
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			// 转成对象输出流
			ObjectOutputStream oos = new ObjectOutputStream(bos);
			String fosStr = outputStream.toString();
			// 将对象写入对象输出流
			oos.writeObject(fosStr);
			// 将二进制数组转成输入流
			byte[] bytes = bos.toByteArray();
			ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
			// 转成对象输入流
			ois = new ObjectInputStream(bis);
			// 读取输入流中的数据
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ois;
	}

	/**
	 * 下载方式一
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@SuppressWarnings("unused")
	private void download(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		BufferedInputStream in = null;
		BufferedOutputStream out = null;
		request.setCharacterEncoding("UTF-8");
		String rootpath = request.getSession().getServletContext()
				.getRealPath("/");
		try {
			File f = new File(rootpath + "jsp_tiles/excel/outFile/READER.xls");
			response.setContentType("application/ms-excel; charset=UTF-8");
			response.setHeader("Content-Disposition",
					"attachment; filename=1.xls");
			response.setHeader("Content-Length", String.valueOf(f.length()));
			in = new BufferedInputStream(new FileInputStream(f));
			out = new BufferedOutputStream(response.getOutputStream());
			byte[] data = new byte[1024];
			int len = 0;
			while (-1 != (len = in.read(data, 0, data.length))) {
				out.write(data, 0, len);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (in != null) {
				in.close();
			}
			if (out != null) {
				out.close();
			}
		}
	}

	/**
	 * 下载方式二
	 * 
	 * @param filePath
	 * @param response
	 * @param outputName
	 */
	@SuppressWarnings("unused")
	private void downLoadExcel(String filePath, HttpServletResponse response,
			String outputName) {
		String filename = outputName + ".xls";
		OutputStream outputStream = null;
		InputStream inputStream = null;
		try {
			response.addHeader("Content-Disposition", "attachment;filename="
					+ new String(filename.getBytes("gbk"), "ISO8859-1"));
			response.setContentType("application/ms-excel;charset=GBK");
			// response.setHeader("Pragma", "No-cache");
			// response.setHeader("Cache-Control", "no-cache");
			// response.setDateHeader("Expires", 0);
			outputStream = response.getOutputStream();
			inputStream = new FileInputStream(filePath);
			byte[] buffer = new byte[1024];
			int i = 0;// -1
			while ((i = inputStream.read(buffer)) != -1) {
				outputStream.write(buffer, 0, i);
				outputStream.flush();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (outputStream != null)
					outputStream.close();
				// if(inputStream!=null)inputStream.close();
				// outputStream = null;
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
	}

	/**
	 * 查询读者
	 * 
	 * @param model
	 * @param fieldName
	 * @param fieldValue
	 */
	@RequiresRoles("admin")
	@RequestMapping(value = "/searchReader")
	public void searchReader(HttpServletResponse response, String fieldName,
			String fieldValue) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("fieldName", fieldName);
		params.put("fieldValue", fieldValue);
		List<Reader> readers = readerService.searchReader(params);
		String result = "";
		int size = readers.size();
		if (size == 0) {
			result = "{\"result\":\"none\"}";
		} else if (size > 0) {
			Reader reader = readers.get(0);
			int groupId = readerService.getReaderGroupId(reader.getRdId());
			reader.setGroupId(groupId);
			result = Jackson.getBaseJsonData(reader);
		}
		ServletUtil.responseOut("UTF-8", result, response);
	}

	/**
	 * 检查读者押金与读者类型规定押金是否一致，多退少补
	 * 
	 * @param rdId
	 * @param response
	 */
	@RequestMapping(value = "/checkReaderDeposity")
	public void checkReaderDeposity(HttpServletRequest request,
			HttpServletResponse response) {
		ReaderSession currReader = (ReaderSession) request.getSession()
				.getAttribute("READER_SESSION");
		String rdLibCode = currReader.getReader().getRdLibCode();
		String rdId = request.getParameter("rdId");
		String paySign = request.getParameter("paySign");
		Reader reader = readerService.getReader(rdId, (byte) 2);
		RdAccount rdAccount = rdAccountService.get(rdId);
		ReaderType readerType = readerTypeService.getReaderType(reader
				.getRdType());
		double deposityDifferent = 0;// 差价
		String deposityOperation = "";
		if (rdAccount != null) {
			deposityDifferent = rdAccount.getDeposit()
					- readerType.getDeposity();
			if (deposityDifferent > 0) { // 需要退款
				deposityOperation = "refund";
			} else if (deposityDifferent < 0) {
				deposityDifferent = 0 - deposityDifferent;
				deposityOperation = "add"; // 需要补交押金
			} else {
				deposityOperation = "none"; // 无
			}
		}
		if (paySign.equals("1")) {
			rdAccount.setDeposit(readerType.getDeposity());
			rdAccountService.update(rdAccount);
			String feeType = "";
			String logType = "";
			String ip = Common.getUserIp(request);
			int paytype = 1;
			if (deposityOperation.equals("refund")) {
				int paySigned = 3;//已退还
				feeType = "110";// 减少押金
				logType = "30210";
				writeDeposityLog(reader, currReader, feeType, logType, ip,
						-deposityDifferent,paySigned, paytype);
			} else if (deposityOperation.equals("add")) {
				int paySigned = 1;//已交付
				feeType = "109";// 补交押金
				logType = "30209";
				writeDeposityLog(reader, currReader, feeType, logType, ip,
						deposityDifferent, paySigned,paytype);
			}
			String result = "{\"result\": {\"deposityOperation\": \""
					+ deposityOperation + "\", \"deposityDifferent\": \""
					+ deposityDifferent + "\", \"success\": \"true\"}}";
			ServletUtils.printHTML(response, result);
			return;
		} else {
			String result = "{\"result\": {\"deposityOperation\": \""
					+ deposityOperation + "\", \"deposityDifferent\": \""
					+ deposityDifferent + "\", \"success\": \"false\"}}";
			ServletUtils.printHTML(response, result);
			return;
		}

	}

	/**
	 * 读者新增页
	 * 
	 * @param model
	 * @return
	 * @throws ParseException
	 */
	@RequiresRoles("admin")
	@RequestMapping("/add/{rdId}")
	public ModelAndView add(HttpServletRequest request,
			@PathVariable String rdId, Model model) throws ParseException {
		ReaderSession currReader = (ReaderSession) request.getSession()
				.getAttribute("READER_SESSION");
		String rdLibCode = currReader.getReader().getRdLibCode();
		String thisLibCode = "";
		List<Map<String, String>> libCodes = readerService.getLibCode();
		if (rdLibCode != null) {
			thisLibCode = rdLibCode;
		} else {
			thisLibCode = libCodes.get(0).get("LIBCODE");
		}
		List<Map<String, String>> readertypes = readerService
				.getLibReaderType(thisLibCode);
		model.addAttribute("readertypes", Jackson.getBaseJsonData(readertypes));
		model.addAttribute("libcodes", Jackson.getBaseJsonData(libCodes));
		model.addAttribute("globaltypes", Jackson.getBaseJsonData(readerService
				.getThisGlobalReaderType(thisLibCode)));
		model.addAttribute("defaultLibCode", thisLibCode);
		// 有效期
		if (!readertypes.isEmpty()) {
			String rdType = readertypes.get(0).get("READERTYPE");
			int effectDays = readerService.getValDate(rdType);
			model.addAttribute("rdEndDate", calculateDate(effectDays));
		} else {
			model.addAttribute("rdEndDate", "");
		}
		Map<String, String> params = new HashMap<String, String>(2);
		params.put("fieldName", "rdId");
		params.put("fieldValue", rdId);
		List<Reader> readers = readerService.searchReader(params);
		// 刷卡消费分组
		List<CardGroup> groups = readerService.getCardGroups();
		if (!groups.isEmpty()) {
			model.addAttribute("groups", Jackson.getBaseJsonData(groups));
		}
		int groupId = readerService.getReaderGroupId(rdId);
		if (!readers.isEmpty()) {
			readers.get(0).setGroupId(groupId);
			ReaderCardInfo card = readerCardInfoService.getByRdId(rdId);
			if (card != null) {
				readers.get(0).setCardId(card.getCardId());
			}
		}
		params = new HashMap<String, String>(2);
		params.put("fieldName", "rdCertify");
		params.put("fieldValue", readers.get(0).getRdCertify());
		List<Reader> moreReaders = readerService.searchReader(params);
		model.addAttribute("reader", Jackson.getBaseJsonData(readers));
		model.addAttribute("moreReaders", moreReaders);
		model.addAttribute("Detail_to_Edit", "why_not");
		model.addAttribute(Constants.ACTIVE_MENU_KEY, "reader_add");
		return new ModelAndView("admin/reader/add");
	}

	/**
	 * 获取读者财经列表信息
	 * 
	 * @param reader
	 * @param paysign
	 * @param doPage
	 * @param toPage
	 * @param pageSize
	 * @return
	 */
	public List<Finance> getReaderFinance(Reader reader, LibCode lib,
			String paysign, boolean doPage, int toPage, int pageSize) {
		String webserviceUrl = lib.getWebserviceUrl();
		if (!"".equals(webserviceUrl) && webserviceUrl != null) {
			webserviceUrl = webserviceUrl
					+ (webserviceUrl.endsWith("/") ? "" : "/")
					+ "webservice/financeWebservice";
			JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();

			factory.setServiceClass(ReaderWebservice.class);

			factory.setAddress(webserviceUrl);

			FinanceWebservice client = (FinanceWebservice) factory.create();

			// Finance对象 cost欠款 tranid 财经流水记录号
			List<Finance> financeList = client.getFinanceByPaysign(
					reader.getRdId(),
					MD5Util.MD5Encode(reader.getRdId() + "0"), paysign, doPage,
					toPage, pageSize);
			return financeList;
		} else {
			return null;
		}
	}

	/**
	 * ADD 2014-05-23 读者未同步列表
	 * 
	 * @return
	 */
	@RequiresRoles("admin")
	@RequestMapping(value = "/readerUnSynList")
	public ModelAndView readerUnSynList(Model model, Reader reader) {
		reader.setLibUser(0);// 只查普通读者，操作员libuser为1
		model.addAttribute("list", readerService.readerUnSynList(reader));
		model.addAttribute("readertypes",
				Jackson.getBaseJsonData(readerService.getReaderType()));
		model.addAttribute("libcodes",
				Jackson.getBaseJsonData(readerService.getLibCode()));
		model.addAttribute("obj", reader);
		model.addAttribute(Constants.ACTIVE_MENU_KEY, "reader_readerUnSynList");
		return new ModelAndView("admin/reader/readerUnSynList");
	}

	/**
	 * ADD 2014-05-26 批量同步操作
	 * 
	 * @param model
	 * @param reader
	 * @return
	 */
	@RequiresRoles("admin")
	@RequestMapping(value = "/doUnSynStatus")
	// public ModelAndView doUnSynStatus(HttpServletRequest
	// request,HttpServletResponse response,Model model,Reader reader){
	public void doUnSynStatus(HttpServletRequest request,
			HttpServletResponse response, Model model, Reader reader) {
		//同步押金 操作员也是必须值 regman
		// 根据rdids信息查找出那些需要同步的读者，然后一个个进行同步
		String rdIdsStr = request.getParameter("rdIds");
		String result = "无同步的读者！";
		if (rdIdsStr == null && "".equals(rdIdsStr))
			ServletUtil.responseOut("GBK", String.valueOf(""), response);
		String[] rdIds = rdIdsStr.split(",");
		String resultSuc = "";
		String resultFail = "";
		int success = 1;
		for (String rdId : rdIds) {
			Reader r = readerService.getReader(rdId, (byte) 2);//查看
			RdAccount rdaccount = rdAccountService.get(rdId);
			if(rdaccount!=null){
				r.setRdDeposit(rdaccount.getDeposit());
			}
//			Map map = syncOpacReader(r, null, "none");// 同步数据
			
			cacheThreadPool.execute(new SyncQueue(r, null, "none",
					libCodeService, readerService, SyncQueue.SEND_SYNC_READER));
			
//			int suc = (Integer) map.get("success");
//			if (suc == 1) {
//				resultSuc = resultSuc + rdId + ",";
//				success = 1;
//			} else {
//				String msg = (String) map.get("message");
//				resultFail = resultFail + rdId + msg + ",";
//				success = -1;
//			}
		}
		result = resultSuc + " " + resultFail;
		StringBuffer message = new StringBuffer();
		message.append("{");
		message.append("\"success\": " + success + ",");
		message.append("\"message\": \"" + resultFail + "\",");
		message.append("\"exception\": \"null\"");
		message.append("}");
		ServletUtils.printHTML(response, message.toString());
		return;
		// ServletUtil.responseOut("GBK", String.valueOf(result), response);

		// return new ModelAndView("redirect:/admin/reader/readerUnSynList");
	}

	/**
	 * 馆权限设置
	 * 
	 * @param rdId
	 * @param model
	 * @return
	 */
	@RequestMapping("/libassign")
	public String libListTree(
			@RequestParam(value = "rdId", required = true) String rdId,
			Model model) {
		model.addAttribute(Constants.ACTIVE_MENU_KEY, "libassign_list");
		// List<String> subsysList = resourcesService.listAllSubsys();
		// //所有资源的父菜单
		// List<String> resIdList =
		// competsService.getResourceByCompetId(competId); //权限包含的资源
		// 查询对应rdID读者下现有的馆的权限
		Map<String, String> map = new HashMap<String, String>();
		map.put("rdId", rdId);
		List<LibCode> selectedLibs = libCodeService.getSelectLibs(map);// 已选的lib
																		// 信息
		List<LibCode> libs = libCodeService.getLibCodeSet();
		List<LibCode> comRes = new ArrayList<LibCode>();// 已选的资源
		StringBuffer sb = new StringBuffer();
		sb.append("[");
		if (true) {// 数据源的操纵 //系统组织管理（前一层菜单）
			sb.append("{");
			sb.append("\"id\":\"0\",");
			sb.append("\"name\":\"\",");
			sb.append("\"children\":[");
			boolean hasResource = false;
			for (LibCode lib : libs) {//
				sb.append("{");
				sb.append("\"id\":\"" + lib.getLibCode() + "\",");
				sb.append("\"name\":\"" + lib.getSimpleName() + "\"");
				if (selectedLibs.contains(lib)) {// r.getResourceId()
					hasResource = true;
					sb.append(",\"checked\":\"true\"");
					comRes.add(lib);
				}
				sb.append("},");
			}
			StringUtils.removeEndChar(sb, ",");
			sb.append("]");
			if (hasResource) {
				sb.append(",");
				sb.append("\"checked\":\"true\"");
			}
			sb.append("},");
		}
		StringUtils.removeEndChar(sb, ",");
		sb.append("]");
		// System.out.println(sb.toString());
		model.addAttribute("selectedLibs", selectedLibs);// 显示的已选的信息
		model.addAttribute("rdId", rdId);
		model.addAttribute("libResourceMenu", sb.toString());

		return "admin/sys/reader/libassign";
	}

	@RequestMapping("/saveLibAssign")
	public ModelAndView saveAssign(HttpServletRequest request,
			HttpServletResponse response, RedirectAttributes redirectAttributes) {
		String resIds = request.getParameter("resIds");
		String rdId = request.getParameter("rdId");
		Reader r = readerService.getReader(rdId, (byte) 2);
		if (resIds != null && !"".equals(resIds)) {
			r.setRdId(rdId);
			r.setLibAsSign(resIds);
			int l = readerService.updateLibAssign(r);// 更新数据到数据
		}

		redirectAttributes.addFlashAttribute("message", "保存成功");
		return new ModelAndView("redirect:/admin/reader/libassign?rdId=" + rdId);
	}
	
	/**
	 * 读者扣费页面
	 * @param model
	 * @param request
	 * @return
	 * @throws ParseException
	 */
	@RequiresRoles("admin")
	@RequestMapping("/deduction")
	public ModelAndView deductionView(HttpServletRequest request,Model model)
			throws ParseException {
		// 取得当前登录的管理员session信息
		ReaderSession currReader = (ReaderSession) request.getSession().getAttribute("READER_SESSION");
		Reader currR = currReader.getReader();
		//查询读者本身所在的appcode
		List<Authorization> authorizations = authorizationService.getAppsByOperator(currR.getRdId());
		model.addAttribute("authorizations", Jackson.getBaseJsonData(authorizations));
		model.addAttribute(Constants.ACTIVE_MENU_KEY, "reader_deduction");
		return new ModelAndView("admin/reader/deduction");
	}
	
	/**
	 * 查询读者和对应可以扣费的信息
	 * @param model
	 * @param fieldName
	 * @param fieldValue
	 */
	@RequiresRoles("admin")
	@RequestMapping(value = "/findReaderDeduction")
	public void findReaderDeduction(HttpServletRequest request,HttpServletResponse response, String fieldName,
			String fieldValue) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("fieldName", fieldName);
		params.put("fieldValue", fieldValue);
		List<Reader> readers = readerService.searchReader(params);
		String result = "";
		int size = readers.size();
		if (size == 0) {
			result = "{\"result\":\"none\"}";
		} else if (size == 1) {
			Reader reader = readers.get(0);
			RdAccount rdAccount = rdAccountService.get(reader.getRdId());
			if (rdAccount != null) {
				reader.setPrepay(rdAccount.getPrepay());//余额
				reader.setRdDeposit(rdAccount.getDeposit());
			}
			ReaderCardInfo cardInfo = readerCardInfoService.getByRdId(reader
					.getRdId());
			if (cardInfo != null) {
				reader.setCardId(cardInfo.getCardId());
			}
			int groupId = readerService.getReaderGroupId(reader.getRdId());
			reader.setGroupId(groupId);
			result = Jackson.getBaseJsonData(reader);
		} else if (size > 1) {
			result = "{\"result\":\"more\"}";
		}
		ServletUtil.responseOut("UTF-8", result, response);
	}
	
	/**
	 * 2014-12-28
	 * @param response
	 * @param fieldName
	 * @param fieldValue
	 */
	@RequiresRoles("admin")
	@RequestMapping(value = "/saveReaderDeduction")
	public void saveReaderDeduction(HttpServletRequest request,HttpServletResponse response,
			String rdid,String cardId,String prepay,String fee,String appcode,String feetype) {
		
		String userid = null;//操作员
		String libcode = null;//馆员馆代码
		String result ;
		//保存数据到数据库
		String reqIp = Common.getUserIp(request);
		R r = new R();
		
		if(cardId ==null && rdid == null) {
			r.message = "卡号和读者证号不能同时为空";
			r.success = "0";
			result = Jackson.getBaseJsonData(r);//返回的JSON
			ServletUtil.responseOut("UTF-8", result, response);
			return;
		}
		
		//查询读者对应的卡号信息
		ReaderCardInfo card = null;
		if(cardId != null && !"".equals(cardId)) {
			card = readerCardInfoService.get(cardId);
			if(card == null) {
				r.message = "该卡号不存在有效记录";
				r.success = "0";
				result = Jackson.getBaseJsonData(r);//返回的JSON
				ServletUtil.responseOut("UTF-8", result, response);
				return;
			}
			if(card.getIsUsable() == 0) {
				r.message = "该卡暂不能使用";
				r.success = "0";
				result = Jackson.getBaseJsonData(r);//返回的JSON
				ServletUtil.responseOut("UTF-8", result, response);
				return;
			}
			rdid = card.getRdId();
		}
		
		Reader reader = readerService.getReader(rdid, (byte)1);
		// 取得当前登录的管理员session信息
		ReaderSession currReader = (ReaderSession) request.getSession().getAttribute("READER_SESSION");
		Reader currR = currReader.getReader();
		if(currR != null){
			userid = currR.getRdId();//操作员
			libcode = currR.getRdLib();//馆代码
		}
		if(fee == null) {
			r.message = "传递的金额不正确";
			r.success = "0";
			result = Jackson.getBaseJsonData(r);//返回的JSON
			ServletUtil.responseOut("UTF-8", result, response);
			return;
		}
		if(userid == null) {
			r.message = "操作员不能为空";
			r.success = "0";
			result = Jackson.getBaseJsonData(r);//返回的JSON
			ServletUtil.responseOut("UTF-8", result, response);
			return;
		}
		if(libcode == null) {
			r.message = "馆代码不能为空";
			r.success = "0";
			result = Jackson.getBaseJsonData(r);//返回的JSON
			ServletUtil.responseOut("UTF-8", result, response);
			return;
		}
		if(appcode == null) {
			r.message = "应用代码不能为空";
			r.success = "0";
			result = Jackson.getBaseJsonData(r);//返回的JSON
			ServletUtil.responseOut("UTF-8", result, response);
			return;
		}
		if(feetype == null) {
			r.message = "扣费类型不能为空";
			r.success = "0";
			result = Jackson.getBaseJsonData(r);//返回的JSON
			ServletUtil.responseOut("UTF-8", result, response);
			return;
		}
		double feed = Double.parseDouble(fee);//扣费金额
		if(feed < 0) {
			r.message = "传递的金额不正确";
			r.success = "0";
			result = Jackson.getBaseJsonData(r);//返回的JSON
			ServletUtil.responseOut("UTF-8", result, response);
			return;
		}
		r.rdid = rdid;
		r.fee = fee;
		
		
		RdAccount rdAccount = rdAccountService.get(rdid);
		if(rdAccount == null) {
			rdAccount = new RdAccount();
			rdAccount.setRdid(rdid);
			rdAccount.setDeposit(0.0);
			rdAccount.setPrepay(0.0);
			rdAccountService.save(rdAccount);
		}
		if(rdAccount.getStatus() != 1) {
			r.message = "账户非正常状态";
			r.success = "0";
			result = Jackson.getBaseJsonData(r);//返回的JSON
			ServletUtil.responseOut("UTF-8", result, response);
			return;
		}
		double rdPrepay = rdAccount.getPrepay();
		r.prepay = rdPrepay + "";
		if(rdPrepay < feed) {
			r.message = "账户余额不足";
			r.prepay = rdAccount.getPrepay() + "";
			r.success = "0";
			result = Jackson.getBaseJsonData(r);//返回的JSON
			ServletUtil.responseOut("UTF-8", result, response);
			return;
		}
		
		rdPrepay = rdPrepay - feed;
		rdAccount.setPrepay(rdPrepay);
		rdAccountService.update(rdAccount);//扣费成功
		
		Date slotTime = new Date();
		if(cardId != null && !"".equals(cardId)) {
			card.setLastUseTime(slotTime);
			card.setTotalOfUsed(card.getTotalOfUsed() + 1);
			readerCardInfoService.update(card);
		}
		
		//记录日志信息
		r.slotTime = TimeUtils.dateToString(slotTime, TimeUtils.YYYYMMDDHHMMSS);
		writeLog(reader, userid, libcode, appcode, feetype, "30219", reqIp, feed, null,0, 2);//30219 读者扣费 操作类型
		r.message = "扣费成功！";
		r.success = "1";
		r.prepay = rdPrepay + "";
		result = Jackson.getBaseJsonData(r);//返回的JSON
		ServletUtil.responseOut("UTF-8", result, response);
		
	}
	
	/**
	 * 同步方法,判断是否存在，存在则更新，不存在则新增
	 * 
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

			String realPasswd = reader.getOldRdpasswd();// reader.getRdPasswd();//这里的密码存的是des加密的
														// modify by
														// 2014-05-13同步认证使用时旧密码

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
					//client.updateReaderInfo(r, rdpasswdMd5);//上传照片
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
	 * 记录财经操作日志
	 * @param reader
	 * @param readerSession
	 * @param ip
	 * @param deposity
	 */
	private void writeLog(Reader reader, String userid, String libcode, String appcode, String feeType,
			String logType, String ip, double fee, String feeMemo,double cost, int paytype) {
		CirFinLog cirFinLog = new CirFinLog();
		LogCir logCir = new LogCir();
		cirFinLog.setRdid(reader.getRdId());
		cirFinLog.setFeetype(feeType);
		cirFinLog.setFee(fee);
		cirFinLog.setRegtime(new Date());
		cirFinLog.setRegman(userid);
		cirFinLog.setRegLib(libcode);
		cirFinLog.setOrgLib(reader.getRdLibCode());
		cirFinLog.setPaySign(1);
		cirFinLog.setTranId(Common.getCirfinTranID());
		cirFinLog.setFeeAppCode(appcode);
		cirFinLog.setGroupID(reader.getGroupId());//ADD 2014-04-28
		cirFinLog.setPaytype(paytype);
		if(feeMemo != null) {
			cirFinLog.setFeeMemo(feeMemo);
			logCir.setData3(feeMemo);
		}
		cirFinLog.setCost(cost);//add 20140321
		cirFinLogService.save(cirFinLog);
		logCir.setLogType(logType);
		logCir.setLibcode(libcode);
		logCir.setUserId(userid);
		logCir.setIpAddr(ip);
		logCir.setData2(reader.getRdId());
		logCir.setData3("");
		logCir.setData4(fee+ "");
		logCirService.save(logCir);
	}
		
	@XmlRootElement(name="Result")
	static class R {
		public String rdid;
		public String prepay; //余额
		public String slotTime;//消费时间
		public String fee; //消费金额
		public String feetype;//消费类型
		public String cardid;
		public String message;
		public String success;
	}
}
