package com.interlib.sso.controller.system;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

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
import com.interlib.sso.controller.BaseController;
import com.interlib.sso.domain.Guide;
import com.interlib.sso.service.GuideService;

@Controller
@RequestMapping("admin/sys/guide")
public class GuideController extends BaseController{
	private static final String ADD_VIEW = "admin/sys/guide/add";
	private static final String LIST_VIEW = "admin/sys/guide/list";
	private static final String LISTONE_VIEW = "admin/sys/guide/list/one";
	private static final String EDIT_VIEW = "admin/sys/guide/edit";
	
	@Autowired
	public GuideService guideService;
	@RequiresRoles("admin")
	@RequestMapping("/list")
	public String list(Model model, Guide guide) { 
		model.addAttribute(Constants.ACTIVE_MENU_KEY, "guide_index");
		
		List<Map<String, Object>> pageList = guideService.queryPageList(guide);
		model.addAttribute("pageList", pageList);
		
		model.addAttribute("obj", guide);
		return LIST_VIEW;
	}
	
	@RequiresRoles("admin")
	@RequestMapping("/list/one/{id}")
	public String listone(HttpServletResponse response, @PathVariable Long id, Model model) {
		model.addAttribute(Constants.ACTIVE_MENU_KEY, "guide_index");
		Guide guide = guideService.get(id);
		model.addAttribute("guide", guide);
		
		return LISTONE_VIEW;
	}
	
	@RequiresRoles("admin")
	@RequestMapping("/add")
	public String add(Model model) { 
		model.addAttribute(Constants.ACTIVE_MENU_KEY, "guide_index");
		return ADD_VIEW;
	}
	@RequiresRoles("admin")
	@RequestMapping("/save")
	public ModelAndView save(
			@ModelAttribute Guide guide, BindingResult bindingResult, 
			Model model, RedirectAttributes redirectAttributes) {
		model.addAttribute(Constants.ACTIVE_MENU_KEY, "guide_index");

		guide=guideService.save(guide);
		redirectAttributes.addFlashAttribute("message", "保存成功");
		return new ModelAndView("redirect:/admin/sys/guide/list");
	}
	
	@RequiresRoles("admin")
	@RequestMapping("/edit/{id}")
	public String edit(HttpServletResponse response, @PathVariable Long id, Model model) {
		model.addAttribute(Constants.ACTIVE_MENU_KEY, "guide_index");
		Guide guide = guideService.get(id);
		model.addAttribute("guide", guide);
		
		return EDIT_VIEW;
	}
	@RequiresRoles("admin")
	@RequestMapping("/update")
	public ModelAndView update(@ModelAttribute("guide") Guide guide, 
			BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {
		model.addAttribute(Constants.ACTIVE_MENU_KEY, "guide_index");
		//保存信息
		guideService.update(guide);
		
		redirectAttributes.addFlashAttribute("message", "保存成功");
		return new ModelAndView("redirect:/admin/sys/guide/list");
	}
	@RequiresRoles("admin")
	@RequestMapping("/delete/{id}")
	public ModelAndView delete(@PathVariable Long id, RedirectAttributes redirectAttributes) throws IOException{
		//删除权限资源关系
		guideService.delete(id);
		redirectAttributes.addFlashAttribute("message", "删除成功");
		return new ModelAndView("redirect:/admin/sys/guide/list");
	}
}
