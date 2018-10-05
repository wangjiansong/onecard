/*
 * ValidSystem 1.0.6 2005-12-1
 * Copyright 2002 Tuchang, Inc. All rights reserved.
 * 创建日期 2005-12-1
 *
 */
package com.interlib.sso.license;

import java.util.Date;

import org.apache.log4j.Logger;


/**
 * 用于自助借还机授权的限定
 * @author toofu
 * @reviewer 	toofu
 * @version     1.0, 2003-9-12
 * @env		    JDK1.4.1	
 * @modified	toofu,	2005-12-1
 * 限定参数# 1：mac地址 2：客户端数量 3：使用分馆 4：起始日期 5：终止日期 免费使用日期默认为60天 
 * #授权客户端的MAC，试用版不判断
 *  0：LICENSE_TYPE=0 1 2   #0表示默认license 1表示试用版 2 表示正式版
 *	1：SIPII_USER_HOST_MAC=00-11-25-2D-31-A2
 *  2：CLIENT_USER_COUNT=2
 *  3：USER_TITLE_NAME=东莞市图书馆
 *  4：STARTDATE=20080511
 *  5：ENDDATE=20090511
 *  
 * 第一步：系统运行时，读取的license可能是默认license，使用期限为6个月。
 * 第二步：试用版的license系统读取后，
 *       自动更改license类型，且更改起始时间，修改为试用版licesen，默认生成起始日期
 * 第三步：根据试用版的授权，换取正式试用版的授权。license号要给出。
 *
 * 需要的几大方法为 1：读取授权 翻译读取的授权
 * 				 2：设置授权 设置后加密
 *  
 */
public class ValidSystem {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(ValidSystem.class);
	
	StringBuffer errorMsgToReader=new StringBuffer("本系统权限受限，请联系管理员！");//读者看到的无授权信息
	StringBuffer errorMsgToAdmin=new StringBuffer();//管理员看到的无授权信息
	static LicenseInfo licenseinfo=null;//授权信息，本机器信息和授权信息即可比较出是否能使用的情况了
	public ValidSystem(){
		//String config=RSAUtil.loadLicenseConfig();//去掉本次多余的信息，因为加载的时候，有些机器速度很慢
		licenseinfo=LicenseInfo.getInstance();
	}

	public boolean isCanUse(){
		System.out.println(licenseinfo);
    	if(!licenseinfo.NO_LICENSE_MSG.equals("")){//没有授权
    		errorMsgToAdmin.append(licenseinfo.NO_LICENSE_MSG+"\n");
    		logger.error("main(String[]) - "+licenseinfo.NO_LICENSE_MSG); //$NON-NLS-1$
    		return false;
    	}
    	if(licenseinfo.LICENSE_TYPE.equals("1")){//测试用版本不用限定mac
    		errorMsgToAdmin.append("您的版本是试用版本，请联系广州图创计算机软件开发有限公司获取正式授权！您的授权编号为["+DESUtil.myDESEncode(FindMacAddr.getMacAddress())+"]，请获取正式授权时提供此编号！\n");
    		logger.info("main(String[]) - 您的版本是试用版本，请联系广州图创计算机软件开发有限公司获取正式授权！您的授权编号为["+DESUtil.myDESEncode(FindMacAddr.getMacAddress())+"]，请获取正式授权时提供此编号！"); //$NON-NLS-1$
    	}else{
    		if(!licenseinfo.LICENSE_TYPE.equals("2")){//判断授权类型是否正确
    			errorMsgToAdmin.append("验证类型不正确，不允许进行使用!\n");
        		logger.error("main(String[]) - 验证类型不正确，不允许进行使用!"); //$NON-NLS-1$
        		return false;
        	}
    		boolean isMACOK=false;
        	if(licenseinfo.INTERLIBSSO_USER_HOST_MAC.equals(LocalInfo.getLocalMac())){
        		isMACOK=true;
        	}else if(DESUtil.myDESDecode(licenseinfo.INTERLIBSSO_USER_HOST_MAC).equals(LocalInfo.getLocalMac())){//判断IP地址是否符合)
        		isMACOK=true;
        	}
        	if(!isMACOK){
        		errorMsgToAdmin.append("非授权机器，不允许进行使用!\n");
        		logger.error("main(String[]) - 非授权机器，不允许进行使用!"); //$NON-NLS-1$
    			return false;
        	}
    	}
    	if((!licenseinfo.CLIENT_USER_COUNT.equals(""))&&(Integer.parseInt(licenseinfo.CLIENT_USER_COUNT)<1)){//没允许并发数
    		errorMsgToAdmin.append("无授权客户端，不允许进行使用!\n");
    		logger.error("main(String[]) - 无授权客户端，不允许进行使用！"); //$NON-NLS-1$
    		return false;
    	}//时间是否过期或者是否没到期
    	if(!isDateRight()){
    		errorMsgToAdmin.append("您已经超出授权期限，不能使用，请联系广州图创计算机软件开发有限公司获取授权!\n");
    		logger.error("main(String[]) - 您已经超出授权期限，不能使用，请联系广州图创计算机软件开发有限公司获取授权！"); //$NON-NLS-1$
    		return false;
    	}
    	logger.error("main(String[]) - "+licenseinfo.USER_TITLE_NAME+"  欢迎"+"使用本系统！\n"); //$NON-NLS-1$
    	return true;//是否能启动
    }
    /**
     * 是否超出了最大允许的并发数
     * @param num
     * @return
     */
    public boolean isOverMaxConunt(int num){
    	if((!licenseinfo.CLIENT_USER_COUNT.equals(""))&&(num>=Integer.parseInt(licenseinfo.CLIENT_USER_COUNT))){
    		//超出了最大限制
    		return true;
    	}
    	if(licenseinfo.CLIENT_USER_COUNT.equals(""))return true;//超出了最大限制，这里是限制为空，相当于没进行限制。
    	return false;
    }
    /**
     * 判断是否超出了时间授权
     * @return
     */
    private boolean isDateRight(){
    	if(licenseinfo.STARTDATE==null||licenseinfo.STARTDATE.equals("")){//没设置起始日期
    		return false;
    	}if(licenseinfo.ENDDATE==null||licenseinfo.ENDDATE.equals("")){//没设置终止日期
    		return false;
    	}
    	Date startDate=TimeUtil.getDate(licenseinfo.STARTDATE,"yyyyMMdd");
    	Date endDate=TimeUtil.getDate(licenseinfo.ENDDATE,"yyyyMMdd");
    	Date currDate=TimeUtil.trimDate(new Date());//取今天的0时
    	if(currDate.compareTo(startDate)>=0&&currDate.compareTo(endDate)<=0){
    		return true;
    	}
    	return false;
    }
    public static void main(String[] args) {
    	ValidSystem vs=new ValidSystem();
    	System.out.println("xx="+vs.isCanUse());
    }
	public StringBuffer getErrorMsgToAdmin() {
		return errorMsgToAdmin;
	}
	public void setErrorMsgToAdmin(StringBuffer errorMsgToAdmin) {
		this.errorMsgToAdmin = errorMsgToAdmin;
	}

	public static LicenseInfo getLicenseinfo() {
		return licenseinfo;
	}

	public static void setLicenseinfo(LicenseInfo licenseinfo) {
		ValidSystem.licenseinfo = licenseinfo;
	}
}
