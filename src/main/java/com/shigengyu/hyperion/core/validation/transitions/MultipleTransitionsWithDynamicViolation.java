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
package com.shigengyu.hyperion.core.validation.transitions;

import java.util.Collection;

import org.springframework.stereotype.Component;

import com.shigengyu.hyperion.core.TransitionDefinitionException;
import com.shigengyu.hyperion.core.WorkflowStateSet;
import com.shigengyu.hyperion.core.WorkflowTransition;

@Component
public class MultipleTransitionsWithDynamicViolation implements TransitionValidationPolicy {

	@Override
	public void validate(WorkflowStateSet currentWorkflowStates, Collection<WorkflowTransition> workflowTransitions) {

		if (workflowTransitions.size() > 1) {
			for (WorkflowTransition workflowTransition : workflowTransitions) {
				if (workflowTransition.isDynamic()) {
					throw new TransitionDefinitionException(
							"Dynamic transition cannot be invoked together with other transitions");
				}
			}
		}
	}
}
