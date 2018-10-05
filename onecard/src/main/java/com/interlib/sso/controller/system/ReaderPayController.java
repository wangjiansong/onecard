package com.interlib.sso.controller.system;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.interlib.opac.webservice.Finance;
import com.interlib.opac.webservice.FinanceWebservice;
import com.interlib.sso.common.Cautions;
import com.interlib.sso.common.Common;
import com.interlib.sso.common.Constants;
import com.interlib.sso.common.EncryptDecryptData;
import com.interlib.sso.common.MD5Util;
import com.interlib.sso.common.ServletUtils;
import com.interlib.sso.common.TimeUtils;
import com.interlib.sso.domain.CirFinLog;
import com.interlib.sso.domain.LibCode;
import com.interlib.sso.domain.LogCir;
import com.interlib.sso.domain.RdAccount;
import com.interlib.sso.domain.Reader;
import com.interlib.sso.domain.ReaderCardInfo;
import com.interlib.sso.domain.ReaderSession;
import com.interlib.sso.service.CirFinLogService;
import com.interlib.sso.service.LibCodeService;
import com.interlib.sso.service.LogCirService;
import com.interlib.sso.service.RdAccountService;
import com.interlib.sso.service.ReaderCardInfoService;
import com.interlib.sso.service.ReaderService;

/**
 * 处理读者欠款
 * @author dehong
 *
 */
@Controller
@RequestMapping("admin/sys/readerPay")
public class ReaderPayController {

	private static final String DETAIL_VIEW = "admin/sys/readerPay/detail";
	
	@Autowired
	public ReaderService readerService;
	
	@Autowired
	public LibCodeService libCodeService;
	
	@Autowired
	public RdAccountService rdAccountService;
	
	@Autowired
	public CirFinLogService cirFinLogService;
	
	@Autowired
	public LogCirService logCirService;
	
	@Autowired
	public ReaderCardInfoService readerCardInfoService;
	
	@RequiresRoles("admin")
	@RequestMapping("/detail/{fieldName}/{fieldValue}")
	public String readerPayDetail(Model model, @PathVariable String fieldName, @PathVariable String fieldValue) {
		model.addAttribute(Constants.ACTIVE_MENU_KEY, "reader_pay");
		String rdId = "";
		if(fieldName.equals("rdId")) {
			rdId = fieldValue;
		} else if(fieldName.equals("cardNo")) {
			ReaderCardInfo cardInfo = readerCardInfoService.get(fieldValue);
			if(cardInfo != null) {
				rdId = cardInfo.getRdId();
			}
		}
		
		Reader reader = readerService.getReader(rdId, (byte)2);
		if(reader == null) {
			model.addAttribute("message", Cautions.LOCALREADER_NOTEXIT);
			return DETAIL_VIEW;
		}
		RdAccount rdAccount = rdAccountService.get(reader.getRdId());
		
		LibCode lib = libCodeService.getLibByCode(reader.getRdLibCode());
		
		List<Finance> financeList = readerService.getReaderFinance(reader, lib, "0", false, 1, 10);
		
		Double allCost = 0.0;
		if(financeList != null) {
			for(Finance fin : financeList) {
				allCost += fin.getCost();
			}
		}
		model.addAttribute("financeList", financeList);
		model.addAttribute("rdAccount", rdAccount);
		model.addAttribute("reader", reader);
		model.addAttribute("allCost", allCost);
		return DETAIL_VIEW;
	}
	@RequiresRoles("admin")
	@RequestMapping("/detail/{rdId}")
	public String readerPayDetail(Model model, @PathVariable String rdId) {
		
		Reader reader = readerService.getReader(rdId, (byte)2);
		
		RdAccount rdAccount = rdAccountService.get(reader.getRdId());
		
		LibCode lib = libCodeService.getLibByCode(reader.getRdLibCode());
		
		List<Finance> financeList = readerService.getReaderFinance(reader, lib, "0", false, 1, 10);
		
		Double allCost = 0.0;
		if(financeList != null)
		for(Finance fin : financeList) {
			allCost += fin.getCost();
		}
		model.addAttribute("financeList", financeList);
		model.addAttribute("rdAccount", rdAccount);
		model.addAttribute("reader", reader);
		model.addAttribute("allCost", allCost);
		model.addAttribute(Constants.ACTIVE_MENU_KEY, "reader_add");
		return DETAIL_VIEW;
	}
	@RequiresRoles("admin")
	@RequestMapping("/detail")
	public ModelAndView readerPayQuery(Model model, HttpServletRequest request) {
		model.addAttribute(Constants.ACTIVE_MENU_KEY, "reader_pay");
		return new ModelAndView("admin/sys/readerPay/detail");
	}
	
	/**
	 * 支付欠款
	 * @param request
	 * @param response
	 */
	@RequiresRoles("admin")
	@RequestMapping("/payFee")
	public void payFee(HttpServletRequest request, HttpServletResponse response) {
		
		String rdId = request.getParameter("rdId");
		String tranids = request.getParameter("tranids");//流水号
		String paytype = request.getParameter("paytype"); //收纸币还是一卡通扣费
		String totalfee = request.getParameter("totalfee");
		double totalfeeD = Double.parseDouble(totalfee);
		
		ReaderSession readerSession = (ReaderSession)SecurityUtils.getSubject().getSession().getAttribute("READER_SESSION");
		String regman = readerSession.getReader().getRdId();
		String reglib = readerSession.getReader().getRdLib();
		
		List<String> trainList = null;
		if(tranids != null && tranids.length() > 0) {
			trainList = new ArrayList<String>();
			String [] tranidArray = tranids.split(";");
			for(String trainid : tranidArray) {
				trainList.add(trainid);
			}
		}
		
		StringBuffer message = new StringBuffer();
		
		Reader reader = readerService.getReader(rdId, (byte)2);
		
		LibCode lib = libCodeService.getLibByCode(reader.getRdLibCode());
		
		String webserviceUrl = lib.getWebserviceUrl();
		
		if (!"".equals(webserviceUrl) && webserviceUrl != null) {
			
			webserviceUrl = webserviceUrl + (webserviceUrl.endsWith("/") ? "" : "/") + "webservice/financeWebservice";
			
			JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();

			factory.setServiceClass(FinanceWebservice.class);

			factory.setAddress(webserviceUrl);
			
			FinanceWebservice client = (FinanceWebservice)factory.create();
			
			//Finance对象 cost欠款 tranid 财经流水记录号
			String rdOldPasswd = EncryptDecryptData.decryptWithCode(Constants.DES_STATIC_KEY, reader.getOldRdpasswd());
			String time = TimeUtils.dateToString(new Date(), "yyyyMMddhh24mmss");
			String sign = MD5Util.MD5Encode(regman + lib.getLibraryId() + time + reglib);//md5(regman + 图书馆全局馆id+ time + reglib)
//			paytype 支付方式 1.现金 2.IC卡
//			time yyyyMMddhhmmss (收款时间，格式固定)
			
			RdAccount rdaccount = rdAccountService.get(rdId);
			double prepay = rdaccount.getPrepay();
			if(paytype.equals("2")) {
				if(prepay < totalfeeD) {
					message.append("{");
					message.append("\"success\": -1");//余额不足
					message.append("}");
					ServletUtils.printHTML(response, message.toString());
					return ;
				}
			}
			
			String result = client.payFinance(trainList, sign, time, regman, reglib, paytype);
//			1 -> 更新成功
//			2 -> 验证失败
//			3 -> 数据库更新异常
//			4 -> 数据不全
			if(result.equals("1")) {//更新成功，本地记录日志
				String ip = Common.getUserIp(request);
				if(paytype.equals("2")) { //扣除一卡通账户
					prepay = prepay - totalfeeD;
					rdaccount.setPrepay(prepay);
					rdAccountService.update(rdaccount);
				}
				writeDeposityLog(reader, readerSession, "206", "30020",  ip, totalfeeD, Integer.parseInt(paytype));
			} 
			message.append("{");
			message.append("\"success\": "+ result);
			message.append("}");
		} else {
			message.append("{");
			message.append("\"success\": -3,");
			message.append("\"message\": " + Cautions.EMPTY_URL);
			message.append("}");
		}
		ServletUtils.printHTML(response, message.toString());
		return ;
	}

	
	/**
	 * 记录财经操作日志
	 * @param reader
	 * @param readerSession
	 * @param ip
	 * @param deposity
	 */
	public void writeDeposityLog(Reader reader, ReaderSession readerSession, String feeType, String logType, String ip, double fee, Integer paytype) {
		if(fee != 0) {
			CirFinLog cirFinLog = new CirFinLog();
			LogCir logCir = new LogCir();
			cirFinLog.setRdid(reader.getRdId());
			cirFinLog.setFeetype(feeType);
			cirFinLog.setFee(fee);
			cirFinLog.setRegtime(new Date());
			cirFinLog.setRegman(readerSession.getReader().getRdId());
			cirFinLog.setRegLib(readerSession.getReader().getRdLibCode());
			cirFinLog.setOrgLib(reader.getRdLib());//modify by 20140714 reader.getRdLibCode() -->reader.getRdLib()
			cirFinLog.setPaySign(1);
			cirFinLog.setPaytype(paytype);
			cirFinLog.setTranId(Common.getCirfinTranID());
			cirFinLogService.save(cirFinLog);
			logCir.setLogType(logType);
			logCir.setLibcode(readerSession.getReader().getRdLibCode());
			logCir.setUserId(readerSession.getReader().getRdId());
			logCir.setIpAddr(ip);
			logCir.setData2(reader.getRdId());
			logCir.setData3("");
			logCir.setData4(fee+ "");
			logCirService.save(logCir);
		}
	}
}
