package com.interlib.sso.controller.system;

import java.util.ArrayList;
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
import com.interlib.sso.domain.CardGroup;
import com.interlib.sso.domain.Reader;
import com.interlib.sso.service.CardGroupService;

/**
 * 刷卡消费分组管理
 * @author Lullaby
 *
 */
@Controller
@RequestMapping("/admin/card/group")
public class CardGroupController {
	
	@Autowired
	private CardGroupService groupService;
	
	/**
	 * 分组index页
	 * @param request
	 * @param group
	 * @return
	 */
	@RequiresRoles("admin")
	@RequestMapping(value = "/index")
	public ModelAndView index(HttpServletRequest request, CardGroup group) {
		List<Map<String,String>> groups = groupService.queryGroupList(group);
		request.setAttribute("groups", groups);
		request.setAttribute("obj", group);
		request.setAttribute(Constants.ACTIVE_MENU_KEY, "card_consumption");
		return new ModelAndView("admin/card/group");
	}
	
	/**
	 * 分组管理新增页
	 * @param request
	 * @return
	 */
	@RequiresRoles("admin")
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public ModelAndView add(HttpServletRequest request) {
		return new ModelAndView("admin/card/group/add");
	}
	
	/**
	 * 新增用户分组
	 * @param response
	 * @param group
	 */
	@RequiresRoles("admin")
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public void add(HttpServletResponse response, CardGroup group) {
		ServletUtil.responseOut("utf-8", String.valueOf(groupService.insertGroup(group)), response);
	}
	
	/**
	 * 用户分组修改页
	 * @param request
	 * @param groupId
	 * @return
	 */
	@RequiresRoles("admin")
	@RequestMapping(value = "/edit/{groupId}", method = RequestMethod.GET)
	public ModelAndView edit(HttpServletRequest request, @PathVariable int groupId) {
		CardGroup group = groupService.getGroupById(groupId);
		request.setAttribute("group", group);
		return new ModelAndView("admin/card/group/edit");
	}
	
	/**
	 * 修改用户分组
	 * @param response
	 * @param group
	 */
	@RequiresRoles("admin")
	@RequestMapping(value = "/edit", method = RequestMethod.POST)
	public void edit(HttpServletResponse response, CardGroup group) {
		ServletUtil.responseOut("utf-8", String.valueOf(groupService.updateGroup(group)), response);
	}
	
	/**
	 * 删除用户分组
	 * @param response
	 * @param ids
	 */
	@RequiresRoles("admin")
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public void delete(HttpServletResponse response, String ids) {
		byte result = 0;
		String[] idArray = ids.split(",");
		int groupResult = groupService.deleteGroup(idArray);
		if (groupResult == idArray.length) {
			groupService.deleteGroupMember(idArray);
			groupService.deleteGroupAssign(idArray);
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
	@RequestMapping(value = "/member/{groupId}")
	public ModelAndView member(HttpServletRequest request, @PathVariable int groupId, Reader reader) {
		List<Map<String, Object>> readers = groupService.queryGroupMemberList(reader);
		request.setAttribute("groupId", groupId);
		request.setAttribute("readers", readers);
		request.setAttribute("obj", reader);
		request.setAttribute(Constants.ACTIVE_MENU_KEY, "card_consumption");
		return new ModelAndView("admin/card/group/member");
	}
	
	/**
	 * 分组成员添加页
	 * @param request
	 * @param groupId
	 * @return
	 */
	@RequiresRoles("admin")
	@RequestMapping(value = "/member/add/{groupId}")
	public ModelAndView memberAdd(HttpServletRequest request, @PathVariable int groupId, Reader reader) {
		List<Map<String, Object>> readers = groupService.queryNoGroupReaderList(reader);
		request.setAttribute("readers", readers);
		request.setAttribute("groupId", groupId);
		request.setAttribute("obj", reader);
		return new ModelAndView("admin/card/group/member/add");
	}
	
	/**
	 * 添加分组成员
	 * @param request
	 * @param groupId
	 * @param ids
	 * @return
	 */
	@RequiresRoles("admin")
	@RequestMapping(value = "/member/addMember", method = RequestMethod.POST)
	public void doAddMember(HttpServletResponse response, int groupId, String ids) {
		byte result = 0;
		String[] rdIdArray = ids.split(",");
		int len = rdIdArray.length;
		List<Map<String, Object>> params = new ArrayList<Map<String, Object>>(len);
		Map<String, Object> map = null;
		for (int i = 0; i < len; i++) {
			map = new HashMap<String, Object>(2);
			map.put("groupId", groupId);
			map.put("rdId", rdIdArray[i]);
			params.add(map);
		}
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
	@RequestMapping(value = "/member/deleteMember", method = RequestMethod.POST)
	public void deleteMember(HttpServletResponse response, int groupId, String ids) {
		byte result = 0;
		String[] rdIdArray = ids.split(",");
		Map<String, Object> params = new HashMap<String, Object>(2);
		params.put("groupId", groupId);
		params.put("rdIdArray", rdIdArray);
		int deleteResult = groupService.deleteMember(params);
		if (deleteResult == rdIdArray.length) {
			result = 1;
		}
		ServletUtil.responseOut("utf-8", String.valueOf(result), response);
	}

}
