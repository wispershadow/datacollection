package org.wispersd.core.datacollect.searchapi.resp;

import org.elasticsearch.action.search.SearchResponse;
import org.wispersd.core.datacollect.searchapi.OMSWorkflowEventConstants;

public abstract class ElasticSearchResponseBuilder<T> implements OMSWorkflowEventConstants {
	public abstract void buildResponse(SearchResponse response, T target);
}
