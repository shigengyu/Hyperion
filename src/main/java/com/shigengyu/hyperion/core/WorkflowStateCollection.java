package com.shigengyu.hyperion.core;

import java.util.ArrayList;
import java.util.Collection;

public class WorkflowStateCollection {

	private final Collection<WorkflowState> workflowStates;

	public WorkflowStateCollection() {
		workflowStates = new ArrayList<>();
	}

	protected WorkflowStateCollection(
			final Collection<WorkflowState> workflowStates) {
		this.workflowStates = workflowStates;
	}
}
