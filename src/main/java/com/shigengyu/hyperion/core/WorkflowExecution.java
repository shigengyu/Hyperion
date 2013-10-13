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
