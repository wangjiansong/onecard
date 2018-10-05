package com.interlib.sso.controller.wechat;


import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.interlib.sso.common.servlet.ServletUtil;

import wxpay.SHA1Util;
import edu.emory.mathcs.backport.java.util.Arrays;

/** 
 * @author home
 */
@Controller
@RequestMapping("/wechat")
public class WechatController {

	//自定义的token
	
	private String TOKEN = "interlib211";
	
	@RequestMapping(value="/callback/api", method=RequestMethod.GET)
	public void WechatCallbackApi (HttpServletRequest request, HttpServletResponse response) 
			throws IOException {
		//微信调用验证本服务器地址的有效性
		//微信加密签名
		String signature = request.getParameter("signature");
		//时间戳
		String timestamp = request.getParameter("timestamp");
		//随机数
		String nonce = request.getParameter("nonce");
		//随机字符串
		String echostr = request.getParameter("echostr");
		
		String[] str = { TOKEN, timestamp, nonce };
		//字典序排序
		Arrays.sort(str);
		
		String bigStr = str[0] + str[1] + str[2];
		//SHA1加密
		String digest = SHA1Util.Sha1(bigStr);
		
		if(digest.equals(signature)) {
			response.getWriter().print(echostr);
		}
	}
}
