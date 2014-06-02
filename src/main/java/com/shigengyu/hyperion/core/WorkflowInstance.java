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

import javax.annotation.Resource;

import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Service;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.DataSerializable;
import com.shigengyu.hyperion.entities.WorkflowInstanceEntity;
import com.shigengyu.hyperion.entities.WorkflowStateEntity;

public class WorkflowInstance implements DataSerializable {

	@Service
	public static class WorkflowInstanceFactory {

		@Resource
		private WorkflowContextSerializer workflowContextSerializer;

		public WorkflowInstance create(WorkflowDefinition workflowDefinition) {

			WorkflowInstance workflowInstance = new WorkflowInstance(workflowDefinition);
			workflowInstance.workflowContextSerializer = workflowContextSerializer;
			return workflowInstance;
		}

		public WorkflowInstance create(WorkflowDefinition workflowDefinition, WorkflowInstanceEntity entity) {

			WorkflowInstance workflowInstance = new WorkflowInstance(workflowDefinition, entity);
			workflowInstance.workflowContextSerializer = workflowContextSerializer;
			workflowInstance.workflowContext = WorkflowContextSerializers.get().deserialize(
					workflowDefinition.getWorkflowContextType(), entity.getWorkflowContext());
			return workflowInstance;
		}
	}

	private WorkflowContextSerializer workflowContextSerializer;

	private final TransitionParameterSet parameters = TransitionParameterSet.create();

	private WorkflowDefinition workflowDefinition;

	private int workflowInstanceId;

	private WorkflowStateSet workflowStateSet;

	private WorkflowContext workflowContext;

	private WorkflowInstance() {
	}

	/**
	 * Creates a new workflow instance with the specified workflow definition
	 * 
	 * @param workflowDefinition
	 */
	private WorkflowInstance(WorkflowDefinition workflowDefinition) {
		this.workflowDefinition = workflowDefinition;
		workflowStateSet = WorkflowStateSet.from(workflowDefinition.getInitialState());
		try {
			workflowContext = workflowDefinition.getWorkflowContextType().newInstance();
		}
		catch (Exception e) {
			throw new WorkflowContextException("Failed to create new workflow context of type ["
					+ workflowDefinition.getWorkflowContextType().getName() + "]");
		}
	}

	/**
	 * Create a {@link WorkflowInstance} object from an existing workflow instance
	 * 
	 * @param workflowDefinition
	 * @param entity
	 */
	private WorkflowInstance(WorkflowDefinition workflowDefinition, WorkflowInstanceEntity entity) {
		this(workflowDefinition);

		workflowInstanceId = entity.getWorkflowInstanceId();

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

	public final <T extends WorkflowContext> T getWorkflowContext() {
		return (T) workflowDefinition.getWorkflowContextType().cast(workflowContext);
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

	@Override
	public void readData(ObjectDataInput in) throws IOException {
		workflowInstanceId = in.readInt();
		workflowDefinition = in.readObject();
		workflowStateSet = in.readObject();
		workflowContext = WorkflowContextSerializers.get().deserialize(workflowDefinition.getWorkflowContextType(),
				in.readUTF());
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
		entity.setWorkflowContext(WorkflowContextSerializers.get().serialize(workflowContext));
		return entity;
	}

	@Override
	public String toString() {
		return "<" + workflowDefinition.getName() + "> (" + workflowInstanceId + ")";
	}

	@Override
	public void writeData(ObjectDataOutput out) throws IOException {
		out.writeInt(workflowInstanceId);
		out.writeObject(workflowDefinition);
		out.writeObject(workflowStateSet);
		out.writeUTF(WorkflowContextSerializers.get().serialize(workflowContext));
	}
}
