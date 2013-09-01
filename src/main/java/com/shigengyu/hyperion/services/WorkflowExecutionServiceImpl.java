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

package com.shigengyu.hyperion.services;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.shigengyu.hyperion.cache.WorkflowTransitionCache;
import com.shigengyu.hyperion.core.StateTransitionStyle;
import com.shigengyu.hyperion.core.TransitionExecutionResult;
import com.shigengyu.hyperion.core.WorkflowDefinition;
import com.shigengyu.hyperion.core.WorkflowExecutionException;
import com.shigengyu.hyperion.core.WorkflowInstance;
import com.shigengyu.hyperion.core.WorkflowStateSet;
import com.shigengyu.hyperion.core.WorkflowTransition;
import com.shigengyu.hyperion.core.WorkflowTransitionSet;

@Service
public class WorkflowExecutionServiceImpl implements WorkflowExecutionService {

	@Resource(name = "incrementalStateTransitor")
	private WorkflowStateTransitor incrementalStateTransitor;

	@Resource(name = "replaceStateTransitor")
	private WorkflowStateTransitor replaceStateTransitor;

	@Override
	public TransitionExecutionResult autoTransit(WorkflowInstance workflowInstance) {
		return TransitionExecutionResult.success();
	}

	@Override
	public TransitionExecutionResult execute(WorkflowInstance workflowInstance, String transitionName) {
		WorkflowDefinition workflowDefinition = workflowInstance.getWorkflowDefinition();
		WorkflowTransitionSet transitions = WorkflowTransitionCache.getInstance().get(workflowDefinition,
				transitionName);

		if (transitions.isEmpty()) {
			throw new WorkflowExecutionException("Transition [{}] not found in workflow definition [{}]",
					transitionName, workflowInstance.getWorkflowDefinition().getName());
		}
		else if (transitions.size() > 0) {
			throw new WorkflowExecutionException(
					"Multiple transitions with name [{}] found in workflow definition [{}]", transitionName,
					workflowInstance.getWorkflowDefinition().getName());
		}

		return execute(workflowInstance, transitions.first());
	}

	@Override
	public TransitionExecutionResult execute(final WorkflowInstance workflowInstance,
			final WorkflowTransition transition) {

		final TransitionExecutionResult executionResult = new TransitionExecutionResult();

		WorkflowInstance backupWorkflowInstance = workflowInstance.clone();
		try {
			WorkflowStateSet fromStates = workflowInstance.getWorkflowStateSet();
			WorkflowStateSet toStates = null;
			if (transition.isDynamic()) {
				toStates = transition.invoke(workflowInstance);
			}
			else {
				transition.invoke(workflowInstance);
				toStates = transition.getToStates();
			}

			if (transition.getStateTransitionStyle() == StateTransitionStyle.INCREMENTAL) {
				incrementalStateTransitor.transit(workflowInstance, fromStates, toStates);
			}
			else {
				replaceStateTransitor.transit(workflowInstance, fromStates, toStates);
			}
		}
		catch (Exception e) {
			workflowInstance.restoreFrom(backupWorkflowInstance);
			throw new WorkflowExecutionException(e);
		}

		return executionResult;
	}

	private void stablize(final WorkflowInstance workflowInstance) {
		WorkflowTransitionSet autoWorkflowTransitions = WorkflowTransitionCache.getInstance()
				.get(workflowInstance.getWorkflowDefinition(), workflowInstance.getWorkflowStateSet())
				.getAutoTransitions();

		if (autoWorkflowTransitions.size() > 1) {
			throw new WorkflowExecutionException(
					"Multiple auto transitions found. Workflow definition = [{}], States = [{}], Auto transitions = [{}]",
					workflowInstance.getWorkflowDefinition(), workflowInstance.getWorkflowStateSet(),
					autoWorkflowTransitions);
		}

		execute(workflowInstance, autoWorkflowTransitions.first());
	}
}
