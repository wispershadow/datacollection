package org.wispersd.core.datacollect.searchapi.dto;

import java.util.HashMap;
import java.util.Map;

public class SearchWorkflowByTimeRangeResp {
	private String workflowName;
	private String startTime;
	private String endTime;
	private final Map<String, Metrics> metricsByStatus = new HashMap<String, Metrics>();
	private final Map<String, Metrics> metricsByAction = new HashMap<String, Metrics>();

	public String getWorkflowName() {
		return workflowName;
	}

	public void setWorkflowName(String workflowName) {
		this.workflowName = workflowName;
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
	
	public void addMetricsByStatus(String status, Metrics metrics) {
		metricsByStatus.put(status, metrics);
	}
	
	public void addMetricsByAction(String actionNode, Metrics metrics) {
		metricsByAction.put(actionNode, metrics);
	}

	public Map<String, Metrics> getMetricsByStatus() {
		return metricsByStatus;
	}

	public Map<String, Metrics> getMetricsByAction() {
		return metricsByAction;
	}
	
	
}
