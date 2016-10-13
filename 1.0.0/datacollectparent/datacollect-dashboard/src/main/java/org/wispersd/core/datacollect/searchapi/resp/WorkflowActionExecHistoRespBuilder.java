package org.wispersd.core.datacollect.searchapi.resp;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.aggregations.bucket.histogram.Histogram;
import org.elasticsearch.search.aggregations.metrics.stats.Stats;
import org.wispersd.core.datacollect.searchapi.dto.Metrics;
import org.wispersd.core.datacollect.searchapi.dto.SearchWorkflowNodeByTimeRangeResp;

public class WorkflowActionExecHistoRespBuilder extends ElasticSearchResponseBuilder<SearchWorkflowNodeByTimeRangeResp>{

	@Override
	public void buildResponse(SearchResponse response, SearchWorkflowNodeByTimeRangeResp result) {
		Histogram statsByActionTimeHist = response.getAggregations().get(AGGREGATION_STATSBYACTIONTIME);
		for(Histogram.Bucket nextStatsBucket: statsByActionTimeHist.getBuckets()) {
			Stats stats = (Stats)nextStatsBucket.getAggregations().get(AGGREGATION_EXECUTIONTIME);
			Metrics metrics = new Metrics();
			metrics.setMax(stats.getMax());
			metrics.setMin(stats.getMin());
			metrics.setAverage(stats.getAvg());
			metrics.setCount(nextStatsBucket.getDocCount());
			result.addMetricsByTimeline(nextStatsBucket.getKeyAsString(), metrics);
		}
		
	}

}
