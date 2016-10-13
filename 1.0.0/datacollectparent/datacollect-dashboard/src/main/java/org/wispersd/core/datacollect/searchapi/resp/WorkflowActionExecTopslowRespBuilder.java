package org.wispersd.core.datacollect.searchapi.resp;

import java.util.Map;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.SearchHit;
import org.wispersd.core.datacollect.searchapi.dto.ExecutionStats;
import org.wispersd.core.datacollect.searchapi.dto.SearchWorkflowNodeByTimeRangeResp;

public class WorkflowActionExecTopslowRespBuilder extends ElasticSearchResponseBuilder<SearchWorkflowNodeByTimeRangeResp>{

	@Override
	public void buildResponse(SearchResponse response, SearchWorkflowNodeByTimeRangeResp target) {
		for(SearchHit nextHit: response.getHits().getHits()) {
			ExecutionStats nextExecStats = new ExecutionStats();
			Map<String, Object> docSource = nextHit.getSource();
			nextExecStats.setKey((String)docSource.get(ATTRIB_PROCESSCODE));
			nextExecStats.setStartTime((String)docSource.get(ATTRIB_STARTTIME));
			nextExecStats.setExecutionTime(((Integer)docSource.get(ATTRIB_EXECUTIONTIME)).longValue());
			target.addTopSlowExecutionStats(nextExecStats);
		}
		
	}

}
