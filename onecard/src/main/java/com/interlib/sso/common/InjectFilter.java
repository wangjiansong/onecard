package com.interlib.sso.common;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Enumeration;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.Filter;
import javax.servlet.http.HttpServlet;
/**
 *  通过过滤器过滤SQL注入特殊字符 
 * */
public  class InjectFilter extends HttpServlet implements Filter {
    /**
     *
     */
    private static final long serialVersionUID = 5286703103846683570L;
    private String failPage = "/info.jsp";//发生注入时，跳转页面
 
    public void doFilter(ServletRequest request,ServletResponse response,
            FilterChain filterchain)throws IOException, ServletException {
        //判断是否有注入攻击字符
        HttpServletRequest req = (HttpServletRequest) request;
        String inj = injectInput(req);
        if (!inj.equals("")) {
             request.getRequestDispatcher(failPage).forward(request, response);
             return;
        } else {
            // 传递控制到下一个过滤器
            filterchain.doFilter(request, response);
        }
    }
     
    /**
     * 判断request中是否含有注入攻击字符
     * @param request
     * @return
     */
    public String injectInput(ServletRequest request) {
         
        Enumeration e = request.getParameterNames();
        String attributeName;
        String attributeValues[];
        String inj = "";
        String injdb = "";
        while (e.hasMoreElements()) {
            attributeName = (String)e.nextElement();
            //不对密码信息进行过滤，一般密码中可以包含特殊字符
            if(attributeName.equals("userPassword")||attributeName.equals("confirmPassword")||attributeName.equals("PASSWORD")
                    ||attributeName.equals("password")||attributeName.equals("PASSWORD2")||attributeName.equals("valiPassword")){
                continue;
            }
             
            attributeValues = request.getParameterValues(attributeName);
            for (int i = 0; i < attributeValues.length; i++) {
                 
    if(attributeValues[i]==null||attributeValues[i].equals(""))
                    continue;
                inj = injectChar(attributeValues[i]);
                 
                if (!inj.equals(""))
                {
                    return inj;
                }       
            }
        }  
        return inj;
    }
     
    /**
     * 判断字符串中是否含有注入攻击字符
     * @param str
     * @return
     */
    public String injectChar(String str) {
         
       String inj_str = "\" ) \' * % < > &";
       String inj_stra[] = inj_str.split(" ");
         
       for (int i = 0 ; i < inj_stra.length ; i++ )
       {
           if (str.indexOf(inj_stra[i])>=0)
           {
               return inj_stra[i];
           }
       }
       return "";
    }
         
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // TODO Auto-generated method stub
           // System.out.println("----注入过滤器初始化----"); 
    } 
     
}