package com.shigengyu.hyperion.core;

public class AutoTransitionRecursionLimitExceededException extends WorkflowExecutionException {

	private static final long serialVersionUID = 1L;

	public AutoTransitionRecursionLimitExceededException(WorkflowInstance workflowInstance,
			WorkflowTransition workflowTransition) {
		super("Auto transition recursion limit exceeded. Transition = " + workflowTransition + ", Workflow Instance = "
				+ workflowInstance + ", Max Entry = " + workflowTransition.getMaxEntry());
	}
}