package com.interlib.sso.interfaces;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.annotation.XmlRootElement;

import net.sf.json.JSON;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.apache.cxf.binding.corba.wsdl.Array;
import org.aspectj.weaver.ast.Var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.interlib.sso.common.Common;
import com.interlib.sso.common.JsonUtil;
import com.interlib.sso.common.TimeUtils;
import com.interlib.sso.domain.CardRuleDetail;
import com.interlib.sso.domain.CirFinLog;
import com.interlib.sso.domain.LogCir;
import com.interlib.sso.domain.RdAccount;
import com.interlib.sso.domain.Reader;
import com.interlib.sso.domain.ReaderCardInfo;
import com.interlib.sso.service.CardRuleService;
import com.interlib.sso.service.CirFinLogService;
import com.interlib.sso.service.LogCirService;
import com.interlib.sso.service.RdAccountService;
import com.interlib.sso.service.ReaderCardInfoService;
import com.interlib.sso.service.ReaderService;


@Controller
@RequestMapping("api/finance")
public class FinanceInterfaces {

	@Autowired
	public RdAccountService rdAccountService;
	
	@Autowired
	public ReaderService readerService;
	
	@Autowired
	public CirFinLogService cirFinLogService;
	
	@Autowired
	public LogCirService logCirService;
	
	@Autowired
	public CardRuleService cardRuleService;
	
	@Autowired
	public ReaderCardInfoService readerCardInfoService;
	
	private DecimalFormat df = new DecimalFormat("0.##");
	
	@XmlRootElement(name="Result")
	static class R {
		public String rdid;
		public String account; //余额
		public String slotTime;//消费时间
		public String fee; //消费金额
		public String feeType;//消费时段类型，上午还是中午etc.
		public String rdType;
		public String message;
		public String success;
	}
	
	/**
	 * 充值
	 * @param req
	 * @param rdid
	 * @param fee
	 * @param userid
	 * @param libcode
	 * @param appcode
	 * @return
	 */
	@RequestMapping(value="/charge", produces="application/xml") 
	public @ResponseBody R charge(HttpServletRequest req, 
			@RequestParam(required=false, value="cardId") String cardId,
			@RequestParam(required=false, value="rdid") String rdid, 
			@RequestParam(required=false, value="fee") String fee,
			@RequestParam(required=false, value="userid") String userid,
			@RequestParam(required=false, value="libcode") String libcode, 
			@RequestParam(required=false, value="appcode") String appcode,
			@RequestParam(required=false, value="payId") String payId) {
		String reqIp = Common.getUserIp(req);
		R r = new R();
		
		if(cardId ==null && rdid == null) {
			r.message = "卡号和读者证号不能同时为空";
			r.success = "0";
			return r;
		}
		
		if(cardId != null && !"".equals(cardId)) {
			cardId = cardId.trim();
			ReaderCardInfo card = readerCardInfoService.get(cardId);
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
			rdid = card.getRdId();
		}
		
		Reader reader = readerService.getReader(rdid, (byte)1);
		if(reader == null) {
			r.message = "读者不存在";
			r.success = "0";
			return r;
		}
		if(reader.getRdCFState() != 1l) {
			r.message = "证状态无效";
			r.success = "0";
			return r;
		}
		if(fee == null) {
			r.message = "传递的金额不正确";
			r.success = "0";
			return r;
		}
		if(userid == null) {
			r.message = "操作员不能为空";
			r.success = "0";
			return r;
		}
		if(libcode == null) {
			r.message = "馆代码不能为空";
			r.success = "0";
			return r;
		}
		if(appcode == null) {
			r.message = "应用代码不能为空";
			r.success = "0";
			return r;
		}
		
		if(payId == null) {
			r.message = "交易票据不能为空";
			r.success = "0";
			return r;
		}
		
		if(payId != null && !"".equals(payId)) {
			CirFinLog cirfin = new CirFinLog();
			cirfin.setPayId(payId);
			List<CirFinLog> pageList = cirFinLogService.queryCirFinLogList(cirfin);
			if(pageList.size()!=0){
				r.message = "该交易票据单号已存在";
				r.success = "0";
				return r;
			}else{
			payId = cirfin.getPayId();
			}
		}
		r.rdid = rdid;
		r.fee = fee;
		double charge = Double.parseDouble(fee);//本次充值
		if(charge <= 0) {
			r.message = "传递的金额不正确";
			r.success = "0";
			return r;
		}
//		try {
//			WebserviceAuthUtils.loginCheck(rdid, reader.getRdPasswd());
//		} catch(RuntimeException re) {
//			r.message = re.toString();
//			r.success = "0";
//			return r;
//		}
		
		RdAccount rdAccount = rdAccountService.get(rdid);
		if(rdAccount == null) {
			rdAccount = new RdAccount();
			rdAccount.setRdid(rdid);
			rdAccount.setDeposit(0.0);
			rdAccount.setPrepay(0.0);
			rdAccount.setStatus(1);
			rdAccountService.save(rdAccount);
		}
		if(rdAccount.getStatus() != 1) {
			r.message = "一卡通账户非正常状态";
			r.success = "0";
			return r;
		}
		double rdPrepay = rdAccount.getPrepay();//账户余额
		rdPrepay = rdPrepay + charge;
		rdAccount.setPrepay(rdPrepay);
		rdAccountService.update(rdAccount);
		//TODO 写日志
		r.rdType = reader.getRdType();
		r.slotTime = TimeUtils.dateToString(new Date(), TimeUtils.YYYYMMDDHHMMSS);
		int paytype = 1;//充值应该是纸币
		writeLog(reader, userid, libcode, appcode, "106", "30216", reqIp, charge, null,0, paytype,payId);
		r.success = "1";
		r.account = rdPrepay + "";
		return r;
	}
	
	/**
	 * 充值查询接口
	 * @param payId
	 * @return
	 */
	@RequestMapping(value="/querycharge", produces="application/xml") 
	public @ResponseBody R querycharge(
			@RequestParam(required=false, value="cardId") String cardId, 
			@RequestParam(required=false, value="rdid") String rdid, 
			@RequestParam(required=false, value="userid") String userid,
			@RequestParam(required=false, value="libcode") String libcode,
			@RequestParam(required=false, value="payId") String payId) {
		R r = new R();

		if(cardId ==null && rdid == null) {
			r.message = "卡号和读者证号不能同时为空";
			r.success = "0";
			return r;
		}
		
		ReaderCardInfo card = null;
		if(cardId != null && !"".equals(cardId)) {
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
			rdid = card.getRdId();
		}
		
		Reader reader = readerService.getReader(rdid, (byte)1);
		if(reader == null) {
			r.message = "读者不存在";
			r.success = "0";
			return r;
		}
		if(reader.getRdCFState() != 1l) {
			r.message = "证状态无效";
			r.success = "0";
			return r;
		}
		
		
		if(userid == null) {
			r.message = "操作员不能为空";
			r.success = "0";
			return r;
		}
		if(libcode == null) {
			r.message = "馆代码不能为空";
			r.success = "0";
			return r;
		}
		
		if(payId != null && !"".equals(payId)) {
			CirFinLog cirfin = new CirFinLog();
			cirfin.setPayId(payId);
			List<CirFinLog> pageList = cirFinLogService.queryCirFinLogList(cirfin);
			if(pageList.size()==0){
				r.message = "该交易票据单号不存在有效记录";
				r.success = "0";
				return r;
			}else{
			payId = cirfin.getPayId();
		}
	}		
		r.rdid = rdid;
		
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
			return r;
		}
		r.rdType = reader.getRdType();
		r.slotTime = TimeUtils.dateToString(new Date(), TimeUtils.YYYYMMDDHHMMSS);
		r.success = "1";
		r.account = rdAccount.getPrepay() + "";
		return r;
	}
	
	/**
	 * 扣费接口
	 * @param rdid
	 * @param fee
	 * @param userid
	 * @param libcode
	 * @param appcode
	 * @param feeType
	 * @return
	 * http://172.28.228.14:8080/onecard/api/finance/deduction?rdid=2015003&appcode=asiastar&userid=admin&libcode=999&feetype=206&paytype=1&enc=95dcf989e7b42fa76345a6e5447871b8&fee=0.1
	 */
	@RequestMapping(value="/deduction", produces="application/xml") 
	public @ResponseBody R deduction(HttpServletRequest req, 
			@RequestParam(required=false, value="cardId") String cardId, 
			@RequestParam(required=false, value="rdid") String rdid, 
			@RequestParam(required=false, value="fee") String fee,
			@RequestParam(required=false, value="userid") String userid,
			@RequestParam(required=false, value="libcode") String libcode, 
			@RequestParam(required=false, value="appcode") String appcode, 
			@RequestParam(required=false, value="paytype") String paytype, 
			@RequestParam(required=false, value="feetype") String feetype,
			@RequestParam(required=false, value="payId") String payId) {
		String reqIp = Common.getUserIp(req);
		R r = new R();
		
		if(cardId ==null && rdid == null) {
			r.message = "卡号和读者证号不能同时为空";
			r.success = "0";
			return r;
		}
		
		ReaderCardInfo card = null;
		if(cardId != null && !"".equals(cardId)) {
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
			rdid = card.getRdId();
		}
		
		Reader reader = readerService.getReader(rdid, (byte)1);
		if(reader == null) {
			r.message = "读者不存在";
			r.success = "0";
			return r;
		}
		if(reader.getRdCFState() != 1l) {
			r.message = "证状态无效";
			r.success = "0";
			return r;
		}
		if(fee == null) {
			r.message = "传递的金额不正确";
			r.success = "0";
			return r;
		}
		if(userid == null) {
			r.message = "操作员不能为空";
			r.success = "0";
			return r;
		}
		if(libcode == null) {
			r.message = "馆代码不能为空";
			r.success = "0";
			return r;
		}
		if(appcode == null) {
			r.message = "应用代码不能为空";
			r.success = "0";
			return r;
		}
		if(feetype == null) {
			r.message = "扣费类型不能为空";
			r.success = "0";
			return r;
		}
		if(StringUtils.trimToEmpty(paytype).equals("")) {
			r.message = "交易类型不能为空";
			r.success = "0";
			return r;
		}
		
		
		double feed = Double.parseDouble(fee);
		if(feed <= 0) {
			r.message = "传递的金额不正确";
			r.success = "0";
			return r;
		}
		r.rdid = rdid;
		r.fee = fee;
//		try {
//			WebserviceAuthUtils.loginCheck(rdid, reader.getRdPasswd());
//		} catch(RuntimeException re) {
//			r.message = re.toString();
//			r.success = "0";
//			return r;
//		}
		RdAccount rdAccount = rdAccountService.get(rdid);
		if(rdAccount == null) {
			rdAccount = new RdAccount();
			rdAccount.setRdid(rdid);
			rdAccount.setDeposit(0.0);
			rdAccount.setPrepay(0.0);
			rdAccountService.save(rdAccount);
		}
		int paytypeInt = Integer.parseInt(paytype);
		if(paytypeInt != 2) {//不是IC卡扣费，就不用去检查账户了
			if(rdAccount.getStatus() != 1) {
				r.message = "账户非正常状态";
				r.success = "0";
				return r;
			}
			
			double rdPrepay = rdAccount.getPrepay();
			r.account = rdPrepay + "";
			if(rdPrepay < feed) {
				r.message = "账户余额不足";
				r.account = rdAccount.getPrepay() + "";
				r.success = "0";
				return r;
			}
			rdPrepay = rdPrepay - feed;
			df.format(rdPrepay);
			rdAccount.setPrepay(rdPrepay);
			rdAccountService.update(rdAccount);//扣费成功
		}
		
		Date slotTime = new Date();
		if(cardId != null && !"".equals(cardId)) {
			card.setLastUseTime(slotTime);
			card.setTotalOfUsed(card.getTotalOfUsed() + 1);
			readerCardInfoService.update(card);
		}
		
		//TODO 写日志
		r.slotTime = TimeUtils.dateToString(slotTime, TimeUtils.YYYYMMDDHHMMSS);
		r.rdType = reader.getRdType();
		//从一卡通余额扣费，交付类型应该是IC卡
//		1 	现金
//		2	IC卡
//		3	预付款
//		4	积分抵扣
//		5	支付宝
//		6	微支付
		writeLog(reader, userid, libcode, appcode, feetype, "30024", reqIp, feed, null,0, paytypeInt,payId);
		r.success = "1";
		r.account = df.format(rdAccount.getPrepay());
		return r;
	}
	
	/**
	 * 回滚扣费接口
	 * @param rdid
	 * @param userid
	 * @param appcode
	 * @return
	 * http://localhost:8089/onecard/api/finance/deduction?cardId=abcdedf&appcode=interlib&enc=e6d616c0f658db70eb22581835ac3885&userid=admin&libcode=999	 
	 */
	
	@RequestMapping(value="/batchDeduction", produces="application/xml")
	public @ResponseBody R alldeduction(HttpServletRequest req, 
			@RequestParam(required=false, value="cardId") String cardId, 
			@RequestParam(required=false, value="rdid") String rdid,
			@RequestParam(required=false, value="fees") String fees,
			@RequestParam(required=false, value="userid") String userid,
			@RequestParam(required=false, value="appcode") String appcode,
			@RequestParam(required=false, value="libcode") String libcode,
			@RequestParam(required=false, value="paytype") String paytype) {
		String reqIp = Common.getUserIp(req);
		R r = new R();
		String feess = req.getParameter("fees");
		String fee = null;
		String feetype = null;
	
//		 fees    =  [{"feetype":"202","fee":17},{"feetype":"209","fee":8}];
		
		if(cardId ==null && rdid == null) {
			r.message = "卡号和读者证号不能同时为空";
			r.success = "0";
			return r;
		}
		
		ReaderCardInfo card = null;
		if(cardId != null && !"".equals(cardId)) {
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
			rdid = card.getRdId();
		}
		
		Reader reader = readerService.getReader(rdid, (byte)1);
		if(reader == null) {
			r.message = "读者不存在";
			r.success = "0";
			return r;
		}
		if(reader.getRdCFState() != 1l) {
			r.message = "证状态无效";
			r.success = "0";
			return r;
		}
		if(userid == null) {
			r.message = "操作员不能为空";
			r.success = "0";
			return r;
		}
		if(libcode == null) {
			r.message = "馆代码不能为空";
			r.success = "0";
			return r;
		}
		if(appcode == null) {
			r.message = "应用代码不能为空";
			r.success = "0";
			return r;
		}
		try {
	        req.setCharacterEncoding("UTF-8");
			//下面是把拿到的json字符串转成 json对象
			
	        //取出数组第一个元素 
			JSONArray json = JSONArray.fromObject(feess ); // 首先把字符串转成 JSONArray  对象
			if(json.size()>0){
			  for(int i=0;i<json.size();i++){
			    JSONObject job = json.getJSONObject(i);  // 遍历 jsonarray 数组，把每一个对象转成 json 对象
			    feetype = job.get("feetype").toString();
			    fee =job.get("fee").toString();
			    System.out.println("-------feetype："+feetype) ;  // 得到 每个对象中的属性值
			    System.out.println("-------fee："+fee) ;
			    
			    double feed = Double.parseDouble(fee);
			    System.out.println("```````````feed："+feed);
				if(feed <= 0) {
					r.message = "传递的金额不正确";
					r.success = "0";
					return r;
				}
				
			    Integer feetyped = Integer.parseInt(feetype);
			    System.out.println("```````````feetyped："+feetyped+"----");
				if(feetyped <= 200) {
					r.message = "传递的类型不正确";
					r.success = "0";
					return r;
				}
				
				r.rdid = rdid;
				
			  RdAccount rdAccount = rdAccountService.get(rdid);
				if(rdAccount == null) {
					rdAccount = new RdAccount();
					rdAccount.setRdid(rdid);
					rdAccount.setDeposit(0.0);
					rdAccount.setPrepay(0.0);
					rdAccountService.save(rdAccount);
				}
				int paytypeInt = Integer.parseInt(paytype);
				if(paytypeInt != 2) {//不是IC卡扣费，就不用去检查账户了
					if(rdAccount.getStatus() != 1) {
						r.message = "账户非正常状态";
						r.success = "0";
						return r;
					}
					
					double rdPrepay = rdAccount.getPrepay();
					r.account = rdPrepay + "";
					if(rdPrepay < feed) {
						r.message = "账户余额不足";
						r.account = rdAccount.getPrepay() + "";
						r.success = "0";
						return r;
					}
					rdPrepay = rdPrepay - feed;
					df.format(rdPrepay);
					rdAccount.setPrepay(rdPrepay);
					rdAccountService.update(rdAccount);//扣费成功
				}
				
				Date slotTime = new Date();
				if(cardId != null && !"".equals(cardId)) {
					card.setLastUseTime(slotTime);
					card.setTotalOfUsed(card.getTotalOfUsed() + 1);
					readerCardInfoService.update(card);
				}

			//TODO 写日志
				r.slotTime = TimeUtils.dateToString(slotTime, TimeUtils.YYYYMMDDHHMMSS);
				r.rdType = reader.getRdType();
			
			
				//从一卡通余额扣费，交付类型应该是IC卡
//				1 	现金
//				2	IC卡
//				3	预付款
//				4	积分抵扣
//				5	支付宝
//				6	微支付
				writeLog(reader, userid, libcode, appcode,feetype, "30024" , reqIp, feed, null,0, paytypeInt,null);
				r.success = "1";
				r.account = df.format(rdAccount.getPrepay());
			  }
			 
			}
			String fes =fee + fee;
			System.out.println("###"+fes);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("-------"+fees+"-------");
		
		return r;
	}
	
	@RequestMapping(value="/autoDeduction", produces="application/xml") 
	public @ResponseBody R autoDeduction(HttpServletRequest req, 
			@RequestParam(required=false, value="cardId") String cardId, 
			@RequestParam(required=false, value="rdid") String rdid, 
			@RequestParam(required=false, value="fee") String fee,
			@RequestParam(required=false, value="userid") String userid,
			@RequestParam(required=false, value="libcode") String libcode, 
			@RequestParam(required=false, value="appcode") String appcode, 
			@RequestParam(required=false, value="feetype") String feetype,
			@RequestParam(required=false, value="payId") String payId) {
		String reqIp = Common.getUserIp(req);
		R r = new R();

		if(cardId ==null && rdid == null) {
			r.message = "卡号和读者证号不能同时为空";
			r.success = "0";
			return r;
		}
		
		ReaderCardInfo card = null;
		if(cardId != null && !"".equals(cardId)) {
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
			rdid = card.getRdId();
		}
		
		Reader reader = readerService.getReader(rdid, (byte)1);
		if(reader == null) {
			r.message = "读者不存在";
			r.success = "0";
			return r;
		}
		r.rdid = rdid;
		if(userid == null) {
			r.message = "操作员不能为空";
			r.success = "0";
			return r;
		}
		if(libcode == null) {
			r.message = "馆代码不能为空";
			r.success = "0";
			return r;
		}
		if(appcode == null) {
			r.message = "应用代码不能为空";
			r.success = "0";
			return r;
		}
		if(feetype == null) {
			r.message = "扣费类型不能为空";
			r.success = "0";
			return r;
		}
		
		
//		try {
//			WebserviceAuthUtils.loginCheck(rdid, reader.getRdPasswd());
//		} catch(RuntimeException re) {
//			r.message = re.toString();
//			r.success = "0";
//			return r;
//		}
		
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
			return r;
		}
		
		double rdPrepay = rdAccount.getPrepay();
		r.account = rdPrepay + "";
		
		//TODO 根据规则获取要扣去的费用
		//1. 根据财经日志，看看现在的消费时段是否满足已消费22顿，早餐22， 午餐22， 晚餐22，每个月开始另计
		Date now = new Date();
		CardRuleDetail cardRule = null;
		//1）. 根据读者ID去取分组所配置的规则，取得当前时间属于哪个规则下的
		cardRule = cardRuleService.slotCard(rdid, now);
	
		//如果规则为空
		if(cardRule == null) {
			r.message = "没有相应扣费规则，请先联系管理员设置";
			r.success = "0";
			return r;
		}

		String yearMonth = TimeUtils.dateToString(now, "yyyyMM");
		
		Map<String, String> params = new HashMap<String, String>();
		params.put("rdid", rdid);
		params.put("startTime", cardRule.getStartTime());
		params.put("endTime", cardRule.getEndTime());
		params.put("yearMonth", yearMonth);
		params.put("feetype", feetype);
		long slotCount = cirFinLogService.slotCountByRules(params);
		//折扣价
		double feed = cardRule.getSalePrice();
		double cost = cardRule.getCostPrice();//成本价
		String priceType = "sale";//折扣价
		//2. 如果本月在当前时段已经消费xx次，则去查规则里当前时段的成本价
		Map<String, String> cardRuleMap = cardRuleService.getCardRuleById(cardRule.getRuleId());
		long subsidizeTimes = 22l;
		try {
			if(cardRuleMap.get("SUBSIDIZETIMES") != null && !"".equals(cardRuleMap.get("SUBSIDIZETIMES"))) {
				subsidizeTimes = Long.parseLong(cardRuleMap.get("SUBSIDIZETIMES"));
			}
		} catch(Exception e) {
			
		}
		System.out.println("规定补助餐数：" + subsidizeTimes);
		if(slotCount >= subsidizeTimes) {
			feed = cardRule.getCostPrice();
			priceType = "cost";//成本价
		} else{
			//3. 如果消费还没到22天， 则看看当前时段今天里是否已经消费过一次（1. 如果已经消费过一次，则扣成本价；2. 如果是第一次，则扣折扣价）
			params.put("yearMonth", null);
			String today=TimeUtils.dateToString(new Date(),"yyyyMMdd");//默认的格式是YYYYMMDD 20140327添加是否当天第一顿
			params.put("today", today);//默认的格式是YYYYMMDD 20140327添加是否当天第一顿
			slotCount = cirFinLogService.slotCountByRules(params);
			if(slotCount >= 1l) {
				feed = cardRule.getCostPrice();
				priceType = "cost";
			}
		}
		
		//4. 
		if(feed <= 0) {
			r.message = "传递的金额不正确";
			r.success = "0";
			return r;
		}
		
		r.fee = feed + "";
		
		if(rdPrepay < feed) {
			r.message = "账户余额不足";
			r.account = rdAccount.getPrepay() + "";
			r.success = "0";
			return r;
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
		
		r.slotTime = TimeUtils.dateToString(slotTime, TimeUtils.YYYYMMDDHHMMSS);
		r.rdType = reader.getRdType();
		String feeMemo = cardRule.getTimeFlag() + "_" + priceType;
		if(feeMemo.equals("1_sale")) {
			r.feeType = "早餐（折扣价）";
		} else if(feeMemo.equals("1_cost")) {
			r.feeType = "早餐（原价）";
		} else if(feeMemo.equals("2_sale")) {
			r.feeType = "午餐（折扣价）";
		} else if(feeMemo.equals("2_cost")) {
			r.feeType = "午餐（原价）";
		} else if(feeMemo.equals("3_sale")) {
			r.feeType = "晚餐（折扣价）";
		} else if(feeMemo.equals("3_cost")) {
			r.feeType = "晚餐（原价）";
		} 
		int groupId = readerService.getReaderGroupId(reader.getRdId());// ADD groupid 2014-04-28
		reader.setGroupId(groupId);
		//TODO 写日志
		int paytype = 2;
		writeLog(reader, userid, libcode, appcode, feetype, "30024", reqIp, feed, feeMemo,cost, paytype, payId);
		r.success = "1";
		r.account = rdPrepay + "";
		return r;
	}
	
	/**
	 * 查询余额接口
	 * @param rdid
	 * @param fee
	 * @param userid
	 * @param libcode
	 * @return
	 */
	@RequestMapping(value="/query", produces="application/xml") 
	public @ResponseBody R query(
			@RequestParam(required=false, value="cardId") String cardId, 
			@RequestParam(required=false, value="rdid") String rdid, 
			@RequestParam(required=false, value="userid") String userid,
			@RequestParam(required=false, value="libcode") String libcode) {
		R r = new R();

		if(cardId ==null && rdid == null) {
			r.message = "卡号和读者证号不能同时为空";
			r.success = "0";
			return r;
		}
		
		ReaderCardInfo card = null;
		if(cardId != null && !"".equals(cardId)) {
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
			rdid = card.getRdId();
		}
		
		Reader reader = readerService.getReader(rdid, (byte)1);
		if(reader == null) {
			r.message = "读者不存在";
			r.success = "0";
			return r;
		}
		if(reader.getRdCFState() != 1l) {
			r.message = "证状态无效";
			r.success = "0";
			return r;
		}
		if(userid == null) {
			r.message = "操作员不能为空";
			r.success = "0";
			return r;
		}
		if(libcode == null) {
			r.message = "馆代码不能为空";
			r.success = "0";
			return r;
		}
		
		r.rdid = rdid;
		
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
			return r;
		}
		r.rdType = reader.getRdType();
		r.slotTime = TimeUtils.dateToString(new Date(), TimeUtils.YYYYMMDDHHMMSS);
		r.success = "1";
		r.account = rdAccount.getPrepay() + "";
		return r;
	}
	
	/**
	 * 记录财经操作日志
	 * @param reader
	 * @param readerSession
	 * @param ip
	 * @param deposity
	 */
	private void writeLog(Reader reader, String userid, String libcode, String appcode, String feeType,
			String logType, String ip, double fee, String feeMemo,double cost, int paytype,String payId) {
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
		cirFinLog.setPaytype(paytype);
		cirFinLog.setTranId(Common.getCirfinTranID());
		cirFinLog.setFeeAppCode(appcode);
		cirFinLog.setGroupID(reader.getGroupId());//ADD 2014-04-28
		cirFinLog.setPayId(payId);
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
	
//	private void writeLogBatch(Reader reader, String userid, String libcode, String appcode, String feeType,
//			String logType, String ip, double fee, String feeMemo,double cost, int paytype) {
//		CirFinLog cirFinLog = new CirFinLog();
//		LogCir logCir = new LogCir();
//		cirFinLog.setRdid(reader.getRdId());
//		cirFinLog.setFeetype(feeType);
//		cirFinLog.setFee(fee);
//		cirFinLog.setRegtime(new Date());
//		cirFinLog.setRegman(userid);
//		cirFinLog.setRegLib(libcode);
//		cirFinLog.setOrgLib(reader.getRdLibCode());
//		cirFinLog.setPaySign(1);
//		cirFinLog.setPaytype(paytype);
//		cirFinLog.setTranId(Common.getCirfinTranID());
//		cirFinLog.setFeeAppCode(appcode);
//		cirFinLog.setGroupID(reader.getGroupId());//ADD 2014-04-28
//		if(feeMemo != null) {
//			cirFinLog.setFeeMemo(feeMemo);
//			logCir.setData3(feeMemo);
//		}
//		cirFinLog.setCost(cost);//add 20140321
//		cirFinLogService.save(cirFinLog);
//		logCir.setLogType(logType);
//		logCir.setLibcode(libcode);
//		logCir.setUserId(userid);
//		logCir.setIpAddr(ip);
//		logCir.setData2(reader.getRdId());
//		logCir.setData3("");
//		logCir.setData4(fee+ "");
//		logCirService.save(logCir);
//	}
	
	public static void main(String[] args) throws ParseException {
		ArrayList<Date> results = new ArrayList<Date>();
		Date startDate = TimeUtils.stringToDate("2017-03-21", "");
		Date endDate = TimeUtils.stringToDate("2017-04-30", "");
		long intervalDays = (endDate.getTime() - startDate.getTime())
				/ (1000 * 60 * 60 * 24);

		results.add(startDate);
		Calendar cal = Calendar.getInstance();
		cal.setTime(startDate);
		for (int i = 0; i < intervalDays; i++) {
			cal.add(Calendar.DATE, 1);
			results.add(cal.getTime());
		}
		System.out.println(results.size());
		for(Date day : results) {
			System.out.print(TimeUtils.dateToString(day, "dd") + ",");
			
		}
		
	}
}
