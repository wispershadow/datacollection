package org.wispersd.core.datacollect.web.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.wispersd.core.datacollect.searchapi.OMSWorkflowEventConstants;
import org.wispersd.core.datacollect.searchapi.OMSWorkflowEventSearchService;
import org.wispersd.core.datacollect.searchapi.OMSWorkflowTypes;
import org.wispersd.core.datacollect.searchapi.dto.SearchWorkflowByTimeRangeCond;
import org.wispersd.core.datacollect.searchapi.dto.SearchWorkflowByTimeRangeResp;
import org.wispersd.core.datacollect.searchapi.dto.SearchWorkflowNodeByTimeRangeCond;
import org.wispersd.core.datacollect.searchapi.dto.SearchWorkflowNodeByTimeRangeResp;
import org.wispersd.core.datacollect.searchapi.dto.StatIntervalUnits;

@Controller
@RequestMapping("/omsdashboard")
public class OMSWorkflowDashboardController {
	private OMSWorkflowEventSearchService omsWorkflowEventSearchService;
	
	
	@Autowired
	@Qualifier("defaultOMSWorkflowEventSearchService")
	public void setOmsWorkflowEventSearchService(OMSWorkflowEventSearchService omsWorkflowEventSearchService) {
		this.omsWorkflowEventSearchService = omsWorkflowEventSearchService;
	}



	@RequestMapping(value="workflowactions", method=RequestMethod.GET)
	public @ResponseBody Map<String, Set<String>> getActionsByWorkflows() {
		List<String> workflows = new ArrayList<String>();
		workflows.add(OMSWorkflowTypes.ORDER_PROCESS.toString());
		workflows.add(OMSWorkflowTypes.CONSIGNMENT_PROCESS.toString());
		workflows.add(OMSWorkflowTypes.RETURN_PROCESS.toString());
		return omsWorkflowEventSearchService.getActionsByWorkflows(workflows);
	}
	
	@RequestMapping(value="searchworkflow", method=RequestMethod.GET)
	public @ResponseBody SearchWorkflowByTimeRangeResp searchWorkflowByTimeRange(@RequestParam(value="workflowName", required=true) String workflowName, 
																				 @RequestParam(value="startTime", required=true) String startTime, 
																				 @RequestParam(value="endTime", required=true) String endTime) {
		SearchWorkflowByTimeRangeCond cond = new SearchWorkflowByTimeRangeCond();
		cond.setIndex(OMSWorkflowEventConstants.OMSWORKFLOW_INDEX_NAME);
		cond.setWorkflowName(workflowName);
		cond.setStartTime(startTime);
		cond.setEndTime(endTime);
		return omsWorkflowEventSearchService.searchWorkflowByTimeRange(cond);
	}
	
	@RequestMapping(value="searchworkflownode", method=RequestMethod.GET)
	public @ResponseBody SearchWorkflowNodeByTimeRangeResp searchWorkflowNodeByTimeRange(@RequestParam(value="nodeActionName", required=true) String nodeActionName, 
																		   				 @RequestParam(value="startTime", required=true) String startTime, 
																		   				 @RequestParam(value="endTime", required=true) String endTime, 
																		   				 @RequestParam(value="statInterval") String statInterval,
																		   				 @RequestParam(value="statUnit") String statUnit,
																		   				 @RequestParam(value="topN") String topN) {
		SearchWorkflowNodeByTimeRangeCond cond = new SearchWorkflowNodeByTimeRangeCond();
		cond.setIndex(OMSWorkflowEventConstants.OMSWORKFLOW_INDEX_NAME);
		cond.setNodeActionName(nodeActionName);
		cond.setStartTime(startTime);
		cond.setEndTime(endTime);
		if (!StringUtils.isEmpty(statInterval)) {
			cond.setStatIntervalValue(Integer.parseInt(statInterval));
		}
		else {
			cond.setStatIntervalValue(1);
		}
		if (!StringUtils.isEmpty(statUnit)) {
			cond.setStatIntervalUnit(StatIntervalUnits.fromVal(statUnit));
		}
		else {
			cond.setStatIntervalUnit(StatIntervalUnits.HOUR);
		}
		
		
		if (!StringUtils.isEmpty(topN)) {
			cond.setTopSlow(Integer.parseInt(topN));
		}
		else {
			cond.setTopSlow(5);
		}
		
		return omsWorkflowEventSearchService.searchWorkflowNodeByTimeRange(cond);
		
	}

}
