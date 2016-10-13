package org.wispersd.core.datacollect.searchapi;

public enum OMSWorkflowTypes {
	ORDER_PROCESS("order-process"),
	CONSIGNMENT_PROCESS("consignment-process"),
	RETURN_PROCESS("return-process");
	
	private String type;
	
	OMSWorkflowTypes(String type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return type;
	}
}
