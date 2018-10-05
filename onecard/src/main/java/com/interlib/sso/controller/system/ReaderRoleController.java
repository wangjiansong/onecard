package com.interlib.sso.controller.system;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.interlib.sso.common.Cautions;
import com.interlib.sso.common.Constants;
import com.interlib.sso.common.EncryptDecryptData;
import com.interlib.sso.common.MD5Util;
import com.interlib.sso.common.ServletUtils;
import com.interlib.sso.common.json.Jackson;
import com.interlib.sso.common.servlet.ServletUtil;
import com.interlib.sso.domain.LibCode;
import com.interlib.sso.domain.Reader;
import com.interlib.sso.domain.ReaderSession;
import com.interlib.sso.domain.Roles;
import com.interlib.sso.domain.UReader_Role;
import com.interlib.sso.service.LibCodeService;
import com.interlib.sso.service.ReaderService;


/**
 * 系统用户也是来自reader表
 * @author Home
 *
 */
@Controller
@RequestMapping("admin/sys/readerRole")
public class ReaderRoleController {
	private static final Logger logger = Logger.getLogger(ReaderRoleController.class);
	
	
	private static final String LIST_VIEW = "admin/sys/readerRole/list";
	private static final String EDIT_VIEW = "admin/sys/readerRole/edit";
	private static final String ASSIGN_VIEW = "admin/sys/readerRole/assign";
	/* DES加密默认静态秘钥 */
	private static final String DES_STATIC_KEY = "64074f968502295ca41b7db452c7c639";
	@Autowired
	private ReaderService readerService;
	
	@Autowired
	private LibCodeService libcodeService;
	@RequiresRoles("admin")
	@RequestMapping("/list")
	public String list(Model model, Reader reader) {
		model.addAttribute(Constants.ACTIVE_MENU_KEY, "reader_role_list");
		reader.setLibUser(1);
		List<Reader> pageList = readerService.queryReaderList(reader);
		model.addAttribute("pageList", pageList);
		model.addAttribute("obj", reader);
		return LIST_VIEW;
	}
	
	@RequiresRoles("admin")
	@RequestMapping("/assign")
	public String assign(@RequestParam(value="rdId", required=true) String rdId, 
			Model model) {
		model.addAttribute(Constants.ACTIVE_MENU_KEY, "reader_role_list");
		List<Roles> otherRoleList = readerService.getOtherRolesByRdId(rdId);
		List<Roles> readerRoleList = readerService.getReaderRolesByRdId(rdId);

		model.addAttribute("rdId", rdId);
		model.addAttribute("otherRoleList", otherRoleList);
		model.addAttribute("readerRoleList", readerRoleList);
		return ASSIGN_VIEW;
	}
	@RequiresRoles("admin")
	@RequestMapping("/saveAssign")
	public ModelAndView saveAssign(HttpServletRequest request, HttpServletResponse response, 
			RedirectAttributes redirectAttributes) {
		String roleIds = request.getParameter("roleIds");
		String rdId = request.getParameter("rdId");
		readerService.deleteReaderRolesByRdId(rdId);
		if(roleIds != null && !"".equals(roleIds)) {
			String roleIdArray[] = roleIds.split("~m~");
			for(String roleIdStr : roleIdArray) {
				Integer roleId = Integer.parseInt(roleIdStr);
				UReader_Role readerRole = new UReader_Role(rdId, roleId);
				readerService.saveReaderRoles(readerRole);
			}
		}
		
		redirectAttributes.addFlashAttribute("message", "保存成功");
		return new ModelAndView("redirect:/admin/sys/readerRole/list");
	}
	@RequiresRoles("admin")
	@RequestMapping("/addOperator")
	public ModelAndView addOperator(Model model, HttpServletRequest request) {
		model.addAttribute(Constants.ACTIVE_MENU_KEY, "reader_role_list");
		ReaderSession currReader = (ReaderSession) request.getSession().getAttribute("READER_SESSION");
		String rdLibCode = currReader.getReader().getRdLibCode();
		String thisLibCode = "";
		List<Map<String,String>> libCodes = readerService.getLibCode();
		if(rdLibCode != null){
			thisLibCode = rdLibCode;
		}else{
			thisLibCode = libCodes.get(0).get("LIBCODE");
		}
		List<Map<String,String>> readertypes = readerService.getLibReaderType(thisLibCode);
		model.addAttribute("readertypes", Jackson.getBaseJsonData(readertypes));
		model.addAttribute("libcodes", Jackson.getBaseJsonData(libCodes));
		model.addAttribute("defaultLibCode", thisLibCode);
		
		return new ModelAndView("admin/operator/add");
	}
	
	/**
	 * 保存操作员
	 * @param request
	 * @param response
	 * @param inputReader
	 */
	@RequiresRoles("admin")
	@RequestMapping("/saveOperator")
	public void saveOperator(HttpServletRequest request, HttpServletResponse response, Reader inputReader) {
		
		String isExist = readerService.checkRdIdExist(inputReader.getRdId());
		StringBuffer message = new StringBuffer();
		if(isExist.equals("1")) {
			message.append("{");
			message.append("\"success\": -1,");
			message.append("\"message\":\"" + Cautions.READER_EXIT + "\"");
			message.append("}");
			ServletUtils.printHTML(response, message.toString());
			return ;
		} 
		
		try {
			inputReader.setRdCFState((byte)1);
			String password = inputReader.getRdPasswd();
			inputReader.setRdPasswd(EncryptDecryptData.encryptWithCode(DES_STATIC_KEY, password));
			readerService.addReaderAsOperator(inputReader);
			message.append("{");
			message.append("\"success\": 1");
			message.append("}");
			
			ServletUtils.printHTML(response, message.toString());
			return ;
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	@RequiresRoles("admin")
	@RequestMapping("/editOperator/{rdId}")
	public ModelAndView detailOperator(Model model, HttpServletRequest request, @PathVariable String rdId) {
		model.addAttribute(Constants.ACTIVE_MENU_KEY, "reader_role_list");
		Reader reader = readerService.getReader(rdId, (byte)2);
		
		List<LibCode> libcodes = libcodeService.getSimpleInfo();
		
		model.addAttribute("libcodes", libcodes);
		model.addAttribute("reader", reader);
		return new ModelAndView("admin/operator/edit");
	}
	
	@RequiresRoles("admin")
	@RequestMapping("/updOperator")
	public void updateOperator(HttpServletRequest request, HttpServletResponse response, Reader inputReader) {
		
		String newRdId = request.getParameter("newRdId");
		StringBuffer message = new StringBuffer();
		if(!newRdId.equals(inputReader.getRdId())) {
			String isExist = readerService.checkRdIdExist(newRdId);
			if(isExist.equals("1")) {
				message.append("{");
				message.append("\"success\": -1,");
				message.append("\"message\":\"" + Cautions.READER_EXIT + "\"");
				message.append("}");
				ServletUtils.printHTML(response, message.toString());
				return ;
			} 
		}
		try {
			Map<String, Reader> map = new HashMap<String, Reader>();
			Reader newReader = new Reader();
			newReader.setRdId(newRdId);
			map.put("newReader", newReader);
			map.put("inputReader", inputReader);
			readerService.updateReaderAsOperator(map);
			message.append("{");
			message.append("\"success\": 1");
			message.append("}");
			
			ServletUtils.printHTML(response, message.toString());
			return ;
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	@RequiresRoles("admin")
	@RequestMapping("/deleteOperator/{rdId}")
	public ModelAndView updateOperator(Model model, HttpServletRequest request, @PathVariable String rdId, RedirectAttributes redirectAttributes) {
		model.addAttribute(Constants.ACTIVE_MENU_KEY, "reader_role_list");
		if(rdId.equals("admin")) {
			redirectAttributes.addFlashAttribute("message", Cautions.CANNOT_DELETE_ADMIN);
			return new ModelAndView("redirect:/admin/sys/readerRole/list");
		}
		ReaderSession currReader = (ReaderSession) request.getSession().getAttribute("READER_SESSION");
		if(rdId.equals(currReader.getReader().getRdId())) {
			redirectAttributes.addFlashAttribute("message", Cautions.CANNOT_DELETE_CURRENUSER);
			return new ModelAndView("redirect:/admin/sys/readerRole/list");
		}
		readerService.deleteReader(rdId);
		readerService.deleteReaderRolesByRdId(rdId);
		return new ModelAndView("redirect:/admin/sys/readerRole/list");
	}
	
	/**
	 * 重置密码页
	 * @param rdId
	 * @param model
	 * @return
	 */
	@RequiresRoles("admin")
	@RequestMapping(value = "/resetPasswd/{rdId}")
	public ModelAndView resetPasswd(@PathVariable String rdId, Model model) {
		model.addAttribute("rdId", rdId);
		model.addAttribute(Constants.ACTIVE_MENU_KEY, "reader_role_list");
		return new ModelAndView("admin/operator/resetPasswd");
	}
	
	/**
	 * 重置密码
	 * @param request
	 * @param response
	 * @throws InvalidKeySpecException 
	 * @throws NoSuchPaddingException 
	 * @throws NoSuchAlgorithmException 
	 * @throws BadPaddingException 
	 * @throws IllegalBlockSizeException 
	 * @throws InvalidKeyException 
	 */
	@RequiresRoles("admin")
	@RequestMapping("/resetPassword")
	public void resetPassword(HttpServletRequest request, HttpServletResponse response) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeySpecException {
		String rdId = request.getParameter("rdId");
		String oldPassword = request.getParameter("oldPassword");
		//20140626 用户管理修改密码去掉原密码的处理
		if(oldPassword==null || "".equals(oldPassword)){//页面没有传递原密码
			//去掉页面输入原密码，从数据库获取
			String oldPasswordDB = readerService.getReader(rdId, (byte)2).getRdPasswd() ;//request.getParameter("oldPassword");
			oldPassword = EncryptDecryptData.decrypt(DES_STATIC_KEY, oldPasswordDB.substring(1, oldPasswordDB.length()-1));//去掉 ^^
		}
		String newPassword = request.getParameter("newPassword");
		String realPassword = readerService.getRealPassword(rdId);
		int result;
		if(realPassword == null || "".equals(realPassword)) {
			try {
				String encryptPassword = EncryptDecryptData.encrypt(DES_STATIC_KEY, newPassword);
				result = readerService.updatePassword(rdId, "^"+encryptPassword+"^");
			} catch (Exception e) {
				result = -1;
				logger.error("【修改密码】新密码加密出错！");
			}
		} else {
			try {
				String decryptPassword = EncryptDecryptData.decrypt(DES_STATIC_KEY, realPassword.substring(1, realPassword.length()-1));
				
				if(oldPassword.equals(decryptPassword)) {
					String encryptPassword = EncryptDecryptData.encrypt(DES_STATIC_KEY, newPassword);
					result = readerService.updatePassword(rdId, "^"+encryptPassword+"^");
				} else {
					result = -1;
				}
			} catch (Exception e) {
				if(oldPassword.equals(realPassword)) {
					try {
						String encryptPassword = EncryptDecryptData.encrypt(DES_STATIC_KEY, newPassword);
						result = readerService.updatePassword(rdId, "^"+encryptPassword+"^");
					} catch (Exception e1) {
						result = -1;
						logger.error("【修改密码】新密码加密出错！！！");
					}
				} else {
					result = -1;
				}
				logger.error("【修改密码】原密码解密出错！");
			} 
		}
		ServletUtil.responseOut("GBK", String.valueOf(result), response);
	}
	
}
