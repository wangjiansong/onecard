package com.interlib.sso.controller.system;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.interlib.sso.acs.Extractor;
import com.interlib.sso.acs.serivce.AcsService;
import com.interlib.sso.common.Constants;
import com.interlib.sso.common.ServletUtils;
import com.interlib.sso.common.json.Jackson;
import com.interlib.sso.domain.LibCode;
import com.interlib.sso.domain.LogCirType;
import com.interlib.sso.domain.SyncRecord;
import com.interlib.sso.service.LibCodeService;
import com.interlib.sso.service.LogCirTypeService;
import com.interlib.sso.service.ReaderService;
import com.interlib.sso.service.SyncRecordService;


@Controller
@RequestMapping("admin/sys/syncrecord")
public class SyncRecordController {
	
	private static final String LIST_VIEW = "admin/sys/syncrecord/list";
	
	@Autowired
	private LibCodeService libcodeService;
	
	@Autowired
	public ReaderService readerService;
	
	@Autowired
	public SyncRecordService syncRecordService;
	
	@Autowired
	private LogCirTypeService logCirTypeService;
	
	@Autowired
	private AcsService acsService;
	
	@RequiresRoles("admin")
	@RequestMapping("/list")
	public String list(Model model, SyncRecord syncRecord) {
		model.addAttribute(Constants.ACTIVE_MENU_KEY, "syncRecord_list");
		List<SyncRecord> pageList = syncRecordService.querySyncRecordList(syncRecord);
		List<LibCode> simpleList = libcodeService.getSimpleInfo();
		List<LogCirType> logCirTypeList = logCirTypeService.getAll();
		model.addAttribute("simpleLibcode", simpleList).addAttribute("logCirTypeList", logCirTypeList);
		model.addAttribute("pageList", pageList);
		model.addAttribute("obj", syncRecord);
		return LIST_VIEW;
	}
	@RequiresRoles("admin")
	@RequestMapping("/list/{syncId}")
	public String list(Model model, SyncRecord syncRecord, @PathVariable String syncId) {
		
		model.addAttribute(Constants.ACTIVE_MENU_KEY, "syncRecord_list");
		syncRecord = syncRecordService.get(syncId);
		model.addAttribute("syncRecord", syncRecord);
		model.addAttribute("libcodes", Jackson.getBaseJsonData(readerService.getLibCode()));
		return LIST_VIEW;
	}
	
	@RequiresRoles("admin")
	@RequestMapping("/dosync/{syncId}")
	public void dosync(HttpServletResponse resp, Model model, SyncRecord syncRecord, @PathVariable String syncId, 
			RedirectAttributes redirectAttributes) {
		model.addAttribute(Constants.ACTIVE_MENU_KEY, "syncRecord_list");
		//保存信息
		SyncRecord entity = syncRecordService.get(syncId);
		String retMsg = acsService.execute(entity.getSyncLib(), entity.getSyncCode());
		Extractor ex = new Extractor(retMsg);
		String OK = ex.extract("OK");
		String AF = ex.extract("AF");
		StringBuffer message = new StringBuffer();
		if(OK.equals("1") || AF.contains("成功")) {
			message.append("{");
			message.append("\"success\": 1,");
			message.append("\"message\": \"同步成功\"");
			message.append("}");
			entity.setSyncStatus("1");
			syncRecordService.update(entity);
		} else {
			message.append("{");
			message.append("\"success\": 0,");
			message.append("\"message\": \""+retMsg+"\"");
			message.append("}");
		}
		model.addAttribute("obj", syncRecord);
		ServletUtils.printHTML(resp, message.toString());
		return;
	}
	
}
