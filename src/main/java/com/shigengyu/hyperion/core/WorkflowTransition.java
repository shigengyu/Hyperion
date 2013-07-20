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

public class WorkflowTransition {

	private final boolean auto;
	private final TransitionConditionSet conditions;
	private boolean dynamic;
	private WorkflowStateSet fromStates;
	private boolean hidden;
	private int maxEntry;
	private Method method;
	private boolean multiEntry;
	private String name;
	private StateTransitionStyle stateTransitionStyle;

	private WorkflowStateSet toStates;

	public WorkflowTransition(Method method, Transition transition, TransitionShared transitionShared) {
		auto = transition.override() ? transition.auto() : transitionShared.auto();
		conditions = TransitionConditionSet.from(transition.override() ? transition.conditions() : transitionShared
				.conditions());
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
}
