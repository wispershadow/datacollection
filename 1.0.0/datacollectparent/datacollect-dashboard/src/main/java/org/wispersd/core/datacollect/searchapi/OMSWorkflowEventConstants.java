package org.wispersd.core.datacollect.searchapi;

public interface OMSWorkflowEventConstants {
	public static final String DATETIME_FORMAT = "yyyy/MM/dd HH:mm:ss";
	public static final String OMSWORKFLOW_INDEX_NAME = "omsworkflow";
	public static final String TYPE_NAME_NODE = "nodeevent";
	public static final String TYPE_NAME_PROCESS = "processevent";
	public static final String ATTRIB_PROCESSNAME = "processName";
	public static final String ATTRIB_PROCESSCODE = "processCode";
	public static final String ATTRIB_ACTION = "action";
	public static final String ATTRIB_STARTTIME = "startTime";
	public static final String ATTRIB_ENDTIME = "endTime";
	public static final String ATTRIB_EXECUTIONTIME = "executionTime";
	public static final String ATTRIB_STATUS = "status";
	
	public static final String AGGREGATION_STATSBYNODETYPE = "statsByNodeType";
	public static final String AGGREGATION_EXECUTIONTIME = "executionTime";
	public static final String AGGREGATION_COUNTBYPROCSTATE = "countByProcState";
	public static final String AGGREGATION_STATSBYACTIONTIME = "statsByActionTime";
	public static final String AGGREGATION_WORKFLOWS = "workflows";
	public static final String AGGREGATION_NODES = "nodes";
}
