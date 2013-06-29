package com.shigengyu.hyperion.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "WORKFLOW_PROCESS")
public class WorkflowProcess {

	@Id
	@Column(name = "WORKFLOW_PROCESS_ID")
	private String workflowProcessId;

}
