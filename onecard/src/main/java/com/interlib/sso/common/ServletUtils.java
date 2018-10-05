package com.interlib.sso.common;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

public class ServletUtils {

	public static void printXML(HttpServletResponse res, String result) {
	    res.setStatus(HttpServletResponse.SC_OK);
		res.setContentType(Constants.CONTENTTYPE_XML);
		PrintWriter out;
		try {
			out = res.getWriter();
			out.print(result);
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void printHTML(HttpServletResponse res, String html) {
		res.setStatus(HttpServletResponse.SC_OK);
		res.setContentType(Constants.CONTENTTYPE_HTML);
		PrintWriter out;
		try {
			out = res.getWriter();
			out.write(html);
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
