package org.wispersd.core.datacollect.searchintegration;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.elasticsearch.action.admin.indices.create.CreateIndexRequestBuilder;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.bulk.BackoffPolicy;
import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkProcessor;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.indices.IndexAlreadyExistsException;
import org.elasticsearch.script.Script;
import org.elasticsearch.script.ScriptService.ScriptType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wispersd.core.integration.DataProcessDelegate;

import com.fasterxml.jackson.databind.ObjectMapper;

public class KafkaToElasticSearchSink implements DataProcessDelegate<String, String, String>{
	private static final Logger logger = LoggerFactory.getLogger(KafkaToElasticSearchSink.class);
	private int batchSubmitSize = 1;
	private long submitMaxWait = 2000;
	
	private final Set<String> indexMappingSet = Collections.synchronizedSet(new HashSet<String>());
	private Client elasticSearchClient;
	private ObjectMapper objectMapper;
	private ElasticSearchMappingStrategy elasticSearchMappingStrategy;
	private BulkProcessor bulkProcessor;

	private static final String UPDATE_SCRIPT = "ctx._source.status=status \n ctx._source.endTime=endTime \n ctx._source.executionTime=new Date().parse(\"yyyy/MM/dd HH:mm:ss\", endTime).getTime()-new Date().parse(\"yyyy/MM/dd HH:mm:ss\", ctx._source.startTime).getTime()";
	
	public int getBatchSubmitSize() {
		return batchSubmitSize;
	}

	public void setBatchSubmitSize(int batchSubmitSize) {
		this.batchSubmitSize = batchSubmitSize;
	}

	public long getSubmitMaxWait() {
		return submitMaxWait;
	}

	public void setSubmitMaxWait(long submitMaxWait) {
		this.submitMaxWait = submitMaxWait;
	}


	public Client getElasticSearchClient() {
		return elasticSearchClient;
	}

	public void setElasticSearchClient(Client elasticSearchClient) {
		this.elasticSearchClient = elasticSearchClient;
	}

	public ObjectMapper getObjectMapper() {
		return objectMapper;
	}

	public void setObjectMapper(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
	}

	public ElasticSearchMappingStrategy getElasticSearchMappingStrategy() {
		return elasticSearchMappingStrategy;
	}

	public void setElasticSearchMappingStrategy(
			ElasticSearchMappingStrategy elasticSearchMappingStrategy) {
		this.elasticSearchMappingStrategy = elasticSearchMappingStrategy;
	}




	public void init() {
		bulkProcessor = BulkProcessor.builder(elasticSearchClient, new BulkProcessor.Listener() {

			public void beforeBulk(long executionId, BulkRequest request) {
				// TODO Auto-generated method stub
				
			}

			public void afterBulk(long executionId, BulkRequest request,
					BulkResponse response) {
				if (logger.isDebugEnabled()) {
					logger.debug("Bulk request successfully submitted");
				}
				for(BulkItemResponse nextItem: response.getItems()) {
					if (nextItem.getFailureMessage() != null) {
						logger.error("Error submitting bulk request ", nextItem.getItemId()+ " "+ nextItem.getFailureMessage());
					}
				}
			}

			public void afterBulk(long executionId, BulkRequest request,
					Throwable failure) {
				failure.printStackTrace();
				logger.error("Error submitting bulk request " + request.toString() , failure);
			}}
		).setBulkActions(this.batchSubmitSize)
		 .setFlushInterval(TimeValue.timeValueMillis(submitMaxWait))
		 .setConcurrentRequests(1)
		 .setBackoffPolicy(BackoffPolicy.exponentialBackoff(TimeValue.timeValueMillis(100), 3))
		 .build();
	}


	public void destroy() {
		bulkProcessor.close();
	}


	public String execute(String topic, String key, String param) throws Exception {
		addToBulkRequest(topic, key, param);
		return "submitted";
	}

	
	protected void addToBulkRequest(String topic, String key, String param) throws Exception {	
		Map<String, Object> rootNodeMap = objectMapper.readValue(param, Map.class);
		ElasticSearchMappingContext<Map<String, Object>> context = new ElasticSearchMappingContext<Map<String, Object>>(rootNodeMap);
		context.addAttribute(MappingContextAttributes.ATTRIB_TOPIC, topic);
		context.addAttribute(MappingContextAttributes.ATTRIB_KEY, key);
		ElasticSearchMappingResult mappingRes = elasticSearchMappingStrategy.getElasticSearchMapping(context);
		String indexName = mappingRes.getIndexName();
		String typeName = mappingRes.getTypeName();
		String documentId = mappingRes.getDocumentId();
		String mappingFileName = mappingRes.getMappingFileName();
		if ((!indexMappingSet.contains(mappingFileName))) {
			try {
				CreateIndexRequestBuilder createIndexRequestBuilder = elasticSearchClient.admin().indices().prepareCreate(indexName);
				String mappingDtl = readMappingFile(mappingFileName);
				createIndexRequestBuilder.addMapping(typeName, mappingDtl);
				CreateIndexResponse indexResponse = createIndexRequestBuilder.execute().actionGet();
				indexMappingSet.add(mappingFileName);
			} catch (IndexAlreadyExistsException ie)  {
				logger.error("Error adding mapping file", ie);
				indexMappingSet.add(mappingFileName);
			} catch (Exception e) {
				logger.error("Error adding mapping file", e);
			}
		}
		
		if (logger.isDebugEnabled()) {
			logger.debug("Adding new document to index indexname={}, typename={}, documentid={}", new Object[]{indexName, typeName, documentId});
		}
		bulkProcessor.add(new UpdateRequest(indexName, typeName, documentId).script(new Script(UPDATE_SCRIPT, ScriptType.INLINE, "groovy", rootNodeMap)).upsert(rootNodeMap));
	}
	
	private String readMappingFile(String mappingFileName) {
		try {
			StringBuilder res = new StringBuilder();
			BufferedReader br = new BufferedReader(new InputStreamReader(KafkaToElasticSearchSink.class.getClassLoader().getResourceAsStream(mappingFileName)));
			String nextLine = br.readLine();
			while (nextLine != null) {
				res.append(nextLine);
				nextLine = br.readLine();
			}
			br.close();
			return res.toString();
		} catch (IOException e) {
		}
		return null;
	}
	
}
