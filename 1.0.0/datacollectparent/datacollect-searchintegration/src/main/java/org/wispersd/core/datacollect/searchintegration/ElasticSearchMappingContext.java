package org.wispersd.core.datacollect.searchintegration;

import java.util.HashMap;
import java.util.Map;

public class ElasticSearchMappingContext<T> {
	private T data;
	private final Map<String, Object> attributes = new HashMap<String, Object>();
	
	public ElasticSearchMappingContext(T data) {
		this.data = data;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	public Object getAttribute(String key) {
		return attributes.get(key);
	}
	
	public void addAttribute(String key, Object attributeValue) {
		attributes.put(key, attributeValue);
	}
}
