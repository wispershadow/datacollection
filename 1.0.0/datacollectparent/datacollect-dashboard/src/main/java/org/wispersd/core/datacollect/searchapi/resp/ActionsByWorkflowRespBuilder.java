package org.wispersd.core.datacollect.searchapi.resp;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;

public class ActionsByWorkflowRespBuilder extends ElasticSearchResponseBuilder<Map<String, Set<String>>>{

	@Override
	public void buildResponse(SearchResponse resp,
			Map<String, Set<String>> result) {
		StringTerms workflowAggrs = resp.getAggregations().get(AGGREGATION_WORKFLOWS);
		for(Terms.Bucket nextWorkflowBucket: workflowAggrs.getBuckets()) {
			StringTerms nodeAggrs = nextWorkflowBucket.getAggregations().get(AGGREGATION_NODES);
			String workflowName = nextWorkflowBucket.getKeyAsString();
			Set<String> valueSet = result.get(workflowName);
			if (valueSet == null) {
				valueSet = new HashSet<String>();
				result.put(workflowName, valueSet);
			}
			for(Terms.Bucket nextNodeBucket: nodeAggrs.getBuckets()) {
				valueSet.add(nextNodeBucket.getKeyAsString());
			}
		}
		
	}

}
