package com.interlib.sso.common;


import java.io.InputStream;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.GetMethod;
import org.castor.core.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.interlib.sso.domain.Reader;
import com.interlib.sso.service.ReaderService;


@Component
public class WebserviceAuthUtils {

	private static ReaderService readerService;
	
	
	@Autowired
	public void setReaderService(ReaderService readerService){
		WebserviceAuthUtils.readerService = readerService;
	}
	
	/**
	 * 判断读者是否验证通过
	 * @param rdid
	 * @param password 加密后的密码
	 * @return
	 */
	public static void loginCheck(String rdid,String password) throws RuntimeException{
		
		Reader reader = new Reader();
		reader.setRdId(rdid);
		reader.setRdPasswd(password);
		//SZIP=58.60.2.15
		//SZwebservice=http\://58.60.2.15:8881/serviceBusiness/readerManage?wsdl
		SysConfiguration sysConfig = new SysConfiguration();
		String szIp = sysConfig.getProps().getProperty("SZIP");	
		String szWebservice = sysConfig.getProps().getProperty("SZwebservice");
		
		String url = szWebservice + "/getreaderxmlByCardno?arg0=" + rdid;
		
		reader = getSZReader(url, szIp);
		
		if(reader == null) {
			throw new RuntimeException("深图数据库不存在有效记录");
		}
		if(reader.getRdSort1().equals("有效")) {
			
			reader = readerService.readerLogin(reader, true);
			if (reader != null) {
			} else {
				throw new RuntimeException("一卡通平台无有效记录，请先开卡");
			}
			
		}
		
	}
	
	/**
	 * 20140218 更换深圳读者信息接口
	 * 这个wsdl需要basic认证
	 * http://58.60.2.154/WSBusiness/readerManage?wsdl
	 * @param url 深圳宝安接口地址
	 * @param szinterfaceIp 深圳宝安接口的ip
	 * @return
	 */
	public static Reader getSZReader(String url,String szinterfaceIp){
		Reader reader=null;
		try{
			//"0440050000284" demo
			HttpClient httpClient=new HttpClient();
			httpClient.getState().setCredentials(new AuthScope(szinterfaceIp,/*"58.60.2.154"*/ //不写死在xml有对应配置
							80, AuthScope.ANY_REALM),
					new UsernamePasswordCredentials("balib", "balib"));//basic密码验证
			//String url = "http://58.60.2.154/WSBusiness/readerManage"+"/getreaderxmlByCardno?arg0=0440050000240";
			GetMethod get = new GetMethod(url);
			get.setDoAuthentication(true);
			String res = getPageContent(httpClient, get, "utf-8");
			
			if(res!=null&&!"".equals(res.trim())){
				String ns = getFromweb(res,"errmsg");
				if(ns==null){
					String cardno = getFromweb(res,"Cardno");
					if(cardno!=null && !"".equals(cardno)){
						reader=new Reader();
						reader.setRdId( getFromweb(res,"Cardno"));
						reader.setRdPasswd(getFromweb(res, "password"));
						reader.setRdName( getFromweb(res,"Name"));
						reader.setRdAddress( getFromweb(res,"Address"));
						reader.setRdCertify( getFromweb(res,"IDno"));//读者的 身份证
						reader.setRdBornDate(TimeUtils.stringToDate(getFromweb(res,"Birth"), "yyyyMMdd"));//时间需要转换
						reader.setRdBornDateStr(TimeUtils.dateToString(reader.getRdBornDate(), "yyyy-MM-dd"));
						reader.setRdType( getFromweb(res,"CardTypeID"));//读者类型
						reader.setRdPhone( getFromweb(res,"Mobile"));//手机号码
						reader.setRdLib( getFromweb(res,"Library"));//所属的分馆
						reader.setRdStartDate(TimeUtils.stringToDate(getFromweb(res,"CardBegdate"), "yyyyMMdd"));
						reader.setRdStartDateStr(TimeUtils.dateToString(reader.getRdStartDate(), "yyyy-MM-dd"));
			            reader.setRdEndDate(TimeUtils.stringToDate(getFromweb(res,"CardEnddate"), "yyyyMMdd"));
			            reader.setRdEndDateStr(TimeUtils.dateToString(reader.getRdEndDate(), "yyyy-MM-dd"));
			            reader.setRdInTime(TimeUtils.stringToDate(getFromweb(res,"Regdate"), "yyyyMMdd"));
			            reader.setRdInTimeStr(TimeUtils.dateToString(reader.getRdInTime(), "yyyy-MM-dd"));
			            String status = getFromweb(res, "Status");
			            if(status.equals("有效")) {
			            	reader.setRdCFState((byte)1);
			            } else if(status.equals("暂停")) {
			            	reader.setRdCFState((byte)4);
			            	
			            } else if(status.equals("注销")) {
			            	reader.setRdCFState((byte)5);
			            	
			            } else if(status.equals("验证")) {
			            	reader.setRdCFState((byte)2);
			            	
			            } else if(status.equals("挂失")) {
			            	reader.setRdCFState((byte)3);
			            	
			            }
						int sex = 1;
						if("女".equals( getFromweb(res,"Gender"))){
							sex = 0;
						}
						reader.setRdSex((byte)sex);
						
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return reader;
	}
	/**
	 * 截取对应的字符串信息
	 * @param res
	 * @param fieldName
	 * @return
	 */
	public static String getFromweb(String res,String fieldName){
		String bb = StringUtil.replaceAll(res, "&lt;", "<");
		bb = StringUtil.replaceAll(bb, "&gt;", ">");
		System.out.println(bb);
		String lt="&lt;"+fieldName+"&gt;";
		int start=res.indexOf(lt);
		if(start<0){
			return null;
		}
		int end=res.indexOf("&lt;/"+fieldName+"&gt;");
		String result=res.substring(start+lt.length(),end);
		return result;
	}
	
	public static String getPageContent(HttpClient client, GetMethod method, 
			String defaultCharset) {
		if(method == null) return null;
		String content = null;
		try {
			@SuppressWarnings("unused")
			int status = client.executeMethod(method);
			String charset = method.getResponseCharSet();
			if(defaultCharset != null) {
				charset = defaultCharset;
			}
			InputStream is = method.getResponseBodyAsStream();
			if(charset != null) {
				content = org.apache.commons.io.IOUtils.toString(is, charset);
			} else {
				content = org.apache.commons.io.IOUtils.toString(is);
			}
			is.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			method.abort();
			method.releaseConnection();
		}
		return content;
	}
	
	public static void main(String[] args) {
		WebserviceAuthUtils.getSZReader("http://58.60.2.154/WSBusiness/readerManage/getreaderxmlByIdno?arg0=440306198311110011", "58.60.2.154");
	}
}
