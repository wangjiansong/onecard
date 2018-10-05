package com.interlib.sso.handle;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author home
 */
public class MyExceptionHandler implements HandlerExceptionResolver {

	public ModelAndView resolveException(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex) {
		Map<String, Object> model = new HashMap<String, Object>();
		StackTraceElement[] trace = ex.getStackTrace();
		StringBuffer sb = new StringBuffer();
		sb.append(ex.toString());
		for (int i = 0; i < trace.length; i++) {
			sb.append("\tat " + trace[i]);
		}
		model.put("ex", sb.toString());
		// 根据不同错误转向不同页面
		ModelAndView mv = new ModelAndView();
		mv.addObject("model", model);
		if (ex instanceof org.apache.shiro.authz.AuthorizationException) {
			mv.setViewName("page_not_found");
			return mv;
		} else if (ex instanceof org.apache.shiro.authz.UnauthorizedException) {
			mv.setViewName("page_not_found");
			return mv;
		} else {
			mv.setViewName("page_not_found");
			return mv;
		}
	}
}
