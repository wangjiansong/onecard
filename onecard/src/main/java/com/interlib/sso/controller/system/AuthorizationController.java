package com.interlib.sso.controller.system;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
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

import com.interlib.sso.common.Constants;
import com.interlib.sso.common.ServletUtils;
import com.interlib.sso.common.json.Jackson;
import com.interlib.sso.domain.Authorization;
import com.interlib.sso.domain.FinType;
import com.interlib.sso.domain.Reader;
import com.interlib.sso.service.AuthorizationService;
import com.interlib.sso.service.FinTypeService;
import com.interlib.sso.service.ReaderService;


@Controller
@RequestMapping("/admin/sys/auth")
public class AuthorizationController {
	private static final String LIST_VIEW = "admin/sys/auth/list";
	private static final String EDIT_VIEW = "admin/sys/auth/edit";
	private static final String ADD_VIEW = "admin/sys/auth/add";
	private static final String APIINFO_VIEW = "admin/sys/auth/apiInfo";
	
	private static final Logger logger = Logger.getLogger(AuthorizationController.class);
	
	@Autowired
	private AuthorizationService authorizationService;
	
	@Autowired
	private FinTypeService finTypeService;
	
	@Autowired
	private ReaderService readerService;
	@RequiresRoles("admin")
	@RequestMapping("/list")
	public String list(Model model, Authorization entity) {
		model.addAttribute(Constants.ACTIVE_MENU_KEY, "authorization_list");
		List<Authorization> pageList = authorizationService.queryAuthorizationList(entity);
		model.addAttribute("pageList", pageList);
		model.addAttribute("obj", entity);
		return LIST_VIEW;
	}
	@RequiresRoles("admin")
	@RequestMapping("/add")
	public ModelAndView add(Model model) {
		model.addAttribute(Constants.ACTIVE_MENU_KEY, "authorization_list");
		List<Reader> libReader = readerService.getAllLlbUser(1);
		model.addAttribute("libReader", libReader);
		return new ModelAndView(ADD_VIEW);
	}
	@RequiresRoles("admin")
	@RequestMapping("/save")
	public ModelAndView save(
			@ModelAttribute Authorization entity, BindingResult bindingResult, 
			Model model, RedirectAttributes redirectAttributes) {
		entity = authorizationService.save(entity);
		redirectAttributes.addFlashAttribute("message", "保存成功");
		return new ModelAndView("redirect:/admin/sys/auth/list");
	}
	@RequiresRoles("admin")
	@RequestMapping("/edit/{id}")
	public void edit(HttpServletResponse response, @PathVariable Integer id, Model model) {
		Authorization authorization = authorizationService.get(id);
//		Reader reader = readerService.getReader(authorization.getBindingUserId(), (byte)2);
//		authorization.setBindingUserName(reader.getRdName());
		String jsonStr = Jackson.getBaseJsonData(authorization);
		ServletUtils.printHTML(response, jsonStr);
	}
	@RequiresRoles("admin")
	@RequestMapping("/update")
	public ModelAndView update(@ModelAttribute("authorization") Authorization authorization, 
			BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {
		//保存信息
		authorizationService.update(authorization);
		String [] appCodes = {authorization.getAppCode()};
		List<FinType> finTypsLists = finTypeService.getFinTypesByAppcodes(appCodes);
		for(FinType finType : finTypsLists) {
			finType.setAppName(authorization.getAppName());
			finTypeService.update(finType);
		}
		redirectAttributes.addFlashAttribute("message", "保存成功");
		return new ModelAndView("redirect:/admin/sys/auth/list");
	}
	@RequiresRoles("admin")
	@RequestMapping("/delete/{id}")
	public ModelAndView delete(@PathVariable Integer id, RedirectAttributes redirectAttributes) throws IOException{
		authorizationService.delete(id);
		redirectAttributes.addFlashAttribute("message", "删除成功");
		return new ModelAndView("redirect:/admin/sys/auth/list");
	}
	@RequiresRoles("admin")
	@RequestMapping("/apiInfo")
	public String apiInfo(Model model) {
		model.addAttribute(Constants.ACTIVE_MENU_KEY, "authorization_api");
		
		return APIINFO_VIEW;
	}
	
}
