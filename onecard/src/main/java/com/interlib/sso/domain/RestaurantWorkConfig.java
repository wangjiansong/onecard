package com.interlib.sso.domain;

import java.io.Serializable;
import java.util.Date;

import com.interlib.sso.page.PageEntity;


/**
 * 餐饮类工作设置
 * @author Home
 *
 */
public class RestaurantWorkConfig implements Serializable {
	
	
	private Integer id;
	/*
	 * 早餐
	 */
	private String breakfastStartTime;
	private String breakfastEndTime;
	/*
	 * 中餐
	 */
	private String lunchStartTime;
	private String lunchEndTime;
	/*
	 * 晚餐
	 */
	private String dinnerStartTime;
	private String dinnerEndTime;
	/*
	 * 夜宵
	 */
	private String supperStartTime;
	private String supperEndTime;
	
	private PageEntity page;//分页对象
	
	public PageEntity getPage() {
		return page;
	}
	public void setPage(PageEntity page) {
		this.page = page;
	}
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getBreakfastStartTime() {
		return breakfastStartTime;
	}
	public void setBreakfastStartTime(String breakfastStartTime) {
		this.breakfastStartTime = breakfastStartTime;
	}
	public String getBreakfastEndTime() {
		return breakfastEndTime;
	}
	public void setBreakfastEndTime(String breakfastEndTime) {
		this.breakfastEndTime = breakfastEndTime;
	}
	public String getLunchStartTime() {
		return lunchStartTime;
	}
	public void setLunchStartTime(String lunchStartTime) {
		this.lunchStartTime = lunchStartTime;
	}
	public String getLunchEndTime() {
		return lunchEndTime;
	}
	public void setLunchEndTime(String lunchEndTime) {
		this.lunchEndTime = lunchEndTime;
	}
	public String getDinnerStartTime() {
		return dinnerStartTime;
	}
	public void setDinnerStartTime(String dinnerStartTime) {
		this.dinnerStartTime = dinnerStartTime;
	}
	public String getDinnerEndTime() {
		return dinnerEndTime;
	}
	public void setDinnerEndTime(String dinnerEndTime) {
		this.dinnerEndTime = dinnerEndTime;
	}
	public String getSupperStartTime() {
		return supperStartTime;
	}
	public void setSupperStartTime(String supperStartTime) {
		this.supperStartTime = supperStartTime;
	}
	public String getSupperEndTime() {
		return supperEndTime;
	}
	public void setSupperEndTime(String supperEndTime) {
		this.supperEndTime = supperEndTime;
	}
	
}
