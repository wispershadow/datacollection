package org.wispersd.core.datacollect.searchapi.queries;

import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.wispersd.core.datacollect.searchapi.dto.SearchWorkflowNodeByTimeRangeCond;

public class WorkflowActionExecTopslowQueryBuilder extends ElasticSearchQueryBuilder<SearchWorkflowNodeByTimeRangeCond>{

	public SearchRequestBuilder convert(SearchWorkflowNodeByTimeRangeCond cond) {
		SearchRequestBuilder reqBuilderNodeTopHit = elasticSearchClient.prepareSearch(cond.getIndex()).setTypes(TYPE_NAME_NODE)
				.setSize(cond.getTopSlow())
				.setQuery(QueryBuilders.boolQuery()
						  .filter(QueryBuilders.matchQuery(ATTRIB_ACTION, cond.getNodeActionName()))
						  .filter(QueryBuilders.rangeQuery(ATTRIB_STARTTIME).gt(cond.getStartTime()).format(DATETIME_FORMAT))
						  .filter(QueryBuilders.rangeQuery(ATTRIB_ENDTIME).lt(cond.getEndTime()).format(DATETIME_FORMAT))
						  ).addSort(ATTRIB_EXECUTIONTIME, SortOrder.DESC);
		return reqBuilderNodeTopHit;
	}

}
