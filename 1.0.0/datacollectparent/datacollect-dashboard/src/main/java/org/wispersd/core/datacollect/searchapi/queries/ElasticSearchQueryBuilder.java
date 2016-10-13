package org.wispersd.core.datacollect.searchapi.queries;

import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.client.Client;
import org.springframework.core.convert.converter.Converter;
import org.wispersd.core.datacollect.searchapi.OMSWorkflowEventConstants;

public abstract class ElasticSearchQueryBuilder<S> implements Converter<S, SearchRequestBuilder>, OMSWorkflowEventConstants {
	protected Client elasticSearchClient;
	
	public Client getElasticSearchClient() {
		return elasticSearchClient;
	}

	public void setElasticSearchClient(Client elasticSearchClient) {
		this.elasticSearchClient = elasticSearchClient;
	}
}
