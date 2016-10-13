package org.wispersd.core.datacollect.searchapi.queries;

import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.wispersd.core.datacollect.searchapi.dto.SearchWorkflowByTimeRangeCond;

public class WorkflowStatusCountQueryBuilder extends ElasticSearchQueryBuilder<SearchWorkflowByTimeRangeCond>{
	public SearchRequestBuilder convert(SearchWorkflowByTimeRangeCond cond) {
		SearchRequestBuilder reqBuilderCountByStatus = elasticSearchClient.prepareSearch(cond.getIndex()).setTypes(TYPE_NAME_PROCESS)
				//.setFetchSource(new String[]{"id", "processName", "processCode"}, null)
				.setSize(0)
				.setQuery(QueryBuilders.boolQuery()
						  .filter(QueryBuilders.termQuery(ATTRIB_PROCESSNAME, cond.getWorkflowName()))	
						  .mustNot(QueryBuilders.rangeQuery(ATTRIB_ENDTIME).lt(cond.getStartTime()).format(DATETIME_FORMAT))
						  .mustNot(QueryBuilders.rangeQuery(ATTRIB_STARTTIME).gt(cond.getEndTime()).format(DATETIME_FORMAT))
						  )
				.addAggregation(AggregationBuilders.terms(AGGREGATION_COUNTBYPROCSTATE).field(ATTRIB_STATUS)
									.subAggregation(AggregationBuilders.stats(AGGREGATION_EXECUTIONTIME).field(ATTRIB_EXECUTIONTIME))	
								);
		return reqBuilderCountByStatus;
	}
}
