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

import org.springframework.stereotype.Service;

import com.shigengyu.hyperion.core.TransitionExecutionResult;
import com.shigengyu.hyperion.core.TransitionExecutor;
import com.shigengyu.hyperion.core.WorkflowInstance;
import com.shigengyu.hyperion.core.WorkflowTransition;

@Service("workflowExecutionService")
public class WorkflowExecutionServiceImpl implements WorkflowExecutionService {

	@Override
	public TransitionExecutionResult execute(final WorkflowInstance workflowInstance,
			final WorkflowTransition transition, final TransitionExecutor executor) {

		final TransitionExecutionResult executionResult = new TransitionExecutionResult();

		return executionResult;
	}
}
