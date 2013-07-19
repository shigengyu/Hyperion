package com.shigengyu.hyperion.core;

import java.util.List;

import com.google.common.collect.ImmutableList;

public class WorkflowTransitionCollection {

	public static WorkflowTransitionCollection copyOf(List<WorkflowTransition> transitions) {
		return new WorkflowTransitionCollection(ImmutableList.copyOf(transitions));
	}

	private final ImmutableList<WorkflowTransition> transitions;

	private WorkflowTransitionCollection(ImmutableList<WorkflowTransition> transitions) {
		this.transitions = transitions;
	}

	public final WorkflowTransitionCollection filter(String transitionName) {
		return new WorkflowTransitionCollection(transitions);
	}

	public final WorkflowTransitionCollection filter(WorkflowStateCollection fromStates) {

		return new WorkflowTransitionCollection(transitions);
	}
}
