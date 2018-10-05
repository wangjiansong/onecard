package com.interlib.sso.controller.system;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.interlib.sso.acs.Clients;
import com.interlib.sso.acs.serivce.AcsService;
import com.interlib.sso.common.Constants;
import com.interlib.sso.common.PropertyUtils;
import com.interlib.sso.common.ServletUtils;
import com.interlib.sso.common.servlet.ServletUtil;
import com.interlib.sso.domain.CirFinLog;
import com.interlib.sso.domain.LibCode;
import com.interlib.sso.domain.LogCir;
import com.interlib.sso.domain.LogCirType;
import com.interlib.sso.domain.RdAccount;
import com.interlib.sso.domain.Reader;
import com.interlib.sso.domain.ReaderSession;
import com.interlib.sso.page.PageEntity;
import com.interlib.sso.service.LibCodeService;
import com.interlib.sso.service.LogCirService;
import com.interlib.sso.service.LogCirTypeService;
import com.interlib.sso.service.RdAccountService;
import com.interlib.sso.service.ReaderService;

/** 
 * @author home
 */

@Controller
@RequestMapping("admin/sys/logcir")
public class LogCirController {
	
	private static final Logger logger = Logger.getLogger(LogCirController.class);
	
	private static final String SYNCFAIL_VIEW = "admin/sys/logcir/syncFailurelist";
	private static final String LIST_VIEW = "admin/sys/logcir/list";
	
	@Autowired
	public ReaderService readerService;
	
	@Autowired
	private LogCirService logCirService;
	
	@Autowired
	private LibCodeService libcodeService;
	
	@Autowired
	private RdAccountService rdAccountService;
	
	@Autowired
	private LogCirTypeService logCirTypeService;
	
	@Autowired
	public AcsService acsService;
	
	/**
	 * 操作记录明细
	 */
	@RequiresRoles("admin")
	@RequestMapping("/list")
	public String list(Model model, LogCir logCir) {
		model.addAttribute(Constants.ACTIVE_MENU_KEY, "logcir_list");
		List<LogCir> pageList = logCirService.queryLogCirList(logCir);
		List<LibCode> simpleList = libcodeService.getSimpleInfo();
		List<LogCirType> logCirTypeList = logCirTypeService.getAll();
		model.addAttribute("simpleLibcode", simpleList).addAttribute("logCirTypeList", logCirTypeList);
		model.addAttribute("pageList", pageList);
		model.addAttribute("obj", logCir);
		return LIST_VIEW;
	}
	
	/**
	 * 未同步的操作记录
	 */
	@RequiresRoles("admin")
	@RequestMapping("/syncFailurelist")
	public String syncFailurelist(Model model, LogCir logCir) {
		logCir.setData5("0");//这里只查找同步失败的
		//groupID 分组添加到 cirFinLog 对象里面
		model.addAttribute(Constants.ACTIVE_MENU_KEY, "logcir_syncFailurelist");
		List<LogCir> pageList = logCirService.queryLogCirList(logCir);
		List<LibCode> simpleList = libcodeService.getSimpleInfo();
		model.addAttribute("simpleLibcode", simpleList);
		model.addAttribute("pageList", pageList);
		model.addAttribute("obj", logCir);
		return SYNCFAIL_VIEW;
	}
	
	/**
	 * 执行同步操作，主要是针对办证
	 */
	@RequiresRoles("admin")
	@RequestMapping("/syncLogcir")
	public void batchSyncLogcir(HttpServletResponse resp, HttpServletRequest req, Model model, LogCir params) {
		// 取得当前登录的管理员session信息
		ReaderSession readerSession = (ReaderSession) req.getSession()
					.getAttribute("READER_SESSION");
		String idsStr = req.getParameter("ids");
		String result = "无同步的记录！";
		if (idsStr == null && "".equals(idsStr)) {
			ServletUtil.responseOut("GBK", String.valueOf(""), resp);
		}
		String[] ids = idsStr.split(",");
		Properties sysConfig=PropertyUtils.getProperties("sysConfig");
		String acsIp = sysConfig.getProperty("acs_ip");
		String acsPort = sysConfig.getProperty("acs_port");
		//====连接acs做同步操作
		Clients client = new Clients();
		StringBuffer message = new StringBuffer();
		try {
			if(!acsIp.equals("") && !acsPort.equals("")) {
				client.startup(acsIp, Integer.parseInt(acsPort));
				List<String> idList = new ArrayList<String>();
				for(String id : ids) {
					LogCir logCir = logCirService.get(Integer.parseInt(id));
					String payedStr = logCir.getData1();
					String oldRdid = logCir.getData2();
					String newRdid = logCir.getData4();
					Reader reader = readerService.getReader(oldRdid, (byte)2);
					Reader newReader = readerService.getReader(newRdid, (byte)2);
					RdAccount rdAccount = rdAccountService.get(newRdid);
					newReader.setRdDeposit(rdAccount.getDeposit());
					LibCode lib = libcodeService.getLibByCode(newReader.getRdLibCode());
					acsService.changeReader(lib, reader, newReader, payedStr, readerSession.getReader().getRdId());
				}
				//更新本地同步标示
				LogCir lc = new LogCir();
				lc.setData5("1");//用data5记录同步标识
				if(idList.size() > 0) {
					logCirService.batchUpdate(lc, idList);
				}
				message.append("{");
				message.append("\"success\": 1,");
				message.append("\"message\": '同步：'" + ids.length + "条, 成功：" + idList.size() + "条。");
				message.append("}");
			}
		} catch (Exception e) {
			e.printStackTrace();
			message.append("{");
			message.append("\"success\": -1,");
			message.append("\"message\": '同步未完成,");
			message.append("\"exception\": '"+e.toString()+"'");
			message.append("}");
		} finally {
			try {
				client.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		//====同步操作结束
		ServletUtils.printHTML(resp, message.toString());
		return;
	}
	
	
	@RequiresRoles("admin")
	@RequestMapping(value = "/exportLogcirListExcel")
	public synchronized void exportCirfinlogListExcel(HttpServletRequest request,
			HttpServletResponse response, LogCir logCir){
		//统计的总数  modify 2014-03-03
				int count=logCirService.getLogCirCount(logCir);
				if(count==0)count=1;
				if(logCir!=null){
					PageEntity page= new PageEntity();//前台没有传这个，肯定null啦
					logCir.setPage(page);
					if(count<65535){//工作表最大的行数
						logCir.getPage().setShowCount(count);
					}else{
						logCir.getPage().setShowCount(65535);
					}
				}
		List<LogCir> pageList =logCirService.queryLogCirList(logCir);
		String inFilePath = request.getSession().getServletContext().getRealPath("/jsp_tiles/excel/inFile/logcir.xls");
		String filename = "操作日志_"+System.currentTimeMillis()+".xls";
		
		OutputStream outputStream = null;
		InputStream inputStream = null;
		try {
			response.setContentType("application/vnd.ms-excel; charset=GBK");
			response.addHeader("Content-Disposition", "attachment;filename=" + new String(filename.getBytes("GBK"), "ISO8859-1"));
			outputStream = response.getOutputStream();
			inputStream = new FileInputStream(inFilePath);
			
			HSSFWorkbook workbook = new HSSFWorkbook(inputStream);
			HSSFSheet worksheet = workbook.getSheetAt(0);
			int size = pageList.size();
			for(int i=0; i<size; i++){
				LogCir logcir = pageList.get(i);
				HSSFRow row = worksheet.createRow(i+1);
				row.createCell(0).setCellValue(logcir.getLogType());
				row.createCell(1).setCellValue(logcir.getLibcode());
				row.createCell(2).setCellValue(logcir.getUserId());
				row.createCell(3).setCellValue(logcir.getData2());
				row.createCell(4).setCellValue(logcir.getData4());				
				//设置时间格式
				SimpleDateFormat format1=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
				row.createCell(5).setCellValue(format1.format(logcir.getRegtime()));
			}
			
			workbook.write(outputStream);
			outputStream.flush();
		} catch (FileNotFoundException e) {
			logger.error("【结算中心】导出操作日志列表时未读取到模板文件！", e);
		} catch (IOException e) {
			logger.error("【结算中心】导出操作日志列表时出现IO流异常！", e);
		} catch (IllegalStateException e) {
			logger.error("【结算中心】导出操作日志列表时出现异常！", e);
		}finally {
			if(inputStream != null || outputStream != null){
				try {
					inputStream.close();
					outputStream.close();
				} catch (IOException e) {
					logger.error("【结算中心】导出操作日志列表关闭输出流时异常！", e);
				}
			}
		}
	}
	
	
	public static void main(String[] args) {
		String idsStr = "1";
		String[] ids = idsStr.split(",");
		System.out.println(idsStr.indexOf(","));
		for(String id : ids) {
			
		}
	}
	
}
