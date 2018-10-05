package com.interlib.sso.common;

public class Cautions {

	public static final String READER_LOGIN_ERROR = "用户名或密码不正确";
	public static final String NO_LOGIN = "请先登录";
	public static final String STATUS_ERROR = "账户非正常使用状态";
	public static final String INPUT_ERROR = "输入有误";
	public static final String INVALID_VISIT = "无效访问";
	public static final String INVALID_LIBCODE = "无效馆代码";
	public static final String ERROR_ENC = "无效验证";
	public static final String ERROR_IP = "ip地址不在授权范围";
	public static final String AUTH_OUTDATE = "API授权过期";
	public static final String SAVESUCCESS_BUTSYNCFAIL = "中心保存成功但同步失败";
	public static final String SYNCFAIL = "同步失败";
	public static final String EMPTY_URL = "找不到接口地址";
	public static final String CANNOT_REPAIR = "只有挂失、注销状态的读者才能补办读者证";
	public static final String CANNOT_DEFER = "注销的读者证不能延期";
	public static final String CANNOT_CHANGE_RDTYPE = "您不能在这里修改读者证类型！要修改读者类型请使用修改功能";
	public static final String READER_EXIT = "读者证号已存在";
	public static final String CANNOT_DELETE_ADMIN = "系统内置管理员，无法删除";
	public static final String CANNOT_DELETE_CURRENUSER = "您正使用该用户登录，无法删除";
	public static final String DIFFERENT_STATUS = "该用户状态在一卡通中心和深图数据状态不一致";
	public static final String LOCALREADER_NOTEXIT = "一卡通无此用户记录";
	public static final String LOCALANDWEBREADER_NOTEXIT = "一卡通和深图均无此用户记录";
	public static final String READERCARDID_EXSITS = "该卡号已有绑定，不能二次绑定";
	public static final String LOANEDBOOKS = "用户当前有在借书未还，请先还回";
	public static final String FEE_PAY = "用户有未交付财经记录，清先交付";
	
}
