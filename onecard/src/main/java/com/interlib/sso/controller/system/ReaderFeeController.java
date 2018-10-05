package com.interlib.sso.controller.system;


import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.interlib.sso.common.Constants;
import com.interlib.sso.common.json.Jackson;
import com.interlib.sso.domain.ReaderFee;
import com.interlib.sso.domain.ReaderSession;
import com.interlib.sso.service.ReaderFeeService;
import com.interlib.sso.service.ReaderService;


@Controller
@RequestMapping("admin/sys/readerfee")
public class ReaderFeeController {
	
	private static final String LIST_VIEW = "admin/sys/readerfee/list";
	
	@Autowired
	public ReaderFeeService readerFeeService; 
	
	@Autowired
	public ReaderService readerService;
	@RequiresRoles("admin")
	@RequestMapping("/list")
	public String list(Model model, ReaderFee readerFee) {
		
		ReaderSession readerSession = (ReaderSession)SecurityUtils.getSubject().getSession().getAttribute("READER_SESSION");
		model.addAttribute(Constants.ACTIVE_MENU_KEY, "readerfee_list");
		readerFee = readerFeeService.get(readerSession.getReader().getRdLibCode());
		model.addAttribute("readerFee", readerFee);
		model.addAttribute("libcodes", Jackson.getBaseJsonData(readerService.getLibCode()));
		model.addAttribute("libcode", readerSession.getReader().getRdLibCode());
		return LIST_VIEW;
	}
	@RequiresRoles("admin")
	@RequestMapping("/list/{libcode}")
	public String list(Model model, ReaderFee readerFee,  @PathVariable String libcode) {
		
		ReaderSession readerSession = (ReaderSession)SecurityUtils.getSubject().getSession().getAttribute("READER_SESSION");
		model.addAttribute(Constants.ACTIVE_MENU_KEY, "readerfee_list");
		readerFee = readerFeeService.get(libcode);
		model.addAttribute("readerFee", readerFee);
		model.addAttribute("libcodes", Jackson.getBaseJsonData(readerService.getLibCode()));
		model.addAttribute("libcode", libcode);
		return LIST_VIEW;
	}
	
	@RequiresRoles("admin")
	@RequestMapping("/update")
	public ModelAndView update(Model model, ReaderFee readerFee,RedirectAttributes redirectAttributes) {
		model.addAttribute(Constants.ACTIVE_MENU_KEY, "readerfee_list");
		//保存信息
		readerFeeService.update(readerFee);
		redirectAttributes.addFlashAttribute("message", "保存成功");
		return new ModelAndView("redirect:/admin/sys/readerfee/list/"+readerFee.getLibcode());
		
	}
}
