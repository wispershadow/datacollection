package org.wispersd.core.datacollect.searchapi.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.elasticsearch.action.search.MultiSearchRequestBuilder;
import org.elasticsearch.action.search.MultiSearchResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.wispersd.core.datacollect.searchapi.OMSWorkflowEventConstants;
import org.wispersd.core.datacollect.searchapi.OMSWorkflowEventSearchService;
import org.wispersd.core.datacollect.searchapi.dto.SearchWorkflowByTimeRangeCond;
import org.wispersd.core.datacollect.searchapi.dto.SearchWorkflowByTimeRangeResp;
import org.wispersd.core.datacollect.searchapi.dto.SearchWorkflowNodeByTimeRangeCond;
import org.wispersd.core.datacollect.searchapi.dto.SearchWorkflowNodeByTimeRangeResp;
import org.wispersd.core.datacollect.searchapi.queries.ElasticSearchQueryBuilder;
import org.wispersd.core.datacollect.searchapi.resp.ElasticSearchResponseBuilder;

public class DefaultOMSWorkflowEventSearchService implements OMSWorkflowEventSearchService, OMSWorkflowEventConstants {
	
	private Client elasticSearchClient;
	
	private ElasticSearchQueryBuilder<List<String>> workflowActionsQueryBuilder;
	private ElasticSearchResponseBuilder<Map<String, Set<String>>> workflowActionsRespBuilder;
	
	private List<ElasticSearchQueryBuilder<SearchWorkflowByTimeRangeCond>> workflowByTimeRangeQueryBuilders;
	private List<ElasticSearchResponseBuilder<SearchWorkflowByTimeRangeResp>> workflowByTimeRangeRespBuilders;
	
	private List<ElasticSearchQueryBuilder<SearchWorkflowNodeByTimeRangeCond>> workflowNodeByTimeRangeQueryBuilders;
	private List<ElasticSearchResponseBuilder<SearchWorkflowNodeByTimeRangeResp>> workflowNodeByTimeRangeRespBuilders;
	
	public Client getElasticSearchClient() {
		return elasticSearchClient;
	}

	public void setElasticSearchClient(Client elasticSearchClient) {
		this.elasticSearchClient = elasticSearchClient;
	}
	
	public ElasticSearchQueryBuilder<List<String>> getWorkflowActionsQueryBuilder() {
		return workflowActionsQueryBuilder;
	}

	public void setWorkflowActionsQueryBuilder(
			ElasticSearchQueryBuilder<List<String>> workflowActionsQueryBuilder) {
		this.workflowActionsQueryBuilder = workflowActionsQueryBuilder;
	}

	public ElasticSearchResponseBuilder<Map<String, Set<String>>> getWorkflowActionsRespBuilder() {
		return workflowActionsRespBuilder;
	}

	public void setWorkflowActionsRespBuilder(
			ElasticSearchResponseBuilder<Map<String, Set<String>>> workflowActionsRespBuilder) {
		this.workflowActionsRespBuilder = workflowActionsRespBuilder;
	}

	public List<ElasticSearchQueryBuilder<SearchWorkflowByTimeRangeCond>> getWorkflowByTimeRangeQueryBuilders() {
		return workflowByTimeRangeQueryBuilders;
	}

	public void setWorkflowByTimeRangeQueryBuilders(
			List<ElasticSearchQueryBuilder<SearchWorkflowByTimeRangeCond>> workflowByTimeRangeQueryBuilders) {
		this.workflowByTimeRangeQueryBuilders = workflowByTimeRangeQueryBuilders;
	}

	public List<ElasticSearchResponseBuilder<SearchWorkflowByTimeRangeResp>> getWorkflowByTimeRangeRespBuilders() {
		return workflowByTimeRangeRespBuilders;
	}

	public void setWorkflowByTimeRangeRespBuilders(
			List<ElasticSearchResponseBuilder<SearchWorkflowByTimeRangeResp>> workflowByTimeRangeRespBuilders) {
		this.workflowByTimeRangeRespBuilders = workflowByTimeRangeRespBuilders;
	}

	public List<ElasticSearchQueryBuilder<SearchWorkflowNodeByTimeRangeCond>> getWorkflowNodeByTimeRangeQueryBuilders() {
		return workflowNodeByTimeRangeQueryBuilders;
	}

	public void setWorkflowNodeByTimeRangeQueryBuilders(
			List<ElasticSearchQueryBuilder<SearchWorkflowNodeByTimeRangeCond>> workflowNodeByTimeRangeQueryBuilders) {
		this.workflowNodeByTimeRangeQueryBuilders = workflowNodeByTimeRangeQueryBuilders;
	}

	public List<ElasticSearchResponseBuilder<SearchWorkflowNodeByTimeRangeResp>> getWorkflowNodeByTimeRangeRespBuilders() {
		return workflowNodeByTimeRangeRespBuilders;
	}

	public void setWorkflowNodeByTimeRangeRespBuilders(
			List<ElasticSearchResponseBuilder<SearchWorkflowNodeByTimeRangeResp>> workflowNodeByTimeRangeRespBuilders) {
		this.workflowNodeByTimeRangeRespBuilders = workflowNodeByTimeRangeRespBuilders;
	}

	public Map<String, Set<String>> getActionsByWorkflows(List<String> workflows) {
		Map<String, Set<String>> result = new HashMap<String, Set<String>>();
		SearchResponse resp = workflowActionsQueryBuilder.convert(workflows).execute().actionGet();
		workflowActionsRespBuilder.buildResponse(resp, result);
		return result;
	}
	
	
	
	public SearchWorkflowByTimeRangeResp  searchWorkflowByTimeRange(SearchWorkflowByTimeRangeCond cond) {
		SearchWorkflowByTimeRangeResp result = new SearchWorkflowByTimeRangeResp();
		result.setWorkflowName(cond.getWorkflowName());
		result.setStartTime(cond.getStartTime());
		result.setEndTime(cond.getEndTime());
		this.performMultiSerach(cond, result, workflowByTimeRangeQueryBuilders, workflowByTimeRangeRespBuilders);
		return result;
	}
	
	public SearchWorkflowNodeByTimeRangeResp  searchWorkflowNodeByTimeRange(SearchWorkflowNodeByTimeRangeCond cond) {
		SearchWorkflowNodeByTimeRangeResp result = new SearchWorkflowNodeByTimeRangeResp();
		result.setActionName(cond.getNodeActionName());
		result.setStartTime(cond.getStartTime());
		result.setEndTime(cond.getEndTime());
		this.performMultiSerach(cond, result, workflowNodeByTimeRangeQueryBuilders, workflowNodeByTimeRangeRespBuilders);
		return result;
	}
	
	
	private  <R, S> void  performMultiSerach(R reqObject, S respObject, List<ElasticSearchQueryBuilder<R>> reqBuilders, List<ElasticSearchResponseBuilder<S>> respBuilders) {
		MultiSearchRequestBuilder multiSearchReqBuilder = elasticSearchClient.prepareMultiSearch();
		for(ElasticSearchQueryBuilder<R> nextReqBuilder: reqBuilders) {
			multiSearchReqBuilder.add(nextReqBuilder.convert(reqObject));
		}
		
		MultiSearchResponse sr = multiSearchReqBuilder.execute().actionGet();
		MultiSearchResponse.Item[] items = sr.getResponses();
		for(int i=0; i<respBuilders.size(); i++) {
			ElasticSearchResponseBuilder<S> nextRespBuilder = respBuilders.get(i);
			nextRespBuilder.buildResponse(items[i].getResponse(), respObject);
		}
	}
	
	
}
