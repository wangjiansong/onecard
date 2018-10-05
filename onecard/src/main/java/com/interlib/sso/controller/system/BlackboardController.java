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
import com.interlib.sso.domain.Blackboard;
import com.interlib.sso.service.BlackboardService;


@Controller
@RequestMapping("admin/sys/blackboard")
public class BlackboardController extends BaseController{

	private static final String ADD_VIEW = "admin/sys/blackboard/add";
	private static final String LIST_VIEW = "admin/sys/blackboard/list";
	private static final String LISTONE_VIEW = "admin/sys/blackboard/list/one";
	private static final String EDIT_VIEW = "admin/sys/blackboard/edit";
	@Autowired
	public BlackboardService blackboardService;
	@RequiresRoles("admin")
	@RequestMapping("/list")
	public String list(Model model, Blackboard blackboard) { 
		model.addAttribute(Constants.ACTIVE_MENU_KEY, "blackboard_index");
		
		List<Map<String, Object>> pageList = blackboardService.queryPageList(blackboard);
		model.addAttribute("pageList", pageList);
		model.addAttribute("obj", blackboard);
		return LIST_VIEW;
	}
	
	@RequiresRoles("admin")
	@RequestMapping("/list/one/{id}")
	public String listone(HttpServletResponse response, @PathVariable Integer id, Model model) {
		model.addAttribute(Constants.ACTIVE_MENU_KEY, "blackboard_index");
		Blackboard blackboard = blackboardService.get(id);
		model.addAttribute("blackboard", blackboard);
		
		return LISTONE_VIEW;
	}
	
	@RequiresRoles("admin")
	@RequestMapping("/add")
	public String add(Model model) { 
		model.addAttribute(Constants.ACTIVE_MENU_KEY, "blackboard_index");
		return ADD_VIEW;
	}
	@RequiresRoles("admin")
	@RequestMapping("/save")
	public ModelAndView save(
			@ModelAttribute Blackboard blackboard, BindingResult bindingResult, 
			Model model, RedirectAttributes redirectAttributes) {
		model.addAttribute(Constants.ACTIVE_MENU_KEY, "blackboard_index");

		blackboard=blackboardService.save(blackboard);
		redirectAttributes.addFlashAttribute("message", "保存成功");
		return new ModelAndView("redirect:/admin/sys/blackboard/list");
	}
	
	@RequiresRoles("admin")
	@RequestMapping("/edit/{id}")
	public String edit(HttpServletResponse response, @PathVariable Integer id, Model model) {
		model.addAttribute(Constants.ACTIVE_MENU_KEY, "blackboard_index");
		Blackboard blackboard = blackboardService.get(id);
		model.addAttribute("blackboard", blackboard);
		
		return EDIT_VIEW;
	}
	
	
	@RequiresRoles("admin")
	@RequestMapping("/update")
	public ModelAndView update(@ModelAttribute("blackboard") Blackboard blackboard, 
			BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {
		model.addAttribute(Constants.ACTIVE_MENU_KEY, "blackboard_index");
		//保存信息
		blackboardService.update(blackboard);
		
		redirectAttributes.addFlashAttribute("message", "保存成功");
		return new ModelAndView("redirect:/admin/sys/blackboard/list");
	}
	@RequiresRoles("admin")
	@RequestMapping("/delete/{id}")
	public ModelAndView delete(@PathVariable Integer id, RedirectAttributes redirectAttributes) throws IOException{
		//删除权限资源关系
		blackboardService.delete(id);
		redirectAttributes.addFlashAttribute("message", "删除成功");
		return new ModelAndView("redirect:/admin/sys/blackboard/list");
	}
	
}
