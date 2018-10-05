package com.interlib.sso.common;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.util.PropertyFilter;
/**
 * @author toofu
 * 对json封装处理的类
 */
public class JsonUtil{
	/**
	 * 获取log4j的log句柄
	 */
	private static final Log log = LogFactory.getLog(JsonUtil.class);
	/**
	 * 解析出从请求获求传来的json内容。这里为了便于修改期间，简单封装此方法获取。
	 * @param req
	 * @return
	 */
	public static String getReqJson(HttpServletRequest req){
		String jsonStr=req.getParameter("json");
		log.debug("前台传递过来的json参数="+jsonStr);
		if(jsonStr==null)jsonStr="";
		return jsonStr;
	}
	//add by yao 处理对象日期属性
	/**
	  * 把数据对象转换成json字符串
	  * DTO对象形如：{"id" : idValue, "name" : nameValue, ...}
	  * 数组对象形如：[{}, {}, {}, ...]
	  * map对象形如：{key1 : {"id" : idValue, "name" : nameValue, ...}, key2 : {}, ...}
	  * @param object
	  * @return 转换后的 json字符串
	  */
	public static String getJSONString(Object object) throws Exception {
		String jsonString = null;
		// 日期值处理器
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.registerJsonValueProcessor(java.util.Date.class, new JsonDateValueProcessor());
		if (object != null) {
			if (object instanceof Collection || object instanceof Object[]) {
				jsonString = JSONArray.fromObject(object, jsonConfig).toString();
			} else {
				jsonString = JSONObject.fromObject(object, jsonConfig).toString();
			}
		}
		return jsonString == null ? "{}" : jsonString;
	}
	 
  //end add

	/**
	 * 简单的类转换成字符串，这里是没设置配置的方式
	 * @param obj
	 * @return
	 */
	public static String beanTOjson(Object obj){
		JSONObject jsonobj=JSONObject.fromObject(obj);
		return jsonobj.toString();
	}
	/**
	 * 简单的类转换成字符串，使用jsonconfig进行配置
	 * @param obj
	 * @return
	 */
	public static String beanTOjson(Object obj,JsonConfig jsoncfg){
		JSONObject jsonobj=JSONObject.fromObject(obj,jsoncfg);
		return jsonobj.toString();
	}
	/**
	 * 类转换成字符串 可以配置不需要转换的类和属性名称
	 * @param obj 需要转换成json的对象
	 * @param delClassNames 对象中不需要转换成json的类
	 * @param delPropertiesNames 对象中不需要转换成json的属性名称
	 * @return
	 */
	public static String beanTOjson(Object obj,Object[] delClassNames,String[] delPropertiesNames){
		return beanTOjson(obj,getJsonConfig(delClassNames,delPropertiesNames));
	}
	/**
	 * 数组转换成json字符串
	 * @param obj数组
	 * @return
	 */
	public static String arrTOjson(Object[] obj){
		JSONArray jsonobj=JSONArray.fromObject(obj);
		return jsonobj.toString();
	}
	/**
	 * 简单的json转换成类的方式，这里自动判断类，不支持配置
	 * @param jsonStr
	 * @return
	 */
	public static Object jsonTObean(String jsonStr){
		JSONObject j = JSONObject.fromObject(jsonStr); 
		return JSONObject.toBean(j);
	}
	/**
	 * 简单的json转换成类的配置，这里可以设置根类，不支持配置、不支持子类配置
	 * @param jsonStr
	 * @param classinstance eg. JsonUtil.class  
	 * @return
	 */
	public static Object jsonTObean(String jsonStr,Class classinstance){
		JSONObject j = JSONObject.fromObject(jsonStr); 
		return JSONObject.toBean(j,classinstance);
	}
	/**
	 * 简单的json转换成类的配置，这里可以设置根类，支持配置、支持子类配置
	 * @param jsonStr 要转换成bean的json字符串
	 * @param classinstance 该json字符串要转换成的类 如:Useraccount.class
	 * @param classMapKey   字符串节点到类的映射配置，如果为null或者长度为0 则表示没配置，这里是节点
	 * @param classMapClass 节点所对应的类 如："UUserRoles", URoles.class
	 * @return
	 */
	public static Object jsonTObean(String jsonStr,Class classinstance,String[] classMapKey,Object[] classMapClass){
		JSONObject j = JSONObject.fromObject(jsonStr);
		if(classMapKey!=null&&classMapKey.length>0){
			Map classMap = new HashMap();
			for(int i=0;i<classMapKey.length;i++){
				classMap.put(classMapKey[i], classMapClass[i]);
			}
			return JSONObject.toBean(j,classinstance,classMap);
    	}
		return JSONObject.toBean(j,classinstance);
	}
	/**
	 * 把结果组装成json的方法
	 * @param isOk true or false 返回的执行结果
	 * @param msg 返回的提示信息
	 * @return
	 */
	public static String makeOneRsJson(boolean isOk,Object msg,Object[] delClassNames,String[] delPropertiesNames){
		Map m=new HashMap();
		m.put("success", isOk);
		m.put("failure", !isOk);
		m.put("errors",warpMsg(msg));
		return JsonUtil.beanTOjson(m,getJsonConfig(delClassNames,delPropertiesNames));
	}
	/**
	 * 把结果组装成json的方法 
	 * @param isOk  true or false 返回的执行结果
	 * @param msg 需要返回的提示信息
	 * @return
	 */
	public static String makeOneRsJson(boolean isOk,Object msg){
		Map m=new HashMap();
		m.put("success", isOk);
		m.put("failure", !isOk);
		m.put("errors",warpMsg(msg));
		//String jsonStr="{success:"+isOk+",failure:"+!isOk+",errors:"+warpMsg(msg)+"}";
		return JsonUtil.beanTOjson(m);
	}
	/**
	 * 返回多个msg结果
	 * @param isOk
	 * @param msg
	 * @return
	 */
	public static String makeMoreRsJson(boolean isOk,Object[] msg){
		Map m=new HashMap();
		m.put("success", isOk);
		m.put("failure", !isOk);
		
		//转化传递进来的结果数组
		Object[] msgArr=new Object[msg.length];
		for(int i=0;i<msg.length;i++){
			msgArr[i]=warpMsg(msg[i]);
		}
		//输出转换好的结果数组
		m.put("errors",msgArr);
		return JsonUtil.beanTOjson(m);
	}
	/**
	 * 组装单条返回的msg结果
	 * @param msg
	 * @return
	 */
	public static Map warpMsg(Object msg){
		Map map=new HashMap();
		map.put("msg",msg);
		//还可以添加其他节点
		return map;
	}
	/**
	 * 针对bean转换成json所需要的JsonConfig配置类的获取
	 * @param delClassNames 所不需要转换为json的类
	 * @param delPropertiesNames  所不需要转换为json的属性名称
	 * @return
	 */
	private static JsonConfig getJsonConfig(final Object[] delClassNames,final String[] delPropertiesNames){
		JsonConfig cfg = new JsonConfig();
		cfg.registerJsonValueProcessor(java.util.Date.class, new JsonDateValueProcessor());
	    cfg.setJsonPropertyFilter(new PropertyFilter(){
	    	 public boolean apply(Object source, String name, Object value) {
    			 if(delClassNames!=null){
		        	 for(int m=0;m<delClassNames.length;m++){//取消对指定类的json
		        		 if(source.getClass().getName().equals(((Class)delClassNames[m]).getName())){
		        			 return true;
		        		 }
		        	 }
	        	 }
	        	 if(delPropertiesNames!=null){
	        		 String allConfig="",beforeStr="",afterStr="";
	        		 for(int m=0;m<delPropertiesNames.length;m++){//取消对指定属性的json
	        			 allConfig=delPropertiesNames[m];
	        			 if(allConfig.lastIndexOf(".")>0){
	        				 beforeStr=allConfig.substring(0, allConfig.indexOf("."));
	        				 afterStr=allConfig.substring(allConfig.indexOf(".")+1);
	        				 if(source.getClass().getName().equals("com.interlibsso.hibernate.object."+beforeStr)&&name.equals(afterStr)){
			        			 return true;
			        		 }
	        			 }else if(name.equals(delPropertiesNames[m])){
		        			 return true;
		        		 }
		        	 }
	        	 }
	        	return false;
	        }
	         public boolean juge(String[] s,String s2){ 
	                boolean b = false; 
	                for(String sl : s){ 
	                    if(s2.equals(sl)){ 
	                        b=true; 
	                    } 
	                } 
	                return b; 
	            }
	     });
	    return cfg;
	}
	
	public static void main(String[] args) {
		System.out.println("dd"+JsonUtil.class.getName());
	}
}
