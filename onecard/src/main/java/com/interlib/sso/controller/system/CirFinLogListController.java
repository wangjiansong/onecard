package com.interlib.sso.controller.system;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.interlib.sso.common.Constants;
import com.interlib.sso.common.json.Jackson;
import com.interlib.sso.common.servlet.ServletUtil;
import com.interlib.sso.controller.BaseController;
import com.interlib.sso.domain.CirFinLog;
import com.interlib.sso.domain.FinType;
import com.interlib.sso.domain.LibCode;
import com.interlib.sso.service.CirFinLogService;
import com.interlib.sso.service.FinTypeService;
import com.interlib.sso.service.LibCodeService;
import com.interlib.sso.service.ReaderService;


@Controller
@RequestMapping("cirfinloglist")
public class CirFinLogListController extends BaseController{

	private static final String CIRFINLOG_LIST_VIEW = "cirfinloglist/list";
	
	private static final Logger logger = Logger.getLogger(CirFinLogListController.class);
	
	@Autowired
	public CirFinLogService cirFinLogService;
	
	@Autowired
	public LibCodeService libcodeService;
	
	@Autowired
	public FinTypeService finTypeService;
	
	@Autowired
	public ReaderService readerService;
	
	/**
	 * add 20140318
	 * 只有一个页数 
	 * 收费明细统计
	 */
	@RequiresRoles("admin")
	@RequestMapping("/list")
	public String cirfinloglist(Model model, CirFinLog cirFinLog) {
		
		
		model.addAttribute(Constants.ACTIVE_MENU_KEY, "cirfinlog_list");
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
	public void cirfinloglistJson(Model model, CirFinLog cirFinLog,HttpServletResponse response) throws IOException { 
		model.addAttribute(Constants.ACTIVE_MENU_KEY, "cirfinlog_list");
		List<CirFinLog> pageList = cirFinLogService.queryCirFinLogList(cirFinLog);
		List<LibCode> simpleList = libcodeService.getSimpleInfo();
		List<FinType> finTypeList = finTypeService.getAll();
		model.addAttribute("simpleLibcode", simpleList).addAttribute("finTypeList", finTypeList);
		model.addAttribute("pageList", pageList);
		model.addAttribute("obj", cirFinLog);
		ServletUtil.responseOut("utf-8", Jackson.getBaseJsonData(pageList), response);
	}
	
}
