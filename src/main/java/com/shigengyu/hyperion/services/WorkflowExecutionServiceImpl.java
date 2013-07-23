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

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.google.common.collect.ImmutableList;
import com.shigengyu.hyperion.cache.WorkflowTransitionCache;
import com.shigengyu.hyperion.core.StateTransitionStyle;
import com.shigengyu.hyperion.core.TransitionExecutionResult;
import com.shigengyu.hyperion.core.WorkflowDefinition;
import com.shigengyu.hyperion.core.WorkflowInstance;
import com.shigengyu.hyperion.core.WorkflowStateSet;
import com.shigengyu.hyperion.core.WorkflowTransition;

@Service
public class WorkflowExecutionServiceImpl implements WorkflowExecutionService {

	@Resource(name = "incrementalStateTransitor")
	private WorkflowStateTransitor incrementalStateTransitor;

	@Resource(name = "replaceStateTransitor")
	private WorkflowStateTransitor replaceStateTransitor;

	@Override
	public TransitionExecutionResult execute(final WorkflowInstance workflowInstance,
			final List<WorkflowTransition> transitions) {

		final TransitionExecutionResult executionResult = new TransitionExecutionResult();

		WorkflowInstance backupWorkflowInstance = workflowInstance.clone();
		try {
			WorkflowStateSet workflowStateSet = workflowInstance.getWorkflowStateSet();
			for (WorkflowTransition transition : transitions) {
				WorkflowStateSet fromStates = workflowInstance.getWorkflowStateSet();
				WorkflowStateSet toStates = null;
				if (transition.isDynamic()) {
					toStates = transition.invoke(backupWorkflowInstance);
				}
				else {
					transition.invoke(backupWorkflowInstance);
					toStates = transition.getToStates();
				}

				if (transition.getStateTransitionStyle() == StateTransitionStyle.INCREMENTAL) {
					incrementalStateTransitor.transit(workflowInstance, fromStates, toStates);
				}
				else {
					replaceStateTransitor.transit(workflowInstance, fromStates, toStates);
				}
			}
		}
		catch (Exception e) {
			workflowInstance.fill(backupWorkflowInstance);
		}

		return executionResult;
	}

	@Override
	public TransitionExecutionResult execute(WorkflowInstance workflowInstance, String transitionName) {
		WorkflowDefinition workflowDefinition = workflowInstance.getWorkflowDefinition();
		ImmutableList<WorkflowTransition> transitions = WorkflowTransitionCache.getInstance().get(workflowDefinition,
				transitionName);
		return execute(workflowInstance, transitions);
	}
}
