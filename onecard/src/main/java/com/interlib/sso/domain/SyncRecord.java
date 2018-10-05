package com.interlib.sso.domain;

import java.io.Serializable;
import java.util.Date;
import com.interlib.sso.page.PageEntity;

public class SyncRecord implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3694512613996011353L;
	private String syncId;
	private String syncType;
	private String syncRdid;
	private String syncLib;
	private Date syncDate;
	private String syncStatus;
	private String syncCode;
	private String syncOperator;
	
	private PageEntity page;//分页对象
	
	private String startDate;
	private String endDate;
	private String sortColumn;
	private String sortType;
	public String getSyncId() {
		return syncId;
	}
	public void setSyncId(String syncId) {
		this.syncId = syncId;
	}
	public String getSyncType() {
		return syncType;
	}
	public void setSyncType(String syncType) {
		this.syncType = syncType;
	}
	public String getSyncRdid() {
		return syncRdid;
	}
	public void setSyncRdid(String syncRdid) {
		this.syncRdid = syncRdid;
	}
	public String getSyncLib() {
		return syncLib;
	}
	public void setSyncLib(String syncLib) {
		this.syncLib = syncLib;
	}
	public Date getSyncDate() {
		return syncDate;
	}
	public void setSyncDate(Date syncDate) {
		this.syncDate = syncDate;
	}
	public String getSyncStatus() {
		return syncStatus;
	}
	public void setSyncStatus(String syncStatus) {
		this.syncStatus = syncStatus;
	}
	public String getSyncCode() {
		return syncCode;
	}
	public void setSyncCode(String syncCode) {
		this.syncCode = syncCode;
	}
	
	public String getSyncOperator() {
		return syncOperator;
	}
	public void setSyncOperator(String syncOperator) {
		this.syncOperator = syncOperator;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public String getSortColumn() {
		return sortColumn;
	}
	public void setSortColumn(String sortColumn) {
		this.sortColumn = sortColumn;
	}
	public String getSortType() {
		return sortType;
	}
	public void setSortType(String sortType) {
		this.sortType = sortType;
	}
	public PageEntity getPage() {
		return page;
	}
	public void setPage(PageEntity page) {
		this.page = page;
	}
	
}
