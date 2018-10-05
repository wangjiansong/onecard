package com.interlib.sso.controller.system;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.interlib.sso.common.Constants;


@Controller
@RequestMapping("admin")
public class AdminController {
	
	@RequiresRoles("admin")
	@RequestMapping({""})
	public String index(HttpServletRequest request, Model model) {
		model.addAttribute(Constants.ACTIVE_MENU_KEY, "admin_index");
		return "admin/index";
	}
	
}
