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
	private final String name;
	private final StateTransitionStyle stateTransitionStyle;

	private final WorkflowStateSet toStates;

	public WorkflowTransition(Method method, Transition transition, TransitionShared transitionShared) {
		this.method = method;
		String transitionName = transition.override() ? transition.name() : transitionShared.name();
		if (StringUtils.isEmpty(transitionName)) {
			transitionName = method.getName();
		}
		name = transitionName;

		auto = transition.override() ? transition.auto() : transitionShared.auto();
		hidden = transition.override() ? transition.hidden() : transitionShared.hidden();
		multiEntry = transition.override() ? transition.multiEntry() : transitionShared.multiEntry();
		maxEntry = transition.override() ? transition.maxEntry() : transitionShared.maxEntry();
		stateTransitionStyle = transition.override() ? transition.stateTransitionStyle() : transitionShared
				.stateTransitionStyle();

		fromStates = WorkflowStateSet.from(transition.override() ? transition.fromStates() : transitionShared
				.fromStates());
		WorkflowStateSet specifiedToStates = WorkflowStateSet.from(transition.override() ? transition.toStates()
				: transitionShared.toStates());
		toStates = specifiedToStates.size() > 0 ? specifiedToStates : fromStates;

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
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		WorkflowTransition other = (WorkflowTransition) obj;
		if (auto != other.auto) {
			return false;
		}
		if (conditions == null) {
			if (other.conditions != null) {
				return false;
			}
		}
		else if (!conditions.equals(other.conditions)) {
			return false;
		}
		if (dynamic != other.dynamic) {
			return false;
		}
		if (fromStates == null) {
			if (other.fromStates != null) {
				return false;
			}
		}
		else if (!fromStates.equals(other.fromStates)) {
			return false;
		}
		if (hidden != other.hidden) {
			return false;
		}
		if (maxEntry != other.maxEntry) {
			return false;
		}
		if (method == null) {
			if (other.method != null) {
				return false;
			}
		}
		else if (!method.equals(other.method)) {
			return false;
		}
		if (multiEntry != other.multiEntry) {
			return false;
		}
		if (name == null) {
			if (other.name != null) {
				return false;
			}
		}
		else if (!name.equals(other.name)) {
			return false;
		}
		if (stateTransitionStyle != other.stateTransitionStyle) {
			return false;
		}
		if (toStates == null) {
			if (other.toStates != null) {
				return false;
			}
		}
		else if (!toStates.equals(other.toStates)) {
			return false;
		}
		return true;
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (auto ? 1231 : 1237);
		result = prime * result + (conditions == null ? 0 : conditions.hashCode());
		result = prime * result + (dynamic ? 1231 : 1237);
		result = prime * result + (fromStates == null ? 0 : fromStates.hashCode());
		result = prime * result + (hidden ? 1231 : 1237);
		result = prime * result + maxEntry;
		result = prime * result + (method == null ? 0 : method.hashCode());
		result = prime * result + (multiEntry ? 1231 : 1237);
		result = prime * result + (name == null ? 0 : name.hashCode());
		result = prime * result + (stateTransitionStyle == null ? 0 : stateTransitionStyle.hashCode());
		result = prime * result + (toStates == null ? 0 : toStates.hashCode());
		return result;
	}

	public WorkflowStateSet invoke(WorkflowInstance workflowInstance) {
		try {
			if (method.getParameterTypes().length == 0) {
				return (WorkflowStateSet) method.invoke(workflowInstance.getWorkflowDefinition());
			}
			else if (method.getParameterTypes().length == 1 && method.getParameterTypes()[0] == WorkflowInstance.class) {
				return (WorkflowStateSet) method.invoke(workflowInstance.getWorkflowDefinition(), workflowInstance);
			}
			else {
				throw new WorkflowExecutionException(
						"Workflow transition must take one parameter of type WorkflowInstance or no parameters");
			}
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

	@Override
	public String toString() {
		return name;
	}
}
