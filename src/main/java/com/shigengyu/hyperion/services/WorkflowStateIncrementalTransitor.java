package com.shigengyu.hyperion.services;

import org.springframework.stereotype.Component;

import com.shigengyu.hyperion.core.WorkflowInstance;
import com.shigengyu.hyperion.core.WorkflowStateSet;

@Component("incrementalStateTransitor")
public class WorkflowStateIncrementalTransitor implements WorkflowStateTransitor {

	@Override
	public void transit(WorkflowInstance workflowInstance, WorkflowStateSet fromStates, WorkflowStateSet toStates) {
		workflowInstance.setWorkflowStateSet(workflowInstance.getWorkflowStateSet());
	}
}
