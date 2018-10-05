package com.interlib.sso.controller.system;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.Region;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.interlib.sso.common.Constants;
import com.interlib.sso.common.TimeUtils;
import com.interlib.sso.common.json.Jackson;
import com.interlib.sso.common.servlet.ServletUtil;
import com.interlib.sso.controller.BaseController;
import com.interlib.sso.domain.Authorization;
import com.interlib.sso.domain.CardGroup;
import com.interlib.sso.domain.ChargeType;
import com.interlib.sso.domain.CirFinLog;
import com.interlib.sso.domain.FinSettlementResult;
import com.interlib.sso.domain.FinType;
import com.interlib.sso.domain.LibCode;
import com.interlib.sso.domain.Reader;
import com.interlib.sso.domain.ReaderSession;
import com.interlib.sso.page.PageEntity;
import com.interlib.sso.service.AuthorizationService;
import com.interlib.sso.service.CardGroupService;
import com.interlib.sso.service.ChargeTypeService;
import com.interlib.sso.service.CirFinLogService;
import com.interlib.sso.service.FinTypeService;
import com.interlib.sso.service.LibCodeService;
import com.interlib.sso.service.ReaderService;


@Controller
@RequestMapping("admin/sys/cirfinlog")
public class CirFinLogController extends BaseController{

	private static final String LIST_VIEW = "admin/sys/cirfinlog/list";
	private static final String STATIS_VIEW = "admin/sys/cirfinlog/statistics";
	private static final String RESULT_VIEW = "admin/sys/cirfinlog/statisticsResult";
	private static final String CHARGELIST_VIEW = "admin/sys/cirfinlog/chargeList";//充值交易查询 ADD BY WJS 20170522
	private static final String MONETARY_VIEW="admin/sys/cirfinlog/monetaryList";//消费金额查询 add by pyx 20140227
	private static final String FINSETTLEMENT_VIEW = "admin/sys/cirfinlog/finSettlement";
	private static final String CIRFINLOG_LIST_VIEW = "admin/sys/cirfinlog/cirfinlogList";
	private static final String AUTOSTATISTICS_VIEW = "admin/sys/cirfinlog/autoStatistics";//自动统计 2014-05-17
	private static final String MORELIBFINSETTLE_VIEW = "admin/sys/cirfinlog/moreLibFinSettle";//多馆财经结算 20140710
	private static final String MORELIBFINSETTLE_DETAIL_VIEW = "admin/sys/cirfinlog/moreLibFinSettleDetail";//20140712
	private static final String CIRFINSTATISTICS_VIEW = "admin/sys/cirfinlog/cirFinStatistics";//流通财经统计
	private static final String CIRFINSTATISTICSRESULT_VIEW = "admin/sys/cirfinlog/cirFinStatisticsResult";//流通财经统计
	private static final String CHARGE_VIEW = "admin/sys/cirfinlog/charge";//充值类型查询 ADD BY WJS 20170920

	private static final Logger logger = Logger.getLogger(CirFinLogController.class);
	
	@Autowired
	public CirFinLogService cirFinLogService;
	
	@Autowired
	public LibCodeService libcodeService;
	
	@Autowired
	public FinTypeService finTypeService;
	
	@Autowired
	public ReaderService readerService;
	
	@Autowired
	public ChargeTypeService chargeTypeService;

	@Autowired
	public CardGroupService groupService;
	
	@Autowired
	public AuthorizationService authService;
	
	
	/**
	 * 流通财经统计界面
	 * @param request
	 * @param model
	 * @param cirFinLog
	 */
	@RequiresRoles("admin")
	@RequestMapping("/cirFinStatistics")
	public String cirfinStatistics(HttpServletRequest request,Model model, CirFinLog cirFinLog) {
		model.addAttribute(Constants.ACTIVE_MENU_KEY, "cirfinlog_cirFinStatistics");
		List<FinType> finTypeList = finTypeService.getAll();
		List<Reader> libUserList = readerService.getAllLlbUser(1);
		List<LibCode> simpleList = libcodeService.getSimpleInfo();
		List<Authorization> authAppList = authService.getAllSimple();
		model.addAttribute("finTypeList", finTypeList);
		model.addAttribute("libUserList", libUserList);
		model.addAttribute("simpleLibcode", simpleList).addAttribute("authAppList", authAppList);
		model.addAttribute("obj", cirFinLog);
		return CIRFINSTATISTICS_VIEW;
	}
	/**
	 * 流通财经统计执行
	 * @param request
	 * @param model
	 * @param cirFinLog
	 */
	@RequiresRoles("admin")
	@RequestMapping("/doCirFinStatistics")
	public String doCirFinStatistics(Model model, HttpServletRequest request, HttpServletResponse response, 
			CirFinLog cirFinLog) {
		model.addAttribute(Constants.ACTIVE_MENU_KEY, "cirfinlog_cirFinStatistics");
		
		//这里传过来的是多个regman，处理下
		String regman = cirFinLog.getRegman();
		if(regman != null) {
			if(regman.startsWith(",")) {
				regman = regman.substring(1);
			}
			String regmanArray [] = regman.split(","); 
			cirFinLog.setRegman(regman);
			model.addAttribute("regmanArray", regmanArray);
		}
		String feetype = cirFinLog.getFeetype();
		if(feetype != null) {
			if(feetype.startsWith(",")) {
				feetype = feetype.substring(1);
			}
			String feetypeArray [] = feetype.split(",");
			cirFinLog.setFeetype(feetype);
			model.addAttribute("feetypeArray", feetypeArray);
			List<FinType> finTypeList = finTypeService.getFeeTypeByMultiFeetype(feetypeArray);
			model.addAttribute("finTypeList", finTypeList);
		}
		String feeAppCode = cirFinLog.getFeeAppCode();
		if(feeAppCode != null){
			if(feeAppCode.startsWith(",")) {
				feeAppCode = feeAppCode.substring(1);
			}
			String appCodes [] = feeAppCode.split(",");
			List<Reader> regmanList = readerService.getAllLlbUser(1);
			List<Map<String, Object>> results = cirFinLogService.cirFinLogStatistics(cirFinLog); 
			Map<String, Object> otherResults = cirFinLogService.cirFinLogOtherStatistics(cirFinLog);
			Map<String, Object> totalResults = cirFinLogService.cirFinLogTotalStatistics(cirFinLog);
		
			if(otherResults != null) {
				results.add(otherResults);
			}
			results.add(totalResults);
			
			List<Authorization> authList = authService.getAppsByMultAppcode(appCodes);
			String today = TimeUtils.dateToString(new Date(), "yyyy-MM-dd");
			model.addAttribute("today", today);
			model.addAttribute("cirFinLog", cirFinLog).addAttribute("results", results);
			model.addAttribute("regmanList", regmanList);
			model.addAttribute("authList", authList);
		}
		return CIRFINSTATISTICSRESULT_VIEW;
	}
	/**
	 * 流通财经统计执行导出excel
	 * @param request
	 * @param model
	 * @param cirFinLog
	 */
	@RequiresRoles("admin")
	@RequestMapping("/exportCifFinStatisticsExcel")
	public String exportCifFinStatisticsExcel(Model model, HttpServletRequest request, HttpServletResponse response, 
			CirFinLog cirFinLog) {
		model.addAttribute(Constants.ACTIVE_MENU_KEY, "cirfinlog_cirFinStatistics");
		
		//这里传过来的是多个regman，处理下
		String regman = cirFinLog.getRegman();
		if(regman != null) {
			if(regman.startsWith(",")) {
				regman = regman.substring(1);
			}
			String regmanArray [] = regman.split(","); 
			cirFinLog.setRegman(regman);
			model.addAttribute("regmanArray", regmanArray);
		}
		String feetype = cirFinLog.getFeetype();
		if(feetype != null) {
			if(feetype.startsWith(",")) {
				feetype = feetype.substring(1);
			}
			String feetypeArray [] = feetype.split(",");
			cirFinLog.setFeetype(feetype);
			model.addAttribute("feetypeArray", feetypeArray);
			List<FinType> finTypeList = finTypeService.getFeeTypeByMultiFeetype(feetypeArray);
			model.addAttribute("finTypeList", finTypeList);
		}
		String feeAppCode = cirFinLog.getFeeAppCode();
		if(feeAppCode.startsWith(",")) {
			feeAppCode = feeAppCode.substring(1);
		}
		String appCodes [] = feeAppCode.split(",");
		List<Reader> regmanList = readerService.getAllLlbUser(1);
		List<Map<String, Object>> results = cirFinLogService.cirFinLogStatistics(cirFinLog); 
		Map<String, Object> otherResults = cirFinLogService.cirFinLogOtherStatistics(cirFinLog);
		Map<String, Object> totalResults = cirFinLogService.cirFinLogTotalStatistics(cirFinLog);
		
		if(otherResults != null) {
			results.add(otherResults);
		}
		results.add(totalResults);
		
		List<Authorization> authList = authService.getAppsByMultAppcode(appCodes);
		String today = TimeUtils.dateToString(new Date(), "yyyy-MM-dd");
		model.addAttribute("today", today);
		model.addAttribute("cirFinLog", cirFinLog).addAttribute("results", results);
		model.addAttribute("regmanList", regmanList);
		model.addAttribute("authList", authList);
		response.setHeader("Content-disposition","attachment; filename=doCirFinStatistics_" + new Date().getTime() + ".xls");
		return CIRFINSTATISTICSRESULT_VIEW;
	}
	
	/**
	 * 收费明细统计
	 */
	@RequiresRoles("admin")
	@RequestMapping("/list")
	public String list(Model model, CirFinLog cirFinLog) {
		//groupID 分组添加到 cirFinLog 对象里面
		model.addAttribute(Constants.ACTIVE_MENU_KEY, "cirfinlog_list");
		List<CirFinLog> pageList = cirFinLogService.queryCirFinLogList(cirFinLog);
		List<LibCode> simpleList = libcodeService.getSimpleInfo();
		List<FinType> finTypeList = finTypeService.getAll();
		//统计的总数
		CirFinLog StatisticsCFL=cirFinLogService.monetaryStatistics(cirFinLog);
		if(cirFinLog!=null){
			cirFinLog.setRdidCount(StatisticsCFL.getRdidCount());//人数
			cirFinLog.setRdidSum(StatisticsCFL.getRdidSum());//人次
			cirFinLog.setFeeSum(StatisticsCFL.getFeeSum());
		}
		CardGroup cg = new CardGroup();
		List<Map<String,String>> groups = groupService.queryGroupList(cg);
		model.addAttribute("groups", groups);//添加的分组的列表 2014-04-28
		model.addAttribute("simpleLibcode", simpleList).addAttribute("finTypeList", finTypeList);
		model.addAttribute("pageList", pageList);
		model.addAttribute("obj", cirFinLog);
		return LIST_VIEW;
	}
	/**
	 * 个人收费明细统计
	 */
	@RequiresRoles("admin")
	@RequestMapping("/personalList")
	public String personalList(Model model, CirFinLog cirFinLog, HttpServletRequest request) {
		//groupID 分组添加到 cirFinLog 对象里面
		model.addAttribute(Constants.ACTIVE_MENU_KEY, "cirfinlog_personalList");
		ReaderSession curOperator = (ReaderSession) request.getSession().getAttribute("READER_SESSION");
		cirFinLog.setRegman(curOperator.getReader().getRdId());//只能查当前登录的管理员的财经
		List<CirFinLog> pageList = cirFinLogService.queryCirFinLogList(cirFinLog);
		List<LibCode> simpleList = libcodeService.getSimpleInfo();
		List<FinType> finTypeList = finTypeService.getAll();
		//统计的总数
		CirFinLog StatisticsCFL=cirFinLogService.monetaryStatistics(cirFinLog);
		if(cirFinLog!=null){
			cirFinLog.setRdidCount(StatisticsCFL.getRdidCount());//人数
			cirFinLog.setRdidSum(StatisticsCFL.getRdidSum());//人次
			cirFinLog.setFeeSum(StatisticsCFL.getFeeSum());
		}
		CardGroup cg = new CardGroup();
		List<Map<String,String>> groups = groupService.queryGroupList(cg);
		model.addAttribute("groups", groups);//添加的分组的列表 2014-04-28
		model.addAttribute("simpleLibcode", simpleList).addAttribute("finTypeList", finTypeList);
		model.addAttribute("pageList", pageList);
		model.addAttribute("obj", cirFinLog);
		return "admin/sys/cirfinlog/personalList";
	}
	
	/**
	 * add 20140318
	 * 只有一个页数 
	 * 收费明细统计
	 */
	@RequiresRoles("admin")
	@RequestMapping("/cirfinlogList")
	public String cirfinloglist(Model model, CirFinLog cirFinLog) { 
		model.addAttribute(Constants.ACTIVE_MENU_KEY, "cirfinlogList");
		List<CirFinLog> pageList = cirFinLogService.queryCirFinLogList(cirFinLog);
		List<LibCode> simpleList = libcodeService.getSimpleInfo();
		List<FinType> finTypeList = finTypeService.getAll();
		model.addAttribute("simpleLibcode", simpleList).addAttribute("finTypeList", finTypeList);
		model.addAttribute("pageList", pageList);
		model.addAttribute("obj", cirFinLog);
		return CIRFINLOG_LIST_VIEW;
	}
	@RequiresRoles("admin")
	@RequestMapping("/cirfinlogListJson")
	public void cirfinloglistJson(Model model, CirFinLog cirFinLog,String feeType,String showCount,
			HttpServletResponse response) throws IOException { 
		model.addAttribute(Constants.ACTIVE_MENU_KEY, "cirfinlogList");
		if(cirFinLog!=null && feeType!=null && !"".equals(feeType)){
			cirFinLog.setFeetype(feeType);
			if(cirFinLog.getPage()!=null && showCount!=null && !"".equals(showCount))
				cirFinLog.getPage().setShowCount(Integer.parseInt(showCount));
		}
		List<CirFinLog> pageList = cirFinLogService.queryCirFinLogList(cirFinLog);
		List<LibCode> simpleList = libcodeService.getSimpleInfo();
		List<FinType> finTypeList = finTypeService.getAll();
		model.addAttribute("simpleLibcode", simpleList).addAttribute("finTypeList", finTypeList);
		model.addAttribute("pageList", pageList);
		model.addAttribute("obj", cirFinLog);
		ServletUtil.responseOut("utf-8", Jackson.getBaseJsonData(pageList), response);
	}
	
	
	/**
	 * 收费统计
	 * @param model
	 * @return
	 */
	@RequiresRoles("admin")
	@RequestMapping("/statistics")
	public String statistics(Model model) {
		model.addAttribute(Constants.ACTIVE_MENU_KEY, "cirfinlog_statistics");
		List<LibCode> simpleList = libcodeService.getSimpleInfo();
		List<FinType> finTypeList = finTypeService.getAll();
		//ADD BY 20131225
		List<Reader> readerList = readerService.getAllLlbUser(1);
		CardGroup cg = new CardGroup();
		List<Map<String,String>> groups = groupService.queryGroupList(cg);
		model.addAttribute("groups", groups);//添加的分组的列表 2014-05-04
		model.addAttribute("simpleLibcode", simpleList)
			 .addAttribute("finTypeList", finTypeList)
			 .addAttribute("readerList",readerList);
		return STATIS_VIEW;
	}
	/**
	 * 收费统计结果
	 * @param model
	 * @return
	 */
	@RequiresRoles("admin")
	@RequestMapping("/dostatistics")
	public String doStatistics(HttpServletResponse response, Model model, CirFinLog cirFinLog,String groupby) {
		model.addAttribute(Constants.ACTIVE_MENU_KEY, "cirfinlog_statistics");
		List<Map<String, String>> statisList =null;
		//ADD BY 20131230
		String[] feetypes = null;
		if(cirFinLog!=null && cirFinLog.getFeetype()!=null && !"".equals(cirFinLog.getFeetype())){
			feetypes = cirFinLog.getFeetype().split(",");
			cirFinLog.setFeetypes(feetypes);//ADD 2014-05-06
		}
		if("regLib".equals(groupby)){
			statisList = cirFinLogService.statisticsGroupByReglib(cirFinLog);
		}else if("rdtype".equals(groupby)){
			statisList = cirFinLogService.statisticsGroupByRdtype(cirFinLog);
		}else{
			statisList = cirFinLogService.statistics(cirFinLog);
			groupby="regman";
		}
		//List<Map<String, String>> 
		Map<String, Integer> colMap = new HashMap<String,Integer>();
		for(int i=0;i<statisList.size()-1;i++){
			Map map =statisList.get(i);
			String groupname = (String) map.get("GROUPNAME");
			if(colMap.get(groupname)!=null){
				int num =colMap.get(groupname)+1;
				colMap.put(groupname, num);
			}else{
				colMap.put((String) map.get("GROUPNAME"), 1);
			}
		}
		
		List<String> showColNameList=getShowColNameList(groupby);//收费统计最后显示的列名在这里规定
		//modify by 2014-05-07 控制显示财经类型
		List<FinType> finTypeList = new ArrayList<FinType>();
		if(feetypes!=null && feetypes.length>0){
			finTypeList=finTypeService.searchFeeType(feetypes);
		}else{
			finTypeList=finTypeService.getAllFeeType();
		}
		CardGroup groupbean = new CardGroup();
		if(cirFinLog != null && cirFinLog.getGroupID()!=null){
			groupbean = groupService.getGroupById(cirFinLog.getGroupID());//根据分组查出对应的分组的名字
		}
		model.addAttribute("groupbean", groupbean);//添加的分组的列表 2014-05-04
		model.addAttribute("cirFinLog", cirFinLog).addAttribute("statisList", statisList).addAttribute("groupby", groupby)
			.addAttribute("finTypeList", finTypeList).addAttribute("showColNameList",showColNameList)
			.addAttribute("colMap",colMap);
		return RESULT_VIEW;
	}
	
	/**
	 * //ADD BY 20131230
	 * @param groupby
	 * @return
	 */
	private List<String> getShowColNameList(String groupby) {
		List<String> showColName=new ArrayList<String>();
		if("regLib".equals(groupby)){//按照操作馆
			showColName.add("操作馆类型");
			showColName.add("读者类型");
			showColName.add("收款人");
		}else if("rdtype".equals(groupby)){//按照读者类型
			showColName.add("读者类型");
			showColName.add("操作馆类型");
			showColName.add("收款人");
		}else{//默认是按照收款人
			showColName.add("收款人");
			showColName.add("操作馆类型");
			showColName.add("读者类型");
		}
		showColName.add("财经类型");
		showColName.add("人次");
		showColName.add("总收款金额(单位：元)");
		return showColName;
	}

	/**
	 * 充值查询
	 * @param response
	 * @param model
	 * @param cirFinLog
	 * @return
	 */
	@RequiresRoles("admin")
	@RequestMapping("/chargeList")
	public String chargeList(HttpServletResponse response, 
			Model model, CirFinLog cirFinLog,ChargeType chargeType) {
		model.addAttribute(Constants.ACTIVE_MENU_KEY, "cirfinlog_chargelist");
		List<Map<String, Object>> list = chargeTypeService.queryPageList(chargeType);
		
		cirFinLog.setFeetype("106");//一卡通充值的类型
		List<CirFinLog> pageList = cirFinLogService.queryCirFinLogList(cirFinLog);
		model.addAttribute("chargeType",chargeType).addAttribute("list", list);
		model.addAttribute("cirFinLog", cirFinLog).addAttribute("pageList", pageList);
		model.addAttribute("obj", cirFinLog);
		return CHARGELIST_VIEW;
	}
	
	/**
	 * 充值查询
	 * @param response
	 * @param model
	 * @param cirFinLog
	 * @return
	 */
	@RequiresRoles("admin")
	@RequestMapping("/charge")
	public ModelAndView add(Model model) {
		model.addAttribute(Constants.ACTIVE_MENU_KEY, "cirfinlog_charge");
		List<Reader> libReader = readerService.getAllLlbUser(1);
		model.addAttribute("libReader", libReader);
		return new ModelAndView(CHARGE_VIEW);
	}	
	/**
	 * 消费金额查询
	 * ADD 2014-02-27
	 * @param response
	 * @param model
	 * @return
	 */
	@RequiresRoles("admin")
	@RequestMapping("/monetaryList")
	public String monetary(HttpServletResponse response,Model model, CirFinLog cirFinLog){
		model.addAttribute(Constants.ACTIVE_MENU_KEY,"cirfinlog_monetaryList");
		List<CirFinLog> pageList = cirFinLogService.monetaryList(cirFinLog);
		List<LibCode> simpleList = libcodeService.getSimpleInfo();
		//统计的总数
		CirFinLog StatisticsCFL=cirFinLogService.monetaryStatistics(cirFinLog);
		if(cirFinLog!=null){
			cirFinLog.setRdidCount(StatisticsCFL.getRdidCount());//人数
			cirFinLog.setRdidSum(StatisticsCFL.getRdidSum());//人次
			cirFinLog.setFeeSum(StatisticsCFL.getFeeSum());
		}
		CardGroup cg = new CardGroup();
		List<Map<String,String>> groups = groupService.queryGroupList(cg);
		model.addAttribute("groups", groups);//添加的分组的列表 2014-05-04
		model.addAttribute("simpleLibcode", simpleList);
		model.addAttribute("pageList", pageList);
		model.addAttribute("obj", cirFinLog);
		
		return MONETARY_VIEW;
	}
	
	@RequiresRoles("admin")
	@RequestMapping(value = "/exportMonetaryListExcel")
	public synchronized void exportMonetaryListExcel(HttpServletRequest request,HttpServletResponse response, CirFinLog cirFinLog){
		//统计的总数  modify 2014-03-03
		int count=cirFinLogService.getTotalCount();
		if(cirFinLog!=null && cirFinLog.getPage()!=null){
			if(count<65535){//工作表最大的行数
				cirFinLog.getPage().setShowCount(count);
			}else{
				cirFinLog.getPage().setShowCount(65535);
			}
		}
		List<CirFinLog> cirfinlogList = cirFinLogService.monetaryList(cirFinLog);
		CirFinLog StatisticsCFL=cirFinLogService.monetaryStatistics(cirFinLog);
		if(cirFinLog!=null){
			cirFinLog.setRdidCount(StatisticsCFL.getRdidCount());//人数
			cirFinLog.setRdidSum(StatisticsCFL.getRdidSum());//人次
			cirFinLog.setFeeSum(StatisticsCFL.getFeeSum());
		}
		String inFilePath = request.getSession().getServletContext().getRealPath("/jsp_tiles/excel/inFile/monetaryList.xls");
		String filename = "消费金额统计列表_"+System.currentTimeMillis()+".xls";
		
		OutputStream outputStream = null;
		InputStream inputStream = null;
		try {
			response.setContentType("application/vnd.ms-excel; charset=GBK");
			response.addHeader("Content-Disposition", "attachment;filename=" + new String(filename.getBytes("GBK"), "ISO8859-1"));
			outputStream = response.getOutputStream();
			inputStream = new FileInputStream(inFilePath);
			
			HSSFWorkbook workbook = new HSSFWorkbook(inputStream);
			HSSFSheet worksheet = workbook.getSheetAt(0);
			int size = cirfinlogList.size();
			for(int i=0; i<size; i++){
				CirFinLog cirfinlog = cirfinlogList.get(i);
				HSSFRow row = worksheet.createRow(i+1);
				row.createCell(0).setCellValue(cirfinlog.getRdname());
				row.createCell(1).setCellValue(cirfinlog.getRdid());
				row.createCell(2).setCellValue(cirfinlog.getFeeAppCode());
				row.createCell(3).setCellValue(cirfinlog.getFeetype());
				row.createCell(4).setCellValue(cirfinlog.getFee());
				row.createCell(5).setCellValue(cirfinlog.getRegman());
				//设置时间格式
				SimpleDateFormat format1=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
				row.createCell(6).setCellValue(format1.format(cirfinlog.getRegtime()));
			}
			
			workbook.write(outputStream);
		} catch (FileNotFoundException e) {
			logger.error("【结算中心】导出消费金额统计列表时未读取到模板文件！", e);
		} catch (IOException e) {
			logger.error("【结算中心】导出消费金额统计列表时出现IO流异常！", e);
		} finally {
			if(inputStream != null || outputStream != null){
				try {
					inputStream.close();
					outputStream.close();
				} catch (IOException e) {
					logger.error("【结算中心】导出消费金额统计列表关闭输出流时异常！", e);
				}
			}
		}
		
	}
	
	@RequiresRoles("admin")
	@RequestMapping(value = "/exportCirfinlogListExcel")
	public synchronized void exportCirfinlogListExcel(HttpServletRequest request,HttpServletResponse response, CirFinLog cirFinLog){
		//统计的总数  modify 2014-03-03
		int count=cirFinLogService.getCirFinLogCount(cirFinLog);
		if(count==0)count=1;

		if(cirFinLog!=null){
			PageEntity page= new PageEntity();//前台没有传这个，肯定null啦
			cirFinLog.setPage(page);
			if(count<65535){//工作表最大的行数
				cirFinLog.getPage().setShowCount(count);
			}else{
				cirFinLog.getPage().setShowCount(65535);
			}
		}
		List<CirFinLog> cirfinlogList = cirFinLogService.queryCirFinLogList(cirFinLog);
		String inFilePath = request.getSession().getServletContext().getRealPath("/jsp_tiles/excel/inFile/cirfinlogList.xls");
		String filename = "财经明细统计_"+System.currentTimeMillis()+".xls";
		
		OutputStream outputStream = null;
		InputStream inputStream = null;
		try {
			response.setContentType("application/vnd.ms-excel; charset=GBK");
			response.addHeader("Content-Disposition", "attachment;filename=" + new String(filename.getBytes("GBK"), "ISO8859-1"));
			outputStream = response.getOutputStream();
			inputStream = new FileInputStream(inFilePath);
			
			HSSFWorkbook workbook = new HSSFWorkbook(inputStream);
			HSSFSheet worksheet = workbook.getSheetAt(0);
			int size = cirfinlogList.size();
			for(int i=0; i<size; i++){
				CirFinLog cirfinlog = cirfinlogList.get(i);
				HSSFRow row = worksheet.createRow(i+1);
				row.createCell(0).setCellValue(cirfinlog.getRegman()+"");
				row.createCell(1).setCellValue(cirfinlog.getRegLib());
				row.createCell(2).setCellValue(cirfinlog.getRdid());
				row.createCell(3).setCellValue(cirfinlog.getFeetype());
				row.createCell(4).setCellValue(cirfinlog.getFee());
				String FeeMemoStr="";
				if("1_sale".equals(cirfinlog.getFeeMemo())){
					FeeMemoStr="早餐折扣";
				}else if("1_cost".equals(cirfinlog.getFeeMemo())){
					FeeMemoStr="早餐原价";
				}else if("2_cost".equals(cirfinlog.getFeeMemo())){
					FeeMemoStr="午餐原价";
				}else if("2_sale".equals(cirfinlog.getFeeMemo())){
					FeeMemoStr="午餐折扣";
				}else if("3_sale".equals(cirfinlog.getFeeMemo())){
					FeeMemoStr="晚餐折扣";
				}else if("3_cost".equals(cirfinlog.getFeeMemo())){
					FeeMemoStr="晚餐原价";
				}
				row.createCell(5).setCellValue(FeeMemoStr);
				String paySignStr="";
				if(cirfinlog.getPaySign()==0){
					paySignStr="未交付";
				}else if(cirfinlog.getPaySign()==1){
					paySignStr="已交付";
				}else if(cirfinlog.getPaySign()==2){
					paySignStr="已取消";
				}else if(cirfinlog.getPaySign()==3){
					paySignStr="已退还";
				}
				row.createCell(6).setCellValue(paySignStr);
				//设置时间格式
				SimpleDateFormat format1=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
				row.createCell(7).setCellValue(format1.format(cirfinlog.getRegtime()));
			}
			
			workbook.write(outputStream);
			outputStream.flush();
		} catch (FileNotFoundException e) {
			logger.error("【结算中心】导出财经明细统计列表时未读取到模板文件！", e);
		} catch (IOException e) {
			logger.error("【结算中心】导出财经明细统计列表时出现IO流异常！", e);
		} catch (IllegalStateException e) {
			logger.error("【结算中心】导出财经明细统计列表时出现异常！", e);
		}finally {
			if(inputStream != null || outputStream != null){
				try {
					inputStream.close();
					outputStream.close();
				} catch (IOException e) {
					logger.error("【结算中心】导出财经明细统计列表关闭输出流时异常！", e);
				}
			}
		}
	}
	
	/**
	 * 个人财经明细导出
	 * @param request
	 * @param response
	 * @param cirFinLog
	 */
	@RequiresRoles("admin")
	@RequestMapping(value = "/exportPersonalListExcel")
	public synchronized void exportPersonalListExcel(HttpServletRequest request,HttpServletResponse response, CirFinLog cirFinLog){
		ReaderSession curOperator = (ReaderSession) request.getSession().getAttribute("READER_SESSION");
		cirFinLog.setRegman(curOperator.getReader().getRdId());//只能查当前登录的管理员的财经
		//统计的总数  modify 2014-03-03
		int count=cirFinLogService.getCirFinLogCount(cirFinLog);
		if(count==0)count=1;
		String rdid = "";
		String regman = "";
		String regLib = "";
		String feetype = "";
		Integer paytype = 0;
		int groupID = 0;
		if(cirFinLog!=null){
			rdid = cirFinLog.getRdid();
			regman = cirFinLog.getRegman();
			regLib = cirFinLog.getRegLib();
			paytype = cirFinLog.getPaytype();
			if(cirFinLog.getFeetype()!=null)feetype = cirFinLog.getFeetype();
			if(cirFinLog.getGroupID()!=null)groupID = cirFinLog.getGroupID();
		}
		if(cirFinLog!=null && cirFinLog.getPage()!=null){
			if(count<65535){//工作表最大的行数
				cirFinLog.getPage().setShowCount(count);
			}else{
				cirFinLog.getPage().setShowCount(65535);
			}
		}else{
			cirFinLog=new CirFinLog();
			PageEntity page= new PageEntity();
			cirFinLog.setPage(page);
			if(count<65535){//工作表最大的行数
				cirFinLog.getPage().setShowCount(count);
			}else{
				cirFinLog.getPage().setShowCount(65535);
			}
		}
		cirFinLog.setRdid(rdid);
		cirFinLog.setRegman(regman);
		cirFinLog.setRegLib(regLib);
		cirFinLog.setFeetype(feetype);
		cirFinLog.setPaytype(paytype);
		if(groupID!=0)cirFinLog.setGroupID(groupID);
		List<CirFinLog> cirfinlogList = cirFinLogService.queryCirFinLogList(cirFinLog);
		String inFilePath = request.getSession().getServletContext().getRealPath("/jsp_tiles/excel/inFile/cirfinlogList.xls");
		String filename = "个人财经明细_"+System.currentTimeMillis()+".xls";
		
		OutputStream outputStream = null;
		InputStream inputStream = null;
		try {
			response.setContentType("application/vnd.ms-excel; charset=GBK");
			response.addHeader("Content-Disposition", "attachment;filename=" + new String(filename.getBytes("GBK"), "ISO8859-1"));
			outputStream = response.getOutputStream();
			inputStream = new FileInputStream(inFilePath);
			
			HSSFWorkbook workbook = new HSSFWorkbook(inputStream);
			HSSFSheet worksheet = workbook.getSheetAt(0);
			int size = cirfinlogList.size();
			for(int i=0; i<size; i++){
				CirFinLog cirfinlog = cirfinlogList.get(i);
				HSSFRow row = worksheet.createRow(i+1);
				row.createCell(0).setCellValue(cirfinlog.getRegman()+"");
				row.createCell(1).setCellValue(cirfinlog.getRegLib());
				row.createCell(2).setCellValue(cirfinlog.getRdid());
				row.createCell(3).setCellValue(cirfinlog.getFeetype());
				row.createCell(4).setCellValue(cirfinlog.getFee());
				String FeeMemoStr="";
				if("1_sale".equals(cirfinlog.getFeeMemo())){
					FeeMemoStr="早餐折扣";
				}else if("1_cost".equals(cirfinlog.getFeeMemo())){
					FeeMemoStr="早餐原价";
				}else if("2_cost".equals(cirfinlog.getFeeMemo())){
					FeeMemoStr="午餐原价";
				}else if("2_sale".equals(cirfinlog.getFeeMemo())){
					FeeMemoStr="午餐折扣";
				}else if("3_sale".equals(cirfinlog.getFeeMemo())){
					FeeMemoStr="晚餐折扣";
				}else if("3_cost".equals(cirfinlog.getFeeMemo())){
					FeeMemoStr="晚餐原价";
				}
				row.createCell(5).setCellValue(FeeMemoStr);
				String paySignStr="";
				if(cirfinlog.getPaySign()==0){
					paySignStr="未交付";
				}else if(cirfinlog.getPaySign()==1){
					paySignStr="已交付";
				}else if(cirfinlog.getPaySign()==2){
					paySignStr="已取消";
				}else if(cirfinlog.getPaySign()==3){
					paySignStr="已退还";
				}
				row.createCell(6).setCellValue(paySignStr);
				//设置时间格式
				SimpleDateFormat format1=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
				row.createCell(7).setCellValue(format1.format(cirfinlog.getRegtime()));
			}
			
			workbook.write(outputStream);
		} catch (FileNotFoundException e) {
			logger.error("【结算中心】导出个人财经明细时未读取到模板文件！", e);
		} catch (IOException e) {
			logger.error("【结算中心】导出个人财经明细时出现IO流异常！", e);
		} finally {
			if(inputStream != null || outputStream != null){
				try {
					inputStream.close();
					outputStream.close();
				} catch (IOException e) {
					logger.error("【结算中心】导出个人财经明细关闭输出流时异常！", e);
				}
			}
		}
	}
	
	//exportStaticsExcel
	/**
	 * 2015-04-08
	 * 财经统计报表
	 * @param request
	 * @param response
	 * @param cirFinLog
	 * @param groupby
	 */
	@RequiresRoles("admin")
	@RequestMapping(value = "/exportStaticsExcel")
	public synchronized void exportStaticsExcel(HttpServletRequest request,HttpServletResponse response, CirFinLog cirFinLog
			,String groupby){
		
		List<Map<String, String>> statisList =null;
		String[] feetypes = null;
		if(cirFinLog!=null && cirFinLog.getFeetype()!=null && !"".equals(cirFinLog.getFeetype())){
			feetypes = cirFinLog.getFeetype().split(",");
			cirFinLog.setFeetypes(feetypes);//ADD 2014-05-06
		}
		if("regLib".equals(groupby)){
			statisList = cirFinLogService.statisticsGroupByReglib(cirFinLog);
		}else if("rdtype".equals(groupby)){
			statisList = cirFinLogService.statisticsGroupByRdtype(cirFinLog);
		}else{
			statisList = cirFinLogService.statistics(cirFinLog);
			groupby="regman";
		}
		//List<Map<String, String>> 
		Map<String, Integer> colMap = new HashMap<String,Integer>();
		for(int i=0;i<statisList.size()-1;i++){
			Map map =statisList.get(i);
			String groupname = (String) map.get("GROUPNAME");
			if(colMap.get(groupname)!=null){
				int num =colMap.get(groupname)+1;
				colMap.put(groupname, num);
			}else{
				colMap.put((String) map.get("GROUPNAME"), 1);
			}
		}
		
		List<String> showColNameList=getShowColNameList(groupby);//收费统计最后显示的列名在这里规定
		//modify by 2014-05-07 控制显示财经类型
		List<FinType> finTypeList = new ArrayList<FinType>();
		if(feetypes!=null && feetypes.length>0){
			finTypeList=finTypeService.searchFeeType(feetypes);
		}else{
			finTypeList=finTypeService.getAllFeeType();
		}
		CardGroup groupbean = new CardGroup();
		if(cirFinLog != null && cirFinLog.getGroupID()!=null){
			groupbean = groupService.getGroupById(cirFinLog.getGroupID());//根据分组查出对应的分组的名字
		}
		
		int count = statisList.size();//统计的总数
		String feetype = "";
		String inFilePath = request.getSession().getServletContext().getRealPath("/jsp_tiles/excel/inFile/statistcsResult.xls");
		String filename = "财经 统计报表_"+System.currentTimeMillis()+".xls";
		
		OutputStream outputStream = null;
		InputStream inputStream = null;
		try {
			response.setContentType("application/vnd.ms-excel; charset=GBK");
			response.addHeader("Content-Disposition", "attachment;filename=" + new String(filename.getBytes("GBK"), "ISO8859-1"));
			outputStream = response.getOutputStream();
			inputStream = new FileInputStream(inFilePath);
			
			HSSFWorkbook workbook = new HSSFWorkbook(inputStream);
			HSSFSheet worksheet = workbook.getSheetAt(0);
			HSSFRow rowname = worksheet.createRow(2);//第二行开始
			for(int k=0; k<showColNameList.size(); k++){//设置列名
				String colname = showColNameList.get(k);
				rowname.createCell(k).setCellValue(colname);
			}
			int line = 0;
			for(int i=0; i<count; i++){//设置列内容
				//List<Map<String, String>>
				Map<String, String> resultMap = statisList.get(i);
				int mapsize = resultMap.size();
				Iterator iter = resultMap.entrySet().iterator();
				for (int j = 0; j< mapsize; j++){//转换下格式
					Map.Entry entry=  (Entry) iter.next();
					String key = (String) entry.getKey();
					Object value =  entry.getValue();
					resultMap.put(key, value.toString());
				}
				HSSFRow row = worksheet.createRow(line+3);
				String groupnamesign = (String)resultMap.get("GROUPNAMESIGN");
				feetype = resultMap.get("FEETYPE");
				if("1".equals(groupnamesign)){
					row.createCell(0).setCellValue("总计");
					row.createCell(1).setCellValue("");
					row.createCell(2).setCellValue("");
					for(FinType ft:finTypeList){
						feetype = resultMap.get("FEETYPE");
						if(feetype!=null){
							if(feetype.equals(ft.getFeeType())){
								row.createCell(3).setCellValue(ft.getDescribe());//消费类型的名字
							}
						}
					}
					row.createCell(4).setCellValue(resultMap.get("COUNT"));
					row.createCell(5).setCellValue(resultMap.get("NUM"));
					line++;
				}else if(feetype!=null && !"".equals(feetype)){
					row.createCell(0).setCellValue(resultMap.get("GROUPNAME"));
					row.createCell(1).setCellValue(resultMap.get("COLNAME1"));
					row.createCell(2).setCellValue(resultMap.get("COLNAME2"));
					for(FinType ft:finTypeList){
						if(feetype!=null){
							if(feetype.equals(ft.getFeeType())){
								row.createCell(3).setCellValue(ft.getDescribe());//消费类型的名字
							}
						}
					}
					row.createCell(4).setCellValue(resultMap.get("COUNT"));
					row.createCell(5).setCellValue(resultMap.get("NUM"));
					line++;
				}
			}
		    workbook.write(outputStream);
		} catch (FileNotFoundException e) {
			logger.error("【结算中心】导出财经 统计报表时未读取到模板文件！", e);
		} catch (IOException e) {
			logger.error("【结算中心】导出财财经 统计报表时出现IO流异常！", e);
		} finally {
			if(inputStream != null || outputStream != null){
				try {
					inputStream.close();
					outputStream.close();
						} catch (IOException e) { 
					e.printStackTrace();
					logger.error("【结算中心】财经 统计报表关闭输出流时异常！", e);
				}
			}
		}
		
		
	}
	
	
	/**
	 * 财经结算
	 * @param response
	 * @param model
	 * @param cirFinLog
	 * @return
	 */
	@RequiresRoles("admin")
	@RequestMapping("/finSettlement")
	public String finSettlement(HttpServletRequest request, HttpServletResponse response, Model model, CirFinLog cirFinLog) {
		
		model.addAttribute(Constants.ACTIVE_MENU_KEY,"cirfinlog_finSettlement");
		List<FinSettlementResult> pageList = cirFinLogService.finSettlement(cirFinLog);
		List<LibCode> simpleList = libcodeService.getSimpleInfo();
		FinSettlementResult result = cirFinLogService.finSettlementStatistics(cirFinLog);
		FinSettlementResult repairCost = cirFinLogService.finSettlementRepairCost(cirFinLog);//add by 20140321 应补差价
		if(pageList.size() > 0) {
			
			if(cirFinLog.getEndTime() == null || "".equals(cirFinLog.getEndTime())) {
				String endTime = pageList.get(pageList.size()-1).getDay();
				cirFinLog.setEndTime(endTime);
			}
			if(cirFinLog.getStartTime() == null || "".equals(cirFinLog.getStartTime())) {
				String startTime = pageList.get(0).getDay();
				cirFinLog.setStartTime(startTime);
			}
		}
		CardGroup cg = new CardGroup();
		List<Map<String,String>> groups = groupService.queryGroupList(cg);
		CardGroup groupbean = new CardGroup();
		if(cirFinLog != null && cirFinLog.getGroupID()!=null){
			groupbean = groupService.getGroupById(cirFinLog.getGroupID());//根据分组查出对应的分组的名字
		}
		model.addAttribute("groups", groups);//添加的分组的列表 2014-04-28
		model.addAttribute("groupbean",groupbean);//ADD 2014-05-04
		model.addAttribute("simpleLibcode", simpleList);
		model.addAttribute("pageList", pageList);
		model.addAttribute("obj", cirFinLog);
		model.addAttribute("result", result);
		model.addAttribute("repairCost", repairCost);
		return FINSETTLEMENT_VIEW;
	}
	@RequiresRoles("admin")
	@RequestMapping(value = "/exportFinSettlementExcel")
	public synchronized void exportFinSettlementExcel (HttpServletRequest request,
			HttpServletResponse response, CirFinLog cirFinLog) throws ServletException,IOException{
		List<FinSettlementResult> pageList = cirFinLogService.finSettlement(cirFinLog);
		FinSettlementResult result = cirFinLogService.finSettlementStatistics(cirFinLog);
		FinSettlementResult repairCost = cirFinLogService.finSettlementRepairCost(cirFinLog);//add by 20140321 应补差价
		if(cirFinLog.getEndTime() == null || "".equals(cirFinLog.getEndTime())) {
			String endTime = pageList.get(pageList.size()-1).getDay();
			cirFinLog.setEndTime(endTime);
		}
		if(cirFinLog.getStartTime() == null || "".equals(cirFinLog.getStartTime())) {
			String startTime = pageList.get(0).getDay();
			cirFinLog.setStartTime(startTime);
		}
		
		String inFilePath = request.getSession().getServletContext().getRealPath("/jsp_tiles/excel/inFile/finSettlementList.xls");
		String filename = "财经结算报表_"+System.currentTimeMillis()+".xls";
		
		OutputStream outputStream  = null;
		InputStream inputStream = null;
		try {
			response.setContentType("application/vnd.ms-excel; charset=GBK");
			response.addHeader("Content-Disposition", "attachment;filename=" + new String(filename.getBytes("GBK"), "ISO8859-1"));

			outputStream  = response.getOutputStream();
			inputStream = new FileInputStream(inFilePath);
		
			HSSFWorkbook workbook = new HSSFWorkbook(inputStream);
			HSSFCellStyle style = workbook.createCellStyle();
			style.setAlignment(HSSFCellStyle.VERTICAL_CENTER);
			style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			HSSFSheet worksheet = workbook.getSheetAt(0);
			HSSFRow hssRow = worksheet.createRow(0);
			worksheet.addMergedRegion(new Region(     
                    0, //first row (0-based)       
                   (short)0, //first column  (0-based)       
                    0, //last row (0-based)    
                   (short)8  //last column  (0-based)       
            )); 
			String dateFormat = "日统计";
			if(cirFinLog.getDateFormat().equals("DAY")) {
				dateFormat = "日统计";
			} else if(cirFinLog.getDateFormat().equals("MONTH")) {
				dateFormat = "月统计";
			} else if(cirFinLog.getDateFormat().equals("YEAR")) {
				dateFormat = "年统计";
			}
			if(cirFinLog.getEndTime() == null || "".equals(cirFinLog.getEndTime())) {
				String endTime = pageList.get(pageList.size()-1).getDay();
				cirFinLog.setEndTime(endTime);
			}
			if(cirFinLog.getStartTime() == null || "".equals(cirFinLog.getStartTime())) {
				String startTime = pageList.get(0).getDay();
				cirFinLog.setStartTime(startTime);
			}
			String firstRow = dateFormat + "：【" + cirFinLog.getStartTime() + "】 至 【" + cirFinLog.getEndTime() + "】";
			
			if(cirFinLog.getRegman() != null && !"".equals(cirFinLog.getRegman())) {
				firstRow = firstRow + " 收费人员：【" + cirFinLog.getRegman() + "】"; 
			}
			
			hssRow.createCell(0).setCellValue(firstRow);
			
			int size = pageList.size();
			for(int i = 0; i < size; i ++){
				FinSettlementResult r = pageList.get(i);
				HSSFRow row = worksheet.createRow(i+2);
				row.createCell(0).setCellValue(r.getDay());
				row.createCell(1).setCellValue(r.getMorningSale());
				row.createCell(2).setCellValue(r.getMorningCost());
				row.createCell(3).setCellValue(r.getNoonSale());
				row.createCell(4).setCellValue(r.getNoonCost());
				row.createCell(5).setCellValue(r.getNightSale());
				row.createCell(6).setCellValue(r.getNightCost());
				row.createCell(7).setCellValue(r.getDaySale());
				row.createCell(8).setCellValue(r.getDayCost());
				row.createCell(9).setCellValue(r.getSumFee());
				//设置时间格式
			}
			
			workbook.write(outputStream);
		} catch (FileNotFoundException e) {
			logger.error("【结算中心】导出消费金额统计列表时未读取到模板文件！", e);
		} catch (IOException e) {
			logger.error("【结算中心】导出消费金额统计列表时出现IO流异常！", e);
		} finally {
			if(inputStream != null || outputStream != null){
				try {
					inputStream.close();
					outputStream.close();
					
				} catch (IOException e) {
					logger.error("【结算中心】导出消费金额统计列表关闭输出流时异常！", e);
				}
			}
		}
	}
	
	/**
	 * 自动收费统计
	 */
	@RequiresRoles("admin")
	@RequestMapping("/autoStatistics")//自动统计功能
	public String autoStatistics(Model model, CirFinLog cirFinLog) {
		//groupID 分组添加到 cirFinLog 对象里面
		model.addAttribute(Constants.ACTIVE_MENU_KEY, "cirfinlog_autoStatistics");
		int count = cirFinLogService.autoStatisticsCount(cirFinLog);
		if(cirFinLog!=null){
			if(cirFinLog.getPage()!=null){
				cirFinLog.getPage().setTotalResult(count);
			}else{
				PageEntity page = new PageEntity();
				page.setTotalResult(count);//总的记录数
				cirFinLog.setPage(page);
			}
		}
		cirFinLog.setFirstResult(cirFinLog.getPage().getCurrentResult());//从第几条记录开始查询
		cirFinLog.setMaxResult(cirFinLog.getPage().getShowCount()*cirFinLog.getPage().getCurrentPage());//查询到区间值
		
		List<CirFinLog> pageList = cirFinLogService.autoStatistics(cirFinLog);
		List<LibCode> libs = libcodeService.getStaticsLibCodeSet();
		//对结果集进行控制
		Map result = formatAndStatistics(pageList,libs);
		pageList = (List<CirFinLog>)result.get("pageList");
		Map totalFeeResult = (Map) result.get("totalFeeResult");//小计的集合
		//20140627 总计的集合  统计全部
		cirFinLog.setFirstResult(0);//从第几条记录开始查询
		cirFinLog.setMaxResult(count);//查询全部
		List<CirFinLog> allList = cirFinLogService.autoStatistics(cirFinLog);
		result = formatAndStatistics(allList,libs);
		Map alltotalFeeResult = (Map) result.get("totalFeeResult");//小计的集合
		
		List<LibCode> simpleList = libcodeService.getSimpleInfo();
		List<FinType> finTypeList = finTypeService.getAll();
		CardGroup cg = new CardGroup();
		List<Map<String,String>> groups = groupService.queryGroupList(cg);
		model.addAttribute("groups", groups);//添加的分组的列表 
		model.addAttribute("simpleLibcode", libs).addAttribute("finTypeList", finTypeList);
		model.addAttribute("pageList", pageList);
		model.addAttribute("obj", cirFinLog);
		model.addAttribute("libs",libs);//新添加的数据
		model.addAttribute("totalFeeResult",totalFeeResult);
		model.addAttribute("alltotalFeeResult", alltotalFeeResult);//总计的集合 20140627
		return AUTOSTATISTICS_VIEW;
	}
	
	/**
	 * 20140710
	 * 多馆财经结算
	 */
	@RequiresRoles("admin")
	@RequestMapping("/moreLibFinSettle")//多馆财经结算
	public String moreLibFinSettle(HttpServletRequest request,Model model, CirFinLog cirFinLog) {
		model.addAttribute(Constants.ACTIVE_MENU_KEY, "cirfinlog_moreLibFinSettle");
		ReaderSession currReader = (ReaderSession) request.getSession().getAttribute("READER_SESSION");
		Map<String, String> params = new HashMap<String, String>();
		List<LibCode> libs = libcodeService.getStaticsLibCodeSet();
		String colNames = getColNamesByLibs(libs);
		params.put("colNames", colNames);//显示要显示的动态列和对应的名字
		params.put("orgLib","");//当前馆名字 不选择可以查询全部的信息
		if(currReader!=null && currReader.getReader()!=null &&  currReader.getReader().getRdLib()!=null){
			params.put("orgLib",currReader.getReader().getRdLib());//当前馆名字 不选择可以查询全部的信息
		}
		if(cirFinLog!=null){
			params.put("startTime", cirFinLog.getStartTime());
			params.put("endTime", cirFinLog.getEndTime());
			if(cirFinLog.getGroupID()!=null)params.put("groupID", cirFinLog.getGroupID()+"");
			if(cirFinLog.getRegLib()!=null)params.put("orgLib",cirFinLog.getRegLib());//查询条件的信息
			
		}
		//查询的条件和动态显示列项sql
		List<Map<String, String>> pageList = cirFinLogService.moreLibFinSettle(params);
		CardGroup cg = new CardGroup();
		List<Map<String,String>> groups = groupService.queryGroupList(cg);
		model.addAttribute("groups", groups);//添加的分组的列表 
		model.addAttribute("pageList", pageList);
		model.addAttribute("simpleLibcode", libs);//查询条件操作馆的集合
		model.addAttribute("libs",libs);//新添加的数据
		model.addAttribute("obj", cirFinLog);
		return MORELIBFINSETTLE_VIEW;
	}
	
	private String getColNamesByLibs(List<LibCode> libs) {
		//sum(case  when c.reglib='999' then fee else 0 end)as reg_999,
		String colname = "";
		for(int i=0;i<libs.size();i++){
			LibCode lib = libs.get(i);
			colname +=" sum(case  when c.reglib='"+lib.getLibCode()+"' then fee else 0 end)as LIBNAME"+i+", ";
		}
		return colname;
	}
	@RequiresRoles("admin")
	@RequestMapping("/exportMoreLibFinSettleExcel")//多馆财经结算报表
	public synchronized void exportMoreLibFinSettleExcel(HttpServletRequest request,HttpServletResponse response,
			Model model,CirFinLog cirFinLog){
		//当前馆，默认显示当前馆的信息 20140714
		ReaderSession currReader = (ReaderSession) request.getSession().getAttribute("READER_SESSION");
		Map<String, String> params = new HashMap<String, String>();
		List<LibCode> libs = libcodeService.getStaticsLibCodeSet();
		String colNames = getColNamesByLibs(libs);
		params.put("colNames", colNames);//显示要显示的动态列和对应的名字
		params.put("orgLib","");//当前馆名字 不选择可以查询全部的信息
		if(currReader!=null && currReader.getReader()!=null &&  currReader.getReader().getRdLib()!=null){
			params.put("orgLib",currReader.getReader().getRdLib());//当前馆名字 不选择可以查询全部的信息
		}
		if(cirFinLog!=null){
			params.put("startTime", cirFinLog.getStartTime());
			params.put("endTime", cirFinLog.getEndTime());
			if(cirFinLog.getGroupID()!=null)params.put("groupID", cirFinLog.getGroupID()+"");
			if(cirFinLog.getRegLib()!=null)params.put("orgLib",cirFinLog.getRegLib());//查询条件的信息
			
		}
		//查询的条件和动态显示列项sql
		List<Map<String, String>> pageList = cirFinLogService.moreLibFinSettle(params);
		String inFilePath = request.getSession().getServletContext().getRealPath("/jsp_tiles/excel/inFile/moreLibFinSettle.xls");
		String filename = "多馆财经结算报表_"+System.currentTimeMillis()+".xls";
		
		OutputStream outputStream = null;
		InputStream inputStream = null;
		try {
			response.setContentType("application/vnd.ms-excel; charset=GBK");
			response.addHeader("Content-Disposition", "attachment;filename=" + new String(filename.getBytes("GBK"), "ISO8859-1"));
			outputStream = response.getOutputStream();
			inputStream = new FileInputStream(inFilePath);
			
			HSSFWorkbook workbook = new HSSFWorkbook(inputStream);
			HSSFCellStyle style = workbook.createCellStyle();
			style.setAlignment(HSSFCellStyle.VERTICAL_CENTER);
			style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			HSSFSheet worksheet = workbook.getSheetAt(0);
			HSSFRow hssRow = worksheet.createRow(0);//设置标题栏
			hssRow.createCell(0).setCellValue("所属馆名称");
			for(int h=0;h<libs.size();h++){//动态的生成标题栏
				hssRow.createCell(h+1).setCellValue(libs.get(h).getSimpleName());
			}
			hssRow.createCell(libs.size()+1).setCellValue("馆小计");
			int size = pageList.size();
			for(int i = 0; i < size; i ++){//循环显示数据
				Map<String, String> map = pageList.get(i);
				HSSFRow row = worksheet.createRow(i+1);//设置标题栏
				row.createCell(0).setCellValue(map.get("SIMPLENAME"));
				for(int n=0;n<libs.size();n++){//
					Object val = map.get("LIBNAME"+n);//数据查询到集合是一个对象类型的数据
					int v = Integer.parseInt(val.toString());
					if(v>0){
						row.createCell(n+1).setCellValue("收取"+val.toString());
					}else if(v<0){
						row.createCell(n+1).setCellValue("退还"+(-v));
					}else{
						row.createCell(n+1).setCellValue(val.toString());
					}
					
				}
				Object total_fee = map.get("TOTAL_FEE");
				row.createCell(libs.size()+1).setCellValue(total_fee.toString());
			}
			
			workbook.write(outputStream);//流的方式写入excel文件
		}catch (FileNotFoundException e) {
			logger.error("【结算中心】多馆财经结算报表时未读取到模板文件！", e);
		} catch (IOException e) {
			logger.error("【结算中心】多馆财经结算报表时出现IO流异常！", e);
		} finally {
			if(inputStream != null || outputStream != null){
				try {
					inputStream.close();
					outputStream.close();
				} catch (IOException e) {
					logger.error("【结算中心】多馆财经结算报表关闭输出流时异常！", e);
				}
			}
		}
	}
	
	/**
	 * 20140712
	 * @param model
	 * @param cirFinLog
	 * @return
	 */
	@RequiresRoles("admin")
	@RequestMapping("/moreLibFinSettleDetail")//多馆财经结算明细
	public String moreLibFinSettleDetail(HttpServletRequest request,Model model, CirFinLog cirFinLog) {
		model.addAttribute(Constants.ACTIVE_MENU_KEY, "cirfinlog_moreLibFinSettleDetail");
		//当前馆，默认显示当前馆的信息 20140714
		ReaderSession currReader = (ReaderSession) request.getSession().getAttribute("READER_SESSION");
		Map<String, String> params = new HashMap<String, String>();
		List<LibCode> libs = libcodeService.getStaticsLibCodeSet();
		String colNames = getColNamesByLibs(libs);
		params.put("colNames", colNames);//显示要显示的动态列和对应的名字
		params.put("orgLib","");//当前馆名字 不选择可以查询全部的信息
		if(currReader!=null && currReader.getReader()!=null &&  currReader.getReader().getRdLib()!=null){
			params.put("orgLib",currReader.getReader().getRdLib());//当前馆名字 不选择可以查询全部的信息
		}
		if(cirFinLog!=null){
			params.put("startTime", cirFinLog.getStartTime());
			params.put("endTime", cirFinLog.getEndTime());
			if(cirFinLog.getGroupID()!=null)params.put("groupID", cirFinLog.getGroupID()+"");
			if(cirFinLog.getRegLib()!=null)params.put("orgLib",cirFinLog.getRegLib());//查询条件的信息
			
		}
		//查询的条件和动态显示列项sql
		List<Map<String, String>> pageList = cirFinLogService.moreLibFinSettleDetail(params);
		CardGroup cg = new CardGroup();
		List<Map<String,String>> groups = groupService.queryGroupList(cg);
		model.addAttribute("groups", groups);//添加的分组的列表 
		model.addAttribute("pageList", pageList);
		model.addAttribute("simpleLibcode", libs);//查询条件操作馆的集合
		model.addAttribute("libs",libs);//新添加的数据
		model.addAttribute("obj", cirFinLog);
		return MORELIBFINSETTLE_DETAIL_VIEW;
	}
	
	/**
	 * @param request
	 * @param response
	 * @param model
	 * @param cirFinLog
	 */
	@RequiresRoles("admin")
	@RequestMapping("/exportmoreLibFinSettleDetailExcel")//多馆财经结算报表
	public synchronized void exportmoreLibFinSettleDetailExcel(HttpServletRequest request,HttpServletResponse response,
			Model model,CirFinLog cirFinLog){
		//当前馆，默认显示当前馆的信息 20140714
		ReaderSession currReader = (ReaderSession) request.getSession().getAttribute("READER_SESSION");
		Map<String, String> params = new HashMap<String, String>();
		List<LibCode> libs = libcodeService.getStaticsLibCodeSet();
		String colNames = getColNamesByLibs(libs);
		params.put("colNames", colNames);//显示要显示的动态列和对应的名字
		params.put("orgLib","");//当前馆名字 不选择可以查询全部的信息
		if(currReader!=null && currReader.getReader()!=null &&  currReader.getReader().getRdLib()!=null){
			params.put("orgLib",currReader.getReader().getRdLib());//当前馆名字 不选择可以查询全部的信息
		}
		if(cirFinLog!=null){
			params.put("startTime", cirFinLog.getStartTime());
			params.put("endTime", cirFinLog.getEndTime());
			if(cirFinLog.getGroupID()!=null)params.put("groupID", cirFinLog.getGroupID()+"");
			if(cirFinLog.getRegLib()!=null)params.put("orgLib",cirFinLog.getRegLib());//查询条件的信息
			
		}
		//查询的条件和动态显示列项sql
		List<Map<String, String>> pageList = cirFinLogService.moreLibFinSettleDetail(params);
		String inFilePath = request.getSession().getServletContext().getRealPath("/jsp_tiles/excel/inFile/moreLibFinSettle.xls");
		String filename = "多馆财经明细报表_"+System.currentTimeMillis()+".xls";
		
		OutputStream outputStream = null;
		InputStream inputStream = null;
		try {
			response.setContentType("application/vnd.ms-excel; charset=GBK");
			response.addHeader("Content-Disposition", "attachment;filename=" + new String(filename.getBytes("GBK"), "ISO8859-1"));
			outputStream = response.getOutputStream();
			inputStream = new FileInputStream(inFilePath);
			
			HSSFWorkbook workbook = new HSSFWorkbook(inputStream);
			HSSFCellStyle style = workbook.createCellStyle();
			style.setAlignment(HSSFCellStyle.VERTICAL_CENTER);
			style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			HSSFSheet worksheet = workbook.getSheetAt(0);
			HSSFRow hssRow = worksheet.createRow(0);//设置标题栏
			hssRow.createCell(0).setCellValue("所属馆名称");
			hssRow.createCell(1).setCellValue("消费类型");
			for(int h=0;h<libs.size();h++){//动态的生成标题栏
				hssRow.createCell(h+2).setCellValue(libs.get(h).getSimpleName());
			}
			hssRow.createCell(libs.size()+2).setCellValue("小计");
			int size = pageList.size();
			for(int i = 0; i < size; i ++){//循环显示数据
				Map<String, String> map = pageList.get(i);
				HSSFRow row = worksheet.createRow(i+1);//设置标题栏
				row.createCell(0).setCellValue(map.get("SIMPLENAME"));
				row.createCell(1).setCellValue(map.get("FEETYPENAME"));
				for(int n=0;n<libs.size();n++){//
					Object val = map.get("LIBNAME"+n);//数据查询到集合是一个对象类型的数据
					int v = Integer.parseInt(val.toString());
					if(v>0){
						row.createCell(n+2).setCellValue("收取"+val.toString());
					}else if(v<0){
						row.createCell(n+2).setCellValue("退还"+(-v));
					}else{
						row.createCell(n+2).setCellValue(val.toString());
					}
					
				}
				Object total_fee = map.get("TOTAL_FEE");
				row.createCell(libs.size()+2).setCellValue(total_fee.toString());
			}
			
			workbook.write(outputStream);//流的方式写入excel文件
		}catch (FileNotFoundException e) {
			logger.error("【结算中心】多馆财经明细报表时未读取到模板文件！", e);
		} catch (IOException e) {
			logger.error("【结算中心】多馆财经明细报表时出现IO流异常！", e);
		} finally {
			if(inputStream != null || outputStream != null){
				try {
					inputStream.close();
					outputStream.close();
				} catch (IOException e) {
					logger.error("【结算中心】多馆财经明细报表关闭输出流时异常！", e);
				}
			}
		}
	}
	

	/**
	 * 自动统计导出数据excel
	 * @param model
	 * @param cirFinLog
	 */
	@RequiresRoles("admin")
	@RequestMapping("/exportAutoStatistcsExcel")//自动统计功能
	public synchronized void  exportAutoStatistcsExcel(HttpServletRequest request,HttpServletResponse response,Model model, CirFinLog cirFinLog) {
		model.addAttribute(Constants.ACTIVE_MENU_KEY, "autoStatistics");
		int count = cirFinLogService.autoStatisticsCount(cirFinLog);
		if(cirFinLog!=null){
			if(cirFinLog.getPage()!=null){
				cirFinLog.getPage().setTotalResult(count);
			}else{
				PageEntity page = new PageEntity();
				page.setTotalResult(count);
				cirFinLog.setPage(page);
			}
		}
		cirFinLog.setFirstResult(0);//開始查詢的記錄 从0开始
		cirFinLog.setMaxResult(count);//查詢的最大的頁數 查询所有
		
		List<CirFinLog> pageList = cirFinLogService.autoStatistics(cirFinLog);
		List<LibCode> libs = libcodeService.getStaticsLibCodeSet();
		//对结果集进行控制
		Map result = formatAndStatistics(pageList,libs);
		pageList = (List<CirFinLog>)result.get("pageList");
		Map totalFeeResult = (Map) result.get("totalFeeResult");
		
		String inFilePath = request.getSession().getServletContext().getRealPath("/jsp_tiles/excel/inFile/autoStatistcs.xls");
		String filename = "多馆自动结算明细报表_"+System.currentTimeMillis()+".xls";
		
		OutputStream outputStream = null;
		InputStream inputStream = null;
		try {
			response.setContentType("application/vnd.ms-excel; charset=GBK");
			response.addHeader("Content-Disposition", "attachment;filename=" + new String(filename.getBytes("GBK"), "ISO8859-1"));
			outputStream = response.getOutputStream();
			inputStream = new FileInputStream(inFilePath);
			
			HSSFWorkbook workbook = new HSSFWorkbook(inputStream);
			HSSFCellStyle style = workbook.createCellStyle();
			style.setAlignment(HSSFCellStyle.VERTICAL_CENTER);
			style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			HSSFSheet worksheet = workbook.getSheetAt(0);
			worksheet.setColumnWidth(0,10 * 256);//设置单元格的宽度
			HSSFRow hssRow = worksheet.createRow(0);//设置标题栏
			hssRow.setHeight((short)(35*20));//设置单元格的高度
			hssRow.createCell(0).setCellValue("读者账号");
			
			for(int h=0;h<libs.size();h++){//动态的生成标题栏
				worksheet.setColumnWidth(h+1,20 * 256);//设置单元格的宽度
				hssRow.createCell(h+1).setCellValue(libs.get(h).getSimpleName()+"费用(单位：元)");
			}
			int size = pageList.size();
			for(int i = 0; i < size; i ++){//循环显示数据
				CirFinLog r = pageList.get(i);
				worksheet.setColumnWidth(0,10 * 256);//设置单元格的宽度
				HSSFRow row = worksheet.createRow(i+1);//设置标题栏
				row.createCell(0).setCellValue(r.getRdid());
				for(int n=0;n<libs.size();n++){//
					LibCode lib = libs.get(n);
					String[] reglibSet = r.getReglibSet().split(",");
					String[] totalfeeSet = r.getTotalfeeSet().split(",");
					for(int j=0;j<reglibSet.length;j++){
						String reglib = reglibSet[j];
						if(reglib.equals(lib.getLibCode())){
							worksheet.setColumnWidth(n+1,20 * 256);//设置单元格的宽度
							row.createCell(n+1).setCellValue(totalfeeSet[j]);
						}
					}
				}
			}
			//添加统计数据
			if(totalFeeResult.size()>0){
				worksheet.setColumnWidth(0,10 * 256);//设置单元格的宽度
				HSSFRow row = worksheet.createRow(size+2);//总计
				row.createCell(0).setCellValue("总计");
				for(int n=0;n<libs.size();n++){//
					LibCode lib = libs.get(n);
					worksheet.setColumnWidth(n+1,20 * 256);//设置单元格的宽度
					long val = (Long) totalFeeResult.get(lib.getLibCode());
					if(val > 0){
						row.createCell(n+1).setCellValue("应收取"+val);
					}else if(val < 0){
						row.createCell(n+1).setCellValue("应退还"+(-val));
					}else if(val == 0){
						row.createCell(n+1).setCellValue(val);
					}
				}
			}
			workbook.write(outputStream);//流的方式写入excel文件
		}catch (FileNotFoundException e) {
			logger.error("【结算中心】多馆财经明细报表时未读取到模板文件！", e);
		} catch (IOException e) {
			logger.error("【结算中心】多馆财经明细报表时出现IO流异常！", e);
		} finally {
			if(inputStream != null || outputStream != null){
				try {
					inputStream.close();
					outputStream.close();
				} catch (IOException e) {
					logger.error("【结算中心】多馆财经明细报表关闭输出流时异常！", e);
				}
			}
		}
		
	}
	
	
	/**
	 * 2014-05-30
	 * @param pageList
	 * @param libs
	 * @return
	 */
	private Map formatAndStatistics(List<CirFinLog> pageList,
			List<LibCode> libs) {
		Map map = new HashMap();
		List<CirFinLog> temp = new ArrayList<CirFinLog>();
		Map totalFeeResult = new HashMap();
		int n = 0;
		for(Iterator it=pageList.iterator();it.hasNext();){
			CirFinLog c = (CirFinLog) it.next();
			String reglibSet = c.getReglibSet();//每个读者统计对应的馆
			String totalfeeSet = c.getTotalfeeSet();//每个读者统计对应的馆的费用值
			if(n==0){
				totalFeeResult.put("totalrdfee",c.getSumFee());
			}else{
				if(totalFeeResult.get("totalrdfee")!=null){
					Double fee = (Double) totalFeeResult.get("totalrdfee");
					totalFeeResult.put("totalrdfee",c.getSumFee()+fee);
				}else{
					if(c.getSumFee()!=0){
						totalFeeResult.put("totalrdfee",c.getSumFee()+0);
					}else{
						totalFeeResult.put("totalrdfee",0);
					}
				}
			}
			if(reglibSet!=null && reglibSet.split(",").length<=libs.size()){//集合的馆数量小于或者等于总显示馆数量
				for(int i=0;i<libs.size();i++){
					LibCode lib = libs.get(i);
					String libcode = lib.getLibCode();
					if(libcode!=null && !reglibSet.contains(libcode)){//没有包括在里面，手动添加到集合
						reglibSet = reglibSet+","+libcode;
						totalfeeSet = totalfeeSet+",0";
						c.setReglibSet(reglibSet);
						c.setTotalfeeSet(totalfeeSet);
						double val = totalFeeResult.get(libcode)==null?0:(Double) totalFeeResult.get(libcode);
						totalFeeResult.put(libcode,0+val);
					}else if(libcode!=null && reglibSet.contains(libcode)){//统计有相应的值，手动进行累计
						double val = totalFeeResult.get(libcode)==null?0:(Double) totalFeeResult.get(libcode);
						Double t = getReglibFee(reglibSet.split(","),libcode,totalfeeSet.split(","));
						totalFeeResult.put(libcode,t+val);
					}
				}
			}
			temp.add(c);
			n++;
		}
		
		map.put("pageList", temp);
		map.put("totalFeeResult", totalFeeResult);
		return map;
	}
	
	private static Double getReglibFee(String[] reglibSet, String s, String[] libs) {
		double l = 0;
		for(int k=0;k<reglibSet.length;k++){
			String lib = reglibSet[k];
			if(lib.equals(s)){
				l = Double.parseDouble(libs[k]);
			}
		}
		return l;
	}
	
	
	
}
