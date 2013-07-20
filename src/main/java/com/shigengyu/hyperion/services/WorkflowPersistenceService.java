package com.shigengyu.hyperion.services;

import com.shigengyu.hyperion.core.WorkflowDefinition;
import com.shigengyu.hyperion.core.WorkflowInstance;

public interface WorkflowPersistenceService {

	public WorkflowInstance createWorkflowInstance(WorkflowDefinition workflowDefinition);
}
