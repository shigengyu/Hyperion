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

public class WorkflowInstance {

	private WorkflowStateSet backupWorkflowStateSet;

	private WorkflowDefinition workflowDefinition;

	private int workflowInstanceId;

	private WorkflowStateSet workflowStateSet;

	public WorkflowStateSet getBackupWorkflowStateSet() {
		return backupWorkflowStateSet;
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

	public void setBackupWorkflowStateSet(WorkflowStateSet backupWorkflowStateSet) {
		this.backupWorkflowStateSet = backupWorkflowStateSet;
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
}
