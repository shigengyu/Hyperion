package com.shigengyu.hyperion.scenarios.simple;

import com.shigengyu.hyperion.core.Transition;
import com.shigengyu.hyperion.core.Workflow;
import com.shigengyu.hyperion.core.WorkflowDefinition;
import com.shigengyu.hyperion.core.WorkflowInstance;
import com.shigengyu.hyperion.core.WorkflowStateSet;

@Workflow(id = "793c4e61-c81a-44ed-847c-d6a8e0a08e35", initialState = States.Initialized.class)
public class RecursiveTransitionWorkflow extends WorkflowDefinition {

	@Transition(fromStates = States.WorkInProgress.class, auto = true, multiEntry = true, maxEntry = 5)
	public WorkflowStateSet recursive(final WorkflowInstance workflowInstance) {

		Integer count = workflowInstance.getParameter("Count");
		if (count > 0) {
			workflowInstance.setParameter("Count", count - 1);
			return WorkflowStateSet.from(States.WorkInProgress.class);
		}
		else {
			return WorkflowStateSet.from(States.WorkCompleted.class);
		}
	}

	@Transition(fromStates = States.Endless.class, auto = true)
	public void recursiveSingleEntry(final WorkflowInstance workflowInstance) {
	}

	@Transition(fromStates = States.Initialized.class, toStates = States.WorkInProgress.class)
	public void start(final WorkflowInstance workflowInstance) {
		workflowInstance.setParameter("Count", 3);
	}

	@Transition(fromStates = States.Initialized.class, toStates = States.Endless.class)
	public void startEndless(final WorkflowInstance workflowInstance) {
	}

	@Transition(name = "startExceedLimit",
				fromStates = States.Initialized.class,
				toStates = States.WorkInProgress.class)
	public void startExceedLimit(final WorkflowInstance workflowInstance) {
		workflowInstance.setParameter("Count", 10);
	}
}
