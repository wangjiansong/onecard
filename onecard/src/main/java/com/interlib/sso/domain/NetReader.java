package com.interlib.sso.domain;

import java.io.Serializable;
import java.util.Date;

import com.interlib.sso.page.PageEntity;

/**
 * 网络读者
 * @author Lullaby
 *
 */
public class NetReader implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String readerId;//读者证号
	
	private String readerName;//读者名称
	
	private String readerPassword;//读者密码
	
	private String readerType;//读者类型
	
	private byte readerCardState;//证状态
	
	private String readerLib;//开户管
	
	private Date readerHandleDate;//办证时间
	
	private Date readerStartDate;//启用日期
	
	private Date readerEndDate;//终止日期
	
	private byte checkState;//审批状态
	
	private byte readerGender;//性别
	
	private String readerCertify;//身份证号码
	
	private String readerMobile;//手机
	
	private String readerEmail;//邮箱
	
	private String readerNative;//籍贯
	
	private String readerAddress;//地址
	
	private String readerUnit;//单位
	
	private String readerSort1;//专业
	
	private String readerSort2;//职业
	
	private String readerSort3;//职务
	
	private String readerSort4;//职称
	
	private String readerSort5;//文化
	
	private String remark;//备注
	
	private String readerCardStateStr;//证状态说明
	
	private String checkStateStr;//审批状态说明
	
	private String readerHandleDateStr;//办证时间String
	
	private String readerStartDateStr;//启用日期String
	
	private String readerEndDateStr;//终止日期String
	
	private static PageEntity page = new PageEntity();//分页对象
	
	static{
		page.setShowCount(100);
	}
	
	//缺少出生日期字段 2014-11-12
	private Date readerBornDate;//出生日期
	
	private String readerBornDateStr;//出生日期String
	
	//做时间段查询用的
		private String searchKey;
		private String searchValue;
		private String rdname;
//		private Date startTime;
//		private Date endTime;
		private String startTime;
		private String endTime;
		private String startRdid;
		private String endRdid;
		//用于按日、月、年统计 DAY MONTH YEAR
		private String dateFormat = "DAY";
		
		private String day;

		//福建省图导出时间查询字段
		private String readerHandleStartDate;
		private String readerHandleEndDate;
		
		
		
		
		public String getReaderHandleStartDate() {
			return readerHandleStartDate;
		}

		public void setReaderHandleStartDate(String readerHandleStartDate) {
			this.readerHandleStartDate = readerHandleStartDate;
		}

		public String getReaderHandleEndDate() {
			return readerHandleEndDate;
		}

		public void setReaderHandleEndDate(String readerHandleEndDate) {
			this.readerHandleEndDate = readerHandleEndDate;
		}

		public String getDay() {
			return day;
		}

		public void setDay(String day) {
			this.day = day;
		}

		public String getSearchKey() {
			return searchKey;
		}

		public void setSearchKey(String searchKey) {
			this.searchKey = searchKey;
		}

		public String getSearchValue() {
			return searchValue;
		}

		public void setSearchValue(String searchValue) {
			this.searchValue = searchValue;
		}

		public String getRdname() {
			return rdname;
		}

		public void setRdname(String rdname) {
			this.rdname = rdname;
		}

		public String getStartTime() {
			return startTime;
		}

		public void setStartTime(String startTime) {
			this.startTime = startTime;
		}

		public String getEndTime() {
			return endTime;
		}

		public void setEndTime(String endTime) {
			this.endTime = endTime;
		}

		public String getStartRdid() {
			return startRdid;
		}

		public void setStartRdid(String startRdid) {
			this.startRdid = startRdid;
		}

		public String getEndRdid() {
			return endRdid;
		}

		public void setEndRdid(String endRdid) {
			this.endRdid = endRdid;
		}

		public String getDateFormat() {
			return dateFormat;
		}

		public void setDateFormat(String dateFormat) {
			this.dateFormat = dateFormat;
		}

	public Date getReaderBornDate() {
		return readerBornDate;
	}

	public void setReaderBornDate(Date readerBornDate) {
		this.readerBornDate = readerBornDate;
	}

	public String getReaderBornDateStr() {
		return readerBornDateStr;
	}

	public void setReaderBornDateStr(String readerBornDateStr) {
		this.readerBornDateStr = readerBornDateStr;
	}

	public String getReaderId() {
		return readerId;
	}

	public void setReaderId(String readerId) {
		this.readerId = readerId;
	}

	public String getReaderName() {
		return readerName;
	}

	public void setReaderName(String readerName) {
		this.readerName = readerName;
	}

	public String getReaderPassword() {
		return readerPassword;
	}

	public void setReaderPassword(String readerPassword) {
		this.readerPassword = readerPassword;
	}

	public String getReaderType() {
		return readerType;
	}

	public void setReaderType(String readerType) {
		this.readerType = readerType;
	}
	
	public byte getReaderCardState() {
		return readerCardState;
	}

	public void setReaderCardState(byte readerCardState) {
		this.readerCardState = readerCardState;
	}

	public String getReaderLib() {
		return readerLib;
	}

	public void setReaderLib(String readerLib) {
		this.readerLib = readerLib;
	}

	public Date getReaderHandleDate() {
		return readerHandleDate;
	}

	public void setReaderHandleDate(Date readerHandleDate) {
		this.readerHandleDate = readerHandleDate;
	}

	public Date getReaderStartDate() {
		return readerStartDate;
	}

	public void setReaderStartDate(Date readerStartDate) {
		this.readerStartDate = readerStartDate;
	}

	public Date getReaderEndDate() {
		return readerEndDate;
	}

	public void setReaderEndDate(Date readerEndDate) {
		this.readerEndDate = readerEndDate;
	}

	public byte getCheckState() {
		return checkState;
	}

	public void setCheckState(byte checkState) {
		this.checkState = checkState;
	}

	public byte getReaderGender() {
		return readerGender;
	}

	public void setReaderGender(byte readerGender) {
		this.readerGender = readerGender;
	}

	public String getReaderCertify() {
		return readerCertify;
	}

	public void setReaderCertify(String readerCertify) {
		this.readerCertify = readerCertify;
	}

	public String getReaderMobile() {
		return readerMobile;
	}

	public void setReaderMobile(String readerMobile) {
		this.readerMobile = readerMobile;
	}

	public String getReaderEmail() {
		return readerEmail;
	}

	public void setReaderEmail(String readerEmail) {
		this.readerEmail = readerEmail;
	}

	public String getReaderNative() {
		return readerNative;
	}

	public void setReaderNative(String readerNative) {
		this.readerNative = readerNative;
	}

	public String getReaderAddress() {
		return readerAddress;
	}

	public void setReaderAddress(String readerAddress) {
		this.readerAddress = readerAddress;
	}

	public String getReaderUnit() {
		return readerUnit;
	}

	public void setReaderUnit(String readerUnit) {
		this.readerUnit = readerUnit;
	}

	public String getReaderSort1() {
		return readerSort1;
	}

	public void setReaderSort1(String readerSort1) {
		this.readerSort1 = readerSort1;
	}

	public String getReaderSort2() {
		return readerSort2;
	}

	public void setReaderSort2(String readerSort2) {
		this.readerSort2 = readerSort2;
	}

	public String getReaderSort3() {
		return readerSort3;
	}

	public void setReaderSort3(String readerSort3) {
		this.readerSort3 = readerSort3;
	}

	public String getReaderSort4() {
		return readerSort4;
	}

	public void setReaderSort4(String readerSort4) {
		this.readerSort4 = readerSort4;
	}

	public String getReaderSort5() {
		return readerSort5;
	}

	public void setReaderSort5(String readerSort5) {
		this.readerSort5 = readerSort5;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	public String getReaderCardStateStr() {
		return readerCardStateStr;
	}

	public void setReaderCardStateStr(String readerCardStateStr) {
		this.readerCardStateStr = readerCardStateStr;
	}
	
	public String getCheckStateStr() {
		return checkStateStr;
	}

	public void setCheckStateStr(String checkStateStr) {
		this.checkStateStr = checkStateStr;
	}
	
	public String getReaderHandleDateStr() {
		return readerHandleDateStr;
	}

	public void setReaderHandleDateStr(String readerHandleDateStr) {
		this.readerHandleDateStr = readerHandleDateStr;
	}

	public String getReaderStartDateStr() {
		return readerStartDateStr;
	}

	public void setReaderStartDateStr(String readerStartDateStr) {
		this.readerStartDateStr = readerStartDateStr;
	}

	public String getReaderEndDateStr() {
		return readerEndDateStr;
	}

	public void setReaderEndDateStr(String readerEndDateStr) {
		this.readerEndDateStr = readerEndDateStr;
	}
	
	public PageEntity getPage() {
		return page;
	}

	public void setPage(PageEntity page) {
		this.page = page;
	}
	
}
