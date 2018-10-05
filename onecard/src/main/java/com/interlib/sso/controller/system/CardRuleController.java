package com.interlib.sso.controller.system;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
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
import com.interlib.sso.common.TimeUtils;
import com.interlib.sso.common.servlet.ServletUtil;
import com.interlib.sso.domain.CardRule;
import com.interlib.sso.domain.CardRuleDetail;
import com.interlib.sso.service.CardRuleService;

/**
 * 刷卡消费规则
 * @author Lullaby
 *
 */
@Controller
@RequestMapping("/admin/card/consumption")
public class CardRuleController {
	
	@Autowired
	private CardRuleService ruleService;
	
	/**
	 * 刷卡消费管理index页
	 * @param request
	 * @param rule
	 * @return
	 */
	@RequiresRoles("admin")
	@RequestMapping(value = "/rule")
	public ModelAndView ruleIndex(HttpServletRequest request, CardRule rule) {
		List<Map<String, String>> list = ruleService.queryCardRuleList(rule);
		request.setAttribute("list", list);
		request.setAttribute("obj", rule);
		request.setAttribute(Constants.ACTIVE_MENU_KEY, "card_consumption");
		return new ModelAndView("admin/card/consumption/rule");
	}
	
	/**
	 * 刷卡消费规则新增页
	 * @param request
	 * @param rule
	 * @return
	 */
	@RequiresRoles("admin")
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public ModelAndView addCardRule(HttpServletRequest request, CardRule rule) {
		request.setAttribute(Constants.ACTIVE_MENU_KEY, "card_consumption");
		return new ModelAndView("admin/card/consumption/rule/add");
	}
	
	/**
	 * 新增刷卡消费规则
	 * @param response
	 * @param rule
	 */
	@RequiresRoles("admin")
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public void addCardRule(HttpServletResponse response, CardRule rule) {
		byte result = 0;
		List<String> details = rule.getDetails();
		if (!details.isEmpty()) {
			int ruleId = ruleService.getRuleId();
			rule.setRuleId(ruleId);
			int ruleResult = ruleService.insertCardRule(rule);
			if (ruleResult == 1) {
				int size = details.size();
				List<CardRuleDetail> params = new ArrayList<CardRuleDetail>(size);
				String item;
				String[] array;
				CardRuleDetail detail;
				for (int i = 0; i < size; i++) {
					item = details.get(i);
					array = item.split(",");
					if (array.length == 5) {
						detail = new CardRuleDetail();
						detail.setRuleId(ruleId);
						detail.setStartTime(array[0]);
						detail.setEndTime(array[1]);
						detail.setSalePrice(Float.parseFloat(array[2]));
						detail.setCostPrice(Float.parseFloat(array[3]));
						detail.setTimeFlag(Byte.parseByte(array[4]));
						params.add(detail);
					}
				}
				int detailResult = ruleService.insertCardRuleDetail(params);
				if (detailResult == size) {
					result = 1;
				}
			}
		}
		ServletUtil.responseOut("utf-8", String.valueOf(result), response);
	}
	
	/**
	 * 删除消费规则
	 * @param response
	 * @param ids
	 */
	@RequiresRoles("admin")
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public void delete(HttpServletResponse response, String ids) {
		byte result = 0;
		String[] idArray = ids.split(",");
		int ruleResult = ruleService.deleteCardRule(idArray);
		int length = idArray.length;
		if (ruleResult == length) {
			int detailResult = ruleService.deleteCardRuleDetail(idArray);
			if (detailResult == length * 3) {
				ruleService.deleteCardRuleAssign(idArray);
				result = 1;
			}
		}
		ServletUtil.responseOut("utf-8", String.valueOf(result), response);
	}
	
	/**
	 * 消费规则详情
	 * @param request
	 * @param ruleId
	 * @return
	 */
	@RequiresRoles("admin")
	@RequestMapping(value = "/rule/detail/{ruleId}", method = RequestMethod.GET)
	public ModelAndView ruleDetail(HttpServletRequest request, @PathVariable int ruleId) {
		Map<String, String> rule = ruleService.getCardRuleById(ruleId);
		List<Map<String, String>> details = ruleService.getCardRuleDetails(ruleId);
		request.setAttribute("rule", rule);
		request.setAttribute("details", details);
		return new ModelAndView("admin/card/consumption/rule/detail");
	}
	
	/**
	 * 刷卡消费规则修改页
	 * @param request
	 * @param rule
	 * @return
	 */
	@RequiresRoles("admin")
	@RequestMapping(value = "/edit/{ruleId}", method = RequestMethod.GET)
	public ModelAndView editCardRule(HttpServletRequest request, @PathVariable int ruleId) {
		Map<String, String> rule = ruleService.getCardRuleById(ruleId);
		List<Map<String, String>> details = ruleService.getCardRuleDetails(ruleId);
		request.setAttribute("rule", rule);
		request.setAttribute("details", details);
		request.setAttribute(Constants.ACTIVE_MENU_KEY, "card_consumption");
		return new ModelAndView("admin/card/consumption/rule/edit");
	}
	
	/**
	 * 修改刷卡消费规则
	 * @param response
	 * @param rule
	 */
	@RequiresRoles("admin")
	@RequestMapping(value = "/edit", method = RequestMethod.POST)
	public void editCardRule(HttpServletResponse response, CardRule rule) {
		byte result = 0;
		List<String> details = rule.getDetails();
		if (!details.isEmpty()) {
			int ruleId = rule.getRuleId();
			int ruleResult = ruleService.updateCardRule(rule);
			if (ruleResult == 1) {
				int size = details.size();
				String[] idArray = new String[1];
				idArray[0] = String.valueOf(ruleId);
				int deleteResult = ruleService.deleteCardRuleDetail(idArray);
				if (deleteResult == size) {
					List<CardRuleDetail> params = new ArrayList<CardRuleDetail>(size);
					String item;
					String[] array;
					CardRuleDetail detail;
					for (int i = 0; i < size; i++) {
						item = details.get(i);
						array = item.split(",");
						if (array.length == 5) {
							detail = new CardRuleDetail();
							detail.setRuleId(ruleId);
							detail.setStartTime(array[0]);
							detail.setEndTime(array[1]);
							detail.setSalePrice(Float.parseFloat(array[2]));
							detail.setCostPrice(Float.parseFloat(array[3]));
							detail.setTimeFlag(Byte.parseByte(array[4]));
							params.add(detail);
						}
					}
					int detailResult = ruleService.insertCardRuleDetail(params);
					if (detailResult == size) {
						result = 1;
					}
				}
			}
		}
		ServletUtil.responseOut("utf-8", String.valueOf(result), response);
	}
	
	/**
	 * 指定消费分组页
	 * @param request
	 * @param rule
	 * @return
	 */
	@RequiresRoles("admin")
	@RequestMapping(value = "/assign/{ruleId}")
	public ModelAndView assign(HttpServletRequest request, CardRule rule) {
		List<Map<String, String>> groups = ruleService.queryAssignGroupList(rule);
		request.setAttribute("ruleId", rule.getRuleId());
		request.setAttribute("groups", groups);
		request.setAttribute("obj", rule);
		request.setAttribute(Constants.ACTIVE_MENU_KEY, "card_consumption");
		return new ModelAndView("admin/card/consumption/rule/assign");
	}
	
	/**
	 * 指定消费分组新增页
	 * @param request
	 * @param rule
	 * @return
	 */
	@RequiresRoles("admin")
	@RequestMapping(value = "/assign/add/{ruleId}")
	public ModelAndView add(HttpServletRequest request, CardRule rule) {
		List<Map<String, String>> list = ruleService.queryUnAssignedGroupList(rule);
		request.setAttribute("ruleId", rule.getRuleId());
		request.setAttribute("groups", list);
		request.setAttribute("obj", rule);
		return new ModelAndView("admin/card/consumption/rule/assign/add");
	}
	
	/**
	 * 添加指定分组
	 * @param request
	 * @param ruleId
	 * @param ids
	 */
	@RequiresRoles("admin")
	@RequestMapping(value = "/assign/group/{ruleId}", method = RequestMethod.POST)
	public void add(HttpServletResponse response, @PathVariable int ruleId, String ids) {
		byte result = 0;
		String[] groupIds = ids.split(",");
		int len = groupIds.length;
		List<Map<String, Object>> paramList = new ArrayList<Map<String, Object>>(len);
		Map<String, Object> map = null;
		for (int i = 0; i < len; i++) {
			map = new HashMap<String, Object>(2);
			map.put("ruleId", ruleId);
			map.put("groupId", groupIds[i]);
			paramList.add(map);
		}
		int groupResult = ruleService.insertAssignGroup(paramList);
		if (groupResult == len) {
			int updateResult = ruleService.updateGroupAssigned(groupIds);
			if (updateResult == len) {
				result = 1;
			}
		}
		ServletUtil.responseOut("utf-8", String.valueOf(result), response);
	}
	
	/**
	 * 删除指定分组
	 * @param request
	 * @param ruleId
	 * @param ids
	 */
	@RequiresRoles("admin")
	@RequestMapping(value = "/assign/slotCard", method = RequestMethod.POST)
	public void delete(HttpServletResponse response, int ruleId, String ids) {
		byte result = 0;
		String[] groupIds = ids.split(",");
		int length = groupIds.length;
		Map<String, Object> params = new HashMap<String, Object>(2);
		params.put("ruleId", ruleId);
		params.put("groupIds", groupIds);
		int deleteResult = ruleService.deleteAssignGroup(params);
		if (deleteResult == length) {
			int updateResult = ruleService.updateGroupUnAssigned(groupIds);
			if (updateResult == length) {
				result = 1;
			}
		}
		ServletUtil.responseOut("utf-8", String.valueOf(result), response);
	}
	
	/**
	 * 刷卡
	 * @param response
	 * @param rdId
	 * @param slotTime
	 * @throws ParseException 
	 */
	@RequiresRoles("admin")
	@RequestMapping(value = "/slotCard", method = RequestMethod.POST)
	public void slotCard(HttpServletResponse response, String rdId, String slotTime) throws ParseException {
		Date datetime = TimeUtils.stringToDate(slotTime, "yyyy-MM-dd HH:mm:ss");
		CardRuleDetail rule = ruleService.slotCard(rdId, datetime);
		if (rule == null) {
			ServletUtil.responseOut("utf-8", "0", response);
		} else {
			ServletUtil.responseOut("utf-8", "1", response);
		}
	}
	
}
