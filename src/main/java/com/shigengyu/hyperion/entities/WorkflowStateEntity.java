package com.shigengyu.hyperion.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "WORKFLOW_STATE")
public class WorkflowStateEntity {

	@Column(name = "NAME", unique = true)
	private String name;

	@Id
	@Column(name = "WORKFLOW_STATE_ID", length = 36)
	private Integer workflowStateId;
}
