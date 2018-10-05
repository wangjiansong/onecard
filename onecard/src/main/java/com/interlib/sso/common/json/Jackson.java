package com.interlib.sso.common.json;

import java.io.IOException;
import java.io.StringWriter;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

public class Jackson {

	/**
	 * 转换为Json字符串
	 * @param obj
	 * @return
	 */
	public static String getBaseJsonData(Object obj){
		StringWriter writer = new StringWriter();
		if(obj != null){
			ObjectMapper mapper = JacksonMapper.getInstance();
			try {
				mapper.writeValue(writer, obj);
			} catch (JsonGenerationException e) {
				e.printStackTrace();
			} catch (JsonMappingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return writer.toString();
	}
	
}
