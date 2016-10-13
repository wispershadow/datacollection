package org.wispersd.core.datacollect.searchapi;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.wispersd.core.datacollect.searchapi.dto.SearchWorkflowByTimeRangeCond;
import org.wispersd.core.datacollect.searchapi.dto.SearchWorkflowByTimeRangeResp;
import org.wispersd.core.datacollect.searchapi.dto.SearchWorkflowNodeByTimeRangeCond;
import org.wispersd.core.datacollect.searchapi.dto.SearchWorkflowNodeByTimeRangeResp;

public interface OMSWorkflowEventSearchService {
	public Map<String, Set<String>> getActionsByWorkflows(List<String> workflows);
	
	public SearchWorkflowByTimeRangeResp  searchWorkflowByTimeRange(SearchWorkflowByTimeRangeCond cond);
	
	public SearchWorkflowNodeByTimeRangeResp  searchWorkflowNodeByTimeRange(SearchWorkflowNodeByTimeRangeCond cond);

}
