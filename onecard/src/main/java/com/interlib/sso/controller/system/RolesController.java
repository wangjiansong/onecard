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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.interlib.sso.common.Constants;
import com.interlib.sso.common.ServletUtils;
import com.interlib.sso.common.json.Jackson;
import com.interlib.sso.controller.BaseController;
import com.interlib.sso.domain.Compets;
import com.interlib.sso.domain.Roles;
import com.interlib.sso.domain.URole_Compet;
import com.interlib.sso.service.CompetsService;
import com.interlib.sso.service.RolesService;

@Controller
@RequestMapping("/admin/sys/role")
public class RolesController extends BaseController {

	
	@Autowired
	public RolesService rolesService;
	
	@Autowired 
	public CompetsService competsService;
	
	private static final String LIST_VIEW = "admin/sys/role/list";
	private static final String EDIT_VIEW = "admin/sys/role/edit";
	private static final String ADD_VIEW = "admin/sys/role/add";
	private static final String ASSIGN_VIEW = "admin/sys/role/assign";
	@RequiresRoles("admin")
	@RequestMapping("/list")
	public String list(Model model, Roles roles) {
		model.addAttribute(Constants.ACTIVE_MENU_KEY, "role_list");
		System.out.println(rolesService);
		List<Map<String, Object>> pageList = rolesService.queryPageList(roles);
		model.addAttribute("pageList", pageList);
		model.addAttribute("obj", roles);
		return LIST_VIEW;
	}
	@RequiresRoles("admin")
	@RequestMapping("/assign")
	public String assign(@RequestParam(value="roleId", required=true) Integer roleId, 
			Model model) {
		model.addAttribute(Constants.ACTIVE_MENU_KEY, "role_list");
		List<Compets> otherComList = rolesService.getOtherCompetByRoleId(roleId);
		List<Compets> roleComList = rolesService.getRoleCompetByRoleId(roleId);

		model.addAttribute("roleId", roleId);
		model.addAttribute("roleComList", roleComList);
		model.addAttribute("otherComList", otherComList);
		return ASSIGN_VIEW;
	}
	@RequiresRoles("admin")
	@RequestMapping("/saveAssign")
	public ModelAndView saveAssign(HttpServletRequest request, HttpServletResponse response, 
			RedirectAttributes redirectAttributes) {
		String competIds = request.getParameter("competIds");
		Integer roleId = Integer.parseInt(request.getParameter("roleId"));
		rolesService.deleteRoleCompByRoleId(roleId);
		if(competIds != null && !"".equals(competIds)) {
			String comIdArray[] = competIds.split("~m~");
			for(String comIdStr : comIdArray) {
				Integer compId = Integer.parseInt(comIdStr);
				URole_Compet roleComp = new URole_Compet(roleId, compId);
				rolesService.saveRoleCompet(roleComp);
			}
		}
		
		redirectAttributes.addFlashAttribute("message", "保存成功");
		return new ModelAndView("redirect:/admin/sys/role/list");
	}
	@RequiresRoles("admin")
	@RequestMapping("/add")
	public ModelAndView add(@ModelAttribute Roles roles, Model model) {
		model.addAttribute(Constants.ACTIVE_MENU_KEY, "role_list");
		return new ModelAndView(ADD_VIEW);
	}
	@RequiresRoles("admin")
	@RequestMapping("/save")
	public ModelAndView save(
			@ModelAttribute Roles roles, BindingResult bindingResult, 
			Model model, RedirectAttributes redirectAttributes) {
		roles = rolesService.save(roles);
		redirectAttributes.addFlashAttribute("message", "保存成功");
		return new ModelAndView("redirect:/admin/sys/role/list");
	}
	@RequiresRoles("admin")
	@RequestMapping("/edit/{id}")
	public void edit(HttpServletResponse response, @PathVariable Integer id, Model model) {
		Roles roles = rolesService.get(id);
		String jsonStr = Jackson.getBaseJsonData(roles);
		ServletUtils.printHTML(response, jsonStr);
//		model.addAttribute("roles", roles);
//		return EDIT_VIEW;
	}
	
	/**
	 * 根据id更新记录
	 * @param compets
	 * @param bindingResult
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresRoles("admin")
	@RequestMapping("/update")
	public ModelAndView update(@ModelAttribute("roles") Roles roles, 
			BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {
		//保存信息
		rolesService.update(roles);
		redirectAttributes.addFlashAttribute("message", "保存成功");
		return new ModelAndView("redirect:/admin/sys/role/list");
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
	public ModelAndView delete(@PathVariable Integer id, RedirectAttributes redirectAttributes) throws IOException{
		//删除角色用户关系、删除角色权限关系
		rolesService.deleteRoleCompByRoleId(id);
		rolesService.delete(id);
		redirectAttributes.addFlashAttribute("message", "删除成功");
		return new ModelAndView("redirect:/admin/sys/role/list");
	}
	
}
