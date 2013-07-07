package com.shigengyu.hyperion.scenarios.simple;

import com.shigengyu.hyperion.core.Transition;
import com.shigengyu.hyperion.core.Workflow;
import com.shigengyu.hyperion.core.WorkflowDefinition;
import com.shigengyu.hyperion.core.WorkflowProcess;

@Workflow(id = "bfc0c860-e52e-421e-95b6-1fbd5b9d710e", initialState = InitializedState.class)
public class SimpleWorkflow extends WorkflowDefinition {

	@Transition
	public void complete(final WorkflowProcess workflowProcess) {
	}

	@Transition
	public void start(final WorkflowProcess workflowProcess) {
	}
}
