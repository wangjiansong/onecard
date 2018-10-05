package com.interlib.sso.controller.system;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.interlib.sso.common.JsonUtil;
import com.interlib.sso.common.ResponseUtil;
import com.interlib.sso.domain.ReaderCardInfo;
import com.interlib.sso.service.ReaderCardInfoService;
import com.interlib.sso.service.ReaderService;

/**
 * 用来和读卡写卡程序数据交互
 * @author huang
 *
 */
@Controller
@RequestMapping("admin/sys/card")
public class CardController {
	
	@Autowired
	public ReaderService readerService;
	
	@Autowired
	public ReaderCardInfoService readerCardInfoService;
	
	@RequiresRoles("admin")
	@RequestMapping(value="/bindCardAndRdid") 
	public void readCard(HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(required=true, value="rdId") String rdId, 
			@RequestParam(required=true, value="cardId") String cardId) {
		Map<String,Object> map = new HashMap<String,Object>();
		try {
			ReaderCardInfo card = null;
			if(!StringUtils.trimToEmpty(cardId).equals("")) {
				card = readerCardInfoService.get(cardId);
				if(card != null) {
					map.put("success", false);
					map.put("message", "卡号已被其他证号绑定");
					String jsonStr = JsonUtil.beanTOjson(map);
					ResponseUtil.sendJson(response, jsonStr);
					return;
				} else {
					card = new ReaderCardInfo();
					card.setCardId(cardId);
					card.setRdId(rdId);
					card.setCardType("0");
					card.setIsUsable(1);
					readerCardInfoService.save(card);
					map.put("success", true);
					map.put("message", "卡号绑定成功");
					String jsonStr = JsonUtil.beanTOjson(map);
					ResponseUtil.sendJson(response, jsonStr);
					return;
				}
			}
		} catch(Exception e) {
			map.put("success", false);
			map.put("message", "绑定出错:" + e.toString());
			String jsonStr = JsonUtil.beanTOjson(map);
			ResponseUtil.sendJson(response, jsonStr);
			return;
		}
	}
	
}
