package org.wispersd.core.datacollect.searchapi.queries;

import java.util.List;

import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;

public class ActionsByWorkflowQueryBuilder extends ElasticSearchQueryBuilder<List<String>>{

	public SearchRequestBuilder convert(List<String> source) {
		SearchRequestBuilder reqBuilderActions = elasticSearchClient.prepareSearch(OMSWORKFLOW_INDEX_NAME).setTypes(TYPE_NAME_NODE)
		.setSize(0)
		.addAggregation(AggregationBuilders.terms(AGGREGATION_WORKFLOWS).field(ATTRIB_PROCESSNAME)
						.subAggregation(AggregationBuilders.terms(AGGREGATION_NODES).field(ATTRIB_ACTION)));
		return reqBuilderActions;
	}

}
