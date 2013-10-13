package com.shigengyu.hyperion.scenarios.simple;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.shigengyu.hyperion.core.Transition;
import com.shigengyu.hyperion.core.Workflow;
import com.shigengyu.hyperion.core.WorkflowDefinition;
import com.shigengyu.hyperion.core.WorkflowInstance;

@Workflow(id = "deba1df0-4092-418a-bbc7-99563c237d6d", initialState = States.Initialized.class)
public class AutoTransitionWorkflow extends WorkflowDefinition {

	private static final Logger LOGGER = LoggerFactory.getLogger(AutoTransitionWorkflow.class);

	/**
	 * @param workflowInstance
	 */
	@Transition(fromStates = States.WorkInProgress.class, toStates = States.WorkCompleted.class, auto = true)
	public void autoComplete(final WorkflowInstance workflowInstance) {
		LOGGER.info("auto complete");
	}

	/**
	 * @param workflowInstance
	 */
	@Transition(fromStates = States.Initialized.class, toStates = States.WorkInProgress.class, auto = true)
	public void autoStart(final WorkflowInstance workflowInstance) {
		LOGGER.info("auto start");
	}
}
