package com.interlib.sso.common;

/**
 * @author shun
 * 常量类，定义了系统中所用到的常量
 * TODO 这里是否考虑有更好的方式,方便在jsp上使用这些常量
 */
public class Constants {
	public static final String XML_HEADER = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
	public static final String CONTENTTYPE_XML = "text/xml; charset=UTF-8";
	public static final String CONTENTTYPE_HTML = "text/html; charset=UTF-8";
	
	public static final String SESSION_ID_FOR_READER = "reader";
	
	//分馆名称MAP，key为libcode，value为simplename
	public static final String LIBCODEMAP = "libcodeMap";
	
	//馆藏地点MAP，key为localcode，value为name
	public static final String LOCALMAP = "localMap";
	
	//文献类型MAP，key为typeno，value为typevalue
	public static final String BOOKTYPEMAP = "booktypeMap";
	
	//财经类型MAP，key为typeno，value为describe
	public static final String FININFOMAP = "finInfoMap";
	
	//流通类型Map, key为cirtype, value为PBCtype
	public static final String PBCTYPE_MAP = "pbctypeMap";
	
	//馆藏状态Map, key为statetype, value为HoldState
	public static final String HOLDSTATE_MAP = "holdstateMap";
	
	//读者类型Map, key为rdtype, value为describe
	public static final String READERTYPE_MAP = "rdtypeMap";
	
	//财经类型Map, key为logtype,value为typename
	public static final String CIRTYPE_MAP = "cirtypeMap";
	
	//rss描述信息在Model中的键名
	public static final String RSS_FEED_METADATA = "RSS_FEED_METADATA";
	//rss条目信息在Model中的键名
	public static final String RSS_FEED_ITEMS = "RSS_FEED_ITEMS";
	
	public static final String DYNAMIC_BOOKTYPE_ICON_PATH = 
			"/media/dynamic/booktype";
	
	public static final int DEFAULT_PAGE_SIZE = 10;//默认的分页每页显示的条数
	
	//在model里存放的用于判断是否当前选中菜单的键
	public static final String ACTIVE_MENU_KEY = "ACTIVE_MENU";
	
	public static final String GLOBAL_SYSTEMOPTIONS_MAP = 
			"GLOBAL_SYSTEMOPTIONS_MAP";
	
	/**
	 * 默认缓存的缓存名称
	 */
	public static final String DEFAULT_CACHE_NAME = "DEFAULT_CACHE";
	
	/**
	 * 缓存名称，5分钟过期
	 */
	public static final String DEFAULT_CACHE_5_MIN_NAME 
		= "DEFAULT_CACHE_5_MIN";
	
	
	public static final String OPERATOR_IN_SESSION_KEY = "OPERATOR_IN_SESSION";
	
	public static final String HAND_KEY = "TCSOFT_INTERLIB";
	
	public static final String DES_STATIC_KEY = "64074f968502295ca41b7db452c7c639";
	
}
