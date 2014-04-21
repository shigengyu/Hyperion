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

import org.apache.commons.io.IOUtils;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.shigengyu.hyperion.entities.WorkflowInstanceEntity;
import com.shigengyu.hyperion.entities.WorkflowStateEntity;

public class WorkflowInstance {

	private final TransitionParameterSet parameters = TransitionParameterSet.create();

	private WorkflowDefinition workflowDefinition;

	private int workflowInstanceId;

	private WorkflowStateSet workflowStateSet;

	/**
	 * Creates a new workflow instance with the specified workflow definition
	 *
	 * @param workflowDefinition
	 */
	public WorkflowInstance(WorkflowDefinition workflowDefinition) {
		this.workflowDefinition = workflowDefinition;
		workflowStateSet = WorkflowStateSet.from(workflowDefinition.getInitialState());
	}

	/**
	 * Create a {@link WorkflowInstance} object from an existing workflow instance
	 *
	 * @param workflowDefinition
	 * @param entity
	 */
	public WorkflowInstance(WorkflowDefinition workflowDefinition, WorkflowInstanceEntity entity) {
		this(workflowDefinition);

		workflowStateSet = WorkflowStateSet.from(Lists.transform(entity.getWorkflowStateEntities(),
				new Function<WorkflowStateEntity, String>() {

			@Override
			public String apply(WorkflowStateEntity input) {
				return input.getWorkflowStateId();
			}
		}));
	}

	private WorkflowInstance(WorkflowInstance workflowInstance) {
		workflowInstanceId = workflowInstance.workflowInstanceId;
		workflowDefinition = workflowInstance.workflowDefinition;
		workflowStateSet = WorkflowStateSet.from(workflowInstance.workflowStateSet);
	}

	@Override
	public WorkflowInstance clone() {
		WorkflowInstance workflowInstance = new WorkflowInstance(this);
		return workflowInstance;
	}

	public String debugString() {
		return "Workflow Instance <" + workflowDefinition.getName() + ">" + IOUtils.LINE_SEPARATOR + "ID = "
				+ workflowInstanceId + IOUtils.LINE_SEPARATOR + "State = " + workflowStateSet;
	}

	public <T> T getParameter(final Class<T> clazz, String name) {
		return parameters.get(clazz, name);
	}

	public WorkflowDefinition getWorkflowDefinition() {
		return workflowDefinition;
	}

	public int getWorkflowInstanceId() {
		return workflowInstanceId;
	}

	public WorkflowStateSet getWorkflowStateSet() {
		return workflowStateSet;
	}

	public void restoreFrom(WorkflowInstance workflowInstance) {
		workflowInstanceId = workflowInstance.workflowInstanceId;
		workflowDefinition = workflowInstance.workflowDefinition;
		workflowStateSet = workflowInstance.workflowStateSet;
	}

	public <T> WorkflowInstance setParameter(String name, T value) {
		parameters.set(name, value);
		return this;
	}

	public void setWorkflowDefinition(WorkflowDefinition workflowDefinition) {
		this.workflowDefinition = workflowDefinition;
	}

	public void setWorkflowInstanceId(int workflowInstanceId) {
		this.workflowInstanceId = workflowInstanceId;
	}

	public void setWorkflowStateSet(WorkflowStateSet workflowStateSet) {
		this.workflowStateSet = workflowStateSet;
	}

	public WorkflowInstanceEntity toEntity() {
		WorkflowInstanceEntity entity = new WorkflowInstanceEntity();
		entity.setWorkflowDefinitionEntity(workflowDefinition.toEntity());
		entity.setWorkflowInstanceId(workflowInstanceId);
		entity.setWorkflowStateEntities(workflowStateSet.toEntityList());
		return entity;
	}

	@Override
	public String toString() {
		return "<" + workflowDefinition.getName() + "> (" + workflowInstanceId + ")";
	}
}
