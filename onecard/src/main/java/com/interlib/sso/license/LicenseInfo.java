package com.interlib.sso.license;

import java.util.Properties;



/**
 *  * 限定参数# 1：mac地址 2：客户端数量 3：使用分馆 4：起始日期 5：终止日期 免费使用日期默认为60天 
 * #授权客户端的MAC，试用版不判断
 *  0：LICENSE_TYPE=0 1 2   #0表示默认license 1表示试用版 2 表示正式版
 *	1：SIPII_USER_HOST_MAC=00-11-25-2D-31-A2
 *  2：CLIENT_USER_COUNT=2
 *  3：USER_TITLE_NAME=东莞市图书馆
 *  4：STARTDATE=20080511
 *  5：ENDDATE=20090511
 *  附加
 *  SERVER_MAC=表示的是获取到的服务器的mac，加密后的mac
 * @author toofu
 *
 */
public class LicenseInfo {
	    /**
	     * 授权类型 
	     */
	    public String LICENSE_TYPE="";
	    
	    /**
	     * 服务器的mac地址
	     * 
	     */
	    public String SERVER_MAC="";
	    /**
	     * 授权的机器网卡地址
	     */
	    public String INTERLIBSSO_USER_HOST_MAC="";
	    /**
	     * 客户端数量
	     */
	    public String CLIENT_USER_COUNT="";
	    /**
	     * 单位名称 如东莞市图书馆
	     */
	    public String USER_TITLE_NAME="";
	    /**
	     * 启用时间
	     */
	    public String STARTDATE="";
	    /**
	     * 终止时间
	     */
	    public String ENDDATE="";
	    /**
	     *  没有license时的提示。
	     */
	    public String NO_LICENSE_MSG="";
	    
	    private static LicenseInfo instance=new LicenseInfo();
	    public static LicenseInfo getInstance(){
	    	return instance;
	    }
	    public static void reload(){
	    	instance=new LicenseInfo();
	    }
	    /**
	     * 初始化构造函数
	     * 通过license.dat读取内容并进行解密
	     */
	    LicenseInfo(){
//	    	this.SERVER_MAC=DESUtil.myDESEncode(FindMac.getMacAddress());
	    	this.SERVER_MAC=DESUtil.myDESEncode(FindMacAddr.getMacAddress());
			String config=RSAUtil.loadLicenseConfig();
	    	if(config==null||config.equals("")){
				this.NO_LICENSE_MSG="您没有授权，请联系广州图创计算机软件开发有限公司获取授权。您的授权编号为["+this.SERVER_MAC+"]，请获取正式授权时提供此编号！";
			}else{
		    	String[] configArr=config.split("\n");//根据回车分割
		    	if(configArr.length==6){
		    		this.LICENSE_TYPE=configArr[0];
		    		this.INTERLIBSSO_USER_HOST_MAC=configArr[1];
		    		this.CLIENT_USER_COUNT=configArr[2];
		    		this.USER_TITLE_NAME=configArr[3];
		    		this.STARTDATE=configArr[4];
		    		this.ENDDATE=configArr[5];
		    	}else{
		    	}
			}
	    }
	    /**
	     * 获取本机器的机器信息属性
	     * @return
	     */
	    public Properties getInfoProperties(){
	            Properties p=new Properties();
	            p.put("LICENSE_TYPE",LICENSE_TYPE);
	            p.put("INTERLIBSSO_USER_HOST_MAC",INTERLIBSSO_USER_HOST_MAC);
	            p.put("CLIENT_USER_COUNT",CLIENT_USER_COUNT);
	            p.put("USER_TITLE_NAME",USER_TITLE_NAME);
	            p.put("STARTDATE",STARTDATE);
	            p.put("ENDDATE",ENDDATE);
	            return p;
	    }
	    /**
	     * 测试Main类
	     * @param args
	     */
	    public static void main(String[] args) {
	    	LicenseInfo lin=LicenseInfo.getInstance();
	    	System.out.println("1"+lin.LICENSE_TYPE);
	    	System.out.println("2="+lin.USER_TITLE_NAME);
	    }
	}
