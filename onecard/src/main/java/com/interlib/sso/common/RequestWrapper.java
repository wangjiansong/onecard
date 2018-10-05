package com.interlib.sso.common;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
 
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
 
/*
 * 增加XSS过滤
 * 自定义的RequestWrapper中，对getParameter和getParameterValues进行重写，从而达到对各个用户输入的form参数的值进行过滤
 */
public class RequestWrapper extends HttpServletRequestWrapper {
    HttpServletRequest orgRequest = null; 
        
    public RequestWrapper(HttpServletRequest request) { 
        super(request); 
        orgRequest = request; 
    } 
    /**
     * 获取最原始的request
     * 
     * @return
     */
    public HttpServletRequest getOrgRequest() { 
        return orgRequest; 
    } 
    
    /**
     * 获取最原始的request的静态方法
     * 
     * @return
     */
    public static HttpServletRequest getOrgRequest(HttpServletRequest req) { 
        if (req instanceof RequestWrapper) { 
            return ((RequestWrapper) req).getOrgRequest(); 
        } 
    
        return req; 
    }
 
   /**
    * 覆盖getParameterMap 方法，对sql、html、script注入进行过滤
    */
   public Map<String,String> getParameterMap() {
       HashMap<String,String> paramMap = (HashMap<String,String>) super.getParameterMap();
       paramMap = (HashMap<String,String>) paramMap.clone();
 
       for (Iterator iterator = paramMap.entrySet().iterator(); iterator.hasNext(); ) {
           Map.Entry<String,String[]> entry = (Map.Entry<String,String[]>) iterator.next();
           String [] values = entry.getValue();
           for (int i = 0; i < values.length; i++) {
               if(values[i] instanceof String){
                   values[i] = XssClean.xssClean(values[i]);
               }
           }
           entry.setValue(values);
       }
       return paramMap;
   }
   //////////////////////////////////////////////////////////////////////////////////////////
   ///////////////////////////////////////////////////////////////////////////////////////////
   //滤掉form data中的恶意脚本
   public String[] getParameterValues(String paramString){ 
     String[] arrayOfString1 = super.getParameterValues(paramString); 
     if (arrayOfString1 == null) 
       return null; 
     int i = arrayOfString1.length; 
     String[] arrayOfString2 = new String[i]; 
     for (int j = 0; j < i; j++) 
       arrayOfString2[j] =XssClean.xssClean_New(arrayOfString1[j]); 
     return arrayOfString2; 
   } 
    
 
   public String getParameter(String paramString){ 
     String str = super.getParameter(paramString); 
     if (str == null) 
       return null; 
     return XssClean.xssClean(str); 
   } 
//////////////////////////////////////////////////
   //在输入流中检测滤掉来自其他网站的URL中的恶意脚本
   public String getHeader(String paramString) { 
     String str = super.getHeader(paramString); 
     if (str == null) 
       return null; 
     return XssClean.xssClean(str); 
   }
   ////////////////////////////////////////////////////////////////////////////////////////////
   ///////////////////////////////////////////////////////////////////////////////////////////////
    
   public String getQueryString() {
        String value = super.getQueryString();
        if (value != null) {
            value = XssClean.xssClean(value);
        }
        return value;
    }
 }
