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

package com.shigengyu.hyperion.scenarios.simple;

import com.shigengyu.hyperion.core.Transition;
import com.shigengyu.hyperion.core.Workflow;
import com.shigengyu.hyperion.core.WorkflowDefinition;
import com.shigengyu.hyperion.core.WorkflowProcess;

@Workflow(id = "bfc0c860-e52e-421e-95b6-1fbd5b9d710e", initialState = InitializedState.class)
public class SimpleWorkflow extends WorkflowDefinition {

	/**
	 * @param workflowProcess
	 */
	@Transition
	public void complete(final WorkflowProcess workflowProcess) {
	}

	/**
	 * @param workflowProcess
	 */
	@Transition
	public void start(final WorkflowProcess workflowProcess) {
	}
}
