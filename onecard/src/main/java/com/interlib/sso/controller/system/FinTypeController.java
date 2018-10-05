package com.interlib.sso.controller.system;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
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
import com.interlib.sso.common.json.Jackson;
import com.interlib.sso.common.servlet.ServletUtil;
import com.interlib.sso.domain.Authorization;
import com.interlib.sso.domain.FinType;
import com.interlib.sso.service.AuthorizationService;
import com.interlib.sso.service.FinTypeService;


@Controller
@RequestMapping("/admin/sys/fintype")
public class FinTypeController {

	private static final String LIST_VIEW = "admin/sys/fintype/list";
	private static final String EDIT_VIEW = "admin/sys/fintype/edit";
	private static final String ADD_VIEW = "admin/sys/fintype/add";
	
	@Autowired
	public AuthorizationService authorizationService;
	
	@Autowired
	public FinTypeService finTypeService;
	@RequiresRoles("admin")
	@RequestMapping("/list")
	public String list(Model model, FinType finType) {
		model.addAttribute(Constants.ACTIVE_MENU_KEY, "finType_list");
		List<Map<String, Object>> pageList = finTypeService.queryPageList(finType);
		Map<String, String> appMap = authorizationService.getAppMap();
		model.addAttribute("finAppMap", appMap);
		model.addAttribute("pageList", pageList);
		model.addAttribute("obj", finType);
		return LIST_VIEW;
	}
	@RequiresRoles("admin")
	@RequestMapping("/add")
	public ModelAndView add(Model model) {
		model.addAttribute(Constants.ACTIVE_MENU_KEY, "finType_list");
		Map<String, String> appMap = authorizationService.getAppMap();
		model.addAttribute("finAppMap", appMap);
		return new ModelAndView(ADD_VIEW);
	}
	@RequiresRoles("admin")
	@RequestMapping("/save")
	public ModelAndView save(
			@ModelAttribute FinType finType, BindingResult bindingResult, 
			Model model, RedirectAttributes redirectAttributes) {
		Authorization auth = authorizationService.getByAppCode(finType.getAppCode());
		finType.setAppName(auth.getAppName());
		finType = finTypeService.save(finType);
		redirectAttributes.addFlashAttribute("message", "保存成功");
		return new ModelAndView("redirect:/admin/sys/fintype/list");
	}
	@RequiresRoles("admin")
	@RequestMapping("/edit/{id}")
	public String edit(@PathVariable String id, Model model) {
		model.addAttribute(Constants.ACTIVE_MENU_KEY, "finType_list");
		FinType finType = finTypeService.get(id);
		Map<String, String> finAppMap = authorizationService.getAppMap();
		
		Authorization authorization = authorizationService.getByAppCode(finType.getAppCode());
		
		finType.setAppName(authorization.getAppName());
		
		model.addAttribute("finType", finType);
		model.addAttribute("finAppMap", finAppMap);
		return EDIT_VIEW;
	}
	@RequiresRoles("admin")
	@RequestMapping("/update")
	public ModelAndView update(@ModelAttribute("finType") FinType finType, 
			BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {
		//保存信息
		Authorization auth = authorizationService.getByAppCode(finType.getAppCode());
		finType.setAppName(auth.getAppName());
		finTypeService.update(finType);
		redirectAttributes.addFlashAttribute("message", "保存成功");
		return new ModelAndView("redirect:/admin/sys/fintype/list");
	}
	@RequiresRoles("admin")
	@RequestMapping("/delete/{id}")
	public ModelAndView delete(@PathVariable String id, RedirectAttributes redirectAttributes) throws IOException{
		finTypeService.delete(id);
		redirectAttributes.addFlashAttribute("message", "删除成功");
		return new ModelAndView("redirect:/admin/sys/fintype/list");
	}
	
	/**
	 * 2015-01-15
	 * 查询对应的app下的消费类型
	 * @param request
	 * @param response
	 * @param appcode
	 */
	@RequiresRoles("admin")
	@RequestMapping(value = "/getFinTypesByAppcode")
	public void getFinTypesByAppcode(HttpServletRequest request,HttpServletResponse response,String appcode){
		String[] appcodes = new String[1]; 
		if(appcode !=null ) appcodes[0] = appcode;
		List<FinType> finTypes = finTypeService.getFinTypesByAppcodes(appcodes);
		String result = "{\"result\":\"none\"}";
		result = Jackson.getBaseJsonData(finTypes);
		ServletUtil.responseOut("UTF-8", result, response);
	}
}
