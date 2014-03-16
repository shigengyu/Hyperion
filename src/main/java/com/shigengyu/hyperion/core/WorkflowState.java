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

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import net.jcip.annotations.Immutable;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.shigengyu.common.StringMessage;
import com.shigengyu.hyperion.cache.WorkflowStateCache;
import com.shigengyu.hyperion.entities.WorkflowStateEntity;

@Immutable
public abstract class WorkflowState {

	@Service
	public static class WorkflowStateFactory {

		private static WorkflowStateFactory instance;

		@Resource
		private WorkflowStateCache workflowStateCache;

		private final <T extends WorkflowState> T byId(final String workflowStateId) {
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

	public static <T extends WorkflowState> T byId(final String workflowStateId) {
		T state = WorkflowStateFactory.instance.byId(workflowStateId);
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

	protected WorkflowState() {
		final State annotation = this.getClass().getAnnotation(State.class);
		if (annotation == null) {
			throw new WorkflowStateException("Workflow state [{}] does not have [{}] annotation specified", this
					.getClass().getName(), State.class.getName());
		}

		workflowStateId = annotation.id();
		name = StringUtils.isEmpty(annotation.name()) ? this.getClass().getSimpleName() : annotation.name();
		displayName = StringUtils.isEmpty(annotation.displayName()) ? name : annotation.displayName();
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
}
