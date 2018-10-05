package com.interlib.sso.common;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import com.interlib.sso.common.RequestWrapper;
import javax.servlet.http.HttpServlet;
/*
 * XSS过滤
 */
public class XssFilter extends HttpServlet implements Filter {
     
    /**
     *
     */
    private static final long serialVersionUID = 6846384055249714181L;
 
    /** 
        * 需要排除的页面 
        */   
        private String excludedPages;   
             
        private String[] excludedPageArray;  
           
        @SuppressWarnings("unused") 
        private FilterConfig filterConfig; 
       
        public void destroy() { 
            this.filterConfig = null; 
           // System.out.println("----过滤器销毁----");
        } 
           
        public void doFilter(ServletRequest request, ServletResponse response, 
                FilterChain chain) throws IOException, ServletException { 
            boolean isExcludedPage = false; 
         // 强制类型转换 HttpServletRequest
            HttpServletRequest request2 = (HttpServletRequest) request; 
            String ctx_path = request2.getContextPath(); 
            String request_url = request2.getRequestURI(); 
            String action = request_url.substring(ctx_path.length()); 
             
            HttpServletResponse response2 = (HttpServletResponse)response;
            response2.addHeader("Set-Cookie", "uid=112; Path=/; HttpOnly");
               
                if (isExcludedPage) { 
                chain.doFilter(request, response2); 
            } else {  // 构造RequestWrapper对象处理XSS
                chain.doFilter(new RequestWrapper(request2), response2); 
            } 
               
        } 
           
        /**
         * 自定义过滤规则
         */ 
        public void init(FilterConfig filterConfig) throws ServletException { 
            this.filterConfig = filterConfig; 
            excludedPages = filterConfig.getInitParameter("excludedPages");   
            if (StringUtils.isNotEmpty(excludedPages)) { 
                excludedPageArray = excludedPages.replaceAll("[\\s]", "").split(","); 
                //System.out.println("----过滤器初始化----");
            } 
        } 
}