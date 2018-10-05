package com.interlib.sso.controller.system;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.interlib.sso.common.Constants;
import com.interlib.sso.common.json.Jackson;
import com.interlib.sso.common.servlet.ServletUtil;
import com.interlib.sso.domain.ReaderGroup;
import com.interlib.sso.domain.SubsidyGrant;
import com.interlib.sso.service.SubsidyGrantService;

/**
 * 补助发放
 * @author Lullaby
 *
 */
@Controller
@RequestMapping("/admin/subsidy")
public class SubsidyGrantController {
	
	@Autowired
	private SubsidyGrantService grantService;
	
	/**
	 * 自定义数据格式绑定
	 * @param binder
	 */
	@InitBinder
	public void initBinder(ServletRequestDataBinder binder) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
	}
	
	/**
	 * 补助发放
	 * @param request
	 * @return
	 */
	@RequiresRoles("admin")
	@RequestMapping(value = "/grant")
	public ModelAndView index(HttpServletRequest request, SubsidyGrant grant) {
		List<Map<String, String>> list = grantService.queryGrantList(grant);
		request.setAttribute("list", list);
		request.setAttribute("obj", grant);
		request.setAttribute(Constants.ACTIVE_MENU_KEY, "subsidy_grant");
		return new ModelAndView("admin/subsidy/grant");
	}
	
	/**
	 * 补助发放新增页
	 * @param request
	 * @return
	 */
	@RequiresRoles("admin")
	@RequestMapping(value = "/grant/add", method = RequestMethod.GET)
	public ModelAndView add(HttpServletRequest request) {
		request.setAttribute(Constants.ACTIVE_MENU_KEY, "subsidy_grant");
		return new ModelAndView("admin/subsidy/grant/add");
	}
	
	/**
	 * 新增补助发放
	 * @param request
	 * @return
	 */
	@RequiresRoles("admin")
	@RequestMapping(value = "/grant/add", method = RequestMethod.POST)
	public void add(HttpServletResponse response, SubsidyGrant grant) {
		byte result = 0;
		int grantId = grantService.getGrantId();
		grant.setGrantId(grantId);
		int grantResult = grantService.insertGrant(grant);
		if (grantResult == 1) {
			List<Date> dateList = grant.getGrantDateList();
			int size = dateList.size();
			List<Map<String, Object>> paraList = new ArrayList<Map<String, Object>>(size);
			Map<String, Object> map = null;
			for (int i = 0; i < size; i++) {
				map = new HashMap<String, Object>(2);
				map.put("grantId", grantId);
				map.put("grantDate", dateList.get(i));
				paraList.add(map);
			}
			int dateResult = grantService.insertGrantDate(paraList);
			if (dateResult == size) {
				result = 1;
			}
		}
		ServletUtil.responseOut("gbk", String.valueOf(result), response);
	}
	
	/**
	 * 补助发放修改页
	 * @param request
	 * @param grantId
	 * @return
	 */
	@RequiresRoles("admin")
	@RequestMapping(value = "/grant/edit/{grantId}", method = RequestMethod.GET)
	public ModelAndView edit(HttpServletRequest request, @PathVariable int grantId) {
		SubsidyGrant grant = grantService.getGrantById(grantId);
		List<String> dateList = grantService.getGrantDatesById(grantId);
		request.setAttribute("grant", grant);
		request.setAttribute("dateList", Jackson.getBaseJsonData(dateList));
		request.setAttribute(Constants.ACTIVE_MENU_KEY, "subsidy_grant");
		return new ModelAndView("admin/subsidy/grant/edit");
	}
	
	/**
	 * 修改补助发放
	 * @param request
	 * @param grant
	 * @return
	 */
	@RequiresRoles("admin")
	@RequestMapping(value = "/grant/edit", method = RequestMethod.POST)
	public void edit(HttpServletResponse response, SubsidyGrant grant) {
		byte result = 0;
		int grantId = grant.getGrantId();
		int dateResult = grantService.deleteGrantDateById(grantId);
		if (dateResult > 0) {
			List<Date> dateList = grant.getGrantDateList();
			int size = dateList.size();
			List<Map<String, Object>> paraList = new ArrayList<Map<String, Object>>(size);
			Map<String, Object> map = null;
			for (int i = 0; i < size; i++) {
				map = new HashMap<String, Object>(2);
				map.put("grantId", grantId);
				map.put("grantDate", dateList.get(i));
				paraList.add(map);
			}
			int insertResult = grantService.insertGrantDate(paraList);
			if (insertResult == size) {
				int updateResult = grantService.updateGrantById(grant);
				if (updateResult == 1) {
					result = 1;
				}
			}
		}
		ServletUtil.responseOut("gbk", String.valueOf(result), response);
	}
	
	/**
	 * 删除补助发放
	 * @param response
	 * @param ids
	 */
	@RequiresRoles("admin")
	@RequestMapping(value = "/grant/delete", method = RequestMethod.POST)
	public void delete(HttpServletResponse response, String ids) {
		byte result = 0;
		String[] idArray = ids.split(",");
		int idCount = idArray.length;
		int grantResult = grantService.deleteGrantByIdArray(idArray);
		if (grantResult == idCount) {
			int dateResult = grantService.deleteGrantDateByIdArray(idArray);
			if (dateResult >= idCount) {
				result = 1;
			}
		}
		ServletUtil.responseOut("utf-8", String.valueOf(result), response);
	}
	
	/**
	 * 指定发放分组
	 * @param request
	 * @param grantId
	 * @return
	 */
	@RequiresRoles("admin")
	@RequestMapping(value = "/grant/assign/{grantId}")
	public ModelAndView assign(HttpServletRequest request, ReaderGroup group) {
		List<Map<String, String>> groups = grantService.queryAssignGroupList(group);
		request.setAttribute("grantId", group.getGrantId());
		request.setAttribute("groups", groups);
		request.setAttribute("obj", group);
		request.setAttribute(Constants.ACTIVE_MENU_KEY, "subsidy_grant");
		return new ModelAndView("admin/subsidy/grant/assign");
	}
	
	/**
	 * 指定分组新增页
	 * @param request
	 * @param grantId
	 * @return
	 */
	@RequiresRoles("admin")
	@RequestMapping(value = "/grant/assign/add/{grantId}")
	public ModelAndView add(HttpServletRequest request, ReaderGroup group) {
		List<Map<String, String>> list = grantService.queryUnassignedGroupList(group);
		request.setAttribute("grantId", group.getGrantId());
		request.setAttribute("groups", list);
		request.setAttribute("obj", group);
		return new ModelAndView("admin/subsidy/grant/assign/add");
	}
	
	/**
	 * 添加指定分组
	 * @param request
	 * @param grantId
	 * @param ids
	 */
	@RequiresRoles("admin")
	@RequestMapping(value = "/grant/assign/group/addGroup/{grantId}", method = RequestMethod.POST)
	public void add(HttpServletResponse response, @PathVariable int grantId, String ids) {
		byte result = 0;
		String[] groupIds = ids.split(",");
		int len = groupIds.length;
		List<Map<String, Object>> paramList = new ArrayList<Map<String, Object>>(len);
		Map<String, Object> map = null;
		for (int i = 0; i < len; i++) {
			map = new HashMap<String, Object>(2);
			map.put("grantId", grantId);
			map.put("groupId", groupIds[i]);
			paramList.add(map);
		}
		int groupResult = grantService.insertAssignGroup(paramList);
		if (groupResult == len) {
			result = 1;
		}
		ServletUtil.responseOut("utf-8", String.valueOf(result), response);
	}
	
	/**
	 * 删除已指派的分组
	 * @param response
	 * @param grantId
	 * @param ids
	 */
	@RequiresRoles("admin")
	@RequestMapping(value = "/grant/assign/delete", method = RequestMethod.POST)
	public void delete(HttpServletResponse response, int grantId, String ids) {
		byte result = 0;
		String[] groupIds = ids.split(",");
		Map<String, Object> param = new HashMap<String, Object>(2);
		param.put("grantId", grantId);
		param.put("groupIds", groupIds);
		int groupResult = grantService.deleteAssignGroup(param);
		if (groupResult == groupIds.length) {
			result = 1;
		}
		ServletUtil.responseOut("utf-8", String.valueOf(result), response);
	}
	
}