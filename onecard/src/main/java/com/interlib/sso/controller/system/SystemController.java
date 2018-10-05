package com.interlib.sso.controller.system;

import java.util.List;
import java.util.Map;

import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.interlib.sso.common.Constants;
import com.interlib.sso.domain.ReaderType;
import com.interlib.sso.service.ReaderTypeService;

/**
 * 系统管理
 * @author Lullaby
 *
 */
@Controller
@RequestMapping("system")
public class SystemController {

	@Autowired
	private ReaderTypeService readerTypeService;
	@RequiresRoles("admin")
	@RequestMapping(value = "/index")
	public String systemIndex(Model model) {
		ReaderType readerType = new ReaderType();
		List<Map<String,String>> list = readerTypeService.queryReaderTypeList(new ReaderType());
		model.addAttribute("list", list);
		model.addAttribute("obj", readerType);
		model.addAttribute(Constants.ACTIVE_MENU_KEY, "system_index");
		model.addAttribute("defaultTab", "readertype");
		return "system/index";
	}
	
}
