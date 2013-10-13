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

package com.shigengyu.hyperion.entities;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.IndexColumn;

@Entity
@Table(name = "WORKFLOW_INSTANCE")
public class WorkflowInstanceEntity implements Serializable {

	private static final long serialVersionUID = 3680613315489463538L;

	@ManyToOne(cascade = CascadeType.PERSIST)
	@ForeignKey(name = "WORKFLOW_DEFINITION_ID")
	private WorkflowDefinitionEntity workflowDefinitionEntity;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "WORKFLOW_INSTANCE_ID")
	private int workflowInstanceId;

	@ManyToMany(targetEntity = WorkflowStateEntity.class)
	@ForeignKey(name = "WORKFLOW_STATE_ID")
	@IndexColumn(name = "WORKFLOW_STATE_ID")
	private List<WorkflowStateEntity> workflowStateEntities;

	public WorkflowDefinitionEntity getWorkflowDefinitionEntity() {
		return workflowDefinitionEntity;
	}

	public int getWorkflowInstanceId() {
		return workflowInstanceId;
	}

	public List<WorkflowStateEntity> getWorkflowStateEntities() {
		return workflowStateEntities;
	}

	public void setWorkflowDefinitionEntity(WorkflowDefinitionEntity workflowDefinitionEntity) {
		this.workflowDefinitionEntity = workflowDefinitionEntity;
	}

	public void setWorkflowInstanceId(int workflowInstanceId) {
		this.workflowInstanceId = workflowInstanceId;
	}

	public void setWorkflowStateEntities(List<WorkflowStateEntity> workflowStateEntities) {
		this.workflowStateEntities = workflowStateEntities;
	}
}
