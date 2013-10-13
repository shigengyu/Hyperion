/*******************************************************************************
 * Copyright 2013 Gengyu Shi
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
