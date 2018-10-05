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
import com.interlib.sso.common.ServletUtils;
import com.interlib.sso.common.json.Jackson;
import com.interlib.sso.domain.Resources;
import com.interlib.sso.service.ResourceService;

@Controller
@RequestMapping("/admin/sys/res")
public class ResourcesController {

	@Autowired
	public ResourceService resourcesService;
	
	private static final String LIST_VIEW = "admin/sys/res/list";
	private static final String EDIT_VIEW = "admin/sys/res/edit";
	private static final String ADD_VIEW = "admin/sys/res/add";
	
	private static final String MAX_PAGE_SIZE_STR = Integer.MAX_VALUE + "";
	@RequiresRoles("admin")
	@RequestMapping("/list")
	public String listResources(Model model, Resources resources) {
		model.addAttribute(Constants.ACTIVE_MENU_KEY, "resource_list");
		List<Map<String, Object>> pageList = resourcesService.queryPageList(resources);
		model.addAttribute("pageList", pageList);
		model.addAttribute("obj", resources);
		return LIST_VIEW;
	}
	@RequiresRoles("admin")
	@RequestMapping("/add")
	public ModelAndView add(Model model, @ModelAttribute Resources resources) {
		model.addAttribute(Constants.ACTIVE_MENU_KEY, "resource_list");
		return new ModelAndView(ADD_VIEW);
	}
	@RequiresRoles("admin")
	@RequestMapping("/save")
	public ModelAndView save(
			@ModelAttribute Resources resources, BindingResult bindingResult, 
			Model model, RedirectAttributes redirectAttributes) {
		model.addAttribute(Constants.ACTIVE_MENU_KEY, "resource_list");
		if(resourcesService.get(resources.getResourceId()) != null) {
			redirectAttributes.addFlashAttribute("message", "已存在该资源代码");
			return new ModelAndView(ADD_VIEW);
		}
		resources = resourcesService.save(resources);
		redirectAttributes.addFlashAttribute("message", "保存成功");
		return new ModelAndView("redirect:/admin/sys/res/list");
	}
	@RequiresRoles("admin")
	@RequestMapping("/edit/{id}")
	public void edit(HttpServletResponse response, @PathVariable String id, Model model) {
		Resources resources = resourcesService.get(id);
		String jsonStr = Jackson.getBaseJsonData(resources);
		ServletUtils.printHTML(response, jsonStr);
	}
	
	/**
	 * 根据id更新记录
	 * @param resources
	 * @param bindingResult
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresRoles("admin")
	@RequestMapping("/update")
	public ModelAndView update(@ModelAttribute("resources") Resources resources, 
			BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {
		//保存信息
		resourcesService.update(resources);
		
		redirectAttributes.addFlashAttribute("message", "保存成功");
		return new ModelAndView("redirect:/admin/sys/res/list");
	}
	@RequiresRoles("admin")
	@RequestMapping("/validate/{id}")
	public void validateResource(@PathVariable String id, HttpServletResponse response) {
		//检查当前资源是否已经分配给权限了，返回提示
		List<Integer> competIdList = resourcesService.getCompResourceByResourceId(id);
		String result = "";
		if(competIdList != null && competIdList.size() > 0) {
			result = "该资源已被分配在权限中";
		}
		ServletUtils.printHTML(response, result);
	}
	
	/**
	 * 删除单条记录
	 * @param id
	 * @param response
	 * @return
	 * @throws IOException
	 */
	@RequiresRoles("admin")
	@RequestMapping("/delete/{id}")
	public ModelAndView delete(@PathVariable String id, RedirectAttributes redirectAttributes) throws IOException{
		//删除权限资源关系
		resourcesService.deleteCompResourceByResourceId(id);
		resourcesService.delete(id);
		redirectAttributes.addFlashAttribute("message", "删除成功");
		return new ModelAndView("redirect:/admin/sys/res/list");
	}
}
