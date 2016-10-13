package org.wispersd.core.datacollect.searchapi.dto;

public class ElasticSearchCondition {
	private String index;
	private String typeName;

	public String getIndex() {
		return index;
	}

	public void setIndex(String index) {
		this.index = index;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
}
