package com.interlib.sso.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ExceptionController {
	
	@RequestMapping("/page_not_found")
	public ModelAndView pageNotFound() {
		return new ModelAndView("page_not_found");
	}
	
	@RequestMapping("/uncaught_error")
	public ModelAndView uncaughtError() {
		return new ModelAndView("uncaught_error");
	}
	
}
