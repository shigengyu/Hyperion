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
package com.shigengyu.hyperion.services;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Maps;
import com.shigengyu.hyperion.cache.WorkflowTransitionCache;
import com.shigengyu.hyperion.core.AutoTransitionRecursionLimitExceededException;
import com.shigengyu.hyperion.core.StateTransitionStyle;
import com.shigengyu.hyperion.core.TransitionExecutionResult;
import com.shigengyu.hyperion.core.WorkflowDefinition;
import com.shigengyu.hyperion.core.WorkflowExecution;
import com.shigengyu.hyperion.core.WorkflowExecutionException;
import com.shigengyu.hyperion.core.WorkflowInstance;
import com.shigengyu.hyperion.core.WorkflowStateSet;
import com.shigengyu.hyperion.core.WorkflowTransition;
import com.shigengyu.hyperion.core.WorkflowTransitionSet;
import com.shigengyu.hyperion.dao.WorkflowExecutionDao;
import com.shigengyu.hyperion.dao.WorkflowInstanceDao;

@Service
public class WorkflowExecutionServiceImpl implements WorkflowExecutionService {

	private static class AutoTransitionExecutionContext {

		private final Map<WorkflowTransition, Integer> map = Maps.newHashMap();

		int getExecutionCount(WorkflowTransition workflowTransition) {
			if (map.containsKey(workflowTransition)) {
				return map.get(workflowTransition);
			}
			else {
				return 0;
			}
		}

		int increment(WorkflowTransition workflowTransition) {
			if (!map.containsKey(workflowTransition)) {
				map.put(workflowTransition, 1);
			}
			else {
				map.put(workflowTransition, map.get(workflowTransition) + 1);
			}
			return map.get(workflowTransition);
		}
	}

	@Resource(name = "incrementalStateTransitor")
	private WorkflowStateTransitor incrementalStateTransitor;

	@Resource(name = "replaceStateTransitor")
	private WorkflowStateTransitor replaceStateTransitor;

	@Resource
	private WorkflowExecutionDao workflowExecutionDao;

	@Resource
	private WorkflowInstanceDao workflowInstanceDao;

	@Override
	public TransitionExecutionResult execute(WorkflowInstance workflowInstance, String transitionName) {
		WorkflowDefinition workflowDefinition = workflowInstance.getWorkflowDefinition();
		WorkflowTransitionSet transitions = WorkflowTransitionCache.getInstance().get(workflowDefinition,
				transitionName);

		if (transitions.isEmpty()) {
			throw new WorkflowExecutionException("Transition [{}] not found in workflow definition [{}]",
					transitionName, workflowInstance.getWorkflowDefinition().getName());
		}
		else if (transitions.size() > 1) {
			throw new WorkflowExecutionException(
					"Multiple transitions with name [{}] found in workflow definition [{}]", transitionName,
					workflowInstance.getWorkflowDefinition().getName());
		}

		return execute(workflowInstance, transitions.get(0));
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public TransitionExecutionResult execute(final WorkflowInstance workflowInstance,
			final WorkflowTransition transition) {

		final TransitionExecutionResult executionResult = new TransitionExecutionResult();
		return execute(workflowInstance, transition, executionResult);
	}

	private TransitionExecutionResult execute(final WorkflowInstance workflowInstance,
			final WorkflowTransition transition, final TransitionExecutionResult transitionExecutionResult) {

		if (transition == null) {
			throw new WorkflowExecutionException("Transition cannot be null");
		}

		WorkflowInstance backupWorkflowInstance = workflowInstance.clone();
		try {
			WorkflowStateSet fromStates = workflowInstance.getWorkflowStateSet();
			WorkflowStateSet toStates = null;
			if (transition.isDynamic()) {
				// Set the to states based on return value of dynamic transition
				toStates = transition.invoke(workflowInstance);
			}
			else {
				transition.invoke(workflowInstance);
				// Set the to states as the state defined in the static transition
				toStates = transition.getToStates();
			}

			// Transit states based on transition style
			if (transition.getStateTransitionStyle() == StateTransitionStyle.INCREMENTAL) {
				incrementalStateTransitor.transit(workflowInstance, fromStates, toStates);
			}
			else {
				replaceStateTransitor.transit(workflowInstance, fromStates, toStates);
			}

			// Save workflow execution into database
			WorkflowExecution workflowExecution = new WorkflowExecution(workflowInstance, transition.getName());
			workflowExecutionDao.saveOrUpdate(workflowExecution.toEntity());

			if (!transition.isAuto()) {
				// Invoke available auto transitions to stabilize the workflow instance
				stabilize(workflowInstance, transitionExecutionResult, new AutoTransitionExecutionContext());
			}

			// Save the workflow instance in database
			workflowInstanceDao.saveOrUpdate(workflowInstance.toEntity());
		}
		catch (WorkflowExecutionException e) {
			workflowInstance.restoreFrom(backupWorkflowInstance);

			// Throwing this exception will trigger database transaction rollback
			throw e;
		}
		catch (Exception e) {
			workflowInstance.restoreFrom(backupWorkflowInstance);

			// Throwing this exception will trigger database transaction rollback
			throw new WorkflowExecutionException(e);
		}

		return transitionExecutionResult;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public TransitionExecutionResult stabilize(final WorkflowInstance workflowInstance) {
		final TransitionExecutionResult transitionExecutionResult = new TransitionExecutionResult();
		stabilize(workflowInstance, transitionExecutionResult, new AutoTransitionExecutionContext());
		return transitionExecutionResult;
	}

	private void stabilize(final WorkflowInstance workflowInstance,
			final TransitionExecutionResult transitionExecutionResult,
			AutoTransitionExecutionContext autoTransitionExecutionContext) {
		WorkflowTransitionSet autoWorkflowTransitions = WorkflowTransitionCache.getInstance()
				.get(workflowInstance.getWorkflowDefinition(), workflowInstance.getWorkflowStateSet())
				.getAutoTransitions();

		if (autoWorkflowTransitions.size() > 1) {
			throw new WorkflowExecutionException(
					"Multiple auto transitions found. Workflow definition = [{}], States = [{}], Auto transitions = [{}]",
					workflowInstance.getWorkflowDefinition(), workflowInstance.getWorkflowStateSet(),
					autoWorkflowTransitions);
		}

		if (autoWorkflowTransitions.size() == 1) {
			WorkflowTransition autoWorkflowTransition = autoWorkflowTransitions.get(0);

			int executionCount = autoTransitionExecutionContext.getExecutionCount(autoWorkflowTransition);
			if (executionCount >= autoWorkflowTransition.getMaxEntry() || executionCount > 0
					&& !autoWorkflowTransition.isMultiEntry()) {
				throw new AutoTransitionRecursionLimitExceededException(workflowInstance, autoWorkflowTransition);
			}

			// Execute the auto transition
			execute(workflowInstance, autoWorkflowTransition, transitionExecutionResult);

			// Increment auto transition execution count
			autoTransitionExecutionContext.increment(autoWorkflowTransition);

			stabilize(workflowInstance, transitionExecutionResult, autoTransitionExecutionContext);
		}
	}
}
