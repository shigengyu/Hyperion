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

package com.shigengyu.hyperion.core;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.apache.commons.lang.StringUtils;

public class WorkflowTransition {

	private final boolean auto;
	private final TransitionConditionSet conditions;
	private final boolean dynamic;
	private final WorkflowStateSet fromStates;
	private final boolean hidden;
	private final int maxEntry;
	private final Method method;
	private final boolean multiEntry;
	private String name;
	private final StateTransitionStyle stateTransitionStyle;

	private final WorkflowStateSet toStates;

	public WorkflowTransition(Method method, Transition transition, TransitionShared transitionShared) {
		this.method = method;
		name = transition.override() ? transition.name() : transitionShared.name();
		if (StringUtils.isEmpty(name)) {
			name = method.getName();
		}

		fromStates = WorkflowStateSet.from(transition.override() ? transition.fromStates() : transitionShared
				.fromStates());
		toStates = WorkflowStateSet.from(transition.override() ? transition.toStates() : transitionShared.toStates());

		conditions = TransitionConditionSet.from(transition.override() ? transition.conditions() : transitionShared
				.conditions());

		if (method.getReturnType() == WorkflowStateSet.class) {
			dynamic = true;
		}
		else if (method.getReturnType() == Void.TYPE) {
			dynamic = false;
		}
		else {
			throw new WorkflowTransitionException(
					"Transition method return type cannot be other types other than [{}]",
					WorkflowStateSet.class.getName());
		}

		auto = transition.override() ? transition.auto() : transitionShared.auto();
		hidden = transition.override() ? transition.hidden() : transitionShared.hidden();
		multiEntry = transition.override() ? transition.multiEntry() : transitionShared.multiEntry();
		maxEntry = transition.override() ? transition.maxEntry() : transitionShared.maxEntry();
		stateTransitionStyle = transition.override() ? transition.stateTransitionStyle() : transitionShared
				.stateTransitionStyle();
	}

	public TransitionConditionSet getConditions() {
		return conditions;
	}

	public WorkflowStateSet getFromStates() {
		return fromStates;
	}

	public int getMaxEntry() {
		return maxEntry;
	}

	public String getName() {
		return name;
	}

	public StateTransitionStyle getStateTransitionStyle() {
		return stateTransitionStyle;
	}

	public WorkflowStateSet getToStates() {
		return toStates;
	}

	public WorkflowStateSet invoke(WorkflowInstance workflowInstance) {
		try {
			WorkflowStateSet workflowStateSet = (WorkflowStateSet) method.invoke(
					workflowInstance.getWorkflowDefinition(), workflowInstance);
			return workflowStateSet;
		}
		catch (InvocationTargetException e) {
			Throwable t = e;
			do {
				t = t.getCause();
			}
			while (t instanceof InvocationTargetException);

			throw new WorkflowExecutionException(t);
		}
		catch (Exception e) {
			throw new WorkflowTransitionException(e);
		}
	}

	public boolean isAuto() {
		return auto;
	}

	public boolean isDynamic() {
		return dynamic;
	}

	public boolean isHidden() {
		return hidden;
	}

	public boolean isMultiEntry() {
		return multiEntry;
	}
}
