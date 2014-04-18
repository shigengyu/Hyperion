/*******************************************************************************
 * Copyright 2013-2014 Gengyu Shi
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
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

		Integer count = workflowInstance.getParameter(Integer.class, "Count");
		if (count > 0) {
			workflowInstance.setParameter("Count", count - 1);
			return WorkflowStateSet.from(States.WorkInProgress.class);
		}
		else {
			return WorkflowStateSet.from(States.WorkCompleted.class);
		}
	}

	@Transition(fromStates = States.Endless.class, auto = true)
	public void recursiveSingleEntry() {
	}

	@Transition(fromStates = States.Initialized.class, toStates = States.WorkInProgress.class)
	public void start(final WorkflowInstance workflowInstance) {
		workflowInstance.setParameter("Count", 3);
	}

	@Transition(fromStates = States.Initialized.class, toStates = States.Endless.class)
	public void startEndless() {
	}

	@Transition(name = "startExceedLimit",
			fromStates = States.Initialized.class,
			toStates = States.WorkInProgress.class)
	public void startExceedLimit(final WorkflowInstance workflowInstance) {
		workflowInstance.setParameter("Count", 10);
	}
}
