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

import com.shigengyu.hyperion.entities.WorkflowExecutionEntity;

public class WorkflowExecution {

	private String transitionName;

	private WorkflowInstance workflowInstance;

	public WorkflowExecution() {
	}

	public WorkflowExecution(WorkflowInstance workflowInstance, String transitionName) {
		this.workflowInstance = workflowInstance;
		this.transitionName = transitionName;
	}

	public final String getTransitionName() {
		return transitionName;
	}

	public final WorkflowInstance getWorkflowInstance() {
		return workflowInstance;
	}

	public final void setTransitionName(String transitionName) {
		this.transitionName = transitionName;
	}

	public final void setWorkflowInstance(WorkflowInstance workflowInstance) {
		this.workflowInstance = workflowInstance;
	}

	public WorkflowExecutionEntity toEntity() {
		WorkflowExecutionEntity entity = new WorkflowExecutionEntity();
		entity.setTransitionName(transitionName);
		entity.setWorkflowInstanceEntity(workflowInstance.toEntity());
		entity.setWorkflowStateEntities(workflowInstance.getWorkflowStateSet().toEntityList());
		return entity;
	}
}
