package com.interlib.sso.controller.system;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.interlib.sso.common.Constants;
import com.interlib.sso.common.servlet.ServletUtil;
import com.interlib.sso.domain.LibCode;
import com.interlib.sso.domain.ReaderFee;
import com.interlib.sso.service.LibCodeService;
import com.interlib.sso.service.ReaderFeeService;

/**
 * 分馆管理
 * @author Lullaby
 *
 */
@Controller
@RequestMapping("system/reader/libcode")
public class LibCodeController {
	
	@Autowired
	private LibCodeService service;
	
	@Autowired
	private ReaderFeeService readerFeeService;
	
	/**
	 * 分馆管理
	 * @param model
	 * @return
	 */
	@RequiresRoles("admin")
	@RequestMapping(value = "/libCodeIndex")
	public String libCodeIndex(Model model,LibCode libCode) {
		List<Map<String,String>> list = service.queryLibCodeList(libCode);
		model.addAttribute("list", list);
		model.addAttribute("obj", libCode);
		model.addAttribute(Constants.ACTIVE_MENU_KEY, "system_index");
		model.addAttribute("defaultTab", "libcode");
		return "system/index";
	}
	
	/**
	 * 新增分馆页
	 * @param model
	 * @return
	 */
	@RequiresRoles("admin")
	@RequestMapping(value = "/goAddLibCode")
	public String goAddLibCode(Model model) {
		model.addAttribute(Constants.ACTIVE_MENU_KEY, "system_index");
		return "system/reader/libcode/libCodeAdd";
	}
	
	/**
	 * 检查分馆代码是否已经存在
	 * @param response
	 * @param libCode
	 */
	@RequiresRoles("admin")
	@RequestMapping(value = "/checkLibCode")
	public void checkLibCode(HttpServletResponse response, String libCode) {
		ServletUtil.responseOut("GBK", service.checkLibCode(libCode), response);
	}
	
	/**
	 * 新增分馆
	 * @param model
	 * @return
	 */
	@RequiresRoles("admin")
	@RequestMapping(value = "/addLibCode")
	public void addLibCode(HttpServletResponse response, LibCode libCode) {
		int result = service.insertLibCode(libCode);
		ReaderFee readerFee = new ReaderFee(libCode.getLibCode(), "000", "000", "000", "000", 
				"000","000","000","000","000","000");
		readerFeeService.save(readerFee);
		ServletUtil.responseOut("GBK", String.valueOf(result), response);
	}
	
	/**
	 * 分馆修改页
	 * @param model
	 * @param libCode
	 * @return
	 */
	@RequiresRoles("admin")
	@RequestMapping(value = "/goEditLibCode/{libCode}")
	public String goEditLibCode(Model model, @PathVariable String libCode) {
		model.addAttribute("lib", service.getLibByCode(libCode));
		model.addAttribute(Constants.ACTIVE_MENU_KEY, "system_index");
		return "system/reader/libcode/libCodeEdit";
	}
	
	/**
	 * 修改分馆信息时检查分馆代码是否存在
	 * @param response
	 * @param libCode
	 */
	@RequiresRoles("admin")
	@RequestMapping(value = "/checkLibCodeEdit")
	public void checkLibCodeEdit(HttpServletResponse response, String libCode) {
		ServletUtil.responseOut("GBK", service.checkLibCodeEdit(libCode), response);
	}
	
	/**
	 * 修改分馆信息
	 * @param response
	 * @param libCode
	 */
	@RequiresRoles("admin")
	@RequestMapping(value = "/editLibCode")
	public void editLibCode(HttpServletResponse response, LibCode libCode) {
		ReaderFee readerFee = readerFeeService.get(libCode.getLibCode());
		if(readerFee == null) {
			readerFee = new ReaderFee(libCode.getLibCode(), "000", "000", "000", "000", 
					"000","000","000","000","000","000");
			readerFeeService.save(readerFee);
		}
		ServletUtil.responseOut("GBK", String.valueOf(service.editLibCode(libCode)), response);
	}
	
	/**
	 * 删除分馆记录
	 * @param response
	 * @param libCode
	 */
	@RequiresRoles("admin")
	@RequestMapping(value = "/deleteLibCode")
	public void deleteLibCode(HttpServletResponse response, String libCode) {
		if(!libCode.equals("999")) {
			ServletUtil.responseOut("GBK", String.valueOf(service.deleteLibCode(libCode)), response);
		}
	}
	
}
