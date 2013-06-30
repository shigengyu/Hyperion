package com.shigengyu.hyperion.core;

import java.util.Collection;

import com.google.common.collect.Lists;

public class WorkflowStateCollection {

	private final Collection<WorkflowState> workflowStates;

	public WorkflowStateCollection() {
		workflowStates = Lists.newArrayList();
	}

	protected WorkflowStateCollection(
			final Collection<WorkflowState> workflowStates) {
		this.workflowStates = workflowStates;
	}
}
