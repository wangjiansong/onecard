package com.interlib.sso.task;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Controller;

import com.interlib.sso.common.TimeUtils;
import com.interlib.sso.service.SubsidyGrantTaskService;

/**
 * 补助发放定时任务
 * @author Lullaby
 *
 */
@Controller
public class SubsidyGrantTask extends QuartzJobBean {
	
	private static final Logger logger = Logger.getLogger(SubsidyGrantTask.class);
	
	private SubsidyGrantTaskService subsidyGrantTaskService;
	
	public void setSubsidyGrantTaskService(
			SubsidyGrantTaskService subsidyGrantTaskService) {
		this.subsidyGrantTaskService = subsidyGrantTaskService;
	}

	@Override
	protected void executeInternal(JobExecutionContext context)
			throws JobExecutionException {
		Date currentDate = new Date();
		logger.info("[-------------补助发放定时任务开始执行-------------]" + TimeUtils.dateToString(currentDate, "yyyy-MM-dd HH:mm:ss SSS"));
		try {
			List<Map<String, Object>> grants = subsidyGrantTaskService.queryCurrentGrants(TimeUtils.formatDate(currentDate, "yyyy-MM-dd"));
			Map<String, Object> map = null;
			int grantId;
			float grantAmount;
			String grantTitle;
			Map<String, Object> param = null;
			List<Integer> groupIds = new ArrayList<Integer>();
			for (int i = 0, size = grants.size(); i < size; i++) {
				map = grants.get(i);
				grantId = Integer.parseInt(map.get("GRANTID").toString());
				grantAmount = Float.parseFloat(map.get("GRANTAMOUNT").toString());
				grantTitle = map.get("GRANTTITLE").toString();
				groupIds = subsidyGrantTaskService.queryGrantGroups(grantId);
				if (!groupIds.isEmpty()) {
					param = new HashMap<String, Object>(2);
					param.put("grantAmount", grantAmount);
					param.put("groupIds", groupIds);
					subsidyGrantTaskService.doGrant(param);
					logger.info(TimeUtils.dateToString(currentDate, "yyyy年MM月dd日：") + "补助【" + grantTitle + "】发放成功！");
				} else {
					logger.warn(TimeUtils.dateToString(currentDate, "yyyy年MM月dd日：") + "补助【" + grantTitle + "】未指派发放分组！");
				}
			}
			logger.info("[-------------补助发放定时任务执行完毕-------------]" + TimeUtils.dateToString(new Date(), "yyyy-MM-dd HH:mm:ss SSS"));
		} catch (Exception e) {
			logger.error(TimeUtils.dateToString(currentDate, "yyyy年MM月dd日：") + "【补助发放】定时任务执行异常！", e);
		}
	}

}
