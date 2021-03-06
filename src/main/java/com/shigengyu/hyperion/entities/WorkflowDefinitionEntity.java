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
package com.shigengyu.hyperion.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "WORKFLOW_DEFINITION")
public class WorkflowDefinitionEntity implements Serializable {

	private static final long serialVersionUID = 6736572663635946908L;

	@Column(name = "NAME", unique = true)
	private String name;

	@Id
	@Column(name = "WORKFLOW_DEFINITION_ID", length = 36)
	private String workflowDefinitionId;

	public String getName() {
		return name;
	}

	public String getWorkflowDefinitionId() {
		return workflowDefinitionId;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public void setWorkflowDefinitionId(final String workflowDefinitionId) {
		this.workflowDefinitionId = workflowDefinitionId;
	}
}
