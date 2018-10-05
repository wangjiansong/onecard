package com.interlib.sso.controller.system;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.interlib.sso.common.Constants;
import com.interlib.sso.common.servlet.ServletUtil;
import com.interlib.sso.domain.Reader;
import com.interlib.sso.domain.ReaderGroup;
import com.interlib.sso.service.ReaderGroupService;

@Controller
@RequestMapping("/admin/subsidy")
public class ReaderGroupController {
	
	@Autowired
	private ReaderGroupService groupService;
	
	/**
	 * 分组管理
	 * @param request
	 * @return
	 */
	@RequiresRoles("admin")
	@RequestMapping(value = "/group")
	public ModelAndView index(HttpServletRequest request, ReaderGroup group) {
		List<Map<String,String>> groups = groupService.queryGroupList(group);
		request.setAttribute("groups", groups);
		request.setAttribute("obj", group);
		request.setAttribute(Constants.ACTIVE_MENU_KEY, "subsidy_grant");
		return new ModelAndView("admin/subsidy/group");
	}

	/**
	 * 分组管理新增页
	 * @param request
	 * @return
	 */
	@RequiresRoles("admin")
	@RequestMapping(value = "/group/add", method = RequestMethod.GET)
	public ModelAndView add(HttpServletRequest request) {
		return new ModelAndView("admin/subsidy/group/add");
	}
	
	/**
	 * 新增用户分组
	 * @param response
	 * @param group
	 */
	@RequiresRoles("admin")
	@RequestMapping(value = "/group/add", method = RequestMethod.POST)
	public void add(HttpServletResponse response, ReaderGroup group) {
		ServletUtil.responseOut("utf-8", String.valueOf(groupService.insertReaderGroup(group)), response);
	}
	
	/**
	 * 用户分组修改页
	 * @param request
	 * @param groupId
	 * @return
	 */
	@RequiresRoles("admin")
	@RequestMapping(value = "/group/edit/{groupId}", method = RequestMethod.GET)
	public ModelAndView edit(HttpServletRequest request, @PathVariable int groupId) {
		ReaderGroup group = groupService.getGroupById(groupId);
		request.setAttribute("group", group);
		return new ModelAndView("admin/subsidy/group/edit");
	}
	
	/**
	 * 修改用户分组
	 * @param response
	 * @param group
	 */
	@RequiresRoles("admin")
	@RequestMapping(value = "/group/edit", method = RequestMethod.POST)
	public void edit(HttpServletResponse response, ReaderGroup group) {
		ServletUtil.responseOut("utf-8", String.valueOf(groupService.updateGroup(group)), response);
	}
	
	/**
	 * 删除用户分组
	 * @param response
	 * @param ids
	 */
	@RequiresRoles("admin")
	@RequestMapping(value = "/group/delete", method = RequestMethod.POST)
	public void delete(HttpServletResponse response, String ids) {
		byte result = 0;
		String[] idArray = ids.split(",");
		int groupResult = groupService.deleteGroup(idArray);
		if (groupResult == idArray.length) {
			result = 1;
		}
		ServletUtil.responseOut("utf-8", String.valueOf(result), response);
	}
	
	/**
	 * 用户分组成员管理
	 * @param request
	 * @param groupId
	 * @return
	 */
	@RequiresRoles("admin")
	@RequestMapping(value = "/group/member/{groupId}")
	public ModelAndView member(HttpServletRequest request, @PathVariable int groupId, Reader reader) {
		List<Map<String, Object>> readers = groupService.queryGroupMemberList(reader);
		request.setAttribute("groupId", groupId);
		request.setAttribute("readers", readers);
		request.setAttribute("obj", reader);
		request.setAttribute(Constants.ACTIVE_MENU_KEY, "subsidy_grant");
		return new ModelAndView("admin/subsidy/group/member");
	}
	
	/**
	 * 分组成员添加页
	 * @param request
	 * @param groupId
	 * @return
	 */
	@RequiresRoles("admin")
	@RequestMapping(value = "/group/member/add/{groupId}")
	public ModelAndView memberAdd(HttpServletRequest request, @PathVariable int groupId, Reader reader) {
		List<Map<String, Object>> readers = groupService.queryNoGroupReaderList(reader);
		request.setAttribute("readers", readers);
		request.setAttribute("groupId", groupId);
		request.setAttribute("obj", reader);
		return new ModelAndView("admin/subsidy/group/member/add");
	}
	
	/**
	 * 添加分组成员
	 * @param request
	 * @param groupId
	 * @param ids
	 * @return
	 */
	@RequiresRoles("admin")
	@RequestMapping(value = "/group/member/addMember", method = RequestMethod.POST)
	public void doAddMember(HttpServletResponse response, int groupId, String ids) {
		byte result = 0;
		String[] rdIdArray = ids.split(",");
		Map<String, Object> params = new HashMap<String, Object>(2);
		params.put("groupId", groupId);
		params.put("rdIdArray", rdIdArray);
		int memberResult = groupService.addMember(params);
		if (memberResult == rdIdArray.length) {
			result = 1;
		}
		ServletUtil.responseOut("utf-8", String.valueOf(result), response);
	}
	
	/**
	 * 删除分组成员
	 * @param response
	 * @param ids
	 */
	@RequiresRoles("admin")
	@RequestMapping(value = "/group/member/deleteMember", method = RequestMethod.POST)
	public void deleteMember(HttpServletResponse response, String ids) {
		byte result = 0;
		String[] rdIdArray = ids.split(",");
		int deleteResult = groupService.deleteMember(rdIdArray);
		if (deleteResult == rdIdArray.length) {
			result = 1;
		}
		ServletUtil.responseOut("utf-8", String.valueOf(result), response);
	}
	
}
