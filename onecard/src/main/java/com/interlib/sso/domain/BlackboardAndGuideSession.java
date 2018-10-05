package com.interlib.sso.domain;

import java.util.ArrayList;
import java.util.List;

public class BlackboardAndGuideSession {
	
	private Blackboard blackboard;
	
	private Guide guide;
	
	private List<Blackboard> blackboardList = new ArrayList<Blackboard>();
	
	private List<Guide> guideList = new ArrayList<Guide>();

	public Blackboard getBlackboard() {
		return blackboard;
	}

	public void setBlackboard(Blackboard blackboard) {
		this.blackboard = blackboard;
	}

	public Guide getGuide() {
		return guide;
	}

	public void setGuide(Guide guide) {
		this.guide = guide;
	}

	public List<Blackboard> getBlackboardList() {
		return blackboardList;
	}

	public void setBlackboardList(List<Blackboard> blackboardList) {
		this.blackboardList = blackboardList;
	}

	public List<Guide> getGuideList() {
		return guideList;
	}

	public void setGuideList(List<Guide> guideList) {
		this.guideList = guideList;
	}
	
	
}
