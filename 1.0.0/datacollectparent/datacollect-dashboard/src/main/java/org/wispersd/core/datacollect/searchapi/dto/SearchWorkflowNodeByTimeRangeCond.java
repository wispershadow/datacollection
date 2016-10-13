package org.wispersd.core.datacollect.searchapi.dto;


public class SearchWorkflowNodeByTimeRangeCond extends ElasticSearchCondition{
	private String nodeActionName;
	private int statIntervalValue = 1;
	private StatIntervalUnits statIntervalUnit = StatIntervalUnits.HOUR;
	private String startTime;
	private String endTime;
	private int topSlow = 5;

	public String getNodeActionName() {
		return nodeActionName;
	}

	public void setNodeActionName(String nodeActionName) {
		this.nodeActionName = nodeActionName;
	}
	
	public int getStatIntervalValue() {
		return statIntervalValue;
	}

	public void setStatIntervalValue(int statIntervalValue) {
		this.statIntervalValue = statIntervalValue;
	}

	public StatIntervalUnits getStatIntervalUnit() {
		return statIntervalUnit;
	}

	public void setStatIntervalUnit(StatIntervalUnits statIntervalUnit) {
		this.statIntervalUnit = statIntervalUnit;
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

	public int getTopSlow() {
		return topSlow;
	}

	public void setTopSlow(int topSlow) {
		this.topSlow = topSlow;
	}
}
