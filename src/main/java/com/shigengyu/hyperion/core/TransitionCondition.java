package com.shigengyu.hyperion.core;

public interface TransitionCondition {

	boolean canPerform(WorkflowTransition workflowTransition, WorkflowInstance workflowInstance);
}
