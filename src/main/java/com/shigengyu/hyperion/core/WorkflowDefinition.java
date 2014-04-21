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

import net.jcip.annotations.Immutable;

import org.springframework.util.StringUtils;

import com.shigengyu.common.StringMessage;
import com.shigengyu.hyperion.entities.WorkflowDefinitionEntity;

@Immutable
public abstract class WorkflowDefinition {

	private final WorkflowState initialState;

	private final String name;

	private final Class<? extends WorkflowContext> workflowContextType;

	private final String workflowDefinitionId;

	private final Class<? extends WorkflowDefinition> workflowDefinitionType;

	protected WorkflowDefinition() {
		final Class<? extends WorkflowDefinition> clazz = this.getClass();
		final Workflow workflow = clazz.getAnnotation(Workflow.class);
		if (workflow == null) {
			final String message = StringMessage.with(
					"Workflow definition class [{}] does not have @Workflow annotation present", clazz.getName());
			throw new WorkflowDefinitionException(message);
		}

		workflowDefinitionId = workflow.id();
		workflowDefinitionType = clazz;
		name = StringUtils.isEmpty(workflow.name()) ? clazz.getSimpleName() : workflow.name();
		initialState = WorkflowState.of(workflow.initialState());
		workflowContextType = workflow.contextType() == null ? WorkflowContext.class : workflow.contextType();
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof WorkflowDefinition)) {
			return false;
		}

		WorkflowDefinition other = (WorkflowDefinition) obj;
		return workflowDefinitionId.equals(other.workflowDefinitionId);
	}

	public WorkflowState getInitialState() {
		return initialState;
	}

	public String getName() {
		return name;
	}

	public Class<? extends WorkflowContext> getWorkflowContextType() {
		return workflowContextType;
	}

	public String getWorkflowDefinitionId() {
		return workflowDefinitionId;
	}

	public Class<? extends WorkflowDefinition> getWorkflowDefinitionType() {
		return workflowDefinitionType;
	}

	@Override
	public int hashCode() {
		return workflowDefinitionId.hashCode();
	}

	public WorkflowDefinitionEntity toEntity() {
		WorkflowDefinitionEntity entity = new WorkflowDefinitionEntity();
		entity.setWorkflowDefinitionId(workflowDefinitionId);
		entity.setName(name);
		return entity;
	}

	@Override
	public String toString() {
		return name;
	}
}
