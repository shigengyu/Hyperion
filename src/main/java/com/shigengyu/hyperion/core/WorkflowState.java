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

import java.io.IOException;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.DataSerializable;
import com.shigengyu.common.StringMessage;
import com.shigengyu.hyperion.cache.WorkflowStateCache;
import com.shigengyu.hyperion.cache.WorkflowStateCacheLoader;
import com.shigengyu.hyperion.entities.WorkflowStateEntity;

public abstract class WorkflowState implements DataSerializable {

	@Service
	public static class WorkflowStateFactory {

		private static WorkflowStateFactory instance;

		@Resource
		private WorkflowStateCache workflowStateCache;

		private final WorkflowState byId(final String workflowStateId) {
			return workflowStateCache.byId(workflowStateId);
		}

		@PostConstruct
		private void initialize() {
			instance = this;
		}

		private final <T extends WorkflowState> T of(final Class<T> workflowStateClass) {
			return workflowStateCache.get(workflowStateClass);
		}
	}

	public static WorkflowState byId(final String workflowStateId) {
		WorkflowState state = WorkflowStateFactory.instance.byId(workflowStateId);
		if (state == null) {
			throw new WorkflowStateException(StringMessage.with("Workflow state not found by ID [{}]", workflowStateId));
		}
		return state;
	}

	public static <T extends WorkflowState> WorkflowState of(final Class<T> workflowStateClass) {
		return WorkflowStateFactory.instance.of(workflowStateClass);
	}

	private final String displayName;

	private final String name;

	private final String workflowStateId;

	private final boolean intermediate;

	/**
	 * This field is set by {@link WorkflowStateCacheLoader} using reflection
	 */
	private StateOwner stateOwner;

	protected WorkflowState() {
		final State annotation = this.getClass().getAnnotation(State.class);
		if (annotation == null) {
			throw new WorkflowStateException("Workflow state [{}] does not have [{}] annotation specified", this
					.getClass().getName(), State.class.getName());
		}

		workflowStateId = annotation.id();
		name = StringUtils.isEmpty(annotation.name()) ? this.getClass().getSimpleName() : annotation.name();
		displayName = StringUtils.isEmpty(annotation.displayName()) ? name : annotation.displayName();
		intermediate = annotation.intermediate();
	}

	public boolean among(final Class<?>... workflowClasses) {
		if (workflowClasses == null || workflowClasses.length == 0) {
			return false;
		}

		for (final Class<?> clazz : workflowClasses) {
			if (this.getClass().equals(clazz)) {
				return true;
			}
		}

		return false;
	}

	public boolean belongsToWorkflow(Class<? extends WorkflowDefinition> workflowDefinitionClass) {
		return stateOwner != null && workflowDefinitionClass.isAssignableFrom(stateOwner.workflow());
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof WorkflowState)) {
			return false;
		}

		return workflowStateId.equals(((WorkflowState) obj).workflowStateId);
	}

	public String getDisplayName() {
		return displayName;
	}

	public String getName() {
		return name;
	}

	public StateOwner getStateOwner() {
		return stateOwner;
	}

	public String getWorkflowStateId() {
		return workflowStateId;
	}

	@Override
	public int hashCode() {
		return workflowStateId.hashCode();
	}

	public <T extends WorkflowState> boolean is(final Class<T> workflowClass) {
		return this.getClass().equals(workflowClass);
	}

	public final boolean isIntermediate() {
		return intermediate;
	}

	@Override
	public void readData(ObjectDataInput in) throws IOException {
	}

	public WorkflowStateEntity toEntity() {
		WorkflowStateEntity entity = new WorkflowStateEntity();
		entity.setWorkflowStateId(workflowStateId);
		entity.setName(name);
		entity.setDisplayName(displayName);
		return entity;
	}

	@Override
	public String toString() {
		return !StringUtils.isEmpty(displayName) ? displayName : name;
	}

	@Override
	public void writeData(ObjectDataOutput out) throws IOException {
	}
}
