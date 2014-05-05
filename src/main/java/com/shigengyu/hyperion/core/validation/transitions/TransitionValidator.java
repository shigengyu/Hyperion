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
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.shigengyu.common.springextension.SpringBeanSet;
import com.shigengyu.hyperion.core.WorkflowStateSet;
import com.shigengyu.hyperion.core.WorkflowTransition;

/**
 *
 * Transition validators are initialized in Spring as they are mandatory for any transition.
 *
 * @author Gengyu (Univer) Shi
 *
 */
@Service
public class TransitionValidator {

	@Component
	public static class TransitionValidationPolicies extends SpringBeanSet<TransitionValidationPolicy> {

		protected TransitionValidationPolicies() {
			super(TransitionValidationPolicy.class);
		}
	}

	@Resource
	private TransitionValidationPolicies transitionViolationBeanSet;

	public List<TransitionValidationPolicy> getValidators() {
		return transitionViolationBeanSet.getBeans();
	}

	public void validate(WorkflowStateSet currentWorkflowStates, Collection<WorkflowTransition> workflowTransitions) {

		for (TransitionValidationPolicy violation : transitionViolationBeanSet.getBeans()) {
			violation.validate(currentWorkflowStates, workflowTransitions);
		}
	}
}
