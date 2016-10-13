package org.wispersd.core.datacollect.searchapi.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class SearchWorkflowNodeByTimeRangeResp {
	private String actionName;
	private String startTime;
	private String endTime;
	private final Map<String, Metrics> metricsByTimeline = new TreeMap<String, Metrics>();
	private final List<ExecutionStats> topSlowExecutionStats = new ArrayList<ExecutionStats>();

	public String getActionName() {
		return actionName;
	}

	public void setActionName(String actionName) {
		this.actionName = actionName;
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

	public void addMetricsByTimeline(String timeline, Metrics metrics) {
		metricsByTimeline.put(timeline, metrics);
	}

	public Map<String, Metrics> getMetricsByTimeline() {
		return metricsByTimeline;
	}
	
	public void addTopSlowExecutionStats(ExecutionStats stats) {
		topSlowExecutionStats.add(stats);
	}
	

	public List<ExecutionStats> getTopSlowExecutionStats() {
		return topSlowExecutionStats;
	}
}
