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
import com.interlib.sso.domain.ChargeType;
import com.interlib.sso.domain.FinType;
import com.interlib.sso.service.AuthorizationService;
import com.interlib.sso.service.ChargeTypeService;

/**
 * 充值类型管理
 * @author Administrator
 *
 */
@Controller
@RequestMapping("/admin/sys/chargetype")
public class ChargeTypeController {

	private static final String LIST_VIEW = "admin/sys/chargetype/list";
	private static final String EDIT_VIEW = "admin/sys/chargetype/edit";
	private static final String ADD_VIEW = "admin/sys/chargetype/add";
	
	@Autowired
	public AuthorizationService authorizationService;
	
	@Autowired
	public ChargeTypeService chargeTypeService;
	@RequiresRoles("admin")
	@RequestMapping("/list")
	public String list(Model model, ChargeType chargeType) {
		model.addAttribute(Constants.ACTIVE_MENU_KEY, "chargeType_list");
		List<Map<String, Object>> pageList = chargeTypeService.queryPageList(chargeType);
		Map<String, String> appMap = authorizationService.getAppMap();
		model.addAttribute("chargeAppMap", appMap);
		model.addAttribute("pageList", pageList);
		model.addAttribute("obj", chargeType);
		return LIST_VIEW;
	}
	@RequiresRoles("admin")
	@RequestMapping("/add")
	public ModelAndView add(Model model) {
		model.addAttribute(Constants.ACTIVE_MENU_KEY, "chargeType_list");
		Map<String, String> appMap = authorizationService.getAppMap();
		model.addAttribute("chargeAppMap", appMap);
		return new ModelAndView(ADD_VIEW);
	}
	@RequiresRoles("admin")
	@RequestMapping("/save")
	public ModelAndView save(
			@ModelAttribute ChargeType chargeType, BindingResult bindingResult, 
			Model model, RedirectAttributes redirectAttributes) {
		Authorization auth = authorizationService.getByAppCode(chargeType.getAppCode());
		chargeType.setAppName(auth.getAppName());
		chargeType = chargeTypeService.save(chargeType);
		redirectAttributes.addFlashAttribute("message", "保存成功");
		return new ModelAndView("redirect:/admin/sys/chargetype/list");
	}
	@RequiresRoles("admin")
	@RequestMapping("/edit/{id}")
	public String edit(@PathVariable String id, Model model) {
		model.addAttribute(Constants.ACTIVE_MENU_KEY, "chargeType_list");
		ChargeType chargeType = chargeTypeService.get(id);
		Map<String, String> chargeAppMap = authorizationService.getAppMap();
		
		Authorization authorization = authorizationService.getByAppCode(chargeType.getAppCode());
		
		chargeType.setAppName(authorization.getAppName());
		
		model.addAttribute("chargeType", chargeType);
		model.addAttribute("chargeAppMap", chargeAppMap);
		return EDIT_VIEW;
	}
	@RequiresRoles("admin")
	@RequestMapping("/update")
	public ModelAndView update(@ModelAttribute("chargeType") ChargeType chargeType, 
			BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {
		//保存信息
		Authorization auth = authorizationService.getByAppCode(chargeType.getAppCode());
		chargeType.setAppName(auth.getAppName());
		chargeTypeService.update(chargeType);
		redirectAttributes.addFlashAttribute("message", "保存成功");
		return new ModelAndView("redirect:/admin/sys/chargetype/list");
	}
	@RequiresRoles("admin")
	@RequestMapping("/delete/{id}")
	public ModelAndView delete(@PathVariable String id, RedirectAttributes redirectAttributes) throws IOException{
		chargeTypeService.delete(id);
		redirectAttributes.addFlashAttribute("message", "删除成功");
		return new ModelAndView("redirect:/admin/sys/chargetype/list");
	}
	
	/**
	 * 2015-01-15
	 * 查询对应的app下的消费类型
	 * @param request
	 * @param response
	 * @param appcode
	 */
	@RequiresRoles("admin")
	@RequestMapping(value = "/getChargeTypesByAppcode")
	public void getChargeTypesByAppcode(HttpServletRequest request,HttpServletResponse response,String appcode){
		String[] appcodes = new String[1]; 
		if(appcode !=null ) appcodes[0] = appcode;
		List<ChargeType> chargeTypes = chargeTypeService.getChargeTypesByAppcodes(appcodes);
		String result = "{\"result\":\"none\"}";
		result = Jackson.getBaseJsonData(chargeTypes);
		ServletUtil.responseOut("UTF-8", result, response);
	}	
}
