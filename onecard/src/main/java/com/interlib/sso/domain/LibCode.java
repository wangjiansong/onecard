package com.interlib.sso.domain;

import java.io.Serializable;

import com.interlib.sso.page.PageEntity;

public class LibCode implements Serializable {

	/**
	 * 分馆管理
	 */
	private static final long serialVersionUID = 1093812700288877735L;
	
	private Integer id;//主键

	private String libCode;//分馆代码
	
	private String simpleName;//分馆简称
	
	private String name;//分馆名称
	
	private String address;//分馆所在地址
	
	private byte workMode;//工作模式
	
	private String retSite;//馆际还书指定地点
	
	private String defaultRdPasswd; // 默认读者密码
	
	private String webserviceUrl;//webservice接口地址
	
	private String opacKey;//密钥
	
	private String libraryId;//全局馆ID
	
	private PageEntity page;//分页对象
	
	private String acsIp;//ACS ip 2015-1-15
	
	private String acsPort;//ACS 接口 2015-1-15
	
	
	public LibCode() {
		super();
	}
	
	public LibCode(String libCode, String simpleName) {
		super();
		this.libCode = libCode;
		this.simpleName = simpleName;
	}

	public LibCode(String libCode, String simpleName, String name,
			String address, byte workMode, String retSite,
			String defaultRdPasswd, String webserviceUrl, 
			String opacKey,String acsIp,String acsPort) {
		super();
		this.libCode = libCode;
		this.simpleName = simpleName;
		this.name = name;
		this.address = address;
		this.workMode = workMode;
		this.retSite = retSite;
		this.defaultRdPasswd = defaultRdPasswd;
		this.webserviceUrl = webserviceUrl;
		this.opacKey = opacKey;
		this.acsIp = acsIp;
		this.acsPort = acsPort;
	}

	public LibCode(String libCode, String simpleName, String name,
			String address, byte workMode, String retSite,
			String defaultRdPasswd, String webserviceUrl,
			String acsIp,String acsPort,PageEntity page) {
		super();
		this.libCode = libCode;
		this.simpleName = simpleName;
		this.name = name;
		this.address = address;
		this.workMode = workMode;
		this.retSite = retSite;
		this.defaultRdPasswd = defaultRdPasswd;
		this.webserviceUrl = webserviceUrl;
		this.acsIp = acsIp;
		this.acsPort = acsPort;
		this.page = page;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getLibCode() {
		return libCode;
	}

	public void setLibCode(String libCode) {
		this.libCode = libCode;
	}

	public String getSimpleName() {
		return simpleName;
	}

	public void setSimpleName(String simpleName) {
		this.simpleName = simpleName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public byte getWorkMode() {
		return workMode;
	}

	public void setWorkMode(byte workMode) {
		this.workMode = workMode;
	}

	public String getRetSite() {
		return retSite;
	}

	public void setRetSite(String retSite) {
		this.retSite = retSite;
	}
	
	public String getDefaultRdPasswd() {
		return defaultRdPasswd;
	}

	public void setDefaultRdPasswd(String defaultRdPasswd) {
		this.defaultRdPasswd = defaultRdPasswd;
	}

	public String getWebserviceUrl() {
		return webserviceUrl;
	}

	public void setWebserviceUrl(String webserviceUrl) {
		this.webserviceUrl = webserviceUrl;
	}

	public String getOpacKey() {
		return opacKey;
	}

	public void setOpacKey(String opacKey) {
		this.opacKey = opacKey;
	}

	public String getLibraryId() {
		return libraryId;
	}

	public void setLibraryId(String libraryId) {
		this.libraryId = libraryId;
	}

	public PageEntity getPage() {
		return page;
	}

	public void setPage(PageEntity page) {
		this.page = page;
	}
	
	
	public String getAcsIp() {
		return acsIp;
	}



	public void setAcsIp(String acsIp) {
		this.acsIp = acsIp;
	}



	public String getAcsPort() {
		return acsPort;
	}



	public void setAcsPort(String acsPort) {
		this.acsPort = acsPort;
	}



	//重写实体类的是否是同一个对象的方法 2014-06-19
	@Override
	public boolean equals(Object obj) {
		if(obj!=null){
    		if(this.getClass() == obj.getClass()){  
    			LibCode lib = (LibCode)obj;
    			if(this.getLibCode().equals(lib.getLibCode())){
    				return true;
    			}
    		}
		}
		return false;
	}



	
	
	
	
}
