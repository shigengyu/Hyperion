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

package com.shigengyu.hyperion.core.validation.transitions;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.shigengyu.hyperion.common.SpringBeanSet;
import com.shigengyu.hyperion.core.WorkflowStateSet;
import com.shigengyu.hyperion.core.WorkflowTransition;

@Service
public class TransitionViolationDetector {

	@Component
	public static class TransitionViolationBeanSet extends SpringBeanSet<TransitionViolation> {

		protected TransitionViolationBeanSet() {
			super(TransitionViolation.class);
		}
	}

	@Resource
	private TransitionViolationBeanSet transitionViolationBeanSet;

	public List<TransitionViolation> getValidators() {
		return transitionViolationBeanSet.getBeans();
	}

	public boolean isValid(WorkflowStateSet currentWorkflowStates, List<WorkflowTransition> workflowTransitions) {

		for (TransitionViolation violation : transitionViolationBeanSet.getBeans()) {
			if (violation.violatesWith(currentWorkflowStates, workflowTransitions)) {
				return false;
			}
		}

		return true;
	}
}
