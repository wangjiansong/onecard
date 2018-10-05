package com.interlib.sso.controller.system;

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
import com.interlib.sso.controller.BaseController;
import com.interlib.sso.domain.RestaurantWorkConfig;
import com.interlib.sso.service.RestaurantWorkConfigService;

@Controller
@RequestMapping("admin/sys/restaurant")
public class RestaurantWorkConfigController extends BaseController{
	
	@Autowired
	public RestaurantWorkConfigService restaurantService;
	
	private static final String LIST_VIEW = "admin/sys/restaurant/list";
	@RequiresRoles("admin")
	@RequestMapping("/list")
	public String list(Model model, RestaurantWorkConfig config) {
		model.addAttribute(Constants.ACTIVE_MENU_KEY, "restaurant_list");
		List<RestaurantWorkConfig> pageList = restaurantService.queryRestaurantWorkConfigList(config);

		model.addAttribute("pageList", pageList);
		model.addAttribute("obj", config);
		return LIST_VIEW;
	}
	@RequiresRoles("admin")
	@RequestMapping("/edit/{id}")
	public String edit(@PathVariable Integer id, Model model, RestaurantWorkConfig config) {
		model.addAttribute(Constants.ACTIVE_MENU_KEY, "restaurant_list");
		List<RestaurantWorkConfig> pageList = restaurantService.queryRestaurantWorkConfigList(config);

		model.addAttribute("pageList", pageList);
		config = restaurantService.get(id);
		config.setId(id);
		model.addAttribute("config", config);
		return LIST_VIEW;
	}
	@RequiresRoles("admin")
	@RequestMapping("/update")
	public ModelAndView update(@ModelAttribute("config") RestaurantWorkConfig config, 
			BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {
		//保存信息
		restaurantService.update(config);
		redirectAttributes.addFlashAttribute("message", "保存成功");
		return new ModelAndView("redirect:/admin/sys/restaurant/list");
	}
	
}
