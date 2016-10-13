package org.wispersd.core.datacollect.searchapi.resp;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.metrics.stats.Stats;
import org.wispersd.core.datacollect.searchapi.dto.Metrics;
import org.wispersd.core.datacollect.searchapi.dto.SearchWorkflowByTimeRangeResp;

public class WorkflowStatusCountRespBuilder extends ElasticSearchResponseBuilder<SearchWorkflowByTimeRangeResp>{

	@Override
	public void buildResponse(SearchResponse response, SearchWorkflowByTimeRangeResp result) {
		Aggregations stateCountAggr = response.getAggregations();
		StringTerms countByProcState  = (StringTerms)stateCountAggr.get(AGGREGATION_COUNTBYPROCSTATE);
		for(Terms.Bucket nextStatsBucket: countByProcState.getBuckets()) {
			Stats stats = (Stats)nextStatsBucket.getAggregations().get(AGGREGATION_EXECUTIONTIME);
			Metrics metrics = new Metrics();
			metrics.setMax(stats.getMax());
			metrics.setMin(stats.getMin());
			metrics.setAverage(stats.getAvg());
			metrics.setCount(nextStatsBucket.getDocCount());
			result.addMetricsByStatus(nextStatsBucket.getKeyAsString(), metrics);
		}	
	}

}
