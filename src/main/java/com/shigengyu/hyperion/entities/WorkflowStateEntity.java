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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.IndexColumn;

@Entity
@Table(name = "WORKFLOW_STATE")
public class WorkflowStateEntity {

	@Column(name = "DISPLAY_NAME", unique = true)
	private String displayName;

	@Column(name = "NAME", unique = true)
	private String name;

	@Id
	@Column(name = "WORKFLOW_STATE_ID", length = 36)
	@IndexColumn(name = "WORKFLOW_STATE_ID")
	private String workflowStateId;

	public String getDisplayName() {
		return displayName;
	}

	public String getName() {
		return name;
	}

	public String getWorkflowStateId() {
		return workflowStateId;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setWorkflowStateId(String workflowStateId) {
		this.workflowStateId = workflowStateId;
	}
}
