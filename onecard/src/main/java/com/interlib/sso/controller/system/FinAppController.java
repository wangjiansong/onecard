package com.interlib.sso.controller.system;

import java.io.IOException;
import java.util.List;


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
import com.interlib.sso.domain.FinApp;
import com.interlib.sso.service.FinAppService;


@Controller
@RequestMapping("/admin/sys/finapp")
public class FinAppController {
	private static final String LIST_VIEW = "admin/sys/finapp/list";
	private static final String EDIT_VIEW = "admin/sys/finapp/edit";
	private static final String ADD_VIEW = "admin/sys/finapp/add";
	
	@Autowired
	private FinAppService finAppService;
	@RequiresRoles("admin")
	@RequestMapping("/list")
	public String list(Model model, FinApp finApp) {
		model.addAttribute(Constants.ACTIVE_MENU_KEY, "finApp_list");
		List<FinApp> pageList = finAppService.queryFinAppList(finApp);
		model.addAttribute("pageList", pageList);
		model.addAttribute("obj", finApp);
		return LIST_VIEW;
	}
	@RequiresRoles("admin")
	@RequestMapping("/add")
	public ModelAndView add(Model model) {
		model.addAttribute(Constants.ACTIVE_MENU_KEY, "finApp_list");
		return new ModelAndView(ADD_VIEW);
	}
	@RequiresRoles("admin")
	@RequestMapping("/save")
	public ModelAndView save(
			@ModelAttribute FinApp finApp, BindingResult bindingResult, 
			Model model, RedirectAttributes redirectAttributes) {
		finApp = finAppService.save(finApp);
		redirectAttributes.addFlashAttribute("message", "保存成功");
		return new ModelAndView("redirect:/admin/sys/finapp/list");
	}
	@RequiresRoles("admin")
	@RequestMapping("/edit/{id}")
	public String edit(@PathVariable String id, Model model) {
		model.addAttribute(Constants.ACTIVE_MENU_KEY, "finApp_list");
		FinApp finApp = finAppService.get(id);
		model.addAttribute("finApp", finApp);
		return EDIT_VIEW;
	}
	@RequiresRoles("admin")
	@RequestMapping("/update")
	public ModelAndView update(@ModelAttribute("finApp") FinApp finApp, 
			BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {
		//保存信息
		finAppService.update(finApp);
		redirectAttributes.addFlashAttribute("message", "保存成功");
		return new ModelAndView("redirect:/admin/sys/finapp/list");
	}
	@RequiresRoles("admin")
	@RequestMapping("/delete/{id}")
	public ModelAndView delete(@PathVariable String id, RedirectAttributes redirectAttributes) throws IOException{
		finAppService.delete(id);
		redirectAttributes.addFlashAttribute("message", "删除成功");
		return new ModelAndView("redirect:/admin/sys/finapp/list");
	}
	
}
