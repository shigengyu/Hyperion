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

import net.jcip.annotations.ThreadSafe;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Predicate;
import com.google.common.collect.Maps;
import com.shigengyu.hyperion.cache.WorkflowTransitionCache;
import com.shigengyu.hyperion.core.AutoTransitionRecursionLimitExceededException;
import com.shigengyu.hyperion.core.StateTransitionStyle;
import com.shigengyu.hyperion.core.TransitionCompensation;
import com.shigengyu.hyperion.core.TransitionCompensationResult;
import com.shigengyu.hyperion.core.TransitionCompensationResult.Status;
import com.shigengyu.hyperion.core.TransitionCompensator;
import com.shigengyu.hyperion.core.TransitionCondition;
import com.shigengyu.hyperion.core.TransitionConditionValidationResult;
import com.shigengyu.hyperion.core.TransitionExecution;
import com.shigengyu.hyperion.core.TransitionExecutionLog;
import com.shigengyu.hyperion.core.TransitionExecutionResult;
import com.shigengyu.hyperion.core.TransitionInvocationException;
import com.shigengyu.hyperion.core.WorkflowBusinessViolationException;
import com.shigengyu.hyperion.core.WorkflowDefinition;
import com.shigengyu.hyperion.core.WorkflowExecutionException;
import com.shigengyu.hyperion.core.WorkflowInstance;
import com.shigengyu.hyperion.core.WorkflowState;
import com.shigengyu.hyperion.core.WorkflowStateSet;
import com.shigengyu.hyperion.core.WorkflowTransition;
import com.shigengyu.hyperion.core.WorkflowTransitionSet;
import com.shigengyu.hyperion.dao.WorkflowExecutionDao;

@ThreadSafe
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

	private static final TransitionCompensationResult compensate(final WorkflowInstance workflowInstance,
			final WorkflowTransition transition, final RuntimeException e) {

		TransitionCompensationResult finalResult = TransitionCompensationResult.notCompensated();

		for (TransitionCompensation compensation : transition.getTransitionCompensations()) {
			if (!compensation.getException().isAssignableFrom(e.getClass())) {
				continue;
			}

			TransitionCompensator compensator = compensation.getTransitionCompensator();
			if (compensator.canHandle(e)) {
				TransitionCompensationResult result = compensator.compensate(workflowInstance);
				if (result.getStatus() == Status.FAILED) {
					return result;
				}
				else {
					finalResult = result;
				}
			}
		}

		return finalResult;
	}

	@Resource(name = "incrementalStateTransitor")
	private WorkflowStateTransitor incrementalStateTransitor;

	@Resource(name = "replaceStateTransitor")
	private WorkflowStateTransitor replaceStateTransitor;

	@Resource
	private WorkflowExecutionDao workflowExecutionDao;

	@Resource
	private WorkflowPersistenceService workflowPersistenceService;

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

		// Apply transition condition validation
		TransitionConditionValidationResult transitionConditionValidationResult = TransitionConditionValidationResult
				.create();
		for (TransitionCondition transitionCondition : transition.getConditions()) {
			transitionConditionValidationResult.merge(transitionCondition.apply(transition, workflowInstance));
			if (!transitionConditionValidationResult.isPassed()) {
				for (String reason : transitionConditionValidationResult.getReasons()) {
					transitionExecutionResult.addLog(TransitionExecutionLog.info(reason));
				}
				return TransitionExecutionResult.notExecuted();
			}
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
			if (transition.getStateTransitionStyle() == StateTransitionStyle.REMOVE_AND_ADD) {
				incrementalStateTransitor.transit(workflowInstance, fromStates, toStates);
			}
			else {
				replaceStateTransitor.transit(workflowInstance, fromStates, toStates);
			}

			// Save workflow execution into database
			TransitionExecution workflowExecution = new TransitionExecution(workflowInstance, transition.getName());
			workflowExecutionDao.saveOrUpdate(workflowExecution.toEntity());

			// Stabilize the workflow instance by executing available auto transitions based on current workflow states
			if (!transition.isAuto()) {
				// Invoke available auto transitions to stabilize the workflow instance
				stabilize(workflowInstance, transitionExecutionResult, new AutoTransitionExecutionContext());
			}

			final WorkflowStateSet intermediateStates = workflowInstance.getWorkflowStateSet().filter(
					new Predicate<WorkflowState>() {

						@Override
						public boolean apply(WorkflowState input) {
							return input.isIntermediate();
						}
					});

			// Detect intermediate states
			if (intermediateStates.size() > 0) {
				throw new WorkflowExecutionException(
						"Intermediate states detected after stablizing workflow instance [{}] of definition [{}]. Intermediate states = [{}]",
						workflowInstance.getWorkflowInstanceId(), workflowInstance.getWorkflowDefinition().getName(),
						intermediateStates);
			}

			// Save the workflow instance in database
			workflowPersistenceService.persistWorkflowInstance(workflowInstance);
		}
		catch (WorkflowBusinessViolationException | TransitionInvocationException e) {
			TransitionCompensationResult compensationResult = compensate(workflowInstance, transition, e);

			// Restore the workflow instance if compensation failed
			if (!compensationResult.isSuccess()) {
				workflowInstance.restoreFrom(backupWorkflowInstance);
				throw e;
			}
		}
		catch (WorkflowExecutionException e) {
			workflowInstance.restoreFrom(backupWorkflowInstance);

			// Rethrow the WorkflowExecutionException after rolling back workflow instance
			throw e;
		}
		catch (Exception e) {
			// Wrap all other exceptions into WorkflowExecutionException
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
			final AutoTransitionExecutionContext autoTransitionExecutionContext) {

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
