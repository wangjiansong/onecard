package com.interlib.sso.controller.system;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.interlib.sso.common.Cautions;
import com.interlib.sso.common.Common;
import com.interlib.sso.common.Constants;
import com.interlib.sso.common.ServletUtils;
import com.interlib.sso.common.json.Jackson;
import com.interlib.sso.domain.ChargeType;
import com.interlib.sso.domain.CirFinLog;
import com.interlib.sso.domain.LogCir;
import com.interlib.sso.domain.RdAccount;
import com.interlib.sso.domain.Reader;
import com.interlib.sso.domain.ReaderCardInfo;
import com.interlib.sso.domain.ReaderSession;
import com.interlib.sso.service.ChargeTypeService;
import com.interlib.sso.service.CirFinLogService;
import com.interlib.sso.service.LogCirService;
import com.interlib.sso.service.RdAccountService;
import com.interlib.sso.service.ReaderCardInfoService;
import com.interlib.sso.service.ReaderService;

@Controller
@RequestMapping("admin/sys/rdaccount")
public class RdAccountController {
	
	private static final String CHARGE_VIEW = "admin/sys/rdaccount/charge";
	private static final String REFUND_VIEW = "admin/sys/rdaccount/refund";
	private static final String EDIT_VIEW = "admin/sys/rdaccount/edit";
	private static final String ADD_VIEW = "admin/sys/rdaccount/add";
	private static final String LIST_VIEW = "admin/sys/rdaccount/list";
	
	
	@Autowired
	public RdAccountService rdAccountService ;
	
	@Autowired
	public ReaderService readerService;
	
	@Autowired
	public CirFinLogService cirFinLogService;
	
	@Autowired
	public LogCirService logCirService;
	
	@Autowired
	private ReaderCardInfoService readerCardInfoService;
	@Autowired
	private ChargeTypeService chargeTypeService;
	/**
	 * 账户列表
	 * @param rdAccount
	 * @param model
	 * @return
	 */
	@RequiresRoles("admin")
	@RequestMapping("/list")
	public String rdAccountList(@ModelAttribute RdAccount rdAccount, Model model) {
		model.addAttribute(Constants.ACTIVE_MENU_KEY, "rdaccount_list");
		List<RdAccount> list = rdAccountService.queryRdAccountList(rdAccount);
		model.addAttribute("pageList", list);
		model.addAttribute("obj", rdAccount);
		return LIST_VIEW;
	}

	/**
	 * 查询读者账户
	 * @param response
	 * @param rdid
	 */
	@RequiresRoles("admin")
	@RequestMapping("/getRdAccount")
	public void getRdAccount(HttpServletRequest request, HttpServletResponse response) {
		String fieldName = request.getParameter("fieldName");
		String fieldValue = request.getParameter("fieldValue");
		String rdid = "";
		if(fieldName.equals("rdId")) {
			rdid = fieldValue;
		} else if(fieldName.equals("cardNo")) {
			ReaderCardInfo cardInfo = readerCardInfoService.get(fieldValue);
			rdid = cardInfo.getRdId();
		}
		
		RdAccount rdAccount = rdAccountService.get(rdid);
		Reader reader = readerService.getReader(rdid, (byte)2);
		if(reader != null && rdAccount != null) {
			rdAccount.setRdname(reader.getRdName());
			String jsonStr = Jackson.getBaseJsonData(rdAccount);
			ServletUtils.printHTML(response, jsonStr);
		} else {
			ServletUtils.printHTML(response, "");
		}
	}
	
	/**
	 * 充值界面
	 * @param rdAccount
	 * @param bindingResult
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresRoles("admin")
	@RequestMapping("/charge")
	public String charge(Model model) {
		model.addAttribute(Constants.ACTIVE_MENU_KEY, "rdaccount_charge");
		
		List<ChargeType> chargeTypeList = chargeTypeService.getAll();
		model.addAttribute("chargeTypeList",chargeTypeList);
		return CHARGE_VIEW;
	}
	
	/**
	 * 充值
	 * @param rdAccount
	 * @param bindingResult
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresRoles("admin")
	@RequestMapping("/doCharge")
	public void doCharge(HttpServletRequest request, HttpServletResponse response, @ModelAttribute RdAccount rdAccount) {
		double charge = Double.parseDouble(request.getParameter("charge"));//充值的金额
		int payType = Integer.parseInt(StringUtils.trimToEmpty(request.getParameter("payType")).equals("")?"1":request.getParameter("payType"));
		rdAccount = rdAccountService.get(rdAccount.getRdid());
		
		StringBuffer message = new StringBuffer();
		
		if(rdAccount.getStatus() != 1) { //非正常状态
			message.append("{");
			message.append("\"success\": 0, ");
			message.append("\"message\": \"" + Cautions.STATUS_ERROR + "\"");
			message.append("}");
			ServletUtils.printHTML(response, message.toString());
			return;
		}
		double prepay = 0d;

		prepay = rdAccount.getPrepay() + charge;
		rdAccount.setPrepay(prepay);
		
		rdAccountService.update(rdAccount);
		String feeAppCode = "onecard";//本系统充值默认代码
		//log
		int paySign = 1;//已交付
		writeDeposityLog(rdAccount.getRdid(), request, "106", "30216", charge, paySign, payType, "", feeAppCode);
		
		message.append("{");
		message.append("\"success\": 1 ,");
		message.append("\"rdid\":\"" + rdAccount.getRdid() + "\",");
		message.append("\"prepay\":" + rdAccount.getPrepay() + ",");
		message.append("\"status\":" + rdAccount.getStatus());
		message.append("}");
		ServletUtils.printHTML(response, message.toString());
		return;
	}
	
	
	/**
	 * 退款界面
	 * @param rdAccount
	 * @param bindingResult
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresRoles("admin")
	@RequestMapping("/refund")
	public String refund(Model model) {
		model.addAttribute(Constants.ACTIVE_MENU_KEY, "rdaccount_refund");
		
		return REFUND_VIEW;
	}
	
	/**
	 * 退款
	 * @param rdAccount
	 * @param bindingResult
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresRoles("admin")
	@RequestMapping("/doRefund")
	public void doRefund(HttpServletRequest request, HttpServletResponse response, @ModelAttribute RdAccount rdAccount) {
		double refund = Double.parseDouble(request.getParameter("refund"));//退款的金额
		String feeMemo = request.getParameter("feeMemo");//退款备注，原因
		int payType = Integer.parseInt(StringUtils.trimToEmpty(request.getParameter("payType")).equals("")?"1":request.getParameter("payType"));
		rdAccount = rdAccountService.get(rdAccount.getRdid());
		
		StringBuffer message = new StringBuffer();
		
		if(rdAccount.getStatus() != 1) { //非正常状态
			message.append("{");
			message.append("\"success\": 0, ");
			message.append("\"message\": \"" + Cautions.STATUS_ERROR + "\"");
			message.append("}");
			ServletUtils.printHTML(response, message.toString());
			return;
		}
		if(refund > rdAccount.getPrepay()) {
			message.append("{");
			message.append("\"success\": 0, ");
			message.append("\"message\": \"" + Cautions.INPUT_ERROR + "\"");
			message.append("}");
			ServletUtils.printHTML(response, message.toString());
			return;
		}
		double prepay = rdAccount.getPrepay() - refund;
		DecimalFormat df = new DecimalFormat("#0.0"); 
		rdAccount.setPrepay(prepay);
		
		rdAccountService.update(rdAccount);
		String feeAppCode = "onecard";//本系统默认代码
		//log
		int paysign = 3;//已退还
		writeDeposityLog(rdAccount.getRdid(), request, "205", "30213", -refund, paysign, payType, feeMemo, feeAppCode);
		
		message.append("{");
		message.append("\"success\": 1 ,");
		message.append("\"rdid\":\"" + rdAccount.getRdid() + "\",");
		message.append("\"prepay\":" + df.format(prepay) + ",");
		message.append("\"status\":" + rdAccount.getStatus());
		message.append("}");
		ServletUtils.printHTML(response, message.toString());
		return;
	}
	
	/**
	 * 退证
	 * @param rdAccount
	 * @param bindingResult
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresRoles("admin")
	@RequestMapping("/doQuit")
	public ModelAndView doQuit(@ModelAttribute RdAccount rdAccount, BindingResult bindingResult, 
			Model model, RedirectAttributes redirectAttributes) {
		rdAccountService.update(rdAccount);
		redirectAttributes.addFlashAttribute("message", "退证成功");
		return new ModelAndView("redirect:/admin/sys/rdaccount/refund");
	}
	
	/**
	 * 冻结操作
	 * @param rdid
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresRoles("admin")
	@RequestMapping("/doLock/{rdid}")
	public ModelAndView delete(HttpServletRequest request, @PathVariable String rdid, RedirectAttributes redirectAttributes) throws IOException{
		ReaderSession readerSession = (ReaderSession)request.getSession().getAttribute("READER_SESSION");
		String ip = Common.getUserIp(request);
		RdAccount rdAccount = rdAccountService.get(rdid);
		rdAccount.setStatus(2);
		rdAccountService.update(rdAccount);
		
		LogCir logCir = new LogCir();
		logCir.setLogType("30701");
		logCir.setLibcode(readerSession.getReader().getRdLibCode());
		logCir.setUserId(readerSession.getReader().getRdId());
		logCir.setIpAddr(ip);
		logCir.setData2(rdid);
		logCir.setData3("");
		logCir.setData4("");
		logCirService.save(logCir);
		
		redirectAttributes.addFlashAttribute("message", "冻结成功");
		return new ModelAndView("redirect:/admin/sys/rdaccount/list");
	}
	/**
	 * 恢复操作
	 * @param rdid
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresRoles("admin")
	@RequestMapping("/doUnLock/{rdid}")
	public ModelAndView doUnLock(HttpServletRequest request, @PathVariable String rdid, RedirectAttributes redirectAttributes) throws IOException{
		ReaderSession readerSession = (ReaderSession)request.getSession().getAttribute("READER_SESSION");
		String ip = Common.getUserIp(request);
		RdAccount rdAccount = rdAccountService.get(rdid);
		rdAccount.setStatus(1);
		LogCir logCir = new LogCir();
		logCir.setLogType("30702");
		logCir.setLibcode(readerSession.getReader().getRdLibCode());
		logCir.setUserId(readerSession.getReader().getRdId());
		logCir.setIpAddr(ip);
		logCir.setData2(rdid);
		logCir.setData3("");
		logCir.setData4("");
		logCirService.save(logCir);
		rdAccountService.update(rdAccount);
		redirectAttributes.addFlashAttribute("message", "恢复成功");
		return new ModelAndView("redirect:/admin/sys/rdaccount/list");
	}
	
	/**
	 * 记录财经操作日志
	 * @param reader
	 * @param readerSession
	 * @param ip
	 * @param deposity
	 */
	public void writeDeposityLog(String rdid, HttpServletRequest request, String feeType, String logType,
			double fee, int paySign, int payType, String feeMemo, String feeAppCode) {
		Reader reader = readerService.getReader(rdid, (byte)2);
		reader.setRdLib(reader.getRdLibCode());
		ReaderSession readerSession = (ReaderSession)request.getSession().getAttribute("READER_SESSION");
		String ip = Common.getUserIp(request);
		CirFinLog cirFinLog = new CirFinLog();
		LogCir logCir = new LogCir();
		cirFinLog.setRdid(reader.getRdId());
		cirFinLog.setFeetype(feeType);
		cirFinLog.setFee(fee);
		cirFinLog.setRegtime(new Date());
		cirFinLog.setRegman(readerSession.getReader().getRdId());
		cirFinLog.setRegLib(readerSession.getReader().getRdLibCode());
		cirFinLog.setOrgLib(reader.getRdLib());//modify by 20140714 reader.getRdLibCode() -->reader.getRdLib()
		cirFinLog.setPaySign(paySign);
		cirFinLog.setPaytype(payType);//纸币
		cirFinLog.setTranId(Common.getCirfinTranID());
		cirFinLog.setFeeMemo(feeMemo);
		cirFinLog.setFeeAppCode(feeAppCode);
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
