package org.wispersd.core.datacollect.searchapi.resp;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.search.SearchHit;
import org.wispersd.core.datacollect.searchapi.dto.AllWorkflowNodesExecResp;

public class AllWorkflowNodesExecRespBuilder extends ElasticSearchResponseBuilder<AllWorkflowNodesExecResp>{
	protected Client elasticSearchClient;
	
	public Client getElasticSearchClient() {
		return elasticSearchClient;
	}

	public void setElasticSearchClient(Client elasticSearchClient) {
		this.elasticSearchClient = elasticSearchClient;
	}
	
	@Override
	public void buildResponse(SearchResponse response, AllWorkflowNodesExecResp result) {
		SearchResponse scrollResp = response;
		while (true) {
		    for (SearchHit hit : scrollResp.getHits().getHits()) {
		        //Handle the hit...
		    }
		    scrollResp = elasticSearchClient.prepareSearchScroll(scrollResp.getScrollId()).setScroll(new TimeValue(60000)).execute().actionGet();
		    //Break condition: No hits are returned
		    if (scrollResp.getHits().getHits().length == 0) {
		        break;
		    }
		}
		
	}

}
