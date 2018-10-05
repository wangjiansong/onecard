package com.interlib.sso.controller.portal;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.apache.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.jasig.cas.client.authentication.AttributePrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.interlib.opac.webservice.ReaderWebservice;
import com.interlib.sso.common.Cautions;
import com.interlib.sso.common.Common;
import com.interlib.sso.common.Constants;
import com.interlib.sso.common.EncryptDecryptData;
import com.interlib.sso.common.IdCard;
import com.interlib.sso.common.MD5Util;
import com.interlib.sso.common.PropertyUtils;
import com.interlib.sso.common.ServletUtils;
import com.interlib.sso.common.StringUtils;
import com.interlib.sso.common.json.Jackson;
import com.interlib.sso.common.servlet.ServletUtil;
import com.interlib.sso.domain.Blackboard;
import com.interlib.sso.domain.BlackboardAndGuideSession;
import com.interlib.sso.domain.FinApp;
import com.interlib.sso.domain.Guide;
import com.interlib.sso.domain.LibCode;
import com.interlib.sso.domain.NetReader;
import com.interlib.sso.domain.Reader;
import com.interlib.sso.domain.ReaderSession;
import com.interlib.sso.domain.ReaderType;
import com.interlib.sso.domain.Resources;
import com.interlib.sso.exception.UnAcceptableLicenseException;
import com.interlib.sso.service.BlackboardService;
import com.interlib.sso.service.CompetsService;
import com.interlib.sso.service.FinAppService;
import com.interlib.sso.service.GuideService;
import com.interlib.sso.service.LibCodeService;
import com.interlib.sso.service.NetReaderService;
import com.interlib.sso.service.NumService;
import com.interlib.sso.service.ReaderService;
import com.interlib.sso.service.ReaderTypeService;
import com.interlib.sso.service.ResourceService;
import com.interlib.sso.service.RolesService;

@Controller
@RequestMapping("/portal")
public class PortalController {

	
	private static final Logger logger = Logger
			.getLogger(PortalController.class);
	
	private static final String LOGIN_VIEW = "login";
	private static final String INDEX_VIEW = "portal/index";
	//电脑，手机跳转两种不同页面
	private static final String READERINFO_VIEW = "readerInfo";//ADD 2017/10/20  by wjs
	private static final String READERINFO_VIEW_MOBLE = "readerInfo_moble";

	private static final String REGISTER_VIEW = "register";
	private static final String REGISTER_MOBLE_VIEW = "register_moble";

	private static final String REGRESULT_VIEW = "regresult";
	private static final String REGRESULT_MOBLE_VIEW = "regresult_moble";
	
	private static final String AGREEMENT_VIEW = "agreement";//ADD 2017/10/20  by wjs
	private static final String AGREEMENT_MOBLE_VIEW = "agreement_moble";
	private static final String RETRIEVEPASSWORD_VIEW = "retrievePassword";//ADD 2017/10/20  by wjs
	private static final String DORETRIEVEPASSWORD_VIEW = "doRetrievePassword";//ADD 2017/10/23  by wjs
	
    private ExecutorService executor = Executors.newFixedThreadPool(1);

//    @Autowired
//	private ThreadPoolTaskExecutor threadPool;

	@Autowired
	public ReaderService readerService;
	
	@Autowired
	public NumService numService;
	@Autowired
	public ReaderTypeService readerTypeService;
	@Autowired
	public RolesService rolesService;
	@Autowired
	public NetReaderService netReaderService;

	@Autowired
	public CompetsService competsService;
	@Autowired
	public ResourceService resourceService;
	@Autowired
	public LibCodeService libCodeService;
	@Autowired
	public FinAppService finAppService;
	
	//add 20131218
	@Autowired
	private BlackboardService blackboardService;
	@Autowired
	private GuideService guideService;
	
	/* DES加密默认静态秘钥 */
	private static final String DES_STATIC_KEY = "64074f968502295ca41b7db452c7c639";

	private static String RD_CERTIFY = "rdcertify";//身份证

	//非cas模式的要启用这个方法,cas的注释掉这个方法
	@RequestMapping("/login")
	public ModelAndView login(@ModelAttribute Reader reader) {
		if(reader.getRdCFState()==2){
			return new ModelAndView(READERINFO_VIEW);
		}else{
			return new ModelAndView(LOGIN_VIEW);
		}
		
	}

	@RequestMapping("/doLogin")
//	@RequestMapping("/login")
	public ModelAndView doLogin(HttpServletRequest request,
			HttpServletResponse response, @RequestParam(value="returnUrl",required=false) String returnUrl,
			@ModelAttribute Reader reader, HttpSession session, RedirectAttributes redirectAttributes) {
		//非cas启用
		ModelAndView mav = new ModelAndView("redirect:/portal/index");
		String r =request.getParameter("rdId");
	//		reader表已经失效的读者
		reader = readerService.getReader(r, (byte) 1);
			if(reader !=null){
				reader = readerService.getReader(r, (byte) 2);
				int cfstate = reader.getRdCFState();
				//判断证的状态
				if(cfstate == 2){
					mav = new ModelAndView("redirect:/portal/readerInfo?rdId="+r);				
					if(reader.getRdCertify() != null && reader.getRdLoginId() !=null &&
							reader.getRdEmail() !=null){
						reader.setRdCFState((byte) 1);
						readerService.updateReader(reader);
					}
				}else{
					mav = new ModelAndView("redirect:/portal/index");
				}	
			}else{
				//net_reader表中未审批的读者
				NetReader netReader = netReaderService.getNetReader(r);
				int checkState = netReader.getCheckState();
				byte readerCardState = netReader.getReaderCardState();
				//判断证的状态
				if(checkState  ==1 || readerCardState ==2){
					String[] idsArr = r.split(",");
					List<String> idList = new ArrayList<String>();
					int size = idsArr.length;
					for(int i=0; i<size; i++){
						idList.add(idsArr[i]);
					}
					netReaderService.approvePass(idList);
					System.out.println("网路读者转换到读者表..............");
					
					reader = readerService.getReader(r, (byte) 2);
					
					int cfstate = reader.getRdCFState();
					//判断证的状态
					if(cfstate == 2){
						mav = new ModelAndView("redirect:/portal/readerInfo?rdId="+r);
						if(reader.getRdCertify() != null && reader.getRdLoginId() !=null &&
								reader.getRdEmail() !=null){
							reader.setRdCFState((byte) 1);
							readerService.updateReader(reader);
						}
					}else{
						mav = new ModelAndView("redirect:/portal/index");
					}
				}
			}
//登录
	try {
			String userId = request.getParameter("rdId");
			String rdpasswd = request.getParameter("rdPasswd");			
			System.out.println("rdpasswd is :"+rdpasswd);
			if(userId == null || rdpasswd == null) {
				mav.setViewName("forward:login");
				return mav;
			}
			String rdpasswdDES = EncryptDecryptData.encryptWithCode(Constants.DES_STATIC_KEY, rdpasswd);
			AuthenticationToken a  = new UsernamePasswordToken(userId, rdpasswdDES.toCharArray());
			SecurityUtils.getSubject().login(a);
	} catch(UnknownAccountException ae) {
		redirectAttributes.addFlashAttribute("message", Cautions.READER_LOGIN_ERROR);
		return new ModelAndView("redirect:/portal/login");
	} catch(UnAcceptableLicenseException e) {
		redirectAttributes.addFlashAttribute("message", e.toString());
		return new ModelAndView("redirect:/portal/login");
	} catch (InvalidKeyException e) {
		e.printStackTrace();
	} catch (NoSuchAlgorithmException e) {
		e.printStackTrace();
	} catch (IllegalBlockSizeException e) {
		e.printStackTrace();
	} catch (BadPaddingException e) {
		e.printStackTrace();
	} catch (NoSuchPaddingException e) {
		e.printStackTrace();
	} catch (InvalidKeySpecException e) {
		e.printStackTrace();
	}
		return mav;
		//非cas结束
		
//		cas模式要启用下面的代码
//		if (SecurityUtils.getSubject().getSession() != null) {
//			SecurityUtils.getSubject().logout();
//		}			
//		String userId = request.getRemoteUser();
//		System.out.println(userId);
//		AttributePrincipal principal = (AttributePrincipal) request.getUserPrincipal();  
//        Map<String, Object> attributes = principal.getAttributes(); 
//        System.out.println("===============" + attributes.get("rdname"));
//		ModelAndView mav = new ModelAndView("redirect:/portal/index");
//		
//		try {
//			reader = readerService.getReader(userId, (byte)2);
//			
//			String rdpasswd = readerService.getRealPassword(userId);
//			
//			AuthenticationToken a  = new UsernamePasswordToken(userId, rdpasswd.toCharArray());
//			
//			SecurityUtils.getSubject().login(a);
//			
//		} catch(UnknownAccountException ae) {
//			mav.addObject("message", Cautions.READER_LOGIN_ERROR);
//			mav.setViewName("forward:/login");
//		} catch(UnAcceptableLicenseException ue) {
//			mav.addObject("message", ue.toString());
//			mav.setViewName("forward:/uncaught_error");
//			
//		}
//		
//		return mav;
	}
	/**
	 * 互联网读者注册页面//福建省图的要求
	 * @return
	 */
	@RequestMapping("/register")
	public ModelAndView register(HttpServletRequest request, HttpServletResponse response) {
		String userAgent = request.getHeader("user-agent");
		if (userAgent.indexOf("Android") != -1) {
			// 安卓
			return new ModelAndView(REGISTER_MOBLE_VIEW);

		} else if (userAgent.indexOf("iPhone") != -1 || userAgent.indexOf("iPad") != -1) {
			// 苹果
			return new ModelAndView(REGISTER_MOBLE_VIEW);

		} else {   // 电脑
			//逻辑处理
			return new ModelAndView(REGISTER_VIEW);

		}
	}
	

	/**
	 * 检查身份证号是否已经存在
	 * 
	 * @param response
	 * @param rdCertify
	 */
	@RequestMapping(value = "/checkRdCertifyExist")
	public void checkRdCertifyExist(HttpServletResponse response,String rdCertify) {
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

	/**
	 * 互联网读者注册返回结果页面//福建省图的要求
	 * @return
	 * @throws ParseException 
	 * @throws InvalidKeySpecException 
	 * @throws NoSuchPaddingException 
	 * @throws BadPaddingException 
	 * @throws IllegalBlockSizeException 
	 * @throws NoSuchAlgorithmException 
	 * @throws InvalidKeyException 
	 */
	@RequestMapping("/regresult")
	public ModelAndView regresult(HttpServletRequest request,
			HttpServletResponse response,
			@ModelAttribute Reader reader, HttpSession session){
		String userAgent = request.getHeader("user-agent");
		if (userAgent.indexOf("Android") != -1) {
			// 安卓
			String rdname =request.getParameter("rdname");
			String rdType =request.getParameter("rdType");
			String rdpasswd =request.getParameter("rdpasswd");
			String rdLoginId =request.getParameter("rdLoginId");
			String rdcertify =request.getParameter("rdcertify");
			String rdemail =request.getParameter("rdemail");
			String rdSort2 =request.getParameter("rdSort2");
			String rdSort5 =request.getParameter("rdSort5");
			ReaderType readerType = readerTypeService.getReaderType(reader
					.getRdType());
			if(readerType !=null){
			
			int valdate = readerType.getValdate(); // 有效期
			
//			String num = numService.getNum("9999").getNum();//存入的数量
//				
//				int nUm = numService.updateNum("9999");
//
//				DecimalFormat df=new DecimalFormat("0000000");
//			     String str2=df.format(Integer.parseInt(num));
//			     String rdId = "E9999"+str2;
			     
			   //小写转大写 toUpperCase  大写转小写toLowerCase 
				System.out.println("rdType.substring(0,1):"+rdType.substring(0,1).toUpperCase());
				System.out.println("rdType.substring(8):"+rdType.substring(8));
				String nuM =  numService.getNum(rdType.substring(8)).getNum();//存入的数量
				
				int nUm = numService.updateNum(rdType.substring(8));
//				System.out.println("--nuM为："+nuM+"--");
//				
//				System.out.println("--nUm为："+nUm+"--");
				 DecimalFormat df=new DecimalFormat("0000000");
			     String str2=df.format(Integer.parseInt(nuM));
//			     System.out.println("1243546767iu--------"+str2);
				String rdId = rdType.substring(0,1).toUpperCase()+rdType.substring(8)+str2;
//			     System.out.println("rdid::::::::--------"+rdId);

			     
			if(readerService.getReader(rdId, (byte) 1) == null
					&& readerService.checkRdCertifyExist(rdcertify)==0 &&
					readerService.getReaderByRdLoginId(rdLoginId) == null){ 
	    		
//	 			rdId = "E9999"+str2;
	 		
	 			reader.setRdId(rdId);
	 			reader.setRdCFState((byte) 1);
	 			reader.setRdGlobal((byte) 0);
	 			reader.setRdSort2(rdSort2);
	 			reader.setRdSort5(rdSort5);
	 			reader.setRdName(rdname);
	 			// 对密码des加密
	 			String desPassword;
	 			try {
	 				desPassword = EncryptDecryptData.encrypt(DES_STATIC_KEY,
	 						rdpasswd);
	 				reader.setRdPasswd("^" + desPassword + "^");
	 				reader.setOldRdpasswd("^" + desPassword + "^");// ADD 第一次添加数据时候旧密码和密码一致

	 				SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
	 				reader.setRdBornDate(formatter.parse(
	 						IdCard.getYearByIdCard(rdcertify)+"/"+
	 							IdCard.getMonthByIdCard(rdcertify)+"/"+
	 								IdCard.getDateByIdCard(rdcertify)));
	 			} catch (Exception e) {
	 				// TODO Auto-generated catch block
	 				e.printStackTrace();
	 			}
	 			
	 			reader.setRdCertify(rdcertify);
	 			reader.setRdSex(Byte.parseByte(IdCard.getGenderByIdCard(rdcertify)));
	 			
	 			reader.setRdLoginId(rdLoginId);

	 			reader.setRdEmail(rdemail);
	 			reader.setLibUser(0);
	 			reader.setRdEndDate(Common.getDateAfterDaysOtherWay(new Date(),
	 					valdate));
	 			reader.setSynStatus(0);
	 			if(reader.getRdStartDate()==null){//2014-11-10
	 				reader.setRdStartDate(new Date());
	 			}
	 			reader.setRdInTime(new Date());
	 			readerService.addReader(reader);
	  			System.out.println("读者证号为"+reader.getRdId());

	    	 }else{
	    		 request.setAttribute("message", "读者已经注册，请切换到登录页面");
	    		 return new ModelAndView(REGISTER_MOBLE_VIEW);
	    	 }
			request.setAttribute("rdId", rdId);
			request.setAttribute("rdName", rdname);
			return new ModelAndView(REGRESULT_MOBLE_VIEW);
		}else{
			request.setAttribute("message", "此读者类型不存在，请添加后再输入！");
			return new ModelAndView(REGISTER_MOBLE_VIEW);
		}
		} else if (userAgent.indexOf("iPhone") != -1 || userAgent.indexOf("iPad") != -1) {
			// 苹果
			String rdname =request.getParameter("rdname");
			String rdType =request.getParameter("rdType");
			String rdpasswd =request.getParameter("rdpasswd");
			String rdLoginId =request.getParameter("rdLoginId");
			String rdcertify =request.getParameter("rdcertify");
			String rdemail =request.getParameter("rdemail");
			String rdSort2 =request.getParameter("rdSort2");
			String rdSort5 =request.getParameter("rdSort5");
			ReaderType readerType = readerTypeService.getReaderType(reader
					.getRdType());
			if(readerType !=null){
			
			int valdate = readerType.getValdate(); // 有效期
			
//			String num = numService.getNum("9999").getNum();//存入的数量
//				
//				int nUm = numService.updateNum("9999");
//
//				DecimalFormat df=new DecimalFormat("0000000");
//			     String str2=df.format(Integer.parseInt(num));
//			     String rdId = "E9999"+str2;
			
			 //小写转大写 toUpperCase  大写转小写toLowerCase 
			System.out.println("rdType.substring(0,1):"+rdType.substring(0,1).toUpperCase());
			System.out.println("rdType.substring(8):"+rdType.substring(8));
			String nuM =  numService.getNum(rdType.substring(8)).getNum();//存入的数量
			
			int nUm = numService.updateNum(rdType.substring(8));
//			System.out.println("--nuM为："+nuM+"--");
//			
//			System.out.println("--nUm为："+nUm+"--");
			 DecimalFormat df=new DecimalFormat("0000000");
		     String str2=df.format(Integer.parseInt(nuM));
//		     System.out.println("1243546767iu--------"+str2);
			String rdId = rdType.substring(0,1).toUpperCase()+rdType.substring(8)+str2;
//		     System.out.println("rdid::::::::--------"+rdId);
			
			if(readerService.getReader(rdId, (byte) 1) == null
					&& readerService.checkRdCertifyExist(rdcertify)==0 &&
					readerService.getReaderByRdLoginId(rdLoginId) == null){ 
	    		
//	 			rdId = "E9999"+str2;
	 		
	 			reader.setRdId(rdId);
	 			reader.setRdCFState((byte) 1);
	 			reader.setRdGlobal((byte) 0);
	 			reader.setRdSort2(rdSort2);
	 			reader.setRdSort5(rdSort5);
	 			reader.setRdName(rdname);
	 			// 对密码des加密
	 			String desPassword;
	 			try {
	 				desPassword = EncryptDecryptData.encrypt(DES_STATIC_KEY,
	 						rdpasswd);
	 				reader.setRdPasswd("^" + desPassword + "^");
	 				reader.setOldRdpasswd("^" + desPassword + "^");// ADD 第一次添加数据时候旧密码和密码一致

	 				SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
	 				reader.setRdBornDate(formatter.parse(
	 						IdCard.getYearByIdCard(rdcertify)+"/"+
	 							IdCard.getMonthByIdCard(rdcertify)+"/"+
	 								IdCard.getDateByIdCard(rdcertify)));
	 			} catch (Exception e) {
	 				// TODO Auto-generated catch block
	 				e.printStackTrace();
	 			}
	 			
	 			reader.setRdCertify(rdcertify);
	 			reader.setRdSex(Byte.parseByte(IdCard.getGenderByIdCard(rdcertify)));
	 			
	 			reader.setRdLoginId(rdLoginId);

	 			reader.setRdEmail(rdemail);
	 			reader.setLibUser(0);
	 			reader.setRdEndDate(Common.getDateAfterDaysOtherWay(new Date(),
	 					valdate));
	 			reader.setSynStatus(0);
	 			if(reader.getRdStartDate()==null){//2014-11-10
	 				reader.setRdStartDate(new Date());
	 			}
	 			reader.setRdInTime(new Date());
	 			readerService.addReader(reader);
	  			System.out.println("读者证号为"+reader.getRdId());

	    	 }else{
	    		 request.setAttribute("message", "读者已经注册，请切换到登录页面");
	    		 return new ModelAndView(REGISTER_MOBLE_VIEW);
	    	 }
			request.setAttribute("rdId", rdId);
			request.setAttribute("rdName", rdname);
			return new ModelAndView(REGRESULT_MOBLE_VIEW);
		}else{
			request.setAttribute("message", "此读者类型不存在，请添加后再输入！");
			return new ModelAndView(REGISTER_MOBLE_VIEW);
		}
		} else {   // 电脑
			//逻辑处理

		String rdname =request.getParameter("rdname");
		String rdType =request.getParameter("rdType");
		String rdpasswd =request.getParameter("rdpasswd");
		String rdLoginId =request.getParameter("rdLoginId");
		String rdcertify =request.getParameter("rdcertify");
		String rdemail =request.getParameter("rdemail");
		String rdSort2 =request.getParameter("rdSort2");
		String rdSort5 =request.getParameter("rdSort5");
		ReaderType readerType = readerTypeService.getReaderType(reader
				.getRdType());
		if(readerType !=null){
		
		int valdate = readerType.getValdate(); // 有效期
		
//		String num = numService.getNum("9999").getNum();//存入的数量
//			
//			int nUm = numService.updateNum("9999");
//
//			DecimalFormat df=new DecimalFormat("0000000");
//		     String str2=df.format(Integer.parseInt(num));
//		     String rdId = "E9999"+str2;
		 //小写转大写 toUpperCase  大写转小写toLowerCase 
		System.out.println("rdType.substring(0,1):"+rdType.substring(0,1).toUpperCase());
		System.out.println("rdType.substring(8):"+rdType.substring(8));
		String nuM =  numService.getNum(rdType.substring(8)).getNum();//存入的数量
		
		int nUm = numService.updateNum(rdType.substring(8));
//		System.out.println("--nuM为："+nuM+"--");
//		
//		System.out.println("--nUm为："+nUm+"--");
		 DecimalFormat df=new DecimalFormat("0000000");
	     String str2=df.format(Integer.parseInt(nuM));
//	     System.out.println("1243546767iu--------"+str2);
		String rdId = rdType.substring(0,1).toUpperCase()+rdType.substring(8)+str2;
//	     System.out.println("rdid::::::::--------"+rdId);
		if(readerService.getReader(rdId, (byte) 1) == null
				&& readerService.checkRdCertifyExist(rdcertify)==0 &&
				readerService.getReaderByRdLoginId(rdLoginId) == null){ 
    		
// 			rdId = "E9999"+str2;
 		
 			reader.setRdId(rdId);
 			reader.setRdCFState((byte) 1);
 			reader.setRdGlobal((byte) 0);
 			reader.setRdSort2(rdSort2);
 			reader.setRdSort5(rdSort5);
 			reader.setRdName(rdname);
 			// 对密码des加密
 			String desPassword;
 			try {
 				desPassword = EncryptDecryptData.encrypt(DES_STATIC_KEY,
 						rdpasswd);
 				reader.setRdPasswd("^" + desPassword + "^");
 				reader.setOldRdpasswd("^" + desPassword + "^");// ADD 第一次添加数据时候旧密码和密码一致

 				SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
 				reader.setRdBornDate(formatter.parse(
 						IdCard.getYearByIdCard(rdcertify)+"/"+
 							IdCard.getMonthByIdCard(rdcertify)+"/"+
 								IdCard.getDateByIdCard(rdcertify)));
 			} catch (Exception e) {
 				// TODO Auto-generated catch block
 				e.printStackTrace();
 			}
 			
 			reader.setRdCertify(rdcertify);
 			reader.setRdSex(Byte.parseByte(IdCard.getGenderByIdCard(rdcertify)));
 			
 			reader.setRdLoginId(rdLoginId);

 			reader.setRdEmail(rdemail);
 			reader.setLibUser(0);
 			reader.setRdEndDate(Common.getDateAfterDaysOtherWay(new Date(),
 					valdate));
 			reader.setSynStatus(0);
 			if(reader.getRdStartDate()==null){//2014-11-10
 				reader.setRdStartDate(new Date());
 			}
 			reader.setRdInTime(new Date());
 			readerService.addReader(reader);
  			System.out.println("读者证号为"+reader.getRdId());

    	 }else{
    		 request.setAttribute("message", "读者已经注册，请切换到登录页面");
    		 return new ModelAndView(REGISTER_VIEW);
    	 }
		request.setAttribute("rdId", rdId);
		request.setAttribute("rdName", rdname);
		return new ModelAndView(REGRESULT_VIEW);
	}else{
		request.setAttribute("message", "此读者类型不存在，请添加后再输入！");
		return new ModelAndView(REGISTER_VIEW);
	}
  }
}
	/**
	 * 取回密码
	 * @return
	 */
	@RequestMapping("/retrievePassword")
	public ModelAndView retrievePassword() {
		return new ModelAndView(RETRIEVEPASSWORD_VIEW);
	} 
	
	/**
	 * 补全信息页面//福建省图的要求
	 * @return
	 */
	@RequestMapping("/readerInfo")
	public ModelAndView readerInfo(String rdId,HttpServletRequest request) {
		//根据userAgent判断手机访问还是电脑
		String userAgent = request.getHeader("user-agent");
		if (userAgent.indexOf("Android") != -1) {
			// 安卓
//			String defaultFailureUrl = "/login_moblie.jsp";
//			System.out.println("Android访问！！！" + "没有登录,返回的页面===" +defaultFailureUrl);
			ModelAndView mav = new ModelAndView(READERINFO_VIEW_MOBLE);
			mav.addObject("rdId", rdId);
			Reader reader = readerService.getReader(rdId, (byte)2);
			if(reader ==null){
				NetReader netreader = netReaderService.getNetReader(rdId);
				mav.getModel().put("netreader", netreader);
			}
			mav.getModel().put("reader", reader);
			return mav;
		} else if (userAgent.indexOf("iPhone") != -1 || userAgent.indexOf("iPad") != -1) {
			// 苹果
			ModelAndView mav = new ModelAndView(READERINFO_VIEW_MOBLE);
			mav.addObject("rdId", rdId);
			Reader reader = readerService.getReader(rdId, (byte)2);
			if(reader ==null){
				NetReader netreader = netReaderService.getNetReader(rdId);
				mav.getModel().put("netreader", netreader);
			}
			mav.getModel().put("reader", reader);
			return mav;
		} else {   // 电脑
			//逻辑处理
			ModelAndView mav = new ModelAndView(READERINFO_VIEW);
			mav.addObject("rdId", rdId);
			Reader reader = readerService.getReader(rdId, (byte)2);
			if(reader ==null){
				NetReader netreader = netReaderService.getNetReader(rdId);
				mav.getModel().put("netreader", netreader);
			}
			mav.getModel().put("reader", reader);
			return mav;
		}
		
	}
	/**
	 * 同意协议页面//福建省图的要求
	 * @return
	 */
	@RequestMapping("/agreement")
	public ModelAndView agreement() {
		return new ModelAndView(AGREEMENT_VIEW);
	}
	/**
	 * 补全信息跳转中间
	 */
	//福建省图的要求
	@RequestMapping("/onecardtointerlibSSO")
	public ModelAndView onecardtointerlibSSO() {
		return new ModelAndView("onecardtointerlibSSO");
	}
	/**
	 * 补全信息之后跳转页面////福建省图的要求
	 * @param request
	 * @param rdId
	 * @param returnUrl
	 * @param reader
	 * @param session
	 * @param redirectAttributes
	 * @return
	 * @throws ParseException
	 */
	@RequestMapping("/doindex")
	public ModelAndView doindex(HttpServletRequest request,String rdId, @RequestParam(value="returnUrl",required=false) String returnUrl,
			@ModelAttribute Reader reader, HttpSession session, RedirectAttributes redirectAttributes) 
					throws ParseException {
//		getBlackAndGuideInfo();
//		getReaderWebServiceUrl();//查询对应的读者的URL
//		ReaderSession rs = (ReaderSession)SecurityUtils.getSubject().getSession().getAttribute("READER_SESSION");
//		String r =rs.getReader().getRdId();
//		
//		
//		
//		System.out.println("r:"+r);
//		Reader reader1 = readerService.getReader(r, (byte)2);

		rdId = request.getParameter("rdId");
		
		
		Reader reader1 = readerService.getReader(rdId, (byte)2);
		if(reader1 == null){
//			netReaderService.getNetReader(rdId);
			String[] idsArr = rdId.split(",");
			List<String> idList = new ArrayList<String>();
			int size = idsArr.length;
			for(int i=0; i<size; i++){
				idList.add(idsArr[i]);
			}
			netReaderService.approvePass(idList);
			System.out.println("网路读者转换到读者表..............");
			 reader1 = readerService.getReader(rdId, (byte)2);

		}
		System.out.println("reader1为："+reader1);
		String rdName =request.getParameter("rdName");
		String rdLoginId =request.getParameter("rdLoginId");
		System.out.println("rdName为："+rdName);

		reader1.setRdName(rdName);
		reader1.setRdLoginId(rdLoginId);
		System.out.println("rdLoginId:"+rdLoginId);
		
		String rdcertify = request.getParameter("rdCertify");

		reader1.setRdCertify(rdcertify);
		reader1.setRdEmail(request.getParameter("rdEmail"));
		reader1.setRdSex(Byte.parseByte(IdCard.getGenderByIdCard(rdcertify)));
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
		reader1.setRdBornDate(formatter.parse(
				IdCard.getYearByIdCard(rdcertify)+"/"+
					IdCard.getMonthByIdCard(rdcertify)+"/"+
						IdCard.getDateByIdCard(rdcertify)));
		reader1.setRdSort2(request.getParameter("rdSort2"));
		reader1.setRdSort5(request.getParameter("rdSort5"));
		
		reader1.setRdCFState((byte) 1);

		
		readerService.updateReader(reader1);
		System.out.println("信息更新成功..............");
//		List<Map<String, String>> rdLibType = readerService.getLibReaderType(reader
//				.getRdLibType());
//		for (Map<String, String> m : rdLibType)
//	    {
//	      for (String k : m.keySet())
//	      {
//	        System.out.println(k + " : " + m.get(k));
//	        if(m.get(k) !=null){
//	    		
//				return new ModelAndView(INDEX_VIEW);
//			}
//	      }
//
//	    }
		
		return new ModelAndView("onecardtointerlibSSO");
	}
	
	/**
	 * 公告和办事指南的信息到前台显示
	 * @param response
	 */
	@RequestMapping("/getBlackAndGuideInfo")
	public void getBlackAndGuideInfo(){
		BlackboardAndGuideSession  blackboardAndGuideSession=new BlackboardAndGuideSession();
		List<Blackboard> blackboardList=blackboardService.getBlackboardsByLastSomeone();
		List<Guide> guideList=guideService.getGuidesByLastSomeone();
		
		blackboardAndGuideSession.setBlackboardList(blackboardList);
		blackboardAndGuideSession.setGuideList(guideList);
		SecurityUtils.getSubject().getSession().setAttribute("BlackboardAndGuideSession", blackboardAndGuideSession);
	}
	@RequiresRoles("admin")
	@RequestMapping("/getMyResources")
	public void getMyResources(HttpServletResponse response) {
		ReaderSession rs = (ReaderSession)SecurityUtils.getSubject().getSession().getAttribute("READER_SESSION");
		List<Resources> list = rs.getResourceList();
		StringBuffer sb = new StringBuffer();
		sb.append("[");
		for(Resources res : list) {
			if(res.getIsMenu() == 1) {
				sb.append(Jackson.getBaseJsonData(res));
				sb.append(",");
			}
		}
		StringUtils.removeEndChar(sb, ",");
		sb.append("]");
		ServletUtils.printHTML(response, sb.toString());
		return;
	}
	
	@RequestMapping("/getPubResource")
	public void getPubResource(HttpServletResponse response) {
		List<FinApp> list = finAppService.getAllSimple();
		
		StringBuffer sb = new StringBuffer();
		sb.append("[");
		for(FinApp res : list) {
				sb.append(Jackson.getBaseJsonData(res));
				sb.append(",");
		}
		StringUtils.removeEndChar(sb, ",");
		sb.append("]");
		ServletUtils.printHTML(response, sb.toString());
		return;
	}
	
	
	@RequestMapping("/index")
	public ModelAndView index(HttpServletRequest request, Model model) {
		getBlackAndGuideInfo();
		getReaderWebServiceUrl();//查询对应的读者的URL

		return new ModelAndView(INDEX_VIEW);	
	}
	/**
	 * 读者切换使用
	 * @param request
	 * @param model
	 * @return
	 */
//	@RequestMapping("/indexChange")
//	public ModelAndView indexChange(HttpServletRequest request, Model model) {
//		getBlackAndGuideInfo();
//		getReaderWebServiceUrl();//查询对应的读者的URL
//
//		ReaderSession rs = (ReaderSession)SecurityUtils.getSubject().getSession().getAttribute("READER_SESSION");
//		Reader r = rs.getReader();
//
//		List<Reader> list = readerService.getReaderListByRdCertifyMore(r.getRdCertify());
//		model.addAttribute("list",list);
//		//登录
//		try {
//			String UserId = request.getParameter("select");
//			String rdpasswd = readerService.getReader(UserId, (byte)2)
//					.getRdPasswd();
//			if(UserId == null || rdpasswd == null) {
//				ModelAndView mav = new ModelAndView();
//				mav.setViewName("forward:login");
//				return mav;
//			}
//			String rdpasswdDES = EncryptDecryptData.decryptWithCode(Constants.DES_STATIC_KEY, rdpasswd);						
//			AuthenticationToken a = new UsernamePasswordToken(UserId, EncryptDecryptData.encryptWithCode(Constants.DES_STATIC_KEY, rdpasswdDES));									
//
//			SecurityUtils.getSubject().login(a);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return new ModelAndView(INDEX_VIEW);	
//	}
	
	
	/**
	2015-04-19
	 * 根据读者rdid 查询读者对应所属馆的webServiceURL地址信息
	 * 作用：用于自动跳转webServiceURL的地址信息
	 */
	private void getReaderWebServiceUrl() {
		ReaderSession rs = (ReaderSession)SecurityUtils.getSubject().getSession().getAttribute("READER_SESSION");
		Reader r = rs.getReader();
		String webServiceUrl = "";
		String rdpasswdMD5 = "";//MD5加密之后的读者密码
		if(r != null){
			String rdid = r.getRdId();
			String rdpasswd = r.getRdPasswd();
			rdpasswd = EncryptDecryptData.decryptWithCode(DES_STATIC_KEY, rdpasswd);
			rdpasswdMD5 = MD5Util.MD5Encode(rdpasswd, "UTF-8");
			if( rdid != null && !"".equals(rdid)){
				webServiceUrl = readerService.getReaderWebServiceUrl(rdid);
			}
		}
		SecurityUtils.getSubject().getSession().setAttribute("WebServiceURLSession", webServiceUrl);
		SecurityUtils.getSubject().getSession().setAttribute("RdpasswdMD5Session", rdpasswdMD5);
	}

	@RequestMapping("/logout")
	public void logout(HttpServletResponse response, 
			@RequestParam(value="returnUrl",required=false) String returnUrl){
		SecurityUtils.getSubject().logout();
		
		ServletUtils.printHTML(response, "ok");
		
//		HttpSession session = request.getSession();
//		//session存在先清除
//		if(session !=null){
//			session.invalidate();
//		}
		//重定向到cas 退出
//        Properties p = PropertyUtils.getProperties("sysConfig");	        
//        String casUrl = p.getProperty("casUrl");
//        
//        return "redirect:"+casUrl+"/logout?service=";
	}

	
	@RequestMapping("/resetPassword")
	public void resetPassword(HttpServletRequest request,
			HttpServletResponse response) throws InvalidKeyException,
			IllegalBlockSizeException, BadPaddingException,
			NoSuchAlgorithmException, NoSuchPaddingException,
			InvalidKeySpecException {
		String rdId = request.getParameter("rdId");
		String oldPassword = "";
		oldPassword = request.getParameter("oldPassword");//
		if (oldPassword == null || "".equals(oldPassword)) {// 页面没有传递原密码
			// 去掉页面输入原密码，从数据库获取
			String oldPasswordDB = readerService.getReader(rdId, (byte) 2)
					.getRdPasswd();// request.getParameter("oldPassword");
			oldPassword = EncryptDecryptData.decrypt(DES_STATIC_KEY,
					oldPasswordDB.substring(1, oldPasswordDB.length() - 1));
		}
		String newPassword = request.getParameter("newPassword");
		String realPassword = readerService.getRealPassword(rdId);
		int result;
		Map synmap = null;
		if (realPassword == null || "".equals(realPassword)) {
			try {
				String encryptPassword = EncryptDecryptData.encryptWithCode(DES_STATIC_KEY, newPassword);
				result = readerService.updatePassword(rdId, encryptPassword);

				if (result == 1) {// 只有修改成功才进行同步操作//ADD 添加同步到opac端
					synmap = UpdateOpacReaderPasswd(rdId, oldPassword, newPassword);
				}

			} catch (Exception e) {
				result = -1;
				logger.error("【修改密码】新密码加密出错！");
			}
		} else {
			try {
				String decryptPassword = EncryptDecryptData.decryptWithCode(DES_STATIC_KEY, realPassword);
				if (oldPassword.equals(decryptPassword)) {
					String encryptPassword = EncryptDecryptData.encryptWithCode(DES_STATIC_KEY, newPassword);
					result = readerService.updatePassword(rdId, encryptPassword);

					if (result == 1) {// 只有修改成功才进行同步操作 ADD 添加同步到opac端
						synmap = UpdateOpacReaderPasswd(rdId, oldPassword,
								newPassword);
					}
				} else {
					result = -1;
				}
			} catch (Exception e) {
				if (oldPassword.equals(realPassword)) {
					try {
						String encryptPassword = EncryptDecryptData.encryptWithCode(DES_STATIC_KEY, newPassword);
						result = readerService.updatePassword(rdId, encryptPassword);
						if (result == 1) {// 只有修改成功才进行同步操作//ADD 添加同步到opac端
											// 同步成功同时修改reader旧密码和标识
							synmap = UpdateOpacReaderPasswd(rdId, oldPassword, newPassword);
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
		if (synmap != null) {
			int rsyn = (Integer) synmap.get("success");
			if (result == 1 && rsyn == 1) {
				result = 1;// 修改成功，同步成功
			} else if (result == 1 && rsyn != 1) {
				result = 0;// 修改成功，同步失败
			}
		}
		ServletUtil.responseOut("GBK", String.valueOf(result), response);
	}
	
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
	 * 用户密码找回
	 * @param request
	 * @param userName
	 * @return
	 */
	@RequestMapping(value = "/doRetrievePassword")
	@ResponseBody
	public Map doRetrievePassword(HttpServletRequest request,HttpServletResponse response,String rdId){
	    
		Map map = new HashMap<String ,String >();
	    String msg = "";
	    
		String method = request.getMethod();
		 System.out.println("method:----"+method);
		if((method.equalsIgnoreCase("get")||method.equalsIgnoreCase("post")
			||method.equalsIgnoreCase("head")||method.equalsIgnoreCase("trace")
				||method.equalsIgnoreCase("connect")||method.equalsIgnoreCase("options"))){

		String rdId1 = request.getParameter("rdId");
		System.out.println("rdId1为:"+rdId1);
		Reader readers = readerService.getReader(rdId1, (byte) 2);
		System.out.println("readers为:"+readers);

	    if(readers == null){       //用户名不存在
	      msg = "读者证号不存在,你不会忘记了吧?";
	      map.put("msg",msg);
	      return map;
	    }
	    try{
	      String secretKey= UUID.randomUUID().toString(); //密钥
	      Timestamp outDate = new Timestamp(System.currentTimeMillis()+30*60*1000);//30分钟后过期
	      long date = outDate.getTime()/1000*1000;         //忽略毫秒数
	      readers.setRetrievePasswordkey(secretKey);
	      readers.setRetrievePasswordDate(outDate);
	      System.out.println("retrievePasswordKey为:"+readers.getRetrievePasswordkey());
	      System.out.println("RetrievePasswordDate为:"+readers.getRetrievePasswordDate());

	      readerService.updateReader(readers);//保存到数据库
//	      update(readers);  
	      System.out.println("最新readers.getRetrievePasswordKey为:"+readers.getRetrievePasswordkey());
	      System.out.println("最新readers.getRetrievePasswordDate为:"+readers.getRetrievePasswordDate());

	      String key = readers.getRdId()+"$"+date+"$"+secretKey;
	      String digitalSignature = MD5Util.MD5Encode(key);         //数字签名

	      String emailTitle = "统一用户密码找回";
	      String path = request.getContextPath();
	      String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
	      String resetPassHref = basePath+"portal/reSetPassword?sid="+digitalSignature+"&rdId="+readers.getRdId();
	      String emailContent = "<div style='width:640px; background:#fff; border:solid 1px #efefef; margin:0 auto; padding:35px 0 35px 0'>"+
	    		  " <table width='560' border='0' align='center' cellpadding='0' cellspacing='0' style=' margin:0 auto; margin-left:30px; margin-right:30px;'>"+
	    		  "    <tbody><tr>"+
	    		  " <td><img src=''></td>"+
	    		  "    </tr>"+
	    		  "    <tr>"+
	    		  "      <td>"+
	    		  "      <h3 style='font-weight:normal; font-size:18px'>您好！亲爱的<span style='font-weight:bold; margin-left:5px;'>"+readers.getRdId()+"</span></h3>"+
	    		  "      <p style='margin:5px 0; padding:3px 0;color:#666; font-size:14px'>统一用户找回登录密码通知:</p>"+
	    		  "      <p style='color:#666; font-size:14px'>请在30分钟内点击下面链接找回您的登录密码： "+
	    		  "      <a href='"+resetPassHref+"' target='_blank' style='display:block; margin-top:10px; color:#2980b9; line-height:24px; text-decoration:none;word-break:break-all; width:575px;'>"+
	    		  "  "+resetPassHref+" </a>"+
	    		  "      </p>"+
	    		  "      <p style='margin:10px 0 5px 0; padding:3px 0;color:#666; font-size:14px'>如果上面不是链接形式，请将地址复制到您的浏览器（例如：IE）的地址栏再访问。</p>"+
	    		  "      <p style='margin:5px 0; padding:3px 0;color:#666; font-size:14px'>如果链接已经失效，请重新到统一用户登录网站找回您的密码！谢谢您的合作<br></p>"+
	    		  "      <p style='text-align:center'><img src=''></p>"+
	    		  "      </td>"+
	    		  "    </tr>"+
	    		  "    </tbody>"+
	    		  " </table>"+
	    		  "</div>";
	      System.out.print(resetPassHref);
//	      SendMail.getInstance().
	      sendHtmlMail(emailTitle,emailContent,readers.getRdEmail());
	      msg = "操作成功,已经发送找回密码链接到您邮箱。请在30分钟内重置密码";
//	      logInfo(request,userName,"申请找回密码");
	    }catch (Exception e){
	      e.printStackTrace();
	      msg="邮箱不存在？未知错误,联系管理员吧。";
	    }
		    map.put("msg",msg);
		    return map;
	}else{
	    	response.setContentType("text/html;charset=GBK");  
	    	response.setCharacterEncoding("GBK");  
	    	response.setStatus(403);
		
	      msg = "<font size=6 color=red>对不起，您的请求非法，系统拒绝响应!</font>";
	      map.put("msg",msg);
	      return map;
	}
}
	
	
	public void sendHtmlMail(String emailTitle, String emailContent,
			String rdEmail) {
		// TODO Auto-generated method stub
		try {

			// 接收者的邮箱
	        String to = rdEmail;

	        // 配置发送邮箱的配置--
	        Properties p = PropertyUtils.getProperties("sendEmail");	        
	        String username = p.getProperty("username");
	        String password = p.getProperty("password");
	        String host = p.getProperty("mail.smtp.host");
	        String protocol = p.getProperty("mail.transport.protocol");
	        String port = p.getProperty("mail.smtp.port");
	        Authenticator authenticator = null;
			// 建立会话
	        Session session = Session.getInstance(p, authenticator);
	        		//Session.getInstance(p);
	        // 建立信息
	        Message msg = new MimeMessage(session);
	        // 发件人
	        msg.setFrom(new InternetAddress(username));
	        // 收件人
//	        String toList = getMailList(to);
//	        InternetAddress[] iaToList =InternetAddress.parse(toList);
//	        msg.setRecipients(Message.RecipientType.CC,InternetAddress.parse(p.getProperty("username")));
	        msg.setRecipients(Message.RecipientType.TO,InternetAddress.parse(to));
	        // 发送日期
	        msg.setSentDate(new Date());
	        // 主题
	        msg.setSubject(emailTitle);
	        // 内容
		      
	        msg.setText(emailContent);
	        // 邮件服务器进行验证
	        Transport tran = session.getTransport(protocol);
	        // *配置发送者的邮箱账户名和密码
	        tran.connect(host, username,password);
	        // 发送
	        tran.sendMessage(msg, msg.getAllRecipients());
	        System.out.println("邮件发送成功");

	    } catch (Exception e) {
	        e.printStackTrace();
	    }
    }
	
	/**
	 * 以下为链接检验代码，验证通过 跳转到修改密码界面,否则跳转到失败界面
	 * @param sid
	 * @param rdId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/reSetPassword",method = RequestMethod.GET)
	  public ModelAndView checkResetLink(
			  @RequestParam("sid") String sid,@RequestParam("rdId") String rdId){
	    ModelAndView model = new ModelAndView(DORETRIEVEPASSWORD_VIEW);
	    String msg = "";
	    if(sid.equals("") || rdId.equals("")){
	      msg="链接不完整,请重新生成";
	      model.addObject("msg",msg) ;
//	      logInfo(userName,"找回密码链接失效");
	      return model;
	    }
	    Reader readers = readerService.getReader(rdId, (byte) 2);
	    if(readers == null){
	      msg = "链接错误,无法找到匹配用户,请重新申请找回密码.";
	      model.addObject("msg",msg) ;
//	      logInfo(readers,"找回密码链接失效");
	      return model;
	    }
	    Timestamp outDate = new Timestamp(System.currentTimeMillis()+30*60*1000);//30分钟后过期
	    long date = outDate.getTime()/1000*1000;         //忽略毫秒数	
	    readers.setRetrievePasswordDate(outDate);
	    System.out.println("date为"+date);
	    if(date <= System.currentTimeMillis()){     //表示已经过期
	      msg = "链接已经过期,请重新申请找回密码.";
	      model.addObject("msg",msg) ;
//	      logInfo(rdId,"找回密码链接失效");
	      return model;
	    }
	    String key = readers.getRdId()+"$"+date+"$"+readers.getRetrievePasswordDate();     //数字签名
	    String digitalSignature = MD5Util.MD5Encode(key);
	    System.out.println(key+"\t"+digitalSignature);
	    if(!digitalSignature.equals(sid)) {
	      msg = "链接不正确,是否已经过期了?重新申请吧";
	      model.addObject("msg",msg) ;
//	      logInfo(rdId,"找回密码链接失效");
	      return model;
	    }
	    model.setViewName(LOGIN_VIEW); //返回到修改密码的界面
	    model.addObject("rdId",rdId);
	    return model;
	  }
	@RequestMapping(value = "/selectPassword",method = RequestMethod.GET)
	  public ModelAndView selectPassword(HttpServletRequest request,
				HttpServletResponse response) throws InvalidKeyException, 
				IllegalBlockSizeException, BadPaddingException, 
				NoSuchAlgorithmException, NoSuchPaddingException, 
				InvalidKeySpecException {
	    ModelAndView model = new ModelAndView("oldPassword");
//
//			String rdId = request.getParameter("rdId");
//			String oldPasswordDB = readerService.getReader(rdId, (byte) 2)
//						.getRdPasswd();// request.getParameter("oldPassword");
//			String oldPassword = EncryptDecryptData.decrypt(DES_STATIC_KEY,
//						oldPasswordDB.substring(1, oldPasswordDB.length() - 1));
//			System.out.println("你的旧密码为"+oldPassword);
//			
//			request.getSession().setAttribute("oldPassword",oldPassword);
//		
		String rdId = request.getParameter("rdId");
		// 去掉页面输入原密码，从数据库获取
		String oldPasswordDB = readerService.getReader(rdId, (byte) 2)
				.getRdPasswd();// request.getParameter("oldPassword");
		String oldPassword = EncryptDecryptData.decrypt(DES_STATIC_KEY,
				oldPasswordDB.substring(1, oldPasswordDB.length() - 1));
		String newPassword = request.getParameter("newPassword");
		String realPassword = readerService.getRealPassword(rdId);
		
		String decryptPassword = EncryptDecryptData.decryptWithCode(DES_STATIC_KEY, realPassword);
		if (oldPassword.equals(decryptPassword)) {
			String encryptPassword = EncryptDecryptData.encryptWithCode(DES_STATIC_KEY, newPassword);
			 readerService.updatePassword(rdId, encryptPassword);
			
		} else if (oldPassword.equals(realPassword)) {
			String encryPassword = EncryptDecryptData.encryptWithCode(DES_STATIC_KEY, newPassword);
			readerService.updatePassword(rdId, encryPassword);			
		}
		request.getSession().setAttribute("newPassword",newPassword);
		return model;
	}
	
	/**
	 * 发送手机验证码  福建省图虚拟读者注册使用
	 * @param request
	 * @param response
	 * @throws MalformedURLException
	 */
	@RequestMapping(value = "/sendValidate")
	public @ResponseBody Map<String, String> sendValidate(HttpServletRequest request,
			HttpServletResponse response,final HttpSession session){
		boolean flag = false;
		String vcode ="";

		Map<String, String> map = new HashMap();
		
	    String rdLoginId = request.getParameter("phone");
 	    logger.info("短信执行前。。。。。。。。。。");

	    List<Reader> reader = readerService.getReaderListByRdLoginId(rdLoginId);
	    
	    for(int i = 0;i < reader.size(); i ++){
	    	   System.out.println(reader.get(i));

	    	 if(reader.get(i) != null){
//	 	    	String  msg = "读者手机号已经注册，请换手机号重试";
	 	    	map.put("msg", "读者手机号已经注册，请换手机号重试");
	 		    return map;
	 	    }
	    	
	    }
	   
	    

	    System.out.println("手机号是："+rdLoginId);
        logger.info("手机号是："+rdLoginId); 

	    //生成验证码 
	    for (int i = 0; i < 6; i++) {
	    	 vcode = vcode + (int) (Math.random() * 9);
	    
	    }
        logger.info("验证码是："+vcode); 
		// 配置发送短信的配置--
        Properties p = PropertyUtils.getProperties("sysConfig");
        
        String url= p.getProperty("sms.url");
        logger.info("url是："+url); 

        String user= p.getProperty("sms.user");
        logger.info("user是："+user); 

        String password= p.getProperty("sms.password");
        logger.info("password是："+password); 


        /** 网络的url地址 */        
        URL url1 = null;
        logger.info("我在url1这里"); 

           /** http连接 */    
        HttpURLConnection httpConn = null;
        logger.info("我在httpConn这里"); 

            /**//** 输入流 */   
        BufferedReader in = null;
        logger.info("我在in这里"); 

        StringBuffer sb = new StringBuffer();   
        logger.info("我在sb这里"); 

        try{     


	    String strURL = url+"?smsUser="+user+"&smsPassword="+password+"&phone="+rdLoginId
        		+"&content=" +URLEncoder.encode("[统一用户]虚拟读者注册验证码"+vcode
		        				 +"，60秒之内有效，请不要把验证码泄露给其他人。如非本人操作，请不要理会。", "UTF-8")+"&sendFirst=1";
        logger.info(strURL); 

         
        url1 = new URL(strURL);     
         in = new BufferedReader( new InputStreamReader(url1.openStream(),"UTF-8") );   
         String str = null;    
         while((str = in.readLine()) != null) {    
          sb.append( str );
	        logger.info("我在sb.append( str );这里"); 

                }     
            } catch (Exception ex) {   
                
            } finally{    
             try{             
              if(in!=null) {  
               in.close();
		        logger.info("我在in.close();这里"); 

                    }     
                }catch(IOException ex) {      
                }     
            }     
            String result =sb.toString();     
            System.out.println(result);  
            logger.info(result);
      
		if(session !=null){
			session.setAttribute("vcode", vcode); // 保存验证码到session里面
		}else{
			logger.error("session没保存 你看着办 -----------");
		}
		//TimerTask实现1分钟后从session中删除checkCode
		final Timer timer=new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				session.removeAttribute("vcode");
				System.out.println("checkCode删除成功");
				timer.cancel();
				logger.info("checkCode删除成功");
			}
		},1*60*1000);
		logger.info("线程关闭。。。");
   		
		logger.info("我在这里，没有报错-----------");  
	    
//		asyncTask at = new asyncTask(request, response,session);
//	    Thread r = new Thread(at);
//	    r.start();
	    logger.info("短信执行后。。。。。。。。。。");
    	map.put("success","success");
	    return map;
	}
	
	   /** 
     * 验证短信验证码是否正确 
     *  
     * @throws Exception 
     */  
	@RequestMapping(value = "/checkValidate")
    public void checkCode(HttpServletRequest request,HttpServletResponse response) throws Exception{  
        String result = "0";  
        /** 获取手动输入的手机短信验证码 */  
        String SmsCheckCode = request.getParameter("checkCode"); 
        System.out.println("手动输入的验证码是"+SmsCheckCode);
		/** 获取session中存放的手机短信验证码 */  
        String code = (String) request.getSession().getAttribute("vcode"); 
        System.out.println("session中的验证码是"+code);
        try {  
            if(SmsCheckCode != code && !SmsCheckCode.equals(code)){  
                result = "0";  
            }else{  
                result = "1";  
            }  
        } catch (Exception e) {  
            throw new RuntimeException("短信验证失败", e);  
        }  
        response.setContentType("application/json;charset=UTF-8");  
        response.setHeader("Cache-Control", "no-cache");  
        PrintWriter out = response.getWriter();  
        out.write(result.toString()); 
	
    }
	
}
 