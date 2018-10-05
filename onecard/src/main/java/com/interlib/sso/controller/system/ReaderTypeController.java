package com.interlib.sso.controller.system;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.interlib.sso.common.Constants;
import com.interlib.sso.common.json.Jackson;
import com.interlib.sso.common.servlet.ServletUtil;
import com.interlib.sso.domain.Num;
import com.interlib.sso.domain.ReaderType;
import com.interlib.sso.service.NumService;
import com.interlib.sso.service.ReaderTypeService;

/**
 * 读者流通类型
 * @author Lullaby
 *
 */
@Controller
@RequestMapping("system/reader/readertype")
public class ReaderTypeController {
	
	@Autowired
	private ReaderTypeService readerTypeService;
	
	@Autowired
	private NumService numService;
	
	public void setReaderTypeSerive(ReaderTypeService readerTypeService) {
		this.readerTypeService = readerTypeService;
	}
	
	/**
	 * 读者类型列表
	 * @param model
	 * @param readerType
	 * @return
	 */
	@RequiresRoles("admin")
	@RequestMapping(value = "/readerTypeIndex")
	public ModelAndView queryReaderTypeList(Model model, ReaderType readerType) {
		List<Map<String,String>> list = readerTypeService.queryReaderTypeList(readerType);
		model.addAttribute("list", list);
		model.addAttribute("obj", readerType);
		model.addAttribute(Constants.ACTIVE_MENU_KEY, "system_index");
		model.addAttribute("defaultTab", "readertype");
		return new ModelAndView("system/reader/readertype/readerTypeIndex");
	}
	
	/**
	 * 读者流通类型新增页
	 * @return
	 */
	@RequiresRoles("admin")
	@RequestMapping(value = "/goAddReaderType")
	public ModelAndView goAddReaderType(Model model) {
		model.addAttribute("libcodes", Jackson.getBaseJsonData(readerTypeService.getLibCode()));
		model.addAttribute(Constants.ACTIVE_MENU_KEY, "system_index");
		return new ModelAndView("system/reader/readertype/readerTypeAdd");
	}
	
	/**
	 * 检查读者类型是否存在(新增操作)
	 * @param response
	 * @param readerType
	 */
	@RequiresRoles("admin")
	@RequestMapping(value = "/checkReaderType")
	public void checkReaderType(HttpServletResponse response, String readerType) {
		ServletUtil.responseOut("GBK", readerTypeService.checkReaderType(readerType), response);
	}
	
	/**
	 * 检查读者类型是否存在(修改操作)
	 * @param response
	 * @param readerType
	 */
	@RequiresRoles("admin")
	@RequestMapping(value = "/checkReaderTypeEdit")
	public void checkReaderTypeEdit(HttpServletResponse response, String readerType, String readerType_old) {
		Map<String,String> params = new HashMap<String,String>();
		params.put("readerType", readerType);
		params.put("readerType_old", readerType_old);
		ServletUtil.responseOut("GBK", readerTypeService.checkReaderTypeEdit(params), response);
	}
	
	/**
	 * 新增读者类型
	 * @param response
	 * @param readerType
	 */
	@RequiresRoles("admin")
	@RequestMapping(value = "/addReaderType")
	public void addReaderType(HttpServletResponse response, ReaderType readerType) {
		ServletUtil.responseOut("GBK", String.valueOf(readerTypeService.addReaderType(readerType)), response);
		
		//福建省图注册读者根据读者类型添加Num
		Num num = new Num();
		num.setId(readerType.getReaderType().substring(8));
		num.setNum("0");
		numService.addNum(num);
	}
	
	/**
	 * 读者类型修改页
	 * @param model
	 * @return
	 */
	@RequiresRoles("admin")
	@RequestMapping(value = "/goEditReaderType/{readerType}")
	public ModelAndView goEditReaderType(Model model, @PathVariable String readerType) {
		model.addAttribute("libcodes", Jackson.getBaseJsonData(readerTypeService.getLibCode()));
		model.addAttribute("readertype", readerTypeService.getReaderType(readerType));
		model.addAttribute(Constants.ACTIVE_MENU_KEY, "system_index");
		return new ModelAndView("system/reader/readertype/readerTypeEdit");
	}
	
	/**
	 * 修改读者类型信息
	 * @param response
	 * @param readerType
	 */
	@RequiresRoles("admin")
	@RequestMapping(value = "/editReaderType")
	public void editReaderType(HttpServletRequest request, HttpServletResponse response, ReaderType readerType) {
		String readerType_old = request.getParameter("readerType_old");
		Map<String,String> params = new HashMap<String,String>();
		params.put("readerType_new", readerType.getReaderType());
		params.put("readerType_old", readerType_old);
		params.put("libCode", readerType.getLibCode());
		params.put("sign", String.valueOf(readerType.getSign()));
		params.put("libSign", String.valueOf(readerType.getLibSign()));
		params.put("descripe", readerType.getDescripe());
		params.put("valdate", readerType.getValdate()+"");
		params.put("deposity", String.valueOf(readerType.getDeposity()));
		params.put("checkfee", String.valueOf(readerType.getCheckfee()));
		params.put("idfee", String.valueOf(readerType.getIdfee()));
		params.put("servicefee", String.valueOf(readerType.getServicefee()));
		
		ServletUtil.responseOut("GBK", String.valueOf(readerTypeService.editReaderType(params)), response);
	}
	
	/**
	 * 删除读者类型
	 * @param response
	 * @param readerType
	 */
	@RequiresRoles("admin")
	@RequestMapping(value = "/deleteReaderType")
	public void deleteReaderType(HttpServletResponse response, String readerType) {
		ServletUtil.responseOut("GBK", String.valueOf(readerTypeService.deleteReaderType(readerType)), response);
	}
	
}
