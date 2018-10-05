package com.interlib.sso.domain;

public class UCompet_Resource {

	private String resourceId;
	private Integer competId;
	
	public UCompet_Resource(Integer competId, String resourceId) {
		this.competId = competId;
		this.resourceId = resourceId;
	}
	public String getResourceId() {
		return resourceId;
	}
	public void setResourceId(String resourceId) {
		this.resourceId = resourceId;
	}
	public Integer getCompetId() {
		return competId;
	}
	public void setCompetId(Integer competId) {
		this.competId = competId;
	}


}
