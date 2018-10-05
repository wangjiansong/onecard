package com.interlib.sso.domain;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.interlib.sso.page.PageEntity;

public class Reader implements Serializable{
	
	/**
	 * 读者
	 */
	private static final long serialVersionUID = -8764976668238767442L;
	
	private String rdId;//读者证号
	private String rdLoginId;//手机
	private String rdPasswd;//密码
	private String rdName;//姓名
	private String rdCertify;//身份证号
	private Date rdBornDate;//生日
	private byte rdSex;//性别，1为男，0为女
	private byte rdCFState;//证状态，读者证状态，有效=1、验证=2、丢失=3、暂停=4、注销=5
	private String rdType;//读者类型
	private byte rdGlobal;//是否馆际读者
	private String rdLibType;//馆际类型
	private String rdLib;//开户馆
	private Date rdStartDate;//启用时间
	private Date rdEndDate;//终止时间
	private String rdAddress;//地址
	private String rdPostCode;//邮编
	private String rdEmail;//Email地址
	private String rdPhone;//电话
	private String rdUnit;//单位
	private String rdRemark;//备注
	private String rdInterest;//兴趣
	private String rdSpecialty;//特长
	private String rdSort1; //专业，一般情况下对应reader表中的rdsort1
	private String rdSort2;//职业，一般情况下对应reader表中的rdsort2
	private String rdSort3;//职务，一般情况下对应reader表中的rdsort3
	private String rdSort4;//职称，一般情况下对应reader表中的rdsort4
	private String rdSort5;//文化程序，一般情况下对应reader表中的rdsort5
	private byte[] rdPhoto;//头像
	private String rdNation;//民族
	private String rdNative;//籍贯
	private Date rdInTime;//办证时间
	private Integer rdScore;//积分
	private Integer rdTotalLoanCount;//借阅次数
	private Integer totalResNum;
	private Integer totalReNewNum;
	private Integer totalInterlibNum;
	private Integer totalPeccancy;
	private String operator;
	private Date dueTime;
	private String sprule;
	private byte rdCenter;
	private String centerRule;
	private String rdRegisterName;//注册名
	private byte notGetBookNum;
	private String grade;
	private String period;
	private byte eleReadingNoControl;
	private String department;
	private Integer onlineSeconds;
	private Integer sSordScore;
	private String workCardNo;//工作卡号
	private String otherCardNo;
	private byte SMS_SYNC_FLAG;
	private byte receiveMobileNotifies;
	private byte isShowRemark;
	private Integer childNum;
	private String rdCustomRemark;
	private byte isShowCustomRemark;
	
	private String rdLibCode;//开户馆代码
	private String rdStateStr;//读者证状态描述
	private String rdInTimeStr;//办证时间字符串
	private String rdStartDateStr;//启用日期字符串
	private String rdEndDateStr;//终止日期字符串
	private String rdBornDateStr;//出生日期字符串
	
	private Double rdSpDeposit;//专项押金
	private Integer rdTotalOrderCount;//预约次数
	private Integer rdTotalRenewCount;//续借次数
	private Integer rdTotalPeccancy;//违规次数
	private Integer rdTotalInterlibNum;//馆际互借次数
	private Double rdDeposit;//押金
	private Double rdDebt;//欠款
	private String rdHostIp;//IP地址
	private List<String> rdSort1List;//rdSort1列表
	private List<String> rdSort2List;//rdSort2列表
	private List<String> rdSort3List;//rdSort3列表
	private List<String> rdSort4List;//rdSort4列表
	private List<String> rdSort5List;//rdSort5列表
	private Integer openMsgService;//是否开通短信
	private String socialSecurityCard;//社保卡号
	private boolean hasPhoto;//是否存在图片
	private Double rdPrepay;//预付款
	private String rdDepartment;//部门
	
	private Integer libUser; //是否馆内用户，默认普通读者
	private List<Roles> roleList;
	
	private String condition;//查询条件
	private String condvalue;//条件值
	private String ordertype;//排序方式
	private PageEntity page;//分页对象
	private byte[] photobytes;//add 20131220 照片的数组
	private Integer groupId;//分组ID
	
	private String cardId;
	private String regman;//操作员账号 ADD 20140322
	private String paytype;//押金方式 ：1、现金 2、IC卡   不填写 默认是现金传递到opac ADD 20140322
	private String oldRdpasswd;//旧密码 ADD 20140513
	private Integer synStatus;//同步是否成功标识 ADD 20140513
	private String libAsSign;//多馆权限设置的字段
	
	private Double prepay;//账户余额
	
	private String retrievePasswordkey;//取回密码key值
	private Date retrievePasswordDate;//取回密码时间   ADD 20171023 BY WJS 
	
	private Integer isAuthor;//上传国图标识

	
	public Integer getIsAuthor() {
		return isAuthor;
	}

	public void setIsAuthor(Integer isAuthor) {
		this.isAuthor = isAuthor;
	}

	public String getRetrievePasswordkey() {
		return retrievePasswordkey;
	}

	public void setRetrievePasswordkey(String retrievePasswordkey) {
		this.retrievePasswordkey = retrievePasswordkey;
	}

	public Date getRetrievePasswordDate() {
		return retrievePasswordDate;
	}

	public void setRetrievePasswordDate(Date retrievePasswordDate) {
		this.retrievePasswordDate = retrievePasswordDate;
	}

	public Double getPrepay() {
		return prepay;
	}

	public void setPrepay(Double prepay) {
		this.prepay = prepay;
	}

	public String getLibAsSign() {
		return libAsSign;
	}
	
	public void setLibAsSign(String libAsSign) {
		this.libAsSign = libAsSign;
	}

	public String getOldRdpasswd() {
		return oldRdpasswd;
	}

	public void setOldRdpasswd(String oldRdpasswd) {
		this.oldRdpasswd = oldRdpasswd;
	}

	public Integer getSynStatus() {
		return synStatus;
	}

	public void setSynStatus(Integer synStatus) {
		this.synStatus = synStatus;
	}

	public String getRegman() {
		return regman;
	}

	public void setRegman(String regman) {
		this.regman = regman;
	}

	public String getPaytype() {
		return paytype;
	}

	public void setPaytype(String paytype) {
		this.paytype = paytype;
	}

	public String getRdId() {
		return rdId == null ? "":rdId.replaceAll("\\s", "");
	}

	public void setRdId(String rdId) {
		this.rdId = rdId == null ? "":rdId.replaceAll("\\s", "");;
	}

	public String getRdLoginId() {
		return rdLoginId == null ? "":rdLoginId.replaceAll("\\s", "");
	}

	public void setRdLoginId(String rdLoginId) {
		this.rdLoginId = rdLoginId == null ? "":rdLoginId.replaceAll("\\s", "");
	}

	public String getRdPasswd() {
		return rdPasswd ;
	}

	public void setRdPasswd(String rdPasswd) {
		this.rdPasswd = rdPasswd;
	}

	public String getRdName() {
		return rdName == null ? "":rdName.replaceAll("\\s", "");
	}

	public void setRdName(String rdName) {
		this.rdName = rdName == null ? "":rdName.replaceAll("\\s", "");
	}

	public String getRdCertify() {
		return rdCertify == null ? "":rdCertify.replaceAll("\\s", "");
	}

	public void setRdCertify(String rdCertify) {
		this.rdCertify = rdCertify == null ? "":rdCertify.replaceAll("\\s", "");
	}

	public Date getRdBornDate() {
		return rdBornDate;
	}

	public void setRdBornDate(Date rdBornDate) {
		this.rdBornDate = rdBornDate;
	}

	public byte getRdSex() {
		return rdSex;
	}

	public void setRdSex(byte rdSex) {
		this.rdSex = rdSex;
	}

	public byte getRdCFState() {
		return rdCFState;
	}

	public void setRdCFState(byte rdCFState) {
		this.rdCFState = rdCFState;
	}

	public String getRdType() {
		return rdType;
	}

	public void setRdType(String rdType) {
		this.rdType = rdType;
	}

	public byte getRdGlobal() {
		return rdGlobal;
	}

	public void setRdGlobal(byte rdGlobal) {
		this.rdGlobal = rdGlobal;
	}

	public String getRdLibType() {
		return rdLibType;
	}

	public void setRdLibType(String rdLibType) {
		this.rdLibType = rdLibType;
	}

	public String getRdLib() {
		return rdLib;
	}

	public void setRdLib(String rdLib) {
		this.rdLib = rdLib;
	}

	public Date getRdStartDate() {
		return rdStartDate;
	}

	public void setRdStartDate(Date rdStartDate) {
		this.rdStartDate = rdStartDate;
	}

	public Date getRdEndDate() {
		return rdEndDate;
	}

	public void setRdEndDate(Date rdEndDate) {
		this.rdEndDate = rdEndDate;
	}

	public String getRdAddress() {
		return rdAddress;
	}

	public void setRdAddress(String rdAddress) {
		this.rdAddress = rdAddress;
	}

	public String getRdPostCode() {
		return rdPostCode;
	}

	public void setRdPostCode(String rdPostCode) {
		this.rdPostCode = rdPostCode;
	}

	public String getRdEmail() {
		return rdEmail;
	}

	public void setRdEmail(String rdEmail) {
		this.rdEmail = rdEmail;
	}

	public String getRdPhone() {
		return rdPhone;
	}

	public void setRdPhone(String rdPhone) {
		this.rdPhone = rdPhone;
	}
	
	public String getRdUnit() {
		return rdUnit==null ? "" : rdUnit.replaceAll("\\s", "");
	}

	public void setRdUnit(String rdUnit) {
		this.rdUnit = rdUnit == null ? "":rdUnit.replaceAll("\\s", "");
	}

	public String getRdRemark() {
		return rdRemark;
	}

	public void setRdRemark(String rdRemark) {
		this.rdRemark = rdRemark;
	}

	public String getRdInterest() {
		return rdInterest;
	}

	public void setRdInterest(String rdInterest) {
		this.rdInterest = rdInterest;
	}

	public String getRdSpecialty() {
		return rdSpecialty;
	}

	public void setRdSpecialty(String rdSpecialty) {
		this.rdSpecialty = rdSpecialty;
	}

	public String getRdSort1() {
		return rdSort1;
	}

	public void setRdSort1(String rdSort1) {
		this.rdSort1 = rdSort1;
	}

	public String getRdSort2() {
		return rdSort2;
	}

	public void setRdSort2(String rdSort2) {
		this.rdSort2 = rdSort2;
	}

	public String getRdSort3() {
		return rdSort3;
	}

	public void setRdSort3(String rdSort3) {
		this.rdSort3 = rdSort3;
	}

	public String getRdSort4() {
		return rdSort4;
	}

	public void setRdSort4(String rdSort4) {
		this.rdSort4 = rdSort4;
	}

	public String getRdSort5() {
		return rdSort5;
	}

	public void setRdSort5(String rdSort5) {
		this.rdSort5 = rdSort5;
	}

	public byte[] getRdPhoto() {
		return rdPhoto;
	}

	public void setRdPhoto(byte[] rdPhoto) {
		this.rdPhoto = rdPhoto;
	}

	public String getRdNation() {
		return rdNation;
	}

	public void setRdNation(String rdNation) {
		this.rdNation = rdNation;
	}

	public String getRdNative() {
		return rdNative;
	}

	public void setRdNative(String rdNative) {
		this.rdNative = rdNative;
	}

	public Date getRdInTime() {
		return rdInTime;
	}

	public void setRdInTime(Date rdInTime) {
		this.rdInTime = rdInTime;
	}

	public Integer getRdScore() {
		return rdScore;
	}

	public void setRdScore(Integer rdScore) {
		this.rdScore = rdScore;
	}

	public Integer getRdTotalLoanCount() {
		return rdTotalLoanCount;
	}

	public void setRdTotalLoanCount(Integer rdTotalLoanCount) {
		this.rdTotalLoanCount = rdTotalLoanCount;
	}

	public Integer getTotalResNum() {
		return totalResNum;
	}

	public void setTotalResNum(Integer totalResNum) {
		this.totalResNum = totalResNum;
	}

	public Integer getTotalReNewNum() {
		return totalReNewNum;
	}

	public void setTotalReNewNum(Integer totalReNewNum) {
		this.totalReNewNum = totalReNewNum;
	}

	public Integer getTotalInterlibNum() {
		return totalInterlibNum;
	}

	public void setTotalInterlibNum(Integer totalInterlibNum) {
		this.totalInterlibNum = totalInterlibNum;
	}

	public Integer getTotalPeccancy() {
		return totalPeccancy;
	}

	public void setTotalPeccancy(Integer totalPeccancy) {
		this.totalPeccancy = totalPeccancy;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public Date getDueTime() {
		return dueTime;
	}

	public void setDueTime(Date dueTime) {
		this.dueTime = dueTime;
	}

	public String getSprule() {
		return sprule;
	}

	public void setSprule(String sprule) {
		this.sprule = sprule;
	}

	public byte getRdCenter() {
		return rdCenter;
	}

	public void setRdCenter(byte rdCenter) {
		this.rdCenter = rdCenter;
	}

	public String getCenterRule() {
		return centerRule;
	}

	public void setCenterRule(String centerRule) {
		this.centerRule = centerRule;
	}

	public String getRdRegisterName() {
		return rdRegisterName;
	}

	public void setRdRegisterName(String rdRegisterName) {
		this.rdRegisterName = rdRegisterName;
	}

	public byte getNotGetBookNum() {
		return notGetBookNum;
	}

	public void setNotGetBookNum(byte notGetBookNum) {
		this.notGetBookNum = notGetBookNum;
	}

	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}

	public String getPeriod() {
		return period;
	}

	public void setPeriod(String period) {
		this.period = period;
	}

	public byte getEleReadingNoControl() {
		return eleReadingNoControl;
	}

	public void setEleReadingNoControl(byte eleReadingNoControl) {
		this.eleReadingNoControl = eleReadingNoControl;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public Integer getOnlineSeconds() {
		return onlineSeconds;
	}

	public void setOnlineSeconds(Integer onlineSeconds) {
		this.onlineSeconds = onlineSeconds;
	}

	public Integer getsSordScore() {
		return sSordScore;
	}

	public void setsSordScore(Integer sSordScore) {
		this.sSordScore = sSordScore;
	}

	public String getWorkCardNo() {
		return workCardNo;
	}

	public void setWorkCardNo(String workCardNo) {
		this.workCardNo = workCardNo;
	}

	public String getOtherCardNo() {
		return otherCardNo;
	}

	public void setOtherCardNo(String otherCardNo) {
		this.otherCardNo = otherCardNo;
	}

	public byte getSMS_SYNC_FLAG() {
		return SMS_SYNC_FLAG;
	}

	public void setSMS_SYNC_FLAG(byte sMS_SYNC_FLAG) {
		SMS_SYNC_FLAG = sMS_SYNC_FLAG;
	}

	public byte getReceiveMobileNotifies() {
		return receiveMobileNotifies;
	}

	public void setReceiveMobileNotifies(byte receiveMobileNotifies) {
		this.receiveMobileNotifies = receiveMobileNotifies;
	}

	public byte getIsShowRemark() {
		return isShowRemark;
	}

	public void setIsShowRemark(byte isShowRemark) {
		this.isShowRemark = isShowRemark;
	}

	public Integer getChildNum() {
		return childNum;
	}

	public void setChildNum(Integer childNum) {
		this.childNum = childNum;
	}

	public String getRdCustomRemark() {
		return rdCustomRemark;
	}

	public void setRdCustomRemark(String rdCustomRemark) {
		this.rdCustomRemark = rdCustomRemark;
	}

	public byte getIsShowCustomRemark() {
		return isShowCustomRemark;
	}

	public void setIsShowCustomRemark(byte isShowCustomRemark) {
		this.isShowCustomRemark = isShowCustomRemark;
	}

	public String getRdLibCode() {
		return rdLibCode;
	}

	public void setRdLibCode(String rdLibCode) {
		this.rdLibCode = rdLibCode;
	}

	public String getRdStateStr() {
		return rdStateStr;
	}

	public void setRdStateStr(String rdStateStr) {
		this.rdStateStr = rdStateStr;
	}

	public String getRdInTimeStr() {
		return rdInTimeStr;
	}

	public void setRdInTimeStr(String rdInTimeStr) {
		this.rdInTimeStr = rdInTimeStr;
	}

	public String getRdStartDateStr() {
		return rdStartDateStr;
	}

	public void setRdStartDateStr(String rdStartDateStr) {
		this.rdStartDateStr = rdStartDateStr;
	}

	public String getRdEndDateStr() {
		return rdEndDateStr;
	}

	public void setRdEndDateStr(String rdEndDateStr) {
		this.rdEndDateStr = rdEndDateStr;
	}

	public String getRdBornDateStr() {
		return rdBornDateStr;
	}

	public void setRdBornDateStr(String rdBornDateStr) {
		this.rdBornDateStr = rdBornDateStr;
	}

	public Double getRdSpDeposit() {
		return rdSpDeposit;
	}

	public void setRdSpDeposit(Double rdSpDeposit) {
		this.rdSpDeposit = rdSpDeposit;
	}

	public Integer getRdTotalOrderCount() {
		return rdTotalOrderCount;
	}

	public void setRdTotalOrderCount(Integer rdTotalOrderCount) {
		this.rdTotalOrderCount = rdTotalOrderCount;
	}

	public Integer getRdTotalRenewCount() {
		return rdTotalRenewCount;
	}

	public void setRdTotalRenewCount(Integer rdTotalRenewCount) {
		this.rdTotalRenewCount = rdTotalRenewCount;
	}

	public Integer getRdTotalPeccancy() {
		return rdTotalPeccancy;
	}

	public void setRdTotalPeccancy(Integer rdTotalPeccancy) {
		this.rdTotalPeccancy = rdTotalPeccancy;
	}

	public Integer getRdTotalInterlibNum() {
		return rdTotalInterlibNum;
	}

	public void setRdTotalInterlibNum(Integer rdTotalInterlibNum) {
		this.rdTotalInterlibNum = rdTotalInterlibNum;
	}

	public Double getRdDeposit() {
		return rdDeposit;
	}

	public void setRdDeposit(Double rdDeposit) {
		this.rdDeposit = rdDeposit;
	}

	public Double getRdDebt() {
		return rdDebt;
	}

	public void setRdDebt(Double rdDebt) {
		this.rdDebt = rdDebt;
	}

	public String getRdHostIp() {
		return rdHostIp;
	}

	public void setRdHostIp(String rdHostIp) {
		this.rdHostIp = rdHostIp;
	}

	public List<String> getRdSort1List() {
		return rdSort1List;
	}

	public void setRdSort1List(List<String> rdSort1List) {
		this.rdSort1List = rdSort1List;
	}

	public List<String> getRdSort2List() {
		return rdSort2List;
	}

	public void setRdSort2List(List<String> rdSort2List) {
		this.rdSort2List = rdSort2List;
	}

	public List<String> getRdSort3List() {
		return rdSort3List;
	}

	public void setRdSort3List(List<String> rdSort3List) {
		this.rdSort3List = rdSort3List;
	}

	public List<String> getRdSort4List() {
		return rdSort4List;
	}

	public void setRdSort4List(List<String> rdSort4List) {
		this.rdSort4List = rdSort4List;
	}

	public List<String> getRdSort5List() {
		return rdSort5List;
	}

	public void setRdSort5List(List<String> rdSort5List) {
		this.rdSort5List = rdSort5List;
	}

	public Integer getOpenMsgService() {
		return openMsgService;
	}

	public void setOpenMsgService(Integer openMsgService) {
		this.openMsgService = openMsgService;
	}

	public String getSocialSecurityCard() {
		return socialSecurityCard;
	}

	public void setSocialSecurityCard(String socialSecurityCard) {
		this.socialSecurityCard = socialSecurityCard;
	}

	public boolean isHasPhoto() {
		return hasPhoto;
	}

	public void setHasPhoto(boolean hasPhoto) {
		this.hasPhoto = hasPhoto;
	}

	public Double getRdPrepay() {
		return rdPrepay;
	}

	public void setRdPrepay(Double rdPrepay) {
		this.rdPrepay = rdPrepay;
	}

	public String getRdDepartment() {
		return rdDepartment;
	}

	public void setRdDepartment(String rdDepartment) {
		this.rdDepartment = rdDepartment;
	}

	public Integer getLibUser() {
		return libUser;
	}

	public void setLibUser(Integer libUser) {
		this.libUser = libUser;
	}

	public List<Roles> getRoleList() {
		return roleList;
	}

	public void setRoleList(List<Roles> roleList) {
		this.roleList = roleList;
	}

	public String getCondition() {
		return condition;
	}

	public void setCondition(String condition) {
		this.condition = condition;
	}

	public String getCondvalue() {
		return condvalue;
	}

	public void setCondvalue(String condvalue) {
		this.condvalue = condvalue;
	}

	public String getOrdertype() {
		return ordertype;
	}

	public void setOrdertype(String ordertype) {
		this.ordertype = ordertype;
	}

	public PageEntity getPage() {
		return page;
	}

	public void setPage(PageEntity page) {
		this.page = page;
	}

	public byte[] getPhotobytes() {
		return photobytes;
	}

	public void setPhotobytes(byte[] photobytes) {
		this.photobytes = photobytes;
	}

	public Integer getGroupId() {
		return groupId;
	}

	public void setGroupId(Integer groupId) {
		this.groupId = groupId;
	}

	public String getCardId() {
		return cardId;
	}

	public void setCardId(String cardId) {
		this.cardId = cardId;
	}
	
	
	
	
}