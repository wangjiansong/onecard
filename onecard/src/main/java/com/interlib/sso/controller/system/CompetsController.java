package com.interlib.sso.controller.system;

import java.io.IOException;
import java.util.ArrayList;
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
import com.interlib.sso.common.StringUtils;
import com.interlib.sso.common.json.Jackson;
import com.interlib.sso.domain.Compets;
import com.interlib.sso.domain.Resources;
import com.interlib.sso.domain.UCompet_Resource;
import com.interlib.sso.service.CompetsService;
import com.interlib.sso.service.ResourceService;

@Controller
@RequestMapping("/admin/sys/compet")
public class CompetsController {

	@Autowired
	public CompetsService competsService;
	
	@Autowired
	public ResourceService resourcesService;
	
	private static final String LIST_VIEW = "admin/sys/compet/list";
	private static final String EDIT_VIEW = "admin/sys/compet/edit";
	private static final String ADD_VIEW = "admin/sys/compet/add";
	private static final String ASSIGN_VIEW = "admin/sys/compet/assign";
	
	@RequiresRoles("admin")
	@RequestMapping("/list")
	public String list(Model model, Compets compets) {
		model.addAttribute(Constants.ACTIVE_MENU_KEY, "compet_list");
		List<Map<String, Object>> pageList = competsService.queryPageList(compets);
		model.addAttribute("pageList", pageList);
		model.addAttribute("obj", compets);
		return LIST_VIEW;
	}
	@RequiresRoles("admin")
	@RequestMapping("/assign")
	public String listTree(@RequestParam(value="competId", required=true) Integer competId, 
			Model model) {
		model.addAttribute(Constants.ACTIVE_MENU_KEY, "compet_list");
		List<String> subsysList = resourcesService.listAllSubsys(); //所有资源的父菜单
		List<String> resIdList = competsService.getResourceByCompetId(competId); //权限包含的资源
		StringBuffer sb = new StringBuffer();
		sb.append("[");
		List<Resources> comRes = new ArrayList<Resources>();
		for(String s : subsysList) {
			sb.append("{");
			sb.append("\"id\":\"" + s + "\",");
			sb.append("\"name\":\"" + getSubsysName(s) + "\",");
			List<Resources> resList = resourcesService.getResourcesBySubsys(s); //父菜单包含的资源
			sb.append("\"children\":[");
			boolean hasResource = false;
			for(Resources r : resList) {
				sb.append("{");
				sb.append("\"id\":\"" + r.getResourceId() + "\",");
				sb.append("\"name\":\"" + r.getResourceName() + "\"");
				if(resIdList.contains(r.getResourceId())) {
					hasResource = true;
					sb.append(",\"checked\":\"true\"");
					comRes.add(r);
				}
				sb.append("},");
			}
			StringUtils.removeEndChar(sb, ",");
			sb.append("]");
			if(hasResource) {
				sb.append(",");
				sb.append("\"checked\":\"true\"");
			}
			sb.append("},");
		}
		StringUtils.removeEndChar(sb, ",");
		sb.append("]");
		System.out.println(sb.toString());
		model.addAttribute("comRes", comRes);
		model.addAttribute("competId", competId);
		model.addAttribute("resourceMenu", sb.toString());
		
		return ASSIGN_VIEW;
	}
	@RequiresRoles("admin")
	@RequestMapping("/saveAssign")
	public ModelAndView saveAssign(HttpServletRequest request, HttpServletResponse response, 
			RedirectAttributes redirectAttributes) {
		String resIds = request.getParameter("resIds");
		Integer competId = Integer.parseInt(request.getParameter("competId"));
		competsService.deleteComResourceByCompetId(competId);
		if(resIds != null && !"".equals(resIds)) {
			String resIdArray[] = resIds.split("~m~");
			for(String resId : resIdArray) {
				UCompet_Resource comRes = new UCompet_Resource(competId, resId);
				competsService.saveCompet_Resource(comRes);
			}
		}
		redirectAttributes.addFlashAttribute("message", "保存成功");
		return new ModelAndView("redirect:/admin/sys/compet/list");
	}
	
	@RequiresRoles("admin")
	@RequestMapping("/add")
	public ModelAndView add(Model model, @ModelAttribute Compets compets) {
		model.addAttribute(Constants.ACTIVE_MENU_KEY, "compet_list");
		return new ModelAndView(ADD_VIEW);
	}
	
	@RequiresRoles("admin")
	@RequestMapping("/save")
	public ModelAndView save(
			@ModelAttribute Compets compets, BindingResult bindingResult, 
			Model model, RedirectAttributes redirectAttributes) {
		compets = competsService.save(compets);
		redirectAttributes.addFlashAttribute("message", "保存成功");
		return new ModelAndView("redirect:/admin/sys/compet/list");
	}
	
	@RequiresRoles("admin")
	@RequestMapping("/edit/{id}")
	public void edit(HttpServletResponse response, @PathVariable Integer id, Model model) {
		Compets compets = competsService.get(id);
		
		String jsonStr = Jackson.getBaseJsonData(compets);
		
		ServletUtils.printHTML(response, jsonStr);
		
//		model.addAttribute("compets", compets);
		
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
	public ModelAndView update(@ModelAttribute("compets")  Compets compets, 
			BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {
		//保存信息
		competsService.update(compets);
		
		redirectAttributes.addFlashAttribute("message", "保存成功");
		return new ModelAndView("redirect:/admin/sys/compet/list");
	}
	
	@RequiresRoles("admin")
	@RequestMapping("/validate/{id}")
	public void validateResource(@PathVariable Integer id, HttpServletResponse response) {
		//检查当前资源是否已经分配给权限了，返回提示
		List<Integer> roleIdList = competsService.getRoleCompByCompetId(id);
		String result = "";
		if(roleIdList != null && roleIdList.size() > 0) {
			result = "该权限已被分配在角色中";
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
	public ModelAndView delete(@PathVariable Integer id,RedirectAttributes redirectAttributes) throws IOException{
		//删除角色权限关系，删除权限资源关系
		competsService.deleteRoleCompByCompetId(id);
		competsService.deleteComResourceByCompetId(id);
		competsService.delete(id);
		return new ModelAndView("redirect:/admin/sys/compet/list");
	}
	
	
	public String getSubsysName(String sysid) {
		if(sysid.equals("library")) {
			return "系统组织管理";
		} else if(sysid.equals("rights")) {
			return "授权管理";
		} else if(sysid.equals("reader")) {
			return "用户管理";
		} else if(sysid.equals("cirfin")) {
			return "结算中心";
		} else if(sysid.equals("logcir")) {
			return "系统日志";
		} else {
			return "";
		}
		
	}
}
