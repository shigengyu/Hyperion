package com.shigengyu.hyperion.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "WORKFLOW_DEFINITION")
public class WorkflowDefinitionEntity {

	@Column(name = "NAME", unique = true)
	private String name;

	@Id
	@Column(name = "WORKFLOW_DEFINITION_ID", length = 36)
	private Integer workflowDefinitionId;
}
