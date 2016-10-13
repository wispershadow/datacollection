package org.wispersd.core.datacollect.searchapi.queries;

import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramInterval;
import org.wispersd.core.datacollect.searchapi.dto.SearchWorkflowNodeByTimeRangeCond;
import org.wispersd.core.datacollect.searchapi.dto.StatIntervalUnits;

public class WorkflowActionExecHistoQueryBuilder extends ElasticSearchQueryBuilder<SearchWorkflowNodeByTimeRangeCond>{

	public SearchRequestBuilder convert(SearchWorkflowNodeByTimeRangeCond cond) {
		DateHistogramInterval interval = fromRequest(cond);
		SearchRequestBuilder reqBuilderNodeHist = elasticSearchClient.prepareSearch(cond.getIndex()).setTypes(TYPE_NAME_NODE)
		.setSize(0)
		.setQuery(QueryBuilders.boolQuery()
				  .filter(QueryBuilders.matchQuery(ATTRIB_ACTION, cond.getNodeActionName()))
				  .filter(QueryBuilders.rangeQuery(ATTRIB_STARTTIME).gt(cond.getStartTime()).format(DATETIME_FORMAT))
				  .filter(QueryBuilders.rangeQuery(ATTRIB_ENDTIME).lt(cond.getEndTime()).format(DATETIME_FORMAT))
				  )
		.addAggregation(AggregationBuilders.dateHistogram(AGGREGATION_STATSBYACTIONTIME).field(ATTRIB_STARTTIME).interval(interval).format(DATETIME_FORMAT)
						.subAggregation(AggregationBuilders.stats(AGGREGATION_EXECUTIONTIME).field(ATTRIB_EXECUTIONTIME)));
	
		return reqBuilderNodeHist;
	}
	
	protected DateHistogramInterval fromRequest(SearchWorkflowNodeByTimeRangeCond cond) {
		StatIntervalUnits statIntervalUnit = cond.getStatIntervalUnit();
		if (StatIntervalUnits.DAY == statIntervalUnit) {
			return DateHistogramInterval.days(cond.getStatIntervalValue());
		}
		else if (StatIntervalUnits.HOUR == statIntervalUnit) {
			return DateHistogramInterval.hours(cond.getStatIntervalValue());
		}
		else if (StatIntervalUnits.MINUTE == statIntervalUnit) {
			return DateHistogramInterval.minutes(cond.getStatIntervalValue());
		}
		else {
			return DateHistogramInterval.HOUR;
		}
	}

}
