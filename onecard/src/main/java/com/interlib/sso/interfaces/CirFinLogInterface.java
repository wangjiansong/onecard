package com.interlib.sso.interfaces;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.annotation.XmlRootElement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.interlib.sso.common.Constants;
import com.interlib.sso.common.EncryptDecryptData;
import com.interlib.sso.common.MD5Util;
import com.interlib.sso.common.TimeUtils;
import com.interlib.sso.domain.CirFinLog;
import com.interlib.sso.domain.Reader;
import com.interlib.sso.service.CirFinLogService;
import com.interlib.sso.service.ReaderService;

/**
 * 读者财经日志的接口信息
 * @author USER
 *
 */
@Controller
@RequestMapping("api/cirfinlog")
public class CirFinLogInterface {
	
	
	@Autowired
	public ReaderService readerService;
	
	@Autowired
	public CirFinLogService cirFinLogService;
	
	
	
	
	//http://localhost:8089/onecard/api/cirfinlog/listCirFinLogForPager?rdid=pyx&rdpasswd=e10adc3949ba59abbe56e057f20f883e&appcode=interlib&startdate=2014-12-01&enddate=2015-01-20&pageno=1&pagesize=10&feetype=106&libcode=999&enc=0da2875feae38dee85b7322076032652
	@RequestMapping(value="/listCirFinLogForPager")
	public @ResponseBody List listCirFinLogForPager(HttpServletRequest req, HttpServletResponse response,
			@RequestParam(required=false, value="rdid") String rdid, //必须
			@RequestParam(required=false, value="rdpasswd") String rdpasswd, //必须
			@RequestParam(required=false, value="libcode") String libcode, //必须
			@RequestParam(required=false, value="appcode") String appcode, //必须
			@RequestParam(required=false, value="pageno") String pageno, //可不传,默认1
			@RequestParam(required=false, value="pagesize") String pagesize, //可不传, 默认10
			@RequestParam(required=false, value="feetype") String feetype, //可不传，默认返回全部
			@RequestParam(required=false, value="startdate") String startdate, //可不传，默认返回全部
			@RequestParam(required=false, value="enddate") String enddate) {//可不传，默认返回全部
		List<CirFinLog> cirfinlogList = new ArrayList<CirFinLog>();
		List<R> result = new ArrayList<R>(); 
		R r = new R();
		if(rdid == null){
			r.message="读者账号不能为空！"; 
			r.success="0";
			result.add(r);
			return result;
		}
		if(rdpasswd == null){
			r.message="读者密码不能为空！"; 
			r.success="0";
			result.add(r);
			return result;
		}
		
		Reader reader = readerService.getReader(rdid, (byte)1);
		boolean checkFlag = false;
		String rdPassword = "";
		if(reader !=null){
			try {
				if(reader.getRdPasswd() !=null )rdPassword = EncryptDecryptData.decryptWithCode(Constants.DES_STATIC_KEY, reader.getRdPasswd());// 转成明文;
				if(rdPassword != null)rdPassword = MD5Util.MD5Encode(rdPassword);//再把明文转换为MD5
				
				if(rdpasswd != null)checkFlag =rdpasswd.equals(rdPassword);
			} catch (Exception e) {
				//验证用户名和密码发生异常
				r.message="验证读者账号和密码发生异常！"; 
				r.success="0";
				result.add(r);
				return result;
			}
		}
		if(!checkFlag){
			r.message="读者账号或密码错误！"; 
			r.success="0";
			result.add(r);
			return result;
		}
		if(appcode == null) {
			r.message="应用代码不能为空！"; 
			r.success="0";
			result.add(r);
			return result;
		}
		CirFinLog cirFinLog = new CirFinLog();
		cirFinLog.setRdid(rdid);
		//currentPage
		if(pageno==null || "".equals(pageno)) {
			pageno = "1";
		}
		if(pagesize==null || "".equals(pagesize)) {
			pagesize="10";
		}
		
		int pagenum = Integer.parseInt(pageno);
		int size = Integer.parseInt(pagesize);
		Map params = new HashMap();
		params.put("rdid", rdid);
		params.put("feetype", feetype);
		params.put("startTime", startdate);
		params.put("endTime", enddate);
		params.put("maxNum",pagenum*size);
		params.put("currentNum", (pagenum-1)*size);
		cirfinlogList = cirFinLogService.queryPageCirFinLogSet(params);//自己写的SQL分页
		List<CirFinLogJsonBean> jsonBeans = convertCirFinLog(cirfinlogList);//转换输出格式
//		cirfinlogList = cirFinLogService.queryCirFinLogList(cirFinLog);//加入rdid 分页失败
		return jsonBeans;//自定封装成json格式的数据
	}

	/**
	 * 转换需要输出的对象
	 * @param cirfinlogList
	 * @return
	 */
	private List<CirFinLogJsonBean> convertCirFinLog(
			List<CirFinLog> cirfinlogList) {
		List<CirFinLogJsonBean> beans = new ArrayList<CirFinLogJsonBean>();
		if(cirfinlogList!=null){
			for(CirFinLog cirfinlog:cirfinlogList){
				CirFinLogJsonBean bean = new CirFinLogJsonBean();
				bean.id = cirfinlog.getId();
				bean.rdid = cirfinlog.getRdid();
				bean.rdname = cirfinlog.getRdname();
				bean.feetype = cirfinlog.getFeetype();//消费类型
				bean.fee = cirfinlog.getFee();//消费金额
				bean.regtime = TimeUtils.dateToString(cirfinlog.getRegtime(),"yyyy-MM-dd HH:mm:ss");//创建时间
				bean.regman = cirfinlog.getRegman();//操作员
				bean.regLib = cirfinlog.getRegLib();//操作馆
				bean.paySign = cirfinlog.getPaySign();//缴付标识
				bean.feeAppCode = cirfinlog.getFeeAppCode();//应用代码
				bean.paytype = cirfinlog.getPaytype();//支付类型
				bean.payId = cirfinlog.getPayId();//交易单号
				beans.add(bean);
			}
		}
		
		return beans;
	}
	
	
	@XmlRootElement(name="Result")
	static class R {
		public String message;
		public String success;
	}
	
	/**
	 * 需要返回CirFinLog字段的信息自定义
	 * @author USER
	 *
	 */
	@XmlRootElement(name="CirFinLogJsonBean")
	static class CirFinLogJsonBean {
		public String payId;//交易单号
		private Integer id;
		private String rdid;//读者账号
		private String rdname;//读者姓名
		private String feetype;//消费类型
		private Double fee;//消费金额
		private String regtime;//创建时间
		private String regman;//操作员
		private String regLib;//操作馆
		private Integer paySign;//缴付标识
		private String feeAppCode;//应用代码
		private Integer paytype;//支付类型
		private String tranId;
		
		
		public String getPayId() {
			return payId;
		}
		public void setPayId(String payId) {
			this.payId = payId;
		}
		public Integer getId() {
			return id;
		}
		public void setId(Integer id) {
			this.id = id;
		}
		public String getRdid() {
			return rdid;
		}
		public void setRdid(String rdid) {
			this.rdid = rdid;
		}
		public String getRdname() {
			return rdname;
		}
		public void setRdname(String rdname) {
			this.rdname = rdname;
		}
		public String getFeetype() {
			return feetype;
		}
		public void setFeetype(String feetype) {
			this.feetype = feetype;
		}
		public Double getFee() {
			return fee;
		}
		public void setFee(Double fee) {
			this.fee = fee;
		}
		public String getRegtime() {
			return regtime;
		}
		public void setRegtime(String regtime) {
			this.regtime = regtime;
		}
		public String getRegman() {
			return regman;
		}
		public void setRegman(String regman) {
			this.regman = regman;
		}
		public String getRegLib() {
			return regLib;
		}
		public void setRegLib(String regLib) {
			this.regLib = regLib;
		}
		public Integer getPaySign() {
			return paySign;
		}
		public void setPaySign(Integer paySign) {
			this.paySign = paySign;
		}
		public String getFeeAppCode() {
			return feeAppCode;
		}
		public void setFeeAppCode(String feeAppCode) {
			this.feeAppCode = feeAppCode;
		}
		public Integer getPaytype() {
			return paytype;
		}
		public void setPaytype(Integer paytype) {
			this.paytype = paytype;
		}
		public String getTranId() {
			return tranId;
		}
		public void setTranId(String tranId) {
			this.tranId = tranId;
		}
		
		
	}
	
}
