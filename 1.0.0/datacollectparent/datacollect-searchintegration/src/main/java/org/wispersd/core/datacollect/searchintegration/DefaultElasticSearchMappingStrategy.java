package org.wispersd.core.datacollect.searchintegration;

import java.util.Map;

public class DefaultElasticSearchMappingStrategy implements
		ElasticSearchMappingStrategy {
	private Map<String, String> topicToIndexMapping;

	
	
	public Map<String, String> getTopicToIndexMapping() {
		return topicToIndexMapping;
	}

	public void setTopicToIndexMapping(Map<String, String> topicToIndexMapping) {
		this.topicToIndexMapping = topicToIndexMapping;
	}

	public ElasticSearchMappingResult getElasticSearchMapping(
			ElasticSearchMappingContext context) {
		ElasticSearchMappingResult res = new ElasticSearchMappingResult();
		String topic = (String)context.getAttribute(MappingContextAttributes.ATTRIB_TOPIC);
		res.setIndexName(topicToIndexMapping.get(topic));
		String key = (String)context.getAttribute(MappingContextAttributes.ATTRIB_KEY);
		int lastSepInd = key.lastIndexOf('-');
		Map<String, Object> attribMap = (Map<String, Object>)context.getData();
		res.setTypeName(attribMap.get("type").toString() + "event");
		res.setDocumentId(key.substring(lastSepInd+1));
		res.setMappingFileName("mapping.json");
		return res;
	}

	

}
