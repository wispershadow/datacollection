package org.wispersd.core.datacollect.searchapi.queries;

import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.wispersd.core.datacollect.searchapi.dto.SearchWorkflowByTimeRangeCond;

public class WorkflowActionExecStatQueryBuilder extends ElasticSearchQueryBuilder<SearchWorkflowByTimeRangeCond>{
	
	public SearchRequestBuilder convert(SearchWorkflowByTimeRangeCond cond) {
		SearchRequestBuilder reqBuilderExecTime = elasticSearchClient.prepareSearch(cond.getIndex()).setTypes(TYPE_NAME_NODE)
				.setSize(0)
				.setQuery(QueryBuilders.boolQuery()
						  .filter(QueryBuilders.matchQuery(ATTRIB_PROCESSNAME, cond.getWorkflowName()))	
						  .filter(QueryBuilders.rangeQuery(ATTRIB_STARTTIME).gt(cond.getStartTime()).format(DATETIME_FORMAT))
						  .filter(QueryBuilders.rangeQuery(ATTRIB_ENDTIME).lt(cond.getEndTime()).format(DATETIME_FORMAT))
						  )
				.addAggregation(AggregationBuilders.terms(AGGREGATION_STATSBYNODETYPE).field(ATTRIB_ACTION)
								   .subAggregation(AggregationBuilders.stats(AGGREGATION_EXECUTIONTIME).field(ATTRIB_EXECUTIONTIME))	
							   );
		return reqBuilderExecTime;
	}

}
