package com.shigengyu.hyperion.services;

import com.shigengyu.hyperion.core.WorkflowInstance;
import com.shigengyu.hyperion.core.WorkflowStateSet;

public interface WorkflowStateTransitor {

	void transit(WorkflowInstance workflowInstance, WorkflowStateSet fromStates, WorkflowStateSet toStates);
}
